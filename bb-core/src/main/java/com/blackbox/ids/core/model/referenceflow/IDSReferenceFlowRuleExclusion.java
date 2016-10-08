package com.blackbox.ids.core.model.referenceflow;

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

import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Status;
import com.blackbox.ids.core.model.reference.ReferenceRuleType;

/**
 * 
 * Entity bean for IDS Reference Flow Rule Exclusion
 *
 */
@Entity
@Table(name = "BB_IDS_REFERENCE_FLOW_RULE_EXCLUSION")
public class IDSReferenceFlowRuleExclusion extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1185103098723464227L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_IDS_REFERENCE_FLOW_RULE_EXCLUSION_ID")
	private Long referenceFlowRuleExclusionId;

	/** Reference RULE TYPE */
	@Column(name = "RULE_TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private ReferenceRuleType referenceRuleType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_SUBJECT_MATTER_LINK_ID", referencedColumnName = "BB_SUBJECT_MATTER_LINK_ID", nullable = true)
	private SubjectMatterLink subjectMatterLink;

	@Column(name = "TARGET_FAMILY_ID", nullable = false)
	private String targetFamilyId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TARGET_APPLICATION_ID", referencedColumnName = "BB_APPLICATION_ID", nullable = false)
	private ApplicationBase targetApplication;

	@Column(name = "SOURCE_FAMILY_ID", nullable = false)
	private String sourceFamilyId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOURCE_APPLICATION_ID", referencedColumnName = "BB_APPLICATION_ID", nullable = false)
	private ApplicationBase sourceApplication;

	/** Reference flow status */
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "COMMENTS")
	private String comments;

	/**
	 * @return the referenceFlowRuleExclusionId
	 */
	public Long getReferenceFlowRuleExclusionId() {
		return referenceFlowRuleExclusionId;
	}

	/**
	 * @param referenceFlowRuleExclusionId
	 *            the referenceFlowRuleExclusionId to set
	 */
	public void setReferenceFlowRuleExclusionId(Long referenceFlowRuleExclusionId) {
		this.referenceFlowRuleExclusionId = referenceFlowRuleExclusionId;
	}

	/**
	 * @return the referenceRuleType
	 */
	public ReferenceRuleType getReferenceRuleType() {
		return referenceRuleType;
	}

	/**
	 * @param referenceRuleType
	 *            the referenceRuleType to set
	 */
	public void setReferenceRuleType(ReferenceRuleType referenceRuleType) {
		this.referenceRuleType = referenceRuleType;
	}

	/**
	 * @return the subjectMatterLink
	 */
	public SubjectMatterLink getSubjectMatterLink() {
		return subjectMatterLink;
	}

	/**
	 * @param subjectMatterLink
	 *            the subjectMatterLink to set
	 */
	public void setSubjectMatterLink(SubjectMatterLink subjectMatterLink) {
		this.subjectMatterLink = subjectMatterLink;
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
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the targetFamilyId
	 */
	public String getTargetFamilyId() {
		return targetFamilyId;
	}

	/**
	 * @param targetFamilyId
	 *            the targetFamilyId to set
	 */
	public void setTargetFamilyId(String targetFamilyId) {
		this.targetFamilyId = targetFamilyId;
	}

	/**
	 * @return the sourceFamilyId
	 */
	public String getSourceFamilyId() {
		return sourceFamilyId;
	}

	/**
	 * @param sourceFamilyId
	 *            the sourceFamilyId to set
	 */
	public void setSourceFamilyId(String sourceFamilyId) {
		this.sourceFamilyId = sourceFamilyId;
	}

}