package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class CountryState implements Serializable{
	
	private int 	id;
	
	private int 	countryId;
	
	/**
	 * 州/省份的简写，一般是两个字符
	 */
	private String 	code;
	
	/**
	 * 具体州/省份的名称
	 */
	private String  name;
	
	private Timestamp gmtCreate;

    private Timestamp gmtUpdate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
