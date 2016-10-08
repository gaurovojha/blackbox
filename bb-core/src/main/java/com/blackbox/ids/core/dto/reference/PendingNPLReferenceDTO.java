package com.blackbox.ids.core.dto.reference;

import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.mysema.query.annotations.QueryProjection;

/**
 * The Class PendingNPLReferenceDTO.
 */
public class PendingNPLReferenceDTO {

	/** The correspondence id. */
	private Long correspondenceId;

	/** The reference base data id. */
	private Long referenceBaseDataId;

	private ApplicationBase applicationBase;

	/**
	 * Instantiates a new pending npl reference dto.
	 *
	 * @param correspondenceId
	 *            the correspondence id
	 * @param referenceBaseDataId
	 *            the reference base data id
	 * @param applicationId
	 *            the application id
	 */
	@QueryProjection
	public PendingNPLReferenceDTO(Long correspondenceId, Long referenceBaseDataId, ApplicationBase applicationBase) {
		super();
		this.correspondenceId = correspondenceId;
		this.referenceBaseDataId = referenceBaseDataId;
		this.applicationBase = applicationBase;
	}

	/**
	 * Instantiates a new pending npl reference dto.
	 */
	public PendingNPLReferenceDTO() {
		super();
	}

	/**
	 * Gets the correspondence id.
	 *
	 * @return the correspondence id
	 */
	public Long getCorrespondenceId() {
		return correspondenceId;
	}

	/**
	 * Sets the correspondence id.
	 *
	 * @param correspondenceId
	 *            the new correspondence id
	 */
	public void setCorrespondenceId(Long correspondenceId) {
		this.correspondenceId = correspondenceId;
	}

	/**
	 * Gets the reference base data id.
	 *
	 * @return the reference base data id
	 */
	public Long getReferenceBaseDataId() {
		return referenceBaseDataId;
	}

	/**
	 * Sets the reference base data id.
	 *
	 * @param referenceBaseDataId
	 *            the new reference base data id
	 */
	public void setReferenceBaseDataId(Long referenceBaseDataId) {
		this.referenceBaseDataId = referenceBaseDataId;
	}

	
	public ApplicationBase getApplicationBase() {
		return applicationBase;
	}

	public void setApplicationBase(ApplicationBase applicationBase) {
		this.applicationBase = applicationBase;
	}

	
}
