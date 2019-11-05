package com.ezc.hsil.webapp.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ListSelector {

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fromDate;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date toDate;
	
	private String status;

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
	
	
}
