package com.subaru.maneki.dao;

import com.subaru.maneki.model.ShippingInfo;

/**
 * @author zhangchaojie
 * @since 2016-09-06
 */
public interface ShippingInfoDao {

    int insert(ShippingInfo shippingInfo);

    int update(ShippingInfo shippingInfo);

    int delete(int id);

    ShippingInfo select(int id);

}