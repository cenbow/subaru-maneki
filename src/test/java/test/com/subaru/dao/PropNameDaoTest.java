package test.com.subaru.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.PropNameDao;
import com.subaru.maneki.model.PropName;
import com.subaru.core.BaseTestCaseJunit;

/**
 * @author zhangchaojie
 * @since 2016-08-24
 */
public class PropNameDaoTest extends BaseTestCaseJunit {

    @Resource
    private PropNameDao propNameDao;

    private PropName    propName;

    @Before
    public void setUp() {

        propName = new PropName();
        propName.setName("propName");
        propName.setParentId(0);
        propNameDao.insert(propName);

    }

    @Test
    public void testSelectAndInsert() {
        PropName selectedPropName = propNameDao.select(propName.getId());
        Assert.assertTrue(selectedPropName.getName().equals(propName.getName()));
        Assert.assertTrue(selectedPropName.getParentId() == propName.getParentId());
    }

    @Test
    public void testSelectByParentAndName() {
        PropName selectedPropName = propNameDao.selectByParentAndName(0, propName.getName());
        Assert.assertTrue(selectedPropName.getName().equals(propName.getName()));
        Assert.assertTrue(selectedPropName.getParentId() == propName.getParentId());
    }

    @Test
    public void testSelectByParent() {
        List<PropName> subPropNameList = propNameDao.selectByParent(0);
        Assert.assertTrue(subPropNameList.size() > 0);
    }

    @Test
    public void testUpdate() {

        propName.setName("PropName2");
        propName.setParentId(1);
        propNameDao.update(propName);

        PropName updatedPropName = propNameDao.select(propName.getId());
        Assert.assertTrue(updatedPropName.getName().equals(propName.getName()));
        Assert.assertTrue(updatedPropName.getParentId() == propName.getParentId());

    }

    @Test
    public void testDelete() {
        propNameDao.delete(propName.getId());
        PropName deletedPropName = propNameDao.select(propName.getId());
        Assert.assertNull(deletedPropName);
    }

}
