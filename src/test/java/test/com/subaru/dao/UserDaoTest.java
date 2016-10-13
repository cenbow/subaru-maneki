package test.com.subaru.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.UserDao;
import com.subaru.maneki.enumeration.RegisterType;
import com.subaru.maneki.model.User;
import com.subaru.core.BaseTestCaseJunit;
import com.subaru.core.encryption.MD5;

/**
 * @author zhangchaojie
 * @since 2016-08-11
 */
public class UserDaoTest extends BaseTestCaseJunit {

    @Resource
    private UserDao userDao;

    private User    user;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("chaojiezhang@epiclouds.net");
        user.setCellphone("18258477020");
        user.setPassword(MD5.MakeMD5("password"));
        user.setNick("zcj");
        user.setRegisterType(RegisterType.EMAIL.getValue());
        user.setOldId(123);
        userDao.insert(user);
    }

    @Test
    public void testSelect() {
        User newUser = userDao.select(user.getId());
        Assert.assertTrue(user.getId() == newUser.getId());
        Assert.assertTrue(user.getEmail().equals(newUser.getEmail()));
        Assert.assertTrue(user.getCellphone().equals(newUser.getCellphone()));
        Assert.assertTrue(user.getPassword().equals(newUser.getPassword()));
        Assert.assertTrue(user.getNick().equals(newUser.getNick()));
        Assert.assertTrue(user.getRegisterType() == newUser.getRegisterType());
    }

    @Test
    public void testUpdate() {
        user.setEmail("sa9530@163.com");
        user.setCellphone("18657912863");
        user.setPassword(MD5.MakeMD5("newpassword"));
        user.setNick("Rin");
        user.setRegisterType(RegisterType.FACEBOOK.getValue());
        userDao.update(user);
        User newUser = userDao.select(user.getId());
        Assert.assertTrue(user.getId() == newUser.getId());
        Assert.assertTrue(user.getEmail().equals(newUser.getEmail()));
        Assert.assertTrue(user.getCellphone().equals(newUser.getCellphone()));
        Assert.assertTrue(user.getPassword().equals(newUser.getPassword()));
        Assert.assertTrue(user.getNick().equals(newUser.getNick()));
        Assert.assertTrue(user.getRegisterType() == newUser.getRegisterType());
    }

    @Test
    public void testSelectByEmail() {
        User newUser = userDao.selectByEmail(user.getEmail());
        Assert.assertTrue(user.getId() == newUser.getId());
        Assert.assertTrue(user.getEmail().equals(newUser.getEmail()));
        Assert.assertTrue(user.getCellphone().equals(newUser.getCellphone()));
        Assert.assertTrue(user.getPassword().equals(newUser.getPassword()));
        Assert.assertTrue(user.getNick().equals(newUser.getNick()));
        Assert.assertTrue(user.getRegisterType() == newUser.getRegisterType());
    }

    @Test
    public void testSelectByCellphone() {
        User newUser = userDao.selectByCellphone(user.getCellphone());
        Assert.assertTrue(user.getId() == newUser.getId());
        Assert.assertTrue(user.getEmail().equals(newUser.getEmail()));
        Assert.assertTrue(user.getCellphone().equals(newUser.getCellphone()));
        Assert.assertTrue(user.getPassword().equals(newUser.getPassword()));
        Assert.assertTrue(user.getNick().equals(newUser.getNick()));
        Assert.assertTrue(user.getRegisterType() == newUser.getRegisterType());
    }

    @Test
    public void testSelectByOldId() {
        User newUser = userDao.selectByOldId(this.user.getOldId());
        Assert.assertTrue(user.getId() == newUser.getId());
        Assert.assertTrue(user.getEmail().equals(newUser.getEmail()));
        Assert.assertTrue(user.getCellphone().equals(newUser.getCellphone()));
        Assert.assertTrue(user.getPassword().equals(newUser.getPassword()));
        Assert.assertTrue(user.getNick().equals(newUser.getNick()));
        Assert.assertTrue(user.getRegisterType() == newUser.getRegisterType());
    }

}
