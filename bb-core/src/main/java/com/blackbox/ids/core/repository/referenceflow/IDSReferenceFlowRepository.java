/**
 * 
 */
package com.blackbox.ids.core.repository.referenceflow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;

/**
 * The Interface IDSReferenceFlowRepository.
 *
 * @param <T>
 *            the generic type
 */
public interface IDSReferenceFlowRepository<T extends IDSReferenceFlow>
		extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T>, IDSReferenceFlowCustomRepository<T> {

	@Query(value = "SELECT idsReferenceFlow from IDSReferenceFlow idsReferenceFlow where idsReferenceFlow.referenceBaseData = :refId and idsReferenceFlow.referenceFlowStatus != :status")
	public IDSReferenceFlow findByReferenceBaseDataAndReferenceFlowStatus(
			@Param("refId") final ReferenceBaseData referenceBaseData, @Param("status") final ReferenceFlowStatus status);

	public List<IDSReferenceFlow> findByReferenceBaseDataAndSourceApplicationAndReferenceFlowStatus(
			final ReferenceBaseData referenceBaseData, final ApplicationBase sourceApplication,
			final ReferenceFlowStatus status);

	public List<IDSReferenceFlow> findByReferenceBaseDataAndTargetApplicationAndReferenceFlowStatusNotInAndReferenceFlowSubStatusNot(
			final ReferenceBaseData referenceBaseData, final ApplicationBase sourceApplication,
			final List<ReferenceFlowStatus> statusList, final ReferenceFlowSubStatus subStatus);

}
