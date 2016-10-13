package test.com.subaru.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.AddressDao;
import com.subaru.maneki.dao.CountryDao;
import com.subaru.maneki.dao.CountryStateDao;
import com.subaru.maneki.model.Address;
import com.subaru.maneki.model.Country;
import com.subaru.maneki.model.CountryState;
import com.subaru.core.BaseTestCaseJunit;

public class AddressDaoTest extends BaseTestCaseJunit {

    @Resource
    private AddressDao      addressDao;

    @Resource
    private CountryDao      countryDao;

    @Resource
    private CountryStateDao countryStateDao;

    private Address         address;

    private Country         country;

    private CountryState    countryState;

    @Before
    public void Setup() {

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
    }

    @Test
    public void testSelect() {
        Address newAddress = addressDao.select(address.getId());

        //		Assert.assertTrue(address.getProvince().equals(newAddress.getProvince()));
        Assert.assertTrue(address.getStreet().equals(newAddress.getStreet()));
        Assert.assertTrue(address.getZip().equals(newAddress.getZip()));
        Assert.assertTrue(address.getCountryId() == newAddress.getCountryId());
    }

    //	@Test
    //	public void testUpdate(){
    //		Address result = addressDao.select(address.getId());
    //		System.out.println(result.getGmtCreate());
    //		Timestamp oldUpdateTime = addressDao.select(address.getId()).getGmtUpdate();
    //
    //		//更新Street字段的值
    //		address.setStreet("FJ");
    //
    //		try{
    //        	Thread.sleep(1000 * 2);
    //        }catch(InterruptedException e){
    //        	e.printStackTrace();
    //        }
    //
    //		addressDao.update(address);
    //
    //		Timestamp newUpdateTime = addressDao.select(address.getId()).getGmtUpdate();
    //
    //		Assert.assertNotNull(oldUpdateTime);
    //		Assert.assertNotNull(newUpdateTime);
    //
    //		Assert.assertFalse(newUpdateTime.toString().equals(oldUpdateTime.toString()));
    //	}

    @Test
    public void testDelete() {
        addressDao.delete(address.getId());
        Address newAddress = addressDao.select(address.getId());
        Assert.assertNull(newAddress);
    }
}
