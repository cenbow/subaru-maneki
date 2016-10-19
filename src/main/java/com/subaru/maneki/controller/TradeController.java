package com.subaru.maneki.controller;

import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.subaru.maneki.bo.StripePayParam;
import com.subaru.maneki.config.StripeConfig;
import com.subaru.maneki.cons.DeliveryConstant;
import com.subaru.maneki.dao.CountryStateDao;
import com.subaru.maneki.dao.UserAddressRelationDao;
import com.subaru.maneki.enumeration.PayType;
import com.subaru.maneki.exception.TradeException;
import com.subaru.maneki.exception.UserAddressException;
import com.subaru.maneki.manager.CartManager;
import com.subaru.maneki.manager.CommonManager;
import com.subaru.maneki.model.*;
import com.subaru.maneki.service.cart.CartService;
import com.subaru.maneki.service.trade.*;
import com.subaru.maneki.service.user.AddressService;
import com.subaru.maneki.service.user.LoginService;
import com.subaru.maneki.service.user.UserService;
import com.subaru.maneki.vo.CartVO;
import com.subaru.common.vo.JsonVO;
import com.subaru.core.configure.ConfigResolver;
import com.subaru.core.configure.DefaultConfigResolver;

/**
 * @author zhangchaojie
 * @since 2016-08-26
 */
@Controller
public class TradeController {

    @Resource
    private LoginService           loginService;

    @Resource
    private AddressService         addressService;

    @Resource
    private CartManager            cartManager;

    @Resource
    private CartService            cartService;

    @Resource
    private UserService            detailedUserService;

    @Resource
    private PayService             stripePayService;

    @Resource
    private ShippingInfoService    shippingInfoService;

    @Resource
    private DeliveryService        deliveryService;

    @Resource
    private OrderService           orderService;

    @Resource
    private CountryStateDao        countryStateDao;

    @Resource
    private UserService            userService;

    @Resource
    private CountryStateService    countryStateService;

    @Resource
    private UserAddressRelationDao userAddressRelationDao;

    @Resource
    private CommonManager          commonService;

    private static Logger          logger   = LoggerFactory.getLogger(TradeController.class);

    private static String          ENV_MODE = "";

    static {
        ConfigResolver configResolver = new DefaultConfigResolver();
        try {
            Configuration configuration = configResolver.getAppConfig();
            ENV_MODE = configuration.getString("app.env");
        } catch (ConfigurationException e) {
            logger.error("", e);
        } catch (MalformedURLException e) {
            logger.error("", e);
        }
    }

    @RequestMapping(value = "/trade/confirm", method = RequestMethod.GET)
    public String confirmOrderPage(Model model, HttpServletRequest request) {

        if (!loginService.isLogin(request)) {
            String redirectUrl = "/trade/confirm";
            return "redirect:/auth/login?redirectUrl=" + redirectUrl;
        }

        User currentUser = loginService.getCurrentUser(request);

        //目前的逻辑是：当用户更新确认订单的地址信息时，更新后的地址信息就是默认地址
        List<Address> userAddresses = addressService.getUserAddresses(currentUser.getId());
        Address userAddress = null;
        for (Address address : userAddresses) {
            if (address.getIsDefault()) {
                userAddress = address;
                model.addAttribute("address", address);
                break;
            }
        }

        //获得国家和相关州省份的信息
        Set<String> countryAbbrList = DeliveryConstant.SHIPPING_CONSTANT.keySet();
        List<Integer> countryIdList = new ArrayList<>();
        List<Country> countryList = new ArrayList<>();
        String countryName = null;
        Country country = null;
        for (String abbr : countryAbbrList) {
            Country c = countryStateService.getCountryByAbbr(abbr.toUpperCase());
            countryIdList.add(c.getId());
            countryList.add(c);

            if (userAddress != null && c.getId() == userAddress.getCountryId()) {
                countryName = c.getName();
                model.addAttribute("countryName", countryName);
                model.addAttribute("currency", c.getCurrencyUnit());
                country = c;
            }
        }

        Map<Integer, List<CountryState>> countryStateMap = countryStateService
            .getCountryState(countryIdList);
        model.addAttribute("countryList", countryList);
        model.addAttribute("countryStateMap", countryStateMap);

        //如果用户地址是空，那么尝试根据cookie的内容来定位国家信息
        if (userAddress == null) {
            String abbr = commonService.getCountryCodeFromCookie(request);
            country = countryStateService.getCountryByAbbr(abbr);

            model.addAttribute("countryName", country.getName());
            model.addAttribute("showForm", true);
        } else {
            String stateName = null;
            List<CountryState> countryStateList = countryStateMap.get(userAddress.getCountryId());
            if (countryStateList != null){
	            for (CountryState countryState : countryStateList) {
	                if (countryState != null && (countryState.getId() == userAddress.getStateId())) {
	                    stateName = countryState.getName();
	
	                    model.addAttribute("stateName", stateName);
	                    break;
	                }
	            }
            }

            /*
            if (userAddress.getZip() != null){
            	detailStreet = userAddress.getStreet() + " " + userAddress.getCity()
            	+ " " + stateName + " " + userAddress.getZip() + " "  + countryName;
            }else if(userAddress.getCity() != null){
            	detailStreet = userAddress.getStreet() + " " + userAddress.getCity()
            	+ " " + stateName + " "  + countryName;
            }
            
            model.addAttribute("detailStreet", detailStreet);
            */
            model.addAttribute("showForm", false);
        }

        DetailedUser detailedUser = (DetailedUser) detailedUserService.get(currentUser.getId());
        model.addAttribute("detailedUser", detailedUser);

        List<CartVO> userCartVOList = cartManager.getUserCartVOs(currentUser.getId());

        //根据用户选择的国家，计算前端页面上换算后显示的金额信息
        String abbr = commonService.getCountryCodeFromCookie(request);
        Country chooseCountry = countryStateService.getCountryByAbbr(abbr);

        double cartTotalPrice = cartManager.getCartTotalPrice(currentUser.getId());
        double postFee = deliveryService.getPostFee(country.getId(), cartTotalPrice);

        cartTotalPrice = commonService.calculateCountryPrice(cartTotalPrice,
            chooseCountry.getRate());
        postFee = commonService.calculateCountryPrice(postFee, chooseCountry.getRate());
        for (CartVO cartVO : userCartVOList) {
            cartVO.transferPrice(chooseCountry.getRate());
        }
        model.addAttribute("userCartVOList", userCartVOList);
        model.addAttribute("cartTotalPrice", cartTotalPrice);
        model.addAttribute("postFee", postFee);
        model.addAttribute("currency", chooseCountry.getCurrencyUnit());

        return "page/trade/confirm";

    }

    /**
     * 当用户更新邮寄表单中的国家信息后，要获得计算当前购物车金额对应的邮费
     *
     * @param request
     * @param countryId
     * @param cartTotal
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/trade/update/shipping_info", method = RequestMethod.POST)
    public String updateShippingInfo(HttpServletRequest request, @RequestParam int countryId) {

        JsonVO json = new JsonVO();
        if (countryId <= 0) {
            json.setIsSuccess(0);
            return json.toString();
        }
        if (!loginService.isLogin(request)) {
            json.setIsSuccess(0);
            json.setIsRedirect(1);
            json.setRedirectURL("/auth/login?redirectUrl=/trade/confirm");
            return json.toString();
        }

        User currentUser = loginService.getCurrentUser(request);
        double cartTotalPrice = cartManager.getCartTotalPrice(currentUser.getId());

        String abbr = commonService.getCountryCodeFromCookie(request);
        double postFee = deliveryService.getPostFee(countryId, cartTotalPrice);

        //货币换算
        Country country = countryStateService.getCountryByAbbr(abbr);
        cartTotalPrice = commonService.calculateCountryPrice(cartTotalPrice, country.getRate());
        postFee = commonService.calculateCountryPrice(postFee, country.getRate());

        Map<String, Double> tmp = new HashMap<>();
        tmp.put("subtotal", cartTotalPrice);
        tmp.put("postFee", postFee);
        json.setMsg(JSON.toJSONString(tmp));
        json.setIsSuccess(1);

        return json.toString();
    }

    /**
     * 保存用户提交的邮寄信息
     * email\name\phone放在trade_shipping_info表
     * street\city\stateId\countryId\zip\isDefault放在address表
     * 当地址信息变化后，目前的操作是：在address表中插入新的一条记录，同时update表user_address_relation
     *
     * @param request
     * @param email
     * @param name
     * @param street
     * @param city
     * @param stateId
     * @param countryId
     * @param zip
     * @param phone
     * @param isDefault
     * @param addressId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/trade/save/shipping_info", method = RequestMethod.POST)
    public String saveShippingInfo(HttpServletRequest request, @RequestParam String email,
                                   @RequestParam String name, @RequestParam String street,
                                   @RequestParam String city, @RequestParam int stateId,
                                   @RequestParam int countryId,
                                   @RequestParam(required = false) String zip,
                                   @RequestParam String phone, @RequestParam boolean isDefault) {

        JsonVO json = new JsonVO();
        if (!loginService.isLogin(request)) {
            json.setIsSuccess(0);
            json.setIsRedirect(1);
            json.setRedirectURL("/auth/login?redirectUrl=/trade/confirm");
            return json.toString();
        }

        User currentUser = loginService.getCurrentUser(request);
        Address defaultAddress = null;
        List<Address> addressList = addressService.getUserAddresses(currentUser.getId());
        for (Address a : addressList) {
            if (a.getIsDefault() == true) {
                defaultAddress = a;
                break;
            }
        }
        boolean userChanged = true;

        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setReceiverEmail(email);
        shippingInfo.setReceiverName(name);
        shippingInfo.setReceiverPhone(phone);

        if (defaultAddress == null) {
            Address address = new Address();
            address.setCity(city);
            address.setCountryId(countryId);
            address.setIsDefault(true);
            address.setStateId(stateId);
            address.setStreet(street);
            address.setZip(zip);
            try {
                addressService.insert(address);
                //这一步最好放在AddressService中
                UserAddressRelation userAddressRelation = new UserAddressRelation();
                userAddressRelation.setAddressId(address.getId());
                userAddressRelation.setUserId(currentUser.getId());
                userAddressRelationDao.insert(userAddressRelation);

                shippingInfo.setAddressId(address.getId());

                json.setIsSuccess(1);
            } catch (UserAddressException uae) {
                json.setIsSuccess(0);
                json.setMsg(uae.getMessage());
            }

            //第一次填写用户信息时把电话号码信息插入到user_etc表中
            DetailedUser detailedUser = (DetailedUser) detailedUserService.get(currentUser.getId());
            if ((detailedUser.getEmail().equals(email)) && (detailedUser.getNick().equals(name))) {
                detailedUser.setCellphone(phone);
                detailedUserService.update(detailedUser);
            }
        } else {
            //判断地址是否有变化
            if (street.equals(defaultAddress.getStreet()) && city.equals(defaultAddress.getCity())
                && (stateId == defaultAddress.getStateId())
                && (countryId == defaultAddress.getCountryId())
                && (zip != null && zip.equals(defaultAddress.getZip()))) {
                userChanged = false;
            }
            if (userChanged) {
                try {
                    int newAddressId = addressService.update(defaultAddress.getId(),
                        currentUser.getId(), countryId, stateId, city, street, zip, isDefault);
                    shippingInfo.setAddressId(newAddressId);

                    json.setIsSuccess(1);
                } catch (UserAddressException uae) {
                    json.setIsSuccess(0);
                    json.setMsg(uae.getMessage());
                }
            } else {
                shippingInfo.setAddressId(defaultAddress.getId());

                json.setIsSuccess(1);
            }
        }

        if (json.getIsSuccess() == 1) {
            double cartTotalPrice = cartManager.getCartTotalPrice(currentUser.getId());
            double postFee = deliveryService.getPostFee(countryId, cartTotalPrice);
            shippingInfo.setPostFee(postFee);

            String method = deliveryService.getDeliveryMethod(countryId);
            shippingInfo.setDeliveryMethod(method);

            try {
                shippingInfoService.add(shippingInfo);
            } catch (TradeException te) {
                json.setIsSuccess(0);
                json.setMsg(te.getMessage());
                return json.toString();
            }

            json.setMsg(Integer.toString(shippingInfo.getId()));
        }

        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/trade/order/create", method = RequestMethod.POST)
    public String createOrder(HttpServletRequest request, @RequestParam int shippingId) {

        JsonVO json = new JsonVO();

        if (!loginService.isLogin(request)) {
            json.setIsSuccess(0);
            json.setIsRedirect(1);
            json.setRedirectURL("/auth/login?redirectUrl=/trade/confirm");
            return json.toString();
        }

        User currentUser = loginService.getCurrentUser(request);

        List<CartVO> userCartVOs = cartManager.getUserCartVOs(currentUser.getId());
        //TODO 增加SKU库存判断
        List<TradeProduct> tradeProductList = getTradeProducts(userCartVOs);

        try {
            long orderNumber = orderService.add(currentUser.getId(), shippingId, tradeProductList);
            json.setIsSuccess(1);
            json.setIsRedirect(1);
            json.setRedirectURL("/trade/paytype?orderNumber=" + orderNumber);
        } catch (Exception e) {
            json.setMsg(e.getMessage());
            json.setIsSuccess(0);
        }

        return json.toString();

    }

    private List<TradeProduct> getTradeProducts(List<CartVO> userCartVOs) {
        if (userCartVOs == null || userCartVOs.size() <= 0) {
            return null;
        }
        List<TradeProduct> tradeProductList = new ArrayList<>();
        for (CartVO cartVO : userCartVOs) {
            TradeProduct tradeProduct = new TradeProduct();
            tradeProduct.setSkuId(cartVO.getSkuId());
            tradeProduct.setQuantity(cartVO.getQuantity());
            tradeProduct.setProductName(cartVO.getProductName());
            tradeProduct.setImageUrl(cartVO.getImageUrl());
            tradeProduct.setOriginalPrice(cartVO.getPrice());
            /**
             * zhangchaojie
             * 目前站内无优惠工具，buyedPrice即OriginalPrice
             * 后续引入优惠工具，buyedPrice应为减去优惠后的价格
             */
            tradeProduct.setBuyedPrice(cartVO.getPrice());
            tradeProductList.add(tradeProduct);
        }
        return tradeProductList;
    }

    @RequestMapping(value = "/trade/paytype", method = RequestMethod.GET)
    public String payTypePage(HttpServletRequest request, Model model,
                              @RequestParam long orderNumber) throws TradeException {

        if (orderNumber <= 0) {
            throw new TradeException("Illegal orderNumber " + orderNumber);
        }

        Order order = orderService.get(orderNumber);
        if (order == null) {
            throw new TradeException("Cannot find any order by the given orderNumber "
                                     + orderNumber);
        }
        //        int addressId = order.getAddressId();
        int shippingId = order.getShippingId();
        ShippingInfo shippingInfo = shippingInfoService.get(shippingId);
        if (shippingInfo == null) {
            throw new TradeException("Cannot find any shippingInfo by the given orderNumber "
                                     + orderNumber);
        }
        int addressId = shippingInfo.getAddressId();
        if (addressId <= 0) {
            throw new TradeException("Illegal addressId " + addressId + "of order " + orderNumber);
        }
        Address address = addressService.get(addressId);
        Country country = countryStateService.getCountry(address.getCountryId());
        CountryState countryState = countryStateDao.select(address.getStateId());

        model.addAttribute("order", order);
        model.addAttribute("address", address);
        model.addAttribute("shippingInfo", shippingInfo);
        model.addAttribute("countryName", country.getName());
        model.addAttribute("stateName", countryState.getName());

        //根据用户选择的国家，计算前端页面上换算后显示的金额信息
        String abbr = commonService.getCountryCodeFromCookie(request);
        Country chooseCountry = countryStateService.getCountryByAbbr(abbr);
        model.addAttribute("currency", chooseCountry.getCurrencyUnit());
        model.addAttribute("localAmountTotal",
            commonService.calculateCountryPrice(order.getAmount(), chooseCountry.getRate()));

        return "page/trade/paytype";

    }

    @RequestMapping(value = "/trade/stripe/card_info", method = RequestMethod.GET)
    public String cardInfoPage(HttpServletRequest request, Model model,
                               @RequestParam long orderNumber) {

        String paymentPublishableKey = "";
        if (ENV_MODE.equals("release")) {
            paymentPublishableKey = StripeConfig.PUBLISHABLE_KEY_LIVE;
        } else {
            paymentPublishableKey = StripeConfig.PUBLISHABLE_KEY_TEST;
        }

        Order order = orderService.get(orderNumber);

        model.addAttribute("order", order);
        model.addAttribute("payment_publishable_key", paymentPublishableKey);

        //根据用户选择的国家，计算前端页面上换算后显示的金额信息
        String abbr = commonService.getCountryCodeFromCookie(request);
        Country chooseCountry = countryStateService.getCountryByAbbr(abbr);
        model.addAttribute("currency", chooseCountry.getCurrencyUnit());
        model.addAttribute("localAmountTotal",
            commonService.calculateCountryPrice(order.getAmount(), chooseCountry.getRate()));

        model.addAttribute("isUs", (abbr.equals("US")));

        return "page/trade/card_info";

    }

    @RequestMapping(value = "/trade/moneygram", method = RequestMethod.GET)
    public String moneygramPay(HttpServletRequest request, Model model,
                               @RequestParam long orderNumber) throws TradeException {

        Order order = orderService.get(orderNumber);
        if (order == null) {
            throw new TradeException("Illegal orderNumber " + orderNumber);
        }
        //zhangchaojie: 更新订单支付方式为COD
        order.setPayType(PayType.COD.getValue());
        orderService.update(order);

        int shippingId = order.getShippingId();
        ShippingInfo shippingInfo = shippingInfoService.get(shippingId);
        if (shippingInfo == null) {
            throw new TradeException("Cannot find any order by the given orderNumber "
                                     + orderNumber);
        }
        int addressId = shippingInfo.getAddressId();
        if (addressId <= 0) {
            throw new TradeException("Illegal addressId " + addressId + "of order " + orderNumber);
        }
        Address address = addressService.get(addressId);
        Country deliveryCountry = countryStateService.getCountry(address.getCountryId());

        model.addAttribute("method", "MoneyGram");
        model.addAttribute("order", order);

        //根据用户选择的国家，计算前端页面上换算后显示的金额信息
        String abbr = commonService.getCountryCodeFromCookie(request);
        Country chooseCountry = countryStateService.getCountryByAbbr(abbr);
        model.addAttribute("currency", chooseCountry.getCurrencyUnit());
        model.addAttribute("amountTotal",
            commonService.calculateCountryPrice(order.getAmount(), chooseCountry.getRate()));

        String shippingTime = DeliveryConstant.SHIPPING_CONSTANT.get(deliveryCountry.getAbbr())
            .getShippingTime();
        model.addAttribute("shippingTime", shippingTime);

        return "page/trade/moneygram";
    }

    @ResponseBody
    @RequestMapping(value = "/api/trade/pay/stripe/ipn", method = RequestMethod.POST)
    public String pay(@RequestParam String tokenId, @RequestParam long orderNumber) {

        JsonVO json = new JsonVO();
        if (orderNumber <= 0) {
            json.setMsg("Invalid orderNumber !!");
            json.setIsSuccess(0);
            return json.toString();
        }

        Order order = orderService.get(orderNumber);
        if (order == null || order.getUserId() <= 0 || order.getAmount() < 0) {
            json.setMsg("Illegal order info, please check this order !! The orderNumber is "
                        + orderNumber);
            json.setIsSuccess(0);
            return json.toString();
        }

        double amount = order.getAmount();
        if (amount <= 0) {
            json.setMsg("The amount of order cannot be less than 0 !!");
            json.setIsSuccess(0);
            return json.toString();
        }

        //zhangchaojie：将美元换算成美分，stripe中的结算以美分为单位，且为整型
        int amountOfCent = (int) (amount * 100);

        try {
            StripePayParam param = new StripePayParam(orderNumber, tokenId, amountOfCent,
                order.getCurrency(), orderNumber + "");
            stripePayService.pay(param);
            order.setTradeStatus(TradeStatus.WAIT_FOR_THE_DELIVER.getStatusCode());
            order.setPayType(PayType.STRIPE.getValue());
            order.setGmtPaied(new Timestamp(System.currentTimeMillis()));
            orderService.update(order);
            json.setRedirectURL("/trade/pay_success?orderNumber=" + orderNumber);
            json.setIsRedirect(1);
            json.setIsSuccess(1);
        } catch (Exception e) {
            logger.error("", e);
            json.setMsg(e.getMessage());
            json.setIsRedirect(0);
            json.setIsSuccess(0);
        }
        return json.toString();

    }

    @RequestMapping(value = "/trade/pay_success", method = RequestMethod.GET)
    public String stripePaySuccess(HttpServletRequest request, Model model, long orderNumber)
                                                                                             throws TradeException {

        if (orderNumber <= 0) {
            throw new TradeException("Cannot show paySuccess page, error orderNumber !!");
        }

        Order order = orderService.get(orderNumber);
        model.addAttribute("order", order);

        // zhangchaojie: 交易成功，移除用户购物车信息
        int userId = order.getUserId();
        if (userId <= 0) {
            throw new TradeException(
                "Cannot show paySuccess page, the userId of order is less than 0 !! OrderNumber:"
                        + orderNumber);
        }
        cartService.removeUserCarts(userId);

        int shippingId = order.getShippingId();
        ShippingInfo shippingInfo = shippingInfoService.get(shippingId);
        if (shippingInfo == null) {
            throw new TradeException("Cannot find any shippingInfo by the given orderNumber "
                                     + orderNumber);
        }
        int addressId = shippingInfo.getAddressId();
        //在model中添加邮寄相关的信息
        if (addressId > 0) {
            Address address = addressService.get(addressId);

            Country deliveryCountry = countryStateService.getCountry(address.getCountryId());
            CountryState countryState = countryStateDao.select(address.getStateId());

            model.addAttribute("address", address);
            model.addAttribute("countryName", deliveryCountry.getName());
            model.addAttribute("stateName", countryState.getName());

            String shippingMethod = DeliveryConstant.SHIPPING_CONSTANT.get(
                deliveryCountry.getAbbr()).getShippingMethod();
            String shippingTime = DeliveryConstant.SHIPPING_CONSTANT.get(deliveryCountry.getAbbr())
                .getShippingTime();

            model.addAttribute("shippingMethod", shippingMethod);
            if (shippingTime != null) {
                model.addAttribute("shippingTime", shippingTime);
            } else {
                model.addAttribute("shippingTime", "7-12 days");
            }

            //预估送达的时间
            int prepareDay = 4;
            int deltaDay = Integer.parseInt(shippingTime.split(" ")[0].split("-")[0]) + prepareDay;

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, deltaDay);

            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            model.addAttribute("arriveDate", formatter.format(cal.getTime()));
        }

        //根据用户选择的国家，计算前端页面上换算后显示的金额信息
        String abbr = commonService.getCountryCodeFromCookie(request);
        Country chooseCountry = countryStateService.getCountryByAbbr(abbr);
        model.addAttribute("currency", chooseCountry.getCurrencyUnit());
        model.addAttribute("amountTotal",
            commonService.calculateCountryPrice(order.getAmount(), chooseCountry.getRate()));

        model.addAttribute("shippingInfo", shippingInfo);

        return "page/trade/payment_complete";
    }
}
