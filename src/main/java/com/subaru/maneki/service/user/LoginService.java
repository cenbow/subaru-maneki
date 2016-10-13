package com.subaru.maneki.service.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subaru.maneki.enumeration.LoginType;
import com.subaru.maneki.exception.UserLoginException;
import com.subaru.maneki.model.User;

/**
 * 登录服务接口
 * @author zhangchaojie
 * @since 2016-08-11
 */
public interface LoginService {

    /**
     * zhangchaojie
     * 账号密码登录，账号可涵盖邮箱、手机号(需拓展实现)等
     * @param request
     * @param response
     * @param accountName
     * @param password
     * @param loginType
     * @return
     * @throws UserLoginException
     */
    public String login(HttpServletRequest request, HttpServletResponse response,
                        String accountName, String password, LoginType loginType)
                                                                                 throws UserLoginException;

    /**
     * zhangchaojie
     * 判断当前用户是否已登录，登录状态是否有效
     * @return true or false
     */
    public boolean isLogin(HttpServletRequest request);

    /**
     * zhangchaojie
     * 联合登录
     * @param request
     * @param response
     * @param accessToken
     * @param loginType
     * @return loginToken
     * @throws UserLoginException
     */
    public String oauthLogin(HttpServletRequest request, HttpServletResponse response,
                             String accessToken, LoginType loginType) throws UserLoginException;

    /**
     * zhangchaojie
     * 登出
     * @param request
     * @param response
     */
    public void logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * zhangchaojie
     * 获取当前缓存用户
     * @param request
     * @return
     */
    public User getCurrentUser(HttpServletRequest request);

    /**
     * zhangchaojie
     * 判断用户是否为首次登录
     * @param userId
     * @return
     */
    public boolean isFisrtLogin(int userId);

}
