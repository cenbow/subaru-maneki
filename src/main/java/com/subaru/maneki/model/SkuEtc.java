package com.subaru.maneki.model;

import java.sql.Timestamp;

/**
 * @author zhangchaojie
 * @since 2016-08-23
 */
public class SkuEtc {

    private int       skuId;

    private String    platformPrice;

    private String    platformUrl;

    private String    platformPriceDetails;

    private String    platformName;

    private String    urlOf1688;

    private Timestamp gmtCreate;

    private Timestamp gmtUpdate;

    public SkuEtc() {

    }

    public SkuEtc(int skuId, String platformPrice, String platformUrl, String platformPriceDetails,
                  String platformName, String urlOf1688) {
        this.skuId = skuId;
        this.platformPrice = platformPrice;
        this.platformUrl = platformUrl;
        this.platformPriceDetails = platformPriceDetails;
        this.platformName = platformName;
        this.urlOf1688 = urlOf1688;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
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

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Timestamp getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Timestamp gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }
}
