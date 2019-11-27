package com.ezc.hsil.webapp.dto;

import javax.validation.constraints.NotNull;

public class BDRequestDto  implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	@NotNull
	private String bdMatCode;
	private String bdMatDesc;
	@NotNull 
	private Integer bdQty;
	
	
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


}
