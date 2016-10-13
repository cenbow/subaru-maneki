package com.subaru.maneki.dao;

import com.subaru.maneki.model.PropValue;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
public interface PropValueDao {

    int delete(int id);

    int insert(PropValue propValue);

    PropValue select(int id);

    PropValue selectByValue(String value);

    int update(PropValue propValue);
}