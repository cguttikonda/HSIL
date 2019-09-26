package com.ezc.hsil.webapp.dto;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class TpmRequestDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String erhState;
	private String erhRequestedBy;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date erhConductedOn;
	private Integer erhNoOfAttendee;
	private String erhDistrubutor;
	private String erhCity;
	private String erhInstructions;

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

	

}

