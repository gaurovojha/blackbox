package com.blackbox.ids.core.model.usermanagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.blackbox.ids.core.model.base.BaseEntity;

/**
 * UserAccess profile entity.
 *
 * @author Nagarro
 */
@NamedQueries({ @NamedQuery(name = "findAllProfileNames", query = "SELECT p.name FROM AccessProfile p where p.active = true ") })
@Entity
@Table(name = "BB_ACCESS_PROFILE")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccessProfile extends BaseEntity {

	/** system generated serial version id. */
	private static final long serialVersionUID = 1472156815719427387L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_ACCESS_PROFILE_ID", nullable = false)
	private Long id;

	/** The name. */
	@Column(name = "NAME", nullable = false)
	private String name;

	/** The description. */
	@Column(name = "DESCRIPTION", nullable = true)
	private String description;

	/** The active. */
	@Column(name = "ACTIVE", nullable = true)
	private boolean active;

	/** The seeded. */
	@Column(name = "SEEDED", nullable = false)
	private boolean seeded;

	/** The end date. */
	@Column(name = "END_DATE", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endDate;

	/** The permissions. */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "BB_ACCESS_PROFILE_PERMISSION", joinColumns = @JoinColumn(name = "BB_ACCESS_PROFILE_ID") , inverseJoinColumns = @JoinColumn(name = "BB_PERMISSION_ID") )
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Permission> permissions;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Checks if is active.
	 *
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Checks if is seeded.
	 *
	 * @return the seeded
	 */
	public boolean isSeeded() {
		return seeded;
	}

	/**
	 * Sets the seeded.
	 *
	 * @param seeded
	 *            the seeded to set
	 */
	public void setSeeded(boolean seeded) {
		this.seeded = seeded;
	}

	/**
	 * Gets the end date.
	 *
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the permissions
	 */
	public List<Permission> getPermissions() {

		if (permissions == null) {
			permissions = new ArrayList<Permission>();
		}
		return permissions;
	}

	/**
	 * Sets the permissions.
	 *
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
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
	 * Gets the roles.
	 *
	 * @return the roles
	 *//*
	public List<Role> getRoles() {
		return roles;
	}

	*//**
	 * Sets the roles.
	 *
	 * @param roles
	 *            the new roles
	 *//*
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}*/

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}
}