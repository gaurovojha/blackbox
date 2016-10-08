package com.blackbox.ids.services.reference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.blackbox.ids.core.dto.reference.JurisdictionDTO;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.dto.reference.ReferenceDashboardDto;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceStagingData;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.it.AbstractIntegrationTest;
import com.blackbox.ids.services.notification.process.NotificationProcessService;

public class ReferenceServiceTest extends AbstractIntegrationTest {
	
	private static final Logger log = Logger.getLogger(ReferenceServiceTest.class);
	
	@Autowired
	private JpaRepository<ReferenceStagingData, Long> refStagingData;

	private static int count = 0;
	
	@Autowired
	private ReferenceService referenceService;
	
	@Autowired
	private NotificationProcessService notificationProcessService;
	
	@Autowired
	private ReferenceManagementService referenceMgmtService;
	
	@Autowired
	private JpaRepository<ReferenceBaseData, Long> referenceBaseData;
	
	/** The reference dashboard service. */
	@Autowired
	private ReferenceDashboardService referenceDashboardService;
	
	@BeforeClass
	public static void beforeClass() {
		log.info("Junit Test cases start for Reference Service");
	}

	@Before
	public void setUp() {
		count++;
		log.info(MessageFormat.format("Test case {0} start executing", count));
		setTestUser();
	}
	
	@Test
	public void testCreateReference() {
		Assert.assertNotNull(referenceService);
		Long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		ReferenceBaseDTO referenceDto = referenceMgmtService.getReferenceWithApplicationDetails(1l);
		referenceDto = getReferenceDto(referenceDto);
		referenceDto.setNotificationProcessId(notificationProcessId);
		referenceDto.setRefStagingId(1l);
		referenceDto.setCorrespondenceNumber(1l);
		referenceDto.setCorrespondenceId(null);
		long countBefore = referenceBaseData.count();
		referenceService.createReference(referenceDto);
		long countAfter = referenceBaseData.count();
		Assert.assertTrue("Count after should be greater than count before", Long.compare(countAfter, countBefore) > 0);
	}
	
	@Test
	public void testCreateDuplicateReferences() {
		Assert.assertNotNull(referenceService);
		Long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		ReferenceBaseDTO referenceDto = referenceMgmtService.getReferenceWithApplicationDetails(1l);
		referenceDto = getReferenceDto(referenceDto);
		referenceDto.setNotificationProcessId(notificationProcessId);
		referenceDto.setRefStagingId(1l);
		referenceDto.setCorrespondenceNumber(1l);
		referenceDto.setCorrespondenceId(null);
		long countBefore = referenceBaseData.count();
		referenceService.createReference(referenceDto);
		referenceService.createReference(referenceDto);
		long countAfter = referenceBaseData.count();
		Assert.assertTrue("Count after should be greater than count before", Long.compare(countAfter, countBefore) > 0);
	}
	
	@Test
	public void testCreateNPLPendingReference() {
		Assert.assertNotNull(referenceService);
		Long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		ReferenceBaseDTO referenceDto = referenceService.getReferenceById(3l);
		referenceDto = getReferenceDto(referenceDto);
		referenceDto.setNotificationProcessId(notificationProcessId);
		referenceDto.setRefStagingId(1l);
		referenceDto.setCorrespondenceNumber(1l);
		referenceDto.setCorrespondenceId(null);
		referenceDto.setReferenceType(ReferenceType.NPL);
		referenceDto.setCreateFYANotification(true);
		long countBefore = referenceBaseData.count();
		referenceService.createReference(referenceDto);
		long countAfter = referenceBaseData.count();
		Assert.assertTrue("Count after should be greater than count before", Long.compare(countAfter, countBefore) > 0);
	}
	
	@Test
	public void testAddReference() {
		Assert.assertNotNull(referenceService);
		Long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		ReferenceBaseDTO referenceDto = referenceMgmtService.getReferenceWithApplicationDetails(1l);
		referenceDto = getReferenceDto(referenceDto);
		referenceDto.setNotificationProcessId(notificationProcessId);
		referenceDto.setRefStagingId(1l);
		referenceDto.setCorrespondenceNumber(1l);
		referenceDto.setCorrespondenceId(null);
		referenceDto.setReferenceType(ReferenceType.NPL);
		referenceDto.setCreateFYANotification(true);
		long countBefore = referenceBaseData.count();
		referenceService.addReference(referenceDto);
		long countAfter = referenceBaseData.count();
		Assert.assertTrue("Count after should be greater than count before", Long.compare(countAfter, countBefore) > 0);
	}
	
	@Test
	public void testAddDuplicateReference() {
		Assert.assertNotNull(referenceService);
		Long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		ReferenceBaseDTO referenceDto = referenceMgmtService.getReferenceWithApplicationDetails(1l);
		referenceDto = getReferenceDto(referenceDto);
		referenceDto.setNotificationProcessId(notificationProcessId);
		referenceDto.setRefStagingId(1l);
		referenceDto.setCorrespondenceNumber(1l);
		referenceDto.setCorrespondenceId(null);
		referenceDto.setReferenceType(ReferenceType.NPL);
		referenceDto.setCreateFYANotification(true);
		long countBefore = referenceBaseData.count();
		referenceService.addReference(referenceDto);
		referenceService.addReference(referenceDto);
		long countAfter = referenceBaseData.count();
		Assert.assertTrue("Count after should be greater than count before", Long.compare(countAfter, countBefore) > 0);
	}
	
	@Test
	public void testAddReferenceStagingData() {
		Assert.assertNotNull(referenceService);
		ReferenceBaseDTO referenceDto = getReferenceDto(new ReferenceBaseDTO());
		referenceDto.setRefStagingId(1l);
		referenceDto.setCorrespondenceNumber(1l);
		referenceDto.setCorrespondenceId(null);
		referenceDto.setReferenceType(ReferenceType.NPL);
		referenceDto.setCreateFYANotification(true);
		referenceService.addReferenceStagingData(referenceDto);
	}
	
	@Test
	public void testDeleteDocument() {
		Assert.assertNotNull(referenceDashboardService);
		Long notificationId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.OCR_DATA, NotificationProcessType.REFERENCE_MANUAL_ENTRY, null);
		referenceService.deleteDocument(notificationId);
	}

	@Test
	public void testUpdateNPLReferenceManually() {
		Assert.assertNotNull(referenceDashboardService);
		Long id = notificationProcessService.createNotification(null, null, null, 3l, 
				EntityName.REFERENCE_BASE_DATA, NotificationProcessType.NPL_DUPLICATE_CHECK, null);
		referenceService.updateNPLReferenceManually(3l, true, id);
	}
	
	@Test
	public void testUpdateNPLReferenceManuallyNew() {
		Assert.assertNotNull(referenceDashboardService);
		Long id = notificationProcessService.createNotification(null, null, null, 3l, 
				EntityName.REFERENCE_BASE_DATA, NotificationProcessType.NPL_DUPLICATE_CHECK, null);
		referenceService.updateNPLReferenceManually(3l, false, id);
	}
	
	@Test
	public void testGetReferenceById() {
		Assert.assertNotNull(referenceDashboardService);
		referenceService.getReferenceById(1l);
	}
	
	@Test
	public void testUpdateReferenceFamilyLinkage() {
		Assert.assertNotNull(referenceService);
		referenceService.updateReferenceFamilyLinkage(1l, "F000008", "F000009");
	}
	
	@Test
	public void testUploadPDFAttachment() throws IOException {
		Assert.assertNotNull(referenceService);
		ReferenceBaseDTO referenceDto = getReferenceDto(new ReferenceBaseDTO());
		referenceDto.setReferenceBaseDataId(1l);
		File temp = File.createTempFile("temp-file-name", ".tmp"); 
		
		Path path = Paths.get(temp.getPath());
		String name = "temp-file-name";
		String originalFileName = "temp-file-name";
		String contentType = "text/plain";
		byte[] content = null;
		
		try {
		    content = Files.readAllBytes(path);
		} catch (final IOException e) {
		}
		
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);
		
		referenceDto.setFile(result);
		referenceService.uploadPDFAttachment(referenceDto);
	}
	
	@Test
	public void testRemoveReference() {
		Assert.assertNotNull(referenceService);
		referenceService.removeReference(1l);
	}
	
	@Test
	public void testImportReference() {
		Assert.assertNotNull(referenceService);
		referenceService.importReference(refStagingData.findOne(1l));
		referenceService.importReference(refStagingData.findOne(1l));
	}
	
	@Test
	public void testRemoveReferences() {
		Assert.assertNotNull(referenceService);
		List<Long> refIds = new ArrayList<Long>();
		refIds.add(1l);
		refIds.add(2l);
		referenceService.removeReferences(refIds);
	}
	
	@Test
	public void testGetNPLDuplicates() {
		Assert.assertNotNull(referenceService);
		ReferenceDashboardDto dto = referenceDashboardService.getDuplicateNPLReference(3l);
		dto.setReferenceBaseId(3l);
		List<ReferenceDashboardDto> dtos = referenceService.getNPLDuplicates(dto);
		Assert.assertNotNull(dtos);
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
	
	@After
	public void afterTest() {
		log.info(MessageFormat.format("Test case {0} stop executing", count));
	}

	@AfterClass
	public static void afterClass() {
		log.info("Junit Test cases end for Reference Service");
	}
}
