package com.ezc.hsil.webapp.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ezc.hsil.webapp.model.EzcRequestDealers;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.RequestMaterials;


public class BDRequestDetailDto {

	
	Set<EzcRequestDealers> ezcRequestDealers = null;
	EzcRequestHeader reqHeader = new EzcRequestHeader();
	List<EzcRequestItems> ezcRequestItems=new ArrayList<EzcRequestItems>();
	List<RequestMaterials> ezReqMatList = null;
	private String contactNo="";
	private String city="";
	List purpose=null;
	private String recordedText="";
	
	
	
	public List<EzcRequestItems> getEzcRequestItems() {
		return ezcRequestItems;
	}
	public void setEzcRequestItems(List<EzcRequestItems> ezcRequestItems) {
		this.ezcRequestItems = ezcRequestItems;
	}
	public String getRecordedText() {
		return recordedText;
	}
	public void setRecordedText(String recordedText) {
		this.recordedText = recordedText;
	}
	public List<RequestMaterials> getEzReqMatList() {
		return ezReqMatList;
	}
	public void setEzReqMatList(List<RequestMaterials> ezReqMatList) {
		this.ezReqMatList = ezReqMatList;
	}
	public EzcRequestHeader getReqHeader() {
		return reqHeader;
	}
	public void setReqHeader(EzcRequestHeader reqHeader) {
		this.reqHeader = reqHeader;
	}
	public Set<EzcRequestDealers> getEzcRequestDealers() {
		return ezcRequestDealers;
	}
	public void setEzcRequestDealers(Set<EzcRequestDealers> ezcRequestDealers) {
		this.ezcRequestDealers = ezcRequestDealers;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public List getPurpose() {
		return purpose;
	}
	public void setPurpose(List purpose) {
		this.purpose = purpose;
	}
	
	
	
}
