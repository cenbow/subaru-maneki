package com.subaru.maneki.dao;

import java.util.List;

import com.subaru.maneki.model.Cate;

public interface CateDao {
	Cate select(int id);
	
	int update(Cate cate);
	
	int insert(Cate cate);
	
	void delete(int id);
	
	List<Cate> selectAll();
	
	Cate selectByName(String name);
	
	Cate selectByOldId(int oldId);
	
	List<Cate> selectByParentId(int parentId);
}
