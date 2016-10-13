package com.subaru.maneki.service.product;

import java.util.List;

import com.subaru.maneki.exception.ProductException;
import com.subaru.maneki.model.Prop;
import com.subaru.maneki.model.Sku;
import com.subaru.maneki.model.SkuEtc;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
public interface SkuService {

    /**
     * 通过ID获取SKU
     * @param id
     * @return
     */
    Sku get(int id);

    /**
     * 通过skuId获取skuEtc
     * @param skuId
     * @return
     */
    SkuEtc getSkuEtc(int skuId);

    /**
     * 通过SPUID获取SKU
     * @param spuId
     * @return
     */
    List<Sku> getBySpuId(int spuId);

    /**
     * 增加一个SKU
     * @param sku
     * @throws ProductException
     */
    void add(Sku sku) throws ProductException;

    /**
     * 更新一个SKU
     * @param sku
     * @throws ProductException 
     */
    void update(Sku sku) throws ProductException;

    void delete(int skuId) throws ProductException;

    /**
     * 统计所有SKU的数量
     * @return
     */
    int countAll(boolean isPublished);

    List<Prop> getSkuPropList(int skuId);

}
