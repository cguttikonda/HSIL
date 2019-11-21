package com.ezc.hsil.webapp.service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.TpsRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.persistance.dao.MaterialMasterRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestDetailsRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestHeaderRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestMaterialsRepo;
import com.ezc.hsil.webapp.service.ITPSService;
import com.ezc.hsil.webapp.service.TpsServiceImpl;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@Transactional
public class TpsServiceImpl implements ITPSService {
	
	
	 @Autowired
	    private RequestHeaderRepo reqHeaderRepo;
	 @Autowired
	    private RequestMaterialsRepo reqMatRep;
	 @Autowired
	 private RequestDetailsRepo reqDealRep;
	 @Autowired
	 MaterialMasterRepo masterRepo; 
	 
	@Override
	public void createTPSRequest(EzcRequestHeader ezcRequestHeader) {
		// TODO Auto-generated method stub
		log.debug("In service class","I"); 
		reqHeaderRepo.save(ezcRequestHeader);
	
	}
	@Override
	public void approveTPSRequest(EzcRequestHeader ezcRequestHeader) {
	  EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(ezcRequestHeader.getId()).orElseThrow(() -> new EntityNotFoundException());
	  Set<RequestMaterials> ezReqMatList = ezcRequestHeader.getRequestMaterials();
	  ezReqHeader.getRequestMaterials().addAll(ezReqMatList);
	  ezReqHeader.setErhStatus("APPROVED");
	  
	  for(RequestMaterials tempItem : ezReqMatList) { 
		  tempItem.setEzcRequestHeader(ezReqHeader);	
		  reqMatRep.save(tempItem); 
		}
	  for(RequestMaterials tempItem : ezReqMatList) {
	      Character c1 = new Character('N'); 
		  if(c1.equals(tempItem.getIsNew()))
		  {
			  RequestMaterials requestMaterials = reqMatRep.findById(tempItem.getAllocId()).orElseThrow(() -> new EntityNotFoundException());
			  requestMaterials.setLeftOverQty(requestMaterials.getLeftOverQty()-tempItem.getApprQty());
		  }
		  else
		  {
			  Optional<MaterialMaster> matMaster = masterRepo.findById(tempItem.getMatCode());
                if(matMaster.isPresent())
                {  
                       MaterialMaster mat = matMaster.get();
                       int qty = mat.getQuantity()-tempItem.getApprQty();
                       mat.setQuantity(qty);
                }      

		  }
	  }
	  for(RequestMaterials tempItem : ezReqMatList) { 
		  tempItem.setEzcRequestHeader(ezReqHeader);	
		  reqMatRep.save(tempItem); 
		}	
	}

	@Override
	public List<EzcRequestHeader> getTPSRequestListByDate(ListSelector listSelector) {
		if("ALL".equals(listSelector.getStatus()))
			return reqHeaderRepo.findByErhReqTypeAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getType(),listSelector.getToDate(),listSelector.getFromDate());	
		else	
			return reqHeaderRepo.findByErhReqTypeAndErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getType(),listSelector.getStatus(),listSelector.getToDate(),listSelector.getFromDate());
	}
	@Override
	public EzcRequestHeader getTPSRequest(String docId) {
		EzcRequestHeader reqHeader = reqHeaderRepo.findById(docId).orElseThrow(() -> new EntityNotFoundException());
		return reqHeader;
	}
	
	@Override
	public void createTPSDetails(TpsRequestDetailDto tpsRequestDetailDto) {
		EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(tpsRequestDetailDto.getReqHeader().getId()).orElseThrow(() -> new EntityNotFoundException());
		List<EzcRequestItems> ezReqItemList = tpsRequestDetailDto.getEzcRequestItems();
		Set<RequestMaterials> reqMatSet = ezReqHeader.getRequestMaterials();
	  ezReqHeader.setErhStatus("SUBMITTED");
	  ezReqHeader.setErhCostIncured(tpsRequestDetailDto.getReqHeader().getErhCostIncured());
	  ezReqHeader.setErhVenue(tpsRequestDetailDto.getReqHeader().getErhVenue());
	  ezReqHeader.setEzcRequestItems(new HashSet<EzcRequestItems>(ezReqItemList));
	  for(EzcRequestItems tempItem : ezReqItemList) { 
		  tempItem.setEzcRequestHeader(ezReqHeader);	
		  reqDealRep.save(tempItem); 
		}
	  int matCnt = ezReqItemList.size();
	   for(RequestMaterials requestMaterials : reqMatSet) { 
		   int apprQty =requestMaterials.getApprQty();
		   if(apprQty > matCnt)
		   {
			   requestMaterials.setLeftOverQty(apprQty-matCnt);
			   requestMaterials.setUsedQty(matCnt);
		   }
		   else
		   {
			   requestMaterials.setUsedQty(apprQty);
			   matCnt = matCnt-apprQty;
		   }
		} 
	  

	}
	@Override
	public Set<RequestMaterials> getLastRequestDet(String requestedBy) {
		EzcRequestHeader ezRequestHeader =reqHeaderRepo.findTopByErhCreatedGroupAndErhRequestedByAndErhStatusOrderByErhConductedOnDesc("TPS",requestedBy,"CLOSED");
		if(ezRequestHeader != null)
		{
			return reqMatRep.findByRequest(ezRequestHeader.getId());
		}
		else
		{
			return null;
		}
	}
	@Override
	public List<Object[]> getLeftOverStock(String requestedBy) {
		return reqHeaderRepo.getLeftOverStock(requestedBy);
	}
}
