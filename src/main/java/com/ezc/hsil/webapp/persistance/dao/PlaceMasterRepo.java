package com.ezc.hsil.webapp.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezc.hsil.webapp.model.EzPlaceMaster;

public interface PlaceMasterRepo extends JpaRepository<EzPlaceMaster, Integer>{

}
