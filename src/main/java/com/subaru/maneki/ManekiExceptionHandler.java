package com.subaru.maneki;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.subaru.mvc.spring.ExceptionHandler;

/**
 * @author zhangchaojie
 * @since 2016-08-08
 */
@Component("ExceptionHandler")
public class ManekiExceptionHandler implements ExceptionHandler {
    /**
    * Logger for this class
    */
    private static final Logger logger = Logger.getLogger(ManekiExceptionHandler.class);

    @Override
    public String resolveAsyncException(HttpServletRequest arg0, HttpServletResponse arg1,
                                        Exception arg2) {
        return arg2.getStackTrace().toString();
    }

    @Override
    public ModelAndView resolveSyncException(HttpServletRequest arg0, HttpServletResponse arg1,
                                             Exception arg2) {
        return new ModelAndView();
    }

}
