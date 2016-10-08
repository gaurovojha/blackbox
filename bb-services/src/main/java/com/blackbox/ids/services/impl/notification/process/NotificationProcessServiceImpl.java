package com.blackbox.ids.services.impl.notification.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.notification.Notification;
import com.blackbox.ids.core.model.notification.NotificationBusinessRule;
import com.blackbox.ids.core.model.notification.NotificationDefaultRole;
import com.blackbox.ids.core.model.notification.NotificationEscalationRole;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcess;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.repository.RoleRepository;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.notification.NotificationRepository;
import com.blackbox.ids.core.repository.notification.process.DelegationRuleRepository;
import com.blackbox.ids.core.repository.notification.process.NotificationProcessRepository;
import com.blackbox.ids.services.notification.process.NotificationEmailService;
import com.blackbox.ids.services.notification.process.NotificationProcessConstant;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.workflows.notification.NotificationAuditData;
import com.blackbox.ids.workflows.notification.NotificationProcessUtil;

/**
 * The Class WorkflowProcessServiceImpl.
 */
@Service(NotificationProcessService.BEAN_NAME)
public class NotificationProcessServiceImpl implements NotificationProcessService {

	/** The runtime service. */
	@Autowired
	private RuntimeService runtimeService;

	/** The task service. */
	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	/** The super admin role id. */
	@Value("${super.admin.role.id}")
	private String superAdminRoleId;

	@Value("${task.unlock.period}")
	private String taskUnlockPeriod;

	@Value("${task.escalation.duration}")
	private String taskEscalationDuration;

	/** The notification process repo. */
	@Autowired
	private NotificationProcessRepository notificationProcessRepo;

	/** The notification repository. */
	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private DelegationRuleRepository delegationRuleRepo;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ApplicationBaseDAO applicationBaseDAO;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private NotificationEmailService emailService;

	/** The log. */
	private static final Logger log = Logger.getLogger(NotificationProcessServiceImpl.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #getTasksByUser(java.lang.String)
	 */
	@Override
	@Transactional
	public List<Task> getTasksByUser(String userId) {
		TaskQuery query = taskService.createTaskQuery().processDefinitionKey(NotificationProcessConstant.PROCESS_NAME)
				.includeProcessVariables().taskAssignee(userId);
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #getTaskById(java.lang.String)
	 */
	@Override
	@Transactional
	public Task getTaskById(String taskId) {
		return taskService.createTaskQuery().taskId(taskId).includeProcessVariables().singleResult();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #assignNotificatioinToUser(java.lang. Long, java.lang.Long)
	 */
	@Override
	@Transactional
	public void assignNotificationToUser(Long userId, Long notificationProcessId) {
		notificationProcessRepo.assignNotificationProcess(new User(userId), Calendar.getInstance(),
				BlackboxSecurityContextHolder.getUserId(), notificationProcessId);
		assignTask(notificationProcessId, userId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #completeNotification(java.lang.String)
	 */
	@Override
	@Transactional
	public void completeNotification(String notificationActivitiId, NotificationStatus status) {
		notificationProcessRepo.completeNotificationProcess(BlackboxSecurityContextHolder.getUserId(),
				Calendar.getInstance(), notificationActivitiId, status);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #deleteNotification(java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional
	public void deleteNotification(Long notificationProcessId, String reason) {
		String activitiProcessId = notificationProcessRepo.getActivitiProcessIdByNPId(notificationProcessId);
		notificationProcessRepo.deleteNotificationProcess(BlackboxSecurityContextHolder.getUserId(),
				Calendar.getInstance(), notificationProcessId);
		setNotificationAuditData(notificationProcessRepo.findOne(notificationProcessId), NotificationStatus.DROPPED);
		runtimeService.deleteProcessInstance(activitiProcessId, reason);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #unassignNotificatioin(java.lang.Long)
	 */
	@Override
	@Transactional
	public void unassignNotificatioin(Long notificationProcessId) {
		notificationProcessRepo.unassignNotificationProcess(BlackboxSecurityContextHolder.getUserId(),
				Calendar.getInstance(), notificationProcessId);
		unassignTask(notificationProcessId);
	}

	/**
	 * Assign task.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @param userId
	 *            the user id
	 */
	private void assignTask(Long notificationProcessId, Long userId) {
		String activitiProcessId = notificationProcessRepo.getActivitiProcessIdByNPId(notificationProcessId);
		TaskQuery query = taskService.createTaskQuery().processInstanceId(activitiProcessId);
		Task theTask = query.singleResult();
		taskService.claim(theTask.getId(), userId.toString());
	}

	/**
	 * Unassign task.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 */
	private void unassignTask(Long notificationProcessId) {
		String activitiProcessId = notificationProcessRepo.getActivitiProcessIdByNPId(notificationProcessId);
		TaskQuery query = taskService.createTaskQuery().processInstanceId(activitiProcessId);
		Task theTask = query.singleResult();
		taskService.unclaim(theTask.getId());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #getNotificationProcessIdByActivitiId( java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public long getNotificationProcessIdByActivitiId(String activitiProcessId) {
		return notificationProcessRepo.getNotificationProcessIdByActivitiId(activitiProcessId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #completeNotificationLevel(java.lang. Long, java.util.Calendar,
	 * java.lang.Long, java.util.Set)
	 */
	@Override
	@Transactional
	public long completeNotificationLevel(Long notificationProcessId, Set<Long> roleIds, int currentLevel,
			Boolean isDelegate, NotificationStatus status) {
		NotificationProcess notificationProcess = notificationProcessRepo.getOne(notificationProcessId);
		List<Role> roles = roleIds.stream().map(roleId -> new Role(roleId)).collect(Collectors.toList());

		notificationProcess.getRoles().clear();
		notificationProcess.getRoles().addAll(roles);
		notificationProcess.setUpdatedByUser(BlackboxSecurityContextHolder.getUserId());
		notificationProcess.setUpdatedDate(Calendar.getInstance());
		notificationProcess.setNotifiedDate(Calendar.getInstance());
		notificationProcess.setActive(true);
		notificationProcess.setStatus(status);
		notificationProcess.setLockedByUser(null);
		notificationProcess.setCurrentLevel(currentLevel);
		notificationProcess.setDelegated(isDelegate);
		notificationProcessRepo.save(notificationProcess);

		return notificationProcessId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #deleteNotificationForAppId(java.lang. Long, java.lang.String)
	 */
	@Override
	@Transactional
	public void deleteNotificationForAppId(Long applicationId, String reason) {
		List<String> activitiProcessIds = notificationProcessRepo.getActivitiProcessIdByAppId(applicationId);
		List<Long> notificationProcessIds = notificationProcessRepo.getNotificationProcessIdByAppId(applicationId);
		notificationProcessRepo.deleteNotificationForAppId(BlackboxSecurityContextHolder.getUserId(),
				Calendar.getInstance(), applicationId);

		for (Long notificationProId : notificationProcessIds) {
			setNotificationAuditData(notificationProcessRepo.findOne(notificationProId), NotificationStatus.DROPPED);
		}

		for (String activitiProcessId : activitiProcessIds) {
			runtimeService.deleteProcessInstance(activitiProcessId, reason);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #deleteNotificationForCorrespondenceId( java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional
	public void deleteNotificationForCorrespondenceId(Long correspondenceId, String reason) {
		List<String> activitiProcessIds = notificationProcessRepo.getActivitiProcessIdByCorresId(correspondenceId);
		List<Long> notificationProcessIds = notificationProcessRepo
				.getNotificationProcessIdByCorresId(correspondenceId);

		notificationProcessRepo.deleteNotificationForCorresId(BlackboxSecurityContextHolder.getUserId(),
				Calendar.getInstance(), correspondenceId);

		for (Long notificationProId : notificationProcessIds) {
			setNotificationAuditData(notificationProcessRepo.findOne(notificationProId), NotificationStatus.DROPPED);
		}

		for (String activitiProcessId : activitiProcessIds) {
			runtimeService.deleteProcessInstance(activitiProcessId, reason);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #deleteNotificationForReferenceId(java. lang.Long, java.lang.String)
	 */
	@Override
	@Transactional
	public void deleteNotificationForReferenceId(Long referenceId, String reason) {
		List<String> activitiProcessIds = notificationProcessRepo.getActivitiProcessIdByRefId(referenceId);
		List<Long> notificationProcessIds = notificationProcessRepo.getNotificationProcessIdByRefId(referenceId);

		notificationProcessRepo.deleteNotificationForRefId(BlackboxSecurityContextHolder.getUserId(),
				Calendar.getInstance(), referenceId);

		for (Long notificationProId : notificationProcessIds) {
			setNotificationAuditData(notificationProcessRepo.findOne(notificationProId), NotificationStatus.DROPPED);
		}

		for (String activitiProcessId : activitiProcessIds) {
			runtimeService.deleteProcessInstance(activitiProcessId, reason);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #getEntityIdByNotificationId(java.lang. Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Long getEntityIdByNotificationId(Long notificationProcessId) {
		Set<Long> roles = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();
		return notificationProcessRepo.getEntityIdByRolesAndProcessId(notificationProcessId, roles);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationProcessService
	 * #getActivitiIdByNotificationProcessId(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public String getActivitiIdByNotificationProcessId(Long notificationProcessId) {
		return notificationProcessRepo.getActivitiProcessIdByNPId(notificationProcessId);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void setNotificationAuditData(NotificationProcess notificationProcess,
			NotificationStatus notificationStatus) {
		List<NotificationAuditData> auditDatas = null;

		if (NotificationStatus.CREATED == notificationStatus) {
			List<Role> roles = notificationProcess.getRoles();
			List<User> users = notificationProcess.getUsers();
			List<Long> roleIds = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
			List<Long> userIds = users.stream().map(user -> user.getId()).collect(Collectors.toList());
			auditDatas = new ArrayList<>();
			NotificationAuditData auditData = new NotificationAuditData();

			auditData.setLevel(notificationProcess.getCurrentLevel());
			auditData.setNotificationProcessType(notificationProcess.getNotificationProcessType());
			auditData.setSubNotificationProcessType(notificationProcess.getSubNotificationProcessType());
			auditData.setCurrentStatus(NotificationStatus.PENDING);
			auditData.setPreviousStatus(NotificationStatus.CREATED);
			auditData.setIsRestarted(false);
			auditData.setIsDelegated(notificationProcess.getDelegated());
			auditData.setNotificationSentDate(notificationProcess.getCreatedDate());
			auditData.setNotificationSentBy(notificationProcess.getCreatedByUser());
			auditData.setLevelCompleteBy(notificationProcess.getCreatedByUser());
			auditData.getCurrentAssignedRoles().addAll(roleIds);
			auditData.getCurrentAssignedUsers().addAll(userIds);
			auditDatas.add(auditData);
		} else {
			auditDatas = (List<NotificationAuditData>) runtimeService
					.getVariable(notificationProcess.getActivitiProcessId(), NotificationProcessConstant.AUDIT_DATA);
			int size = auditDatas.size();

			NotificationAuditData oldAuditData = auditDatas.get(size - 1);
			oldAuditData.setCurrentStatus(notificationStatus);
			oldAuditData.setActionTakenBy(BlackboxSecurityContextHolder.getUserId());
			oldAuditData.setActionTakenDate(Calendar.getInstance());
			auditDatas.set(size - 1, oldAuditData);
			
			if (NotificationStatus.QUERIED == notificationStatus
					|| NotificationStatus.RESUBMITTED == notificationStatus
					|| NotificationStatus.RESPONSED == notificationStatus
					|| NotificationStatus.COMPLETED == notificationStatus
					|| NotificationStatus.APPROVED == notificationStatus
					|| NotificationStatus.REJECTED == notificationStatus) {
				oldAuditData.setLevelCompleteBy(BlackboxSecurityContextHolder.getUserId());
			}

			if ((NotificationStatus.DROPPED != notificationStatus
					&& notificationProcess.getNotification().getNotificationLevel() > oldAuditData.getLevel())
					|| NotificationStatus.RESTARTED == notificationStatus
					|| NotificationStatus.DELEGATED == notificationStatus
					|| NotificationStatus.QUERIED == notificationStatus
					|| NotificationStatus.RESUBMITTED == notificationStatus
					|| NotificationStatus.RESPONSED == notificationStatus) {
				List<Role> roles = notificationProcess.getRoles();
				List<User> users = notificationProcess.getUsers();
				List<Long> roleIds = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
				List<Long> userIds = users.stream().map(user -> user.getId()).collect(Collectors.toList());

				NotificationAuditData auditData = new NotificationAuditData();
				auditData.setLevel(notificationProcess.getCurrentLevel());
				auditData.setNotificationProcessType(notificationProcess.getNotificationProcessType());
				//auditData.setSubNotificationProcessType(notificationProcess.getSubNotificationProcessType());
				auditData.setCurrentStatus(notificationProcess.getStatus());
				auditData.setIsDelegated(notificationProcess.getDelegated());
				auditData.setNotificationSentDate(notificationProcess.getCreatedDate());
				auditData.setNotificationSentBy(notificationProcess.getCreatedByUser());
				auditData.getCurrentAssignedRoles().addAll(roleIds);
				auditData.getCurrentAssignedUsers().addAll(userIds);

				if (NotificationStatus.RESTARTED == notificationStatus) {
					auditData.setIsRestarted(true);
				} else {
					auditData.setIsRestarted(false);
				}

				auditDatas.add(auditData);
			}
		}

		runtimeService.setVariable(notificationProcess.getActivitiProcessId(), NotificationProcessConstant.AUDIT_DATA,
				auditDatas);
	}

	@Override
	@Transactional
	public Long createNotification(ApplicationBase application, Long referenceId, Long correspondenceId, Long entityId,
			EntityName entityName, NotificationProcessType notificationProcessType, List<Long> userIds,
			List<String> filePaths) {
		log.info("Notification creation in progress");
		Notification notification = notificationRepository
				.findByNotificationProcessTypeAndActive(notificationProcessType, true);
		Set<NotificationBusinessRule> businessRules = new HashSet<>();
		Set<Long> senderRoleIds = null;
		Map<String, Object> processVariables = null;
		NotificationStatus status = NotificationStatus.PENDING;

		try {
			senderRoleIds = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();
		} catch (SecurityException exception) {
			// Notification is initiated by system
			if (application != null) {
				senderRoleIds = NotificationProcessUtil.getSystemRoleIds(notification, application);
			}
		}

		Set<Long> validSenderRoleIds = getValidSenderRoleIds(senderRoleIds, application);
		Set<Long> receiverRoleIds = getReceiverRoleIds(notification, validSenderRoleIds, businessRules);
		removeDeletedRoleIds(receiverRoleIds);
		Boolean delegated = checkAndUpdateRoleIdsForDelegationRule(receiverRoleIds);

		if (CollectionUtils.isEmpty(receiverRoleIds)) {
			receiverRoleIds.add(Long.valueOf(superAdminRoleId));
			status = NotificationStatus.ERRORED;
			String appNo = application != null ? application.getApplicationNumber() : null;
			List<Long> senderIds = new ArrayList<>();
			senderIds.add(BlackboxSecurityContextHolder.getUserId());

			setErrorNotification(notification);
			emailService.sendEmailOnNotification(notification, senderIds, null, appNo);
		}

		processVariables = getProcessInstanceVariableMap(notification, application, receiverRoleIds, entityId,
				notificationProcessType, businessRules, entityName, status);

		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(NotificationProcessConstant.PROCESS_NAME, processVariables);

		NotificationProcess notificationProcess = new NotificationProcess(processInstance.getId(),
				notificationProcessType, notification, entityId, true, status, entityName, application.getId(),
				correspondenceId, referenceId);
		notificationProcess.setDelegated(delegated);

		for (Long roleId : receiverRoleIds) {
			notificationProcess.getRoles().add(new Role(roleId));
		}

		if (userIds != null) {
			for (Long userId : userIds) {
				notificationProcess.getUsers().add(new User(userId));
			}
		}

		Long notificationProcessId = notificationProcessRepo.save(notificationProcess).getNotificationProcessId();
		setNotificationAuditData(notificationProcess, NotificationStatus.CREATED);
		log.info("Notification created successfully");

		return notificationProcessId;
	}

	private Set<Long> getReceiverRoleIds(Notification notification, Set<Long> senderRoleIds,
			Set<NotificationBusinessRule> businessRules) {
		Set<Long> receiverRoleIds = new LinkedHashSet<>();

		if (CollectionUtils.isEmpty(senderRoleIds)) {
			if (CollectionUtils.isNotEmpty(notification.getDefaultRoles())) {
				for (NotificationDefaultRole defaultRule : notification.getDefaultRoles()) {
					receiverRoleIds.add(defaultRule.getRole().getId());
				}
			}
		} else {
			businessRules
					.addAll(NotificationProcessUtil.getCandidateBusinessRuleIds(notification, null, senderRoleIds, 1));

			if (CollectionUtils.isNotEmpty(businessRules) && businessRules.size() == 1) {
				receiverRoleIds = NotificationProcessUtil.getReceiverRoleIds(businessRules, 1);
			}
		}

		return receiverRoleIds;
	}

	/**
	 * Removes the deleted role ids from the list of given role ids.
	 *
	 * @param receiverRoleIds
	 *            the receiver role ids i.e the role ids that are still active
	 *            in the system.
	 */
	@Override
	@Transactional(readOnly = true)
	public void removeDeletedRoleIds(Set<Long> receiverRoleIds) {
		if (CollectionUtils.isNotEmpty(receiverRoleIds)) {
			Set<Long> finalReceiverRoleIds = new HashSet<>();

			for (Long roleId : receiverRoleIds) {
				if (roleRepository.isActiveRoleId(roleId)) {
					finalReceiverRoleIds.add(roleId);
				}
			}

			receiverRoleIds.clear();
			receiverRoleIds.addAll(finalReceiverRoleIds);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean checkAndUpdateRoleIdsForDelegationRule(Set<Long> receiverRoleIds) {
		Set<Long> validReceiverRoleIds = null;
		boolean delegated = false;

		if (CollectionUtils.isNotEmpty(receiverRoleIds) && roleRepository.countUsersByRoleIds(receiverRoleIds) <= 1) {
			validReceiverRoleIds = new LinkedHashSet<>();

			for (Long roleId : receiverRoleIds) {
				Long validRoleId = delegationRuleRepo.getDelegateRoleId(roleId, Calendar.getInstance());

				if (validRoleId != null && roleRepository.isActiveRoleId(validRoleId)) {
					validReceiverRoleIds.add(validRoleId);
					delegated = true;
				} else {
					validReceiverRoleIds.add(roleId);
				}
			}
		} else {
			validReceiverRoleIds = receiverRoleIds;
		}

		receiverRoleIds.clear();
		receiverRoleIds.addAll(validReceiverRoleIds);

		return delegated;
	}

	private Set<Long> getValidSenderRoleIds(Set<Long> senderRoleIds, ApplicationBase application) {
		Set<Long> validSenderRoleIds = new LinkedHashSet<>();
		
		if (CollectionUtils.isNotEmpty(senderRoleIds) && application != null) {
			List<Role> roles = roleRepository.findByIdIn(senderRoleIds);
			
			for (Role role : roles) {
				if (role.getAssignees().contains(application.getAssignee())
						&& role.getOrganizations().contains(application.getOrganization())
						&& role.getCustomers().contains(application.getCustomer())
						&& role.getTechnologyGroups().contains(application.getTechnologyGroup())
						&& role.getJurisdictions().contains(application.getJurisdiction())) {
					validSenderRoleIds.add(role.getId());
				}
			}
		}

		return validSenderRoleIds;
	}

	private Map<String, Object> getProcessInstanceVariableMap(Notification notification, ApplicationBase application,
			Set<Long> redeiverRoleIds, Long entityId, NotificationProcessType notificationProcessType,
			Set<NotificationBusinessRule> businessRules, EntityName entityName, NotificationStatus status) {
		Map<String, Object> processVariables = new HashMap<>();
		Set<String> escalation = new HashSet<>();
		Set<String> roles = redeiverRoleIds.stream().map(id -> id.toString()).collect(Collectors.toSet());

		if (CollectionUtils.isNotEmpty(notification.getEscalationRoles())) {
			for (NotificationEscalationRole role : notification.getEscalationRoles()) {
				escalation.add(role.getRole().getId().toString());
			}
		}

		if (application != null && application.getId() != null) {
			processVariables.put(NotificationProcessConstant.APPLICATION_ID, application.getId());
		}

		processVariables.put(NotificationProcessConstant.NOTIFICATION, notification);
		processVariables.put(NotificationProcessConstant.NOTIFICATION_STATUS, status);
		processVariables.put(NotificationProcessConstant.ROLE_IDS, redeiverRoleIds);
		processVariables.put(NotificationProcessConstant.TASK_CANDIDATE_GROUPS, roles);
		processVariables.put(NotificationProcessConstant.ENTITY_ID, entityId);
		processVariables.put(NotificationProcessConstant.ENTITY_NAME, entityName);
		processVariables.put(NotificationProcessConstant.NOTIFICATION_TYPE, notificationProcessType);
		processVariables.put(NotificationProcessConstant.BUSINESS_RULES, businessRules);
		processVariables.put(NotificationProcessConstant.ESCALATION_CANDIDATE_GROUPS, escalation);
		processVariables.put(NotificationProcessConstant.PASS_TO, NotificationProcessConstant.PASS_TO_LEVEL);
		processVariables.put(NotificationProcessConstant.CURRENT_LEVEL, 1);
		processVariables.put(NotificationProcessConstant.TOTAL_LEVEL, notification.getNotificationLevel());
		processVariables.put(NotificationProcessConstant.TASK_UNLOCK_PERIOD, taskUnlockPeriod);
		processVariables.put(NotificationProcessConstant.ESCALATION_TASK_PERIOD,
				getEscalationTaskPeriod(notification, status));
		processVariables.put(NotificationProcessConstant.REMINDER_TASK_PERIOD,
				getReminderTaskPeriod(notification, status));

		return processVariables;
	}

	private String getReminderTaskPeriod(Notification notification, NotificationStatus status) {
		String reminderTaskPeriod = StringUtils.EMPTY;

		if (NotificationStatus.PENDING != status || !notification.isReminder()) {
			reminderTaskPeriod = "R0/P100Y";
		} else {
			reminderTaskPeriod = "R" + notification.getNoOfReminders() + "/P"
					+ notification.getFrequencyOfSendingReminders() + "D";
		}

		return reminderTaskPeriod;
	}

	private String getEscalationTaskPeriod(Notification notification, NotificationStatus status) {
		String escalationTaskPeriod = StringUtils.EMPTY;

		if (NotificationStatus.PENDING != status || !notification.isEscalation()) {
			escalationTaskPeriod = "R0/P100Y";
		} else {
			escalationTaskPeriod = "R1/P" + notification.getNoOfPastDueDays() + "D";
		}

		return escalationTaskPeriod;
	}

	@Override
	@Transactional
	public void delegateNotifications(List<Long> notificationProcessIds, Long roleId) {
		for (Long notificationProcessId : notificationProcessIds) {
			delegateNotification(notificationProcessId, roleId);
		}
	}

	private void delegateNotification(Long notificationProcessId, Long roleId) {
		NotificationProcess notificationProcess = notificationProcessRepo.findOne(notificationProcessId);

		if (notificationProcess.getNotification().getAssignedToRole()) {
			notificationProcess.getRoles().clear();
			notificationProcess.getRoles().add(new Role(roleId));
			notificationProcess.setDelegated(true);
			notificationProcessRepo.save(notificationProcess);
			setNotificationAuditData(notificationProcess, NotificationStatus.DELEGATED);
		}
	}

	@Override
	@Transactional
	public void restartNotification(Long notificationProcessId, List<Long> userIds) {
		NotificationProcess notificationProcess = notificationProcessRepo.findOne(notificationProcessId);
		Notification notification = notificationRepository
				.findByNotificationProcessTypeAndActive(notificationProcess.getNotificationProcessType(), true);
		NotificationProcessType notificationProcessType = notificationProcess.getNotificationProcessType();
		Set<NotificationBusinessRule> businessRules = new HashSet<>();
		Set<Long> senderRoleIds = null;
		Map<String, Object> processVariables = null;
		ApplicationBase application = null;
		NotificationStatus status = NotificationStatus.PENDING;

		if (notificationProcess.getApplicationId() != null) {
			application = applicationBaseDAO.find(notificationProcess.getApplicationId());
		}

		if (Long.compare(notificationProcess.getCreatedByUser(), -1l) != 0) {
			Set<Role> roles = userRepo.findOne(notificationProcess.getCreatedByUser()).getUserRoles();
			senderRoleIds = roles.stream().map(role -> role.getId()).collect(Collectors.toSet());
		} else if (application != null) {
			senderRoleIds = NotificationProcessUtil.getSystemRoleIds(notification, application);
		} else {
			senderRoleIds = new HashSet<>();
		}

		if (application != null) {
			senderRoleIds = getValidSenderRoleIds(senderRoleIds, application);
		}

		Set<Long> receiverRoleIds = getReceiverRoleIds(notification, senderRoleIds, businessRules);
		removeDeletedRoleIds(receiverRoleIds);
		Boolean delegated = checkAndUpdateRoleIdsForDelegationRule(receiverRoleIds);

		if (CollectionUtils.isEmpty(receiverRoleIds)) {
			receiverRoleIds.add(Long.valueOf(superAdminRoleId));
			status = NotificationStatus.ERRORED;
			String appNo = application != null ? application.getApplicationNumber() : null;
			List<Long> senderIds = new ArrayList<>();
			senderIds.add(BlackboxSecurityContextHolder.getUserId());

			setErrorNotification(notification);
			emailService.sendEmailOnNotification(notification, senderIds, null, appNo);
		}

		processVariables = getProcessInstanceVariableMap(notification, application, receiverRoleIds,
				notificationProcess.getEntityId(), notificationProcessType, businessRules,
				notificationProcess.getEntityName(), NotificationStatus.RESTARTED);

		runtimeService.setVariables(notificationProcess.getActivitiProcessId(), processVariables);

		notificationProcess.setNotificationProcessType(notificationProcessType);
		notificationProcess.setNotification(notification);
		notificationProcess.setDelegated(delegated);
		notificationProcess.setStatus(status);
		notificationProcess.getRoles().clear();
		notificationProcess.setCurrentLevel(1);

		for (Long roleId : receiverRoleIds) {
			notificationProcess.getRoles().add(new Role(roleId));
		}
		
		if (CollectionUtils.isNotEmpty(userIds)) {
			notificationProcess.getUsers().clear();
			
			for (Long userId : userIds) {
				notificationProcess.getUsers().add(new User(userId));
			}
		}

		notificationProcessRepo.save(notificationProcess);

		TaskQuery query = taskService.createTaskQuery().processInstanceId(notificationProcess.getActivitiProcessId());
		Task theTask = query.singleResult();
		taskService.complete(theTask.getId());
	}

	@Override
	@Transactional
	public void resubmitNotification(Long notificationProcessId, List<Long> userIds) {
		NotificationProcess notificationProcess = notificationProcessRepo.findOne(notificationProcessId);
		Notification notification = notificationRepository
				.findByNotificationProcessTypeAndActive(notificationProcess.getNotificationProcessType(), true);
		NotificationProcessType notificationProcessType = notificationProcess.getNotificationProcessType();
		Set<NotificationBusinessRule> businessRules = new HashSet<>();
		Set<Long> senderRoleIds = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();
		Set<Long> validSenderRoleIds = null;
		Map<String, Object> processVariables = null;
		ApplicationBase application = null;
		NotificationStatus status = NotificationStatus.PENDING;

		if (notificationProcess.getApplicationId() != null) {
			application = applicationBaseDAO.find(notificationProcess.getApplicationId());
			validSenderRoleIds = getValidSenderRoleIds(senderRoleIds, application);
		} else {
			validSenderRoleIds = senderRoleIds;
		}
		
		Set<Long> receiverRoleIds = getReceiverRoleIds(notification, validSenderRoleIds, businessRules);
		removeDeletedRoleIds(receiverRoleIds);
		Boolean delegated = checkAndUpdateRoleIdsForDelegationRule(receiverRoleIds);

		if (CollectionUtils.isEmpty(receiverRoleIds)) {
			receiverRoleIds.add(Long.valueOf(superAdminRoleId));
			status = NotificationStatus.ERRORED;
			String appNo = application != null ? application.getApplicationNumber() : null;
			List<Long> senderIds = new ArrayList<>();
			senderIds.add(BlackboxSecurityContextHolder.getUserId());

			setErrorNotification(notification);
			emailService.sendEmailOnNotification(notification, senderIds, null, appNo);
		}

		processVariables = getProcessInstanceVariableMap(notification, application, receiverRoleIds,
				notificationProcess.getEntityId(), notificationProcessType, businessRules,
				notificationProcess.getEntityName(), NotificationStatus.RESUBMITTED);

		runtimeService.setVariables(notificationProcess.getActivitiProcessId(), processVariables);

		notificationProcess.setNotificationProcessType(notificationProcessType);
		notificationProcess.setNotification(notification);
		notificationProcess.setDelegated(delegated);
		notificationProcess.setStatus(status);
		notificationProcess.getRoles().clear();
		notificationProcess.setCurrentLevel(1);

		for (Long roleId : receiverRoleIds) {
			notificationProcess.getRoles().add(new Role(roleId));
		}
		
		if (CollectionUtils.isNotEmpty(userIds)) {
			notificationProcess.getUsers().clear();
			
			for (Long userId : userIds) {
				notificationProcess.getUsers().add(new User(userId));
			}
		}

		notificationProcessRepo.save(notificationProcess);

		TaskQuery query = taskService.createTaskQuery().processInstanceId(notificationProcess.getActivitiProcessId());
		Task theTask = query.singleResult();
		taskService.complete(theTask.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void requestNotificationInfo(Long notificationProcessId) {
		NotificationProcess notificationProcess = notificationProcessRepo.findOne(notificationProcessId);
		List<NotificationAuditData> auditDatas = (List<NotificationAuditData>) runtimeService
				.getVariable(notificationProcess.getActivitiProcessId(), NotificationProcessConstant.AUDIT_DATA);
		NotificationAuditData auditData = auditDatas.get(auditDatas.size() - 1);
		Long notificationSendBy = auditData.getLevelCompleteBy();
		int level = (auditData.getLevel() - 1)  < 1 ? 0 : (auditData.getLevel() - 1);
		
		//notificationProcess.getRoles().clear();
		notificationProcess.setCurrentLevel(level);
		notificationProcess.setUpdatedByUser(BlackboxSecurityContextHolder.getUserId());
		notificationProcess.setUpdatedDate(Calendar.getInstance());
		notificationProcess.getUsers().clear();
		notificationProcess.getUsers().add(new User(notificationSendBy));
		notificationProcessRepo.save(notificationProcess);
		
		completeTask(notificationProcessId, NotificationStatus.QUERIED);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Long> getReceiverUsers(final NotificationProcessType notificationType, final Long applicationId) {
		ApplicationBase application = applicationBaseDAO.find(applicationId);
		Notification notification = notificationRepository.findByNotificationProcessTypeAndActive(notificationType,
				true);
		Set<Long> roleIds = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();
		Set<Long> validSenderRoleIds = getValidSenderRoleIds(roleIds, application);
		Set<Long> receiverRoleIds = getReceiverRoleIds(notification, validSenderRoleIds,
				new HashSet<NotificationBusinessRule>());
		removeDeletedRoleIds(receiverRoleIds);

		return roleRepository.getUsersByRoleIds(receiverRoleIds);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public void getHistory(Long notificationProcessId) {
		String processInstanceId = notificationProcessRepo.getActivitiProcessIdByNPId(notificationProcessId);

		HistoricVariableInstance var = historyService.createHistoricVariableInstanceQuery()
				.variableName(NotificationProcessConstant.AUDIT_DATA).processInstanceId(processInstanceId)
				.singleResult();
		List<NotificationAuditData> auditDatas = (List<NotificationAuditData>) var.getValue();

		for (NotificationAuditData notificationAuditData : auditDatas) {
			System.out.println(notificationAuditData.getLevel());
			System.out.println(notificationAuditData.getActionTakenBy());
			if (notificationAuditData.getActionTakenDate() != null) {
				System.out.println(notificationAuditData.getActionTakenDate().getTime());
			}
			System.out.println(notificationAuditData.getCurrentStatus());
			System.out.println(notificationAuditData.getIsDelegated());
			System.out.println(notificationAuditData.getIsRestarted());
			System.out.println(notificationAuditData.getNotificationSentBy());
			System.out.println(notificationAuditData.getNotificationSentDate().getTime());
			System.out.println(Arrays.toString(notificationAuditData.getCurrentAssignedRoles().toArray()));
			System.out.println(Arrays.toString(notificationAuditData.getCurrentAssignedUsers().toArray()));
		}

		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).includeProcessVariables().singleResult();
		System.out.println("history process with definition id " + historicProcessInstance.getProcessDefinitionId()
				+ ", started at " + historicProcessInstance.getStartTime() + ", ended at "
				+ historicProcessInstance.getEndTime() + ", duration was "
				+ historicProcessInstance.getDurationInMillis());

		List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).list();
		for (HistoricActivityInstance historicActivityInstance : activityList) {
			System.out.println("history activity " + historicActivityInstance.getActivityName() + ", type "
					+ historicActivityInstance.getActivityType() + ", duration was "
					+ historicActivityInstance.getDurationInMillis());
		}

		List<HistoricDetail> historicVariableUpdateList = historyService.createHistoricDetailQuery()
				.processInstanceId(processInstanceId).variableUpdates().list();
		for (HistoricDetail historicDetail : historicVariableUpdateList) {
			HistoricVariableUpdate historicVariableUpdate = (HistoricVariableUpdate) historicDetail;
			System.out.println("historic variable update, revision " + historicVariableUpdate.getRevision()
					+ ", variable type name " + historicVariableUpdate.getVariableTypeName() + ", variable name "
					+ historicVariableUpdate.getVariableName() + ", Variable value '"
					+ historicVariableUpdate.getValue() + "'");
		}
	}
	
	@Override
	@Transactional
	public void completeTask(Long notificationProcessId, NotificationStatus notificationStatus) {
		String activitiProcessId = notificationProcessRepo.getActivitiProcessIdByNPId(notificationProcessId);
		runtimeService.setVariable(activitiProcessId, NotificationProcessConstant.NOTIFICATION_STATUS,
				notificationStatus);
		TaskQuery query = taskService.createTaskQuery().processInstanceId(activitiProcessId);
		Task theTask = query.singleResult();
		taskService.complete(theTask.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void responseNotificationInfo(Long notificationProcessId) {
		NotificationProcess notificationProcess = notificationProcessRepo.findOne(notificationProcessId);
		List<NotificationAuditData> auditDatas = (List<NotificationAuditData>) runtimeService
				.getVariable(notificationProcess.getActivitiProcessId(), NotificationProcessConstant.AUDIT_DATA);
		NotificationAuditData auditData = auditDatas.get(auditDatas.size() - 1);
		Long notificationSendBy = auditData.getLevelCompleteBy();
		int level = (auditData.getLevel() + 1);
		
		notificationProcess.setCurrentLevel(level);
		notificationProcess.setUpdatedByUser(BlackboxSecurityContextHolder.getUserId());
		notificationProcess.setUpdatedDate(Calendar.getInstance());
		notificationProcess.getUsers().clear();
		notificationProcess.getUsers().add(new User(notificationSendBy));
		notificationProcessRepo.save(notificationProcess);
		
		completeTask(notificationProcessId, NotificationStatus.RESPONSED);
		
		setRequestInfoNotification(notificationProcess.getNotification());
		String appNo = notificationProcess.getApplicationId() != null ? applicationBaseDAO.getApplicationBase(notificationProcess.getApplicationId()).getApplicationNumber() : null;
		List<Long> senderIds = new ArrayList<>();
		senderIds.add(notificationSendBy);
		emailService.sendEmailOnNotification(notificationProcess.getNotification(), senderIds, null, appNo);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<NotificationAuditData> getNotificationAuditData(Long notificationProcessId) {
		String processInstanceId = notificationProcessRepo.getActivitiProcessIdByNPId(notificationProcessId);

		HistoricVariableInstance var = historyService.createHistoricVariableInstanceQuery()
				.variableName(NotificationProcessConstant.AUDIT_DATA).processInstanceId(processInstanceId)
				.singleResult();
		
		return (List<NotificationAuditData>) var.getValue();
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isNotificationEligibleForDelegation(Long notificationProcessId) {
		boolean eligible = true;
		List<Long> assigneeUserIds = notificationProcessRepo.getAssigneeUserIdsByNotificationProcessId(notificationProcessId);
			
		if (CollectionUtils.isNotEmpty(assigneeUserIds) && assigneeUserIds.size() > 1) {
			eligible = false;
		}
		
		return eligible;
	}
	
	private void setErrorNotification(Notification notification) {
		notification.setSubject("Recipient not found");
		notification.setMessage("Recipient not found");
	}
	
	private void setRequestInfoNotification(Notification notification) {
		notification.setSubject("Need More Information");
		notification.setMessage("Need more information");
	}
}
