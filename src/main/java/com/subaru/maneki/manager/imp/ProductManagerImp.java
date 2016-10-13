package com.subaru.maneki.manager.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.subaru.maneki.cons.DeliveryConstant;
import com.subaru.maneki.dao.*;
import com.subaru.maneki.exception.ProductException;
import com.subaru.maneki.manager.ProductManager;
import com.subaru.maneki.manager.SkuManager;
import com.subaru.maneki.model.*;
import com.subaru.maneki.service.cart.CartService;
import com.subaru.maneki.service.trade.CountryStateService;
import com.subaru.maneki.service.user.LoginService;
import com.subaru.maneki.service.user.WishListService;
import com.subaru.maneki.vo.*;
import com.subaru.maneki.vo.ProductPageVO.Comment;
import com.subaru.maneki.vo.ProductPageVO.Description;
import com.subaru.maneki.vo.ProductPageVO.PlatformPriceDetail;

/**
 * @author zhangchaojie
 * @since 2016-08-25
 */
@Service("productManager")
public class ProductManagerImp implements ProductManager {

    @Resource
    private CateDao             cateDao;

    @Resource
    private SpuDao              spuDao;

    @Resource
    private SkuDao              skuDao;

    @Resource
    private SkuEtcDao           skuEtcDao;

    @Resource
    private ImageDao            imageDao;

    @Resource
    private SpuImageRelationDao spuImageRelationDao;

    //    @Resource
    //    private PropDao             propDao;
    //
    //    @Resource
    //    private PropNameDao         propNameDao;
    //
    //    @Resource
    //    private PropValueDao        propValueDao;
    //
    //    @Resource
    //    private SkuPropDao          skuPropDao;

    @Resource
    private PropViewDao         propViewDao;

    @Resource
    private SkuManager          skuManager;

    //    @Resource
    //    private CountryDao          countryDao;

    @Resource
    private LoginService        loginService;

    @Resource
    private CartService         cartService;

    @Resource
    private WishListService     wishListService;

    @Resource
    private CommonManagerImp    commonService;

    @Resource
    private CountryStateService countryStateService;

    private Logger              logger = LoggerFactory.getLogger(ProductManagerImp.class);

    /**
     * @param productVO
     * @return
     */
    @Override
    public ProductPageVO getProductDetail(HttpServletRequest request, ProductVO productVO)
                                                                                          throws ProductException {

        ProductPageVO productPageVO = new ProductPageVO();

        //从sku列表中取出商品共有的属性，这里默认从第一个元素取
        if (productVO.getSkuVOList().size() < 1) {
            throw new ProductException("No relation sku info, product id = "
                                       + productVO.getSpuVO().getSpuId());
        }

        SkuVO skuVO = productVO.getSkuVOList().get(0);
        double price = skuVO.getPrice();
        double platformPrice = Double.parseDouble(skuVO.getPlatformPrice());

        //解析弹窗信息
        String cDetails = skuVO.getPlatformPriceDetails();
        List<PlatformPriceDetail> platformPriceDetailList = createPlatformDetails(productPageVO,
            cDetails);
        productPageVO.setPlatformPriceDetailList(platformPriceDetailList);

        //解析comment的内容
        String comment = productVO.getSpuVO().getComment();
        if (comment != "") {
            List<Comment> commentList = createComment(productPageVO, comment);
            productPageVO.setCommentList(commentList);
        } else {
            productPageVO.setCommentList(null);
        }

        productPageVO.setIsAvailable(!skuVO.getIsSaleOk());
        productPageVO.setPrice(price);

        //spu
        productPageVO.setName(productVO.getSpuVO().getName());
        productPageVO.setImageList(productVO.getSpuVO().getImageList());
        productPageVO.setIsPublished(productVO.getSpuVO().getIsPublished());
        productPageVO.setProductId(productVO.getSpuVO().getSpuId());
        productPageVO.setSoldNum(productVO.getSpuVO().getSold());
        productPageVO.setScore(productVO.getSpuVO().getScore());
        productPageVO.setProductNo(productVO.getSpuVO().getProductNo());

        //添加该商品所属category相关的信息
        List<Integer> categoryIdList = productVO.getCateList();
        productPageVO.setCategoryIdList(categoryIdList);

        //解析sku和spu相关的属性信息
        String skuPropJson = productVO.getSpuVO().getSkuPropJson();
        String spuPropJson = productVO.getSpuVO().getSpuPropJson();
        productPageVO.setSkuPropJson(skuPropJson);
        productPageVO.setSpuDescriptionList(createSpuDescription(productPageVO, spuPropJson));
        productPageVO.setSkuMap(createSkuList(productPageVO, skuPropJson));

        //枚举所有sku的值
        Map<String, List<String>> skuValueMap = createSkuValue(productVO.getSpuVO().getSpuId());
        productPageVO.setSkuValueMap(skuValueMap);
        productPageVO.setSkuValueJson(JSON.toJSONString(skuValueMap));

        //计算GA的值
        Map<String, String> gaMap = new HashMap<>();
        gaMap.put("product_name", productVO.getSpuVO().getName());
        //!!!品类的名字需要重新计算
        gaMap.put("category_name_str", productVO.getSpuVO().getCateName());
        gaMap.put("product_no", productVO.getSpuVO().getProductNo());
        gaMap.put("price", Double.toString(skuVO.getPrice()));
        productPageVO.setGaData(JSON.toJSONString(gaMap));

        //获得购物车里的商品数量
        if (loginService.isLogin(request) == false) {
            productPageVO.setCartNum(0);
        } else {
            User currentUser = loginService.getCurrentUser(request);
            int cartNum = cartService.countUserCart(currentUser.getId());
            productPageVO.setCartNum(cartNum);
        }

        //其他通过计算得来的信息
        //下面计算rateNum有点扯，先套python的思路
        int commentLength = productVO.getSpuVO().getComment().length();
        int rateNum = commentLength + (productVO.getSpuVO().getSold() - commentLength) / 2 + 3;
        if (rateNum > productVO.getSpuVO().getSold()) {
            rateNum = productVO.getSpuVO().getSold();
        }
        productPageVO.setRatingNum(rateNum);

        //打折
        int off = commonService.computeSaving(platformPrice, price);
        productPageVO.setOff(off);

        //判断是否在心愿单，需要依赖于心愿单这个表
        boolean isInWishlist = false;
        if (loginService.isLogin(request) == true) {
            List<Wishlist> wishlists = wishListService.getBySpuId(productVO.getSpuVO().getSpuId());
            if (wishlists.isEmpty() == false) {
                isInWishlist = true;
            }
        }
        productPageVO.setIsInWishlist(isInWishlist);

        //获取countryAbbr
        String countryAbbr = commonService.getCountryCodeFromCookie(request);
        //获得shipping time
        String shippingTime = this.getShippingTime(countryAbbr);
        productPageVO.setShippingTime(shippingTime);

        //获取free shipping fee和shipping fee
        String countryCode = "US";
        if (hasShippingInfoContainCountry(countryAbbr)) {
            countryCode = countryAbbr;
        }

        Country country = countryStateService.getCountryByAbbr(countryCode);

        double freeShippingPrice = country.getDollarThreshold();
        freeShippingPrice = commonService.calculateCountryPrice(freeShippingPrice,
            country.getRate());
        productPageVO.setFreeShippingPrice((int) Math.ceil(freeShippingPrice));

        double shippingFee = country.getDeliveryFee();
        productPageVO.setShippingFee(shippingFee);

        productPageVO.setCurrency(country.getCurrencyUnit());

        //最后进行货币单位转换
        productPageVO.transferPrice(country.getRate());

        return productPageVO;
    }

    //    /**
    //     * 给定一个spuId，枚举该id所有sku的值
    //     * @param spuId
    //     * @return
    //     */
    //    private Map<String, List<String>> createSkuValue(int spuId) {
    //        Map<String, List<String>> skuValueMap = new HashMap<>();
    //
    //        List<Sku> skuList = skuDao.selectBySpuId(spuId);
    //        for (Sku sku : skuList) {
    //            skuValueMap.put(Integer.toString(sku.getId()), new ArrayList<String>());
    //
    //            List<SkuProp> skuPropList = skuPropDao.selectBySkuId(sku.getId());
    //            for (SkuProp skuProp : skuPropList) {
    //                Prop prop = propDao.select(skuProp.getPropId());
    //                String propName = propNameDao.select(prop.getNameId()).getName();
    //                String propValue = propValueDao.select(prop.getValueId()).getValue();
    //
    //                skuValueMap.get(Integer.toString(sku.getId())).add(propValue);
    //            }
    //        }
    //
    //        return skuValueMap;
    //    }

    /**
     * 给定一个spuId，枚举该id所有sku的值
     * @param spuId
     * @return
     */
    private Map<String, List<String>> createSkuValue(int spuId) {
        Map<String, List<String>> skuValueMap = new HashMap<>();

        List<Sku> skuList = skuDao.selectBySpuId(spuId);
        for (Sku sku : skuList) {
            skuValueMap.put(Integer.toString(sku.getId()), new ArrayList<String>());

            //			List<SkuProp> skuPropList = skuPropDao.selectBySkuId(sku.getId());
            //			for (SkuProp skuProp : skuPropList) {
            //				Prop prop = propDao.select(skuProp.getPropId());
            //				String propName = propNameDao.select(prop.getNameId()).getName();
            //				String propValue = propValueDao.select(prop.getValueId()).getValue();
            //
            //				skuValueMap.get(Integer.toString(sku.getId())).add(propValue);
            //			}
            if (StringUtils.isNotBlank(sku.getPropColor())) {
                skuValueMap.get(Integer.toString(sku.getId())).add(sku.getPropColor());
            }
            if (StringUtils.isNotBlank(sku.getPropSize())) {
                skuValueMap.get(Integer.toString(sku.getId())).add(sku.getPropSize());
            }
            if (StringUtils.isNotBlank(sku.getPropStyle())) {
                skuValueMap.get(Integer.toString(sku.getId())).add(sku.getPropStyle());
            }
            if (StringUtils.isNotBlank(sku.getPropMetalColor())) {
                skuValueMap.get(Integer.toString(sku.getId())).add(sku.getPropMetalColor());
            }
            if (StringUtils.isNotBlank(sku.getPropColor())) {
                skuValueMap.get(Integer.toString(sku.getId())).add(sku.getPropCColor());
            }
        }

        return skuValueMap;
    }

    private Cate getCategoryByName(String name) {
        return cateDao.selectByName(name);
    }

    private Map<String, List<String>> createSkuList(ProductPageVO productPageVO, String skuPropJson) {
        Map<String, List<String>> skuMap = JSON.parseObject(skuPropJson,
            new TypeReference<Map<String, List<String>>>() {
            });
        return skuMap;
    }

    private List<Description> createSpuDescription(ProductPageVO productPageVO, String spuPropJson) {
        List<Description> descriptionList = new ArrayList<>();

        JSONArray jsonArray = JSON.parseArray(spuPropJson);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String key = null, value = null;
            if (jsonObject.containsKey("key")) {
                key = jsonObject.getString("key");
            }
            if (jsonObject.containsKey("value")) {
                value = jsonObject.getString("value");
            }
            Description description = productPageVO.new Description(key, value);

            descriptionList.add(description);
        }

        return descriptionList;
    }

    private List<Comment> createComment(ProductPageVO productPageVO, String comment) {
        List<Comment> commentList = new ArrayList<Comment>();

        JSONArray jsonArray = JSON.parseArray(comment);
        if (jsonArray == null) {
            return null;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String buyer = null;
            double rating = 0.0;

            List<Map<String, String>> imageMapList = new ArrayList<>();
            List<String> bigImageList = new ArrayList<>();

            String feedback = null;

            String time = null;

            if (jsonObject.containsKey("buyer")) {
                buyer = jsonObject.getString("buyer");
            }
            if (jsonObject.containsKey("rating")) {
                rating = jsonObject.getDoubleValue("rating");
            }
            if (jsonObject.containsKey("feedback")) {
                feedback = jsonObject.getString("feedback");
            }
            if (jsonObject.containsKey("time")) {
                time = jsonObject.getString("time");
            }
            if (jsonObject.containsKey("images")) {
                String imageStr = jsonObject.getString("images");

                JSONArray imageJsonArray = JSON.parseArray(imageStr);

                //!!!这里固定j<2，后面需要在配置文件中指定要展示出图片的数量
                for (int j = 0; j < imageJsonArray.size() && j < 2; j++) {
                    JSONObject imageJsonObj = imageJsonArray.getJSONObject(j);
                    Map<String, String> imageMap = new HashMap<>();
                    if (imageJsonObj.containsKey("s")) {
                        imageMap.put("s", imageJsonObj.getString("s"));
                    }
                    if (imageJsonObj.containsKey("b")) {
                        imageMap.put("b", imageJsonObj.getString("b"));
                        bigImageList.add(imageJsonObj.getString("b"));
                    }
                    imageMapList.add(imageMap);
                }
            }

            Comment c = productPageVO.new Comment(buyer, rating, imageMapList, bigImageList,
                feedback, time);
            commentList.add(c);
        }
        return commentList;
    }

    private List<PlatformPriceDetail> createPlatformDetails(ProductPageVO productPageVO,
                                                            String platformPriceDetails) {
        if (platformPriceDetails == null) {
            return null;
        }
        List<PlatformPriceDetail> platformPriceDetailList = new ArrayList<PlatformPriceDetail>();
        JSONArray jsonArray = JSON.parseArray(platformPriceDetails);

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            double basePrice = 0.0, shippingFee = 0.0, totalPrice = 0.0;
            String url = null;
            String platform = null;
            if (jsonObject.containsKey("c_shipping")) {
                shippingFee = jsonObject.getDoubleValue("c_shipping");
            }
            if (jsonObject.containsKey("c_price_sub")) {
                basePrice = jsonObject.getDoubleValue("c_price_sub");
            }
            if (jsonObject.containsKey("c_price")) {
                totalPrice = jsonObject.getDoubleValue("c_price");
            }
            if (jsonObject.containsKey("c_url")) {
                url = jsonObject.getString("c_url");
            }
            if (jsonObject.containsKey("c_platform")) {
                platform = jsonObject.getString("c_platform");
            }

            PlatformPriceDetail platformPriceDetail = productPageVO.new PlatformPriceDetail(
                basePrice, shippingFee, totalPrice, url, platform);

            platformPriceDetailList.add(platformPriceDetail);
        }
        return platformPriceDetailList;
    }

    private boolean hasShippingInfoContainCountry(String countryCode) {
        return DeliveryConstant.SHIPPING_CONSTANT.containsKey(countryCode);
    }

    /**
     * 根据国家计算运费的时间，目前主要是从const包中获取
     * @param countryAbbr
     * @return
     */
    private String getShippingTime(String countryAbbr) {
        String countryCode = countryAbbr.toUpperCase();
        if (hasShippingInfoContainCountry(countryCode)) {
            return DeliveryConstant.SHIPPING_CONSTANT.get(countryCode).getShippingTime();
        } else {
            return DeliveryConstant.SHIPPING_CONSTANT.get("US").getShippingTime();
        }
    }

    @Override
    public ProductVO getProductVO(int spuId) throws ProductException {

        ProductVO productVO = new ProductVO();

        //1.SpuVO
        SpuVO spuVO = new SpuVO();

        if (spuId <= 0) {
            throw new ProductException("Illegal spuId " + spuId
                                       + ", leaving method getProductVO with null !!");
        }

        if (spuDao == null) {
            throw new ProductException("SPU Dao import error!!");
        }

        //返回带有品类id列表的商品信息
        Spu spu = spuDao.selectForCateList(spuId);
        if (spu == null) {
            throw new ProductException("Cannot get spu by the given spuId " + spuId
                                       + ", leaving method getProductVO with null !!");
        }
        spuVO.setSpuId(spuId);
        spuVO.setName(spu.getName());
        spuVO.setIsPublished(spu.getIsPublished());
        spuVO.setProductNo(spu.getProductNo());
        spuVO.setSearchKeywords(spu.getSearchKeywords());
        spuVO.setComment(spu.getComment());
        spuVO.setScore(spu.getScore());
        spuVO.setClusterId(spu.getClusterId());
        spuVO.setOldId(spu.getOldId());
        spuVO.setSold(spu.getSold());

        int cateId = spu.getCateId();
        if (cateId <= 0) {
            throw new ProductException("Illegal cateId, leaving method getProductVO with null !!");
        }
        Cate cate = cateDao.select(cateId);
        if (cate == null) {
            throw new ProductException("Canoot get cate by the given spuId " + spuId
                                       + ", leaving method getProductVO with null !!");
        }
        spuVO.setCateName(cate.getName());

        PropView propView = propViewDao.select(spuId);
        if (propView != null) {

            //            throw new ProductException("Canoot get propView by the given spuId " + spuId
            //                                       + ", leaving method getProductVO with null !!");
            spuVO.setSpuPropJson(propView.getSpuPropJson());
            /**
             * zhangchaojie
             * 重写skuPropJson逻辑，直接从sku里面获取
             */
            //            spuVO.setSkuPropJson(propView.getSkuPropJson());
            spuVO.setSkuPropJson(skuManager.getSubSkuPropJson(spuId));
        }

        List<Image> spuImageList = new ArrayList<>();
        List<SpuImageRelation> spuImageRelationList = spuImageRelationDao.selectBySpuId(spuId);
        if (spuImageRelationList == null) {
            throw new ProductException("Canoot get spuImageRelationList by the given spuId "
                                       + spuId + ", leaving method getProductVO with null !!");
        }
        for (SpuImageRelation spuImageRelation : spuImageRelationList) {
            int imageId = spuImageRelation.getImageId();
            if (imageId <= 0) {
                continue;
            }
            Image image = imageDao.select(imageId);
            spuImageList.add(image);
        }
        spuVO.setImageList(spuImageList);

        productVO.setSpuVO(spuVO);
        //转换并添加cate id列表
        List<SpuCateRelation> spuCateRelationList = spu.getCateList();
        List<Integer> cateList = new ArrayList<>(spuCateRelationList.size());
        for (SpuCateRelation spuCateRelation : spuCateRelationList) {
            cateList.add(spuCateRelation.getCateId());
        }
        productVO.setCateList(cateList);

        //2.SkuVOList
        List<SkuVO> skuVOList = new ArrayList<>();
        List<Sku> skuList = skuDao.selectBySpuId(spuId);
        if (skuList == null || skuList.size() <= 0) {
            throw new ProductException("Canoot get skuList by the given spuId " + spuId
                                       + ", leaving method getProductVO with null !!");
        }
        for (Sku sku : skuList) {
            SkuVO skuVO = new SkuVO();
            skuVO.setSpuId(sku.getSpuId());
            skuVO.setPrice(sku.getPrice());
            skuVO.setWeight(sku.getWeight());
            skuVO.setCurrencyUnit(sku.getCurrencyUnit());
            skuVO.setInventory(sku.getInventory());
            skuVO.setIsSaleOk(sku.getIsSaleOk());
            skuVO.setOldId(sku.getOldId());
            //setSkuEtc
            SkuEtc skuEtc = skuEtcDao.selectBySkuId(sku.getId());
            if (skuEtc == null) {
                throw new ProductException("Canoot get skuEtc by the given skuId " + sku.getId()
                                           + ", leaving method getProductVO with null !!");
            }
            skuVO.setPlatformPrice(skuEtc.getPlatformPrice());
            skuVO.setPlatformUrl(skuEtc.getPlatformUrl());
            skuVO.setPlatformPriceDetails(skuEtc.getPlatformPriceDetails());
            skuVO.setPlatformName(skuEtc.getPlatformName());
            skuVO.setUrlOf1688(skuEtc.getUrlOf1688());
            /**
             * zhangchaojie
             * 临时修改获取SKU属性的方式，修改为直接从SKU表中获取
             */
            // former
            //            //setSkuProp
            //            List<PropVO> propVOList = new ArrayList<>();
            //            List<SkuProp> skuPropList = skuPropDao.selectBySkuId(sku.getId());
            //            if (skuPropList == null || skuPropList.size() <= 0) {
            ////                throw new ProductException("Canoot get skuPropList by the given skuId "
            ////                                           + sku.getId()
            ////                                           + ", leaving method getProductVO with null !!");
            //            }
            //            for (SkuProp skuProp : skuPropList) {
            //                int propId = skuProp.getPropId();
            //                if (propId <= 0) {
            //                    continue;
            //                }
            //                Prop prop = propDao.select(propId);
            //                if (prop == null) {
            //                    continue;
            //                }
            //                if (prop.getNameId() <= 0 || prop.getValueId() <= 0) {
            //                    continue;
            //                }
            //                PropName propName = propNameDao.select(prop.getNameId());
            //                PropValue propValue = propValueDao.select(prop.getValueId());
            //                if (propName == null || propValue == null) {
            //                    continue;
            //                }
            //                PropVO propVO = new PropVO();
            //                propVO.setPropName(propName.getName());
            //                propVO.setPropValue(propValue.getValue());
            //                propVOList.add(propVO);
            //            }
            //            skuVO.setPropList(propVOList);

            //new
            //setSkuProp
            List<PropVO> skuPropVOList = new ArrayList<>();
            if (StringUtils.isNotBlank(sku.getPropColor())) {
                skuPropVOList.add(new PropVO("Color", sku.getPropColor()));
            }
            if (StringUtils.isNotBlank(sku.getPropSize())) {
                skuPropVOList.add(new PropVO("Size", sku.getPropSize()));
            }
            if (StringUtils.isNotBlank(sku.getPropStyle())) {
                skuPropVOList.add(new PropVO("Style", sku.getPropStyle()));
            }
            if (StringUtils.isNotBlank(sku.getPropMetalColor())) {
                skuPropVOList.add(new PropVO("Metal Color", sku.getPropMetalColor()));
            }
            if (StringUtils.isNotBlank(sku.getPropColor())) {
                skuPropVOList.add(new PropVO("cCOLOR", sku.getPropCColor()));
            }
            skuVO.setPropList(skuPropVOList);

            skuVOList.add(skuVO);
        }
        productVO.setSkuVOList(skuVOList);

        return productVO;
    }

}
