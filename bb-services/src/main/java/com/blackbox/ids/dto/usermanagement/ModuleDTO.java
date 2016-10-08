package com.blackbox.ids.dto.usermanagement;

import java.util.ArrayList;
import java.util.List;

public class ModuleDTO {
	private Long id;
	private String code;
	private String name;
	private String description;
	private List<AccessControlDTO> accessControlDtos;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
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

	public List<AccessControlDTO> getAccessControlDtos() {
		if (accessControlDtos == null) {
			accessControlDtos = new ArrayList<>();
		}
		
		return accessControlDtos;
	}

	public void setAccessControlDtos(List<AccessControlDTO> accessControlDtos) {
		this.accessControlDtos = accessControlDtos;
	}
}