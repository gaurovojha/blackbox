/**
 * 
 */
package com.blackbox.ids.core.repository.impl.referenceflow;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.model.reference.SourceReference;
import com.blackbox.ids.core.model.referenceflow.IDSSourceReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.QIDSSourceReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.SourceReferenceFlowStatus;
import com.blackbox.ids.core.repository.referenceflow.IDSSourceReferenceFlowCustomRepository;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

/**
 * @author nagarro
 *
 */
@Repository
public class IDSSourceReferenceFlowRepositoryImpl
		implements IDSSourceReferenceFlowCustomRepository<IDSSourceReferenceFlow> {

	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public boolean duplicateCheck(final SourceReference sourceReference, final Long targetApplicationId) {
		QIDSSourceReferenceFlow sourceRefFlow = QIDSSourceReferenceFlow.iDSSourceReferenceFlow;
		List<SourceReferenceFlowStatus> statusList = Collections.emptyList();
		statusList.add(SourceReferenceFlowStatus.DROPPED);
		JPAQuery query = new JPAQuery(entityManager);
		query.from(sourceRefFlow)
				.where(sourceRefFlow.sourceReference.id.eq(sourceReference.getId())
						.and(sourceRefFlow.targetApplication.id.eq(targetApplicationId))
						.and(sourceRefFlow.sourceReferenceFlowStatus.notIn(statusList)));

		return query.exists();
	}

	@Override
	public IDSSourceReferenceFlow findBySourceReferenceAndTargetApplication(final SourceReference sourceReference,
			final Long targetApplicationId) {
		QIDSSourceReferenceFlow sourceRefFlow = QIDSSourceReferenceFlow.iDSSourceReferenceFlow;
		JPAQuery query = new JPAQuery(entityManager);
		return query.from(sourceRefFlow)
				.where(sourceRefFlow.sourceReference.id.eq(sourceReference.getId())
						.and(sourceRefFlow.targetApplication.id.eq(targetApplicationId))
						.and(sourceRefFlow.sourceReferenceFlowStatus.eq(SourceReferenceFlowStatus.ACTIVE)))
				.uniqueResult(sourceRefFlow);
	}

	@Override
	public boolean dropSourceReferenceFlow(final Long sourceReferenceId, final Long sourceApplicationId) {
		QIDSSourceReferenceFlow sourceRefFlow = QIDSSourceReferenceFlow.iDSSourceReferenceFlow;
		JPAUpdateClause updateQuery = new JPAUpdateClause(entityManager, sourceRefFlow);
		return updateQuery.set(sourceRefFlow.sourceReferenceFlowStatus, SourceReferenceFlowStatus.DROPPED)
				.set(sourceRefFlow.updatedByUser, BlackboxSecurityContextHolder.getUserId())
				.set(sourceRefFlow.updatedDate, Calendar.getInstance()).where(sourceRefFlow.sourceReference.id
						.eq(sourceReferenceId).and(sourceRefFlow.sourceApplication.id.eq(sourceApplicationId)))
				.execute() > 0;
	}

}
