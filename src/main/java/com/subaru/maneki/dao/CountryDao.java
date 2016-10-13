package com.subaru.maneki.dao;

import com.subaru.maneki.model.Country;

/**
 * @author zhangchaojie
 * @since 2016-08-26
 */
public interface CountryDao {

    int insert(Country country);

    int update(Country country);

    int delete(int countryId);

    Country select(int countryId);

    /**
     * 根据国家的缩写（比如美国的US）查找相关国家的信息
     * @param abbr
     * @return
     */
    Country selectByAbbr(String abbr);

    Country selectByName(String name);

}
