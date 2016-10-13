package com.subaru.maneki.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.subaru.maneki.exception.ProductException;
import com.subaru.maneki.manager.ProductManager;
import com.subaru.maneki.vo.ProductPageVO;
import com.subaru.maneki.vo.ProductVO;
import com.subaru.common.http.DomainManager;

/**
 * @author zhangchaojie
 * @since 2016-08-08
 */
@Controller
public class ProductController {

    //    @Resource
    //    private ProductService      productService;
	
	@Resource
	private ProductManager	productManager;
	
    private static final Logger logger              = Logger.getLogger(ProductController.class);

//    private static String       PYTHON_SERVER_URL   = DomainManager.getInstance().getDomain(
//                                                        "python.server.url");

//    private static String       GET_PRODUCTLIST_URL = PYTHON_SERVER_URL + "/product_list";

    @RequestMapping(value = "/product/{spuId}", method = RequestMethod.GET)
    public String productListPage(HttpServletRequest request, @PathVariable int spuId, Model model) {
    	//从cookies中获取countryAbbr的信息
    	try{
    		ProductVO productVO = productManager.getProductVO(spuId);
        	System.out.println(productVO);
    		ProductPageVO productPageVO = productManager.getProductDetail(request, productVO);
    		
    		model.addAttribute("productPage", productPageVO);
    	}catch(ProductException pe){
    		System.out.println(pe);
    		return "page/error/404";
    	}

        //return "page/product/index_template";
    	return "page/product/details";
    }

}
