package com.ezc.hsil.webapp.dto;

import java.util.List;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ReportSelector {

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fromDate;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date toDate;
	
	private String status;
	private String type;
	
	private List<Object[]> userGrp;
	private List<Object[]> hdGrp;
	private String selUser;
	
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Object[]> getUserGrp() {
		return userGrp;
	}
	public void setUserGrp(List<Object[]> userGrp) {
		this.userGrp = userGrp;
	}
	public List<Object[]> getHdGrp() {
		return hdGrp;
	}
	public void setHdGrp(List<Object[]> hdGrp) {
		this.hdGrp = hdGrp;
	}
	public String getSelUser() {
		return selUser;
	}
	public void setSelUser(String selUser) {
		this.selUser = selUser;
	}
		
		
}
