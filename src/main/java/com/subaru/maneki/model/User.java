package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author zhangchaojie
 * @since 2016-08-11
 */
public class User implements Serializable {

    protected int       id;

    protected String    email;

    protected String    cellphone;

    protected String    password;

    protected String    nick;

    protected int       registerType;

    protected Timestamp gmtCreate;

    protected Timestamp gmtUpdate;

    //zhangchaojie: 对应odoo中表res_partner的id
    protected int       oldId;

    public User() {
    }

    public User(String email, String cellphone, String nick, String password, int registerType) {
        this.email = email;
        this.cellphone = cellphone;
        this.nick = nick;
        this.password = password;
        this.registerType = registerType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getRegisterType() {
        return registerType;
    }

    public void setRegisterType(int registerType) {
        this.registerType = registerType;
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

    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }
}
