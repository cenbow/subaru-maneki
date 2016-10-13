package com.subaru.maneki.exception;


/**
 * @author zhangchaojie
 * @since 2016-08-15
 */
public class UserLoginException extends UserException {
    /**  */

    private static final long   serialVersionUID = 1L;
    private static final String ERROR_MSG        = "登录异常";

    public UserLoginException(String msg) {
        super(msg);
    }

    public UserLoginException() {
        super(ERROR_MSG);
    }
}
