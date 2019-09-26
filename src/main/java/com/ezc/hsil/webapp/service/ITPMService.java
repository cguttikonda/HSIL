package com.ezc.hsil.webapp.service;

import java.util.List;
import java.util.Optional;

import com.ezc.hsil.webapp.dto.TpmRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;

public interface ITPMService {

	public void createTPMDetails(TpmRequestDetailDto tpmRequestDetailDto);
	public void createTPMRequest(EzcRequestHeader ezcRequestHeader);
	public List<EzcRequestHeader> getTPMRequestList(String status);
	public EzcRequestHeader getTPMRequest(int docId);
}
