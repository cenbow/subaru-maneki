package com.subaru.maneki.service.user.imp;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.subaru.maneki.enumeration.RegisterType;
import com.subaru.maneki.exception.UserException;
import com.subaru.maneki.exception.UserRegisterException;
import com.subaru.maneki.model.DetailedUser;
import com.subaru.maneki.model.User;
import com.subaru.maneki.model.UserOAuth;
import com.subaru.maneki.service.user.OAuthService;
import com.subaru.maneki.service.user.RegisterService;
import com.subaru.maneki.service.user.UserService;
import com.subaru.maneki.vo.FacebookOauthInfoVO;
import com.subaru.common.util.VerifyUtil;
import com.subaru.core.encryption.MD5;

/**
 * 注册服务实现类
 * @author zhangchaojie
 * @since 2016-08-11
 */
@Service("registerService")
public class RegisterServiceImp implements RegisterService {

    @Resource
    private UserService   userService;

    @Resource
    private UserService   detailedUserService;

    @Resource
    private OAuthService  facebookOAuthService;

    private static Logger logger = LoggerFactory.getLogger(RegisterServiceImp.class);

    @Override
    public int register(String accountName, String nick, String password, RegisterType registerType)
                                                                                                    throws UserRegisterException {

        if (registerType == RegisterType.EMAIL) {
            return register(accountName, null, password, nick, registerType, null, null, null, 0,
                0, null, null, null, null);
        }

        return -1;

    }

    @Override
    //    @Transactional
    public int register(String email, String cellphone, String password, String nick,
                        RegisterType registerType, String phone, Timestamp birthday,
                        String language, int stateId, int countryId, String street, String city,
                        String zip, String oauthUid) throws UserRegisterException {

        if (StringUtils.isBlank(email)) {
            throw new UserRegisterException("Email不能为空！！");
        }
        if (password.length() < 6 || password.length() > 16) {
            throw new UserRegisterException("密码应为6-16个字符！！");
        }
        if (registerType.getValue() <= 0) {
            throw new UserRegisterException("未知的注册类型！！");
        }

        if (StringUtils.isNotBlank(cellphone) && VerifyUtil.checkCellphoneNumber(cellphone)) {
            User user = userService.getByCellphone(cellphone);
            if (user != null) {
                throw new UserRegisterException("该手机号已注册！！");
            }
        } else if (StringUtils.isNotBlank(email) && VerifyUtil.checkEmail(email)) {
            User user = userService.getByEmail(email);
            if (user != null) {
                throw new UserRegisterException("该邮箱已注册，请直接登录");
            }
        }

        DetailedUser detailedUser = new DetailedUser(email, cellphone, nick, MD5.MakeMD5(password),
            registerType.getValue(), phone, birthday, language, countryId);

        int userId = 0;
        try {
            userId = detailedUserService.add(detailedUser);
        } catch (UserException e) {
            logger.info("", e);
        }
        if (userId <= 0) {
            throw new UserRegisterException("用户添加失败");
        }

        if (StringUtils.isNotBlank(oauthUid)) {
            UserOAuth userOAuth = new UserOAuth(userId, oauthUid, registerType.name());
            int userOAuthId = userService.addUserOAuth(userOAuth);
            if (userOAuthId <= 0) {
                throw new UserRegisterException("UserOAuth信息添加失败");
            }
        }

        return userId;
    }

    @Override
    public int oauthRegister(String accessToken, RegisterType registerType)
                                                                           throws UserRegisterException {
        if (registerType == RegisterType.EMAIL) {
            throw new UserRegisterException("注册类型不为OAUTH，不能进行OAUTH注册！！");
        }

        if (StringUtils.isBlank(accessToken)) {
            throw new UserRegisterException("注册类型为OAUTH，accessToken不能为空！！");
        }
        /**
         * zhangchaojie
         * 1. 根据accessToken获取accountName
         * 		accountName可能是email，也可能不是email
         * 		如果是email，那么直接注册
         * 		如果不是email，目前做法也是直接写入数据库中
         * 2. 根据accessToken获取oauthUid
         * 3. 用户密码统一不为空，对于联合登录方式注册的新用户，默认设置其明文密码为password，将来在联合登录成功后应提供引导页面让用户完善基本注册信息
         */
        /*
        String accountName = facebookOAuthService.getAccountName(accessToken, registerType);
        String oauthUid = facebookOAuthService.getOAuthUid(accessToken, registerType);
        */
        FacebookOauthInfoVO facebookOauthInfoVO = facebookOAuthService.getOAuthInfo(accessToken);
        if (facebookOauthInfoVO == null) {
            throw new UserRegisterException("用户是非法的，注册失败！！");
        }
        String accountName = facebookOauthInfoVO.getName();
        String oauthUid = facebookOauthInfoVO.getUid();
        String email = facebookOauthInfoVO.getEmail();
        if (VerifyUtil.checkEmail(email)) {
            return register(email, null, "password", accountName, registerType, null, null, null,
                0, 0, null, null, null, oauthUid);
        } else {
            return register(email, null, "password", accountName, registerType, null, null, null,
                0, 0, null, null, null, oauthUid);
        }

        //return -1;
    }

    @Override
    public int oauthRegister(String accountName, String uid, String email, RegisterType registerType)
                                                                                                     throws UserRegisterException {
        if (registerType == RegisterType.EMAIL) {
            throw new UserRegisterException("注册类型不为OAUTH，不能进行OAUTH注册！！");
        }

        if (VerifyUtil.checkEmail(email)) {
            return register(email, null, "password", accountName, registerType, null, null, null,
                0, 0, null, null, null, uid);
        } else {
            return register(email, null, "password", accountName, registerType, null, null, null,
                0, 0, null, null, null, uid);
        }
    }
}
