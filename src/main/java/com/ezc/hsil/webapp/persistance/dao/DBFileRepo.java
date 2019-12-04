package com.ezc.hsil.webapp.persistance.dao;

import com.ezc.hsil.webapp.model.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBFileRepo extends JpaRepository<DBFile, String> {

}
