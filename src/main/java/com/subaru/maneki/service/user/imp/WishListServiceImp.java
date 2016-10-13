package com.subaru.maneki.service.user.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.WishlistDao;
import com.subaru.maneki.exception.TradeException;
import com.subaru.maneki.model.Wishlist;
import com.subaru.maneki.service.user.WishListService;

/**
 * @author zhangchaojie
 * @since 2016-09-09
 */
@Service("wishListService")
public class WishListServiceImp implements WishListService {

    @Resource
    private WishlistDao wishlistDao;

    @Override
    public Wishlist get(int id) {
        if (id <= 0) {
            return null;
        }
        return wishlistDao.select(id);
    }

    @Override
    public List<Wishlist> getByUserId(int userId) {
        if (userId <= 0) {
            return null;
        }
        return wishlistDao.selectByUserId(userId);
    }

    @Override
    public List<Wishlist> getBySpuId(int spuId) {
        if (spuId <= 0) {
            return null;
        }
        return wishlistDao.selectBySpuId(spuId);
    }

    @Override
    public void add(Wishlist wishlist) throws TradeException {
        if (wishlist == null) {
            throw new TradeException("Add wishList error, the given wishList is null !!");
        }
        wishlistDao.insert(wishlist);
    }

    @Override
    public int delete(int id){
    	if(id <= 0){
    		return 0;
    	}
    	
    	return wishlistDao.delete(id);
    }
}
