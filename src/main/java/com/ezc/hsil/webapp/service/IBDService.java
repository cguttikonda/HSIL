package com.ezc.hsil.webapp.service;
import java.util.List;
import java.util.Set;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.BDRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcRequestHeader;

public interface IBDService {

	public EzcRequestHeader createBDRequest(EzcRequestHeader ezcRequestHeader);
	public List<EzcRequestHeader> getBDRequestListByDate(ListSelector listSelector);
	public EzcRequestHeader getBDRequest(String docId);
	public void submitBDDetails(BDRequestDetailDto bdRequestDetailDto);
	public void saveBDDetails(BDRequestDetailDto bdRequestDetailDto);
	public void submitBDDet(String id,String appQty,EzcRequestHeader ezcRequestHeader) throws Exception;
	public List<EzcComments> getBDCommentRequest(String docId);
	public List<Object[]> getBDLeftOverStock(String reqBy);
	public void NullifyBDQty(String leftOverId,String reasonNullify,String commentsNullify);
	public void rejectBDRequest(EzcRequestHeader ezcRequestHeader);
	public List<Object[]>  getPendingList(String requestBY,String reqType);
	List<EzcRequestHeader> pendingRequests(String userId);
	List<Object[]> getAvailableStock(String requestedBy, String requestType);
	
	
}
