/**
 *
 */
package com.blackbox.ids.core.dao.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.ApplicationStageDAO;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dto.mdm.ActionsBaseDto;
import com.blackbox.ids.core.dto.mdm.DraftDTO;
import com.blackbox.ids.core.dto.mdm.DraftIdentityDTO;
import com.blackbox.ids.core.dto.mdm.QDraftIdentityDTO;
import com.blackbox.ids.core.model.IDS.QIDS;
import com.blackbox.ids.core.model.IDS.IDS.Status;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.Application;
import com.blackbox.ids.core.model.mdm.Application.SubSource;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationValidationStatus;
import com.blackbox.ids.core.model.mdm.QApplicationStage;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ConstructorExpression;
/**
 * The <code>ApplicationStageDaoImpl</code> provides implementation of {@link ApplicationStageDAO} for common database CRUD
 * operations for entity class {@link ApplicationStage}.
 *
 * @author ajay2258
 */
@Repository
public class ApplicationStageDaoImpl extends BaseDaoImpl<ApplicationStage, Long> implements ApplicationStageDAO {

	@Override
	public List<DraftDTO> getUserApplicationDrafts(final Long createdBy) throws ApplicationException {

		QApplicationStage application = QApplicationStage.applicationStage;
		return getJPAQuery()
				.from(application)
				.where(application.source.eq(Application.Source.DRAFT)
						.and(application.createdByUser.eq(createdBy)))
				.list(ConstructorExpression.create(DraftDTO.class,
						application.id, application.jurisdiction,
						application.appDetails.applicationNumberRaw, application.updatedDate));
	}

	@Override
	public List<ActionsBaseDto> getActionBaseDTOStaging() throws ApplicationException {
		QApplicationStage application = QApplicationStage.applicationStage;
		return getJPAQuery()
				.from(application)
				.list(ConstructorExpression.create(ActionsBaseDto.class,
						application.jurisdiction,application.applicationNumber));
	}

	@Override
	public List<ApplicationStage> findApplicationStageByNumberJurisdiction(final String jurisdiction, final String applicationNo)throws ApplicationException {
		QApplicationStage application = QApplicationStage.applicationStage;
		return getJPAQuery()
				.from(application)
				.where(application.jurisdiction.equalsIgnoreCase(jurisdiction)
						.and(application.applicationNumber.equalsIgnoreCase(applicationNo)))
				.list(application) ;
	}

	@Override
	public Map<DraftIdentityDTO, Long> mapApplicationDrafts(List<DraftIdentityDTO> draftIds) {
		QApplicationStage draft = QApplicationStage.applicationStage;
		return getJPAQuery().from(draft)
				.where(draftPredicates(draft, draftIds))
				.map(new QDraftIdentityDTO(draft.jurisdiction, draft.appDetails.applicationNumberRaw, draft.createdByUser), draft.id);
	}

	@Override
	public long delete(List<DraftIdentityDTO> draftIds) {
		QApplicationStage draft = QApplicationStage.applicationStage;
		return getJPADeleteClause(draft)
				.where(draftPredicates(draft, draftIds))
				.execute();
	}

	private BooleanBuilder draftPredicates(QApplicationStage draft, List<DraftIdentityDTO> draftIds) {
		BooleanBuilder predicate = new BooleanBuilder();

		draftIds.forEach(e -> {
			predicate.or((draft.jurisdiction.equalsIgnoreCase(e.getJurisdiction()))
					.and(draft.appDetails.applicationNumberRaw.equalsIgnoreCase(e.getApplicationNo()))
					.and(draft.createdByUser.eq(e.getUserId())));
		});

		return predicate;
	}

	/**
	 * Get Application nos for given customers for stage table
	 * @param customerNos
	 * @return
	 * @throws ApplicationException
	 */
	@Override
	public List<ApplicationStage> getApplicationStageForCustomers(final List<String> customerNos) throws ApplicationException {
		QApplicationStage applicationStage = QApplicationStage.applicationStage;
		return getJPAQuery()
				.from(applicationStage)
				.where(applicationStage.customer.in(customerNos))
				.list(applicationStage);
	}
	
	@Override
	public Map<String, List<ApplicationStage>> getApplicationsToImport() {

		QApplicationStage application = QApplicationStage.applicationStage;
		
		JPAQuery query = getJPAQuery()
				.from(application)
				.where(application.source.eq(Application.Source.AUTOMATIC)
						.and(application.status.eq(QueueStatus.IMPORT_READY))
						.and(application.familyId.isNotEmpty()))
						.orderBy(application.familyId.asc());
		List<ApplicationStage> list = query.list(application);
		return list.stream().collect(Collectors.groupingBy(a -> a.getFamilyId()));
	}
	
	@Override
	public List<ApplicationStage> getPendingApplicationsByStatus(QueueStatus status, int noOfDays) {
		QApplicationStage application = QApplicationStage.applicationStage;
		JPAQuery query = getJPAQuery()
				.from(application)
				.where(application.source
						.eq(Application.Source.AUTOMATIC)
						.and(application.subSource.eq(SubSource.USPTO))
						.and(application.status.eq(status))
						.and(application.createdDate.loe(BlackboxDateUtil.dateAfterDays(Calendar.getInstance(),
								-noOfDays))));
		return query.list(application);
	}
	
	@Override
	public void updateApplicationStatus(final List<Long> applicationIds, final QueueStatus newStatus,
			final ApplicationValidationStatus validationStatus, final long updatedBy) {
		QApplicationStage application = QApplicationStage.applicationStage;
		getJPAUpdateClause(application).set(application.status, newStatus)
				.set(application.validationStatus, validationStatus)
				.set(application.updatedDate, Calendar.getInstance()).set(application.updatedByUser, updatedBy)
				.where(application.id.in(applicationIds)).execute();
	}
	
}
