package com.ezc.hsil.webapp.service;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestParam;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.TpmRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.RequestMaterials;

public interface ITPMService {

	public void submitTPMDetails(TpmRequestDetailDto tpmRequestDetailDto);
	public void saveTPMDetails(TpmRequestDetailDto tpmRequestDetailDto);
	public EzcRequestHeader createTPMRequest(EzcRequestHeader ezcRequestHeader);
	public List<EzcComments> getTPMCommentRequest(String docId);
	public List<EzcRequestHeader> getTPMRequestList(String status); 
	public List<EzcRequestHeader> getTPMRequestListByDate(ListSelector listSelector);
	public EzcRequestHeader getTPMRequest(String docId);
	public void approveTPMRequest(EzcRequestHeader ezcRequestHeader);
	public void rejectTPMRequest(EzcRequestHeader ezcRequestHeader);
	public void closeTPMRequest(TpmRequestDetailDto tpmRequestDetailDto);
	public Set<RequestMaterials> getLastRequestDet(String requestedBy);
	public List<Object[]> getLeftOverStock(String requestedBy,String requestType);
	public List<Object[]> getTPMRequestList(ListSelector listSelector);
	public void NullifyTpmQty(String leftOverId,String reasonNullify,String commentsNullify);
	public List<Object[]> getMeetDetailsById(String docId);
	List<Object[]> getAllStock(String requestedBy);
	List<Object[]> pendingRequests(String userId);
	List<Object[]> getAvailableStock(String requestedBy, String requestType);
}
