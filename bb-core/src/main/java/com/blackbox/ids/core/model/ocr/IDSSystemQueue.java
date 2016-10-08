package com.blackbox.ids.core.model.ocr;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BB_IDS_SYSTEM_QUEUE")
public class IDSSystemQueue implements Serializable {

	private static final long serialVersionUID = 8375415678082971828L;

	public enum RequestType {

		FETCH_REFERENCE, FAMILY_VALIDATION;
	}

	public enum Source {
		SYSTEM, OCR, MANUAL;
	}

	public enum Status {
		NEW, ASSIGNED, ERROR_INVALID_INPUT, ERROR_OPS_FAILURE, COMPLETED, REVIEW_REQUIRED, REJECTED, NO_REFERENCE, LOCKED, ERROR;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_IDS_SYSTEM_QUEUE_ID", unique = true, nullable = false, length = 50)
	private Long id;

	@Column(name = "CLIENT_ID")
	private long clientId;

	@Column(name = "CLIENT_REQUEST_ID")
	private long clientRequestId;

	@Column(name = "REQUEST_ID")
	private long requestId;

	@Column(name = "CORRESPONDENCE_ID")
	private long correspondenceId;
	
	@Column(name = "STAGING_REFERENCE_ID")
	private long stagingRefereceId;

	@Column(name = "REQUEST_TYPE")
	@Enumerated(EnumType.STRING)
	private RequestType requestType;

	@Column(name = "SOURCE")
	@Enumerated(EnumType.STRING)
	private Source source;

	@Column(name = "JURISDICTION")
	private String jurisdiction;

	@Column(name = "APPLICATION_NUMBER")
	private String appNumber;

	@Column(name = "APPLICATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar applicationDate;

	@Column(name = "DOCUMENT_DESCRIPTION")
	private String documentDescription;

	@Column(name = "MAILING_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar mailingDate;

	@Column(name = "TEMPLATE_ID")
	private String templateId;

	@Column(name = "DOCUMENT_SECTION")
	private String documentSection;

	@Column(name = "PUBLICATION_NUMBER")
	private String publicationNumber;

	@Column(name = "PUBLICATION_CODE")
	private String publicationKindCode;

	@Column(name = "FAMILY_ID")
	private String familyId;

	@Column(name = "SYSTEM_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar systemTime;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private Status status;

	public Long getId() {
		return id;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public long getClientRequestId() {
		return clientRequestId;
	}

	public void setClientRequestId(long clientRequestId) {
		this.clientRequestId = clientRequestId;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}

	public Calendar getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Calendar applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getDocumentDescription() {
		return documentDescription;
	}

	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	public Calendar getMailingDate() {
		return mailingDate;
	}

	public void setMailingDate(Calendar mailingDate) {
		this.mailingDate = mailingDate;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getDocumentSection() {
		return documentSection;
	}

	public void setDocumentSection(String documentSection) {
		this.documentSection = documentSection;
	}

	public String getPublicationNumber() {
		return publicationNumber;
	}

	public void setPublicationNumber(String publicationNumber) {
		this.publicationNumber = publicationNumber;
	}

	public String getPublicationKindCode() {
		return publicationKindCode;
	}

	public void setPublicationKindCode(String publicationKindCode) {
		this.publicationKindCode = publicationKindCode;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public Calendar getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(Calendar systemTime) {
		this.systemTime = systemTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public long getCorrespondenceId() {
		return correspondenceId;
	}

	public void setCorrespondenceId(long correspondenceId) {
		this.correspondenceId = correspondenceId;
	}

	public long getStagingRefereceId() {
		return stagingRefereceId;
	}

	public void setStagingRefereceId(long stagingRefereceId) {
		this.stagingRefereceId = stagingRefereceId;
	}
}
