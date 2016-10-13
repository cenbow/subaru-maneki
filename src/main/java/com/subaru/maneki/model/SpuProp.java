package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author zhangchaojie
 * @since 2016-08-23
 */
@Deprecated
public class SpuProp implements Serializable {
    int       id;

    /**
     * SPU的ID
     */
    int       spuId;

    /**
     * prop的ID
     */
    int       propId;

    Timestamp gmtCreate;

    Timestamp gmtUpdate;

    public SpuProp(){
    	
    }
    
    public SpuProp(int spuId, int propId) {
        this.spuId = spuId;
        this.propId = propId;
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

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
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
