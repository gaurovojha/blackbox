package com.blackbox.ids.core.repository.impl.reference;

import static com.mysema.query.group.GroupBy.groupBy;
import static com.mysema.query.group.GroupBy.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.auth.UserAuthDetail;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.correspondence.QCorrespondenceBase;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.mdm.QApplicationBase;
import com.blackbox.ids.core.model.reference.QOcrData;
import com.blackbox.ids.core.model.reference.QReferenceStagingData;
import com.blackbox.ids.core.model.reference.ReferenceStagingData;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.reference.ReferenceStagingDataCustomRepository;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;

/**
 * The Class ReferenceStagingDataRepositoryImpl.
 */
public class ReferenceStagingDataRepositoryImpl implements ReferenceStagingDataCustomRepository {

	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;

	/** The user repository. */
	@Autowired
	private UserRepository userRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.core.repository.reference.ReferenceStagingDataCustomRepository#filterPendingRecords(com.mysema.
	 * query.types.Predicate, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<CorrespondenceDTO> filterPendingRecords(Predicate predicate, Pageable pageable)
			throws ApplicationException {

		QCorrespondenceBase correspondenceBaseData = QCorrespondenceBase.correspondenceBase;
		QReferenceStagingData referenceStagingData = QReferenceStagingData.referenceStagingData;
		QCorrespondenceBase correspondenceBaseData2 = QCorrespondenceBase.correspondenceBase;
		QCorrespondenceBase correspondenceBaseData3 = QCorrespondenceBase.correspondenceBase;

		QApplicationBase applicationBaseData = QApplicationBase.applicationBase;

		BooleanBuilder condition = new BooleanBuilder();
		condition.and(predicate).and(referenceStagingData.referenceStatus.eq(ReferenceStatus.INPADOC_PENDING));
		condition.and(getDataAccessPredicate(applicationBaseData));

		JPAQuery query = new JPAQuery(entityManager);
		query.from(correspondenceBaseData, applicationBaseData, referenceStagingData);
		query.where(correspondenceBaseData.id.eq(referenceStagingData.correspondence.id));
		query.where(correspondenceBaseData.application.id.eq(applicationBaseData.id));
		query.where(correspondenceBaseData.status.eq(Status.ACTIVE));
		query.where(applicationBaseData.recordStatus.eq(MDMRecordStatus.ACTIVE));
		query.where(condition);
		query.where(correspondenceBaseData.id.in((new JPASubQuery().from(correspondenceBaseData3).where(correspondenceBaseData3.mailingDate.in(new JPASubQuery().from(correspondenceBaseData2)
				.groupBy(correspondenceBaseData2.application.id).list(correspondenceBaseData2.mailingDate.max()))).list(correspondenceBaseData3.id))));
		query.distinct();
		
		Querydsl queryDsl = new Querydsl(entityManager,
				new PathBuilder<CorrespondenceBase>(CorrespondenceBase.class, "correspondenceBase"));

		queryDsl.applyPagination(pageable, query);
		List<CorrespondenceDTO> results = query.list(ConstructorExpression.create(CorrespondenceDTO.class,
				correspondenceBaseData.id, applicationBaseData.jurisdiction.id,
				applicationBaseData.jurisdiction.code, applicationBaseData.id,
				applicationBaseData.applicationNumber, correspondenceBaseData.mailingDate,
				correspondenceBaseData.createdDate, correspondenceBaseData.documentCode.description,
				correspondenceBaseData.createdByUser, correspondenceBaseData.updatedByUser,
				referenceStagingData.ocrData.ocrStatus));

		List<CorrespondenceDTO> dtoListWithFirstGroupRecord = new ArrayList<CorrespondenceDTO>();
		for (CorrespondenceDTO dto : results) {

			String uploadedBy = userRepository.getUserFullName(userRepository.getEmailId(dto.getUploadedByUserId()));
			String lastUpdateBy = null;
			if (dto.getLastUpdateByUserId() != null) {
				lastUpdateBy = userRepository.getUserFullName(userRepository.getEmailId(dto.getLastUpdateByUserId()));
			}

			dto.setUploadedBy(uploadedBy);
			dto.setLastUpdateBy(lastUpdateBy);
			dtoListWithFirstGroupRecord.add(dto);
		}

		long count = query.count();
		// long count = results.size();

		return count > 0 ? new PageImpl<CorrespondenceDTO>(dtoListWithFirstGroupRecord, pageable, count)
				: new PageImpl<CorrespondenceDTO>(new ArrayList<CorrespondenceDTO>(), pageable, 0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.core.repository.reference.ReferenceStagingDataCustomRepository#fetchCorrespondenceData(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public List<CorrespondenceDTO> fetchCorrespondenceData(final String jurisCode, final String appNo)
			throws ApplicationException {

		QReferenceStagingData referenceStagingData = QReferenceStagingData.referenceStagingData;
		QCorrespondenceBase correspondenceBaseData = QCorrespondenceBase.correspondenceBase;

		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceStagingData, correspondenceBaseData);
		query.where(correspondenceBaseData.id.eq(referenceStagingData.correspondence.id));

		BooleanBuilder condition = new BooleanBuilder();
		condition.and(correspondenceBaseData.application.applicationNumber.eq(appNo))
				.and(correspondenceBaseData.application.jurisdiction.code.eq(jurisCode));
		query.where(condition);
		Map<Long, List<CorrespondenceDTO>> dtoMap = query.transform(groupBy(referenceStagingData.correspondence.id)
				.as(list(ConstructorExpression.create(CorrespondenceDTO.class, correspondenceBaseData.id,
						correspondenceBaseData.application.jurisdiction.id,
						correspondenceBaseData.application.jurisdiction.code, correspondenceBaseData.application.id,
						correspondenceBaseData.application.applicationNumber, correspondenceBaseData.mailingDate,
						correspondenceBaseData.createdDate, correspondenceBaseData.documentCode.description,
						correspondenceBaseData.createdByUser, correspondenceBaseData.updatedByUser,
						referenceStagingData.ocrData.ocrStatus))));

		List<CorrespondenceDTO> dtoListWithFirstGroupRecord = new ArrayList<CorrespondenceDTO>();
		for (List<CorrespondenceDTO> dtoList : dtoMap.values()) {
			CorrespondenceDTO dto = dtoList.get(0);
			String uploadedBy = userRepository.getUserFullName(userRepository.getEmailId(dto.getUploadedByUserId()));
			String lastUpdateBy = null;
			if (dto.getLastUpdateByUserId() != null) {
				lastUpdateBy = userRepository.getUserFullName(userRepository.getEmailId(dto.getLastUpdateByUserId()));
			}

			dto.setUploadedBy(uploadedBy);
			dto.setLastUpdateBy(lastUpdateBy);
			dtoListWithFirstGroupRecord.add(dto);
		}
		return dtoListWithFirstGroupRecord;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.core.repository.reference.ReferenceStagingDataCustomRepository#findByReferenceStatusIn(java.util
	 * .Set)
	 */
	@Override
	public List<ReferenceStagingData> findByReferenceStatusIn(Set<ReferenceStatus> status) {

		QReferenceStagingData referenceStagingData = QReferenceStagingData.referenceStagingData;
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceStagingData);
		query.where(referenceStagingData.referenceStatus.in(status).and(referenceStagingData.active.eq(true))
				.and(referenceStagingData.notificationSent.eq(false)))
				.orderBy(referenceStagingData.referenceStatus.desc()).orderBy(referenceStagingData.couplingId.desc())
				.orderBy(referenceStagingData.primaryCouple.desc());
		return query.list(referenceStagingData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.core.repository.reference.ReferenceStagingDataCustomRepository#findCorrespondenceDataById(java.
	 * lang.Long)
	 */
	@Override
	public CorrespondenceDTO findCorrespondenceDataById(Long corrId) throws ApplicationException {
		QCorrespondenceBase correspondenceBaseData = QCorrespondenceBase.correspondenceBase;
		QOcrData ocrData = QOcrData.ocrData;

		JPAQuery query = new JPAQuery(entityManager);
		query.from(correspondenceBaseData, ocrData);
		query.where(correspondenceBaseData.id.eq(ocrData.correspondenceId.id));
		query.where(correspondenceBaseData.id.eq(corrId));

		return query.singleResult(ConstructorExpression.create(CorrespondenceDTO.class, correspondenceBaseData.id,
				correspondenceBaseData.application.jurisdiction.id,
				correspondenceBaseData.application.jurisdiction.code, correspondenceBaseData.application.id,
				correspondenceBaseData.application.applicationNumber, correspondenceBaseData.mailingDate,
				correspondenceBaseData.createdDate, correspondenceBaseData.documentCode.description,
				correspondenceBaseData.createdByUser, correspondenceBaseData.updatedByUser,
				correspondenceBaseData.updatedDate, ocrData.ocrStatus));
	}

	/**
	 * Gets the data access predicate.
	 *
	 * @return the data access predicate
	 */
	private Predicate getDataAccessPredicate(QApplicationBase application) {
		BooleanBuilder predicate = new BooleanBuilder();
		UserAuthDetail authDetail = BlackboxSecurityContextHolder.getUserAuthData();

		predicate.and(application.jurisdiction.id.in(authDetail.getJurisdictionIds()));
		predicate.and(application.assignee.id.in(authDetail.getAssigneeIds()));
		predicate.and(application.organization.id.in(authDetail.getOrganizationsIds()));
		predicate.and(application.customer.id.in(authDetail.getCustomerIds()));
		predicate.and(application.technologyGroup.id.in(authDetail.getTechnologyGroupIds()));
		return predicate;
	}
}
