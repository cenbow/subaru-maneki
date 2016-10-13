package com.subaru.maneki.bo;

/**
 * @author zhangchaojie
 * @since 2016-08-31
 */
public abstract class PayParam {

    protected long orderNumber;

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }
}
