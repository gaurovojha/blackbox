package com.blackbox.ids.core.model;

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
 * Nationality entity
 * 
 * @author Nagarro
 *
 */
@Entity
@Table(name = "BB_NATIONALITY")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Nationality implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 6709513996768680827L;

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name = "BB_NATIONALITY_ID")
	private long id;

	@Column(name = "NATIONALITY_VALUE")
	private String value;

	/*- ------------------------ Constructors -- */
	public Nationality() {
		super();
	}
	
	public Nationality(final Long id) {
		super();
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
	 * @return the id
	 */
	public long getId() {
		return id;
	}


}
