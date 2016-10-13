package test.com.subaru.dao;
import java.sql.Timestamp;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.ImageDao;
import com.subaru.maneki.model.Image;
import com.subaru.core.BaseTestCaseJunit;

public class ImageDaoTest extends BaseTestCaseJunit{
	
	@Resource
	public ImageDao imageDao;
	
	public Image image;
	
	@Before
    public void Setup() {
		String url = "http://www.baidu.com";
		String smallUrl = url + "_20-20.jpg";
		String middleUrl = url + "_100-100.jpg";
		int sequence = 40;
		image = new Image(url, smallUrl, middleUrl, sequence);
		
		imageDao.insert(image);
	}
	
	@Test
	public void testSelect(){
		Image newImage = imageDao.select(image.getId());
		
		Assert.assertTrue(newImage.getUrl().equals(image.getUrl()));
		Assert.assertTrue(newImage.getMiddleUrl().equals(image.getMiddleUrl()));
		Assert.assertTrue(newImage.getSmallUrl().equals(image.getSmallUrl()));
		Assert.assertTrue(newImage.getSequence() == image.getSequence());
		Assert.assertTrue(newImage.getIsMain() == image.getIsMain());
	}
	
	@Test
	public void testUpdate(){
		Timestamp oldUpdateTime = imageDao.select(image.getId()).getGmtUpdate();
//		String newUrl = "http://www.163.com";
//		image.setUrl(newUrl);
		image.setIsMain(true);
		
		try{
        	Thread.sleep(1000 * 2);
        }catch(InterruptedException e){
        	e.printStackTrace();
        }
		
		imageDao.update(image);
		
		Timestamp newUpdateTime = imageDao.select(image.getId()).getGmtUpdate();
		
		Assert.assertFalse(newUpdateTime.toString().equals(oldUpdateTime.toString()));
	}
}
