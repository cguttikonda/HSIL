package com.ezc.hsil.webapp.controller;

import java.sql.SQLException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.service.IMasterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/master")
public class MasterDataController {

	@Autowired
	IMasterService masterService;
	
	
	@GetMapping("/addDis")
	public String showDistributorForm(Model model) {

	
		log.info("In the class {}", this.getClass());
		
		DistributorDto disDto = new DistributorDto();
		
		model.addAttribute("distributorDto", disDto);
		
		return "master/addDistributor";
	}
	
	
	@PostMapping("/add")
	public String saveDistData(@Valid @ModelAttribute("distributorDto") DistributorDto disDto, final BindingResult bindingResult,RedirectAttributes model) {
	
		if(bindingResult.hasErrors()) {
		
			log.info("Binding Results:::: {}", bindingResult);
			return "master/addDistributor";
		}
		else {
		
			log.info("In the class {}", disDto.toString());
		
			model.addFlashAttribute("success", "Distributor added successfully.");
			masterService.addNewDistributor(disDto);
			return "redirect:/master/addDis";
		}
	}
	
	
	@GetMapping("/listDis")
	public String listDistributors(Model model) {
		
		model.addAttribute("distList", masterService.findAll());

		return "master/distributorList";
	}

	@PostMapping("/edit-dist")
	public String updateDistributor(@Valid @ModelAttribute("distributorDto") DistributorDto disDto,
			final BindingResult bindingResult) throws SQLException {
		
		if(bindingResult.hasErrors()) {
			
			log.info("Binding Results:::: {}", bindingResult);
			return "master/distributorList :: edit-dist"; 
		}
		else {
		
			log.info("In the class {}", disDto.toString());
		
			masterService.updateDistributor(disDto);
			return "redirect:/master/listDis";
		}
		
	}
	@PostMapping("/delete-dist/{id}")
	public String deleteDistributor(@PathVariable("id") int id) {
		
		
		
			masterService.deleteDistributor(id);
			return "redirect:/master/listDis";
		}

	
	
}
