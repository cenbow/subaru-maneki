package com.subaru.maneki.service.trade;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
public interface DeliveryService {

    /**
     * zhangchaojie
     * 根据国家和实际费用计算运费
     * @param countryId
     * @param cartTotalPrice
     * @return
     */
    double getPostFee(int countryId, double cartTotalPrice);

    double getAmountToFreeDelivery(int countryId, double cartTotalPrice);
    
    String getDeliveryMethod(int countryId);
}
