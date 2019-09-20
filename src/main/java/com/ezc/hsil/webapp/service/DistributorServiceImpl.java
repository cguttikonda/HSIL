package com.ezc.hsil.webapp.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.model.DistributorMaster;
import com.ezc.hsil.webapp.persistance.dao.DistributorMasterRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class DistributorServiceImpl implements IMasterService {

	
	@Autowired
	DistributorMasterRepo distMastRepo;
	
	
	@Override
	public DistributorMaster addNewDistributor(final DistributorDto distDto) {

		
		if(distDto!=null) {
			
			
			DistributorMaster dm = new DistributorMaster();
			
			dm.setName(distDto.getName());
			dm.setContact(distDto.getPhone());
			dm.setOrganisation(distDto.getOrganisation());
			dm.setCity(distDto.getCity());
			
			distMastRepo.save(dm);
			
		}else {
			
			throw new RuntimeException();
		}
		
		return null;
	}


	@Override
	public List<DistributorMaster> findAll() {
	
		
		List<DistributorMaster> distList = new ArrayList<DistributorMaster>();
		
		distList.addAll(distMastRepo.findAll());
		
		return distList;
	}

}
