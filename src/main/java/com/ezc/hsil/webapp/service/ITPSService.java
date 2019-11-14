package com.ezc.hsil.webapp.service;

import java.util.List;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.TpsRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;

public interface ITPSService {
	
	public void createTPSRequest(EzcRequestHeader ezcRequestHeader);
	public List<EzcRequestHeader> getTPSRequestListByDate(ListSelector listSelector);
	public void approveTPSRequest(EzcRequestHeader ezcRequestHeader);
	public EzcRequestHeader getTPSRequest(String docId);
	public void createTPSDetails(TpsRequestDetailDto tpsRequestDetailDto);

}
