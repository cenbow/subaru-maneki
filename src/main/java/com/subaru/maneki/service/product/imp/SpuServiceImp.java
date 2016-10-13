package com.subaru.maneki.service.product.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.SpuDao;
import com.subaru.maneki.dao.SpuImageRelationDao;
import com.subaru.maneki.model.Spu;
import com.subaru.maneki.service.product.ImageService;
import com.subaru.maneki.service.product.SpuService;
import com.subaru.common.util.PageUtil;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
@Service("spuService")
public class SpuServiceImp implements SpuService {

    @Resource
    private SpuDao              spuDao;

    @Resource
    private SpuImageRelationDao spuImageRelationDao;

    @Resource
    private ImageService        imageService;

    @Override
    public List<Spu> get(int page, int pageNum) {
        if (page <= 0 || pageNum <= 0) {
            return null;
        }

        int start = PageUtil.getStart(page, pageNum);
        int limit = PageUtil.getLimit(page, pageNum);

        List<Spu> spuList = spuDao.selectByLimit(start, limit);
        return spuList;
    }

    @Override
    public List<Spu> get(boolean isPublished, int page, int pageNum) {
        List<Spu> spuList = this.get(page, pageNum);
        if (spuList == null) {
            return null;
        }

        List<Spu> resultSpuList = new ArrayList<Spu>();
        for (Spu spu : spuList) {
            if (spu.getIsPublished() == true) {
                resultSpuList.add(spu);
            }
        }

        if (resultSpuList.size() == 0) {
            return null;
        } else {
            return resultSpuList;
        }

    }
    
    @Override
    public Spu getWithCateList(int spuId){
    	if (spuId <= 0){
    		return null;
    	}
    	
    	return spuDao.selectForCateList(spuId);
    }

    @Override
    public List<Spu> getByCate(int cateId, int page, int pageNum) {
        if (cateId <= 0 || page <= 0 || pageNum <= 0) {
            return null;
        }

        int start = PageUtil.getStart(page, pageNum);
        int limit = PageUtil.getLimit(page, pageNum);

        List<Spu> spuList = spuDao.selectByCate(cateId, start, limit);
        return spuList;
    }

    @Override
    public List<Spu> getByCate(int cateId, boolean isPublished, int page, int pageNum) {
        List<Spu> spuList = this.getByCate(cateId, page, pageNum);
        if (spuList == null) {
            return null;
        }

        List<Spu> resultSpuList = new ArrayList<Spu>();
        for (Spu spu : spuList) {
            if (spu.getIsPublished() == true) {
                resultSpuList.add(spu);
            }
        }

        if (resultSpuList.size() == 0) {
            return null;
        } else {
            return resultSpuList;
        }
    }

    @Override
    public List<Spu> get(int[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        String idList = this.buildIntArrToString(ids, ",");
        /*if (StringUtils.isBlank(idList)) {
            return null;
        }*/
        return spuDao.selectByIdList(idList);
    }

    @Override
    public Spu get(int spuId) {
        if (spuId <= 0) {
            return null;
        }
        return spuDao.select(spuId);
    }

    @Override
    public int countAll(boolean isPublished) {
        return spuDao.countAll(isPublished);
    }

    @Override
    public int countByCate(int cateId, boolean isPublished) {
        if (cateId <= 0) {
            return 0;
        }
        return spuDao.countByCate(cateId, isPublished);
    }

    @Override
    public void add(Spu spu) {
        if (validate(spu)) {
            spuDao.insert(spu);
        }
    }

    @Override
    public void update(Spu spu) {
        if (validate(spu)) {
            spuDao.update(spu);
        }
    }

    @Override
    public void delete(int spuId) {
        if (spuId > 0) {
            spuDao.delete(spuId);
        }
    }

    private String buildIntArrToString(int[] ids, String token) {
        int len = ids.length;
        int i = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (; i < len - 1; i++) {
            sb.append(ids[i]);
            sb.append(",");
        }
        sb.append(ids[len - 1]);
        sb.append(")");
        return sb.toString();
    }

    private boolean validate(Spu spu) {
        if ((spu == null) || (spu.getCateId() <= 0) || (spu.getClusterId() <= 0)
            || (spu.getName() == null) || (spu.getProductNo() == null)) {
            return false;
        } else {
            return true;
        }
    }
}
