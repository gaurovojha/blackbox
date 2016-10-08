/**
 * 
 */
package com.blackbox.ids.core.repository.referenceflow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.referenceflow.IDSSourceReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.IDSSourceReferenceFlowFilingInfo;
import com.blackbox.ids.core.model.referenceflow.SourceReferenceFlowStatus;

/**
 * The Interface IDSSourceReferenceFlowRepository.
 *
 * @param <T>
 *            the generic type
 */
public interface IDSSourceReferenceFlowFilingInfoRepository<T extends IDSSourceReferenceFlowFilingInfo>
		extends JpaRepository<IDSSourceReferenceFlowFilingInfo, Long>, QueryDslPredicateExecutor<T>,
		IDSSourceReferenceFlowFilingInfoCustomRepository<IDSSourceReferenceFlowFilingInfo> {

	public List<IDSSourceReferenceFlowFilingInfo> findBySourceReferenceFlowAndSourceReferenceFlowStatus(
			final IDSSourceReferenceFlow sourceReferenceFlow,
			final SourceReferenceFlowStatus sourceReferenceFlowStatus);

	public IDSSourceReferenceFlowFilingInfo findByTempIDS(final IDS id);
}
