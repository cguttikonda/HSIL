package com.ezc.hsil.webapp.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.ezc.hsil.webapp.model.DistributorMaster;

public class BDRequestDto  implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	@NotNull
	private String bdMatCode;
	private String bdMatDesc;
	@NotNull 
	private Integer bdQty;
	@NotNull
	private String erhDistrubutor;
	@NotNull
	private String erhPurpose;
	private List<DistributorMaster> distList=new ArrayList<DistributorMaster>();
	private List<MaterialQtyDto> matLoopList=new ArrayList<MaterialQtyDto>();
	public BDRequestDto() {
	}


	public String getBdMatCode() {
		return bdMatCode;
	}


	public void setBdMatCode(String bdMatCode) {
		this.bdMatCode = bdMatCode;
	}


	public String getBdMatDesc() {
		return bdMatDesc;
	}


	public void setBdMatDesc(String bdMatDesc) {
		this.bdMatDesc = bdMatDesc;
	}


	public Integer getBdQty() {
		return bdQty;
	}


	public void setBdQty(Integer bdQty) {
		this.bdQty = bdQty;
	}


	public String getErhDistrubutor() {
		return erhDistrubutor;
	}


	public void setErhDistrubutor(String erhDistrubutor) {
		this.erhDistrubutor = erhDistrubutor;
	}


	public String getErhPurpose() {
		return erhPurpose;
	}


	public void setErhPurpose(String erhPurpose) {
		this.erhPurpose = erhPurpose;
	}


	public List<DistributorMaster> getDistList() {
		return distList;
	}


	public void setDistList(List<DistributorMaster> distList) {
		this.distList = distList;
	}

	public List<MaterialQtyDto> getMatLoopList() {
		return matLoopList;
	}
	public void setMatLoopList(List<MaterialQtyDto> matLoopList) {
		this.matLoopList = matLoopList;
	}

}
