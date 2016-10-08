/**
 *
 */
package com.blackbox.ids.core.dto.mdm;

import com.mysema.query.annotations.QueryProjection;

/**
 * @author ajay2258
 */
public class DraftIdentityDTO {

	private String jurisdiction;

	private String applicationNo;

	private Long userId;

	@QueryProjection
	public DraftIdentityDTO(final String jurisdiction, final String applicationNo, final Long userId) {
		super();
		this.jurisdiction = jurisdiction;
		this.applicationNo = applicationNo;
		this.userId = userId;
	}

	/*- ----------------------------------- Overridden Method -- */
	@Override
	public String toString() {
		return this.jurisdiction.concat(", ").concat(applicationNo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationNo == null) ? 0 : applicationNo.hashCode());
		result = prime * result + ((jurisdiction == null) ? 0 : jurisdiction.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DraftIdentityDTO other = (DraftIdentityDTO) obj;
		if (applicationNo == null) {
			if (other.applicationNo != null) {
				return false;
			}
		} else if (!applicationNo.equalsIgnoreCase(other.applicationNo)) {
			return false;
		}
		if (jurisdiction == null) {
			if (other.jurisdiction != null) {
				return false;
			}
		} else if (!jurisdiction.equalsIgnoreCase(other.jurisdiction)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}

	/*- ----------------------------------- getter-setters -- */
	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
