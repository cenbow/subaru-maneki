package test.com.subaru.dao;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.CateDao;
import com.subaru.maneki.model.Cate;
import com.subaru.core.BaseTestCaseJunit;

public class CateDaoTest extends BaseTestCaseJunit{
	@Resource
	public CateDao cateDao;
	
	public Cate cate;
	
	@Before
    public void Setup() {
		String name = "Clothing";
		int parentId = 0;
		int sequence = 5;
		
		cate = new Cate(parentId, name, sequence);
		
		cateDao.insert(cate);
	}
	
	@Test
	public void testSelect(){
		Cate newCate = cateDao.select(cate.getId());
		
		Assert.assertTrue(newCate.getName().equals(cate.getName()));
		Assert.assertTrue(newCate.getParentId() == cate.getParentId());
		Assert.assertTrue(newCate.getSequence() == cate.getSequence());
	}
	
	@Test
	public void testUpdate(){
		Timestamp oldUpdateTime = cateDao.select(cate.getId()).getGmtUpdate();
		
		//等待两秒后再插入
		try{
        	Thread.sleep(1000 * 2);
        }catch(InterruptedException e){
        	e.printStackTrace();
        }
		
		cate.setName("Shoes");
		cateDao.update(cate);
		
		Timestamp newUpdateTime = cateDao.select(cate.getId()).getGmtUpdate();
		
		Assert.assertFalse(newUpdateTime.toString().equals(oldUpdateTime.toString()));
		
	}
}
