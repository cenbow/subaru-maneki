package com.subaru.maneki.cons;

import java.util.HashMap;
import java.util.Map;

public class DeliveryConstant {
	
	public static final Map<String, ShippingInfo> SHIPPING_CONSTANT = new HashMap<String, ShippingInfo>();
	
	public static class ShippingInfo{
		private String 	shippingTime;
		private String 	shippingMethod;
		
		public ShippingInfo(String time, String method){
			shippingMethod = method;
			shippingTime = time;
		}

		public String getShippingTime() {
			return shippingTime;
		}

		public void setShippingTime(String shippingTime) {
			this.shippingTime = shippingTime;
		}

		public String getShippingMethod() {
			return shippingMethod;
		}

		public void setShippingMethod(String shippingMethod) {
			this.shippingMethod = shippingMethod;
		}
	
	}
	
	static{
		SHIPPING_CONSTANT.put("BH", new ShippingInfo("5-9 days", "Aramex"));
		SHIPPING_CONSTANT.put("EG", new ShippingInfo("5-9 days", "Aramex"));
		SHIPPING_CONSTANT.put("KW", new ShippingInfo("5-9 days", "Aramex"));
		SHIPPING_CONSTANT.put("LB", new ShippingInfo("5-9 days", "Aramex"));
		SHIPPING_CONSTANT.put("QA", new ShippingInfo("5-9 days", "Aramex"));
		SHIPPING_CONSTANT.put("SA", new ShippingInfo("15-20 days", "Saudi Post"));
		SHIPPING_CONSTANT.put("AE", new ShippingInfo("5-9 days", "Aramex"));
		SHIPPING_CONSTANT.put("CA", new ShippingInfo("7-12 days", "Canada Post"));
		SHIPPING_CONSTANT.put("AU", new ShippingInfo("7-10 days", "Australia Post"));
		SHIPPING_CONSTANT.put("GB", new ShippingInfo("15-20 days", "UK Post"));
		SHIPPING_CONSTANT.put("FR", new ShippingInfo("15-20 days", "France Post"));
		SHIPPING_CONSTANT.put("IN", new ShippingInfo("7-12 days", "Aramex"));
		SHIPPING_CONSTANT.put("US", new ShippingInfo("7-12 days", "USPS"));//default
	}
}
