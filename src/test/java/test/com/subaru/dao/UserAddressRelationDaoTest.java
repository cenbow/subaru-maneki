package test.com.subaru.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.AddressDao;
import com.subaru.maneki.dao.UserAddressRelationDao;
import com.subaru.maneki.dao.UserDao;
import com.subaru.maneki.model.Address;
import com.subaru.maneki.model.User;
import com.subaru.maneki.model.UserAddressRelation;
import com.subaru.core.BaseTestCaseJunit;

public class UserAddressRelationDaoTest extends BaseTestCaseJunit {

    @Resource
    public UserAddressRelationDao userAddressRelationDao;

    @Resource
    public UserDao                userDao;

    @Resource
    public AddressDao             addressDao;

    public User                   user;

    public Address                address;

    public UserAddressRelation    userAddressRelation;

    @Before
    public void Setup() {
        user = new User();
        user.setCellphone("1234567890");
        user.setEmail("test@test.com");
        user.setNick("hello");
        user.setPassword("123455");
        user.setRegisterType(1);

        userDao.insert(user);

        address = new Address();
        address.setCountryId(123);
        address.setStateId(1);
        address.setCity("HZ");
        address.setStreet("WuChang");
        address.setZip("123456");
        addressDao.insert(address);

        userAddressRelation = new UserAddressRelation();
        userAddressRelation.setAddressId(address.getId());
        userAddressRelation.setUserId(user.getId());
        userAddressRelationDao.insert(userAddressRelation);
    }

    @Test
    public void testSelect() {
        UserAddressRelation uar = userAddressRelationDao.select(userAddressRelation.getUserId(),
            userAddressRelation.getAddressId());
        Assert.assertNotNull(uar);

        User newUser = userDao.select(uar.getUserId());
        Assert.assertTrue(newUser.getCellphone().equals(user.getCellphone()));

        Address newAddress = addressDao.select(address.getId());
        //		Assert.assertTrue(newAddress.getProvince().equals(address.getProvince()));
    }
}
