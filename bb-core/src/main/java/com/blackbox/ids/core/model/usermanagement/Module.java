package com.blackbox.ids.core.model.usermanagement;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * The Class Module.
 */
@Entity
@Table(name = "BB_MODULE")
public class Module implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4641566859006459585L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_MODULE_ID", unique = true, nullable = false, length = 50)
	private Long id;

	/** The module code. */
	@Column(name = "MODULE_CODE", unique = true, nullable = false)
	private String moduleCode;

	/** The module name. */
	@Column(name = "MODULE_NAME", unique = true, nullable = false)
	private String moduleName;

	/** The module description. */
	@Column(name = "MODULE_DESCRIPTION", unique = true, nullable = false)
	private String moduleDescription;

	/** The access controls. */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_MODULE_ID", referencedColumnName = "BB_MODULE_ID")
	@OrderBy("name ASC")
	private Set<AccessControl> accessControls;

	/**
	 * Gets the module name.
	 *
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * Sets the module name.
	 *
	 * @param moduleName
	 *            the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * Gets the module description.
	 *
	 * @return the moduleDescription
	 */
	public String getModuleDescription() {
		return moduleDescription;
	}

	/**
	 * Sets the module description.
	 *
	 * @param moduleDescription
	 *            the moduleDescription to set
	 */
	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the module code.
	 *
	 * @return the moduleCode
	 */
	public String getModuleCode() {
		return moduleCode;
	}

	/**
	 * Sets the module code.
	 *
	 * @param moduleCode
	 *            the moduleCode to set
	 */
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	/**
	 * Gets the access controls.
	 *
	 * @return the access controls
	 */
	public Set<AccessControl> getAccessControls() {
		if (accessControls == null) {
			accessControls = new LinkedHashSet<>();
		}
		
		return accessControls;
	}

	/**
	 * Sets the access controls.
	 *
	 * @param accessControls
	 *            the new access controls
	 */
	public void setAccessControls(Set<AccessControl> accessControls) {
		this.accessControls = accessControls;
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

}
