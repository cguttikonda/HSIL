package com.ezc.hsil.webapp.persistance.dao;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query; 

import com.ezc.hsil.webapp.model.RequestMaterials;

public interface RequestMaterialsRepo extends JpaRepository<RequestMaterials, Integer>{

	Optional<RequestMaterials> findById(Integer id);
	@Query(value = "Select a from RequestMaterials a where a.ezcRequestHeader.id=:reqId")
	Set<RequestMaterials> findByRequest(String reqId);
	
	@Modifying
	@Query(value = "delete from RequestMaterials a where a.ezcRequestHeader.id=:reqId")
	void deleteByRequestId(String reqId);
	
}
