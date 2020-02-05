package com.ezc.hsil.webapp.controller;




	import java.util.ArrayList;
	import java.util.Calendar;
	import java.util.Date;
	import java.util.HashSet;
	import java.util.List;
	import java.util.Set;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.format.annotation.DateTimeFormat;
	import org.springframework.security.core.Authentication;
	import org.springframework.security.core.context.SecurityContextHolder;
	import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.ModelAttribute;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestMethod;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.ResponseBody;

	import com.ezc.hsil.webapp.dto.ListSelector;
	import com.ezc.hsil.webapp.dto.ReportSelector;
	import com.ezc.hsil.webapp.dto.TpmRequestDto;
	import com.ezc.hsil.webapp.model.DistributorMaster;
	import com.ezc.hsil.webapp.model.EzcComments;
	import com.ezc.hsil.webapp.model.EzcRequestHeader;
	import com.ezc.hsil.webapp.model.Users;
	import com.ezc.hsil.webapp.service.IMasterService;
	import com.ezc.hsil.webapp.service.IReportService;
	import com.ezc.hsil.webapp.service.ITPMService;

	import lombok.extern.slf4j.Slf4j;
	
	@Slf4j
	@Controller 
	@RequestMapping("/faq")
	public class FAQController {

	   	    @RequestMapping(value = "/faqAll", method = RequestMethod.GET)
	    public String faqAll(Model model) {
	    	 
	        return "/FAQ/FAQ1"; 

	    }
	    
	   
	 
}
