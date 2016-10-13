package com.subaru.maneki.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.subaru.maneki.service.trade.CountryStateService;
import com.subaru.maneki.service.user.AddressService;
import com.subaru.maneki.service.user.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.subaru.maneki.cons.DeliveryConstant;
import com.subaru.maneki.dao.CountryDao;
import com.subaru.maneki.dao.UserAddressRelationDao;
import com.subaru.maneki.exception.UserAddressException;
import com.subaru.maneki.manager.CommonManager;
import com.subaru.maneki.model.Address;
import com.subaru.maneki.model.Country;
import com.subaru.maneki.model.CountryState;
import com.subaru.maneki.model.DetailedUser;
import com.subaru.maneki.model.User;
import com.subaru.maneki.model.UserAddressRelation;
import com.subaru.maneki.service.user.UserService;
import com.subaru.common.vo.JsonVO;

import org.springframework.ui.Model;

/**
 * @author zhangchaojie
 * @since 2016-08-11
 */
@Controller
public class UserController {

    @Resource
    private UserService  detailedUserService;
    
    @Resource
    private LoginService loginService;
    
    @Resource
    private CommonManager	commonService;
    
    @Resource
    private AddressService	addressService;
    
    @Resource
    private CountryStateService	countryStateService;
    
    @Resource
    private CountryDao	 countryDao;
    
    @Resource
    private UserAddressRelationDao	userAddressRelationDao;

    @RequestMapping(value = "/user/user_center", method = RequestMethod.GET)
    public String userCenterPage(Model model, HttpServletRequest request) {
        if (!loginService.isLogin(request)) {
            //未登录，重定向至登录页面
            String redirectUrl = "/user/user_center";
            return "redirect:/auth/login?redirectUrl=" + redirectUrl;
        }
        User user = loginService.getCurrentUser(request);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("nick", user.getNick());

        return "page/user_center/user_center";
    }

    @RequestMapping(value = "/info/shipping", method = RequestMethod.GET)
    public String shippingPage(Model model, HttpServletRequest request) {
    	
    	String abbr = commonService.getCountryCodeFromCookie(request);
    	Country country = countryDao.selectByAbbr(abbr);
    	
    	model.addAttribute("country", country);
    	model.addAttribute("shipping_method", DeliveryConstant.SHIPPING_CONSTANT.get(abbr).getShippingMethod());
    	model.addAttribute("shipping_time", DeliveryConstant.SHIPPING_CONSTANT.get(abbr).getShippingTime());
        return "page/user_center/shipping";
    }

    @RequestMapping(value = "/info/return", method = RequestMethod.GET)
    public String returnPage(Model model) {
        return "page/user_center/return";
    }

    @RequestMapping(value = "/info/about_us", method = RequestMethod.GET)
    public String aboutUsPage(Model model) {
        return "page/user_center/about_us";
    }

    @RequestMapping(value = "/info/private_policy", method = RequestMethod.GET)
    public String privatePolicyPage(Model model) {
        return "page/user_center/private_policy";
    }

    @RequestMapping(value = "/info/contact_info", method = RequestMethod.GET)
    public String contactInfoPage(Model model, HttpServletRequest request) {
    	if (!loginService.isLogin(request)) {
            //未登录，重定向至登录页面
            String redirectUrl = "/user/user_center";
            return "redirect:/auth/login?redirectUrl=" + redirectUrl;
        }
    	
    	User user = loginService.getCurrentUser(request);
    	DetailedUser detailedUser = (DetailedUser) detailedUserService.get(user.getId());
    	
    	List<Address> userAddresses = addressService.getUserAddresses(user.getId());
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
        for (String abbr : countryAbbrList) {
            Country c = countryStateService.getCountryByAbbr(abbr.toUpperCase());
            countryIdList.add(c.getId());
            countryList.add(c);
        }

        Map<Integer, List<CountryState>> countryStateMap = countryStateService.getCountryState(countryIdList);
        model.addAttribute("countryList", countryList);
        model.addAttribute("countryStateMap", countryStateMap);
    	
    	model.addAttribute("detailedUser", detailedUser);
    	model.addAttribute("address", userAddress);
        return "page/user_center/contact_info";
    }
    
    /**
     * 修改地址表中is_default字段为true的行
     * @param email
     * @param name
     * @param street
     * @param city
     * @param stateId
     * @param countryId
     * @param zip
     * @param phone
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/user/update/contact_info", method = RequestMethod.POST)
    public String updateContactInfo(
    		Model model, 
    		HttpServletRequest request,
    		@RequestParam String email,
            @RequestParam String name, @RequestParam String street,
            @RequestParam String city, @RequestParam int stateId,
            @RequestParam int countryId, @RequestParam(required = false) String zip,
            @RequestParam String phone) {
    	
    	JsonVO json = new JsonVO();
        if (!loginService.isLogin(request)) {
            json.setIsSuccess(0);
            json.setIsRedirect(1);
            json.setRedirectURL("/auth/login?redirectUrl=/info/contact_info");
            return json.toString();
        }

        User user = loginService.getCurrentUser(request);
        DetailedUser detailedUser = (DetailedUser) detailedUserService.get(user.getId());
        boolean isChanged = true;
        //先更新detail user的信息
        try{
        	if (detailedUser.getCellphone().equals(phone) == true && 
        			detailedUser.getCountryId() == countryId &&
        			detailedUser.getEmail().equals(email) == true &&
        			detailedUser.getNick().equals(name) == true){
        		isChanged = false;
        	}
        	if (isChanged == true){
		        detailedUser.setCellphone(phone);
		        detailedUser.setCountryId(countryId);
		        detailedUser.setEmail(email);
		        detailedUser.setNick(name);
		        detailedUserService.update(detailedUser);
        	}
        }catch(Exception e){
        	json.setIsSuccess(0);
            json.setMsg(e.getMessage());
            
            return json.toString();
        }
        
        Address defaultAddress = null;
        List<Address> addressList = addressService.getUserAddresses(user.getId());
        for (Address a : addressList) {
            if (a.getIsDefault() == true) {
                defaultAddress = a;
                break;
            }
        }
        
        //再添加或者更新地址信息
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
                userAddressRelation.setUserId(user.getId());
                userAddressRelationDao.insert(userAddressRelation);

                json.setIsSuccess(1);
            } catch (UserAddressException uae) {
                json.setIsSuccess(0);
                json.setMsg(uae.getMessage());
            }
        }else{
        	//如果用户地址信息没有变化，那么不更新
        	isChanged = true;
        	if (street.equals(defaultAddress.getStreet()) 
            		&& city.equals(defaultAddress.getCity())
                    && (stateId == defaultAddress.getStateId()) 
                    && (countryId == defaultAddress.getCountryId())) {
                isChanged = false;
            }
        	if (zip != null && false == zip.equals(defaultAddress.getZip())){
        		isChanged = true;
        	}
        	if (isChanged == true){
	        	try {
	                addressService.update(defaultAddress.getId(), user.getId(),
	                        countryId, stateId, city, street, zip, true);
	
	                json.setIsSuccess(1);
	            } catch (UserAddressException uae) {
	                json.setIsSuccess(0);
	                json.setMsg(uae.getMessage());
	            }
        	}else{
        		json.setIsSuccess(1);
        	}
        }
        
        return json.toString();
    }
}
