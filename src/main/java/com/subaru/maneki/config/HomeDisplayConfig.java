package com.subaru.maneki.config;

import java.util.HashMap;
import java.util.Map;

public class HomeDisplayConfig {
	
	public static String CATAGORY_SELECTED = "Jewelry,Clothing,Home"; 
	
	public static String BANNER_DATE = "20160901";
	
	public static String ANDROID_VERSION = "android_2.27";
	
	public static String IOS_VERSION = "ios_1.4.3";
	
	public static int 	DEFAULT_LIMIT = 50;
	
	public static int 	PRODUCT_DETAIL_DEFAULT_LIMIT = 50;

	public static String ELASTICSEARCH_URL = "http://47.88.85.15:9200/ff_shop/item/_search";
	
	public static String THEME_COUNTRY_CODE = "US,KW,AE,AU,CA";
	
	public static final Map<String, String> THEME_DATE_MAP = new HashMap<String, String>();
	
	static{
		THEME_DATE_MAP.put("20160708", "16910,27035,61668;27856,20181,27034;17244,16894,16855");
		THEME_DATE_MAP.put("20160729", "24644");
		THEME_DATE_MAP.put("20160817", "101113,101457,90068,102179,101375,101480,101259,76392");
		THEME_DATE_MAP.put("20160901", "100947,27844,55066;93418,76006,58041;58091,102740,102053");
		THEME_DATE_MAP.put("20160909", "107109,27237,27246;20026;62671,16910,62537,61748");
	}
}
