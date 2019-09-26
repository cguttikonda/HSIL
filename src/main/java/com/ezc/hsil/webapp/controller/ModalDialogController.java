package com.ezc.hsil.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ezc.hsil.webapp.service.IMasterService;

@Controller
@RequestMapping("/modal")
public class ModalDialogController {

	@Autowired
	IMasterService masterService;	
	
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
	
	
	
	
	
	
	
}
