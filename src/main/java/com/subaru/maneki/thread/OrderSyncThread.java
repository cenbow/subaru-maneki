//package com.subaru.appserver.thread;
//
//import java.util.List;
//
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.alibaba.fastjson.JSON;
//import com.subaru.appserver.bo.OrderSync;
//import com.subaru.common.http.HttpBuilder;
//
///**
// * @author zhangchaojie
// * @since 2016-09-06
// */
//public class OrderSyncThread implements Runnable {
//
//    private String          orderSyncUrl;
//
//    private List<OrderSync> orderSyncList;
//
//    private static Logger   logger = LoggerFactory.getLogger(OrderSyncThread.class);
//
//    public String getOrderSyncUrl() {
//        return orderSyncUrl;
//    }
//
//    public void setOrderSyncUrl(String orderSyncUrl) {
//        this.orderSyncUrl = orderSyncUrl;
//    }
//
//    public List<OrderSync> getOrderSyncList() {
//        return orderSyncList;
//    }
//
//    public void setOrderSyncList(List<OrderSync> orderSyncList) {
//        this.orderSyncList = orderSyncList;
//    }
//
//    @Override
//    public void run() {
//
//        if (StringUtils.isBlank(orderSyncUrl) || orderSyncList == null || orderSyncList.size() == 0) {
//            return;
//        }
//
//        String result = "";
//
//        for (int i = 0; StringUtils.isBlank(result) || !result.equals("1"); i++) {
//            logger.info("Begin to synchronize orders, the orderSyncUrl is : " + this.orderSyncUrl
//                        + ", and the orderSyncListJson is : " + JSON.toJSONString(orderSyncList));
//            HttpBuilder httpBuilder = new HttpBuilder(orderSyncUrl);
//            httpBuilder.addParam("orderSyncList", JSON.toJSONString(orderSyncList));
//            result = httpBuilder.get();
//            if (i > 50) {
//                break;
//            }
//            logger.info("The synchronize result is :" + result);
//        }
//
//    }
//}
