package com.ezc.hsil.webapp.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;  

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
import com.ezc.hsil.webapp.dto.TpsRequestDetailDto;
import com.ezc.hsil.webapp.dto.TpsRequestDto;
import com.ezc.hsil.webapp.model.EzPlaceMaster;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcRequestDealers;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.service.IMasterService;
import com.ezc.hsil.webapp.service.ITPSService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TPSController {
	
	 @Autowired
	 ITPSService tpsService;
	 
	 @Autowired
	 IMasterService masterService;
	    
	 
	 @InitBinder
	    public void initBinder(WebDataBinder binder) {
		    binder.setAutoGrowCollectionLimit(2000);
	        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	    }
	@RequestMapping("/tps/add")
	public String add(Model model) {
		TpsRequestDto tpsRequestDto = new TpsRequestDto();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Users userObj = (Users)authentication.getPrincipal();
			model.addAttribute("matList", tpsService.getLastRequestDet(userObj.getUserId()));
		} catch (Exception e) {
			
		}
    	model.addAttribute("distList",masterService.findAll());
    	List<EzPlaceMaster> placeList = masterService.findAllCities();
    	tpsRequestDto.setPlaceList(placeList);
        model.addAttribute("tpsRequestDto",tpsRequestDto);
        return "tps/tpsForm";

    }
	@RequestMapping("/tps/addretrows")
	 public String addRetRows(TpsRequestDto reqDto, Model model) {
       
       int j= reqDto.getNoOfRetailers();
       
		List<EzcRequestDealers> arrTempList = new ArrayList<EzcRequestDealers>();
		
		for(int i=0;i<j;i++)
		{
		arrTempList.add(new EzcRequestDealers());
		}
		reqDto.setDealerName(arrTempList);
	   model.addAttribute("distList",masterService.findAll());
       model.addAttribute("tpsRequestDto",reqDto);
       return "tps/tpsForm";
	}
	  @RequestMapping(value = "/tps/saveRequest", method = RequestMethod.POST)
	    
		  public String save(@ModelAttribute @Valid TpsRequestDto reqDto,BindingResult bindingResult, final RedirectAttributes ra, Model model)
	  {
		  
		  if(bindingResult.hasErrors())
	    	{
			  model.addAttribute("distList",masterService.findAll());
	    		return "tps/tpsForm";
	    	}
	    	else
	    	{
    		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        	Users userObj = (Users)authentication.getPrincipal();
	    	
	    	EzcRequestHeader ezRequestHeader = new EzcRequestHeader();
	    	List<EzcRequestDealers> arrTempList =reqDto.getDealerName();
	    	
	    	int j= reqDto.getNoOfRetailers();
	    	
	    	
	    	ezRequestHeader.setErhCity(reqDto.getCity());
	    	ezRequestHeader.setErhConductedOn(reqDto.getPlannedOn());
	    	ezRequestHeader.setErhCreatedGroup("TPS");
	    	ezRequestHeader.setErhDistrubutor(reqDto.getDistrubutor());
	    	ezRequestHeader.setErhNoOfAttendee(reqDto.getNoOfAttendee());
	    	ezRequestHeader.setErhReqType("TPS");
	    	ezRequestHeader.setErhRequestedOn(new Date());
	    	ezRequestHeader.setErhState(masterService.getDistributorDetails(reqDto.getDistrubutor()).getOrganisation()); 
	    	ezRequestHeader.setErhStatus("NEW"); 
	    	ezRequestHeader.setErhRequestedBy(userObj.getUserId());
	    	ezRequestHeader.setErhDistName(masterService.getDistributorDetails(reqDto.getDistrubutor()).getName());
	    	ezRequestHeader.setErhReqName(userObj.getFirstName()+" "+userObj.getLastName());
	    	Set<EzcRequestDealers> reqDealSet = new HashSet<EzcRequestDealers>(arrTempList);
	    	for(EzcRequestDealers reqDealer : reqDealSet)
	    	{
	    		reqDealer.setEzcRequestHeader(ezRequestHeader);
	    	}
	    	ezRequestHeader.setEzcRequestDealers(reqDealSet);
	    	 
	    	EzcRequestHeader ezReqHeadOut = tpsService.createTPSRequest(ezRequestHeader);
	    	ra.addFlashAttribute("success","TPS request details saved sucessfully with reference : "+"TPS-"+ezReqHeadOut.getId()+"."); 
	        return "redirect:/tps/add";
	    	}
	    }
	   @RequestMapping(value = "/tps/appr-tps-post", method = RequestMethod.POST)
		public @ResponseBody String approveTpsRequest(@RequestParam String id,@RequestParam(value = "comments", required = false) String  comments,@RequestParam(value = "apprMat", required = false) String [] apprMat,@RequestParam(value = "apprQty", required = false) Integer [] apprQty,@RequestParam(value = "leftOverMat", required = false) String [] leftOverMat,@RequestParam(value = "allocQty", required = false) Integer [] allocQty,@RequestParam(value = "leftOverId", required = false) Integer [] leftOverId,@RequestParam(value = "outStore", required = false) String outStore) {
		
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
			if(apprMat!=null)
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
			tpsService.approveTPSRequest(ezcRequestHeader);
			return "ok"; 
		}
	   @RequestMapping(value = "/tps/rej-tps-post", method = RequestMethod.POST)
		public @ResponseBody String rejectTpsRequest(@RequestParam(value = "id", required = false) String id,@RequestParam(value = "rejectComments", required = false) String rejectComments ) {
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
			tpsService.rejectTPSRequest(ezcRequestHeader);
			return "ok";
		}
	    
	   @RequestMapping(value = "/tps/addDetails/{docId}", method = RequestMethod.GET)
	    public String addDetails(@PathVariable String docId, Model model) {
	        EzcRequestHeader ezcRequestHeader = tpsService.getTPSRequest(docId);
	        TpsRequestDetailDto reqDto = new TpsRequestDetailDto();
	        List<EzcRequestItems> ezcRequestItems=new ArrayList<EzcRequestItems>();
	        List<RequestMaterials> ezcMatList=new ArrayList<RequestMaterials>();
	        List<EzcComments> ezcComm=new ArrayList<EzcComments>();
	        List<EzcRequestDealers> ezcRequestDealers=new ArrayList<EzcRequestDealers>();
	     
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
			for(EzcRequestDealers item : ezcRequestHeader.getEzcRequestDealers())
			{
				ezcRequestDealers.add(item); 
			}
	        reqDto.setEzcRequestItems(ezcRequestItems);
	        reqDto.setReqHeader(ezcRequestHeader);
	        reqDto.setEzcRequestDealers(ezcRequestDealers);
	        reqDto.setEzcComments(ezcComm);
	        reqDto.setEzReqMatList(ezcMatList);
	        model.addAttribute("reqDto", reqDto);
	        if("APPROVED".equals(ezcRequestHeader.getErhStatus()))
	        	return "tps/tpsDetailsForm";
	        else
	        	return "tps/detailsEditForm";

	    }   
	   @RequestMapping(value = "/tps/viewDetails/{docId}", method = RequestMethod.GET)
	    public String viewDetails(@PathVariable String docId, Model model) {
	        EzcRequestHeader ezcRequestHeader = tpsService.getTPSRequest(docId);
	        TpsRequestDetailDto reqDto = new TpsRequestDetailDto();
	        List<EzcRequestItems> ezcRequestItems=new ArrayList<EzcRequestItems>();
	        List<RequestMaterials> ezcMatList=new ArrayList<RequestMaterials>();
	        List<EzcRequestDealers> ezcRequestDealers=new ArrayList<EzcRequestDealers>();
	        List<EzcComments> ezcComm=new ArrayList<EzcComments>();
	        ezcComm=tpsService.getTPSCommentRequest(docId);
			for(EzcRequestItems item : ezcRequestHeader.getEzcRequestItems())
			{
				ezcRequestItems.add(item); 
			}
			for(RequestMaterials item : ezcRequestHeader.getRequestMaterials())
			{
				ezcMatList.add(item); 
			}
			for(EzcRequestDealers item : ezcRequestHeader.getEzcRequestDealers())
			{
				ezcRequestDealers.add(item); 
			}
	        reqDto.setEzcRequestItems(ezcRequestItems);
	        reqDto.setReqHeader(ezcRequestHeader);
	        reqDto.setEzcRequestDealers(ezcRequestDealers);
	        reqDto.setEzcComments(ezcComm);
	        reqDto.setEzReqMatList(ezcMatList);
	        model.addAttribute("reqDto", reqDto);
	        
	        return "tps/detailsViewForm";

	    }   


	   @RequestMapping(value = "/tps/back", method = RequestMethod.POST)
	   public String list( Model model) {
		   return "redirect:/tps/tpsRequestList";
		   
	   }
	    @RequestMapping(value = "/tps/tpsRequestList", method = RequestMethod.GET)
	    public String list(ListSelector listSelector , Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {
	    	ArrayList<String> typeList=new ArrayList<String>();
	    	typeList.add("TPS");
	    	//typeList.add("BD");
	    	if(listSelector == null || listSelector.getFromDate() == null)
	    	{
	    		Date todayDate = new Date();
	    		Calendar c = Calendar.getInstance(); 
	    		c.setTime(todayDate); 
	    		c.add(Calendar.MONTH, -3);
	    		listSelector = new ListSelector();	    		
	    		listSelector.setFromDate(c.getTime());
	    		listSelector.setToDate(todayDate); 
	    		//listSelector.setTypeList(typeList);
	    	}
	    	listSelector.setType("TPS");
	    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Users userObj = (Users)authentication.getPrincipal();
			ArrayList<String> userList=new ArrayList<String>();
	    	if(requestWrapper.isUserInRole("ROLE_REQ_CR") || requestWrapper.isUserInRole("ROLE_ST_HEAD"))
	    	{
	    		userList.add(userObj.getUserId());
	    		listSelector.setUser(userList);
	    	}
	    	List<EzcRequestHeader> list = tpsService.getTPSRequestListByDate(listSelector);
	        model.addAttribute("reqList", list);
	        model.addAttribute("listSelector", listSelector);
	        return "tps/tpsList";
 
	    } 
	    @RequestMapping(value = "/tps/tpsReqListSts/{status}", method = RequestMethod.GET)
	    public String listByStatus(Model model,SecurityContextHolderAwareRequestWrapper requestWrapper,@PathVariable String status) {
	    	ArrayList<String> typeList=new ArrayList<String>();
	    	typeList.add("TPS");
	    	
	    	ListSelector listSelector = new ListSelector();
	    	//listSelector.setTypeList(typeList);
	    	listSelector.setType("TPS");
	    	listSelector.setStatus(status);
	    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Users userObj = (Users)authentication.getPrincipal();
			ArrayList<String> userList=new ArrayList<String>();
	    	if(requestWrapper.isUserInRole("ROLE_ST_HEAD"))
	    	{
	    		userList.add(userObj.getUserId());
	    		listSelector.setUser(userList);
	    	}
	    	List<EzcRequestHeader> list = tpsService.getTPSRequestListByDate(listSelector);
	        model.addAttribute("reqList", list);
	        model.addAttribute("listSelector", listSelector);
	        return "tps/tpsList"; 

	    }
 
	   /* @RequestMapping("/tps/addRetailer")
		 public String addRetailer(TpsRequestDetailDto reqDto, @RequestParam(value = "retailer", required = false) String retailer[],@RequestParam(value = "retailerSize", required = false) Integer  retailerSize,Model model) {
	       
	       int j= retailerSize;
	       log.debug(retailerSize+"retailerSize");
			List<EzcRequestDealers> arrTempList = new ArrayList<EzcRequestDealers>();
			EzcRequestDealers myObject=new EzcRequestDealers();
			for(int i=0;i<retailer.length;i++)
			{	
				log.debug(retailer[i]+"retailerSize");
				myObject.setErdDealerName(retailer[i]);
				arrTempList.add(myObject);
			}
		     
			
			arrTempList.add(new EzcRequestDealers());
			List<EzcRequestItems> ezcRequestItems=null;
		   
		 
			reqDto.setReqHeader(reqDto.getReqHeader());
			reqDto.setEzcRequestDealers(arrTempList);
					
			
			model.addAttribute("reqDto", reqDto);  ;
	       return "tps/tpsDetailsForm";
		}*/
	    @RequestMapping("/tps/addRetailer")
		 public String addRetailer(TpsRequestDetailDto reqDto, @RequestParam(value = "retailer", required = false) String retailer[],@RequestParam(value = "retailerSize", required = false) Integer  retailerSize,Model model) {
	       
	    	 List<EzcRequestDealers> ezcRequestDealers=null;
		     ezcRequestDealers = reqDto.getEzcRequestDealers();
		      
		 	for(EzcRequestDealers item :ezcRequestDealers)
			{
				log.debug(item.getErdDealerName());
			} 
	        if(ezcRequestDealers== null)
	        	ezcRequestDealers = new ArrayList<EzcRequestDealers>();
	        else
	        	ezcRequestDealers = reqDto.getEzcRequestDealers();
	        
		    ezcRequestDealers.add(new EzcRequestDealers());
			
		   
		 
			reqDto.setReqHeader(reqDto.getReqHeader());
			reqDto.setEzcRequestDealers(ezcRequestDealers);
					
			
			model.addAttribute("reqDto", reqDto);  ;
	       return "tps/tpsDetailsForm";
		}
	  @RequestMapping(value = "/tps/addNewItem", method = RequestMethod.POST)
	    public String addNewTPSItem(TpsRequestDetailDto reqDto, Model model) {
	        List<EzcRequestItems> ezcRequestItems=null;
	        List<EzcRequestDealers> ezcRequestDealers=null;
	       	ezcRequestDealers = reqDto.getEzcRequestDealers();
	        
	        if(reqDto.getEzcRequestItems() == null)
	        	ezcRequestItems = new ArrayList<EzcRequestItems>();
	        else
	        	ezcRequestItems = reqDto.getEzcRequestItems();
	        ezcRequestItems.add(new EzcRequestItems());
	        reqDto.setEzcRequestItems(ezcRequestItems);
			reqDto.setReqHeader(reqDto.getReqHeader());
			//reqDto.setEzcRequestDealers(ezcRequestDealers);
			
	        model.addAttribute("reqDto", reqDto);  
	        return "tps/tpsDetailsForm";

	    }

	/*
	 * public List<EzcRequestItems> processText(List<EzcRequestItems>
	 * ezcRequestItems,String text) { if(text != null && !"null".equals(text) &&
	 * !"".equals(text)) { text = text.toUpperCase(); String [] requestArr =
	 * text.split("NEXT"); SimpleDateFormat sdf = new
	 * SimpleDateFormat("dd/MM/yyyy"); for(String str :requestArr) { String []
	 * tokens = new String[5]; str = str.replaceAll("\\s+",""); //Pattern p =
	 * Pattern.compile("(\\d+)|([a-zA-Z]+)"); //(\\d+)([a-zA-Z]+)(\\d+) Pattern p =
	 * Pattern.compile(
	 * "([a-zA-Z]+)|([6789][0-9]{9})|([0-9[TH]|[ST]|[ND]|[RD]]{3,4}[a-zA-Z]+[0-9]{4})|([0-9]{1,2}[a-zA-Z]+[0-9]{4})"
	 * ); Matcher m = p.matcher(str); int strCnt=0; while(m.find()) { String token =
	 * m.group(0); //group 0 is always the entire match
	 * tokens[strCnt]=parseDate(token); strCnt++; }
	 * 
	 * EzcRequestItems ezcRequestItemsObj = new EzcRequestItems();
	 * ezcRequestItemsObj.setEriDealer(tokens[0]);
	 * ezcRequestItemsObj.setEriPlumberName(tokens[1]);
	 * ezcRequestItemsObj.setEriContact(tokens[2]); try {
	 * ezcRequestItemsObj.setEriDob(sdf.parse(tokens[3]));
	 * ezcRequestItemsObj.setEriDoa(sdf.parse(tokens[4])); } catch (ParseException
	 * e) {
	 * 
	 * } ezcRequestItems.add(ezcRequestItemsObj); } } return ezcRequestItems; }
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

	  @RequestMapping(value = "/tps/processSpeech", method = RequestMethod.POST)
	    public String processSpeechData(TpsRequestDetailDto reqDto, Model model) {
			
	        List<EzcRequestItems> ezcRequestItems=null; 
	        if(reqDto.getEzcRequestItems() == null)
	        	ezcRequestItems = new ArrayList<EzcRequestItems>();
	        else
	        	ezcRequestItems = reqDto.getEzcRequestItems();
	        
	        log.info("reqDto.getRecordedText()::::::"+reqDto.getRecordedText());
	        reqDto.setEzcRequestItems(processText(ezcRequestItems,reqDto.getRecordedText()));
			reqDto.setReqHeader(reqDto.getReqHeader());
			reqDto.setEzReqMatList(reqDto.getEzReqMatList());
	        model.addAttribute("reqDto", reqDto); 
	        return "tps/tpsDetailsForm";

	    }
	  @RequestMapping(value = "/tps/saveDetails", method = RequestMethod.POST)
	    public String saveDetails(TpsRequestDetailDto tpsRequestDetailDto,final RedirectAttributes ra) {
			
	    	tpsService.createTPSDetails(tpsRequestDetailDto);
	        ra.addFlashAttribute("successFlash", "Success");
	        return "redirect:/tps/tpsRequestList";

	    }
	  @RequestMapping(value = "/tps/nullify-qty", method = RequestMethod.POST)
		public @ResponseBody String NullifyTpsQty(@RequestParam String leftOverId,@RequestParam(value = "reasonNullify", required = false) String  reasonNullify,@RequestParam(value = "commentsNullify", required = false) String  commentsNullify) {
	    	tpsService.NullifyTpsQty(leftOverId,reasonNullify,commentsNullify);
	    	return "ok";
		}   
        
}
 