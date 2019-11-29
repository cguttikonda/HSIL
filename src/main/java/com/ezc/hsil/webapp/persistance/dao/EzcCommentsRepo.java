package com.ezc.hsil.webapp.persistance.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezc.hsil.webapp.model.EzcComments;

public interface EzcCommentsRepo extends JpaRepository<EzcComments, Integer>
{
	Optional<EzcComments> findById(Integer id);
	@Query(value = "Select a from EzcComments a where a.ezcRequestHeader.id=:reqId")
	List<EzcComments> findByRequest(String reqId);
}
