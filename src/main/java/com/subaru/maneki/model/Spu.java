package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
public class Spu implements Serializable {

    int       id;

    String    name;

    int       cateId;

    boolean   isPublished;

    String    productNo;

    String    searchKeywords;

    String    comment;

    String    score;
    
    /**
     * 卖出去的商品数量
     */
    int		  sold;

    int       clusterId;

    int       oldId;
    
    int 	  overallScore;
    
    int 	  newUserScore;
    
    List<SpuCateRelation> 	cateList;

    Timestamp gmtCreate;

    Timestamp gmtUpdate;

    public Spu() {

    }

    public Spu(String name, int cateId, boolean isPublished, String productNo,
               String searchKeywords, String comment, String score, int sold, int clusterId, int oldId, int overallScore) {

        this.name = name;
        this.cateId = cateId;
        this.isPublished = isPublished;
        this.productNo = productNo;
        this.searchKeywords = searchKeywords;
        this.comment = comment;
        this.score = score;
        this.sold = sold;
        this.clusterId = clusterId;
        this.oldId = oldId;
        this.overallScore = overallScore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getSearchKeywords() {
        return searchKeywords;
    }

    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

    public boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(boolean isPublished) {
        this.isPublished = isPublished;
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

	public int getOverallScore() {
		return overallScore;
	}

	public void setOverallScore(int overallScore) {
		this.overallScore = overallScore;
	}

	public int getNewUserScore() {
		return newUserScore;
	}

	public void setNewUserScore(int newUserScore) {
		this.newUserScore = newUserScore;
	}
    
	public List<SpuCateRelation> getCateList() {
		return cateList;
	}

	public void setCateList(List<SpuCateRelation> cateList) {
		this.cateList = cateList;
	}
}
