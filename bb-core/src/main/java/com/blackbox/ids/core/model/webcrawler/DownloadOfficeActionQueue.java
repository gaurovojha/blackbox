package com.blackbox.ids.core.model.webcrawler;

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

import com.blackbox.ids.core.model.enums.QueueStatus;

// TODO: Auto-generated Javadoc
/**
 * The Class DownloadOfficeActionQueue.
 */
@Entity
@Table(name = "BB_SCR_DOWNLOAD_OFFICE_ACTION")
public class DownloadOfficeActionQueue extends BaseQueueEntity {


	/** The serial version UID. */
	private static final long serialVersionUID = 6242286903148655293L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	/** The mailing date. Mailing date null refers to all dates.*/
	@Column(name = "MAILING_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar mailingDate;

	/** The correspondence code. */
	@Column(name = "DOCUMENT_CODE")
	private String documentCode;

	/** The retry count. */
	@Column(name = "RETRY_COUNT")
	private int retryCount = 1;

	/** The status. */
	@Column(name  = "STATUS")
	@Enumerated(EnumType.STRING)
	private QueueStatus status = QueueStatus.INITIATED;

	/** The filing date. */
	@Column(name = "FILING_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar filingDate;

	/** The page count. */
	@Column(name = "ATTACHMENT_PAGE_COUNT")
	private Integer pageCount;


	/**
	 * Instantiates a new download office action queue.
	 *
	 * @param applicationNumber the application number
	 * @param customerNumber the customer number
	 * @param jurisdictionCode the jurisdiction code
	 * @param createdDate the created date
	 * @param createdByUser the created by user
	 * @param updatedDate the updated date
	 * @param updatedByUser the updated by user
	 * @param applicationNumberRaw the application number raw
	 * @param mailingDate the mailing date
	 * @param correspondenceCode the correspondence code
	 * @param retryCount the retry count
	 * @param status the status
	 * @param filingDate the filing date
	 * @param pageCount the page count
	 */
	public DownloadOfficeActionQueue(String appNumberRaw, String applicationNumber, String customerNumber,
			String jurisdictionCode, Calendar createdDate, Long createdByUser, Calendar updatedDate, Long updatedByUser,
			Calendar mailingDate, String correspondenceCode, int retryCount,
			QueueStatus status, Calendar filingDate, Integer pageCount) {

		super(appNumberRaw, applicationNumber, customerNumber, jurisdictionCode, createdDate, createdByUser,
				updatedDate, updatedByUser);
		this.mailingDate = mailingDate;
		this.documentCode = correspondenceCode;
		this.retryCount = retryCount;
		this.status = status;
		this.filingDate = filingDate;
		this.pageCount = pageCount;
	}

	/**
	 * Instantiates a new download office action queue.
	 */
	public DownloadOfficeActionQueue() {
		super();
	}

	public DownloadOfficeActionQueue(String jurisdiction, String applicationNumber, String appNumberRaw,
			String customerNumber,
			Calendar mailingDate, String documentCode) {
		super();
		setJurisdictionCode(jurisdiction);
		setApplicationNumberFormatted(applicationNumber);
		setAppNumberRaw(appNumberRaw);
		setCustomerNumber(customerNumber);
		setMailingDate(mailingDate);
		this.documentCode = documentCode;
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
	 * Gets the correspondence code.
	 *
	 * @return the correspondence code
	 */
	public String getDocumentCode() {
		return documentCode;
	}

	/**
	 * Sets the correspondence code.
	 *
	 * @param documentCode the new correspondence code
	 */
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	/**
	 * Gets the retry count.
	 *
	 * @return the retry count
	 */
	public int getRetryCount() {
		return retryCount;
	}

	/**
	 * Sets the retry count.
	 *
	 * @param retryCount the new retry count
	 */
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public QueueStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(QueueStatus status) {
		this.status = status;
	}

	/**
	 * Gets the filing date.
	 *
	 * @return the filing date
	 */
	public Calendar getFilingDate() {
		return filingDate;
	}

	/**
	 * Sets the filing date.
	 *
	 * @param filingDate the new filing date
	 */
	public void setFilingDate(Calendar filingDate) {
		this.filingDate = filingDate;
	}

	/**
	 * Gets the page count.
	 *
	 * @return the page count
	 */
	public Integer getPageCount() {
		return pageCount;
	}

	/**
	 * Sets the page count.
	 *
	 * @param pageCount the new page count
	 */
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
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

}
