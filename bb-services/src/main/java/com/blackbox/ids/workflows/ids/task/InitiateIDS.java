/**
 *
 */
package com.blackbox.ids.workflows.ids.task;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;

import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.services.ids.IDSBuildService;
import com.blackbox.ids.workflows.ids.common.IDSBuildConst;

/**
 * The delegation class {@code InitiateIDS} is responsible to do following actions
 * <ul>
 * <li>Claims uncited references flows, targeted for given application</li>
 * <li>Creates an IDS instance in database</li>
 * </ul>
 *
 * @author ajay2258
 */
public class InitiateIDS extends IDSTask {

	private final Logger log = Logger.getLogger(InitiateIDS.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Map<String, Object> processData = execution.getVariables();
		Long application = (Long) processData.get(IDSBuildConst.TARGET_APPLICATION);

		log.info(String.format("Generating IDS build Id for aplication {0}.", application));
		IDSBuildService idsBuildService = (IDSBuildService) this.idsBuildService.getValue(execution);
		IDS ids = idsBuildService.initiateIDS(application);
		execution.setVariable(IDSBuildConst.IDS_dbID, ids.getId());
		execution.setVariable(IDSBuildConst.IDS_STATUS, IDS.Status.IN_PROGRESS);
	}

}
