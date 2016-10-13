package com.subaru.maneki.vo;

/**
 * @author zhangchaojie
 * @since 2016-08-24
 */
public class PropVO {

    private String propName;

    private String propValue;

    private int    sequence;

    public PropVO(){

    }

    public PropVO(String propName, String propValue){
        this.propName = propName;
        this.propValue = propValue;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
