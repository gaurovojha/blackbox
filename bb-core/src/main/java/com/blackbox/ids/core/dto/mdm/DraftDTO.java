/**
 * 
 */
package com.blackbox.ids.core.dto.mdm;

import java.util.Calendar;
import java.util.Date;

import com.mysema.query.annotations.QueryProjection;

/**
 * The {@link DraftDTO} holds all the attributes in MDM draft records list.
 * 
 * @author tusharagarwal
 *
 */
public class DraftDTO {
	
	private Long dbId;
	private String jurisdiction;
	private String applicationNumber;
	private Date modifiedOn;
	
	@QueryProjection
	public DraftDTO(final Long id, final String jurisdiction, String applicationNumber, final Calendar modifiedOn) {
		super();
		this.dbId = id;
		this.jurisdiction = jurisdiction;
		this.applicationNumber = applicationNumber;
		this.modifiedOn = modifiedOn.getTime();
	}
	
	/*- -------------------------- getter-setters -- */
	public Long getDbId() {
		return dbId;
	}
	
	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}
	
	public String getJurisdiction() {
		return jurisdiction;
	}
	
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}
	
	public String getApplicationNumber() {
		return applicationNumber;
	}
	
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	

}
