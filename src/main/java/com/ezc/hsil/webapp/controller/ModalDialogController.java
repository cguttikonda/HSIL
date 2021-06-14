package com.ezc.hsil.webapp.controller;

import java.util.ArrayList;
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

import com.ezc.hsil.webapp.model.EzStores;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.service.IBDService;
import com.ezc.hsil.webapp.service.IMasterService;
import com.ezc.hsil.webapp.service.ITPMService;
import com.ezc.hsil.webapp.service.ITPSService;
import com.ezc.hsil.webapp.service.IUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	@Autowired
    private IBDService iBDService;
	
	
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
	

	@GetMapping(value="/edit-material/{materialCode}/{stockLoc}")
	public String editMaterialModal(@PathVariable("materialCode") String materialCode,@PathVariable("stockLoc") String stockLoc, Model model) {
		model.addAttribute("materialDto", masterService.getMaterialDetails(materialCode,stockLoc));
		model.addAttribute("city", city);
		return "modals/editMaterial" ;
	}
	@GetMapping(value="/delete-material/{materialCode}/{stockLoc}")
	public String deleteMaterialModal(@PathVariable("materialCode") String materialCode,@PathVariable("stockLoc") String stockLoc, Model model) {
		model.addAttribute("materialDto", masterService.getMaterialDetails(materialCode,stockLoc));
		return "modals/deleteMaterial" ;
	}
	@GetMapping(value="/delete-city/{city}")
	public String deleteCityModal(@PathVariable("city") String city, Model model) {
		model.addAttribute("placeMasterDto", masterService.getCityDetails(city));
		return "modals/deleteCity" ;
	}
	  
	@GetMapping(value="/appr-tpm/{id}")
	public String approveTPMModal(@PathVariable("id") String id, Model model) {
		EzcRequestHeader ezReqHead = iTPMService.getTPMRequest(id);
		Integer attendee=ezReqHead.getErhNoOfAttendee();
		log.debug("att"+ezReqHead.getErhNoOfAttendee());
		int leftOverStk=0;
		List<Object[]> matLi=iTPMService.getLeftOverStock(ezReqHead.getErhRequestedBy(),"TPM");
		//List<Users> outStoreList = iUserService.findUsersByRole("ROLE_OUT_STOR");
		List<EzStores> outStoreList = masterService.listStores();
		List<Object[]> list=iTPMService.pendingRequests(ezReqHead.getErhRequestedBy());
		
		for(int k=0;k<matLi.size();k++)
		{	
		leftOverStk=leftOverStk+(Integer)matLi.get(k)[4];
		}
		
		log.debug("leftOverStk"+leftOverStk);
		try {
			/*
			 * Authentication authentication =
			 * SecurityContextHolder.getContext().getAuthentication(); Users userObj =
			 * (Users)authentication.getPrincipal();
			 */
			model.addAttribute("matList", iTPMService.getLeftOverStock(ezReqHead.getErhRequestedBy(),"TPM"));
			model.addAttribute("leftOverStk", leftOverStk);
			model.addAttribute("attendee", attendee);
			model.addAttribute("outStoreList", outStoreList);
			model.addAttribute("reqList", list);
		} catch (Exception e) {
			 
		}
		model.addAttribute("tpmDetails", ezReqHead);
		return "modals/approveTPM" ; 
	}
	@GetMapping(value="/rej-tpm/{id}")
	public String rejectTPMModal(@PathVariable("id") String id, Model model) {
		EzcRequestHeader ezReqHead = iTPMService.getTPMRequest(id);
		
		model.addAttribute("tpmDetails", ezReqHead);
		return "modals/rejectTPM" ; 
	}
	@GetMapping(value="/rej-tps/{id}")
	public String rejectTPSModal(@PathVariable("id") String id, Model model) {
		EzcRequestHeader ezReqHead = iTPSService.getTPSRequest(id);
		
		model.addAttribute("tpmDetails", ezReqHead);
		return "modals/rejectTPM" ; 
	}
	@GetMapping(value="/rej-bd/{id}")
	public String rejectBDModal(@PathVariable("id") String id, Model model) {
		EzcRequestHeader ezReqHead = iBDService.getBDRequest(id);
		model.addAttribute("bdDetails", ezReqHead);
		return "modals/rejectBD" ; 
	}
	@RequestMapping(value = "/mat-autocomp", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<MaterialMaster> materialAutoComplete(@RequestParam String q,@RequestParam String stockLoc) {
		List<MaterialMaster> matList =masterService.findAllMaterialsLike(q);
		//List<MaterialMaster> matListOut =new ArrayList<MaterialMaster>();
		log.debug("matlist"+matList.size());
		for(MaterialMaster matObj:matList)
		{
			if(stockLoc != null && !"null".equals(stockLoc) && !"".equals(stockLoc))
			{
				if(!matObj.getStockLoc().equals(stockLoc))
					continue;
			}
			
			int tempQty =matObj.getQuantity();
			Integer tempBlockQty =matObj.getBlockQty();
			if(tempBlockQty==null)tempBlockQty=0;
			int outQty =0; 
			if(tempBlockQty > 0)
				outQty = tempQty-tempBlockQty;
			else
				outQty = tempQty;
			//log.debug("outQty"+outQty);
			if(outQty > 0)
			{
				matObj.setQuantity(outQty);
			}
			else
			{
				matObj.setQuantity(0);
			}
			//log.debug("tempBlockQty"+tempBlockQty);
		}
		return matList;
		
	}
	
	@RequestMapping(value = "/matstock-autocomp", method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody List<MaterialMaster> materialStockAutoComplete(@RequestParam String q,@RequestParam String stockLoc) {
				if(stockLoc == null || "null".equals(stockLoc) || "".equals(stockLoc))
				{
					return null;
				}
				List<MaterialMaster> matList =masterService.findAllMaterialsLike(q);
				List<MaterialMaster> toBeRemoved =new ArrayList<MaterialMaster>(); 
				//List<MaterialMaster> matListOut =new ArrayList<MaterialMaster>();
				log.debug("matlist"+matList.size());
				for(MaterialMaster matObj:matList)
				{
					if(stockLoc != null && !"null".equals(stockLoc) && !"".equals(stockLoc))
					{
						if(!matObj.getStockLoc().equals(stockLoc))
							toBeRemoved.add(matObj);
					}
					
					int tempQty =matObj.getQuantity();
					Integer tempBlockQty =matObj.getBlockQty();
					if(tempBlockQty==null)tempBlockQty=0;
					int outQty =0; 
					if(tempBlockQty > 0)
						outQty = tempQty-tempBlockQty;
					else
						outQty = tempQty;
					//log.debug("outQty"+outQty);
					if(outQty > 0)
					{
						matObj.setQuantity(outQty);
					}
					else
					{
						matObj.setQuantity(0);
					}
					//log.debug("tempBlockQty"+tempBlockQty);
				}
				matList.removeAll(toBeRemoved);

				return matList;
				
			}
	
	@GetMapping(value="/appr-tps/{id}")
	public String approveTPSModal(@PathVariable("id") String id, Model model) {
		EzcRequestHeader ezReqHead = iTPSService.getTPSRequest(id);
		Integer attendee=ezReqHead.getErhNoOfAttendee();
		log.debug("att"+ezReqHead.getErhNoOfAttendee());
		int leftOverStk=0;
		List<Object[]> matLi=iTPSService.getLeftOverStock(ezReqHead.getErhRequestedBy(),"TPS");
		List<Users> outStoreList = iUserService.findUsersByRole("ROLE_OUT_STOR");
		for(int k=0;k<matLi.size();k++)
		{	
		log.debug("matli"+matLi.get(k)[4]);
		leftOverStk=leftOverStk+(Integer)matLi.get(k)[4];
		}
		
		log.debug("leftOverStk"+leftOverStk);
		try {
			/*
			 * Authentication authentication =
			 * SecurityContextHolder.getContext().getAuthentication(); Users userObj =
			 * (Users)authentication.getPrincipal();
			 */
			model.addAttribute("matList", iTPSService.getLeftOverStock(ezReqHead.getErhRequestedBy(),"TPS"));
		} catch (Exception e) {
			 
		}
		model.addAttribute("tpsDetails", ezReqHead);
		model.addAttribute("leftOverStk", leftOverStk);
		model.addAttribute("attendee", attendee);
		model.addAttribute("outStoreList", outStoreList);
		return "modals/approveTPS" ; 
	}

	@GetMapping(value="/dispmodal/{id}")
	public String dispatchModal(@PathVariable("id") String id, Model model) {
		EzcRequestHeader ezReqHead = iTPSService.getTPSRequest(id);
		model.addAttribute("reqDetails", ezReqHead);
		return "modals/dispatchDet" ; 
	}
	
	@GetMapping(value="/dispackmodal/{id}")
	public String dispatchAckModal(@PathVariable("id") String id, Model model) {
		EzcRequestHeader ezReqHead = iTPSService.getTPSRequest(id);
		model.addAttribute("reqDetails", ezReqHead);
		return "modals/dispatchAckDet" ; 
	}
	
	@GetMapping(value="/att-file")
	public String attachFileModal( ) {
			
		return "modals/attachFile" ; 
	}
}



