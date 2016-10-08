/**
 * 
 */
package com.blackbox.ids.core.repository.referenceflow;

import com.blackbox.ids.core.model.reference.SourceReference;

/**
 * IDSSourceReferenceFlowCustomRepository with custom methods
 * 
 * @author nagarro
 *
 */
public interface IDSSourceReferenceFlowCustomRepository<IDSSourceReferenceFlow> {

	public boolean duplicateCheck(final SourceReference sourceReference, final Long targetApplicationId);

	public IDSSourceReferenceFlow findBySourceReferenceAndTargetApplication(final SourceReference sourceReference,
			final Long targetApplicationId);

	public boolean dropSourceReferenceFlow(Long sourceReferenceId, Long sourceApplicationId);
}
