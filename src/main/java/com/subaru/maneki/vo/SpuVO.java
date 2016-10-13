package com.subaru.maneki.vo;

import java.util.List;
import com.subaru.maneki.model.Image;

/**
 * @author zhangchaojie
 * @since 2016-08-23
 */
public class SpuVO {
	
	int 	spuId;

    int     oldId;

    String  name;

    String  cateName;

    boolean isPublished;

    String  productNo;

    String  searchKeywords;

    String  comment;

    String  score;
    
    int		sold;

    int     clusterId;

    String  spuPropJson;

    String  skuPropJson;
        
    int newUserScore;

//    String  imageUrl;
//
//    String  smallImageUrl;
//
//    String  middleImageUrl;
//
//    int     imageSequence;
    
    List<Image> imageList;
    
    int 	overallScore;

    public int getOverallScore() {
		return overallScore;
	}

	public void setOverallScore(int overallScore) {
		this.overallScore = overallScore;
	}

	public int getSpuId() {
		return spuId;
	}

	public void setSpuId(int spuId) {
		this.spuId = spuId;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(boolean isPublished) {
        this.isPublished = isPublished;
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

    public String getSpuPropJson() {
        return spuPropJson;
    }

    public void setSpuPropJson(String spuPropJson) {
        this.spuPropJson = spuPropJson;
    }

    public String getSkuPropJson() {
        return skuPropJson;
    }

    public void setSkuPropJson(String skuPropJson) {
        this.skuPropJson = skuPropJson;
    }

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public List<Image> getImageList() {
		return imageList;
	}

	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}

	public int getNewUserScore() {
		return newUserScore;
	}

	public void setNewUserScore(int newUserScore) {
		this.newUserScore = newUserScore;
	}
    
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    public String getSmallImageUrl() {
//        return smallImageUrl;
//    }
//
//    public void setSmallImageUrl(String smallImageUrl) {
//        this.smallImageUrl = smallImageUrl;
//    }
//
//    public String getMiddleImageUrl() {
//        return middleImageUrl;
//    }
//
//    public void setMiddleImageUrl(String middleImageUrl) {
//        this.middleImageUrl = middleImageUrl;
//    }
//
//    public int getImageSequence() {
//        return imageSequence;
//    }
//
//    public void setImageSequence(int imageSequence) {
//        this.imageSequence = imageSequence;
//    }
}
