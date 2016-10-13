package com.subaru.maneki.dao;

import java.util.List;

import com.subaru.maneki.model.SkuImageRelation;

public interface SkuImageRelationDao {
	int insert(SkuImageRelation skuImage);

    int update(SkuImageRelation skuImage);

    void delete(int skuId, int imageId);
        
    List<SkuImageRelation> selectBySkuId(int skuId);
    
    List<SkuImageRelation> selectByImageId(int imageId);
}
