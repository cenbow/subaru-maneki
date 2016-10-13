package com.subaru.maneki.exception;


/**
 * @author zhangchaojie
 * @since 2016-08-11
 */
public class UserRegisterException extends UserException {
    /**  */

    private static final long   serialVersionUID = 1L;
    private static final String ERROR_MSG        = "注册异常";

    public UserRegisterException(String msg) {
        super(msg);
    }

    public UserRegisterException() {
        super(ERROR_MSG);
    }
}
