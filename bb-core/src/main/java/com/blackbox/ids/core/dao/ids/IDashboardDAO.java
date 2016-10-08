package com.blackbox.ids.core.dao.ids;

import java.util.List;

import com.blackbox.ids.core.dto.IDS.dashboard.InitiateIDSRecordDTO;

public interface IDashboardDAO {

	List<InitiateIDSRecordDTO> getUrgentApplicationsInIDS();
	
	List<InitiateIDSRecordDTO> getAllApplicationsInIDS();
}
