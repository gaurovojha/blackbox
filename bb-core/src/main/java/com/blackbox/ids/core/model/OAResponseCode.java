package com.blackbox.ids.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entity for OA Response codes
 */
@Entity
@Table(name = "BB_OA_RESPONSE_CODE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OAResponseCode {
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_OA_RESPONSE_CODE_ID", unique = true, nullable = false, length = 50)
	private Long id;

	/** The code. */
	@Column(name = "OA_RESPONSE_CODE", unique = true, nullable = false)
	private String code;

	/** The description. */
	@Column(name = "RESPONSE_DESCRIPTION", nullable = false)
	private String description;
	
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
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
