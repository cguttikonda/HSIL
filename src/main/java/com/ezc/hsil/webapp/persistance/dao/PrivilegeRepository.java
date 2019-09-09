package com.ezc.hsil.webapp.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezc.hsil.webapp.model.Privilages;

public interface PrivilegeRepository extends JpaRepository<Privilages, Long> {

	Privilages findByName(String name);

    @Override
    void delete(Privilages privilege);

}
