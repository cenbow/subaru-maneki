package com.subaru.maneki.model;

/**
 * @author zhangchaojie
 * @since 2016-09-01
 */
public enum TradeStatus {
    /** 等待买家支付 */
    WAIT_FOR_THE_PAYMENT(1),

    /** 等待卖家发货 */
    WAIT_FOR_THE_DELIVER(2),

    /** 等待买家确认收货 */
    WAIT_FOR_THE_CONFIRM(3),

    /** 交易成功 */
    TRADE_SUCCESS(4),

    /** 交易被取消 */
    TRADE_CANCELED(-1);

    private int statusCode;

    public static TradeStatus getTradeStatus(int statusCode) {
        TradeStatus[] tradeStatuses = TradeStatus.values();
        for (TradeStatus TradeStatus : tradeStatuses) {
            if (TradeStatus.getStatusCode() == statusCode) {
                return TradeStatus;
            }
        }
        return null;
    }

    private TradeStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
