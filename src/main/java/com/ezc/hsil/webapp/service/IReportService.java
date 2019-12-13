package com.ezc.hsil.webapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.ReportSelector;
import com.ezc.hsil.webapp.model.EzcRequestHeader;

public interface IReportService {
	public List<EzcRequestHeader> getRequestStatus(ListSelector listSelector);
	public List<Object[]> getDispatchReport(String statu);
	public void dispatchUpdate(EzcRequestHeader ezcRequestHeader);
	public Map<String,String> getDashBoardValues(ArrayList<String> role,String user);
	public List<Object[]> getStockAvailabilityForAll();
	public List<Object[]> getToAckDispReport(String user);
	public void dispatchAckUpdate(EzcRequestHeader ezcRequestHeader);
	public List<Object[]> getUsersByHead(String userId);
	public List<EzcRequestHeader> getTeamTPMReport(ReportSelector reportSelector);
	public List<Object[]> getUsersByZoneHd(String userId);
	public List<Object[]> getStockAvailabilityByUser(List<String> userList);
	public List<Object[]> getStateHdByZoneHd(String userId);
	public List<Object[]> getAllStateHd();
	public List<Object[]> getAllUsers();
}
