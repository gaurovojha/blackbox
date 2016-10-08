package com.blackbox.ids.webcrawler.service.uspto.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationStatusData;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.exception.WebCrawlerGenericException;

public interface IApplicationListValidationService {
	
	void execute(WebCrawlerJobAudit webCrawlerJobAudit) throws InterruptedException, ApplicationException, WebCrawlerGenericException;
	
	Map<String, ArrayList<Long>> compareCustomerApplicationNumbers(final Map<String, List<ApplicationStatusData>> customerApplicationData, final List<ApplicationBase> mdmApplicationBaseList,
			final List<String> mdmApplicationNumbers, final List<String> createQApplicationNumbers, final List<String> stageApplicationNumbers);
}
