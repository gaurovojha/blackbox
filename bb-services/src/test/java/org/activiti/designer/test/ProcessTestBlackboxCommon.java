package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestBlackboxCommon {

	private String filename = "D:/Project/BlackBox/workspace/blackbox/bb-services/src/main/resources/workflows/blackboxNotification.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		ManagementService managementService = activitiRule.getManagementService();
		HistoryService historyService = activitiRule.getHistoryService();

		repositoryService.createDeployment().addInputStream("blackboxCommon.bpmn20.xml", new FileInputStream(filename))
				.deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("blackboxNotification", variableMap);
		assertNotNull(processInstance.getId());

		TaskQuery query = taskService.createTaskQuery().processInstanceId(processInstance.getId())
				.includeProcessVariables();
		Task theTask = query.singleResult();
		taskService.complete(theTask.getId());

		theTask = query.singleResult();
		taskService.complete(theTask.getId());

		theTask = query.singleResult();
		taskService.complete(theTask.getId());
		queryHistoricInstances(processInstance.getId());
		queryHistoricActivities();
		queryHistoricVariableUpdates();

		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

		// New Process Start

		processInstance = runtimeService.startProcessInstanceByKey("blackboxCommon", variableMap);
		assertNotNull(processInstance.getId());

		query = taskService.createTaskQuery().processInstanceId(processInstance.getId()).includeProcessVariables();
		theTask = query.singleResult();
		taskService.complete(theTask.getId());

		theTask = query.singleResult();
		taskService.complete(theTask.getId());

		theTask = query.singleResult();
		queryTask(processInstance.getId());
		// taskService.complete(theTask.getId());
		triggerEscalate(managementService, processInstance.getId());

		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

		queryProcessInstance(runtimeService, taskService);

		queryDeploymentService();
	}

	/*
	 * This method is used to get current running process instances of given a given process.
	 */
	private void queryProcessInstance(RuntimeService runtimeService, TaskService taskService) {
		List<ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey("blackboxCommon").list();

		for (ProcessInstance processInstance : instanceList) {
			assertEquals(false, processInstance.isEnded());
			System.out.println("id " + processInstance.getId() + ", ended=" + processInstance.isEnded());
			TaskQuery query = taskService.createTaskQuery().processInstanceId(processInstance.getId())
					.includeProcessVariables();
			Task theTask = query.singleResult();
			System.out
					.println("current Task id is " + theTask.getId() + " For process id is " + processInstance.getId());
		}
	}

	/*
	 * This method is used to trigger boundary timer event.
	 */
	private void triggerEscalate(ManagementService managementService, String processInstanceId) {
		Job timer = managementService.createJobQuery().processInstanceId(processInstanceId).singleResult();

		if (timer != null) {
			System.out.println("Timer task id is " + timer.getId());
			managementService.executeJob(timer.getId());
		}
	}

	/*
	 * This method is used to query task service.
	 */
	public void queryTask(String processInstanceId) {
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).includeProcessVariables()
				.taskCandidateGroup("admin").singleResult();
		assertEquals("User Task 1", task.getName());
		System.out.println(
				"task id " + task.getId() + ", name " + task.getName() + ", def key " + task.getTaskDefinitionKey());
	}

	/*
	 * Uses of Identity Service
	 */
	public void queryIdentityService() {
		IdentityService identityService = activitiRule.getIdentityService();
		User user = identityService.newUser("JohnDoe");
		identityService.saveUser(user);
	}

	/*
	 * Uses of Repository Service
	 */
	public void queryDeploymentService() {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		Deployment deployment = repositoryService.createDeploymentQuery().singleResult();
		assertNotNull(deployment);
		System.out.println("Deployment " + deployment.getName() + " " + deployment.getId());

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().latestVersion()
				.singleResult();
		assertNotNull(processDefinition);
		System.out.println("Found process definition " + processDefinition.getId() + " " + processDefinition.getKey());
	}

	/*
	 * Uses of Historic process instances.
	 */
	public void queryHistoricInstances(String processInstanceID) {
		HistoryService historyService = activitiRule.getHistoryService();
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceID).singleResult();
		assertNotNull(historicProcessInstance);
		assertEquals(processInstanceID, historicProcessInstance.getId());
		System.out.println("history process with definition id " + historicProcessInstance.getProcessDefinitionId()
				+ ", started at " + historicProcessInstance.getStartTime() + ", ended at "
				+ historicProcessInstance.getEndTime() + ", duration was "
				+ historicProcessInstance.getDurationInMillis());
	}

	public void queryHistoricActivities() {
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery().list();

		System.out.println(activityList.size());

		for (HistoricActivityInstance historicActivityInstance : activityList) {
			assertNotNull(historicActivityInstance.getActivityId());
			System.out.println("history activity " + historicActivityInstance.getActivityName() + ", type "
					+ historicActivityInstance.getActivityType() + ", duration was "
					+ historicActivityInstance.getDurationInMillis());
		}
	}

	public void queryHistoricVariableUpdates() {
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricDetail> historicVariableUpdateList = historyService.createHistoricDetailQuery().variableUpdates().list();
		assertNotNull(historicVariableUpdateList);

		for (HistoricDetail historicDetail : historicVariableUpdateList) {
			assertTrue(historicDetail instanceof HistoricVariableUpdate);
			
			HistoricVariableUpdate historicVariableUpdate = (HistoricVariableUpdate) historicDetail;
			
			assertNotNull(historicVariableUpdate.getExecutionId());
			
			System.out.println("historic variable update, revision " + historicVariableUpdate.getRevision()
					+ ", variable type name " + historicVariableUpdate.getVariableTypeName() + ", variable name "
					+ historicVariableUpdate.getVariableName() + ", Variable value '"
					+ historicVariableUpdate.getValue() + "'");
		}
	}
}