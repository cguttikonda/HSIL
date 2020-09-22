package com.ezc.hsil.webapp.persistance.dao;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezc.hsil.webapp.model.EzcMktGiveAway;

public interface MktgGiveAwayRepo extends JpaRepository<EzcMktGiveAway, Integer> {


	//EzcMktGiveAway findById(int i);
	
	Optional<EzcMktGiveAway> findById(String string);
		
}
                             