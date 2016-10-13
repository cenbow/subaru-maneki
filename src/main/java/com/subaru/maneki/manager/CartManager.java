package com.subaru.maneki.manager;

import java.util.List;

import com.subaru.maneki.model.Cart;
import com.subaru.maneki.vo.CartVO;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
public interface CartManager {

    CartVO getCartVO(Cart cart);

    List<CartVO> getUserCartVOs(int userId);

    /**
     * zhangchaojie
     * 计算用户当前购物车的总价
     * @param userId
     * @return
     */
    double getCartTotalPrice(int userId);

    /**
     * zhangchaojie
     * 计算用户当前购物车中商品对应的竞价平台总价
     * @param userId
     * @return
     */
    double getCartPlatformTotalPrice(int userId);

}
