package com.subaru.maneki.dao;

import com.subaru.maneki.model.User;

/**
 * @author zhangchaojie
 * @since 2016-08-11
 */
public interface UserDao {

    int insert(User user);

    User select(int id);

    int update(User user);

    User selectByEmail(String email);

    User selectByCellphone(String cellphone);

    User selectByOldId(int oldId);

}
