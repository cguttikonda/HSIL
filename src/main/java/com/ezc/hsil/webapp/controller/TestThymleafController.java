package com.ezc.hsil.webapp.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TestThymleafController {

	
	@GetMapping("/dashboard/*")
	public String showView(Principal principal,Authentication auth) {
		
		
		
		if(auth!= null && auth.isAuthenticated()) {
			
			log.info("Principal:::::::::{}",principal.getName());
			log.info("Authentication:::::::::{}",auth.getPrincipal());
			return "dashboard/index";
		}
		else
			return "redirect:/login?error=true";
		
	}
	
	
	
	
	
	
}
