package com.subaru.maneki.dao;

import java.util.List;

import com.subaru.maneki.model.SpuProp;

public interface SpuPropDao {
	int insert(SpuProp spuProp);

    int update(SpuProp spuProp);

    void delete(int id);
    
    SpuProp select(int id);
    
    List<SpuProp> selectBySpuId(int spuId);
    
    List<SpuProp> selectByPropId(int propId);
}
