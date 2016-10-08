package com.blackbox.ids.core.dto.IDS.notification;

import java.util.Calendar;
import java.util.Date;

import com.mysema.query.annotations.QueryProjection;
import com.blackbox.ids.core.dto.IDS.notification.NotificationBaseDTO;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
/**
 * The Class ReferenceDashboardDto.
 */
public class UploadDocumentNotificationDTO extends NotificationBaseDTO{
	
	/** The npl description. */
	private String docDescription;

	private Date mailingDate;
	
	private long downloadOfficeActionId;

	public UploadDocumentNotificationDTO() {
		super();
	}

	@QueryProjection
	public UploadDocumentNotificationDTO(final long notificationId,final String jurisdictionCode,final String appNo,
			final Calendar notifiedDate,final String docDescription,final String message,
			final NotificationStatus status,final NotificationProcessType notificationName,final Calendar mailingDate, final long downloadOfficeActionId) {
		
		super(notificationId,jurisdictionCode,appNo,(notifiedDate==null ? null : notifiedDate.getTime()),message,status,notificationName);
		this.docDescription = docDescription;
		this.mailingDate = mailingDate == null ? null : mailingDate.getTime();
		this.downloadOfficeActionId = downloadOfficeActionId;
	}

	public String getDocDescription() {
		return docDescription;
	}

	public void setDocDescription(String docDescription) {
		this.docDescription = docDescription;
	}

	public Date getMailingDate() {
		return mailingDate;
	}

	public void setMailingDate(Date mailingDate) {
		this.mailingDate = mailingDate;
	}

	public long getDownloadOfficeActionId() {
		return downloadOfficeActionId;
	}

	public void setDownloadOfficeActionId(long downloadOfficeActionId) {
		this.downloadOfficeActionId = downloadOfficeActionId;
	}	
	
}
