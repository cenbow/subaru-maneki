package com.subaru.maneki.service.product;

import java.util.List;

import com.subaru.maneki.model.Cate;

public interface CateService {
	
	/**
	 * 获取所有品类信息
	 * @return
	 */
	List<Cate> getAll();
	
	/**
	 * 根据指定的id，获取所有父类为该id的品类信息
	 * @param parentId
	 * @return
	 */
	List<Cate> getChildCategory(int parentId);
}
