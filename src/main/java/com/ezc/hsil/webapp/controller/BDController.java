package com.ezc.hsil.webapp.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezc.hsil.webapp.dto.BDRequestDetailDto;
import com.ezc.hsil.webapp.dto.BDRequestDto;
import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.persistance.dao.RequestMaterialsRepo;
import com.ezc.hsil.webapp.service.IBDService;
import com.ezc.hsil.webapp.service.IMasterService;
import com.ezc.hsil.webapp.service.IUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BDController {
	
	@Autowired
    private IUserService iUserService;
	
	 @Autowired
	 IMasterService masterService;
	 
	 @Autowired
	 IBDService bdService;
	 
	 @Autowired
	 private RequestMaterialsRepo reqMatRep;
	 
	 @InitBinder
	    public void initBinder(WebDataBinder binder) {
	        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	    }
	    
	
	 @RequestMapping("/bd/add")
	 public String add(Model model) {
		 BDRequestDto bdReqDto = new BDRequestDto();
		 String loggedUser="";
			try {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				Users userObj = (Users)authentication.getPrincipal();
				loggedUser=(String)userObj.getUserId();
				
			} catch (Exception e) {
				
			} 
			
		 	model.addAttribute("matList", bdService.getBDLeftOverStock());
	        model.addAttribute("bdReqDto",bdReqDto);
	        return "bd/bdForm";

	    }
	/* @RequestMapping(value = "/bd/saveRequest", method = RequestMethod.POST)
	 public String save(@Valid @ModelAttribute("bdReqDto") BDRequestDto bdReqDto,BindingResult bindingResult,@RequestParam(value = "leftOverMat", required = false) String [] leftOverMat,@RequestParam(value = "allocQty", required = false) Integer [] allocQty,@RequestParam(value = "leftOverId", required = false) Integer [] leftOverId,final RedirectAttributes ra)
	 {
		 log.debug("in controller");
		 if(bindingResult.hasErrors())
	    	{
			 log.debug("in controller has err");
	    		return "bd/bdForm";
	    	}
	    	else 
	    	{
	    		log.debug("in controller no err"+bdReqDto.getBdMatCode());	
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				Users userObj = (Users)authentication.getPrincipal();
   	
			   	EzcRequestHeader ezRequestHeader = new EzcRequestHeader();
			   	
			   	
			   	ezRequestHeader.setErhCreatedGroup("BD");
			   	
			   	ezRequestHeader.setErhNoOfAttendee(bdReqDto.getBdQty());
			   	ezRequestHeader.setErhReqType("BD");
			   	ezRequestHeader.setErhRequestedOn(new Date());
			   	ezRequestHeader.setErhState("TEST"); 
			   	ezRequestHeader.setErhStatus("NEW"); 
			   	ezRequestHeader.setErhRequestedBy(userObj.getUserId());
			   	
			   	Set<RequestMaterials> reqMatSet = new HashSet<RequestMaterials>();
				
				RequestMaterials reqMat = new RequestMaterials();
				
				reqMat.setMatCode(bdReqDto.getBdMatCode().toString().split("#")[0]);
				reqMat.setMatDesc(bdReqDto.getBdMatCode().toString().split("#")[1]);
				reqMat.setApprQty(bdReqDto.getBdQty());
				reqMat.setIsNew('Y');
				
				reqMat.setEzcRequestHeader(ezRequestHeader);
				reqMatSet.add(reqMat);
				 if(leftOverId != null && allocQty != null )
				 {	 
				log.debug("in controller no err"+leftOverId.length+allocQty.length);	
				 if(leftOverId != null && leftOverId.length > 0 && allocQty != null && allocQty.length > 0)
					{
						for(int i=0;i<leftOverId.length;i++)
						{
							if(allocQty.length>0 )
							{
								if(allocQty[i] != null && allocQty[i] > 0)
								{	
								RequestMaterials reqMat1 = new RequestMaterials();
								reqMat1.setMatCode(leftOverMat[i].split("#")[0]);
								reqMat1.setMatDesc(leftOverMat[i].split("#")[1]);
								reqMat1.setApprQty(allocQty[i]);
								reqMat1.setIsNew('N');
								reqMat1.setAllocId(leftOverId[i]);
								reqMatSet.add(reqMat1);
								}
							}
						}
						
					}
				 }
				ezRequestHeader.setRequestMaterials(reqMatSet);
   	
				bdService.createBDRequest(ezRequestHeader);
				ra.addFlashAttribute("success","BD request details saved sucessfully.");
				return "redirect:/bd/add";
	    	}
   }*/
	 @RequestMapping(value = "/bd/saveRequest", method = RequestMethod.POST)
	 public String save(@Valid @ModelAttribute("bdReqDto") BDRequestDto bdReqDto,BindingResult bindingResult,@RequestParam(value = "leftOverMat", required = false) String [] leftOverMat,@RequestParam(value = "allocQty", required = false) Integer [] allocQty,@RequestParam(value = "leftOverId", required = false) Integer [] leftOverId,final RedirectAttributes ra)
	 {
		 log.debug("in controller");
		 if(bindingResult.hasErrors())
	    	{
			 log.debug("in controller has err");
	    		return "bd/bdForm";
	    	}
	    	else 
	    	{
	    		log.debug("in controller no err"+bdReqDto.getBdMatCode());	
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				Users userObj = (Users)authentication.getPrincipal();
 	
			   	EzcRequestHeader ezRequestHeader = new EzcRequestHeader();
			   	
			   	
			   	ezRequestHeader.setErhCreatedGroup("BD");
			   	
			   	ezRequestHeader.setErhNoOfAttendee(bdReqDto.getBdQty());
			   	ezRequestHeader.setErhReqType("BD");
			   	ezRequestHeader.setErhRequestedOn(new Date());
			   	ezRequestHeader.setErhState("TEST"); 
			   	ezRequestHeader.setErhStatus("NEW"); 
			   	ezRequestHeader.setErhRequestedBy(userObj.getUserId());
			   	
			   	Set<RequestMaterials> reqMatSet = new HashSet<RequestMaterials>();
				
				RequestMaterials reqMat = new RequestMaterials();
				
				reqMat.setMatCode(bdReqDto.getBdMatCode().toString().split("#")[0]);
				reqMat.setMatDesc(bdReqDto.getBdMatCode().toString().split("#")[1]);
				reqMat.setApprQty(bdReqDto.getBdQty());
				reqMat.setIsNew('Y');
				
				reqMat.setEzcRequestHeader(ezRequestHeader);
				reqMatSet.add(reqMat);
				 if(leftOverId != null && allocQty != null )
				 {	 
				log.debug("in controller no err"+leftOverId.length+allocQty.length);	
				 if(leftOverId != null && leftOverId.length > 0 && allocQty != null && allocQty.length > 0)
					{
						for(int i=0;i<leftOverId.length;i++)
						{
							if(allocQty.length>0 )
							{
								if(allocQty[i] != null && allocQty[i] > 0)
								{	
								RequestMaterials reqMat1 = new RequestMaterials();
								reqMat1.setMatCode(leftOverMat[i].split("#")[0]);
								reqMat1.setMatDesc(leftOverMat[i].split("#")[1]);
								reqMat1.setApprQty(allocQty[i]);
								reqMat1.setIsNew('N');
								reqMat1.setAllocId(leftOverId[i]);
								reqMatSet.add(reqMat1);
								}
							}
						}
						
					}
				 }
				ezRequestHeader.setRequestMaterials(reqMatSet);
 	
				bdService.createBDRequest(ezRequestHeader);
				ra.addFlashAttribute("success","BD request details saved sucessfully.");
				return "redirect:/bd/add";
	    	}
 }
	
	    @RequestMapping(value = "/bd/bdRequestList", method = RequestMethod.GET)
	    public String list(ListSelector listSelector , Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {
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
	    	listSelector.setType("BD");
	    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Users userObj = (Users)authentication.getPrincipal();
			ArrayList<String> userList=new ArrayList<String>();
	    	if(requestWrapper.isUserInRole("ROLE_BD_MKT"))
	    	{
	    		userList.add(userObj.getUserId());
	    		listSelector.setUser(userList);
	    	}
	    	List<EzcRequestHeader> list = bdService.getBDRequestListByDate(listSelector);
	        model.addAttribute("reqList", list);
	        model.addAttribute("listSelector", listSelector);
	        if(requestWrapper.isUserInRole("ROLE_ST_HEAD"))
	    	{
	    		
	        	return "tps/tpsRequestList"; 
	    	}
	    	else 
	    	{
	    		return "bd/bdlist"; 
	    	}
	       
	        

	    }
	    
	    @RequestMapping(value = "/bd/bdRequestList/{status}", method = RequestMethod.GET)
	    public String listByStatus(Model model,SecurityContextHolderAwareRequestWrapper requestWrapper,@PathVariable String status) {
	    	ListSelector listSelector = new ListSelector();
	    	listSelector.setType("BD");
	    	listSelector.setStatus(status);
	    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Users userObj = (Users)authentication.getPrincipal();
			ArrayList<String> userList=new ArrayList<String>();
	    	if(requestWrapper.isUserInRole("ROLE_BD_MKT"))
	    	{
	    		userList.add(userObj.getUserId());
	    		listSelector.setUser(userList);
	    	}
	    	List<EzcRequestHeader> list = bdService.getBDRequestListByDate(listSelector);
	        model.addAttribute("reqList", list);
	        model.addAttribute("listSelector", listSelector);
	        return "bd/bdlist"; 

	    }
	    @RequestMapping(value = "/bd/apprbd", method = RequestMethod.POST)
		public  String approveBDRequest(@RequestParam String id,@RequestParam(value = "bdQty", required = false) Integer  bdQty,@RequestParam(value = "commentReqDto", required = false) String  comments,@RequestParam(value = "leftOverMat", required = false) String [] leftOverMat,@RequestParam(value = "allocQty", required = false) Integer [] allocQty,@RequestParam(value = "leftOverId", required = false) Integer [] leftOverId,@RequestParam(value = "outStore", required = false) String outStore, final RedirectAttributes ra) {
	    	EzcRequestHeader ezcRequestHeader = new EzcRequestHeader(); 
			ezcRequestHeader.setId(id);
			String loggedUser="";
			           
           try {
                  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                  Users userObj = (Users)authentication.getPrincipal();
                  loggedUser=(String)userObj.getUserId();
                  
           } catch (Exception e) {
                  
           }
			Set<RequestMaterials> reqMatSet = new HashSet<RequestMaterials>();
			Set<EzcComments> commSet = new HashSet<EzcComments>();
			log.debug("comments"+comments);
			if(comments!= null)
			{
				EzcComments comm=new EzcComments();
				comm.setComments(comments);
				comm.setType("APPROVE");
				comm.setCreatedBy(loggedUser);
				comm.setLastModifiedBy(loggedUser);
				commSet.add(comm);
			
			}
	    	Integer appQty=bdQty;
	    	log.debug("appQty"+appQty);
	    	if(leftOverId != null && leftOverId.length > 0 && allocQty != null && allocQty.length > 0)
			{
				for(int i=0;i<leftOverId.length;i++)
				{
					if(allocQty[i] != null && allocQty[i] > 0)
					{
						RequestMaterials reqMat = new RequestMaterials();
						reqMat.setMatCode(leftOverMat[i].split("#")[0]);
						reqMat.setMatDesc(leftOverMat[i].split("#")[1]);
						reqMat.setApprQty(allocQty[i]);
						reqMat.setIsNew('N');
						reqMat.setAllocId(leftOverId[i]);
						reqMatSet.add(reqMat);
					}
				}
				
			}
	    	ezcRequestHeader.setRequestMaterials(reqMatSet);
	    	ezcRequestHeader.setEzcComments(commSet);
	    	ezcRequestHeader.setErhOutStore(outStore);
	    	
	    	bdService.submitBDDet(id,appQty,ezcRequestHeader); 
	        ra.addFlashAttribute("successFlash", "Success");
	        return "redirect:/bd/bdRequestList/NEW";
	        
	        
	        //return "bd/bdlist"; 
	        
	        
	    }
	/*
	 * @RequestMapping(value = "/bd/bdRequestList", method = RequestMethod.GET)
	 * public String list(ListSelector listSelector , Model model) { if(listSelector
	 * == null || listSelector.getFromDate() == null) { Date todayDate = new Date();
	 * Calendar c = Calendar.getInstance(); c.setTime(todayDate);
	 * c.add(Calendar.MONTH, -3); listSelector = new ListSelector();
	 * listSelector.setStatus("ALL"); listSelector.setType("BD");
	 * listSelector.setFromDate(c.getTime()); listSelector.setToDate(todayDate); }
	 * 
	 * List<EzcRequestHeader> list = bdService.getBDRequestListByDate(listSelector);
	 * 
	 * 
	 * model.addAttribute("reqList",list); model.addAttribute("listSelector",
	 * listSelector); return "bd/bdlist";
	 * 
	 * }
	 */ 
	  @RequestMapping(value = "/bd/addDetails/{docId}", method = RequestMethod.GET)
	    public String addDetails(@PathVariable String docId, Model model) {
	        EzcRequestHeader ezcRequestHeader = bdService.getBDRequest(docId);
	        String loggedUser="";
			try {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				Users userObj = (Users)authentication.getPrincipal();
				loggedUser=(String)userObj.getUserId();
				
			} catch (Exception e) {
				
			} 
			List<Users> outStoreList = iUserService.findUsersByRole("ROLE_OUT_STOR");
	        List<RequestMaterials> ezcMatList=new ArrayList<RequestMaterials>();
	        List<EzcRequestItems> ezcRequestItems=new ArrayList<EzcRequestItems>();
	        List<EzcComments> ezcComm=new ArrayList<EzcComments>();
	        ezcComm=bdService.getBDCommentRequest(docId);
	       
	        for(EzcRequestItems item : ezcRequestHeader.getEzcRequestItems())
			{
				ezcRequestItems.add(item); 
			}
	        for(RequestMaterials item : ezcRequestHeader.getRequestMaterials())
			{
				ezcMatList.add(item); 
			}
	        BDRequestDetailDto reqDto = new BDRequestDetailDto();
	        reqDto.setReqHeader(ezcRequestHeader);
	        reqDto.setEzReqMatList(ezcMatList);
	        reqDto.setEzcComments(ezcComm);
	        model.addAttribute("reqDto", reqDto);
	        model.addAttribute("matList", bdService.getBDLeftOverStock());
	        model.addAttribute("outStoreList", outStoreList);
	       	return "bd/bdDetailsForm";
	       

	    }
	  @RequestMapping(value = "/bd/viewDetails/{docId}", method = RequestMethod.GET)
	    public String viewDetails(@PathVariable String docId, Model model) {
	        EzcRequestHeader ezcRequestHeader = bdService.getBDRequest(docId);
	       
	        List<RequestMaterials> ezcMatList=new ArrayList<RequestMaterials>();
	        List<EzcRequestItems> ezcRequestItems=new ArrayList<EzcRequestItems>();
	        List<EzcComments> ezcComm=new ArrayList<EzcComments>();
	        ezcComm=bdService.getBDCommentRequest(docId);
	       
	        for(EzcRequestItems item : ezcRequestHeader.getEzcRequestItems())
			{
				ezcRequestItems.add(item); 
			}
	        for(RequestMaterials item : ezcRequestHeader.getRequestMaterials())
			{
				ezcMatList.add(item); 
			}
	        BDRequestDetailDto reqDto = new BDRequestDetailDto();
	        reqDto.setReqHeader(ezcRequestHeader);
	        reqDto.setEzcRequestItems(ezcRequestItems); 
	        reqDto.setEzReqMatList(ezcMatList);
	        reqDto.setEzcComments(ezcComm);
	        model.addAttribute("reqDto", reqDto);
	        
	        
	       	return "bd/bdviewForm";
	       

	    } 
	  @RequestMapping(value = "/bd/addNewItem/{purpose}", method = RequestMethod.POST)
	    public String addNewTPMItem(@PathVariable String purpose,BDRequestDetailDto reqDto, Model model) {
	        List<EzcRequestItems> ezcRequestItems=null;
	        String disabledStr="true";
	        log.debug(purpose);
	        if("Event".equals(purpose.trim()))disabledStr="false";
	        
	        log.debug(disabledStr);
	        if(reqDto.getEzcRequestItems() == null)
	        	ezcRequestItems = new ArrayList<EzcRequestItems>();
	        else
	        	ezcRequestItems = reqDto.getEzcRequestItems();
	        ezcRequestItems.add(new EzcRequestItems());
	        reqDto.setEzcRequestItems(ezcRequestItems);
			reqDto.setReqHeader(reqDto.getReqHeader());
			model.addAttribute("disabledStr", disabledStr);
	        model.addAttribute("reqDto", reqDto); 
	        return "bd/bdDetailsForm";

	    }
	  @RequestMapping(value = "/bd/saveDetails", method = RequestMethod.POST)
	    public String saveDetails(BDRequestDetailDto bdRequestDetailDto, final RedirectAttributes ra) {
	    	bdService.createBDDetails(bdRequestDetailDto);
	        ra.addFlashAttribute("successFlash", "Success");
	        return "redirect:/bd/bdRequestList";

	    }
	  @RequestMapping(value = "/bd/nullify-qty", method = RequestMethod.POST)
		public @ResponseBody String NullifyBDQty(@RequestParam String leftOverId,@RequestParam(value = "reasonNullify", required = false) String  reasonNullify,@RequestParam(value = "commentsNullify", required = false) String  commentsNullify) {
		  bdService.NullifyBDQty(leftOverId,reasonNullify,commentsNullify);
	    	return "ok";
		}  
	  @RequestMapping(value = "/bd/rej-bd-post", method = RequestMethod.POST)
		public @ResponseBody String rejectBDRequest(@RequestParam(value = "id", required = false) String id,@RequestParam(value = "rejectComments", required = false) String rejectComments ) {
			log.debug("rejectComments"+rejectComments);
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
			if(rejectComments != null)
			{
				
					EzcComments reqCom = new EzcComments();
					reqCom.setComments(rejectComments);
					reqCom.setCreatedBy(loggedUser);
					reqCom.setLastModifiedBy(loggedUser);
					reqCom.setType("REJECT");
					
					comments.add(reqCom);
				
			}
				ezcRequestHeader.setEzcComments(comments);
			bdService.rejectBDRequest(ezcRequestHeader);
			return "ok";
		}
}
