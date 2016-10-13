package com.subaru.maneki.manager.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.subaru.maneki.config.HomeDisplayConfig;
import com.subaru.maneki.cons.CategoryDisplayConstant;
import com.subaru.maneki.dao.CateDao;
import com.subaru.maneki.dao.CountryDao;
import com.subaru.maneki.dao.SpuCateRelationDao;
import com.subaru.maneki.dao.SpuDao;
import com.subaru.maneki.manager.CommonManager;
import com.subaru.maneki.manager.ProductListManager;
import com.subaru.maneki.model.Cate;
import com.subaru.maneki.model.Image;
import com.subaru.maneki.model.Sku;
import com.subaru.maneki.model.SkuEtc;
import com.subaru.maneki.model.Spu;
import com.subaru.maneki.service.product.ImageService;
import com.subaru.maneki.service.product.SkuService;
import com.subaru.maneki.service.product.SpuService;
import com.subaru.maneki.vo.ProductCategorySetVO;
import com.subaru.maneki.vo.ProductListVO;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@Service("productListService")
public class ProductListManagerImp implements ProductListManager{
	
	@Resource
	private SkuService skuService;
	
	@Resource
	private SpuService spuService;
	
	@Resource
	private ImageService imageService;
	
	@Resource
	private CommonManager	commonService;
	
	@Resource
	private CateDao		cateDao;
	
	@Resource
	private SpuDao		spuDao;
	
	@Resource
	private CountryDao  countryDao;
	
	@Resource
	private SpuCateRelationDao	spuCateRelationDao;
	
	private List<Integer> getCateIdWithSameParent(int parentCateId){
		//找到所有父类为categoryId的品类
		List<Integer> categoryIdList = new ArrayList<>();
		if (parentCateId != 0){
			List<Cate> cateList = cateDao.selectByParentId(parentCateId);
			for(Cate cate : cateList){
				categoryIdList.add(cate.getId());
			}
			//把自身的id也添加进去
			categoryIdList.add(parentCateId);
		}
		
		return categoryIdList;
	}
		
	public String idList2Str(List<Integer> idList){
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < idList.size(); i++){
			buf.append(idList.get(i)).append(",");
		}
		
		if (buf.length() > 0){
			return buf.substring(0, buf.length()-1).toString();
		}else{
			return buf.toString();
		}
	}
	
	public List<ProductListVO> getProductList(int categoryId, int limit, int offset, 
			boolean isNewUser, boolean isShippedToMe, List<Integer> productSetIds, boolean isShuffle){
		
		List<Spu> spuList = null;
		int realLimit = limit;
		if (isShuffle){
			realLimit = 100;
		}
				
		if(categoryId != 0){
			List<Integer> cateIdList = this.getCateIdWithSameParent(categoryId);
			String cateIdStr = this.idList2Str(cateIdList);

			try{
				spuList = spuDao.selectByCateIdListAndIsPushlished(cateIdStr, offset, realLimit, true);
				if (spuList == null){
					return null;
				}
			}catch(Exception e){
				System.out.println(e);
				return null;
			}
		}else{
			try{
				spuList = spuDao.selectByLimitAndIsPublished(offset, realLimit, true);
			}catch(Exception e){
				return null;
			}
		}
		
		//删除id等于spuId的商品
		Iterator<Spu> spuIterator = spuList.iterator();
		while (spuIterator.hasNext()){
			Spu spu = spuIterator.next();
			if (productSetIds != null){
				for (int j = 0; j < productSetIds.size(); j++){
					if (spu.getId() == productSetIds.get(j)){
						spuIterator.remove();
						break;
					}
				}
			}
		}
		
		//排序，根据overall_score_new和id的值来排序
		if (isNewUser){
			Collections.sort(spuList, new SpuOverallScoreNewComparator());
		}else{
			Collections.sort(spuList, new SpuOverallScoreComparator());
		}
		
		if (isShuffle){
			Collections.shuffle(spuList);
			return composeProductListVO(spuList.subList(0, limit));
		}else{
			return composeProductListVO(spuList);
		}
	}
	
	public void computeSaving(ProductListVO productListVO){
		if (productListVO == null){
			return;
		}
		
		int saving = commonService.computeSaving(productListVO.getPlatformPrice(), productListVO.getPrice());
		productListVO.setSaving(saving);
	}
	
	public List<ProductCategorySetVO> getProductSet(HttpServletRequest request, boolean isNewUser, boolean isShippedToMe){
				
		List<ProductCategorySetVO> productCategorySetVOList = new ArrayList<>();
		String selectedCategory = HomeDisplayConfig.CATAGORY_SELECTED;
		if (selectedCategory == null){
			return null;
		}
		
		int parentId = 0;
		for(String cateStr : selectedCategory.split(",")){
			String cateIdStr = CategoryDisplayConstant.CATEGORY_DISPLAY_CONSTANT.get(cateStr);
			
			if (cateIdStr == null){
				continue;
			}
			
			String[] cateIdList = cateIdStr.split(",");
			if (cateIdList.length <= 0){
				continue;
			}
			
			int categoryId = Integer.parseInt(cateIdList[0]);
			
			List<ProductListVO> productListVOList = getProductList(categoryId, 4, 0, isNewUser, isShippedToMe, null, false);
			
			if (cateIdList.length > 1){
				parentId = Integer.parseInt(cateIdList[1]);
			}else{
				parentId = categoryId;
			}
			
			ProductCategorySetVO productCategorySetVO = new ProductCategorySetVO();
			productCategorySetVO.setCategoryId(categoryId);
			productCategorySetVO.setCategoryName(cateStr);
			productCategorySetVO.setParentId(parentId);
			productCategorySetVO.setProductListVO(productListVOList);
			
			productCategorySetVOList.add(productCategorySetVO);
		}
		
		return productCategorySetVOList;
	}
	
	
	public List<ProductListVO> getSingleProductSet(int categoryId, boolean isShuffle){
		return getProductList(categoryId, 4, 0, false, true, null, isShuffle);
	}
	
	public List<ProductListVO> getRelatedProducts(int categoryId, boolean canShippedToMe, int spuId){
		
		return getProductList(categoryId, 0, HomeDisplayConfig.PRODUCT_DETAIL_DEFAULT_LIMIT, false, canShippedToMe, null, true);
	}
	
	private List<ProductListVO> composeProductListVO(List<Spu> spuList){
		List<ProductListVO> productListVOList = new ArrayList<>();
		for (Spu spu : spuList){
			ProductListVO productListVO = new ProductListVO();
			productListVO.setProductNo(spu.getProductNo());
			productListVO.setSpuId(spu.getId());
			
			//sku
			List<Sku> skuList = skuService.getBySpuId(spu.getId());
			Sku sku = skuList.get(0);
			
			productListVO.setPrice(sku.getPrice());
			
			SkuEtc skuEtc = skuService.getSkuEtc(sku.getId());
			
			if(skuEtc != null){
				productListVO.setPlatformName(skuEtc.getPlatformName());
				productListVO.setPlatformPrice(Double.parseDouble(skuEtc.getPlatformPrice()));
			}
			
			//image
			List<Image> imageList = imageService.getSpuImage(spu.getId());
			for(Image image : imageList){
				if (image.getIsMain()){
					productListVO.setImageUrl(image.getUrl());
				}
			}
			
			productListVOList.add(productListVO);
		}
		
		return productListVOList;
	}
	
	public List<ProductListVO> queryByElasticSearch(String queryStr, int offset, int limit){
		// ES config
//	    Settings settings = Settings.settingsBuilder()
//	            .put("cluster.name", "elasticsearch")
//	            .put("client.transport.sniff", true)
//	            .build();
//	    try{
//			Client client = TransportClient.builder().settings(settings).build()
//					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("47.88.85.15"), 9300));
//			
//			SearchResponse response = client.prepareSearch("ff_shop")
//					.setTypes("item")
//					.setSearchType(SearchType.DEFAULT)
//					.setFetchSource(new String[]{"template_id", "image_url", "on_sale", "c_platform_price", "price", "c_platform_name", "product_no"}, null)
//					.setQuery(QueryBuilders.termQuery("name", queryStr))
//					.setQuery(QueryBuilders.termQuery("website_published", "True"))
//					.setQuery(QueryBuilders.termQuery("can_shipped_to_me", "True"))
//					.setFrom(offset).setSize(limit)
//					.execute()
//					.actionGet();
//			
//			for (SearchHit hit : response.getHits()){
//				System.out.println(hit);
//			}
//			return null;
//	    }catch(Exception e){
//	    	System.out.println(e.getMessage());
//	    	e.printStackTrace();
//	    	
//	    	return null;
//	    }
//	    
	    //v2 send with http
		JSONObject sendObj = new JSONObject();
		
		List<String> fieldsArray = new ArrayList<>();
		fieldsArray.add("template_id");
		fieldsArray.add("image_url");
		fieldsArray.add("on_sale");
		fieldsArray.add("c_platform_price");
		fieldsArray.add("price");
		fieldsArray.add("c_platform_name");
		fieldsArray.add("product_no");
		
		sendObj.put("from", offset);
		sendObj.put("size", limit);
		sendObj.put("fields", fieldsArray);
		
		JSONObject queryObj = new JSONObject();
		JSONObject mustObj = new JSONObject();
		
		List<JSONObject> mustArray = new ArrayList<>();
		
		JSONObject matchObj = new JSONObject();
		JSONObject searchObj = new JSONObject();
		searchObj.put("website_published", "True");
		matchObj.put("match", searchObj);
		mustArray.add(matchObj);
		
		matchObj = new JSONObject();
		searchObj = new JSONObject();
		searchObj.put("name", queryStr);
		matchObj.put("match", searchObj);
		mustArray.add(matchObj);
		
		matchObj = new JSONObject();
		searchObj = new JSONObject();
		searchObj.put("can_shipped_to_me", "True");
		matchObj.put("match", searchObj);
		mustArray.add(matchObj);
		
		mustObj.put("must", mustArray);
		queryObj.put("bool", mustObj);
		sendObj.put("query", queryObj);
		
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new BasicNameValuePair("body", sendObj.toJSONString()));
		try{
			JSONObject result = commonService.jsonRequest(HomeDisplayConfig.ELASTICSEARCH_URL, sendObj);
			
			if (result != null){
				if(result.containsKey("hits")){
					JSONObject hits = result.getJSONObject("hits");
					if(hits.containsKey("hits")){
						JSONArray hitsArray = hits.getJSONArray("hits");
						List<ProductListVO> productListVOList = new ArrayList<>();
						for (int i = 0; i < hitsArray.size(); i++){
							JSONObject hitItem = (JSONObject) hitsArray.get(i);
							if (hitItem == null){
								continue;
							}
							JSONObject fields = hitItem.getJSONObject("fields");
							if (fields == null){
								continue;
							}
							System.out.println(fields.toJSONString());
							ProductListVO productListVO = new ProductListVO();
							
							JSONArray imageArray = fields.getJSONArray("image_url");
							String imageStr = imageArray.getString(0);
							if (imageStr != null){
								productListVO.setImageUrl(imageStr);
							}
							
							JSONArray templateIdArray = fields.getJSONArray("template_id");
							if (templateIdArray.getInteger(0) != null){
								int oldId = templateIdArray.getInteger(0);
								Spu spu = spuDao.selectByOldId(oldId);
								if (spu == null){
									//先为了测试用
									productListVO.setSpuId(oldId);
								}else{
									productListVO.setSpuId(spu.getId());
								}
							}
							
							JSONArray platformNameArray = fields.getJSONArray("c_platform_name");
							if (platformNameArray.getString(0) != null){
								productListVO.setPlatformName(platformNameArray.getString(0));
							}
							
							JSONArray platformPriceArray = fields.getJSONArray("c_platform_price");
							if (platformPriceArray.getDouble(0) != null){
								productListVO.setPlatformPrice(platformPriceArray.getDouble(0));
							}
							
							JSONArray priceArray = fields.getJSONArray("price");
							if (priceArray.getDouble(0) != null){
								productListVO.setPrice(priceArray.getDouble(0));
							}
							
							JSONArray productNoArray = fields.getJSONArray("product_no");
							if (productNoArray.getString(0) != null){
								productListVO.setProductNo(productNoArray.getString(0));
							}
							
							productListVOList.add(productListVO);
						}
						System.out.println(hits);
						return productListVOList;
					}else{
						return null;
					}
				}
			}
		}catch(Exception e){
			System.err.println(e);
			return null;
		}
		
		return null;
	}
	
	static class SpuOverallScoreComparator implements Comparator<Spu>{
		public int compare(Spu spu1, Spu spu2){
			return new Integer(spu1.getOverallScore()).compareTo(new Integer(spu2.getOverallScore()));
		}
	}
	
	static class SpuOverallScoreNewComparator implements Comparator<Spu>{
		public int compare(Spu spu1, Spu spu2){
			return new Integer(spu1.getNewUserScore()).compareTo(new Integer(spu2.getNewUserScore()));
		}
	}
}
