package com.subaru.maneki.exception;

import com.subaru.common.exception.BizException;

/**
 * @author zhangchaojie
 * @since 2016-08-11
 */
public class UserException extends BizException {

    public UserException(String msg) {
        super(DEFAULT_ERROR_CODE, msg);
    }

}
