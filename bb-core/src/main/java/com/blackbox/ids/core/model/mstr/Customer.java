package com.blackbox.ids.core.model.mstr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

// TODO: Auto-generated Javadoc
/**
 * CustomerNumber entity.
 *
 * @author Nagarro
 */
@Entity
@Table(name = "BB_MSTR_CUSTOMER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer implements Serializable{

	/** The serial version UID. */
	private static final long serialVersionUID = 7154402140799905830L;

	/** Database identifier for default customer instance. */
	public static final Customer DEFAULT_CUSTOMER = new Customer(-1L, "9999999999");

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_CUSTOMER_ID")
	private Long id;

	/** The customer number. */
	@Column(name = "CUSTOMER_NUMBER")
	private String customerNumber;

	/** The authentication data. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_MSTR_AUTHENTICATION_DATA", referencedColumnName = "BB_MSTR_AUTHENTICATION_DATA_ID")
	private AuthenticationData authenticationData;

	/*- ----------------------------- Constructors -- */
	public Customer() {
		super();
	}

	public Customer(final Long id) {
		super();
		this.id = id;
	}

	public Customer(final Long id, final String customerNumber) {
		super();
		this.id = id;
		this.customerNumber = customerNumber;
	}

	/*- ---------------------------- getter-setters -- */
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
	 * Gets the authentication data.
	 *
	 * @return the authentication data
	 */
	public AuthenticationData getAuthenticationData() {
		return authenticationData;
	}

	/**
	 * Sets the authentication data.
	 *
	 * @param authenticationData the new authentication data
	 */
	public void setAuthenticationData(AuthenticationData authenticationData) {
		this.authenticationData = authenticationData;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/*- ---------------------------- equals 'n' hashCode -- */
	@Override
	public boolean equals(Object object) {
		boolean result = false;
		if (object == null || object.getClass() != getClass()) {
			result = false;
		} else {
			Customer customer = (Customer) object;
			if (this.id == customer.getId()) {
				result = true;
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 3;
		if(this.id != null) {
			hash = 7 * hash + this.id.hashCode();
		}
		if(this.customerNumber != null) {
			hash = 7 * hash + this.customerNumber.hashCode();
		}
		return hash;
	}

}
