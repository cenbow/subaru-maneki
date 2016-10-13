package com.subaru.maneki.controller;

import com.subaru.maneki.config.HomeDisplayConfig;
import com.subaru.maneki.cons.DeliveryConstant;
import com.subaru.maneki.manager.CommonManager;
import com.subaru.maneki.manager.ProductListManager;
import com.subaru.maneki.manager.ThemeManager;
import com.subaru.maneki.model.Country;
import com.subaru.maneki.service.trade.CountryStateService;
import com.subaru.maneki.vo.ProductCategorySetVO;
import com.subaru.maneki.vo.ProductListVO;
//import com.subaru.appserver.Config.HomePageConfig;
import com.subaru.common.vo.JsonVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhangchaojie
 * @since 2016-08-08
 */
@Controller
public class HomepageController {
	
	@Resource
	private ProductListManager	productListService;
	
	@Resource
	private CommonManager		commonService;
	
	@Resource
	private CountryStateService countryStateService;
	
	@Resource
	private ThemeManager		themeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homepage(HttpServletRequest request, 
    		Model model,
    		@RequestParam(defaultValue = "0") int categoryId, 
    		@RequestParam(defaultValue = "0") int limit,
    		@RequestParam(defaultValue = "0") int currentPage,
    		@RequestParam(required = false) String q,
    		@RequestParam(defaultValue = "0") int appendPage,
    		@RequestParam(defaultValue = "0") int fromDetail,
    		@RequestParam(defaultValue = "0") int spuId,
    		@RequestParam(required = false) String productSetIds,
    		@RequestParam(required = false) String ad) {
    	
    	boolean isAppendPageReq = (appendPage == 1);
    	boolean isFromDetail = (fromDetail == 1);
    	
    	if(currentPage == 0){
    		currentPage = 1;
    	}
    	
    	if (limit == 0){
    		limit = HomeDisplayConfig.DEFAULT_LIMIT;
    	}
    	int offset = (currentPage - 1) * limit;
    	
    	boolean isShippedToMe = true;
    	
    	boolean showBanner = ((categoryId == 0 && (q == null)));
    	
    	String bannerDate = HomeDisplayConfig.BANNER_DATE;
        
    	//从cookies中获取用户选择的国家
    	String countryCode = commonService.getCountryCodeFromCookie(request);
    	Country country = countryStateService.getCountryByAbbr(countryCode);
    	
    	//从cookies中获取客户端判断当前用户是否是新的用户
    	String newUserStr = commonService.getCookieValue(request, "isNew_customer");
    	boolean isNewUser = false;
    	if (newUserStr != null && newUserStr.equals("yes")){
    		isNewUser = true;
    	}
    	
    	//从cookies中读取客户端设置的手机app版本
    	String version = commonService.getCookieValue(request, "v");
    	boolean showMore = false;
    	if (version != null){
    		if(version.contains("android")){
    			if(version.compareTo(HomeDisplayConfig.ANDROID_VERSION) >= 0){
    				showMore = true;
    			}
    		}
    		if(version.contains("IOS")){
    			if(version.compareTo(HomeDisplayConfig.IOS_VERSION) >= 0){
    				showMore = true;
    			}
    		}
    	}
    	
    	boolean showCbBanner = showBanner;
    	if (countryCode.toUpperCase().equals("US")){
    		showCbBanner = false;
    	}
    	
    	//ad source    	
    	    	
    	//显示首页中product set的信息
    	List<Integer> productSetIdList = new ArrayList<>();
    	List<ProductCategorySetVO> productCategorySetVOList = null;
    	boolean showCategory = false;
    	if (isAppendPageReq == false && (isFromDetail == false) && (categoryId == 0) && (q == null)){
    		showCategory = true;
    		productCategorySetVOList = productListService.getProductSet(request, isNewUser, isShippedToMe);
    		
    		for (ProductCategorySetVO productCategorySetVO: productCategorySetVOList){
    			for (ProductListVO productListVO: productCategorySetVO.getProductListVO()){
    				productSetIdList.add(productListVO.getSpuId());
    				
    				//转换货币单位
    				productListVO.transferPrice(country.getRate());
    				//计算折扣
    				productListService.computeSaving(productListVO);
    			}
    		}
    	}
    	
    	List<ProductListVO> productListVOList = null;
    	if (q != null){
    		productListVOList = productListService.queryByElasticSearch(q, offset, limit);
    	}else if (isFromDetail) {
    		productListVOList = productListService.getRelatedProducts(categoryId, isShippedToMe, spuId);
    	}else{
    		productListVOList = productListService.getProductList(categoryId, limit, offset, 
    				isNewUser, isShippedToMe, productSetIdList, false);
    	}
    	
    	//价格转换
    	if (productListVOList != null){
	    	for (ProductListVO productListVO: productListVOList){			
				//转换货币单位
				productListVO.transferPrice(country.getRate());
				//计算折扣
				productListService.computeSaving(productListVO);
			}
    	}
    	
    	//是否显示search no result提示
    	boolean searchNoResult = false;
    	if ((productListVOList != null && productListVOList.isEmpty()) && (currentPage == 1) && (q != null)){
    		searchNoResult = true;
    	}
    	
    	String nextPageParam = "currentPage=" + (currentPage + 1) + "&limit=" + limit + "&categoryId=" + categoryId + "&appendPage=1";
    	String lastPageParam = "currentPage=" + (currentPage) + "&limit=" + limit + "&categoryId=" + categoryId + "&appendPage=1";
    	
    	if(q != null){
    		nextPageParam += "&q=" + q;
    		lastPageParam += "&q=" + q;
    	}
    	if (isFromDetail){
    		nextPageParam += "&fromDetail=1";
    		lastPageParam += "&fromDetail=1";
    	}
    	
    	if (productSetIdList.isEmpty() == false){
    		String productSetIdStr = productListService.idList2Str(productSetIdList);
    		nextPageParam += "&productSetIds=" + productSetIdStr;
    		lastPageParam += "&productSetIds=" + productSetIdStr;
    	}
    	
    	if(isAppendPageReq == false && isFromDetail == false){
    		model.addAttribute("products", productListVOList);
    		model.addAttribute("showCategory", showCategory);
    		model.addAttribute("productCategorySet", productCategorySetVOList);
    		model.addAttribute("showBanner", showBanner);
    		model.addAttribute("showCbBanner", showCbBanner);
    		model.addAttribute("bannerDate", bannerDate);
    		model.addAttribute("searchNoResult", searchNoResult);
    		model.addAttribute("currency", country.getCurrencyUnit());
    		model.addAttribute("nextPageParam", nextPageParam);
    		model.addAttribute("isAppendPageReq", isAppendPageReq);
    		model.addAttribute("lastPageParam", lastPageParam);
    		model.addAttribute("isFromDetail", isFromDetail);
    		model.addAttribute("countryCode", countryCode);
    		model.addAttribute("showMore", showMore);
    		model.addAttribute("isNewUser", isNewUser);
    		return "page/product/home";
    	}else{
    		model.addAttribute("products", productListVOList);
    		model.addAttribute("searchNoResult", searchNoResult);
    		model.addAttribute("nextPageParam", nextPageParam);
    		model.addAttribute("currency", country.getCurrencyUnit());
    		model.addAttribute("isAppendPageReq", isAppendPageReq);
    		model.addAttribute("lastPageParam", lastPageParam);
    		model.addAttribute("isFromDetail", isFromDetail);
    		model.addAttribute("countryCode", countryCode);
    		model.addAttribute("isNewUser", isNewUser);
    		return "page/product/product_list";
    	}
    	
    }

    @ResponseBody
    @RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
    public String home(){
        JsonVO json = new JsonVO();
        json.setData("This is a json from home !!");
        json.setIsSuccess(1);
        return json.toString();
    }

    @RequestMapping(value = "/theme", method = {RequestMethod.GET})
    public String theme(HttpServletRequest request, Model model, @RequestParam(required = true) String bannerDate){
    	
    	List<String[]> idList = themeService.getThemeProductId(request, bannerDate);
    	if (idList == null){
    		return "page/error/404";
    	}
    	
    	List<String> titleList = new ArrayList<>();
    	List<List<String>> rowSrcList = new ArrayList<>();
    	
    	String prefix = "/img/theme/";
    	String countryCode = commonService.getCountryCodeFromCookie(request).toLowerCase();
    	for (int i = 0; i < idList.size(); i++){
    		List<String> innerRowList = new ArrayList<>();
    		for (int j = 0; j < idList.get(i).length; j++){
    			innerRowList.add(prefix + bannerDate + "/" + countryCode + "/s" + i + "p" + j + ".jpg");
    		}
    		rowSrcList.add(innerRowList);
    		titleList.add(prefix + bannerDate + "/" + "section_title" + i + ".jpg");
    	}
    	
    	model.addAttribute("productIdList", idList);
    	model.addAttribute("title", titleList);
    	model.addAttribute("row", rowSrcList);
    	model.addAttribute("footer", prefix + "/" + bannerDate + "/footer.jpg");
    	
    	return "page/theme/theme";
    }
    
    @ResponseBody
    @RequestMapping(value = "/club_factory/country_info", method = {RequestMethod.GET})
    public String theme(HttpServletRequest request){
    	JsonVO json = new JsonVO();
    	
    	List<String> countryList = new ArrayList<>(DeliveryConstant.SHIPPING_CONSTANT.keySet());
    	//默认按照字典序排序
    	Collections.sort(countryList);
    	json.setData(countryList);
    	json.setIsSuccess(1);
    	return json.toString();
    }
    
}
