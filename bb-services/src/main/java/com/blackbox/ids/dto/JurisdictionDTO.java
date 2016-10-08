package com.blackbox.ids.dto;

/**
 *
 * @author Nagarro
 *
 */
public class JurisdictionDTO {

	/**
	 * system generated uid
	 */
	// private static final long serialVersionUID = 7386422094780559037L;

	private long id;

	private String name;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
