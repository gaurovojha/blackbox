package com.blackbox.ids.core.dto.correspondence.dashboard;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase.OCRStatus;
import com.blackbox.ids.core.model.notification.process.NotificationProcess;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.mysema.query.annotations.QueryProjection;

/**
 * @author shikhagupta
 */
public class CorrespondenceRecordDTO implements Serializable {

	private static final long serialVersionUID = -2559246704966275509L;

	private static final String	US_NATIONAL				= "USNational";
	private static final String	US_GREEN_CARD_HOLDER	= "USGreenCardHolder";

	private Long					dbId;
	private Long					appdbId;
	private String					jurisdictionCode;
	private String					applicationNumber;
	private String					mailingDate;
	private String					documentDescription;
	private String					uploadedOn;
	private String					uploadedByUser;
	private Long					uploadedBy;
	private boolean					locked;
	private String					documentLocation;
	private String					lockedBy;
	private transient Status		status;
	private String					comments;
	private String					createdOn;
	private Boolean					exportControl;
	private String					nationality;
	private Boolean					viewDocumentLink;
	private String					createdDate;
	private String					updatedDate;
	private String					ocrStatus;
	private transient MultipartFile	file;
	private String					requester;
	private String					approver;
	private String					notificationStatus;
	private Long					updatedBy;
	private String					createdByName;
	private String					updatedByName;
	private String					notifiedDate;
	private String					notificationEndDate;

	public CorrespondenceRecordDTO()
	{

	}

	// Query Projection for Active Documents
	@QueryProjection
	public CorrespondenceRecordDTO(Long dbId, Long appdbId, String jurisdictionCode, String applicationNumber,
			Calendar mailingDate, String documentDescription, Long uploadedBy, Status status, String firstName,
			String lastName, String comments, Calendar createdOn, Boolean exportControl, String nationality)
	{
		super();
		this.dbId = dbId;
		this.appdbId = appdbId;
		this.jurisdictionCode = jurisdictionCode;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate != null
				? BlackboxDateUtil.dateToStr(mailingDate.getTime(), TimestampFormat.MMMDDYYYY) : null;
		this.documentDescription = documentDescription;
		this.createdByName = BlackboxUtils.concat(firstName, Constant.SPACE_STRING, lastName);
		this.uploadedBy = uploadedBy;
		this.status = status;
		this.comments = comments != null ? comments : null;
		this.createdOn = createdOn != null ? BlackboxDateUtil.dateToStr(createdOn.getTime(), TimestampFormat.MMMDDYYYY)
				: null;
		this.exportControl = exportControl;
		this.nationality = nationality;
		if (this.exportControl && (US_NATIONAL.equalsIgnoreCase(StringUtils.trimAllWhitespace(this.nationality))
				|| US_GREEN_CARD_HOLDER.equalsIgnoreCase(StringUtils.trimAllWhitespace(this.nationality)))) {
			this.viewDocumentLink = true;
		} else if (!this.exportControl) {
			this.viewDocumentLink = true;
		} else {
			this.viewDocumentLink = false;
		}
	}

	// Query Projection for Inactive Documents
	@QueryProjection
	public CorrespondenceRecordDTO(Long dbId, Long appdbId, String jurisdictionCode, String applicationNumber,
			Calendar mailingDate, String documentDescription, Status status, String firstName, String lastName,
			String comments, Calendar uploadedOn, Boolean exportControl, String nationality)
	{
		super();
		this.dbId = dbId;
		this.appdbId = appdbId;
		this.jurisdictionCode = jurisdictionCode;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate != null
				? BlackboxDateUtil.dateToStr(mailingDate.getTime(), TimestampFormat.MMMDDYYYY) : null;
		this.documentDescription = documentDescription;
		this.updatedByName = BlackboxUtils.concat(firstName, Constant.SPACE_STRING, lastName);
		this.status = status;
		this.comments = comments != null ? comments : null;
		this.uploadedOn = uploadedOn != null
				? BlackboxDateUtil.dateToStr(uploadedOn.getTime(), TimestampFormat.MMMDDYYYY) : null;
		this.exportControl = exportControl;
		this.nationality = nationality;
		if (this.exportControl && (US_NATIONAL.equalsIgnoreCase(StringUtils.trimAllWhitespace(this.nationality))
				|| US_GREEN_CARD_HOLDER.equalsIgnoreCase(StringUtils.trimAllWhitespace(this.nationality)))) {
			this.viewDocumentLink = true;
		} else if (!this.exportControl) {
			this.viewDocumentLink = true;
		} else {
			this.viewDocumentLink = false;
		}
	}

	@QueryProjection
	public CorrespondenceRecordDTO(Long dbId, String jurisdictionCode, String applicationNumber, Calendar mailingDate,
			String documentDescription, Calendar updatedDate, OCRStatus ocrStatus)
	{
		super();
		this.dbId = dbId;
		this.jurisdictionCode = jurisdictionCode;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate != null
				? BlackboxDateUtil.dateToStr(mailingDate.getTime(), TimestampFormat.MMMDDYYYY) : null;
		this.documentDescription = documentDescription;

		this.updatedDate = updatedDate != null
				? BlackboxDateUtil.dateToStr(updatedDate.getTime(), TimestampFormat.MMMDDYYYY) : null;
		this.ocrStatus = ocrStatus.name();
		this.documentLocation = "/documents/correspondence/createapplication/OCRFontsBoldItalic.pdf";
	}

	// Query Projection for View Document Documents
	@QueryProjection
	public CorrespondenceRecordDTO(Long dbId, Long appdbId, String jurisdictionCode, String applicationNumber,
			Calendar mailingDate, String documentDescription, Boolean exportControl, String nationality)
	{
		super();
		this.dbId = dbId;
		this.appdbId = appdbId;
		this.jurisdictionCode = jurisdictionCode;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate != null
				? BlackboxDateUtil.dateToStr(mailingDate.getTime(), TimestampFormat.MMMDDYYYY) : null;
		this.documentDescription = documentDescription;
		this.exportControl = exportControl;
		this.nationality = nationality;
		if (this.exportControl && (US_NATIONAL.equalsIgnoreCase(StringUtils.trimAllWhitespace(this.nationality))
				|| US_GREEN_CARD_HOLDER.equalsIgnoreCase(StringUtils.trimAllWhitespace(this.nationality)))) {
			this.viewDocumentLink = true;
		} else if (!this.exportControl) {
			this.viewDocumentLink = true;
		} else {
			this.viewDocumentLink = false;
		}
	}

	@QueryProjection
	public CorrespondenceRecordDTO(Long dbId, String jurisdictionCode, String applicationNumber, Calendar mailingDate,
			String documentDescription)
	{
		super();
		this.dbId = dbId;
		this.jurisdictionCode = jurisdictionCode;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate != null
				? BlackboxDateUtil.dateToStr(mailingDate.getTime(), TimestampFormat.MMMDDYYYY) : null;
		this.documentDescription = documentDescription;
	}

	@QueryProjection
	public CorrespondenceRecordDTO(Long dbId, String jurisdictionCode, String applicationNumber, Calendar mailingDate,
			String documentDescription, Calendar updatedDate)
	{
		super();
		this.dbId = dbId;
		this.jurisdictionCode = jurisdictionCode;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate != null
				? BlackboxDateUtil.dateToStr(mailingDate.getTime(), TimestampFormat.MMMDDYYYY) : null;
		this.documentDescription = documentDescription;
		this.updatedDate = updatedDate != null
				? BlackboxDateUtil.dateToStr(updatedDate.getTime(), TimestampFormat.MMMDDYYYY) : null;
	}

	@QueryProjection
	public CorrespondenceRecordDTO(Long dbId, String jurisdictionCode, String applicationNumber, Calendar mailingDate,
			String documentDescription, Status status, User createdUser, NotificationProcess notificationProcess)
	{
		super();
		this.dbId = dbId;
		this.jurisdictionCode = jurisdictionCode;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate != null
				? BlackboxDateUtil.dateToStr(mailingDate.getTime(), TimestampFormat.MMMDDYYYY) : null;
		this.documentDescription = documentDescription;
		Calendar notifiedDate = notificationProcess.getNotifiedDate();
		Calendar updatedDate = notificationProcess.getUpdatedDate();
		if (createdUser != null) {
			this.createdByName = createdUser.getPerson().getFirstName() + " " + createdUser.getPerson().getLastName();
			this.notifiedDate = notifiedDate != null
					? BlackboxDateUtil.dateToStr(notifiedDate.getTime(), TimestampFormat.MMMDDYYYY) : "";
		}
		if (status == Status.CREATE_APPLICATION_REQUEST_PENDING) {
			this.updatedBy = null;
			this.approver = getRoles(notificationProcess.getRoles());
			this.notificationStatus = Constant.PENDING;
			this.notificationEndDate = null;
		} else if (status == Status.CREATE_APPLICATION_REQUEST_APPROVED
				|| status == Status.CREATE_APPLICATION_REQUEST_REJECTED) {
			this.updatedBy = notificationProcess.getUpdatedByUser();
			if (status == Status.CREATE_APPLICATION_REQUEST_APPROVED) {
				this.notificationStatus = Constant.APPROVED;
			} else {
				this.notificationStatus = Constant.REJECTED;
			}
			this.notificationEndDate = updatedDate != null
					? BlackboxDateUtil.dateToStr(updatedDate.getTime(), TimestampFormat.MMMDDYYYY) : "";
		}
	}

	private String getRoles(List<Role> roles) {
		StringBuffer stringBuffer = new StringBuffer();
		if (!CollectionUtils.isEmpty(roles)) {
			for (Role role : roles) {
				stringBuffer.append(role.getName()).append(" ");
			}
		}
		return stringBuffer.toString();
	}

	public Long getDbId() {
		return dbId;
	}

	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}

	public String getJurisdictionCode() {
		return jurisdictionCode;
	}

	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getDocumentDescription() {
		return documentDescription;
	}

	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	public Long getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(Long uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getDocumentLocation() {
		return documentLocation;
	}

	public void setDocumentLocation(String documentLocation) {
		this.documentLocation = documentLocation;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public String getMailingDate() {
		return mailingDate;
	}

	public void setMailingDate(String mailingDate) {
		this.mailingDate = mailingDate;
	}

	public String getUploadedOn() {
		return uploadedOn;
	}

	public void setUploadedOn(String uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public Boolean getExportControl() {
		return exportControl;
	}

	public void setExportControl(Boolean exportControl) {
		this.exportControl = exportControl;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Boolean getViewDocumentLink() {
		return viewDocumentLink;
	}

	public void setViewDocumentLink(Boolean viewDocumentLink) {
		this.viewDocumentLink = viewDocumentLink;
	}

	public String getUploadedByUser() {
		return uploadedByUser;
	}

	public void setUploadedByUser(String uploadedByUser) {
		this.uploadedByUser = uploadedByUser;
	}

	public Long getAppdbId() {
		return appdbId;
	}

	public void setAppdbId(Long appdbId) {
		this.appdbId = appdbId;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getOcrStatus() {
		return ocrStatus;
	}

	public void setOcrStatus(String ocrStatus) {
		this.ocrStatus = ocrStatus;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(String notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getUpdatedByName() {
		return updatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	public String getNotifiedDate() {
		return notifiedDate;
	}

	public void setNotifiedDate(String notifiedDate) {
		this.notifiedDate = notifiedDate;
	}

	public String getNotificationEndDate() {
		return notificationEndDate;
	}

	public void setNotificationEndDate(String notificationEndDate) {
		this.notificationEndDate = notificationEndDate;
	}
}
