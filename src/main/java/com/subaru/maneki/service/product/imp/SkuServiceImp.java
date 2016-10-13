package com.subaru.maneki.service.product.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.*;
import com.subaru.maneki.exception.ProductException;
import com.subaru.maneki.model.Prop;
import com.subaru.maneki.model.Sku;
import com.subaru.maneki.model.SkuEtc;
import com.subaru.maneki.model.SkuProp;
import com.subaru.maneki.service.product.SkuService;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
@Service("skuService")
public class SkuServiceImp implements SkuService {

    @Resource
    private SkuDao       skuDao;

    @Resource
    private SkuEtcDao    skuEtcDao;

    @Resource
    private PropNameDao  propNameDao;

    @Resource
    private PropValueDao propValueDao;

    @Resource
    private PropDao      propDao;

    @Resource
    private SkuPropDao   skuPropDao;

    @Override
    public Sku get(int id) {
        if (id <= 0) {
            return null;
        }
        return skuDao.select(id);
    }

    @Override
    public SkuEtc getSkuEtc(int skuId) {
        if (skuId <= 0) {
            return null;
        }
        return skuEtcDao.selectBySkuId(skuId);
    }

    @Override
    public List<Sku> getBySpuId(int spuId) {
        if (spuId <= 0) {
            return null;
        }
        return skuDao.selectBySpuId(spuId);
    }

    @Override
    public void add(Sku sku) throws ProductException {

        if (sku == null) {
            throw new ProductException("Insert failed, the given sku is null !!");
        }

        skuDao.insert(sku);

    }

    @Override
    public void update(Sku sku) throws ProductException {

        if (sku == null) {
            throw new ProductException("Update failed, the given sku is null !!");
        }

        skuDao.update(sku);

    }

    @Override
    public void delete(int skuId) throws ProductException {

        if (skuId <= 0) {
            throw new ProductException("Illegal skuId: " + skuId);
        }

    }

    @Override
    public int countAll(boolean isPublished) {

        return skuDao.countAll(isPublished);
    }

    @Override
    public List<Prop> getSkuPropList(int skuId) {
        if (skuId <= 0) {
            return null;
        }
        List<Prop> propList = new ArrayList<>();

        List<SkuProp> skuProps = skuPropDao.selectBySkuId(skuId);
        if (skuProps == null || skuProps.size() <= 0) {
            return null;
        }
        for (SkuProp skuProp : skuProps) {
            int propId = skuProp.getPropId();
            if (propId <= 0) {
                continue;
            }
            Prop prop = propDao.select(propId);
            propList.add(prop);
        }
        return propList;
    }
}
