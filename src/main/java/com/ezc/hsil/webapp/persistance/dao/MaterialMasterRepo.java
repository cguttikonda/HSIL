package com.ezc.hsil.webapp.persistance.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezc.hsil.webapp.dto.MaterialDto;
import com.ezc.hsil.webapp.model.MaterialMaster;

public interface MaterialMasterRepo extends JpaRepository<MaterialMaster, Integer> {

	
	@Query(nativeQuery = true)
	MaterialDto materialDetails(@Param("id") int id);

	@Query(nativeQuery = true,value="SELECT * FROM EZC_MATERIAL_MASTER WHERE EMM_MAT_ISACTIVE='Y'")
	List<MaterialMaster> findAllActiveMaterials();

	
}
