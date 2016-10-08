package com.blackbox.ids.services.mdm;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.ChangeRequestDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.ActiveRecordsFilter;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.SortAttribute;
import com.blackbox.ids.it.AbstractIntegrationTest;

public class DashboardServiceTest extends AbstractIntegrationTest{

	@Autowired
	private MdmDashboardService dashboardService;

	@Autowired
	private JdbcTemplate jdbcTemplateTest;

	/** Verifies active records list*/
	@Test
	public void testActiveRecordsList() {
		int exectedActiveRecSize = 2;
		int actualActiveRecSize =0 ;
		setTestUser();
		ActiveRecordsFilter filter = new ActiveRecordsFilter();
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(200L);
		pageInfo.setSortAttribute(SortAttribute.FILING_DATE);
		SearchResult<MdmRecordDTO> mdmRecord = dashboardService.filterActiveRecords(filter, pageInfo);
		actualActiveRecSize = mdmRecord.getItems().size() ;
		assertNotNull(mdmRecord);
		Assert.assertTrue("There should be atleast 2 active recrods ",actualActiveRecSize>=2);

	}

	/** Verifies active records list*/
	@Test
	public void testActiveRecordsList_WithDateRange() {
		int exectedActiveRecSize = 2;
		int actualActiveRecSize ;
		setTestUser();
		ActiveRecordsFilter filter = new ActiveRecordsFilter();
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(200L);
		pageInfo.setSortAttribute(SortAttribute.FILING_DATE);
		SearchResult<MdmRecordDTO> mdmRecord = dashboardService.filterActiveRecords(filter, pageInfo);
		actualActiveRecSize = mdmRecord.getItems().size() ;
		assertNotNull(mdmRecord);
		Assert.assertTrue("There should be atleast 3 inactive recrods ",actualActiveRecSize>=3);

	}

	/** Verifies my active records list*/
	//@Test
	public void testMyActiveRecordsList() {
		int exectedActiveRecSize = 2;
		int actualActiveRecSize ;
		setTestUser();
		ActiveRecordsFilter filter = new ActiveRecordsFilter();
		filter.setOwnedBy(3L);
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(200L);
		pageInfo.setSortAttribute(SortAttribute.FILING_DATE);
		SearchResult<MdmRecordDTO> mdmRecord = dashboardService.filterActiveRecords(filter, pageInfo);
		actualActiveRecSize = mdmRecord.getItems().size() ;
		assertNotNull(mdmRecord);
		Assert.assertEquals(actualActiveRecSize , exectedActiveRecSize);

	}

	/** Verifies inactive records list*/
	@Test
	public void testInActiveRecordsList(){
		int exectedActiveRecSize = 3;
		int actualActiveRecSize ;
		setTestUser();
		ActiveRecordsFilter filter = new ActiveRecordsFilter();
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(200L);
		pageInfo.setSortAttribute(SortAttribute.FILING_DATE);
		SearchResult<MdmRecordDTO> mdmRecord = dashboardService.filterInActiveRecords(filter, pageInfo);
		actualActiveRecSize = mdmRecord.getItems().size() ;
		assertNotNull(mdmRecord);
		Assert.assertTrue("There should be atleast 3 inactive recrods ",actualActiveRecSize>=3);
	}


	@Test
	public void testMockito() {
		System.out.println(BlackboxSecurityContextHolder.getUserId());

		ActiveRecordsFilter filter = new ActiveRecordsFilter(null, null, 1L);
		PaginationInfo pageInfo = new PaginationInfo(100L, 0L, null, true);
		try {
			SearchResult<MdmRecordDTO> result = dashboardService.filterActiveRecords(filter, pageInfo);
			Assert.assertTrue(result.getItems().isEmpty());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Verifies change request records list*/
	@Test
	public void testChangeList() {
		int exectedActiveRecSize = 2;
		int actualActiveRecSize ;
		//loadActiveRecordsScript();
		setTestUser();
		ActiveRecordsFilter filter = new ActiveRecordsFilter();
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(200L);
		pageInfo.setSortAttribute(SortAttribute.FILING_DATE);
		SearchResult<ChangeRequestDTO> changeRecords = dashboardService.getChangeRequestRecords(filter, pageInfo);
		actualActiveRecSize = changeRecords.getItems().size() ;
		assertNotNull(changeRecords);
		Assert.assertEquals(actualActiveRecSize , exectedActiveRecSize);
	}

}
