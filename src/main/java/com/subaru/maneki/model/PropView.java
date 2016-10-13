package com.subaru.maneki.model;

import java.sql.Timestamp;

/**
 * @author zhangchaojie
 * @since 2016-08-23
 */
public class PropView {

    private int       spuId;

    private String    spuPropJson;

    private String    skuPropJson;

    private Timestamp gmtCreate;

    private Timestamp gmtUpdate;

    public PropView() {

    }

    public PropView(int spuId, String spuPropJson, String skuPropJson) {
        this.spuId = spuId;
        this.spuPropJson = spuPropJson;
        this.skuPropJson = skuPropJson;
    }

    public int getSpuId() {
        return spuId;
    }

    public void setSpuId(int spuId) {
        this.spuId = spuId;
    }

    public String getSpuPropJson() {
        return spuPropJson;
    }

    public void setSpuPropJson(String spuPropJson) {
        this.spuPropJson = spuPropJson;
    }

    public String getSkuPropJson() {
        return skuPropJson;
    }

    public void setSkuPropJson(String skuPropJson) {
        this.skuPropJson = skuPropJson;
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
