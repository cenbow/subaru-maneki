package test.com.subaru.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import com.subaru.maneki.dao.SpuDao;
import com.subaru.maneki.dao.UserDao;
import com.subaru.maneki.dao.WishlistDao;
import com.subaru.maneki.model.User;
import com.subaru.maneki.model.Spu;
import com.subaru.maneki.model.Wishlist;
import com.subaru.core.BaseTestCaseJunit;

public class WishlistDaoTest extends BaseTestCaseJunit{
	
	@Resource
	public WishlistDao wishlistDao;
	
	@Resource
	public UserDao 		userDao;
	
	@Resource
	public SpuDao		spuDao;
	
	public Wishlist wishlist;
	
	public User 	user;
	
	public Spu		spu;
	
	@Before
	public void Setup(){
		user = new User();
		spu = new Spu();
		user.setCellphone("1234567890");
		user.setEmail("test@test.com");
		user.setNick("hello");
		user.setPassword("123455");
		user.setRegisterType(1);
		
		userDao.insert(user);
		
		spu.setCateId(1);
		spu.setClusterId(10234);
		spu.setComment("this is a good");
		spu.setIsPublished(true);
		spu.setName("good");
		spu.setOldId(1239);
		spu.setProductNo("SK2300304004");
		spu.setScore("4/5");
		spu.setSearchKeywords("good");
		spu.setSold(12);
		
		spuDao.insert(spu);
		
		wishlist = new Wishlist();
		wishlist.setSpuId(spu.getId());
		wishlist.setUserId(user.getId());
		
		wishlistDao.insert(wishlist);
	}
	
	@Test
	public void testSelect(){
		Wishlist newWishlist = wishlistDao.select(wishlist.getId());
		
		Assert.assertTrue(wishlist.getSpuId() == newWishlist.getSpuId());
		Assert.assertTrue(wishlist.getUserId() == newWishlist.getUserId());
	}
	
	@Test
	public void testInsert(){
		int originCnt = wishlistDao.selectByUserId(wishlist.getUserId()).size();
		
		//insert a new record with the same userId
		//为了测试方便，以下的spuId的随机生成
		Wishlist newWishlist = new Wishlist();
		newWishlist.setSpuId(2340);
		newWishlist.setUserId(user.getId());
		wishlistDao.insert(newWishlist);
		
		int newCnt = wishlistDao.selectByUserId(user.getId()).size();
		
		Assert.assertTrue(originCnt + 1 == newCnt);
		
		wishlistDao.delete(newWishlist.getId());
	}
	
	@After
	public void testDelete(){
		int userId = user.getId();
		int spuId = spu.getId();
		wishlistDao.delete(wishlist.getId());
		Assert.assertNull(wishlistDao.select(wishlist.getId()));
		
		//userDao.delete(userId);
		spuDao.delete(spuId);
	}
}
