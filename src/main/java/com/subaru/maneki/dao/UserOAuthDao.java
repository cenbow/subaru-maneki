package com.subaru.maneki.dao;

import com.subaru.maneki.model.UserOAuth;

/**
 * @author zhangchaojie
 * @since 2016-08-11
 */
public interface UserOAuthDao {

    int insert(UserOAuth userOAuth);

    UserOAuth select(int userId);

    UserOAuth selectByOAuthUid(String oauthUid);

    int update(UserOAuth userOAuth);

}
