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
    @Autowired
	 IBDService bdService;
	 
    
	 
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
	public void submitBDDet(String id,Integer appQty,EzcRequestHeader ezcRequestHeader)
	{
		
		EzcRequestHeader ezReqHeader = bdService.getBDRequest(id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		log.debug("id123"+id);
		Set<RequestMaterials> reqMatSet = ezReqHeader.getRequestMaterials();
		Set<RequestMaterials> ezReqMatList = ezcRequestHeader.getRequestMaterials();
		Set<EzcComments> ezcComments = ezcRequestHeader.getEzcComments();
		ezReqHeader.setErhStatus("APPROVED");
		ezReqHeader.setErhModifiedBy(userObj.getUserId());
		ezReqHeader.setErhModifiedOn(new Date());
		ezReqHeader.getRequestMaterials().addAll(ezReqMatList);
		ezReqHeader.getEzcComments().addAll(ezcComments);
		
		  log.debug("appQty"+appQty);
		  for(RequestMaterials tempItem : reqMatSet) {
			  log.debug("matid"+tempItem.getId());
		      Character c1 = new Character('Y'); 
			  if(c1.equals(tempItem.getIsNew()))
			  {
				  RequestMaterials requestMaterials = reqMatRep.findById(tempItem.getId()).orElseThrow(() -> new EntityNotFoundException());
				  requestMaterials.setApprQty(appQty);
				  log.debug("matid"+tempItem.getId());
			  }
			  
		  } 
		  log.debug("appQtymat"+appQty);
		  for(RequestMaterials tempItem : ezReqMatList) { 
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  reqMatRep.save(tempItem); 
			}
		  for(EzcComments tempItem : ezcComments) { 
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  commRepo.save(tempItem); 
			}
	
		
 
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
				comm.setType("APPROVE");
				
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
