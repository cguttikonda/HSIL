package com.ezc.hsil.webapp.service;

import java.util.List;
import java.util.Set;

import com.ezc.hsil.webapp.dto.ListSelector;
import com.ezc.hsil.webapp.dto.TpmRequestDetailDto;
import com.ezc.hsil.webapp.model.EzcRequestHeader;
import com.ezc.hsil.webapp.model.RequestMaterials;

public interface IReportService {
	public List<EzcRequestHeader> getRequestStatus(ListSelector listSelector);
}
