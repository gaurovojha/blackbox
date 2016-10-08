/**
 *
 */
package com.blackbox.ids.workflows.ids.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;

import com.blackbox.ids.workflows.ids.common.IDSBuildConst;

/**
 * @author ajay2258
 *
 */
public class UpdateIDS extends IDSTask {

	private final Logger log = Logger.getLogger(UpdateIDS.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		/*-
		 * - Set flag 'changesRequested' to true.
		 * - IDS Status remains 'PENDING_APPROVAL'. Assignee becomes the paralegal
		 * -
		 */
		long idsID = (long) execution.getVariable(IDSBuildConst.IDS_dbID);
		log.info(String.format("[IDS: CHANGES REQUESTSED] Processing for IDS {0}.", idsID));
		execution.setVariable(IDSBuildConst.CHANGES_REQUESTED, true);
		log.info(String.format("[IDS: CHANGES REQUESTSED] Completed task for IDS {0}.", idsID));
	}

}
