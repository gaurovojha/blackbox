package com.blackbox.ids.core.model.mstr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * PersonClassification entity
 *
 * @author Nagarro
 *
 */
@Entity
@Table(name = "BB_PERSON_CLASSIFICATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PersonClassification {

	/** Enumerates the code identifiers for master classification types of a person. */
	public enum Code {
		FULL_TIME, TEMP, EXTERNAL_ASSOCIATED_AGENT, EXTERNAL_CLIENT;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_PERSON_CLASSIFICATION_ID")
	private long id;

	@Column(name = "CLASSIFICATION_VALUE")
	private String value;

	@Column(name = "CLASSIFICATION_DETAIL")
	private String detail;

	/*- ------------------------------ Constructors -- */
	public PersonClassification() {
		super();
	}

	public PersonClassification(final Long id) {
		super();
		this.id = id;
	}

	/*- ------------------------------ getter-setters -- */
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

}
