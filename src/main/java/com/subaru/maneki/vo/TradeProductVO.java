package com.subaru.maneki.vo;

import java.util.List;

/**
 * @author zhangchaojie
 * @since 2016-09-09
 */
public class TradeProductVO {

    private int          spuId;

    private int          skuId;

    private String       productName;

    private String       productImage;

    private double       productOriginalPrice;

    private double       productOrderPrice;

    private int          productQuantity;

    private List<PropVO> productProps;
    
    public void transferPrice(double rate){
    	if(Double.compare(rate, 0.0) == 0){
    		return;
    	}
    	this.productOriginalPrice = this.productOriginalPrice * rate;
    	this.productOriginalPrice = this.productOriginalPrice * rate;
    }

    public int getSpuId() {
        return spuId;
    }

    public void setSpuId(int spuId) {
        this.spuId = spuId;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
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

    public double getProductOriginalPrice() {
        return productOriginalPrice;
    }

    public void setProductOriginalPrice(double productOriginalPrice) {
        this.productOriginalPrice = productOriginalPrice;
    }

    public double getProductOrderPrice() {
        return productOrderPrice;
    }

    public void setProductOrderPrice(double productOrderPrice) {
        this.productOrderPrice = productOrderPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public List<PropVO> getProductProps() {
        return productProps;
    }

    public void setProductProps(List<PropVO> productProps) {
        this.productProps = productProps;
    }
}
