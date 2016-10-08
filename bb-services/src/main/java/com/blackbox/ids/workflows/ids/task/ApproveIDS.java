/**
 *
 */
package com.blackbox.ids.workflows.ids.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;

import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.services.ids.IDSBuildService;
import com.blackbox.ids.workflows.ids.common.IDSBuildConst;

/**
 * @author ajay2258
 *
 */
public class ApproveIDS extends IDSTask {

	private final Logger log = Logger.getLogger(ApproveIDS.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		long idsID = (long) execution.getVariable(IDSBuildConst.IDS_dbID);

		IDSBuildService idsBuildService = (IDSBuildService) this.idsBuildService.getValue(execution);
		long attorney = (long) execution.getVariable(IDSBuildConst.ATTORNEY_USER);

		log.info(String.format("[APPROVE IDS] Processing for IDS: {0}, Attorney: {1}.", idsID, attorney));
		idsBuildService.processIDSApproval(idsID, attorney);
		execution.setVariable(IDSBuildConst.IDS_STATUS, IDS.Status.APPROVED);
		log.info(String.format("[APPROVE IDS] Task completed for IDS {0}.", idsID));
	}

}
