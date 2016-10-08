package com.blackbox.ids.core.model.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Base entity with common fields.
 * <p/>
 * Apart from the {@link AuditableEntity audit details}, it holds it specifies the version of parent entity class that
 * serves as its optimistic lock value.
 *
 * @author nagarro
 */
@MappedSuperclass
public abstract class BaseEntity extends AuditableEntity {

	/** system generated serial version id. */
	private static final long serialVersionUID = -7394262530832340514L;

	/** The version. */
	@Version
	@Column(name = "VERSION")
	private int version;

	/*- ------------------------ getter-setters -- */
	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 *
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}
}
