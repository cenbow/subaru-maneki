package com.subaru.maneki.service.user.imp;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.UserDao;
import com.subaru.maneki.dao.UserOAuthDao;
import com.subaru.maneki.exception.UserException;
import com.subaru.maneki.model.User;
import com.subaru.maneki.model.UserOAuth;
import com.subaru.maneki.service.user.UserService;

/**
 * @author zhangchaojie
 * @since 2016-08-11
 */
@Service("userService")
public class UserServiceImp implements UserService {

    @Resource
    private UserDao      userDao;

    @Resource
    private UserOAuthDao userOAuthDao;

    @Override
    public User get(int userId) {
        if (userId <= 0) {
            return null;
        }
        return userDao.select(userId);
    }

    @Override
    public User getByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return null;
        }
        return userDao.selectByEmail(email);
    }

    @Override
    public User getByCellphone(String cellphone) {
        if (StringUtils.isBlank(cellphone)) {
            return null;
        }
        return userDao.selectByCellphone(cellphone);
    }

    @Override
    public int add(User user) throws UserException {
        if (user == null) {
            return -1;
        }
        String email = user.getEmail();
        if (StringUtils.isNotBlank(email) && userDao.selectByEmail(email) != null) {
            throw new UserException("This email has registered !!");
        }
        String cellPhone = user.getCellphone();
        if (StringUtils.isNotBlank(cellPhone) && userDao.selectByCellphone(cellPhone) != null) {
            throw new UserException("This cellphone has registered !!");
        }
        userDao.insert(user);
        return user.getId();
    }

    @Override
    public int addUserOAuth(UserOAuth userOAuth) {
        if (userOAuth == null) {
            return -1;
        }
        userOAuthDao.insert(userOAuth);
        return userOAuth.getUserId();
    }

    @Override
    public UserOAuth getUserOAuth(int userId) {
        if (userId <= 0) {
            return null;
        }
        return userOAuthDao.select(userId);
    }

    @Override
    public int update(User user) {
        if (user == null) {
            return -1;
        }
        int userId = userDao.update(user);
        return userId;
    }

    @Override
    public int delete(int id) {
        return 1;
    }
}
