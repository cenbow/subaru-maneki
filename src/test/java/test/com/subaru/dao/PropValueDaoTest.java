package test.com.subaru.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.PropValueDao;
import com.subaru.maneki.model.PropValue;
import com.subaru.core.BaseTestCaseJunit;

/**
 * @author zhangchaojie
 * @since 2016-08-24
 */
public class PropValueDaoTest extends BaseTestCaseJunit {

    @Resource
    private PropValueDao propValueDao;

    private PropValue    propValue;

    @Before
    public void setUp() {

        propValue = new PropValue();
        propValue.setValue("propValue");
        propValueDao.insert(propValue);

    }

    @Test
    public void testSelectAndInsert() {
        PropValue selectedPropValue = propValueDao.select(propValue.getId());
        Assert.assertTrue(selectedPropValue.getValue().equals(propValue.getValue()));
    }

    @Test
    public void testSelectByValue() {
        PropValue selectedPropValue = propValueDao.selectByValue(propValue.getValue());
        Assert.assertTrue(selectedPropValue.getValue().equals(propValue.getValue()));
    }

    @Test
    public void testUpdate() {

        propValue.setValue("PropValue2");
        propValueDao.update(propValue);

        PropValue updatedPropValue = propValueDao.select(propValue.getId());
        Assert.assertTrue(updatedPropValue.getValue().equals(propValue.getValue()));

    }

    @Test
    public void testDelete() {
        propValueDao.delete(propValue.getId());
        PropValue deletedPropValue = propValueDao.select(propValue.getId());
        Assert.assertNull(deletedPropValue);
    }

}
