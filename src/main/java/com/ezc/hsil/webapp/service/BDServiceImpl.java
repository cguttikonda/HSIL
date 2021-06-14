package com.ezc.hsil.webapp.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
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
import com.ezc.hsil.webapp.model.MaterialMasterKey;
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
public class BDServiceImpl implements IBDService{
	 private static final Exception Exception = null;
	@Autowired
	 private  UseLeftOverStockSer useLeftOverStk;
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
	public EzcRequestHeader createBDRequest(EzcRequestHeader ezcRequestHeader) {
		// TODO Auto-generated method stub
		log.debug("In service class BD","I"); 
		EzcRequestHeader reqHeader = reqHeaderRepo.save(ezcRequestHeader);
		return reqHeader;
		/*
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
		*/
	
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
		//log.debug("docid"+docId);
		EzcRequestHeader reqHeader = reqHeaderRepo.findById(docId).orElseThrow(() -> new EntityNotFoundException());
		return reqHeader;
	}
	@Override
	public List<EzcComments> getBDCommentRequest(String docId) {
		return commRepo.findByRequest(docId);
	}

	@Override
	public void submitBDDet(String id,String matQty,EzcRequestHeader ezcRequestHeader) throws java.lang.Exception
	{
		reqMatRep.deleteByRequestId(id);
		EzcRequestHeader ezReqHeader = bdService.getBDRequest(id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		log.debug("id123"+id);
		Set<RequestMaterials> reqMatSet = new HashSet<RequestMaterials>();
		Set<EzcComments> ezcComments = ezcRequestHeader.getEzcComments();
		ezReqHeader.setErhStatus("APPROVED");
		ezReqHeader.setErhOutStore(ezcRequestHeader.getErhOutStore());
		ezReqHeader.setErhModifiedBy(userObj.getUserId());
		ezReqHeader.setErhModifiedOn(new Date());
		ezReqHeader.getEzcComments().addAll(ezcComments);
		Hashtable<String,String> matHt=new Hashtable<String,String>();
		String stockLoc = ezcRequestHeader.getErhOutStore();
		for(int i=0;i<matQty.split(",").length;i++)
		{	
			matHt.put(matQty.split(",")[i].split("#")[0],matQty.split(",")[i].split("#")[1]);
		}
		
		  log.debug("reqMatSet"+reqMatSet);
		  log.debug("reqMatSetsize"+reqMatSet.size());
		  Set<String> matKeySet = matHt.keySet();
		  
		  for(String tempMat : matKeySet) {
			  log.debug("matHt"+matHt);
			  {
				  Integer appQty=Integer.parseInt((String)matHt.get(tempMat));
				  log.debug("appQty"+appQty);
				  MaterialMasterKey matMasterKey=new MaterialMasterKey(tempMat, stockLoc);
				  Optional<MaterialMaster> matMaster = masterRepo.findById(matMasterKey);
	                if(matMaster.isPresent())
	                {  
	                       MaterialMaster mat = matMaster.get();
	                       int blockQtyTemp=0;
	    	        	   if(mat.getBlockQty() != null)
	    	        		   blockQtyTemp=mat.getBlockQty();
	    	               log.debug("blockQty::"+blockQtyTemp);
	    	               log.debug("inputQty::"+appQty);
	    	               log.debug("dbqty::"+mat.getQuantity());
	                       int stockChk = (mat.getQuantity()-blockQtyTemp)-appQty;
	    	               if(stockChk < 0)
	    	               {
	    	            	   log.debug("Exception thrown::stockChk"+stockChk);
	    	            	   throw Exception;	//need to be changed to custom exception
	    	            	   
	    	               }
	    	               else
	    	               {	  
		                       int blockQty=appQty;
		                       if(mat.getBlockQty() != null)
		                    	   blockQty += mat.getBlockQty();
		                       mat.setBlockQty(blockQty);

		                        RequestMaterials reqMat = new RequestMaterials();
								reqMat.setMatCode(mat.getMaterialCode());
								reqMat.setMatDesc(mat.getMaterialDesc());
								reqMat.setApprQty(appQty);
								reqMat.setIsNew('Y');
								reqMatSet.add(reqMat);	
		                       
		                       
	    	               }
	                }
			  }
			  
		  } 
		  
		  for(RequestMaterials tempItem : reqMatSet) { 
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  reqMatRep.save(tempItem); 
			}
		  for(EzcComments tempItem : ezcComments) { 
			  tempItem.setEzcRequestHeader(ezReqHeader);	
			  commRepo.save(tempItem); 
			}
	
		
 
	}
	
	/*
	 * @Override public void submitBDDet(String id,String matQty,EzcRequestHeader
	 * ezcRequestHeader) throws java.lang.Exception {
	 * 
	 * EzcRequestHeader ezReqHeader = bdService.getBDRequest(id); Authentication
	 * authentication = SecurityContextHolder.getContext().getAuthentication();
	 * Users userObj = (Users)authentication.getPrincipal(); log.debug("id123"+id);
	 * Set<RequestMaterials> reqMatSet = ezReqHeader.getRequestMaterials();
	 * Set<RequestMaterials> ezReqMatList = ezcRequestHeader.getRequestMaterials();
	 * Set<EzcComments> ezcComments = ezcRequestHeader.getEzcComments();
	 * ezReqHeader.setErhStatus("APPROVED");
	 * ezReqHeader.setErhOutStore(ezcRequestHeader.getErhOutStore());
	 * ezReqHeader.setErhModifiedBy(userObj.getUserId());
	 * ezReqHeader.setErhModifiedOn(new Date());
	 * ezReqHeader.getRequestMaterials().addAll(ezReqMatList);
	 * ezReqHeader.getEzcComments().addAll(ezcComments); Hashtable matHt=new
	 * Hashtable(); for(int i=0;i<matQty.split(",").length;i++) {
	 * matHt.put(matQty.split(",")[i].split("#")[0],matQty.split(",")[i].split("#")[
	 * 1]); }
	 * 
	 * log.debug("reqMatSet"+reqMatSet);
	 * log.debug("reqMatSetsize"+reqMatSet.size()); for(RequestMaterials tempItem :
	 * reqMatSet) { log.debug("matHt"+matHt); { Integer
	 * appQty=Integer.parseInt((String)matHt.get(tempItem.getMatCode()));
	 * log.debug("appQty"+appQty); tempItem.setApprQty(appQty);//added by goutham
	 * Optional<MaterialMaster> matMaster =
	 * masterRepo.findById(tempItem.getMatCode()); if(matMaster.isPresent()) {
	 * MaterialMaster mat = matMaster.get(); int blockQtyTemp=0;
	 * if(mat.getBlockQty() != null) blockQtyTemp=mat.getBlockQty();
	 * log.debug("blockQty::"+blockQtyTemp); log.debug("inputQty::"+appQty);
	 * log.debug("dbqty::"+mat.getQuantity()); int stockChk =
	 * (mat.getQuantity()-blockQtyTemp)-appQty; if(stockChk < 0) {
	 * log.debug("Exception thrown::stockChk"+stockChk); throw Exception; //need to
	 * be changed to custom exception
	 * 
	 * } else {
	 * 
	 * 
	 * int blockQty=appQty; if(mat.getBlockQty() != null) blockQty +=
	 * mat.getBlockQty(); mat.setBlockQty(blockQty); } } }
	 * 
	 * }
	 * 
	 * for(RequestMaterials tempItem : ezReqMatList) {
	 * tempItem.setEzcRequestHeader(ezReqHeader); reqMatRep.save(tempItem); }
	 * for(EzcComments tempItem : ezcComments) {
	 * tempItem.setEzcRequestHeader(ezReqHeader); commRepo.save(tempItem); }
	 * 
	 * 
	 * 
	 * }
	 */
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
			  log.debug("matCnt in items"+matCnt);
			  if(tempItem.getEriDealer() != null && !"null".equals(tempItem.getEriDealer()) && !"".equals(tempItem.getEriDealer()))
			  {
				  String quan=tempItem.getEriQuantity();
				  log.debug("quan"+quan);
				  if(quan == null || "null".equals(quan) || "".equals(quan))quan="0";
				// matCnt++;
				  matCnt=matCnt+Integer.parseInt(quan);
				  tempItem.setEzcRequestHeader(ezReqHeader);	
				  reqDealRep.save(tempItem);
			  }
			}
		 log.debug("matCnt"+matCnt);
		  for(RequestMaterials requestMaterials : reqMatSet) { 
			   int apprQty =requestMaterials.getApprQty();
			   log.debug("apprQty"+apprQty);
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
	  	useLeftOverStk.updateLeftOverStock(ezReqHeader.getErhRequestedBy(),matCnt,"BD");
	  ezReqHeader.setErhStatus("SUBMITTED");
	  ezReqHeader.setErhCity(bdRequestDetailDto.getReqHeader().getErhCity());
	  ezReqHeader.setErhPurpose(bdRequestDetailDto.getReqHeader().getErhPurpose());
	  ezReqHeader.setEzcRequestItems(new HashSet<EzcRequestItems>(ezReqItemList));
	  
	  
	  
	  
	  
	}
	

	@Override
	public List<Object[]> getBDLeftOverStock(String reqBY) {
		return reqHeaderRepo.getLeftOverStock(reqBY,"BD");
	}
	@Override
	public List<Object[]> getPendingList(String reqBY,String reqType) {
		return reqHeaderRepo.getPendingList(reqBY,reqType);
	}
	@Override
	public void NullifyBDQty(String leftOverId, String reasonNullify, String commentsNullify) {
		
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
	public void rejectBDRequest(EzcRequestHeader ezcRequestHeader) {
	  EzcRequestHeader ezReqHeader = reqHeaderRepo.findById(ezcRequestHeader.getId()).orElseThrow(() -> new EntityNotFoundException());
	  Set<EzcComments> ezComments = ezcRequestHeader.getEzcComments();
	  ezReqHeader.getEzcComments().addAll(ezComments);
	  ezReqHeader.setErhStatus("REJECTED");
	
	  for(EzcComments tempItem : ezComments) { 
		  tempItem.setEzcRequestHeader(ezReqHeader);	
		  commRepo.save(tempItem); 
		}		
	}


}
