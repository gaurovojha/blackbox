package com.blackbox.ids.core.dto.mdm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.blackbox.ids.core.model.notification.process.EntityName;
import com.mysema.query.annotations.QueryProjection;

/**
 * The {@link UpdateReqAssigneeDTO} holds all the attributes in MDM update request assignee records list.
 * 
 * @author tusharagarwal
 *
 */

public class UpdateReqAssigneeDTO  extends ActionsBaseDto {
	
	private String familyId;
	
	private String attorneyDocketNumber;
		
	@QueryProjection
	public UpdateReqAssigneeDTO(final Long notificationId,final long entityId,final EntityName entityName,final String familyId,final String jurisdiction,final String applicationNo,final String attorneyDocketNumber,final Calendar notifiedDate) {
		super(jurisdiction,applicationNo,notifiedDate,notificationId,entityId,entityName.name());
		this.familyId = familyId;
		this.attorneyDocketNumber = attorneyDocketNumber;	
		
	}
	
	public UpdateReqAssigneeDTO(){}

	/*-- getters - setters--*/
	
	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getAttorneyDocketNumber() {
		return attorneyDocketNumber;
	}

	public void setAttorneyDocketNumber(String attorneyDocketNumber) {
		this.attorneyDocketNumber = attorneyDocketNumber;
	}
}
