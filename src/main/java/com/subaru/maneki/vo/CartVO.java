package com.subaru.maneki.vo;

import java.util.List;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
public class CartVO implements BaseVO{

    private int          id;

    private int          skuId;

    private int          spuId;

    private String       productName;

    //main image
    private String       imageUrl;

    private double       price;

    private String       platformPrice;

    private String       currencyUnit;

    private int          quantity;

    private boolean      isSaleOk;

    private List<PropVO> propList;
    
    public void transferPrice(double rate){
    	if(Double.compare(rate, 0.0) == 0){
    		return;
    	}
    	this.platformPrice = Double.toString(Double.parseDouble(this.platformPrice) * rate);
    	this.price = this.price * rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPlatformPrice() {
        return platformPrice;
    }

    public void setPlatformPrice(String platformPrice) {
        this.platformPrice = platformPrice;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<PropVO> getPropList() {
        return propList;
    }

    public void setPropList(List<PropVO> propList) {
        this.propList = propList;
    }

    public boolean getIsSaleOk() {
        return isSaleOk;
    }

    public void setIsSaleOk(boolean isSaleOk) {
        this.isSaleOk = isSaleOk;
    }
}
