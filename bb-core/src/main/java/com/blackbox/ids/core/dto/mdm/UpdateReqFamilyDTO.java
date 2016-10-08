package com.blackbox.ids.core.dto.mdm;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.model.notification.process.EntityName;
import com.mysema.query.annotations.QueryProjection;

/**
 * The {@link UpdateReqFamilyDTO} holds all the attributes in MDM update request family linkage records list.
 *
 * @author tusharagarwal
 *
 */

public class UpdateReqFamilyDTO extends ActionsBaseDto {

	private Long dbId;

	private String createdBy;

	private Date createdOn;

	private String familyId;

	private String linkedFamilyId;

	private String linkedJurisdiction;

	private String linkedApplicationNumber;

	private String source;

	@QueryProjection
	public UpdateReqFamilyDTO(final Long dbId,final Long notificationId,final Long entityId,final EntityName entityName,final String jurisdiction,final String ApplicationNum,final Calendar notifiedDate, final String userFirstName , final String userLastName ,final Calendar createdOn,final String familyId,final String linkedFamilyId,final String linkedJurisdiction,
								final String linkedApplicationNumber)
	{
		super(jurisdiction,ApplicationNum,notifiedDate,notificationId,entityId,entityName.name());
		this.dbId = dbId;
		this.createdBy = userFirstName + " " + userLastName;
		this.createdOn = createdOn.getTime();
		this.familyId = familyId;
		this.linkedFamilyId = linkedFamilyId;
		this.linkedJurisdiction = linkedJurisdiction;
		this.linkedApplicationNumber = linkedApplicationNumber;
		this.source = "Private PAIR";

	}

	/*---getters - setters ---*/

	
	public Long getDbId() {
		return dbId;
	}

	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public UpdateReqFamilyDTO(){}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getLinkedFamilyId() {
		return linkedFamilyId;
	}

	public void setLinkedFamilyId(String linkedFamilyId) {
		this.linkedFamilyId = linkedFamilyId;
	}

	public String getLinkedJurisdiction() {
		return linkedJurisdiction;
	}

	public void setLinkedJurisdiction(String linkedJurisdiction) {
		this.linkedJurisdiction = linkedJurisdiction;
	}

	public String getLinkedApplicationNumber() {
		return linkedApplicationNumber;
	}

	public void setLinkedApplicationNumber(String linkedApplicationNumber) {
		this.linkedApplicationNumber = linkedApplicationNumber;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}



}
