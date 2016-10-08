package com.blackbox.ids.core.dto.IDS.notification;

import java.util.Calendar;

import com.mysema.query.annotations.QueryProjection;
import com.blackbox.ids.core.dto.IDS.notification.NotificationBaseDTO;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
/**
 * The Class ReferenceDashboardDto.
 */
public class NPLDuplicateCheckNotificationDTO extends NotificationBaseDTO{

	/** The correspondence id. */
	private long correspondenceId;

	/** The npl description. */
	private String nplDescription;

	/** The reference base id. */
	private long referenceBaseId;
	
	/** The document description. */
	private String docDescription;
	
	private String familyId;

	public NPLDuplicateCheckNotificationDTO() {
		super();
	}

	@QueryProjection
	public NPLDuplicateCheckNotificationDTO(final long notificationId, final long appdbId,final String jurisdictionCode,
			final String appNo, long cId,
			final Calendar notifiedDate,final long refBaseId,final String nplDescription,final String message,final NotificationStatus status, 
			final NotificationProcessType notificationName,final String docDescription, final String familyId) {
		super(notificationId,appdbId,jurisdictionCode,appNo,(notifiedDate==null ? null : notifiedDate.getTime()),message,status,notificationName);
		this.correspondenceId = cId;
		this.referenceBaseId = refBaseId;
		this.nplDescription = nplDescription;
		this.docDescription = docDescription;
		this.familyId = familyId;
	}


	
	/**
	 * Gets the npl description.
	 *
	 * @return the npl description
	 */
	public String getNplDescription() {
		return nplDescription;
	}

	/**
	 * Sets the npl description.
	 *
	 * @param nplDescription
	 *            the new npl description
	 */
	public void setNplDescription(String nplDescription) {
		this.nplDescription = nplDescription;
	}

	/**
	 * Gets the reference base id.
	 *
	 * @return the reference base id
	 */
	public long getReferenceBaseId() {
		return referenceBaseId;
	}

	/**
	 * Sets the reference base id.
	 *
	 * @param referenceBaseId
	 *            the new reference base id
	 */
	public void setReferenceBaseId(long referenceBaseId) {
		this.referenceBaseId = referenceBaseId;
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

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}
	
}
