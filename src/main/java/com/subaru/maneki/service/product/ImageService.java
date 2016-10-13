package com.subaru.maneki.service.product;

import java.util.List;

import com.subaru.maneki.model.Image;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
public interface ImageService {

    int add(Image image);

    int update(Image image);

    void delete(int id);

    Image get(int id);

    /** 
	 * 向SpuImageRelation表中添加记录
	 *
	 */
    void addSpuImage(int spuId, int imageId);

    /**
	 * 删除SpuImageRelation表中的记录
	 */
    void deleteSpuImage(int spuId, int imageId);

    List<Image> getSpuImage(int spuId);

    /** 
	 * 向SkuImageRelation表中添加记录
	 */
    void addSkuImage(int skuId, int imageId);

    /**
	 * 删除SkuImageRelation和Image表中的记录
	 */
    void deleteSkuImage(int skuId, int imageId);

    List<Image> getSkuImage(int skuId);

}
