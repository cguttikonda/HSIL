package com.ezc.hsil.webapp.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.ezc.hsil.webapp.model.EzcRequestDealers;

public class TpsRequestDto implements java.io.Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private String requestedBy;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date plannedOn;
	private Integer noOfAttendee;
	private Integer noOfRetailers;
	private String distrubutor;
	private String city;
	private String instructions;
	private List<EzcRequestDealers> dealerName;
	
	public TpsRequestDto() {
	}
	public String getRequestedBy() {
		return requestedBy;
	}
	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}
	public Date getPlannedOn() {
		return plannedOn;
	}
	public void setPlannedOn(Date plannedOn) {
		this.plannedOn = plannedOn;
	}
	public Integer getNoOfRetailers() {
		return noOfRetailers;
	}
	public void setNoOfRetailers(Integer noOfRetailers) {
		this.noOfRetailers = noOfRetailers;
	}
	public String getDistrubutor() {
		return distrubutor;
	}
	public void setDistrubutor(String distrubutor) {
		this.distrubutor = distrubutor;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	public Integer getNoOfAttendee() {
		return noOfAttendee;
	}
	public void setNoOfAttendee(Integer noOfAttendee) {
		this.noOfAttendee = noOfAttendee;
	}
	public List<EzcRequestDealers> getDealerName() {
		return dealerName;
	}
	public void setDealerName(List<EzcRequestDealers> dealerName) {
		this.dealerName = dealerName;
	}
	
	
	
}
