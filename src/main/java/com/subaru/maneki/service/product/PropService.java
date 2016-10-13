package com.subaru.maneki.service.product;

import com.subaru.maneki.enumeration.PropType;
import com.subaru.maneki.model.Prop;
import com.subaru.maneki.model.PropName;
import com.subaru.maneki.model.PropValue;

/**
 * @author zhangchaojie
 * @since 2016-08-18
 */
public interface PropService {

    /**
     * zhangchaojie
     * 通过属性名、属性值和属性类型获取属性
     * @param propName
     * @param propValue
     * @param propType
     * @return
     */
    Prop getProp(PropName propName, PropValue propValue, PropType propType);

    /**
     * @param propName
     * @param propValue
     * @param propType
     */
    void addProp(PropName propName, PropValue propValue, PropType propType);

    /**
     * 通过父属性和属性名来获取属性名对象
     * @param parentId      父属性名的ID
     * @param name          属性名
     * @return
     */
    PropName getName(int parentId, String name);

    PropValue getValue(String value);

    //    /**
    //     * zhangchaojie
    //     * 根据id获取一个属性
    //     * @param id
    //     * @return
    //     */
    //    Prop getProp(int id);
    //
    //    /**
    //     * zhangchaojie
    //     * 增加一个属性
    //     * @param prop
    //     * @throws PropException
    //     */
    //    void addProp(Prop prop) throws PropException;
    //
    //    /**
    //     * zhangchaojie
    //     * 通过ID获取一个属性名对象
    //     * @param nameId
    //     * @return
    //     */
    //    PropName getName(int nameId);

    //    /**
    //     * zhangchaojie
    //     * 获取指定属性名的顶层属性名
    //     * @param name
    //     * @return
    //     */
    //    PropName getTopName(String name);
    //
    //    /**
    //     * zhangchaojie
    //     * 获取所有顶层的属性名
    //     * @return
    //     */
    //    List<PropName> getTopNames();
    //
    //    /**
    //     * zhangchaojie
    //     * 通过父属性名获取子属性名
    //     * @param parentId      父属性名的ID
    //     * @return
    //     */
    //    List<PropName> getNamesByParent(int parentId);
    //
    //    /**
    //     * zhangchaojie
    //     * 增加一个属性名
    //     * @param name          属性名对象
    //     * @throws PropException
    //     */
    //    void addName(PropName name) throws PropException;
    //
    //    /**
    //     * zhagnchaojie
    //     * 更新一个属性名
    //     * @param name          属性名对象
    //     * @throws PropException
    //     */
    //    void updateName(PropName name) throws PropException;
    //
    //    /**
    //     * zhangchaojie
    //     * 删除一个属性名
    //     * @param nameId        属性名ID
    //     */
    //    void deleteName(int nameId);
    //
    //    /**
    //     * zhangchaojie
    //     * 分页获取子属性
    //     * @param nameId            属性名ID
    //     * @param page              页码
    //     * @param pageSize          每页元素数
    //     * @return
    //     */
    //    List<PropValue> getValues(int nameId, int page, int pageSize);

    //    /**
    //     * zhangchaojie
    //     * 通过Id获取属性值对象
    //     * @param valueId           属性值ID
    //     * @return
    //     */
    //    PropValue getValue(int valueId);
    //
    //    /**
    //     * zhangchaojie
    //     * 增加一个属性值
    //     * @param nameId            属性名ID
    //     * @param propValue         属性值对象
    //     * @param propType          属性类型
    //     * @throws PropException
    //     */
    //    void addValue(int nameId, PropValue propValue, PropType propType) throws PropException;
    //
    //    /**
    //     * zhangchaojie
    //     * 更新一个属性值
    //     * @param propValue             属性值对象
    //     * @throws PropException
    //     */
    //    void updateValue(PropValue propValue) throws PropException;
    //
    //    /**
    //     * zhangchaojie
    //     * 删除一个属性值
    //     * @param valueId           属性值ID
    //     */
    //    void deleteValue(int valueId);

}
