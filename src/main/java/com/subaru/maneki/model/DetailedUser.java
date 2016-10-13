package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author zhangchaojie
 * @since 2016-08-10
 */
public class DetailedUser extends User implements Serializable {

    private String    phone;

    private Timestamp birthday;

    private String    language;

    private int       countryId;

    private Timestamp gmtCreate;

    private Timestamp gmtUpdate;

    public DetailedUser() {

    }

    public DetailedUser(String email, String cellphone, String nick, String password,
                        int registerType, String phone, Timestamp birthday, String language,
                        int countryId) {
        this.email = email;
        this.cellphone = cellphone;
        this.nick = nick;
        this.password = password;
        this.registerType = registerType;
        this.phone = phone;
        this.birthday = birthday;
        this.language = language;
        this.countryId = countryId;

    }

    public DetailedUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.cellphone = user.getCellphone();
        this.nick = user.getNick();
        this.password = user.getPassword();
        this.registerType = user.getRegisterType();
        this.gmtCreate = user.getGmtCreate();
        this.gmtUpdate = user.getGmtUpdate();
    }

    public DetailedUser(User user, UserEtc userEtc) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.cellphone = user.getCellphone();
        this.nick = user.getNick();
        this.password = user.getPassword();
        this.registerType = user.getRegisterType();
        this.phone = userEtc.getPhone();
        this.birthday = userEtc.getBirthday();
        this.language = userEtc.getLanguage();
        this.countryId = userEtc.getCountryId();
        this.gmtCreate = user.getGmtCreate();
        this.gmtUpdate = user.getGmtUpdate();
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
