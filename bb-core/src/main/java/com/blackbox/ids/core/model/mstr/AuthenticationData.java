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
 * AuthenticationData entity.
 *
 * @author Nagarro
 */
@Entity
@Table(name = "BB_MSTR_AUTHENTICATION_DATA")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AuthenticationData implements Serializable{

	/** The serial version UID. */
	private static final long serialVersionUID = 7154402140799905830L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_MSTR_AUTHENTICATION_DATA_ID")
	private Long id;

	/** The customer number. */
	@Column(name = "PASSWORD")
	private String password;
	
	/** The customer number. */
	@Column(name = "FILE_LOCATION_RELATIVE_PATH")
	private String authenticationFileName;
	

	
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
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the file path relative.
	 *
	 * @return the file path relative
	 */
	public String getAuthenticationFileName() {
		return authenticationFileName;
	}

	/**
	 * Sets the file path relative.
	 *
	 * @param filePathRelative the new file path relative
	 */
	public void setAuthenticationFileName(String filePathRelative) {
		this.authenticationFileName = filePathRelative;
	}

}
