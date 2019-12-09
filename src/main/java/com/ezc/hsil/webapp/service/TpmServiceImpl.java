package com.ezc.hsil.webapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.RequestCustomDto;
import com.ezc.hsil.webapp.dto.TpmRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.persistance.dao.MaterialMasterRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestDetailsRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestHeaderRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestMaterialsRepo;
import com.ezc.hsil.webapp.persistance.dao.EzcCommentsRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TpmServiceImpl implements ITPMService{
	
	    @Autowired
	    private RequestHeaderRepo reqHeaderRepo;
	    
	    @Autowired
	    private RequestDetailsRepo reqDtlRep;
	    
	    @Autowired
	    private RequestMaterialsRepo reqMatRep;
	    
	    @Autowired
	    private MaterialMasterRepo masterRepo;
	    
	    @Autowired
	    private EzcCommentsRepo commRepo;
	    
	    @Autowired
	    private RequestCustomDto requestCustomDto;
	    
	    

		@Override
		public void createTPMRequest(EzcRequestHeader ezcRequestHeader) {
			log.debug("In service class","I");
			reqHeaderRepo.save(ezcRequestHeader);
		}

		@Override
		public List<EzcRequestHeader> getTPMRequestList(String status) {
			//return reqHeaderRepo.findAll();
			return reqHeaderRepo.findByErhStatus(status);
		}

		@Override
		public EzcRequestHeader getTPMRequest(String docId) {
			return reqHeaderRepo.findById(docId).orElseThrow(() -> new EntityNotFoundException());
		}
		@Override
		public List<EzcComments> getTPMCommentRequest(String docId) {
			return commRepo.findByRequest(docId);
		}
		@Override
		public void createTPMDetails(TpmRequestDetailDto tpmRequestDetailDto) {
			EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(tpmRequestDetailDto.getReqHeader().getId()).orElseThrow(() -> new EntityNotFoundException());
			List<EzcRequestItems> ezReqItemList = tpmRequestDetailDto.getEzcRequestItems();
			List<EzcComments> ezcommList = tpmRequestDetailDto.getEzcComments();
			Set<RequestMaterials> reqMatSet = ezReqHeader.getRequestMaterials();
			Set<EzcComments> commSet = new HashSet<EzcComments>();
			String comments=tpmRequestDetailDto.getCommentReqDto();
		  ezReqHeader.setErhStatus("SUBMITTED");
		  ezReqHeader.setErhCostIncured(tpmRequestDetailDto.getReqHeader().getErhCostIncured());
		  ezReqHeader.setEzcRequestItems(new HashSet<EzcRequestItems>(ezReqItemList));
		  
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
			  if(tempItem.getEriPlumberName() != null && !"null".equals(tempItem.getEriPlumberName()) && !"".equals(tempItem.getEriPlumberName()))
			  {
				  matCnt++;
				  tempItem.setEzcRequestHeader(ezReqHeader);	
				  reqDtlRep.save(tempItem);
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
		   
		  
		}
 
		@Override
		public List<EzcRequestHeader> getTPMRequestListByDate(ListSelector listSelector) {
			/*
			if("ALL".equals(listSelector.getStatus()))
				return reqHeaderRepo.findByErhReqTypeAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getType(),listSelector.getToDate(),listSelector.getFromDate());	
			else	
				return reqHeaderRepo.findByErhReqTypeAndErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getType(),listSelector.getStatus(),listSelector.getToDate(),listSelector.getFromDate());
			*/
			log.debug("type in getTPMRequestListByDate::::"+listSelector.getType()); 
			return requestCustomDto.findRequestList(listSelector);
		}

		@Override
		public void approveTPMRequest(EzcRequestHeader ezcRequestHeader) {
		  EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(ezcRequestHeader.getId()).orElseThrow(() -> new EntityNotFoundException());
		  Set<RequestMaterials> ezReqMatList = ezcRequestHeader.getRequestMaterials();
		  Set<EzcComments> ezcComments = ezcRequestHeader.getEzcComments();
		  ezReqHeader.getRequestMaterials().addAll(ezReqMatList);
		  ezReqHeader.getEzcComments().addAll(ezcComments);
		  ezReqHeader.setErhStatus("APPROVED");
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
		  for(EzcComments tempItem : ezcComments) { 
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  commRepo.save(tempItem); 
			}
		}
		@Override
		public void rejectTPMRequest(EzcRequestHeader ezcRequestHeader) {
		  EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(ezcRequestHeader.getId()).orElseThrow(() -> new EntityNotFoundException());
		  Set<EzcComments> ezComments = ezcRequestHeader.getEzcComments();
		  ezReqHeader.getEzcComments().addAll(ezComments);
		  ezReqHeader.setErhStatus("REJECTED");
		
		  for(EzcComments tempItem : ezComments) { 
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  commRepo.save(tempItem); 
			}		
		}
		@Override
		public void closeTPMRequest(TpmRequestDetailDto tpmRequestDetailDto) {
		
		  EzcRequestHeader ezReqHeader =
		  reqHeaderRepo.findById(tpmRequestDetailDto.getReqHeader().getId()).
		  orElseThrow(() -> new EntityNotFoundException());
		  ezReqHeader.setErhStatus("CLOSED"); 
		  List<RequestMaterials> ezReqMatList = tpmRequestDetailDto.getEzReqMatList();
		  System.out.print("ezReqMatList:::"+ezReqMatList.size());
		 
		
		  for(RequestMaterials tempItem : ezReqMatList) { 
			  System.out.print("id:::"+tempItem.getId());
			  RequestMaterials ezReqMat = reqMatRep.findById(tempItem.getId()).orElseThrow(() -> new EntityNotFoundException()); 
			  ezReqMat.setUsedQty(tempItem.getUsedQty()); 
		  }
		  
		}

		@Override
		public Set<RequestMaterials> getLastRequestDet(String requestedBy) {
			EzcRequestHeader ezRequestHeader =reqHeaderRepo.findTopByErhCreatedGroupAndErhRequestedByAndErhStatusOrderByErhConductedOnDesc("TPM",requestedBy,"CLOSED");
			if(ezRequestHeader != null)
			{
				return reqMatRep.findByRequest(ezRequestHeader.getId());
			}
			else
			{
				return null;
			}
		}

		@Override
		public List<Object[]> getLeftOverStock(String requestedBy) {
			return reqHeaderRepo.getLeftOverStock(requestedBy);
		}

		

}
