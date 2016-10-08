/**
 * 
 */
package com.blackbox.ids.core.repository.impl.reference;

import static com.mysema.query.group.GroupBy.groupBy;
import static com.mysema.query.group.GroupBy.list;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.auth.UserAuthDetail;
import com.blackbox.ids.core.dto.IDS.IDSReferenceRecordDTO;
import com.blackbox.ids.core.dto.IDS.IDSReferenceRecordStatusCountDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSDrillDownInfoDTO;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.dto.reference.PendingNPLReferenceDTO;
import com.blackbox.ids.core.dto.reference.ReferenceDashboardDto;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.correspondence.QCorrespondenceBase;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.mdm.QApplicationBase;
import com.blackbox.ids.core.model.reference.QReferenceBaseData;
import com.blackbox.ids.core.model.reference.QReferenceBaseFPData;
import com.blackbox.ids.core.model.reference.QReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.QReferenceBasePUSData;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.QIDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.reference.ReferenceBaseDataCustomRepository;
import com.blackbox.ids.core.repository.reference.ReferenceBaseDataRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.group.GroupBy;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.PathBuilder;

/**
 * The Class ReferenceBaseDataRepositoryImpl.
 *
 * @author nagarro
 * @param <T>
 *            the generic type
 */
public class ReferenceBaseDataRepositoryImpl<T extends ReferenceBaseData>
		implements ReferenceBaseDataCustomRepository<T> {

	/** The Constant USER_ME. */
	private static final String USER_ME = "ME";

	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;

	/** The reference repo. */
	@Autowired
	private ReferenceBaseDataRepository<ReferenceBaseData> referenceRepo;

	/** The user repository. */
	@Autowired
	private UserRepository userRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.reference.
	 * ReferenceBaseDataCustomRepository#filterRecords(com.mysema.query.types
	 * .Predicate, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<ReferenceBaseData> filterRecords(Predicate predicate, Pageable pageable) {

		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;

		JPAQuery query = new JPAQuery(entityManager);

		query.from(referenceBaseData);

		BooleanBuilder conditions = getDefaultPredicate(referenceBaseData);
		conditions.and(predicate);

		query.where(conditions);

		Querydsl queryDsl = new Querydsl(entityManager,
				new PathBuilder<ReferenceBaseData>(ReferenceBaseData.class, "referenceBaseData"));
		queryDsl.applyPagination(pageable, query);

		long count = referenceRepo.count(conditions);
		Page<ReferenceBaseData> page = buildPage(count, query, pageable, referenceBaseData);

		return page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.reference.
	 * ReferenceBaseDataCustomRepository#dropReference(java.lang.Long)
	 */
	@Override
	public boolean dropReference(Long referenceId) {

		String update = "update ReferenceBaseData rb set rb.active = :deactivate where r.id = :id";
		Query deactivateReferenceQuery = entityManager.createQuery(update).setParameter("deactivate", false)
				.setParameter("id", referenceId);
		deactivateReferenceQuery.executeUpdate();
		return true;
	}

	/**
	 * (non-Javadoc).
	 *
	 * @param corrId
	 *            the corr id
	 * @return the integer
	 * @see com.blackbox.ids.core.repository.reference.ReferenceBaseDataCustomRepository#fetchReferenceCount(java.lang.Long)
	 */
	@Override
	public Integer fetchReferenceCount(Long corrId) {
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		BooleanBuilder predicate = new BooleanBuilder();
		predicate.and(referenceBaseData.correspondence.id.eq(corrId)).and(getDefaultPredicate(referenceBaseData));
		return (int) referenceRepo.count(predicate);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.reference.
	 * ReferenceBaseDataCustomRepository#getNplRecords(com.mysema.query.types
	 * .Predicate, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<ReferenceBaseNPLData> getNplRecords(Predicate predicate, Pageable pageable)
			throws ApplicationException {
		QReferenceBaseNPLData nplData = QReferenceBaseNPLData.referenceBaseNPLData;
		JPAQuery query = new JPAQuery(entityManager);
		query.from(nplData).where(predicate);
		Querydsl queryDsl = new Querydsl(entityManager,
				new PathBuilder<ReferenceBaseNPLData>(ReferenceBaseNPLData.class, "referenceBaseNPLData"));
		queryDsl.applyPagination(pageable, query);
		JPAQuery countQuery = new JPAQuery(entityManager);
		countQuery.from(nplData).where(predicate);
		long count = countQuery.count();

		return count > 0 ? new PageImpl<ReferenceBaseNPLData>(query.list(nplData), pageable, count)
				: new PageImpl<ReferenceBaseNPLData>(new ArrayList<ReferenceBaseNPLData>(), pageable, 0);
	}

	@Override
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.reference.ReferenceBaseDataCustomRepository#getListOfDistinctCorrespondenceId(java.lang.Long)
	 */
	public List<Long> getListOfDistinctCorrespondenceId(Long applicationId) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * returns default mandatory conditions to fetch references.
	 *
	 * @param referenceBaseData
	 *            the reference base data
	 * @return the default predicate
	 */
	private BooleanBuilder getDefaultPredicate(QReferenceBaseData referenceBaseData) {
		List<ReferenceStatus> statusList = new ArrayList<>();
		statusList.add(ReferenceStatus.SOURCE_REF);
		statusList.add(ReferenceStatus.OFFICE_ACTION);
		statusList.add(ReferenceStatus.PENDING);
		statusList.add(ReferenceStatus.DROPPED);

		BooleanBuilder conditions = new BooleanBuilder();
		conditions.and(referenceBaseData.active.eq(true)).and(referenceBaseData.referenceStatus.notIn(statusList));
		return conditions;
	}

	/**
	 * Builds the page.
	 *
	 * @param count
	 *            the count
	 * @param query
	 *            the query
	 * @param pageable
	 *            the pageable
	 * @param referenceBaseData
	 *            the reference base data
	 * @return the page
	 */
	private Page<ReferenceBaseData> buildPage(long count, JPQLQuery query, Pageable pageable,
			EntityPathBase<ReferenceBaseData> referenceBaseData) {
		return count > 0 ? new PageImpl<ReferenceBaseData>(query.list(referenceBaseData), pageable, count)
				: new PageImpl<ReferenceBaseData>(new ArrayList<ReferenceBaseData>(), pageable, 0);
	}

	/**
	 * (non-Javadoc).
	 *
	 * @param predicate
	 *            the predicate
	 * @param pageable
	 *            the pageable
	 * @return the page
	 * @see com.blackbox.ids.core.repository.reference.ReferenceBaseDataCustomRepository#filterCorrespondenceData(com.mysema.query.types.Predicate,
	 *      org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<CorrespondenceDTO> filterCorrespondenceData(final Predicate predicate, final Pageable pageable) {

		QCorrespondenceBase correspondenceBaseData = QCorrespondenceBase.correspondenceBase;
		QApplicationBase applicationBaseData = QApplicationBase.applicationBase;
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		QCorrespondenceBase correspondenceBaseData2 = QCorrespondenceBase.correspondenceBase;
		QCorrespondenceBase correspondenceBaseData3 = QCorrespondenceBase.correspondenceBase;

		BooleanBuilder condition = new BooleanBuilder();
		condition.and(predicate).and(getDefaultPredicate(referenceBaseData));
		condition.and(getDataAccessPredicate(applicationBaseData));

		JPAQuery query = new JPAQuery(entityManager);
		query.from(correspondenceBaseData, applicationBaseData, referenceBaseData);
		query.where(correspondenceBaseData.id.eq(referenceBaseData.correspondence.id));
		query.where(correspondenceBaseData.application.id.eq(applicationBaseData.id));
		query.where(correspondenceBaseData.status.eq(Status.ACTIVE));
		query.where(applicationBaseData.recordStatus.eq(MDMRecordStatus.ACTIVE));
		query.where(condition);
		query.where(
				correspondenceBaseData.id.in((new JPASubQuery().from(correspondenceBaseData3)
						.where(correspondenceBaseData3.mailingDate.in(new JPASubQuery().from(correspondenceBaseData2)
								.groupBy(correspondenceBaseData2.application.id)
								.list(correspondenceBaseData2.mailingDate.max())))
						.list(correspondenceBaseData3.id))));

		query.distinct();

		Querydsl queryDsl = new Querydsl(entityManager,
				new PathBuilder<CorrespondenceBase>(CorrespondenceBase.class, "correspondenceBase"));

		List<CorrespondenceDTO> results = queryDsl.applyPagination(pageable, query)
				.list(ConstructorExpression.create(CorrespondenceDTO.class, correspondenceBaseData.id,
						applicationBaseData.jurisdiction.id, applicationBaseData.jurisdiction.code,
						applicationBaseData.id, applicationBaseData.applicationNumber,
						correspondenceBaseData.mailingDate, correspondenceBaseData.createdDate,
						correspondenceBaseData.documentCode.description, correspondenceBaseData.createdByUser,
						correspondenceBaseData.updatedByUser));

		List<CorrespondenceDTO> dtoListWithFirstGroupRecord = new ArrayList<CorrespondenceDTO>();
		for (CorrespondenceDTO dto : results) {

			Long CreatedByUserId = dto.getUploadedByUserId();
			Long updatedByUserId = dto.getLastUpdateByUserId();

			Long currentId = BlackboxSecurityContextHolder.getUserId();
			String uploadedBy = USER_ME;
			String lastUpdateBy = USER_ME;

			if (CreatedByUserId != currentId) {
				uploadedBy = userRepository.getUserFullName(userRepository.getEmailId(dto.getUploadedByUserId()));
			}

			if (updatedByUserId != currentId) {
				lastUpdateBy = userRepository.getUserFullName(userRepository.getEmailId(dto.getLastUpdateByUserId()));
			}

			// update entered by user details
			Map<String, String> createdDateAndUser = getUser(dto);
			dto.setReferenceEnteredByUsers(createdDateAndUser);

			dto.setUploadedBy(uploadedBy);
			dto.setLastUpdateBy(lastUpdateBy);
			dtoListWithFirstGroupRecord.add(dto);

			// reference count for correspondence
			dto.setReferenceCount(getReferenceCountbyCorrepondenceId(dto.getId()));
		}

		long count = query.count();
		// long count = results.size();

		return count > 0 ? new PageImpl<CorrespondenceDTO>(dtoListWithFirstGroupRecord, pageable, count)
				: new PageImpl<CorrespondenceDTO>(new ArrayList<CorrespondenceDTO>(), pageable, 0);
	}

	/**
	 * (non-Javadoc).
	 *
	 * @param jurisCode
	 *            the juris code
	 * @param appNo
	 *            the app no
	 * @return the list
	 * @throws ApplicationException
	 *             the application exception
	 * @see com.blackbox.ids.core.repository.reference.ReferenceBaseDataCustomRepository#fetchCorrespondenceData(java.lang.Long,
	 *      java.lang.Long)
	 */
	@Override
	public List<CorrespondenceDTO> fetchCorrespondenceData(final String jurisCode, final String appNo)
			throws ApplicationException {

		QCorrespondenceBase correspondenceBaseData = QCorrespondenceBase.correspondenceBase;
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;

		JPAQuery query = new JPAQuery(entityManager);
		query.from(correspondenceBaseData, referenceBaseData);
		query.where(correspondenceBaseData.id.eq(referenceBaseData.correspondence.id));

		BooleanBuilder condition = new BooleanBuilder();
		condition
				.and((correspondenceBaseData.application.applicationNumber.isNotNull()
						.and(correspondenceBaseData.application.applicationNumber.eq(appNo))))
				.and(correspondenceBaseData.application.jurisdiction.code.eq(jurisCode));
		query.where(condition).where(getDefaultPredicate(referenceBaseData));
		Map<Long, List<CorrespondenceDTO>> dtoMap = query.transform(groupBy(referenceBaseData.correspondence.id)
				.as(list(ConstructorExpression.create(CorrespondenceDTO.class, correspondenceBaseData.id,
						correspondenceBaseData.application.jurisdiction.id,
						correspondenceBaseData.application.jurisdiction.code, correspondenceBaseData.application.id,
						correspondenceBaseData.application.applicationNumber, correspondenceBaseData.mailingDate,
						correspondenceBaseData.createdDate, correspondenceBaseData.documentCode.description,
						correspondenceBaseData.createdByUser, correspondenceBaseData.updatedByUser,
						referenceBaseData.createdByUser, referenceBaseData.createdDate))));

		List<CorrespondenceDTO> dtoListWithFirstGroupRecord = new ArrayList<CorrespondenceDTO>();
		for (List<CorrespondenceDTO> dtoList : dtoMap.values()) {
			CorrespondenceDTO dto = dtoList.get(0);

			// update entered by user details
			Map<String, String> createdDateAndUser = getAllUsers(dtoList);

			String uploadedBy = userRepository.getUserFullName(userRepository.getEmailId(dto.getUploadedByUserId()));
			String lastUpdateBy = userRepository
					.getUserFullName(userRepository.getEmailId(dto.getLastUpdateByUserId()));

			dto.setUploadedBy(uploadedBy);
			dto.setLastUpdateBy(lastUpdateBy);
			dto.setReferenceCount(Long.valueOf(dtoList.size()));
			dto.setReferenceEnteredByUsers(createdDateAndUser);
			dtoListWithFirstGroupRecord.add(dto);
		}
		return dtoListWithFirstGroupRecord;
	}

	/**
	 * Gets the all users.
	 *
	 * @param dtoList
	 *            the dto list
	 * @return the all users
	 */
	private Map<String, String> getAllUsers(List<CorrespondenceDTO> dtoList) {

		Map<String, String> map = new HashMap<>();
		for (CorrespondenceDTO dto : dtoList) {
			String referenceCreatedByUser = userRepository
					.getUserFullName(userRepository.getEmailId(dto.getReferenceCreatedByUser()));
			map.put(referenceCreatedByUser, dto.getReferenceCreatedDate());
		}
		return map;
	}

	/**
	 * Gets the users.
	 *
	 * @param dtoList
	 *            the dto list
	 * @return the all users
	 */
	private Map<String, String> getUser(CorrespondenceDTO dto) {

		Map<String, String> map = new HashMap<>();
		String referenceCreatedByUser = userRepository
				.getUserFullName(userRepository.getEmailId(dto.getReferenceCreatedByUser()));
		map.put(referenceCreatedByUser, dto.getReferenceCreatedDate());
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.reference.
	 * ReferenceBaseDataCustomRepository#fetchReferencesByCorrespondenceId(
	 * java.lang.Long)
	 */
	@Override
	public List<ReferenceBaseData> fetchReferencesByCorrespondenceId(Long corrId) throws ApplicationException {

		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceBaseData);
		BooleanBuilder conditions = getDefaultPredicate(referenceBaseData);
		conditions.and(
				referenceBaseData.correspondence.id.isNotNull().and(referenceBaseData.correspondence.id.eq(corrId)));
		query.where(conditions);
		return query.list(referenceBaseData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.reference.
	 * ReferenceBaseDataCustomRepository#getAllPendingNPLReferences(com.
	 * blackbox.ids.core.model.reference.ReferenceStatus,
	 * com.blackbox.ids.core.model.reference.ReferenceType, boolean)
	 */
	@Override
	public List<PendingNPLReferenceDTO> getAllPendingNPLReferences(ReferenceStatus referenceStatus,
			ReferenceType referenceType, boolean active) throws ApplicationException {
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceBaseData);
		query.where(referenceBaseData.active.eq(active).and(referenceBaseData.referenceStatus.eq(referenceStatus))
				.and(referenceBaseData.referenceType.eq(referenceType))
				.and(referenceBaseData.notificationSent.eq(false)));
		List<PendingNPLReferenceDTO> pendingNPLReferences = query
				.list(ConstructorExpression.create(PendingNPLReferenceDTO.class, referenceBaseData.correspondence.id,
						referenceBaseData.referenceBaseDataId, referenceBaseData.correspondence.application));

		return pendingNPLReferences;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.reference.
	 * ReferenceBaseDataCustomRepository#getDuplicateNPLReferenceById(java.
	 * lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public ReferenceDashboardDto getDuplicateNPLReferenceById(Long referenceBaseDataId) {

		QReferenceBaseNPLData referenceBaseNPLData = QReferenceBaseNPLData.referenceBaseNPLData;
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceBaseNPLData);
		query.where(referenceBaseNPLData.referenceBaseDataId.eq(referenceBaseDataId)
				.and(referenceBaseNPLData.referenceStatus.eq(ReferenceStatus.PENDING)));

		ReferenceDashboardDto referenceDashboardDto = query.singleResult(ConstructorExpression.create(
				ReferenceDashboardDto.class, referenceBaseNPLData.stringData,
				referenceBaseNPLData.application.jurisdiction.code, referenceBaseNPLData.application.applicationNumber,
				referenceBaseNPLData.correspondence.documentCode.description,
				referenceBaseNPLData.parentReferenceId.referenceBaseDataId, referenceBaseNPLData.familyId));

		return referenceDashboardDto;

	}

	/**
	 * Fetch references by correspondence id.
	 *
	 * @param corrId
	 *            the corr id
	 * @return the list
	 */
	@Override
	public List<Long> fetchReferenceIdsByCorrespondenceId(final Long corrId) {
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceBaseData);
		BooleanBuilder conditions = getDefaultPredicate(referenceBaseData);
		conditions.and(referenceBaseData.correspondence.id.eq(corrId));
		query.where(conditions);
		return query.list(referenceBaseData.referenceBaseDataId);
	}

	@Override
	public Long getReferenceCountbyCorrepondenceId(Long corrId) {
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		JPAQuery query = new JPAQuery(entityManager);
		long count = query.from(referenceBaseData).where(referenceBaseData.correspondence.id.eq(corrId))
				.where(getDefaultPredicate(referenceBaseData)).count();
		return count;
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

	@Override
	public List<ReferenceDashboardDto> getDuplicateNPLByFamilyId(String familyId, Long referenceBaseDataId) {

		QReferenceBaseNPLData referenceBaseData = QReferenceBaseNPLData.referenceBaseNPLData;
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceBaseData);
		BooleanBuilder conditions = new BooleanBuilder();
		conditions.and(referenceBaseData.familyId.eq(familyId))
				.and(referenceBaseData.referenceStatus.ne(ReferenceStatus.DROPPED))
				.and(referenceBaseData.referenceBaseDataId.ne(referenceBaseDataId));
		query.where(conditions);
		List<ReferenceDashboardDto> referenceDashboardList = query.list(ConstructorExpression.create(
				ReferenceDashboardDto.class, referenceBaseData.stringData, referenceBaseData.application.familyId,
				referenceBaseData.application.jurisdiction.code, referenceBaseData.application.applicationNumber,
				referenceBaseData.application.attorneyDocketNumber.segment,
				referenceBaseData.correspondence.documentCode.description));

		return referenceDashboardList;

	}

	@Override
	public List<ReferenceBaseData> getReferenceByApplicationAndFamilyId(Long applicationId, String familyId) {

		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceBaseData);
		BooleanBuilder conditions = new BooleanBuilder();
		conditions.and(referenceBaseData.familyId.eq(familyId))
				.and(referenceBaseData.referenceStatus.ne(ReferenceStatus.DROPPED))
				.and(referenceBaseData.application.id.eq(applicationId));
		query.where(conditions);
		return query.list(referenceBaseData);

	}

/*	@Override
	public List<IDSReferenceRecordDTO> fetchAllIDSReferenceRecords(Predicate predicate, Pageable pageable) {
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QReferenceBasePUSData referenceBasePUSData = QReferenceBasePUSData.referenceBasePUSData;
		QReferenceBaseFPData referenceBaseFPData = QReferenceBaseFPData.referenceBaseFPData;
		QReferenceBaseNPLData referenceBaseNPLData = QReferenceBaseNPLData.referenceBaseNPLData;
		
		Long patentCount = 0L;
		Long nplCount = 0L;
		
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceBaseData).leftJoin(referenceBasePUSData).on(referenceBaseData.referenceBaseDataId.eq(referenceBasePUSData.referenceBaseDataId));
		query.leftJoin(referenceBaseFPData).on(referenceBaseData.referenceBaseDataId.eq(referenceBaseFPData.referenceBaseDataId));
		query.leftJoin(referenceBaseNPLData).on(referenceBaseData.referenceBaseDataId.eq(referenceBaseNPLData.referenceBaseDataId));
		query.join(referenceFlow);
		query.where(referenceBaseData.referenceBaseDataId.eq(referenceFlow.referenceBaseData.referenceBaseDataId));
		query.where(predicate);
		
		if (referenceFlow.referenceFlowStatus.equals(ReferenceFlowStatus.CITED)) {
			query.where(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.CITED));
			JPAQuery patentCountquery = query.clone();
			patentCount = patentCountquery.where(referenceBaseData.referenceType.ne(ReferenceType.NPL)).count();
			
			JPAQuery nplCountquery = query.clone();
			nplCount = nplCountquery.where(referenceBaseData.referenceType.eq(ReferenceType.NPL)).count();
		} else if (referenceFlow.referenceFlowStatus.equals(ReferenceFlowStatus.UNCITED)){
			
		} else if (referenceFlow.referenceFlowStatus.equals(ReferenceFlowStatus.EXAMINER_CITED)) {
			
		} //else if (referenceFlow.referenceFlowStatus.equals(ReferenceFlowStatus.)) {
		

		
		query.distinct();
		
		long count = query.count();
		
		List<IDSReferenceRecordDTO> items = Collections.emptyList();
		
		if (count > 0L){
			items = query.list(ConstructorExpression.create(IDSReferenceRecordDTO.class, referenceBaseData, referenceFlow));
		}
		
		
		return null;
	}*/


	@Override
	public IDSReferenceRecordStatusCountDTO fetchAllTabStatusCount(final Predicate predicate) {
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceFlow,referenceBaseData);
		query.where(referenceBaseData.referenceBaseDataId.eq(referenceFlow.referenceBaseData.referenceBaseDataId));
		query.where(predicate);
		query.where(referenceFlow.referenceFlowStatus.ne(ReferenceFlowStatus.DROPPED));
		query.distinct();
		
		JPAQuery citedCountQuery = query.clone();
		citedCountQuery.where(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.CITED));
		Long citedCount = citedCountQuery.count();
		
		JPAQuery citedPatentCountQuery = citedCountQuery.clone();
		citedPatentCountQuery.where(referenceFlow.referenceBaseData.referenceType.ne(ReferenceType.NPL));
		Long citedpatentCount = citedPatentCountQuery.count();
		
		JPAQuery citedNplCountQuery = citedCountQuery.clone();
		citedNplCountQuery.where(referenceFlow.referenceBaseData.referenceType.eq(ReferenceType.NPL));
		Long citedNplCount = citedNplCountQuery.count();
		
		JPAQuery uncitedCountQuery = query.clone();
		uncitedCountQuery.where(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.UNCITED));
		Long unCitedCount = uncitedCountQuery.count();
		
		JPAQuery uncitedPatentCountQuery = uncitedCountQuery.clone();
		uncitedPatentCountQuery.where(referenceFlow.referenceBaseData.referenceType.ne(ReferenceType.NPL));
		Long uncitedpatentCount = citedPatentCountQuery.count();
		
		JPAQuery uncitedNplCountQuery = uncitedCountQuery.clone();
		uncitedNplCountQuery.where(referenceFlow.referenceBaseData.referenceType.eq(ReferenceType.NPL));
		Long uncitedNplCount = uncitedNplCountQuery.count();
		
		JPAQuery examinerCountQuery = query.clone();
		examinerCountQuery.where(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.EXAMINER_CITED));
		Long examinerCount = examinerCountQuery.count();
		
		JPAQuery examinerPatentCountQuery = examinerCountQuery.clone();
		examinerPatentCountQuery.where(referenceFlow.referenceBaseData.referenceType.ne(ReferenceType.NPL));
		Long examinerpatentCount = examinerPatentCountQuery.count();
		
		JPAQuery examinerNplCountQuery = examinerCountQuery.clone();
		examinerNplCountQuery.where(referenceFlow.referenceBaseData.referenceType.eq(ReferenceType.NPL));
		Long examinerNplCount = examinerNplCountQuery.count();
		
		JPAQuery citeInParentCountQuery = query.clone();
		citeInParentCountQuery.where(referenceFlow.referenceFlowSubStatus.eq(ReferenceFlowSubStatus.CITED_IN_PARENT));
		Long citeInParentCount = citeInParentCountQuery.count();
		
		JPAQuery citeInParentPatentCountQuery = citeInParentCountQuery.clone();
		citeInParentPatentCountQuery.where(referenceFlow.referenceBaseData.referenceType.ne(ReferenceType.NPL));
		Long citeInParentpatentCount = citeInParentPatentCountQuery.count();
		
		JPAQuery citeInParentNplCountQuery = citeInParentPatentCountQuery.clone();
		citeInParentNplCountQuery.where(referenceFlow.referenceBaseData.referenceType.eq(ReferenceType.NPL));
		Long citeInParentNplCount = citeInParentNplCountQuery.count();
		
		JPAQuery doNotFileCountQuery = query.clone();
		doNotFileCountQuery.where(referenceFlow.doNotFile.eq(true));
		Long doNotFileCount = doNotFileCountQuery.count();
		
		JPAQuery doNotFilePatentCountQuery = citeInParentCountQuery.clone();
		doNotFilePatentCountQuery.where(referenceFlow.referenceBaseData.referenceType.ne(ReferenceType.NPL));
		Long doNotFilepatentCount = doNotFilePatentCountQuery.count();
		
		JPAQuery doNotFileNplCountQuery = citeInParentPatentCountQuery.clone();
		doNotFileNplCountQuery.where(referenceFlow.referenceBaseData.referenceType.eq(ReferenceType.NPL));
		Long doNotFileNplCount = doNotFileNplCountQuery.count();
		
		JPAQuery deletedCountQuery = query.clone();
		deletedCountQuery.where(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.DROPPED));
		Long deletedCount = deletedCountQuery.count();
		
		JPAQuery deletedPatentCountQuery = citeInParentCountQuery.clone();
		deletedPatentCountQuery.where(referenceFlow.referenceBaseData.referenceType.ne(ReferenceType.NPL));
		Long deletedpatentCount = deletedPatentCountQuery.count();
		
		JPAQuery deletedNplCountQuery = citeInParentPatentCountQuery.clone();
		deletedNplCountQuery.where(referenceFlow.referenceBaseData.referenceType.eq(ReferenceType.NPL));
		Long deletedNplCount = deletedNplCountQuery.count();
		
		return new IDSReferenceRecordStatusCountDTO(citedCount,unCitedCount,examinerCount,citeInParentCount,doNotFileCount,deletedCount,
				citedpatentCount,citedNplCount,uncitedpatentCount,uncitedNplCount,examinerpatentCount,examinerNplCount,citeInParentpatentCount,citeInParentNplCount,
				doNotFilepatentCount,doNotFileNplCount,deletedpatentCount,deletedNplCount);
		
	}

	@Override
	public Page<IDSReferenceRecordDTO> fetchNPLReferenceRecords(Predicate predicate, Pageable pageable) {
		// TODO Auto-generated method stub
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceFlow,referenceBaseData);
		query.where(referenceBaseData.referenceBaseDataId.eq(referenceFlow.referenceBaseData.referenceBaseDataId));
		query.where(predicate);
		query.where(referenceFlow.referenceBaseData.referenceType.eq(ReferenceType.NPL));
		
		List<IDSReferenceRecordDTO> nplList = Collections.emptyList();
		
		Querydsl queryDsl = new Querydsl(entityManager,
				new PathBuilder<IDSReferenceFlow>(IDSReferenceFlow.class, "iDSReferenceFlow"));
		
		long count = query.count();
		
		if (count > 0L){
			nplList = queryDsl.applyPagination(pageable, query).list(ConstructorExpression.create(IDSReferenceRecordDTO.class, referenceBaseData, referenceFlow));
		}
		
		return count > 0 ? new PageImpl<IDSReferenceRecordDTO>(nplList, pageable, count)
				: new PageImpl<IDSReferenceRecordDTO>(new ArrayList<IDSReferenceRecordDTO>(), pageable, 0);
	}

	@Override
	public Page<IDSReferenceRecordDTO> fetchPatentReferenceRecords(Predicate predicate, Pageable pageable) {
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceFlow,referenceBaseData);
		query.where(referenceBaseData.referenceBaseDataId.eq(referenceFlow.referenceBaseData.referenceBaseDataId));
		query.where(predicate);
		query.where(referenceFlow.referenceBaseData.referenceType.ne(ReferenceType.NPL));
		
		List<IDSReferenceRecordDTO> patentList = Collections.emptyList();
		
		long count = query.count();
		
		Querydsl queryDsl = new Querydsl(entityManager,
				new PathBuilder<IDSReferenceFlow>(IDSReferenceFlow.class, "iDSReferenceFlow"));
		
		if (count > 0L){
			patentList = queryDsl.applyPagination(pageable, query).list(ConstructorExpression.create(IDSReferenceRecordDTO.class, referenceBaseData, referenceFlow));
		}
		
		List<IDSReferenceRecordDTO> dtoListWithCreateName = new ArrayList<IDSReferenceRecordDTO>();
		for (IDSReferenceRecordDTO dto : patentList) {

			Long CreatedByUserId = dto.getRefEnteredById();

			Long currentId = BlackboxSecurityContextHolder.getUserId();
			String createdBy = USER_ME;

			if (CreatedByUserId != currentId) {
				createdBy = userRepository.getUserFirstAndLastName(CreatedByUserId);
			}

			dto.setRefEnteredBy(createdBy);
			dtoListWithCreateName.add(dto);
		}
		
		
		return count > 0 ? new PageImpl<IDSReferenceRecordDTO>(dtoListWithCreateName, pageable, count)
				: new PageImpl<IDSReferenceRecordDTO>(new ArrayList<IDSReferenceRecordDTO>(), pageable, 0);
	}

	@Override
	public Map<String, Long> fetchDistinctFilingDateAndCount() {
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		
		Map<Calendar, Long> filingDateCounts = new HashMap<>();
		
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceFlow,referenceBaseData);
		filingDateCounts = query.where(referenceBaseData.referenceBaseDataId.eq(referenceFlow.referenceBaseData.referenceBaseDataId)).groupBy(referenceFlow.filingDate).transform(GroupBy.groupBy(referenceFlow.filingDate).as(referenceFlow.filingDate.count()));
		
		Map<String, Long> filingDateAndCount = new HashMap<String, Long>();
		Iterator<Map.Entry<Calendar, Long>> entries = filingDateCounts.entrySet().iterator();
		while (entries.hasNext()) {
		    Map.Entry<Calendar, Long> entry = entries.next();
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		    filingDateAndCount.put(entry.getKey() != null ? BlackboxDateUtil.calendarToStr(entry.getKey(),TimestampFormat.MMMDDYYYY) : null, entry.getValue());
		}
		return filingDateAndCount;
	}

	@Override
	public IDSReferenceRecordDTO fetchNplAndPatentCount() {
		
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceFlow,referenceBaseData);
		query.where(referenceBaseData.referenceBaseDataId.eq(referenceFlow.referenceBaseData.referenceBaseDataId));
		
		JPAQuery patentQry = query.clone();
		patentQry.where(referenceFlow.referenceBaseData.referenceType.ne(ReferenceType.NPL));
		
		Long patentCount = patentQry.count();
		
		JPAQuery nplQry = query.clone();
		nplQry.where(referenceFlow.referenceBaseData.referenceType.eq(ReferenceType.NPL));
		
		Long nplCount = nplQry.count();
		
		return new IDSReferenceRecordDTO(patentCount,nplCount);
	}

	@Override
	public List<ReferenceFlowSubStatus> fetchChangeSubStatus() {
		QReferenceBaseData referenceBaseData = QReferenceBaseData.referenceBaseData;
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceFlow,referenceBaseData);
		query.where(referenceBaseData.referenceBaseDataId.eq(referenceFlow.referenceBaseData.referenceBaseDataId)).distinct();
		
		Long count = query.count();
		query.groupBy(referenceFlow.referenceFlowSubStatus).distinct();
		List<ReferenceFlowSubStatus> subStatus = Collections.emptyList();
		
		if (count > 0L){
			subStatus = query.list(referenceFlow.referenceFlowSubStatus);
		}
		
		return subStatus;
	}

	@Override
	public IDSReferenceRecordDTO fetchSourceDocumentData(Long refFlowId) {
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		
		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceFlow);
		query.where(referenceFlow.referenceFlowId.eq(refFlowId));
		List<IDSReferenceRecordDTO> sourceDocList = null;
		
		if (query.count() > 0L){
			sourceDocList = query.list(ConstructorExpression.create(IDSReferenceRecordDTO.class,referenceFlow));
		}
		return sourceDocList.get(0);
	}
}