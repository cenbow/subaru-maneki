package com.subaru.maneki.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.subaru.maneki.manager.CommonManager;
import com.subaru.maneki.manager.ProductListManager;
import com.subaru.maneki.model.Cate;
import com.subaru.maneki.service.product.CateService;
import com.subaru.maneki.service.trade.CountryStateService;
import com.subaru.maneki.vo.CateVO;
import com.subaru.common.vo.JsonVO;

@Controller
public class CategoryController {

	@Resource
	private CateService cateService;
	
	@Resource
	private ProductListManager	productListService;
	
	@Resource
	private CommonManager	commonService;
	
	@Resource
	private CountryStateService countryStateService;

	@ResponseBody
	@RequestMapping(value = "/category/get_home_category", method = RequestMethod.GET)
	public String getHomeCategory(HttpServletRequest request) {

		JsonVO json = new JsonVO();
		
		try{
			List<Cate> cateList = cateService.getAll();
			List<Cate> topCateList = getTopCategory(cateList);
			
			List<CateVO> cateVOList = getCateVOs(topCateList);
			json.setIsSuccess(1);
			json.setData(cateVOList);
			
		}catch(Exception e){
			json.setIsSuccess(0);
		}
		
		return json.toString();
	}
	
	@RequestMapping(value = "/category/get_main_category", method = RequestMethod.GET)
	public String getMainCategory(HttpServletRequest request, Model model,  @RequestParam(required = false) String category) {
		
		return "page/error/404";
//		String categoryName = category;
//		if(category == null){
//			categoryName = "Total";
//		}
//		
//		Map<String, List<Integer>> categoryMap = new HashMap<>();
//		for (String key : CategoryDisplayConstant.CATEGORY_DISPLAY_CONSTANT.keySet()){
//			String value = CategoryDisplayConstant.CATEGORY_DISPLAY_CONSTANT.get(key);
//			
//			String[] valueList = value.split(",");
//			if (valueList.length > 0){
//				List<Integer> categoryIdArray = new ArrayList<>();
//				for(String s: valueList){
//					categoryIdArray.add(Integer.parseInt(s));
//				}
//				categoryMap.put(key, categoryIdArray);
//			}
//		}
//		model.addAttribute("categoryName", category);
//		model.addAttribute("categoryMap", categoryMap);
//		
//		String abbr = commonService.getCountryCodeFromCookie(request);
//		Country country = countryStateService.getCountryByAbbr(abbr);
//		
//		model.addAttribute("currency", country.getCurrencyUnit());
//		
//		int categoryId = 0;
//		categoryName = categoryName.toLowerCase();
//		if (categoryName.equals("total") == false && CategoryDisplayConstant.CATEGORY_DISPLAY_CONSTANT.containsKey(categoryName)){
//						
//			String categoryIdStr = CategoryDisplayConstant.CATEGORY_DISPLAY_CONSTANT.get(categoryName.toLowerCase());
//			String[] valueList = categoryIdStr.split(",");
//			if (valueList.length > 1) {
//				categoryId = Integer.parseInt(valueList[1]);
//			} else {
//				categoryId = Integer.parseInt(valueList[0]);
//			}
//
//			List<ProductListVO> productListVOList = productListService.getSingleProductSet(categoryId, true);
//			if (productListVOList != null) {
//				for (ProductListVO productListVO : productListVOList) {
//					// 转换货币单位
//					productListVO.transferPrice(country.getRate());
//					// 计算折扣
//					productListService.computeSaving(productListVO);
//				}
//			}
//			model.addAttribute("productListVOList", productListVOList);
//
//			List<Cate> cateList = cateService.getChildCategory(categoryId);
//			model.addAttribute("cateList", cateList);
//			
//		}else{
//			model.addAttribute("productListVOList", null);
//			model.addAttribute("cateList", null);
//		}
//		
//		model.addAttribute("categoryId", categoryId);
//		
//		return "page/category/categories";
	}
	
	/**
	 * 获取品类结构中最顶层的品类列表
	 * @return
	 */
	private List<Cate> getTopCategory(List<Cate> cateList){
		if (cateList == null){
			return null;
		}
		List<Cate> topCateList = new ArrayList<>();
		for(Cate cate : cateList){
			if (cate.getParentId() == 0){
				topCateList.add(cate);
			}
		}
	    
		return topCateList;
	}

	/**
	 * 将所有品类信息转换为前端方便读取的格式，格式参见CateVO
	 * @param cateList
	 * @return
	 */
	private List<CateVO> getCateVOs(List<Cate> topCateList) {
		if (topCateList == null) {
			return null;
		}
		if (topCateList.isEmpty()) {
			return null;
		}

		List<CateVO> cateVOList = new ArrayList<>(topCateList.size());
		for (Cate cate : topCateList) {
			CateVO cateVO = new CateVO();

			cateVO.setId(cate.getId());
			cateVO.setName(cate.getName());

			// get all child category
			List<Cate> childCateList = cateService.getChildCategory(cate.getId());
			List<CateVO> childCateVOList = getCateVOs(childCateList);
			if(childCateVOList != null){
				cateVO.setChildList(childCateVOList);
			}
			cateVOList.add(cateVO);
		}
		return cateVOList;
	}
	
}
