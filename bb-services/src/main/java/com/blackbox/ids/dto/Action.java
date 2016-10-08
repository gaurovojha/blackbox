package com.blackbox.ids.dto;

import java.io.Serializable;

public class Action implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1812052755065821344L;
	
	private String name;

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

}
