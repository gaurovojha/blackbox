package com.blackbox.ids.webcrawler.service.uspto.application;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.model.mdm.ApplicationStage;

public interface IApplicationWebCrawler {

	void selectNewCase(WebDriver webDriver);

	boolean checkApplicationDetailsPageStatus(WebDriver webDriver, String applicationNumber, String jurisdiction,
			Calendar filingDate);

	void searchApplicationNumber(WebDriver webDriver, String applicationNumber, String jurisdiction);

	ApplicationStage readApplicationDetails(WebDriver webDriver);

	boolean checkContinuityTab(WebDriver webDriver, boolean isParents);

	Map<String, String> readParentApplications(WebDriver webDriver);

	boolean checkForeignPriorityTab(WebDriver webDriver);

	Map<String, String> readChildApplications(WebDriver webDriver);

	List<List<String>> readForeignPriorities(WebDriver webDriver);
		
	boolean isErrorFound(WebDriver webDriver);
	
	MdmRecordDTO getAppMetaData(WebDriver webDriver , String applicationumber);

}
