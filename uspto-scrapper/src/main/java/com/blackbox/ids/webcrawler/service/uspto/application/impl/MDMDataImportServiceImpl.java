package com.blackbox.ids.webcrawler.service.uspto.application.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.ApplicationStageDAO;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.Application.SubSource;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.ApplicationValidationStatus;
import com.blackbox.ids.core.model.mdm.IncompatibleAttribute;
import com.blackbox.ids.core.model.mdm.IncompatibleAttribute.Field;
import com.blackbox.ids.core.model.mdm.IncompatibleAttribute.Jurisdiction;
import com.blackbox.ids.core.model.mdm.PatentDetails;
import com.blackbox.ids.core.model.mdm.PublicationDetails;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.Assignee.Entity;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.repository.AssigneeRepository;
import com.blackbox.ids.core.webcrawler.dao.IWebCrawlerApplicationDAO;
import com.blackbox.ids.dto.mdm.ApplicationValidationDTO;
import com.blackbox.ids.dto.mdm.MdmValidationData;
import com.blackbox.ids.services.common.MasterDataService;
import com.blackbox.ids.services.mdm.ApplicationService;
import com.blackbox.ids.services.mdm.IImportData;
import com.blackbox.ids.services.notification.process.NotificationProcessService;

@Service("mdmDataImportService")
public class MDMDataImportServiceImpl implements IImportData {

	private static final Logger			LOGGER	= Logger.getLogger(MDMDataImportServiceImpl.class);

	@Autowired
	private ApplicationService			applicationService;

	@Autowired
	private IWebCrawlerApplicationDAO	applicationDAO;

	@Autowired
	private ApplicationStageDAO			applicationStageDAO;

	@Autowired
	private MasterDataService			masterDataService;

	@Autowired
	private ApplicationBaseDAO			applicationBaseDao;

	@Autowired
	private NotificationProcessService	notificationProcessService;

	@Autowired
	private AssigneeRepository			assigneeRepository;

	@Override
	public void importData() {
		Map<String, List<ApplicationStage>> familyApplicationMap = applicationStageDAO.getApplicationsToImport();
		
		if (MapUtils.isNotEmpty(familyApplicationMap)) {
			LOGGER.info(MessageFormat.format("Import data : Family({0}), Applications({1})", familyApplicationMap.size(), familyApplicationMap.values().size()));
			// base case - list of applications
			MdmValidationData masterData = fetchValidationData();
			familyApplicationMap.entrySet().stream()
					.forEach(entry -> processFamilyApplications(entry.getKey(), entry.getValue(), masterData));
			LOGGER.info("Applications data import processed sucessfully");
		} else {
			LOGGER.info("No application found to import");
		}
		
		// Pending check Program
		processPendingApplications();
		
	}

	private void processFamilyApplications(final String familyId, final List<ApplicationStage> applicationList,
			final MdmValidationData masterData) {
		Optional<ApplicationStage> firstFilingApplication = applicationList.stream()
				.filter(a -> ApplicationType.isFirstFiling(a.getAppDetails().getChildApplicationType())).findAny();

		ApplicationStage firstFilingCase = null;
		List<ApplicationStage> subsequentFilingApplicationList = new ArrayList<>();
		subsequentFilingApplicationList.addAll(applicationList);
		
		if (firstFilingApplication.isPresent()) {
			firstFilingCase = firstFilingApplication.get();
			subsequentFilingApplicationList.remove(firstFilingCase);
			
			// validate first filing case
			validateApplicationFields(firstFilingCase, masterData);

			if (ApplicationValidationStatus.DUPLICATE.equals(firstFilingCase.getValidationStatus())) {
				// find family id from MDM application, and update applications
				if (applicationList.size() > 1) {
					String correctFamily = applicationBaseDao.findFamilyByApplication(
							firstFilingCase.getJurisdiction(), firstFilingCase.getApplicationNumber());
					applicationList.stream().forEach(app -> app.setFamilyId(correctFamily));
				}
				// update status of first filling as duplicate error
				firstFilingCase.setStatus(QueueStatus.ERROR);
			} else if (!ApplicationValidationStatus.OK.equals(firstFilingCase.getValidationStatus())) {
				applicationList.remove(firstFilingCase);
				handleFirstFilingImportError(firstFilingCase, subsequentFilingApplicationList);
				return;
			}

		} else {
			// check for family existence
			long familyApplicationsCount = applicationBaseDao.countFamilyApplications(familyId);
			boolean isFamilyExist = familyApplicationsCount > 0;
			// family doesn't exist any more
			if (!isFamilyExist) {
				subsequentFilingApplicationList.stream().forEach(app -> handleSubsequentFilingImportError(app, ApplicationValidationStatus.FAMILY_NOT_EXIST));
			}
			applicationService.saveApplicationStage(subsequentFilingApplicationList);
			return;
		}
		// validate all applications
		subsequentFilingApplicationList.stream().forEach(app -> validateApplicationFields(app, masterData));

		Map<Boolean, List<ApplicationStage>> validatedApplicationMap = applicationList.stream().collect(
				Collectors.partitioningBy(app -> ApplicationValidationStatus.OK.equals(app.getValidationStatus())));
		
		saveImportedApplications(firstFilingCase, validatedApplicationMap);
	}
	
	private void saveImportedApplications(ApplicationStage firstFilingCase,
			Map<Boolean, List<ApplicationStage>> validatedApplicationMap) {
		for (ApplicationStage app : validatedApplicationMap.get(Boolean.FALSE)) {
			handleSubsequentFilingImportError(app, app.getValidationStatus());
		}
		applicationService.saveImportedApplications(firstFilingCase, validatedApplicationMap);
	}
	
	private void handleFirstFilingImportError(final ApplicationStage firstFilingCase, final List<ApplicationStage> subsequentFilingApplicationList) {
		firstFilingCase.setStatus(QueueStatus.ERROR);
		// error out all subsequent filings and send notifications
		subsequentFilingApplicationList.stream().forEach(app -> handleSubsequentFilingImportError(app, ApplicationValidationStatus.FIRST_FILING_IMPORT_FAILED));
		
		List<ApplicationStage> applications = new ArrayList<>();
		applications.add(firstFilingCase);
		applications.addAll(subsequentFilingApplicationList);
		applicationService.saveApplicationStage(applications);
	}

	private void handleSubsequentFilingImportError(final ApplicationStage appStage, final ApplicationValidationStatus validationStatus) {
		appStage.setStatus(QueueStatus.ERROR);
		appStage.setValidationStatus(validationStatus);
		if (!ApplicationValidationStatus.DUPLICATE.equals(appStage.getValidationStatus())) {
			ApplicationBase appBase = applicationBaseDao.findByApplicationNoAndJurisdictionCode(appStage.getJurisdiction(), appStage.getApplicationNumber());
			if (appBase != null && !appStage.getFamilyId().equalsIgnoreCase(appBase.getFamilyId())) {
				// send update family linkage notification
				Long findFamilyQueueId = applicationDAO.getFindParentEntityByApplicationStage(appStage.getId());
				sendUpdateFamilyLinkageNotification(findFamilyQueueId, appBase);
			}
		} else {
			sendNotification(appStage);
		}
	}

	private void validateApplicationFields(final ApplicationStage app, final MdmValidationData masterData) {
		ApplicationType appType = app.getAppDetails().getChildApplicationType();
		Jurisdiction jurisdiction = Jurisdiction.fromString(app.getJurisdiction());

		if (validateApplicationType(appType, jurisdiction)) {
			app.setValidationStatus(ApplicationValidationStatus.CHILD_APPLICATION_TYPE_INVALID);
		} else if (app.getAppDetails().getFilingDate() == null) {
			app.setValidationStatus(ApplicationValidationStatus.FILING_DATE_NOT_FOUND);
		} else if (!validateApplicationNumber(app, appType)) {} else if (StringUtils.isBlank(app.getFamilyId())) {
			app.setValidationStatus(ApplicationValidationStatus.FAMILY_LINKAGE_NOT_FOUND);
		} else if (!validateCustomer(app, jurisdiction, appType, masterData.getCustomerNumbers())) {} else if (StringUtils
				.isBlank(app.getAttorneyDocketNumber())) {
			app.setValidationStatus(ApplicationValidationStatus.DOCKET_NUMBER_NOT_FOUND);
		} else if (!applicationService.validateDocketNumber(app.getAttorneyDocketNumber())) {
			app.setValidationStatus(ApplicationValidationStatus.DOCKET_NUMBER_INVALID_FORMAT);
		} else if (!validateAssignee(app, masterData.getAssignees()) || !validateConfirmationNumber(app, jurisdiction)
				|| !validateEntity(app, jurisdiction)) {} else if (StringUtils.isBlank(app.getPatentDetails()
				.getTitle())) {
			app.setValidationStatus(ApplicationValidationStatus.TITLE_NOT_FOUND);
		} else if (!validatePublicationDetails(app)) {}

		if (app.getValidationStatus() == null) {
			app.setValidationStatus(ApplicationValidationStatus.OK);
		}
	}

	private boolean validatePublicationDetails(final ApplicationStage app) {
		boolean isValid = true;
		PublicationDetails publicationDetails = app.getPublicationDetails();
		PatentDetails patentDetails = app.getPatentDetails();
		if (StringUtils.isBlank(publicationDetails.getPublicationNumber())
				&& publicationDetails.getPublishedOn() != null) {
			app.setValidationStatus(ApplicationValidationStatus.PUBLICATION_NUMBER_NOT_FOUND);
			isValid = false;
		} else if (StringUtils.isNotBlank(publicationDetails.getPublicationNumber())
				&& publicationDetails.getPublishedOn() == null) {
			app.setValidationStatus(ApplicationValidationStatus.PUBLICATION_DATE_NOT_FOUND);
			isValid = false;
		} else if (StringUtils.isBlank(patentDetails.getPatentNumber()) && patentDetails.getIssuedOn() != null) {
			app.setValidationStatus(ApplicationValidationStatus.PATENT_NUMBER_NOT_FOUND);
			isValid = false;
		} else if (StringUtils.isNotBlank(patentDetails.getPatentNumber()) && patentDetails.getIssuedOn() == null) {
			app.setValidationStatus(ApplicationValidationStatus.ISSUED_DATE_NOT_FOUND);
			isValid = false;
		}
		return isValid;
	}

	private boolean validateEntity(final ApplicationStage app, final Jurisdiction jurisdiction) {
		Entity entity = app.getOrganizationDetails().getEntity();
		if (Jurisdiction.US.equals(jurisdiction) && entity == null) {
			app.setValidationStatus(ApplicationValidationStatus.ENTITY_NOT_FOUND);
			return false;
		}
		return true;
	}

	private boolean validateAssignee(final ApplicationStage app, List<String> assignees) {
		// set Assignee
		if (SubSource.USPTO.equals(app.getSubSource())) {
			Assignee assignee = applicationService.getAssigneeFromDocketNo(app.getAttorneyDocketNumber());
			// TODO check first filing case if assignee-docket switch is off
			if (assignee != null) {
				app.setAssignee(assignee.getName());
				app.setAppAssignee(assignee);
				return true;
			}
		}
		// validate
		if (StringUtils.isEmpty(app.getAssignee())) {
			app.setValidationStatus(ApplicationValidationStatus.ASSIGNEE_NOT_FOUND);
			return false;
		} else if (!assignees.contains(app.getAssignee())) {
			app.setValidationStatus(ApplicationValidationStatus.ASSIGNEE_NOT_EXIST);
			return false;
		}
		
		if (StringUtils.isNotBlank(app.getAssignee()) && app.getAppAssignee() == null) {
			app.setAppAssignee(assigneeRepository.findByName(app.getAssignee()));
		}
		return true;
	}

	private boolean validateApplicationNumber(final ApplicationStage app, final ApplicationType appType) {
		ApplicationValidationDTO validationData = new ApplicationValidationDTO(app.getJurisdiction(),
				app.getApplicationNumber(), appType, app.getAppDetails().getFilingDate());
		ApplicationValidationStatus status = applicationService.validateApplicationNo(validationData);
		if (!ApplicationValidationStatus.OK.equals(status)) {
			app.setValidationStatus(status);
			return false;
		} else {
			return true;
		}
	}

	private boolean validateConfirmationNumber(final ApplicationStage app, final Jurisdiction jurisdiction) {
		if (Jurisdiction.US.equals(jurisdiction) && StringUtils.isBlank(app.getAppDetails().getConfirmationNumber())) {
			app.setValidationStatus(ApplicationValidationStatus.CONFIRMATION_NUMBER_NOT_FOUND);
			return false;
		}
		return true;
	}

	private static boolean validateCustomer(final ApplicationStage app, final Jurisdiction jurisdiction,
			final ApplicationType appType, List<String> customerNumbers) {
		boolean isValid = true;
		if (IncompatibleAttribute.isMandatory(Field.CUSTOMER_NO, jurisdiction, appType)) {
			if (StringUtils.isBlank(app.getCustomer())) {
				app.setValidationStatus(ApplicationValidationStatus.CUSTOMER_NOT_FOUND);
				isValid = false;
			} else if (!customerNumbers.contains(app.getCustomer())) {
				app.setValidationStatus(ApplicationValidationStatus.CUSTOMER_NOT_EXIST);
				isValid = false;
			}
		}
		return isValid;
	}

	private static boolean validateApplicationType(final ApplicationType applicationType,
			final Jurisdiction jurisdiction) {
		boolean isValid = false;
		if (applicationType != null && jurisdiction != null) {
			switch (jurisdiction) {
			case US:
				isValid = ApplicationType.usAppTypes().contains(applicationType);
				break;
			case WO:
				isValid = ApplicationType.woAppTypes().contains(applicationType);
				break;
			case OTHER:
				isValid = ApplicationType.otherAppTypes().contains(applicationType);
				break;
			default:
				break;
			}
		}
		return isValid;

	}

	private MdmValidationData fetchValidationData() {
		List<String> customers = masterDataService.getUserCustomerNumbers();
		List<String> assignees = masterDataService.getUserAssignees();
		return new MdmValidationData(customers, assignees, null);
	}
	
	private void processPendingApplications() {
		List<ApplicationStage> pendingApplications = applicationStageDAO.getPendingApplicationsByStatus(
				QueueStatus.INITIATED, 30);// TODO constant

		if (CollectionUtils.isNotEmpty(pendingApplications)) {
			LOGGER.info(MessageFormat.format("Pending application count({0}) found. Updating error status...",
					pendingApplications.size()));
			pendingApplications.forEach(appStage -> {
				appStage.setStatus(QueueStatus.ERROR);
				appStage.setValidationStatus(ApplicationValidationStatus.PENDING);
				sendNotification(appStage);
			});
			applicationService.saveApplicationStage(pendingApplications);
			LOGGER.info("Pending application status updated successfully");
		} else {
			LOGGER.info("Pending application not found");
		}
	}

	private void sendNotification(final ApplicationStage app) {
		Long notificationProcessId = notificationProcessService.createNotification(null, null, null, app.getId(),
				EntityName.APPLICATION_STAGE, NotificationProcessType.CREATE_APPLICATION_REQUEST, null, null);
		LOGGER.info(MessageFormat
				.format("Notification created for Create-Application-Request [notification process id : {0}, application stage id : {1},  Application Number : {2}, Jurisdiction : {3}].",
						notificationProcessId, app.getId(), app.getApplicationNumber(), app.getJurisdiction()));
	}
	
	private void sendUpdateFamilyLinkageNotification(Long findFamilyQueueId, ApplicationBase baseApplication) {
		Long notificationProcessId = notificationProcessService.createNotification(baseApplication, null, null, 
				findFamilyQueueId, EntityName.FIND_FAMILY_QUEUE, NotificationProcessType.UPDATE_FAMILY_LINKAGE, null, null);
		LOGGER.info(MessageFormat
				.format("Notification created for Update-Family-Linkage (notification process id : {0}, find-family queue id : {1}, Base Application Id : {2}",
						notificationProcessId, findFamilyQueueId, baseApplication.getId()));
	}

}
