/**
 *
 */
package com.blackbox.ids.core.dto.IDS;

import com.mysema.query.annotations.QueryProjection;

/**
 * Holds the IDS actor details.
 *
 * @author ajay2258
 */
public class UserDetails {

	private long dbId;
	private String name;
	private String registrationNo;

	/*- ---------------------------- Constructors -- */
	@QueryProjection
	public UserDetails(final long dbId, final String name, final String registrationNo) {
		super();
		this.dbId = dbId;
		this.name = name;
		this.registrationNo = registrationNo;
	}

	/*- ---------------------------- getter-setters -- */
	public long getDbId() {
		return dbId;
	}

	public void setDbId(long dbId) {
		this.dbId = dbId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

}
