package com.subaru.maneki.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.subaru.maneki.enumeration.LoginType;
import com.subaru.maneki.exception.UserLoginException;
import com.subaru.maneki.manager.CommonManager;
import com.subaru.maneki.model.User;
import com.subaru.maneki.service.user.LoginService;

/**
 * @author zhangchaojie
 * @since 2016-08-08
 */
@Controller
public class LoginController {

    @Resource
    LoginService loginService;
    
    @Resource
    private CommonManager	commonService;

    @RequestMapping(value = "/auth/login", method = RequestMethod.GET)
    public String authLoginPage(Model model,
                                HttpServletRequest request,
                                @RequestParam(required = false, defaultValue = "/user/user_center") String redirectUrl) {
        if (loginService.isLogin(request)) {
            return "redirect:" + redirectUrl;
        }
        model.addAttribute("redirectUrl", redirectUrl);
        return "page/auth/login";
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public String login(Model model,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam String email,
                        @RequestParam String password,
                        @RequestParam(required = false, defaultValue = "1") int loginType,
                        @RequestParam(required = false, defaultValue = "/user/user_center") String redirectUrl) {
        String token = null;
        try {
            token = loginService.login(request, response, email, password,
                LoginType.getType(loginType));
        } catch (UserLoginException e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        User currentUser = loginService.getCurrentUser(request);
        if (StringUtils.isBlank(token) || currentUser == null) {
            return "page/auth/login";
        }
        return "redirect:" + redirectUrl;
    }

    @ResponseBody
    @RequestMapping(value = "/auth/oauth_login", method = RequestMethod.POST)
    public String oauthLogin(HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestParam String accessToken,
                             @RequestParam(required = false, defaultValue = "2") int loginType,
                             @RequestParam(required = false, defaultValue = "/user/user_center") String redirectUrl) {
        String token = null;
        Map<String, String> result = new HashMap<String, String>();  
        try {
        	System.out.println(accessToken);
            token = loginService.oauthLogin(request, response, accessToken,
                LoginType.getType(loginType));
        } catch (UserLoginException e) {
        	System.out.println(e);;
            result.put("message", e.getMessage().toString());
            result.put("result", "Fail");
            
            return JSON.toJSONString(result);
        }
        User currentUser = loginService.getCurrentUser(request);
        if (StringUtils.isBlank(token) || currentUser == null) {
            result.put("result", "Redirect");
            result.put("redirectUrl", "/auth/login");
        }else{
        	result.put("result", "Success");
            result.put("redirectUrl", redirectUrl);
        }
        System.out.println(JSON.toJSONString(result));

        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/auth/logout", method = RequestMethod.GET)
    public String logout(Model model, HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(required = false, defaultValue = "/") String redirectUrl) {

        loginService.logout(request, response);
        return "redirect:" + redirectUrl;

    }
    
    @RequestMapping(value = "/auth/reset_password", method = RequestMethod.GET)
    public String resetPasswordPage(Model model, HttpServletRequest request){
    	String token = commonService.getCookieValue(request, "token");
    	if (token == null){
    		return "page/auth/reset_password";
    	}else{
    		return "page/auth/reset_password_with_token";
    	}
    }

}
