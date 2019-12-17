package com.ezc.hsil.webapp.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ezc.hsil.webapp.dto.ReportSelector;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.service.IReportService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TestThymleafController {

	@Autowired
    IReportService repService;
	
	@GetMapping("/dashboard/*")
	public String showView(Principal principal,Authentication auth,Model model,ReportSelector reportSelector,SecurityContextHolderAwareRequestWrapper requestWrapper) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObjt = (Users)authentication.getPrincipal();
		List<Object[]> userListObj = null; 
    	if(requestWrapper.isUserInRole("ROLE_ST_HEAD"))
    	{
    		userListObj=repService.getUsersByHead(userObjt.getUserId()); 
    	}
		
		if(auth!= null && auth.isAuthenticated()) {
			log.info("Principal:::::::::{}",principal.getName());
			log.info("Authentication:::::::::{}",auth.isAuthenticated());
			Users userObj = (Users)auth.getPrincipal();
			ArrayList<String> userRoles = new  ArrayList<String>();
			Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)auth.getAuthorities();
			for(SimpleGrantedAuthority authObj:authorities)
			{
				userRoles.add(authObj.getAuthority().trim());
				log.debug("role:::"+authObj.getAuthority());
			}
			List<Object[]> reqList=repService.getTPMMonthWise();
			
			  
				
			Map<String, String> htMap = repService.getDashBoardValues(userRoles,userObj.getUserId());
			 
			model.addAllAttributes(htMap);
			model.addAttribute("reqList", reqList);
			
			return "dashboard/index";
		}
		else
			return "redirect:/login?error=true";
		
	}
	
	
	
	
	
	
}
