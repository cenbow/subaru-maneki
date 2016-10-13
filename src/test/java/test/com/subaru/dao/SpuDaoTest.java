package test.com.subaru.dao;

import javax.annotation.Resource;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.CateDao;
import com.subaru.maneki.dao.SpuDao;
import com.subaru.maneki.model.Cate;
import com.subaru.maneki.model.Spu;
import com.subaru.core.BaseTestCaseJunit;

public class SpuDaoTest extends BaseTestCaseJunit{
	
	@Resource
	private SpuDao spuDao;
	
	@Resource
	private CateDao cateDao;
	
	private Spu spu;
	
	private Cate cate;
	
	@Before
    public void Setup() {
		cate = new Cate();
        cate.setName("clothing");
        cate.setParentId(0);
		cate.setOldId(1);
        cateDao.insert(cate);
        
        spu= new Spu();

        spu.setCateId(cate.getId());
        spu.setClusterId(784780);
        spu.setIsPublished(true);
        spu.setName("Twist Sheath Slim Gray O Neck Casual Knitted Sweater Dress");
        spu.setProductNo("CBS000101633N");
        spu.setScore("5.0/5.0");
        spu.setSearchKeywords("High Quality Women Twist Sweater Dress Autumn Winter New Arrival Fashion Sheath Slim Gray O Neck Casual Knitted Sweater Dress");
        spu.setComment("[{\"buyer\": \"Elena A.\", \"rating\": \"4.0\", \"images\": "
        		+ "[{\"s\": \"http://g02.a.alicdn.com/kf/UT8qYj1XsxaXXagOFbXz.jpg_50x50.jpg\", \"b\": \"http://g02.a.alicdn.com/kf/UT8qYj1XsxaXXagOFbXz.jpg_350x350.jpg\"}, {\"s\": \"http://g04.a.alicdn.com/kf/UT8zYv0XEdaXXagOFbXL.jpg_50x50.jpg\", {\"buyer\": \"A***a J.\", \"rating\": \"5.0\", \"images\": [], \"feedback\": \"The bike is good. very soft. I recommend at this price\", \"time\": \"15 Apr 2016 05:10\"}]");
        spu.setOldId(1);
		spuDao.insert(spu);

    }
	
	@Test
	public void testSelect(){
		Spu newSpu = spuDao.select(spu.getId());
		Assert.assertTrue(newSpu.getComment().equals(spu.getComment()));
		Assert.assertTrue(newSpu.getIsPublished()==spu.getIsPublished());
	}
	
	@Test
	public void testSelectByLimit(){
		int start = 0;
		int limit = 10;
		
		List<Spu> spuList = spuDao.selectByLimit(start, limit);
		Assert.assertTrue(spuList != null);
	}
	
	//test selectCountAll AND delete
	@Test
	public void testCountAll(){
		boolean isPublished = true;
		int oldCount = spuDao.countAll(isPublished);
		
		//insert a record
		Spu newSpu= new Spu();

		newSpu.setCateId(spu.getCateId());
		newSpu.setClusterId(784781);
		newSpu.setIsPublished(true);
		newSpu.setName("Twist Sheath Slim Gray O Neck Casual Knitted Sweater Dress");
		newSpu.setProductNo("CBS000101632N");
		newSpu.setScore("4.5/5.0");
		newSpu.setSearchKeywords("Twist Sweater Dress Autumn Winter New Arrival Fashion Sheath Slim Gray O Neck Casual Knitted Sweater Dress");
		newSpu.setComment("[{\"buyer\": \"Elena A.\", \"rating\": \"4.0\", \"images\": "
        		+ "[{\"s\": \"http://g02.a.alicdn.com/kf/UT8qYj1XsxaXXagOFbXz.jpg_50x50.jpg\", \"b\": \"http://g02.a.alicdn.com/kf/UT8qYj1XsxaXXagOFbXz.jpg_350x350.jpg\"}, {\"s\": \"http://g04.a.alicdn.com/kf/UT8zYv0XEdaXXagOFbXL.jpg_50x50.jpg\", {\"buyer\": \"A***a J.\", \"rating\": \"5.0\", \"images\": [], \"feedback\": \"The bike is good. very soft. I recommend at this price\", \"time\": \"15 Apr 2016 05:10\"}]");
		newSpu.setOldId(2);
		spuDao.insert(newSpu);
        
        int newCount = spuDao.countAll(isPublished);
        Assert.assertTrue(oldCount + 1 == newCount);
        
        spuDao.delete(newSpu.getId());
	}
	
	//test selectCountAll AND delete
	@Test
	public void testCountByCate(){
		boolean isPublished = true;
		int cateId = cate.getId();
		int oldCount = spuDao.countByCate(cateId, isPublished);
		
		//insert a record
		Spu newSpu= new Spu();

		newSpu.setCateId(spu.getCateId());
		newSpu.setClusterId(784781);
		newSpu.setIsPublished(true);
		newSpu.setName("Twist Sheath Slim Gray O Neck Casual Knitted Sweater Dress");
		newSpu.setProductNo("CBS000101632N");
		newSpu.setScore("4.5/5.0");
		newSpu.setSearchKeywords("Twist Sweater Dress Autumn Winter New Arrival Fashion Sheath Slim Gray O Neck Casual Knitted Sweater Dress");
		newSpu.setComment("[{\"buyer\": \"Elena A.\", \"rating\": \"4.0\", \"images\": "
        		+ "[{\"s\": \"http://g02.a.alicdn.com/kf/UT8qYj1XsxaXXagOFbXz.jpg_50x50.jpg\", \"b\": \"http://g02.a.alicdn.com/kf/UT8qYj1XsxaXXagOFbXz.jpg_350x350.jpg\"}, {\"s\": \"http://g04.a.alicdn.com/kf/UT8zYv0XEdaXXagOFbXL.jpg_50x50.jpg\", {\"buyer\": \"A***a J.\", \"rating\": \"5.0\", \"images\": [], \"feedback\": \"The bike is good. very soft. I recommend at this price\", \"time\": \"15 Apr 2016 05:10\"}]");
        newSpu.setOldId(2);
		spuDao.insert(newSpu);
        
        int newCount = spuDao.countByCate(cateId, isPublished);
      
        Assert.assertTrue(oldCount + 1 == newCount);
        
        spuDao.delete(newSpu.getId());
	}
	
	
	@Test
	public void testSelectByCate(){
		int cateId = spu.getCateId();
		int start = 0;
		int limit = 10;
		List<Spu> spuList = spuDao.selectByCate(cateId, start, limit);
		Assert.assertTrue(spuList.size() >= 1);
		
		//如果start = 0, limit<=0，那么默认把所有符合的记录都会取出来
		limit = 0;
		spuList = spuDao.selectByCate(cateId, start, limit);
		Assert.assertTrue(spuList.size() > 0);
		
		//如果start < 0, limit > 0，那么会取出满足limit个的记录出来
		start = -1;
		limit = 1;
		spuList = spuDao.selectByCate(cateId, start, limit);
		Assert.assertTrue(spuList.size() > 0);
	}
	
	@Test
	public void testUpdate(){
		Timestamp oldUpdateTime = spuDao.select(spu.getId()).getGmtUpdate();
		
		spu.setIsPublished(false);
		
		try{
        	Thread.sleep(1000 * 2);
        }catch(InterruptedException e){
        	e.printStackTrace();
        }
		
		spuDao.update(spu);
		
		Timestamp newUpdateTime = spuDao.select(spu.getId()).getGmtUpdate();
		Assert.assertFalse(newUpdateTime.toString().equals(oldUpdateTime.toString()));
	}
}
