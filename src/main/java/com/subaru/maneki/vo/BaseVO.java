package com.subaru.maneki.vo;

/**
 * @author lee
 */
public interface BaseVO {
	
	/**
	 * 所有实现该接口的类属性中，将表示价格的成员按照汇率进行换算
	 * @param rate
	 */
	void transferPrice(double rate);
	
}
