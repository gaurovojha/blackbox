package com.blackbox.ids.webcrawler.service.uspto.application.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.model.mdm.ApplicationDetails;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.PatentDetails;
import com.blackbox.ids.core.model.mdm.PublicationDetails;
import com.blackbox.ids.webcrawler.service.uspto.application.IApplicationWebCrawler;

public class ApplicationWebCrawlerImpl implements IApplicationWebCrawler{


	@Override
	public void selectNewCase(WebDriver webDriver) {
	return;
		
	}
	int i=0;

	@Override
	public boolean checkApplicationDetailsPageStatus(WebDriver webDriver, String applicationNumber,
			String jurisdiction, Calendar filingDate) {
	        return true;
	}

	@Override
	public void searchApplicationNumber(WebDriver webDriver, String applicationNumber, String jurisdiction) {
		return;
		
	}

	@Override
	public ApplicationStage readApplicationDetails(WebDriver webDriver) {
		ApplicationStage applicationStage = populateApplicationDetails();
		return  applicationStage;
	}

	private ApplicationStage populateApplicationDetails() {
		ApplicationStage applicationStage = new ApplicationStage();
		applicationStage.setUpdatedByUser(new Long(-1));
		applicationStage.setUpdatedDate(Calendar.getInstance());
		applicationStage.setCreatedByUser(new Long(-1));
		applicationStage.setCreatedDate(Calendar.getInstance());
		applicationStage.setApplicationNumber("12968124");
		applicationStage.setFamilyId("DEFAULT_FAMILY");
		PatentDetails patentDetails = applicationStage.getPatentDetails();
		patentDetails.setArtUnit("2156");
		patentDetails.setFirstNameInventor("Naresh Sundaram , Redmond, WA (US) ");
		patentDetails.setIssuedOn(Calendar.getInstance());
		patentDetails.setPatentNumberRaw("US 2012-0150917 A1");
		patentDetails.setTitle("USAGE-OPTIMIZED TABLES");
		patentDetails.setExaminer("LU, KUEN S");
		applicationStage.setPatentDetails(patentDetails);;
		PublicationDetails publicationDetails= applicationStage.getPublicationDetails();
		publicationDetails.setPublicationNumberRaw("US 2012-0150917 A1");
		publicationDetails.setPublishedOn(Calendar.getInstance());
		applicationStage.setPublicationDetails(publicationDetails);
		ApplicationDetails applicationDetails = applicationStage.getAppDetails();
		applicationDetails.setApplicationNumberRaw("12968124");
		applicationDetails.setFilingDate(Calendar.getInstance());
		applicationDetails.setChildApplicationType(ApplicationType.PCT_US_FIRST_FILING);
		return applicationStage;
	}

	@Override
	public boolean checkContinuityTab(WebDriver webDriver, boolean isParents) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, String> readParentApplications(WebDriver webDriver) {
		
		Map<String, String> map = new java.util.HashMap<String, String>();
		map.put("PCT/US14/65656", "01-14-2016");
		return map;
	}

	@Override
	public boolean checkForeignPriorityTab(WebDriver webDriver) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, String> readChildApplications(WebDriver webDriver) {
		Map<String, String> map = new java.util.HashMap<String, String>(); 
		map.put("PCT/US15/65656", "01-14-2016");
		return map;
	}

	@Override
	public List<List<String>> readForeignPriorities(WebDriver webDriver) {
		List<String> sList = new ArrayList<>();
		sList.add("14310489");
		sList.add("US");
		sList.add("01-14-2016");
		
		sList.add("PCT/UW14/041615");
		sList.add("US");
		sList.add("01-14-2016");
		
		List<List<String>> list = new ArrayList<>();
		list.add(sList);
		return list;
	}

	@Override
	public boolean isErrorFound(WebDriver webDriver) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public MdmRecordDTO getAppMetaData(WebDriver webDriver, String applicationumber) {
		// TODO Auto-generated method stub
		return null;
	}
}
