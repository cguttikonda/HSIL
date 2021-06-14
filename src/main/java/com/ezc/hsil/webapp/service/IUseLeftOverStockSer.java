package com.ezc.hsil.webapp.service;
import java.util.List;
import java.util.Set;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.BDRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcRequestHeader;

public interface IUseLeftOverStockSer {

	public void updateLeftOverStock(String reqBy,Integer expAttendee,String requestType);

	
}
