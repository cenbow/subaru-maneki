package com.subaru.maneki.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.subaru.maneki.model.User;
import com.subaru.maneki.service.user.LoginService;

@Controller
public class CashBackController {
	
	@Resource
    private LoginService    loginService;

	@RequestMapping(value = "/how_it_works", method = {RequestMethod.GET})
	public String howItWork(HttpServletRequest request, Model model){
		if (!loginService.isLogin(request)) {
            String redirectUrl = "/how_it_works";
            return "redirect:/auth/login?redirectUrl=" + redirectUrl;
        }
		
		User currentUser = loginService.getCurrentUser(request);
		
		boolean isVisit = loginService.isFisrtLogin(currentUser.getId());
		if (isVisit == false){
			return "page/cash_back/how_it_works";
		}else{
			return "forward:/go_to_ebay";
		}
	}
	
	@RequestMapping(value = "/go_to_ebay", method = {RequestMethod.GET})
	public String goToEbay(HttpServletRequest request, Model model){
		if (!loginService.isLogin(request)) {
            String redirectUrl = "/go_to_ebay";
            return "redirect:/auth/login?redirectUrl=" + redirectUrl;
        }
		
		User currentUser = loginService.getCurrentUser(request);
		
		model.addAttribute("uid", currentUser.getId());
		
		return "page/cash_back/go_to_ebay";
	}
}
