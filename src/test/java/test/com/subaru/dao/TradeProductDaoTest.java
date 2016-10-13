package test.com.subaru.dao;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.CateDao;
import com.subaru.maneki.dao.SkuDao;
import com.subaru.maneki.dao.SpuDao;
import com.subaru.maneki.dao.TradeProductDao;
import com.subaru.maneki.model.Cate;
import com.subaru.maneki.model.Sku;
import com.subaru.maneki.model.Spu;
import com.subaru.maneki.model.TradeProduct;
import com.subaru.core.BaseTestCaseJunit;

public class TradeProductDaoTest extends BaseTestCaseJunit {

    @Resource
    private SkuDao          skuDao;

    @Resource
    private SpuDao          spuDao;

    @Resource
    private CateDao         cateDao;

    @Resource
    private TradeProductDao tradeProductDao;

    private Sku             sku;

    private Spu             spu;

    private Cate            cate;

    private TradeProduct    tradeProduct;

    private long            orderNumber = 123456654321L;

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
        //        spu.setIsPublished(true);
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

        tradeProduct = new TradeProduct();
        tradeProduct.setOrderNumber(orderNumber);
        tradeProduct.setSkuId(sku.getId());
        tradeProduct.setQuantity(1);
        tradeProduct.setProductName(spu.getName());
        tradeProduct.setImageUrl("http//www.imageTest.com/");
        tradeProduct.setOriginalPrice(sku.getPrice());
        tradeProduct.setBuyedPrice(sku.getPrice() - 100);
        tradeProductDao.insert(tradeProduct);

    }

    @Test
    public void testSelectAndInsert() {
        TradeProduct newTradeProduct = tradeProductDao.select(tradeProduct.getId());
        Assert.assertTrue(tradeProduct.getOrderNumber() == newTradeProduct.getOrderNumber());
        Assert.assertTrue(tradeProduct.getSkuId() == newTradeProduct.getSkuId());
        Assert.assertTrue(tradeProduct.getQuantity() == newTradeProduct.getQuantity());
        Assert.assertTrue(tradeProduct.getProductName().equals(newTradeProduct.getProductName()));
        Assert.assertTrue(tradeProduct.getImageUrl().equals(newTradeProduct.getImageUrl()));
        Assert.assertTrue(tradeProduct.getOriginalPrice() == newTradeProduct.getOriginalPrice());
        Assert.assertTrue(tradeProduct.getBuyedPrice() == newTradeProduct.getBuyedPrice());
    }

    @Test
    public void testUpdate() {
        cate.setName("cateName2");
        cate.setParentId(1);
        cateDao.insert(cate);

        spu.setName("spuName2");
        spu.setCateId(cate.getId());
        //        spu.setIsPublished(true);
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
        skuDao.update(sku);

        tradeProduct.setOrderNumber(12344321L);
        tradeProduct.setSkuId(sku.getId());
        tradeProduct.setQuantity(2);
        tradeProduct.setProductName(spu.getName());
        tradeProduct.setImageUrl("http://www.newImage.net");
        tradeProduct.setOriginalPrice(sku.getPrice());
        tradeProduct.setBuyedPrice(sku.getPrice() - 5);
        tradeProductDao.update(tradeProduct);

        TradeProduct newTradeProduct = tradeProductDao.select(tradeProduct.getId());
        Assert.assertTrue(tradeProduct.getOrderNumber() == newTradeProduct.getOrderNumber());
        Assert.assertTrue(tradeProduct.getSkuId() == newTradeProduct.getSkuId());
        Assert.assertTrue(tradeProduct.getQuantity() == newTradeProduct.getQuantity());
        Assert.assertTrue(tradeProduct.getProductName().equals(newTradeProduct.getProductName()));
        Assert.assertTrue(tradeProduct.getImageUrl().equals(newTradeProduct.getImageUrl()));
        Assert.assertTrue(tradeProduct.getOriginalPrice() == newTradeProduct.getOriginalPrice());
        Assert.assertTrue(tradeProduct.getBuyedPrice() == newTradeProduct.getBuyedPrice());
    }

    @Test
    public void testDelete() {
        tradeProductDao.delete(tradeProduct.getId());
        TradeProduct newTradeProduct = tradeProductDao.select(tradeProduct.getId());
        Assert.assertNull(newTradeProduct);
    }

    @Test
    public void testSelectByOrderNumber() {
        List<TradeProduct> tradeProductList = tradeProductDao.selectByOrderNumber(orderNumber);
        Assert.assertTrue(tradeProductList.size() > 0);
        TradeProduct first = tradeProductList.get(0);
        Assert.assertTrue(tradeProduct.getSkuId() == first.getSkuId());
        Assert.assertTrue(tradeProduct.getQuantity() == first.getQuantity());
        Assert.assertTrue(tradeProduct.getProductName().equals(first.getProductName()));
        Assert.assertTrue(tradeProduct.getImageUrl().equals(first.getImageUrl()));
        Assert.assertTrue(tradeProduct.getOriginalPrice() == first.getOriginalPrice());
        Assert.assertTrue(tradeProduct.getBuyedPrice() == first.getBuyedPrice());
    }

}
