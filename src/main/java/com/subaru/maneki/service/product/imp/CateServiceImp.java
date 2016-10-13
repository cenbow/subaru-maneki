package com.subaru.maneki.service.product.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.CateDao;
import com.subaru.maneki.model.Cate;
import com.subaru.maneki.service.product.CateService;

@Service("cateService")
public class CateServiceImp implements CateService{
	
	@Resource
	private CateDao cateDao;
	
	public List<Cate> getChildCategory(int parentId){
		return cateDao.selectByParentId(parentId);
	}
	
	public List<Cate> getAll(){
		return cateDao.selectAll();
	}
}
