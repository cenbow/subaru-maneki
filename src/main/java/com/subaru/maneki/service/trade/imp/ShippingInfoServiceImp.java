package com.subaru.maneki.service.trade.imp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.ShippingInfoDao;
import com.subaru.maneki.exception.TradeException;
import com.subaru.maneki.model.ShippingInfo;
import com.subaru.maneki.service.trade.ShippingInfoService;

/**
 * @author zhangchaojie
 * @since 2016-09-06
 */
@Service("shippingInfoService")
public class ShippingInfoServiceImp implements ShippingInfoService {

    @Resource
    private ShippingInfoDao shippingInfoDao;

    @Override
    public void add(ShippingInfo shippingInfo) throws TradeException {

        if (shippingInfo == null) {
            throw new TradeException("Cannot add shippingInfo, shippingInfo is null !!");
        }

        shippingInfoDao.insert(shippingInfo);

    }

    @Override
    public ShippingInfo get(int id) {

        if (id <= 0) {
            return null;
        }

        return shippingInfoDao.select(id);
    }

    @Override
    public void update(ShippingInfo shippingInfo) throws TradeException {

        if (shippingInfo == null) {
            throw new TradeException("Cannot update shippingInfo, shippingInfo is null !!");
        }

        shippingInfoDao.update(shippingInfo);

    }

    @Override
    public void delete(int id) throws TradeException {

        if (id <= 0) {
            throw new TradeException("Cannot delete shippingInfo, illeagal id !!");
        }

    }
}
