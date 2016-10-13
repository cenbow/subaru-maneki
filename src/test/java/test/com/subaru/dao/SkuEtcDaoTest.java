package test.com.subaru.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.CateDao;
import com.subaru.maneki.dao.SkuDao;
import com.subaru.maneki.dao.SkuEtcDao;
import com.subaru.maneki.dao.SpuDao;
import com.subaru.maneki.model.Cate;
import com.subaru.maneki.model.Sku;
import com.subaru.maneki.model.SkuEtc;
import com.subaru.maneki.model.Spu;
import com.subaru.core.BaseTestCaseJunit;

/**
 * @author zhangchaojie
 * @since 2016-08-24
 */
public class SkuEtcDaoTest extends BaseTestCaseJunit {

    @Resource
    private SkuDao    skuDao;

    @Resource
    private SpuDao    spuDao;

    @Resource
    private CateDao   cateDao;

    @Resource
    private SkuEtcDao skuEtcDao;

    private Spu       spu;

    private Cate      cate;

    private Sku       sku;

    private SkuEtc    skuEtc;

    @Before
    public void setUp() {

        Cate cate = new Cate();
        cate.setName("cateName");
        cate.setParentId(0);
        cate.setOldId(1);
        cateDao.insert(cate);

        Spu spu = new Spu();
        spu.setName("spuName");
        spu.setCateId(cate.getId());
        spu.setIsPublished(true);
        spu.setProductNo("1234abcd");
        spu.setSearchKeywords("spuSearch");
        spu.setComment("comment");
        spu.setScore("5.4");
        spu.setClusterId(12344321);
        spu.setOldId(1);
        spuDao.insert(spu);

        sku = new Sku();
        sku.setSpuId(spu.getId());
        sku.setPrice(5.0);
        sku.setWeight(90);
        sku.setCurrencyUnit("$");
        sku.setInventory(100);
        sku.setIsSaleOk(true);
        sku.setOldId(1);
        skuDao.insert(sku);

        skuEtc = new SkuEtc();
        skuEtc.setSkuId(sku.getId());
        skuEtc.setPlatformPrice("60");
        skuEtc.setPlatformUrl("url");
        skuEtc.setPlatformPriceDetails("details");
        skuEtc.setPlatformName("wish");
        skuEtc.setUrlOf1688("urlof1688");
        skuEtcDao.insert(skuEtc);

    }

    @Test
    public void testSelectAndInsert() {
        SkuEtc selectedSkuEtc = skuEtcDao.selectBySkuId(skuEtc.getSkuId());
        Assert.assertTrue(selectedSkuEtc.getSkuId() == skuEtc.getSkuId());
        Assert.assertTrue(selectedSkuEtc.getPlatformPrice().equals(skuEtc.getPlatformPrice()));
        Assert.assertTrue(selectedSkuEtc.getPlatformUrl().equals(skuEtc.getPlatformUrl()));
        Assert.assertTrue(selectedSkuEtc.getPlatformPriceDetails().equals(
            skuEtc.getPlatformPriceDetails()));
        Assert.assertTrue(selectedSkuEtc.getPlatformName().equals(skuEtc.getPlatformName()));
        Assert.assertTrue(selectedSkuEtc.getUrlOf1688().equals(skuEtc.getUrlOf1688()));
    }

    @Test
    public void testUpdate() {

        skuEtc.setPlatformPrice("600");
        skuEtc.setPlatformUrl("url2");
        skuEtc.setPlatformPriceDetails("details2");
        skuEtc.setPlatformName("amazon");
        skuEtc.setUrlOf1688("url2of1688");
        skuEtcDao.update(skuEtc);

        SkuEtc updatedSkuEtc = skuEtcDao.selectBySkuId(skuEtc.getSkuId());
        Assert.assertTrue(updatedSkuEtc.getPlatformPrice().equals(skuEtc.getPlatformPrice()));
        Assert.assertTrue(updatedSkuEtc.getPlatformUrl().equals(skuEtc.getPlatformUrl()));
        Assert.assertTrue(updatedSkuEtc.getPlatformPriceDetails().equals(
            skuEtc.getPlatformPriceDetails()));
        Assert.assertTrue(updatedSkuEtc.getPlatformName().equals(skuEtc.getPlatformName()));
        Assert.assertTrue(updatedSkuEtc.getUrlOf1688().equals(skuEtc.getUrlOf1688()));

    }

    @Test
    public void testDelete() {
        skuEtcDao.delete(skuEtc.getSkuId());
        SkuEtc deletedSkuEtc = skuEtcDao.selectBySkuId(skuEtc.getSkuId());
        Assert.assertNull(deletedSkuEtc);
    }

}
