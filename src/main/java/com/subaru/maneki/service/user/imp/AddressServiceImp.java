package com.subaru.maneki.service.user.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.AddressDao;
import com.subaru.maneki.dao.UserAddressRelationDao;
import com.subaru.maneki.exception.UserAddressException;
import com.subaru.maneki.model.Address;
import com.subaru.maneki.model.UserAddressRelation;
import com.subaru.maneki.service.user.AddressService;

/**
 * @author zhangchaojie
 * @since 2016-09-02
 */
@Service("addressService")
public class AddressServiceImp implements AddressService {

    @Resource
    private AddressDao             addressDao;

    @Resource
    private UserAddressRelationDao userAddressRelationDao;

    @Override
    public Address get(int id) {
        if (id <= 0) {
            return null;
        }
        return addressDao.select(id);
    }

    @Override
    public int update(int formerAddressId, int userId, int countryId, int stateId, String city,
                      String street, String zip, boolean isDefault) throws UserAddressException {

    	//zip可以是空的
        if (formerAddressId <= 0 || userId <= 0 || countryId <= 0 || stateId <= 0
            || StringUtils.isBlank(city) || StringUtils.isBlank(street)) {
            throw new UserAddressException("Update address error, incomplete address info !!");
        }

        UserAddressRelation userAddressRelation = userAddressRelationDao.select(userId,
            formerAddressId);
        if (userAddressRelation != null) {
            userAddressRelationDao.delete(userId, formerAddressId);
        }

        Address address = new Address(countryId, stateId, city, street, zip, isDefault);
        addressDao.insert(address);

        UserAddressRelation newUserAddressRelation = new UserAddressRelation();
        newUserAddressRelation.setUserId(userId);
        newUserAddressRelation.setAddressId(address.getId());
        userAddressRelationDao.insert(newUserAddressRelation);

        return address.getId();

    }

    @Override
    public void insert(Address address) throws UserAddressException {

        if (address == null) {
            throw new UserAddressException(
                "Insert address error, the given address cannot be null !!");
        }

        addressDao.insert(address);

    }

    @Override
    public void delete(int id) throws UserAddressException {

        if (id <= 0) {
            throw new UserAddressException("Illegal addressId " + id + ", cannot delete address !!");
        }

        addressDao.delete(id);

    }

    @Override
    public List<Address> getUserAddresses(int userId) {

        if (userId <= 0) {
            return null;
        }
        
        List<UserAddressRelation> userAddressRelationList = userAddressRelationDao.selectByUserId(userId);
        List<Address> addresseList = new ArrayList<>();
        for (UserAddressRelation uar : userAddressRelationList){
        	addresseList.add(addressDao.select(uar.getAddressId()));
        }

        //return addressDao.selectByUserId(userId);
        return addresseList;
    }
}
