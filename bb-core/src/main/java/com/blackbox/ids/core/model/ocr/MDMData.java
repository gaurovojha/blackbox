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

import com.blackbox.ids.core.model.mstr.Assignee;

@Entity
@Table(name = "BB_MDM_DATA")
public class MDMData implements Serializable {

	private static final long	serialVersionUID	= -3687439637154430101L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_MDM_DATA_ID", unique = true, nullable = false, length = 50)
	private Long				id;

	@Column(name = "DOC_ID", nullable = false)
	private String				docId;

	@Column(name = "JURISDICTION")
	private String				jurisdiction;

	@Column(name = "DOCUMENT_CODE")
	private String				docCode;

	@Column(name = "TEMPLATE_ID")
	private String				templateId;

	@Column(name = "APPLICATION_NUMBER")
	private String				appNumber;

	@Column(name = "FILING_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar			filingDate;

	@Column(name = "FIRST_NAME_INVENTOR")
	private String				inventorName;

	@Column(name = "ATTRONEY_DOCKET_NUMBER")
	private String				attorneyDocketNumber;

	@Column(name = "CONFIRMATION_NUMBER")
	private String				confirmationNumber;

	@Column(name = "EXAMINER_NAME")
	private String				examinerName;

	@Column(name = "ART_UNIT")
	private String				artUnit;

	@Column(name = "MAILING_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar			mailingDate;

	@Column(name = "CUSTOMER_NUMBER")
	private String				customerNumber;

	@Column(name = "ENTITY")
	@Enumerated(EnumType.STRING)
	private Assignee.Entity		entity;

	@Column(name = "TITLE")
	private String				title;

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getDocCode() {
		return docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}

	public Calendar getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(Calendar filingDate) {
		this.filingDate = filingDate;
	}

	public String getInventorName() {
		return inventorName;
	}

	public void setInventorName(String inventorName) {
		this.inventorName = inventorName;
	}

	public String getAttorneyDocketNumber() {
		return attorneyDocketNumber;
	}

	public void setAttorneyDocketNumber(String attorneyDocketNumber) {
		this.attorneyDocketNumber = attorneyDocketNumber;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getExaminerName() {
		return examinerName;
	}

	public void setExaminerName(String examinerName) {
		this.examinerName = examinerName;
	}

	public String getArtUnit() {
		return artUnit;
	}

	public void setArtUnit(String artUnit) {
		this.artUnit = artUnit;
	}

	public Calendar getMailingDate() {
		return mailingDate;
	}

	public void setMailingDate(Calendar mailingDate) {
		this.mailingDate = mailingDate;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public Assignee.Entity getEntity() {
		return entity;
	}

	public void setEntity(Assignee.Entity entity) {
		this.entity = entity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getId() {
		return id;
	}
}
