package com.subaru.maneki.vo;

import java.sql.Timestamp;

/**
 * @author zhangchaojie
 * @since 2016-09-09
 */
public class WishListVO {
	
	private int 		id;
	
	private int 		spuId;

    private String    productName;

    private String    productImage;

    private Timestamp gmtCreate;

    private String    productPrice;

    private String    productPlatformPrice;
    
    public void transferPrice(double rate){
    	if(Double.compare(rate, 0.0) == 0){
    		return;
    	}
    	this.productPrice = Double.toString(Double.parseDouble(this.productPrice) * rate);
    	this.productPlatformPrice = Double.toString(Double.parseDouble(this.productPlatformPrice) * rate);
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSpuId() {
		return spuId;
	}

	public void setSpuId(int spuId) {
		this.spuId = spuId;
	}

	public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductPlatformPrice() {
        return productPlatformPrice;
    }

    public void setProductPlatformPrice(String productPlatformPrice) {
        this.productPlatformPrice = productPlatformPrice;
    }
}
