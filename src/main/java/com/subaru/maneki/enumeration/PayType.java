package com.subaru.maneki.enumeration;

/**
 * @author zhangchaojie
 * @since 2016-08-08
 */
public enum PayType {

    STRIPE(1), PAY_PAL(2), COD(3);

    private PayType(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static PayType getType(int value) {
        for (PayType type : PayType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }
}
