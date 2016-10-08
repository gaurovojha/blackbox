/**
 *
 */
package com.blackbox.ids.services.mdm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.blackbox.ids.it.AbstractIntegrationTest;
import com.blackbox.ids.services.ids.IDSWorkflowService;
import com.blackbox.ids.workflows.ids.common.ControlFlags.ParallegalAction;

/**
 * @author ajay2258
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IdsBuildTest extends AbstractIntegrationTest {

	private static final long appID = 1L;

	@Autowired
	private IDSWorkflowService idsWorkflowService;

	@Test
	@Rollback(false)
	public void testIDSBuild_1() {
		idsWorkflowService.initiateIDSBuild(appID);
		Assert.assertEquals("Control must reach here.", true, true);
	}

	@Test
	@Rollback(false)
	public void testIDSBuild_2() {
		idsWorkflowService.processParalegalAction(appID, ParallegalAction.DISCARD_IDS, null);
		Assert.assertEquals("Control must reach here.", true, true);
	}

}
