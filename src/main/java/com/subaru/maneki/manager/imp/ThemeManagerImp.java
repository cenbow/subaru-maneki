package com.subaru.maneki.manager.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.subaru.maneki.config.HomeDisplayConfig;
import com.subaru.maneki.manager.CommonManager;
import com.subaru.maneki.manager.ThemeManager;

@Service("themeService")
public class ThemeManagerImp implements ThemeManager{
	
	@Resource
	private CommonManager commonService;
	
	public List<String[]> getThemeProductId(HttpServletRequest request, String date){
		
		String chooseCountryCode = commonService.getCountryCodeFromCookie(request);
		if (HomeDisplayConfig.THEME_COUNTRY_CODE == null){
			return null;
		}
		String[] themeCountryCodeList = HomeDisplayConfig.THEME_COUNTRY_CODE.split(",");
		
		boolean isFound = false;
		for(String countryCode: themeCountryCodeList){
			if (countryCode.equals(chooseCountryCode) == true){
				isFound = true;
				break;
			}
		}
		
		if (isFound == false){
			chooseCountryCode = "US";
		}
		
		if (date == null){
			date = HomeDisplayConfig.BANNER_DATE;
		}
		
		if (date.compareTo("20160817") >= 0){
			chooseCountryCode = "US";
		}
		
		List<String[]> idList = new ArrayList<>();
		if (HomeDisplayConfig.THEME_DATE_MAP.containsKey(date)){
			String productList = HomeDisplayConfig.THEME_DATE_MAP.get(date);
			for (String productId : productList.split(";")){
				idList.add(productId.split(","));
			}
			return idList;
		}else{
			return null;
		}
		
	}
	
}
