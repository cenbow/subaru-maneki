package com.subaru.maneki.service.user.imp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.UserDao;
import com.subaru.maneki.dao.UserEtcDao;
import com.subaru.maneki.model.DetailedUser;
import com.subaru.maneki.model.User;
import com.subaru.maneki.model.UserEtc;
import com.subaru.maneki.model.UserOAuth;
import com.subaru.maneki.service.user.UserService;

/**
 * @author zhangchaojie
 * @since 2016-08-09
 */
@Service("detailedUserService")
public class DetailedUserServiceImp implements UserService {

    @Resource
    private UserDao    userDao;

    @Resource
    private UserEtcDao userEtcDao;

    @Override
    public User get(int userId) {
        if (userId < 0) {
            return null;
        }
        User user = userDao.select(userId);
        UserEtc userEtc = userEtcDao.select(userId);
        DetailedUser detailedUser;
        if (user == null) {
            return null;
        }
        if (userEtc == null) {
            detailedUser = new DetailedUser(user);
        } else {
            detailedUser = new DetailedUser(user, userEtc);
        }

        return detailedUser;
    }

    @Override
    public User getByEmail(String email) {
        if (email == null) {
            System.out.println("email obj is null");
            return null;
        }
        User user = userDao.selectByEmail(email);
        if (user == null) {
            System.out.println("user obj is null");
            return null;
        }
        UserEtc userEtc = userEtcDao.select(user.getId());
        if (userEtc == null) {
            System.out.println("userEtc obj is null");
            return null;
        }
        return new DetailedUser(user, userEtc);
    }

    @Override
    public User getByCellphone(String cellphone) {
        if (cellphone == null) {
            return null;
        }
        User user = userDao.selectByCellphone(cellphone);
        if (user == null) {
            return null;
        }
        UserEtc userEtc = userEtcDao.select(user.getId());
        if (userEtc == null) {
            return null;
        }
        return new DetailedUser(user, userEtc);
    }

    @Override
    public int add(User user) {
        if (user == null) {
            return -1;
        }
        int result = userDao.insert(user);
        if (result < 1) {
            return -1;
        }

        DetailedUser detailedUser = (DetailedUser) user;

        UserEtc userEtc = new UserEtc(user.getId(), detailedUser.getPhone(),
            detailedUser.getBirthday(), detailedUser.getLanguage(), detailedUser.getCountryId());
        result = userEtcDao.insert(userEtc);

        return user.getId();
    }

    @Override
    public int addUserOAuth(UserOAuth userOAuth) {
        return 0;
    }

    @Override
    public UserOAuth getUserOAuth(int userId) {
        return null;
    }

    @Override
    public int update(User user) {
        userDao.update(user);

        int userId = user.getId();

        DetailedUser detailedUser = (DetailedUser) user;

        UserEtc userEtc = new UserEtc(userId, detailedUser.getPhone(), detailedUser.getBirthday(),
            detailedUser.getLanguage(), detailedUser.getCountryId());
        int cnt = userEtcDao.update(userEtc);

        return cnt;
    }

    @Override
    public int delete(int id) {
        return 1;
    }
}
