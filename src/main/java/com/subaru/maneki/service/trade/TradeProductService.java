package com.subaru.maneki.service.trade;

import java.util.List;

import com.subaru.maneki.model.TradeProduct;

/**
 * @author zhangchaojie
 * @since 2016-09-07
 */
public interface TradeProductService {

    List<TradeProduct> getByOrderNumber(long orderNumber);

    TradeProduct get(int id);

}
