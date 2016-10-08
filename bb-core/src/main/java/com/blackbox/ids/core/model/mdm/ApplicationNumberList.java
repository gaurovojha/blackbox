/**
 *
 */
package com.blackbox.ids.core.model.mdm;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.blackbox.ids.core.model.enums.ApplicationNumberStatus;

/**
 * The {@code ApplicationNumberList} is an embeddable entity type containing the application number attributes.
 *
 * @author ajay2258
 */
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "jurisdiction", "appNumber" }) )
@Embeddable
public class ApplicationNumberList {

	@Column(name = "JURISDICTION", nullable = false)
	private String jurisdiction;

	/** Application number, as input by the end user. */
	@Column(name = "APPLICATION_NUMBER_RAW", nullable = false)
	private String appNumberRaw;

	/** Application number converted. */
	@Column(name = "APPLICATION_NUMBER", nullable = false)
	private String appNumber;

	@Column(name = "CUSTOMER_NUMBER")
	private String customerNumber;

	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private ApplicationNumberStatus status;

	/*- ---------------------------- Constructors -- */
	public ApplicationNumberList() {
		super();
	}

	public ApplicationNumberList(String jurisdiction, String appNumber, String appNumberRaw, String customerNumber,
			ApplicationNumberStatus status) {
		super();
		this.jurisdiction = jurisdiction;
		this.appNumber = appNumber;
		this.appNumberRaw = appNumberRaw;
		this.customerNumber = customerNumber;
		this.status = status;
	}

	/*- ---------------------------- getter-setters -- */
	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getAppNumberRaw() {
		return appNumberRaw;
	}

	public void setAppNumberRaw(String appNumberRaw) {
		this.appNumberRaw = appNumberRaw;
	}

	public String getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public ApplicationNumberStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationNumberStatus status) {
		this.status = status;
	}

}
