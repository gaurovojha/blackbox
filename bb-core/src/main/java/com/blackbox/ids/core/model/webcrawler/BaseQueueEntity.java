package com.blackbox.ids.core.model.webcrawler;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.mdm.ApplicationBase;

/**
 * Base queue entity with common fields.
 *
 * @author nagarro
 */
@MappedSuperclass
public class BaseQueueEntity implements Serializable {

	/** system generated serial version id. */
	private static final long serialVersionUID = -7394262530832340514L;

	/** The app number converted. */
	@Column(name = "APPLICATION_NUMBER_FORMATTED", nullable = false)
	private String applicationNumberFormatted;

	/** The application no. raw */
	@Column(name = "APPLICATION_NUMBER_RAW", nullable = false)
	private String appNumberRaw;

	/** The customer number. */
	@Column(name = "CUSTOMER_NUMBER", nullable = false)
	private String customerNumber;

	/** The jurisdiction code. */
	@Column(name = "JURISDICTION_CODE", nullable = false)
	private String jurisdictionCode;

	/** The created date. */
	@Column(name = "CREATED_ON")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdDate;

	/** The created by user. */
	@Column(name = "CREATED_BY")
	private Long createdByUser;

	/** The updated date. */
	@Column(name = "MODIFIED_ON")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar updatedDate;

	/** The updated by user. */
	@Column(name = "MODIFIED_BY")
	private Long updatedByUser;

	/**
	 * Instantiates a new base queue entity.
	 *
	 * @param appNumberRaw
	 *            the application no. in raw format
	 * @param applicationNumber
	 *            the application number converted
	 * @param customerNumber
	 *            the customer number
	 * @param jurisdictionCode
	 *            the jurisdiction code
	 * @param createdDate
	 *            the created date
	 * @param createdByUser
	 *            the created by user
	 * @param updatedDate
	 *            the updated date
	 * @param updatedByUser
	 *            the updated by user
	 */
	public BaseQueueEntity(String appNumberRaw, String applicationNumber, String customerNumber,
			String jurisdictionCode,
			Calendar createdDate, Long createdByUser, Calendar updatedDate, Long updatedByUser)
	{
		super();
		this.appNumberRaw = appNumberRaw;
		this.applicationNumberFormatted = applicationNumber;
		this.customerNumber = customerNumber;
		this.jurisdictionCode = jurisdictionCode;
		this.createdDate = createdDate;
		this.createdByUser = createdByUser;
		this.updatedDate = updatedDate;
		this.updatedByUser = updatedByUser;
	}

	public BaseQueueEntity() {
		super();
	}

	public String getAppNumberRaw() {
		return appNumberRaw;
	}

	public void setAppNumberRaw(String appNumberRaw) {
		this.appNumberRaw = appNumberRaw;
	}

	/**
	 * Gets the app number.
	 *
	 * @return the app number
	 */
	public String getApplicationNumberFormatted() {
		return applicationNumberFormatted;
	}

	/**
	 * Sets the app number.
	 *
	 * @param applicationNumberFormatted the new application number
	 */
	public void setApplicationNumberFormatted(String applicationNumberFormatted) {
		this.applicationNumberFormatted = applicationNumberFormatted;
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
	 * @param customerNumber the new customer number
	 */
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
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
	 * @param jurisdictionCode the new jurisdiction code
	 */
	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Calendar getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the created by user.
	 *
	 * @return the created by user
	 */
	public Long getCreatedByUser() {
		return createdByUser;
	}

	/**
	 * Sets the created by user.
	 *
	 * @param createdByUser the new created by user
	 */
	public void setCreatedByUser(Long createdByUser) {
		this.createdByUser = createdByUser;
	}

	/**
	 * Gets the updated date.
	 *
	 * @return the updated date
	 */
	public Calendar getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * Sets the updated date.
	 *
	 * @param updatedDate the new updated date
	 */
	public void setUpdatedDate(Calendar updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * Gets the updated by user.
	 *
	 * @return the updated by user
	 */
	public Long getUpdatedByUser() {
		return updatedByUser;
	}

	/**
	 * Sets the updated by user.
	 *
	 * @param updatedByUser the new updated by user
	 */
	public void setUpdatedByUser(Long updatedByUser) {
		this.updatedByUser = updatedByUser;
	}
	
	public ApplicationBase createApplication() {
		ApplicationBase application = new ApplicationBase();
		application.setApplicationNumber(applicationNumberFormatted);
		
		return application;
	}

}
