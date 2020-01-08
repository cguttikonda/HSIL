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
import com.ezc.hsil.webapp.dto.ReportSelector;
import com.ezc.hsil.webapp.dto.RequestCustomDto;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.persistance.dao.EzcCommentsRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestHeaderRepo;
import com.ezc.hsil.webapp.persistance.dao.WorkGroupUsersRepository;

@Service
@Transactional
public class ReportService implements IReportService {
 
    @Autowired
    private RequestHeaderRepo reqHeaderRepo;

    @Autowired
    private EzcCommentsRepo commRepo;
    
    @Autowired
    private RequestCustomDto customDto;
    
    @Autowired
    private WorkGroupUsersRepository workGrpUserRepo;
    
	@Override
	public List<EzcRequestHeader> getRequestStatus(ListSelector listSelector) {
		return customDto.findRequestList(listSelector);
		/*
		 * if("ALL".equals(listSelector.getStatus())) return reqHeaderRepo.
		 * findByErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(
		 * listSelector.getToDate(),listSelector.getFromDate()); else return
		 * reqHeaderRepo.
		 * findByErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual
		 * (listSelector.getStatus(),listSelector.getToDate(),listSelector.getFromDate()
		 * );
		 */
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
		ArrayList<String> userList = new ArrayList<String>(); 
		userList.add(user);
		if(role.contains("ROLE_OUT_STOR"))
		{
			long dispCnt = reqHeaderRepo.getPendingDispatchCount();
			htMap.put("pendDispCnt",dispCnt+"");
		}
		if(role.contains("ROLE_REQ_CR"))
		{
			long dispCnt = reqHeaderRepo.getToAckDispCnt(user);
			htMap.put("ackDispCnt",dispCnt+"");
			ListSelector listSelector = new ListSelector();
			listSelector.setStatus("APPROVED");
			listSelector.setType("TPM");
			listSelector.setUser(userList);
			dispCnt = customDto.findRequestListCnt(listSelector);
			htMap.put("apprTpmCnt",dispCnt+"");
		}
		if(role.contains("ROLE_ST_HEAD"))
		{
			long dispCnt = reqHeaderRepo.getToAckDispCnt(user);
			htMap.put("ackDispCnt",dispCnt+"");
			ListSelector listSelector = new ListSelector();
			listSelector.setStatus("APPROVED");
			listSelector.setType("TPS");
			listSelector.setUser(userList);
			dispCnt = customDto.findRequestListCnt(listSelector);
			htMap.put("apprTpsCnt",dispCnt+"");
		}
		if(role.contains("ROLE_ST_HEAD"))
		{
			long dispCnt = reqHeaderRepo.getToAckDispCnt(user);
			htMap.put("ackDispCnt",dispCnt+"");
			ListSelector listSelector = new ListSelector();
			listSelector.setStatus("APPROVED");
			listSelector.setType("BD");
			listSelector.setUser(userList);
			dispCnt = customDto.findRequestListCnt(listSelector);
			htMap.put("apprBDCnt",dispCnt+"");
		}
		if(role.contains("ROLE_BD_MKT"))
		{
			long dispCnt = reqHeaderRepo.getToAckDispCnt(user);
			htMap.put("ackDispCnt",dispCnt+"");
			ListSelector listSelector = new ListSelector();
			listSelector.setStatus("APPROVED");
			listSelector.setType("BD");
			listSelector.setUser(userList);
			dispCnt = customDto.findRequestListCnt(listSelector);
			htMap.put("apprBDCnt",dispCnt+"");
		}
		if(role.contains("ROLE_ADMIN"))
		{
			ListSelector listSelector = new ListSelector();
			listSelector.setStatus("NEW");
			listSelector.setType("TPM");
			long dispCnt = customDto.findRequestListCnt(listSelector);
			htMap.put("toActTpmRequest",dispCnt+"");
			listSelector = new ListSelector();
			listSelector.setStatus("NEW");
			listSelector.setType("TPS");
			dispCnt = customDto.findRequestListCnt(listSelector);
			htMap.put("toActTpsRequest",dispCnt+"");
			listSelector = new ListSelector();
			listSelector.setStatus("NEW");
			listSelector.setType("BD");
			dispCnt = customDto.findRequestListCnt(listSelector);
			htMap.put("toActBDRequest",dispCnt+""); 
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
	public List<Object[]> getTPMMonthWise(List<String> userList) {
		return reqHeaderRepo.getTPMMonthWise(userList);
	}
	@Override
	public List<Object[]> getTPSMonthWise(List<String> userList) {
		return reqHeaderRepo.getTPSMonthWise(userList);
	}
	/*@Override
	public List<Object[]> getBDMonthWise(List<String> userList) {
		return reqHeaderRepo.getBDMonthWise(userList);
	}*/
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

	@Override
	public List<Object[]> getUsersByHead(String userId) {
		return workGrpUserRepo.getUsersByHead(userId);
	}

	@Override
	public List<EzcRequestHeader> getTeamTPMReport(ReportSelector reportSelector) {
		return customDto.findRequestList(reportSelector);
	}

	@Override
	public List<Object[]> getUsersByZoneHd(String userId) {
		
		return workGrpUserRepo.getUsersByZoneHd(userId);
	}

	@Override
	public List<Object[]> getStockAvailabilityByUser(List<String> userList) {
		return reqHeaderRepo.getStockAvailabilityByUser(userList);
	}

	@Override
	public List<Object[]> getStateHdByZoneHd(String userId) {
		return workGrpUserRepo.getStateHdByZoneHd(userId);
	}

	@Override
	public List<Object[]> getAllStateHd() {
		return workGrpUserRepo.getAllStateHd();
	}

	@Override
	public List<Object[]> getAllUsers() {
		return workGrpUserRepo.getAllUsers();
	}
	@Override
	public List<Object[]> getAllMKTUsers() {
		return workGrpUserRepo.getAllMKTUsers();
	}
	
	@Override
	public List<Object[]> getPlumberMaster(List<String> dist) {
		return reqHeaderRepo.getPlumberMaster(dist);
	}

}
