package com.ezc.hsil.webapp.persistance.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ezc.hsil.webapp.model.UserDefaults;
import com.ezc.hsil.webapp.model.UserDefaultsKey;

public interface UserDefRepository extends JpaRepository<UserDefaults, UserDefaultsKey> {
     
	@Modifying
	@Query("DELETE FROM UserDefaults u WHERE u.userId = ?1")	
	void deleteUserDefaults(Long userId);
}