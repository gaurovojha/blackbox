/**
 * 
 */
package com.blackbox.ids.services.referenceflow;

import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;

/**
 * @author nagarro
 *
 */
public interface IDSReferenceFlowService {

	/**
	 * reference flow duplicate check
	 * 
	 * @param referenceBaseData
	 * @param applicationId
	 * @return
	 */
	public boolean duplicateCheck(final ReferenceBaseData referenceBaseData, final Long applicationId);

	/**
	 * Find duplicate reference flow recordF
	 * 
	 * @param referenceBaseData
	 * @param applicationId
	 * @return
	 */
	public IDSReferenceFlow findDuplicateRefFlow(final ReferenceBaseData referenceBaseData, final Long applicationId);

	/**
	 * checks if new ref flow should be created with UNCITED - CITED_IN_PARENT status
	 * 
	 * @param referenceId
	 * @param targetApplication
	 * @return boolean
	 */
	public boolean isEligibleForCitedInParentStatus(final ReferenceBaseData referenceBaseData,
			final ApplicationBase targetAppBase);

	/**
	 * create reference flow when it is rejected from IDS
	 * 
	 * @param refFlowId
	 */
	public void createReferenceFlowAfterRejectAction(Long refFlowId);

	/**
	 * Drop reference flow by ref flow id
	 * 
	 * @param refFlowId
	 */
	public void dropReferenceFlow(Long refFlowId);

	/**
	 * creates a reference flow
	 * 
	 * @param referenceFlowDTO
	 * @return
	 */
	// public ReferenceFlowDTO createReferenceFlow(final ReferenceFlowDTO referenceFlowDTO);

}
