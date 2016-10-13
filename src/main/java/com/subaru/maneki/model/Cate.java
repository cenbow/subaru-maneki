package com.subaru.maneki.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
public class Cate implements Serializable {

    /**
     *主键
     */
    private int       id;
    /**
     *父类目Id
     */
    private int       parentId;
    /**
     *类目名
     */
    private String    name;

    private int       oldId;
    
    private int		  sequence;
    /**
     * 创建时间
     */
    private Timestamp gmtCreate;
    /**
     * 修改时间
     */
    private Timestamp gmtUpdate;

    public Cate() {

    }

    /**
     * @param parentId
     * @param name
     * @param oldId
     * 如果没有传递SEQUENCE值，那么默认值设为0
     */
    public Cate(int parentId, String name, int oldId) {
        this.parentId = parentId;
        this.name = name;
        this.oldId = oldId;
        this.sequence = 0;
    }

    public Cate(int parentId, String name, int oldId, int sequence) {
        this.parentId = parentId;
        this.name = name;
        this.sequence = sequence;
        this.oldId = oldId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
    
}
