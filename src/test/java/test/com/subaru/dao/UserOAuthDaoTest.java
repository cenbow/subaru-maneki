package test.com.subaru.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.UserOAuthDao;
import com.subaru.maneki.enumeration.RegisterType;
import com.subaru.maneki.model.UserOAuth;
import com.subaru.core.BaseTestCaseJunit;

/**
 * @author zhangchaojie
 * @since 2016-08-11
 */
public class UserOAuthDaoTest extends BaseTestCaseJunit {

    @Resource
    private UserOAuthDao userOAuthDao;

    private UserOAuth    userOAuth;

    @Before
    public void setUp() {
        userOAuth = new UserOAuth();
        userOAuth.setUserId(1);
        userOAuth.setOauthUid("1234abcd");
        userOAuth.setOauthProvider(RegisterType.FACEBOOK.name());
        userOAuthDao.insert(userOAuth);
    }

    @Test
    public void testSelect() {
        UserOAuth newUserOAuth = userOAuthDao.select(userOAuth.getUserId());
        Assert.assertTrue(userOAuth.getUserId() == newUserOAuth.getUserId());
        Assert.assertTrue(userOAuth.getOauthUid().equals(newUserOAuth.getOauthUid()));
        Assert.assertTrue(userOAuth.getOauthProvider().equals(newUserOAuth.getOauthProvider()));
    }

    @Test
    public void testSelectByOAuthUid() {
        UserOAuth newUserOAuth = userOAuthDao.selectByOAuthUid(userOAuth.getOauthUid());
        Assert.assertTrue(userOAuth.getUserId() == newUserOAuth.getUserId());
        Assert.assertTrue(userOAuth.getOauthUid().equals(newUserOAuth.getOauthUid()));
        Assert.assertTrue(userOAuth.getOauthProvider().equals(newUserOAuth.getOauthProvider()));
    }

    @Test
    public void testUpdate() {
        userOAuth.setOauthUid("4321dbca");
        userOAuth.setOauthProvider("twitter");
        userOAuthDao.update(userOAuth);
        UserOAuth newUserOAuth = userOAuthDao.select(userOAuth.getUserId());
        Assert.assertTrue(userOAuth.getOauthUid().equals(newUserOAuth.getOauthUid()));
        Assert.assertTrue(userOAuth.getOauthProvider().equals(newUserOAuth.getOauthProvider()));
    }

}
