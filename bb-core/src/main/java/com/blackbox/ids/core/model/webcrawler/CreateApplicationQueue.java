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

import com.blackbox.ids.core.model.enums.QueueStatus;

/**
 * The Class CreateApplicationQueue.
 */
@Entity
@Table(name = "BB_SCR_CREATE_APPLICATION")
public class CreateApplicationQueue extends BaseQueueEntity {

	
	private static final long	serialVersionUID	= 4626764020571867937L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	/** The correspondence link. */
	@Column(name = "CORRESPONDENCE_LINK")
	private String correspondenceLink;

	/** The status. */
	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private QueueStatus status = QueueStatus.INITIATED;

	/** The retry count. */
	@Column(name = "RETRY_COUNT")
	private int retryCount = 0;
	
	/** The filing date. */
	@Column(name = "FILING_DATE", nullable = false)
	private Calendar filingDate;
	
	@Column(name = "BASE_APPLICATION_QUEUE_ID")
	private Long baseCaseApplicationQueueId;
	
	/** Family id for foreign priority application notification. */
	@Column(name = "FAMILY_ID")
	private String familyId;
	
	/**
	 * Instantiates a new creates the application queue.
	 */
	public CreateApplicationQueue() {
		super();
	}

	/**
	 * Instantiates a new creates the application queue.
	 *
	 * @param id the id
	 * @param applicationNumber the application number
	 * @param customerNumber the customer number
	 * @param jurisdictionCode the jurisdiction code
	 * @param correspondenceLink the correspondence link
	 */
	public CreateApplicationQueue(Long id, String applicationNumber,
			String customerNumber, String jurisdictionCode,
			Calendar filingDate) {
		super();
		this.id = id;
		this.setApplicationNumberFormatted(applicationNumber);
		this.setCustomerNumber(customerNumber);
		this.setJurisdictionCode(jurisdictionCode);
		this.filingDate = filingDate;
	}

	/**
	 * Instantiates create application queue object to be used for
	 * validating application numbers with customers
	 * @param id the id
	 * @param applicationNumber the application number
	 * @param customerNumber the customer number
	 * @param jurisdictionCode the jurisdiction code
	 * @param correspondenceLink the correspondence link
	 * 
	 */
	public CreateApplicationQueue(long id, String applicationNumber, 
			String customerNumber, String jurisdictionCode, 
			Calendar filingDate, String correspondanceLink, 
			int retryCount, Calendar modifiedDate){
		super();
		this.id = id;
		this.setApplicationNumberFormatted(applicationNumber);
		this.setCustomerNumber(customerNumber);
		this.setJurisdictionCode(jurisdictionCode);
		this.setCorrespondenceLink(correspondenceLink);
		this.filingDate = filingDate;
		this.retryCount = retryCount;		
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
	 * Gets the correspondence link.
	 *
	 * @return the correspondence link
	 */
	public String getCorrespondenceLink() {
		return correspondenceLink;
	}

	/**
	 * Sets the correspondence link.
	 *
	 * @param correspondenceLink the new correspondence link
	 */
	public void setCorrespondenceLink(String correspondenceLink) {
		this.correspondenceLink = correspondenceLink;
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

	public Calendar getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(Calendar filingDate) {
		this.filingDate = filingDate;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public Long getBaseCaseApplicationQueueId() {
		return baseCaseApplicationQueueId;
	}

	public void setBaseCaseApplicationQueueId(Long baseCaseApplicationQueueId) {
		this.baseCaseApplicationQueueId = baseCaseApplicationQueueId;
	}
}
