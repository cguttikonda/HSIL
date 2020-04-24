package com.ezc.hsil.webapp.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.ezc.hsil.webapp.dto.PlaceMasterDto;
import com.ezc.hsil.webapp.model.EzPlaceMaster;

public interface PlaceMasterRepo extends JpaRepository<EzPlaceMaster, Integer>{

	
	@Query(nativeQuery = true)
	PlaceMasterDto cityDetails(@Param("city") String city);

}
