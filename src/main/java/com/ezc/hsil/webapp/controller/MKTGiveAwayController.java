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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
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

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.MaterialDto;
import com.ezc.hsil.webapp.dto.MaterialQtyDto;
import com.ezc.hsil.webapp.dto.MktgGiveAwayDto;
import com.ezc.hsil.webapp.dto.TPMMeetDto;
import com.ezc.hsil.webapp.dto.TpmRequestDto;
import com.ezc.hsil.webapp.model.DistributorMaster;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcMktGiveAway;
import com.ezc.hsil.webapp.model.EzcRequestDealers;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.UserDefaults;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.service.IMKTGGiveAwyService;
import com.ezc.hsil.webapp.service.IMasterService;
import com.ezc.hsil.webapp.service.IUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MKTGiveAwayController {  

    @Autowired
    IMKTGGiveAwyService iMKTGGiveAwyService;
    
    @Autowired
    IMasterService masterService;
    
    @Autowired
    IUserService userService;
    
 
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    	binder.setAutoGrowCollectionLimit(2000);
    	binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @RequestMapping("/mktg/add")
    public String add(Model model) {
	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		String loggedUser=(String)userObj.getUserId();
		String store="";
		Set<UserDefaults> userDefSet= userObj.getUserDefaults();
		for(UserDefaults userDef:userDefSet)
		{
			if("STORE".equals(userDef.getKey()))
			{
				store = userDef.getValue();
				break;
			}
		}
    	MktgGiveAwayDto mktgGiveAwayDto = new MktgGiveAwayDto();
    	List<String> userCatList=new ArrayList<String>();
    	userCatList.add("PLUMBING");
    	userCatList.add("COLUMN");
    	userCatList.add("WATERTANK");
    	
    	List<DistributorMaster> distList = masterService.findAll();
    	List<Users> userList = userService.findUsersByRole("ROLE_ST_HEAD");
    	log.debug("userList:::::::"+userList);
    	mktgGiveAwayDto.setDistList(distList);
    	mktgGiveAwayDto.setUserList(userList);
    	mktgGiveAwayDto.setOutstore(store);
    	mktgGiveAwayDto.setUserCatList(userCatList);
    	List<MaterialQtyDto> matList=new ArrayList<MaterialQtyDto>();
    	for(int i=0;i<10;i++)
    	{
    		matList.add(new MaterialQtyDto());
    	}
    	
		log.debug("loggedUser"+loggedUser);
    	mktgGiveAwayDto.setMatList(matList);
        model.addAttribute("mktgGiveAwayDto", mktgGiveAwayDto);
        model.addAttribute("loggedUser", loggedUser);
        return "mktg/form";

    } 
    @RequestMapping(value="/mktg/getDistDetails/{dist}",method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody  DistributorDto getMatDetails(@PathVariable String dist)
	{
		DistributorDto distdto=masterService.getDistributorDetails(dist);
		String city="";
		try {
			city=distdto.getCity();
		
		}catch(Exception e)
		{
			
		}
		
		log.debug("city"+city);
		
		return distdto;
	}

    @RequestMapping(value = "/mktg/saveRequest", method = RequestMethod.POST)
    public String save(@ModelAttribute MktgGiveAwayDto mktgGiveAwayDto,BindingResult bindingResult, final RedirectAttributes ra) {
/*    	if(bindingResult.hasErrors())
    	{
    		return "tpm/form";
    	}
    	else 
    	{
 */    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	Users userObj = (Users)authentication.getPrincipal();
    	Users stateHeadDet=userService.findUserByUserId(mktgGiveAwayDto.getSentTo());
    	DistributorDto distMast=masterService.getDistributorDetails(mktgGiveAwayDto.getDistrubutor());
    	
    	mktgGiveAwayDto.setCreatedBy(userObj.getUserId());
    	mktgGiveAwayDto.setSentToName(stateHeadDet.getFirstName()+" "+stateHeadDet.getLastName());
    	mktgGiveAwayDto.setDistName(distMast.getName());
    	//mktgGiveAwayDto.setVertical(distMast.getType());
    	try {
			iMKTGGiveAwyService.createMKTData(mktgGiveAwayDto); 
			ra.addFlashAttribute("success","Details Saved Successfully.");
		} catch (Exception e) {
			ra.addFlashAttribute("error","Error updating request. Please create request again");
		}
        
        return "redirect:/mktg/add";
    	
    }
    
    @RequestMapping(value = "/mktg/mktgRequestList", method = RequestMethod.GET)
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
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		ArrayList<String> userList=new ArrayList<String>();
    	if(requestWrapper.isUserInRole("ROLE_ST_HEAD"))
    	{
    		listSelector.setSentTo(userObj.getUserId());
    	}
    	else if(requestWrapper.isUserInRole("ADMIN"))
    	{
    		
    	}
    	else
    	{
    		userList.add(userObj.getUserId());
    		listSelector.setUser(userList);
    	}
		
    	List<EzcMktGiveAway> list = iMKTGGiveAwyService.getRequestList(listSelector);
    	log.debug("list:::"+list.size());
        model.addAttribute("reqList", list);
        model.addAttribute("listSelector", listSelector);
        return "mktg/list"; 

    }
    
    @RequestMapping(value = "/mktg/mktgListByStatus/{status}", method = RequestMethod.GET)
    public String listByStatus(Model model,SecurityContextHolderAwareRequestWrapper requestWrapper,@PathVariable String status) {
    	
    	ListSelector listSelector=new ListSelector();
    	listSelector.setStatus(status);
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
    	listSelector.setSentTo(userObj.getUserId());
    	List<EzcMktGiveAway> list = iMKTGGiveAwyService.getRequestList(listSelector);
        model.addAttribute("reqList", list);
        model.addAttribute("listSelector", listSelector);
        return "mktg/list"; 

    }
/*    
    @RequestMapping(value = "/mktg/ackRequest/{id}", method = RequestMethod.GET)
    public String ackRequest(@PathVariable String id , Model model,SecurityContextHolderAwareRequestWrapper requestWrapper) {	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		iMKTGGiveAwyService.acknowledgeRequest(Integer.parseInt(id), userObj.getUserId());
        return "redirect:/mktg/mktgRequestList"; 

    }
*/
    @RequestMapping(value = "/mktg/ackRequest", method = RequestMethod.POST)
    public @ResponseBody String ackRequest(@RequestParam(value = "id", required = false) String id,@RequestParam(value = "ackComments", required = false) String ackComments) {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	   		Users userObj = (Users)authentication.getPrincipal();
	   		iMKTGGiveAwyService.acknowledgeRequest(Integer.parseInt(id), userObj.getUserId(),ackComments);
	        return "ok";
    }   

}
