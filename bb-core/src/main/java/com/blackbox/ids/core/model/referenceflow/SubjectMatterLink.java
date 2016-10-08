/**
 * 
 */
package com.blackbox.ids.core.model.referenceflow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.mstr.Status;

/**
 * Entity bean for SML Link
 * 
 * @author nagarro
 *
 */
@Entity
@Table(name = "BB_SUBJECT_MATTER_LINK")
public class SubjectMatterLink extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5598689251600679881L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_SUBJECT_MATTER_LINK_ID")
	private Long smlLinkId;

	@Column(name = "SOURCE_FAMILY_ID", nullable = false)
	private String sourceFamilyId;

	@Column(name = "TARGET_FAMILY_ID", nullable = false)
	private String targetFamilyId;

	/** SML Link Status */
	@Column(name = "SML_LINK_STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	/** SML Link Comments */
	@Column(name = "COMMENTS", nullable = true)
	private String comments;

	/**
	 * @return the smlLinkId
	 */
	public Long getSmlLinkId() {
		return smlLinkId;
	}

	/**
	 * @param smlLinkId
	 *            the smlLinkId to set
	 */
	public void setSmlLinkId(Long smlLinkId) {
		this.smlLinkId = smlLinkId;
	}

	/**
	 * @return the sourceFamilyId
	 */
	public String getSourceFamily() {
		return sourceFamilyId;
	}

	/**
	 * @param sourceFamily
	 *            the sourceFamilyId to set
	 */
	public void setSourceFamilyId(String sourceFamilyId) {
		this.sourceFamilyId = sourceFamilyId;
	}

	/**
	 * @return the targetFamilyId
	 */
	public String getTargetFamilyId() {
		return targetFamilyId;
	}

	/**
	 * @param targetFamily
	 *            the targetFamilyId to set
	 */
	public void setTargetFamilyId(String targetFamilyId) {
		this.targetFamilyId = targetFamilyId;
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

}
