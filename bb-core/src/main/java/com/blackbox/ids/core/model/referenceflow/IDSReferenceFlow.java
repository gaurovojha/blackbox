package com.blackbox.ids.core.model.referenceflow;

import java.util.Calendar;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;

/**
 * 
 * Entity bean for IDS Reference Flow
 *
 */
@Entity
@Table(name = "BB_IDS_REFERENCE_FLOW")
public class IDSReferenceFlow extends BaseEntity {

	private static final long serialVersionUID = 7445746817758146699L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "REFERENCE_FLOW_ID")
	private Long referenceFlowId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_REFERENCE_BASE_DATA_ID", referencedColumnName = "BB_REFERENCE_BASE_DATA_ID", nullable = false)
	private ReferenceBaseData referenceBaseData;

	/** The correspondence. */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_CORRESPONDENCE_ID", referencedColumnName = "BB_CORRESPONDENCE_ID", nullable = true)
	private CorrespondenceBase correspondence;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TARGET_APPLICATION_ID", referencedColumnName = "BB_APPLICATION_ID", nullable = false)
	private ApplicationBase targetApplication;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOURCE_APPLICATION_ID", referencedColumnName = "BB_APPLICATION_ID", nullable = false)
	private ApplicationBase sourceApplication;

	/** Reference flow status */
	@Column(name = "REFERENCE_FLOW_STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private ReferenceFlowStatus referenceFlowStatus;

	/** The reference sub status */
	@Column(name = "REFERENCE_FLOW_SUB_STATUS")
	@Enumerated(EnumType.STRING)
	private ReferenceFlowSubStatus referenceFlowSubStatus;

	/** The original reference sub status */
	@Column(name = "ORIGINAL_REFERENCE_FLOW_SUB_STATUS")
	@Enumerated(EnumType.STRING)
	private ReferenceFlowSubStatus originalReferenceFlowSubStatus;

	@Column(name = "FILING_DATE", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar filingDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_IDS_ID", referencedColumnName = "BB_IDS_ID", nullable = true)
	private IDS ids;

	@Column(name = "INTERNAL_FINAL_IDS_ID", nullable = true)
	private String internalFinalIDSId;

	@Column(name = "EXTERNAL_FINAL_IDS_ID", nullable = true)
	private String externalFinalIDSId;

	@Column(name = "DO_NOT_FILE", nullable = true)
	private Boolean doNotFile = false;

	@Column(name = "CITE_ID", nullable = true)
	private Long citeId;

	@Column(name = "NOTIFY_ATTORNEY_FLAG", nullable = true)
	private boolean notifyAttorney;

	/**
	 * empty constructor
	 */
	public IDSReferenceFlow() {
		super();
	}

	/**
	 * @param referenceFlowId
	 */
	public IDSReferenceFlow(Long referenceFlowId) {
		super();
		this.referenceFlowId = referenceFlowId;
	}

	/**
	 * @return the referenceFlowId
	 */
	public Long getReferenceFlowId() {
		return referenceFlowId;
	}

	/**
	 * @param referenceFlowId
	 *            the referenceFlowId to set
	 */
	public void setReferenceFlowId(Long referenceFlowId) {
		this.referenceFlowId = referenceFlowId;
	}

	/**
	 * @return the referenceBaseData
	 */
	public ReferenceBaseData getReferenceBaseData() {
		return referenceBaseData;
	}

	/**
	 * @param referenceBaseDataId
	 *            the referenceBaseDataId to set
	 */
	public void setReferenceBaseData(ReferenceBaseData referenceBaseData) {
		this.referenceBaseData = referenceBaseData;
	}

	/**
	 * @return the correspondence
	 */
	public CorrespondenceBase getCorrespondence() {
		return correspondence;
	}

	/**
	 * @param correspondence
	 *            the correspondence to set
	 */
	public void setCorrespondence(CorrespondenceBase correspondence) {
		this.correspondence = correspondence;
	}

	/**
	 * @return the targetApplication
	 */
	public ApplicationBase getTargetApplication() {
		return targetApplication;
	}

	/**
	 * @param targetApplication
	 *            the targetApplication to set
	 */
	public void setTargetApplication(ApplicationBase targetApplication) {
		this.targetApplication = targetApplication;
	}

	/**
	 * @return the sourceApplication
	 */
	public ApplicationBase getSourceApplication() {
		return sourceApplication;
	}

	/**
	 * @param sourceApplication
	 *            the sourceApplication to set
	 */
	public void setSourceApplication(ApplicationBase sourceApplication) {
		this.sourceApplication = sourceApplication;
	}

	/**
	 * @return the referenceFlowStatus
	 */
	public ReferenceFlowStatus getReferenceFlowStatus() {
		return referenceFlowStatus;
	}

	/**
	 * @param referenceFlowStatus
	 *            the referenceFlowStatus to set
	 */
	public void setReferenceFlowStatus(ReferenceFlowStatus referenceFlowStatus) {
		this.referenceFlowStatus = referenceFlowStatus;
	}

	/**
	 * @return the referenceFlowSubStatus
	 */
	public ReferenceFlowSubStatus getReferenceFlowSubStatus() {
		return referenceFlowSubStatus;
	}

	/**
	 * @param referenceFlowSubStatus
	 *            the referenceFlowSubStatus to set
	 */
	public void setReferenceFlowSubStatus(ReferenceFlowSubStatus referenceFlowSubStatus) {
		this.referenceFlowSubStatus = referenceFlowSubStatus;
	}

	/**
	 * @return the filingDate
	 */
	public Calendar getFilingDate() {
		return filingDate;
	}

	/**
	 * @param filingDate
	 *            the filingDate to set
	 */
	public void setFilingDate(Calendar filingDate) {
		this.filingDate = filingDate;
	}

	/**
	 * @return the idsId
	 */
	public IDS getIdsId() {
		return ids;
	}

	/**
	 * @param idsId
	 *            the idsId to set
	 */
	public void setIdsId(IDS idsId) {
		this.ids = idsId;
	}

	/**
	 * @return the internalFinalIDSId
	 */
	public String getInternalFinalIDSId() {
		return internalFinalIDSId;
	}

	/**
	 * @param internalFinalIDSId
	 *            the internalFinalIDSId to set
	 */
	public void setInternalFinalIDSId(String internalFinalIDSId) {
		this.internalFinalIDSId = internalFinalIDSId;
	}

	/**
	 * @return the externalFinalIDSId
	 */
	public String getExternalFinalIDSId() {
		return externalFinalIDSId;
	}

	/**
	 * @param externalFinalIDSId
	 *            the externalFinalIDSId to set
	 */
	public void setExternalFinalIDSId(String externalFinalIDSId) {
		this.externalFinalIDSId = externalFinalIDSId;
	}

	/**
	 * @return the doNotFile
	 */
	public Boolean getDoNotFile() {
		return doNotFile;
	}

	/**
	 * @param doNotFile
	 *            the doNotFile to set
	 */
	public void setDoNotFile(Boolean doNotFile) {
		this.doNotFile = doNotFile;
	}

	/**
	 * @return the citeId
	 */
	public Long getCiteId() {
		return citeId;
	}

	/**
	 * @param citeId
	 *            the citeId to set
	 */
	public void setCiteId(Long citeId) {
		this.citeId = citeId;
	}

	public boolean isNotifyAttorney() {
		return notifyAttorney;
	}

	public void setNotifyAttorney(boolean notifyAttorney) {
		this.notifyAttorney = notifyAttorney;
	}

	/**
	 * @return the originalReferenceFlowSubStatus
	 */
	public ReferenceFlowSubStatus getOriginalReferenceFlowSubStatus() {
		return originalReferenceFlowSubStatus;
	}

	/**
	 * @param originalReferenceFlowSubStatus
	 *            the originalReferenceFlowSubStatus to set
	 */
	public void setOriginalReferenceFlowSubStatus(ReferenceFlowSubStatus originalReferenceFlowSubStatus) {
		this.originalReferenceFlowSubStatus = originalReferenceFlowSubStatus;
	}

	/**
	 * @return the ids
	 */
	public IDS getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(IDS ids) {
		this.ids = ids;
	}

}
