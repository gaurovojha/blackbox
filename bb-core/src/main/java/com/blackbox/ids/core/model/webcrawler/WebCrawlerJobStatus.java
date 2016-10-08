package com.blackbox.ids.core.model.webcrawler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

// TODO: Auto-generated Javadoc
/**
 * The Class WebCrawlerJobStatus.
 */
@Entity
@Table(name = "BB_WEB_CRAWLER_JOB_STATUS")
public class WebCrawlerJobStatus {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	/** The job name. */
	@Column(name = "JOB_NAME")
	private String jobName;

	/** The retry count for host not found. */
	@Column(name = "HOST_NOT_FOUND_RETRIES")
	private Integer retryCountForHostNotFound;

	/** The retry count for host not found. */
	@Column(name = "MAX_HOST_NOT_FOUND_RETRIES")
	private Integer maxCountForHostNotFound;

	/** The retry count for uspto updation. */
	@Column(name = "USPTO_UPDATING_RETRIES")
	private Integer retryCountForUsptoUpdation;

	/** The retry count for host not found. */
	@Column(name = "MAX_USPTO_UPDATING_RETRIES")
	private Integer maxCountForUsptoUpdation;

	/** The retry count for uspto updation. */
	@Column(name = "XML_CORRUPT_COUNT")
	private Integer xmlOrPDFCorruptRetryCount;

	/** The retry count for host not found. */
	@Column(name = "MAX_XML_CORRUPT_COUNT")
	private Integer maxXmlCorruptRetryCount;

	/** The retry count for uspto updation. */
	@Column(name = "XML_UI_MISMATCH_COUNT")
	private Integer xmlUIMismatchRetryCount;

	/** The retry count for host not found. */
	@Column(name = "MAX_XML_UI_MISMATCH_COUNT")
	private Integer maxXmlUIMismatchRetryCount;
	
	/** Maximum no. of retry, before sending a notification for a record in the queue. */
	@Column(name = "MAX_RECORD_RETRY_COUNT", nullable = false)
	private Integer maxRecordRetryCount = 1; 
	/**
	 * Gets the job name.
	 *
	 * @return the job name
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * Sets the job name.
	 *
	 * @param jobName
	 *            the new job name
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * Gets the retry count for host not found.
	 *
	 * @return the retry count for host not found
	 */
	public Integer getRetryCountForHostNotFound() {
		return retryCountForHostNotFound;
	}

	/**
	 * Sets the retry count for host not found.
	 *
	 * @param retryCountForHostNotFound the new retry count for host not found
	 */
	public void setRetryCountForHostNotFound(Integer retryCountForHostNotFound) {
		this.retryCountForHostNotFound = retryCountForHostNotFound;
	}

	/**
	 * Gets the max count for host not found.
	 *
	 * @return the max count for host not found
	 */
	public Integer getMaxCountForHostNotFound() {
		return maxCountForHostNotFound;
	}

	/**
	 * Sets the max count for host not found.
	 *
	 * @param maxCountForHostNotFound the new max count for host not found
	 */
	public void setMaxCountForHostNotFound(Integer maxCountForHostNotFound) {
		this.maxCountForHostNotFound = maxCountForHostNotFound;
	}

	/**
	 * Gets the retry count for uspto updation.
	 *
	 * @return the retry count for uspto updation
	 */
	public Integer getRetryCountForUsptoUpdation() {
		return retryCountForUsptoUpdation;
	}

	/**
	 * Sets the retry count for uspto updation.
	 *
	 * @param retryCountForUsptoUpdation the new retry count for uspto updation
	 */
	public void setRetryCountForUsptoUpdation(Integer retryCountForUsptoUpdation) {
		this.retryCountForUsptoUpdation = retryCountForUsptoUpdation;
	}

	/**
	 * Gets the max count for uspto updation.
	 *
	 * @return the max count for uspto updation
	 */
	public Integer getMaxCountForUsptoUpdation() {
		return maxCountForUsptoUpdation;
	}

	/**
	 * Sets the max count for uspto updation.
	 *
	 * @param maxCountForUsptoUpdation the new max count for uspto updation
	 */
	public void setMaxCountForUsptoUpdation(Integer maxCountForUsptoUpdation) {
		this.maxCountForUsptoUpdation = maxCountForUsptoUpdation;
	}

	/**
	 * Gets the xml or pdf corrupt retry count.
	 *
	 * @return the xml or pdf corrupt retry count
	 */
	public Integer getXmlOrPDFCorruptRetryCount() {
		return xmlOrPDFCorruptRetryCount;
	}

	/**
	 * Sets the xml or pdf corrupt retry count.
	 *
	 * @param xmlOrPDFCorruptRetryCount the new xml or pdf corrupt retry count
	 */
	public void setXmlOrPDFCorruptRetryCount(Integer xmlOrPDFCorruptRetryCount) {
		this.xmlOrPDFCorruptRetryCount = xmlOrPDFCorruptRetryCount;
	}

	/**
	 * Gets the max xml corrupt retry count.
	 *
	 * @return the max xml corrupt retry count
	 */
	public Integer getMaxXmlCorruptRetryCount() {
		return maxXmlCorruptRetryCount;
	}

	/**
	 * Sets the max xml corrupt retry count.
	 *
	 * @param maxXmlCorruptRetryCount the new max xml corrupt retry count
	 */
	public void setMaxXmlCorruptRetryCount(Integer maxXmlCorruptRetryCount) {
		this.maxXmlCorruptRetryCount = maxXmlCorruptRetryCount;
	}

	/**
	 * Gets the xml ui mismatch retry count.
	 *
	 * @return the xml ui mismatch retry count
	 */
	public Integer getXmlUIMismatchRetryCount() {
		return xmlUIMismatchRetryCount;
	}

	/**
	 * Sets the xml ui mismatch retry count.
	 *
	 * @param xmlUIMismatchRetryCount the new xml ui mismatch retry count
	 */
	public void setXmlUIMismatchRetryCount(Integer xmlUIMismatchRetryCount) {
		this.xmlUIMismatchRetryCount = xmlUIMismatchRetryCount;
	}

	/**
	 * Gets the max xml ui mismatch retry count.
	 *
	 * @return the max xml ui mismatch retry count
	 */
	public Integer getMaxXmlUIMismatchRetryCount() {
		return maxXmlUIMismatchRetryCount;
	}

	/**
	 * Sets the max xml ui mismatch retry count.
	 *
	 * @param maxXmlUIMismatchRetryCount the new max xml ui mismatch retry count
	 */
	public void setMaxXmlUIMismatchRetryCount(Integer maxXmlUIMismatchRetryCount) {
		this.maxXmlUIMismatchRetryCount = maxXmlUIMismatchRetryCount;
	}

	/**
	 * Gets the max record retry count.
	 *
	 * @return the max record retry count
	 */
	public Integer getMaxRecordRetryCount() {
		return maxRecordRetryCount;
	}

	/**
	 * Sets the max record retry count.
	 *
	 * @param maxRecordRetryCount the new max record retry count
	 */
	public void setMaxRecordRetryCount(Integer maxRecordRetryCount) {
		this.maxRecordRetryCount = maxRecordRetryCount;
	}


}
