package com.subaru.maneki.vo;

import java.util.List;

public class ProductCategorySetVO {
	
	List<ProductListVO> productListVO;
	
	int 		parentId;
	
	String 		categoryName;
	
	int 		categoryId;

	public List<ProductListVO> getProductListVO() {
		return productListVO;
	}

	public void setProductListVO(List<ProductListVO> productListVO) {
		this.productListVO = productListVO;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
}
