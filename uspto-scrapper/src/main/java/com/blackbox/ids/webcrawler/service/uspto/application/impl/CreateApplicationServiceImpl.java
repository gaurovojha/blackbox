package com.blackbox.ids.webcrawler.service.uspto.application.impl;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.mdm.PublicationDetails;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.webcrawler.CreateApplicationQueue;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.model.webcrawler.FindFamilyQueue;
import com.blackbox.ids.core.model.webcrawler.FindFamilyQueue.FamilyType;
import com.blackbox.ids.core.util.BlackboxPropertyUtil;
import com.blackbox.ids.core.webcrawler.dao.IWebCrawlerApplicationDAO;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.validation.NumberFormatValidationService;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.webcrawler.service.uspto.application.IApplicationWebCrawler;
import com.blackbox.ids.webcrawler.service.uspto.application.ICreateApplicationService;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;

@Service
public class CreateApplicationServiceImpl implements ICreateApplicationService {

	@Autowired
	private IWebCrawlerApplicationDAO		applicationDAO;

	@Autowired
	private NumberFormatValidationService	formatService;

	@Autowired
	private NotificationProcessService		notificationProcessService;

	@Autowired
	private IApplicationWebCrawler			webCrawler;

	@Autowired
	private IUSPTOLoginService				loginService;

	private static final Logger				LOGGER	= Logger.getLogger(CreateApplicationServiceImpl.class);

	@Override
	public void execute(final int maxRetryCount) {
		LOGGER.info("Web crawler for create-application started..");

		// step : fetch application staging records sorted by customer number
		Map<String, List<CreateApplicationQueue>> customerApplicationsMap = applicationDAO.getCreateApplicationList();

		if (MapUtils.isNotEmpty(customerApplicationsMap)) {
			initWebCrawler(customerApplicationsMap, maxRetryCount);
		} else {
			LOGGER.info("Create Application queue found empty.");
		}
		LOGGER.info("Web crawler for create-application completed.");
	}

	private void initWebCrawler(final Map<String, List<CreateApplicationQueue>> customerApplicationsMap,
			final int maxRetryCount) {
		WebDriver webDriver = null;
		for (Entry<String, List<CreateApplicationQueue>> entry : customerApplicationsMap.entrySet()) {
			try {
				// step1 : fetch list of create-application-requests for each customer
				String customer = entry.getKey();
				List<CreateApplicationQueue> createApplicationList = entry.getValue();

				LOGGER.info(MessageFormat.format("Started application(s) creation for Customer [{0}] ", customer));

				// step2 : create web driver
				webDriver = BBWebCrawlerUtils.getWebDriver(null);

				// step3 : authenticate customer
				boolean success = loginService.login(webDriver, customer);
				if (!success) {
					createApplicationList.stream().forEach(app -> updateErrorStatus(app, app.getRetryCount() + 1, maxRetryCount));
					continue;
				}

				// step4 : create applications
				for (CreateApplicationQueue app : createApplicationList) {
					createApplication(app, webDriver, maxRetryCount);
				}
			} finally {
				BBWebCrawlerUtils.logoutAndCloseWebDriver(webDriver, LOGGER);
			}
		}
	}

	private void createApplication(final CreateApplicationQueue app, final WebDriver webDriver, final int maxRetryCount) {
		// step1 : select new case
		webCrawler.selectNewCase(webDriver);

		Long id = app.getId();
		String appNumber = app.getAppNumberRaw();
		Calendar filingDate = app.getFilingDate();
		String jurisdiction = app.getJurisdictionCode();
		String customer = app.getCustomerNumber();
		int retryCount = app.getRetryCount() + 1;
		String convertedAppNumber = app.getApplicationNumberFormatted();

		LOGGER.info(MessageFormat.format(
				"Creating application [Queue Id -{0}, Application No.- {1} , Juridiction- {2}.", id, appNumber,
				jurisdiction));

		// step2 : check application number in exclusion list
		LOGGER.info("Checking application in Exclusion list");
		boolean existsInExclustions = applicationDAO.checkApplicationInExclustionList(convertedAppNumber, jurisdiction);
		if (existsInExclustions) {
			LOGGER.info("Application is found in Exclusion list, updating error status for the application in queue.");
			applicationDAO.updateCreateApplicationQueueStatus(id, QueueStatus.ERROR_APPLICATION_IN_EXCLUSION,
					retryCount);
			return;
		}

		// step3 : check application number in inclusion list
		checkInclusionList(appNumber, convertedAppNumber, jurisdiction, customer, filingDate);

		// step4 : Search for application
		webCrawler.searchApplicationNumber(webDriver, appNumber, jurisdiction);

		// step5 : check if application details page opened
		boolean isFindApplicationSuccess = webCrawler.checkApplicationDetailsPageStatus(webDriver, appNumber,
				jurisdiction, filingDate);

		// in case of error
		if (!isFindApplicationSuccess) {
			// error opening page
			LOGGER.info("Unable to open application details page. Updating status and retry count for the application.");
			updateErrorStatus(app, retryCount, maxRetryCount);
			return;
		}

		// step6 : read application details
		LOGGER.info("Finding application details..");
		ApplicationStage applicationStage = getApplicationInfo(webDriver, convertedAppNumber, jurisdiction);

		if (applicationStage == null) {
			// error reading application
			updateErrorStatus(app, retryCount, maxRetryCount);
			return;
		}

		// step7: update application info in staging
		Long applicationStageId = applicationDAO.createApplicationStageEntry(applicationStage);

		if (applicationStageId != null) {
			// step8: create entry in find parent queue
			LOGGER.info("Creating entry in find parent queue.");
			createFindParentEntry(app, applicationStageId);

			// step9: create entry in download_office_action queue
			LOGGER.info("Creating entry in download office action queue.");
			createDownloadOfficeActionEntry(app);

		} else {
			LOGGER.info("Application entry already exists in application Stage. Not entering record in Application-Stage, Find-Parent Queue and Download-Office-Action queue");
		}
		// step10: update status to completed
		applicationDAO.updateCreateApplicationQueueStatus(id, QueueStatus.SUCCESS, retryCount);
		LOGGER.info(MessageFormat.format("Application process completed successfully for Application : [{0}].",
				app.getApplicationNumberFormatted()));
	}

	private void updateErrorStatus(final CreateApplicationQueue app, final int retryCount, final int maxRetryCount) {
		if (retryCount >= maxRetryCount) {
			if (app.getBaseCaseApplicationQueueId() == null) {
				// application is base case
				LOGGER.info(MessageFormat.format("Application search tried {0} times, sending notification.",
						maxRetryCount));
				sendNotification(app);
				applicationDAO.updateCreateApplicationQueueStatus(app.getId(), QueueStatus.CRAWLER_ERROR, retryCount);
			} else {
				// find base case application
				CreateApplicationQueue baseCase = applicationDAO.getCreateApplicationQueueEntry(app
						.getBaseCaseApplicationQueueId());

				LOGGER.info(MessageFormat
						.format("Application search tried {2} times, sending notification for the base case[id:{0}, application number : {1}].",
								baseCase.getId(), baseCase.getApplicationNumberFormatted(), maxRetryCount));
				sendNotification(baseCase);
				applicationDAO.updateFamilyApplicationsStatus(baseCase.getId());
			}
		} else {
			applicationDAO.updateCreateApplicationQueueStatus(app.getId(), QueueStatus.INITIATED, retryCount);
		}
	}

	private void checkInclusionList(final String applicationNumberRaw, final String convertedAppNumber,
			final String jurisdiction, final String customer, final Calendar filingDate) {
		LOGGER.info("Checking application in Inclusion list..");
		// check if inclusion list is switched on
		boolean isInclusionListSwitchOn = StringUtils.equalsIgnoreCase(Constant.ON,
				BlackboxPropertyUtil.getProperty(Constant.INCLUSION_LIST_ON_FLAG));

		if (isInclusionListSwitchOn) {
			LOGGER.info("Updating Inclusion list for the application number.");
			applicationDAO.createInclusionListEntry(convertedAppNumber, applicationNumberRaw, jurisdiction, customer,
					filingDate);
		} else {
			LOGGER.info("Inclusion list is switched off.");
		}
	}

	private ApplicationStage getApplicationInfo(final WebDriver webDriver, final String convertedAppNumber,
			final String jurisdiction) {
		ApplicationStage applicationStage = webCrawler.readApplicationDetails(webDriver);
		applicationStage.setStatus(QueueStatus.INITIATED);

		if (applicationStage != null) {
			applicationStage.setApplicationNumber(convertedAppNumber);
			applicationStage.setJurisdiction(jurisdiction);
			PublicationDetails publicationDetails = applicationStage.getPublicationDetails();
			if (publicationDetails != null && publicationDetails.getPublicationNumberRaw() != null) {
				publicationDetails.setPublicationNumber(formatService.getConvertedValue(
						publicationDetails.getPublicationNumberRaw(), NumberType.PUBLICATION, jurisdiction, 
						ApplicationType.ALL, null, null));
			}
			
			if (StringUtils.isNotEmpty(applicationStage.getPatentDetails().getPatentNumberRaw())) {
				applicationStage.getPatentDetails().setPatentNumber(
						formatService.getConvertedValue(applicationStage.getPatentDetails().getPatentNumberRaw(),
								NumberType.PATENT, jurisdiction, ApplicationType.ALL, null, null));
			}
		}
		return applicationStage;
	}

	private void createFindParentEntry(final CreateApplicationQueue application, final Long applicationStageId) {
		FindFamilyQueue findParentEntry = new FindFamilyQueue();
		findParentEntry.setType(FamilyType.PARENT);
		findParentEntry.setApplicationNumberFormatted(application.getApplicationNumberFormatted());
		findParentEntry.setAppNumberRaw(application.getAppNumberRaw());
		findParentEntry.setCustomerNumber(application.getCustomerNumber());
		findParentEntry.setFilingDate(application.getFilingDate());
		findParentEntry.setJurisdictionCode(application.getJurisdictionCode());
		Long baseCaseId = (Long) ObjectUtils.defaultIfNull(application.getBaseCaseApplicationQueueId(),
				application.getId());
		findParentEntry.setBaseCaseApplicationQueueId(baseCaseId);
		findParentEntry.setApplicationStageId(applicationStageId);
		// save
		applicationDAO.createFindFamilyEntry(findParentEntry);
	}

	private void createDownloadOfficeActionEntry(final CreateApplicationQueue application) {
		DownloadOfficeActionQueue downloadOfficeAction = new DownloadOfficeActionQueue();
		downloadOfficeAction.setDocumentCode(Constant.ALL_DOCUMENT_CODE);
		downloadOfficeAction.setApplicationNumberFormatted(application.getApplicationNumberFormatted());
		downloadOfficeAction.setAppNumberRaw(application.getAppNumberRaw());
		downloadOfficeAction.setCustomerNumber(application.getCustomerNumber());
		downloadOfficeAction.setFilingDate(application.getFilingDate());
		downloadOfficeAction.setJurisdictionCode(application.getJurisdictionCode());
		// save
		applicationDAO.createDownloadOfficeActionEntry(downloadOfficeAction);
	}

	private void sendNotification(final CreateApplicationQueue app) {
		//TODO Create application notification, application attributes are not available here
		Long notificationProcessId = notificationProcessService.createNotification(null, null, null, app.getId(), 
				EntityName.CREATE_APPLICATION_QUEUE, NotificationProcessType.CREATE_APPLICATION_REQUEST, null, null);
		LOGGER.info(MessageFormat
				.format("Notification created for Create-Application-Request [notification process id : {0}, queue id : {1},  Application Number : {2}, Jurisdiction : {3}].",
						notificationProcessId, app.getId(), app.getApplicationNumberFormatted(),
						app.getJurisdictionCode()));
	}

}