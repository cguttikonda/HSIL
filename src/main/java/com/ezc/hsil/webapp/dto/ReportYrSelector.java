package com.ezc.hsil.webapp.dto;

import java.util.List;

public class ReportYrSelector {

	private Integer selYear;
	private List<Integer> yearArr;
	public Integer getSelYear() {
		return selYear;
	}
	public void setSelYear(Integer selYear) {
		this.selYear = selYear;
	}
	public List<Integer> getYearArr() {
		return yearArr;
	}
	public void setYearArr(List<Integer> yearArr) {
		this.yearArr = yearArr;
	}
	
}
