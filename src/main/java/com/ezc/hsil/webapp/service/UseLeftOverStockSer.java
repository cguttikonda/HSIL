package com.ezc.hsil.webapp.service;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.RequestCustomDto;
import com.ezc.hsil.webapp.dto.BDRequestDetailDto;
import com.ezc.hsil.webapp.model.EzNullifiedStock;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.persistance.dao.EzcCommentsRepo;
import com.ezc.hsil.webapp.persistance.dao.MaterialMasterRepo;
import com.ezc.hsil.webapp.persistance.dao.NullifiedStockRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestDetailsRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestHeaderRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestMaterialsRepo;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@Transactional

public class UseLeftOverStockSer  implements IUseLeftOverStockSer{
	
	
	 @Autowired
	    private RequestHeaderRepo reqHeaderRepo;
	 
	 @Autowired
	 private RequestDetailsRepo reqDealRep;
	 @Autowired
	 MaterialMasterRepo masterRepo; 
 @Autowired
 private RequestCustomDto requestCustomDto;
 @Autowired
	 private EzcCommentsRepo commRepo;
 @Autowired
 private RequestMaterialsRepo reqMatRep;
 @Autowired
	 IBDService bdService;
	 
 @Autowired
 private NullifiedStockRepo nullifiedStockRepo;


	
	@Override
	public void updateLeftOverStock(String reqBy,Integer expAttendee,String requestType) {
			int usedQuant=0;
		   if(expAttendee>0)
		   {
			   List<Object[]> leftOverStkList=reqHeaderRepo.getLeftOverStock(reqBy,requestType);
			  
			   for(int i = 0; i < leftOverStkList.size(); i++)
			   {
				 int leftOverStk=(Integer)leftOverStkList.get(i)[4];
				 String matCode=(String)leftOverStkList.get(i)[2];
				 log.debug("reqID"+leftOverStkList.get(i)[5]);
				 Optional<RequestMaterials> reqMaterials = reqMatRep.findById((Integer)leftOverStkList.get(i)[5]);
				 
				 log.debug("leftOverStk"+leftOverStk+"expAttendee"+expAttendee);
				
				
					 if(expAttendee>=leftOverStk)
					 {
						 	if(reqMaterials.isPresent())
							 {	 
								 RequestMaterials reqMaterialObj = reqMaterials.get();
								 int appQty1=reqMaterialObj.getApprQty();
								 expAttendee=expAttendee-leftOverStk;
								 reqMaterialObj.setLeftOverQty(0);
								 reqMaterialObj.setUsedQty(appQty1);
							 }
					 }
					 else
					 {
						 
						 if(reqMaterials.isPresent())
						 {	 
							 RequestMaterials reqMaterialObj = reqMaterials.get();
							 int appQty1=reqMaterialObj.getApprQty();
							 int usedQty=leftOverStk-expAttendee;
							 reqMaterialObj.setLeftOverQty(usedQty);
							 usedQuant=appQty1-usedQty;
							 reqMaterialObj.setUsedQty(usedQuant);
						 }
						 expAttendee=0;
					 }
						 
						 
			   }	 
				 
				  log.debug("expAttendee"+expAttendee); 
				
			   
		   }
		
		
	}
	

}
