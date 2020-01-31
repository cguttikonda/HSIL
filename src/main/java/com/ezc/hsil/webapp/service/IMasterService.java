package com.ezc.hsil.webapp.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.dto.MaterialDto;
import com.ezc.hsil.webapp.model.DistributorMaster;
import com.ezc.hsil.webapp.model.EzPlaceMaster;
import com.ezc.hsil.webapp.model.MaterialMaster;

public interface IMasterService {



	DistributorMaster addNewDistributor(DistributorDto distDto);
	List<DistributorMaster> findAll();
	List<MaterialMaster> findMatAll();
	DistributorDto getDistributorDetails(String code);
	String updateDistributor(DistributorDto distDto) throws SQLException;
	String deleteDistributor(String code);
	String addDistributorMultiple(List<DistributorMaster> distList);
	String addMaterialMultiple(List<MaterialMaster> matList);
	
	MaterialMaster addNewMaterial(MaterialDto mDto);
	List<MaterialMaster> getAllMaterials();
	MaterialDto getMaterialDetails(String materialCode);
	String updateMaterial(MaterialDto matDto)  throws SQLException;
	List<MaterialMaster> findAllMaterialsLike(String q);
	List<EzPlaceMaster> findAllCities();
	Map<String,String> checkMaterialStock(String material,int qty);

}
