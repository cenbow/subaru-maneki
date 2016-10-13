package com.subaru.maneki.dao;

import com.subaru.maneki.model.SkuEtc;

/**
 * @author zhangchaojie
 * @since 2016-08-23
 */
public interface SkuEtcDao {

    void insert(SkuEtc skuEtc);

    void update(SkuEtc skuEtc);

    void delete(int id);

    SkuEtc selectBySkuId(int skuId);

}