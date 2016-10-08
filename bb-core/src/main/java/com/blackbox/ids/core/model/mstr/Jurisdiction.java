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
 * The entity class {@code Jurisdiction} represents a legal jurisdiction in application.
 *
 * @author ajay2258
 */
@Entity
@Table(name = "BB_MSTR_JURISDICTION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Jurisdiction implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -1476697901788575028L;

	public enum Code {
		US("US"), WIPO("WO"), EUROPE("EP");

		public final String value;

		private Code(final String code) {
			this.value = code;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_JURISDICTION_ID")
	private Long id;

	@Column(name = "CODE", nullable = false, unique = true, length = 2)
	private String code;

	@Column(name = "DESCRIPTION")
	private String description;

	/*- --------------------------- Constructor -- */
	public Jurisdiction() {
		super();
	}

	public Jurisdiction(final Long id) {
		super();
		this.id = id;
	}

	public Jurisdiction(final Long id, final String code) {
		super();
		this.id = id;
		this.code = code;
	}

	/*- ---------------------------- getter-setters -- */
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*- ---------------------------- equals n hashCode -- */
	@Override
	public boolean equals(Object object) {
		boolean result = false;
		if (object == null || object.getClass() != getClass()) {
			result = false;
		} else {
			Jurisdiction jurisdiction = (Jurisdiction) object;
			if (this.id == jurisdiction.getId()) {
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
		if(this.code != null) {
			hash = 7 * hash + this.code.hashCode();
		}
		return hash;
	}

}
