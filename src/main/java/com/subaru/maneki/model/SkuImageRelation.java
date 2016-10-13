package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author zhangchaojie
 * @since 2016-08-23
 */
@Deprecated
public class SkuImageRelation implements Serializable {
    /**
     *sku的ID
     */
    int       skuId;

    /**
     * image的ID
     */
    int       imageId;

    Timestamp gmtCreate;

    Timestamp gmtUpdate;

    public SkuImageRelation(int skuId, int imageId) {
        this.skuId = skuId;
        this.imageId = imageId;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
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
