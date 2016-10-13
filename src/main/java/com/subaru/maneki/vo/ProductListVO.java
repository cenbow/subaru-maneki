package com.subaru.maneki.vo;

/**
 * @author lee
 * 首页所需要的信息
 */
public class ProductListVO implements BaseVO{
	
	int 		spuId;
	
	String 		imageUrl;
	
	double 		platformPrice;
	
	String 		platformName;
	
	double 		price;
	
	String 		productNo;
	
	/**
	 * 计算折扣
	 */
	int			saving;
	
	public void transferPrice(double rate){
		this.platformPrice = this.platformPrice * rate;
		this.price = this.price * rate;
	}

	public int getSpuId() {
		return spuId;
	}

	public void setSpuId(int spuId) {
		this.spuId = spuId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public double getPlatformPrice() {
		return platformPrice;
	}

	public void setPlatformPrice(double platformPrice) {
		this.platformPrice = platformPrice;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public int getSaving() {
		return saving;
	}

	public void setSaving(int saving) {
		this.saving = saving;
	}

}
