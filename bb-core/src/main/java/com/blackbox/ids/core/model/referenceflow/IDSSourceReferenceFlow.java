package com.blackbox.ids.core.model.referenceflow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.reference.SourceReference;

/**
 *
 * Entity bean for IDS Source Reference Flow
 *
 * @author nagarro
 *
 */
@Entity
@Table(name = "BB_IDS_SOURCE_REFERENCE_FLOW")
public class IDSSourceReferenceFlow extends BaseEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = 8432332385803190509L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_IDS_SOURCE_REFERENCE_FLOW_ID")
	private Long sourceReferenceFlowId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_SOURCE_REFERENCE_ID", referencedColumnName = "BB_SOURCE_REFERENCE_ID")
	private SourceReference sourceReference;

	/** The correspondence. */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_CORRESPONDENCE_ID", referencedColumnName = "BB_CORRESPONDENCE_ID", nullable = true)
	private CorrespondenceBase correspondence;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TARGET_APPLICATION_ID", referencedColumnName = "BB_APPLICATION_ID")
	private ApplicationBase targetApplication;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOURCE_APPLICATION_ID", referencedColumnName = "BB_APPLICATION_ID")
	private ApplicationBase sourceApplication;

	@Column(name = "STATUS", nullable = true)
	private SourceReferenceFlowStatus sourceReferenceFlowStatus;

	public IDSSourceReferenceFlow()
	{}

	public IDSSourceReferenceFlow(long sourceRef)
	{
		this.sourceReferenceFlowId = sourceRef;
	}
	/**
	 * @return the sourceReferenceFlowId
	 */
	public Long getSourceReferenceFlowId() {
		return sourceReferenceFlowId;
	}

	/**
	 * @param sourceReferenceFlowId
	 *            the sourceReferenceFlowId to set
	 */
	public void setSourceReferenceFlowId(Long sourceReferenceFlowId) {
		this.sourceReferenceFlowId = sourceReferenceFlowId;
	}

	/**
	 * @return the sourceReference
	 */
	public SourceReference getSourceReference() {
		return sourceReference;
	}

	/**
	 * @param sourceReference
	 *            the sourceReference to set
	 */
	public void setSourceReference(SourceReference sourceReference) {
		this.sourceReference = sourceReference;
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

}