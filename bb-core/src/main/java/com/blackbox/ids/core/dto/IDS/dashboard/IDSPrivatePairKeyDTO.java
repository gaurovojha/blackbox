package com.blackbox.ids.core.dto.IDS.dashboard;

import com.mysema.query.annotations.QueryProjection;

public class IDSPrivatePairKeyDTO {
	
	private Long dbID;
	private String keyType;
	private String filingPKIName;

	
	
	@QueryProjection
	public IDSPrivatePairKeyDTO(Long dbID, String keyType, String filingPKIName) {
		super();
		this.dbID = dbID;
		this.keyType = keyType;
		this.filingPKIName = filingPKIName;
	}

	/********************************Getters and Setters***************************/

	public Long getDbID() {
		return dbID;
	}
	public void setDbID(Long dbID) {
		this.dbID = dbID;
	}
	public String getKeyType() {
		return keyType;
	}
	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}
	public String getFilingPKIName() {
		return filingPKIName;
	}
	public void setFilingPKIName(String filingPKIName) {
		this.filingPKIName = filingPKIName;
	}
}
