package com.subaru.maneki.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.subaru.maneki.exception.CartException;
import com.subaru.maneki.manager.CartManager;
import com.subaru.maneki.manager.CommonManager;
import com.subaru.maneki.model.Country;
import com.subaru.maneki.model.User;
import com.subaru.maneki.service.cart.CartService;
import com.subaru.maneki.service.trade.CountryStateService;
import com.subaru.maneki.service.trade.DeliveryService;
import com.subaru.maneki.service.user.LoginService;
import com.subaru.maneki.service.user.UserService;
import com.subaru.maneki.vo.CartVO;
import com.subaru.common.vo.JsonVO;

/**
 * @author zhangchaojie
 * @since 2016-08-08
 */
@Controller
public class CartController {

    @Resource
    private LoginService    loginService;

    @Resource
    private UserService     detailedUserService;

    @Resource
    private DeliveryService deliveryService;

    @Resource
    private CartManager     cartManager;

    @Resource
    private CartService     cartService;
    
    @Resource
    private CountryStateService countryStateService;
    
    @Resource
    private CommonManager  commonService;
    
    @RequestMapping(value = "/cart/my_cart", method = RequestMethod.GET)
    public String myCartPage(Model model, HttpServletRequest request) {

        if (!loginService.isLogin(request)) {
            String redirectUrl = "/cart/my_cart";
            return "redirect:/auth/login?redirectUrl=" + redirectUrl;
        }

        User currentUser = loginService.getCurrentUser(request);

        List<CartVO> userCartVOs = cartManager.getUserCartVOs(currentUser.getId());

        //DetailedUser detailedUser = (DetailedUser) detailedUserService.get(currentUser.getId());

        double actualPrice = 0;
        actualPrice = cartManager.getCartTotalPrice(currentUser.getId());

        //根据cookie中获得国家信息，后进行汇率换算
        String abbr = commonService.getCountryCodeFromCookie(request);
        Country country = countryStateService.getCountryByAbbr(abbr);
        double deliveryFee = country.getDeliveryFee();
        
        double amountToFreeDelivery = deliveryService.getAmountToFreeDelivery(
        		country.getId(), actualPrice);

        actualPrice = commonService.calculateCountryPrice(actualPrice, country.getRate());
        amountToFreeDelivery = commonService.calculateCountryPrice(amountToFreeDelivery, country.getRate());
        
        if(userCartVOs != null){
	        for(CartVO cartVO : userCartVOs){
	        	cartVO.transferPrice(country.getRate());
	        }
        }
        
        model.addAttribute("cartList", userCartVOs);
        //运费向上取整，这是目前Python的目前
        model.addAttribute("postFee", (int)(Math.ceil(commonService.calculateCountryPrice(deliveryFee, country.getRate()))));
        model.addAttribute("amountToFreeDelivery", amountToFreeDelivery);
        model.addAttribute("actualPrice", actualPrice);
        model.addAttribute("currency", country.getCurrencyUnit());

        return "page/cart/my_cart";

    }

    @ResponseBody
    @RequestMapping(value = "/cart/add", method = RequestMethod.GET)
    public String addTradeCart(HttpServletRequest request, @RequestParam int skuId,
                               @RequestParam int quantity) {

        JsonVO json = new JsonVO();

        if (skuId <= 0 || quantity <= 0) {
            json.setMsg("Invalid skuId or quantity !!");
            json.setIsSuccess(0);
            return json.toString();
        }

        if (!loginService.isLogin(request)) {
              //String redirectUrl = "/cart/add?skuId=" + skuId + "&quantity=" + quantity;
            String redirectUrl = "/auth/login";
            json.setRedirectURL(redirectUrl);
            json.setIsRedirect(1);
            json.setIsSuccess(0);
            return json.toString();
        }

        User currentUser = loginService.getCurrentUser(request);

        try {
            cartService.add(currentUser.getId(), skuId, quantity);
            json.setIsSuccess(1);
        } catch (CartException e) {
            json.setMsg(e.getMessage());
            json.setIsSuccess(0);
        }
        json.setIsSuccess(1);

        return json.toString();

    }
    
    @ResponseBody
    @RequestMapping(value = "/cart/cart_total", method = RequestMethod.GET)
    public String getCartTotalPrice(HttpServletRequest request){
    	JsonVO json = new JsonVO();
    	
    	Map<String, Double> priceMap = new HashMap<>();
    	
    	//如果用户在获取最新总额数据时未登陆，那么让用户先登陆后再直接返回cart页面
    	if (!loginService.isLogin(request)) {
            String redirectUrl = "/cart/my_cart";
            return "redirect:/auth/login?redirectUrl=" + redirectUrl;
        }
    	
    	User currentUser = loginService.getCurrentUser(request);
        
    	double actualPrice = 0;
        actualPrice = cartManager.getCartTotalPrice(currentUser.getId());
        
        String abbr = commonService.getCountryCodeFromCookie(request);
        Country country = countryStateService.getCountryByAbbr(abbr);
        double priceLeft = deliveryService.getAmountToFreeDelivery(
        		country.getId(), actualPrice);
        
        //汇率换算
        actualPrice = commonService.calculateCountryPrice(actualPrice, country.getRate());
        priceLeft = commonService.calculateCountryPrice(priceLeft, country.getRate());
        
        priceMap.put("actualPrice", actualPrice);
        priceMap.put("priceLeft", priceLeft);
        
        json.setIsSuccess(1);
        json.setData(JSON.toJSONString(priceMap));
        
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/cart/update", method = RequestMethod.GET)
    public String updateTradeCart(HttpServletRequest request, @RequestParam int skuId,
                                  @RequestParam int quantity) {

        JsonVO json = new JsonVO();

        if (!loginService.isLogin(request)) {
            json.setMsg("Please login first !!");
            json.setIsSuccess(0);
            return json.toString();
        }

        User currentUser = loginService.getCurrentUser(request);

        try {
            cartService.updateQuantity(currentUser.getId(), skuId, quantity);
            json.setIsSuccess(1);
        } catch (CartException e) {
            json.setMsg(e.getMessage());
            json.setIsSuccess(0);
        }

        return json.toString();

    }

    @ResponseBody
    @RequestMapping(value = "/cart/delete", method = RequestMethod.GET)
    public String removeTradeCart(HttpServletRequest request, @RequestParam int cartId) {

        JsonVO json = new JsonVO();

        if (!loginService.isLogin(request)) {
            json.setMsg("Please login first !!");
            json.setIsSuccess(0);
            return json.toString();
        }

        User currentUser = loginService.getCurrentUser(request);

        try {
            cartService.remove(cartId);
            json.setIsSuccess(1);
        } catch (CartException e) {
            json.setMsg(e.getMessage());
            json.setIsSuccess(0);
        }

        return json.toString();

    }

    @ResponseBody
    @RequestMapping(value = "/cart/count", method = RequestMethod.GET)
    public String countTradeCart(HttpServletRequest request) {

        JsonVO json = new JsonVO();

        if (!loginService.isLogin(request)) {
            json.setMsg("Please login first !!");
            json.setIsSuccess(0);
            return json.toString();
        }

        User currentUser = loginService.getCurrentUser(request);

        int skuNum = 0;
        skuNum = cartService.countUserCart(currentUser.getId());
        json.setData(skuNum);
        json.setIsSuccess(1);

        return json.toString();

    }
}
