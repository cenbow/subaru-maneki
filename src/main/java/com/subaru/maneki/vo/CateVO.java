package com.subaru.maneki.vo;

import java.util.List;

public class CateVO {
	
	private int id;
	
	private String name;
	
	private List<CateVO> childList;

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

	public List<CateVO> getChildList() {
		return childList;
	}

	public void setChildList(List<CateVO> childList) {
		this.childList = childList;
	}
	
}
