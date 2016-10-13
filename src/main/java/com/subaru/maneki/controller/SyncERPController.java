//package com.subaru.appserver.controller;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.alibaba.fastjson.JSON;
//import com.subaru.appserver.bo.OrderSkuSync;
//import com.subaru.appserver.bo.OrderSync;
//import com.subaru.appserver.enumeration.PayType;
//import com.subaru.appserver.exception.TradeException;
//import com.subaru.appserver.model.*;
//import com.subaru.appserver.service.product.SkuService;
//import com.subaru.appserver.service.trade.CountryStateService;
//import com.subaru.appserver.service.trade.OrderService;
//import com.subaru.appserver.service.trade.ShippingInfoService;
//import com.subaru.appserver.service.trade.TradeProductService;
//import com.subaru.appserver.service.user.AddressService;
//import com.subaru.appserver.service.user.UserService;
//import com.subaru.common.http.DomainManager;
//import com.subaru.common.http.HttpBuilder;
//import com.subaru.common.vo.JsonVO;
//
///**
// * @author zhangchaojie
// * @since 2016-09-06
// */
//@Controller
//public class SyncERPController {
//
//    @Resource
//    private OrderService        orderService;
//
//    @Resource
//    private UserService         userService;
//
//    @Resource
//    private AddressService      addressService;
//
//    @Resource
//    private CountryStateService countryStateService;
//
//    @Resource
//    private ShippingInfoService shippingInfoService;
//
//    @Resource
//    private TradeProductService tradeProductService;
//
//    @Resource
//    private SkuService          skuService;
//
//    //    private ExecutorService     executorService   = Executors.newCachedThreadPool();
//
//    private static String       PYTHON_SERVER_URL = DomainManager.getInstance().getDomain(
//                                                      "python.server.url");
//
//    private static String       SYNC_ORDER_URL    = PYTHON_SERVER_URL + "/sync_order";
//
//    private static Logger       logger            = LoggerFactory
//                                                      .getLogger(SyncERPController.class);
//
//    /**
//     * zhangchaojie
//     * 按每5分钟一次的频率，向ERP系统推送新生成的订单
//     */
//    @Scheduled(cron = "0 0/5 * * * ?")
//    @ResponseBody
//    @RequestMapping(value = "/sync/pushOrder", method = RequestMethod.GET)
//    public void pushOrder() {
//
//        //zhangchaojie 同步最近5分钟新生成的订单至ERP
//        List<Order> needToPushOrderList = orderService.getNeedToPush();
//        if (needToPushOrderList == null || needToPushOrderList.size() == 0) {
//            return;
//        }
//        List<OrderSync> needToPushOrderSyncList = getOrderSyncList(needToPushOrderList);
//
//        for (OrderSync orderSync : needToPushOrderSyncList) {
//
//            logger.info("Begin to synchronize order, the orderSyncUrl is : " + SYNC_ORDER_URL
//                        + ", and the orderJson is : " + JSON.toJSONString(orderSync));
//            HttpBuilder httpBuilder = new HttpBuilder(SYNC_ORDER_URL);
//            httpBuilder.addParam("order", JSON.toJSONString(orderSync));
//            String result = httpBuilder.get();
//            logger.info("The synchronize result of order " + orderSync.getOrder_no() + "is : "
//                        + result);
//        }
//    }
//
//    private List<OrderSync> getOrderSyncList(List<Order> orderList) {
//        if (orderList == null || orderList.size() == 0) {
//            return null;
//        }
//        List<OrderSync> orderSyncList = new ArrayList<>();
//        for (Order order : orderList) {
//            OrderSync orderSync = new OrderSync();
//            orderSync.setOrder_no(order.getOrderNumber() + "");
//            int userId = order.getUserId();
//            User user = userService.get(userId);
//            if (user == null) {
//                continue;
//            }
//            ShippingInfo shippingInfo = shippingInfoService.get(order.getShippingId());
//            if (shippingInfo == null) {
//                continue;
//            }
//            orderSync.setAccount(user.getEmail());
//            orderSync.setShipping_name(shippingInfo.getReceiverName());
//
//            int addressId = shippingInfo.getAddressId();
//            Address address = addressService.get(addressId);
//            if (address == null) {
//                continue;
//            }
//            orderSync.setShipping_street(address.getStreet());
//            orderSync.setShipping_city(address.getCity());
//            CountryState state = countryStateService.getState(address.getStateId());
//            Country country = countryStateService.getCountry(address.getCountryId());
//            if (state == null || country == null) {
//                continue;
//            }
//            orderSync.setShipping_state(state.getName());
//            orderSync.setShipping_country(country.getName());
//            orderSync.setShipping_zip(address.getZip());
//            orderSync.setShipping_phone(shippingInfo.getReceiverPhone());
//            if (order.getGmtPaied() != null) {
//                orderSync.setPaid_date(order.getGmtPaied());
//            }
//            if (order.getPayType() > 0) {
//                orderSync.setPay_type(PayType.getType(order.getPayType()).name());
//            }
//
//            List<OrderSkuSync> orderSkuSyncList = new ArrayList<>();
//            List<TradeProduct> tradeProductList = tradeProductService.getByOrderNumber(order
//                .getOrderNumber());
//            if (tradeProductList == null || tradeProductList.size() == 0) {
//                continue;
//            }
//            for (TradeProduct tradeProduct : tradeProductList) {
//                OrderSkuSync orderSkuSync = new OrderSkuSync();
//                orderSkuSync.setSku_id(tradeProduct.getSkuId() + "");
//                orderSkuSync.setProduct_name(tradeProduct.getProductName());
//                orderSkuSync.setProduct_qty(tradeProduct.getQuantity() + "");
//                Sku sku = skuService.get(tradeProduct.getSkuId());
//                if (sku != null) {
//                    orderSkuSync.setPrice_unit(sku.getCurrencyUnit());
//                }
//                orderSkuSyncList.add(orderSkuSync);
//            }
//            orderSync.setOrderSkuSyncList(orderSkuSyncList);
//
//            orderSync.setDelivery_name(shippingInfo.getDeliveryMethod());
//            orderSync.setDelivery_fee(shippingInfo.getPostFee() + "");
//            orderSyncList.add(orderSync);
//        }
//        return orderSyncList;
//    }
//
//    /**
//     * zhangchaojie
//     * 接收来自ERP的订单更新信息，包括支付状态及支付方式
//     * @param orderSyncString
//     */
//    @ResponseBody
//    @RequestMapping(value = "/sync/updateOrder", method = RequestMethod.GET)
//    public String updateOrderStatus(@RequestParam String orderSyncString) throws TradeException {
//
//        JsonVO json = new JsonVO();
//
//        if (StringUtils.isBlank(orderSyncString)) {
//            json.setIsSuccess(0);
//            json.setMsg("The orderSyncString is blank !!");
//            return json.toString();
//        }
//
//        OrderSync orderSync = JSON.parseObject(orderSyncString, OrderSync.class);
//        String orderNumberStr = orderSync.getOrder_no();
//        if (StringUtils.isBlank(orderNumberStr)) {
//            json.setMsg("The orderNumberStr in the orderSyncString is blank !!");
//            json.setIsSuccess(0);
//            logger.info("The orderNumberStr in the orderSyncString is blank !!");
//            return json.toString();
//        }
//        long orderNumber = Long.parseLong(orderNumberStr);
//        Order order = orderService.get(orderNumber);
//        if (order == null) {
//            json.setMsg("Cannot get any order by orderNumber in the orderSyncString !!");
//            json.setIsSuccess(0);
//            return json.toString();
//        }
//        Timestamp paid_date = orderSync.getPaid_date();
//        String pay_type = orderSync.getPay_type();
//        if (StringUtils.isNotBlank(pay_type) && paid_date != null) {
//            order.setGmtPaied(paid_date);
//            order.setPayType(PayType.valueOf(pay_type).getValue());
//        }
//
//        orderService.update(order);
//
//        json.setIsSuccess(1);
//        return json.toString();
//
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/sync/updateProducts", method = RequestMethod.GET)
//    public void updateProducts(@RequestParam String productsJsonStr) {
//
//    }
//
//}
