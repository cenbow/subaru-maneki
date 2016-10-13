package test.com.subaru.dao;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import com.subaru.maneki.dao.CountryStateDao;
import com.subaru.maneki.model.CountryState;
import com.subaru.core.BaseTestCaseJunit;

import junit.framework.Assert;

public class CountryStateDaoTest extends BaseTestCaseJunit{

	@Resource
	private CountryStateDao countryStateDao;
	
	public CountryState countryState;
	
	@Before
	public void Setup(){
		countryState = new CountryState();
		countryState.setCode("AL");
		countryState.setCountryId(39);
		countryState.setName("Alberta");
		
		countryStateDao.insert(countryState);
	}
	
	@Test
	public void testSelect(){
		CountryState newCountryState = countryStateDao.select(countryState.getId());
		
		Assert.assertTrue(countryState.getName().equals(newCountryState.getName()));
		Assert.assertTrue(countryState.getCode().equals(newCountryState.getCode()));
	}
	
	@Test
	public void testSelectByCountryId(){
		int oldCnt = countryStateDao.selectByCountryId(countryState.getCountryId()).size();
		CountryState newCountryState = new CountryState();
		newCountryState.setCode("BC");
		newCountryState.setCountryId(39);
		newCountryState.setName("British Columbia");
		
		countryStateDao.insert(newCountryState);
		
		int newCnt = countryStateDao.selectByCountryId(countryState.getCountryId()).size();
		
		Assert.assertTrue((oldCnt + 1) == newCnt);
		
		countryStateDao.delete(newCountryState.getId());
	}
	
	@Test
	public void testUpdate(){
		Timestamp oldUpdateTime = countryStateDao.select(countryState.getId()).getGmtUpdate();

		//更新Code字段的值
		countryState.setCode("AA");
		
		try{
        	Thread.sleep(1000 * 2);
        }catch(InterruptedException e){
        	e.printStackTrace();
        }
		
		countryStateDao.update(countryState);
		
		Timestamp newUpdateTime = countryStateDao.select(countryState.getId()).getGmtUpdate();
		
		Assert.assertNotNull(oldUpdateTime);
		Assert.assertNotNull(newUpdateTime);
		
		Assert.assertFalse(newUpdateTime.toString().equals(oldUpdateTime.toString()));
	}
	
	@After
	public void testDelete(){
		countryStateDao.delete(countryState.getId());
		CountryState newCountryState = countryStateDao.select(countryState.getId());
		Assert.assertNull(newCountryState);
	}
}
