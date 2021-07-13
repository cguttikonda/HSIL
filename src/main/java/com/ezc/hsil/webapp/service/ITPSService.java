package com.ezc.hsil.webapp.service;

import java.util.List;
import java.util.Set;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.TpsRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.RequestMaterials;

public interface ITPSService {
	
	public EzcRequestHeader createTPSRequest(EzcRequestHeader ezcRequestHeader);
	public List<EzcRequestHeader> getTPSRequestListByDate(ListSelector listSelector);
	public List<EzcComments> getTPSCommentRequest(String docId);
	public void approveTPSRequest(EzcRequestHeader ezcRequestHeader);
	public EzcRequestHeader getTPSRequest(String docId);
	public void rejectTPSRequest(EzcRequestHeader ezcRequestHeader);
	public void submitTPSDetails(TpsRequestDetailDto tpsRequestDetailDto);
	public void saveTPSDetails(TpsRequestDetailDto tpsRequestDetailDto);
	public Set<RequestMaterials> getLastRequestDet(String requestedBy);
	public List<Object[]> getLeftOverStock(String requestedBy,String requestType);
	public void NullifyTpsQty(String leftOverId,String reasonNullify,String commentsNullify);
	List<EzcRequestHeader> pendingRequests(String userId);
	List<Object[]> getAvailableStock(String requestedBy, String requestType);
	
}
