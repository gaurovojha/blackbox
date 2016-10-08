package com.blackbox.ids.core.model.webcrawler;

import java.io.Serializable;

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
 * The Class ValidateApplication.
 */
@Entity
@Table(name = "BB_SCR_VALIDATE_APPLICATION")
public class ValidateApplication implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5721366397021765616L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_SCR_VALIDATE_APPLICATION_ID", unique = true, nullable = false, length = 50)
	private Long id;

	/** The customer number. */
	@Column(name = "BB_CUSTOMER_NUMBER", nullable = false)
	private String customerNumber;

	/** The application number. */
	@Column(name = "BB_APPLICATION_NUMBER", nullable = false)
	private String applicationNumber;
	
	/** The application number in raw format . it is only for search purpose on USPTO. */
	@Column(name = "BB_APPLICATION_NUMBER_RAW", nullable = false)
	private String applicationNumberRaw;

	/** The jurisdiction code. */
	@Column(name = "BB_JURISDICTION_CODE", nullable = false)
	private String jurisdictionCode;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private QueueStatus status = QueueStatus.INITIATED;
	
	@Column(name = "RETRY_COUNT", nullable = false)
	private Integer retryCount;


	public QueueStatus getStatus() {
		return status;
	}

	public void setStatus(QueueStatus status) {
		this.status = status;
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
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the customer number.
	 *
	 * @return the customer number
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}

	/**
	 * Sets the customer number.
	 *
	 * @param customerNumber
	 *            the new customer number
	 */
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
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
	 * @param applicationNumber
	 *            the new application number
	 */
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	/**
	 * Gets the jurisdiction code.
	 *
	 * @return the jurisdiction code
	 */
	public String getJurisdictionCode() {
		return jurisdictionCode;
	}

	/**
	 * Sets the jurisdiction code.
	 *
	 * @param jurisdictionCode
	 *            the new jurisdiction code
	 */
	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public String getApplicationNumberRaw() {
		return applicationNumberRaw;
	}

	public void setApplicationNumberRaw(String applicationNumberRaw) {
		this.applicationNumberRaw = applicationNumberRaw;
	}

}
