package com.blackbox.ids.core.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.PersonClassification;

/**
 * The User Entity
 *
 * @author Nagarro
 *
 */
@NamedQueries({
	  @NamedQuery(name="findEmailById",
			  	query="SELECT u.loginId FROM User u " +
					  "WHERE u.id = ?1"),
	  @NamedQuery(name="findUserIdByEmail",
			  	query="SELECT u.id FROM User u " +
					  "WHERE u.loginId = ?1")
	})
@Entity
@Table(name = "BB_USER")
public class User extends BaseEntity {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = -5069737674018258423L;

	/**
	 * Id for System user.
	 */
	public static final Long SYSTEM_ID = -1L;
	
	public User() {
	}
	
	public User(Long userId) {
		this.id = userId;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_USER_ID", unique = true, nullable = false, length = 50)
	private Long id;

	@Column(name = "LOGIN_ID", unique = true, nullable = false)
	private String loginId;

	/** The password. */
	@Column(name = "PASSWORD", nullable = false)
	private String password;

	/** The enabled. */
	@Column(name = "IS_ACTIVE", nullable = false)
	private Boolean isActive;

	@Basic(optional = false)
	@Column(name = "IS_LOCKED")
	private Boolean isLocked;

	@Column(name = "PASSWORD_CREATED_ON")
	@Temporal(TemporalType.TIMESTAMP)
	private Date passwordCreatedON;

	@Column(name = "PASSWORD_MODIFIED_ON")
	@Temporal(TemporalType.TIMESTAMP)
	private Date passwordModifiedON;

	@Basic(optional = false)
	@Column(name = "LOGIN_ATTEMPTS")
	private Long loginAttempts;

	@Column(name = "END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_ASSIGNEE_ID", nullable = true)
	private Assignee assignee;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_PERSON_CLASSIFICATION_ID", nullable = false)
	private PersonClassification classification;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "BB_USER_PERSON", joinColumns = {
			@JoinColumn(name = "BB_USER_ID", referencedColumnName = "BB_USER_ID") }, inverseJoinColumns = {
					@JoinColumn(name = "BB_PERSON_ID", referencedColumnName = "BB_PERSON_ID") })
	private Person person;

	/** The user roles. */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "BB_USER_ROLE", joinColumns = {
			@JoinColumn(name = "BB_USER_ID", referencedColumnName = "BB_USER_ID") }, inverseJoinColumns = {
					@JoinColumn(name = "BB_ROLE_ID", referencedColumnName = "BB_ROLE_ID") })
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Role> userRoles = new HashSet<Role>();

	@Column(name = "IS_FIRST_LOGIN")
	private Boolean isFirstLogin;

	@Column(name="IS_ENABLED")
	private Boolean isEnabled;

	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * @param loginId
	 *            the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the isActive
	 */
	public Boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the isLocked
	 */
	public Boolean getIsLocked() {
		return isLocked;
	}

	/**
	 * @param isLocked
	 *            the isLocked to set
	 */
	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	/**
	 * @return the passwordCreatedON
	 */
	public Date getPasswordCreatedON() {
		return passwordCreatedON;
	}

	/**
	 * @param passwordCreatedON
	 *            the passwordCreatedON to set
	 */
	public void setPasswordCreatedON(Date passwordCreatedON) {
		this.passwordCreatedON = passwordCreatedON;
	}

	/**
	 * @return the passwordModifiedON
	 */
	public Date getPasswordModifiedON() {
		return passwordModifiedON;
	}

	/**
	 * @param passwordModifiedON
	 *            the passwordModifiedON to set
	 */
	public void setPasswordModifiedON(Date passwordModifiedON) {
		this.passwordModifiedON = passwordModifiedON;
	}

	/**
	 * @return the loginAttempts
	 */
	public Long getLoginAttempts() {
		return loginAttempts;
	}

	/**
	 * @param loginAttempts
	 *            the loginAttempts to set
	 */
	public void setLoginAttempts(Long loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the assignee
	 */
	public Assignee getAssignee() {
		return assignee;
	}

	/**
	 * @param assignee
	 *            the assignee to set
	 */
	public void setAssignee(Assignee assignee) {
		this.assignee = assignee;
	}



	/**
	 * @return the classification
	 */
	public PersonClassification getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(PersonClassification classification) {
		this.classification = classification;
	}

	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person
	 *            the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return the userRoles
	 */
	public Set<Role> getUserRoles() {
		return userRoles;
	}

	/**
	 * @param userRoles
	 *            the userRoles to set
	 */
	public void setUserRoles(Set<Role> userRoles) {
		this.userRoles = userRoles;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	public Boolean isFirstLogin() {
		return isFirstLogin;
	}

	public void setFirstLogin(Boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	/**
	 * @return the isEnabled
	 */
	public Boolean isEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}



}