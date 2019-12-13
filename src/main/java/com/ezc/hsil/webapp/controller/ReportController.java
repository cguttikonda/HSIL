package com.ezc.hsil.webapp.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.ezc.hsil.webapp.service.IReportService;
import com.ezc.hsil.webapp.service.ITPMService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller 
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    IReportService repService;
    
    @Autowired
    ITPMService tpmService;

    @RequestMapping(value = "/requeststatus", method = RequestMethod.GET)
    public String requestStatus(ListSelector listSelector , Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {

    	Date todayDate = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(todayDate); 
		c.add(Calendar.MONTH, -3);
		listSelector = new ListSelector();
		listSelector.setFromDate(c.getTime());
		listSelector.setToDate(todayDate);
		
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		ArrayList<String> userList=new ArrayList<String>();
    	if(requestWrapper.isUserInRole("ROLE_REQ_CR") || requestWrapper.isUserInRole("ROLE_ST_HEAD") || requestWrapper.isUserInRole("ROLE_BD_MKT"))
    	{
    		userList.add(userObj.getUserId());
    		listSelector.setUser(userList);
    	}

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
    
    @RequestMapping(value = "/stockavailbydept", method = RequestMethod.GET)
    public String getStockAvailabilityByDept(Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) 
    {	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		List<Object[]> userListObj = null; 
    	if(requestWrapper.isUserInRole("ROLE_ST_HEAD"))
    	{
    		userListObj=repService.getUsersByHead(userObj.getUserId()); 
    	}
    	if(requestWrapper.isUserInRole("ROLE_ZN_HEAD"))
    	{
    		userListObj=repService.getUsersByZoneHd(userObj.getUserId());
    	}
    	List<String> userList = new ArrayList<String>();
    	if(userListObj != null)
    		userListObj.forEach(obj->userList.add((String) obj[0]));
    	model.addAttribute("reqList", repService.getStockAvailabilityByUser(userList));
    	return "/reports/stockforallrep"; 
	}

    @RequestMapping("/stockavailuser")
    public String add(Model model) {
    	try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Users userObj = (Users)authentication.getPrincipal();
			model.addAttribute("reqList", tpmService.getLeftOverStock(userObj.getUserId()));
		} catch (Exception e) {
			
		}
    	return "/reports/stockavailability"; 

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
    
    @RequestMapping(value = "/teamTPMReport", method = RequestMethod.GET)
    public String teamTPMReport(ReportSelector reportSelector , Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {
    	if(reportSelector == null || reportSelector.getFromDate() == null)
    	{
    		Date todayDate = new Date();
    		Calendar c = Calendar.getInstance(); 
    		c.setTime(todayDate); 
    		c.add(Calendar.MONTH, -3);
    		reportSelector = new ReportSelector();
    		reportSelector.setType("TPM");
    		reportSelector.setFromDate(c.getTime());
    		reportSelector.setToDate(todayDate);    		
    	}
    	else
    	{
    		log.debug(":::reportSelector.setFromDate::"+reportSelector.getFromDate());
    	}
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		List<Object[]> userList= null;
		List<Object[]> tempUserList = null;
    	if(requestWrapper.isUserInRole("ROLE_ST_HEAD"))
    	{
    		userList=repService.getUsersByHead(userObj.getUserId());
    		reportSelector.setUserGrp(userList); 
    	}
    	if(requestWrapper.isUserInRole("ROLE_ZN_HEAD"))
    	{
    		userList=repService.getUsersByZoneHd(userObj.getUserId());
    		List<Object[]> stateHdGrp=repService.getStateHdByZoneHd(userObj.getUserId());
			/*
			 * for(int i = 0; i<userList.size(); i++){ Object[] tempUserArr =
			 * userList.get(i); String stateGrp = (String)tempUserArr[3]; if(stateGrp ==
			 * null || "null".equals(stateGrp) || "".equals(stateGrp)) {
			 * stateHdGrp.add(tempUserArr); userList.remove(tempUserArr); i--; } }
			 */
    		
    		reportSelector.setHdGrp(stateHdGrp);
    		reportSelector.setUserGrp(userList); 
    	}
    	tempUserList = userList;
    	
    	String selStHd = reportSelector.getSelStHd();
    	if(selStHd != null && !"null".equals(selStHd) && !"".equals(selStHd))
    	{
    		userList=repService.getUsersByHead(selStHd);
    		reportSelector.setUserGrp(userList); 
    	}
    	
    	String selUser = reportSelector.getSelUser();
    	if(selUser != null && !"null".equals(selUser) && !"".equals(selUser))
    	{
    		userList= new ArrayList<Object[]>();
    		userList.add(new Object[]{selUser});
    		reportSelector.setUserGrp(userList);
    	}
    	
    	
        List<EzcRequestHeader> list = repService.getTeamTPMReport(reportSelector);
        reportSelector.setUserGrp(tempUserList);
        model.addAttribute("reportSelector", reportSelector);
        model.addAttribute("reqList", list);
        return "reports/teamTPMReport"; 

    }
    
    @RequestMapping(value = "/teamTPSReport", method = RequestMethod.GET)
    public String teamTPSReport(ReportSelector reportSelector , Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {
    	if(reportSelector == null || reportSelector.getFromDate() == null)
    	{
    		Date todayDate = new Date();
    		Calendar c = Calendar.getInstance(); 
    		c.setTime(todayDate); 
    		c.add(Calendar.MONTH, -3);
    		reportSelector = new ReportSelector();
    		reportSelector.setFromDate(c.getTime());
    		reportSelector.setToDate(todayDate);    		
    	}
    	else
    	{
    		log.debug(":::reportSelector.setFromDate::"+reportSelector.getFromDate());
    	}
    	reportSelector.setType("TPS");
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		List<Object[]> userList= null;
		List<Object[]> tempUserList = null;
    	if(requestWrapper.isUserInRole("ROLE_ZN_HEAD"))
    	{
    		userList=repService.getUsersByZoneHd(userObj.getUserId());
    		List<Object[]> stateHdGrp=new ArrayList<Object[]>();
    		for(int i = 0; i<userList.size(); i++){
    			Object[] tempUserArr = userList.get(i);	
    			String stateGrp = (String)tempUserArr[3];
    			if(stateGrp == null || "null".equals(stateGrp)  || "".equals(stateGrp))
    			{
    				stateHdGrp.add(tempUserArr);
    				userList.remove(tempUserArr);
    				i--;
    			}
    		}
    		
    		reportSelector.setHdGrp(stateHdGrp);
    		reportSelector.setUserGrp(stateHdGrp); 
    	}
    	tempUserList = userList;
    	String selStHd = reportSelector.getSelStHd();
    	if(selStHd != null && !"null".equals(selStHd) && !"".equals(selStHd))
    	{
    		userList = new ArrayList<Object[]>();
    		userList.add(new Object[] {selStHd});
    		reportSelector.setUserGrp(userList); 
    	}
        List<EzcRequestHeader> list = null;
        if(userList != null && userList.size()>0)
        {
        	list = repService.getTeamTPMReport(reportSelector);
        }
        reportSelector.setUserGrp(tempUserList);
        model.addAttribute("reportSelector", reportSelector);
        model.addAttribute("reqList", list);
        return "reports/teamTPSReport"; 

    }
    
}
 