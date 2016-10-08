package com.blackbox.ids.core.model.notification.process;

import java.util.ArrayList;
import java.util.List;

/**
 * Notification Process Type.
 *
 * @author nagarro
 */
public enum NotificationProcessType {

	/** The npl duplicate check. */
	NPL_DUPLICATE_CHECK("NplDuplicationCheck", 1),

	/** The reference manual entry. */
	REFERENCE_MANUAL_ENTRY("ReferenceManualEntry", 2),

	/** The inpadoc failed. */
	INPADOC_FAILED("InpadocFailed", 3),

	/** The correspondence record failed in download queue. */
	CORRESPONDENCE_RECORD_FAILED_IN_DOWNLOAD_QUEUE("CorrespondenceRecordFailedInDownloadQueue", 4),

	/** The create application request. */
	CREATE_APPLICATION_REQUEST("CreateApplicationRequest", 5),

	/** The dashboard action status. */
	DASHBOARD_ACTION_STATUS("Change Record Status", 6),

	/** The update family linkage. */
	UPDATE_FAMILY_LINKAGE("UpdateFamilyLinkage", 7),

	/** The create family member. */
	CREATE_FAMILY_MEMBER("CreateFamilyMember", 8),

	/** The update assignee. */
	UPDATE_ASSIGNEE("UpdateAssignee", 9),

	/** The create sml. */
	CREATE_SML("CreateSML", 10),

	/** The update assignee. */
	UPDATE_IDS_STATUS("UpdateIDSStatus", 11),
	
	/** The create application request. */
	CREATE_APPLICATION_REQUEST_USER_INITIATED("CreateApplicationRequestUserInitiated", 12),

	/**
	 * IDS file has been generated but the system is not able to download the IDS from IFW.
	 */
	IDS_DOWNLOAD_FAILED("IdsDownloadFailed" , 13),
	
	/**
	 * Validate reference status when IDS is WITHDRAWN
	 */
	VALIDATE_REFERENCE_STATUS("ValidateReferenceStatus", 14),
	
	/**
	 * Upload manually filed ids in the system
	 */	
	UPLOAD_MANUALLY_FILED_IDS("UploadManuallyFiledIds", 15),
	
	/** Request for IDS approval to attorney. */
	IDS_APPROVAL_REQUEST("IDSApprovalRequest", 16),
	
	/** 1449 Notification */
	N1449("N1449",17),
	
	/** Request for changes in IDS to Parallegal. */
	REQUEST_IDS_CHANGE("Request for changes in IDS to parallegal by attorney",18),
	
	/** Track IDS filing after IDS file generation for manual channel. */
	TRACK_IDS_FILING("TrackIDSFiling", 19);

	/** The parameter type. */
	private String notificationProcessType;

	/** The id. */
	private int id;

	/**
	 * This is used as a constructor.
	 *
	 * @param notificationProcessType
	 *            the notification process type
	 * @param id
	 *            id
	 */
	private NotificationProcessType(final String notificationProcessType, final int id) {
		this.notificationProcessType = notificationProcessType;
		this.id = id;
	}

	/**
	 * Gets the parameter type list.
	 *
	 * @return the parameter type list
	 */
	public List<String> getNotificationProcessTypeList() {
		List<String> parameterTypeList = new ArrayList<String>();
		NotificationProcessType[] parameterTypes = NotificationProcessType.values();

		for (NotificationProcessType parameterType : parameterTypes) {
			parameterTypeList.add(parameterType.getNotificationProcessType());
		}

		return parameterTypeList;
	}

	/**
	 * Gets the parameter type on id.
	 *
	 * @param id
	 *            the id
	 * @return the parameter type on id
	 */
	public static NotificationProcessType getNotificationProcessTypeById(final int id) {
		NotificationProcessType[] types = NotificationProcessType.values();
		NotificationProcessType notProType = null;

		for (NotificationProcessType notificationProcessType : types) {
			if (notificationProcessType.getId() == id) {
				notProType = notificationProcessType;
				break;
			}
		}

		return notProType;
	}

	/**
	 * Gets the notification process type by name.
	 *
	 * @param name
	 *            the name
	 * @return the notification process type by name
	 */
	public static NotificationProcessType getNotificationProcessTypeByName(final String name) {
		NotificationProcessType[] types = NotificationProcessType.values();
		NotificationProcessType notiProcessType = null;

		for (NotificationProcessType notificationProcessType : types) {
			if (notificationProcessType.getNotificationProcessType().equalsIgnoreCase(name)) {
				notiProcessType = notificationProcessType;
				break;
			}
		}

		return notiProcessType;
	}

	/**
	 * Gets the notification process type.
	 *
	 * @return the notification process type
	 */
	public String getNotificationProcessType() {
		return notificationProcessType;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
}
