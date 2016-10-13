package com.subaru.maneki.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.subaru.maneki.model.SkuProp;

public interface SkuPropDao {

    int insert(SkuProp skuProp);

    int update(SkuProp skuProp);

    void delete(int id);

    SkuProp select(int id);

    SkuProp selectBySkuIdAndPropId(@Param(value="skuId") int skuId, @Param(value="propId") int propId);

    List<SkuProp> selectBySkuId(int skuId);

    List<SkuProp> selectByPropId(int propId);
}
