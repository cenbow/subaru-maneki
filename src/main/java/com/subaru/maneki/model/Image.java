package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Image implements Serializable {
	int id;
	
	/**
	 * 图片的URL地址
	 */
	private String 	  url;
	
	private String 	  smallUrl;
	
	private String    middleUrl;
	
	/**
	 * 作为图片排序的依据
	 */
	private int		  sequence;
	
	/**
	 * 作为是否显示在首页的标志
	 */
	private boolean   isMain;
	/**
     * 创建时间
     */
    private Timestamp gmtCreate;
    /**
     * 修改时间
     */
    private Timestamp gmtUpdate;
    
    public Image(){
    	
    }
    
    /**
     * @param url
     * @param smallUrl
     * @param middleUrl
     * 如果没有传递SEQUENCE值，那么默认值设为0
     */
    public Image(String url, String smallUrl, String middleUrl){
    	this.url = url;
    	this.smallUrl = smallUrl;
    	this.middleUrl = middleUrl;
    	this.sequence = 0;
    }

    /**
     * @param url
     * @param smallUrl
     * @param middleUrl
     * @param sequence
     * 如果没有传递isMain的值，那么默认值设为false
     */
    public Image(String url, String smallUrl, String middleUrl, int sequence){
    	this.url = url;
    	this.smallUrl = smallUrl;
    	this.middleUrl = middleUrl;
    	this.sequence = sequence;
    	this.isMain = false;
    }
    
    public Image(String url, String smallUrl, String middleUrl, int sequence, boolean isMain){
    	this.url = url;
    	this.smallUrl = smallUrl;
    	this.middleUrl = middleUrl;
    	this.sequence = sequence;
    	this.isMain = isMain;
    }
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getSmallUrl() {
		return smallUrl;
	}

	public void setSmallUrl(String smallUrl) {
		this.smallUrl = smallUrl;
	}

	public String getMiddleUrl() {
		return middleUrl;
	}

	public void setMiddleUrl(String middleUrl) {
		this.middleUrl = middleUrl;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public boolean getIsMain() {
		return isMain;
	}

	public void setIsMain(boolean isMain) {
		this.isMain = isMain;
	}

	
}
