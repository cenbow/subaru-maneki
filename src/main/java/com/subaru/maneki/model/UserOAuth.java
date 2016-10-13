package com.subaru.maneki.model;

import java.sql.Timestamp;

/**
 * @author zhangchaojie
 * @since 2016-08-11
 */
public class UserOAuth {

    private int    userId;

    private String    oauthUid;

    private String oauthProvider;

    private Timestamp gmtCreate;

    private Timestamp gmtUpdate;

    public UserOAuth() {

    }

    public UserOAuth(int userId, String oauthUid, String oauthProvider) {
        this.userId = userId;
        this.oauthUid = oauthUid;
        this.oauthProvider = oauthProvider;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOauthUid() {
        return oauthUid;
    }

    public void setOauthUid(String oauthUid) {
        this.oauthUid = oauthUid;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
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
