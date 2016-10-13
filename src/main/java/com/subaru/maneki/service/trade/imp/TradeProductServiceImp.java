package com.subaru.maneki.service.trade.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.TradeProductDao;
import com.subaru.maneki.model.TradeProduct;
import com.subaru.maneki.service.trade.TradeProductService;

/**
 * @author zhangchaojie
 * @since 2016-09-07
 */
@Service("tradeProductService")
public class TradeProductServiceImp implements TradeProductService {

    @Resource
    private TradeProductDao tradeProductDao;

    @Override
    public List<TradeProduct> getByOrderNumber(long orderNumber) {
        if (orderNumber <= 0) {
            return null;
        }
        return tradeProductDao.selectByOrderNumber(orderNumber);
    }

    @Override
    public TradeProduct get(int id) {
        if (id <= 0) {
            return null;
        }
        return tradeProductDao.select(id);
    }

}
