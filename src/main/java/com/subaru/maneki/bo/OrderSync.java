package com.subaru.maneki.bo;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author zhangchaojie
 * @since 2016-09-06
 */
public class OrderSync {

    private String             order_no;

    private String             account;

    private List<OrderSkuSync> orderSkuSyncList;

    private String             shipping_name;

    private String             shipping_street;

    private String             shipping_city;

    private String             shipping_state;

    private String             shipping_country;

    private String             shipping_zip;

    private String             shipping_phone;

    private String             delivery_name;

    private String             delivery_fee;

    private Timestamp          paid_date;

    private String             pay_type;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<OrderSkuSync> getOrderSkuSyncList() {
        return orderSkuSyncList;
    }

    public void setOrderSkuSyncList(List<OrderSkuSync> orderSkuSyncList) {
        this.orderSkuSyncList = orderSkuSyncList;
    }

    public String getShipping_name() {
        return shipping_name;
    }

    public void setShipping_name(String shipping_name) {
        this.shipping_name = shipping_name;
    }

    public String getShipping_street() {
        return shipping_street;
    }

    public void setShipping_street(String shipping_street) {
        this.shipping_street = shipping_street;
    }

    public String getShipping_city() {
        return shipping_city;
    }

    public void setShipping_city(String shipping_city) {
        this.shipping_city = shipping_city;
    }

    public String getShipping_state() {
        return shipping_state;
    }

    public void setShipping_state(String shipping_state) {
        this.shipping_state = shipping_state;
    }

    public String getShipping_country() {
        return shipping_country;
    }

    public void setShipping_country(String shipping_country) {
        this.shipping_country = shipping_country;
    }

    public String getShipping_zip() {
        return shipping_zip;
    }

    public void setShipping_zip(String shipping_zip) {
        this.shipping_zip = shipping_zip;
    }

    public String getShipping_phone() {
        return shipping_phone;
    }

    public void setShipping_phone(String shipping_phone) {
        this.shipping_phone = shipping_phone;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public Timestamp getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(Timestamp paid_date) {
        this.paid_date = paid_date;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }
}
