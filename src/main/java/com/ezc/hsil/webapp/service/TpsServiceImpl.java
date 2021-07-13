package com.ezc.hsil.webapp.service;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.ezc.hsil.webapp.dto.TpsRequestDetailDto;
import com.ezc.hsil.webapp.model.EzNullifiedStock;
import com.ezc.hsil.webapp.model.EzcComments;
import com.ezc.hsil.webapp.model.EzcRequestDealers;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.MaterialMasterKey;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.persistance.dao.EzcCommentsRepo;
import com.ezc.hsil.webapp.persistance.dao.MaterialMasterRepo;
import com.ezc.hsil.webapp.persistance.dao.NullifiedStockRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestDealersRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestDetailsRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestHeaderRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestMaterialsRepo;
import com.ezc.hsil.webapp.service.ITPSService;
import com.ezc.hsil.webapp.service.TpsServiceImpl;
import com.ezc.hsil.webapp.persistance.dao.EzcCommentsRepo;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@Transactional
public class TpsServiceImpl implements ITPSService {
	
	 @Autowired
	 private  UseLeftOverStockSer useLeftOverStk;
	 @Autowired
	    private RequestHeaderRepo reqHeaderRepo;
	 @Autowired
	    private RequestMaterialsRepo reqMatRep;
	 @Autowired
	 private RequestDetailsRepo reqDealRep;
	 @Autowired
	 MaterialMasterRepo masterRepo; 
	 @Autowired
	 private EzcCommentsRepo commRepo;
	 @Autowired
	 private RequestCustomDto requestCustomDto;
	 @Autowired
	 private RequestDealersRepo requestDealerRepo;   
	 @Autowired
	 private NullifiedStockRepo nullifiedStockRepo;
	 
	@Override
	public EzcRequestHeader createTPSRequest(EzcRequestHeader ezcRequestHeader) {
		// TODO Auto-generated method stub
		log.debug("In service class","I"); 
		return reqHeaderRepo.save(ezcRequestHeader);
	
	}
	@Override
	public void approveTPSRequest(EzcRequestHeader ezcRequestHeader) {
	  EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(ezcRequestHeader.getId()).orElseThrow(() -> new EntityNotFoundException());
	  Set<RequestMaterials> ezReqMatList = ezcRequestHeader.getRequestMaterials();
	  Set<EzcComments> ezcComments = ezcRequestHeader.getEzcComments();
	  ezReqHeader.getRequestMaterials().addAll(ezReqMatList);
	  ezReqHeader.setErhStatus("APPROVED");
	  ezReqHeader.setErhOutStore(ezcRequestHeader.getErhOutStore());
	 /* for(RequestMaterials tempItem : ezReqMatList) { 
		  tempItem.setEzcRequestHeader(ezReqHeader);	
		  reqMatRep.save(tempItem); 
		}*/
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
	public List<EzcComments> getTPSCommentRequest(String docId) {
		return commRepo.findByRequest(docId);
	}
	@Override
	public List<EzcRequestHeader> getTPSRequestListByDate(ListSelector listSelector) {
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
	public EzcRequestHeader getTPSRequest(String docId) {
		EzcRequestHeader reqHeader = reqHeaderRepo.findById(docId).orElseThrow(() -> new EntityNotFoundException());
		return reqHeader;
	}
	
	@Override
	public void submitTPSDetails(TpsRequestDetailDto tpsRequestDetailDto) {
		EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(tpsRequestDetailDto.getReqHeader().getId()).orElseThrow(() -> new EntityNotFoundException());
		List<EzcRequestItems> ezReqItemList = tpsRequestDetailDto.getEzcRequestItems();
		List<EzcRequestDealers> ezReqDeal = tpsRequestDetailDto.getEzcRequestDealers();
		Set<EzcRequestDealers> ezReqDealSet=new  HashSet<EzcRequestDealers>();
		Set<EzcRequestDealers> fromDBReqDeal=requestDealerRepo.findByRequest(tpsRequestDetailDto.getReqHeader().getId());
		Set<RequestMaterials> reqMatSet = ezReqHeader.getRequestMaterials();
		Set<EzcRequestItems> ezReqItems = ezReqHeader.getEzcRequestItems();
		 for(EzcRequestDealers tempItem : fromDBReqDeal) { 
			  log.debug("id delete"+tempItem.getId());	
			  requestDealerRepo.deleteById(tempItem.getId()); 
			}
		 for(EzcRequestDealers tempItem : ezReqDeal) { 
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  requestDealerRepo.save(tempItem); 
			}
		 for(EzcRequestItems delItem:ezReqItems)
			{
				delItem.setEzcRequestHeader(null);
			}
		 ezReqItems.removeAll(ezReqItems);
		 
		 for(EzcRequestDealers tempItem : ezReqDeal) {
			 ezReqDealSet.add(tempItem); 

		 }
		 ezReqHeader.setEzcRequestDealers(ezReqDealSet);
		String comments=tpsRequestDetailDto.getCommentReqDto();
		Set<EzcComments> commSet = new HashSet<EzcComments>();
		 if(comments!= null)
			{
				EzcComments comm=new EzcComments();
				comm.setComments(comments);
				comm.setType("SUBMIT");
				commSet.add(comm);
			
			}
	  ezReqHeader.setErhStatus("SUBMITTED");
	  ezReqHeader.setErhCostIncured(tpsRequestDetailDto.getReqHeader().getErhCostIncured());
	  ezReqHeader.setErhVenue(tpsRequestDetailDto.getReqHeader().getErhVenue());
	  ezReqHeader.setEzcRequestItems(new HashSet<EzcRequestItems>(ezReqItemList));
	  int matCnt = 0;
	  ezReqHeader.setEzcComments(commSet);
	 
	
	  int expAttendee=ezReqHeader.getErhNoOfAttendee();
	  
	  log.debug("expAttendee"+expAttendee); 
	  for(EzcComments tempItem : commSet) { 
		  tempItem.setEzcRequestHeader(ezReqHeader);	
		  commRepo.save(tempItem); 
		}
	  for(EzcRequestItems tempItem : ezReqItemList) {
		  if(tempItem.getEriPlumberName() != null && !"null".equals(tempItem.getEriPlumberName()) && !"".equals(tempItem.getEriPlumberName()))
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
			   matCnt=0;
		   }
		   else
		   {
			   requestMaterials.setUsedQty(apprQty);
			   requestMaterials.setLeftOverQty(0);
			   if(matCnt>0)
				   matCnt = matCnt-apprQty;
		   }
		} 
	   if(matCnt>0)
	   		useLeftOverStk.updateLeftOverStock(ezReqHeader.getErhRequestedBy(),matCnt,"TPS");
	  	log.debug("expAttendee"+expAttendee); 
			
		   
	   
	}
	@Override
	public void rejectTPSRequest(EzcRequestHeader ezcRequestHeader) {
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
	public Set<RequestMaterials> getLastRequestDet(String requestedBy) {
		EzcRequestHeader ezRequestHeader =reqHeaderRepo.findTopByErhCreatedGroupAndErhRequestedByAndErhStatusOrderByErhConductedOnDesc("TPS",requestedBy,"CLOSED");
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
	public void NullifyTpsQty(String leftOverId, String reasonNullify, String commentsNullify) {
		
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
	public List<EzcRequestHeader> pendingRequests(String userId)
    {
    	ListSelector listSelector=null; 
    	if(listSelector == null || listSelector.getFromDate() == null)
    	{
    		Date todayDate = new Date();
    		Calendar c = Calendar.getInstance(); 
    		c.setTime(todayDate); 
    		c.add(Calendar.MONTH, -12);
    		listSelector = new ListSelector();
    		listSelector.setFromDate(c.getTime());
    		listSelector.setToDate(todayDate);
    		listSelector.setStatus("APPROVED");
    	}
    	listSelector.setType("TPS");
    	ArrayList<String> userList=new ArrayList<String>();
    	userList.add(userId);
    	listSelector.setUser(userList);
    	List<EzcRequestHeader> list = this.getTPSRequestListByDate(listSelector);
    	log.debug("list:::"+list.size());
        return list;
    }
	@Override
	public List<Object[]> getAvailableStock(String requestedBy,String requestType) {
		return reqHeaderRepo.getAvailableStock(requestedBy,requestType);
	}
	@Override
	public void saveTPSDetails(TpsRequestDetailDto tpsRequestDetailDto) {
		EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(tpsRequestDetailDto.getReqHeader().getId()).orElseThrow(() -> new EntityNotFoundException());
		List<EzcRequestItems> ezReqItemList = tpsRequestDetailDto.getEzcRequestItems();
		List<EzcRequestDealers> ezReqDeal = tpsRequestDetailDto.getEzcRequestDealers();
		Set<EzcRequestDealers> ezReqDealSet=new  HashSet<EzcRequestDealers>();
		Set<EzcRequestDealers> fromDBReqDeal=requestDealerRepo.findByRequest(tpsRequestDetailDto.getReqHeader().getId());
		Set<EzcRequestItems> ezReqItems = ezReqHeader.getEzcRequestItems();
		 for(EzcRequestDealers tempItem : fromDBReqDeal) { 
			  log.debug("id delete"+tempItem.getId());	
			  requestDealerRepo.deleteById(tempItem.getId()); 
			}
		 for(EzcRequestDealers tempItem : ezReqDeal) { 
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  requestDealerRepo.save(tempItem); 
			}
		 for(EzcRequestItems delItem:ezReqItems)
			{
				delItem.setEzcRequestHeader(null);
			}
		 ezReqItems.removeAll(ezReqItems);
		 for(EzcRequestDealers tempItem : ezReqDeal) {
			 ezReqDealSet.add(tempItem); 

		 }
	  ezReqHeader.setEzcRequestDealers(ezReqDealSet);
	  ezReqHeader.setErhCostIncured(tpsRequestDetailDto.getReqHeader().getErhCostIncured());
	  ezReqHeader.setErhVenue(tpsRequestDetailDto.getReqHeader().getErhVenue());
	  ezReqHeader.setEzcRequestItems(new HashSet<EzcRequestItems>(ezReqItemList));
	  for(EzcRequestItems tempItem : ezReqItemList) {
		  if(tempItem.getEriPlumberName() != null && !"null".equals(tempItem.getEriPlumberName()) && !"".equals(tempItem.getEriPlumberName()))
          {
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  reqDealRep.save(tempItem);
          }
		}
	  
	}

}
