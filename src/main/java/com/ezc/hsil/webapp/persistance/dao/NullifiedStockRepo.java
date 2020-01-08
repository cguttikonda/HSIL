package com.ezc.hsil.webapp.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezc.hsil.webapp.model.EzNullifiedStock;

public interface NullifiedStockRepo extends JpaRepository<EzNullifiedStock, Integer>{

}
