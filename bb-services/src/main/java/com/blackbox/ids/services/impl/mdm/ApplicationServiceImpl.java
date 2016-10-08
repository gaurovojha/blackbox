package com.blackbox.ids.services.impl.mdm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.ApplicationStageDAO;
import com.blackbox.ids.core.dao.webcrawler.DownloadOfficeActionQueueDAO;
import com.blackbox.ids.core.dto.IDS.ApplicationDetailsDTO;
import com.blackbox.ids.core.dto.mdm.DraftIdentityDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilyDetailDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilySearchFilter;
import com.blackbox.ids.core.model.enums.ApplicationNumberStatus;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.Application.Source;
import com.blackbox.ids.core.model.mdm.Application.SubSource;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationNumberList;
import com.blackbox.ids.core.model.mdm.ApplicationScenario;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.ApplicationValidationStatus;
import com.blackbox.ids.core.model.mdm.InclusionList;
import com.blackbox.ids.core.model.mdm.IncompatibleAttribute;
import com.blackbox.ids.core.model.mdm.IncompatibleAttribute.Field;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.mdm.PatentDetails;
import com.blackbox.ids.core.model.mdm.PublicationDetails;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.AssigneeAttorneyDocketNo;
import com.blackbox.ids.core.model.mstr.AttorneyDocketFormat;
import com.blackbox.ids.core.model.mstr.AttorneyDocketNumber;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.mstr.Organization;
import com.blackbox.ids.core.model.mstr.ProsecutionStatus;
import com.blackbox.ids.core.model.mstr.TechnologyGroup;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.model.webcrawler.FindFamilyQueue;
import com.blackbox.ids.core.model.webcrawler.FindFamilyQueue.FamilyType;
import com.blackbox.ids.core.repository.AssigneeRepository;
import com.blackbox.ids.core.repository.CustomerRepository;
import com.blackbox.ids.core.repository.ExclusionListRepository;
import com.blackbox.ids.core.repository.InclusionListRepository;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.core.repository.mdm.AttorneyDocketRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.webcrawler.dao.IWebCrawlerApplicationDAO;
import com.blackbox.ids.dto.mdm.ApplicationValidationDTO;
import com.blackbox.ids.dto.mdm.DocketNoValidationStatus;
import com.blackbox.ids.services.common.MasterDataService;
import com.blackbox.ids.services.mdm.ApplicationService;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.validation.NumberFormatValidationService;

@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService {

	private Logger log = Logger.getLogger(ApplicationService.class);

	@Autowired
	private ApplicationBaseDAO applicationBaseDAO;

	@Autowired
	private MasterDataService masterDataService;

	@Autowired
	private DownloadOfficeActionQueueDAO downloadOfficeActionQueueDAO;

	@Autowired
	private IWebCrawlerApplicationDAO crawlerApplicationDAO;

	@Autowired
	private NumberFormatValidationService numberFormatValidator;

	@Autowired
	private ExclusionListRepository exclusionListRepository;

	@Autowired
	private InclusionListRepository inclusionListRepository;

	@Autowired
	private ApplicationStageDAO applicationStageDAO;

	@Autowired
	private NotificationProcessService notificationProcessService;

	@Autowired
	private AssigneeRepository assigneeRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private JurisdictionRepository jurisdictionRepository;
	
	@Autowired
	private AttorneyDocketRepository attorneyDocketRepository;

	@Value("${setup.switch.inclusionList}")
	private String switchInclusionList;

	@Value("${assignee.docketno.validation}")
	private String assigneeDocketValidation;

	@Override
	public ApplicationValidationStatus validateApplicationNo(final ApplicationValidationDTO app) {

		ApplicationValidationStatus status = null;
		if (app == null || StringUtils.isBlank(app.jurisdiction) || StringUtils.isBlank(app.jurisdiction)
				|| app.applicationType == null) {
			status = ApplicationValidationStatus.ILLEGAL_ARGS;
		} else {
			final String appNoConverted = convertApplicationNo(app);
			status = StringUtils.isBlank(appNoConverted) ? ApplicationValidationStatus.BAD_FORMAT : null;

			if (status == null) {
				status = masterDataService.isValidJurisdiction(app.jurisdiction) ? null
						: ApplicationValidationStatus.INVALID_JURISDICTION;
			}

			if (status == null) {
				status = isUnique(app.jurisdiction, appNoConverted) ? null : ApplicationValidationStatus.DUPLICATE;
			}
			if (status == null) {
				status = isExcluded(app.jurisdiction, appNoConverted) ? ApplicationValidationStatus.EXCLUDED : null;
			}
		}
		return status == null ? ApplicationValidationStatus.OK : status;
	}

	@Override
	public boolean isUnique(String jurisdiction, String applicationNo) {
		return !applicationBaseDAO.exists(jurisdiction, applicationNo);
	}

	@Override
	public String convertApplicationNo(final ApplicationValidationDTO app) {
		return numberFormatValidator.getConvertedValue(app.applicationNo, NumberType.APPLICATION, app.jurisdiction,
				app.applicationType, app.filingDate, null);
	}

	@Override
	public boolean isExcluded(String jurisdiction, String applicationNo) {
		return exclusionListRepository.isExcluded(jurisdiction, applicationNo).longValue() > 0L;
	}

	@Override
	public DocketNoValidationStatus validateDocketNo(final String docketNo, final String assignee) {
		DocketNoValidationStatus status = null;
		if (StringUtils.isBlank(docketNo) || StringUtils.isBlank(assignee)) {
			status = DocketNoValidationStatus.ILLEGAL_ARGS;
		} else {
			// Validate the docket format
			status = validateDocketNumber(docketNo) ? null : DocketNoValidationStatus.INVALID_FORMAT;
			if (status == null && isAssigneeDocketDerivationOn()) {
				// Validate combination of assignee and docket.
				Assignee docketAssignee = getAssigneeFromDocketNo(docketNo);
				status = docketAssignee != null && docketAssignee.getName().equalsIgnoreCase(assignee) ?
						null : DocketNoValidationStatus.WRONG_COMBO;
			}
		}

		return status == null ? DocketNoValidationStatus.OK : status;
	}

	@Override
	public boolean validateDocketNumber(String docketNumber) {
		AttorneyDocketFormat docketFormat = masterDataService.getAttorneyDocketFormat();
		int segmentSize = docketFormat.getSegmentSize();
		int totalSegments = docketFormat.getNumSegments();
		String seperator = docketFormat.getSeparator();
		String regex = "(.{" + segmentSize + "}" + seperator + ")" + "{" + (totalSegments - 1) + "}" + ".{"
				+ segmentSize + "}";
		return docketNumber.matches(regex);
	}

	@Override
	public List<FamilyDetailDTO> fetchFamilyDetail(final FamilySearchFilter searchFilter) {
		if (searchFilter == null || searchFilter.getFamilyLinker() == null) {
			throw new IllegalArgumentException("[Searching Family Details]: Missing mandatory parameters.");
		}
		return applicationBaseDAO.getFamilyDetails(searchFilter);
	}

	@Override
	public FamilyDetailDTO fetchFamilyDetails(final String familyId) {
		List<FamilyDetailDTO> families = fetchFamilyDetail(new FamilySearchFilter(familyId, true));
		return families.isEmpty() ? null : families.get(0);
	}

	@Override
	public ApplicationBase findByApplicationNo(final String jurisdiction, final String applicationNo) {
		if (StringUtils.isEmpty(jurisdiction) || StringUtils.isEmpty(applicationNo)) {
			throw new IllegalArgumentException("[Application by Number]: Missing required parameters.");
		}
		return applicationBaseDAO.findByApplicationNoAndJurisdictionCode(jurisdiction, applicationNo);
	}

	@Override
	public MdmRecordDTO fetchApplicationDetails(final long applicationId) {
		return applicationBaseDAO.fetchApplicationDetails(applicationId);
	}

	@Override
	public List<MdmRecordDTO> fetchApplicationByApplicationNoAndJurisdiction(final String jurisdiction,
			final String applicationNo) {
		return applicationBaseDAO.findByApplicationNoAndJurisdiction(jurisdiction, applicationNo);
	}

	@Override
	public List<MdmRecordDTO> fetchApplicationByFamily(String family) {
		return applicationBaseDAO.findByFamily(family);
	}

	@Override
	public List<MdmRecordDTO> fetchApplicationByFamilyAndRecordStatus(long recordId, String familyId,
			MDMRecordStatus mdmRecordStatus) {
		return applicationBaseDAO.findByFamilyAndRecordStatus(recordId, familyId, mdmRecordStatus);
	}

	@Override
	public List<MdmRecordDTO> fetchApplicationByAttorneyDocket(String attorneyDocket) {
		return applicationBaseDAO.findByAttorneyDocket(attorneyDocket);
	}

	@Override
	@Transactional
	public void saveManualApplications(final List<ApplicationBase> applications, final Long notificationId) {
		assertApplicationDetails(applications);
		try {
			setApplicationReferences(applications);
			setBusinessAttributes(applications);
			setDocketSegments(applications);
			applicationBaseDAO.persist(applications);
			performPostCreateActions(applications);
			resolveCreateApplicationRequest(notificationId);
		} catch (final Exception e) {
			log.error("[SAVING MANUAL APPLICATION]: An error occurred while saving applications details.", e);
			throw new ApplicationException(e);
		}
	}

	@Override
	@Transactional
	public long rejectCreateAppRequest(final long entityId, final String entityName, final long notificationId) {
		if (StringUtils.isEmpty(entityName) || EntityName.fromString(entityName) == null) {
			throw new IllegalArgumentException("[Reject 'Create Application Request']: Missing mandatory parameters.");
		}
		notificationProcessService.completeTask(notificationId, NotificationStatus.COMPLETED);
		log.info(String.format("[Notification]: Successfully updated notification for {0}.", notificationId));
		return applicationBaseDAO.rejectCreateAppRequest(entityId, EntityName.fromString(entityName));

	}

	@Override
	@Transactional
	public long rejectUpdateFamilyRequest(final long entityId, final String entityName, final long notificationId) {
		if (StringUtils.isEmpty(entityName) || EntityName.fromString(entityName) == null) {
			throw new IllegalArgumentException("[Reject 'Update Family Request']: Missing mandatory parameters.");
		}
		notificationProcessService.completeTask(notificationId, NotificationStatus.COMPLETED);
		log.info(String.format("[Notification]: Successfully updated notification for {0}.", notificationId));
		return applicationBaseDAO.rejectUpdateFamilyRequest(entityId, EntityName.fromString(entityName));

	}

	private void assertApplicationDetails(final List<ApplicationBase> applications) {
		applications.forEach(e -> assertApplicationDetails(e));
	}

	private void assertApplicationDetails(ApplicationBase application) {
		if (invalidApplicationDetails(application)) {
			throw new IllegalArgumentException("[Manual Application]: Missing mandatory details.");
		}
		log.info(String.format("[Manual Application]: Processing creation for application {0}-{1}.",
				application.getJurisdiction().getCode(), application.getAppDetails().getApplicationNumberRaw()));
	}

	private boolean invalidApplicationDetails(ApplicationBase application) {
		return application == null || application.getJurisdiction() == null || application.getAssignee() == null
				|| application.getAttorneyDocketNumber() == null || application.getAppDetails() == null
				|| application.getAppDetails().getChildApplicationType() == null;
	}

	private void setApplicationReferences(final List<ApplicationBase> applications) {
		Set<String> strAssignees = applications.stream().map(e -> e.getAssignee().getName())
				.collect(Collectors.toSet());
		Set<String> strJurisdictions = applications.stream().map(e -> e.getJurisdiction().getCode())
				.collect(Collectors.toSet());
		Set<String> strCustomers = applications.stream().map(e -> e.getCustomer().getCustomerNumber())
				.collect(Collectors.toSet());

		strCustomers.removeAll(Collections.singleton(null));
		Map<String, Long> assignees = masterDataService.mapAssigneeNameIds(strAssignees);
		Map<String, Long> jurisdictions = masterDataService.mapJurisdictionNameIds(strJurisdictions);
		Map<String, Long> customers = CollectionUtils.isEmpty(strCustomers) ? Collections.emptyMap()
				: masterDataService.mapCustomerNameIds(strCustomers);

		applications.forEach(e -> setApplicationReferences(e, assignees, jurisdictions, customers));
	}

	private void setApplicationReferences(ApplicationBase application, Map<String, Long> assignees,
			Map<String, Long> jurisdictions, Map<String, Long> customers) {

		final String jurisdiction = application.getJurisdiction().getCode().toUpperCase();
		application.setJurisdiction(new Jurisdiction(jurisdictions.get(jurisdiction), jurisdiction));
		application.setAssignee(new Assignee(assignees.get(application.getAssignee().getName().toUpperCase())));
		String customer = application.getCustomer().getCustomerNumber();
		if (StringUtils.isNotBlank(customer)) {
			application.setCustomer(new Customer(customers.get(customer.toUpperCase()), customer.toUpperCase()));
		}
	}

	private void setBusinessAttributes(List<ApplicationBase> applications) {
		final String familyId = getFamilyId(applications);

		applications.forEach(e -> {
			e.setFamilyId(familyId);
			e.setRecordStatus(MDMRecordStatus.ACTIVE);
			e.setNewRecordStatus(MDMRecordStatus.BLANK);
			e.setSource(Source.MANUAL);
			e.setSubSource(SubSource.MANUAL);
			e.setOrganization(Organization.DEFAULT_ORGANIZATION);
			e.setTechnologyGroup(TechnologyGroup.DEFAULT_TECHNOLOGY_GROUP);
			setConvertedNumerValues(e);
			updateCustomerNumber(e);
		});
	}

	private String getFamilyId(List<ApplicationBase> applications) {
		String familyId = null;

		if (applications.size() == 1
				&& ApplicationType.isFirstFiling(applications.get(0).getAppDetails().getChildApplicationType())) {
			familyId = applicationBaseDAO.generateFamilyId();
		} else {
			familyId = applications.get(0).getFamilyId();
		}

		return familyId;
	}

	private void updateCustomerNumber(ApplicationBase application) {
		if (IncompatibleAttribute.violoates(Field.CUSTOMER_NO,
				com.blackbox.ids.core.model.mdm.IncompatibleAttribute.Jurisdiction
				.fromString(application.getJurisdiction().getCode()),
				application.getAppDetails().getChildApplicationType())) {
			application.setCustomer(Customer.DEFAULT_CUSTOMER);
		}
	}

	private void setConvertedNumerValues(ApplicationBase application) {
		final String jurisdiction = application.getJurisdiction().getCode();
		final ApplicationType applicationType = application.getAppDetails().getChildApplicationType();
		final Calendar filingDate = application.getAppDetails().getFilingDate();
		final Calendar publicationDate = application.getPublicationDetails().getPublishedOn();

		application.setApplicationNumber(
				numberFormatValidator.getConvertedValue(application.getAppDetails().getApplicationNumberRaw(),
						NumberType.APPLICATION, jurisdiction, applicationType, filingDate, publicationDate));

		if (StringUtils.isNoneBlank(application.getPublicationDetails().getPublicationNumberRaw())) {
			application.getPublicationDetails()
			.setPublicationNumber(numberFormatValidator.getConvertedValue(
					application.getPublicationDetails().getPublicationNumberRaw(), NumberType.PUBLICATION,
					jurisdiction, applicationType, filingDate, publicationDate));
		}

		if (StringUtils.isNoneBlank(application.getPatentDetails().getPatentNumberRaw())) {
			application.getPatentDetails().setPatentNumber(
					numberFormatValidator.getConvertedValue(application.getPatentDetails().getPatentNumberRaw(),
							NumberType.PATENT, jurisdiction, applicationType, filingDate, publicationDate));
		}

	}

	private void performPostCreateActions(List<ApplicationBase> applications) {
		applications.stream().forEach(e -> performPostCreateActions(e));
	}

	private void performPostCreateActions(ApplicationBase application) {
			/*-
		WRITE INTO CRAWLER QUEUE
		#1, #2
		When New MDM Record created,
		If ((JURISDICTION is US) or (If JURISDICTION is WO and APPLICATION NUMBER_INPUT starts with “PCT/US”)) OR (JURISDICTION is EP)
		Send following values from the MDM record to the DOWNLOAD [EUROPEAN] OFFICE ACTION QUEUE –
		{JURISDICTION / APPLICATION NUMBER_INPUT / CUSTOMER NUMBER / Mailing Date = “ALL” / Document Code = “ALL IDS RELEVANT CODES”}
		 */
		final String jurisdiction = application.getJurisdiction().getCode().toUpperCase();
		final String appNumberRaw = application.getAppDetails().getApplicationNumberRaw();
		final String appNumberConverted = application.getApplicationNumber();
		final String customerNo = application.getCustomer().getCustomerNumber();

		boolean usApplication = Jurisdiction.Code.US.value.equalsIgnoreCase(jurisdiction)
				|| (Jurisdiction.Code.WIPO.value.equalsIgnoreCase(jurisdiction)
						&& (appNumberRaw.startsWith("PCT") || appNumberRaw.startsWith("US")));
		boolean europenApplication = Jurisdiction.Code.EUROPE.value.equals(jurisdiction);

		if (usApplication || europenApplication) {
			DownloadOfficeActionQueue officeActionQueue = new DownloadOfficeActionQueue(jurisdiction,
					appNumberConverted, appNumberRaw, customerNo, null, Constant.ALL_DOCUMENT_CODE);
			downloadOfficeActionQueueDAO.persist(officeActionQueue);
		}

		/*- Only send entry to #3 and #5, if jurisdiction is US or WO (number starts with PCT/US) */
			/*-
		#3
		When New MDM Record created.
		If (SYS DATE – FILING DATE ) > 3 MONTHS
		Send following values from the MDM record to the FIND CHILD QUEUE –
		{JURISDICTION / APPLICATION NUMBER_INPUT / FAMILY ID / CUSTOMER NUMBER}
		 */

		// Find child queue - raw
		if (usApplication
				&& BlackboxDateUtil.daysDiff(application.getAppDetails().getFilingDate().getTime(), new Date()) > 90) {
			FindFamilyQueue findChildEntry = new FindFamilyQueue(null, appNumberConverted, customerNo, jurisdiction,
					application.getAppDetails().getFilingDate(), application.getFamilyId(), FamilyType.CHILD,
					QueueStatus.INITIATED, 0);
			findChildEntry.setAppNumberRaw(appNumberRaw);
			crawlerApplicationDAO.createFindFamilyEntry(findChildEntry);
		}

			/*-
		#5
		When New MDM Record created,
		If (SYS DATE – FILING DATE) > 3 MONTHS --- Now Send Always
		Send following values from the MDM record to the FIND FOREIGN PRIORITY QUEUE –
		{JURISDICTION / APPLICATION NUMBER_INPUT / FAMILY ID / CUSTOMER NUMBER}
		 */

		// Foreign priority - raw
		if (usApplication) {
			FindFamilyQueue findForeignPriority = new FindFamilyQueue(null, appNumberConverted, customerNo,
					jurisdiction, application.getAppDetails().getFilingDate(), application.getFamilyId(),
					FamilyType.FOREIGN_PRIORITY, QueueStatus.INITIATED, 0);
			findForeignPriority.setAppNumberRaw(appNumberRaw);
			crawlerApplicationDAO.createFindFamilyEntry(findForeignPriority);
		}

		/*- Activate application in inclusion list. */
		updateInclusionList(application);
	}

	private void updateInclusionList(final ApplicationBase application) {
		if (isInclusionListOn()) {
				/*-
			7.	When Inclusion List is ON, and application X1 is manually entered in MDM
			a.	If X1 is already ACTIVE in inclusion list (check the corresponding application number_converted –
			this is unique value in MDM. Conversion logic has been discussed earlier in Data Format Matrix), X1 is allowed to be entered
			b.	If X1 is already INACTIVE in inclusion list, X1 is REACTIVATED in inclusion list and application is allowed to enter in MDM
			c.	If X1 is not available in inclusion list, X1 is CREATED in ACTIVE status in inclusion list and application is allowed to enter in MDM
			 */
			InclusionList inclusionList = inclusionListRepository.findByJuridictionAndApplication(
					application.getJurisdiction().getCode(), application.getApplicationNumber());
			if (inclusionList == null) {
				inclusionList = new InclusionList();
				inclusionList.setApplications(new ApplicationNumberList(application.getJurisdiction().getCode(),
						application.getApplicationNumber(), application.getAppDetails().getApplicationNumberRaw(),
						application.getCustomer().getCustomerNumber(), ApplicationNumberStatus.ACTIVE));
			} else {
				// XXX: Update customer number or not?
				inclusionList.getApplications().setStatus(ApplicationNumberStatus.ACTIVE);
			}
			inclusionListRepository.save(inclusionList);
		}
	}

	private boolean isInclusionListOn() {
		return InclusionList.VALUE_LIST_ON.equalsIgnoreCase(switchInclusionList.trim());
	}

	private boolean isAssigneeDocketDerivationOn() {
		return InclusionList.VALUE_LIST_ON.equalsIgnoreCase(assigneeDocketValidation.trim());
	}

	private void resolveCreateApplicationRequest(final Long notificationId) {
		if (notificationId != null) {
			long recordUpdated = applicationBaseDAO.acceptCreateAppRequest(notificationId);
			log.info(String.format("[CREATE APPLICATION REQUEST]: Successfully updated {0} records.", recordUpdated));
			notificationProcessService.completeTask(notificationId, NotificationStatus.COMPLETED);
			log.info(String.format("[Notification]: Successfully updated notification for {0}.", notificationId));
		}
	}

	private void resolveCreateFamilyRequest(final Long notificationId) {
		if (notificationId != null) {
			long recordUpdated = applicationBaseDAO.acceptCreateFamilyRequest(notificationId);
			log.info(String.format("[CREATE FAMILY REQUEST]: Successfully updated {0} records.", recordUpdated));
			notificationProcessService.completeTask(notificationId, NotificationStatus.COMPLETED);
			log.info(String.format("[Notification]: Successfully updated notification for {0}.", notificationId));
		}
	}

	@Override
	public MdmRecordDTO fetchApplicationDataForUpdate(final long applicationId) {
		return applicationBaseDAO.fetchApplicationData(applicationId);
	}

	@Override
	@Transactional
	public void updateApplication(final ApplicationBase uiApplication, Long notificationId) {
		final String module = "[Update Application]";
		if (invalidApplicationDetails(uiApplication) || uiApplication.getId() == null) {
			throw new IllegalArgumentException(String.format("{0}: Missing required attributes.", module));
		}

		final Long appId = uiApplication.getId();
		log.info(String.format("{0}: Processing update request for application {1}.", module, appId));
		final ApplicationBase dbApplication = applicationBaseDAO.find(appId);
		assertApplicationForUpdates(dbApplication, uiApplication);
		mergeUpdates(dbApplication, uiApplication);
		setApplicationReferences(Arrays.asList(dbApplication));
		setDocketSegments(Arrays.asList(dbApplication));
		updateApplicationFamily(dbApplication, uiApplication.getFamilyId());
		if (notificationId != null) {
			resolveCreateFamilyRequest(notificationId);
		}
		if (notificationId != null) {
			resolveCreateFamilyRequest(notificationId);
		}
		try {
			applicationBaseDAO.persist(dbApplication);
		} catch (final Exception e) {
			log.error(String.format("{0}: Error occurred while persisting changes.", module), e);
			throw new ApplicationException(e);
		}

	}

	private void updateApplicationFamily(ApplicationBase dbApplication, String familyId) {
		final String existingFamily = dbApplication.getFamilyId();
		if (!StringUtils.equalsIgnoreCase(existingFamily, familyId)) {
			if (/*- StringUtils.isBlank(familyId) && */
					ApplicationType.isFirstFiling(dbApplication.getAppDetails().getChildApplicationType())) {

				log.info(String.format("[Update Application]: Generating new family Id for application {0}.",
						dbApplication.getApplicationNumber()));
				familyId = countFamilyApplications(existingFamily) != 1L ? applicationBaseDAO.generateFamilyId()
						: existingFamily;
			}
			log.info(String.format("[Update Application]: Assigning new family Id {0} for application {1}.", familyId,
					dbApplication.getApplicationNumber()));
			dbApplication.setFamilyId(familyId);
		}
	}

	private void mergeUpdates(ApplicationBase dbApplication, ApplicationBase uiApplication) {
		final SubSource subSource = dbApplication.getSubSource();
		dbApplication.getAppDetails().setChildApplicationType(uiApplication.getAppDetails().getChildApplicationType());
		dbApplication.getCustomer().setCustomerNumber(uiApplication.getCustomer().getCustomerNumber());

		if (isEditable(Field.DOCKET_NO, subSource)) {
			dbApplication.getAttorneyDocketNumber().setSegment(uiApplication.getAttorneyDocketNumber().getSegment());
		}
		dbApplication.getAssignee().setName(uiApplication.getAssignee().getName());

		dbApplication.getOrganizationDetails().setEntity(uiApplication.getOrganizationDetails().getEntity());

		if (isEditable(Field.FILING_DATE, subSource)) {
			dbApplication.getAppDetails().setFilingDate(uiApplication.getAppDetails().getFilingDate());
		}
		if (isEditable(Field.CONFIRMATION_NO, subSource)) {
			dbApplication.getAppDetails().setConfirmationNumber(uiApplication.getAppDetails().getConfirmationNumber());
		}
		if (isEditable(Field.TITLE, subSource)) {
			dbApplication.getPatentDetails().setTitle(uiApplication.getPatentDetails().getTitle());
		}

		dbApplication.getOrganizationDetails()
		.setExportControl(uiApplication.getOrganizationDetails().isExportControl());

		PublicationDetails uiPublish = uiApplication.getPublicationDetails();
		if (uiPublish != null) {
			if (dbApplication.getPublicationDetails() == null) {
				dbApplication.setPublicationDetails(new PublicationDetails());
			}
			dbApplication.getPublicationDetails().setPublicationNumberRaw(uiPublish.getPublicationNumberRaw());
			dbApplication.getPublicationDetails().setPublishedOn(uiPublish.getPublishedOn());
		} else {
			dbApplication.setPublicationDetails(null);
		}

		PatentDetails uiPatent = uiApplication.getPatentDetails();
		if (uiPatent != null) {
			if (dbApplication.getPatentDetails() == null) {
				dbApplication.setPatentDetails(new PatentDetails());
			}
			dbApplication.getPatentDetails().setPatentNumberRaw(uiPatent.getPatentNumberRaw());
			dbApplication.getPatentDetails().setIssuedOn(uiPatent.getIssuedOn());
		} else {
			dbApplication.setPatentDetails(null);
		}
	}

	private boolean isEditable(Field field, SubSource subSource) {
		return IncompatibleAttribute.canUpdateAttribute(field, subSource.name());
	}

	private void assertApplicationForUpdates(ApplicationBase dbApplication, ApplicationBase uiApplication) {
		if (!(StringUtils.equalsIgnoreCase(dbApplication.getAppDetails().getApplicationNumberRaw(),
				uiApplication.getAppDetails().getApplicationNumberRaw())
				&& StringUtils.equalsIgnoreCase(dbApplication.getJurisdiction().getCode(),
						uiApplication.getJurisdiction().getCode()))) {

			log.error("[Update Application]: Mismatch detected in non-editable fields.");
			throw new SecurityException("[Update Application]: Mismatch detected in non-editable fields.");
		}
	}

	@Override
	@Transactional
	public void saveApplicationDrafts(List<ApplicationStage> drafts) {
		List<ApplicationStage> draftsToSave = filterDraftsToPersist(drafts);

		if (CollectionUtils.isNotEmpty(draftsToSave)) {
			saveDrafts(draftsToSave);
		}
	}

	private void saveDrafts(List<ApplicationStage> draftsToSave) {
		List<DraftIdentityDTO> draftApplications = new ArrayList<>();
		Long currentUser = BlackboxSecurityContextHolder.getUserId();

		// Set business attributes
		draftsToSave.forEach(e -> {
			e.setSource(Source.DRAFT);
			e.setSubSource(SubSource.MANUAL);
			e.setApplicationNumber(e.getAppDetails().getApplicationNumberRaw());
			DraftIdentityDTO draftId = new DraftIdentityDTO(e.getJurisdiction(), e.getApplicationNumber(), currentUser);
			draftApplications.add(draftId);
			e.setDraftId(draftId);
			e.setCreatedByUser(currentUser);
		});

		try {
			log.info(String.format("Saving drafts for applications {0}.", draftApplications));
			Map<DraftIdentityDTO, Long> draftIds = applicationStageDAO.mapApplicationDrafts(draftApplications);
			draftsToSave.forEach(e -> {
				e.setId(draftIds.get(e.getDraftId()));
				e.preUpdate();
			});
			this.applicationStageDAO.persist(draftsToSave);

		} catch (final Exception e) {
			log.error(String.format("[Save Drafts]: An exception occurred while saving drafts for {0}.",
					draftApplications), e);
			throw new ApplicationException(e);
		}
	}

	private List<ApplicationStage> filterDraftsToPersist(List<ApplicationStage> drafts) {
		return drafts.stream()
				.filter(draft -> StringUtils.isNoneBlank(draft.getJurisdiction())
						&& StringUtils.isNoneBlank(draft.getAppDetails().getApplicationNumberRaw()))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteDrafts(List<DraftIdentityDTO> draftIds) {
		if (CollectionUtils.isEmpty(draftIds)) {
			throw new IllegalArgumentException("[Delete Application Drafts]: Draft Ids must not be empty.");
		}
		try {
			log.info(String.format("[Delete Application Drafts]: Deleting drafts for application {0}.", draftIds));
			long numDraftsDeleted = this.applicationStageDAO.delete(draftIds);
			log.info(String.format("[Delete Application Drafts]: Total {0} drafts deleted for application {1}.",
					numDraftsDeleted, draftIds));
		} catch (final Exception e) {
			log.error(String.format(
					"[Delete Application Drafts]: Exception occurred while deleting drafts for applications {0}.",
					draftIds), e);
			throw new ApplicationException(e);
		}

	}

	private void setDocketSegments(List<ApplicationBase> applications) {
		String docketSeperator = masterDataService.getAttorneyDocketSeperator();
		for (ApplicationBase application : applications) {
			application.getAttorneyDocketNumber().setSeparator(docketSeperator);
		}
	}

	@Override
	public long countFamilyApplications(final String familyId) {
		if (StringUtils.isBlank(familyId)) {
			throw new IllegalArgumentException("[Count Family Applications] - Family Id must not be blank.");
		}
		return applicationBaseDAO.countFamilyApplications(familyId);
	}

	@Override
	public long sendUpdateAssigneeNotification() {
		long noOfRequests = 0L;
		List<ApplicationBase> items = applicationBaseDAO.getDefaultAssigneeApplications();
		for (ApplicationBase applicationBase : items) {
			if (applicationBaseDAO.checkIfNotificationExists(applicationBase.getId()) == 0) {
				notificationProcessService.createNotification(applicationBase, null, null, applicationBase.getId(), 
						EntityName.APPLICATION_BASE, NotificationProcessType.UPDATE_ASSIGNEE, null, null);
				noOfRequests += 1L;
			}
		}
		return noOfRequests;
	}

	@Override
	public Assignee getAssigneeFromDocketNo(String attorneyDocketNumber) {
		int segmentToMatch = masterDataService.getAttorneyDocketFormat().getSegmentToMatch();
		if (isAssigneeDocketDerivationOn()) {
			String selectedSegmentValue = (attorneyDocketNumber != null)
					? attorneyDocketNumber.split("\\.")[segmentToMatch - 1] : null;
					List<AssigneeAttorneyDocketNo> assigneeAttorneyDocketNoList = masterDataService
							.getAssigneeAttorneyDocketNo(selectedSegmentValue);
					if (assigneeAttorneyDocketNoList.size() == 0
							|| !"ACTIVE".equals(assigneeAttorneyDocketNoList.get(0).getStatus())) {
						return null;
					}
					long assigneeId = assigneeAttorneyDocketNoList.get(0).getAssigneeId();
					return assigneeRepository.findOne(assigneeId);
		} else {
			return assigneeRepository.findByName(Assignee.NAME_DEFAULT_ASSIGNEE);
		}
	}

	@Override
	public Assignee getApplicationAssignee(final long applicationId) {
		return applicationBaseDAO.findAssignee(applicationId);
	}

	@Override
	@Transactional
	public void updateApplication(final ApplicationDetailsDTO appDetails) {
		if (appDetails == null) {
			throw new IllegalArgumentException("Missing mandatory parameters.");
		}

		log.info(String.format("[APPLICATION UPDATE]: Merging updates for application {0}.", appDetails.getDbId()));
		ApplicationBase dbApplication = applicationBaseDAO.find(appDetails.getDbId());
		mergeUpdates(dbApplication, appDetails);
		applicationBaseDAO.persist(dbApplication);
		log.info(String.format("[APPLICATION UPDATE]: Successfully updated application {0}.", appDetails.getDbId()));
	}
	
	@Override
	@Transactional
	public void saveImportedApplications(final ApplicationStage firstFilingCase,
			final Map<Boolean, List<ApplicationStage>> validatedApplicationMap) {
		List<ApplicationBase> applicationBaseList = new ArrayList<>();
		List<ApplicationStage> applicationStageList = new ArrayList<>();
		
		if (firstFilingCase != null) {
			ApplicationBase firstFilingApplication = createApplicationBaseEnity(firstFilingCase);
			applicationBaseDAO.persist(firstFilingApplication);
			firstFilingCase.setStatus(QueueStatus.SUCCESS);
			applicationStageList.add(firstFilingCase);
		}
		
		for (ApplicationStage app : validatedApplicationMap.get(Boolean.TRUE)) {
			ApplicationBase application = createApplicationBaseEnity(app);
			applicationBaseList.add(application);
			firstFilingCase.setStatus(QueueStatus.SUCCESS);
		}
		
		applicationStageList.addAll(validatedApplicationMap.get(Boolean.TRUE));
		applicationStageList.addAll(validatedApplicationMap.get(Boolean.FALSE));
		
		if (CollectionUtils.isNotEmpty(applicationBaseList)) {
			applicationBaseDAO.persist(applicationBaseList);
		}
		applicationStageDAO.persist(applicationStageList);
	}
	
	@Override
	public void saveApplicationStage(List<ApplicationStage> applicationList) {
		applicationStageDAO.persist(applicationList);
	}
	
	@Override
	@Transactional
	public void updateApplicationStageStatus(List<Long> applicationIds, QueueStatus newStatus,
			ApplicationValidationStatus validationStatus, long updatedBy){
		applicationStageDAO.updateApplicationStatus(applicationIds, newStatus, validationStatus, updatedBy);
	}

	private void mergeUpdates(ApplicationBase dbApplication, ApplicationDetailsDTO appDetails) {
		dbApplication.getAppDetails().setFilingDate(
				BlackboxDateUtil.toCalendar(appDetails.getFilingDate(), ApplicationDetailsDTO.DATE_FORMAT));
		dbApplication.getPatentDetails().setFirstNameInventor(appDetails.getInventor());
		dbApplication.getPatentDetails().setArtUnit(appDetails.getArtUnit());
		dbApplication.getPatentDetails().setExaminer(appDetails.getExaminer());
		dbApplication.setAttorneyDocketNumber(new AttorneyDocketNumber(appDetails.getDocketNo()));
	}
	
	private ApplicationBase createApplicationBaseEnity(ApplicationStage appStage) {
		ApplicationBase application = new ApplicationBase();
		application.setAppDetails(appStage.getAppDetails());
		application.setApplicationNumber(appStage.getApplicationNumber());
		application.setAssignee(appStage.getAppAssignee());
		AttorneyDocketNumber dNumber = attorneyDocketRepository.findAttorneyDocketNumberByValue(appStage
				.getAttorneyDocketNumber());

		if (null == dNumber) {
			dNumber = new AttorneyDocketNumber();
			dNumber.setSegment(appStage.getAttorneyDocketNumber());
			attorneyDocketRepository.save(dNumber);
		}
		application.setAttorneyDocketNumber(dNumber);

		Customer customer = customerRepository.findCustomerByNumber(appStage.getCustomer());
		application.setCustomer(customer);
		application.setDescription(appStage.getDescription());
		application.setFamilyId(appStage.getFamilyId());

		com.blackbox.ids.core.model.mstr.Jurisdiction jurisdiction = jurisdictionRepository
				.findByJurisdictionValue(appStage.getJurisdiction());
		application.setJurisdiction(jurisdiction);
		application.setNewRecordStatus(MDMRecordStatus.BLANK);
		application.setOrganization(Organization.DEFAULT_ORGANIZATION);
		setProsecutionStatus(appStage);
		application.setOrganizationDetails(appStage.getOrganizationDetails());
		application.setPatentDetails(appStage.getPatentDetails());
		application.setPublicationDetails(appStage.getPublicationDetails());
		application.setRecordStatus(MDMRecordStatus.ACTIVE);
		ApplicationScenario scenario = ApplicationScenario.getScenario(
				com.blackbox.ids.core.model.mdm.IncompatibleAttribute.Jurisdiction.fromString(appStage
						.getJurisdiction()), appStage.getAppDetails().getChildApplicationType());
		application.setScenario(scenario);
		application.setSource(appStage.getSource());
		application.setSubSource(appStage.getSubSource());
		application.setTechnologyGroup(TechnologyGroup.DEFAULT_TECHNOLOGY_GROUP);
		setConvertedNumerValues(application);
		return application;
	}
	
	private void setProsecutionStatus(final ApplicationStage application) {
		ProsecutionStatus status = application.getOrganizationDetails().getProsecutionStatus();
		if (!ProsecutionStatus.ABANDONED.equals(status)) {
			if (StringUtils.isNotBlank(application.getPatentDetails().getPatentNumber())) {
				status = ProsecutionStatus.GRANTED;
			} else if (StringUtils.isNotBlank(application.getPublicationDetails().getPublicationNumber())) {
				status = ProsecutionStatus.PUBLISHED;
			} else {
				status = ProsecutionStatus.PENDING_PUBLICATION;
			}
		}
		application.getOrganizationDetails().setProsecutionStatus(status);
	}
	
}
