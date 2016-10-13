package com.subaru.maneki.service.product.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.ImageDao;
import com.subaru.maneki.dao.SkuImageRelationDao;
import com.subaru.maneki.dao.SpuImageRelationDao;
import com.subaru.maneki.model.Image;
import com.subaru.maneki.model.SkuImageRelation;
import com.subaru.maneki.model.SpuImageRelation;
import com.subaru.maneki.service.product.ImageService;

@Service("imageService")
public class ImageServiceImp implements ImageService{
	
	@Resource
	public ImageDao imageDao;
	
	@Resource
	public SpuImageRelationDao spuImageRelationDao;
	
	@Resource
	public SkuImageRelationDao skuImageRelationDao;
	
	@Override
	public int add(Image image){
		if(validate(image)){
			return imageDao.insert(image);
		}else{
			return -1;
		}
	}

	@Override
    public int update(Image image){
		if(validate(image)){
			return imageDao.update(image);
		}else{
			return -1;
		}
	}
	
	@Override
    public void delete(int id){
		if (id > 0){
			imageDao.delete(id);
		}
	}

	@Override
    public Image get(int id){
    	if(id > 0){
    		return imageDao.select(id);
    	}else{
    		return null;
    	}
    }

	@Override
    public void addSpuImage(int spuId, int imageId){
    	if (spuId <= 0 || imageId <= 0){
    		return;
    	}
    	
    	SpuImageRelation spuImage = new SpuImageRelation(spuId, imageId);
    	spuImageRelationDao.insert(spuImage);
    }

	
	@Override
    public void deleteSpuImage(int spuId, int imageId){
		if (spuId <= 0 || imageId <=0){
    		return;
    	}
		spuImageRelationDao.delete(spuId, imageId);
	}

	@Override
    public List<Image> getSpuImage(int spuId){
		if(spuId <= 0){
			return null;
		}
		List<SpuImageRelation> spuImageList = spuImageRelationDao.selectBySpuId(spuId);
		
		List<Image> imageList = new ArrayList<Image>();
		for (SpuImageRelation sir: spuImageList){
			int imageId = sir.getImageId();
			Image image = imageDao.select(imageId);
			imageList.add(image);
		}
		
		return imageList;
	}

	@Override
    public void addSkuImage(int skuId, int imageId){
		if (skuId <= 0 || imageId <= 0){
    		return;
    	}
    	
    	SkuImageRelation skuImage = new SkuImageRelation(skuId, imageId);
    	skuImageRelationDao.insert(skuImage);
	}

	@Override
    public void deleteSkuImage(int skuId, int imageId){
		if (skuId <= 0 || imageId <=0){
    		return;
    	}
		skuImageRelationDao.delete(skuId, imageId);
    }

	@Override
    public List<Image> getSkuImage(int skuId){
		if(skuId <= 0){
			return null;
		}
		List<SkuImageRelation> skuImageList = skuImageRelationDao.selectBySkuId(skuId);
		
		List<Image> imageList = new ArrayList<Image>();
		for (SkuImageRelation sir: skuImageList){
			int imageId = sir.getImageId();
			Image image = imageDao.select(imageId);
			imageList.add(image);
		}
		
		return imageList;
    }

	private boolean validate(Image image){
		if((image == null) || (image.getUrl() == null)){
			return false;
		}
		return true;
	}
}
