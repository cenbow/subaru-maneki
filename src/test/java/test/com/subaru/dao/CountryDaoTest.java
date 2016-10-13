package test.com.subaru.dao;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.CountryDao;
import com.subaru.maneki.model.Country;
import com.subaru.core.BaseTestCaseJunit;

public class CountryDaoTest extends BaseTestCaseJunit{
	@Resource
	public CountryDao countryDao;
	
	public Country country;
	
	@Before
	public void Setup(){
		country = new Country();
		country.setAbbr("us");
		country.setCurrencyUnit("us");
		country.setDollarThreshold(80);
		country.setDeliveryFee(20);
		country.setName("American");
		country.setRate(1);
		
		countryDao.insert(country);
	}
	
	@Test
	public void testSelect(){
		Country newCountry = countryDao.select(country.getId());
		
		Assert.assertTrue(country.getAbbr().equals(newCountry.getAbbr()));
		Assert.assertTrue(country.getCurrencyUnit().equals(newCountry.getCurrencyUnit()));
		Assert.assertTrue(country.getDollarThreshold() == (newCountry.getDollarThreshold()));
		Assert.assertTrue(country.getName().equals(newCountry.getName()));
		Assert.assertTrue(country.getRate() == newCountry.getRate());
	}
	
	@Test
	public void testUpdate(){
		Timestamp oldUpdateTime = countryDao.select(country.getId()).getGmtUpdate();

		//更新Street字段的值
		country.setRate(1.1);
		
		try{
        	Thread.sleep(1000 * 2);
        }catch(InterruptedException e){
        	e.printStackTrace();
        }
		
		countryDao.update(country);
		
		Timestamp newUpdateTime = countryDao.select(country.getId()).getGmtUpdate();
		
		Assert.assertNotNull(oldUpdateTime);
		Assert.assertNotNull(newUpdateTime);
		
		Assert.assertFalse(newUpdateTime.toString().equals(oldUpdateTime.toString()));
	}
	
	@After
	public void testDelete(){
		countryDao.delete(country.getId());
		Country newCountry = countryDao.select(country.getId());
		Assert.assertNull(newCountry);
	}
}
