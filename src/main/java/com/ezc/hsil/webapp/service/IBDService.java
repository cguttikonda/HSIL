package com.ezc.hsil.webapp.service;
import java.util.List;
import java.util.Set;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.BDRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;

public interface IBDService {

	public void createBDRequest(EzcRequestHeader ezcRequestHeader);
	public List<EzcRequestHeader> getBDRequestListByDate(ListSelector listSelector);
	public EzcRequestHeader getBDRequest(String docId);
	public void createBDDetails(BDRequestDetailDto bdRequestDetailDto);
}
