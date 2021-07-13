package com.ezc.hsil.webapp.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.ezc.hsil.webapp.dto.OverallReportDto;
import com.ezc.hsil.webapp.dto.ReportSelector;
import com.ezc.hsil.webapp.dto.TPMSummaryDto;
import com.ezc.hsil.webapp.dto.TPSSummaryDto;
import com.ezc.hsil.webapp.dto.TpmRequestDto;
import com.ezc.hsil.webapp.model.DistributorMaster;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.Roles;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.model.WorkGroup_Users;
import com.ezc.hsil.webapp.model.Work_Groups;
import com.ezc.hsil.webapp.service.IMasterService;
import com.ezc.hsil.webapp.service.IReportService;
import com.ezc.hsil.webapp.service.ITPMService;
import com.ezc.hsil.webapp.service.IUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller 
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    IReportService repService;
    
    @Autowired
    ITPMService tpmService;
    
    @Autowired
    IMasterService iMasterService;
    
    @Autowired
    IUserService iUserService;

    @RequestMapping(value = "/requeststatus", method = RequestMethod.GET)
    public String requestStatus(ListSelector listSelector , Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {

    	if(listSelector == null || listSelector.getFromDate() == null)
    	{
			Date todayDate = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(todayDate); 
			c.add(Calendar.MONTH, -3);
			listSelector = new ListSelector();
			listSelector.setFromDate(c.getTime());
			listSelector.setToDate(todayDate);
    	}
		
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
  
    @RequestMapping(value = "/requeststatus/{type}", method = RequestMethod.GET)
    public String requestStatus( @PathVariable String type, Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {
    	ListSelector listSelector = new ListSelector();
    	Date todayDate = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(todayDate); 
		if("UPCMG".equals(type))
		{
			listSelector.setFromDate(todayDate);
			c.add(Calendar.MONTH, +1);
			listSelector.setToDate(c.getTime());
		}
		else if("DUE".equals(type))
		{
			c.add(Calendar.MONTH, -3);
			listSelector.setFromDate(c.getTime());
			listSelector.setToDate(todayDate);
		}
		listSelector.setStatus("APPROVED");
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
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
    	model.addAttribute("matList", repService.getDispatchReport(status,userObj.getUserId())); 
        return "/reports/dispatchreport"; 

    }
    
    @RequestMapping(value = "/dispatchupdate", method = RequestMethod.POST)
    public @ResponseBody String dispatchUpdate(@RequestParam(value = "id", required = false) String id,@RequestParam(value = "dispComments", required = false) String dispComments,@RequestParam(value = "prdInv", required = false) String prdInv,@RequestParam @DateTimeFormat(pattern="dd/MM/yyyy") Date dispDate) {
    Double prdInvVal=0.0;	
    try
    {
    	log.debug("prdInv:::"+prdInv);
    	prdInvVal = Double.parseDouble(prdInv); 
    }catch(Exception e)
    {
    	
    }
    String loggedUser="";
    EzcRequestHeader ezcRequestHeader = new EzcRequestHeader(); 
           ezcRequestHeader.setId(id);
           Set<EzcComments> comments = new HashSet<EzcComments>();
           try {
                  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                  Users userObj = (Users)authentication.getPrincipal();
                  loggedUser=(String)userObj.getUserId();
                  
           } catch (Exception e) {
                  
           }
           if(dispComments != null)
           {
                  
                        EzcComments reqCom = new EzcComments();
                        reqCom.setType("DISPATCH");
                        reqCom.setComments(dispComments);
                        reqCom.setCreatedBy(loggedUser);
                        reqCom.setLastModifiedBy(loggedUser);
                        comments.add(reqCom);
           }
                  ezcRequestHeader.setEzcComments(comments);
                  ezcRequestHeader.setErhDispDate(dispDate);
                  ezcRequestHeader.setErhPrdInv(prdInvVal);
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
    	if(requestWrapper.isUserInRole("ADMIN"))
    	{
    		userListObj=repService.getAllStateHd();
    		userListObj.addAll(repService.getAllUsers());
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
			model.addAttribute("reqList", tpmService.getAllStock(userObj.getUserId()));
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
           String loggedUser="";
           try {
                  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                  Users userObj = (Users)authentication.getPrincipal();
                  loggedUser=(String)userObj.getUserId();
                  
           } catch (Exception e) {
                  
           }
           if(dispComments != null)
           {
                  
                        EzcComments reqCom = new EzcComments();
                        reqCom.setType("DISACK");
                        reqCom.setComments(dispComments);
                        reqCom.setCreatedBy(loggedUser);
                        reqCom.setLastModifiedBy(loggedUser);
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
    		reportSelector.setHdGrp(stateHdGrp); 
    		reportSelector.setUserGrp(userList); 
    	}
    	if(requestWrapper.isUserInRole("ADMIN"))
    	{
    		userList=repService.getAllUsers();
    		List<Object[]> stateHdGrp=repService.getAllStateHd();
    		reportSelector.setHdGrp(stateHdGrp);
    		reportSelector.setUserGrp(userList);
    	}
    	reportSelector.setDistList(iMasterService.findAll());
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
    		userList=repService.getStateHdByZoneHd(userObj.getUserId());
    		reportSelector.setHdGrp(userList);
    		reportSelector.setUserGrp(userList); 
    	}
    	if(requestWrapper.isUserInRole("ADMIN"))
    	{
    		
    		userList=repService.getAllStateHd();
    		reportSelector.setHdGrp(userList);
    		reportSelector.setUserGrp(userList);
    		reportSelector.setDistList(iMasterService.findAll());
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
    @RequestMapping(value = "/teamTPMSum", method = RequestMethod.GET)
    public String teamTPMSummary(ReportSelector reportSelector , Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {
    	if(reportSelector == null || reportSelector.getFromDate() == null)
    	{
    		Date todayDate = new Date();
    		Calendar c = Calendar.getInstance(); 
    		c.setTime(todayDate); 
    		c.add(Calendar.MONTH, -12);
    		reportSelector = new ReportSelector();
    		reportSelector.setFromDate(c.getTime());
    		reportSelector.setToDate(todayDate);    		
    	}
    	else
    	{
    		log.debug(":::reportSelector.setFromDate::"+reportSelector.getFromDate());
    	}
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		
		String loggedUser =userObj.getUserId(); 
		String loggedOnRole = new ArrayList<Roles>(userObj.getRoles()).get(0).getName();
		
		List<Object[]> tempUserList = null;
		List<Users> usersList= iUserService.getUsersList();
		List<Work_Groups> workGrpList= iUserService.getAllGroups();
		List<WorkGroup_Users> workGrpUsersList= iUserService.getWorkGrpUsers();
		Hashtable<String,String> workGrpHT=new Hashtable<String,String>(); 
		Hashtable<String,Users> usersListDet=new Hashtable<String,Users>();
		
		for(Users userObjTemp:usersList)
		{
			usersListDet.put(userObjTemp.getUserId(),userObjTemp);
		}
		
		for(Work_Groups grp:workGrpList)
		{
			workGrpHT.put(grp.getName(),grp.getDesc());
		}
		log.debug("workGrpHT:::"+workGrpHT);
		List<Object[]> list = null;
		{
        	list = repService.getTeamTPMSummary(reportSelector);
        }
		//filter rows by role start
		ArrayList<Object[]> toBeRemovedRows=new ArrayList<Object[]>();
		
		List<WorkGroup_Users> loggedOnWorkGroup=workGrpUsersList.stream().filter((e) ->  loggedUser.equals(e.getUserId())).collect(Collectors.toList());
		if(!loggedOnWorkGroup.isEmpty())
		{
			String logOnZonGrp=loggedOnWorkGroup.get(0).getZonalGrp();
			String logOnGrp=loggedOnWorkGroup.get(0).getGroupId();
			//String logOnStateGrp=loggedOnWorkGroup.get(0).getStateGrp();
			if(!list.isEmpty())
			{
				for(Object[] objArr:list)
				{
					Users userTempObj = usersListDet.get(objArr[5]+"");
					List<WorkGroup_Users> tempWorkGroup=workGrpUsersList.stream().filter((e) ->  userTempObj.getUserId().equals(e.getUserId())).collect(Collectors.toList());
					if(tempWorkGroup != null && !tempWorkGroup.isEmpty())
					{	
						WorkGroup_Users selTempWorkGroup= tempWorkGroup.get(0);
						if("ROLE_ZN_HEAD".equals(loggedOnRole))
						{
							if(!selTempWorkGroup.getZonalGrp().equals(logOnZonGrp))
								toBeRemovedRows.add(objArr);
						}
						else if("ROLE_ST_HEAD".equals(loggedOnRole))
						{
							if(!selTempWorkGroup.getGroupId().equals(logOnGrp) && !selTempWorkGroup.getStateGrp().equals(logOnGrp))
								toBeRemovedRows.add(objArr);
						}
						else if("ROLE_REQ_CR".equals(loggedOnRole))
						{
							//log.debug(loggedUser+":::loggedUser"+userTempObj.getUserId());
							if(!loggedUser.equals(userTempObj.getUserId()))
								toBeRemovedRows.add(objArr);
						}
					}
				}
				if(!toBeRemovedRows.isEmpty())
					list.removeAll(toBeRemovedRows);
			}
		}
		//filter rows by role end
        List<TPMSummaryDto> tpmSumList = new ArrayList<TPMSummaryDto>();
        SimpleDateFormat monthYearFmt = new SimpleDateFormat("MMM/yyyy");
        
        for(Object[] objArr:list)
        {
        	try {
        		int noOfAtt = checkNumInt(objArr[10]+"");
				Double costIncured = (Double)objArr[13];
				if(costIncured == null)
					costIncured = 0.0;
				Double avgValue = costIncured/noOfAtt;
				String [] tempQtyArr = (objArr[11]+"").split("#");
				TPMSummaryDto tpmSumDto=new TPMSummaryDto(); 
				tpmSumDto.setRequestDate((Date)objArr[0]);
				tpmSumDto.setRequestId(objArr[1]+"-"+objArr[2]);
				tpmSumDto.setMonth(monthYearFmt.format((Date)objArr[0]));
				tpmSumDto.setDistCode(objArr[3]+"");
				tpmSumDto.setDistName(objArr[4]+"");
				tpmSumDto.setEmpCode(objArr[5]+"");
				tpmSumDto.setEmpName(objArr[6]+"");
				tpmSumDto.setCity(objArr[7]+"");
				tpmSumDto.setRetailerName(objArr[12]+"");
				tpmSumDto.setNoOfAtt(noOfAtt);
				tpmSumDto.setExpense(costIncured);
				tpmSumDto.setAvgCost(avgValue);
				tpmSumDto.setGiftsRequested(checkNumInt(tempQtyArr[0]));
				tpmSumDto.setPendingGifts(checkNumInt(tempQtyArr[1]));
				tpmSumDto.setVertical(objArr[9]+"");
				
				Users userTempObj = usersListDet.get(objArr[5]+"");
				if(userTempObj != null)
				{
					List<Roles> roleList = new ArrayList<Roles>(userTempObj.getRoles());
					String tempRole = roleList.get(0).getName();
					String tempZone = "";
					String tempRepMgr = "";
					String zonalHead = "";
					String tempState = "";
					if("ROLE_REQ_CR".equals(tempRole))
					{
						List<WorkGroup_Users> selGrpUsersList=workGrpUsersList.stream().filter((e) ->  userTempObj.getUserId().equals(e.getUserId())).collect(Collectors.toList());
						//log.debug("selGrpUsersList::::"+selGrpUsersList.size());
						if(selGrpUsersList != null)
						{
							WorkGroup_Users selUserGrp=selGrpUsersList.get(0);
							tempState = selUserGrp.getGroupId();
							tempZone = selUserGrp.getZonalGrp();
							if(tempZone != null)
							{
								tempZone=tempZone.replaceAll("_ZONE_GRP","");
							}
							List<WorkGroup_Users> selGrpUsersZoneList=workGrpUsersList.stream().filter((e) ->  selUserGrp.getZonalGrp().equals(e.getZonalGrp())).collect(Collectors.toList());
							if(selGrpUsersZoneList != null)
							{
								for(WorkGroup_Users workGrpUsr:selGrpUsersZoneList)
								{
									Users tempSelUser=usersListDet.get(workGrpUsr.getUserId());
									List<Roles> roleListSel = new ArrayList<Roles>(tempSelUser.getRoles());
									String tempSelRole = roleListSel.get(0).getName();
									if("ROLE_ZN_HEAD".equals(tempSelRole))
									{
										zonalHead += tempSelUser.getFirstName()+",";
									}
									else if("ROLE_ST_HEAD".equals(tempSelRole) && (tempState+"_HD_GRP").equals(workGrpUsr.getGroupId()))
									{
										tempRepMgr += tempSelUser.getFirstName()+",";
									}
										
								}
							}
						}
					}
					else if("ROLE_ST_HEAD".equals(tempRole))
					{
						tempRepMgr = userTempObj.getFirstName();
						List<WorkGroup_Users> selGrpUsersList=workGrpUsersList.stream().filter((e) ->  userTempObj.getUserId().equals(e.getUserId())).collect(Collectors.toList());
						//log.debug("selGrpUsersList::::"+selGrpUsersList.size());
						if(selGrpUsersList != null)
						{
							WorkGroup_Users selUserGrp=selGrpUsersList.get(0);
							tempState = selUserGrp.getGroupId();
							if(tempState != null)
							{
								tempState=tempState.replaceAll("_HD_GRP","");
							}
							tempZone = selUserGrp.getZonalGrp();
							if(tempZone != null)
							{
								tempZone=tempZone.replaceAll("_ZONE_GRP","");
							}
							List<WorkGroup_Users> selGrpUsersZoneList=workGrpUsersList.stream().filter((e) ->  selUserGrp.getZonalGrp().equals(e.getZonalGrp())).collect(Collectors.toList());
							if(selGrpUsersZoneList != null)
							{
								for(WorkGroup_Users workGrpUsr:selGrpUsersZoneList)
								{
									Users tempSelUser=usersListDet.get(workGrpUsr.getUserId());
									List<Roles> roleListSel = new ArrayList<Roles>(tempSelUser.getRoles());
									String tempSelRole = roleListSel.get(0).getName();
									if("ROLE_ZN_HEAD".equals(tempSelRole))
									{
										zonalHead += tempSelUser.getFirstName()+",";
									}
										
								}
							}
						}
					}
						
					tpmSumDto.setSalesPersonName(userTempObj.getFirstName());
					tpmSumDto.setZone(tempZone);
					tpmSumDto.setState(workGrpHT.get(tempState));
					tpmSumDto.setReportingManager(tempRepMgr);
					tpmSumDto.setZonalHead(zonalHead);
					
					
				}
				else
				{
					tpmSumDto.setSalesPersonName("");
					tpmSumDto.setZone("");
					tpmSumDto.setState("");
					tpmSumDto.setReportingManager("");
					tpmSumDto.setZonalHead("");
				}
				tpmSumList.add(tpmSumDto);
			} catch (Exception e) {
				log.debug("Exception while generating tpm summary"+e);
			}
        }
        reportSelector.setUserGrp(tempUserList);
        model.addAttribute("reportSelector", reportSelector);
        model.addAttribute("reqList", tpmSumList);
        return "reports/teamTPMSummary"; 

    }    
    @RequestMapping(value = "/teamTPSSum", method = RequestMethod.GET)
    public String teamTPSSummary(ReportSelector reportSelector , Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {
    	if(reportSelector == null || reportSelector.getFromDate() == null)
    	{
    		Date todayDate = new Date();
    		Calendar c = Calendar.getInstance(); 
    		c.setTime(todayDate); 
    		c.add(Calendar.MONTH, -12);
    		reportSelector = new ReportSelector();
    		reportSelector.setFromDate(c.getTime());
    		reportSelector.setToDate(todayDate);    		
    	}
    	else
    	{
    		log.debug(":::reportSelector.setFromDate::"+reportSelector.getFromDate());
    	}
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		
		String loggedUser =userObj.getUserId(); 
		String loggedOnRole = new ArrayList<Roles>(userObj.getRoles()).get(0).getName();
		
		List<Object[]> tempUserList = null;
		List<Users> usersList= iUserService.getUsersList();
		List<Work_Groups> workGrpList= iUserService.getAllGroups();
		List<WorkGroup_Users> workGrpUsersList= iUserService.getWorkGrpUsers();
		Hashtable<String,String> workGrpHT=new Hashtable<String,String>(); 
		Hashtable<String,Users> usersListDet=new Hashtable<String,Users>();
		
		for(Users userObjTemp:usersList)
		{
			usersListDet.put(userObjTemp.getUserId(),userObjTemp);
		}
		
		for(Work_Groups grp:workGrpList)
		{
			workGrpHT.put(grp.getName(),grp.getDesc());
		}
		log.debug("workGrpHT:::"+workGrpHT);
		List<Object[]> list = null;
		{
        	list = repService.getTeamTPSSummary(reportSelector);
        }
		//filter rows by role start
		ArrayList<Object[]> toBeRemovedRows=new ArrayList<Object[]>();
		
		List<WorkGroup_Users> loggedOnWorkGroup=workGrpUsersList.stream().filter((e) ->  loggedUser.equals(e.getUserId())).collect(Collectors.toList());
		if(!loggedOnWorkGroup.isEmpty())
		{
			String logOnZonGrp=loggedOnWorkGroup.get(0).getZonalGrp();
			String logOnGrp=loggedOnWorkGroup.get(0).getGroupId();
			//String logOnStateGrp=loggedOnWorkGroup.get(0).getStateGrp();
			if(!list.isEmpty())
			{
				for(Object[] objArr:list)
				{
					Users userTempObj = usersListDet.get(objArr[5]+"");
					List<WorkGroup_Users> tempWorkGroup=workGrpUsersList.stream().filter((e) ->  userTempObj.getUserId().equals(e.getUserId())).collect(Collectors.toList());
					if(tempWorkGroup != null && !tempWorkGroup.isEmpty())
					{	
						WorkGroup_Users selTempWorkGroup= tempWorkGroup.get(0);
						if("ROLE_ZN_HEAD".equals(loggedOnRole))
						{
							if(!selTempWorkGroup.getZonalGrp().equals(logOnZonGrp))
								toBeRemovedRows.add(objArr);
						}
						else if("ROLE_ST_HEAD".equals(loggedOnRole))
						{
							if(!selTempWorkGroup.getGroupId().equals(logOnGrp) && !selTempWorkGroup.getStateGrp().equals(logOnGrp))
								toBeRemovedRows.add(objArr);
						}
						else if("ROLE_REQ_CR".equals(loggedOnRole))
						{
							//log.debug(loggedUser+":::loggedUser"+userTempObj.getUserId());
							if(!loggedUser.equals(userTempObj.getUserId()))
								toBeRemovedRows.add(objArr);
						}
					}
				}
				if(!toBeRemovedRows.isEmpty())
					list.removeAll(toBeRemovedRows);
			}
		}
		//filter rows by role end
        List<TPSSummaryDto> tpsSumList = new ArrayList<TPSSummaryDto>();
        SimpleDateFormat monthYearFmt = new SimpleDateFormat("MMM/yyyy");
        
        for(Object[] objArr:list)
        {
        	try {
        		int noOfAtt = checkNumInt(objArr[10]+"");
				Double costIncured = (Double)objArr[8];
				if(costIncured == null)
					costIncured = 0.0;
				Double avgValue = costIncured/noOfAtt;
				String [] tempQtyArr = (objArr[11]+"").split("#");
				TPSSummaryDto tpsSumDto=new TPSSummaryDto(); 
				tpsSumDto.setRequestDate((Date)objArr[0]);
				tpsSumDto.setRequestId(objArr[1]+"-"+objArr[2]);
				tpsSumDto.setMonth(monthYearFmt.format((Date)objArr[0]));
				tpsSumDto.setDistCode(objArr[3]+"");
				tpsSumDto.setDistName(objArr[4]+"");
				tpsSumDto.setEmpCode(objArr[5]+"");
				tpsSumDto.setEmpName(objArr[6]+"");
				tpsSumDto.setCity(objArr[7]+"");
				tpsSumDto.setRetailerName(objArr[12]+"");
				tpsSumDto.setNoOfAtt(noOfAtt);
				tpsSumDto.setExpense(costIncured);
				tpsSumDto.setAvgCost(avgValue);
				tpsSumDto.setGiftsRequested(checkNumInt(tempQtyArr[0]));
				tpsSumDto.setPendingGifts(checkNumInt(tempQtyArr[1]));
				tpsSumDto.setVertical(objArr[9]+"");
				
				Users userTempObj = usersListDet.get(objArr[5]+"");
				if(userTempObj != null)
				{
					List<Roles> roleList = new ArrayList<Roles>(userTempObj.getRoles());
					String tempRole = roleList.get(0).getName();
					String tempZone = "";
					String tempRepMgr = "";
					String zonalHead = "";
					String tempState = "";
					if("ROLE_REQ_CR".equals(tempRole))
					{
						List<WorkGroup_Users> selGrpUsersList=workGrpUsersList.stream().filter((e) ->  userTempObj.getUserId().equals(e.getUserId())).collect(Collectors.toList());
						//log.debug("selGrpUsersList::::"+selGrpUsersList.size());
						if(selGrpUsersList != null)
						{
							WorkGroup_Users selUserGrp=selGrpUsersList.get(0);
							tempState = selUserGrp.getGroupId();
							tempZone = selUserGrp.getZonalGrp();
							if(tempZone != null)
							{
								tempZone=tempZone.replaceAll("_ZONE_GRP","");
							}
							List<WorkGroup_Users> selGrpUsersZoneList=workGrpUsersList.stream().filter((e) ->  selUserGrp.getZonalGrp().equals(e.getZonalGrp())).collect(Collectors.toList());
							if(selGrpUsersZoneList != null)
							{
								for(WorkGroup_Users workGrpUsr:selGrpUsersZoneList)
								{
									Users tempSelUser=usersListDet.get(workGrpUsr.getUserId());
									List<Roles> roleListSel = new ArrayList<Roles>(tempSelUser.getRoles());
									String tempSelRole = roleListSel.get(0).getName();
									if("ROLE_ZN_HEAD".equals(tempSelRole))
									{
										zonalHead += tempSelUser.getFirstName()+",";
									}
									else if("ROLE_ST_HEAD".equals(tempSelRole) && (tempState+"_HD_GRP").equals(workGrpUsr.getGroupId()))
									{
										tempRepMgr += tempSelUser.getFirstName()+",";
									}
										
								}
							}
						}
					}
					else if("ROLE_ST_HEAD".equals(tempRole))
					{
						tempRepMgr = userTempObj.getFirstName();
						List<WorkGroup_Users> selGrpUsersList=workGrpUsersList.stream().filter((e) ->  userTempObj.getUserId().equals(e.getUserId())).collect(Collectors.toList());
						//log.debug("selGrpUsersList::::"+selGrpUsersList.size());
						if(selGrpUsersList != null)
						{
							WorkGroup_Users selUserGrp=selGrpUsersList.get(0);
							tempState = selUserGrp.getGroupId();
							if(tempState != null)
							{
								tempState=tempState.replaceAll("_HD_GRP","");
							}
							tempZone = selUserGrp.getZonalGrp();
							if(tempZone != null)
							{
								tempZone=tempZone.replaceAll("_ZONE_GRP","");
							}
							List<WorkGroup_Users> selGrpUsersZoneList=workGrpUsersList.stream().filter((e) ->  selUserGrp.getZonalGrp().equals(e.getZonalGrp())).collect(Collectors.toList());
							if(selGrpUsersZoneList != null)
							{
								for(WorkGroup_Users workGrpUsr:selGrpUsersZoneList)
								{
									Users tempSelUser=usersListDet.get(workGrpUsr.getUserId());
									List<Roles> roleListSel = new ArrayList<Roles>(tempSelUser.getRoles());
									String tempSelRole = roleListSel.get(0).getName();
									if("ROLE_ZN_HEAD".equals(tempSelRole))
									{
										zonalHead += tempSelUser.getFirstName()+",";
									}
										
								}
							}
						}
					}
						
					tpsSumDto.setSalesPersonName(userTempObj.getFirstName());
					tpsSumDto.setZone(tempZone);
					tpsSumDto.setState(workGrpHT.get(tempState));
					tpsSumDto.setReportingManager(tempRepMgr);
					tpsSumDto.setZonalHead(zonalHead);
					
					
				}
				else
				{
					tpsSumDto.setSalesPersonName("");
					tpsSumDto.setZone("");
					tpsSumDto.setState("");
					tpsSumDto.setReportingManager("");
					tpsSumDto.setZonalHead("");
				}
				tpsSumList.add(tpsSumDto);
			} catch (Exception e) {
				log.debug("Exception while generating tps summary"+e);
			}
        }
        reportSelector.setUserGrp(tempUserList);
        model.addAttribute("reportSelector", reportSelector);
        model.addAttribute("reqList", tpsSumList);
        return "reports/teamTPSSummary"; 

    }
    @RequestMapping(value = "/teamBDSum", method = RequestMethod.GET)
    public String teamBDSummary(ReportSelector reportSelector , Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {
    	if(reportSelector == null || reportSelector.getFromDate() == null)
    	{
    		Date todayDate = new Date();
    		Calendar c = Calendar.getInstance(); 
    		c.setTime(todayDate); 
    		c.add(Calendar.MONTH, -12);
    		reportSelector = new ReportSelector();
    		reportSelector.setFromDate(c.getTime());
    		reportSelector.setToDate(todayDate);    		
    	}
    	else
    	{
    		log.debug(":::reportSelector.setFromDate::"+reportSelector.getFromDate());
    	}
	List<Object[]> list = null;
	{
	list = repService.getTeamBDSummary(reportSelector);
	}
        model.addAttribute("reportSelector", reportSelector);
        model.addAttribute("reqList", list);
        return "reports/teamBDSummary"; 

    }
    @RequestMapping(value = "/teamINFSum", method = RequestMethod.GET)
    public String teamINFSummary(ReportSelector reportSelector , Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {
    	if(reportSelector == null || reportSelector.getFromDate() == null)
    	{
    		Date todayDate = new Date();
    		Calendar c = Calendar.getInstance(); 
    		c.setTime(todayDate); 
    		c.add(Calendar.MONTH, -12);
    		reportSelector = new ReportSelector();
    		reportSelector.setFromDate(c.getTime());
    		reportSelector.setToDate(todayDate);    		
    	}
    	else
    	{
    		log.debug(":::reportSelector.setFromDate::"+reportSelector.getFromDate());
    	}
	List<Object[]> list = null;
	{
	list = repService.getTeamINFSummary(reportSelector);
	}
        model.addAttribute("reportSelector", reportSelector);
        model.addAttribute("reqList", list);
        return "reports/teamINFSummary"; 

    }
    @RequestMapping(value = "/plumbermaster", method = RequestMethod.GET)
    public String plumberMaster(ReportSelector reportSelector,Model model) {
    	String selDist = reportSelector.getSelDist();
    	List<DistributorMaster> distMastList = iMasterService.findAll();
    	List<String> list = new ArrayList<String>();
        model.addAttribute("distList", distMastList);
        if(selDist != null && !"null".equals(selDist) && !"".equals(selDist))
        {
        	list.add(selDist);
        }
        else
        {
        	distMastList.forEach(obj->list.add(obj.getCode()));
        }
        model.addAttribute("reqList", repService.getPlumberMaster(list));
        model.addAttribute("selDist", "");
        return "reports/plumberMaster"; 

    }
    @GetMapping(value="/monthWiseReport")
    public String overAllReport(Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
              Users userObj = (Users)authentication.getPrincipal();
              List<Object[]> reqHeaderLt= repService.getAllReqMonthWise();
              List<Object[]> reqDealerLt = repService.getAllMeetsMonthWise();
              List<Object[]> userDefaults=repService.getUserDefaults();
              List<Object[]> noofPlumb=repService.getNoofPlumbersPerUser();
              List<Object[]> usedLeftQty=repService.getUsedLeftQtyPerUser();
              List<Object[]> inProcessLt=repService.getAllInProcessReqPerUser();
              
              List<OverallReportDto> overallRepLt=new ArrayList<OverallReportDto>();
              //OverallReportDto compRep=new OverallReportDto();
              Hashtable<String, String> reportHt=new Hashtable<String, String>();
              Hashtable<String, String> plumberHt=new Hashtable<String, String>();
              Hashtable<String, String> meetHt=new Hashtable<String, String>();
              Hashtable<String, String> usedQtytHT=new Hashtable<String, String>();
              Hashtable<String, String> leftQtytHT=new Hashtable<String, String>();
              Hashtable<String, String> inProcesstHT=new Hashtable<String, String>();
              Hashtable<String, String> headerHT=new Hashtable<String, String>();
              
              for(Object[] plum:noofPlumb)
              {
                     String key=(String)plum[1]+(String)plum[2];
                     log.debug("value"+(BigInteger)plum[0]);
                     String value=((BigInteger)plum[0])+"";
                     log.debug("value"+value);
                     if(!plumberHt.containsKey(key))
                           plumberHt.put(key,value);
                     else
                     {
                           String val=plumberHt.get(key);
                           val=(checkNumInt(val)+checkNumInt(value))+"";
                           plumberHt.put(key, val);
                     }
              }
              log.debug("plumberHt"+plumberHt);
              for(Object[] meet:reqDealerLt)
              {
                     String key=(String)meet[3]+(String)meet[0];
                     String value=((BigInteger)meet[1])+"";
                     if(!meetHt.containsKey(key))
                           meetHt.put(key, value);
                     else
                     {
                           String val=meetHt.get(key);
                           val=(checkNumInt(val)+checkNumInt(value))+"";
                           meetHt.remove(key);
                           meetHt.put(key, val);
                     }
              }
              log.debug("meetHt"+meetHt);
              for(Object[] qty:usedLeftQty)
              {
                     String key=(String)qty[3]+(String)qty[4];
                     BigDecimal valueBD=(BigDecimal)qty[2];
                     String value=valueBD.toString(); 
                     if(!leftQtytHT.containsKey(key))
                           leftQtytHT.put(key, value);
                     else
                     {
                           String val=leftQtytHT.get(key);
                           val=(checkNumInt(val)+checkNumInt(value))+"";
                           leftQtytHT.remove(key);
                           leftQtytHT.put(key, val);
                     }
              }
              log.debug("leftQtytHT"+leftQtytHT);
              for(Object[] qty:usedLeftQty)
              {
                     String key=(String)qty[3]+(String)qty[4];
                     BigDecimal valueBD=(BigDecimal)qty[1];
                     String value=valueBD.toString(); 
                     if(!usedQtytHT.containsKey(key))
                           usedQtytHT.put(key, value);
                     else
                     {
                           String val=usedQtytHT.get(key);
                           val=(checkNumInt(val)+checkNumInt(value))+"";
                           usedQtytHT.remove(key);
                           usedQtytHT.put(key, val);
                     }
              }
              log.debug("usedQtytHT"+usedQtytHT);
              for(Object[] inProc:inProcessLt)
              {
                     
                     String key=(String)inProc[2]+(String)inProc[1];
                     String value=((BigDecimal)inProc[3])+"";
                     if(!inProcesstHT.containsKey(key))
                           inProcesstHT.put(key, value);
              }
              log.debug("inProcesstHT"+inProcesstHT);
              for(Object[] obj:userDefaults)
              {
                     if(!reportHt.containsKey((String)obj[2]))
                           reportHt.put((String)obj[2], (String)obj[3]+"#"+(String)obj[4]+"#"+(String)obj[1]+"#"+(String)obj[4]);
                     /*compRep.setEmpCode((String)obj[2]);
                     compRep.setZone((String)obj[3]);
                     compRep.setArea((String)obj[4]);
                     compRep.setEmpName((String)obj[1]);
                     compRep.setEmpRole((String)obj[4]);
                     overallRepLt.add(compRep);*/
              }
              
              log.debug("meetHt"+meetHt);
              for(Object[] reqHd:reqHeaderLt)
              {
                                  
                     String empFromHeaderLt=(String)reqHd[3];
                     int mon=(Integer)reqHd[0];
                     String stType=(String)reqHd[2];
                     String keyHeader=empFromHeaderLt+stType+mon;
                     BigDecimal tpValBD=(BigDecimal) reqHd[4];
                     int tpVal=tpValBD.intValue();
                     String tpValue=tpVal+"";
                     if(!headerHT.containsKey(keyHeader))
                           headerHT.put(keyHeader, tpValue);
                     
              }
              
       
        log.debug("headerHT"+headerHT);

        Set<String> keys = reportHt.keySet();

        for(String key: keys)
        {
             OverallReportDto compRep=new OverallReportDto();
             String keyVal=key;
             String val=reportHt.get(keyVal);
             
             int valLen=val.split("#").length;
             String zone=val.split("#")[0];
             String area=val.split("#")[1];
             String name=val.split("#")[2];
             String role=val.split("#")[3];
             int janTPS=0,janTPM=0,febTPS=0,febTPM=0,marTPM=0,marTPS=0,aprTPM=0,aprTPS=0,mayTPM=0,mayTPS=0,junTPM=0,junTPS=0;
             int julTPS=0,julTPM=0,augTPS=0,augTPM=0,sepTPM=0,sepTPS=0,octTPM=0,octTPS=0,novTPM=0,novTPS=0,decTPM=0,decTPS=0;
             int meetTPM=0,meetTPS=0,plumTPM=0,plumTPS=0,usedQtyTPM=0,usedQtyTPS=0,leftQtyTPM=0,leftQtyTPS=0;
             int inProcessTPM=0,inProcessTPS=0;
             String janTPMBGC="",janTPSBGC="",febTPMBGC="",febTPSBGC="",marTPMBGC="",marTPSBGC="",aprTPMBGC="",aprTPSBGC="",mayTPMBGC="",mayTPSBGC="",junTPMBGC="",junTPSBGC="",julTPMBGC="",julTPSBGC="",augTPMBGC="",augTPSBGC="",sepTPMBGC="",sepTPSBGC="",octTPMBGC="",octTPSBGC="",novTPMBGC="",novTPSBGC="",decTPMBGC="",decTPSBGC="";
             String meetsDoneTPMBGC="",meetsDoneTPSBGC="",noOFPlumTPMBGC="",noOFPlumTPSBGC="",usedQtyTPMBGC="",usedQtyTPSBGC="",leftQtyTPMBGC="",leftQtyTPSBGC="",inProcessTPMBGC="",inProcessTPSBGC="";

             String keyTPM=keyVal+"TPM";
             String keyTPS=keyVal+"TPS";             
             compRep.setEmpCode(key);
              compRep.setZone(zone);
              compRep.setArea(area);
              compRep.setEmpName(name);
              compRep.setEmpRole(role);
              log.debug("role"+role);
              String janKeyTPM=keyVal+"TPM1";
              String janKeyTPS=keyVal+"TPS1";
              String febKeyTPM=keyVal+"TPM2";
              String febKeyTPS=keyVal+"TPS2";
              String marKeyTPM=keyVal+"TPM3";
              String marKeyTPS=keyVal+"TPS3";
              String aprKeyTPM=keyVal+"TPM4";
              String aprKeyTPS=keyVal+"TPS4";
              String mayKeyTPM=keyVal+"TPM5";
              String mayKeyTPS=keyVal+"TPS5";
              String junKeyTPM=keyVal+"TPM6";
              String junKeyTPS=keyVal+"TPS6";
              String julKeyTPM=keyVal+"TPM7";
              String julKeyTPS=keyVal+"TPS7";
              String augKeyTPM=keyVal+"TPM8";
              String augKeyTPS=keyVal+"TPS8";
              String sepKeyTPM=keyVal+"TPM9";
              String sepKeyTPS=keyVal+"TPS9";
              String octKeyTPM=keyVal+"TPM10";
              String octKeyTPS=keyVal+"TPS10";
              String novKeyTPM=keyVal+"TPM11";
              String novKeyTPS=keyVal+"TPS11";
              String decKeyTPM=keyVal+"TPM12";
              String decKeyTPS=keyVal+"TPS12";
              log.debug("aprTP4"+keyVal);
            if(headerHT.containsKey(janKeyTPM))janTPM=checkNumInt((String)headerHT.get(janKeyTPM));
            if(headerHT.containsKey(janKeyTPS))janTPS=checkNumInt((String)headerHT.get(janKeyTPM));
            if(headerHT.containsKey(febKeyTPM))febTPM=checkNumInt((String)headerHT.get(febKeyTPM));
            if(headerHT.containsKey(febKeyTPS))febTPS=checkNumInt((String)headerHT.get(febKeyTPS));
            if(headerHT.containsKey(marKeyTPM))marTPM=checkNumInt((String)headerHT.get(marKeyTPM));
            if(headerHT.containsKey(marKeyTPS))marTPS=checkNumInt((String)headerHT.get(marKeyTPS));
            if(headerHT.containsKey(aprKeyTPM))aprTPM=checkNumInt((String)headerHT.get(aprKeyTPM));
            if(headerHT.containsKey(aprKeyTPS))aprTPS=checkNumInt((String)headerHT.get(aprKeyTPS));
            if(headerHT.containsKey(mayKeyTPM))mayTPM=checkNumInt((String)headerHT.get(mayKeyTPM));
            if(headerHT.containsKey(mayKeyTPS))mayTPS=checkNumInt((String)headerHT.get(mayKeyTPS));
            if(headerHT.containsKey(junKeyTPM))junTPM=checkNumInt((String)headerHT.get(junKeyTPM));
            if(headerHT.containsKey(junKeyTPS))junTPS=checkNumInt((String)headerHT.get(junKeyTPS));
              log.debug("aprTP3"+aprTPM);
            if(headerHT.containsKey(julKeyTPM))julTPM=checkNumInt((String)headerHT.get(julKeyTPM));
            if(headerHT.containsKey(julKeyTPS))julTPS=checkNumInt((String)headerHT.get(julKeyTPS));
            if(headerHT.containsKey(augKeyTPM))augTPM=checkNumInt((String)headerHT.get(augKeyTPM));
            if(headerHT.containsKey(augKeyTPS))augTPS=checkNumInt((String)headerHT.get(augKeyTPS));
            if(headerHT.containsKey(sepKeyTPM))sepTPM=checkNumInt((String)headerHT.get(sepKeyTPM));
            if(headerHT.containsKey(sepKeyTPS))sepTPS=checkNumInt((String)headerHT.get(sepKeyTPS));
            if(headerHT.containsKey(octKeyTPM))octTPM=checkNumInt((String)headerHT.get(octKeyTPM));
            if(headerHT.containsKey(octKeyTPS))octTPS=checkNumInt((String)headerHT.get(octKeyTPS));
            if(headerHT.containsKey(novKeyTPM))novTPM=checkNumInt((String)headerHT.get(novKeyTPM));
            if(headerHT.containsKey(novKeyTPS))novTPS=checkNumInt((String)headerHT.get(novKeyTPS));
            if(headerHT.containsKey(decKeyTPM))decTPM=checkNumInt((String)headerHT.get(decKeyTPM));
            if(headerHT.containsKey(decKeyTPS))decTPS=checkNumInt((String)headerHT.get(decKeyTPS));
              if(meetHt.containsKey(keyTPM))meetTPM=checkNumInt((String)meetHt.get(keyTPM));
              if(meetHt.containsKey(keyTPS))meetTPS=checkNumInt((String)meetHt.get(keyTPS));
              log.debug("keyTPM"+keyTPM);
              if(inProcesstHT.containsKey(keyTPM))
                     {inProcessTPM=checkNumInt((String)inProcesstHT.get(keyTPM));
                     log.debug("inProcessTPM"+inProcessTPM);
                     }
       if(inProcesstHT.containsKey(keyTPS))inProcessTPS=checkNumInt((String)inProcesstHT.get(keyTPS));
              if(plumberHt.containsKey(keyTPM))plumTPM=checkNumInt((String)plumberHt.get(keyTPM));
              if(plumberHt.containsKey(keyTPS))plumTPS=checkNumInt((String)plumberHt.get(keyTPS));
          if(usedQtytHT.containsKey(keyTPM))usedQtyTPM=checkNumInt((String)usedQtytHT.get(keyTPM));
          if(usedQtytHT.containsKey(keyTPS))usedQtyTPS=checkNumInt((String)usedQtytHT.get(keyTPS));
          if(leftQtytHT.containsKey(keyTPM))leftQtyTPM=checkNumInt((String)leftQtytHT.get(keyTPM));
          if(leftQtytHT.containsKey(keyTPS))leftQtyTPS=checkNumInt((String)leftQtytHT.get(keyTPS));

          if(janTPM>2)janTPMBGC="green";
          else
        	  janTPMBGC="orange";
          if(febTPM>2)febTPMBGC="green";
          else
          febTPMBGC="orange";

          if(marTPM>2)marTPMBGC="green";
          else
          marTPMBGC="orange";

          if(aprTPM>2)aprTPMBGC="green";
          else
          aprTPMBGC="orange";


          if(mayTPM>2)mayTPMBGC="green";
          else
          mayTPMBGC="orange";

          if(junTPM>2)junTPMBGC="green";
          else
          junTPMBGC="orange";

          if(julTPM>2)julTPMBGC="green";
          else
          julTPMBGC="orange";

          if(augTPM>2)augTPMBGC="green";
          else
          augTPMBGC="orange";

          if(sepTPM>2)sepTPMBGC="green";
          else
          sepTPMBGC="orange";

          if(octTPM>2)octTPMBGC="green";
          else
          octTPMBGC="orange";

          if(novTPM>2)novTPMBGC="green";
          else
          novTPMBGC="orange";

          if(decTPM>2)decTPMBGC="green";
          else
          decTPMBGC="orange";
          
          if(meetTPM>2)meetsDoneTPMBGC="green";
          else
        	  meetsDoneTPMBGC="orange";
          
          if(plumTPM>2)noOFPlumTPMBGC="green";
          else
        	  noOFPlumTPMBGC="orange";
          
          if(usedQtyTPM>2)usedQtyTPMBGC="green";
          else
        	  usedQtyTPMBGC="orange";
          
          if(leftQtyTPM>2)leftQtyTPMBGC="green";
          else
        	  leftQtyTPMBGC="orange";
          
          if(inProcessTPM>2)inProcessTPMBGC="green";
          else
        	  inProcessTPMBGC="orange";
          
          if(janTPS>0)janTPSBGC="green";
          if(febTPS>0)febTPSBGC="green";
          if(marTPS>0)marTPSBGC="green";
          if(aprTPS>0)aprTPSBGC="green";
          if(mayTPS>0)mayTPSBGC="green";
          if(junTPS>0)junTPSBGC="green";
          if(julTPS>0)julTPSBGC="green";
          if(augTPS>0)augTPSBGC="green";
          if(sepTPS>0)sepTPSBGC="green";
          if(octTPS>0)octTPSBGC="green";
          if(novTPS>0)novTPSBGC="green";
          if(decTPS>0)decTPSBGC="green";
          if(meetTPS>0)meetsDoneTPSBGC="green";
          if(plumTPS>0)noOFPlumTPSBGC="green";
          if(usedQtyTPS>0)usedQtyTPSBGC="green";
          if(leftQtyTPS>0)leftQtyTPSBGC="green";
          if(inProcessTPS>0)inProcessTPSBGC="green";
          
          compRep.setJanTPM(janTPM);
          compRep.setJanTPS(janTPS);
          compRep.setFebTPS(febTPS);
          compRep.setFebTPM(febTPM);
          compRep.setMarTPM(marTPM);
          compRep.setMarTPS(marTPS);
          compRep.setAprTPM(aprTPM);
          compRep.setAprTPS(aprTPS);
          compRep.setMayTPM(mayTPM);
          compRep.setMayTPS(mayTPS);
          compRep.setJunTPM(junTPM);
          compRep.setJunTPS(junTPS);
          compRep.setJulyTPM(julTPM);
          compRep.setJulyTPS(julTPS);
          compRep.setAugTPM(augTPM);
          compRep.setAugTPS(augTPS);
          compRep.setSepTPM(sepTPM);
          compRep.setSepTPS(sepTPS);
          compRep.setOctTPM(octTPM);
          compRep.setOctTPS(octTPS);
          compRep.setNovTPM(novTPM);
          compRep.setNovTPS(novTPS);
          compRep.setDecTPM(decTPM);
          compRep.setDecTPS(decTPS);
          compRep.setMeetsDoneTPM(meetTPM);
          compRep.setMeetsDoneTPS(meetTPS);
          compRep.setInProcessTPM(inProcessTPM);
          compRep.setInProcessTPS(inProcessTPS);
          compRep.setNoOFPlumTPM(plumTPM);
          compRep.setNoOFPlumTPS(plumTPS);
          compRep.setUsedQtyTPM(usedQtyTPM);
          compRep.setUsedQtyTPS(usedQtyTPS);
          compRep.setLeftQtyTPM(leftQtyTPM);
          compRep.setLeftQtyTPS(leftQtyTPS);
          compRep.setAprTPMBGC(aprTPMBGC);
          compRep.setAprTPSBGC(aprTPSBGC);
          compRep.setMayTPMBGC(mayTPMBGC);
          compRep.setMayTPSBGC(mayTPSBGC);
          compRep.setJunTPMBGC(junTPMBGC);
          compRep.setJunTPSBGC(junTPSBGC);
          compRep.setJulTPMBGC(julTPMBGC);
          compRep.setJulTPSBGC(julTPSBGC);
          compRep.setAugTPMBGC(augTPMBGC);
          compRep.setAugTPSBGC(augTPSBGC);
          compRep.setSepTPMBGC(sepTPMBGC);
          compRep.setSepTPSBGC(sepTPSBGC);
          compRep.setOctTPMBGC(octTPMBGC);
          compRep.setOctTPSBGC(octTPSBGC);
          compRep.setNovTPMBGC(novTPMBGC);
          compRep.setNovTPSBGC(novTPSBGC);
          compRep.setDecTPMBGC(decTPMBGC);
          compRep.setDecTPSBGC(decTPSBGC);
          compRep.setJanTPMBGC(janTPMBGC);
          compRep.setJanTPSBGC(janTPSBGC);
          compRep.setFebTPMBGC(febTPMBGC);
          compRep.setFebTPSBGC(febTPSBGC);
          compRep.setMarTPMBGC(marTPMBGC);
          compRep.setMarTPSBGC(marTPSBGC);
          compRep.setMeetsDoneTPMBGC(meetsDoneTPMBGC);
          compRep.setMeetsDoneTPSBGC(meetsDoneTPSBGC);
          compRep.setNoOFPlumTPMBGC(noOFPlumTPMBGC);
          compRep.setNoOFPlumTPSBGC(noOFPlumTPSBGC);
          compRep.setUsedQtyTPMBGC(usedQtyTPMBGC);
          compRep.setUsedQtyTPSBGC(usedQtyTPSBGC);
          compRep.setLeftQtyTPMBGC(leftQtyTPMBGC);
          compRep.setLeftQtyTPSBGC(leftQtyTPSBGC);
          compRep.setInProcessTPMBGC(inProcessTPMBGC);
          compRep.setInProcessTPSBGC(inProcessTPSBGC);
          
              overallRepLt.add(compRep); 
             
            log.debug("Value of "+key+" is: "+reportHt.get(key));
        }
        model.addAttribute("overallRepLt", overallRepLt);
        return "reports/monthWiseReport"; 

    }
    
    public int checkNumInt(String chkVal)
    {
    	int tempVal=0;
    	try
    	{
    		tempVal = Integer.parseInt(chkVal); 
    	}catch(Exception e) {}

    	return tempVal;
    }
   
    
}
  