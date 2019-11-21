package com.ezc.hsil.webapp.persistance.dao;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezc.hsil.webapp.model.EzcRequestHeader;

public interface RequestHeaderRepo extends JpaRepository<EzcRequestHeader, Integer> {


	//EzcRequestHeader findById(int i);
	
	Optional<EzcRequestHeader> findById(String string);
	List<EzcRequestHeader> findByErhStatus(String erhStatus);
	List<EzcRequestHeader> findByErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(String erhStatus,Date fromDate,Date toDate);
	List<EzcRequestHeader> findByErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(Date fromDate,Date toDate);
	List<EzcRequestHeader> findByErhReqTypeAndErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(String erhReqType,String erhStatus,Date fromDate,Date toDate);
	List<EzcRequestHeader> findByErhReqTypeAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(String erhReqType,Date fromDate,Date toDate);
	EzcRequestHeader getById(int i);
	EzcRequestHeader findTopByErhCreatedGroupAndErhRequestedByAndErhStatusOrderByErhConductedOnDesc(String group,String requestedBy,String status);
	@Query(value="select a.id,a.erhDistrubutor,b.matCode,b.matDesc,b.leftOverQty,b.id from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and a.erhRequestedBy=:requestedBy and a.erhStatus='SUBMITTED' and b.leftOverQty > 0")
	List<Object[]> getLeftOverStock(String requestedBy);	

}
