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
/*	@Query(value="select a.id,a.erhDistrubutor,b.matCode,b.matDesc,b.apprQty,b.leftOverQty,b.id from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and a.erhStatus=:erhStatus and a.erhReqType=:erhReqType")
	List<Object[]> getBdRequestList(String erhStatus,String erhReqType);	
	@Query(value="select a.id,a.erhDistrubutor,b.matCode,b.matDesc,b.apprQty,b.leftOverQty,b.id,a.erhStatus from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and  a.erhReqType=:erhReqType and erhRequestedOn>=:fromdate  AND erhRequestedOn <=:todate")
	List<Object[]> getBdALLRequestList(String erhReqType,Date fromdate,Date todate);	*/
	@Query(value="select a.id,a.erhReqType,a.erhDistrubutor,a.erhRequestedBy,b.matCode,b.matDesc,b.apprQty from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and a.erhStatus<>'NEW' and (a.erhDispatchFlag IS NULL or a.erhDispatchFlag='')")
	List<Object[]> getPendingDispatchDetails();
	@Query(value="select count(a.id) from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id  and (a.erhDispatchFlag IS NULL or a.erhDispatchFlag='')")
	Long getPendingDispatchCount();
	@Query(value="select a.erhRequestedBy,a.erhDistrubutor,b.matCode,b.matDesc,SUM(b.leftOverQty) from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id  and a.erhStatus='SUBMITTED' and a.erhReqType in ('TPM','TPS') group by a.erhRequestedBy,a.erhDistrubutor,b.matCode,b.matDesc")
	List<Object[]> getStockAvailabilityForAll();
	@Query(value="select a.id,a.erhReqType,a.erhDistrubutor,a.erhRequestedBy,b.matCode,b.matDesc,b.apprQty from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id  and a.erhDispatchFlag='Y' and a.erhRequestedBy=:requestedBy")
	List<Object[]> getToAckDispReport(String requestedBy);
	@Query(value="select count(a.id) from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id  and a.erhDispatchFlag='Y' and a.erhRequestedBy=:requestedBy")
	Long getToAckDispCnt(String requestedBy);
	@Query(value="select a.erhRequestedBy,a.erhDistrubutor,b.matCode,b.matDesc,SUM(b.leftOverQty) from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id  and a.erhStatus='SUBMITTED' and a.erhReqType in ('TPM','TPS') and a.erhRequestedBy in :userList group by a.erhRequestedBy,a.erhDistrubutor,b.matCode,b.matDesc")
	List<Object[]> getStockAvailabilityByUser(List<String> userList);
	@Query(value="select b.eriPlumberName,a.erhState,b.eriDob,b.eriDoa,a.erhDistrubutor from EzcRequestHeader a,EzcRequestItems b where a.id=b.ezcRequestHeader.id  and a.erhDistrubutor in :distList")
	List<Object[]> getPlumberMaster(List<String> distList);
	@Query(value="SELECT MONTHNAME(ERH_REQUESTED_ON) as mon, COUNT(*),SUM(CASE WHEN ERH_STATUS='SUBMITTED' THEN 1 ELSE 0 END) AS SUBMITTED FROM ezc_request_header  where ERH_REQ_TYPE='TPM' GROUP BY mon",nativeQuery = true)
	List<Object[]> getTPMMonthWise();
	
}
                             