package test.com.subaru.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.CateDao;
import com.subaru.maneki.dao.PropViewDao;
import com.subaru.maneki.dao.SpuDao;
import com.subaru.maneki.model.Cate;
import com.subaru.maneki.model.PropView;
import com.subaru.maneki.model.Spu;
import com.subaru.core.BaseTestCaseJunit;

/**
 * @author zhangchaojie
 * @since 2016-08-24
 */
public class PropViewDaoTest extends BaseTestCaseJunit {

    @Resource
    private PropViewDao propViewDao;

    @Resource
    private SpuDao      spuDao;

    @Resource
    private CateDao     cateDao;

    private PropView    propView;

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

        propView = new PropView();
        propView.setSpuId(spu.getId());
        propView.setSpuPropJson("SpuPropJson");
        propView.setSkuPropJson("SkuPropJson");
        propViewDao.insert(propView);

    }

    @Test
    public void testSelectAndInsert() {
        PropView selectedPropView = propViewDao.select(propView.getSpuId());
        Assert.assertTrue(selectedPropView.getSpuId() == propView.getSpuId());
        Assert.assertTrue(selectedPropView.getSpuPropJson().equals(propView.getSpuPropJson()));
        Assert.assertTrue(selectedPropView.getSkuPropJson().equals(propView.getSkuPropJson()));
    }

    @Test
    public void testUpdate() {

        propView.setSpuPropJson("SpuPropJson2");
        propView.setSkuPropJson("SkuPropJson2");
        propViewDao.update(propView);

        PropView updatedPropView = propViewDao.select(propView.getSpuId());
        Assert.assertTrue(updatedPropView.getSpuPropJson().equals(propView.getSpuPropJson()));
        Assert.assertTrue(updatedPropView.getSkuPropJson().equals(propView.getSkuPropJson()));

    }

    @Test
    public void testDelete() {
        propViewDao.delete(propView.getSpuId());
        PropView deletedPropView = propViewDao.select(propView.getSpuId());
        Assert.assertNull(deletedPropView);
    }

}
