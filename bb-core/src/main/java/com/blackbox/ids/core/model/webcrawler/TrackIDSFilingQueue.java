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

/**
 * Track IDS filing queue, used for web scraping
 */
@Entity
@Table(name = "BB_SCR_TRACK_IDS_FILING")
public class TrackIDSFilingQueue extends BaseQueueEntity {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= -8681525499948015850L;

	/**
	 * The Enum RequestType.
	 */
	public enum RequestType {

		/** The first run. */
		FIRST_RUN,
		/** The subsequent run. */
		SUBSEQUENT_RUN
	}

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long		id;

	/** The application filing date. */
	@Column(name = "FILING_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar	filingDate;

	/** The base application id. */
	@Column(name = "APPLICATION_ID", nullable = false)
	private long		applicationId;

	/** The ids id. */
	@Column(name = "IDS_ID", nullable = false)
	private long		idsId;
	
	/** The ids build id. */
	@Column(name = "IDS_BUILD_ID", nullable = true)
	private String idsBuildId;

	/** Attorney user who has approved the IDS. */
	@Column(name = "IDS_APPROVED_BY")
	private Long		approvedBy;

	/** Paralegal user who has filed the IDS. */
	@Column(name = "IDS_FILED_BY")
	private Long		filedBy;

	/** The ids date. */
	@Column(name = "IDS_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Calendar	idsDate;

	/** The request type. */
	@Column(name = "REQUEST_TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private RequestType	requestType;

	/** The status. */
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private QueueStatus	status		= QueueStatus.INITIATED;

	/** The retry count. */
	@Column(name = "RETRY_COUNT")
	private int			retryCount	= 0;

	/** The next run date. */
	@Column(name = "NEXT_RUN_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Calendar	nextRunDate;

	/**
	 * Gets the id.
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the ids id.
	 * @return the ids id
	 */
	public long getIdsId() {
		return idsId;
	}

	/**
	 * Sets the ids id.
	 * @param idsId
	 *            the new ids id
	 */
	public void setIdsId(long idsId) {
		this.idsId = idsId;
	}

	/**
	 * Gets the request type.
	 * @return the request type
	 */
	public RequestType getRequestType() {
		return requestType;
	}

	/**
	 * Sets the request type.
	 * @param requestType
	 *            the new request type
	 */
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	/**
	 * Gets the status.
	 * @return the status
	 */
	public QueueStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * @param status
	 *            the new status
	 */
	public void setStatus(QueueStatus status) {
		this.status = status;
	}

	/**
	 * Gets the filing date.
	 * @return the filing date
	 */
	public Calendar getFilingDate() {
		return filingDate;
	}

	/**
	 * Sets the filing date.
	 * @param filingDate
	 *            the new filing date
	 */
	public void setFilingDate(Calendar filingDate) {
		this.filingDate = filingDate;
	}

	/**
	 * Gets the next run date.
	 * @return the next run date
	 */
	public Calendar getNextRunDate() {
		return nextRunDate;
	}

	/**
	 * Sets the next run date.
	 * @param nextRunDate
	 *            the new next run date
	 */
	public void setNextRunDate(Calendar nextRunDate) {
		this.nextRunDate = nextRunDate;
	}

	/**
	 * Gets the ids date.
	 * @return the ids date
	 */
	public Calendar getIdsDate() {
		return idsDate;
	}

	/**
	 * Sets the ids date.
	 * @param idsDate
	 *            the new ids date
	 */
	public void setIdsDate(Calendar idsDate) {
		this.idsDate = idsDate;
	}

	/**
	 * Gets the retry count.
	 * @return the retry count
	 */
	public int getRetryCount() {
		return retryCount;
	}

	/**
	 * Sets the retry count.
	 * @param retryCount
	 *            the new retry count
	 */
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}

	public String getIdsBuildId() {
		return idsBuildId;
	}

	public void setIdsBuildId(String idsBuildId) {
		this.idsBuildId = idsBuildId;
	}

	public Long getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Long getFiledBy() {
		return filedBy;
	}

	public void setFiledBy(Long filedBy) {
		this.filedBy = filedBy;
	}

}
