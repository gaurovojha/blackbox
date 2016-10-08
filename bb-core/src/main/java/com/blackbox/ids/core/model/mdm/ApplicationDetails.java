/**
 *
 */
package com.blackbox.ids.core.model.mdm;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The embeddable class {@code ApplicationDetails} maintains the application level details for a patent application.
 *
 * @author ajay2258
 */
@Embeddable
public class ApplicationDetails {

	@Column(name = "APPLICATION_NUMBER_RAW", nullable = false)
	private String applicationNumberRaw;

	@Column(name = "CHILD_APPLICATION_TYPE")
	@Enumerated(EnumType.STRING)
	private ApplicationType childApplicationType;

	@Column(name = "CONFIRMATION_NUMBER")
	private String confirmationNumber;

	@Column(name = "FILING_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar filingDate;
	
	public ApplicationDetails(){}
	
	public ApplicationDetails(String applicationNumberRaw,
			ApplicationType childApplicationType, String confirmationNumber,
			Calendar filingDate) {
		this.applicationNumberRaw = applicationNumberRaw;
		this.childApplicationType = childApplicationType;
		this.confirmationNumber = confirmationNumber;
		this.filingDate = filingDate;
	}

	/*- ---------------------------- getter-setters -- */
	public String getApplicationNumberRaw() {
		return applicationNumberRaw;
	}

	public void setApplicationNumberRaw(String applicationNumberRaw) {
		this.applicationNumberRaw = applicationNumberRaw;
	}

	public ApplicationType getChildApplicationType() {
		return childApplicationType;
	}

	public void setChildApplicationType(ApplicationType childApplicationType) {
		this.childApplicationType = childApplicationType;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public Calendar getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(Calendar filingDate) {
		this.filingDate = filingDate;
	}

}
