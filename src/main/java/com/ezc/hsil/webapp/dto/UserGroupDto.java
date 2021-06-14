package com.ezc.hsil.webapp.dto;

import java.util.List;

import com.ezc.hsil.webapp.model.Work_Groups;

public class UserGroupDto {

	private String grpType;
	private List<Work_Groups> groupList;
	/**
	 * @return the grpType
	 */
	public String getGrpType() {
		return grpType;
	}
	/**
	 * @param grpType the grpType to set
	 */
	public void setGrpType(String grpType) {
		this.grpType = grpType;
	}
	/**
	 * @return the groupList
	 */
	public List<Work_Groups> getGroupList() {
		return groupList;
	}
	/**
	 * @param groupList the groupList to set
	 */
	public void setGroupList(List<Work_Groups> groupList) {
		this.groupList = groupList;
	}
	
	
}
