package com.ezc.hsil.webapp.persistance.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezc.hsil.webapp.model.EzcRequestHeader;

public interface RequestHeaderRepo extends JpaRepository<EzcRequestHeader, Integer> {


	//EzcRequestHeader findById(int i);
	
	Optional<EzcRequestHeader> findById(int i);
	
	EzcRequestHeader getById(int i);
		

}
