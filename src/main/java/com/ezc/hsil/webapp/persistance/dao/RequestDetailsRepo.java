package com.ezc.hsil.webapp.persistance.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;

public interface RequestDetailsRepo extends JpaRepository<EzcRequestItems, Integer>{

	//void save(Set<EzcRequestItems> ezcRequestItemses);

	//void save(EzcRequestHeader reqHeader);
	List<EzcRequestItems> findById(int id);
	
}
