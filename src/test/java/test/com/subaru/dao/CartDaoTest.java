package test.com.subaru.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.*;
import com.subaru.maneki.enumeration.RegisterType;
import com.subaru.maneki.model.*;
import com.subaru.core.BaseTestCaseJunit;
import com.subaru.core.encryption.MD5;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
public class CartDaoTest extends BaseTestCaseJunit {

    @Resource
    private UserDao userDao;

    @Resource
    private SkuDao  skuDao;

    @Resource
    private SpuDao  spuDao;

    @Resource
    private CateDao cateDao;

    private Sku     sku;

    private Spu     spu;

    private Cate    cate;

    private User    user;

    @Resource
    private CartDao cartDao;

    private Cart    cart;

    @Before
    public void setUp() throws Exception {

        user = new User();
        user.setEmail("chaojiezhang@epiclouds.net");
        user.setCellphone("18258477020");
        user.setPassword(MD5.MakeMD5("password"));
        user.setNick("zcj");
        user.setRegisterType(RegisterType.EMAIL.getValue());
        userDao.insert(user);

        cate = new Cate();
        cate.setName("cateName");
        cate.setParentId(0);
        cate.setOldId(123);
        cateDao.insert(cate);

        spu = new Spu();
        spu.setName("spuName");
        spu.setCateId(cate.getId());
        //        spu.setIsPublished(true);
        spu.setProductNo("1234abcd");
        spu.setSearchKeywords("spuSearch");
        spu.setComment("comment");
        spu.setScore("5.4");
        spu.setClusterId(12344321);
        spu.setOldId(123);
        spuDao.insert(spu);

        sku = new Sku();
        sku.setSpuId(spu.getId());
        sku.setPrice(5.0);
        sku.setWeight(90);
        sku.setCurrencyUnit("$");
        sku.setInventory(100);
        sku.setIsSaleOk(true);
        skuDao.insert(sku);

        cart = new Cart();
        cart.setSkuId(sku.getId());
        cart.setQuantity(1);
        cart.setUserId(user.getId());
        cartDao.insert(cart);
    }

    @Test
    public void testSelectAndInsert() {
        Cart tradeCartNew = cartDao.select(cart.getId());
        Assert.assertEquals(tradeCartNew.getId(), cart.getId());
        Assert.assertTrue(tradeCartNew.getSkuId() == cart.getSkuId());
        Assert.assertTrue(tradeCartNew.getUserId() == cart.getUserId());
    }

    @Test
    public void testUpdate() {
        user = new User();
        user.setEmail("chaojiezhang@epiclouds.net");
        user.setCellphone("18258477020");
        user.setPassword(MD5.MakeMD5("password2"));
        user.setNick("zcj2");
        user.setRegisterType(RegisterType.EMAIL.getValue());
        userDao.insert(user);

        sku = new Sku();
        sku.setSpuId(spu.getId());
        sku.setPrice(10.0);
        sku.setWeight(99);
        sku.setCurrencyUnit("$");
        sku.setInventory(999);
        sku.setIsSaleOk(false);
        skuDao.insert(sku);

        cart.setSkuId(sku.getId());
        cart.setQuantity(2);
        cart.setUserId(user.getId());
        cartDao.update(cart);

        Cart tradeCartNew = cartDao.select(cart.getId());
        Assert.assertEquals(tradeCartNew.getId(), cart.getId());
        Assert.assertTrue(tradeCartNew.getSkuId() == cart.getSkuId());
        Assert.assertTrue(tradeCartNew.getUserId() == cart.getUserId());
        Assert.assertTrue(tradeCartNew.getQuantity() == cart.getQuantity());

    }

    @Test
    public void testDelete() {
        Assert.assertNotNull(cartDao.select(cart.getId()));
        cartDao.delete(cart.getId());
        Assert.assertNull(cartDao.select(cart.getId()));
    }

    @Test
    public void testDeleteByUserIdAndSkuId() {
        Assert.assertNotNull(cartDao.selectByUserIdAndSkuId(cart.getSkuId(), cart.getUserId()));
        cartDao.deleteByUserIdAndSkuId(cart.getUserId(), cart.getSkuId());
        Assert.assertNull(cartDao.selectByUserIdAndSkuId(cart.getSkuId(), cart.getUserId()));
    }

    @Test
    public void testCartSkus() {
        Assert.assertTrue(cartDao.countCartSkus(sku.getId()) >= 1);
    }

    @Test
    public void testSelectByUserIdAndSkuId() {
        Cart tradeCartNew = cartDao.selectByUserIdAndSkuId(cart.getSkuId(), cart.getUserId());
        Assert.assertEquals(tradeCartNew.getSkuId(), cart.getSkuId());
        Assert.assertEquals(tradeCartNew.getUserId(), cart.getUserId());
    }

    @Test
    public void testSelectUserCart() {
        List<Cart> tradeCarts = cartDao.selectUserCart(cart.getUserId());
        for (Cart tradeCartNew : tradeCarts) {
            Assert.assertEquals(tradeCartNew.getUserId(), cart.getUserId());
        }
    }

}
