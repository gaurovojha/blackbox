package com.blackbox.ids.dto;

/**
 * 
 * @author Nagarro
 *
 */
public class CustomerDTO {

	/**
	 * system generated uid
	 */
	// private static final long serialVersionUID = 7386422094780559037L;

	private long id;
	private String number;

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
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

}
