package com.subaru.maneki.manager.imp;

import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.subaru.maneki.manager.SkuManager;
import com.subaru.maneki.model.Sku;
import com.subaru.maneki.service.product.SkuService;
import com.subaru.maneki.vo.PropVO;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
@Service("skuManager")
public class SkuManagerImp implements SkuManager {

    @Resource
    private SkuService skuService;

    @Override
    public List<PropVO> getSkuPropVOList(int skuId) {
        if (skuId <= 0) {
            return null;
        }
        Sku sku = skuService.get(skuId);
        if (sku == null) {
            return null;
        }

        List<PropVO> skuPropVOList = new ArrayList<>();
        if (StringUtils.isNotBlank(sku.getPropColor())) {
            skuPropVOList.add(new PropVO("Color", sku.getPropColor()));
        }
        if (StringUtils.isNotBlank(sku.getPropSize())) {
            skuPropVOList.add(new PropVO("Size", sku.getPropSize()));
        }
        if (StringUtils.isNotBlank(sku.getPropStyle())) {
            skuPropVOList.add(new PropVO("Style", sku.getPropStyle()));
        }
        if (StringUtils.isNotBlank(sku.getPropMetalColor())) {
            skuPropVOList.add(new PropVO("Metal Color", sku.getPropMetalColor()));
        }
        if (StringUtils.isNotBlank(sku.getPropColor())) {
            skuPropVOList.add(new PropVO("cCOLOR", sku.getPropCColor()));
        }
        return skuPropVOList;
    }

    @Override
    public String getSubSkuPropJson(int spuId) {
        Map<String, Set<String>> skuPropMap = new HashMap<String, Set<String>>();
        List<Sku> skuList = skuService.getBySpuId(spuId);
        if (skuList == null) {
            return null;
        }
        for (Sku sku : skuList) {
            if (StringUtils.isNotBlank(sku.getPropColor())) {
                if (!skuPropMap.containsKey("Color")){
                    skuPropMap.put("Color", new TreeSet<String>());
                }
                skuPropMap.get("Color").add(sku.getPropColor());
            }
            if (StringUtils.isNotBlank(sku.getPropSize())) {
                if (!skuPropMap.containsKey("Size")) {
                    skuPropMap.put("Size", new TreeSet<String>());
                }
                skuPropMap.get("Size").add(sku.getPropSize());
            }
            if (StringUtils.isNotBlank(sku.getPropStyle())) {
                if (!skuPropMap.containsKey("Style")) {
                    skuPropMap.put("Style", new TreeSet<String>());
                }
                skuPropMap.get("Style").add(sku.getPropStyle());
            }
            if (StringUtils.isNotBlank(sku.getPropMetalColor())) {
                if (!skuPropMap.containsKey("Metal Color")) {
                    skuPropMap.put("Metal Color", new TreeSet<String>());
                }
                skuPropMap.get("Metal Color").add(sku.getPropMetalColor());
            }
            if (StringUtils.isNotBlank(sku.getPropCColor())) {
                if (!skuPropMap.containsKey("cCOLOR")) {
                    skuPropMap.put("cCOLOR", new TreeSet<String>());
                }
                skuPropMap.get("cCOLOR").add(sku.getPropCColor());
            }
            //            if (StringUtils.isNotBlank(sku.getPropSize())){
            //                skuPropVOList.add(new PropVO("Size", sku.getPropSize()));
            //            }
            //            if (StringUtils.isNotBlank(sku.getPropStyle())){
            //                skuPropVOList.add(new PropVO("Style", sku.getPropStyle()));
            //            }
            //            if (StringUtils.isNotBlank(sku.getPropMetalColor())){
            //                skuPropVOList.add(new PropVO("Metal Color", sku.getPropMetalColor()));
            //            }
            //            if (StringUtils.isNotBlank(sku.getPropColor())){
            //                skuPropVOList.add(new PropVO("cCOLOR", sku.getPropCColor()));
            //            }
            //            if (skuPropMap.containsKey(propName) == false) {
            //                skuPropMap.put(propName, new TreeSet<String>());
            //            }
            //            skuPropMap.get(propName).add(propValue);
        }

        return JSON.toJSONString(skuPropMap);
    }

}
