package com.ezc.hsil.webapp.dto;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.ezc.hsil.webapp.model.DistributorMaster;

public class TpmRequestDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String erhState;
	private String erhRequestedBy;
	@NotNull
	@DateTimeFormat(pattern = "MM/yyyy")
	private Date erhConductedOn;
	@NotNull 
	@Min(value = 1)
	private Integer noOfMeets;

	private Integer erhNoOfAttendee;
	@NotNull
	private String erhDistrubutor;
	@NotNull
	@Size(min=0, max=40)
	private String erhCity;
	private String erhInstructions;

	private List<DistributorMaster> distList=new ArrayList<DistributorMaster>();
	
	@Valid
	private List<TPMMeetDto> meetList=new ArrayList<TPMMeetDto>();
	
	public TpmRequestDto() {
	}

	public String getErhState() {
		return erhState;
	}

	public void setErhState(String erhState) {
		this.erhState = erhState;
	}

	public String getErhRequestedBy() {
		return erhRequestedBy;
	}

	public void setErhRequestedBy(String erhRequestedBy) {
		this.erhRequestedBy = erhRequestedBy;
	}

	public Date getErhConductedOn() {
		return erhConductedOn;
	}

	public void setErhConductedOn(Date erhConductedOn) {
		this.erhConductedOn = erhConductedOn;
	}

	public Integer getErhNoOfAttendee() {
		return erhNoOfAttendee;
	}

	public void setErhNoOfAttendee(Integer erhNoOfAttendee) {
		this.erhNoOfAttendee = erhNoOfAttendee;
	}

	public String getErhDistrubutor() {
		return erhDistrubutor;
	}

	public void setErhDistrubutor(String erhDistrubutor) {
		this.erhDistrubutor = erhDistrubutor;
	}

	public String getErhCity() {
		return erhCity;
	}

	public void setErhCity(String erhCity) {
		this.erhCity = erhCity;
	}

	public String getErhInstructions() {
		return erhInstructions;
	}

	public void setErhInstructions(String erhInstructions) {
		this.erhInstructions = erhInstructions;
	}

	public List<DistributorMaster> getDistList() {
		return distList;
	}

	public void setDistList(List<DistributorMaster> distList) {
		this.distList = distList;
	}

	public Integer getNoOfMeets() {
		return noOfMeets;
	}

	public void setNoOfMeets(Integer noOfMeets) {
		this.noOfMeets = noOfMeets;
	}

	public List<TPMMeetDto> getMeetList() {
		return meetList;
	}

	public void setMeetList(List<TPMMeetDto> meetList) {
		this.meetList = meetList;
	}
	
	

}

