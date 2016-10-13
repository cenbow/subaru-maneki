package com.subaru.maneki.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.subaru.maneki.model.SpuImageRelation;

/**
 * @author lee
 * 
 */
public interface SpuImageRelationDao {
	int insert(SpuImageRelation spuImage);

    /**
     * @param spuId :要更新的新spu id
     * @param imageId：要更新的新image id
     * @param oldSpuId：原有的spu id
     * @param oldImageId：原有的image id
     * @return
     */
    int update(@Param("spuId") int spuId, @Param("imageId") int imageId, @Param("oldSpuId") int oldSpuId, @Param("oldImageId") int oldImageId);

    void delete(int spuId, int imageId);
    
    SpuImageRelation select(@Param("spuId") int spuId, @Param("imageId") int imageId);
        
    List<SpuImageRelation> selectBySpuId(int spuId);
    
    List<SpuImageRelation> selectByImageId(int imageId);

}
