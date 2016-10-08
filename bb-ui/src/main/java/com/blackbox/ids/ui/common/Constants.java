package com.blackbox.ids.ui.common;

import org.springframework.context.MessageSource;

public interface Constants {

	String APPLICATION_LOCK_ACQUIRED = "applicationLockAcquired";

	String TASK = "TASK";
	String APP_NUMBER = "APN_NUMBER";
	String APP_DESCR = "APP_DESCR";
	String APPLICATION = "APPLICATION";
	String TASKVISE_ITEM_MAP = "TASKVISE_ITEM_MAP";
	String TASK_LIST = "TASK_LIST";
	String USER_ID = "userId";
	String OLD_PASSWORD = "oldPassword";
	String PASSWORD = "password";
	String NEW_PASSWORD = "newPassword";
	String CONFIRM_PASSWORD = "confirmPassword";
	String MESSAGE = "message";
	String OTP = "otp";
	String ERROR = "error";
	String USERNAME = "username";
	String USERFULLNAME = "userFullName";
	String PARTIALLY_AUTHENTICATED = "Partially Authenticated";
	String FULLY_AUTHENTICATED = "Fully Authenticated";
	String	LDAP_ENABLED_FLAG = "ldapFlag";

	/*- ----------------------------------- UI Specific Constants -- */
	 /** Bean name for the {@link MessageSource} implementation class. */
    String BEAN_MESSAGE_SOURCE = "messageSource";

    String JURISDICTION_US = "US";
	String JURIDSCITION_NON_US = "Non-US";

	String SPECIFIER_ASCENDING = "ASC";
	String LIST_JURISDICTIONS = "listJurisdictions";
	String LIST_APPLICATION_TYPES = "listApplicationTypes";
	String LIST_APPLICATION_TYPES_US = "listApplicationTypes_US";
	String LIST_APPLICATION_TYPES_WO = "listApplicationTypes_WO";
	String LIST_APPLICATION_TYPES_OTHER = "listApplicationTypes_Other";
	String LIST_APPLICATION_TYPES_UPDATE_FAMILY_LINKAGE = "listApplicationTypes_FamilyLinkage";
	String LIST_ASSIGNEE = "listAssignee";
	String LIST_ENTITIY = "listEntity";
	String LIST_CUSTOMER = "listCustomer";

	String CONSTANT_CORRESPONDENCE_DASHBOARD_TAB = "DASHBOARD";

	String CONSTANT_CORRESPONDENCE_ACTION_ITEM_TAB = "ACTION ITEM";

	String CONSTANT_CORRESPONDENCE_TRACK_APPLICATION_TAB = "TRACK APPLICATION";

	String CORRESPONDENCE_ACTION_ITEM_COUNT = "actionItemCount";

	String CORRESPONDENCE_UPDATE_REQUEST_COUNT = "updateRequestCount";

	String CORRESPONDENCE_UPLOAD_REQUEST_COUNT = "uploadRequestCount";

	String CLOSE_NOTIFICATION = "closeNotification";
	String REDIRECT_TO_DASHBOARD = "redirectToDashboard";
	String KEY_ID = "id";
	String CORRESPONDENCE_ID = "correspondence_id";

}