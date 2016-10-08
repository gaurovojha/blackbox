/**
 *
 */
package com.blackbox.ids.ui.form.mdm;

import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationDetails;
import com.blackbox.ids.core.model.mdm.ApplicationScenario;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.OrganizationDetails;
import com.blackbox.ids.core.model.mdm.PatentDetails;
import com.blackbox.ids.core.model.mdm.PublicationDetails;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.Assignee.Entity;
import com.blackbox.ids.core.model.mstr.AttorneyDocketNumber;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.ui.form.base.EntityForm;

/**
 * @author ajay2258
 */
public class SingleApplicationForm implements EntityForm<ApplicationBase> {

	public static final TimestampFormat DATE_FORMAT = ApplicationForm.DATE_FORMAT;

	private String scenario;
	private Long id;
	private String applicationNo;
	private String applicationType;
	private Long jurisdictionId;
	private String jurisdictionName;
	private String familyId;

	private String customerNo;
	private String attorneyDocketNo;
	private String assignee;
	private String entity;
	private String filingDate;
	private String confirmationNo;
	private String title;
	private String publicationNumber;
	private String publicationDate;
	private String patentNumber;
	private String issueDate;
	private boolean exportControl;

	/** Indicates whether this particular child application record has been deleted on UI or not. */
	private boolean uiDeleted;
	private boolean firstFiling;
	private String subSource;

	/*- ------------------------------- Conversion methods with entity -- */
	public void updateData(MdmRecordDTO applicationDetails) {
		this.customerNo = applicationDetails.getCustomerNumber();
		this.assignee = applicationDetails.getAssignee();
		this.entity = applicationDetails.getEntity() == null ? null : applicationDetails.getEntity().name();
	}

	public SingleApplicationForm update(MdmRecordDTO application) {
		this.scenario = application.getScenario() == null ? null : application.getScenario().name();
		this.id = application.getDbId();
		this.applicationNo = application.getApplicationNumber();

		ApplicationType applicationType = application.getApplicationType();
		if (applicationType != null) {
			this.applicationType = application.getApplicationType().name();
			this.firstFiling = ApplicationType.isFirstFiling(applicationType);
		}

		this.jurisdictionName = application.getJurisdiction();
		this.familyId = application.getFamilyId();

		this.customerNo = application.getCustomerNumber();
		this.attorneyDocketNo = application.getAttorneyDocket();
		this.assignee = application.getAssignee();
		this.entity = application.getEntity() == null ? null : application.getEntity().name();
		this.filingDate = application.getFilingDate() == null ? null
				: BlackboxDateUtil.dateToStr(application.getFilingDate(), DATE_FORMAT);
		this.confirmationNo = application.getConfirmationNumber();
		this.title = application.getTitleOfInvention();
		this.publicationNumber = application.getPublicationNumber();
		this.publicationDate = application.getPublicationDate() == null ? null
				: BlackboxDateUtil.dateToStr(application.getPublicationDate(), DATE_FORMAT);
		this.patentNumber = application.getPatentNumber();
		this.issueDate = application.getIssuedOn() == null ? null
				: BlackboxDateUtil.dateToStr(application.getIssuedOn(), DATE_FORMAT);
		this.exportControl = application.isExportControl();
		this.subSource = application.getSubSource() == null ? null : application.getSubSource().name();
		return this;
	}

	@Override
	public ApplicationBase toEntity() {
		final ApplicationBase application = new ApplicationBase();

		application.setId(id);
		application.setScenario(ApplicationScenario.fromString(scenario));
		setApplicationReferences(application);
		setApplicationDetails(application.getAppDetails());
		setPublicationDetails(application.getPublicationDetails());
		setPatentDetails(application.getPatentDetails());
		setOrganizationDetails(application.getOrganizationDetails());

		return application;
	}

	public ApplicationStage toDraft() {
		ApplicationStage draft = new ApplicationStage();

		setDraftReferences(draft);
		setApplicationDetails(draft.getAppDetails());
		setPublicationDetails(draft.getPublicationDetails());
		setPatentDetails(draft.getPatentDetails());
		setOrganizationDetails(draft.getOrganizationDetails());

		return draft;
	}

	private void setDraftReferences(ApplicationStage draft) {
		draft.setJurisdiction(jurisdictionName);
		draft.setAssignee(assignee);
		draft.setCustomer(customerNo);
		draft.setAttorneyDocketNumber(attorneyDocketNo);
	}

	private void setOrganizationDetails(OrganizationDetails organizationDetails) {
		organizationDetails.setExportControl(exportControl);
		organizationDetails.setEntity(Entity.fromString(entity));
	}

	private void setPatentDetails(PatentDetails patentDetails) {
		patentDetails.setTitle(title);
		patentDetails.setPatentNumberRaw(patentNumber);
		if (issueDate != null) {
			patentDetails.setIssuedOn(BlackboxDateUtil.toCalendar(issueDate, DATE_FORMAT));
		}
	}

	private void setPublicationDetails(PublicationDetails publicationDetails) {
		publicationDetails.setPublicationNumberRaw(publicationNumber);
		if (publicationDate != null) {
			publicationDetails.setPublishedOn(BlackboxDateUtil.toCalendar(publicationDate, DATE_FORMAT));
		}
	}

	private void setApplicationDetails(ApplicationDetails appDetails) {
		appDetails.setApplicationNumberRaw(applicationNo);
		appDetails.setChildApplicationType(ApplicationType.fromString(applicationType));
		appDetails.setConfirmationNumber(confirmationNo);
		if (filingDate != null) {
			appDetails.setFilingDate(BlackboxDateUtil.toCalendar(filingDate, DATE_FORMAT));
		}
	}

	private void setApplicationReferences(ApplicationBase application) {
		// Jurisdiction
		application
		.setJurisdiction(new com.blackbox.ids.core.model.mstr.Jurisdiction(jurisdictionId, jurisdictionName));
		// Assignee
		application.setAssignee(new Assignee());
		application.getAssignee().setName(this.assignee);

		// Customer
		application.setCustomer(new Customer());
		application.getCustomer().setCustomerNumber(customerNo);

		// AttorneyDocketNumber
		application.setAttorneyDocketNumber(new AttorneyDocketNumber());
		application.getAttorneyDocketNumber().setSegment(attorneyDocketNo);
	}

	@Override
	public void load(ApplicationBase object) {
	}

	/*- ---------------------------- getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public Long getJurisdictionId() {
		return jurisdictionId;
	}

	public void setJurisdictionId(Long jurisdictionId) {
		this.jurisdictionId = jurisdictionId;
	}

	public String getJurisdictionName() {
		return jurisdictionName;
	}

	public void setJurisdictionName(String jurisdictionName) {
		this.jurisdictionName = jurisdictionName;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getAttorneyDocketNo() {
		return attorneyDocketNo;
	}

	public void setAttorneyDocketNo(String attorneyDocketNo) {
		this.attorneyDocketNo = attorneyDocketNo;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(String filingDate) {
		this.filingDate = filingDate;
	}

	public String getConfirmationNo() {
		return confirmationNo;
	}

	public void setConfirmationNo(String confirmationNo) {
		this.confirmationNo = confirmationNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublicationNumber() {
		return publicationNumber;
	}

	public void setPublicationNumber(String publicationNumber) {
		this.publicationNumber = publicationNumber;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getPatentNumber() {
		return patentNumber;
	}

	public void setPatentNumber(String patentNumber) {
		this.patentNumber = patentNumber;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public boolean isExportControl() {
		return exportControl;
	}

	public void setExportControl(boolean exportControl) {
		this.exportControl = exportControl;
	}

	public boolean isUiDeleted() {
		return uiDeleted;
	}

	public void setUiDeleted(boolean uiDeleted) {
		this.uiDeleted = uiDeleted;
	}

	public boolean isFirstFiling() {
		return firstFiling;
	}

	public void setFirstFiling(boolean firstFiling) {
		this.firstFiling = firstFiling;
	}

	public String getSubSource() {
		return subSource;
	}

	public void setSubSource(String subSource) {
		this.subSource = subSource;
	}

}
