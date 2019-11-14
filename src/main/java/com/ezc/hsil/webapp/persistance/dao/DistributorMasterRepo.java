/**
 * 
 */
package com.ezc.hsil.webapp.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezc.hsil.webapp.dto.DistributorDto;
import com.ezc.hsil.webapp.model.DistributorMaster;

/**
 * @author cguttikonda
 *
 */
public interface DistributorMasterRepo extends JpaRepository<DistributorMaster, String> {
	
	
	
	@Query(nativeQuery = true)
	DistributorDto distributorDetails(@Param("code") String code);
	
	

}
