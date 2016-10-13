package com.subaru.maneki.exception;

import com.subaru.common.exception.BizException;

/**
 * @author zhangchaojie
 * @since 2016-08-29
 */
public class TradeException extends BizException {

    public TradeException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public TradeException(String msg) {
        super(DEFAULT_ERROR_CODE, msg);
    }
}