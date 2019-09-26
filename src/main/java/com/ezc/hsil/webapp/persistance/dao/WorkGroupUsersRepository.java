package com.ezc.hsil.webapp.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.ezc.hsil.webapp.model.WorkGroup_Users;

import java.util.List;

public interface WorkGroupUsersRepository extends JpaRepository<WorkGroup_Users, Long> {

	@Query("SELECT wgu FROM WorkGroup_Users wgu WHERE wgu.groupId = ?1 AND wgu.userId = ?2")	
	WorkGroup_Users findByUserANDGroup(String groupId,String userId);
	
	@Query("SELECT wgu FROM WorkGroup_Users wgu WHERE wgu.userId = ?1")	
	WorkGroup_Users getGroupByUserId(String userId);
	
	@Query("SELECT wgu FROM WorkGroup_Users wgu WHERE wgu.stateGrp = ?1")	
	List<WorkGroup_Users> getStateHeadSubGroups(String stateGrp);
	
	//@Query("SELECT wgu FROM WorkGroup_Users wgu LEFT JOIN Work_Groups wg on wgu.groupId = wg.name WHERE wgu.zonalGrp = ?1 and wg.role = 'REQ_CR'")
	@Query(value = "SELECT wgu.* from EZC_WORKGROUP_USERS wgu, EZC_WORK_GROUPS wg where wgu.EGU_ZONAL_GRP = ?1 AND wgu.EGU_ID = wg.EWG_GROUP AND wg.EWG_ROLE = 'REQ_CR'",nativeQuery = true)
	List<WorkGroup_Users> getZonalHeadSubGroups(String zonalGrp);
	
	@Modifying
	@Query("UPDATE WorkGroup_Users wgu SET wgu.groupId = ?1,wgu.stateGrp = ?3,wgu.zonalGrp = ?4  WHERE wgu.userId = ?2")	
	void updateUserGroups(String groupId,String userId,String stateGrp,final String zonalGrp);
	
	@Modifying
	@Query("DELETE FROM WorkGroup_Users wgu WHERE wgu.userId = ?1")	
	void deleteUserGroups(String userId);
}