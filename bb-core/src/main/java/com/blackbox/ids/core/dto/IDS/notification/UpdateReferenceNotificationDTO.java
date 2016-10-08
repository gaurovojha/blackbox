package com.blackbox.ids.core.dto.IDS.notification;

import java.util.Calendar;

import com.mysema.query.annotations.QueryProjection;
import com.blackbox.ids.core.dto.IDS.notification.NotificationBaseDTO;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
/**
 * The Class ReferenceDashboardDto.
 */
public class UpdateReferenceNotificationDTO extends NotificationBaseDTO{
	
	private String refJurisdiction;
	
	/** The correspondence id. */
	private long correspondenceId;
	
	/** The document description. */
	private String docDescription;
	
	private String refPublicationNo;
	
	private long refStageId;
	
	
	public UpdateReferenceNotificationDTO() {
		super();
	}

	@QueryProjection
	public UpdateReferenceNotificationDTO(final long notificationId, final long appdbId,final String jurisdictionCode,final String appNo,final long cId,
			final Calendar notifiedDate,final long refStageId,final String docDescription,final String message,final NotificationStatus status, 
			final NotificationProcessType notificationName,final String refJurisdiction,final String refPublicationNo) {
		super(notificationId,appdbId,jurisdictionCode,appNo,(notifiedDate==null ? null : notifiedDate.getTime()),message,status,notificationName);
		this.correspondenceId = cId;
		this.docDescription = docDescription;
		this.refStageId = refStageId;
		this.refPublicationNo  = refPublicationNo;
		this.refJurisdiction = refJurisdiction;
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

	public String getRefJurisdiction() {
		return refJurisdiction;
	}

	public void setRefJurisdiction(String refJurisdiction) {
		this.refJurisdiction = refJurisdiction;
	}

	public String getRefPublicationNo() {
		return refPublicationNo;
	}

	public void setRefPublicationNo(String refPublicationNo) {
		this.refPublicationNo = refPublicationNo;
	}

	public long getRefStageId() {
		return refStageId;
	}

	public void setRefStageId(long refStageId) {
		this.refStageId = refStageId;
	}

	
}
