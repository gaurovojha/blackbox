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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.base.BaseEntity;

/**
 * Entity bean for IDS Source Reference Flow Filing Info
 * 
 * @author nagarro
 *
 */
@Entity
@Table(name = "BB_IDS_SOURCE_REFERENCE_FLOW_FILING_INFO")
public class IDSSourceReferenceFlowFilingInfo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6081634413682434995L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_IDS_SOURCE_REFERENCE_FLOW_FILING_INFO_ID")
	private Long sourceReferenceFlowFilingId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_IDS_SOURCE_REFERENCE_FLOW_ID", referencedColumnName = "BB_IDS_SOURCE_REFERENCE_FLOW_ID")
	private IDSSourceReferenceFlow sourceReferenceFlow;

	/** Reference flow status */
	@Column(name = "SOURCE_REFERENCE_FLOW_STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private SourceReferenceFlowStatus sourceReferenceFlowStatus;

	/** The reference sub status */
	@Column(name = "SOURCE_REFERENCE_FLOW_SUB_STATUS")
	@Enumerated(EnumType.STRING)
	private SourceReferenceFlowSubStatus sourceReferenceFlowSubStatus;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_IDS_ID", referencedColumnName = "BB_IDS_ID")
	private IDS tempIDS;

	@Column(name = "FILING_DATE", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar filingDate;

	@Column(name = "INTERNAL_FINAL_IDS_ID", nullable = true)
	private String internalFinalIDSId;

	@Column(name = "EXTERNAL_FINAL_IDS_ID", nullable = true)
	private String externalFinalIDSId;

	@Column(name = "CITE_ID", nullable = true)
	private String citeId;

	
	
	/**
	 * @return the sourceReferenceFlowFilingId
	 */
	public Long getSourceReferenceFlowFilingId() {
		return sourceReferenceFlowFilingId;
	}

	/**
	 * @param sourceReferenceFlowFilingId
	 *            the sourceReferenceFlowFilingId to set
	 */
	public void setSourceReferenceFlowFilingId(Long sourceReferenceFlowFilingId) {
		this.sourceReferenceFlowFilingId = sourceReferenceFlowFilingId;
	}

	/**
	 * @return the sourceReferenceFlow
	 */
	public IDSSourceReferenceFlow getSourceReferenceFlow() {
		return sourceReferenceFlow;
	}

	/**
	 * @param sourceReferenceFlow
	 *            the sourceReferenceFlow to set
	 */
	public void setSourceReferenceFlow(IDSSourceReferenceFlow sourceReferenceFlow) {
		this.sourceReferenceFlow = sourceReferenceFlow;
	}

	/**
	 * @return the sourceReferenceFlowStatus
	 */
	public SourceReferenceFlowStatus getSourceReferenceFlowStatus() {
		return sourceReferenceFlowStatus;
	}

	/**
	 * @param sourceReferenceFlowStatus
	 *            the sourceReferenceFlowStatus to set
	 */
	public void setSourceReferenceFlowStatus(SourceReferenceFlowStatus sourceReferenceFlowStatus) {
		this.sourceReferenceFlowStatus = sourceReferenceFlowStatus;
	}

	/**
	 * @return the sourceReferenceFlowSubStatus
	 */
	public SourceReferenceFlowSubStatus getSourceReferenceFlowSubStatus() {
		return sourceReferenceFlowSubStatus;
	}

	/**
	 * @param sourceReferenceFlowSubStatus
	 *            the sourceReferenceFlowSubStatus to set
	 */
	public void setSourceReferenceFlowSubStatus(SourceReferenceFlowSubStatus sourceReferenceFlowSubStatus) {
		this.sourceReferenceFlowSubStatus = sourceReferenceFlowSubStatus;
	}

	/**
	 * @return the tempIDSId
	 */
	public IDS getTempIDS() {
		return tempIDS;
	}

	/**
	 * @param tempIDS
	 *            the tempIDSId to set
	 */
	public void setTempIDS(IDS tempIDS) {
		this.tempIDS = tempIDS;
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
	 * @return the citeId
	 */
	public String getCiteId() {
		return citeId;
	}

	/**
	 * @param citeId
	 *            the citeId to set
	 */
	public void setCiteId(String citeId) {
		this.citeId = citeId;
	}

}
