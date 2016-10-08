/**
 * 
 */
package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.Calendar;

import com.blackbox.ids.core.model.mstr.IDSRelevantStatus;
import com.blackbox.ids.core.model.mstr.ProsecutionStatus;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.mysema.query.annotations.QueryProjection;

/**
 * @author nagarro
 *
 */
public class IDSAttorneyApprovalDTO extends IDSDashBoardBaseDTO {

     
	/** The prosecution status. */
	private String prosecutionStatusAttorneyApproval;
	
	/** The last ids filling date. */
	private String lastIDSFillingDate;
	
	/** The reference count. */
	private Long referenceCount;
	
	/** The ids preparedby. */
	private String idsPreparedby;
	
	/** The attorney approval comments. */
	private String attorneyApprovalComments;
	
	private String applicationID;
	
	private Long notificationProcessId;
	
	
	/**
	 * Default constructor
	 */
	public IDSAttorneyApprovalDTO() {
		// TODO Auto-generated constructor stub
	}
	

	@QueryProjection
	public IDSAttorneyApprovalDTO(final Long dbId , final String familyId, final String jurisdiction, final String applicationNo ,
			final IDSRelevantStatus prosecutionStatus, final Calendar lastIDSFillingDateCal, 
			final Long referenceCount,final Long idsPreparedby, final String attorneyApprovalComments,final Long applicationID, final Long notificationProcessId){
		super(dbId, familyId, jurisdiction, applicationNo, prosecutionStatus);
		this.lastIDSFillingDate= BlackboxDateUtil.calendarToStr(lastIDSFillingDateCal, TimestampFormat.DDMMMYYYY);
		this.referenceCount= referenceCount;
		this.idsPreparedby = idsPreparedby.toString();
		this.attorneyApprovalComments = attorneyApprovalComments;
		this.applicationID = applicationID.toString();
		this.notificationProcessId = notificationProcessId;
		
	}
	
	@QueryProjection
	public IDSAttorneyApprovalDTO(final Long dbId ,final String familyId, final String jurisdiction, final String applicationNo ,final IDSRelevantStatus prosecutionStatus)
	{
		super(dbId, familyId, jurisdiction, applicationNo, prosecutionStatus);
	}


	public String getLastIDSFillingDate() {
		return lastIDSFillingDate;
	}


	public void setLastIDSFillingDate(String lastIDSFillingDate) {
		this.lastIDSFillingDate = lastIDSFillingDate;
	}


	public Long getReferenceCount() {
		return referenceCount;
	}


	public void setReferenceCount(Long referenceCount) {
		this.referenceCount = referenceCount;
	}


	public String getIdsPreparedby() {
		return idsPreparedby;
	}


	public void setIdsPreparedby(String idsPreparedby) {
		this.idsPreparedby = idsPreparedby;
	}


	public String getAttorneyApprovalComments() {
		return attorneyApprovalComments;
	}


	public void setAttorneyApprovalComments(String attorneyApprovalComments) {
		this.attorneyApprovalComments = attorneyApprovalComments;
	}

	public String getProsecutionStatusAttorneyApproval() {
		return prosecutionStatusAttorneyApproval;
	}

	public void setProsecutionStatusAttorneyApproval(String prosecutionStatusAttorneyApproval) {
		this.prosecutionStatusAttorneyApproval = prosecutionStatusAttorneyApproval;
	}

	public String getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}

	public Long getNotificationProcessId() {
		return notificationProcessId;
	}

	public void setNotificationProcessId(Long notificationProcessId) {
		this.notificationProcessId = notificationProcessId;
	}
	
	

}
