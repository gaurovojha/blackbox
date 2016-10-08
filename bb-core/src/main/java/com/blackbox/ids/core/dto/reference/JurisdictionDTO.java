package com.blackbox.ids.core.dto.reference;

/**
 * The Class JurisdictionDTO.
 */
public class JurisdictionDTO {

	private long id;

	/** The name. */
	private String name;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the value
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
