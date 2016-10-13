package com.subaru.maneki.service.product;

import java.util.List;

import com.subaru.maneki.model.Spu;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
public interface SpuService {

    /**
     * zhangchaojie
     * 分页获取SPU，包含已上架和未上架的SPU
     * @param page
     * @param pageNum
     * @return
     */
    List<Spu> get(int page, int pageNum);

    /**
     * zhangchaojie
     * 分页获取已上架或未上架的SPU
     * @param isPublished
     * @param page
     * @param pageNum
     * @return
     */
    List<Spu> get(boolean isPublished, int page, int pageNum);

    /**
     * zhangchaojie
     * 分页获取指定类目下的所有已上架的SPU
     * @param cateId
     * @param page
     * @param pageNum
     * @return
     */
    List<Spu> getByCate(int cateId, int page, int pageNum);

    /**
     * zhangchaojie
     * 分页获取指定类目下的所有已上架或未上架的SPU
     * @param cateId
     * @param isPublished
     * @param page
     * @param pageNum
     * @return
     */
    List<Spu> getByCate(int cateId, boolean isPublished, int page, int pageNum);

    /**
     * zhangchaojie
     * 根据指定id获取对应的SPU
     * @param ids
     * @return
     */
    List<Spu> get(int[] ids);

    /**
     * zhangchaojie
     * 通过id获取SPU
     * @param spuId
     * @return
     */
    Spu get(int spuId);

    /**
     * zhangchaojie
     * 统计所有已上架或未上架的SPU数量
     * @param isPublished
     * @return
     */
    int countAll(boolean isPublished);

    /**
     * zhangchaojie
     * 统计指定类目下所有已上架或未上架SPU的数量
     * @param cateId
     * @param isPublished
     * @return
     */
    int countByCate(int cateId, boolean isPublished);

    /**
     * zhangchaojie
     * 新增SPU
     * @param spu
     */
    void add(Spu spu);

    /**
     * zhangchaojie
     * 更新SPU
     * @param spu
     */
    void update(Spu spu);

    /**
     * zhangchaojie
     * 删除SPU
     * @param spuId
     */
    void delete(int spuId);
    
    /**
     * huangli
     * @param spuId
     * 返回带有品类信息cate list的商品spu
     */
    Spu getWithCateList(int spuId);

}