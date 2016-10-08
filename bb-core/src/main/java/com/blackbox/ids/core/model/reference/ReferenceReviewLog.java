package com.blackbox.ids.core.model.reference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.blackbox.ids.core.model.base.AuditableEntity;

/**
 * The Entity ReferenceReviewLog.
 */
@Entity
@Table(name = "BB_REFERENCE_REVIEW_LOG")
public class ReferenceReviewLog extends AuditableEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5182917153604546861L;

	@Id
	@Column(name = "BB_REFERENCE_REVIEW_LOG_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "BB_REFERENCE_BASE_DATA_ID")
	private Long referenceBaseDataId;

	@Column(name = "COMMENTS")
	private String comments;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the referenceBaseDataId
	 */
	public Long getReferenceBaseDataId() {
		return referenceBaseDataId;
	}

	/**
	 * @param referenceBaseDataId
	 *            the referenceBaseDataId to set
	 */
	public void setReferenceBaseDataId(Long referenceBaseDataId) {
		this.referenceBaseDataId = referenceBaseDataId;
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
	 * Instantiates a new reference review log.
	 */
	public ReferenceReviewLog() {
		super();
	}
}