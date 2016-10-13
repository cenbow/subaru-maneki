package test.com.subaru.dao;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.CateDao;
import com.subaru.maneki.dao.SpuCateRelationDao;
import com.subaru.maneki.dao.SpuDao;
import com.subaru.maneki.model.Cate;
import com.subaru.maneki.model.Spu;
import com.subaru.maneki.model.SpuCateRelation;
import com.subaru.core.BaseTestCaseJunit;

public class SpuCateRelationDaoTest extends BaseTestCaseJunit{
	
	@Resource
	private SpuCateRelationDao spuCateRelationDao;
	
	@Resource
	private SpuDao spuDao;
	
	@Resource
	private CateDao cateDao;
	
	private Spu spu;
	
	private SpuCateRelation spuCateRelation;
	
	private Cate cate;
	
	@Before
	public void setUp(){
		spu = new Spu();
		//虚设
		spu.setCateId(1);
		spu.setClusterId(1123234);
		
        spu.setIsPublished(true);
        spu.setName("Twist Sheath Slim Gray O Neck Casual Knitted Sweater Dress");
        spu.setProductNo("CBS000101633N");
        spu.setScore("5.0/5.0");
        spu.setOldId(1230);
        spu.setSearchKeywords("High Quality Women Twist Sweater Dress Autumn Winter New Arrival Fashion Sheath Slim Gray O Neck Casual Knitted Sweater Dress");
        spu.setComment("[{\"buyer\": \"Elena A.\", \"rating\": \"4.0\", \"images\": "
        		+ "[{\"s\": \"http://g02.a.alicdn.com/kf/UT8qYj1XsxaXXagOFbXz.jpg_50x50.jpg\", \"b\": \"http://g02.a.alicdn.com/kf/UT8qYj1XsxaXXagOFbXz.jpg_350x350.jpg\"},"
        		+ " {\"s\": \"http://g04.a.alicdn.com/kf/UT8zYv0XEdaXXagOFbXL.jpg_50x50.jpg\", {\"buyer\": \"A***a J.\", \"rating\": \"5.0\", \"images\": "
        		+ "[], \"feedback\": \"The bike is good. very soft. I recommend at this price\", \"time\": \"15 Apr 2016 05:10\"}]");
        spuDao.insert(spu);
        
        cate = new Cate();
        cate.setName("clothing");
        cate.setOldId(12334);
        cate.setParentId(1);
        cate.setSequence(1);
        cateDao.insert(cate);
        
        spuCateRelation = new SpuCateRelation();
        spuCateRelation.setCateId(cate.getId());
        spuCateRelation.setSpuId(spu.getId());
        spuCateRelationDao.insert(spuCateRelation);
	}
	
	@Test
	public void testSelect(){
		SpuCateRelation scr = spuCateRelationDao.select(spu.getId(), cate.getId());
		Assert.assertNotNull(scr);
		
		Cate newCate = cateDao.select(scr.getCateId());
		Assert.assertTrue(newCate.getName().equals(cate.getName()));
		
		Spu newSpu = spuDao.select(scr.getSpuId());
		Assert.assertTrue(newSpu.getName().equals(spu.getName()));
	}
	
	@Test
	public void testUpdate(){
		SpuCateRelation scr = spuCateRelationDao.select(spu.getId(), cate.getId());
		
		Timestamp oldUpdateTime = scr.getGmtUpdate();
		
		//等待两秒后再插入
		try{
        	Thread.sleep(1000 * 2);
        }catch(InterruptedException e){
        	e.printStackTrace();
        }
		
		Cate newCate = new Cate();
		newCate.setName("dress");
		newCate.setOldId(12334);
		newCate.setParentId(1);
		newCate.setSequence(1);
        cateDao.insert(newCate);
        
        spuCateRelationDao.update(spu.getId(), newCate.getId(), spu.getId(), cate.getId());
        scr = spuCateRelationDao.select(spu.getId(), newCate.getId());
        
        Timestamp newUpdateTime = scr.getGmtUpdate();
        
        Assert.assertFalse(oldUpdateTime.equals(newUpdateTime));
	}
}
