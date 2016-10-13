package com.subaru.maneki.model;

import java.sql.Timestamp;

public class SpuCateRelation {
	/**
	 *spu的ID
	 */
	int spuId;
	
	/**
	 * cate的ID
	 */
	int cateId;
	
	Timestamp gmtCreate;

    Timestamp gmtUpdate;


	public int getSpuId() {
		return spuId;
	}

	public void setSpuId(int spuId) {
		this.spuId = spuId;
	}

	public int getCateId() {
		return cateId;
	}

	public void setCateId(int cateId) {
		this.cateId = cateId;
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
