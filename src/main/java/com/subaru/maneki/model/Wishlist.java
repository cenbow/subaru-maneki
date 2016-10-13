package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Wishlist implements Serializable {

    private int       id;

    private int       userId;

    private int       spuId;

    private Timestamp gmtCreate;

    private Timestamp gmtUpdate;

    public Wishlist() {

    }

    public Wishlist(int userId, int spuId) {
        this.userId = userId;
        this.spuId = spuId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSpuId() {
        return spuId;
    }

    public void setSpuId(int spuId) {
        this.spuId = spuId;
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
