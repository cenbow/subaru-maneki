package com.subaru.maneki.service.trade.imp;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.subaru.maneki.bo.PayParam;
import com.subaru.maneki.bo.StripePayParam;
import com.subaru.maneki.config.StripeConfig;
import com.subaru.maneki.exception.TradeException;
import com.subaru.maneki.service.trade.PayService;
import com.subaru.core.configure.ConfigResolver;
import com.subaru.core.configure.DefaultConfigResolver;

/**
 * @author zhangchaojie
 * @since 2016-08-31
 */
@Service("stripePayService")
public class StripePayServiceImp implements PayService {

    private static Logger logger   = LoggerFactory.getLogger(StripePayServiceImp.class);

    private static String ENV_MODE = "";

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

    @Override
    public void pay(PayParam payParam) throws TradeException {

        // Set your secret key: remember to change this to your live secret key in production
        // See your keys here: https://dashboard.stripe.com/account/apikeys
        //        Stripe.apiKey = "sk_test_EuRkcrUp89ZyEH1ZQFmYl163";
        if (ENV_MODE.equals("release")) {
            Stripe.apiKey = StripeConfig.SECRET_KEY_LIVE;
        } else {
            Stripe.apiKey = StripeConfig.SECRET_KEY_TEST;
        }

        // Get the credit card details submitted by the forms
        //        String token = request.getParameter("stripeToken");
        if (!(payParam instanceof StripePayParam)) {
            throw new TradeException(
                "Class convert exception, need stripePayParam but the payParam is not !!");
        }
        StripePayParam stripePayParam = (StripePayParam) payParam;
        String token = stripePayParam.getToken();

        // Create a charge: this will charge the user's card
        try {
            Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("amount", stripePayParam.getAmount()); // Amount in cents
            //            chargeParams.put("amount", 1.00); //test charge failed
            chargeParams.put("currency", stripePayParam.getCurrency());
            chargeParams.put("source", token);
            chargeParams.put("description", stripePayParam.getDescription());

            Charge charge = Charge.create(chargeParams);
            logger.info("Charging order " + stripePayParam.getOrderNumber()
                        + "success !! The chargeId is :" + charge.getId());
        } catch (CardException e) {
            // Since it's a decline, CardException will be caught
            logger.info("Payment is failed with CardException, the orderNumber is : "
                        + stripePayParam.getOrderNumber() + ", the status is: " + e.getCode()
                        + ", and the message is :" + e.getMessage());
            throw new TradeException(e.getMessage());
        } catch (RateLimitException e) {
            // Too many requests made to the API too quickly
            logger
                .info("Payment is failed with RateLimitException, too many requests made to the API too quickly...The orderNumber is :"
                      + stripePayParam.getOrderNumber()
                      + ", remaining exception message as follow : " + e.getMessage());
            throw new TradeException(e.getMessage());
        } catch (InvalidRequestException e) {
            // Invalid parameters were supplied to Stripe's API
            logger
                .info("Payment is failed with InvalidRequestException, invalid parameters were supplied to Stripe's API...The orderNumber is :"
                      + stripePayParam.getOrderNumber()
                      + ", remaining exception message as follow : " + e.getMessage());
            throw new TradeException(e.getMessage());
        } catch (AuthenticationException e) {
            // Authentication with Stripe's API failed
            // (maybe you changed API keys recently)
            logger
                .info("Payment is failed with AuthenticationException, authentication with Stripe's API failed, maybe you changed API keys recently...The orderNumber is : "
                      + stripePayParam.getOrderNumber()
                      + ", remaining exception message as follow : " + e.getMessage());
            throw new TradeException(e.getMessage());
        } catch (APIConnectionException e) {
            // Network communication with Stripe failed
            logger
                .info("Payment is failed with APIConnectionException, network communication with Stripe failed... he orderNumber is :"
                      + stripePayParam.getOrderNumber()
                      + ", remaining exception message as follow : " + e.getMessage());
            throw new TradeException(e.getMessage());
        } catch (StripeException e) {
            // Display a very generic error to the user, and maybe send
            // yourself an email
            logger.info("Payment is failed with StripeException, the orderNumber is :"
                        + stripePayParam.getOrderNumber()
                        + ", remaining exception message as follow : " + e.getMessage());
            throw new TradeException(e.getMessage());
        } catch (Exception e) {
            // Something else happened, completely unrelated to Stripe
            logger.info("Payment is failed with StripeException, the orderNumber is "
                        + stripePayParam.getOrderNumber()
                        + ", remaining exception message as follow : " + e.getMessage());
            throw new TradeException(e.getMessage());
        }

    }

}
