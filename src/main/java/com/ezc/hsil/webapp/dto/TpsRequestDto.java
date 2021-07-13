package com.ezc.hsil.webapp.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.ezc.hsil.webapp.model.EzPlaceMaster;
import com.ezc.hsil.webapp.model.EzcRequestDealers;

public class TpsRequestDto implements java.io.Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private String requestedBy;
	
	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date plannedOn;
	@NotNull
	@Min(value = 1)
	private Integer noOfAttendee;
	@NotNull
	private Integer noOfRetailers;
	@NotNull
	private String distrubutor;
	@NotNull
	private String erhVertical;
	@NotNull
	@Size(min=0, max=40)
	private String city;
	private String organisation;
	private String instructions;
	@NotEmpty
	private List<EzcRequestDealers> dealerName;
	
	private List<EzPlaceMaster> placeList=new ArrayList<EzPlaceMaster>();
	
	List<String> userCatList=new ArrayList<String>();
	
	public List<EzPlaceMaster> getPlaceList() {
		return placeList;
	}
	public void setPlaceList(List<EzPlaceMaster> placeList) {
		this.placeList = placeList;
	}
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
	
	
	public String getOrganisation() {
		return organisation;
	}
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
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
	public String getErhVertical() {
		return erhVertical;
	}
	public void setErhVertical(String erhVertical) {
		this.erhVertical = erhVertical;
	}
	public List<String> getUserCatList() {
		return userCatList;
	}
	public void setUserCatList(List<String> userCatList) {
		this.userCatList = userCatList;
	}
	
	
	
}
