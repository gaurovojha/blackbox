/**
 *
 */
package com.blackbox.ids.services.ids.impl;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.services.ids.IDSWorkflowService;
import com.blackbox.ids.workflows.ids.common.ControlFlags;
import com.blackbox.ids.workflows.ids.common.ControlFlags.AttorneyResponse;
import com.blackbox.ids.workflows.ids.common.ControlFlags.ParallegalAction;
import com.blackbox.ids.workflows.ids.common.IDSBuildConst;

/**
 * @implSpec Uses activiti workflow to manage IDS build.
 * @author ajay2258
 */
@Service("idsWorkflowService")
public class IDSWorkflowServiceImpl implements IDSWorkflowService {

	private final Logger log = Logger.getLogger(IDSWorkflowService.class);

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Override
	public long initiateIDSBuild(final Long applicationID) {
		log.info(String.format("Initiating IDS workflow for application {0}.", applicationID));
		Map<String, Object> processData = new HashMap<>();
		processData.put(IDSBuildConst.TARGET_APPLICATION, applicationID);
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(IDSBuildConst.PROCESS_KEY, processData);
		log.info(String.format("IDS build workflow initiated for application {0}.", applicationID));
		return (long) runtimeService.getVariable(instance.getId(), IDSBuildConst.IDS_dbID);
	}

	@Override
	public void processParalegalAction(final long idsID, final ParallegalAction parallegalAction,
			final Long attorneyUser) {
		if (parallegalAction == null
				|| (parallegalAction.equals(ParallegalAction.SUBMIT_FOR_APPROVAL) && attorneyUser == null)) {
			throw new IllegalArgumentException("Missing mandatory attributes.");
		}

		log.info(String.format("[IDS Workflow]: Processing paralegal action {0}, Attorney User: {1}, IDS: {2}.",
				parallegalAction, attorneyUser, idsID));

		long currentUser = BlackboxSecurityContextHolder.getUserId();
		Map<String, Object> variables = new HashMap<>(4);
		variables.put(ControlFlags.ParallegalAction.KEY, parallegalAction.name());
		variables.put(IDSBuildConst.PARALEGAL_USER, currentUser);
		variables.put(IDSBuildConst.LAST_UPDATED_BY, currentUser);
		variables.put(IDSBuildConst.ATTORNEY_USER, attorneyUser);
		completeCurrentTask(idsID, variables);
	}

	@Override
	public void processAttorneyAction(final long idsID, final AttorneyResponse attorneyResponse) {
		if (attorneyResponse == null) {
			throw new IllegalArgumentException("Missing mandatory parameters.");
		}

		log.info(String.format("[IDS Workflow]: Processing attorney response {0} for IDS: {1}.", attorneyResponse,
				idsID));
		Map<String, Object> variables = new HashMap<>(2);
		variables.put(ControlFlags.AttorneyResponse.KEY, attorneyResponse.name());
		variables.put(IDSBuildConst.LAST_UPDATED_BY, BlackboxSecurityContextHolder.getUserId());
		completeCurrentTask(idsID, variables);
	}

	private void completeCurrentTask(final Long idsID, Map<String, Object> variables) {
		Task task = getCurrentTask(idsID);
		taskService.setVariables(task.getId(), variables);
		taskService.complete(task.getId());
	}

	private Task getCurrentTask(final Long idsID) {
		ProcessInstance instance = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(IDSBuildConst.PROCESS_KEY)
				.variableValueEquals(IDSBuildConst.IDS_dbID, idsID)
				.singleResult();

		return taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
	}

}
