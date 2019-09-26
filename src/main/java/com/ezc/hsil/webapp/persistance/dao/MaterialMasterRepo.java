package com.ezc.hsil.webapp.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezc.hsil.webapp.model.MaterialMaster;

public interface MaterialMasterRepo extends JpaRepository<MaterialMaster, Integer> {

}
