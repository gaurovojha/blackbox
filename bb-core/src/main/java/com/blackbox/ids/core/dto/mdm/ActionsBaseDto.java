package com.blackbox.ids.core.dto.mdm;


import java.util.Calendar;
import java.util.Date;

import com.mysema.query.annotations.QueryProjection;

public class ActionsBaseDto {

	private Long dbId;

	private String jurisdiction;

	private String applicationNo;

	private Date notifiedDate;

	private long notificationId;

	private long entityId ;
	
	private String entityName; 

	/*No argument constructor*/

	public ActionsBaseDto() {

	}

	@QueryProjection
	public ActionsBaseDto(final String jurisdiction,final String applicationNo,final Calendar notifiedDate,final long notificationId,  final long entityId,final String entityName) {

		
		this.jurisdiction = jurisdiction;
		this.applicationNo = applicationNo;
		this.notifiedDate = notifiedDate.getTime();
		this.notificationId = notificationId;
		this.entityId = entityId;
		this.entityName = entityName;
	}

	@QueryProjection
	public ActionsBaseDto(final String jurisdiction,final String applicationNo , final long notificationId,  final long entityId) {

		this.jurisdiction = jurisdiction;
		this.applicationNo = applicationNo;
		this.notificationId = notificationId;
		this.entityId = entityId;
	}

	/*- ----------------------------------- getter-setters -- */

	public Long getDbId() {
		return dbId;
	}

	public void setDbId(Long dbId) {
		this.dbId = dbId;
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

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
}
