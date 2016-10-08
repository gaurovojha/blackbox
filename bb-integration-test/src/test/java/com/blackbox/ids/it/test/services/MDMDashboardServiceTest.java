package com.blackbox.ids.it.test.services;


import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.blackbox.ids.core.auth.BlackboxUser;
import com.blackbox.ids.core.common.BbxApplicationContextUtil;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.ChangeRequestDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.ActiveRecordsFilter;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ExclusionList;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.repository.ExclusionListRepository;
import com.blackbox.ids.it.IntegrationTest;
import com.blackbox.ids.services.mdm.MdmDashboardService;
import com.blackbox.ids.services.notification.process.NotificationProcessService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/integration-test.xml")
@Category(IntegrationTest.class)
@ActiveProfiles("integration")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MDMDashboardServiceTest{

	@Autowired
	private MdmDashboardService mdmDashboardService;

	@Autowired
	private ApplicationBaseDAO applicationBaseDao;

	private MdmRecordDTO mdmRecordDTO;

	@Autowired
	private ExclusionListRepository exclusionListRepository;

	@Autowired
	private NotificationProcessService notificationProcessService;

	/** The logger. */
	private Logger	logger	= Logger.getLogger(MDMDashboardServiceTest.class);

	protected static final String TEST_LOGIN_ID = "junit@bbx.com";

	protected void setTestUser() {
		UserDetailsService detailsService = (UserDetailsService) BbxApplicationContextUtil.getBean("userDetailService");
		BlackboxUser userDetails = (BlackboxUser) detailsService.loadUserByUsername(TEST_LOGIN_ID);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Before
	public void setup() {
		setApplicationData();
	}

	@After
	public void tearUp() {
		mdmRecordDTO = null;
	}


	private void setApplicationData(){
		mdmRecordDTO = new MdmRecordDTO();
		mdmRecordDTO.setDbId(1L);
		mdmRecordDTO.setFamilyId("F000008");
		mdmRecordDTO.setJurisdiction("US");
	}

	@Test
	public void testUpdateStatusToActive_1(){
		boolean updateStatus = mdmDashboardService.updateRecordStatusToActive(mdmRecordDTO, "3", MDMRecordStatus.ACTIVE);
		Assert.assertEquals("Status should be updated.", Boolean.TRUE ,updateStatus);
		ExclusionList exclusionList = exclusionListRepository.findAll().get(0);
		Assert.assertNotNull(exclusionList);

	}

	@Test
	public void testUpdateStatusToActive_2(){
		ApplicationBase appBase = applicationBaseDao.getApplicationBase(3L);
		Assert.assertNotNull(appBase);
		Assert.assertEquals("Status should change to Active.", MDMRecordStatus.ACTIVE ,appBase.getRecordStatus());
	}

	@Test
	public void testCreateDeactivateApprovalNotification_1(){
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(1L);
		boolean updateStatus = mdmDashboardService.createDeactivateApprovalNotification(roleIds,1L, EntityName.APPLICATION_BASE, NotificationProcessType.DASHBOARD_ACTION_STATUS, mdmRecordDTO, MDMRecordStatus.DEACTIVATED);
		Assert.assertEquals("Status should be updated.", Boolean.TRUE ,updateStatus);

	}

	@Test
	public void testCreateDeactivateApprovalNotification_2(){
		ApplicationBase appBase = applicationBaseDao.getApplicationBase(1L);
		Assert.assertNotNull(appBase);
		Assert.assertEquals("New Record Status should change to DeActivated.", MDMRecordStatus.DEACTIVATED ,appBase.getNewRecordStatus());
	}

	@Test
	public void testCreateDroppedApprovalNotification_1(){
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(1L);
		boolean updateStatus = mdmDashboardService.createDroppedApprovalNotification(roleIds,1L, EntityName.APPLICATION_BASE, NotificationProcessType.DASHBOARD_ACTION_STATUS, mdmRecordDTO, MDMRecordStatus.DROPPED);
		Assert.assertEquals("Status should be updated.", Boolean.TRUE ,updateStatus);
	}

	@Test
	public void testCreateDroppedApprovalNotification_2(){
		ApplicationBase appBase = applicationBaseDao.getApplicationBase(1L);
		Assert.assertNotNull(appBase);
		Assert.assertEquals("New Record Status should change to DeActivated.", MDMRecordStatus.DROPPED ,appBase.getNewRecordStatus());
	}


	@Test
	public void testGetMdmRecordDTO(){
		MdmRecordDTO mdmRecordDTO = mdmDashboardService.getMdmRecordDTO(1L);
		Assert.assertNotNull(mdmRecordDTO);
		Assert.assertEquals("Application number should be correct.", "12345678" ,mdmRecordDTO.getApplicationNumber());
		Assert.assertEquals("Status should be Active.", MDMRecordStatus.ACTIVE ,mdmRecordDTO.getStatus());
		Assert.assertEquals("Family Id should be correct.", "F000008" ,mdmRecordDTO.getFamilyId());
		Assert.assertEquals("Jurisdiction should be correct.", "US" ,mdmRecordDTO.getJurisdiction());
	}

	@Test
	public void testRejectDeactivateStatusRequest_1(){
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(1L);
		long notificationId = notificationProcessService.createNotification(null, null, null, 6l, EntityName.APPLICATION_BASE, NotificationProcessType.DASHBOARD_ACTION_STATUS, null);
		Assert.assertNotNull(notificationId);
		boolean updateStatus = mdmDashboardService.rejectDeactivateStatusRequest(String.valueOf(notificationId), "6", MDMRecordStatus.BLANK);
		Assert.assertEquals("Status should be updated.", Boolean.TRUE ,updateStatus);
	}

	@Test
	public void testRejectDeactivateStatusRequest_2(){
		ApplicationBase appBase = applicationBaseDao.getApplicationBase(6L);
		Assert.assertNotNull(appBase);
		Assert.assertEquals("New Record Status should change to Blank.", MDMRecordStatus.BLANK ,appBase.getNewRecordStatus());
	}

	@Test
	public void testRejectDroppedStatusRequest_1(){
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(1L);
		long notificationId = notificationProcessService.createNotification(null, null, null, 4l, EntityName.APPLICATION_BASE, NotificationProcessType.DASHBOARD_ACTION_STATUS, null);
		Assert.assertNotNull(notificationId);
		boolean updateStatus = mdmDashboardService.rejectDroppedStatusRequest(String.valueOf(notificationId), "4", MDMRecordStatus.BLANK);
		Assert.assertEquals("Status should be updated.", Boolean.TRUE ,updateStatus);
	}

	@Test
	public void testRejectDroppedStatusRequest_2(){
		ApplicationBase appBase = applicationBaseDao.getApplicationBase(4L);
		Assert.assertNotNull(appBase);
		Assert.assertEquals("New Record Status should change to Blank.", MDMRecordStatus.BLANK ,appBase.getNewRecordStatus());
		ApplicationBase appChildBase = applicationBaseDao.getApplicationBase(5L);
		Assert.assertNotNull(appChildBase);
		Assert.assertEquals("New Record Status should change to Blank.", MDMRecordStatus.BLANK ,appChildBase.getNewRecordStatus());
	}

	@Test
	public void testUpdateRecordStatusToDeactivate_1(){
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(1L);
		long notificationId = notificationProcessService.createNotification(null, null, null, 6l, EntityName.APPLICATION_BASE, NotificationProcessType.DASHBOARD_ACTION_STATUS, null);
		Assert.assertNotNull(notificationId);
		boolean updateStatus = mdmDashboardService.updateRecordStatusToDeactivate(String.valueOf(notificationId), "6", MDMRecordStatus.DEACTIVATED, MDMRecordStatus.BLANK);
		Assert.assertEquals("Status should be updated.", Boolean.TRUE ,updateStatus);
	}

	@Test
	public void testUpdateRecordStatusToDeactivate_2(){
		ApplicationBase appBase = applicationBaseDao.getApplicationBase(6L);
		Assert.assertNotNull(appBase);
		Assert.assertEquals("Record Status should change to Deactivated.", MDMRecordStatus.DEACTIVATED ,appBase.getRecordStatus());
		Assert.assertEquals("New Record Status should change to Blank.", MDMRecordStatus.BLANK ,appBase.getNewRecordStatus());
	}

	@Test
	public void testUpdateRecordStatusToDropped_1(){
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(1L);
		long notificationId = notificationProcessService.createNotification(null, null, null, 4l, EntityName.APPLICATION_BASE, NotificationProcessType.DASHBOARD_ACTION_STATUS, null);
		Assert.assertNotNull(notificationId);
		boolean updateStatus = mdmDashboardService.updateRecordStatusToDelete(String.valueOf(notificationId), "4", MDMRecordStatus.DROPPED, MDMRecordStatus.BLANK);
		Assert.assertEquals("Status should be updated.", Boolean.TRUE ,updateStatus);
	}

	@Test
	public void testUpdateRecordStatusToDropped_2(){
		ApplicationBase appBase = applicationBaseDao.getApplicationBase(4L);
		Assert.assertNotNull(appBase);
		Assert.assertEquals("Record Status should change to Dropped.", MDMRecordStatus.DROPPED ,appBase.getRecordStatus());
		Assert.assertEquals("New Record Status should change to Blank.", MDMRecordStatus.BLANK ,appBase.getNewRecordStatus());
		ApplicationBase appChildBase = applicationBaseDao.getApplicationBase(5L);
		Assert.assertNotNull(appChildBase );
		Assert.assertEquals("Record Status should change to Dropped.", MDMRecordStatus.DROPPED ,appChildBase.getRecordStatus());
		Assert.assertEquals("New Record Status should change to Blank.", MDMRecordStatus.BLANK ,appChildBase.getNewRecordStatus());
	}

	@Test
	public void testGetDeactivateAffectedTransactions(){
		MdmRecordDTO mdmRecordDTO = mdmDashboardService.getDeactivateAffectedTransactions(4L);
		//Assert.assertEquals("Count affected transactions.", 3 ,mdmRecordDTO.getCountAffectedTransaction());
		Assert.assertTrue(mdmRecordDTO.getCountAffectedTransaction()>0);
	}

	@Test
	public void testGetDeleteAffectedTransactions(){
		MdmRecordDTO mdmRecordDTO = mdmDashboardService.getDeleteAffectedTransactions(4L);
		//Assert.assertEquals("Count affected transactions.", 4 ,mdmRecordDTO.getCountAffectedTransaction());
		Assert.assertTrue(mdmRecordDTO.getCountAffectedTransaction()>0);
	}
	@Test
	public void testGetChangeRequestRecords(){
		testNullParameterValues();
		ActiveRecordsFilter filter = new ActiveRecordsFilter();
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setLimit(100);
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(1L);
		mdmDashboardService.createDeactivateApprovalNotification(roleIds,3L, EntityName.APPLICATION_BASE, NotificationProcessType.DASHBOARD_ACTION_STATUS, mdmRecordDTO, MDMRecordStatus.DEACTIVATED);
		SearchResult<ChangeRequestDTO> changeRequestDto = mdmDashboardService.getChangeRequestRecords(filter, pageInfo);
		int count = changeRequestDto.getItems().size();
		Assert.assertTrue(count>0);
	}

	@Test
	public void testCountChangeRequestRecords(){
		setTestUser();
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(1L);
		mdmDashboardService.createDeactivateApprovalNotification(roleIds,1L, EntityName.APPLICATION_BASE, NotificationProcessType.DASHBOARD_ACTION_STATUS, mdmRecordDTO, MDMRecordStatus.DEACTIVATED);
		long count = mdmDashboardService.countChangeRequestRecords();
		Assert.assertTrue(count>0);
	}


	private void testNullParameterValues(){
		try{
			mdmDashboardService.getChangeRequestRecords(null, null);
		}catch(IllegalArgumentException ie){
			logger.error("Filter active records: Filter and pagination details are must."+ie);
		}
	}

	@Test
	public void testUpdateSingleRecordToTransfer_1(){
		mdmRecordDTO.setFamilyMembers(false);
		boolean updateStatus = mdmDashboardService.updateRecordStatusToTransfer(mdmRecordDTO, "1", mdmRecordDTO.getFamilyId(), MDMRecordStatus.TRANSFERRED);
		Assert.assertEquals("Status should be updated.", Boolean.TRUE ,updateStatus);
	}

	@Test
	public void testUpdateSingleRecordToTransfer_2(){
		ApplicationBase appBase = applicationBaseDao.getApplicationBase(1L);
		Assert.assertNotNull(appBase);
		Assert.assertEquals("Record Status should change to Transferred.", MDMRecordStatus.TRANSFERRED ,appBase.getRecordStatus());
	}

	@Test
	public void testUpdateSingleRecordToAbandon_1(){
		mdmRecordDTO.setFamilyMembers(false);
		boolean updateStatus = mdmDashboardService.updateRecordStatusToAbandon(mdmRecordDTO, "1", mdmRecordDTO.getFamilyId(), MDMRecordStatus.ALLOWED_TO_ABANDON);
		Assert.assertEquals("Status should be updated.", Boolean.TRUE ,updateStatus);
	}

	@Test
	public void testUpdateSingleRecordToAbandon_2(){
		ApplicationBase appBase = applicationBaseDao.getApplicationBase(1L);
		Assert.assertNotNull(appBase);
		Assert.assertEquals("Record Status should change to Abandon.", MDMRecordStatus.ALLOWED_TO_ABANDON ,appBase.getRecordStatus());
	}

	@Test
	public void testUpdateFamilyRecordToTransfer_1(){
		mdmRecordDTO.setFamilyMembers(true);
		boolean updateStatus = mdmDashboardService.updateRecordStatusToTransfer(mdmRecordDTO, "1", mdmRecordDTO.getFamilyId(), MDMRecordStatus.TRANSFERRED);
		Assert.assertEquals("Status should be updated.", Boolean.TRUE ,updateStatus);

	}

	@Test
	public void testUpdateFamilyRecordToTransfer_2(){
		ApplicationBase appBaseParent = applicationBaseDao.getApplicationBase(1L);
		Assert.assertNotNull(appBaseParent);
		Assert.assertEquals("Record Status should updated.", MDMRecordStatus.TRANSFERRED ,appBaseParent.getRecordStatus());
		ApplicationBase appBaseChild = applicationBaseDao.getApplicationBase(2L);
		Assert.assertNotNull(appBaseChild);
		Assert.assertEquals("Record Status should updated.", MDMRecordStatus.TRANSFERRED ,appBaseChild.getRecordStatus());
	}

	@Test
	public void testUpdateFamilyRecordToAbandon_1(){
		mdmRecordDTO.setFamilyMembers(true);
		boolean updateStatus = mdmDashboardService.updateRecordStatusToAbandon(mdmRecordDTO, "1", mdmRecordDTO.getFamilyId(), MDMRecordStatus.ALLOWED_TO_ABANDON);
		Assert.assertEquals("Status should be updated.", Boolean.TRUE ,updateStatus);
	}

	@Test
	public void testUpdateFamilyRecordToAbandon_2(){
		ApplicationBase appBaseParent = applicationBaseDao.getApplicationBase(1L);
		Assert.assertNotNull(appBaseParent);
		Assert.assertEquals("Record Status should updated.", MDMRecordStatus.ALLOWED_TO_ABANDON ,appBaseParent.getRecordStatus());
		ApplicationBase appBaseChild = applicationBaseDao.getApplicationBase(2L);
		Assert.assertNotNull(appBaseChild);
		Assert.assertEquals("Record Status should updated.", MDMRecordStatus.ALLOWED_TO_ABANDON ,appBaseChild.getRecordStatus());
	}

	@Test
	public void testNoRecordUpdateToTransfer(){
		mdmRecordDTO.setFamilyMembers(false);
		boolean updateStatus = mdmDashboardService.updateRecordStatusToTransfer(mdmRecordDTO, "1298", mdmRecordDTO.getFamilyId(), MDMRecordStatus.TRANSFERRED);
		Assert.assertEquals("Status should not be updated.", Boolean.FALSE ,updateStatus);
		boolean validRecordId = mdmDashboardService.updateRecordStatusToAbandon(mdmRecordDTO, "s21d", mdmRecordDTO.getFamilyId(), MDMRecordStatus.TRANSFERRED);
		Assert.assertEquals("Status should not be updated.", Boolean.FALSE ,validRecordId);
	}

	@Test
	public void testNoRecordUpdateToAbandon(){
		mdmRecordDTO.setFamilyMembers(false);
		boolean updateStatus = mdmDashboardService.updateRecordStatusToAbandon(mdmRecordDTO, "1298", mdmRecordDTO.getFamilyId(), MDMRecordStatus.ALLOWED_TO_ABANDON);
		Assert.assertEquals("Status should not be updated.", Boolean.FALSE ,updateStatus);
		boolean validRecordId = mdmDashboardService.updateRecordStatusToAbandon(mdmRecordDTO, "s21d", mdmRecordDTO.getFamilyId(), MDMRecordStatus.ALLOWED_TO_ABANDON);
		Assert.assertEquals("Status should not be updated.", Boolean.FALSE ,validRecordId);
	}

}