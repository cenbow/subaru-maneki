package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserEtc implements Serializable {

    private int       userId;

    private String    phone;

    private Timestamp birthday;

    private String    language;

    private int       countryId;

    private Timestamp gmtLastLogin;

    private Timestamp gmtCreate;

    private Timestamp gmtUpdate;

    public UserEtc() {

    }

    public UserEtc(int userId, String phone, Timestamp birthday, String language, int countryId) {
        this.userId = userId;
        this.phone = phone;
        this.birthday = birthday;
        this.language = language;
        this.countryId = countryId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public Timestamp getGmtLastLogin() {
        return gmtLastLogin;
    }

    public void setGmtLastLogin(Timestamp gmtLastLogin) {
        this.gmtLastLogin = gmtLastLogin;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}