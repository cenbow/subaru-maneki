package com.subaru.maneki.manager;

import java.util.List;

import com.subaru.maneki.vo.PropVO;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
public interface SkuManager {

    List<PropVO> getSkuPropVOList(int skuId);

    String getSubSkuPropJson(int spuId);

}
