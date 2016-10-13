package com.subaru.maneki.executor;

import com.subaru.maneki.dao.UserDao;
import com.subaru.maneki.enumeration.RegisterType;
import com.subaru.maneki.model.User;
import com.subaru.core.BaseExecute;
import com.subaru.core.encryption.MD5;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author zhangchaojie
 * @since 2016-08-16
 */
public class UserDaoExecutor extends BaseExecute {

    @Resource
    private UserDao userDao;

    private User    user;

    @Before
    public void setUp() {

    }

    @Test
    public void batchInsert() {
        for (int i = 0; i < 500; i++) {
            user = new User();
            user.setEmail("sa" + (i + 1) + "@epiclouds.net");
            user.setCellphone((i + 1) + "");
            user.setPassword(MD5.MakeMD5("password"));
            user.setNick("zcj" + (i + 1));
            user.setRegisterType(RegisterType.EMAIL.getValue());
            userDao.insert(user);
        }
    }

}
