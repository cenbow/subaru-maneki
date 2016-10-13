package com.subaru.maneki.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.subaru.maneki.model.SpuCateRelation;

public interface SpuCateRelationDao {
	
	int insert(SpuCateRelation spuCateRelation);
	
	int update(@Param("spuId") int spuId, @Param("cateId") int cateId, @Param("oldSpuId") int oldSpuId, @Param("oldCateId") int oldCateId);
	
	void delete(int spuId, int cateId);
	
	SpuCateRelation select(@Param("spuId") int spuId, @Param("cateId") int cateId);
	
	List<SpuCateRelation> selectBySpuId(int spuId);
	
	List<SpuCateRelation> selectByCateIdList(@Param("cateIdList") String cateIdList, @Param("start") int start, @Param("limit") int limit);
}
