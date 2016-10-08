package com.blackbox.ids.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * Person entity
 *
 * @author Nagarro
 *
 */
@Entity
@Table(name = "BB_PERSON")
public class Person implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 4665526346531626431L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_PERSON_ID", nullable = false)
	private long Id;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "FIRST_NAME", nullable = false)
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Column(name = "EMAIL", nullable = false)
	private String email;

	@Column(name = "EMPLOYEE_ID")
	private String employeeId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BB_NATIONALITY_ID", nullable = false)
	private Nationality nationality;


	/*@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BB_ASSOCIATED_AGENT_ID", nullable = false)
	private AssociatedAgent associatedAgent;*/

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId
	 *            the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the nationality
	 */
	public Nationality getNationality() {
		return nationality;
	}

	/**
	 * @param nationality
	 *            the nationality to set
	 */
	public void setNationality(Nationality nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return Id;
	}

}