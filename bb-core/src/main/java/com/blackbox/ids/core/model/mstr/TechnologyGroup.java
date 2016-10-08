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
 * The entity class {@code TechnologyGroup} represents a technology group for <b>corporate</b> client.
 *
 * @author ajay2258
 */
@Entity
@Table(name = "BB_MSTR_TECHNOLOGY_GROUP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TechnologyGroup implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -8801098929230886473L;

	/** Database identifier for default single technology group instance. */
	public static final TechnologyGroup DEFAULT_TECHNOLOGY_GROUP = new TechnologyGroup(-1L);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_TECHNOLOGY_GROUP_ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "BB_CLIENT")
	private Long clientId;

	/*- ---------------------------- Constructor -- */
	public TechnologyGroup() {
		super();
	}

	public TechnologyGroup(Long id) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
			TechnologyGroup technologyGroup = (TechnologyGroup) object;
			if (this.id == technologyGroup.getId()) {
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
