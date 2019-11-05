package com.ezc.hsil.webapp.persistance.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezc.hsil.webapp.model.RequestMaterials;

public interface RequestMaterialsRepo extends JpaRepository<RequestMaterials, Integer>{

	Optional<RequestMaterials> findById(Integer id);
}
