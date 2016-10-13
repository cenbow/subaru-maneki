package com.subaru.maneki.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.subaru.maneki.vo.ProductCategorySetVO;
import com.subaru.maneki.vo.ProductListVO;

public interface ProductListManager {
	
	/**
	 * 根据指定条件从数据库中查找相关商品信息
	 * 如果指定了shuffle排序，那么默认是先取出前100个商品数据，再对这100个进行shuffle，最后取出前limit个商品
	 * @param categoryId
	 * @param limit
	 * @param offset
	 * @param isNewUser
	 * @param isShippedToMe
	 * @param productSetIds
	 * @return
	 */
	List<ProductListVO> getProductList(int categoryId, int limit, int offset, 
			boolean isNewUser, boolean isShippedToMe, List<Integer> productSetIds, boolean isShuffle);
		
	/**
	 * 首页中根据选择要显示的商品品类，列出要品类下排名靠前的商品
	 * @param request
	 * @param isNewUser
	 * @param isShippedToMe
	 * @return
	 */
	List<ProductCategorySetVO> getProductSet(HttpServletRequest request, boolean isNewUser, boolean isShippedToMe);
	
	/**
	 * 返回商品详情页面最后显示Pepole Also See的内容
	 * @param categoryId
	 * @param canShippedToMe
	 * @param spuId
	 * @return
	 */
	List<ProductListVO> getRelatedProducts(int categoryId, boolean canShippedToMe, int spuId);
	
	/**
	 * 根据某个类目的id，随机获取该类目下前4个商品，这个接口获取会改进，不限定4个
	 * @param categoryId
	 * @param isShuffle
	 * @return
	 */
	List<ProductListVO> getSingleProductSet(int categoryId, boolean isShuffle);
	
	/**
	 * 计算折扣
	 * @param request
	 * @param productListVOList
	 */
	void computeSaving(ProductListVO productListVO);
	
	String idList2Str(List<Integer> idList);
	
	/**
	 * 通过elasticsearch引擎返回query相关的内容
	 * @param queryStr
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<ProductListVO> queryByElasticSearch(String queryStr, int offset, int limit);
}
