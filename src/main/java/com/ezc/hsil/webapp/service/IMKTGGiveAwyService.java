package com.ezc.hsil.webapp.service;

import java.util.List;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.MktgGiveAwayDto;
import com.ezc.hsil.webapp.model.EzcMktGiveAway;

public interface IMKTGGiveAwyService {

	public void createMKTData(MktgGiveAwayDto mktgGiveAwayDto) throws Exception; 
	public List<EzcMktGiveAway> getMKTGListByDate(ListSelector listSelector);
	public List<EzcMktGiveAway> getRequestList(ListSelector listSelector);
	public void acknowledgeRequest(Integer id,String ackBy,String ackComments);
	public EzcMktGiveAway getRequestDetails(Integer id);
}
