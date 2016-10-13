package com.subaru.maneki.model;

import java.io.Serializable;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
public class Prop implements Serializable {

    private int id;

    private int nameId;

    private int valueId;

    private int type;

    public Prop() {

    }

    public Prop(int nameId, int valueId, int type) {
        this.nameId = nameId;
        this.valueId = valueId;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public int getValueId() {
        return valueId;
    }

    public void setValueId(int valueId) {
        this.valueId = valueId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}