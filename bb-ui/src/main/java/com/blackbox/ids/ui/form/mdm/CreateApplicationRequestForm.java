/**
 * 
 */
package com.blackbox.ids.ui.form.mdm;

/**
 * @author tusharagarwal
 *
 */
public class CreateApplicationRequestForm {
	

	public static final String MODEL_NAME = "createApplicationRequestForm";
	
	private String jurisdiction;
	
	private String applicationNo;
	
	private String familyId;
	
	private Long correspondenceId;
	
	private Long notificationId;
	
	// getter-setters
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

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public Long getCorrespondenceId() {
		return correspondenceId;
	}

	public void setCorrespondenceId(Long correspondenceId) {
		this.correspondenceId = correspondenceId;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

}
