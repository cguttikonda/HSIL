package com.ezc.hsil.webapp.dto;

import java.util.Date;
import java.util.List;

public class RequestHeaderDto {

	
	
	private int reqId; 
	private String reqtype;
	
	private Date reqCreatedOn;
	
	private String retailer;
	
	private String distributor;
	private String city;
	private String count;
	private String instructions;
	
	private List<RequestDetailDto> reqDtls;

	

	public RequestHeaderDto(int reqId, String reqtype, Date reqCreatedOn, String retailer, String distributor,
			String city, String count, String instructions, List<RequestDetailDto> reqDtls) {
		this.reqId = reqId;
		this.reqtype = reqtype;
		this.reqCreatedOn = reqCreatedOn;
		this.retailer = retailer;
		this.distributor = distributor;
		this.city = city;
		this.count = count;
		this.instructions = instructions;
		this.reqDtls = reqDtls;
	}

	public RequestHeaderDto() {

	}

	public String getReqtype() {
		return reqtype;
	}

	public void setReqtype(String reqtype) {
		this.reqtype = reqtype;
	}

	public Date getReqCreatedOn() {
		return reqCreatedOn;
	}

	public void setReqCreatedOn(Date reqCreatedOn) {
		this.reqCreatedOn = reqCreatedOn;
	}

	public String getRetailer() {
		return retailer;
	}

	public void setRetailer(String retailer) {
		this.retailer = retailer;
	}

	public String getDistributor() {
		return distributor;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCount() {
		return count;
	}

	public int getReqId() {
		return reqId;
	}

	public void setReqId(int reqId) {
		this.reqId = reqId;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public List<RequestDetailDto> getReqDtls() {
		return reqDtls;
	}

	public void setReqDtls(List<RequestDetailDto> reqDtls) {
		this.reqDtls = reqDtls;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + reqId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestHeaderDto other = (RequestHeaderDto) obj;
		if (reqId != other.reqId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RequestHeaderDto [reqId=" + reqId + ", reqtype=" + reqtype + ", reqCreatedOn=" + reqCreatedOn
				+ ", retailer=" + retailer + ", distributor=" + distributor + ", city=" + city + ", count=" + count
				+ ", instructions=" + instructions + ", reqDtls=" + reqDtls + "]";
	}
	
	
	
	
	
	
	
	
	
}
