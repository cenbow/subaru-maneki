package test.com.subaru.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.AddressDao;
import com.subaru.maneki.dao.CountryDao;
import com.subaru.maneki.dao.CountryStateDao;
import com.subaru.maneki.dao.ShippingInfoDao;
import com.subaru.maneki.model.Address;
import com.subaru.maneki.model.Country;
import com.subaru.maneki.model.CountryState;
import com.subaru.maneki.model.ShippingInfo;
import com.subaru.core.BaseTestCaseJunit;

/**
 * @author zhangchaojie
 * @since 2016-09-07
 */
public class ShippingInfoDaoTest extends BaseTestCaseJunit {

    @Resource
    private AddressDao      addressDao;

    @Resource
    private CountryDao      countryDao;

    @Resource
    private CountryStateDao countryStateDao;

    private Address         address;

    private Country         country;

    private CountryState    countryState;

    @Resource
    private ShippingInfoDao shippingInfoDao;

    private ShippingInfo    shippingInfo;

    @Before
    public void setUp() {

        country = new Country();
        country.setName("America");
        country.setAbbr("U.S.");
        country.setRate(1);
        country.setCurrencyUnit("usd");
        country.setDollarThreshold(80);
        country.setDeliveryFee(40);
        countryDao.insert(country);

        countryState = new CountryState();
        countryState.setName("ZheJiang");
        countryState.setCode("code");
        countryState.setCountryId(country.getId());
        countryStateDao.insert(countryState);

        address = new Address();
        address.setCountryId(country.getId());
        address.setStateId(countryState.getId());
        address.setCity("HangZhou");
        address.setStreet("HZ");
        address.setZip("31002");
        address.setIsDefault(true);
        addressDao.insert(address);

        shippingInfo = new ShippingInfo();
        shippingInfo.setReceiverName("name");
        shippingInfo.setReceiverPhone("phone");
        shippingInfo.setReceiverEmail("email");
        shippingInfo.setAddressId(address.getId());
        shippingInfo.setPostFee(40);
        shippingInfo.setDeliveryMethod("method");
        shippingInfoDao.insert(shippingInfo);

    }

    @Test
    public void testSelectAndInsert() {
        ShippingInfo newShippingInfo = shippingInfoDao.select(shippingInfo.getId());
        Assert.assertTrue(shippingInfo.getReceiverName().equals(newShippingInfo.getReceiverName()));
        Assert.assertTrue(shippingInfo.getReceiverEmail()
            .equals(newShippingInfo.getReceiverEmail()));
        Assert.assertTrue(shippingInfo.getReceiverPhone()
            .equals(newShippingInfo.getReceiverPhone()));
        Assert.assertTrue(shippingInfo.getAddressId() == newShippingInfo.getAddressId());
        Assert.assertTrue(shippingInfo.getPostFee() == newShippingInfo.getPostFee());
        Assert
            .assertTrue(shippingInfo.getDeliveryMethod().equals(shippingInfo.getDeliveryMethod()));
    }

    @Test
    public void testUpdate() {

        address = new Address();
        address.setCountryId(country.getId());
        address.setStateId(countryState.getId());
        address.setCity("Nanjing");
        address.setStreet("NJ");
        address.setZip("31002");
        address.setIsDefault(false);
        addressDao.insert(address);

        shippingInfo.setReceiverName("newName");
        shippingInfo.setReceiverEmail("newEmail");
        shippingInfo.setReceiverPhone("newPhone");
        shippingInfo.setAddressId(address.getId());
        shippingInfo.setPostFee(100);
        shippingInfo.setDeliveryMethod("newMethod");
        shippingInfoDao.update(shippingInfo);

        ShippingInfo newShippingInfo = shippingInfoDao.select(shippingInfo.getId());
        Assert.assertTrue(shippingInfo.getReceiverName().equals(newShippingInfo.getReceiverName()));
        Assert.assertTrue(shippingInfo.getReceiverEmail()
            .equals(newShippingInfo.getReceiverEmail()));
        Assert.assertTrue(shippingInfo.getReceiverPhone()
            .equals(newShippingInfo.getReceiverPhone()));
        Assert.assertTrue(shippingInfo.getAddressId() == newShippingInfo.getAddressId());
        Assert.assertTrue(shippingInfo.getPostFee() == newShippingInfo.getPostFee());
        Assert
            .assertTrue(shippingInfo.getDeliveryMethod().equals(shippingInfo.getDeliveryMethod()));
    }

    @Test
    public void testDelete() {
        shippingInfoDao.delete(shippingInfo.getId());
        ShippingInfo deletedShippingInfo = shippingInfoDao.select(shippingInfo.getId());
        Assert.assertNull(deletedShippingInfo);
    }

}
