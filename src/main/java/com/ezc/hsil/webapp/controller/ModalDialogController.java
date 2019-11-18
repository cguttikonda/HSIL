package com.ezc.hsil.webapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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

import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.service.IMasterService;
import com.ezc.hsil.webapp.service.ITPMService;
import com.ezc.hsil.webapp.service.ITPSService;
import com.ezc.hsil.webapp.service.IUserService;

@Controller
@RequestMapping("/modal")
public class ModalDialogController {

	@Autowired
	IMasterService masterService;
	
	@Autowired
    private IUserService iUserService;
	
	@Autowired
    private ITPMService iTPMService;
	
	@Autowired
    private ITPSService iTPSService;
	
	@Value("#{'${city}'.split(',')}")
	private List<String> city;
	
	
	@GetMapping(value="/edit-dist/{code}")
	public String editDistModal(@PathVariable("code") String code, Model model) {
		model.addAttribute("distributorDto", masterService.getDistributorDetails(code));
		//model.addAttribute("city", city);
		return "modals/editDistributor" ;
	}
	
	@GetMapping(value="/delete-dist/{code}")
	public String deleteDistModal(@PathVariable("code") String code, Model model) {
		model.addAttribute("distributorDto", masterService.getDistributorDetails(code));
		return "modals/deleteDistributor" ;
	}
	
	@GetMapping(value = "/delete-user/{id}") 
    public String getUserById(@PathVariable("id") long id, Model model) {   	    	
    	model.addAttribute("user", ((Optional)iUserService.getUserByID(id)).get());
		return "modals/deleteUser" ;
    }
	

	@GetMapping(value="/edit-material/{id}")
	public String editMaterialModal(@PathVariable("materialCode") String materialCode, Model model) {
		model.addAttribute("materialDto", masterService.getMaterialDetails(materialCode));
		model.addAttribute("city", city);
		return "modals/editMaterial" ;
	}
	@GetMapping(value="/delete-material/{id}")
	public String deleteMaterialModal(@PathVariable("materialCode") String materialCode, Model model) {
		model.addAttribute("materialDto", masterService.getMaterialDetails(materialCode));
		return "modals/deleteMaterial" ;
	}
	
	@GetMapping(value="/appr-tpm/{id}")
	public String approveTPMModal(@PathVariable("id") String id, Model model) {
		EzcRequestHeader ezReqHead = iTPMService.getTPMRequest(id);
		try {
			/*
			 * Authentication authentication =
			 * SecurityContextHolder.getContext().getAuthentication(); Users userObj =
			 * (Users)authentication.getPrincipal();
			 */
			model.addAttribute("matList", iTPMService.getLeftOverStock(ezReqHead.getErhRequestedBy()));
		} catch (Exception e) {
			 
		}
		model.addAttribute("tpmDetails", ezReqHead);
		return "modals/approveTPM" ; 
	}

	@RequestMapping(value = "/mat-autocomp", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<MaterialMaster> materialAutoComplete(@RequestParam String q) {
		return masterService.findAllMaterialsLike(q);
		
	}
	
	@GetMapping(value="/appr-tps/{id}")
	public String approveTPSModal(@PathVariable("id") String id, Model model) {
		EzcRequestHeader ezReqHead = iTPSService.getTPSRequest(id);
		
		model.addAttribute("tpsDetails", ezReqHead);
		return "modals/approveTPS" ; 
	}
}
