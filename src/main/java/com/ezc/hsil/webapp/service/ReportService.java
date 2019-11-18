package com.ezc.hsil.webapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.persistance.dao.RequestDetailsRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestHeaderRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestMaterialsRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ReportService implements IReportService {
 
    @Autowired
    private RequestHeaderRepo reqHeaderRepo;
    
    @Autowired
    private RequestDetailsRepo reqDtlRep;
    
    @Autowired
    private RequestMaterialsRepo reqMatRep;

	@Override
	public List<EzcRequestHeader> getRequestStatus(ListSelector listSelector) {
		if("ALL".equals(listSelector.getStatus()))
			return reqHeaderRepo.findByErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getToDate(),listSelector.getFromDate());
		else
			return reqHeaderRepo.findByErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getStatus(),listSelector.getToDate(),listSelector.getFromDate());
	}

}
