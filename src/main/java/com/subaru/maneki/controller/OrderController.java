package com.subaru.maneki.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.subaru.maneki.dao.CountryDao;
import com.subaru.maneki.exception.TradeException;
import com.subaru.maneki.manager.CommonManager;
import com.subaru.maneki.manager.SkuManager;
import com.subaru.maneki.model.*;
import com.subaru.maneki.service.product.ImageService;
import com.subaru.maneki.service.product.SkuService;
import com.subaru.maneki.service.product.SpuService;
import com.subaru.maneki.service.trade.OrderService;
import com.subaru.maneki.service.trade.TradeProductService;
import com.subaru.maneki.service.user.LoginService;
import com.subaru.maneki.service.user.WishListService;
import com.subaru.maneki.vo.OrderVO;
import com.subaru.maneki.vo.PropVO;
import com.subaru.maneki.vo.TradeProductVO;
import com.subaru.maneki.vo.WishListVO;
import com.subaru.common.vo.JsonVO;

/**
 * @author zhangchaojie
 * @since 2016-08-08
 */
@Controller
public class OrderController {

    @Resource
    private OrderService        orderService;

    @Resource
    private LoginService        loginService;

    @Resource
    private TradeProductService tradeProductService;

    @Resource
    private SkuManager          skuManager;

    @Resource
    private SkuService          skuService;

    @Resource
    private CommonManager       commonService;

    @Resource
    private CountryDao          countryDao;

    @Resource
    private SpuService          spuService;

    @Resource
    private ImageService        imageService;

    @Resource
    private WishListService     wishListService;

    @RequestMapping(value = "/order/get_user_orders", method = RequestMethod.GET)
    public String userOrdersPage(HttpServletRequest request,
                                 Model model,
                                 @RequestParam(required = false, defaultValue = "unpaied") String orderType) {

        if (!loginService.isLogin(request)) {
            String redirectUrl = "/order/get_user_orders";
            return "redirect:/auth/login?redirectUrl=" + redirectUrl;
        }

        User currentUser = loginService.getCurrentUser(request);

        int totalOrderNum = orderService.count(currentUser.getId());

        int paiedOrderNum = orderService.countPaied(currentUser.getId());

        model.addAttribute("paiedOrderNum", paiedOrderNum);
        model.addAttribute("unpaiedOrderNum", totalOrderNum - paiedOrderNum);

        int page = 0, pageSize = 20;
        List<Order> orderList = orderService.get(currentUser.getId(), page, pageSize);

        //根据请求的状态对order list进行过滤
        int tradeStatus = 1;//unpaid
        if (orderType.equals("paied")) {
            tradeStatus = 2;
        }
        Order order = null;
        Iterator<Order> ordereListIterator = orderList.iterator();
        while (ordereListIterator.hasNext()) {
            order = ordereListIterator.next();
            if (1 == tradeStatus) {
                if (order.getGmtPaied() != null && order.getPayType() != 0) {
                    ordereListIterator.remove();
                }
            } else if (2 == tradeStatus) {
                if ((order.getGmtPaied() == null && order.getPayType() == 0)) {
                    ordereListIterator.remove();
                }
            }
        }

        List<OrderVO> orderVOList = getOrderVOList(orderList);

        //货币单位转换
        //        String abbr = commonService.getCountryCodeFromCookie(request);
        //        transferPrice(abbr, orderList);

        model.addAttribute("orders", orderVOList);
        model.addAttribute("orderType", orderType);

        return "page/user_center/order_list";
    }

    //进入某个订单的物流信息页面
    @RequestMapping(value = "/order/tracking", method = RequestMethod.GET)
    public String orderDeliveryTracking(Model model, HttpServletRequest request,
                                        @RequestParam(required = true) long orderNumber) {
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/order/delete", method = RequestMethod.GET)
    public String orderDelete(Model model, HttpServletRequest request,
                              @RequestParam(required = true) long orderNumber) {
        JsonVO json = new JsonVO();
        if (orderNumber <= 0) {
            json.setMsg("Illegal orderNumber !!");
            json.setIsSuccess(0);
        }
        try {
            orderService.delete(orderNumber);
        } catch (TradeException e) {
            json.setMsg(e.getMessage());
            json.setIsSuccess(0);
        }
        json.setIsSuccess(1);
        return json.toString();
    }

    private List<OrderVO> getOrderVOList(List<Order> orderList) {

        if (orderList == null || orderList.size() == 0) {
            return null;
        }

        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVO orderVO = new OrderVO();
            orderVO.setOrderNumber(order.getOrderNumber());
            orderVO.setTradeStatus(order.getTradeStatus());
            orderVO.setTotalPrice(order.getAmount());
            orderVO.setCurrency(order.getCurrency().toUpperCase());

            //!!!设置是否有物流的信息，这需要根据物流信息表来判断
            orderVO.setIsShipped(false);

            //setTradeProductVO
            long orderNumber = order.getOrderNumber();
            if (orderNumber <= 0) {
                continue;
            }
            List<TradeProduct> tradeProductList = tradeProductService.getByOrderNumber(orderNumber);
            List<TradeProductVO> tradeProductVOList = getTradeProductVOList(tradeProductList);
            orderVO.setTradeProductList(tradeProductVOList);
            orderVOList.add(orderVO);
        }

        return orderVOList;

    }

    private List<TradeProductVO> getTradeProductVOList(List<TradeProduct> tradeProductList) {
        if (tradeProductList == null || tradeProductList.size() == 0) {
            return null;
        }
        List<TradeProductVO> tradeProductVOList = new ArrayList<>();
        for (TradeProduct tradeProduct : tradeProductList) {
            TradeProductVO tradeProductVO = new TradeProductVO();
            tradeProductVO.setSkuId(tradeProduct.getSkuId());
            Sku sku = skuService.get(tradeProduct.getSkuId());
            if (sku == null) {
                continue;
            }
            tradeProductVO.setSpuId(sku.getSpuId());
            tradeProductVO.setProductName(tradeProduct.getProductName());
            tradeProductVO.setProductOriginalPrice(tradeProduct.getOriginalPrice());
            tradeProductVO.setProductOrderPrice(tradeProduct.getBuyedPrice());
            tradeProductVO.setProductImage(tradeProduct.getImageUrl());
            tradeProductVO.setProductQuantity(tradeProduct.getQuantity());
            int skuId = tradeProduct.getSkuId();
            if (skuId <= 0) {
                continue;
            }
            List<PropVO> skuPropVOList = skuManager.getSkuPropVOList(skuId);
            tradeProductVO.setProductProps(skuPropVOList);
            tradeProductVOList.add(tradeProductVO);
        }
        return tradeProductVOList;
    }

    @ResponseBody
    @RequestMapping(value = "/order/add_user_wishlist", method = RequestMethod.GET)
    public String addUserWishList(Model model, HttpServletRequest request, @RequestParam int spuId)
                                                                                                   throws TradeException {

        JsonVO json = new JsonVO();

        if (!loginService.isLogin(request)) {
            json.setIsRedirect(1);
            json.setMsg("/auth/login");
            return json.toString();
        }

        if (spuId <= 0) {
            throw new TradeException("Add user wishList error, illegal spuId :" + spuId);
        }

        User currentUser = loginService.getCurrentUser(request);

        Wishlist wishlist = new Wishlist(currentUser.getId(), spuId);
        try {
            wishListService.add(wishlist);
        } catch (Exception e) {
            json.setIsSuccess(0);
            return json.toString();
        }

        json.setIsSuccess(1);
        return json.toString();

    }

    @ResponseBody
    @RequestMapping(value = "/order/delete_user_wishlist", method = RequestMethod.GET)
    public String deleteUserWishList(Model model, HttpServletRequest request,
                                     @RequestParam int wishlistId) throws TradeException {

        JsonVO json = new JsonVO();

        if (!loginService.isLogin(request)) {
            String redirectUrl = "/order/get_user_wishlist";
            json.setIsRedirect(1);
            json.setMsg("/auth/login?redirectUrl=" + redirectUrl);
            return json.toString();
        }

        if (wishlistId <= 0) {
            throw new TradeException("Add user wishList error, illegal id :" + wishlistId);
        }

        try {
            wishListService.delete(wishlistId);
        } catch (Exception e) {
            json.setIsSuccess(0);
            return json.toString();
        }

        json.setIsSuccess(1);
        return json.toString();

    }

    @RequestMapping(value = "/order/get_user_wishlist", method = RequestMethod.GET)
    public String userWishListPage(Model model, HttpServletRequest request) {

        if (!loginService.isLogin(request)) {
            String redirectUrl = "/order/get_user_wishlist";
            return "redirect:/auth/login?redirectUrl=" + redirectUrl;
        }

        User currentUser = loginService.getCurrentUser(request);

        List<Wishlist> wishlists = wishListService.getByUserId(currentUser.getId());

        List<WishListVO> wishListVOs = getWishListVOs(wishlists);

        model.addAttribute("detailedUser", currentUser);
        model.addAttribute("wishLists", wishListVOs);

        String abbr = commonService.getCountryCodeFromCookie(request);
        Country country = countryDao.selectByAbbr(abbr);
        model.addAttribute("currency", country.getCurrencyUnit());
        model.addAttribute("exchangeRate", country.getRate());

        return "page/wishlist/wishlist";
    }

    private List<WishListVO> getWishListVOs(List<Wishlist> wishlists) {

        if (wishlists == null || wishlists.size() == 0) {
            return null;
        }

        List<WishListVO> wishListVOs = new ArrayList<>();
        for (Wishlist wishlist : wishlists) {
            int spuId = wishlist.getSpuId();
            Spu spu = spuService.get(spuId);
            if (spu == null) {
                continue;
            }
            WishListVO wishListVO = new WishListVO();
            wishListVO.setId(wishlist.getId());
            wishListVO.setSpuId(spuId);
            wishListVO.setProductName(spu.getName());
            List<Image> spuImageList = imageService.getSpuImage(spuId);
            for (Image image : spuImageList) {
                if (image.getIsMain()) {
                    wishListVO.setProductImage(image.getUrl());
                }
            }
            List<Sku> skuList = skuService.getBySpuId(spuId);
            if (skuList == null || skuList.size() == 0) {
                continue;
            }
            Sku sampleSku = skuList.get(0);
            SkuEtc sampleSkuEtc = skuService.getSkuEtc(sampleSku.getId());
            wishListVO.setProductPrice(sampleSku.getPrice() + "");
            if (sampleSkuEtc != null) {
                wishListVO.setProductPlatformPrice(sampleSkuEtc.getPlatformPrice());
            }
            wishListVOs.add(wishListVO);
        }

        return wishListVOs;
    }
}
