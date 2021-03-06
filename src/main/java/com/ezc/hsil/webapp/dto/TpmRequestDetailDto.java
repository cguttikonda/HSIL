package com.ezc.hsil.webapp.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ezc.hsil.webapp.model.EzcRequestDealers;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.EzcRequestItems;
import com.ezc.hsil.webapp.model.RequestMaterials;
import com.ezc.hsil.webapp.model.EzcComments;

public class TpmRequestDetailDto {

	EzcRequestHeader reqHeader = new EzcRequestHeader();
	List<EzcRequestItems> ezcRequestItems=new ArrayList<EzcRequestItems>();
//	Set<RequestMaterials> ezReqMatList = new HashSet<RequestMaterials>();
	List<RequestMaterials> ezReqMatList = null;
	List<EzcRequestDealers> ezcRequestDealers = null;
	EzcRequestDealers meetDealer = null;
	List<EzcComments> ezcComments =null;
	private String costIncured="";
	private String avgSales="";
	private String recordedText="";
	private String commentReqDto="";
	
	public TpmRequestDetailDto()
	{
	}

	
	


	public String getCommentReqDto() {
		return commentReqDto;
	}





	public void setCommentReqDto(String commentReqDto) {
		this.commentReqDto = commentReqDto;
	}





	public EzcRequestHeader getReqHeader() {
		return reqHeader;
	}

	public void setReqHeader(EzcRequestHeader reqHeader) {
		this.reqHeader = reqHeader;
	}

	
	

	public List<EzcComments> getEzcComments() {
		return ezcComments;
	}

	public void setEzcComments(List<EzcComments> ezcComments) {
		this.ezcComments = ezcComments;
	}

	public List<RequestMaterials> getEzReqMatList() {
		return ezReqMatList;
	}

	public void setEzReqMatList(List<RequestMaterials> ezReqMatList) {
		this.ezReqMatList = ezReqMatList;
	}

	public List<EzcRequestItems> getEzcRequestItems() {
		return ezcRequestItems;
	}

	public void setEzcRequestItems(List<EzcRequestItems> ezcRequestItems) {
		this.ezcRequestItems = ezcRequestItems;
	}

	public String getCostIncured() {
		return costIncured;
	}

	public void setCostIncured(String costIncured) {
		this.costIncured = costIncured;
	}

	public String getAvgSales() {
		return avgSales;
	}

	public void setAvgSales(String avgSales) {
		this.avgSales = avgSales;
	}

	public List<EzcRequestDealers> getEzcRequestDealers() {
		return ezcRequestDealers;
	}

	public void setEzcRequestDealers(List<EzcRequestDealers> ezcRequestDealers) {
		this.ezcRequestDealers = ezcRequestDealers;
	}

	public String getRecordedText() {
		return recordedText;
	}

	public void setRecordedText(String recordedText) {
		this.recordedText = recordedText;
	}





	public EzcRequestDealers getMeetDealer() {
		return meetDealer;
	}





	public void setMeetDealer(EzcRequestDealers meetDealer) {
		this.meetDealer = meetDealer;
	}


	
	
}
