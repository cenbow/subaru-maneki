package com.subaru.maneki.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.subaru.maneki.model.UserAddressRelation;

/**
 * @author zhangchaojie
 * @since 2016-08-26
 */
public interface UserAddressRelationDao {

    int insert(UserAddressRelation userAddressRelation);

    int update(@Param("userId") int userId, @Param("addressId") int addressId,@Param("oldUserId") int oldUserId, @Param("oldAddressId") int oldAddressId);

    int delete(@Param("userId") int userId, @Param("addressId") int addressId);

    UserAddressRelation select(@Param("userId") int userId, @Param("addressId") int addressId);
    
    List<UserAddressRelation> selectByUserId(int userId);

}
