package com.subaru.maneki.exception;

import com.subaru.common.exception.BizException;

/**
 * @author zhangchaojie
 * @since 2016-08-31
 */
public class CartException extends BizException {

    public CartException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public CartException(String msg) {
        super(DEFAULT_ERROR_CODE, msg);
    }
}