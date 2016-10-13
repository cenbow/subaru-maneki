package com.subaru.maneki.service.trade.imp;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.OrderDao;
import com.subaru.maneki.dao.TradeProductDao;
import com.subaru.maneki.exception.TradeException;
import com.subaru.maneki.model.*;
import com.subaru.maneki.service.trade.OrderService;
import com.subaru.maneki.service.trade.ShippingInfoService;
import com.subaru.maneki.service.user.UserService;
import com.subaru.common.util.NumberUtil;
import com.subaru.common.util.PageUtil;

/**
 * @author zhangchaojie
 * @since 2016-08-31
 */
@Service("orderService")
public class OrderServiceImp implements OrderService {

    @Resource
    private UserService         userService;

    @Resource
    private ShippingInfoService shippingInfoService;

    @Resource
    private OrderDao            orderDao;

    @Resource
    private TradeProductDao     tradeProductDao;

    private static final String DEFAULT_CURRENCY = "usd";

    @Override
    public long add(int userId, int shippingId, List<TradeProduct> tradeProductList)
                                                                                    throws TradeException {
        if (userId <= 0 || shippingId <= 0 || tradeProductList == null
            || tradeProductList.size() <= 0) {
            throw new TradeException(
                "Invalid userId or shippingId or empty tradeProductList, create order error !!");
        }

        User user = userService.get(userId);
        if (user == null) {
            throw new TradeException("Cannot find any user by the given userId !!");
        }

        long orderNumber = generateOrderNumber(userId, tradeProductList.get(0).getId());
        ShippingInfo shippingInfo = shippingInfoService.get(shippingId);
        if (shippingInfo == null) {
            throw new TradeException("Cannot find any shippingInfo by the given shippingId:"
                                     + shippingId);
        }
        double actutalPrice = getActualPrice(tradeProductList, shippingInfo);
        Order order = new Order(orderNumber, userId, shippingId,
            TradeStatus.WAIT_FOR_THE_PAYMENT.getStatusCode(), actutalPrice, DEFAULT_CURRENCY);
        int effectRows = orderDao.insert(order);
        if (effectRows < 1) {
            throw new TradeException("Create order error !！");
        }

        for (TradeProduct tradeProduct : tradeProductList) {
            tradeProduct.setOrderNumber(orderNumber);
            tradeProductDao.insert(tradeProduct);
        }

        return orderNumber;
    }

    private double getActualPrice(List<TradeProduct> tradeProductList, ShippingInfo shippingInfo)
                                                                                                 throws TradeException {

        double sum = 0;

        if (tradeProductList == null || tradeProductList.size() == 0) {
            return 0;
        }

        for (TradeProduct tradeProduct : tradeProductList) {
            sum += tradeProduct.getBuyedPrice() * tradeProduct.getQuantity();
        }

        double postFee = shippingInfo.getPostFee();
        if (postFee < 0) {
            throw new TradeException("The postFee of shippingInfo is less than 0 !! ShippingId:"
                                     + shippingInfo.getId());
        }

        sum = sum + postFee;

        return sum;

    }

    @Override
    public Order get(long orderNumber) {
        if (orderNumber <= 0) {
            return null;
        }
        return orderDao.selectByOrderNumber(orderNumber);
    }

    @Override
    public List<Order> get(Timestamp startTime, Timestamp endTime, int page, int pageSize) {
        if (startTime == null || endTime == null || startTime.getTime() > endTime.getTime()
            || page <= 0 || pageSize <= 0) {
            return null;
        }
        return orderDao.selectByTime(startTime, endTime, PageUtil.getStart(page, pageSize),
            PageUtil.getLimit(page, pageSize));
    }

    @Override
    public List<Order> get(int userId, int page, int pageSize) {
        if (userId <= 0 || page < 0 || pageSize <= 0) {
            return null;
        }
        return orderDao.selectByUserId(userId, page, pageSize);
    }

    @Override
    public void update(Order order) throws TradeException {
        if (order == null) {
            throw new TradeException("Cannot update order with null");
        }
        orderDao.update(order);
    }

    @Override
    public int count(Timestamp startTime, Timestamp endTime) {
        if (startTime == null || endTime == null || startTime.getTime() > endTime.getTime()) {
            return 0;
        }
        return orderDao.countByTime(startTime, endTime);
    }

    @Override
    public int count(int userId) {
        if (userId <= 0) {
            return 0;
        }
        return orderDao.countByUserId(userId);
    }

    @Override
    public int countPaied(int userId) {
        if (userId <= 0) {
            return 0;
        }
        return orderDao.countPaiedByUserId(userId);
    }

    @Override
    public void delete(long orderNumber) throws TradeException {
        if (orderNumber <= 0 || orderDao.selectByOrderNumber(orderNumber) == null) {
            throw new TradeException("Cannot get order by the given orderNumber : " + orderNumber);
        }
        Order order = orderDao.selectByOrderNumber(orderNumber);
        order.setIsDelete(true);
        orderDao.update(order);
    }

    @Override
    public List<Order> getNeedToPush() {
        return orderDao.selectNeedToPush();
    }

    /**
     * zhangchaojie
     * 根据userId、skuId以及当前时间生成订单号
     * @param buyerId
     * @param skuId
     * @return
     */
    private static long generateOrderNumber(int buyerId, int skuId) {

        String prefix = NumberUtil.getNumber(skuId, 1, false);
        String suffix = NumberUtil.getNumber(buyerId, 2, false);

        StringBuilder orderNumberBuilder = new StringBuilder(20);

        orderNumberBuilder.append(prefix);
        orderNumberBuilder.append(System.currentTimeMillis());
        orderNumberBuilder.append(suffix);

        return Long.parseLong(orderNumberBuilder.toString());
    }

}
