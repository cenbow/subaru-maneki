package com.subaru.maneki.service.product.imp;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.PropDao;
import com.subaru.maneki.dao.PropNameDao;
import com.subaru.maneki.dao.PropValueDao;
import com.subaru.maneki.enumeration.PropType;
import com.subaru.maneki.model.Prop;
import com.subaru.maneki.model.PropName;
import com.subaru.maneki.model.PropValue;
import com.subaru.maneki.service.product.PropService;

/**
 * @author zhangchaojie
 * @since 2016-08-18
 */
@Service("propService")
public class PropServiceImp implements PropService {

    @Resource
    private PropNameDao  propNameDao;

    @Resource
    private PropValueDao propValueDao;

    @Resource
    private PropDao      propDao;

    @Override
    public Prop getProp(PropName propName, PropValue propValue, PropType propType) {
        if (propName == null || propValue == null || propType == null) {
            return null;
        }
        int propNameId = propName.getId();
        int propValueId = propValue.getId();
        if (propNameId <= 0 || propValueId <= 0) {
            return null;
        }
        return propDao.selectByNameIdAndValueId(propNameId, propValueId, propType.getValue());
    }

    @Override
    public void addProp(PropName propName, PropValue propValue, PropType propType) {

        if (propName == null || propValue == null || propType == null) {
            return;
        }
        propNameDao.insert(propName);
        propValueDao.insert(propValue);
        Prop prop = new Prop(propName.getId(), propValue.getId(), propType.getValue());
        propDao.insert(prop);
    }

    @Override
    public PropName getName(int parentId, String name) {
        if (StringUtils.isBlank(name) || parentId < 0) {
            return null;
        }
        return propNameDao.selectByParentAndName(parentId, name);
    }

    @Override
    public PropValue getValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return propValueDao.selectByValue(value);
    }
}
