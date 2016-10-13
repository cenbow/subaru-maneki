package test.com.subaru.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.AddressDao;
import com.subaru.maneki.dao.OrderDao;
import com.subaru.maneki.dao.ShippingInfoDao;
import com.subaru.maneki.dao.UserDao;
import com.subaru.maneki.enumeration.PayType;
import com.subaru.maneki.enumeration.RegisterType;
import com.subaru.maneki.model.*;
import com.subaru.core.BaseTestCaseJunit;
import com.subaru.core.encryption.MD5;

/**
 * @author zhangchaojie
 * @since 2016-08-11
 */
public class OrderDaoTest extends BaseTestCaseJunit {

    @Resource
    private UserDao         userDao;

    @Resource
    private AddressDao      addressDao;

    @Resource
    private ShippingInfoDao shippingInfoDao;

    @Resource
    private OrderDao        orderDao;

    private Address         address;

    private ShippingInfo    shippingInfo;

    private User            user;

    private Order           order;

    private long            orderNumber = 123456654321L;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("chaojiezhang@epiclouds.net");
        user.setCellphone("18258477020");
        user.setPassword(MD5.MakeMD5("password"));
        user.setNick("zcj");
        user.setRegisterType(RegisterType.EMAIL.getValue());
        userDao.insert(user);

        address = new Address();
        address.setCountryId(123);
        address.setStateId(1);
        address.setCity("HangZhou");
        address.setStreet("WuChang");
        address.setZip("31002");
        addressDao.insert(address);

        shippingInfo = new ShippingInfo();
        shippingInfo.setReceiverName("name");
        shippingInfo.setReceiverPhone("phone");
        shippingInfo.setReceiverEmail("email");
        shippingInfo.setAddressId(address.getId());
        shippingInfo.setPostFee(40);
        shippingInfo.setDeliveryMethod("method");
        shippingInfoDao.insert(shippingInfo);

        order = new Order();
        order.setOrderNumber(orderNumber);
        order.setUserId(user.getId());
        order.setShippingId(shippingInfo.getId());
        order.setTradeStatus(TradeStatus.TRADE_SUCCESS.getStatusCode());
        order.setCurrency("usd");
        order.setGmtPaied(Timestamp.valueOf("2016-09-07 00:00:00"));
        order.setPayType(PayType.STRIPE.getValue());
        orderDao.insert(order);
    }

    @Test
    public void testSelectByOrderNumber() {
        Order newOrder = orderDao.selectByOrderNumber(orderNumber);
        Assert.assertTrue(order.getUserId() == newOrder.getUserId());
        Assert.assertTrue(order.getShippingId() == newOrder.getShippingId());
        Assert.assertTrue(order.getTradeStatus() == newOrder.getTradeStatus());
        Assert.assertTrue(order.getGmtPaied().getTime() == newOrder.getGmtPaied().getTime());
        Assert.assertTrue(order.getPayType() == newOrder.getPayType());
    }

    @Test
    public void testUpdate() {
        user = new User();
        user.setEmail("sa9530@163.com");
        user.setCellphone("18657912863");
        user.setPassword(MD5.MakeMD5("newpassword"));
        user.setNick("Rin");
        user.setRegisterType(RegisterType.FACEBOOK.getValue());
        userDao.insert(user);

        address = new Address();
        address.setCountryId(234);
        address.setStateId(2);
        address.setCity("FuYang");
        address.setStreet("XianLin");
        address.setZip("110");
        addressDao.insert(address);

        order.setUserId(user.getId());
        order.setShippingId(shippingInfo.getId());
        order.setTradeStatus(TradeStatus.TRADE_SUCCESS.getStatusCode());
        order.setGmtPaied(Timestamp.valueOf("2016-09-07 23:59:59"));
        order.setPayType(PayType.PAY_PAL.getValue());
        orderDao.update(order);

        Order newOrder = orderDao.selectByOrderNumber(orderNumber);
        Assert.assertTrue(order.getUserId() == newOrder.getUserId());
        Assert.assertTrue(order.getShippingId() == newOrder.getShippingId());
        Assert.assertTrue(order.getTradeStatus() == newOrder.getTradeStatus());
        Assert.assertTrue(order.getGmtPaied().getTime() == newOrder.getGmtPaied().getTime());
        Assert.assertTrue(order.getPayType() == newOrder.getPayType());

        order.setIsDelete(true);
        orderDao.update(order);
        Order deletedOrder = orderDao.selectByOrderNumber(this.order.getOrderNumber());
        Assert.assertNull(deletedOrder);
    }

    @Test
    public void testDeleteByOrderNumber() {
        orderDao.deleteByOrderNumber(orderNumber);
        Order deletedOrder = orderDao.selectByOrderNumber(orderNumber);
        Assert.assertNull(deletedOrder);
    }

    @Test
    public void testSelectByTime() {
        List<Order> orders = orderDao.selectByTime(Timestamp.valueOf("2016-09-01 00:00:00"),
            new Timestamp(System.currentTimeMillis()), 1, 20);
        Assert.assertTrue(orders.size() > 0);
    }

    @Test
    public void testSelectByUserId() {
        List<Order> orders = orderDao.selectByUserId(user.getId(), 0, 10);
        Assert.assertTrue(orders.size() == 1);
    }

    @Test
    public void testCountByUserId() {
        int orderNum = orderDao.countByUserId(user.getId());
        Assert.assertTrue(orderNum == 1);
    }

    @Test
    public void testCountPaiedByUserId() {

        int orderNum = orderDao.countPaiedByUserId(user.getId());
        Assert.assertTrue(orderNum == 1);

    }

    @Test
    public void testSelectNeedToPush() {
        List<Order> orderList = orderDao.selectNeedToPush();
        Assert.assertTrue(orderList.size() > 0);
    }

}
