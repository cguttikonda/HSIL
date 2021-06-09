package com.ezc.hsil.webapp.dto;

public class MaterialQtyDto {
	private String matCode;
	private String matDesc;
	private String stockLoc;
	private int qty;
	public String getMatCode() {
		return matCode;
	}
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
	public String getMatDesc() {
		return matDesc;
	}
	public void setMatDesc(String matDesc) {
		this.matDesc = matDesc;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	/**
	 * @return the stockLoc
	 */
	public String getStockLoc() {
		return stockLoc;
	}
	/**
	 * @param stockLoc the stockLoc to set
	 */
	public void setStockLoc(String stockLoc) {
		this.stockLoc = stockLoc;
	}
	
}
