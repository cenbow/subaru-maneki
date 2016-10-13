package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author lee
 * 
 * SpuImageRelation对应的数据表中，是以spuId和imageId共同形成主键
 */
public class SpuImageRelation implements Serializable{
	/**
	 *spu的ID
	 */
	int spuId;
	
	/**
	 * image的ID
	 */
	int imageId;
	
	Timestamp gmtCreate;

    Timestamp gmtUpdate;
    
    public SpuImageRelation(){
    	
    }
    
    public SpuImageRelation(int spuId, int imageId){
    	this.spuId = spuId;
    	this.imageId = imageId;
    }

	public int getSpuId() {
		return spuId;
	}

	public void setSpuId(int spuId) {
		this.spuId = spuId;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public Timestamp getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Timestamp gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Timestamp getGmtUpdate() {
		return gmtUpdate;
	}

	public void setGmtUpdate(Timestamp gmtUpdate) {
		this.gmtUpdate = gmtUpdate;
	}
    
}
