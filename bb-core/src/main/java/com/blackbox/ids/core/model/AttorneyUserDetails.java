/**
 *
 */
package com.blackbox.ids.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.blackbox.ids.core.model.base.BaseEntity;

/**
 * The class {@code AttorneyUserDetails} holds the details specific to attorney users in application.
 *
 * @author ajay2258
 */
@Entity
@Table(name = "BB_ATTORNEY_USER_DETAILS")
public class AttorneyUserDetails extends BaseEntity {

	/** The serial version UID. */
	private static final long serialVersionUID = 5554741057349993132L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
	@JoinColumn(name = "BB_USER")
	private User user;

	@Column(name = "REGISTRATION_NO")
	private String registrationNo;

	/*- ------------------------------- getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

}
