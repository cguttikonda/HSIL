package com.ezc.hsil.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ezc.hsil.webapp.dto.Output;
import com.ezc.hsil.webapp.service.IRequestService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RequestController {

	@Autowired
    IRequestService reqService; 
	
	@RequestMapping(value = "/request/revoke/{docId}", method = RequestMethod.GET)
    public String revokeDoc(@PathVariable String docId,Model model) {
		reqService.revokeRequest(docId);
		Output output=new Output();
    	output.setText("Request : "+docId+" has been revoked");
		output.setType("success");
		model.addAttribute("output",output);
		return "confirmpage";
	}
	
}
