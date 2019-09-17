package com.ezc.hsil.webapp.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ezc.hsil.webapp.model.EzcRequestItems;

public class RequestDetailDto {

	
	private String plumberName;
	private String contactNo;
	
	private Date dob;
	
	private Date doa;
	private Double costIncurred;
	
	private int id;
	
	private int reqId;
	
	private List<EzcRequestItems> items = new ArrayList<>();

	public List<EzcRequestItems> getItems() {
		return items;
	}



	public void setItems(List<EzcRequestItems> items) {
		this.items = items;
	}

	 public void adddetail(EzcRequestItems item) {
	        this.items.add(item);
	 }

	public RequestDetailDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public RequestDetailDto(String plumberName, String contactNo, Date dob, Date doa, Double costIncurred, int id) {
		super();
		this.plumberName = plumberName;
		this.contactNo = contactNo;
		this.dob = dob;
		this.doa = doa;
		this.costIncurred = costIncurred;
		this.id = id;
	}



	public String getPlumberName() {
		return plumberName;
	}

	public void setPlumberName(String plumberName) {
		this.plumberName = plumberName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Date getDoa() {
		return doa;
	}

	public void setDoa(Date doa) {
		this.doa = doa;
	}

	public Double getCostIncurred() {
		return costIncurred;
	}

	public void setCostIncurred(Double costIncurred) {
		this.costIncurred = costIncurred;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReqId() {
		return reqId;
	}

	public void setReqId(int reqId) {
		this.reqId = reqId;
	}

	@Override
	public String toString() {
		return "RequestDetailDto [plumberName=" + plumberName + ", contactNo=" + contactNo + ", dob=" + dob + ", doa="
				+ doa + ", costIncurred=" + costIncurred + ", id=" + id + ", reqId=" + reqId + "]";
	}
	
	
	
	
	
	
	
	
	
	
}
