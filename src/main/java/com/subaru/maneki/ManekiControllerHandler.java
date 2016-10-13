package com.subaru.maneki;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.subaru.mvc.spring.ControllerHandler;

/**
 * @author zhangchaojie
 * @since 2016-08-08
 */
@Component("ControllerHandler")
public class ManekiControllerHandler implements ControllerHandler {

    @Override
    public void postSyncHandle(HttpServletRequest request, HttpServletResponse response,
                               ModelAndView modelAndView) throws Exception {
        //    	System.out.println("sss");
    }

    @Override
    public void postAsyncHandle(HttpServletRequest request, HttpServletResponse response,
                                ModelAndView modelAndView) throws Exception {
        //    	System.out.println("sss");
    }
}
