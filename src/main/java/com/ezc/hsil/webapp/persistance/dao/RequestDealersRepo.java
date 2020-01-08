package com.ezc.hsil.webapp.persistance.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezc.hsil.webapp.model.EzcRequestDealers;

public interface RequestDealersRepo extends JpaRepository<EzcRequestDealers, Integer>{
	
	@Query(value="Select a from EzcRequestDealers a where a.ezcRequestHeader.id=:reqId")
	Set<EzcRequestDealers> findByRequest(String reqId);

	
	
	
	

}
