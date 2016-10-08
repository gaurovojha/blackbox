/**
 * 
 */
package com.blackbox.ids.core.dto.reference;

import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.model.referenceflow.SourceReferenceFlowStatus;

/**
 * @author nagarro
 *
 */
public class SourceReferenceFlowDTO {

	private Long id;
	private Long correspondenceId;
	private SourceReferenceDTO sourceReferenceDTO;
	private SourceReferenceFlowStatus Status;
	private MdmRecordDTO sourceApplication;
	private MdmRecordDTO targetApplication;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the correspondenceId
	 */
	public Long getCorrespondenceId() {
		return correspondenceId;
	}
	/**
	 * @param correspondenceId the correspondenceId to set
	 */
	public void setCorrespondenceId(Long correspondenceId) {
		this.correspondenceId = correspondenceId;
	}
	/**
	 * @return the sourceReferenceDTO
	 */
	public SourceReferenceDTO getSourceReferenceDTO() {
		return sourceReferenceDTO;
	}
	/**
	 * @param sourceReferenceDTO the sourceReferenceDTO to set
	 */
	public void setSourceReferenceDTO(SourceReferenceDTO sourceReferenceDTO) {
		this.sourceReferenceDTO = sourceReferenceDTO;
	}
	/**
	 * @return the status
	 */
	public SourceReferenceFlowStatus getStatus() {
		return Status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(SourceReferenceFlowStatus status) {
		Status = status;
	}
	/**
	 * @return the sourceApplication
	 */
	public MdmRecordDTO getSourceApplication() {
		return sourceApplication;
	}
	/**
	 * @param sourceApplication the sourceApplication to set
	 */
	public void setSourceApplication(MdmRecordDTO sourceApplication) {
		this.sourceApplication = sourceApplication;
	}
	/**
	 * @return the targetApplication
	 */
	public MdmRecordDTO getTargetApplication() {
		return targetApplication;
	}
	/**
	 * @param targetApplication the targetApplication to set
	 */
	public void setTargetApplication(MdmRecordDTO targetApplication) {
		this.targetApplication = targetApplication;
	}
	
	
}
