package com.subaru.maneki.service.user;

import com.subaru.maneki.exception.UserException;
import com.subaru.maneki.model.User;
import com.subaru.maneki.model.UserOAuth;

/**
 * Created by zhangchaojie on 2016/8/9.
 * @since 2016-08-11
 */
public interface UserService {

    /**
     * 通过userId获取用户信息
     * @param userId
     * @return user
     */
    User get(int userId);

    /**
     * 通过email获取用户信息
     * @param email
     * @return user
     */
    User getByEmail(String email);

    /**
     * 通过cellphoneNumber获取用户信息
     * @param cellphone
     * @return user
     */
    User getByCellphone(String cellphone);

    /**
     * 添加用户
     * @param user
     * @return userId
     */
    int add(User user) throws UserException;
    
    /**
     * 更新用户信息
     * @param user
     * @return
     */
    int update(User user);
    
    /**
     * 删除用户信息
     * @param userId
     * @return
     */
    int delete(int userId);

    /**
     * 添加用户跨站登录信息
     * @param userOAuth
     * @return userId
     */
    int addUserOAuth(UserOAuth userOAuth);

    /**
     * 通过userId获取用户跨站信息
     * @param userId
     * @return userOAuth
     */
    UserOAuth getUserOAuth(int userId);

}
