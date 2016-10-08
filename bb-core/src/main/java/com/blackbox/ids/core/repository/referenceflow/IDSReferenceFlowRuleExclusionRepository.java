/**
 * 
 */
package com.blackbox.ids.core.repository.referenceflow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Status;
import com.blackbox.ids.core.model.reference.ReferenceRuleType;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlowRuleExclusion;

/**
 * The Interface IDSReferenceFlowRuleExclusionRepository.
 *
 * @param <T>
 *            the generic type
 */
public interface IDSReferenceFlowRuleExclusionRepository<T extends IDSReferenceFlowRuleExclusion>
		extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T>, IDSReferenceFlowRuleExclusionCustomRepository<T> {

	public List<IDSReferenceFlowRuleExclusion> findBySourceApplicationAndReferenceRuleType(
			final ApplicationBase appBase, final ReferenceRuleType referenceRuleType);

	public List<IDSReferenceFlowRuleExclusion> findBySourceApplicationInAndReferenceRuleType(
			final List<ApplicationBase> appBaseList, final ReferenceRuleType referenceRuleType);

	public List<IDSReferenceFlowRuleExclusion> findBySourceApplicationInAndReferenceRuleTypeAndStatus(
			final ApplicationBase appBase, final ReferenceRuleType referenceRuleType, final Status status);

	public IDSReferenceFlowRuleExclusion findBySourceApplicationAndTargetApplication(
			final ApplicationBase sourceAppBase, final ApplicationBase targetAppBase);

	public List<IDSReferenceFlowRuleExclusion> findBySourceFamilyIdAndReferenceRuleTypeAndStatus(
			final String sourceFamilyId, final ReferenceRuleType referenceRuleType, final Status status);
}
