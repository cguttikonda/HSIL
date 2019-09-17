package com.ezc.hsil.webapp.controller;



import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.Date;
import java.util.HashSet;
import java.util.Locale;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ezc.hsil.webapp.dto.RequestDetailDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.security.ActiveUserStore;
import com.ezc.hsil.webapp.service.IUserService;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UserController {

    @Autowired
    ActiveUserStore activeUserStore;

    @Autowired
    IUserService userService;

    @RequestMapping(value = "/loggedUsers", method = RequestMethod.GET)
    public String getLoggedUsers(final Locale locale, final Model model) {
      model.addAttribute("users", activeUserStore.getUsers());
      return "Content";
  }
    
    @RequestMapping(value = "/next", method = RequestMethod.GET)
    public String nextPage(final Locale locale, final Model model) {
      model.addAttribute("users", activeUserStore.getUsers());
      return "Content2";
  }    
    
    //    public String getLoggedUsers(final Locale locale, final Model model) {
//        model.addAttribute("users", activeUserStore.getUsers());
//        return "users";
//    }

    @RequestMapping(value = "/loggedUsersFromSessionRegistry", method = RequestMethod.GET)
    public String getLoggedUsersFromSessionRegistry(final Locale locale, final Model model) {
        model.addAttribute("users", userService.getUsersFromSessionRegistry());
        return "users";
    }
    
    @RequestMapping(value = "/addReq", method = RequestMethod.GET)
    public void addRequestData() {
    	
    	EzcRequestHeader reqHeader = new EzcRequestHeader();
    	reqHeader.setErhReqType("TPM");
    	reqHeader.setErhCreatedGroup("AP_GRP");
    	reqHeader.setErhNoOfAttendee(20);
    	reqHeader.setErhState("AP");
    	reqHeader.setErhRequestedBy("ADMIN");
    	reqHeader.setErhStatus("SUBMITTED");
    	reqHeader.setErhRequestedOn(new Date());
    	
    	    	
    	//userService.addReqData(reqHeader);
    	
    }
    
    
    @RequestMapping(value = "/addDetails", method = RequestMethod.POST)
    public String addDetailsData(@ModelAttribute RequestDetailDto form, Model model) {
    	
    	
    	
    	 
    	
    	EzcRequestItems ezcRequestItemses = new EzcRequestItems();
    	ezcRequestItemses.setEriPlumberName("TEST PLUMBER");
    	ezcRequestItemses.setEriPartName("DEALER");
    	ezcRequestItemses.setEriPartType("RETAILER");
    	ezcRequestItemses.setEriDob(new Date());
    	ezcRequestItemses.setEriContact("9441610585");
    	ezcRequestItemses.setEriDoa(new Date());
    	
    	HashSet<EzcRequestItems> hsItems = new HashSet<EzcRequestItems>();
    	System.out.println("form.getItems()::::::"+form.getItems());
    	hsItems.addAll(form.getItems());
    	
    	//if(reqHeader.isPresent()) {
    		
    		
    		
    		
    	//}
    	    	
    	userService.addReqData(hsItems);
    	
    	return "users";
    	
    }
    
    
    
    @GetMapping("/details")
    public String detailsPage(Model model) {
    	
    	EzcRequestHeader list = userService.findDetailsById(3);
//    	List<EzcRequestItems> list2 = new ArrayList<>();
//    	for(EzcRequestHeader rH :list ) {
//    		
//    		
//    		list2.addAll(rH.getEzcRequestItemses());
//    	};
//    	
//    	System.out.println("list:::::::::"+list.size());
    	
    	model.addAttribute("list", list);
    	
    	
    	return "Content2";
    	
    }
    
    @GetMapping("/tpsForm")
    
    public String showForm(Model model) {
    	
    	  log.info("Simple log statement with inputs {}, {} and {}", 1, 2, 3);
    	
    	RequestDetailDto rqDto = new RequestDetailDto();
    	
    	for(int i=0;i<=20;i++) {
    		
    		rqDto.adddetail(new EzcRequestItems());
    	}
    	
    	model.addAttribute("DetailForm", rqDto);
    	return "Content";
    	
    }
    
//@GetMapping("/")
//    
//    public String login(Model model) {
//    	
//    	  log.info("Login Form {}, {} and {}", 1, 2, 3);
//    	
//    	
//    	return "login";
//    	
//    }
    
    
}
