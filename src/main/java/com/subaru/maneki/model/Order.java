package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author zhangchaojie
 * @since 2016-08-31
 */
public class Order implements Serializable {

    private long      orderNumber;

    private int       userId;

    private int       shippingId;

    private int       tradeStatus;

    private double    amount;

    private String    currency;

    private int       payType;

    private Timestamp gmtPaied;

    private Timestamp gmtCreate;

    private Timestamp gmtUpdate;

    private boolean   isPushed;

    private boolean   isDelete;

    public Order() {
    }

    public Order(long orderNumber, int userId, int shippingId, int tradeStatus, double amount,
                 String currency) {
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.shippingId = shippingId;
        this.tradeStatus = tradeStatus;
        this.amount = amount;
        this.currency = currency;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public Timestamp getGmtPaied() {
        return gmtPaied;
    }

    public void setGmtPaied(Timestamp gmtPaied) {
        this.gmtPaied = gmtPaied;
    }

    public boolean getIsPushed() {
        return this.isPushed;
    }

    public void setPushed(boolean isPushed) {
        this.isPushed = isPushed;
    }

    public boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }
}