package com.blackbox.ids.services.reference;

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

import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.it.AbstractIntegrationTest;

public class ReferenceManagementServiceTest extends AbstractIntegrationTest {
	
	private static final Logger log = Logger.getLogger(ReferenceManagementServiceTest.class);

	private static int count = 0;
	
	@Autowired
	private ReferenceManagementService referenceManagementService;
	
	@BeforeClass
	public static void beforeClass() {
		log.info("Junit Test cases start for Reference Management Service");
	}

	@Before
	public void setUp() {
		count++;
		log.info(MessageFormat.format("Test case {0} start executing", count));
		setTestUser();
	}
	
	@Test
	public void testGetReferencesByCorrespondence() {
		Assert.assertNotNull(referenceManagementService);
		ReferenceBaseDTO dtos = referenceManagementService.getReferencesByCorrespondence(1l);
		Assert.assertNotNull(dtos);
	}
	
	@Test
	public void testIsReferenceExist() {
		Assert.assertNotNull(referenceManagementService);
		Assert.assertTrue(referenceManagementService.isReferenceExist(1l));
	}
	
	@Test
	public void testGetCorrespondenceById() {
		Assert.assertNotNull(referenceManagementService);
		Assert.assertNotNull(referenceManagementService.getCorrespondenceById(1l));
	}
	
	@Test
	public void testGetCorrespondenceFromStaging() {
		Assert.assertNotNull(referenceManagementService);
		Assert.assertNotNull(referenceManagementService.getCorrespondenceFromStaging("US", "12345678"));
	}
	
	@Test
	public void testUpdateReviewedReferences() {
		Assert.assertNotNull(referenceManagementService);
		Assert.assertTrue(referenceManagementService.updateReviewedReferences(1L, ""));
	}
	
	@After
	public void afterTest() {
		log.info(MessageFormat.format("Test case {0} stop executing", count));
	}

	@AfterClass
	public static void afterClass() {
		log.info("Junit Test cases end for Reference Management Service");
	}
}
