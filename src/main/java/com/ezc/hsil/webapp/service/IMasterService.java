package com.ezc.hsil.webapp.service;

import java.util.List;

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.model.DistributorMaster;

public interface IMasterService {

	
	
	DistributorMaster addNewDistributor(DistributorDto distDto);
	
	List<DistributorMaster> findAll();
	
	
	
	
}
