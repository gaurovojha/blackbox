package com.blackbox.ids.core.model.correspondence;

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

import com.blackbox.ids.core.model.enums.PairAuditStatusEnum;

/**
 * The Class PairAudit.
 */
@Entity
@Table(name = "BB_PAIR_AUDIT")
public class PairAudit {
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_PAIR_AUDIT_ID")
	private Long id;
	
	/** The audit date. */
	@Column(name = "AUDIT_DATE", nullable = false)
	private Calendar auditDate;
	
	/** The performed by. */
	@Column(name = "PERFORMED_BY", nullable = false)
	private Long performedBy;
		
	/** The jurisdiction. */
	@Column(name = "JURISDICTION", nullable = false)
	private String jurisdiction;	
	
	/** The application number. */
	@Column(name = "APPLICATION_NUMBER",nullable = false)
	private String applicationNumber;
		
	/** The mailing date. */
	@Column(name = "MAILING_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar mailingDate;
	
	/** The document code. */
	@Column(name = "DOCUMENT_CODE",nullable = false)
	private String documentCode;

	/** The number of pages. */
	@Column(name = "NUMBER_OF_PAGES",nullable = false)
	private Integer numberOfPages;
	
	/** The status of record. */
	@Column(name = "STATUS_OF_RECORD",nullable = false)
	@Enumerated(EnumType.STRING)
	private PairAuditStatusEnum statusOfRecord;

	
	/**
	 * Instantiates a new pair audit.
	 */
	public PairAudit() {
		super();
	}

	/**
	 * Instantiates a new pair audit.
	 *
	 * @param auditDate the audit date
	 * @param performedBy the performed by
	 * @param jurisdiction the jurisdiction
	 * @param applicationNumber the application number
	 * @param mailingDate the mailing date
	 * @param documentCode the document code
	 * @param numberOfPages the number of pages
	 * @param statusOfRecord the status of record
	 */
	public PairAudit(Calendar auditDate, Long performedBy, String jurisdiction, String applicationNumber,
			Calendar mailingDate, String documentCode, Integer numberOfPages, PairAuditStatusEnum statusOfRecord) {
		super();
		this.auditDate = auditDate;
		this.performedBy = performedBy;
		this.jurisdiction = jurisdiction;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate;
		this.documentCode = documentCode;
		this.numberOfPages = numberOfPages;
		this.statusOfRecord = statusOfRecord;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the jurisdiction.
	 *
	 * @return the jurisdiction
	 */
	public String getJurisdiction() {
		return jurisdiction;
	}

	/**
	 * Sets the jurisdiction.
	 *
	 * @param jurisdiction the new jurisdiction
	 */
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	/**
	 * Gets the application number.
	 *
	 * @return the application number
	 */
	public String getApplicationNumber() {
		return applicationNumber;
	}

	/**
	 * Sets the application number.
	 *
	 * @param applicationNumber the new application number
	 */
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	/**
	 * Gets the document code.
	 *
	 * @return the document code
	 */
	public String getDocumentCode() {
		return documentCode;
	}

	/**
	 * Sets the document code.
	 *
	 * @param documentCode the new document code
	 */
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	/**
	 * Gets the number of pages.
	 *
	 * @return the number of pages
	 */
	public Integer getNumberOfPages() {
		return numberOfPages;
	}

	/**
	 * Sets the number of pages.
	 *
	 * @param numberOfPages the new number of pages
	 */
	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	/**
	 * Gets the audit date.
	 *
	 * @return the audit date
	 */
	public Calendar getAuditDate() {
		return auditDate;
	}

	/**
	 * Sets the audit date.
	 *
	 * @param auditDate the new audit date
	 */
	public void setAuditDate(Calendar auditDate) {
		this.auditDate = auditDate;
	}

	/**
	 * Gets the mailing date.
	 *
	 * @return the mailing date
	 */
	public Calendar getMailingDate() {
		return mailingDate;
	}

	/**
	 * Sets the mailing date.
	 *
	 * @param mailingDate the new mailing date
	 */
	public void setMailingDate(Calendar mailingDate) {
		this.mailingDate = mailingDate;
	}

	/**
	 * Gets the performed by.
	 *
	 * @return the performed by
	 */
	public Long getPerformedBy() {
		return performedBy;
	}

	/**
	 * Sets the performed by.
	 *
	 * @param performedBy the new performed by
	 */
	public void setPerformedBy(Long performedBy) {
		this.performedBy = performedBy;
	}

	/**
	 * Gets the status of record.
	 *
	 * @return the status of record
	 */
	public PairAuditStatusEnum getStatusOfRecord() {
		return statusOfRecord;
	}

	/**
	 * Sets the status of record.
	 *
	 * @param statusOfRecord the new status of record
	 */
	public void setStatusOfRecord(PairAuditStatusEnum statusOfRecord) {
		this.statusOfRecord = statusOfRecord;
	}

}
