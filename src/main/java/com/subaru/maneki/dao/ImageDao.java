package com.subaru.maneki.dao;

import com.subaru.maneki.model.Image;

public interface ImageDao {
	Image select(int id);
	
	int update(Image image);
	
	void delete(int id);
	
	int insert(Image image);
}
