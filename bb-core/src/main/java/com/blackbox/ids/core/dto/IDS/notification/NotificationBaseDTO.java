package com.blackbox.ids.core.dto.IDS.notification;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.mysema.query.annotations.QueryProjection;

public class NotificationBaseDTO{
	
	private long appdbId;

	private String jurisdiction;

	private String applicationNo;

	private Date notifiedDate;

	private long notificationId;

	private long entityId ;
	
	private EntityName entityName; 
	
	private NotificationProcessType notificationName;
	
	private String message;
	
	private NotificationStatus status;
	
	private List<String> roles;
	/*No argument constructor*/

	public NotificationBaseDTO() {

	}
	
	@QueryProjection
	public NotificationBaseDTO(final long notificationId,  final long entityId, final NotificationProcessType notificationName, final String message, final Calendar notifiedDate
			,final EntityName entityName) {
		this.notificationId = notificationId;
		this.entityId = entityId;
		this.notificationName = notificationName;
		this.message = message;
		this.notifiedDate = notifiedDate!=null ? notifiedDate.getTime() :null;
		this.entityName = entityName;
	}
	

	public NotificationBaseDTO(final long notificationId, final long appdbId, final String jurisdiction,final String applicationNo , final Date notifiedDate, 
			final String message, final NotificationStatus status,NotificationProcessType notificationName) {
		this.notificationId  = notificationId;
		this.appdbId = appdbId;
		this.jurisdiction = jurisdiction;
		this.applicationNo = applicationNo;
		this.notifiedDate = notifiedDate;
		this.message = message;
		this.status = status;
		this.notificationName = notificationName;
	}
	
	
	public NotificationBaseDTO(final long notificationId,final String jurisdiction, final String applicationNo ,final Date notifiedDate,
			final String message, final NotificationStatus status,NotificationProcessType notificationName) {
		this.notificationId  = notificationId;
		this.jurisdiction = jurisdiction;
		this.applicationNo = applicationNo;
		this.notifiedDate = notifiedDate;
		this.message = message;
		this.status = status;
		this.notificationName = notificationName;
	}
	
	public NotificationBaseDTO(final long notificationId, final long appdbId, final String jurisdiction,final String applicationNo , final Date notifiedDate,
			final String message, final NotificationStatus status,final NotificationProcessType notificationName,final EntityName entityName) {
		this.notificationId  = notificationId;
		this.appdbId = appdbId;
		this.jurisdiction = jurisdiction;
		this.applicationNo = applicationNo;
		this.notifiedDate = notifiedDate;
		this.message = message;
		this.status = status;
		this.notificationName = notificationName;
		this.entityName = entityName;
	}

	/*- ----------------------------------- getter-setters -- */
	

	public long getAppdbId() {
		return appdbId;
	}

	public void setAppdbId(long appdbId) {
		this.appdbId = appdbId;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public Date getNotifiedDate() {
		return notifiedDate;
	}

	public void setNotifiedDate(Date notifiedDate) {
		this.notifiedDate = notifiedDate;
	}


	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
	
	public EntityName getEntityName() {
		return entityName;
	}

	public void setEntityName(EntityName entityName) {
		this.entityName = entityName;
	}

	public NotificationProcessType getNotificationName() {
		return notificationName;
	}

	public void setNotificationName(NotificationProcessType notificationName) {
		this.notificationName = notificationName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NotificationStatus getStatus() {
		return status;
	}

	public void setStatus(NotificationStatus status) {
		this.status = status;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
}
