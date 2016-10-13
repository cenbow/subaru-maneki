package com.subaru.maneki.manager;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public interface CommonManager {
	
	/**
	 * 根据cookieName从cookies中查找到指定name的value
	 * @param request
	 * @param cookieName
	 * @return
	 */
	String getCookieValue(HttpServletRequest request, String cookieName);
	
	/**
	 * 从cookie中获取用户选择的国家简称，比如美国就是US
	 * @param request
	 * @return
	 */
	String getCountryCodeFromCookie(HttpServletRequest request);
	
	String getCurrency(HttpServletRequest request);
	
	/**
	 * 根据汇率信息和美元价格，计算转换后的价格
	 * @param dollarPrice
	 * @param rate
	 * @return
	 */
	double calculateCountryPrice(double dollarPrice, double rate);
	
	double calculateCountryPrice(double dollarPrice, int countryId);
	
	double calculateCountryPrice(double dollarPrice, String abbr);
	
	/**
	 * 计算折扣
	 * @param platformPrice:竞争平台价格
	 * @param price:我们平台价格
	 * @return
	 */
	int computeSaving(double platformPrice, double price);
	
	JSONObject jsonRequest(String url, JSONObject json);
}
