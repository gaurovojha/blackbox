package com.blackbox.ids.services.notification.process;

public final class NotificationProcessConstant {
	
	public static final String NOTIFICATION = "NOTIFICATION";
	public static final String BUSINESS_RULES = "businessRules";
	public static final String ROLE_IDS = "roleIds";
	public static final String APPLICATION_ID = "applicationID";
	public static final String SYSTEM_INITIATED_NOTIFICATION = "Action Item - System Initiated";
	public static final String USER_INITIATED_NOTIFICATION = "Action Item - User Initiated";
	public static final String FYI_NOTIFICATION = "Action Item - FYI";
	public static final String FYA_NOTIFICATION = "Action Item - Approval";
	public static final String PROCESS_NAME = "blackboxNotification";
	public static final String NOTIFICATION_TYPE = "notificationType";
	public static final String STRING = "passTo";
	public static final String PASS_TO_LEVEL = "level";
	public static final String PASS_TO_ADMIN = "admin";
	public static final String PASS_TO_END = "end";
	public static final String PASS_TO = "passTo";
	public static final String TASK_CANDIDATE_GROUPS = "taskCandidateGroups";
	public static final String ESCALATION_CANDIDATE_GROUPS = "escalationCandidateGroups";
	public static final String SENDER = "Sender";
	public static final String RECEIVER = "Receiver";
	public static final String CURRENT_LEVEL = "currentLevel";
	public static final String ENTITY_ID = "entityId";
	public static final String ENTITY_NAME = "entityName";
	public static final String TOTAL_LEVEL = "totalLevel";
	public static final String TASK_UNLOCK_PERIOD = "taskUnlockPeriod";
	public static final String REMINDER_TASK_PERIOD = "reminderTaskPeriod";
	public static final String ESCALATION_TASK_PERIOD = "escalationTaskPeriod";
	public static final String AUDIT_DATA = "auditData";
	public static final String NOTIFICATION_STATUS = "notificationStatus";

	/** NotificationProcessConstant class. Not permitted to be instantiated.
     * @throws InstantiationException */
    private NotificationProcessConstant() throws InstantiationException {
    	throw new InstantiationException("Can't construct a static class instance.");
    }
}

