/**
 *
 */
package com.blackbox.ids.core.model.mdm;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.AttorneyDocketNumber;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.mstr.Organization;
import com.blackbox.ids.core.model.mstr.TechnologyGroup;

/**
 * @author ajay2258
 */
@Entity
@Table(name = "BB_APPLICATION_BASE", uniqueConstraints = @UniqueConstraint(columnNames = { "BB_JURISDICTION",
"APPLICATION_NUMBER" }) )
public class ApplicationBase extends Application {

	private static final long serialVersionUID = 1683894174701908592L;

	public static final String PREFIX_FAMILY_ID = "F";
	public static final String SEQ_FAMILY_ID = "BBX_FAMILY_ID_SEQ";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_APPLICATION_ID")
	private Long id;

	/*- ------------------------------- Foreign References -- */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BB_JURISDICTION", referencedColumnName = "BB_JURISDICTION_ID", nullable = false)
	private Jurisdiction jurisdiction;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_ASSIGNEE", referencedColumnName = "BB_ASSIGNEE_ID", nullable = false, updatable = true)
	private Assignee assignee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_CUSTOMER", referencedColumnName = "BB_CUSTOMER_ID", nullable = false)
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_ORGANIZATION", referencedColumnName = "BB_ORGANIZATION_ID", nullable = false)
	private Organization organization;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_TECHNOLOGY_GROUP", referencedColumnName = "BB_TECHNOLOGY_GROUP_ID", nullable = false)
	private TechnologyGroup technologyGroup;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "BB_ATTORNEY_DOCKET", referencedColumnName = "BB_ATTORNEY_DOCKET_NUMBER_ID", nullable = false)
	private AttorneyDocketNumber attorneyDocketNumber;

	/*- ------------------------------- Base Only Data -- */
	@Column(name = "RECORD_STATUS")
	@Enumerated(EnumType.STRING)
	private MDMRecordStatus recordStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "SCENARIO", nullable = true)
	private ApplicationScenario scenario;

	@Column(name = "BB_NEW_RECORD_STATUS", nullable=true)
	@Enumerated(EnumType.STRING)
	private MDMRecordStatus newRecordStatus;

	/*- ----User Comments -- */
	@Column(name = "BB_COMMENT")
	private String userComment;

	@OneToMany(mappedBy = "application")
	private Set<CorrespondenceBase> correspondenceBase;

	@Version
	@Column(name = "VERSION")
	private int version;

	/*- ---------------------------- Constructors -- */
	public ApplicationBase() {
		super();
	}

	public ApplicationBase(final Long dbId) {
		super();
		this.id = dbId;
	}

	/*- ---------------------------- getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Jurisdiction getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(Jurisdiction jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public Assignee getAssignee() {
		return assignee;
	}

	public void setAssignee(Assignee assignee) {
		this.assignee = assignee;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public TechnologyGroup getTechnologyGroup() {
		return technologyGroup;
	}

	public void setTechnologyGroup(TechnologyGroup technologyGroup) {
		this.technologyGroup = technologyGroup;
	}

	public AttorneyDocketNumber getAttorneyDocketNumber() {
		return attorneyDocketNumber;
	}

	public void setAttorneyDocketNumber(AttorneyDocketNumber attorneyDocketNumber) {
		this.attorneyDocketNumber = attorneyDocketNumber;
	}

	public MDMRecordStatus getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(MDMRecordStatus recordStatus) {
		this.recordStatus = recordStatus;
	}

	public ApplicationScenario getScenario() {
		return scenario;
	}

	public void setScenario(ApplicationScenario scenario) {
		this.scenario = scenario;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	public MDMRecordStatus getNewRecordStatus() {
		return newRecordStatus;
	}

	public void setNewRecordStatus(MDMRecordStatus newRecordStatus) {
		this.newRecordStatus = newRecordStatus;
	}

	public Set<CorrespondenceBase> getCorrespondenceBase() {
		return correspondenceBase;
	}

	public void setCorrespondenceBase(Set<CorrespondenceBase> correspondenceBase) {
		this.correspondenceBase = correspondenceBase;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
