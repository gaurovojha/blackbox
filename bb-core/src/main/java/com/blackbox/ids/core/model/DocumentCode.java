package com.blackbox.ids.core.model;

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

import com.blackbox.ids.core.model.mstr.Jurisdiction;

/**
 * The Class DocumentCode.
 */
@Entity
@Table(name = "BB_DOCUMENT_CODE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DocumentCode implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1476697901788575028L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_DOCUMENT_CODE_ID", unique = true, nullable = false, length = 50)
	private Long id;

	/** The code. */
	@Column(name = "DOCUMENT_CODE", unique = true, nullable = false)
	private String code;

	/** The description. */
	@Column(name = "DOCUMENT_DESCRIPTION", nullable = false)
	private String description;

	/*- ------------------------------- Foreign References -- */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_JURISDICTION_ID", referencedColumnName = "BB_JURISDICTION_ID", nullable = false)
	private Jurisdiction jurisdiction;

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

	public Jurisdiction getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(Jurisdiction jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

}
