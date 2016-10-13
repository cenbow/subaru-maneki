package com.subaru.maneki.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.subaru.maneki.model.Spu;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
public interface SpuDao {

    int insert(Spu spu);

    int update(Spu spu);

    void delete(int id);

    Spu select(int id);
    
    Spu selectForCateList(int id);

    List<Spu> selectByLimit(@Param(value = "start") int start, @Param(value = "limit") int limit);
    
    List<Spu> selectByLimitAndIsPublished(
    		@Param(value = "start") int start, 
    		@Param(value = "limit") int limit,
    		@Param(value="isPublished") boolean isPublished
    		);

    List<Spu> selectByIdList(@Param(value = "idList") String idList);

    List<Spu> selectByCate(@Param(value = "cateId") int cateId, @Param(value = "start") int start,
                           @Param(value = "limit") int limit);
    
    Spu selectByOldId(@Param(value = "oldId") int oldId);
    
    int countAll(@Param(value = "isPublished") boolean isPublished);

    int countByCate(@Param(value = "cateId") int cateId,
                    @Param(value = "isPublished") boolean isPublished);
    
    /**
     * 找到属于品类集合cateIdList中的所有商品
     * @param cateIdList
     * @param start
     * @param limit
     * @param isPublished
     * @return
     */
    List<Spu> selectByCateIdListAndIsPushlished(
    		@Param(value = "cateIdList") String cateIdList, 
    		@Param(value = "start") int start, 
    		@Param(value = "limit") int limit, 
    		@Param(value="isPublished") boolean isPublished
    		);
    
}
