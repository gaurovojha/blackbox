package com.blackbox.ids.workflows.notification;

import java.util.Set;
import java.util.stream.Collectors;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.blackbox.ids.core.model.notification.NotificationBusinessRule;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.repository.notification.process.NotificationProcessRepository;
import com.blackbox.ids.services.notification.process.NotificationProcessConstant;
import com.blackbox.ids.services.notification.process.NotificationProcessService;

@Component("workflowServiceClass2")
public class WorkflowServiceClass2 implements JavaDelegate {

	@Autowired
	private NotificationProcessService service;

	@Autowired
	private NotificationProcessRepository notificationProcessRepo;

	/** The super admin role id. */
	@Value("${super.admin.role.id}")
	private String superAdminRoleId;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Integer level = (Integer) execution.getVariable(NotificationProcessConstant.CURRENT_LEVEL);
		int noOfLevel = (Integer) execution.getVariable(NotificationProcessConstant.TOTAL_LEVEL);
		NotificationStatus notificationStatus = (NotificationStatus) execution
				.getVariable(NotificationProcessConstant.NOTIFICATION_STATUS);
		Long notificationProcessId = service.getNotificationProcessIdByActivitiId(execution.getProcessInstanceId());

		if (NotificationStatus.RESTARTED == notificationStatus
				|| NotificationStatus.RESUBMITTED == notificationStatus
				|| NotificationStatus.RESPONSED == notificationStatus) {
			execution.setVariable(NotificationProcessConstant.PASS_TO, NotificationProcessConstant.PASS_TO_LEVEL);
			execution.setVariable(NotificationProcessConstant.CURRENT_LEVEL, 1);
		} else if (NotificationStatus.QUERIED == notificationStatus) {
			execution.setVariable(NotificationProcessConstant.PASS_TO, NotificationProcessConstant.PASS_TO_LEVEL);
			execution.setVariable(NotificationProcessConstant.CURRENT_LEVEL, level - 1);
		} else if (level <= noOfLevel) {
			Set<Long> senderRoleIds = (Set<Long>) execution.getVariable(NotificationProcessConstant.ROLE_IDS);
			NotificationStatus status = NotificationStatus.PENDING;

			Set<NotificationBusinessRule> businessRules = (Set<NotificationBusinessRule>) execution
					.getVariable(NotificationProcessConstant.BUSINESS_RULES);
			Set<NotificationBusinessRule> candidateBusinessRules = NotificationProcessUtil
					.getCandidateBusinessRuleIds(null, businessRules, senderRoleIds, level);
			Set<Long> receiverRoleIds = NotificationProcessUtil.getReceiverRoleIds(candidateBusinessRules, level);
			service.removeDeletedRoleIds(receiverRoleIds);
			Boolean isDelegated = service.checkAndUpdateRoleIdsForDelegationRule(receiverRoleIds);

			if (CollectionUtils.isEmpty(receiverRoleIds)) {
				status = NotificationStatus.ERRORED;
				receiverRoleIds.add(Long.valueOf(superAdminRoleId));
			}

			Set<String> roles = receiverRoleIds.stream().map(id -> id.toString()).collect(Collectors.toSet());

			execution.setVariable(NotificationProcessConstant.BUSINESS_RULES, candidateBusinessRules);
			execution.setVariable(NotificationProcessConstant.PASS_TO, NotificationProcessConstant.PASS_TO_LEVEL);
			execution.setVariable(NotificationProcessConstant.TASK_CANDIDATE_GROUPS, roles);
			execution.setVariable(NotificationProcessConstant.ROLE_IDS, receiverRoleIds);

			service.completeNotificationLevel(notificationProcessId, receiverRoleIds, level, isDelegated, status);
		} else {
			execution.setVariable(NotificationProcessConstant.PASS_TO, NotificationProcessConstant.PASS_TO_END);
			service.completeNotification(execution.getProcessInstanceId(), notificationStatus);
		}

		service.setNotificationAuditData(notificationProcessRepo.getOne(notificationProcessId), notificationStatus);
	}
}
