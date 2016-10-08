package com.blackbox.ids.dto;

public class NationalityDTO {
	private long id;

	private String name;

	/*- ------------------------ Constructor -- */
	public NationalityDTO() {
		super();
	}

	public NationalityDTO(final long id, final String name) {
		super();
		this.id = id;
		this.name = name;
	}

	/*- ------------------------ getter-setters -- */
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
