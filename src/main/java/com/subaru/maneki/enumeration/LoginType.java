package com.subaru.maneki.enumeration;


/**
 * 登录类型枚举类
 * @author zhangchaojie
 * @since 2016-08-15
 */
public enum LoginType {

    /*
     * 登录方式  1:邮箱登录 2：facebook登录
     */
    EMAIL(1), FACEBOOK(2);

    private int value;

    private LoginType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static LoginType getType(int value) {
        for (LoginType registerType : LoginType.values()) {
            if (registerType.value == value) {
                return registerType;
            }
        }
        return null;
    }

}
