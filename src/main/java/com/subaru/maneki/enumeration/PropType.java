package com.subaru.maneki.enumeration;

/**
 * @author zhangchaojie
 * @since 2016-08-17
 */
public enum PropType {

    SPU(0), SKU(1);

    public static PropType getPropType(int value) {
        for (PropType propType : PropType.values()) {
            if (propType.getValue() == value) {
                return propType;
            }
        }
        return null;
    }

    private PropType(int value) {
        this.setValue(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int value;
}
