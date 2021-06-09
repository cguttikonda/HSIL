package com.ezc.hsil.webapp.model;

import java.io.Serializable;

public class MaterialMasterKey implements Serializable{

	
	public MaterialMasterKey(String materialCode, String stockLoc) {
		super();
		this.materialCode = materialCode;
		this.stockLoc = stockLoc;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MaterialMasterKey() {
		
	}
	private String materialCode;
	private String stockLoc;
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getStockLoc() {
		return stockLoc;
	}
	public void setStockLoc(String stockLoc) {
		this.stockLoc = stockLoc;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((materialCode == null) ? 0 : materialCode.hashCode());
		result = prime * result + ((stockLoc == null) ? 0 : stockLoc.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MaterialMasterKey other = (MaterialMasterKey) obj;
		if (materialCode == null) {
			if (other.materialCode != null)
				return false;
		} else if (!materialCode.equals(other.materialCode))
			return false;
		if (stockLoc == null) {
			if (other.stockLoc != null)
				return false;
		} else if (!stockLoc.equals(other.stockLoc))
			return false;
		return true;
	}
	
	
	
		
	
	}
