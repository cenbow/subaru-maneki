package com.subaru.maneki.enumeration;


/**
 * 注册类型枚举类
 * @author zhangchaojie
 * @since 2016-08-11
 */
public enum RegisterType {

    /*
     * 注册方式  1:邮箱注册 2：facebook注册
     */
    EMAIL(1), FACEBOOK(2);

    private int value;

    private RegisterType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static RegisterType getType(int value) {
        for (RegisterType registerType : RegisterType.values()) {
            if (registerType.value == value) {
                return registerType;
            }
        }
        return null;
    }

}
