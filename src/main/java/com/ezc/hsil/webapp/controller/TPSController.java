package com.ezc.hsil.webapp.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.TpmRequestDetailDto;
import com.ezc.hsil.webapp.dto.TpsRequestDetailDto;
import com.ezc.hsil.webapp.dto.TpsRequestDto;
import com.ezc.hsil.webapp.model.EzcRequestDealers;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.service.ITPSService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TPSController {
	 @Autowired
	 ITPSService tpsService;
	
	@RequestMapping("/tps/add")
	public String add(Model model) {
		TpsRequestDto tpsRequestDto = new TpsRequestDto();
		
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
       model.addAttribute("tpsRequestDto",reqDto);
       return "tps/tpsForm";
	}
	  @RequestMapping(value = "/tps/saveRequest", method = RequestMethod.POST)
	    public String save(TpsRequestDto reqDto, final RedirectAttributes ra) {
	    	EzcRequestHeader ezRequestHeader = new EzcRequestHeader();
	    	List<EzcRequestDealers> arrTempList =reqDto.getDealerName();
	    	
	    	int j= reqDto.getNoOfRetailers();
	    	
	    	
	    	ezRequestHeader.setErhCity(reqDto.getCity());
	    	ezRequestHeader.setErhConductedOn(reqDto.getPlannedOn());
	    	ezRequestHeader.setErhCreatedGroup("TPS");
	    	ezRequestHeader.setErhDistrubutor(reqDto.getDistrubutor());
	    	ezRequestHeader.setErhNoOfAttendee(reqDto.getNoOfRetailers());
	    	ezRequestHeader.setErhReqType("TYP");
	    	ezRequestHeader.setErhRequestedOn(new Date());
	    	ezRequestHeader.setErhState("TEST"); 
	    	ezRequestHeader.setErhStatus("NEW"); 
	    	
	    	Set<EzcRequestDealers> reqDealSet = new HashSet<EzcRequestDealers>(arrTempList);
	    	for(EzcRequestDealers reqDealer : reqDealSet)
	    	{
	    		reqDealer.setEzcRequestHeader(ezRequestHeader);
	    	}
	    	ezRequestHeader.setEzcRequestDealers(reqDealSet);
	    	
	    	tpsService.createTPSRequest(ezRequestHeader);
	        ra.addFlashAttribute("success","TPS request details saved sucessfully.");
	        return "redirect:/tps/add";

	    }
	   @RequestMapping(value = "/tps/appr-tps-post", method = RequestMethod.POST)
		public @ResponseBody String approveTpsRequest(@RequestParam String id,@RequestParam String [] apprMat,@RequestParam Integer [] apprQty) {
			EzcRequestHeader ezcRequestHeader = new EzcRequestHeader(); 
			ezcRequestHeader.setId(id);
			Set<RequestMaterials> reqMatSet = new HashSet<RequestMaterials>();
			for(int i=0;i<apprMat.length;i++)
			{
				RequestMaterials reqMat = new RequestMaterials();
				reqMat.setMatCode(apprMat[i].split("#")[0]);
				reqMat.setMatDesc(apprMat[i].split("#")[1]);
				reqMat.setApprQty(apprQty[i]);
				reqMatSet.add(reqMat);
			}
			ezcRequestHeader.setRequestMaterials(reqMatSet);
			tpsService.approveTPSRequest(ezcRequestHeader);
			return "ok"; 
		}
	    
	   @RequestMapping(value = "/tps/addDetails/{docId}", method = RequestMethod.GET)
	    public String addDetails(@PathVariable String docId, Model model) {
	        EzcRequestHeader ezcRequestHeader = tpsService.getTPSRequest(docId);
	        TpsRequestDetailDto reqDto = new TpsRequestDetailDto();
	        List<EzcRequestItems> ezcRequestItems=new ArrayList<EzcRequestItems>();
	        List<RequestMaterials> ezcMatList=new ArrayList<RequestMaterials>();
	        List<EzcRequestDealers> ezcRequestDealers=new ArrayList<EzcRequestDealers>();
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
	        reqDto.setEzReqMatList(ezcMatList);
	        model.addAttribute("reqDto", reqDto);
	        if("APPROVED".equals(ezcRequestHeader.getErhStatus()))
	        	return "tps/tpsDetailsForm";
	        else
	        	return "tps/detailsEditForm";

	    }   

	  @RequestMapping(value = "/tps/tpsRequestList", method = RequestMethod.GET)
	    public String list(ListSelector listSelector , Model model) {
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
	    	List<EzcRequestHeader> list = tpsService.getTPSRequestListByDate(listSelector);
	        model.addAttribute("reqList", list);
	        model.addAttribute("listSelector", listSelector);
	        return "tps/tpsList"; 

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
			reqDto.setEzcRequestDealers(ezcRequestDealers);
			
	        model.addAttribute("reqDto", reqDto);  
	        return "tps/tpsDetailsForm";

	    }
	  public List<EzcRequestItems> processText(List<EzcRequestItems> ezcRequestItems,String text)
	    {
	    	if(text != null && !"null".equals(text) && !"".equals(text))
	    	{
		    	text = text.toUpperCase();
		    	String [] requestArr = text.split("NEXT");
		    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		    	for(String str :requestArr)
		    	{
		              String [] tokens = new String[5];
		              str = str.replaceAll("\\s+","");
		              //Pattern p = Pattern.compile("(\\d+)|([a-zA-Z]+)"); //(\\d+)([a-zA-Z]+)(\\d+)
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
		    		ezcRequestItemsObj.setEriDealer(tokens[0]);
		    		ezcRequestItemsObj.setEriPlumberName(tokens[1]);
		    		ezcRequestItemsObj.setEriContact(tokens[2]);
		    		try {
						ezcRequestItemsObj.setEriDob(sdf.parse(tokens[3]));
						ezcRequestItemsObj.setEriDoa(sdf.parse(tokens[4]));
					} catch (ParseException e) {
						
					}
		    		ezcRequestItems.add(ezcRequestItemsObj);
		    	}
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
	    public String processSpeechData(TpmRequestDetailDto reqDto, Model model) {
			
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
	        return "tps/tpsDetailsForm";

	    }
	  @RequestMapping(value = "/tps/saveDetails", method = RequestMethod.POST)
	    public String saveDetails(TpsRequestDetailDto tpsRequestDetailDto, final RedirectAttributes ra) {
	    	tpsService.createTPSDetails(tpsRequestDetailDto);
	        ra.addFlashAttribute("successFlash", "Success");
	        return "redirect:/tpm/tpmRequestList";

	    }
        
}
 