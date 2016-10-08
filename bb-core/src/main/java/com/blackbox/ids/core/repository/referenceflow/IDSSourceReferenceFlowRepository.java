/**
 * 
 */
package com.blackbox.ids.core.repository.referenceflow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.reference.SourceReference;
import com.blackbox.ids.core.model.referenceflow.IDSSourceReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.SourceReferenceFlowStatus;

/**
 * The Interface IDSSourceReferenceFlowRepository.
 *
 * @param <T>
 *            the generic type
 */
public interface IDSSourceReferenceFlowRepository<T extends IDSSourceReferenceFlow>
		extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T>, IDSSourceReferenceFlowCustomRepository<T> {

	public List<IDSSourceReferenceFlow> findBySourceReferenceAndTargetApplicationAndSourceReferenceFlowStatus(
			SourceReference sourceReference, ApplicationBase sourceAppBase, SourceReferenceFlowStatus status);

}
