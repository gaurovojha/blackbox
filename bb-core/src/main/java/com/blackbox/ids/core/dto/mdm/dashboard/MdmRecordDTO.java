
/**
 *
 */
package com.blackbox.ids.core.dto.mdm.dashboard;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.mdm.Application.SubSource;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationScenario;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.PatentDetails;
import com.blackbox.ids.core.model.mdm.PublicationDetails;
import com.blackbox.ids.core.model.mstr.Assignee.Entity;
import com.blackbox.ids.core.model.mstr.ProsecutionStatus;
import com.mysema.query.annotations.QueryProjection;

/**
 * @author ajay2258
 *
 */
public class MdmRecordDTO {

	private Long dbId;

	private String jurisdiction;

	private String applicationNumber;

	private String familyId;

	private ApplicationScenario scenario;

	private String attorneyDocket;

	private Date filingDate;

	private String assignee;

	private String customerNumber;

	private String examinerName;

	private String groupArtUnit;

	private String confirmationNumber;

	private String publicationNumber;

	private Date publicationDate;

	private Date patentDate;

	private String patentNumber;

	private String firstNamedInventor;

	private String entityStatus;

	private String titleOfInvention;

	private ApplicationType applicationType;

	private String lockedBy;

	private MDMRecordStatus status;

	private String userComment;

	private String createdBy;

	private Date createdOn;

	private String modifiedBy;

	private Date modifiedOn;

	private boolean familyMembers;

	private MDMRecordStatus newStatus;

	private String viewName;

	private ProsecutionStatus prosecutionStatus ;

	/*- ------------------------ Fields used in create/update application -- */
	private Entity entity;

	private Date issuedOn;

	private int countAffectedTransaction;

	private boolean exportControl;

	private SubSource subSource;

	private boolean nonEditable;

	private Long notificationId;

	public MdmRecordDTO() {
		super();
	}

	public MdmRecordDTO(Long dbId, String jurisdiction, String applicationNumber, String familyId,
			String attorneyDocket, String assignee, ApplicationType applicationType, MDMRecordStatus status) {
		super();
		this.dbId = dbId;
		this.jurisdiction = jurisdiction;
		this.applicationNumber = applicationNumber;
		this.familyId = familyId;
		this.attorneyDocket = attorneyDocket;
		this.assignee = assignee;
		this.applicationType = applicationType;
		this.status = status;
	}

	@QueryProjection
	public MdmRecordDTO(Long dbId, String jurisdiction, String applicationNumber, String familyId,
			String attorneyDocket, Calendar filingDate, String assignee, ApplicationType applicationType, Calendar createdOn, String userFirstName , String userLastName) {
		super();
		this.dbId = dbId;
		this.jurisdiction = jurisdiction;
		this.applicationNumber = applicationNumber;
		this.familyId = familyId;
		this.attorneyDocket = attorneyDocket;
		this.filingDate = filingDate == null ? null : filingDate.getTime();
		this.assignee = assignee;
		this.applicationType = applicationType;
		this.createdOn = createdOn == null ? null : createdOn.getTime();
		this.createdBy = userFirstName + " " +userLastName;
	}


	/*@QueryProjection
	public MdmRecordDTO(Long dbId, String jurisdiction, String applicationNumber, String familyId,
			String attorneyDocket, Calendar filingDate, String assignee, ApplicationType applicationType, Calendar modifiedOn, String modifiedBy , MDMRecordStatus status, MDMRecordStatus newRecordStatus) {
		super();
		this.dbId = dbId;
		this.jurisdiction = jurisdiction;
		this.applicationNumber = applicationNumber;
		this.familyId = familyId;
		this.attorneyDocket = attorneyDocket;
		this.filingDate = filingDate.getTime();
		this.assignee = assignee;
		this.applicationType = applicationType;
		this.modifiedOn = modifiedOn.getTime();
		this.modifiedBy = createdBy;
		this.status = status;
		this.newStatus = newRecordStatus;
	}*/

	@QueryProjection
	public MdmRecordDTO(Long dbId, String jurisdiction, String applicationNumber, String familyId
			,String attorneyDocket, Calendar filingDate, String assignee, ApplicationType applicationType
			, Calendar modifiedOn, String userFirstName, String userLastName, MDMRecordStatus status, MDMRecordStatus newRecordStatus, String userComment) {
		super();
		this.dbId = dbId;
		this.jurisdiction = jurisdiction;
		this.applicationNumber = applicationNumber;
		this.familyId = familyId;
		this.attorneyDocket = attorneyDocket;
		this.filingDate = filingDate == null ? null : filingDate.getTime();
		this.assignee = assignee;
		this.applicationType = applicationType;
		this.modifiedOn = modifiedOn == null ? null : modifiedOn.getTime();
		this.modifiedBy = userFirstName + " " + userLastName ;
		this.status = status;
		this.newStatus = newRecordStatus;
		this.userComment = userComment;
	}


	@QueryProjection
	public MdmRecordDTO(final Long id, final String customerNo, final String docketNo, final String assignee, final Entity entity,
			final Calendar filedOn, final String confirmationNo, final String patentNo, Calendar issuedOn) {

		this.dbId = id;
		this.customerNumber = customerNo;
		this.attorneyDocket = docketNo;
		this.assignee = assignee;
		this.entity = entity;
		this.filingDate = filedOn == null ? null : filedOn.getTime();
		this.confirmationNumber = confirmationNo;
		this.patentNumber = patentNo;
		this.issuedOn = issuedOn == null ? null : issuedOn.getTime();
	}

	@QueryProjection
	public MdmRecordDTO(final Long applicationId, final String assignee, final String customerNo, final Entity entity) {
		super();
		this.dbId = applicationId;
		this.customerNumber = customerNo;
		this.assignee = assignee;
		this.entity = entity;
	}

	@QueryProjection
	public MdmRecordDTO(final ApplicationBase application) {
		super();
		this.dbId = application.getId();
		this.familyId = application.getFamilyId();
		this.jurisdiction = application.getJurisdiction().getCode();
		this.applicationNumber = application.getAppDetails().getApplicationNumberRaw();
		this.applicationType = application.getAppDetails().getChildApplicationType();
		this.scenario = application.getScenario();

		this.customerNumber = application.getCustomer().getCustomerNumber();
		this.attorneyDocket = application.getAttorneyDocketNumber().getSegment();
		this.assignee = application.getAssignee().getName();
		this.entity = application.getOrganizationDetails().getEntity();
		this.filingDate = application.getAppDetails().getFilingDate().getTime();
		this.confirmationNumber = application.getAppDetails().getConfirmationNumber();
		this.titleOfInvention = application.getPatentDetails().getTitle();
		this.exportControl = application.getOrganizationDetails().isExportControl();

		PublicationDetails publicationDetails = application.getPublicationDetails();
		if (publicationDetails != null) {
			this.publicationNumber = publicationDetails.getPublicationNumberRaw();
			this.publicationDate = publicationDetails.getPublishedOn() == null ? null
					: publicationDetails.getPublishedOn().getTime();
		}

		PatentDetails patentDetails = application.getPatentDetails();
		if (patentDetails != null) {
			this.patentNumber = patentDetails.getPatentNumberRaw();
			this.issuedOn = patentDetails.getIssuedOn() == null ? null : patentDetails.getIssuedOn().getTime();
		}
		this.subSource = application.getSubSource();
		this.prosecutionStatus = application.getOrganizationDetails().getProsecutionStatus();
	}


	public MdmRecordDTO(final ApplicationStage application) {
		super();
		//this.dbId = application.getId();
		this.familyId = application.getFamilyId();
		this.jurisdiction = application.getJurisdiction();
		this.applicationNumber = application.getAppDetails().getApplicationNumberRaw();
		this.applicationType = application.getAppDetails().getChildApplicationType();
		//this.scenario = application.getScenario();

		this.customerNumber = application.getCustomer();
		this.attorneyDocket = application.getAttorneyDocketNumber();
		this.assignee = application.getAssignee();
		//this.entity = application.getEntity();
		this.filingDate = application.getAppDetails().getFilingDate() == null ? null : application.getAppDetails().getFilingDate().getTime();
		this.confirmationNumber = application.getAppDetails() == null ? "" : application.getAppDetails().getConfirmationNumber();
		this.titleOfInvention = application.getPatentDetails() == null ? "" : application.getPatentDetails().getTitle();
		this.exportControl = application.getOrganizationDetails() == null ? false : application.getOrganizationDetails().isExportControl();

		PublicationDetails publicationDetails = application.getPublicationDetails();
		if (publicationDetails != null) {
			this.publicationNumber = publicationDetails.getPublicationNumberRaw();
			this.publicationDate = publicationDetails.getPublishedOn() == null ? null
					: publicationDetails.getPublishedOn().getTime();
		}

		PatentDetails patentDetails = application.getPatentDetails();
		if (patentDetails != null) {
			this.patentNumber = patentDetails.getPatentNumberRaw();
			this.issuedOn = patentDetails.getIssuedOn() == null ? null : patentDetails.getIssuedOn().getTime();
		}
		this.subSource = application.getSubSource();
	}

	/*- ----------------------------------- getter-setters -- */
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

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public ApplicationScenario getScenario() {
		return scenario;
	}

	public void setScenario(ApplicationScenario scenario) {
		this.scenario = scenario;
	}

	public String getAttorneyDocket() {
		return attorneyDocket;
	}

	public void setAttorneyDocket(String attorneyDocket) {
		this.attorneyDocket = attorneyDocket;
	}

	public Date getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(Date filingDate) {
		this.filingDate = filingDate;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getExaminerName() {
		return examinerName;
	}

	public void setExaminerName(String examinerName) {
		this.examinerName = examinerName;
	}

	public String getGroupArtUnit() {
		return groupArtUnit;
	}

	public void setGroupArtUnit(String groupArtUnit) {
		this.groupArtUnit = groupArtUnit;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getPublicationNumber() {
		return publicationNumber;
	}

	public void setPublicationNumber(String publicationNumber) {
		this.publicationNumber = publicationNumber;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Date getPatentDate() {
		return patentDate;
	}

	public void setPatentDate(Date patentDate) {
		this.patentDate = patentDate;
	}

	public String getPatentNumber() {
		return patentNumber;
	}

	public void setPatentNumber(String patentNumber) {
		this.patentNumber = patentNumber;
	}

	public String getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}

	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public MDMRecordStatus getStatus() {
		return status;
	}

	public void setStatus(MDMRecordStatus status) {
		this.status = status;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getFirstNamedInventor() {
		return firstNamedInventor;
	}

	public void setFirstNamedInventor(String firstNamedInventor) {
		this.firstNamedInventor = firstNamedInventor;
	}

	public String getTitleOfInvention() {
		return titleOfInvention;
	}

	public void setTitleOfInvention(String titleOfInvention) {
		this.titleOfInvention = titleOfInvention;
	}

	public boolean isFamilyMembers() {
		return familyMembers;
	}

	public void setFamilyMembers(boolean familyMembers) {
		this.familyMembers = familyMembers;
	}

	public Date getIssuedOn() {
		return issuedOn;
	}

	public void setIssuedOn(Date issuedOn) {
		this.issuedOn = issuedOn;
	}


	public int getCountAffectedTransaction() {
		return countAffectedTransaction;
	}

	public void setCountAffectedTransaction(int countAffectedTransaction) {
		this.countAffectedTransaction = countAffectedTransaction;
	}

	public MDMRecordStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(MDMRecordStatus newStatus) {
		this.newStatus = newStatus;
	}

	public boolean isExportControl() {
		return exportControl;
	}

	public void setExportControl(boolean exportControl) {
		this.exportControl = exportControl;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public ProsecutionStatus getProsecutionStatus() {
		return prosecutionStatus;
	}

	public void setProsecutionStatus(ProsecutionStatus prosecutionStatus) {
		this.prosecutionStatus = prosecutionStatus;
	}

	public SubSource getSubSource() {
		return subSource;
	}

	public void setSubSource(SubSource subSource) {
		this.subSource = subSource;
	}

	public boolean isNonEditable() {
		return nonEditable;
	}

	public void setNonEditable(boolean nonEditable) {
		this.nonEditable = nonEditable;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

}
