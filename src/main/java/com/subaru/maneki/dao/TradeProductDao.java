package com.subaru.maneki.dao;

import java.util.List;

import com.subaru.maneki.model.TradeProduct;

/**
 * @author zhangchaojie
 * @since 2016-09-01
 */
public interface TradeProductDao {

    int insert(TradeProduct tradeProduct);

    int update(TradeProduct tradeProduct);

    int delete(int id);

    TradeProduct select(int id);

    List<TradeProduct> selectByOrderNumber(long orderNumber);

}