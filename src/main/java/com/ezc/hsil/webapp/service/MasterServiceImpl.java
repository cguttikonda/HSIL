package com.ezc.hsil.webapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.dto.MaterialDto;
import com.ezc.hsil.webapp.model.DistributorMaster;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.persistance.dao.DistributorMasterRepo;
import com.ezc.hsil.webapp.persistance.dao.MaterialMasterRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class MasterServiceImpl implements IMasterService {

	@Autowired
	DistributorMasterRepo distMastRepo;
	
	@Autowired
	MaterialMasterRepo matMastRep;

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
			throw new EntityNotFoundException("Distributor not found for the id " + distId);

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
			throw new EntityNotFoundException("Distributor not found for the id " + id);
		}

		return "";
	}

	@Override
	public MaterialMaster addNewMaterial(final MaterialDto mDto) {

		if(mDto!=null) {
			
			MaterialMaster mm = new MaterialMaster(); 
			mm.setMaterialCode(mDto.getMaterialCode());
			mm.setMaterialDesc(mDto.getMaterialDesc());
			mm.setQuantity(mDto.getQuantity());
			mm.setIsActive("Y");
			
			matMastRep.save(mm);	
			
		}else
		{
			
			throw new RuntimeException();
		}
		
		
		return null;
	}

	@Override
	public List<MaterialMaster> getAllMaterials() {
		return matMastRep.findAllActiveMaterials();		
	}

	@Override
	public MaterialDto getMaterialDetails(int id) {
		MaterialDto matDto = matMastRep.materialDetails(id);
		return matDto;

 	}

	@Override
	public String updateMaterial(MaterialDto matDto) throws SQLException {

		int matId = matDto.getId();

		if (matId > 0) {

			Optional<MaterialMaster> OMatMaster = matMastRep.findById(matId);
			OMatMaster.ifPresent(processMaterial(matDto, matId));
			OMatMaster.orElseThrow(()->{ return new SQLException();});
		} else {

			log.error("Unable to find the Material id {}", matId);
			throw new EntityNotFoundException("Material not found for the id " + matId);

		}

		return "";
	}

	private Consumer<? super MaterialMaster> processMaterial(MaterialDto matDto, int matId) {
		return matMaster ->{
			matMaster.setId(matId);
			matMaster.setMaterialCode(matDto.getMaterialCode());
			matMaster.setMaterialDesc(matDto.getMaterialDesc());
			matMaster.setQuantity(matDto.getQuantity());
			matMaster.setIsActive(matDto.getIsActive());
			
		};
	}
	
	
	

}
