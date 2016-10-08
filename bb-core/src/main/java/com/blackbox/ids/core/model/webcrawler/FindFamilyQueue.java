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

@Entity
@Table(name = "BB_SCR_FIND_FAMILY")
public class FindFamilyQueue extends BaseQueueEntity {

	/** The serial version UID. */
	private static final long serialVersionUID = 2729968209026017628L;

	public enum FamilyType {
		PARENT,
		CHILD,
		FOREIGN_PRIORITY;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	/** The filing date. */
	@Column(name = "FILING_DATE", nullable = false)
	private Calendar filingDate;

	@Column(name = "Family_TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private FamilyType type;

	@Column(name = "FAMILY_ID")
	private String familyId;

	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private QueueStatus status = QueueStatus.INITIATED;

	/** The retry count. */
	@Column(name = "RETRY_COUNT", nullable = false)
	private int retryCount = 0;

	/**
	 * Column used for Find-Parent, to identify corresponding entry in Application-Stage table
	 */
	@Column(name = "APPLICATION_STAGE_ID")
	private Long applicationStageId;

	/**
	 * Column used for Find-Parent, to identify base case from create-application-queue table
	 */
	@Column(name = "BASE_APPLICATION_QUEUE_ID")
	private Long baseCaseApplicationQueueId;

	public FindFamilyQueue() {
		super();
	}

	public FindFamilyQueue(Long id, String applicationNumber,
			String customerNumber, String jurisdictionCode,
			Calendar filingDate, String familyId, FamilyType type,
			QueueStatus status, int retryCount) {
		super();
		this.id = id;
		this.filingDate = filingDate;
		this.familyId = familyId;
		this.type = type;
		this.status = status;
		this.retryCount = retryCount;
		this.setApplicationNumberFormatted(applicationNumber);
		this.setCustomerNumber(customerNumber);
		this.setJurisdictionCode(jurisdictionCode);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FamilyType getType() {
		return type;
	}

	public void setType(FamilyType type) {
		this.type = type;
	}

	public QueueStatus getStatus() {
		return status;
	}

	public void setStatus(QueueStatus status) {
		this.status = status;
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

	public Long getApplicationStageId() {
		return applicationStageId;
	}

	public void setApplicationStageId(Long applicationStageId) {
		this.applicationStageId = applicationStageId;
	}

	public Long getBaseCaseApplicationQueueId() {
		return baseCaseApplicationQueueId;
	}

	public void setBaseCaseApplicationQueueId(Long baseCaseApplicationQueueId) {
		this.baseCaseApplicationQueueId = baseCaseApplicationQueueId;
	}

}
