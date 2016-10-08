package com.blackbox.ids.core.model.usermanagement;

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
 * The Class Permission.
 */
@Entity
@Table(name = "BB_PERMISSION")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Permission implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_PERMISSION_ID", nullable = false)
	private Long id;

	/** The method. */
	@Column(name = "METHOD")
	private String method;

	/** The url. */
	@Column(name = "URL", nullable = false)
	private String url;
	
	/** The description. */
	@Column(name = "PERMISSION_DESCRIPTION")
	private String description;
	
	/** The access control id. */
	@Column(name = "BB_ACCESS_CONTROL_ID")
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Long accessControlId;
	
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
	 * @param description            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the method.
	 *
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the method.
	 *
	 * @param method            the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the access control id.
	 *
	 * @return the access control id
	 */
	public Long getAccessControlId() {
		return accessControlId;
	}

	/**
	 * Sets the access control id.
	 *
	 * @param accessControlId the new access control id
	 */
	public void setAccessControlId(Long accessControlId) {
		this.accessControlId = accessControlId;
	}
}
