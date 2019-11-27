package com.ezc.hsil.webapp.service;

import java.sql.SQLException;
import java.util.List;

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.dto.MaterialDto;
import com.ezc.hsil.webapp.model.DistributorMaster;
import com.ezc.hsil.webapp.model.MaterialMaster;

public interface IMasterService {



	DistributorMaster addNewDistributor(DistributorDto distDto);
	List<DistributorMaster> findAll();
	DistributorDto getDistributorDetails(String code);
	String updateDistributor(DistributorDto distDto) throws SQLException;
	String deleteDistributor(String code);
	String addDistributorMultiple(List<DistributorMaster> distList);
	
	MaterialMaster addNewMaterial(MaterialDto mDto);
	List<MaterialMaster> getAllMaterials();
	MaterialDto getMaterialDetails(String materialCode);
	String updateMaterial(MaterialDto matDto)  throws SQLException;
	List<MaterialMaster> findAllMaterialsLike(String q);


}
