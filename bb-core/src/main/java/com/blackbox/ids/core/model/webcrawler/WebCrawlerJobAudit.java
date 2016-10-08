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

import com.blackbox.ids.core.model.enums.WebCrawlerJobStatusEnum;

// TODO: Auto-generated Javadoc
/**
 * The Class WebCrawlerJobStatus.
 */
@Entity
@Table(name = "BB_WEB_CRAWLER_AUDIT")
public class WebCrawlerJobAudit {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7394262530832340514L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "JOB_NAME")
	private String jobName;
	
	/** The status. */
	@Column(name = "JOB_STATUS")
	@Enumerated(EnumType.STRING)
	private WebCrawlerJobStatusEnum status;

	/** The status. */
	@Column(name = "ERROR_MESSAGE")
	private String errorMessage;
	
	/** The created date. */
	@Column(name = "START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startTime;
	
	/** The created date. */
	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endTime;

	/**
	 * Instantiates a new web crawler job audit.
	 */
	public WebCrawlerJobAudit() {
		super();
	}

	/**
	 * Instantiates a new web crawler job status.
	 *
	 * @param status the status
	 * @param errorMessage the error message
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param jobName TODO
	 */
	public WebCrawlerJobAudit(WebCrawlerJobStatusEnum status, String errorMessage, Calendar startTime,
			Calendar endTime, String jobName) {
		super();
		this.status = status;
		this.errorMessage = errorMessage;
		this.startTime = startTime;
		this.endTime = endTime;
		this.jobName = jobName;
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
	 * Gets the error message.
	 *
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets the error message.
	 *
	 * @param errorMessage the new error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public Calendar getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime the new start time
	 */
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public Calendar getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime the new end time
	 */
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	/**
	 * Instantiates a new web crawler job audit.
	 *
	 * @param id the id
	 * @param status the status
	 * @param errorMessage the error message
	 * @param startTime the start time
	 * @param endTime the end time
	 */
	public WebCrawlerJobAudit(Long id, WebCrawlerJobStatusEnum status, String errorMessage, Calendar startTime, Calendar endTime) {
		super();
		this.id = id;
		this.status = status;
		this.errorMessage = errorMessage;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public WebCrawlerJobStatusEnum getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(WebCrawlerJobStatusEnum status) {
		this.status = status;
	}
	
}
