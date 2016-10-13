package com.subaru.maneki.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.subaru.maneki.model.Cart;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
public interface CartDao {

    void insert(Cart cart);

    void update(Cart cart);

    void delete(int id);

    void deleteByUserIdAndSkuId(@Param(value = "userId") int userId,
                                @Param(value = "skuId") int skuId);

    void deleteByUserId(int userId);

    Cart select(int id);

    int countUserCart(@Param(value = "userId") int userId);

    int countCartSkus(@Param(value = "skuId") int skuId);

    Cart selectByUserIdAndSkuId(@Param(value = "skuId") int skuId,
                                @Param(value = "userId") int userId);

    List<Cart> selectUserCart(@Param(value = "userId") int userId);

}