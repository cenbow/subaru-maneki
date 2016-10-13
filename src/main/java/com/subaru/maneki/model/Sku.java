package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
public class Sku implements Serializable {

    protected int       id;

    protected int       spuId;

    protected double    price;

    protected double    weight;

    protected String    currencyUnit;

    protected int       inventory;

    protected boolean   isSaleOk;

    protected String propColor;

    protected String propSize;

    protected String propStyle;

    protected String propMetalColor;

    protected String propCColor;

    protected int       oldId;

    protected Timestamp gmtCreate;

    protected Timestamp gmtUpdate;

    public Sku() {
    }

    public Sku(int spuId, double price, double weight, String currencyUnit, int inventory,
               boolean isSaleOk, int oldId) {
        this.spuId = spuId;
        this.price = price;
        this.weight = weight;
        this.currencyUnit = currencyUnit;
        this.inventory = inventory;
        this.isSaleOk = isSaleOk;
        this.oldId = oldId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public boolean getIsSaleOk() {
        return isSaleOk;
    }

    public void setIsSaleOk(boolean isSaleOk) {
        this.isSaleOk = isSaleOk;
    }

    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

    public String getPropColor() {
        return propColor;
    }

    public void setPropColor(String propColor) {
        this.propColor = propColor;
    }

    public String getPropSize() {
        return propSize;
    }

    public void setPropSize(String propSize) {
        this.propSize = propSize;
    }

    public String getPropStyle() {
        return propStyle;
    }

    public void setPropStyle(String propStyle) {
        this.propStyle = propStyle;
    }

    public String getPropMetalColor() {
        return propMetalColor;
    }

    public void setPropMetalColor(String propMetalColor) {
        this.propMetalColor = propMetalColor;
    }

    public String getPropCColor() {
        return propCColor;
    }

    public void setPropCColor(String propCColor) {
        this.propCColor = propCColor;
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
