package com.subaru.maneki.dao;

import com.subaru.maneki.model.LoginRecord;

import java.util.List;

/**
 * @author zhangchaojie
 * @since 2016-08-15
 */
public interface LoginRecordDao {

    LoginRecord select(int id);

    List<LoginRecord> selectByUserId(int userId);

    LoginRecord selectByToken(String token);

    List<LoginRecord> selectByIp(String ip);

    int update(LoginRecord loginRecord);

    int insert(LoginRecord loginRecord);

    int delete(int id);

    int countExpired();

    int deleteExpired();

}
