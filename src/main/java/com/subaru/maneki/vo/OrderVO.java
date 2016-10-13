package com.subaru.maneki.vo;

import java.util.List;

/**
 * @author zhangchaojie
 * @since 2016-09-09
 */
public class OrderVO implements BaseVO{

    private long         orderNumber;

    private int          tradeStatus;

    List<TradeProductVO> tradeProductList;

    private double       totalPrice;

    private String       currency;
    
    //是否有物流信息
    private boolean		 isShipped;
    
    public void transferPrice(double rate){
    	if(Double.compare(rate, 0.0) == 0){
    		return;
    	}
    	this.totalPrice = this.totalPrice * rate;
    	for(TradeProductVO tradeProductVO:tradeProductList){
    		tradeProductVO.transferPrice(rate);
    	}
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public List<TradeProductVO> getTradeProductList() {
        return tradeProductList;
    }

    public void setTradeProductList(List<TradeProductVO> tradeProductList) {
        this.tradeProductList = tradeProductList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

	public boolean getIsShipped() {
		return isShipped;
	}

	public void setIsShipped(boolean isShipped) {
		this.isShipped = isShipped;
	}
    
    
}
