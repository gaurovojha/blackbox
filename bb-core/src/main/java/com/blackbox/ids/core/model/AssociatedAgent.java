package com.blackbox.ids.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AssociatedAgent entity
 * 
 * @author Nagarro
 *
 */
@Entity
@Table(name = "BB_ASSOCIATED_AGENT")
public class AssociatedAgent implements Serializable{

	/**
	 * system generated serial version id
	 */
	private static final long serialVersionUID = -1654308985816308306L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_ASSOCIATED_AGENT_ID")
	private long id;

	@Column(name = "BB_ASSOCIATED_AGENT_DESCRIPTION")
	private String description;

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
	 * @return the id
	 */
	public long getId() {
		return id;
	}

}
