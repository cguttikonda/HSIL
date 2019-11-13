package com.ezc.hsil.webapp.persistance.dao;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository; 

import com.ezc.hsil.webapp.model.EzcRequestHeader;

public interface RequestHeaderRepo extends JpaRepository<EzcRequestHeader, Integer> {


	//EzcRequestHeader findById(int i);
	
	Optional<EzcRequestHeader> findById(String string);
	List<EzcRequestHeader> findByErhStatus(String erhStatus);
	List<EzcRequestHeader> findByErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(String erhStatus,Date fromDate,Date toDate);
	List<EzcRequestHeader> findByErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(Date fromDate,Date toDate);
	EzcRequestHeader getById(int i);
	EzcRequestHeader findTopByErhCreatedGroupAndErhRequestedByAndErhStatusOrderByErhConductedOnDesc(String group,String requestedBy,String status);
		

}
