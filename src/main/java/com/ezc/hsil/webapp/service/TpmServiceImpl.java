package com.ezc.hsil.webapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.TpmRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.persistance.dao.RequestDetailsRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestHeaderRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TpmServiceImpl implements ITPMService{
	
	    @Autowired
	    private RequestHeaderRepo reqHeaderRepo;
	    
	    @Autowired
	    private RequestDetailsRepo reqDtlRep;

		@Override
		public void createTPMRequest(EzcRequestHeader ezcRequestHeader) {
			log.debug("In service class","I");
			reqHeaderRepo.save(ezcRequestHeader);
		}

		@Override
		public List<EzcRequestHeader> getTPMRequestList(String status) {
			return reqHeaderRepo.findAll();
		}

		@Override
		public EzcRequestHeader getTPMRequest(int docId) {
			return reqHeaderRepo.findById(docId).orElseThrow(() -> new EntityNotFoundException());
		}

		@Override
		public void createTPMDetails(TpmRequestDetailDto tpmRequestDetailDto) {
			EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(tpmRequestDetailDto.getReqHeader().getId()).orElseThrow(() -> new EntityNotFoundException());
			List<EzcRequestItems> ezReqItemList = tpmRequestDetailDto.getEzcRequestItems();
		/*
		 * ezReqHeader.setEzcRequestItems(new HashSet<EzcRequestItems>(ezReqItemList));
		 * for(EzcRequestItems tempItem : ezReqItemList) { reqDtlRep.save(tempItem); }
		 */
		}
	    

		

}
