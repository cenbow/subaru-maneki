package com.subaru.maneki.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.subaru.maneki.model.PropName;

/**
 * @author zhangchaojie
 * @since 2016-08-22
 */
public interface PropNameDao {

    int delete(int id);

    int insert(PropName propName);

    PropName select(int id);

    PropName selectByParentAndName(@Param("parentId") int parentId, @Param("name") String name);

    List<PropName> selectByParent(int parentId);

    int update(PropName propName);

}