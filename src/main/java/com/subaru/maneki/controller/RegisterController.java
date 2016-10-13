package com.subaru.maneki.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subaru.maneki.enumeration.LoginType;
import com.subaru.maneki.exception.UserLoginException;
import com.subaru.maneki.service.user.LoginService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.subaru.maneki.enumeration.RegisterType;
import com.subaru.maneki.exception.UserRegisterException;
import com.subaru.maneki.model.User;
import com.subaru.maneki.service.user.RegisterService;
import com.subaru.maneki.service.user.UserService;
import com.subaru.common.util.VerifyUtil;

/**
 * @author zhangchaojie
 * @since 2016-08-08
 */
@Controller
public class RegisterController {

    @Resource
    private UserService     userService;

    @Resource
    private RegisterService registerService;

    @Resource
    private LoginService    loginService;

    private Logger          logger = LoggerFactory.getLogger(RegisterController.class);

    @RequestMapping(value = "/auth/signup", method = RequestMethod.GET)
    public String authSignupPage(Model model) {

        return "page/auth/signup";

    }

    //@CheckToken(resetToken = true)
    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST)
    public String register(Model model,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           @RequestParam String email,
                           @RequestParam String nick,
                           @RequestParam String password,
                           @RequestParam(required = false, defaultValue = "/user/user_center") String redirectUrl) {

        String errorMsg = "";

        if (StringUtils.isBlank(email)) {
            errorMsg = "Please input your email !!";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "Please input your password !!";
        } else if (password.length() < 6 || password.length() > 16) {
            errorMsg = "The length of password should be between 6 and 16 chars !!";
        } else if (StringUtils.isBlank(nick)) {
            errorMsg = "Please input your name !!";
        }

        RegisterType registerType = null;
        if (VerifyUtil.checkEmail(email)) {
            registerType = RegisterType.EMAIL;
        } else {
            errorMsg = "Invalid Email !!";
        }
        User user = userService.getByEmail(email);
        if (user != null) {
            errorMsg = "This email has been signed up, please choose another email instead !!";
        }

        if (StringUtils.isNotBlank(errorMsg)) {

            model.addAttribute("errorMsg", errorMsg);

            return "page/auth/signup";
        }

        int newUserId = -1;

        try {
            newUserId = registerService.register(email, nick, password, registerType);
        } catch (UserRegisterException e) {
            errorMsg = e.getMessage();
        }

        if (newUserId <= 0) {
            errorMsg = "The service of registration is currently not available, please try again !!";
        }

        if (StringUtils.isNotBlank(errorMsg)) {

            model.addAttribute("errorMsg", errorMsg);

            return "page/auth/signup";
        }

        try {
            loginService.login(request, response, email, password,
                LoginType.getType(registerType.getValue()));
        } catch (UserLoginException e) {
            errorMsg = e.getMessage();
        }

        if (StringUtils.isNotBlank(errorMsg)) {

            model.addAttribute("errorMsg", errorMsg);

            return "page/auth/login";
        }

        /**
         * zhangchaojie
         * 登录成功跳往用户中心
         */
        redirectUrl = redirectUrl + "?userId=" + newUserId;
        return "redirect:" + redirectUrl;

    }

}
