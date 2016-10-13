package com.subaru.maneki.service.cart.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.CartDao;
import com.subaru.maneki.model.Cart;
import com.subaru.maneki.model.Sku;
import com.subaru.maneki.model.User;
import com.subaru.maneki.service.cart.CartService;
import com.subaru.maneki.service.product.SkuService;
import com.subaru.maneki.service.user.UserService;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
@Service(value = "cartService")
public class CartServiceImp implements CartService {

    @Resource
    private UserService userService;

    @Resource
    private SkuService  skuService;

    @Resource
    private CartDao     cartDao;

    @Override
    public List<Cart> getUserCarts(int userId) {
        if (userId <= 0) {
            return null;
        }
        List<Cart> cartSkus = cartDao.selectUserCart(userId);
        return cartSkus;
    }

    @Override
    public int countCartSkus(int skuId) {
        if (skuId <= 0) {
            return 0;
        }
        return cartDao.countCartSkus(skuId);
    }

    @Override
    public int countUserCart(int userId) {
        if (userId <= 0) {
            return 0;
        }
        Integer totalNum = cartDao.countUserCart(userId);
        if (totalNum == null) {
            return 0;
        }
        return totalNum;
    }

    @Override
    public Cart get(int userId, int skuId) {
        Cart tradeCart = cartDao.selectByUserIdAndSkuId(skuId, userId);
        return tradeCart;
    }

    @Override
    public void add(int userId, int skuId, int quantity) {
        if (userId <= 0 || skuId <= 0 || quantity <= 0) {
            return;
        }
        User user = userService.get(userId);
        Sku sku = skuService.get(skuId);
        Cart cart = null;
        if (sku != null && user != null) {
        	if ((cart = cartDao.selectByUserIdAndSkuId(skuId, userId)) != null){
        		cart.setQuantity(cart.getQuantity() + quantity);
        		cartDao.update(cart);
        	}else{
	            cart = new Cart();
	            cart.setSkuId(skuId);
	            cart.setQuantity(quantity);
	            cart.setUserId(userId);
	            cartDao.insert(cart);
        	}
        }
    }

    @Override
    public void updateQuantity(int userId, int skuId, int quantity) {
        if (userId <= 0 || skuId <= 0 || quantity <= 0) {
            return;
        }
        Cart cart = cartDao.selectByUserIdAndSkuId(skuId, userId);
        if (cart == null) {
            return;
        }
        cart.setQuantity(quantity);
        cartDao.update(cart);
    }

    @Override
    public void remove(int userId, int skuId) {
        if (userId <= 0 || skuId <= 0) {
            return;
        }
        cartDao.deleteByUserIdAndSkuId(userId, skuId);
    }

    @Override
    public void remove(int cartId) {
        if (cartId > 0) {
            cartDao.delete(cartId);
        }
    }

    @Override
    public boolean isContains(int userId, int skuId) {
        Cart cart = cartDao.selectByUserIdAndSkuId(skuId, userId);
        if (cart != null) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void removeUserCarts(int userId) {

        if (userId <= 0) {
            return;
        }

        cartDao.deleteByUserId(userId);

    }

}
