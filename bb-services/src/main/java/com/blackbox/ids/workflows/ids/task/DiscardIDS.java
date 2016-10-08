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
 * The delegation class {@code DiscardIDS} is responsible to do following actions
 * <ul>
 * <li>Release references flows, claimed by the IDS</li>
 * <li>Update the IDS status to DISCARDED</li>
 * </ul>
 *
 * @author ajay2258
 */
public class DiscardIDS extends IDSTask {

	private final Logger log = Logger.getLogger(InitiateIDS.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Map<String, Object> processData = execution.getVariables();
		Long idsID = (Long) processData.get(IDSBuildConst.IDS_dbID);

		IDSBuildService idsBuildService = (IDSBuildService) this.idsBuildService.getValue(execution);
		long discardedBy = (long) execution.getVariable(IDSBuildConst.LAST_UPDATED_BY);

		log.info(String.format("[DISCARD IDS] Processing for IDS {0}, Discarded By: {1}.", idsID, discardedBy));
		idsBuildService.discardIDS(idsID, discardedBy);
		execution.setVariable(IDSBuildConst.IDS_STATUS, IDS.Status.DISCARDED);
		log.info(String.format("[DISCARD IDS] Task completed for IDS {0}.", idsID));
	}

}
