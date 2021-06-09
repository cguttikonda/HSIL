package com.ezc.hsil.webapp.persistance.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezc.hsil.webapp.dto.MaterialDto;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.MaterialMasterKey;

public interface MaterialMasterRepo extends JpaRepository<MaterialMaster, MaterialMasterKey> {

	
	@Query(nativeQuery = true)
	MaterialDto materialDetails(@Param("materialCode") String materialCode,@Param("stockLoc") String stockLoc);

	@Query(nativeQuery = true,value="SELECT * FROM EZC_MATERIAL_MASTER WHERE EMM_MAT_ISACTIVE='Y'")
	List<MaterialMaster> findAllActiveMaterials();
	
	@Query(nativeQuery = true,value="SELECT * FROM EZC_MATERIAL_MASTER WHERE EMM_MAT_DESC LIKE %:desc%")
	List<MaterialMaster> findAllMaterialsLike(@Param("desc") String desc);
	
	
	MaterialMaster findByMaterialCode(@Param("materialCode") String materialCode);

	
}
