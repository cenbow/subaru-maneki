package test.com.subaru.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.PropDao;
import com.subaru.maneki.dao.PropNameDao;
import com.subaru.maneki.dao.PropValueDao;
import com.subaru.maneki.model.Prop;
import com.subaru.maneki.model.PropName;
import com.subaru.maneki.model.PropValue;
import com.subaru.core.BaseTestCaseJunit;

/**
 * @author zhangchaojie
 * @since 2016-08-24
 */
public class PropDaoTest extends BaseTestCaseJunit {

    @Resource
    private PropDao      propDao;

    @Resource
    private PropNameDao  propNameDao;

    @Resource
    private PropValueDao propValueDao;

    private PropName     propName;

    private PropValue    propValue;

    private Prop         prop;

    @Before
    public void setUp() {

        propName = new PropName();
        propName.setName("propName");
        propName.setParentId(0);
        propNameDao.insert(propName);

        propValue = new PropValue();
        propValue.setValue("propValue");
        propValueDao.insert(propValue);

        prop = new Prop();
        prop.setNameId(propName.getId());
        prop.setValueId(propValue.getId());
        propDao.insert(prop);

    }

    @Test
    public void testSelectAndInsert() {
        Prop selectedProp = propDao.select(prop.getId());
        Assert.assertTrue(selectedProp.getNameId() == prop.getNameId());
        Assert.assertTrue(selectedProp.getValueId() == prop.getValueId());
    }

    @Test
    public void testUpdate() {

        PropName propName2 = new PropName();
        propName2.setName("propName2");
        propName2.setParentId(1);
        propNameDao.insert(propName2);

        PropValue propValue2 = new PropValue();
        propValue2.setValue("propValue2");
        propValueDao.insert(propValue2);

        prop.setNameId(propName2.getId());
        prop.setValueId(propValue2.getId());
        propDao.update(prop);

        Prop updatedProp = propDao.select(prop.getId());
        Assert.assertTrue(updatedProp.getNameId() == prop.getNameId());
        Assert.assertTrue(updatedProp.getValueId() == prop.getValueId());

    }

    @Test
    public void testDelete() {
        propDao.delete(prop.getId());
        Prop deletedProp = propDao.select(prop.getId());
        Assert.assertNull(deletedProp);
    }

}
