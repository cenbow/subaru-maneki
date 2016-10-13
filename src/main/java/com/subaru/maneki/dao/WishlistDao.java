package com.subaru.maneki.dao;

import java.util.List;

import com.subaru.maneki.model.Wishlist;

public interface WishlistDao {

    int insert(Wishlist wishlist);

    int update(Wishlist wishlist);

    int delete(int wishlistId);

    Wishlist select(int wishlistId);

    List<Wishlist> selectByUserId(int userId);

    List<Wishlist> selectBySpuId(int spuId);

}
