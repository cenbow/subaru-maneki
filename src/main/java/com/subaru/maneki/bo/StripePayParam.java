package com.subaru.maneki.bo;

/**
 * @author zhangchaojie
 * @since 2016-08-31
 */
public class StripePayParam extends PayParam {

    private String token;

    //以美分为单位进行结算
    private int    amount;

    private String currency;

    // 统一为内部订单号
    private String description;

    public StripePayParam() {

    }

    public StripePayParam(long orderNumber, String token, int amount, String currency,
                          String description) {
        this.orderNumber = orderNumber;
        this.token = token;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
