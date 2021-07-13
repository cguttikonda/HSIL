package com.ezc.hsil.webapp.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.MaterialQtyDto;
import com.ezc.hsil.webapp.dto.MktgGiveAwayDto;
import com.ezc.hsil.webapp.dto.RequestCustomDto;
import com.ezc.hsil.webapp.model.EzcMktGiveAway;
import com.ezc.hsil.webapp.model.MaterialMaster;
import com.ezc.hsil.webapp.model.MaterialMasterKey;
import com.ezc.hsil.webapp.persistance.dao.MaterialMasterRepo;
import com.ezc.hsil.webapp.persistance.dao.MktgGiveAwayRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class MKTGGiveAwyService implements IMKTGGiveAwyService {

	private static final Exception Exception = null;

	@Autowired
    private RequestCustomDto customDto;
	
	@Autowired
    private MktgGiveAwayRepo mktgGiveAwayRepo;
	
	@Autowired
    private IMasterService masterService;
	
	@Autowired
    private MaterialMasterRepo matRepo;
	
	@Override
	public void createMKTData(MktgGiveAwayDto mktgGiveAwayDto)throws Exception 
	{
		
		List<MaterialQtyDto> matList=mktgGiveAwayDto.getMatList();
		
		for(MaterialQtyDto matDto:matList)
		{
			if(matDto.getMatCode() == null || "null".equals(matDto.getMatCode()) || "".equals(matDto.getMatCode()))
				continue;
			String [] matArr=matDto.getMatCode().split("#");
			
			//Updating stock
			MaterialMasterKey matMasterKey=new MaterialMasterKey(matArr[0], mktgGiveAwayDto.getOutstore());
			Optional<MaterialMaster> matMaster = matRepo.findById(matMasterKey);
			
	        if(matMaster.isPresent())
	        {  
	        	   MaterialMaster mat = matMaster.get();
	        	   int blockQty=0;
	        	   if(mat.getBlockQty() != null)
	        		   blockQty=mat.getBlockQty();
	               log.debug("blockQty::"+blockQty);
	               log.debug("inputQty::"+matDto.getQty());
	               log.debug("dbqty::"+mat.getQuantity());
	               int stockChk = (mat.getQuantity()-blockQty)-matDto.getQty();
	               if(stockChk < 0)
	            	   throw Exception;	//need to be changed to custom exception
	               else
	            	   mat.setQuantity(mat.getQuantity()-matDto.getQty());
	        }
			//saving details
			EzcMktGiveAway ezcMktGiveAway=new EzcMktGiveAway();
			ezcMktGiveAway.setCreatedBy(mktgGiveAwayDto.getCreatedBy());
			ezcMktGiveAway.setCreatedOn(new java.util.Date());
			ezcMktGiveAway.setDistName(mktgGiveAwayDto.getDistName());
			ezcMktGiveAway.setDistrubutor(mktgGiveAwayDto.getDistrubutor());
			ezcMktGiveAway.setPurpose(mktgGiveAwayDto.getPurpose());
			ezcMktGiveAway.setSentTo(mktgGiveAwayDto.getSentTo());
			ezcMktGiveAway.setVehNo(mktgGiveAwayDto.getVehNo());
			ezcMktGiveAway.setSentToName(mktgGiveAwayDto.getSentToName());
			ezcMktGiveAway.setMatCode(matArr[0]);
			ezcMktGiveAway.setQty(matDto.getQty());
			ezcMktGiveAway.setMatDesc(matArr[1]);
			if("Marketing Giveaway".equals(mktgGiveAwayDto.getPurpose()))
				ezcMktGiveAway.setStatus("A");
			else
				ezcMktGiveAway.setStatus("C");
			ezcMktGiveAway.setVertical(mktgGiveAwayDto.getVertical());
			try {
				ezcMktGiveAway.setPrdInv(Double.parseDouble(mktgGiveAwayDto.getPrdInv()));
			} catch (Exception e) {
				
			}
			mktgGiveAwayRepo.save(ezcMktGiveAway);
			
			
		}
		

	}

	@Override
	public List<EzcMktGiveAway> getMKTGListByDate(ListSelector listSelector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EzcMktGiveAway> getRequestList(ListSelector listSelector) {
		return customDto.findMktGiveAwayList(listSelector);
	}

	@Override
	public void acknowledgeRequest(Integer id, String ackBy,String ackComments) {
		Optional<EzcMktGiveAway> mktGiveAway = mktgGiveAwayRepo.findById(id);
		if(mktGiveAway.isPresent())
        {  
			EzcMktGiveAway mktGiveAwayObj = mktGiveAway.get();
			mktGiveAwayObj.setStatus("A");
			mktGiveAwayObj.setAckComments(ackComments);
			mktGiveAwayObj.setModifiedBy(ackBy);
			mktGiveAwayObj.setModifiedOn(new Date());
        }
	}

	@Override
	public EzcMktGiveAway getRequestDetails(Integer id) {
		Optional<EzcMktGiveAway> mktGiveAway = mktgGiveAwayRepo.findById(id);
		if(mktGiveAway.isPresent())
        {  
			EzcMktGiveAway mktGiveAwayObj = mktGiveAway.get();
			return mktGiveAwayObj;
        }
		else
			return null;
		
	}

}
