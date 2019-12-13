package com.ezc.hsil.webapp.service;

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
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.persistance.dao.EzcCommentsRepo;
import com.ezc.hsil.webapp.persistance.dao.MaterialMasterRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestDetailsRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestHeaderRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestMaterialsRepo;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@Transactional
public class BDServiceImpl implements IBDService{
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
    
	 
	@Override
	public void createBDRequest(EzcRequestHeader ezcRequestHeader) {
		// TODO Auto-generated method stub
		log.debug("In service class BD","I"); 
		reqHeaderRepo.save(ezcRequestHeader);
		
		EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(ezcRequestHeader.getId()).orElseThrow(() -> new EntityNotFoundException());
		Set<RequestMaterials> ezReqMatList = ezReqHeader.getRequestMaterials();
		  for(RequestMaterials tempItem : ezReqMatList) {
		      Character c1 = new Character('N'); 
		      if(c1.equals(tempItem.getIsNew()))
			  {
				  RequestMaterials requestMaterials = reqMatRep.findById(tempItem.getAllocId()).orElseThrow(() -> new EntityNotFoundException());
				  requestMaterials.setLeftOverQty(requestMaterials.getLeftOverQty()-tempItem.getApprQty());
			  }
			  else
			  {
			
				 
				  Optional<MaterialMaster> matMaster = masterRepo.findById(tempItem.getMatCode());
	                if(matMaster.isPresent())
	                {  
	                       MaterialMaster mat = matMaster.get();
	                       int qty = mat.getQuantity()-tempItem.getApprQty();
	                       mat.setQuantity(qty);
	                }      

			  }
		  }
		  for(RequestMaterials tempItem : ezReqMatList) { 
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  reqMatRep.save(tempItem); 
			}
	
	}

	@Override
	public List<EzcRequestHeader> getBDRequestListByDate(ListSelector listSelector) {
		/*if("ALL".equals(listSelector.getStatus()))
			return reqHeaderRepo.getBdALLRequestList(listSelector.getType(),listSelector.getFromDate(),listSelector.getToDate()); 
		else	
			return reqHeaderRepo.getBdRequestList(listSelector.getStatus(),listSelector.getType()); */
		/*
		 * if("ALL".equals(listSelector.getStatus())) return reqHeaderRepo.
		 * findByErhReqTypeAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual
		 * (listSelector.getType(),listSelector.getToDate(),listSelector.getFromDate());
		 * else return reqHeaderRepo.
		 * findByErhReqTypeAndErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual
		 * (listSelector.getType(),listSelector.getStatus(),listSelector.getToDate(),
		 * listSelector.getFromDate());
		 */
		return requestCustomDto.findRequestList(listSelector);
	}
	
	@Override
	public EzcRequestHeader getBDRequest(String docId) {
		EzcRequestHeader reqHeader = reqHeaderRepo.findById(docId).orElseThrow(() -> new EntityNotFoundException());
		return reqHeader;
	}
	@Override
	public List<EzcComments> getBDCommentRequest(String docId) {
		return commRepo.findByRequest(docId);
	}
	@Override
	public void createBDDetails(BDRequestDetailDto bdRequestDetailDto) {
		String loggedUser="";
		EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(bdRequestDetailDto.getReqHeader().getId()).orElseThrow(() -> new EntityNotFoundException());
		List<EzcRequestItems> ezReqItemList = bdRequestDetailDto.getEzcRequestItems();
		Set<RequestMaterials> reqMatSet = ezReqHeader.getRequestMaterials();
		String comments=bdRequestDetailDto.getCommentReqDto();
		Set<EzcComments> commSet = new HashSet<EzcComments>();
		int matCnt = 0;
		 if(comments!= null)
			{
				EzcComments comm=new EzcComments();
				comm.setComments(comments);
				comm.setType("SUBMIT");
				
				commSet.add(comm);
			
			}
		 ezReqHeader.setEzcComments(commSet);
		  for(EzcComments tempItem : commSet) { 
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  commRepo.save(tempItem); 
			}
		  for(EzcRequestItems tempItem : ezReqItemList) {
			  if(tempItem.getEriDealer() != null && !"null".equals(tempItem.getEriDealer()) && !"".equals(tempItem.getEriDealer()))
			  {
				  matCnt++;
				  tempItem.setEzcRequestHeader(ezReqHeader);	
				  reqDealRep.save(tempItem);
			  }
			}
		 
		  for(RequestMaterials requestMaterials : reqMatSet) { 
			   int apprQty =requestMaterials.getApprQty();
			   
			   if(apprQty > matCnt)
			   {
				   requestMaterials.setLeftOverQty(apprQty-matCnt);
				   requestMaterials.setUsedQty(matCnt);
			   }
			   else
			   {
				   requestMaterials.setUsedQty(apprQty);
				   matCnt = matCnt-apprQty;
			   }
			} 
	  
	  ezReqHeader.setErhStatus("SUBMITTED");
	  ezReqHeader.setErhCity(bdRequestDetailDto.getReqHeader().getErhCity());
	  ezReqHeader.setErhPurpose(bdRequestDetailDto.getReqHeader().getErhPurpose());
	  ezReqHeader.setEzcRequestItems(new HashSet<EzcRequestItems>(ezReqItemList));
	  
	  
	  
	  
	  
	}

	@Override
	public List<Object[]> getLeftOverStock(String requestedBy) {
		return reqHeaderRepo.getLeftOverStock(requestedBy);
	}
	
	

}
