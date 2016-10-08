package com.blackbox.ids.services.correspondence;


import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordDTO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordsFilter;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.dashboard.SortAttribute;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.it.AbstractIntegrationTest;
import com.blackbox.ids.services.notification.process.NotificationProcessService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CorrespondenceServiceTest extends AbstractIntegrationTest{
	
	@Autowired
	private ICorrespondenceService correspondenceService;
	
	@Autowired
	private ApplicationBaseDAO applicationBaseDAO;
	
	@Autowired
	private NotificationProcessService notificationProcessService;
	
	/** The logger. */
	private Logger logger	= Logger.getLogger(CorrespondenceServiceTest.class);
	
	@Before
	public void setUp() {
		setTestUser();
	}

	/************************************************************/
	/**** CorrespondenceService - Dashboard TestCases Start *****/
	/************************************************************/
	
	/** Tests the Active Documents Filter : Null Date Range Filter - Positive Scenario */
	@Test
	public void testCorrespondenceDashboard_01() {
		logger.info("Test ActiveDocuments : My documents(userID: 1) with no date range filter");		
		CorrespondenceRecordsFilter filter = new  CorrespondenceRecordsFilter(2L,null);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		
		SearchResult<CorrespondenceRecordDTO> items = correspondenceService.filterActiveDocuments(filter, paginationInfo);
		Assert.assertNotNull(items);
		Assert.assertEquals(1, items.getItems().size());
	}

	/** Tests the Active Documents Filter : Date Range Filter - Positive Scenario */
	@Test
	public void testCorrespondenceDashboard_02() {
		logger.info("Test ActiveDocuments : My documents(userID: 1) with  date range ('Jul 16, 2015 - Jan 12, 2016') filter");
		final String dateRange  = "Jul 16, 2015 - Jan 12, 2016";
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(2L,dateRange);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		paginationInfo.setSortBy(null);
		
		SearchResult<CorrespondenceRecordDTO> items = correspondenceService.filterActiveDocuments(filter, paginationInfo);
		Assert.assertNotNull(items);
		Assert.assertEquals(new Long(0), items.getRecordsFiltered());
	}
	
	/** Tests the Active Documents Filter : My documents Filter - Positive Scenario */
	@Test
	public void testCorrespondenceDashboard_03() {
		logger.info("Test ActiveDocuments : My documents(userID: 1) filter");
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(2L, null);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		
		SearchResult<CorrespondenceRecordDTO> items = correspondenceService.filterActiveDocuments(filter, paginationInfo);
		Assert.assertNotNull(items);
		Assert.assertEquals(new Long(1), items.getRecordsFiltered());
	}
	
	/** Tests the Active Documents Filter : All documents Filter - Positive Scenario */
	@Test
	public void testCorrespondenceDashboard_04() {
		logger.info("Test ActiveDocuments :All documents");
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(null, null);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		
		SearchResult<CorrespondenceRecordDTO> items = correspondenceService.filterActiveDocuments(filter, paginationInfo);
		Assert.assertNotNull(items.getItems());
		Assert.assertEquals(new Long(2), items.getRecordsFiltered());
	}
	
	/** Tests the Inactive Documents Filter : Null Date Range Filter - Positive Scenario*/
	@Test
	public void testCorrespondenceDashboard_05() {
		logger.info("Test InactiveDocuments :No date range filter");
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(null, null);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0L);
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		SearchResult<CorrespondenceRecordDTO> items = correspondenceService.filterInactiveDocuments(filter, paginationInfo);
		Assert.assertNotNull(items);
		Assert.assertEquals(new Long(4), items.getRecordsFiltered());
	}

	/** Tests the Inactive Documents Filter : Date Range Filter - Positive Scenario*/
	@Test
	public void testCorrespondenceDashboard_06() {
		logger.info("Test InactiveDocuments : Date range ('Jul 16, 2015 - Jan 12, 2016') filter");
		final String dateRange  = "Jul 16, 2015 - Jan 12, 2016";
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(null, dateRange);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		paginationInfo.setSortBy(null);
		
		SearchResult<CorrespondenceRecordDTO> items = correspondenceService.filterInactiveDocuments(filter, paginationInfo);
		Assert.assertNotNull(items);
		Assert.assertEquals(new Long(3), items.getRecordsFiltered());
	}
	
	/** Tests the Active Documents Collapse feature : My documents with No Date Range Filter - Positive Scenario*/
	@Test
	public void testCorrespondenceDashboard_07() {
		logger.info("Tests the Active Documents Collapse feature : My documents(userId : 2) with No Date Range Filter");
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(2L, null);
		long correspondenceId = 2L;
		SearchResult<CorrespondenceRecordDTO> items = correspondenceService.getActiveCorrespondences(filter,correspondenceId);
		Assert.assertNotNull(items);
		Assert.assertEquals(new Long(4), items.getRecordsFiltered());
	}
	
	/** Tests the Active Documents Collapse feature : All documents with No Date Range Filter - Positive Scenario*/
	@Test
	public void testCorrespondenceDashboard_08() {
		logger.info("Tests the Active Documents Collapse feature : All with No Date Range Filter");
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(null, null);
		long correspondenceId = 2L;
		SearchResult<CorrespondenceRecordDTO> items = correspondenceService.getActiveCorrespondences(filter,correspondenceId);
		Assert.assertNotNull(items);
		Assert.assertEquals(new Long(5), items.getRecordsFiltered());
	}
	
	/** Tests the Active Documents Collapse feature : My documents with Date Range Filter('Jul 16, 2015 - Jan 12, 2016') - Positive Scenario*/
	@Test
	public void testCorrespondenceDashboard_09() {
		logger.info("Tests the Active Documents Collapse feature : My documents(userId: 2) with Date Range Filter('Jul 16, 2015 - Jan 12, 2016')");
		final String dateRange  = "Jul 16, 2015 - Jan 12, 2016";
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(2L, dateRange);
		long correspondenceId = 2L;
		SearchResult<CorrespondenceRecordDTO> items = correspondenceService.getActiveCorrespondences(filter,correspondenceId);
		Assert.assertNotNull(items);
		Assert.assertEquals(new Long(2), items.getRecordsFiltered());
	}
	
	/** Tests the Active Documents Collapse feature : All documents with Date Range Filter('Jul 16, 2015 - Jan 12, 2016') - Positive Scenario*/
	@Test
	public void testCorrespondenceDashboard_10() {
		logger.info("Tests the Active Documents Collapse feature : All documents with Date Range Filter('Jul 16, 2015 - Jan 12, 2016')");
		final String dateRange  = "Jul 16, 2015 - Jan 12, 2016";
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(null, dateRange);
		long correspondenceId = 2L;
		SearchResult<CorrespondenceRecordDTO> items = correspondenceService.getActiveCorrespondences(filter,correspondenceId);
		Assert.assertNotNull(items);
		Assert.assertEquals(new Long(3), items.getRecordsFiltered());
	}
	
	/** Tests the Inactive Documents Collapse feature : All documents with No Date Range Filter - Positive Scenario*/
	@Test
	public void testCorrespondenceDashboard_11() {
		logger.info("Tests the Inactive Documents Collapse feature : All documents with No Date Range Filter");
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(null, null);
		long correspondenceId = 7L;
		SearchResult<CorrespondenceRecordDTO> items = correspondenceService.getInactiveCorrespondences(filter,correspondenceId);
		Assert.assertNotNull(items);
		Assert.assertEquals(new Long(5), items.getRecordsFiltered());
	}
	
	/** Tests the Inactive Documents Collapse feature : All documents with Date Range Filter('Jul 16, 2015 - Jan 12, 2016') - Positive Scenario*/
	@Test
	public void testCorrespondenceDashboard_12() {
		logger.info("Tests the Inactive Documents Collapse feature : All documents with Date Range Filter('Jul 16, 2015 - Jan 12, 2016')");
		final String dateRange  = "Jul 16, 2015 - Jan 12, 2016";
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(null, dateRange);
		long correspondenceId = 7L;
		SearchResult<CorrespondenceRecordDTO> items = correspondenceService.getInactiveCorrespondences(filter,correspondenceId);
		Assert.assertNotNull(items);
		Assert.assertEquals(new Long(3), items.getRecordsFiltered());
	}
	
	
	/** Tests the Active Documents -Actions(View Document) :Positive Scenario*/
	@Test
	public void testCorrespondenceDashboard_13() {
		logger.info("Test the Active Documents - Actions Tab : View Document feature: Correspondence found");
		long correspondenceId = 2L;		
		CorrespondenceRecordDTO correspondenceRecordDTO = correspondenceService.getCorrespondenceRecord(correspondenceId);
		Assert.assertEquals(new Long(6), correspondenceRecordDTO.getAppdbId()); 
	}
	
	/** Tests the Active Documents -Actions(View Document) :Negative Scenario*/
	@Test
	public void testCorrespondenceDashboard_14() {
		logger.info("Test the Active Documents - Actions Tab : View Document feature: Correspondence not found");
		long correspondenceId = 20L;		
		CorrespondenceRecordDTO correspondenceRecordDTO = correspondenceService.getCorrespondenceRecord(correspondenceId);
		Assert.assertNull(correspondenceRecordDTO);
	}
	
	/** Tests the Active Documents -Actions(Delete Document) :Positive Scenario*/
	@Test
	@Transactional
	@Rollback(false)
	public void testCorrespondenceDashboard_15() {
		logger.info("Test the Active Documents - Actions Tab : View Document feature: Correspondence deleted");
		long correspondenceId = 2L;		
		long rowUpdate = correspondenceService.deleteCorrespondenceRecord(correspondenceId,"Deleteing using test");
		Assert.assertEquals(1L, rowUpdate);
	}
	
	/** Tests the Active Documents -Actions(Delete Document) :Negative Scenario*/
	@Test
	@Transactional
	@Rollback(false)
	public void testCorrespondenceDashboard_16() {
		logger.info("Test the Active Documents - Actions Tab : View Document feature: Correspondence not deleted");
		long correspondenceId = 20L;		
		long rowUpdate = correspondenceService.deleteCorrespondenceRecord(correspondenceId,"Deleteing using test");
		Assert.assertEquals(0, rowUpdate);
	}
	
	/** Tests the Active Documents : Active Documents with Null Pagination information- Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testCorrespondenceDashboard_17() {
		logger.info("IllegalArgumentException should be thrown while fetching active documents when pagination info is null.");
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(1L, null);
		PaginationInfo paginationInfo = null;
		correspondenceService.filterActiveDocuments(filter, paginationInfo);
	}
	
	/** Tests the Active Documents : Active Documents with Null CorrespondenceRecordsFilter information- Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testCorrespondenceDashboard_18() {
		logger.info("IllegalArgumentException should be thrown while fetching active documents when filter is null.");
		CorrespondenceRecordsFilter filter = null;
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		correspondenceService.filterActiveDocuments(filter, paginationInfo);
	}
	
	/** Tests the Inactive Documents : Inactive Documents with Null Pagination information- Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testCorrespondenceDashboard_19() {
		logger.info("IllegalArgumentException should be thrown while fetching inactive documents when pagination info is null.");
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(1L, null);
		PaginationInfo paginationInfo = null;
		correspondenceService.filterInactiveDocuments(filter, paginationInfo);
	}
	
	/** Tests the Inactive Documents : Inactive Documents with Null CorrespondenceRecordsFilter information- Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testCorrespondenceDashboard_20() {
		logger.info("IllegalArgumentException should be thrown while fetching inactive documents when filter is null.");
		CorrespondenceRecordsFilter filter = null;
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		correspondenceService.filterInactiveDocuments(filter, paginationInfo);
	}
	
	/** Tests the Delete correspondence : Correspondence Id is null- Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testCorrespondenceDashboard_21() {
		logger.info("IllegalArgumentException should be thrown while deleteing correspondence when id is null.");
		correspondenceService.deleteCorrespondenceRecord(null, null);
	}
	
	/** Tests the View correspondence : Correspondence Id is null- Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testCorrespondenceDashboard_22() {
		logger.info("IllegalArgumentException should be thrown while fetching correspondence when id is null.");
		correspondenceService.getCorrespondenceRecord(null);
	}
	
	/** Tests the Active Documents Collapse feature : Correspondence Id is null- Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testCorrespondenceDashboard_23() {
		logger.info("IllegalArgumentException should be thrown while fetching active correspondences when id is null.");
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(2L, null);
		correspondenceService.getActiveCorrespondences(filter, null);
	}
	
	/** Tests the Inactive Documents Collapse feature : Correspondence Id is null- Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testCorrespondenceDashboard_24() {
		logger.info("IllegalArgumentException should be thrown while fetching inactive correspondences when id is null.");
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(2L, null);
		correspondenceService.getInactiveCorrespondences(filter, null);
	}
	
	/************************************************************/
	/**** CorrespondenceService - Dashboard TestCases End *****/
	/************************************************************/
	
	/*********************** Action Item TestCases Starts ***************************/
	
	/** Tests the Update Request Feature : Default Date Range Filter - Positive Scenario*/
	@Test
	public void testUpdateRequestRecords() {
		Calendar cal = Calendar.getInstance();
		String todayDate = BlackboxDateUtil.dateToStr(cal.getTime(),TimestampFormat.MMMDDYYYY);
		cal.add(Calendar.MONTH, -6);
		String previousDate = BlackboxDateUtil.dateToStr(cal.getTime(),TimestampFormat.MMMDDYYYY);
		String dateRange = BlackboxUtils.concat(previousDate, " - ",todayDate);
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter( 1L, dateRange);
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(10L);
		pageInfo.setOffset(0L);
		pageInfo.setAsc(false);
		pageInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		try {
			SearchResult<CorrespondenceRecordDTO> searchResult = correspondenceService.fetchUpdateRequestRecords(filter,
					pageInfo);
			Assert.assertTrue(!searchResult.getItems().isEmpty());
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
	
	/** Tests the Update Request Feature : Specific Date Range Filter - Positive Scenario*/
	@Test
	public void testUpdateRequestRecords_1() {
		String dateRange = "Jul 16, 2015 - Jan 12, 2016";
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter( 1L, dateRange);
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(10L);
		pageInfo.setOffset(0L);
		pageInfo.setAsc(false);
		pageInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		try {
			SearchResult<CorrespondenceRecordDTO> searchResult = correspondenceService.fetchUpdateRequestRecords(filter,
					pageInfo);
			Assert.assertTrue(!searchResult.getItems().isEmpty());
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
	
	/** Tests the Update Request Feature : Null Date Range Filter - Positive Scenario*/
	@Test
	public void testUpdateRequestRecords_2() {
		String dateRange = null;
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter( 1L, dateRange);
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(10L);
		pageInfo.setOffset(0L);
		pageInfo.setAsc(false);
		pageInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		try {
			SearchResult<CorrespondenceRecordDTO> searchResult = correspondenceService.fetchUpdateRequestRecords(filter,
					pageInfo);
			Assert.assertTrue(!searchResult.getItems().isEmpty());
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
	
	/** Tests the Update Request Feature : Null Correspondence Record Filter - Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateRequestRecords_3() {
		CorrespondenceRecordsFilter filter = null;
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(10L);
		pageInfo.setOffset(0L);
		pageInfo.setAsc(false);
		pageInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
			SearchResult<CorrespondenceRecordDTO> searchResult = correspondenceService.fetchUpdateRequestRecords(filter,
					pageInfo);
			Assert.assertTrue(!searchResult.getItems().isEmpty());
	}
	
	/** Tests the Update Request Feature : Null Pagination Info - Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateRequestRecords_4() {
		String dateRange = "Jul 16, 2015 - Jan 12, 2016";
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter( 1L, dateRange);
		PaginationInfo pageInfo = null;
			SearchResult<CorrespondenceRecordDTO> searchResult = correspondenceService.fetchUpdateRequestRecords(filter,
					pageInfo);
			Assert.assertTrue(!searchResult.getItems().isEmpty());
	}

	/** Tests the Upload Request Feature : Default Date Range Filter - Positive Scenario*/
	@Test
	public void testUploadRequestRecords() {
		Calendar cal = Calendar.getInstance();
		String todayDate = BlackboxDateUtil.dateToStr(cal.getTime(),TimestampFormat.MMMDDYYYY);
		cal.add(Calendar.MONTH, -6);
		String previousDate = BlackboxDateUtil.dateToStr(cal.getTime(),TimestampFormat.MMMDDYYYY);
		String dateRange = BlackboxUtils.concat(previousDate, " - ",todayDate);
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter( 1L, dateRange);
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(10L);
		pageInfo.setOffset(0L);
		pageInfo.setAsc(false);
		pageInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		try {
			SearchResult<CorrespondenceRecordDTO> searchResult = correspondenceService.fetchUploadRequestRecords(filter,
					pageInfo);
			Assert.assertTrue(!searchResult.getItems().isEmpty());
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
	
	/** Tests the Upload Request Feature : Specific Date Range Filter - Positive Scenario*/
	@Test
	public void testUploadRequestRecords_1() {
		String dateRange = "Jul 16, 2015 - Jan 12, 2016";
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter( 1L, dateRange);
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(10L);
		pageInfo.setOffset(0L);
		pageInfo.setAsc(false);
		pageInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		try {
			SearchResult<CorrespondenceRecordDTO> searchResult = correspondenceService.fetchUploadRequestRecords(filter,
					pageInfo);
			Assert.assertTrue(!searchResult.getItems().isEmpty());
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
	
	/** Tests the Upload Request Feature : Null Date Range Filter - Positive Scenario*/
	@Test
	public void testUploadRequestRecords_2() {
		String dateRange = null;
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter( 1L, dateRange);
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(10L);
		pageInfo.setOffset(0L);
		pageInfo.setAsc(false);
		pageInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		try {
			SearchResult<CorrespondenceRecordDTO> searchResult = correspondenceService.fetchUploadRequestRecords(filter,
					pageInfo);
			Assert.assertTrue(!searchResult.getItems().isEmpty());
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
	
	/** Tests the Upload Request Feature : Null Correspondence Record Filter - Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testUploadRequestRecords_3() {
		CorrespondenceRecordsFilter filter = null;
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(10L);
		pageInfo.setOffset(0L);
		pageInfo.setAsc(false);
		pageInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
			SearchResult<CorrespondenceRecordDTO> searchResult = correspondenceService.fetchUploadRequestRecords(filter,
					pageInfo);
			Assert.assertTrue(!searchResult.getItems().isEmpty());
	}
	
	/** Tests the Upload Request Feature : Null Pagination Info - Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testUploadRequestRecords_4() {
		String dateRange = "Jul 16, 2015 - Jan 12, 2016";
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter( 1L, dateRange);
		PaginationInfo pageInfo = null;
			SearchResult<CorrespondenceRecordDTO> searchResult = correspondenceService.fetchUploadRequestRecords(filter,
					pageInfo);
			Assert.assertTrue(!searchResult.getItems().isEmpty());
	}
	
	/** Tests the Update Request --> Delete correspondence Feature : Positive Scenario*/
	@Test
	public void testDeleteCorrespondenceRecord() {
		Long corrId = 13L;
		try {
			long deleteCount = correspondenceService.deleteCorrespondenceRecord(corrId, null);
			Assert.assertEquals(1L, deleteCount);
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
		
	}
	
	/** Tests the  Update Request --> Delete correspondence Feature : Correspondence Id is null- Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteCorrespondenceRecord_1() {
		logger.info("IllegalArgumentException should be thrown while deleteing correspondence when id is null.");
		Long corrId = null;
		long deleteCount = correspondenceService.deleteCorrespondenceRecord(corrId, null);
		Assert.assertEquals(1L, deleteCount);
	}
	
	/** Tests the Update Request --> Review Document : Positive Scenario*/
	@Test
	public void testReviewDocument() {
		Long correspondenceId = 14L;
		try {
			CorrespondenceRecordDTO correspondenceRecordDTO = correspondenceService
					.getCorrespondenceRecordDTO(correspondenceId);
			Assert.assertNotNull(correspondenceRecordDTO);
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
	
	/** Tests the Update Request --> Review Document : Correspondence Id is null- Negative Scenario*/
	@Test(expected = IllegalArgumentException.class)
	public void testReviewDocument_1() {
		Long correspondenceId = null;
			CorrespondenceRecordDTO correspondenceRecordDTO = correspondenceService
					.getCorrespondenceRecordDTO(correspondenceId);
			Assert.assertTrue("Review Document is null", correspondenceRecordDTO == null);
	}
	
	/** Tests the Upload Request --> Reject Document : Positive Scenario*/
	@Test
	public void testRejectDocument() {
		Long actionQueueId = 1L;
		String jurisdictionCode = "US";
		String appNo = "12345678";
		try {
			ApplicationBase applicationBase = applicationBaseDAO.findByApplicationNoAndJurisdictionCode(
					jurisdictionCode, appNo);
			Assert.assertNotNull(applicationBase);

			Long notificationProcessId = notificationProcessService.createNotification(applicationBase, null, null, 
					actionQueueId, EntityName.DOWNLOAD_OFFICE_ACTION_QUEUE, 
					NotificationProcessType.CORRESPONDENCE_RECORD_FAILED_IN_DOWNLOAD_QUEUE, null);
				correspondenceService.rejectDownloadOfficeNotification(notificationProcessId);
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
	
	/** Tests the Action Item Tab : Action Item count - Positive Scenario*/
	@Test
	public void testActionItemCount() {
		try {
			long count = correspondenceService.fetchActionItemCount();
			Assert.assertTrue(count>1);
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
	
	/** Tests the Update Request feature : Update Request Record count - Positive Scenario*/
	@Test
	public void testUpdateRequestCount() {
		try {
			long count = correspondenceService.fetchUpdateRequestCount();
			Assert.assertTrue(count>1);
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
	
	/** Tests the Upload Request feature : Upload Request Record count - Positive Scenario*/
	@Test
	public void testUploadRequestCount() {
		try {
			long count = correspondenceService.fetchUploadRequestCount();
			Assert.assertTrue(count>1);
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
	/*********************** Action Item TestCases End ***************************/
	
	/*********************** Track Application TestCases Starts ***************************/
	
	/** Track Application Test Cases **/
	
	
	/**
	 * This is the test method for FetchAllTrackApplications. This will assert
	 * true if the records in the DB are same as the expected.
	 * 
	 * @author maheshthakur
	 */
	@Test
	public void testFetchAllTrackApplications() {
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(2L, null);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		SearchResult<CorrespondenceRecordDTO> fetchItems = correspondenceService.fetchAllTrackApplications(filter, paginationInfo);
		
		Assert.assertNotNull(fetchItems);
		Assert.assertEquals(2, fetchItems.getItems().size());
	}
	
	/**
	 * This method will verify the method fetchAllTrackApplications.This will
	 * bring the search result from the db, available within the date range. The
	 * data available with the date range is 0 so the result will be 0 i.e no
	 * records available for that range .
	 * 
	 * @author maheshthakur
	 */
	@Test
	public void testFetchAllTrackApplications_WhenStartDateEndDateNotNull_AndNoValueInRange() {
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(2L, null);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		Calendar date = new GregorianCalendar(2006, Calendar.JULY, 3);
	    
		filter.setStartDate(date.getTime());
		date.add(Calendar.DAY_OF_MONTH, 7);
		filter.setEndDate(date.getTime());
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		SearchResult<CorrespondenceRecordDTO> fetchItems = correspondenceService.fetchAllTrackApplications(filter, paginationInfo);
		
		Assert.assertNotNull(fetchItems);
		Assert.assertEquals(0, fetchItems.getItems().size());
	}
	
	/**
	 * This method will verify the method fetchAllTrackApplications.This will
	 * bring the search result from the db, available within the date range. The
	 * data available with the date range is 1 so the result will be 1.
	 * 
	 * @author maheshthakur
	 */
	@Test
	public void testFetchAllTrackApplications_WhenStartDateEndDateNotNull_AndValueInRange() {
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(2L, null);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		Calendar date = new GregorianCalendar(2016, Calendar.JANUARY, 3);
	    
		filter.setStartDate(date.getTime());
		date.add(Calendar.DAY_OF_MONTH, 7);
		filter.setEndDate(date.getTime());
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		SearchResult<CorrespondenceRecordDTO> fetchItems = correspondenceService.fetchAllTrackApplications(filter, paginationInfo);
		
		Assert.assertNotNull(fetchItems);
		Assert.assertEquals(1, fetchItems.getItems().size());
	}

	/**
	 * This is the test method for FetchAllTrackApplications. If the
	 * CorrespondenceRecordsFilter is null than it will check for exception
	 * 
	 * @author maheshthakur
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFetchAllTrackApplications_ForNullRecordsFilter() {
		CorrespondenceRecordsFilter filter = null;
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		correspondenceService.fetchAllTrackApplications(filter, paginationInfo);
	}
	
	/**
	 * This is the test method for FetchMyTrackApplications. This will assert
	 * true if the records in the DB are same as the expected.
	 * 
	 * @author maheshthakur
	 */
	@Test
	public void testFetchMyTrackApplications() {
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(2L, null);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		SearchResult<CorrespondenceRecordDTO> fetchItems = correspondenceService.fetchMyTrackApplications(filter, paginationInfo);
		Assert.assertNotNull(fetchItems);
		Assert.assertEquals(2, fetchItems.getItems().size());
	}
	
	/**
	 * This method will verify the method fetchMyTrackApplications.This will
	 * bring the search result from the db, available within the date range. The
	 * data available with the date range is 0 so the result will be 0 i.e no
	 * records available for that range .
	 * 
	 * @author maheshthakur
	 */
	@Test
	public void testFetchMyTrackApplications_WhenStartDateEndDateNotNull_AndNoValueInRange() {
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(2L, null);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		Calendar date = new GregorianCalendar(2006, Calendar.JULY, 3);
	    
		filter.setStartDate(date.getTime());
		date.add(Calendar.DAY_OF_MONTH, 7);
		filter.setEndDate(date.getTime());
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		SearchResult<CorrespondenceRecordDTO> fetchItems = correspondenceService.fetchMyTrackApplications(filter, paginationInfo);
		
		Assert.assertNotNull(fetchItems);
		Assert.assertEquals(0, fetchItems.getItems().size());
	}
	
	/**
	 * This method will verify the method fetchMyTrackApplications.This will
	 * bring the search result from the db, available within the date range. The
	 * data available with the date range is 1 so the result will be 1.
	 * 
	 * @author maheshthakur
	 */
	@Test
	public void testFetchMyTrackApplications_WhenStartDateEndDateNotNull_AndValueInRange() {
		CorrespondenceRecordsFilter filter = new CorrespondenceRecordsFilter(2L, null);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		Calendar date = new GregorianCalendar(2016, Calendar.JANUARY, 3);
	    
		filter.setStartDate(date.getTime());
		date.add(Calendar.DAY_OF_MONTH, 7);
		filter.setEndDate(date.getTime());
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		SearchResult<CorrespondenceRecordDTO> fetchItems = correspondenceService.fetchMyTrackApplications(filter, paginationInfo);
		
		Assert.assertNotNull(fetchItems);
		Assert.assertEquals(1, fetchItems.getItems().size());
	}
	
	/**
	 * This is the test method for FetchMyTrackApplications. If the
	 * CorrespondenceRecordsFilter is null than it will check for exception
	 * 
	 * @author maheshthakur
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFetchMyTrackApplications_ForNullRecordsFilter() {
		CorrespondenceRecordsFilter filter = null;
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setAsc(false);
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		paginationInfo.setSortAttribute(SortAttribute.CORR_ACTION_ITEM_MAILING_DATE);
		correspondenceService.fetchMyTrackApplications(filter, paginationInfo);
	}
	

	/*********************** Track Application TestCases End ***************************/
	
}
