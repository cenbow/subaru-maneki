package com.subaru.maneki.service.user.imp;

import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.LoginRecordDao;
import com.subaru.maneki.dao.UserDao;
import com.subaru.maneki.dao.UserEtcDao;
import com.subaru.maneki.dao.UserOAuthDao;
import com.subaru.maneki.enumeration.LoginType;
import com.subaru.maneki.enumeration.RegisterType;
import com.subaru.maneki.exception.UserLoginException;
import com.subaru.maneki.exception.UserRegisterException;
import com.subaru.maneki.model.*;
import com.subaru.maneki.service.user.LoginService;
import com.subaru.maneki.service.user.OAuthService;
import com.subaru.maneki.service.user.RegisterService;
import com.subaru.maneki.vo.FacebookOauthInfoVO;
import com.subaru.common.http.IPUtil;
import com.subaru.core.encryption.MD5;

/**
 * 登录服务接口实现类
 * @author zhangchaojie
 * @since 2016-08-11
 */
@Service("loginService")
public class LoginServiceImp implements LoginService {

    @Resource
    private LoginRecordDao  loginRecordDao;

    @Resource
    private UserDao         userDao;

    @Resource
    private UserEtcDao      userEtcDao;

    @Resource
    private UserOAuthDao    userOAuthDao;

    @Resource
    private OAuthService    facebookOAuthService;

    @Resource
    private RegisterService registerService;

    private static String   COOKIE_LOGIN_TICKET  = "clt";

    private static String   COOKIE_DOMAIN        = "localhost";

    private static String   SESSION_CURRENT_USER = "user";

    private Logger          logger               = LoggerFactory.getLogger(LoginService.class);

    @Override
    public String login(HttpServletRequest request, HttpServletResponse response,
                        String accountName, String password, LoginType loginType)
                                                                                 throws UserLoginException {
        /**
         * zhangchaojie
         * 1. 判断用户是否已登录，校验登录状态
         * 2. 若已登录则刷新token的有效时间，并刷新session中的当前用户信息，若未登录则跳至步骤3
         * 3. 校验用户账号密码，成功后生成登录token(应防止token被窃取后非法登录)，以(id, userId, token, ,ip, gmt_create, gmt_update, gmt_expire)作为登录记录存入MYSQL（后续引入单点服务器后接入redis）
         * 4. 在cookie中写入token信息，将用户信息存入session并返回token
         */
        Cookie loginCookie = getLoginCookie(request);
        if (loginCookie != null) {
            String token = loginCookie.getValue();
            if (StringUtils.isNotBlank(token)) {
                if (checkLoginToken(request, token)) {
                    refreshLoginToken(token);
                    refreshSession(request.getSession(), token);
                } else {
                    clearLoginCookie(request, response);
                    //zhangchaojie: 对于token异常的情况，清除用户的cookie，然后进入正常的登录流程，无需抛出异常终止登录
                    //                    throw new UserLoginException(
                    //                        "Unavailable cookie token, please sign in again !!");
                }
            }
        }

        User user = getAccountUser(accountName, password);
        if (user == null) {
            throw new UserLoginException("Invalid account info, please try again !!");
        }
        LoginTicket loginTicket = new LoginTicket();
        setLoginRecord(user.getId(), loginTicket.getToken(), IPUtil.getClientRealIP(request),
            loginType);
        setLoginCookie(response, loginTicket.getToken());
        request.getSession().setAttribute(SESSION_CURRENT_USER, user);
        return loginTicket.getToken();
    }

    @Override
    public boolean isLogin(HttpServletRequest request) {

        Cookie loginCookie = getLoginCookie(request);

        if (loginCookie == null) {
            return false;
        }

        String loginToken = getLoginCookie(request).getValue();

        if (StringUtils.isBlank(loginToken)) {
            return false;
        }
        return checkLoginToken(request, loginToken);
    }

    @Override
    public String oauthLogin(HttpServletRequest request, HttpServletResponse response,
                             String accessToken, LoginType loginType) throws UserLoginException {
        /**
         * zhangchaojie
         * 1. 获取联合登录UID，判断该三方账户是否已注册对应的本网站用户
         * 2. 如果该三方账户已注册本站用户，则生成对应的登录记录存入MYSQL，在cookie中写入token信息并将取出用户信息存入session
         * 3. 如果该三方账户未注册本站用户，则进入联合登录用户注册流程，注册成功后，自动生成登录记录，在cookie中写入token信息并将用户信息存入session
         */
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }

        String uid = "";
        int userId = -1;
        FacebookOauthInfoVO facebookOauthInfoVO = null;

        if (loginType.equals(LoginType.FACEBOOK)) {
            facebookOauthInfoVO = facebookOAuthService.getOAuthInfo(accessToken);
            //uid = facebookOAuthService.getOAuthUid(accessToken);
            uid = facebookOauthInfoVO.getUid();
        }
        if (StringUtils.isBlank(uid)) {
            return null;
        }

        UserOAuth userOAuth = userOAuthDao.selectByOAuthUid(uid);
        if (userOAuth == null) {
            //未注册本站用户
            try {
                /*
                userId = registerService.oauthRegister(accessToken,
                    RegisterType.getType(loginType.getValue()));
                */
                if (facebookOauthInfoVO != null) {
                    String accountName = facebookOauthInfoVO.getName();
                    String email = facebookOauthInfoVO.getEmail();
                    userId = registerService.oauthRegister(accountName, uid, email,
                        RegisterType.getType(loginType.getValue()));
                }

            } catch (UserRegisterException e) {
                logger.info(e.getMessage());
            }

        } else {
            //已注册本站用户
            userId = userOAuth.getUserId();
        }

        if (userId <= 0) {
            throw new UserLoginException("非法的userId，错误的UserOAuth记录或注册本地用户失败！！");
        }
        LoginTicket loginTicket = new LoginTicket();
        setLoginRecord(userId, loginTicket.getToken(), IPUtil.getClientRealIP(request), loginType);
        setLoginCookie(response, loginTicket.getToken());
        User user = userDao.select(userId);
        request.getSession().setAttribute(SESSION_CURRENT_USER, user);

        return loginTicket.getToken();
    }

    /**
     * zhangchaojie
     * 登出接口会清理cookie和session，暂不清除登录记录
     * @param request
     * @param response
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        if (isLogin(request)) {
            clearLoginCookie(request, response);
            request.getSession().removeAttribute(SESSION_CURRENT_USER);
        }
    }

    /**
     * zhangchaojie
     * 获取当前用户缓存用户，优先从session中获取，若session中不存在则从cookie中获取，否则返回null
     * @param request
     * @return
     */
    @Override
    public User getCurrentUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(SESSION_CURRENT_USER);
        if (user == null) {
            Cookie loginCookie = getLoginCookie(request);
            if (loginCookie == null) {
                return null;
            }
            String loginToken = loginCookie.getValue();
            if (StringUtils.isBlank(loginToken)) {
                return null;
            }
            LoginRecord loginRecord = loginRecordDao.selectByToken(loginToken);
            if (loginRecord == null) {
                return null;
            }
            int userId = loginRecord.getUserId();
            if (userId <= 0) {
                return null;
            }
            user = userDao.select(userId);
        }

        return user;
    }

    @Override
    public boolean isFisrtLogin(int userId) {
        if (userId <= 0 || userDao.select(userId) == null) {
            return false;
        }
        UserEtc userEtc = userEtcDao.select(userId);
        if (userEtc != null && userEtc.getGmtLastLogin() != null) {
            return true;
        }
        return false;
    }

    /**
     * zhangchaojie
     * 取出cookie中的登录信息
     */
    public Cookie getLoginCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie c : cookies) {
            if (COOKIE_LOGIN_TICKET.equals(c.getName())) {
                return c;
            }
        }
        return null;
    }

    /**
     * zhangchaojie
     * 检查token记录是否存在，并判断其是否有效（已过期或是token不属于该ip）
     */
    private boolean checkLoginToken(HttpServletRequest request, String token) {

        if (StringUtils.isBlank(token)) {
            return false;
        }

        String ip = IPUtil.getClientRealIP(request);

        LoginRecord loginRecord = loginRecordDao.selectByToken(token);
        if (loginRecord == null) {
            return false;
        }

        if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(loginRecord.getIp())) {
            if (!ip.equals(loginRecord.getIp())) {
                logger.info("客户端IP与登录记录IP不匹配！！");
                return false;
            }
        }

        if (loginRecord.getGmtExpire() == null) {
            logger.info("异常的登录记录，该记录的expireTime为空！！");
            return false;
        }

        DateTime expireTime = new DateTime(loginRecord.getGmtExpire());
        if (expireTime.isBeforeNow()) {
            return false;
        }

        return true;
    }

    /**
     * zhangchaojie
     * 刷新token的有效时间，从当前时间起往后推30分钟
     * @param token
     */
    private void refreshLoginToken(String token) {
        LoginRecord loginRecord = loginRecordDao.selectByToken(token);
        if (loginRecord == null) {
            return;
        }
        loginRecord.setGmtExpire(new Timestamp(DateTime.now().plusMinutes(30).getMillis()));
        loginRecordDao.update(loginRecord);
    }

    /**
     * zhangchaojie
     * 在session中的保存或刷新当前用户信息
     * @param request
     * @param session
     */
    private void refreshSession(HttpSession session, String token) {

        if (StringUtils.isBlank(token)) {
            return;
        }

        LoginRecord loginRecord = loginRecordDao.selectByToken(token);
        if (loginRecord == null || new DateTime(loginRecord.getGmtExpire()).isBeforeNow()
            || loginRecord.getUserId() <= 0) {
            logger.info("loginRecord为空，或已过期，或非法的userId ！！");
            return;
        }

        int userId = loginRecord.getUserId();
        User currentUser = userDao.select(userId);
        session.setAttribute(SESSION_CURRENT_USER, currentUser);

    }

    /**
     * zhangchaojie
     * 设置登录相关的cookie信息
     * domain先按照默认方式保存
     * @param response
     * @param token
     */
    private void setLoginCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_LOGIN_TICKET, token);
        //cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        //cookies的过期时间设置为1天
        cookie.setMaxAge(3600 * 24);
        logger.info(COOKIE_LOGIN_TICKET + ": " + COOKIE_DOMAIN);
        response.addCookie(cookie);
    }

    private void clearLoginCookie(HttpServletRequest request, HttpServletResponse response) {

        Cookie loginCookie = getLoginCookie(request);
        loginCookie.setValue("");
        loginCookie.setPath("/");
        response.addCookie(loginCookie);

    }

    /**
     * zhangchaojie
     * 添加登录记录
     * @param userId
     * @param loginToken
     * @param ip
     * @param loginType
     */
    private void setLoginRecord(int userId, String loginToken, String ip, LoginType loginType) {
        if (userId <= 0 || StringUtils.isBlank(loginToken) || StringUtils.isBlank(ip)
            || loginType == null) {
            return;
        }
        DateTime now = DateTime.now();
        LoginRecord loginRecord = new LoginRecord(userId, loginToken, ip, loginType.getValue(),
            new Timestamp(now.plusMinutes(30).getMillis()));
        loginRecordDao.insert(loginRecord);

        //判断是否初次登录
        UserEtc userEtc = userEtcDao.select(userId);
        if (userEtc != null) {
            userEtc.setGmtLastLogin(new Timestamp(now.getMillis()));
            userEtcDao.update(userEtc);
        }
    }

    /**
     * zhangchaojie
     * 校验用户名密码，通过后返回对应用户
     * @param accountName
     * @param password
     * @return
     */
    private User getAccountUser(String accountName, String password) {
        if (StringUtils.isBlank(accountName) || StringUtils.isBlank(password)) {
            return null;
        }
        User user = userDao.selectByEmail(accountName);
        if (user == null) {
            return null;
        }
        if (!MD5.MakeMD5(password).equals(user.getPassword())) {
            return null;
        }
        return user;
    }

}
