package com.subaru.maneki.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.subaru.maneki.model.Sku;

public interface SkuDao {

    void insert(Sku sku);

    void update(Sku sku);

    void delete(int id);

    Sku select(int id);

    int countAll(@Param(value = "isPublished") boolean isPublished);

    List<Sku> selectBySpuId(int spuId);

}