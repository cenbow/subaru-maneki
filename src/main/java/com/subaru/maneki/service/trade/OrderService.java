package com.subaru.maneki.service.trade;

import java.sql.Timestamp;
import java.util.List;

import com.subaru.maneki.exception.TradeException;
import com.subaru.maneki.model.Order;
import com.subaru.maneki.model.TradeProduct;

/**
 * @author zhangchaojie
 * @since 2016-08-31
 */
public interface OrderService {

    long add(int userId, int shippingId, List<TradeProduct> tradeProductList) throws TradeException;

    Order get(long orderNumber);

    List<Order> get(Timestamp startTime, Timestamp endTime, int page, int pageSize);

    List<Order> get(int userId, int page, int pageSize);

    void update(Order order) throws TradeException;

    int count(Timestamp startTime, Timestamp endTime);

    int count(int userId);

    int countPaied(int userId);

    /**
     * zhangchaojie
     * 采用逻辑删除的方式删除订单，不会级联删除该订单对应的tradeProduct、shippingInfo等其他信息
     * @param orderNumber
     */
    void delete(long orderNumber) throws TradeException;

    /**
     * zhangchaojie
     * 获取需要同步至ERP的订单：1. 选择COD方式的订单 2. 非COD方式且有付款记录的订单
     */
    List<Order> getNeedToPush();

}
