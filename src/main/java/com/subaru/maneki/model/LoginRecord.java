package com.subaru.maneki.model;

import java.sql.Timestamp;

/**
 * @author zhangchaojie
 * @since 2016-08-15
 */
public class LoginRecord {

    private int       id;

    private int       userId;

    private String    token;

    private String    ip;

    private int       loginType;

    private Timestamp gmtCreate;

    private Timestamp gmtUpdate;

    private Timestamp gmtExpire;

    public LoginRecord() {

    }

    public LoginRecord(int userId, String token, String ip, int loginType, Timestamp gmtExpire) {
        this.userId = userId;
        this.token = token;
        this.ip = ip;
        this.loginType = loginType;
        this.gmtExpire = gmtExpire;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
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

    public Timestamp getGmtExpire() {
        return gmtExpire;
    }

    public void setGmtExpire(Timestamp gmtExpire) {
        this.gmtExpire = gmtExpire;
    }
}
