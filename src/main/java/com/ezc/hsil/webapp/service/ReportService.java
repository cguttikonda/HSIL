package com.ezc.hsil.webapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.ReportSelector;
import com.ezc.hsil.webapp.dto.RequestCustomDto;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcMktGiveAway;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.MaterialMasterKey;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.persistance.dao.EzcCommentsRepo;
import com.ezc.hsil.webapp.persistance.dao.MaterialMasterRepo;
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
    
    @Autowired
    private MaterialMasterRepo masterRepo;
    
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
	public List<Object[]> getDispatchReport(String statu,String userId) {
		if("PENDING".equals(statu))
		{
			return reqHeaderRepo.getPendingDispatchDetails(userId);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public List<Object[]> getAllReqMonthWise()
	{
		return reqHeaderRepo.getAllReqMonthWise();
	}
	@Override
	public List<Object[]> getAllMeetsMonthWise()
	{
		return reqHeaderRepo.getAllMeetsMonthWise();
	}
	@Override
	public List<Object[]> getUserDefaults()
	{
		return reqHeaderRepo.getUserDefaults();
	}
	@Override
	public List<Object[]> getNoofPlumbersPerUser()
	{
		return reqHeaderRepo.getNoofPlumbersPerUser();
	}
	@Override
	public List<Object[]> getUsedLeftQtyPerUser()
	{
		return reqHeaderRepo.getUsedLeftQtyPerUser();
	}
	@Override
	public List<Object[]> getAllInProcessReqPerUser()
	{
		return reqHeaderRepo.getAllInProcessReqPerUser();
	}
	@Override
		public void dispatchUpdate(EzcRequestHeader ezcRequestHeader) {
			  EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(ezcRequestHeader.getId()).orElseThrow(() -> new EntityNotFoundException());
			  Set<EzcComments> ezComments = ezcRequestHeader.getEzcComments();
			  ezReqHeader.getEzcComments().addAll(ezComments);
			  ezReqHeader.setErhDispatchFlag('Y');
			  ezReqHeader.setErhDispDate(ezcRequestHeader.getErhDispDate());
			  for(EzcComments tempItem : ezComments) { 
				  tempItem.setEzcRequestHeader(ezReqHeader);	
				  commRepo.save(tempItem); 
				}
			  Set<RequestMaterials> matList = ezReqHeader.getRequestMaterials();
			  for(RequestMaterials reqMatObj : matList)
			  {
				  String apprMat = reqMatObj.getMatCode();
				  int apprQty = reqMatObj.getApprQty();
				  
				  MaterialMasterKey matMasterKey=new MaterialMasterKey(apprMat, ezReqHeader.getErhOutStore());
					Optional<MaterialMaster> matMaster = masterRepo.findById(matMasterKey);
					
				  
	              if(matMaster.isPresent())
	              {  
	                     MaterialMaster mat = matMaster.get();
	                     int blockQty = mat.getBlockQty();
	                     if(mat.getBlockQty() != null)
	                  	   blockQty -= apprQty;
	                     int qty = mat.getQuantity();
	                     qty -= apprQty; 
	                     mat.setBlockQty(blockQty);
	                     mat.setQuantity(qty);
	              }
				  
			  }
	}

	@Override
	public Map<String, String> getDashBoardValues(ArrayList<String> role,String user) {
		Map<String, String> htMap = new HashMap<String, String>();
		ArrayList<String> userList = new ArrayList<String>(); 
		userList.add(user);
		if(role.contains("ROLE_OUT_STOR"))
		{
			long dispCnt = reqHeaderRepo.getPendingDispatchCount(user);
			htMap.put("pendDispCnt",dispCnt+"");
		}
		if(role.contains("ROLE_REQ_CR"))
		{
			long dispCnt = reqHeaderRepo.getToAckDispCnt(user);
			htMap.put("ackDispCnt",dispCnt+"");
			ListSelector listSelector = new ListSelector();
			listSelector.setStatus("APPROVED");
			listSelector.setDispStatus('S');
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
			listSelector.setDispStatus('S');
			listSelector.setType("TPS");
			listSelector.setUser(userList);
			dispCnt = customDto.findRequestListCnt(listSelector);
			htMap.put("apprTpsCnt",dispCnt+"");
			listSelector = new ListSelector();
			listSelector.setSentTo(user);
			listSelector.setStatus("C");
			List<EzcMktGiveAway> mktList= customDto.findMktGiveAwayList(listSelector);
			int mkListCnt=0;
			if(mktList != null)
				mkListCnt=mktList.size();
			htMap.put("mkListCnt",mkListCnt+"");
		}
		if(role.contains("ROLE_ST_HEAD"))
		{
			long dispCnt = reqHeaderRepo.getToAckDispCnt(user);
			htMap.put("ackDispCnt",dispCnt+"");
			ListSelector listSelector = new ListSelector();
			listSelector.setStatus("APPROVED");
			listSelector.setDispStatus('S');
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
			listSelector.setDispStatus('S');
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
	@Override
	public List<Object[]> getBDMonthWise(List<String> userList) {
		return reqHeaderRepo.getBDMonthWise(userList);
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
