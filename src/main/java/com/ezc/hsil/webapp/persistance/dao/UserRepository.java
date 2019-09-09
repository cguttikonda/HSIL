package com.ezc.hsil.webapp.persistance.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ezc.hsil.webapp.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    
    Users findByUserId(String userId);
    
    Users findByUserIdOrEmail(String userId, String email);
    
    
//    https://www.baeldung.com/spring-data-derived-queries

    @Override
    void delete(Users user);

}