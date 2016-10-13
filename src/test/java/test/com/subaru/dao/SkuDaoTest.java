package test.com.subaru.dao;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.subaru.core.BaseTestCaseJunit;
import com.subaru.maneki.dao.CateDao;
import com.subaru.maneki.dao.SkuDao;
import com.subaru.maneki.dao.SpuDao;
import com.subaru.maneki.model.Cate;
import com.subaru.maneki.model.Sku;
import com.subaru.maneki.model.Spu;

public class SkuDaoTest extends BaseTestCaseJunit {

    @Resource
    private SkuDao  skuDao;

    @Resource
    private SpuDao  spuDao;

    @Resource
    private CateDao cateDao;

    private Sku     sku;

    private Spu     spu;

    private Cate    cate;

    @Before
    public void setUp() {

        cate = new Cate();
        cate.setName("cateName");
        cate.setParentId(0);
        cate.setOldId(1);
        cateDao.insert(cate);

        spu = new Spu();
        spu.setName("spuName");
        spu.setCateId(cate.getId());
        spu.setOldId(1);
        spu.setProductNo("1234abcd");
        spu.setSearchKeywords("spuSearch");
        spu.setComment("comment");
        spu.setScore("5.4");
        spu.setClusterId(12344321);
        spuDao.insert(spu);

        sku = new Sku();
        sku.setSpuId(spu.getId());
        sku.setPrice(5.0);
        sku.setWeight(90);
        sku.setCurrencyUnit("$");
        sku.setInventory(100);
        sku.setIsSaleOk(true);
        sku.setOldId(1);
        sku.setPropColor("red");
        sku.setPropSize("38.0");
        sku.setPropStyle("slim");
        sku.setPropMetalColor("mc");
        sku.setPropCColor("cc");
        skuDao.insert(sku);
    }

    @Test
    public void testSelectAndInsert() {
        Sku newSku = skuDao.select(sku.getId());
        Assert.assertTrue(newSku.getSpuId() == sku.getSpuId());
        Assert.assertTrue(newSku.getPrice() == sku.getPrice());
        Assert.assertTrue(newSku.getWeight() == sku.getWeight());
        Assert.assertTrue(newSku.getCurrencyUnit().equals(sku.getCurrencyUnit()));
        Assert.assertTrue(newSku.getInventory() == sku.getInventory());
        Assert.assertTrue(newSku.getIsSaleOk() == sku.getIsSaleOk());
        Assert.assertTrue(newSku.getPropColor().equals(sku.getPropColor()));
        Assert.assertTrue(newSku.getPropSize().equals(sku.getPropSize()));
        Assert.assertTrue(newSku.getPropStyle().equals(sku.getPropStyle()));
        Assert.assertTrue(newSku.getPropMetalColor().equals(sku.getPropMetalColor()));
        Assert.assertTrue(newSku.getPropCColor().equals(sku.getPropCColor()));
    }

    @Test
    public void testUpdate() {
        cate.setName("cateName2");
        cate.setParentId(1);
        cateDao.insert(cate);

        spu.setName("spuName2");
        spu.setCateId(cate.getId());
        spu.setIsPublished(true);
        spu.setProductNo("5678efgh");
        spu.setSearchKeywords("spuSearch2");
        spu.setComment("comment2");
        spu.setScore("9.0");
        spu.setClusterId(56788765);
        spuDao.insert(spu);

        sku.setSpuId(spu.getId());
        sku.setPrice(30.0);
        sku.setWeight(200);
        sku.setCurrencyUnit("￥");
        sku.setInventory(999);
        sku.setIsSaleOk(false);
        sku.setPropColor("blue");
        sku.setPropSize("40.0");
        sku.setPropStyle("newStyle");
        sku.setPropMetalColor("newmc");
        sku.setPropCColor("newcc");
        skuDao.update(sku);

        Sku newSku = skuDao.select(sku.getId());
        Assert.assertTrue(newSku.getSpuId() == sku.getSpuId());
        Assert.assertTrue(newSku.getPrice() == sku.getPrice());
        Assert.assertTrue(newSku.getWeight() == sku.getWeight());
        Assert.assertTrue(newSku.getCurrencyUnit().equals(sku.getCurrencyUnit()));
        Assert.assertTrue(newSku.getInventory() == sku.getInventory());
        Assert.assertTrue(newSku.getIsSaleOk() == sku.getIsSaleOk());
        Assert.assertTrue(newSku.getPropColor().equals(sku.getPropColor()));
        Assert.assertTrue(newSku.getPropSize().equals(sku.getPropSize()));
        Assert.assertTrue(newSku.getPropStyle().equals(sku.getPropStyle()));
        Assert.assertTrue(newSku.getPropMetalColor().equals(sku.getPropMetalColor()));
        Assert.assertTrue(newSku.getPropCColor().equals(sku.getPropCColor()));
    }

    @Test
    public void testDelete() {
        skuDao.delete(sku.getId());
        Sku newSku = skuDao.select(sku.getId());
        Assert.assertNull(newSku);
    }

    @Test
    public void testCountAll() {

        int skuNum = skuDao.countAll(true);

        cate.setName("cateName2");
        cate.setParentId(1);
        cateDao.insert(cate);

        spu.setName("spuName2");
        spu.setCateId(cate.getId());
        spu.setIsPublished(true);
        spu.setProductNo("5678efgh");
        spu.setSearchKeywords("spuSearch2");
        spu.setComment("comment2");
        spu.setScore("9.0");
        spu.setClusterId(56788765);
        spuDao.insert(spu);

        sku.setSpuId(spu.getId());
        sku.setPrice(30.0);
        sku.setWeight(200);
        sku.setCurrencyUnit("￥");
        sku.setInventory(999);
        sku.setIsSaleOk(true);
        sku.setPropColor("blue");
        sku.setPropSize("40.0");
        sku.setPropStyle("newStyle");
        sku.setPropMetalColor("newmc");
        sku.setPropCColor("newcc");
        skuDao.insert(sku);

        int newSkuNum = skuDao.countAll(true);
        Assert.assertTrue(newSkuNum == ++skuNum);

    }

    @Test
    public void testSelectBySpuId() {
        List<Sku> skus = skuDao.selectBySpuId(spu.getId());
        Assert.assertTrue(skus.size() > 0);
    }

}
