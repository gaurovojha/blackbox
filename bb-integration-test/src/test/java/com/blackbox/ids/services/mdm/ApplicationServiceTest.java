/**
 *
 */
package com.blackbox.ids.services.mdm;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.blackbox.ids.core.dao.correspondence.ICorrespondenceStageDAO;
import com.blackbox.ids.core.dao.notification.NotificationProcessDAO;
import com.blackbox.ids.core.dao.webcrawler.CreateApplicationQueueDAO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.CreateReqApplicationDTO;
import com.blackbox.ids.core.dto.mdm.CreateReqFamilyDTO;
import com.blackbox.ids.core.dto.mdm.DraftDTO;
import com.blackbox.ids.core.dto.mdm.DraftIdentityDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqAssigneeDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqFamilyDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.ActiveRecordsFilter;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.SortAttribute;
import com.blackbox.ids.core.dto.mdm.family.FamilyDetailDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilySearchFilter;
import com.blackbox.ids.core.dto.mdm.family.FamilySearchFilter.FamilyLinkage;
import com.blackbox.ids.core.model.correspondence.Correspondence.Source;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.Correspondence.SubSourceTypes;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.Assignee.Entity;
import com.blackbox.ids.core.model.mstr.AttorneyDocketNumber;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcess;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.model.webcrawler.CreateApplicationQueue;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.dto.JurisdictionDTO;
import com.blackbox.ids.dto.correspondence.CorrespondenceFormDto;
import com.blackbox.ids.dto.mdm.ApplicationValidationDTO;
import com.blackbox.ids.dto.mdm.ApplicationValidationStatus;
import com.blackbox.ids.it.AbstractIntegrationTest;
import com.blackbox.ids.services.common.MasterDataService;
import com.blackbox.ids.services.correspondence.ICorrespondenceService;

/**
 * The class {@code ApplicationServiceTest} holds the test cases around the create and edit application manually in
 * Blackbox.
 * <p/>
 * Please note that ordering for test cases has been controlled and executed in lexicographic order.
 *
 * @author ajay2258
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationServiceTest extends AbstractIntegrationTest {

	enum ApplicationData {
		ASSIGNEE("Apple"),
		CUSTOMER("123-456-FAC"),
		JURISDICTION("US"),
		APP_NUMBER("10000001"),
		UPDATED_APP_NUMBER("10000002"),
		DOCKET_NO("123.456.ABC.001"),
		TITLE("Sample Title I"),
		UPDATED_TITLE("Sample Title II"),
		CHILD_APP_NUMBER("20000001"),
		PUBLICATION_NO("2016-1234567"),
		PATENT_NO("1,234,567"),
		DRAFT_APPLICATION_NO("30000001");

		public final String value;

		private ApplicationData(final String value) {
			this.value = value;
		}
	}

	enum CorrespondenceStagingData {
		JURISDICTION("US"),
		APP_NUMBER("11122233");

		public final String value;

		private CorrespondenceStagingData(final String value) {
			this.value = value;
		}
	}

	protected static final ApplicationType MANUAL_APP_TYPE = ApplicationType.FIRST_FILING;
	protected static final ApplicationType MANUAL_CHILD_APP_TYPE = ApplicationType.CIP;
	private static final String CORRESPONDENCE_DOC_PATH = "META-INF/test-data/correspondence/pdf/BookmarkB1.pdf";

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private MasterDataService masterDataService;

	@Autowired
	private MdmDashboardService dashboardService;

	@Autowired
	private CreateApplicationQueueDAO createApplicationQueueDao;

	@Autowired
	private NotificationProcessDAO notificationProcessDAO;

	@Autowired
	private ICorrespondenceStageDAO correspondenceStageDAO;

	@Autowired
	private ICorrespondenceService correspondenceService;

	@Test
	public void testInsertedData() {
		List<JurisdictionDTO> jurisdictions = masterDataService.getAllJurisdictions();
		Assert.assertTrue("Jurisdictions must be defined in setup.", CollectionUtils.isNotEmpty(jurisdictions));
	}

	/*- ----------------------------------------------------------------
	 * 					CREATE APPLICATION - FIRST FILING
	 * -------------------------------------------------------------------- */
	/** Validates the application number before creating manual application. */
	@Test
	public void testManualApplication_0() {
		ApplicationValidationDTO appValidationData = new ApplicationValidationDTO(ApplicationData.JURISDICTION.value,
				ApplicationData.APP_NUMBER.value, MANUAL_APP_TYPE, null);
		ApplicationValidationStatus validationStatus = applicationService.validateApplicationNo(appValidationData);

		Assert.assertNotNull("Must return a non-null status.", validationStatus);
		Assert.assertEquals("Application no. must be available for creation.", ApplicationValidationStatus.OK,
				validationStatus);
	}

	/** Test 'Create Application Manually' Feature. */
	@Test
	@Rollback(false)
	public void testManualApplication_1() {
		ApplicationBase application = createTestApplication();
		applicationService.saveManualApplications(Arrays.asList(application), null);
		Assert.assertNotNull("Unique Id must be generated post application creation.", application.getId());
		Assert.assertNotNull("Converted application number mustn't be null.", application.getApplicationNumber());
		Assert.assertNotNull("Family Id must be generated for application.", application.getFamilyId());
	}

	/** Asserts that created application exists within database. */
	@Test
	public void testManualApplication_2() {
		ApplicationBase application = applicationService.findByApplicationNo(ApplicationData.JURISDICTION.value,
				ApplicationData.APP_NUMBER.value);
		Assert.assertNotNull("Application must exist in database.", application);
	}

	/** Validates the application number for duplicate check after creating manual application. */
	@Test
	public void testManualApplication_3() {
		ApplicationValidationDTO appValidationData = new ApplicationValidationDTO(ApplicationData.JURISDICTION.value,
				ApplicationData.APP_NUMBER.value, MANUAL_APP_TYPE, null);
		ApplicationValidationStatus validationStatus = applicationService.validateApplicationNo(appValidationData);

		Assert.assertNotNull("Must return a non-null status.", validationStatus);
		Assert.assertEquals("Application no. must be not available for creation.",
				ApplicationValidationStatus.DUPLICATE, validationStatus);
	}

	/** Asserts that created application exists with ACTIVE status in database. */
	@Test
	public void testManualApplication_4() {
		setTestUser();
		ActiveRecordsFilter filter = new ActiveRecordsFilter(null, null, null);
		PaginationInfo pageInfo = new PaginationInfo(100L, 0L, SortAttribute.APPLICATION_NO.attr, true);
		SearchResult<MdmRecordDTO> result = dashboardService.filterActiveRecords(filter, pageInfo);
		Assert.assertFalse("Atleast one active application must exists in system.", result.getItems().isEmpty());
		List<String> activeApplications = result.getItems().stream().map(MdmRecordDTO::getApplicationNumber)
				.collect(Collectors.toList());
		Assert.assertTrue("Application must exists in active applications.",
				activeApplications.contains(ApplicationData.APP_NUMBER.value));
	}

	/** Asserts that an entry in download office action queue is made post application creation. */
	@Ignore
	public void testManualApplication_5() {
		// XXX: APIs don't exist to check for entry in queues.
		Assert.assertTrue(true);
	}

	/** Checks the {@link ApplicationService#fetchFamilyDetails(String)} for created application. */
	@Test
	public void testManualApplication_6() {
		ApplicationBase application = applicationService.findByApplicationNo(ApplicationData.JURISDICTION.value,
				ApplicationData.APP_NUMBER.value);
		final String familyId = application.getFamilyId();
		FamilyDetailDTO familyDetails = applicationService.fetchFamilyDetails(familyId);
		Assert.assertNotNull("Family details must not be null.", familyDetails);
		Assert.assertEquals("Application number must match.", ApplicationData.APP_NUMBER.value,
				familyDetails.getApplicationNo());
	}

	/** Tests {@link ApplicationService#fetchApplicationDataForUpdate(long)} */
	@Test
	public void testManualApplication_7() {
		ApplicationBase application = applicationService.findByApplicationNo(ApplicationData.JURISDICTION.value,
				ApplicationData.APP_NUMBER.value);
		MdmRecordDTO applicationDetails = applicationService.fetchApplicationDataForUpdate(application.getId());
		Assert.assertNotNull("Application details must not be null.", applicationDetails);
	}

	private ApplicationBase createTestApplication() {
		ApplicationBase application = new ApplicationBase();

		// Identification Details
		application.setJurisdiction(new Jurisdiction(null, ApplicationData.JURISDICTION.value));
		application.getAppDetails().setApplicationNumberRaw(ApplicationData.APP_NUMBER.value);
		application.getAppDetails().setChildApplicationType(MANUAL_APP_TYPE);

		// Details
		application.setCustomer(new Customer(null, ApplicationData.CUSTOMER.value));
		application.setAttorneyDocketNumber(new AttorneyDocketNumber(ApplicationData.DOCKET_NO.value));
		Assignee assignee = new Assignee();
		assignee.setName(ApplicationData.ASSIGNEE.value);
		application.setAssignee(assignee);
		application.getOrganizationDetails().setEntity(Entity.SMALL);
		application.getAppDetails().setFilingDate(BlackboxDateUtil.getDateBeforeDays(91, false));
		application.getAppDetails().setConfirmationNumber("100121001");
		application.getPatentDetails().setTitle(ApplicationData.TITLE.value);
		application.getPatentDetails().setPatentNumberRaw(ApplicationData.PATENT_NO.value);
		application.getPublicationDetails().setPublicationNumberRaw(ApplicationData.PUBLICATION_NO.value);

		return application;
	}

	/*- ----------------------------------------------------------------
	 * 				CREATE APPLICATION - SUBSEQUENT FILING /CIP
	 * -------------------------------------------------------------------- */
	/** Creates a non-first filing application. */
	@Test
	@Transactional
	@Rollback(false)
	public void testManualApplication_CIP_0() {
		ApplicationBase application = createChildApplication();
		final String familyId = fetchFamilyId();
		application.setFamilyId(familyId);
		applicationService.saveManualApplications(Arrays.asList(application), null);
		Assert.assertNotNull("Unique Id must be generated post application creation.", application.getId());
		Assert.assertNotNull("Converted application number mustn't be null.", application.getApplicationNumber());
		Assert.assertEquals("Application should be linked to given family only.", familyId, application.getFamilyId());
	}

	@Test
	public void testManualApplication_CIP_1() {
		ApplicationBase application = applicationService.findByApplicationNo(ApplicationData.JURISDICTION.value,
				ApplicationData.APP_NUMBER.value);
		FamilySearchFilter searchFilter = new FamilySearchFilter(application.getFamilyId(), false);
		List<FamilyDetailDTO> familyDetails = applicationService.fetchFamilyDetail(searchFilter);
		Assert.assertTrue("Family should have 2 applications linked.", familyDetails.size() >= 2);
		List<String> appNumbers = familyDetails.stream().map(FamilyDetailDTO::getApplicationNo).collect(Collectors.toList());
		Assert.assertTrue("Application must be the member of this family.",
				appNumbers.contains(ApplicationData.APP_NUMBER.value)
				&& appNumbers.contains(ApplicationData.CHILD_APP_NUMBER.value));
	}

	private String fetchFamilyId() {
		FamilySearchFilter searchFilter = new FamilySearchFilter();
		searchFilter.setDocketNo(ApplicationData.DOCKET_NO.value);
		searchFilter.setFamilyLinker(FamilyLinkage.ATTORNEY_DOCKET_NUMBER);
		searchFilter.setOnlyFirstFiling(true);
		List<FamilyDetailDTO> familyDetails = applicationService.fetchFamilyDetail(searchFilter);
		return familyDetails.get(0).getFamilyId();
	}

	private ApplicationBase createChildApplication() {
		ApplicationBase application = new ApplicationBase();

		// Identification Details
		application.setJurisdiction(new Jurisdiction(null, ApplicationData.JURISDICTION.value));
		application.getAppDetails().setApplicationNumberRaw(ApplicationData.CHILD_APP_NUMBER.value);
		application.getAppDetails().setChildApplicationType(MANUAL_CHILD_APP_TYPE);

		// Details
		application.setCustomer(new Customer(null, ApplicationData.CUSTOMER.value));
		application.setAttorneyDocketNumber(new AttorneyDocketNumber(ApplicationData.DOCKET_NO.value));
		Assignee assignee = new Assignee();
		assignee.setName(ApplicationData.ASSIGNEE.value);
		application.setAssignee(assignee);
		application.getOrganizationDetails().setEntity(Entity.SMALL);
		application.getAppDetails().setFilingDate(BlackboxDateUtil.getDateBeforeDays(91, false));
		application.getAppDetails().setConfirmationNumber("200121002");

		application.getPatentDetails().setTitle(ApplicationData.TITLE.value);
		application.getPatentDetails().setPatentNumberRaw(ApplicationData.PATENT_NO.value);
		application.getPublicationDetails().setPublicationNumberRaw(ApplicationData.PUBLICATION_NO.value);

		return application;
	}

	/*- ----------------------------------------------------------------
	 * 							EDIT APPLICATION
	 * -------------------------------------------------------------------- */
	/** Tests the edit application feature - Positive Scenario */
	@Test
	@Transactional
	@Rollback(false)
	public void testManualApplication_Edit_1() {
		// Fetch Application to Update
		ApplicationBase application = applicationService.findByApplicationNo(ApplicationData.JURISDICTION.value,
				ApplicationData.APP_NUMBER.value);
		int version = application.getVersion();

		// Update Attributes
		application.getPatentDetails().setTitle(ApplicationData.UPDATED_TITLE.value);

		// Save Changes
		applicationService.updateApplication(application, null);
		Assert.assertEquals("Version must be incremented to application update.", application.getVersion(),
				version + 1);
	}

	/** Tests the edit application feature - Negative Scenario */
	@Test(expected = SecurityException.class)
	public void testManualApplication_Edit_2() {
		// Fetch Application to Update
		ApplicationBase application = applicationService.findByApplicationNo(ApplicationData.JURISDICTION.value,
				ApplicationData.APP_NUMBER.value);

		ApplicationBase appToUpdate = createTestApplication();
		appToUpdate.setId(application.getId());

		// Update non-editable attributes
		appToUpdate.getAppDetails().setApplicationNumberRaw(ApplicationData.UPDATED_APP_NUMBER.value);

		// Save Changes
		applicationService.updateApplication(appToUpdate, null);
		Assert.assertFalse("Service should throw security exception.", true);
	}

	/** Verifies the fields updated in {@link #testManualApplication_Edit_1()} */
	@Test
	public void testManualApplication_Edit_3() {
		// Fetch Updated Application
		ApplicationBase application = applicationService.findByApplicationNo(ApplicationData.JURISDICTION.value,
				ApplicationData.APP_NUMBER.value);

		// Assert for required updates.
		Assert.assertEquals("Title must have been updated for application.", ApplicationData.UPDATED_TITLE.value,
				application.getPatentDetails().getTitle());
		Assert.assertNotEquals("Application number must not be updated.", ApplicationData.UPDATED_APP_NUMBER.value,
				application.getAppDetails().getApplicationNumberRaw());
		Assert.assertEquals("Application number must remain the original one.", ApplicationData.APP_NUMBER.value,
				application.getAppDetails().getApplicationNumberRaw());
	}

	/** Tests the edit application feature - Negative Scenario - Missing mandatory fields */
	@Test(expected = IllegalArgumentException.class)
	public void testManualApplication_Edit_4() {
		ApplicationBase appToUpdate = createTestApplication();
		appToUpdate.getAppDetails().setApplicationNumberRaw(null);
		appToUpdate.setJurisdiction(null);

		// Update non-editable attributes
		appToUpdate.getPatentDetails().setTitle(ApplicationData.UPDATED_TITLE.value);

		// Save Changes
		applicationService.updateApplication(appToUpdate, null);
		Assert.assertFalse("Service should throw security exception.", true);
	}

	/*- ----------------------------------------------------------------
	 * 							UPDATE FAMILY LINKAGE
	 * -------------------------------------------------------------------- */
	/**
	 * Tests the edit family linkage feature - Negative Scenario - Family for first filing (having child associated) is
	 * being changed (User has done some hack) - Family Id not updated in database.
	 */
	@Test
	public void testManualApplication_EditFamily_1() {
		// Fetch Application to Update
		ApplicationBase application = applicationService.findByApplicationNo(ApplicationData.JURISDICTION.value,
				ApplicationData.APP_NUMBER.value);

		final String existingFamily = application.getFamilyId();
		ApplicationBase appToUpdate = createTestApplication();
		appToUpdate.setId(application.getId());

		// Update Attributes
		appToUpdate.setFamilyId("F0000099");

		// Save Changes
		applicationService.updateApplication(appToUpdate, null);
		ApplicationBase updatedApplication = applicationService.findByApplicationNo(ApplicationData.JURISDICTION.value,
				ApplicationData.APP_NUMBER.value);
		Assert.assertEquals("New family Id must not be generated.", existingFamily, updatedApplication.getFamilyId());
	}

	/** Tests the edit family linkage feature - Positive Scenario - CIP changed to first filing. */
	@Test
	@Transactional
	@Rollback(false)
	public void testManualApplication_EditFamily_2() {
		// Fetch Application to Update
		ApplicationBase childApplication = applicationService.findByApplicationNo(ApplicationData.JURISDICTION.value,
				ApplicationData.CHILD_APP_NUMBER.value);
		final String existingFamily = childApplication.getFamilyId();
		childApplication.setFamilyId(null);
		int version = childApplication.getVersion();

		// Update Attributes
		childApplication.getAppDetails().setChildApplicationType(ApplicationType.FIRST_FILING);

		// Save Changes
		applicationService.updateApplication(childApplication, null);
		Assert.assertEquals("Version must be incremented to application update.", childApplication.getVersion(),
				version + 1);
		Assert.assertNotEquals("New family Id must be generated.", existingFamily, childApplication.getFamilyId());
	}

	/*- ----------------------------------------------------------------
	 * 							DRAFT APPLICATION
	 * -------------------------------------------------------------------- */
	/** Tests the create draft feature. */
	@Test
	@Rollback(false)
	public void testSaveApplicationDraft_1() {
		setTestUser();
		ApplicationStage draft = createApplicationDraft();
		applicationService.saveApplicationDrafts(Arrays.asList(draft));
		Assert.assertNotNull("Draft must be saved successfully.", draft.getId());
	}

	/**
	 * Fetches the list of application drafts for current user. It validates the {@link #testSaveApplicationDraft_1()}.
	 */
	@Test
	public void testSaveApplicationDraft_2() {
		setTestUser();
		List<DraftDTO> userDrafts = dashboardService.getCurrentUserDrafts();
		List<String> draftApplications = userDrafts.stream().map(DraftDTO::getApplicationNumber)
				.collect(Collectors.toList());
		Assert.assertTrue("A draft for application must exist.",
				draftApplications.contains(ApplicationData.DRAFT_APPLICATION_NO.value));
	}

	@Test
	@Rollback(false)
	public void testSaveApplicationDraft_3() {
		setTestUser();
		DraftIdentityDTO draftId = new DraftIdentityDTO(ApplicationData.JURISDICTION.value,
				ApplicationData.DRAFT_APPLICATION_NO.value, TEST_USER_ID);

		// Delete application drafts
		applicationService.deleteDrafts(Arrays.asList(draftId));

		List<DraftDTO> userDrafts = dashboardService.getCurrentUserDrafts();
		List<String> draftApplications = userDrafts.stream().map(DraftDTO::getApplicationNumber)
				.collect(Collectors.toList());
		Assert.assertFalse("Draft must be deleted successfully.",
				draftApplications.contains(ApplicationData.DRAFT_APPLICATION_NO.value));
	}

	private ApplicationStage createApplicationDraft() {
		ApplicationStage draft = new ApplicationStage();

		// Identification Details
		draft.setJurisdiction(ApplicationData.JURISDICTION.value);
		draft.getAppDetails().setApplicationNumberRaw(ApplicationData.DRAFT_APPLICATION_NO.value);
		draft.getAppDetails().setChildApplicationType(MANUAL_APP_TYPE);

		// Details
		draft.setCustomer(ApplicationData.CUSTOMER.value);
		draft.setAttorneyDocketNumber(ApplicationData.DOCKET_NO.value);
		draft.setAssignee(ApplicationData.ASSIGNEE.value);

		return draft;
	}

	/*- ----------------------------------------------------------------
	 * 						Actions on Active Records
	 * -------------------------------------------------------------------- */
	/** Verifies create application AUTO request reject functionality */
	@Test
	@Ignore
	public void testCreateAppRequestFromAutoReject() {
		long entityId = 1L;
		String entityName = EntityName.CREATE_APPLICATION_QUEUE.name();
		long notificationId = 1L;
		Long recordsUpdated ;
		recordsUpdated = applicationService.rejectCreateAppRequest(entityId, entityName, notificationId);
		Assert.assertNotNull(recordsUpdated);
		CreateApplicationQueue appQueue = createApplicationQueueDao.find(entityId);
		NotificationProcess notification = notificationProcessDAO.find(notificationId);
		Assert.assertEquals(QueueStatus.REJECTED, appQueue.getStatus());
		Assert.assertEquals(true , notification.isActive());
	}

	/*- ----------------------------------------------------------------
	 * 							CREATE APPLICATION REQUEST
	 * -------------------------------------------------------------------- */
	/* Creating correspondence and create application notification */
	@Test
	@Rollback(false)
	public void testCreateAppRequest_1() {

		int count = 0;
		// Setting up user
		setTestUser();

		//fetch create application request records
		ActiveRecordsFilter filter = new ActiveRecordsFilter();
		PaginationInfo pageInfo = new PaginationInfo(20L, 0L, SortAttribute.FILING_DATE.attr, false);
		SearchResult<CreateReqApplicationDTO>createAppReqList = dashboardService.getCreateAppRecords(filter, pageInfo);
		count = createAppReqList.getItems().size();

		CorrespondenceFormDto correspondenceFormDto = new CorrespondenceFormDto();
		correspondenceFormDto.setApplicationNumber(CorrespondenceStagingData.APP_NUMBER.value);
		correspondenceFormDto.setJurisdiction(CorrespondenceStagingData.JURISDICTION.value);
		correspondenceFormDto.setDocumentCode(4L);
		correspondenceFormDto.setMailingDate(BlackboxDateUtil.strToDate("Nov 24, 2015", TimestampFormat.MMMDDYYYY));
		correspondenceFormDto.setUrgent(false);
		correspondenceFormDto.setFile((new File(getClass().getClassLoader()
				.getResource(CORRESPONDENCE_DOC_PATH).getFile())).getAbsolutePath());
		correspondenceFormDto.setSource(Source.MANUAL);
		correspondenceFormDto.setSubSourceTypes(SubSourceTypes.SINGLE);
		correspondenceService.createApplicationRequest(correspondenceFormDto);

		SearchResult<CreateReqApplicationDTO>createAppReqList2 = dashboardService.getCreateAppRecords(filter, pageInfo);
		Assert.assertTrue("A new application creation request must have been raised.",
				createAppReqList2.getItems().size() == (count + 1));
	}


	/** Verifies create application manual request record list functionality */
	@Test
	@Rollback(false)
	public void testCreateAppRequest_2() {
		long notificationId = 0L;
		long entityId = 0L;
		String entityName = EntityName.CORRESPONDENCE_STAGING.name();
		Long recordsUpdated;

		//fetch create application request records
		ActiveRecordsFilter filter = new ActiveRecordsFilter();
		PaginationInfo pageInfo = new PaginationInfo(20L, 0L, SortAttribute.FILING_DATE.attr, false);
		SearchResult<CreateReqApplicationDTO>createAppReqList = dashboardService.getCreateAppRecords(filter, pageInfo);
		for (CreateReqApplicationDTO dto : createAppReqList.getItems()) {
			if (dto.getApplicationNo().equals(CorrespondenceStagingData.APP_NUMBER.value)) {
				notificationId = dto.getNotificationId();
				entityId = dto.getEntityId();
				break;
			}
		}

		recordsUpdated = applicationService.rejectCreateAppRequest(entityId, entityName, notificationId);
		Assert.assertNotNull(recordsUpdated);
		CorrespondenceStaging correspondenceStage = correspondenceStageDAO.find(entityId);
		NotificationProcess notification = notificationProcessDAO.find(notificationId);
		Assert.assertEquals(Status.CREATE_APPLICATION_REQUEST_REJECTED,correspondenceStage.getStatus());
		Assert.assertEquals(false , notification.isActive());
		Assert.assertEquals(NotificationStatus.DONE, notification.getStatus());
	}

	@Test
	public void testCreateAppRecordsFamilyList() {
		setTestUser();
		ActiveRecordsFilter filter = new ActiveRecordsFilter();
		PaginationInfo pageInfo = new PaginationInfo(20L, 0L, SortAttribute.FILING_DATE.attr, false);
		SearchResult<CreateReqFamilyDTO>createAppReqList = dashboardService.getCreateFamilyRecords(filter, pageInfo);
		Assert.assertTrue("There must be atleast 2 create family requests.",
				createAppReqList.getRecordsTotal().longValue() >= 2L);
	}
	
	@Test
	public void testSendUpdateAssigneeNotification() {
		long noOfRequests = applicationService.sendUpdateAssigneeNotification();
		Assert.assertTrue("Present Notifications should be greater than or equal to created notifications",notificationProcessDAO.findAll().size()>=noOfRequests);
	}

	@Test
	public void testUpdateAssigneeRecordsList() {
		setTestUser();
		ActiveRecordsFilter filter = new ActiveRecordsFilter();
		PaginationInfo pageInfo = new PaginationInfo(20L, 0L, SortAttribute.FILING_DATE.attr, false);
		SearchResult<UpdateReqAssigneeDTO>updateAssigneeReqList = dashboardService.getUpdateReqAssigneeRecords(filter, pageInfo);
		Assert.assertTrue("There must be atleast 4 notifications for update assignee.",
				updateAssigneeReqList.getRecordsTotal().longValue() >= 4);
	}

	@Test
	public void testFamilyUpdateRecordsList() {
		setTestUser();
		ActiveRecordsFilter filter = new ActiveRecordsFilter();
		PaginationInfo pageInfo = new PaginationInfo(20L, 0L, SortAttribute.FILING_DATE.attr, false);
		SearchResult<UpdateReqFamilyDTO> updateAssigneeReqList = dashboardService.getUpdateReqFamilyRecords(filter,
				pageInfo);
		Assert.assertTrue("There must be atleast 2 family update requests.",
				updateAssigneeReqList.getRecordsTotal().longValue() >= 2L);
	}

	@Test
	@Rollback(false)
	public void testDraftsRecordsList_1() {
		setTestUser();
		int draftsInserted = 4;
		List<DraftDTO> drafts = dashboardService.getCurrentUserDrafts();
		if (!drafts.isEmpty()) {
			Assert.assertTrue("Draft size has to be greater than or equal to 4.", draftsInserted >= drafts.size());
		}
	}

	@Test
	@Rollback(false)
	public void testDraftsRecordList_2() {
		setTestUser();
		List<DraftDTO> drafts = dashboardService.getCurrentUserDrafts();
		int count = drafts.size();
		dashboardService.deleteDraft(drafts.get(0).getDbId());
		List<DraftDTO> draftsList2 = dashboardService.getCurrentUserDrafts();
		Assert.assertEquals("Drafts count must be decrements post draft deletion.", count - 1, draftsList2.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindByApplicationNo() {
		applicationService.findByApplicationNo(ApplicationData.JURISDICTION.value, null);
		Assert.assertFalse("Control must not reach here.", true);
	}

	@Test
	public void testGetAssigneeFromDocketNo() {
		String attorneyDocketNumber = "111.222.333.333";
		String expectedAssignee = "Apple";
		Assignee assignee = applicationService.getAssigneeFromDocketNo(attorneyDocketNumber);
		Assert.assertTrue("This test case should return apple as assignee.", expectedAssignee.equals(assignee.getName()));
	}
	

}
