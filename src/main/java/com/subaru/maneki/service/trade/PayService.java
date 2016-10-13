package com.subaru.maneki.service.trade;

import com.subaru.maneki.bo.PayParam;
import com.subaru.maneki.exception.TradeException;

/**
 * @author zhangchaojie
 * @since 2016-08-31
 */
public interface PayService {

    void pay(PayParam payParam) throws TradeException;

}
