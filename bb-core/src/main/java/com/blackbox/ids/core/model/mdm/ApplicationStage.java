/**
 *
 */
package com.blackbox.ids.core.model.mdm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.blackbox.ids.core.dto.mdm.DraftIdentityDTO;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mstr.Assignee;

/**
 * @author ajay2258
 */
@Entity
@Table(name = "BB_APPLICATION_STAGE",
uniqueConstraints = @UniqueConstraint(columnNames = { "BB_JURISDICTION", "APPLICATION_NUMBER" }))
public class ApplicationStage extends Application {

	private static final long serialVersionUID = -1041278372300204693L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_APPLICATION_ID")
	private Long id;

	/*- ------------------------------- Weak References -- */
	@Column(name = "BB_JURISDICTION")
	private String jurisdiction;

	@Column(name = "BB_ASSIGNEE")
	private String assignee;

	@Column(name = "BB_CUSTOMER")
	private String customer;

	@Column(name = "BB_ORGANIZATION")
	private String organization;

	@Column(name = "BB_ATTORNEY_DOCKET")
	private String attorneyDocketNumber;
	
	/** Export to Base table status */
	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private QueueStatus status;
	
	@Column(name = "VALIDATION_STATUS")
	@Enumerated(EnumType.STRING)
	private ApplicationValidationStatus validationStatus;
	
	@Transient
	private DraftIdentityDTO draftId;
	
	@Transient
	private Assignee appAssignee;

	public ApplicationStage() {}

	public ApplicationStage(String jurisdiction, String assignee,
			String customer, String organization, String attorneyDocketNumber,
			String applicationNumber, String familyId,
			ApplicationDetails appDetails,
			PublicationDetails publicationDetails, PatentDetails patentDetails,
			OrganizationDetails organizationDetails, Source source,
			SubSource subSource, String description) {
		super();
		this.jurisdiction = jurisdiction;
		this.assignee = assignee;
		this.customer = customer;
		this.organization = organization;
		this.attorneyDocketNumber = attorneyDocketNumber;
		this.setApplicationNumber(applicationNumber);
		this.setFamilyId(familyId);
		this.setAppDetails(appDetails);
		this.setPublicationDetails(publicationDetails);
		this.setPatentDetails(patentDetails);
		this.setOrganizationDetails(organizationDetails);
		this.setSource(source);
		this.setSubSource(subSource);
		this.setDescription(description);
	}

	/*- ---------------------------- getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getAttorneyDocketNumber() {
		return attorneyDocketNumber;
	}

	public void setAttorneyDocketNumber(String attorneyDocketNumber) {
		this.attorneyDocketNumber = attorneyDocketNumber;
	}

	public DraftIdentityDTO getDraftId() {
		return draftId;
	}

	public void setDraftId(DraftIdentityDTO draftId) {
		this.draftId = draftId;
	}

	public QueueStatus getStatus() {
		return status;
	}

	public void setStatus(QueueStatus status) {
		this.status = status;
	}

	public ApplicationValidationStatus getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(ApplicationValidationStatus validationStatus) {
		this.validationStatus = validationStatus;
	}

	public Assignee getAppAssignee() {
		return appAssignee;
	}

	public void setAppAssignee(Assignee appAssignee) {
		this.appAssignee = appAssignee;
	}

}
