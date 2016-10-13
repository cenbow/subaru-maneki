package com.subaru.maneki.manager.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.SpuImageRelationDao;
import com.subaru.maneki.manager.CartManager;
import com.subaru.maneki.manager.SkuManager;
import com.subaru.maneki.model.*;
import com.subaru.maneki.service.cart.CartService;
import com.subaru.maneki.service.product.ImageService;
import com.subaru.maneki.service.product.SkuService;
import com.subaru.maneki.service.product.SpuService;
import com.subaru.maneki.vo.CartVO;
import com.subaru.maneki.vo.PropVO;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
@Service("cartManager")
public class CartManagerImp implements CartManager {

    @Resource
    private SkuManager          skuManager;

    @Resource
    private SkuService          skuService;

    @Resource
    private SpuService          spuService;

    @Resource
    private CartService         cartService;

    @Resource
    private ImageService        imageService;

    @Resource
    private SpuImageRelationDao spuImageRelationDao;

    @Override
    public CartVO getCartVO(Cart cart) {

        CartVO cartVO = new CartVO();
        cartVO.setId(cart.getId());

        int skuId = cart.getSkuId();
        if (skuId <= 0) {
            return null;
        }
        cartVO.setSkuId(skuId);

        Sku sku = skuService.get(skuId);
        if (sku == null) {
            return null;
        }
        cartVO.setPrice(sku.getPrice());
        cartVO.setCurrencyUnit(sku.getCurrencyUnit());
        cartVO.setIsSaleOk(sku.getIsSaleOk());

        SkuEtc skuEtc = skuService.getSkuEtc(skuId);
        if (skuEtc == null) {
            return null;
        }
        cartVO.setPlatformPrice(skuEtc.getPlatformPrice());

        int spuId = sku.getSpuId();
        if (spuId <= 0) {
            return null;
        }
        cartVO.setSpuId(spuId);
        Spu spu = spuService.get(spuId);
        if (spu == null) {
            return null;
        }
        cartVO.setProductName(spu.getName());

        List<SpuImageRelation> spuImageRelations = spuImageRelationDao.selectBySpuId(spuId);
        if (spuImageRelations == null || spuImageRelations.size() == 0) {
            return null;
        }
        for (SpuImageRelation spuImageRelation : spuImageRelations) {
            int imageId = spuImageRelation.getImageId();
            if (imageId <= 0) {
                continue;
            }
            Image image = imageService.get(imageId);
            if (image.getIsMain()) {
                cartVO.setImageUrl(image.getUrl());
                break;
            }
        }

        List<PropVO> skuPropVOList = skuManager.getSkuPropVOList(skuId);
        cartVO.setPropList(skuPropVOList);

        cartVO.setQuantity(cart.getQuantity());

        return cartVO;
    }

    @Override
    public List<CartVO> getUserCartVOs(int userId) {

        if (userId <= 0) {
            return null;
        }

        List<Cart> userCarts = cartService.getUserCarts(userId);

        if (userCarts == null || userCarts.size() <= 0) {
            return null;
        }

        List<CartVO> cartVOs = new ArrayList<>();

        for (Cart cart : userCarts) {
            cartVOs.add(getCartVO(cart));
        }

        return cartVOs;
    }

    @Override
    public double getCartTotalPrice(int userId) {

        if (userId <= 0) {
            return 0;
        }

        double sum = 0;
        List<CartVO> userCartVOs = getUserCartVOs(userId);
        if (userCartVOs == null || userCartVOs.size() <= 0) {
            return 0;
        }
        for (CartVO cartVO : userCartVOs) {
            int quantity = cartVO.getQuantity();
            if (quantity <= 0) {
                continue;
            }
            sum += (cartVO.getPrice() * quantity);
        }
        return sum;
    }

    @Override
    public double getCartPlatformTotalPrice(int userId) {
        if (userId <= 0) {
            return 0;
        }

        double sum = 0;
        List<CartVO> userCartVOs = getUserCartVOs(userId);
        if (userCartVOs == null || userCartVOs.size() <= 0) {
            return 0;
        }
        for (CartVO cartVO : userCartVOs) {
            int quantity = cartVO.getQuantity();
            if (quantity <= 0) {
                continue;
            }
            sum += (Integer.parseInt(cartVO.getPlatformPrice()) * quantity);
        }
        return sum;
    }

}
