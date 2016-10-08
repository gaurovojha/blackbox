package com.blackbox.ids.webcrawler.service.uspto.application.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.ApplicationStageDAO;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.ApplicationValidationStatus;
import com.blackbox.ids.core.model.mdm.IncompatibleAttribute.Jurisdiction;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.webcrawler.CreateApplicationQueue;
import com.blackbox.ids.core.model.webcrawler.FindFamilyQueue;
import com.blackbox.ids.core.model.webcrawler.FindFamilyQueue.FamilyType;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.webcrawler.dao.IWebCrawlerApplicationDAO;
import com.blackbox.ids.services.mdm.ApplicationService;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.validation.NumberFormatValidationService;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.webcrawler.service.uspto.application.IApplicationWebCrawler;
import com.blackbox.ids.webcrawler.service.uspto.application.ITrackFamilyService;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;

@Service
public class TrackFamilyServiceImpl implements ITrackFamilyService {

	@Autowired
	private IWebCrawlerApplicationDAO		applicationDAO;

	@Autowired
	private IApplicationWebCrawler			webCrawler;

	@Autowired
	private NotificationProcessService		notificationProcessService;

	@Autowired
	private ApplicationBaseDAO				applicationBaseDAO;

	@Autowired
	private IUSPTOLoginService				loginService;

	@Autowired
	private NumberFormatValidationService	formatService;

	private static final Logger				LOGGER			= Logger.getLogger(TrackFamilyServiceImpl.class);

	private static final String				FAMILY_MISMATCH	= "FAMILY_MISMATCH";

	@Autowired
	private ApplicationStageDAO				applicationStageDAO;
	
	@Autowired
	private ApplicationService			    applicationService;

	@Override
	public void execute(WebCrawlerJobAudit webCrawlerJobAudit, final int maxRetryCount) {
		LOGGER.info("Web crawler for track-family started..");
		
		processFindParentQueue(maxRetryCount);
		
		processFindChildQueue(maxRetryCount);

		processFindForeignPriorityQueue(maxRetryCount);

		LOGGER.info("Web crawler for track-family completed.");
	}

	private void processFindChildQueue(final int maxRetryCount) {
		LOGGER.info("Web crawler for find-child started..");

		// step1 : fetch list of find-child-applications-requests for each customer
		Map<String, List<FindFamilyQueue>> customerApplicationsMap = applicationDAO.getFindFamilyList(FamilyType.CHILD);
		
		if (MapUtils.isNotEmpty(customerApplicationsMap)) {
			for (Entry<String, List<FindFamilyQueue>> entry : customerApplicationsMap.entrySet()) {
				// step2 : create web driver
				final WebDriver webDriver = BBWebCrawlerUtils.getWebDriver(null);
				try {
					String customer = entry.getKey();
					List<FindFamilyQueue> findChildList = entry.getValue();

					LOGGER.info(MessageFormat
							.format("Find Child queue operation started for Customer [{0}] ", customer));

					// step3 : authenticate customer
					boolean success = loginService.login(webDriver, customer);
					if (!success) {
						LOGGER.info(MessageFormat.format("updating retry count and status for customer applications in the find-child queue(customer : {0})", customer));
						findChildList.stream().forEach(app -> updateRetryCount(app.getId(), app.getRetryCount() + 1, maxRetryCount));
						continue;
					}

					// step4 : process find-child applications
					IntStream.range(0, findChildList.size()).forEach(
							idx -> findChild(webDriver, findChildList.get(idx), maxRetryCount, idx == 0));
				} finally {
					BBWebCrawlerUtils.logoutAndCloseWebDriver(webDriver, LOGGER);
				}
			}
		} else {
			LOGGER.info("Find child application queue found empty.");
		}
		LOGGER.info("Web crawler for find-child completed.");
	}

	private void findChild(final WebDriver webDriver, final FindFamilyQueue app, final int maxRetryCount, final boolean isFirstRecord) {
		// step1 : select new case
		if (!isFirstRecord) {
			webCrawler.selectNewCase(webDriver);
		}

		Long id = app.getId();
		Calendar filingDate = app.getFilingDate();
		String appNumber = app.getAppNumberRaw();
		String jurisdiction = app.getJurisdictionCode();
		String customer = app.getCustomerNumber();
		int retryCount = app.getRetryCount() + 1;

		LOGGER.info(MessageFormat.format(
				"Find Child queue operation started for [ Queue Id - {0} , Application No.- {1}]", id,
				app.getApplicationNumberFormatted()));

		// step2 : Search for application
		webCrawler.searchApplicationNumber(webDriver, appNumber, jurisdiction);

		// step3 : check if application details page opened
		boolean isFindApplicationSuccess = webCrawler.checkApplicationDetailsPageStatus(webDriver, appNumber,
				jurisdiction, filingDate);

		// check error case
		if (!isFindApplicationSuccess) {
			updateRetryCount(id, retryCount, maxRetryCount);
			return;
		}

		// step4 : find continuity tab
		boolean isChildContinuityPresent = webCrawler.checkContinuityTab(webDriver, false);

		// No child continuity data present for the application
		if (!isChildContinuityPresent) {
			LOGGER.info("No Child Application found.");
			applicationDAO.updateFindFamilyQueueStatus(id, QueueStatus.NO_CHILD_FOUND);
			return;
		}

		// step5 : read child cases
		Map<String, String> childApplications = webCrawler.readChildApplications(webDriver);

		LOGGER.info("Child Application(s) found : " + StringUtils.join(childApplications.keySet(), " , "));
		childApplications.entrySet().stream().forEach(entry -> processChildApplication(app, customer, entry.getKey(), entry.getValue()));

		// step6 : update success status
		applicationDAO.updateFindFamilyQueueStatus(id, QueueStatus.SUCCESS);
	}

	private void processFindForeignPriorityQueue(final int maxRetryCount) {
		LOGGER.info("Web crawler for find-foreign-priority started..");

		// step1 : fetch list of find-foreign-priority-applications-requests for each customer
		Map<String, List<FindFamilyQueue>> customerApplicationsMap = applicationDAO
				.getFindFamilyList(FamilyType.FOREIGN_PRIORITY);

		if (MapUtils.isNotEmpty(customerApplicationsMap)) {
			for (Entry<String, List<FindFamilyQueue>> entry : customerApplicationsMap.entrySet()) {
				// step2 : create web driver
				final WebDriver webDriver = BBWebCrawlerUtils.getWebDriver(null);
				try {
					String customer = entry.getKey();
					List<FindFamilyQueue> findForeignPriorityList = entry.getValue();

					LOGGER.info(MessageFormat.format("Find foreign priority operation started for Customer [{0}] ",
							customer));

					// step3 : authenticate customer
					boolean success = loginService.login(webDriver, customer);
					if (!success) {
						LOGGER.info(MessageFormat.format("Updating retry count and status for customer applications in the find-foreign-priority queue(customer : {0})", customer));
						findForeignPriorityList.stream().forEach(app -> updateRetryCount(app.getId(), app.getRetryCount() + 1, maxRetryCount));
						continue;
					}

					// step4 : process find-foreign-priority applications
					IntStream.range(0, findForeignPriorityList.size()).forEach(
							idx -> findForeignPriority(webDriver, findForeignPriorityList.get(idx), maxRetryCount, idx == 0));
				} finally {
					BBWebCrawlerUtils.logoutAndCloseWebDriver(webDriver, LOGGER);
				}
			}
		} else {
			LOGGER.info("Find foreign priority queue found empty.");
		}
		LOGGER.info("Web crawler for find-foreign-priority completed.");
	}

	private void findForeignPriority(final WebDriver webDriver, final FindFamilyQueue app, final int maxRetryCount, final boolean isFirstRecord) {
		// step1 : select new case
		if (!isFirstRecord) {
			webCrawler.selectNewCase(webDriver);
		}

		Long id = app.getId();
		String appNumber = app.getAppNumberRaw();
		Calendar filingDate = app.getFilingDate();
		String jurisdiction = app.getJurisdictionCode();
		int retryCount = app.getRetryCount() + 1;

		LOGGER.info(MessageFormat.format(
				"Find foreign priority operation started for [ Queue Id - {0} , Application No.- {1}]", id,
				app.getApplicationNumberFormatted()));

		// step2 : Search for application
		webCrawler.searchApplicationNumber(webDriver, appNumber, jurisdiction);

		// step3 : check if application details page opened
		boolean isFindApplicationSuccess = webCrawler.checkApplicationDetailsPageStatus(webDriver, appNumber,
				jurisdiction, filingDate);

		// check error case
		if (!isFindApplicationSuccess) {
			updateRetryCount(id, retryCount, maxRetryCount);
			return;
		}

		// step4 : find foreign priority tab
		boolean isForeignPriorityPresent = webCrawler.checkForeignPriorityTab(webDriver);

		// No foreign priority data present for the application
		if (!isForeignPriorityPresent) {
			applicationDAO.updateFindFamilyQueueStatus(id, QueueStatus.NO_FOREIGN_PRIORITY_FOUND);
			return;
		}

		// step5 : read foreign priorities
		List<List<String>> foreignPriorityApplicationList = webCrawler.readForeignPriorities(webDriver);
		LOGGER.info(MessageFormat.format("{0} Foreign Priorities found.", foreignPriorityApplicationList.size()));
		
		// step6 : process foreign priorities
		if (CollectionUtils.isNotEmpty(foreignPriorityApplicationList)) {
			for (List<String> appData : foreignPriorityApplicationList) {
				String appNo = appData.get(0);
				String country = appData.get(1);
				String filedOn = appData.get(2);
				Calendar fpFilingDate = BlackboxDateUtil.getCalendar(filedOn);
				String fpJurisdiction = applicationDAO.findJurisdictionByCountry(country);

				if (fpJurisdiction == null) {
					LOGGER.error(MessageFormat.format(
							"Jurisdiction code not found for application(Application - {0}, country - {1})", appNo,
							country));
					continue;
				}

				// find in MDM base in raw and base column
				ApplicationBase baseApplication = applicationBaseDAO.findByApplicationNoAndJurisdictionCode(
						fpJurisdiction, appNo);

				if (baseApplication != null) {

					// match application family id with child family id
					if (!app.getFamilyId().equals(baseApplication.getFamilyId())) {
						// send notification
						sendUpdateFamilyLinkageNotification(app.getId(), baseApplication);
					}
				} else {
					CreateApplicationQueue createApplicationRequest = new CreateApplicationQueue(null, appNo,
							app.getCustomerNumber(), fpJurisdiction, fpFilingDate);
					createApplicationRequest.setAppNumberRaw(appNo);
					createApplicationRequest.setFamilyId(app.getFamilyId());
					Long createRequestId = applicationDAO.createApplicationRequestEntry(createApplicationRequest);
					sendCreateFamilyNotification(createRequestId);
				}
			}
		}

		// step7 : update success status
		applicationDAO.updateFindFamilyQueueStatus(String.valueOf(id), QueueStatus.SUCCESS);
	}

	private void processFindParentQueue(final int maxRetryCount) {
		LOGGER.info("Web crawler for find-parent started..");
		Map<String, Map<Long, List<FindFamilyQueue>>> findParentQueue = applicationDAO.getFindParentList();

		if (MapUtils.isNotEmpty(findParentQueue)) {
			for (Entry<String, Map<Long, List<FindFamilyQueue>>> entry : findParentQueue.entrySet()) {
				// step2 : create web driver
				final WebDriver webDriver = BBWebCrawlerUtils.getWebDriver(null);
				try {
					String customer = entry.getKey();
					Map<Long, List<FindFamilyQueue>> findFamilyData = entry.getValue();

					LOGGER.info(MessageFormat.format("Find parent operation started for Customer [{0}] ", customer));

					// step3 : authenticate customer
					boolean success = loginService.login(webDriver, customer);
					if (!success) {
						LOGGER.info(MessageFormat.format("Updating retry count and status for customer applications in the find parent queue (customer : {0})", customer));
						List<FindFamilyQueue> applicationList = findFamilyData.values().stream().flatMap(list -> list.stream()).collect(Collectors.toList());
						applicationList.forEach(app -> updateRetryCount(app.getId(), app.getRetryCount() + 1, maxRetryCount));
						continue;
					}

					// step4 : process find-parent applications
					List<Entry<Long, List<FindFamilyQueue>>> findParent = new ArrayList<>(findFamilyData.entrySet());
					IntStream.range(0, findParent.size()).forEach(
							idx -> findParent(webDriver, findParent.get(0).getKey(), findParent.get(0).getValue(),
									maxRetryCount, idx == 0));
				} finally {
					BBWebCrawlerUtils.closeWebDriver(webDriver, LOGGER);
				}
			}
		} else {
			LOGGER.info("Find parent application queue found empty.");
		}
		LOGGER.info("Web crawler for find-parent completed.");
	}

	private void findParent(final WebDriver webDriver, final Long baseCaseId,
			final List<FindFamilyQueue> applicationList, final int maxRetryCount, final boolean isFirstRecord) {
		LOGGER.info(MessageFormat.format("Find Parent queue operation started for  (Base case Id - {0}). ", baseCaseId));

		// step1 : select new case
		webCrawler.selectNewCase(webDriver);

		String familyId = null;
		Long firstFilingApplicationId = null;
		List<CreateApplicationQueue> createApplicationRequests = new ArrayList<CreateApplicationQueue>();
		Map<QueueStatus, List<Long>> applicationsInCrawlerError = new HashMap<QueueStatus, List<Long>>();
		List<Long> successfulApplicationIds = new ArrayList<Long>();

		for (FindFamilyQueue app : applicationList) {
			Long id = app.getId();
			String appNumber = app.getAppNumberRaw();
			String jurisdiction = app.getJurisdictionCode();
			Calendar filingDate = app.getFilingDate();

			// step2 : Search for application
			webCrawler.searchApplicationNumber(webDriver, appNumber, jurisdiction);

			// step3 : check if application details page opened
			boolean isFindApplicationSuccess = webCrawler.checkApplicationDetailsPageStatus(webDriver, appNumber,
					jurisdiction, filingDate);
			// add to applications in error collection
			if (!isFindApplicationSuccess) {
				updateRetryCountFindParentQueue(maxRetryCount, applicationsInCrawlerError, app, id);
				continue;
			}

			// step4 : read parent cases
			Map<String, String> parentApplications = readParentApplications(webDriver);
			LOGGER.info(MessageFormat.format("{0} Parent applications found.", parentApplications.size()));

			// No parent continuity data present for the application
			if (MapUtils.isEmpty(parentApplications)) {
				firstFilingApplicationId = id;
				LOGGER.info(MessageFormat.format(
						"First filling application found(Base Case - {0}, First filling application -{1})", baseCaseId,
						appNumber));
				break;
			}

			// step5 : find family id
			familyId = findFamilyByApplication(parentApplications, createApplicationRequests, app);
			if (familyId != null) {
				break;
			}
			successfulApplicationIds.add(id);
		}

		if (familyId != null) {
			if (FAMILY_MISMATCH.equals(familyId)) {
				LOGGER.info("Updating error status in the database.");
				applicationDAO.updateFamilyStatus(baseCaseId, StringUtils.EMPTY, QueueStatus.CRAWLER_ERROR);
				handleFindParentFamilyMismatchError(applicationList);
			} else {
				LOGGER.info(MessageFormat.format("Family id found (Base Case - {0}, Family id -{1}).", baseCaseId, familyId));
				applicationDAO.updateFamilyStatus(baseCaseId, familyId, QueueStatus.SUCCESS);
				updateApplicationStageSuccess(applicationList, familyId, null);
			}
		} else if (firstFilingApplicationId != null) {
			// generate family id
			familyId = applicationBaseDAO.generateFamilyId();
			applicationDAO.updateFamilyStatus(baseCaseId, familyId, QueueStatus.SUCCESS);
			updateApplicationStageSuccess(applicationList, familyId, firstFilingApplicationId);
		} else {
			if (CollectionUtils.isNotEmpty(createApplicationRequests)) {
				createApplicationRequests.stream().forEach(app -> createApplicationRequestEntry(app, app.getBaseCaseApplicationQueueId()));
				applicationDAO.updateFindFamilyQueueStatus(StringUtils.join(successfulApplicationIds, ","),
						QueueStatus.SUCCESS);
			}
			
			if (MapUtils.isNotEmpty(applicationsInCrawlerError)) {
				applicationsInCrawlerError
						.entrySet()
						.stream()
						.forEach(
								entry -> applicationDAO.updateFindFamilyQueueStatus(
										StringUtils.join(entry.getValue(), ","), entry.getKey()));
			}
		}
	}
	

	private Map<String, String> readParentApplications(final WebDriver webDriver) {
		//find continuity tab
		boolean isParentContinuityPresent = webCrawler.checkContinuityTab(webDriver, true);
		Map<String, String> parentApplications = new HashMap<String, String>();
				
		if (isParentContinuityPresent) {
			Map<String, String> applications = webCrawler.readParentApplications(webDriver);
			for (Entry<String, String> entry : applications.entrySet()) {
				String parentAppNo = entry.getKey();
				// check applicationNumber format
				boolean isValidFormat = checkApplicationFormat(parentAppNo);
				if (isValidFormat) {
					parentApplications.put(entry.getKey(), entry.getValue());
				} else {
					LOGGER.info(MessageFormat.format("Ignoring parent application (application number : {0})", parentAppNo));
					continue;
				}
			}
		}
		
		return parentApplications;
	}

	private String findFamilyByApplication(final Map<String, String> parentApplications, 
			final List<CreateApplicationQueue> createApplicationRequests, final FindFamilyQueue app) {
		String familyId = null;
		for (Entry<String, String> entry : parentApplications.entrySet()) {
			String parentAppNo = entry.getKey();
			String parentFiledOn = entry.getValue();
			Calendar parentFilingDate = BlackboxDateUtil.getCalendar(parentFiledOn);
			String parentJurisdiction = BBWebCrawlerUtils.getJurisdictionCodeByApplicationNumber(parentAppNo);
			String convertedAppNumber = formatService.getConvertedValue(parentAppNo, NumberType.APPLICATION,
					parentJurisdiction, BBWebCrawlerUtils.getApplicationType(parentJurisdiction), parentFilingDate, null);

			ApplicationBase baseApplication = applicationBaseDAO.findByApplicationNoAndJurisdictionCode(
					parentJurisdiction, convertedAppNumber);
			if (baseApplication != null) {
				if (familyId != null && familyId != baseApplication.getFamilyId()) {
					// error : family id mismatch
					LOGGER.error(MessageFormat
							.format("Family ids mismatch: Parent applications exist with different family ids. Check applications(family id : {1} and family id : {2})",
									familyId, baseApplication.getFamilyId()));
					familyId = FAMILY_MISMATCH;
					break;
				} else {
					familyId = baseApplication.getFamilyId();
				}
			} else {
				// create application request
				CreateApplicationQueue createApplicationRequest = new CreateApplicationQueue(null, convertedAppNumber,
						app.getCustomerNumber(), parentJurisdiction, parentFilingDate);
				createApplicationRequest.setAppNumberRaw(parentAppNo);
				createApplicationRequest.setBaseCaseApplicationQueueId(app.getBaseCaseApplicationQueueId());
				createApplicationRequests.add(createApplicationRequest);
			}
		}
		return familyId;
	}

	/**
	 * This method checks application numbers for excluded application number formats.
	 * <p>
	 * e.g. 60/nnn,nnn, 60/nnnnnn, 61/nnn,nnn, 61/nnnnnn, 62/nnn,nnn, 62/nnnnnn, PCT/CCyy/nnnnn, PCT/CCccyy/nnnnnn (CC
	 * is other than US)
	 * </p>
	 * @param appNo
	 *            application number
	 * @return true, if valid
	 */
	private static boolean checkApplicationFormat(final String appNo) {
		boolean isValid = true;
		// check for US jurisdiction numbers
		String usIgnoreRegex = "^6[0-2]/[0-9]{3},?[0-9]{3}$";
		isValid = !Pattern.matches(usIgnoreRegex, appNo);

		// check for WO(US) jurisdiction numbers
		if (isValid && appNo.startsWith(BBWebCrawerConstants.WO_CODE_INDICATOR_WITHOUT_CC)) {
			isValid = appNo.startsWith(BBWebCrawerConstants.WO_CODE_INDICATOR);
		}
		return isValid;
	}

	private void processChildApplication(final FindFamilyQueue app, final String customer, final String childAppNo, final String filedOn) {
		LOGGER.info(MessageFormat.format("Processing child application({0})", childAppNo));
		String childJurisdiction = BBWebCrawlerUtils.getJurisdictionCodeByApplicationNumber(childAppNo);
		Calendar childFilingDate = BlackboxDateUtil.getCalendar(filedOn);
		String convertedChildAppNo = formatService.getConvertedValue(childAppNo, NumberType.APPLICATION, childJurisdiction, 
				BBWebCrawlerUtils.getApplicationType(childJurisdiction), childFilingDate, null);

		// check child application in MDM
		ApplicationBase childApplication = applicationBaseDAO.findByApplicationNoAndJurisdictionCode(childJurisdiction,
				convertedChildAppNo);

		if (childApplication != null) {
			LOGGER.info(MessageFormat.format("Child application({0}) exists in MDM", childAppNo));
			// match application family id with child family id
			if (!app.getFamilyId().equals(childApplication.getFamilyId())) {
				// send notification
				sendUpdateFamilyLinkageNotification(app.getId(), childApplication);
			}
		} else {
			CreateApplicationQueue createApplicationRequest = new CreateApplicationQueue(null, convertedChildAppNo,
					customer, childJurisdiction, childFilingDate);
			createApplicationRequest.setAppNumberRaw(childAppNo);
			createApplicationRequestEntry(createApplicationRequest, null);
		}
	}

	private void createApplicationRequestEntry(final CreateApplicationQueue app, final Long baseCaseId) {
		boolean exists = applicationDAO.checkIfApplicationQueueEntryExists(app.getApplicationNumberFormatted(),
				app.getJurisdictionCode(), baseCaseId);
		if (!exists) {
			LOGGER.info(MessageFormat.format("Creating Create-Application-Queue entry for application({0})",
					app.getAppNumberRaw()));
			applicationDAO.createApplicationRequestEntry(app);
		} else {
			LOGGER.info(MessageFormat.format("Application already exists in Create-Application-Queue",
					app.getAppNumberRaw()));
		}
	}

	private void updateRetryCount(Long id, int retryCount, final int maxRetryCount) {
		LOGGER.info("Unable to read application deatils. Updating retry count for the application.");
		QueueStatus status = QueueStatus.INITIATED;
		if (retryCount >= maxRetryCount) {
			LOGGER.info(MessageFormat.format("Search tried {0} times, updating error(WEBCRAWLER_ERROR) status.", maxRetryCount));
			status = QueueStatus.CRAWLER_ERROR;
		}
		applicationDAO.updateFindFamilyQueueStatus(id, status);
	}
	
	private void updateRetryCountFindParentQueue(final int maxRetryCount,
			final Map<QueueStatus, List<Long>> applicationsInCrawlerError, final FindFamilyQueue app, final Long id) {
		QueueStatus status = QueueStatus.INITIATED;
		if (app.getRetryCount() >= maxRetryCount) {
			status = QueueStatus.CRAWLER_ERROR;
		}
		List<Long> appList = applicationsInCrawlerError.get(status);
		if (appList == null) {
			appList = new ArrayList<Long>();
			applicationsInCrawlerError.put(status, appList);
		}
		appList.add(id);
	}
	
	private void handleFindParentFamilyMismatchError(List<FindFamilyQueue> applicationList) {
		List<Long> applicationStageIds = applicationList.stream().map(FindFamilyQueue::getApplicationStageId).collect(Collectors.toList());
		applicationService.updateApplicationStageStatus(applicationStageIds, QueueStatus.CRAWLER_ERROR,
				ApplicationValidationStatus.FAMILY_ID_MISMATCH, User.SYSTEM_ID);
		applicationList.forEach(app -> sendCreateApplicationNotification(app.getApplicationStageId()));
	}
	
	private void updateApplicationStageSuccess(List<FindFamilyQueue> applicationList, String familyId, Long firstFilingApplicationQueueId) {
		List<Long> applicationStageIds = applicationList.stream().map(FindFamilyQueue::getApplicationStageId).collect(Collectors.toList());
		Long firstFilingApplicationId = null;
		if (firstFilingApplicationQueueId != null) {
			OptionalLong applicationStageId = applicationList.stream()
					.filter(app -> app.getId().equals(firstFilingApplicationQueueId))
					.mapToLong(FindFamilyQueue::getApplicationStageId).findFirst();
			firstFilingApplicationId = applicationStageId.getAsLong();
		}
		List<ApplicationStage> appStageList = applicationStageDAO.findAll(applicationStageIds);
		for (ApplicationStage appStage : appStageList) {
			appStage.setFamilyId(familyId);
			Jurisdiction jurisdiction = Jurisdiction.fromString(appStage.getJurisdiction());
			ApplicationType applicationType = getApplicationTypeByJurisdiction(firstFilingApplicationId, appStage,
					jurisdiction);
			appStage.getAppDetails().setChildApplicationType(applicationType);
			appStage.setStatus(QueueStatus.IMPORT_READY);
		}
		applicationService.saveApplicationStage(appStageList);
	}

	private ApplicationType getApplicationTypeByJurisdiction(Long firstFilingApplicationId, ApplicationStage s,
			Jurisdiction jurisdiction) {
		ApplicationType applicationType = null;
		switch (jurisdiction) {
		case US:
			if (s.getId().equals(firstFilingApplicationId)) {
				applicationType = ApplicationType.FIRST_FILING;
			} else {
				applicationType = ApplicationType.US_SUBSEQUENT_FILING;
			}
		case WO:
			if (s.getId().equals(firstFilingApplicationId)) {
				applicationType = ApplicationType.PCT_US_FIRST_FILING;
			} else {
				applicationType = ApplicationType.PCT_US_SUBSEQUENT_FILING;
			}
		default:
			break;
		}
		return applicationType;
	}

	private void sendUpdateFamilyLinkageNotification(Long findFamilyQueueId, ApplicationBase baseApplication) {
		Long notificationProcessId = notificationProcessService.createNotification(baseApplication, null, null, 
				findFamilyQueueId, EntityName.FIND_FAMILY_QUEUE, NotificationProcessType.UPDATE_FAMILY_LINKAGE, null, null);
		LOGGER.info(MessageFormat
				.format("Notification created for Update-Family-Linkage (notification process id : {0}, find-family queue id : {1}, Base Application Id : {2}",
						notificationProcessId, findFamilyQueueId, baseApplication.getId()));
	}

	private void sendCreateFamilyNotification(Long createFamilyQueueId) {
		//TODO Create application, application is not yet created, this is the notification to create application , hence attributes are not available here
		Long notificationProcessId = notificationProcessService.createNotification(null, null, null, 
				createFamilyQueueId, EntityName.CREATE_APPLICATION_QUEUE, NotificationProcessType.CREATE_FAMILY_MEMBER, 
				null, null);
		LOGGER.info(MessageFormat
				.format("Notification created for Create-Family-Member for foreign priority (notification process id :{0}, create-application queue id :{1}",
						notificationProcessId, createFamilyQueueId));
	}
	
	private void sendCreateApplicationNotification(final Long applicationStageId) {
		Long notificationProcessId = notificationProcessService.createNotification(null, null, null, applicationStageId,
				EntityName.APPLICATION_STAGE, NotificationProcessType.CREATE_APPLICATION_REQUEST, null, null);
		LOGGER.info(MessageFormat.format(
				"Notification created for Create-Application-Request [notification process id : {0}, application stage id : {1}].",
				notificationProcessId, applicationStageId));
	}
}
