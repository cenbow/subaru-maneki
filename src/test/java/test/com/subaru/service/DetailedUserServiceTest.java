package test.com.subaru.service;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.enumeration.RegisterType;
import com.subaru.maneki.exception.UserException;
import com.subaru.maneki.model.DetailedUser;
import com.subaru.maneki.model.User;
import com.subaru.maneki.model.UserEtc;
import com.subaru.maneki.service.user.UserService;
import com.subaru.core.BaseTestCaseJunit;

public class DetailedUserServiceTest extends BaseTestCaseJunit {

    @Resource
    private UserService  detailedUserService;

    private DetailedUser detailedUser;

    @Before
    public void setUp() {

        User user = new User();
        UserEtc userEtc = new UserEtc();

        user.setEmail("lihuang@epiclouds.net");
        user.setPassword("123456");
        user.setCellphone("12345678");
        user.setNick("lee");
        user.setRegisterType(RegisterType.EMAIL.getValue());

        userEtc.setBirthday(new Timestamp(System.currentTimeMillis()));
        userEtc.setPhone("1234567890");
        userEtc.setCountryId(235);
        userEtc.setGmtUpdate(new Timestamp(System.currentTimeMillis()));

        detailedUser = new DetailedUser(user, userEtc);

        int result = 0;
        try {
            result = detailedUserService.add(detailedUser);
        } catch (UserException e) {
            System.out.println(e.getMessage());
        }
        if (result == -1) {
            System.out.println("Add detailedUserService error");
        }
    }

    /**
     * 测试用户详情的查询操作
     */
    @Test
    public void testSelectByEmail() {
        DetailedUser emailUser = (DetailedUser) detailedUserService
            .getByEmail("lihuang@epiclouds.net");
        System.out.println("user id = " + emailUser.getCellphone());
        System.out.println("detail user id = " + detailedUser.getCellphone());
        Assert.assertTrue(detailedUser.getCellphone().equals(emailUser.getCellphone()));
    }

    /**
     * 测试用户详情的更新操作
     */
    @Test
    public void testUpdateById() {
        DetailedUser emailUser = (DetailedUser) detailedUserService
            .getByEmail("lihuang@epiclouds.net");

        detailedUserService.update(detailedUser);
    }
}