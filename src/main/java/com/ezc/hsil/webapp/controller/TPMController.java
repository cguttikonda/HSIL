package com.ezc.hsil.webapp.controller;





import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezc.hsil.webapp.dto.TpmRequestDetailDto;
import com.ezc.hsil.webapp.dto.TpmRequestDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.service.ITPMService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TPMController {

    @Autowired
    ITPMService tpmService;

    @RequestMapping("/tpm/add")
    public String add(Model model) {

        model.addAttribute("tpmRequestDto", new TpmRequestDto());
        return "tpm/form";

    } 
    
    @RequestMapping(value = "/tpm/tpmRequestList/{status}", method = RequestMethod.GET)
    public String list(@PathVariable String status, Model model) {
        List<EzcRequestHeader> list = tpmService.getTPMRequestList("TEST");
        model.addAttribute("reqList", list);
        return "tpm/list";

    } 
    
    @RequestMapping(value = "/tpm/saveRequest", method = RequestMethod.POST)
    public String save(TpmRequestDto tpmRequestDto, final RedirectAttributes ra) {
    	EzcRequestHeader ezRequestHeader = new EzcRequestHeader();
    	ezRequestHeader.setErhCity(tpmRequestDto.getErhCity());
    	ezRequestHeader.setErhConductedOn(tpmRequestDto.getErhConductedOn());
    	ezRequestHeader.setErhCreatedGroup("TPM");
    	ezRequestHeader.setErhDistrubutor(tpmRequestDto.getErhDistrubutor());
    	ezRequestHeader.setErhNoOfAttendee(tpmRequestDto.getErhNoOfAttendee());
    	ezRequestHeader.setErhReqType("TYP");
    	ezRequestHeader.setErhRequestedOn(new Date());
    	ezRequestHeader.setErhState("TEST");
    	ezRequestHeader.setErhStatus("S");
    	
    	tpmService.createTPMRequest(ezRequestHeader);
        ra.addFlashAttribute("successFlash", "Success");
        return "redirect:/tpm/list";

    }
    
    @RequestMapping(value = "/tpm/addDetails/{docId}", method = RequestMethod.GET)
    public String addDetails(@PathVariable Integer docId, Model model) {
        EzcRequestHeader ezcRequestHeader = tpmService.getTPMRequest(docId);
        TpmRequestDetailDto reqDto = new TpmRequestDetailDto();
        List<EzcRequestItems> ezcRequestItems=new ArrayList<EzcRequestItems>();
        reqDto.setEzcRequestItems(ezcRequestItems);
        reqDto.setReqHeader(ezcRequestHeader);
        reqDto.setEzcRequestDealers(ezcRequestHeader.getEzcRequestDealers());
        model.addAttribute("reqDto", reqDto);
        return "tpm/detailsForm";

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
		
        model.addAttribute("reqDto", reqDto); 
        return "tpm/detailsForm";

    }
    
    @RequestMapping(value = "/tpm/addNewItem", method = RequestMethod.POST)
    public String addNewTPMItem(TpmRequestDetailDto reqDto, Model model) {
        List<EzcRequestItems> ezcRequestItems=null;
        if(reqDto.getEzcRequestItems() == null)
        	ezcRequestItems = new ArrayList<EzcRequestItems>();
        else
        	ezcRequestItems = reqDto.getEzcRequestItems();
        ezcRequestItems.add(new EzcRequestItems());
        reqDto.setEzcRequestItems(ezcRequestItems);
		reqDto.setReqHeader(reqDto.getReqHeader());
		
        model.addAttribute("reqDto", reqDto); 
        return "tpm/detailsForm";

    }

    @RequestMapping(value = "/tpm/saveDetails", method = RequestMethod.POST)
    public String saveDetails(TpmRequestDetailDto tpmRequestDetailDto, final RedirectAttributes ra) {
    	tpmService.createTPMDetails(tpmRequestDetailDto);
        ra.addFlashAttribute("successFlash", "Success");
        return "redirect:/tpm/tpmRequestList/ALL";

    }
    
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
}
