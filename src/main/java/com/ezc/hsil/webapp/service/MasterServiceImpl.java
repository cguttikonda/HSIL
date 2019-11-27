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
			dm.setCode(distDto.getCode());
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
	public DistributorDto getDistributorDetails(String code) {

		DistributorDto distDto = distMastRepo.distributorDetails(code);

		return distDto;

	}

	@Override
	public String updateDistributor(DistributorDto distDto) throws SQLException {

		String distId = distDto.getCode();


			Optional<DistributorMaster> ODisMaster = distMastRepo.findById(distId);
			ODisMaster.ifPresent(disMaster ->processTheMaster(disMaster,distDto));
			ODisMaster.orElseThrow(()->{ return new SQLException();});

			return "";
	}



	private void processTheMaster(DistributorMaster disMaster,  DistributorDto distDto) {
		// TODO Auto-generated method stub
		
		disMaster.setCode(distDto.getCode());
		disMaster.setName(distDto.getName());
		disMaster.setOrganisation(distDto.getOrganisation());
		disMaster.setContact(distDto.getPhone());
		disMaster.setCity(distDto.getCity());
		
		
	//	return disMaster;
	}

	@Override
	public String deleteDistributor(String code) {


			Optional<DistributorMaster> ODisMaster = distMastRepo.findById(code);
			
			if (ODisMaster.isPresent()) {

				distMastRepo.deleteById(code);
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
	public MaterialDto getMaterialDetails(String materialCode) {
		MaterialDto matDto = matMastRep.materialDetails(materialCode);
		return matDto;

 	}

	@Override
	public String updateMaterial(MaterialDto matDto) throws SQLException {

		String materialCode = matDto.getMaterialCode();

		

			Optional<MaterialMaster> OMatMaster = matMastRep.findById(materialCode);
			OMatMaster.ifPresent(processMaterial(matDto));
			OMatMaster.orElseThrow(()->{ return new SQLException();});
			return "";
	}

	private Consumer<? super MaterialMaster> processMaterial(MaterialDto matDto) {
		return matMaster ->{
			matMaster.setMaterialCode(matDto.getMaterialCode());
			matMaster.setMaterialDesc(matDto.getMaterialDesc());
			matMaster.setQuantity(matDto.getQuantity());
			matMaster.setIsActive(matDto.getIsActive());
			
		};
	}

	@Override
	public List<MaterialMaster> findAllMaterialsLike(String q) {
		return matMastRep.findAllMaterialsLike(q);
	}

	@Override
	public String addDistributorMultiple(List<DistributorMaster> distList) {
		distMastRepo.saveAll(distList);
		return null;
	}
	
	
	

}
