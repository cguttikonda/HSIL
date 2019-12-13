package com.ezc.hsil.webapp.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.TpmRequestDetailDto;
import com.ezc.hsil.webapp.dto.TpmRequestDto;
import com.ezc.hsil.webapp.model.DistributorMaster;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.model.Roles;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.service.IMasterService;
import com.ezc.hsil.webapp.service.ITPMService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TPMController {

    @Autowired
    ITPMService tpmService;
    
    @Autowired
    IMasterService masterService;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @RequestMapping("/tpm/add")
    public String add(Model model) {
    	try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Users userObj = (Users)authentication.getPrincipal();
			model.addAttribute("matList", tpmService.getLeftOverStock(userObj.getUserId()));
		} catch (Exception e) {
			
		}
    	TpmRequestDto tpmRequestDto = new TpmRequestDto();
    	List<DistributorMaster> distList = masterService.findAll();
    	tpmRequestDto.setDistList(distList); 
        model.addAttribute("tpmRequestDto", tpmRequestDto);
        return "tpm/form";

    } 
  
    @RequestMapping(value = "/tpm/appr-tpm-post", method = RequestMethod.POST)
	public @ResponseBody String approveTpmRequest(@RequestParam String id,@RequestParam(value = "comments", required = false) String  comments,@RequestParam(value = "apprMat", required = false) String [] apprMat,@RequestParam(value = "apprQty", required = false) Integer [] apprQty,@RequestParam(value = "leftOverMat", required = false) String [] leftOverMat,@RequestParam(value = "allocQty", required = false) Integer [] allocQty,@RequestParam(value = "leftOverId", required = false) Integer [] leftOverId) {
		EzcRequestHeader ezcRequestHeader = new EzcRequestHeader(); 
		ezcRequestHeader.setId(id);
		Set<RequestMaterials> reqMatSet = new HashSet<RequestMaterials>();
		Set<EzcComments> commSet = new HashSet<EzcComments>();
		if(comments!= null)
		{
			EzcComments comm=new EzcComments();
			comm.setComments(comments);
			comm.setType("APPROVE");
			commSet.add(comm);
		
		}
		if(apprMat != null)
		{
			for(int i=0;i<apprMat.length;i++)
			{
				RequestMaterials reqMat = new RequestMaterials();
				reqMat.setMatCode(apprMat[i].split("#")[0]);
				reqMat.setMatDesc(apprMat[i].split("#")[1]);
				reqMat.setApprQty(apprQty[i]);
				reqMat.setIsNew('Y');
				reqMatSet.add(reqMat);
			}
		}
		if(leftOverId != null && leftOverId.length > 0)
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
		tpmService.approveTPMRequest(ezcRequestHeader);
		return "ok";
	} 
     
    @RequestMapping(value = "/tpm/rej-tpm-post", method = RequestMethod.POST)
	public @ResponseBody String rejectTpmRequest(@RequestParam(value = "id", required = false) String id,@RequestParam(value = "rejectComments", required = false) String rejectComments ) {
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
		tpmService.rejectTPMRequest(ezcRequestHeader);
		return "ok";
	}
    
	
    @RequestMapping(value = "/tpm/close-tpm-post", method = RequestMethod.POST)
    public String closeTpmRequest(TpmRequestDetailDto tpmRequestDetailDto, final RedirectAttributes ra) {
		  tpmService.closeTPMRequest(tpmRequestDetailDto); 
		  ra.addFlashAttribute("success","TPM request details closed sucessfully."); 
		  return "redirect:/tpm/tpmRequestList";

    }


    @RequestMapping(value = "/tpm/tpmRequestList", method = RequestMethod.GET)
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
    	listSelector.setType("TPM");
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		ArrayList<String> userList=new ArrayList<String>();
    	if(requestWrapper.isUserInRole("ROLE_REQ_CR"))
    	{
    		userList.add(userObj.getUserId());
    		listSelector.setUser(userList);
    	}
    	List<EzcRequestHeader> list = tpmService.getTPMRequestListByDate(listSelector);
        model.addAttribute("reqList", list);
        model.addAttribute("listSelector", listSelector);
        return "tpm/list"; 

    }
    
    @RequestMapping(value = "/tpm/tpmReqListSts/{status}", method = RequestMethod.GET)
    public String listByStatus(Model model,SecurityContextHolderAwareRequestWrapper requestWrapper,@PathVariable String status) {
    	ListSelector listSelector = new ListSelector();
    	listSelector.setType("TPM");
    	listSelector.setStatus(status);
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		ArrayList<String> userList=new ArrayList<String>();
    	if(requestWrapper.isUserInRole("ROLE_REQ_CR"))
    	{
    		userList.add(userObj.getUserId());
    		listSelector.setUser(userList);
    	}
    	List<EzcRequestHeader> list = tpmService.getTPMRequestListByDate(listSelector);
        model.addAttribute("reqList", list);
        model.addAttribute("listSelector", listSelector);
        return "tpm/list"; 

    }
    
    
    @RequestMapping(value = "/tpm/saveRequest", method = RequestMethod.POST)
    public String save(@ModelAttribute @Valid TpmRequestDto tpmRequestDto,BindingResult bindingResult, final RedirectAttributes ra) {
    	if(bindingResult.hasErrors())
    	{
    		return "tpm/form";
    	}
    	else 
    	{
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	Users userObj = (Users)authentication.getPrincipal();
    	EzcRequestHeader ezRequestHeader = new EzcRequestHeader();
    	ezRequestHeader.setErhCity(tpmRequestDto.getErhCity());
    	ezRequestHeader.setErhConductedOn(tpmRequestDto.getErhConductedOn());
    	ezRequestHeader.setErhCreatedGroup("TPM");
    	ezRequestHeader.setErhDistrubutor(tpmRequestDto.getErhDistrubutor());
    	ezRequestHeader.setErhNoOfAttendee(tpmRequestDto.getErhNoOfAttendee());
    	ezRequestHeader.setErhReqType("TPM");
    	ezRequestHeader.setErhRequestedOn(new Date());
    	ezRequestHeader.setErhState("TEST"); 
    	ezRequestHeader.setErhStatus("NEW"); 
    	ezRequestHeader.setErhRequestedBy(userObj.getUserId());
  
    	tpmService.createTPMRequest(ezRequestHeader);
        ra.addFlashAttribute("success","TPM request details saved sucessfully.");
        return "redirect:/tpm/add";
    	}
    }
    
    @RequestMapping(value = "/tpm/addDetails/{docId}", method = RequestMethod.GET)
    public String addDetails(@PathVariable String docId, Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {
    	
    	
        EzcRequestHeader ezcRequestHeader = tpmService.getTPMRequest(docId);
        TpmRequestDetailDto reqDto = new TpmRequestDetailDto();
        List<EzcRequestItems> ezcRequestItems=new ArrayList<EzcRequestItems>();
        List<RequestMaterials> ezcMatList=new ArrayList<RequestMaterials>();
        List<EzcComments> ezcComm=new ArrayList<EzcComments>();
        for(EzcComments item : ezcRequestHeader.getEzcComments())
		{
        	ezcComm.add(item); 
		}
		for(EzcRequestItems item : ezcRequestHeader.getEzcRequestItems())
		{
			ezcRequestItems.add(item); 
		}
		for(RequestMaterials item : ezcRequestHeader.getRequestMaterials())
		{
			ezcMatList.add(item); 
		}
		
        reqDto.setEzcRequestItems(ezcRequestItems);
        reqDto.setReqHeader(ezcRequestHeader);
        reqDto.setEzcRequestDealers(ezcRequestHeader.getEzcRequestDealers());
        reqDto.setEzcComments(ezcComm);
        reqDto.setEzReqMatList(ezcMatList);
        model.addAttribute("reqDto", reqDto);
        
        if("APPROVED".equals(ezcRequestHeader.getErhStatus()) && requestWrapper.isUserInRole("ROLE_REQ_CR"))
        	return "tpm/detailsForm";
        else
        	return "tpm/detailsEditForm";

    } 
    
    @RequestMapping(value = "/tpm/processSpeech", method = RequestMethod.POST)
    public String processSpeechData(TpmRequestDetailDto reqDto, Model model) {
		/*
		 * EzcRequestHeader ezcRequestHeader = tpmService.getTPMRequest(docId);
		 * TpmRequestDetailDto reqDto = new TpmRequestDetailDto();
		 */
        List<EzcRequestItems> ezcRequestItems=null;
        if(reqDto.getEzcRequestItems() == null)
        	ezcRequestItems = new ArrayList<EzcRequestItems>();
        else
        	ezcRequestItems = reqDto.getEzcRequestItems();
        
        log.info("reqDto.getRecordedText()::::::",reqDto.getRecordedText());
        reqDto.setEzcRequestItems(processText(ezcRequestItems,reqDto.getRecordedText()));
		reqDto.setReqHeader(reqDto.getReqHeader());
		reqDto.setEzReqMatList(reqDto.getEzReqMatList());
        model.addAttribute("reqDto", reqDto); 
        return "tpm/detailsForm";

    }
    
    @RequestMapping(value = "/tpm/addNewItem", method = RequestMethod.POST)
    public String addNewTPMItem(TpmRequestDetailDto reqDto, Model model) {
        List<EzcRequestItems> ezcRequestItems=null;
        List<EzcComments> ezcComm=null;
        if(reqDto.getEzcRequestItems() == null)
        	ezcRequestItems = new ArrayList<EzcRequestItems>();
        else
        	ezcRequestItems = reqDto.getEzcRequestItems();
        
        if(reqDto.getEzcComments() == null)
        	ezcComm = new ArrayList<EzcComments>();
        else
        	ezcComm = reqDto.getEzcComments();
        
        ezcRequestItems.add(new EzcRequestItems());
        
        reqDto.setEzcRequestItems(ezcRequestItems);
		reqDto.setReqHeader(reqDto.getReqHeader());
		reqDto.setEzcComments(ezcComm);
		
		
        model.addAttribute("reqDto", reqDto); 
        return "tpm/detailsForm";

    }

    @RequestMapping(value = "/tpm/saveDetails", method = RequestMethod.POST)
    public String saveDetails(TpmRequestDetailDto tpmRequestDetailDto, final RedirectAttributes ra) {
    	tpmService.createTPMDetails(tpmRequestDetailDto);
        ra.addFlashAttribute("successFlash", "Success");
        return "redirect:/tpm/tpmRequestList";

    }
    /*
    public List<EzcRequestItems> processText(List<EzcRequestItems> ezcRequestItems,String text)
    {
    	if(text != null && !"null".equals(text) && !"".equals(text))
    	{
	    	text = text.toUpperCase();
	    	String [] requestArr = text.split("NEXT");
	    	for(String string :requestArr)
	    	{
	    		//System.out.print("::"""+retTxt(string));
	    		String name="",mobile="";
				Date dob=null,doa=null;
	    		int i=0;
	    		int cnt = 0;
	    	    boolean nameFound = false;
	            boolean mobileFound = false;
	    	    while (i < string.length())
	            {
	                //System.out.print(":::"+string.charAt(i)+"::"+Character.isDigit(string.charAt(i)));
	                if(!nameFound)
	                {
	                    if(!Character.isDigit(string.charAt(i)))
	                    {
	                        name += string.charAt(i);
	                    }
	                    else
	                    {
	                        nameFound = true;
	                        i--;
	                    }
	                    //System.out.print("nameFound:::"+mobileFound);
	                }
	                else if(!mobileFound)
	                {
	                    
	                    //System.out.print(cnt);    
	                    if(Character.isDigit(string.charAt(i)))
	                    {
	                        cnt++;
	                        mobile+=string.charAt(i);
	                        if(cnt == 10)
	                        {
	                            mobileFound = true;
	                        }
	                    }
	                }
	                else if(nameFound && mobileFound)
	                {
	                    break;
	                }
	                
	                i++; 
	            }
				  int dateCnt =0;
				  SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); 
				  String mobStr = string.substring(i,string.length()); 
				  mobStr = mobStr.trim();
				  String [] mobStrArr = mobStr.split(" ");
				  if(mobStrArr != null && mobStrArr.length > 0) {
					  try {
						dob= sdf.parse(mobStrArr[dateCnt++].replace("TH","")+"-"+mobStrArr[dateCnt++].substring(0,3)+"-"+mobStrArr[dateCnt++]);
					} catch (ParseException e) {
						log.info("parse exception dob::"+e);
					}
				  catch (NullPointerException e) {
					}
					  log.info("dob::"+dob);
					  try {
							doa= sdf.parse(mobStrArr[dateCnt++].replace("TH","")+"-"+mobStrArr[dateCnt++].substring(0,3)+"-"+mobStrArr[dateCnt++]);
						} catch (ParseException e) {
							log.info("parse exception doa::"+e);
						}
					  catch (NullPointerException e) {
						}
					  log.info("doa::"+doa);
				  }
				 
	            
	    		EzcRequestItems ezcRequestItemsObj = new EzcRequestItems();
	    		ezcRequestItemsObj.setEriPlumberName(name);
	    		ezcRequestItemsObj.setEriContact(mobile);
	    		ezcRequestItemsObj.setEriDob(dob);
	    		ezcRequestItemsObj.setEriDoa(doa);
	    		ezcRequestItems.add(ezcRequestItemsObj);
	    	}
    	}	
    	return ezcRequestItems;
    }
    */
    public List<EzcRequestItems> processText(List<EzcRequestItems> ezcRequestItems,String text)
    {
    	try {
				if(text != null && !"null".equals(text) && !"".equals(text))
				{
					text = text.toUpperCase();
					String [] requestArr = text.split("NEXT");
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					for(String str :requestArr)
					{
				          String [] tokens = new String[4];
				          str = str.replaceAll("\\s+","");
				          Pattern p = Pattern.compile("([a-zA-Z]+)|([6789][0-9]{9})|([0-9[TH]|[ST]|[ND]|[RD]]{3,4}[a-zA-Z]+[0-9]{4})|([0-9]{1,2}[a-zA-Z]+[0-9]{4})");
				          Matcher m = p.matcher(str);
				          int strCnt=0;
				          while(m.find())
				          {
				            String token = m.group(0); //group 0 is always the entire match   
				            tokens[strCnt]=parseDate(token);
				            strCnt++;
				          }
				          
						EzcRequestItems ezcRequestItemsObj = new EzcRequestItems();
						ezcRequestItemsObj.setEriPlumberName(tokens[0]);
						ezcRequestItemsObj.setEriContact(tokens[1]);
						try {
							ezcRequestItemsObj.setEriDob(sdf.parse(tokens[2]));
							ezcRequestItemsObj.setEriDoa(sdf.parse(tokens[3]));
						} catch (ParseException e) {
							
						}
						ezcRequestItems.add(ezcRequestItemsObj);
					}
				}
		} catch (Exception e) {
		}	
    	return ezcRequestItems;
    }
    public static String parseDate(String str){
        String parsedDate="";
  Date d = null;
  String[] formats = {"d'ST'MMMMyyyy","d'ND'MMMMyyyy","d'RD'MMMMyyyy","d'TH'MMMMyyyy","ddMMMMyyyy"};
  for (String format : formats) {
      try {
          d = new SimpleDateFormat(format).parse(str);
          if (d != null) {
              SimpleDateFormat date_formatter = new SimpleDateFormat("dd/MM/yyyy");
              parsedDate = date_formatter.format(d);
          }
          return parsedDate;
      }
      catch (Exception e){
      parsedDate = str;
      }       
  }
  return parsedDate;
}

}
