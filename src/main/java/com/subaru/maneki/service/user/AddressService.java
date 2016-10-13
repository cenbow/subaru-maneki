package com.subaru.maneki.service.user;

import java.util.List;

import com.subaru.maneki.exception.UserAddressException;
import com.subaru.maneki.model.Address;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
public interface AddressService {

    Address get(int id);

    /**
     * zhangchaojie
     * 由于订单关联地址不应随地址改变而改变，因此address的更新操作并不执行更新操作，而是新生成一条地址信息并与用户绑定，同时解除旧地址的绑定
     * @param formerAddressId
     * @param userId
     * @param countryid
     * @param stateId
     * @param city
     * @param street
     * @param zip
     * @return 新地址的addressId
     * @throws UserAddressException
     */
    int update(int formerAddressId, int userId, int countryid, int stateId, String city,
                String street, String zip, boolean isDefault) throws UserAddressException;

    void insert(Address address) throws UserAddressException;

    void delete(int id) throws UserAddressException;

    List<Address> getUserAddresses(int userId);

}
