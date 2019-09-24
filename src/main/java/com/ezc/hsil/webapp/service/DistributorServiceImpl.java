package com.ezc.hsil.webapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.error.RequestNotFound;
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

		if (distDto != null) {

			DistributorMaster dm = new DistributorMaster();

			dm.setName(distDto.getName());
			dm.setContact(distDto.getPhone());
			dm.setOrganisation(distDto.getOrganisation());
			dm.setCity(distDto.getCity());

			distMastRepo.save(dm);

		} else {

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

	@Override
	public DistributorDto getDistributorDetails(int id) {

		DistributorDto distDto = distMastRepo.distributorDetails(id);

		return distDto;

	}

	@Override
	public String updateDistributor(DistributorDto distDto) throws SQLException {

		int distId = distDto.getId();

		if (distId > 0) {

			Optional<DistributorMaster> ODisMaster = distMastRepo.findById(distId);
			ODisMaster.ifPresent(disMaster ->processTheMaster(disMaster,distDto));
			ODisMaster.orElseThrow(()->{ return new SQLException();});
		} else {

			log.error("Unable to find the Distributor id {}", distId);
			throw new RequestNotFound("Distributor not found for the id " + distId);

		}

		return "";
	}



	private void processTheMaster(DistributorMaster disMaster,  DistributorDto distDto) {
		// TODO Auto-generated method stub
		
		disMaster.setId(distDto.getId());
		disMaster.setName(distDto.getName());
		disMaster.setOrganisation(distDto.getOrganisation());
		disMaster.setContact(distDto.getPhone());
		disMaster.setCity(distDto.getCity());
		
		
	//	return disMaster;
	}

	@Override
	public String deleteDistributor(int id) {

		if (id > 0) {

			Optional<DistributorMaster> ODisMaster = distMastRepo.findById(id);

			if (ODisMaster.isPresent()) {

				distMastRepo.deleteById(id);
			}

		} else {

			log.error("Unable to find the request id {}", id);
			throw new RequestNotFound("Distributor not found for the id " + id);
		}

		return "";
	}
	
	
	

}
