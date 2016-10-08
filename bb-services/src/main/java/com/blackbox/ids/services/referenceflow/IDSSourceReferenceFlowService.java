package com.blackbox.ids.services.referenceflow;

import com.blackbox.ids.core.model.reference.SourceReference;

/**
 * 
 * @author nagarro
 *
 */
public interface IDSSourceReferenceFlowService {

	public boolean duplicateCheck(final SourceReference sourceReference, final Long applicationId);

	public boolean dropSourceReferenceFlow(final Long sourceReferenceId, final Long sourceApplicationId);

}
