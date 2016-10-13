package com.subaru.maneki.vo;

import java.util.List;

/**
 * @author zhangchaojie
 * @since 2016-08-23
 */
public class ProductVO {

    private SpuVO       spuVO;

    private List<SkuVO> skuVOList;
    
    private List<Integer> cateList;
    
    private boolean 	active;

    public SpuVO getSpuVO() {
        return spuVO;
    }

    public void setSpuVO(SpuVO spuVO) {
        this.spuVO = spuVO;
    }

    public List<SkuVO> getSkuVOList() {
        return skuVOList;
    }

    public void setSkuVOList(List<SkuVO> skuVOList) {
        this.skuVOList = skuVOList;
    }

	public List<Integer> getCateList() {
		return cateList;
	}

	public void setCateList(List<Integer> cateList) {
		this.cateList = cateList;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
    
}
