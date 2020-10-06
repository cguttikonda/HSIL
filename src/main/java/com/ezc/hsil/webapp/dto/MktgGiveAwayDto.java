package com.ezc.hsil.webapp.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ezc.hsil.webapp.model.DistributorMaster;
import com.ezc.hsil.webapp.model.Users;

public class MktgGiveAwayDto {

	private String purpose;
	private String sentTo;
	private String vehNo;
	private String sentToName;
	private String distrubutor;
	private String distName;
	private String createdBy;
	private Date createdOn;
	private List<MaterialQtyDto> matList=new ArrayList<MaterialQtyDto>();
	
	private List<DistributorMaster> distList=new ArrayList<DistributorMaster>();
	private List<Users> userList =new ArrayList<Users>();
	
	
	public List<DistributorMaster> getDistList() {
		return distList;
	}
	public void setDistList(List<DistributorMaster> distList) {
		this.distList = distList;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getSentTo() {
		return sentTo;
	}
	public void setSentTo(String sentTo) {
		this.sentTo = sentTo;
	}
	public String getSentToName() {
		return sentToName;
	}
	public void setSentToName(String sentToName) {
		this.sentToName = sentToName;
	}
	public String getDistrubutor() {
		return distrubutor;
	}
	public void setDistrubutor(String distrubutor) {
		this.distrubutor = distrubutor;
	}
	public String getDistName() {
		return distName;
	}
	public void setDistName(String distName) {
		this.distName = distName;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	public List<Users> getUserList() {
		return userList;
	}
	public void setUserList(List<Users> userList) {
		this.userList = userList;
	}
	public List<MaterialQtyDto> getMatList() {
		return matList;
	}
	public void setMatList(List<MaterialQtyDto> matList) {
		this.matList = matList;
	}
	public String getVehNo() {
		return vehNo;
	}
	public void setVehNo(String vehNo) {
		this.vehNo = vehNo;
	}
	
	
	
	
	
}
