package com.blackbox.ids.core.dto.IDS.notification;

import java.util.Calendar;
import java.util.Date;

import com.mysema.query.annotations.QueryProjection;
import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dto.IDS.notification.NotificationBaseDTO;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.model.reference.OcrStatus;
import com.blackbox.ids.core.util.BlackboxUtils;
/**
 * The Class ReferenceDashboardDto.
 */
public class ReferenceEntryNotificationDTO extends NotificationBaseDTO{
	
	/** The correspondence id. */
	private long correspondenceId;

	/** The npl description. */
	private String docDescription;

	/** The reference base id. */
	private long ocrDataId;
	
	private Date mailingDate;
	
	private OcrStatus ocrStatus;
	
	/** The correspondence created date. */
	private Date correspondenceCreatedDate;

	/** The correspondence created by user. */
	private String correspondenceCreatedByUser;


	public ReferenceEntryNotificationDTO() {
		super();
	}

	@QueryProjection
	public ReferenceEntryNotificationDTO(final long notificationId, final long appdbId,final String jurisdictionCode,final String appNo,final long cId,
			final Calendar notifiedDate,final long ocrDataId,final String docDescription,final String message,
			final NotificationStatus status,final NotificationProcessType notificationName,final Calendar mailingDate,final OcrStatus ocrStatus,
			final Calendar createdDate,final String firstName,final String lastName) {
		super(notificationId,appdbId,jurisdictionCode,appNo,(notifiedDate==null ? null : notifiedDate.getTime()),message,status,notificationName);
		this.correspondenceId = cId;
		this.ocrDataId = ocrDataId;
		this.docDescription = docDescription;
		this.mailingDate = mailingDate == null ? null : mailingDate.getTime();
		this.ocrStatus = ocrStatus;
		this.correspondenceCreatedDate = createdDate==null ? null : createdDate.getTime();
		this.correspondenceCreatedByUser = BlackboxUtils.concat(firstName,Constant.SPACE_STRING,lastName);
	}

	/**
	 * Gets the correspondence id.
	 *
	 * @return the correspondence id
	 */
	public long getCorrespondenceId() {
		return correspondenceId;
	}

	/**
	 * Sets the correspondence id.
	 *
	 * @param correspondenceId
	 *            the new correspondence id
	 */
	public void setCorrespondenceId(long correspondenceId) {
		this.correspondenceId = correspondenceId;
	}

	public String getDocDescription() {
		return docDescription;
	}

	public void setDocDescription(String docDescription) {
		this.docDescription = docDescription;
	}

	public long getOcrDataId() {
		return ocrDataId;
	}

	public void setOcrDataId(long ocrDataId) {
		this.ocrDataId = ocrDataId;
	}

	public Date getMailingDate() {
		return mailingDate;
	}

	public void setMailingDate(Date mailingDate) {
		this.mailingDate = mailingDate;
	}

	public OcrStatus getOcrStatus() {
		return ocrStatus;
	}

	public void setOcrStatus(OcrStatus ocrStatus) {
		this.ocrStatus = ocrStatus;
	}

	public Date getCorrespondenceCreatedDate() {
		return correspondenceCreatedDate;
	}

	public void setCorrespondenceCreatedDate(Date correspondenceCreatedDate) {
		this.correspondenceCreatedDate = correspondenceCreatedDate;
	}

	public String getCorrespondenceCreatedByUser() {
		return correspondenceCreatedByUser;
	}

	public void setCorrespondenceCreatedByUser(String correspondenceCreatedByUser) {
		this.correspondenceCreatedByUser = correspondenceCreatedByUser;
	}
	
	
	
	
}
