package com.ezc.hsil.webapp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.ezc.hsil.webapp.model.MaterialMasterKey;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.model.EzNullifiedStock;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcRequestDealers;
import com.ezc.hsil.webapp.persistance.dao.MaterialMasterRepo;
import com.ezc.hsil.webapp.persistance.dao.NullifiedStockRepo;
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
		private  UseLeftOverStockSer useLeftOverStk;
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
	    
	    @Autowired
	    private NullifiedStockRepo nullifiedStockRepo;

		@Override
		public EzcRequestHeader createTPMRequest(EzcRequestHeader ezcRequestHeader) {
			log.debug("In service class","I");
			EzcRequestHeader reqHeader = reqHeaderRepo.save(ezcRequestHeader);
			return reqHeader;
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
		public void submitTPMDetails(TpmRequestDetailDto tpmRequestDetailDto) {
		EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(tpmRequestDetailDto.getReqHeader().getId()).orElseThrow(() -> new EntityNotFoundException());
		List<EzcRequestItems> ezReqItemList = tpmRequestDetailDto.getEzcRequestItems();
		Set<EzcRequestItems> ezReqItems = ezReqHeader.getEzcRequestItems();
		List<EzcComments> ezcommList = tpmRequestDetailDto.getEzcComments();
		Set<RequestMaterials> reqMatSet = ezReqHeader.getRequestMaterials();
		Set<EzcRequestDealers> reqDealersSet = ezReqHeader.getEzcRequestDealers();
		Set<EzcComments> commSet = new HashSet<EzcComments>();
		String comments=tpmRequestDetailDto.getCommentReqDto();
		  ezReqHeader.setErhCostIncured(tpmRequestDetailDto.getReqHeader().getErhCostIncured());
		  //ezReqHeader.setEzcRequestItems(new HashSet<EzcRequestItems>(ezReqItemList));
		  
		  if(ezcommList != null)
		  {
			  for(EzcComments comm:ezcommList)
			  {
				  commSet.add(comm);
			  }
		  }
		  
		  EzcRequestDealers ezcRequestDealers = tpmRequestDetailDto.getMeetDealer();
			Set<EzcRequestItems> toBeRemovedSet =
					ezReqItems.stream()
			               .filter(reqItem -> ezcRequestDealers.getId().equals(reqItem.getEriDealerId()))
			               .collect(Collectors.toSet());
			
			//ezReqItems.removeAll(toBeRemovedSet);
			if(toBeRemovedSet != null)
			{
				for(EzcRequestItems delItem:toBeRemovedSet)
				{
					delItem.setEzcRequestHeader(null);
				}
				ezReqItems.removeAll(toBeRemovedSet);
			}
 
		 
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
		  boolean isCompleted = true; 
		  
		  int matCnt=0;
		  for(EzcRequestItems tempItem : ezReqItemList) {
			  if(tempItem.getEriPlumberName() != null && !"null".equals(tempItem.getEriPlumberName()) && !"".equals(tempItem.getEriPlumberName()))
			  {
				  tempItem.setEriDealerId(ezcRequestDealers.getId());
				  tempItem.setEzcRequestHeader(ezReqHeader);
				  ezReqItems.add(tempItem);
				  matCnt++;
				  //reqDtlRep.save(tempItem);
			  }
			}
		  int itemCount = ezReqItemList.size();
		  
		  log.debug("itemCount"+itemCount+"matCnt"+matCnt);
		  int expAttendee=ezReqHeader.getErhNoOfAttendee();
		  
		  log.debug("expAttendee"+expAttendee); 
		  log.debug("reqDealersSet size"+reqDealersSet.size()); 
		  int meetCnt=0;
		  for(EzcRequestDealers tempItem : reqDealersSet) {
			  log.debug("dealer"+ezcRequestDealers.getId()+"tempItem"+tempItem.getId());
			  //if(ezcRequestDealers.getId()==tempItem.getId())
			  if(ezcRequestDealers.getId().equals(tempItem.getId()))
			  {
				  log.debug("aytte"+tempItem.getErdNoOfAttendee());
			      expAttendee=tempItem.getErdNoOfAttendee();
				  tempItem.setErdDealerName(ezcRequestDealers.getErdDealerName());
				  tempItem.setErdMeetDate(ezcRequestDealers.getErdMeetDate());
				  tempItem.setErdCostIncured(ezcRequestDealers.getErdCostIncured());
				  tempItem.setErdMeetStatus("COMPLETED");
				  meetCnt++;
			  }
			  if(!"COMPLETED".equals(tempItem.getErdMeetStatus()))
			  {
				  isCompleted = false;
			  }
			}
		 log.debug("meetCnt"+meetCnt+"expAttendee"+expAttendee);
		 if(isCompleted) ezReqHeader.setErhStatus("SUBMITTED");
		  if(meetCnt==1)
		  {
			  
			  
			 // int matCnt = ezReqItemList.size();
			   for(RequestMaterials requestMaterials : reqMatSet) { 
				   int apprQty =requestMaterials.getApprQty();
				   int usedQty =requestMaterials.getUsedQty();
				   int leftQty=requestMaterials.getLeftOverQty();
				   if(matCnt==0)continue;
				   log.debug("apprQty"+apprQty+"usedQty"+usedQty+"matCnt"+matCnt);
				   if(apprQty==usedQty)continue;
				   
				   if(apprQty > matCnt)
				   {
					  int remQty=0;
					  
					   if(leftQty>0)
					   { 
						   if(leftQty>matCnt)  
						   { 
							   remQty=leftQty-matCnt;
							   matCnt=0;
						   }   
						   else
						   {
							   remQty=0;
							   matCnt=matCnt-leftQty;
							   requestMaterials.setLeftOverQty(remQty);
							   requestMaterials.setUsedQty(apprQty-remQty);
							   
							   
						   }
					   } 
					  else
					  {
						  remQty=apprQty-matCnt;
						  matCnt=0;
					  }
						  
					   requestMaterials.setLeftOverQty(remQty);
					   requestMaterials.setUsedQty(apprQty-remQty);
					   
				   }
				   else  
				   {
					  int prevLeftOvernt=requestMaterials.getLeftOverQty();
					   requestMaterials.setUsedQty(apprQty);
					   requestMaterials.setLeftOverQty(0);
					   if(matCnt>0)
					   {
						if(prevLeftOvernt>0)
							matCnt = matCnt-prevLeftOvernt;
						else
							matCnt = matCnt-apprQty;
					   }
						   
				   }
				}
			   log.debug("matCnt"+matCnt);
			   if(matCnt>0)
				   useLeftOverStk.updateLeftOverStock(ezReqHeader.getErhRequestedBy(),matCnt,"TPM");
			   log.debug("matCnt"+matCnt); 
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
		public List<Object[]> getTPMRequestList(ListSelector listSelector) {
			/*
			if("ALL".equals(listSelector.getStatus()))
				return reqHeaderRepo.findByErhReqTypeAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getType(),listSelector.getToDate(),listSelector.getFromDate());	
			else	
				return reqHeaderRepo.findByErhReqTypeAndErhStatusAndErhRequestedOnLessThanEqualAndErhRequestedOnGreaterThanEqual(listSelector.getType(),listSelector.getStatus(),listSelector.getToDate(),listSelector.getFromDate());
			*/
			log.debug("type in getTPMRequestListByDate::::"+listSelector.getType()); 
			return requestCustomDto.findRequestListJoinDealer(listSelector); 
		}

		@Override
		public void approveTPMRequest(EzcRequestHeader ezcRequestHeader) {
		  EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(ezcRequestHeader.getId()).orElseThrow(() -> new EntityNotFoundException());
		  Set<RequestMaterials> ezReqMatList = ezcRequestHeader.getRequestMaterials();
		  Set<EzcComments> ezcComments = ezcRequestHeader.getEzcComments();
		  ezReqHeader.getRequestMaterials().addAll(ezReqMatList);
		  ezReqHeader.getEzcComments().addAll(ezcComments);
		  ezReqHeader.setErhStatus("APPROVED");
		  ezReqHeader.setErhApprDate(new Date());
		  ezReqHeader.setErhOutStore(ezcRequestHeader.getErhOutStore());
		  for(RequestMaterials tempItem : ezReqMatList) {
		      Character c1 = new Character('N'); 
			  if(c1.equals(tempItem.getIsNew()))
			  {
				  RequestMaterials requestMaterials = reqMatRep.findById(tempItem.getAllocId()).orElseThrow(() -> new EntityNotFoundException());
				  requestMaterials.setLeftOverQty(requestMaterials.getLeftOverQty()-tempItem.getApprQty());
			  }
			  else
			  {
				  MaterialMasterKey matMasterKey=new MaterialMasterKey(tempItem.getMatCode(), ezcRequestHeader.getErhOutStore());
				  Optional<MaterialMaster> matMaster = masterRepo.findById(matMasterKey);
				 
	                if(matMaster.isPresent())
	                {  
	                       MaterialMaster mat = matMaster.get();
	                       //int qty = mat.getQuantity()-tempItem.getApprQty();
	                       //mat.setQuantity(qty);
	                       int blockQty = tempItem.getApprQty();
	                       if(mat.getBlockQty() != null)
	                    	   blockQty += mat.getBlockQty();
	                       mat.setBlockQty(blockQty);
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
		  ezReqHeader.setErhApprDate(new Date());
		  
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
		public List<Object[]> getLeftOverStock(String requestedBy,String requestType) {
			return reqHeaderRepo.getLeftOverStock(requestedBy,requestType);
		}
		
		@Override
		public List<Object[]> getAvailableStock(String requestedBy,String requestType) {
			return reqHeaderRepo.getAvailableStock(requestedBy,requestType);
		}
		
		@Override
		public List<Object[]> getAllStock(String requestedBy) {
			return reqHeaderRepo.getAllStock(requestedBy);
		}

		@Override
		public void NullifyTpmQty(String leftOverId, String reasonNullify, String commentsNullify) {
			
			Optional<RequestMaterials> reqMaterials = reqMatRep.findById(Integer.parseInt(leftOverId));
			if(reqMaterials.isPresent())
			{
				RequestMaterials reqMaterialObj = reqMaterials.get();
				int leftOverQty = reqMaterialObj.getLeftOverQty();
				reqMaterialObj.setFreeQty(leftOverQty);
				reqMaterialObj.setLeftOverQty(0);
				Optional<EzcRequestHeader> reqHeader = reqHeaderRepo.findById(reqMaterialObj.getEzcRequestHeader().getId());
				String userId = "";
				if(reqHeader.isPresent())
				{
					userId = reqHeader.get().getErhRequestedBy(); 
				}
				EzNullifiedStock ezNullifiedStock=new EzNullifiedStock();
				ezNullifiedStock.setQty(leftOverQty);
				ezNullifiedStock.setLeftOverId(reqMaterialObj.getId());
				ezNullifiedStock.setMaterial(reqMaterialObj.getMatCode());
				ezNullifiedStock.setReason(reasonNullify);
				ezNullifiedStock.setComments(commentsNullify);
				ezNullifiedStock.setUserId(userId);
				nullifiedStockRepo.save(ezNullifiedStock);
				
			}
		
		}

		@Override
		public List<Object[]> getMeetDetailsById(String docId) {
			return reqHeaderRepo.getMeetDetailsById(docId);
		}

		@Override
		public List<Object[]> pendingRequests(String userId)
	    {
	    	ListSelector listSelector=null; 
	    	if(listSelector == null || listSelector.getFromDate() == null)
	    	{
	    		Date todayDate = new Date();
	    		Calendar c = Calendar.getInstance(); 
	    		c.setTime(todayDate); 
	    		c.add(Calendar.MONTH, -3);
	    		listSelector = new ListSelector();
	    		listSelector.setFromDate(c.getTime());
	    		listSelector.setToDate(todayDate);
	    		listSelector.setStatus("APPROVED");
	    	}
	    	listSelector.setType("TPM");
	    	ArrayList<String> userList=new ArrayList<String>();
	    	userList.add(userId);
	    	listSelector.setUser(userList);
	    	List<Object[]> list = this.getTPMRequestList(listSelector);
	    	log.debug("list:::"+list.size());
	        return list;
	    }

		@Override
		public void saveTPMDetails(TpmRequestDetailDto tpmRequestDetailDto) {
			EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(tpmRequestDetailDto.getReqHeader().getId()).orElseThrow(() -> new EntityNotFoundException());
			List<EzcRequestItems> ezReqItemList = tpmRequestDetailDto.getEzcRequestItems();
			Set<EzcRequestItems> ezReqItems = ezReqHeader.getEzcRequestItems();
			Set<EzcRequestDealers> reqDealersSet = ezReqHeader.getEzcRequestDealers();
			EzcRequestDealers ezcRequestDealers = tpmRequestDetailDto.getMeetDealer();
			
			Set<EzcRequestItems> toBeRemovedSet =
					ezReqItems.stream()
			               .filter(reqItem -> ezcRequestDealers.getId().equals(reqItem.getEriDealerId()))
			               .collect(Collectors.toSet());
			
			//ezReqItems.removeAll(toBeRemovedSet);
			if(toBeRemovedSet != null)
			{
				for(EzcRequestItems delItem:toBeRemovedSet)
				{
					delItem.setEzcRequestHeader(null);
				}
				ezReqItems.removeAll(toBeRemovedSet);
			}
			  
			  
			  for(EzcRequestItems tempItem : ezReqItemList) {
				  if(tempItem.getEriPlumberName() != null && !"null".equals(tempItem.getEriPlumberName()) && !"".equals(tempItem.getEriPlumberName()))
				  {
					  tempItem.setEriDealerId(ezcRequestDealers.getId());
					  tempItem.setEzcRequestHeader(ezReqHeader);
					  ezReqItems.add(tempItem);
				  }
				}
			  log.debug("ezReqItems::after"+ezReqItems.size());
			  log.debug("reqDealersSet size"+reqDealersSet.size()); 
			  for(EzcRequestDealers tempItem : reqDealersSet) {
				  log.debug("dealer:::::"+ezcRequestDealers.getId()+"tempItem::::"+tempItem.getId());
				  //if(ezcRequestDealers.getId()==tempItem.getId())
				  if(ezcRequestDealers.getId().equals(tempItem.getId()))
				  {
					  tempItem.setErdDealerName(ezcRequestDealers.getErdDealerName());
					  tempItem.setErdMeetDate(ezcRequestDealers.getErdMeetDate());
					  tempItem.setErdCostIncured(ezcRequestDealers.getErdCostIncured());
					  tempItem.setErdMeetStatus("SAVED");
				  }
				}
			 }

			
		


		

}
