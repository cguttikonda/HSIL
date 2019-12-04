package com.ezc.hsil.webapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.persistance.dao.EzcCommentsRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestHeaderRepo;

@Service
@Transactional
public class ReportService implements IReportService {
 
    @Autowired
    private RequestHeaderRepo reqHeaderRepo;

    @Autowired
    private EzcCommentsRepo commRepo;
    
	@Override
	public List<EzcRequestHeader> getRequestStatus(ListSelector listSelector) {
		if("ALL".equals(listSelector.getStatus()))
			return reqHeaderRepo.findByErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getToDate(),listSelector.getFromDate());
		else
			return reqHeaderRepo.findByErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getStatus(),listSelector.getToDate(),listSelector.getFromDate());
	}

	@Override
	public List<Object[]> getDispatchReport(String statu) {
		if("PENDING".equals(statu))
		{
			return reqHeaderRepo.getPendingDispatchDetails();
		}
		else
		{
			return null;
		}
	}

	@Override
	public void dispatchUpdate(EzcRequestHeader ezcRequestHeader) {
		  EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(ezcRequestHeader.getId()).orElseThrow(() -> new EntityNotFoundException());
		  Set<EzcComments> ezComments = ezcRequestHeader.getEzcComments();
		  ezReqHeader.getEzcComments().addAll(ezComments);
		  ezReqHeader.setErhDispatchFlag('Y');
		  for(EzcComments tempItem : ezComments) { 
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  commRepo.save(tempItem); 
			}
	}

	@Override
	public Map<String, String> getDashBoardValues(ArrayList<String> role,String user) {
		Map<String, String> htMap = new HashMap<String, String>();
		
		if(role.contains("ROLE_OUT_STOR"))
		{
			long dispCnt = reqHeaderRepo.getPendingDispatchCount();
			htMap.put("pendDispCnt",dispCnt+"");
		}
		if(role.contains("ROLE_REQ_CR"))
		{
			long dispCnt = reqHeaderRepo.getToAckDispCnt(user);
			htMap.put("ackDispCnt",dispCnt+"");
		}
		return htMap;
	}

	@Override
	public List<Object[]> getStockAvailabilityForAll() {
		return reqHeaderRepo.getStockAvailabilityForAll();
	}

	@Override
	public List<Object[]> getToAckDispReport(String user) {
		return reqHeaderRepo.getToAckDispReport(user);
	}

	@Override
	public void dispatchAckUpdate(EzcRequestHeader ezcRequestHeader) {
		  EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(ezcRequestHeader.getId()).orElseThrow(() -> new EntityNotFoundException());
		  Set<EzcComments> ezComments = ezcRequestHeader.getEzcComments();
		  ezReqHeader.getEzcComments().addAll(ezComments);
		  ezReqHeader.setErhDispatchFlag('S');
		  for(EzcComments tempItem : ezComments) {  
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  commRepo.save(tempItem); 
			}
		}

}
