package com.ezc.hsil.webapp.dto;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ListSelector {

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fromDate;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date toDate;
	
	private String status;
	private Character dispStatus;
	private String type;
	
	private ArrayList<String> user;
	private ArrayList<String> typeList;
	private String sentTo;
	
	
	
	
	
	public String getSentTo() {
		return sentTo;
	}

	public void setSentTo(String sentTo) {
		this.sentTo = sentTo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
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

	public ArrayList<String> getUser() {
		return user;
	}

	public void setUser(ArrayList<String> user) {
		this.user = user;
	}

	public ArrayList<String> getTypeList() {
		return typeList;
	}

	public void setTypeList(ArrayList<String> typeList) {
		this.typeList = typeList;
	}

	public Character getDispStatus() {
		return dispStatus;
	}

	public void setDispStatus(Character dispStatus) {
		this.dispStatus = dispStatus;
	}
	
	
}
