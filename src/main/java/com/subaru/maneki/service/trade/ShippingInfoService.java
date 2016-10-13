package com.subaru.maneki.service.trade;

import com.subaru.maneki.exception.TradeException;
import com.subaru.maneki.model.ShippingInfo;

/**
 * @author zhangchaojie
 * @since 2016-09-06
 */
public interface ShippingInfoService {

    void add(ShippingInfo shippingInfo) throws TradeException;

    ShippingInfo get(int id);

    void update(ShippingInfo shippingInfo) throws TradeException;

    void delete(int id) throws TradeException;

}
