package com.ezc.hsil.webapp.service;

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
			Optional<MaterialMaster> matMaster = matRepo.findById(matArr[0]);
	        if(matMaster.isPresent())
	        {  
	               MaterialMaster mat = matMaster.get();
	               int stockChk = (mat.getQuantity()-mat.getBlockQty())-matDto.getQty();
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
			ezcMktGiveAway.setSentToName(mktgGiveAwayDto.getSentToName());
			ezcMktGiveAway.setMatCode(matArr[0]);
			ezcMktGiveAway.setQty(matDto.getQty());
			ezcMktGiveAway.setMatDesc(matArr[1]);
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

}
