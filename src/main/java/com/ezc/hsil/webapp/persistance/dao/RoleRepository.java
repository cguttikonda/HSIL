package com.ezc.hsil.webapp.persistance.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ezc.hsil.webapp.model.Roles;

public interface RoleRepository extends JpaRepository<Roles, Long> {

    Roles findByName(String name);

    @Override
    void delete(Roles role);

}
