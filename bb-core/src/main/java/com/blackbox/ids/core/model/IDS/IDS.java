/**
 *
 */
package com.blackbox.ids.core.model.IDS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Jurisdiction;

/**
 * The class <code>IDS</code> outlines the attributes for an IDS. Please note that {@link #idsBuildId} indicates whether
 * the IDS instance is build within or outside the <b>Blackbox</b>. Non-null value for <i>idsBuildId</i> will be
 * associated with only internal IDS instances.
 *
 * @author ajay2258
 */
@Entity
@Table(name = "BB_IDS")
public class IDS extends BaseEntity {

	public enum Status {
		/** From IDS Build till IDS is submitted for Approval. */
		IN_PROGRESS,

		/** From IDS Submitted for Approval till IDS is Approved. */
		PENDING_APPROVAL,

		/** When IDS is Approved by Attorney. */
		APPROVED,

		/** When Generate File action is initiated, Till the IDS Files are generated. */
		GENERATING_FILE_PACKAGE,

		/** When IDS is filed by System. */
		FILED,

		/** Post status for IDS, generated from system and filed outside system. */
		PENDING_FILING,

		/** When the IDS Filing Channel is MANUAL, once the file is generated, status of IDS will be updated to this. */
		FILE_PACKAGE_GENERATED,

		/** When user intentionally decides to discard the IDS build. */
		DISCARDED,

		/** When the SYSTEM fails to file the IDS. */
		FILING_FAILED,

		/**
		 * When User takes action, IDS WITHDRAWN where filing channel is MANUAL, status of IDS Changes to IDS Withdrawn.
		 * In such case, the related Reference flows – status changes to previous state – either UNCITED – NULL or
		 * UNCITED – CITED IN PARENT.
		 */
		WITHDRAWN,

		/**
		 * When system is not able to generate file and failure occurs.
		 */
		FILE_GENERATION_FAILED,

	}
	
	public enum SubStatus {
		/**
		 * Populate value FAILED in IDS TRACKING STATUS – REQUEST 1 field in IDS BASE TABLE
		 */
		TRACKING_STATUS_REQUEST1_FAILED,

		/**
		 * Populate value FAILED in IDS TRACKING STATUS – REQUEST 2 field in IDS BASE TABLE
		 */
		TRACKING_STATUS_REQUEST2_FAILED,
		
		/**
		 * Populate value SUCCESS in IDS TRACKING STATUS – REQUEST 1 field in IDS BASE TABLE
		 */
		TRACKING_STATUS_REQUEST1_SUCCESS,

		/**
		 * Populate value SUCCESS in IDS TRACKING STATUS – REQUEST 2 field in IDS BASE TABLE
		 */
		TRACKING_STATUS_REQUEST2_SUCCESS;
	}

	public enum FilingChannel {
		MANUAL, SYSTEM
	}

	/*- ------------------------- Initials and Sequence Names -- */
	public enum Sequence {
		BUILD_ID("IDS", "BBX_IDS_BUILD_ID_SEQ"),
		INTERNAL_FINAL_ID("InIDS", "BBX_IDS_INTERNAL_FINAL_ID_SEQ"),
		EXTERNAL_FINAL_ID("ExIDS", "BBX_IDS_EXTERNAL_FINAL_ID_SEQ");

		public final String prefix;
		public final String seqName;

		private Sequence(final String prefix, final String seqName) {
			this.prefix = prefix;
			this.seqName = seqName;
		}
	}


	/** The serial version UID. */
	private static final long serialVersionUID = -8714633947316082509L;

	/** Database primary key. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_IDS_ID", updatable = false)
	private Long id;

	/**
	 * System Generated sequence for each IDS Build. Created and stamped on selected reference flows when user takes
	 * action “Skip & Proceed” or “Continue” after Initiate IDS.
	 */
	@Column(name = "IDS_BUILD_ID", nullable = true)
	private String idsBuildId;

	/** Current status of IDS. */
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private Status status;

	/*- ------------------------------------ Approval Information -- */
	/** Timestamp when paralegal raised approval request to the attorney. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVAL_REQUESTED_ON")
	private Calendar approvalRequestedOn;

	/** Paralegal user who has raised request to attorney for IDS approval. */
	@Column(name = "APPROVAL_REQUESTED_BY")
	private Long approvalRequestedBy;

	/** Timestamp when IDS was last approved on. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVED_ON")
	private Calendar approvedOn;

	/**
	 * User/attorney who approved the IDS or the one, requested to approve the IDS. Exact meaning is derived in
	 * combination with current IDS status.
	 */
	@Column(name = "APPROVED_BY")
	private Long approvedBy;

	/*- ------------------------------------ Filing Information -- */
	/** Timestamp when the File Generation action is taken. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILE_GENERATION_INITIATED_ON")
	private Calendar fileGenerationInitiatedOn;

	/** Timestamp when the File Generation action is completed. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILE_GENERATED_ON")
	private Calendar fileGeneratedOn;

	/** User who initiated IDS File Generation. */
	@Column(name = "FILE_GENERATED_BY")
	private Long fileGeneratedBy;

	/** Channel for IDS filing. */
	@Enumerated(EnumType.STRING)
	@Column(name = "FILING_CHANNEL")
	private FilingChannel filingChannel;

	/** Holds the IDS application fee, calculated at the time user fills in the certification statement. */
	@Column(name = "FILING_FEE")
	private Double filingFee;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "BB_CERTIFICATION_STATEMENT", referencedColumnName = "BB_CERTIFICATION_STATEMENT_ID",
	nullable = true)
	private CertificationStatement certificate;

	/** Filing information for IDS filed successfully through system. */
	@OneToMany(mappedBy = "ids")
	private List<IDSFilingInfo> filingInfos;

	/** Application instance, whose IDS is this object. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "BB_APPLICATION", referencedColumnName = "BB_APPLICATION_ID", nullable = false,
	updatable = false)
	private ApplicationBase application;


	/** Date of successful upload for IDS file. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_FILED_ON")
	private Calendar lastFiledOn;
	
	
	/** Current status of IDS. */
	@Enumerated(EnumType.STRING)
	@Column(name = "SUB_STATUS")
	private SubStatus subStatus;
	
	/*- ------------------------------------ Approval Information -- */
	/** Timestamp when paralegal instruct filing . */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILING_INSTRUCTED_ON")
	private Calendar filingInstructedOn;
	
	/** User who files IDS  */
	@Column(name = "IDS_FILED_BY")
	private Long filedBy;
	
	/*Comments put on by attorney when requesting change from parallegal*/
	@Column(name = "ATTORNEY_COMMENTS", nullable = true)
	private String attorneyComments;
	
	/*private pair key used while filing a IDS*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_PRIVATE_PAIR_KEY", referencedColumnName = "BB_PRIVATE_PAIR_KEY_ID", nullable = true)
	private IDSFilingPrivatePair idsFilingPrivatePair;
	
	/*- ------------------------------------ Constructor -- */
	public IDS() {
		super();
	}

	public IDS(Long dbId) {
		super();
		this.id = dbId;
	}

	public IDS(String idsBuildId, ApplicationBase application, Status status) {
		super();
		this.idsBuildId = idsBuildId;
		this.application = application;
		this.status = status;
	}

	/*- ------------------------ JPA Callbacks -- */
	@Override
	@PrePersist
	public void prePersist() {
		super.prePersist();
		setLastFilingDate();
	}

	@Override
	@PreUpdate
	public void preUpdate() {
		super.preUpdate();
		setLastFilingDate();
	}

	private void setLastFilingDate() {
		if (filingInfos != null && !filingInfos.isEmpty()) {
			List<Calendar> filingDate = new ArrayList<>();
			for (IDSFilingInfo filingInfo : this.filingInfos) {
				filingDate.add(filingInfo.getFilingDate());
			}
			this.lastFiledOn = Collections.max(filingDate);
		}
	}

	/*- ------------------------------------ getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdsBuildId() {
		return idsBuildId;
	}

	public void setIdsBuildId(String idsBuildId) {
		this.idsBuildId = idsBuildId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Calendar getApprovalRequestedOn() {
		return approvalRequestedOn;
	}

	public void setApprovalRequestedOn(Calendar approvalRequestedOn) {
		this.approvalRequestedOn = approvalRequestedOn;
	}

	public Long getApprovalRequestedBy() {
		return approvalRequestedBy;
	}

	public void setApprovalRequestedBy(Long approvalRequestedBy) {
		this.approvalRequestedBy = approvalRequestedBy;
	}

	public Calendar getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Calendar approvedOn) {
		this.approvedOn = approvedOn;
	}

	public Long getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Calendar getFileGenerationInitiatedOn() {
		return fileGenerationInitiatedOn;
	}

	public void setFileGenerationInitiatedOn(Calendar fileGenerationInitiatedOn) {
		this.fileGenerationInitiatedOn = fileGenerationInitiatedOn;
	}

	public Calendar getFileGeneratedOn() {
		return fileGeneratedOn;
	}

	public void setFileGeneratedOn(Calendar fileGeneratedOn) {
		this.fileGeneratedOn = fileGeneratedOn;
	}

	public Long getFileGeneratedBy() {
		return fileGeneratedBy;
	}

	public void setFileGeneratedBy(Long fileGeneratedBy) {
		this.fileGeneratedBy = fileGeneratedBy;
	}

	public FilingChannel getFilingChannel() {
		return filingChannel;
	}

	public void setFilingChannel(FilingChannel filingChannel) {
		this.filingChannel = filingChannel;
	}

	public Double getFilingFee() {
		return filingFee;
	}

	public void setFilingFee(Double filingFee) {
		this.filingFee = filingFee;
	}

	public CertificationStatement getCertificate() {
		return certificate;
	}

	public void setCertificate(CertificationStatement certificate) {
		this.certificate = certificate;
	}

	public List<IDSFilingInfo> getFilingInfos() {
		return filingInfos;
	}

	public void setFilingInfos(List<IDSFilingInfo> filingInfos) {
		this.filingInfos = filingInfos;
	}

	public ApplicationBase getApplication() {
		return application;
	}

	public void setApplication(ApplicationBase application) {
		this.application = application;
	}

	public Calendar getLastFiledOn() {
		return lastFiledOn;
	}

	public void setLastFiledOn(Calendar lastFiledOn) {
		this.lastFiledOn = lastFiledOn;
	}

	public SubStatus getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(SubStatus subStatus) {
		this.subStatus = subStatus;
	}

	public Calendar getFilingInstructedOn() {
		return filingInstructedOn;
	}

	public void setFilingInstructedOn(Calendar filingInstructedOn) {
		this.filingInstructedOn = filingInstructedOn;
	}

	public Long getFiledBy() {
		return filedBy;
	}

	public void setFiledBy(Long filedBy) {
		this.filedBy = filedBy;
	}

	public String getAttorneyComments() {
		return attorneyComments;
	}

	public void setAttorneyComments(String attorneyComments) {
		this.attorneyComments = attorneyComments;
	}

	public IDSFilingPrivatePair getIdsFilingPrivatePair() {
		return idsFilingPrivatePair;
	}

	public void setIdsFilingPrivatePair(IDSFilingPrivatePair idsFilingPrivatePair) {
		this.idsFilingPrivatePair = idsFilingPrivatePair;
	}
	
}