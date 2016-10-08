package com.blackbox.ids.services.reference;

import java.text.MessageFormat;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.StringUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dto.reference.JurisdictionDTO;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.dto.reference.ReferenceDashboardDto;
import com.blackbox.ids.core.dto.reference.ReferenceEntryDTO;
import com.blackbox.ids.core.dto.reference.ReferenceEntryFilter;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.reference.NullReferenceDocument;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.dto.reference.ReferencePredicate;
import com.blackbox.ids.it.AbstractIntegrationTest;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.mysema.query.types.Predicate;

public class ReferenceDashboardServiceTest extends AbstractIntegrationTest {

	private static final Logger log = Logger.getLogger(ReferenceDashboardServiceTest.class);

	private static int count = 0;

	@Autowired
	private ReferenceDashboardService referenceDashboardService;

	@Autowired
	private NotificationProcessService notificationProcessService;

	@Autowired
	private ReferenceManagementService referenceMgmtService;
	
	@Autowired
	private ReferenceService referenceService;

	@Autowired
	private JpaRepository<NullReferenceDocument, Long> nullRefRepo;

	@BeforeClass
	public static void beforeClass() {
		log.info("Junit Test cases start for Reference Dashboard Service");
	}

	@Before
	public void setUp() {
		count++;
		log.info(MessageFormat.format("Test case {0} start executing", count));
		setTestUser();
	}

	@Test
	public void testGetUpdaterRefData() {
		Assert.assertNotNull(referenceDashboardService);
		notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		Page<ReferenceDashboardDto> pages = referenceDashboardService.getUpdaterRefData(getUpdatePredicate(),
				getPageInfo("notifiedDate"));
		Assert.assertNotNull("Pages should not be null", pages);
	}

	@Test
	public void testGetUpdateRefDto() {
		Assert.assertNotNull(referenceDashboardService);
		Long id = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		ReferenceDashboardDto dto = referenceDashboardService.getUpdateRefDto(id);
		Assert.assertNotNull("Dto should not be null", dto);
	}

	@Test
	public void testGetDuplicateCheckRefData() {
		Assert.assertNotNull(referenceDashboardService);
		notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_BASE_DATA, NotificationProcessType.NPL_DUPLICATE_CHECK, null);
		Page<ReferenceDashboardDto> pages = referenceDashboardService
				.getDuplicateCheckRefData(getNplDuplicatePredicate(), getPageInfo("stringData"));
		Assert.assertNotNull("Pages should not be null", pages);
	}

	@Test
	public void testGetRefEntryData() {
		Assert.assertNotNull(referenceDashboardService);
		notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.OCR_DATA, NotificationProcessType.REFERENCE_MANUAL_ENTRY, null);
		Page<ReferenceEntryDTO> pages = referenceDashboardService.getRefEntryData(createReferenceEntryFilter(),
				getPageInfo("jurisdictionCode"));
		Assert.assertNotNull("Pages should not be null", pages);
	}

	@Test
	public void testFetchAndLockNotification() {
		Assert.assertNotNull(referenceDashboardService);
		Long id = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.OCR_DATA, NotificationProcessType.REFERENCE_MANUAL_ENTRY, null);
		ReferenceEntryDTO dto = referenceDashboardService.fetchAndLockNotification(id, 1l, 1l);
		Assert.assertNotNull("dto should not be null", dto);
	}

	@Test
	public void testFetchNotification() {
		Assert.assertNotNull(referenceDashboardService);
		Long id = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.OCR_DATA, NotificationProcessType.REFERENCE_MANUAL_ENTRY, null);
		ReferenceEntryDTO dto = referenceDashboardService.fetchNotification(id, 1l, 1l);
		Assert.assertNotNull("dto should not be null", dto);
	}

	@Test
	public void testGetCorrespondenceId() {
		Assert.assertNotNull(referenceDashboardService);
		Long id = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.OCR_DATA, NotificationProcessType.REFERENCE_MANUAL_ENTRY, null);
		Long corresId = referenceDashboardService.getCorrespondenceId(id, 1l);
		Assert.assertEquals("Correspondence Id should be 1", Long.valueOf(1l), corresId);
	}

	@Test
	public void testGetUpdateReferenceCounts() {
		Assert.assertNotNull(referenceDashboardService);
		notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		Long count = referenceDashboardService.getUpdateReferenceCounts();
		Assert.assertTrue("Counnt should be greater than 0", count > 0);
	}

	@Test
	public void testGetDuplicateNplReferenceCounts() {
		Assert.assertNotNull(referenceDashboardService);
		notificationProcessService.createNotification(null, null, null, 3l, 
				EntityName.REFERENCE_BASE_DATA, NotificationProcessType.NPL_DUPLICATE_CHECK, null);
		Long count = referenceDashboardService.getDuplicateNplReferenceCounts();
		Assert.assertTrue("Counnt should be greater than 0", count > 0);
	}

	@Test
	public void testGetReferenceEntryCount() {
		Assert.assertNotNull(referenceDashboardService);
		notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.OCR_DATA, NotificationProcessType.REFERENCE_MANUAL_ENTRY, null);
		Long count = referenceDashboardService.getReferenceEntryCount();
		Assert.assertTrue("Counnt should be greater than 0", count > 0);
	}

	@Test
	public void testGetDuplicateNPLReference() {
		Assert.assertNotNull(referenceDashboardService);
		ReferenceDashboardDto dto = referenceDashboardService.getDuplicateNPLReference(3l);
		Assert.assertNotNull("Dto should not be null", dto);
	}

	@Test
	public void testDeleteUpdateRefFromStaging() {
		Assert.assertNotNull(referenceDashboardService);
		Long id = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		boolean isDeleted = referenceDashboardService.deleteUpdateRefFromStaging(1l, id);
		Assert.assertEquals("Deleted should be true", isDeleted, true);
	}

	@Test
	public void testCreateNPLStringForNullReferenceDoc() {
		Assert.assertNotNull(referenceDashboardService);
		String nplString = referenceDashboardService.createNPLStringForNullReferenceDoc(1l);
		Assert.assertNotNull("Deleted should be true", nplString);
	}

	@Test
	public void testCreateNullReferenceDocumentAndCloseNotification() {
		Assert.assertNotNull(referenceDashboardService);
		String nplString = referenceDashboardService.createNPLStringForNullReferenceDoc(1l);
		Long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.OCR_DATA, NotificationProcessType.REFERENCE_MANUAL_ENTRY, null);
		referenceDashboardService.createNullReferenceDocumentAndCloseNotification(1l, nplString, 1l,
				notificationProcessId);
		Assert.assertTrue("One entry should be present in null reference document", nullRefRepo.findAll().size() > 0);
	}

	@Test
	public void testAddReferenceDetails() {
		Assert.assertNotNull(referenceDashboardService);
		Long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		ReferenceBaseDTO referenceDto = referenceMgmtService.getReferenceWithApplicationDetails(1l);
		referenceDto = getReferenceDto(referenceDto);
		referenceDto.setNotificationProcessId(notificationProcessId);
		referenceDto.setRefStagingId(1l);
		referenceDashboardService.addReferenceDetails(referenceDto);
		referenceService.createReference(referenceDto);
		
		Assert.assertTrue("One entry should be present in null reference document", nullRefRepo.findAll().size() > 0);
	}

	private ReferenceBaseDTO getReferenceDto(ReferenceBaseDTO dto) {

		JurisdictionDTO jDto = new JurisdictionDTO();
		jDto.setName("US");
		dto.setApplicantName("Test");
		dto.setApplicationFilingDate("20160102");
		dto.setApplicationJurisdictionCode("US");
		dto.setApplicationNumber("14933258");
		dto.setJurisdiction(jDto);
		dto.setKindCode("A1");
		dto.setManualAdd(true);
		dto.setPublicationDateStr("Jan 01, 2016");
		dto.setPublicationNumber("2019/1234567");
		dto.setReferenceComments("No Comments");
		dto.setReferencetype("PUS");
		dto.setReferenceType(ReferenceType.PUS);
		return dto;
	}

	private ReferenceEntryFilter createReferenceEntryFilter() {
		ReferenceEntryFilter filter = new ReferenceEntryFilter();
		Set<Long> roles = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();

		filter.setMyRecords("false");
		filter.setJurisdiction("US");
		filter.setDateRange("May 01, 2000 - May 31, 2100");
		filter.setRoles(roles);

		return filter;
	}

	private Predicate getUpdatePredicate() {
		String myRecords = "false";
		String juris = "US";
		String dateRange = "May 01, 2000 - May 31, 2100";
		return ReferencePredicate.getUpdateReferencePredicate(juris, myRecords, dateRange);
	}

	private Predicate getNplDuplicatePredicate() {
		String myRecords = "false";
		String juris = "US";
		String dateRange = "May 01, 2000 - May 31, 2100";
		return ReferencePredicate.getDuplicateRefCheckPredicate(juris, myRecords, dateRange);
	}

	private Pageable getPageInfo(String sortBy) {
		String dispStart = "10";
		String dispLength = "0";

		final Integer limit = StringUtil.isBlank(dispStart) ? null : Integer.parseInt(dispStart);
		final boolean isAsc = true;
		Integer offset = StringUtil.isBlank(dispLength) ? null : Integer.parseInt(dispLength);

		if (offset != 0) {
			offset = offset / limit;
		}

		Sort sort = isAsc ? new Sort(new Order(Direction.ASC, sortBy)) : new Sort(new Order(Direction.DESC, sortBy));
		PageRequest page = new PageRequest(offset, limit, sort);

		return page;
	}

	@After
	public void afterTest() {
		log.info(MessageFormat.format("Test case {0} stop executing", count));
	}

	@AfterClass
	public static void afterClass() {
		log.info("Junit Test cases end for Reference Dashboard Service");
	}
}
