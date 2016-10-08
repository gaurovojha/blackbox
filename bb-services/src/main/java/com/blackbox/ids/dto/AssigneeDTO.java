package com.blackbox.ids.dto;

/**
 * 
 * @author Nagarro
 *
 */
public class AssigneeDTO {

	/**
	 * system generated uid
	 */
	// private static final long serialVersionUID = 7386422094780559037L;

	private long id;
	private String name;
	private String description;
	private ClientDTO client;

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

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the client
	 */
	public ClientDTO getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(ClientDTO client) {
		this.client = client;
	}

}
