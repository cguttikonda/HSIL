package com.ezc.hsil.webapp.persistance.dao;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
	@Query(value="select a.id,a.erhDistrubutor,b.matCode,b.matDesc,b.leftOverQty,b.id,a.erhDistName from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and a.erhRequestedBy=:requestedBy and a.erhReqType=:requestType  and b.leftOverQty > 0 ORDER BY b.id")
	List<Object[]> getLeftOverStock(String requestedBy,String requestType);
	@Query(value="select a.id,a.erhDistrubutor,b.matCode,b.matDesc,b.apprQty-b.usedQty,b.id,a.erhDistName,a.erhRequestedOn from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and a.erhRequestedBy=:requestedBy and a.erhStatus IN ('SUBMITTED','APPROVED') and b.apprQty-b.usedQty > 0 ORDER BY b.id")
	List<Object[]> getAllStock(String requestedBy);
/*	@Query(value="select a.id,a.erhDistrubutor,b.matCode,b.matDesc,b.apprQty,b.leftOverQty,b.id from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and a.erhStatus=:erhStatus and a.erhReqType=:erhReqType")
	List<Object[]> getBdRequestList(String erhStatus,String erhReqType);	
	@Query(value="select a.id,a.erhDistrubutor,b.matCode,b.matDesc,b.apprQty,b.leftOverQty,b.id,a.erhStatus from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and  a.erhReqType=:erhReqType and erhRequestedOn>=:fromdate  AND erhRequestedOn <=:todate")
	List<Object[]> getBdALLRequestList(String erhReqType,Date fromdate,Date todate);	*/ 
	//@Query(value="select a.id,a.erhReqType,a.erhDistrubutor,a.erhRequestedBy,b.matCode,b.matDesc,b.apprQty,a.erhDistName,a.erhState,a.erhCity,a.erhReqName from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and a.erhStatus<>'NEW' and b.isNew='Y' and (a.erhDispatchFlag IS NULL or a.erhDispatchFlag='') and a.erhOutStore=:userId")
	@Query(value="select a.id,a.erhReqType,a.erhDistrubutor,a.erhRequestedBy,b.matCode,b.matDesc,b.apprQty,a.erhDistName,a.erhState,a.erhCity,a.erhReqName from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and a.erhStatus<>'NEW' and b.isNew='Y' and (a.erhDispatchFlag IS NULL or a.erhDispatchFlag='') and a.erhOutStore IN (select value from UserDefaults where userId IN (select id from Users where userId=:userId) and key='STORE')")
	List<Object[]> getPendingDispatchDetails(String userId);
	//@Query(value="select count(a.id) from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id  and b.isNew='Y' and (a.erhDispatchFlag IS NULL or a.erhDispatchFlag='') and a.erhOutStore=:userId")
	@Query(value="select count(a.id) from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and a.erhStatus<>'NEW' and b.isNew='Y' and (a.erhDispatchFlag IS NULL or a.erhDispatchFlag='') and a.erhOutStore IN (select value from UserDefaults where userId IN (select id from Users where userId=:userId) and key='STORE')")
	Long getPendingDispatchCount(String userId);
	@Query(value="select a.erhRequestedBy,a.erhDistrubutor,b.matCode,b.matDesc,SUM(b.apprQty-b.usedQty),a.erhDistName,a.erhReqName,a.erhReqType from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id  and a.erhStatus IN ('SUBMITTED','APPROVED') and b.apprQty-b.usedQty > 0  group by a.erhRequestedBy,a.erhDistrubutor,a.erhDistName,b.matCode,b.matDesc")
	List<Object[]> getStockAvailabilityForAll();
	@Query(value="select a.id,a.erhReqType,a.erhDistrubutor,a.erhRequestedBy,b.matCode,b.matDesc,b.apprQty,a.erhDistName from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id  and a.erhDispatchFlag='Y' and b.isNew='Y' and a.erhRequestedBy=:requestedBy")
	List<Object[]> getToAckDispReport(String requestedBy);
	@Query(value="select count(a.id) from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id  and a.erhDispatchFlag='Y' and b.isNew='Y' and a.erhRequestedBy=:requestedBy")
	Long getToAckDispCnt(String requestedBy);
	@Query(value="select a.erhRequestedBy,a.erhDistrubutor,b.matCode,b.matDesc,SUM(b.apprQty-b.usedQty),a.erhDistName,a.erhReqName,a.erhReqType from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id  and a.erhStatus  IN ('SUBMITTED','APPROVED')  and a.erhRequestedBy in :userList and b.apprQty-b.usedQty > 0 group by a.erhRequestedBy,a.erhDistrubutor,b.matCode,b.matDesc")
	List<Object[]> getStockAvailabilityByUser(List<String> userList);
	@Query(value="select b.eriPlumberName,a.erhState,b.eriDob,b.eriDoa,a.erhDistrubutor from EzcRequestHeader a,EzcRequestItems b where a.id=b.ezcRequestHeader.id  and a.erhDistrubutor in :distList")
	List<Object[]> getPlumberMaster(List<String> distList);
	@Query(nativeQuery = true,value="SELECT MONTHNAME(ERH_REQUESTED_ON) as mon, COUNT(*),SUM(CASE WHEN ERH_STATUS='SUBMITTED' THEN 1 ELSE 0 END) AS SUBMITTED FROM ezc_request_header   where ERH_REQ_TYPE='TPM' and erh_requested_by in :userList")
	List<Object[]> getTPMMonthWise(List<String> userList);
	//@Query(value="SELECT MONTHNAME(ERH_REQUESTED_ON) as mon, COUNT(*),SUM(CASE WHEN ERH_STATUS='SUBMITTED' THEN 1 ELSE 0 END) AS SUBMITTED FROM ezc_request_header  where ERH_REQ_TYPE='TPM' and ERH_REQUESTED_BY in (SELECT USER_ID FROM EZC_USERS WHERE ID IN(  select user_id from users_roles where role_id in (select id from ezc_roles where name='ROLE_REQ_CR'))) GROUP BY mon",nativeQuery = true)
	//@Query(value="SELECT MONTHNAME(a.erhRequestedOn) as mon, COUNT(*),SUM(CASE WHEN a.erhStatus='SUBMITTED' THEN 1 ELSE 0 END) AS SUBMITTED FROM ezc_request_header a  where a.erhReqType='TPM' and ERH_REQUESTED_BY in :userList group by mon")
	
	@Query(nativeQuery = true,value="SELECT MONTHNAME(ERH_REQUESTED_ON) as mon, COUNT(*),SUM(CASE WHEN ERH_STATUS='SUBMITTED' THEN 1 ELSE 0 END) AS SUBMITTED FROM ezc_request_header   where ERH_REQ_TYPE='TPS' and erh_requested_by in :userList")
	List<Object[]> getTPSMonthWise(List<String> userList);
	@Query(nativeQuery = true,value="SELECT MONTHNAME(ERH_REQUESTED_ON) as mon, COUNT(*),SUM(CASE WHEN ERH_STATUS='SUBMITTED' THEN 1 ELSE 0 END) AS SUBMITTED FROM ezc_request_header   where ERH_REQ_TYPE='BD' and erh_requested_by in :userList")
	List<Object[]> getBDMonthWise(List<String> userList);
	@Query(value="select a.id,a.erhDistrubutor,b.matCode,b.matDesc,b.leftOverQty,b.id,a.erhDistName from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and a.erhReqType='BD'  and b.leftOverQty > 0 ORDER BY b.id")
	List<Object[]> getBDLeftOverStock();
	
	@Query(value="select b.erdMeetId,b.erdMeetDate,b.erdDealerName,a.eriPlumberName,a.eriContact,a.eriDob,a.eriDoa,b.erdCostIncured from EzcRequestItems a,EzcRequestDealers b where a.eriDealerId=b.id and a.ezcRequestHeader.id = :docId")
	List<Object[]> getMeetDetailsById(String docId);
	@Query(value="select a.id,a.erhDistrubutor,b.matCode,b.matDesc,b.leftOverQty,b.id,a.erhDistName from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id  and a.erhStatus='SUBMITTED' and b.leftOverQty > 0 and b.matCode= :matCode and a.erhRequestedBy= :reqBy")
	List<Object[]> UseLeftOverStockSer(String reqBy, String matCode);
	
	//@Query(value="SELECT MONTHNAME(erh_requested_on) as MON , COUNT(*) ,ERH_REQ_TYPE,erh_requested_by,SUM(CASE WHEN ERH_STATUS='SUBMITTED' THEN 1 ELSE 0 END) AS SUBMITTED FROM ezc_request_header WHERE erh_requested_on >= NOW() - INTERVAL 1 YEAR GROUP BY MONTH(erh_requested_on),ERH_REQ_TYPE,erh_requested_by")
	@Query(nativeQuery = true,value="SELECT Month(erh_requested_on) as MON , COUNT(*) ,ERH_REQ_TYPE,erh_requested_by,SUM(CASE WHEN ERH_STATUS='SUBMITTED' THEN 1 ELSE 0 END) AS SUBMITTED FROM ezc_request_header  GROUP BY MONTH(erh_requested_on),ERH_REQ_TYPE,erh_requested_by")
	List<Object[]> getAllReqMonthWise();
	//@Query(nativeQuery = true,value="select Month(erd_meet_date) , COUNT(erd_meet_id),SUM(ERD_ATTENDANCE),erd_req_id from ezc_request_dealers  WHERE  erd_meet_sts='COMPLETED' and erd_meet_date >= NOW() - INTERVAL 1 YEAR group by MONTH(erd_meet_date),erd_req_id")
	@Query(nativeQuery = true,value="select ERH_REQ_TYPE , COUNT(erd_meet_id),SUM(ERD_ATTENDANCE),(SELECT erh_requested_by from ezc_request_header where id=erd_req_id) from ezc_request_dealers a,ezc_request_header b WHERE  a.erd_req_id=b.id  and erd_meet_sts='COMPLETED' and erd_meet_date >= NOW() - INTERVAL 1 YEAR group by erd_req_id")
	List<Object[]> getAllMeetsMonthWise();
	
	@Query(nativeQuery = true,value="select a.user_id,a.first_name,b.egu_user_id,(select ewg_group_desc from ezc_work_groups where EGU_ZONAL_GRP=ewg_group) as  EGU_ZONAL_GRP,(select ewg_group_desc from ezc_work_groups where egu_id=ewg_group) as grp from ezc_users a,ezc_workgroup_users b   where a.user_id=b.egu_user_id and a.id IN(  select user_id from users_roles where role_id in (select id from ezc_roles where name IN ('ROLE_REQ_CR','ROLE_ST_HEAD','ROLE_BD_MKT')))")
	//@Query(nativeQuery=true,value="select a.user_id,a.first_name,b.egu_user_id,(select ewg_group_desc from ezc_work_groups where EGU_ZONAL_GRP=ewg_group) as  EGU_ZONAL_GRP,(select ewg_group_desc from ezc_work_groups where egu_id=ewg_group) as grp from ezc_users a,ezc_workgroup_users b   where a.user_id=b.egu_user_id ")
	List<Object[]> getUserDefaults();
	@Query(nativeQuery = true,value="select COUNT(eri_plumber_name),(SELECT erh_requested_by from ezc_request_header where id=eri_req_id) emp,ERH_REQ_TYPE from ezc_request_items a,ezc_request_header b where a.eri_req_id=b.id and b.erh_status='SUBMITTED' and b.ERH_REQ_TYPE!='BD' group by eri_req_id")
	List<Object[]> getNoofPlumbersPerUser();
	//@Query(nativeQuery = true,value="select SUM(erm_used_qty), SUM(erm_leftover_qty),(SELECT erh_requested_by from ezc_request_header where id=erm_req_id) emp,ERH_REQ_TYPE from ezc_request_materials a,ezc_request_header b where a.erm_req_id=b.id and b.erh_status='SUBMITTED' and b.ERH_REQ_TYPE!='BD' group by erm_req_id")
	@Query(nativeQuery=true,value="select erh_status,SUM(erm_appr_qty), SUM(erm_leftover_qty),(SELECT erh_requested_by from ezc_request_header where id=erm_req_id) emp,ERH_REQ_TYPE from ezc_request_materials a,ezc_request_header b where a.erm_req_id=b.id and b.erh_status in('SUBMITTED','APPROVED') and b.ERH_REQ_TYPE!='BD' group by erm_req_id,erh_status")
	List<Object[]> getUsedLeftQtyPerUser();
	@Query(nativeQuery=true,value="SELECT  COUNT(*) ,ERH_REQ_TYPE,erh_requested_by,SUM(CASE WHEN ERH_STATUS!='SUBMITTED' THEN 1 ELSE 0 END) AS INPROCESS FROM ezc_request_header WHERE erh_requested_on >= NOW() - INTERVAL 1 YEAR GROUP BY ERH_REQ_TYPE,erh_requested_by")
	List<Object[]> getAllInProcessReqPerUser();
	@Query(value="select a.id,a.erhRequestedBy,a.erhDistrubutor,b.matCode,b.matDesc,b.id,b.apprQty,a.erhDistName from EzcRequestHeader a,RequestMaterials b where a.id=b.ezcRequestHeader.id and a.erhReqType=:reqType  and a.erhRequestedBy=:reqBY and a.erhStatus='APPROVED' ORDER BY b.id")
	List<Object[]> getPendingList(String reqBY, String reqType);
	
	@Query(value="select b.eriPlumberName,a.erhState,b.eriDob,b.eriDoa,a.erhDistrubutor,a.erhDistName,a.erhReqType from EzcRequestHeader a,EzcRequestItems b where a.id=b.ezcRequestHeader.id and a.erhReqType IN ('TPM','TPS','BD')")
	List<Object[]> getPlumberList();
}
                             