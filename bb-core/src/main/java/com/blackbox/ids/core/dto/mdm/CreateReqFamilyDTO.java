package com.blackbox.ids.core.dto.mdm;

import java.util.Calendar;

import com.blackbox.ids.core.model.notification.process.EntityName;
import com.mysema.query.annotations.QueryProjection;

public class CreateReqFamilyDTO extends ActionsBaseDto{

	private String linkedFamilyId;

	private String linkedJurisdiction;

	private String linkedApplicationNo;

	private String source;
	
	
	@QueryProjection
	public CreateReqFamilyDTO(final Long notificationId,final Long entityId,final EntityName entityName,final String jurisdiction,final String ApplicationNum,final Calendar notifiedDate,final String linkedFamilyId,final String linkedJurisdiction,
								final String linkedApplicationNumber) 
	{
		super(jurisdiction,ApplicationNum,notifiedDate,notificationId,entityId,entityName.name());
		
		this.linkedFamilyId = linkedFamilyId;
		this.linkedJurisdiction = linkedJurisdiction;
		this.linkedApplicationNo = linkedApplicationNumber;
		this.source = "Private PAIR";
		
	}
	

	/**
	 * @return the linkedFamilyId
	 */
	public String getLinkedFamilyId() {
		return linkedFamilyId;
	}

	/**
	 * @param linkedFamilyId the linkedFamilyId to set
	 */
	public void setLinkedFamilyId(String linkedFamilyId) {
		this.linkedFamilyId = linkedFamilyId;
	}

	/**
	 * @return the linkedJurisdiction
	 */
	public String getLinkedJurisdiction() {
		return linkedJurisdiction;
	}

	/**
	 * @param linkedJurisdiction the linkedJurisdiction to set
	 */
	public void setLinkedJurisdiction(String linkedJurisdiction) {
		this.linkedJurisdiction = linkedJurisdiction;
	}

	/**
	 * @return the linkedApplicationNo
	 */
	public String getLinkedApplicationNo() {
		return linkedApplicationNo;
	}

	/**
	 * @param linkedApplicationNo the linkedApplicationNo to set
	 */
	public void setLinkedApplicationNo(String linkedApplicationNo) {
		this.linkedApplicationNo = linkedApplicationNo;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

}
