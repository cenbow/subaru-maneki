package com.subaru.maneki.dao;

import java.util.List;

import com.subaru.maneki.model.Address;

/**
 * @author zhangchaojie
 * @since 2016-08-26
 */
public interface AddressDao {

    int insert(Address address);

    //    int update(Address address);

    int delete(int addressId);

    Address select(int addressId);

    List<Address> selectByUserId(int userId);

}
