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
import com.ezc.hsil.webapp.dto.BDRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.persistance.dao.MaterialMasterRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestDetailsRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestHeaderRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestMaterialsRepo;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@Transactional
public class BDServiceImpl implements IBDService{
	 @Autowired
	    private RequestHeaderRepo reqHeaderRepo;
	 @Autowired
	    private RequestMaterialsRepo reqMatRep;
	 @Autowired
	 private RequestDetailsRepo reqDealRep;
	 @Autowired
	 MaterialMasterRepo masterRepo; 
	 
	 
	@Override
	public void createBDRequest(EzcRequestHeader ezcRequestHeader) {
		// TODO Auto-generated method stub
		log.debug("In service class BD","I"); 
		reqHeaderRepo.save(ezcRequestHeader);
		
		EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(ezcRequestHeader.getId()).orElseThrow(() -> new EntityNotFoundException());
		Set<RequestMaterials> ezReqMatList = ezReqHeader.getRequestMaterials();
		  for(RequestMaterials tempItem : ezReqMatList) {
		      Character c1 = new Character('Y'); 
			  if(c1.equals(tempItem.getIsNew()))
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
	
	}

	@Override
	public List<EzcRequestHeader> getBDRequestListByDate(ListSelector listSelector) {
		/*if("ALL".equals(listSelector.getStatus()))
			return reqHeaderRepo.getBdALLRequestList(listSelector.getType(),listSelector.getFromDate(),listSelector.getToDate()); 
		else	
			return reqHeaderRepo.getBdRequestList(listSelector.getStatus(),listSelector.getType()); */
		if("ALL".equals(listSelector.getStatus()))
			return reqHeaderRepo.findByErhReqTypeAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getType(),listSelector.getToDate(),listSelector.getFromDate());	
		else	
			return reqHeaderRepo.findByErhReqTypeAndErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getType(),listSelector.getStatus(),listSelector.getToDate(),listSelector.getFromDate());
	}
	
	@Override
	public EzcRequestHeader getBDRequest(String docId) {
		EzcRequestHeader reqHeader = reqHeaderRepo.findById(docId).orElseThrow(() -> new EntityNotFoundException());
		return reqHeader;
	}
	
	@Override
	public void createBDDetails(BDRequestDetailDto bdRequestDetailDto) {
		EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(bdRequestDetailDto.getReqHeader().getId()).orElseThrow(() -> new EntityNotFoundException());
		List<EzcRequestItems> ezReqItemList = bdRequestDetailDto.getEzcRequestItems();
		Set<RequestMaterials> reqMatSet = ezReqHeader.getRequestMaterials();
	  ezReqHeader.setErhStatus("SUBMITTED");
	  ezReqHeader.setErhCity(bdRequestDetailDto.getReqHeader().getErhCity());
	  ezReqHeader.setErhPurpose(bdRequestDetailDto.getReqHeader().getErhPurpose());
	  ezReqHeader.setEzcRequestItems(new HashSet<EzcRequestItems>(ezReqItemList));
	  int matCnt = 0;
	  for(EzcRequestItems tempItem : ezReqItemList) {
		  if(tempItem.getEriDealer() != null && !"null".equals(tempItem.getEriDealer()) && !"".equals(tempItem.getEriDealer()))
		  {
			  matCnt++;
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  reqDealRep.save(tempItem);
		  }
		}
	  
	  
	  
	}

	
	
	

}
