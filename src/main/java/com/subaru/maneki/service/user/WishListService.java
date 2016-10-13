package com.subaru.maneki.service.user;

import java.util.List;

import com.subaru.maneki.exception.TradeException;
import com.subaru.maneki.model.Wishlist;

/**
 * @author zhangchaojie
 * @since 2016-09-09
 */
public interface WishListService {

    Wishlist get(int id);

    List<Wishlist> getByUserId(int userId);

    List<Wishlist> getBySpuId(int spuId);

    void add(Wishlist wishlist) throws TradeException;
    
    int delete(int id);

}
