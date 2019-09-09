package com.ezc.hsil.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestThymleafController {

	
	@GetMapping("/thymeleaf")
	public String showView() {
		
		
		return "layout";
		
	}
	
	
	
}
