package com.subaru.maneki.dao;

import com.subaru.maneki.model.UserEtc;

public interface UserEtcDao {
	
	int insert(UserEtc userEtc);
	
	UserEtc select(int userId);
	
	int update(UserEtc userEtc);
}
