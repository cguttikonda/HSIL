package com.ezc.hsil.webapp.service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.persistance.dao.MaterialMasterRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestHeaderRepo;
import com.ezc.hsil.webapp.persistance.dao.RequestMaterialsRepo;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@Transactional
public class RequestService implements IRequestService {

    @Autowired
    IMasterService masterService;
    @Autowired
    private RequestHeaderRepo reqHeaderRepo;
    @Autowired
	 MaterialMasterRepo masterRepo;

    @Autowired
    private RequestMaterialsRepo reqMaterialRepo;
	
	@Override
	public void revokeRequest(String docId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users userObj = (Users)authentication.getPrincipal();
		Optional<EzcRequestHeader> reqHeaderOpt=reqHeaderRepo.findById(docId);
		if(reqHeaderOpt.isPresent())
		{
			EzcRequestHeader reqHeader=reqHeaderOpt.get();
			reqHeader.setErhOutStore("");
			reqHeader.setErhModifiedBy(userObj.getUserId());
			reqHeader.setErhModifiedOn(new Date());
			reqHeader.setErhStatus("NEW");
			
			Set<RequestMaterials> reqMatSet=reqHeader.getRequestMaterials();
			for(RequestMaterials tempItem : reqMatSet) {
				Integer appQty=tempItem.getApprQty();
				tempItem.setApprQty(0);
				Optional<MaterialMaster> matMaster = masterRepo.findById(tempItem.getMatCode());
				if(matMaster.isPresent())
				{  
					MaterialMaster mat = matMaster.get();
					int blockQtyTemp=0;
					if(mat.getBlockQty() != null)
						blockQtyTemp=mat.getBlockQty();
					log.debug("blockQty::"+blockQtyTemp);
					log.debug("inputQty::"+appQty);
					log.debug("dbqty::"+mat.getQuantity());
					if(blockQtyTemp >= appQty)
					{	  
						int blockQty=0;
						if(mat.getBlockQty() != null)
							blockQty = blockQtyTemp-appQty;
						mat.setBlockQty(blockQty);
					}
				}
			}

			if(!"BD".equals(reqHeader.getErhReqType()))
				reqMaterialRepo.deleteByRequestId(docId);
			
		}	
	}

}
