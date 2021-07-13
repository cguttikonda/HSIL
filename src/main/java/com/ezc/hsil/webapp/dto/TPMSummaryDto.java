package com.ezc.hsil.webapp.dto;

import java.util.Date;

public class TPMSummaryDto {

	private Date requestDate;
	private String requestId;
	private String zone;
	private String month;
	private String state;
	private String salesPersonName;
	private String distCode;
	private String distName;
	private String empCode;
	private String empName;
	private String city;
	private String retailerName;
	private String reportingManager;
	private String zonalHead;
	private Integer noOfAtt;
	private Double expense;
	private Double avgCost;
	private Integer giftsRequested;
	private Integer pendingGifts;
	private String vertical;
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSalesPersonName() {
		return salesPersonName;
	}
	public void setSalesPersonName(String salesPersonName) {
		this.salesPersonName = salesPersonName;
	}
	public String getDistCode() {
		return distCode;
	}
	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}
	public String getDistName() {
		return distName;
	}
	public void setDistName(String distName) {
		this.distName = distName;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRetailerName() {
		return retailerName;
	}
	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}
	public String getReportingManager() {
		return reportingManager;
	}
	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}
	public String getZonalHead() {
		return zonalHead;
	}
	public void setZonalHead(String zonalHead) {
		this.zonalHead = zonalHead;
	}
	public Integer getNoOfAtt() {
		return noOfAtt;
	}
	public void setNoOfAtt(Integer noOfAtt) {
		this.noOfAtt = noOfAtt;
	}
	public Double getExpense() {
		return expense;
	}
	public void setExpense(Double expense) {
		this.expense = expense;
	}
	public Double getAvgCost() {
		return avgCost;
	}
	public void setAvgCost(Double avgCost) {
		this.avgCost = avgCost;
	}
	public Integer getGiftsRequested() {
		return giftsRequested;
	}
	public void setGiftsRequested(Integer giftsRequested) {
		this.giftsRequested = giftsRequested;
	}
	public Integer getPendingGifts() {
		return pendingGifts;
	}
	public void setPendingGifts(Integer pendingGifts) {
		this.pendingGifts = pendingGifts;
	}
	/**
	 * @return the vertical
	 */
	public String getVertical() {
		return vertical;
	}
	/**
	 * @param vertical the vertical to set
	 */
	public void setVertical(String vertical) {
		this.vertical = vertical;
	}
	
	
	
}
