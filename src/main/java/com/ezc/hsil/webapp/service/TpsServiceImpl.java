package com.ezc.hsil.webapp.service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.TpsRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.RequestMaterials;
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
	}

	@Override
	public List<EzcRequestHeader> getTPSRequestListByDate(ListSelector listSelector) {
		if("ALL".equals(listSelector.getStatus()))
			return reqHeaderRepo.findByErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getToDate(),listSelector.getFromDate());	
		else	
			return reqHeaderRepo.findByErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getStatus(),listSelector.getToDate(),listSelector.getFromDate());
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
	  ezReqHeader.setErhStatus("SUBMITTED");
	  ezReqHeader.setErhCostIncured(tpsRequestDetailDto.getReqHeader().getErhCostIncured());
	  ezReqHeader.setEzcRequestItems(new HashSet<EzcRequestItems>(ezReqItemList));
	  for(EzcRequestItems tempItem : ezReqItemList) { 
		  tempItem.setEzcRequestHeader(ezReqHeader);	
		  reqDealRep.save(tempItem); 
		}
	 
	}
}
