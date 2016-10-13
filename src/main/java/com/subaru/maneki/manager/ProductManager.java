package com.subaru.maneki.manager;

import javax.servlet.http.HttpServletRequest;

import com.subaru.maneki.exception.ProductException;
import com.subaru.maneki.vo.ProductPageVO;
import com.subaru.maneki.vo.ProductVO;

/**
 * @author zhangchaojie
 * @since 2016-08-25
 */
public interface ProductManager {

    ProductVO getProductVO(int spuId) throws ProductException;
    
    ProductPageVO getProductDetail(HttpServletRequest request, ProductVO productVO) throws ProductException;

}
