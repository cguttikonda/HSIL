package com.ezc.hsil.webapp.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.dto.MaterialDto;
import com.ezc.hsil.webapp.dto.PlaceMasterDto;
import com.ezc.hsil.webapp.dto.StockLocationDto;
import com.ezc.hsil.webapp.model.DistributorMaster;
import com.ezc.hsil.webapp.model.EzPlaceMaster;
import com.ezc.hsil.webapp.model.EzStores;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.EzPlaceMaster;

public interface IMasterService {



	DistributorMaster addNewDistributor(DistributorDto distDto);
	List<DistributorMaster> findAll();
	List<DistributorMaster> findAllByCat(List<String> catList);
	List<MaterialMaster> findMatAll();
	DistributorDto getDistributorDetails(String code);
	String updateDistributor(DistributorDto distDto) throws SQLException;
	String deleteDistributor(String code);
	String addDistributorMultiple(List<DistributorMaster> distList);
	String addMaterialMultiple(List<MaterialMaster> matList);
	
	MaterialMaster addNewMaterial(MaterialDto mDto);
	List<MaterialMaster> getAllMaterials();
	MaterialDto getMaterialDetails(String materialCode,String stockLoc);
	String updateMaterial(MaterialDto matDto)  throws SQLException;
	List<MaterialMaster> findAllMaterialsLike(String q);
	List<EzPlaceMaster> findAllCities();
	Map<String,String> checkMaterialStock(String material,String stockLoc,int qty);
	boolean findById(String matCode,String stockLoc);
	String deleteMaterial(String code,String stockLoc);
	//List<EzPlaceMaster> getAllPlaceData();
	PlaceMasterDto addNewCity(PlaceMasterDto pDto);
	String deleteCity(int id);
	PlaceMasterDto getCityDetails(String city);
	
	EzStores addStore(StockLocationDto stockLocationDto);
	EzStores findStoreById(String locationId);
	List<EzStores> listStores();
	boolean transferStock(String fromLocation,String toLocation,String material,int qty);
	boolean reduceStock(String store,String material,int qty);
	List<Object[]> getPlumberList();
	
}
