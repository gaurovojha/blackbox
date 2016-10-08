/**
 * 
 */
package com.blackbox.ids.core.repository.impl.referenceflow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlow1449Pending;

/**
 * @author nagarro
 *
 */
@Repository
public interface ReferenceFlow1449PendingRepository<T extends ReferenceFlow1449Pending>
		extends JpaRepository<ReferenceFlow1449Pending, Long>, QueryDslPredicateExecutor<ReferenceFlow1449Pending> {

	public ReferenceFlow1449Pending findByIdsReferenceFlow(final IDSReferenceFlow refFlow);

	public ReferenceFlow1449Pending findByReferenceBaseDataAndIdsReferenceFlow(final ReferenceBaseData refBase,
			final IDSReferenceFlow refFlow);

	public ReferenceFlow1449Pending findByReferenceBaseData(final ReferenceBaseData refBase);

}
