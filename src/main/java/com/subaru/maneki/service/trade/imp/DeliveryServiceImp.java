package com.subaru.maneki.service.trade.imp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.cons.DeliveryConstant;
import com.subaru.maneki.dao.CountryDao;
import com.subaru.maneki.model.Country;
import com.subaru.maneki.service.trade.DeliveryService;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
@Service("deliveryService")
public class DeliveryServiceImp implements DeliveryService {

    @Resource
    private CountryDao countryDao;

    @Override
    public double getPostFee(int countryId, double cartTotalPrice) {

        if (countryId <= 0 || cartTotalPrice < 0) {
            return 0;
        }
        Country country = countryDao.select(countryId);
        if (country == null) {
            return 0;
        }
        double dollarThreshold = country.getDollarThreshold();
        if (cartTotalPrice >= dollarThreshold) {
            return 0;
        } else {
            return country.getDeliveryFee();
        }
    }

    @Override
    public double getAmountToFreeDelivery(int countryId, double cartTotalPrice) {
        if (countryId <= 0 || cartTotalPrice < 0) {
            return 0;
        }
        Country country = countryDao.select(countryId);
        if (country == null) {
            return 0;
        }
        double dollarThreshold = country.getDollarThreshold();
        if (cartTotalPrice >= dollarThreshold) {
            return 0;
        } else {
            return (dollarThreshold - cartTotalPrice);
        }
    }

    @Override
    public String getDeliveryMethod(int countryId){
    	String defaultMethod = DeliveryConstant.SHIPPING_CONSTANT.get("US").getShippingMethod();
    	if (countryId <= 0) {
            return defaultMethod;
        }
    	
    	Country country = countryDao.select(countryId);
    	if (country == null) {
            return defaultMethod;
        }
    	
    	String method = DeliveryConstant.SHIPPING_CONSTANT.get(country.getAbbr()).getShippingMethod();
    	if (method == null){
    		method = defaultMethod;
    	}
    	
    	return method;
    }
}
