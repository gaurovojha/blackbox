package com.blackbox.ids.dto;

public class UserTypeDTO {

	private long id;

	private String name;

	/*- ----------------------- Constructor -- */
	public UserTypeDTO() {
		super();
	}

	public UserTypeDTO(final long id, final String name) {
		super();
		this.id = id;
		this.name = name;
	}

	/*- ----------------------- getter-setters -- */
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



}
