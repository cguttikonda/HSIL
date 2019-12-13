package com.ezc.hsil.webapp.dto;

import java.util.List;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.ezc.hsil.webapp.model.DistributorMaster;

public class ReportSelector {

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fromDate;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date toDate;
	
	private String status;
	private String type;
	
	private List<Object[]> userGrp;
	private List<Object[]> hdGrp;
	private List<DistributorMaster> distList;
	private String selUser;
	private String selStHd;
	private String selDist;
	
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
	public String getSelStHd() {
		return selStHd;
	}
	public void setSelStHd(String selStHd) {
		this.selStHd = selStHd;
	}
	public List<DistributorMaster> getDistList() {
		return distList;
	}
	public void setDistList(List<DistributorMaster> distList) {
		this.distList = distList;
	}
	public String getSelDist() {
		return selDist;
	}
	public void setSelDist(String selDist) {
		this.selDist = selDist;
	}
	
	
		
}
