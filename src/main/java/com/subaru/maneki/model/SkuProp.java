package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class SkuProp implements Serializable{
	private int id;
	
	/**
	 * sku的ID
	 */
	private int skuId;
	
	/**
	 * prop的ID
	 */
	private int propId;
	
	/**
     * 表示propId对应valueId记录中value值排序的依据
     * 之所以放在这里是因为原有odoo里每个商品对应sku属性值都有对应的sequence
     * 这样假如两个商品都有同样sku属性值，那么需要有两条记录来存放对应的SEQUENCE
     */
    private int	sequence;
	
	private Timestamp gmtCreate;

    private Timestamp gmtUpdate;
    
    public SkuProp(){
    	
    }
    
    public SkuProp(int skuId, int propId){
    	this.skuId = skuId;
    	this.propId = propId;
    	this.sequence = 0;
    }
    
    public SkuProp(int skuId, int propId, int sequence){
    	this.skuId = skuId;
    	this.propId = propId;
    	this.sequence = sequence;
    }
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSkuId() {
		return skuId;
	}
	public void setSkuId(int skuId) {
		this.skuId = skuId;
	}
	public int getPropId() {
		return propId;
	}
	public void setPropId(int propId) {
		this.propId = propId;
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

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
