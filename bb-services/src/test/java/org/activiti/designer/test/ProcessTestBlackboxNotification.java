package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import com.blackbox.ids.services.notification.process.NotificationProcessConstant;

public class ProcessTestBlackboxNotification {

	private String filename = "D:/Project/BlackBox/workspace/blackbox/bb-services/src/main/resources/workflows/blackboxNotification.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("blackboxNotification.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("name", "Activiti");
		
		List<String> ids = new ArrayList<>();
		ids.add("1");
		processVariables.put(NotificationProcessConstant.TASK_CANDIDATE_GROUPS, ids);
		processVariables.put(NotificationProcessConstant.ESCALATION_CANDIDATE_GROUPS, ids);
		processVariables.put(NotificationProcessConstant.PASS_TO, NotificationProcessConstant.PASS_TO_LEVEL);
		processVariables.put(NotificationProcessConstant.CURRENT_LEVEL, 1);
		processVariables.put(NotificationProcessConstant.TOTAL_LEVEL, 3);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("blackboxNotification", processVariables);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
}