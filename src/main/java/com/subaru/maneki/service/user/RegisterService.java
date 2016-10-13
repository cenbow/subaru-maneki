package com.subaru.maneki.service.user;

import com.subaru.maneki.enumeration.RegisterType;
import com.subaru.maneki.exception.UserRegisterException;

import java.sql.Timestamp;

/**
 * 注册相关service
 * @author zhangchaojie
 * @since 2016-08-11
 */
public interface RegisterService {

    /**
     * zhangchaojie
     * 基础注册接口，各param均不能为空
     * @param accountName
     * @param password
     * @param registerType
     * @return userId
     * @throws UserRegisterException
     */
    public int register(String accountName, String nick, String password, RegisterType registerType)
                                                                                       throws UserRegisterException;

    /**
     * zhangchaojie
     * 完整注册接口
     * @param email
     * @param cellphone
     * @param password
     * @param nick
     * @param registerType
     * @param phone
     * @param birthday
     * @param language
     * @param stateId
     * @param countryId
     * @param street
     * @param city
     * @param zip
     * @param oauthUid
     * @return userId
     * @throws UserRegisterException
     */
    public int register(String email, String cellphone, String password, String nick,
                        RegisterType registerType, String phone, Timestamp birthday,
                        String language, int stateId, int countryId, String street, String city,
                        String zip, String oauthUid) throws UserRegisterException;

    /**
     * zhangchaojie
     * 跨站登录注册接口
     * @param accessToken
     * @param registerType
     * @return userId
     */
    public int oauthRegister(String accessToken, RegisterType registerType) throws UserRegisterException;
    
    /**
     * huangli
     * 跨站登陆注册接口
     * @param accountName
     * @param uid
     * @param email
     * @param registerType
     * @return
     * @throws UserRegisterException
     */
    public int oauthRegister(String accountName, String uid, String email, RegisterType registerType) throws UserRegisterException;

}
