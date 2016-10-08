package com.blackbox.ids.core.model.mstr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Organization entity
 *
 * @author ajay2258
 *
 */
@Entity
@Table(name = "BB_ORGANIZATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Organization implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 3208539792033836478L;

	/** Database identifier for default single organization instance. */
	public static final Organization DEFAULT_ORGANIZATION = new Organization(-1L);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_ORGANIZATION_ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "ADDRESS_1")
	private String address1;

	@Column(name = "ADDRESS_2")
	private String address2;

	@Column(name = "CITY")
	private String city;

	@Column(name = "STATE")
	private String state;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "ZIPCODE")
	private String zipCode;

	@Column(name = "BB_CLIENT")
	private Long clientId;

	public Organization() {
		super();
	}

	public Organization(final Long id) {
		super();
		this.id = id;
	}

	/*- ---------------------------- getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	/*- ---------------------------- equals 'n' hashCode -- */
	@Override
	public boolean equals(Object object) {
		boolean result = false;
		if (object == null || object.getClass() != getClass()) {
			result = false;
		} else {
			Organization organization = (Organization) object;
			if (this.id == organization.id) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		if(this.id != null) {
			hash = 7 * hash + this.id.hashCode();
		}
		if(this.name != null) {
			hash = 7 * hash + this.name.hashCode();
		}
		return hash;
	}

}
