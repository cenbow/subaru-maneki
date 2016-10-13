package com.subaru.maneki.vo;

import java.util.List;

/**
 * @author zhangchaojie
 * @since 2016-08-23
 */
public class SkuVO {

    private int          oldId;

    private int          spuId;

    private double       price;

    private double       weight;

    private String       currencyUnit;

    private int          inventory;

    private boolean      isSaleOk;

    private String       platformPrice;

    private String       platformUrl;

    private String       platformPriceDetails;

    private String       platformName;

    private String       urlOf1688;

    private List<PropVO> propList;

    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

    public int getSpuId() {
        return spuId;
    }

    public void setSpuId(int spuId) {
        this.spuId = spuId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public boolean getIsSaleOk() {
        return isSaleOk;
    }

    public void setIsSaleOk(boolean isSaleOk) {
        this.isSaleOk = isSaleOk;
    }

    public String getPlatformPrice() {
        return platformPrice;
    }

    public void setPlatformPrice(String platformPrice) {
        this.platformPrice = platformPrice;
    }

    public String getPlatformUrl() {
        return platformUrl;
    }

    public void setPlatformUrl(String platformUrl) {
        this.platformUrl = platformUrl;
    }

    public String getPlatformPriceDetails() {
        return platformPriceDetails;
    }

    public void setPlatformPriceDetails(String platformPriceDetails) {
        this.platformPriceDetails = platformPriceDetails;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getUrlOf1688() {
        return urlOf1688;
    }

    public void setUrlOf1688(String urlOf1688) {
        this.urlOf1688 = urlOf1688;
    }

    public List<PropVO> getPropList() {
        return propList;
    }

    public void setPropList(List<PropVO> propList) {
        this.propList = propList;
    }
}
