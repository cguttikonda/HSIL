package com.ezc.hsil.webapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
import com.ezc.hsil.webapp.service.IMasterService;
import com.ezc.hsil.webapp.service.ITPMService;
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
	
	
	
	@Value("#{'${city}'.split(',')}")
	private List<String> city;
	
	
	@GetMapping(value="/edit-dist/{id}")
	public String editDistModal(@PathVariable("id") int id, Model model) {
		model.addAttribute("distributorDto", masterService.getDistributorDetails(id));
		model.addAttribute("city", city);
		return "modals/editDistributor" ;
	}
	
	@GetMapping(value="/delete-dist/{id}")
	public String deleteDistModal(@PathVariable("id") int id, Model model) {
		model.addAttribute("distributorDto", masterService.getDistributorDetails(id));
		return "modals/deleteDistributor" ;
	}
	
	@GetMapping(value = "/delete-user/{id}") 
    public String getUserById(@PathVariable("id") long id, Model model) {   	    	
    	model.addAttribute("user", ((Optional)iUserService.getUserByID(id)).get());
		return "modals/deleteUser" ;
    }
	

	@GetMapping(value="/edit-material/{id}")
	public String editMaterialModal(@PathVariable("id") int id, Model model) {
		model.addAttribute("materialDto", masterService.getMaterialDetails(id));
		model.addAttribute("city", city);
		return "modals/editMaterial" ;
	}
	@GetMapping(value="/delete-material/{id}")
	public String deleteMaterialModal(@PathVariable("id") int id, Model model) {
		model.addAttribute("materialDto", masterService.getMaterialDetails(id));
		return "modals/deleteMaterial" ;
	}
	
	@GetMapping(value="/appr-tpm/{id}")
	public String approveTPMModal(@PathVariable("id") String id, Model model) {
		EzcRequestHeader ezReqHead = iTPMService.getTPMRequest(id);
		
		model.addAttribute("tpmDetails", ezReqHead);
		return "modals/approveTPM" ; 
	}

	@RequestMapping(value = "/mat-autocomp", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<MaterialMaster> materialAutoComplete(@RequestParam String q) {
		return masterService.findAllMaterialsLike(q);
		
	}
	
}
