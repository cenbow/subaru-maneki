package com.subaru.maneki.service.cart;

import java.util.List;

import com.subaru.maneki.exception.CartException;
import com.subaru.maneki.model.Cart;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
public interface CartService {

    /**
     * 获得用户的购物车
     * @param userId    用户id
     * @return
     */
    List<Cart> getUserCarts(int userId);

    /**
     * 统计购物车中某商品总共添加数量
     */
    int countCartSkus(int skuId);

    /**
     * 统计用户购物车中的sku数量
     * @param userId    用户Id
     * @return
     */
    int countUserCart(int userId);

    /**
     * 获得用户的某个单品的购物车
     * @param userId    用户Id
     * @param skuId    skuId
     * @return
     */
    Cart get(int userId, int skuId);

    /**
     *  添加单品，如果某个单品已在购物车，那么更新购物车里该单品的数量
     * @param userId    用户Id
     * @param skuId    skuId
     * @param quantity    quantity
     */
    void add(int userId, int skuId, int quantity) throws CartException;

    /**
     * 修改单品
     * @param userId    用户Id
     * @param skuId    skuId
     * @param quantity  数量
     */
    void updateQuantity(int userId, int skuId, int quantity) throws CartException;

    /**
     * 移除单品
     * @param userId    用户Id
     * @param skuId    skuId
     */
    void remove(int userId, int skuId) throws CartException;

    /**
     * 移除单品
     * @param cartId    购物车id
     */
    void remove(int cartId) throws CartException;

    /**
     * 是否包含该单品
     * @param userId    用户id
     * @param skuId    skuId
     * @return
     */
    boolean isContains(int userId, int skuId);

    void removeUserCarts(int userId);

}
