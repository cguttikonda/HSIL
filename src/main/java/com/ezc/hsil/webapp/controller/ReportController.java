package com.ezc.hsil.webapp.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.service.IReportService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller 
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    IReportService repService;

    @RequestMapping(value = "/requeststatus", method = RequestMethod.GET)
    public String requestStatus(ListSelector listSelector , Model model) {
    	if(listSelector == null || listSelector.getFromDate() == null)
    	{
    		Date todayDate = new Date();
    		Calendar c = Calendar.getInstance(); 
    		c.setTime(todayDate); 
    		c.add(Calendar.MONTH, -3);
    		listSelector = new ListSelector();
    		listSelector.setStatus("ALL");
    		listSelector.setFromDate(c.getTime());
    		listSelector.setToDate(todayDate); 
    	}
        //List<EzcRequestHeader> list = tpmService.getTPMRequestList("NEW"); 
    	List<EzcRequestHeader> list = repService.getRequestStatus(listSelector);
        model.addAttribute("reqList", list); 
        model.addAttribute("listSelector", listSelector);
        return "/reports/requeststatus"; 

    } 
  
    @RequestMapping(value = "/stockavailablity", method = RequestMethod.GET)
    public String stockAvailablity(ListSelector listSelector , Model model) {
    	if(listSelector == null || listSelector.getFromDate() == null)
    	{
    		Date todayDate = new Date();
    		Calendar c = Calendar.getInstance(); 
    		c.setTime(todayDate); 
    		c.add(Calendar.MONTH, -3);
    		listSelector = new ListSelector();
    		listSelector.setStatus("ALL");
    		listSelector.setFromDate(c.getTime());
    		listSelector.setToDate(todayDate); 
    	}
        //List<EzcRequestHeader> list = tpmService.getTPMRequestList("NEW"); 
    	List<EzcRequestHeader> list = repService.getRequestStatus(listSelector);
        model.addAttribute("reqList", list); 
        model.addAttribute("listSelector", listSelector);
        return "/reports/stockavailability"; 

    }
    
      
    @RequestMapping(value = "/disprep/{status}", method = RequestMethod.GET)
    public String dispatchReport(@PathVariable String status,Model model) {
    	model.addAttribute("matList", repService.getDispatchReport(status)); 
        return "/reports/dispatchreport"; 

    }
    
    @RequestMapping(value = "/dispatchupdate", method = RequestMethod.POST)
	public @ResponseBody String dispatchUpdate(@RequestParam(value = "id", required = false) String id,@RequestParam(value = "dispComments", required = false) String dispComments ) {

    	EzcRequestHeader ezcRequestHeader = new EzcRequestHeader(); 
		ezcRequestHeader.setId(id);
		Set<EzcComments> comments = new HashSet<EzcComments>();
		if(dispComments != null)
		{
			
				EzcComments reqCom = new EzcComments();
				comments.add(reqCom);
		}
			ezcRequestHeader.setEzcComments(comments);
			repService.dispatchUpdate(ezcRequestHeader);
		return "ok";
	}
    
    @RequestMapping(value = "/stockavailforall", method = RequestMethod.GET)
    public String getStockAvailabilityAll(Model model) 
    {	
    	model.addAttribute("reqList", repService.getStockAvailabilityForAll());
    	return "/reports/stockforallrep"; 
	}

    @RequestMapping(value = "/dispack", method = RequestMethod.GET)
    public String dispatchAckReport(Model model) {
    	log.debug("in dispack");
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
    	model.addAttribute("matList", repService.getToAckDispReport(userObj.getUserId())); 
        return "/reports/dispactchackrep"; 

    }
    
    @RequestMapping(value = "/dispackupdate", method = RequestMethod.POST)
	public @ResponseBody String dispackUpdate(@RequestParam(value = "id", required = false) String id,@RequestParam(value = "dispComments", required = false) String dispComments ) {

    	EzcRequestHeader ezcRequestHeader = new EzcRequestHeader(); 
		ezcRequestHeader.setId(id);
		Set<EzcComments> comments = new HashSet<EzcComments>();
		if(dispComments != null)
		{
			
				EzcComments reqCom = new EzcComments();
				comments.add(reqCom);
		}
			ezcRequestHeader.setEzcComments(comments);
			repService.dispatchAckUpdate(ezcRequestHeader);
		return "ok";
	}
}
 