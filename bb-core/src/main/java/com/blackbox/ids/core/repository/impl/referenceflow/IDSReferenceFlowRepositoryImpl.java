/**
 *
 */
package com.blackbox.ids.core.repository.impl.referenceflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.auth.UserAuthDetail;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dto.IDS.IDSFeeCalculationDetails;
import com.blackbox.ids.core.dto.IDS.QIDSFeeCalculationDetails;
import com.blackbox.ids.core.dto.IDS.ReferenceRecordDTO;
import com.blackbox.ids.core.dto.IDS.ReferenceRecordsFilter;
import com.blackbox.ids.core.dto.IDS.dashboard.CitedReferenceDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDS1449ReferenceDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.UpdateRefStatusDTO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.referenceflow.ReferenceFlowDTO;
import com.blackbox.ids.core.dto.referenceflow.ReferenceFlowRuleDTO;
import com.blackbox.ids.core.model.QUser;
import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.IDS.QIDS;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.QApplicationBase;
import com.blackbox.ids.core.model.mstr.Status;
import com.blackbox.ids.core.model.reference.QReferenceBaseData;
import com.blackbox.ids.core.model.reference.QReferenceBaseFPData;
import com.blackbox.ids.core.model.reference.QReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.QReferenceBasePUSData;
import com.blackbox.ids.core.model.reference.QSourceReference;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceRuleType;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlowRuleExclusion;
import com.blackbox.ids.core.model.referenceflow.IDSSourceReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.IDSSourceReferenceFlowFilingInfo;
import com.blackbox.ids.core.model.referenceflow.QIDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.QIDSSourceReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.QIDSSourceReferenceFlowFilingInfo;
import com.blackbox.ids.core.model.referenceflow.QSubjectMatterLink;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlow1449Pending;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.blackbox.ids.core.model.referenceflow.SourceReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.SourceReferenceFlowSubStatus;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.referenceflow.IDSReferenceFlowCustomRepository;
import com.blackbox.ids.core.repository.referenceflow.IDSReferenceFlowRuleExclusionRepository;
import com.blackbox.ids.core.repository.referenceflow.IDSSourceReferenceFlowFilingInfoRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;

/**
 * @author nagarro
 *
 */
@Repository
public class IDSReferenceFlowRepositoryImpl implements IDSReferenceFlowCustomRepository<IDSReferenceFlow> {

	private static final Logger LOGGER = Logger.getLogger(IDSReferenceFlowRepositoryImpl.class);

	/** The Constant USER_ME. */
	private static final String USER_ME = "ME";

	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ApplicationBaseDAO applicationBaseDao;

	/** The user repository. */
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private IDSReferenceFlowRuleExclusionRepository<IDSReferenceFlowRuleExclusion> iDSReferenceFlowRuleExclusionRepository;

	@Autowired
	private IDSSourceReferenceFlowFilingInfoRepository idsSourceReferenceFlowFilingInfoRepository;

	@Autowired
	ReferenceFlow1449PendingRepository<ReferenceFlow1449Pending> referenceFlow1449PendingRepository;

	@Override
	public Page<ReferenceFlowDTO> filterAllRecords(final Predicate predicate, final Pageable pageable) {

		LOGGER.debug("Filter all records");
		QApplicationBase applicationBase = QApplicationBase.applicationBase;
		QSubjectMatterLink subjectMatterLink = QSubjectMatterLink.subjectMatterLink;
		QUser user = QUser.user;

		JPAQuery query = new JPAQuery(entityManager);
		query.from(applicationBase, user).where(applicationBase.createdByUser.eq(user.id));
		query.where(applicationBase.recordStatus.eq(MDMRecordStatus.ACTIVE));
		query.where(predicate);

		long count = query.singleResult(applicationBase.familyId.countDistinct());
		JPAQuery familyMemberCountQuery = query.clone();
		query.distinct();

		Querydsl queryDsl = new Querydsl(entityManager,
				new PathBuilder<ApplicationBase>(ApplicationBase.class, "applicationBase"));
		queryDsl.applyPagination(pageable, query);

		List<String> familyIdList = query.list(applicationBase.familyId);

		List<ReferenceFlowDTO> dtoList = new ArrayList<>();
		for (String id : familyIdList) {
			ReferenceFlowDTO dto = new ReferenceFlowDTO();
			dto.setFamilyId(id);
			query = new JPAQuery(entityManager);
			query.from(subjectMatterLink);

			// fetch count of family member and subject matter
			long subjectMatterCount = query
					.where(subjectMatterLink.sourceFamilyId.eq(id).and(subjectMatterLink.status.eq(Status.ACTIVE)))
					.count();
			long familyMemberCount = familyMemberCountQuery.where(applicationBase.familyId.eq(id)).count();

			dto.setFamilyMemberCount(familyMemberCount);
			dto.setSubjectMatterCount(subjectMatterCount);
			dtoList.add(dto);
		}
		return count > 0 ? new PageImpl<ReferenceFlowDTO>(dtoList, pageable, count)
				: new PageImpl<ReferenceFlowDTO>(new ArrayList<ReferenceFlowDTO>(), pageable, 0);
	}

	@Override
	public Page<ReferenceFlowRuleDTO> filterAllNotifications(final Predicate predicate, final Pageable pageable) {
		// TODO Auto-generated method stub
		return testNotifications(predicate, pageable);
	}

	// TODO remove after use
	private Page<ReferenceFlowRuleDTO> testNotifications(Predicate predicate, Pageable pageable) {
		List<ReferenceFlowRuleDTO> list = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			ReferenceFlowRuleDTO dto = new ReferenceFlowRuleDTO();
			dto.setReferenceFlowRuleId(1L);
			MdmRecordDTO sourcedto = new MdmRecordDTO();
			sourcedto.setApplicationNumber("12345678");
			sourcedto.setAttorneyDocket("123.123.123.123");
			sourcedto.setJurisdiction("US");

			MdmRecordDTO targetdto = new MdmRecordDTO();
			targetdto.setApplicationNumber("87654321");
			targetdto.setAttorneyDocket("125.125.125.125");
			targetdto.setJurisdiction("WO");

			dto.setSourceApplication(sourcedto);
			dto.setTargetApplication(targetdto);
			dto.setFamilyId("F0000" + i);
			dto.setReferenceDocument("Reference Document");
			list.add(dto);
		}

		return new PageImpl<ReferenceFlowRuleDTO>(list, pageable, 30);
	}

	@Override
	public Page<ReferenceFlowRuleDTO> filterFamilyLinks(final Predicate predicate, final Pageable pageable,
			final String familyId, final Long applicationId) {

		List<ReferenceFlowRuleDTO> refFlowDtoList = generateFamilyLinks(familyId, applicationId, pageable);

		int count = refFlowDtoList.size();
		return count > 0 ? new PageImpl<ReferenceFlowRuleDTO>(refFlowDtoList, pageable, count)
				: new PageImpl<ReferenceFlowRuleDTO>(new ArrayList<ReferenceFlowRuleDTO>(), pageable, 0);
	}

	@Override
	public List<ReferenceFlowRuleDTO> generateFamilyLinks(final String familyId, final Long sourceApplicationId,
			final Pageable pageable) {

		ApplicationBase sourceAppBase = new ApplicationBase();
		sourceAppBase.setId(sourceApplicationId);
		List<IDSReferenceFlowRuleExclusion> exclusions = iDSReferenceFlowRuleExclusionRepository
				.findBySourceApplicationAndReferenceRuleType(sourceAppBase, ReferenceRuleType.F);

		List<ReferenceFlowRuleDTO> refFlowRuleDTO = buildAndCreateIntermediateReferenceFlowRule(familyId,
				sourceApplicationId, pageable);

		return generateFlowRules(sourceApplicationId, exclusions, refFlowRuleDTO);
	}

	@Override
	public Page<ReferenceFlowRuleDTO> filterSubjectMatterLinks(final Predicate predicate, final Pageable pageable,
			final String targetFamilyId, final Long sourceApplicationId) {

		List<ReferenceFlowRuleDTO> refFlowDtoList = generateSubjectMatterLinks(targetFamilyId, sourceApplicationId,
				pageable);

		return new PageImpl<ReferenceFlowRuleDTO>(refFlowDtoList, pageable, 0);
	}

	@Override
	public List<ReferenceFlowRuleDTO> generateSubjectMatterLinks(final String targetFamilyId,
			final Long sourceApplicationId, final Pageable pageable) {

		ApplicationBase sourceAppBase = new ApplicationBase();
		sourceAppBase.setId(sourceApplicationId);
		List<IDSReferenceFlowRuleExclusion> exclusions = iDSReferenceFlowRuleExclusionRepository
				.findBySourceApplicationAndReferenceRuleType(sourceAppBase, ReferenceRuleType.SML);

		List<ReferenceFlowRuleDTO> refFlowRuleDTO = buildAndCreateIntermediateReferenceFlowRule(targetFamilyId,
				sourceApplicationId, pageable);

		return generateFlowRules(sourceApplicationId, exclusions, refFlowRuleDTO);
	}

	/**
	 * @param targetFamilyId
	 * @param pageable
	 * @param applicationBase
	 * @return
	 */
	private List<ReferenceFlowRuleDTO> buildAndCreateIntermediateReferenceFlowRule(final String targetFamilyId,
			final Long sourceApplicationId, final Pageable pageable) {

		QApplicationBase applicationBase = QApplicationBase.applicationBase;
		JPAQuery query = new JPAQuery(entityManager);
		query.from(applicationBase);
		query.where(getDataAccessPredicate(applicationBase));
		query.where(applicationBase.recordStatus.eq(MDMRecordStatus.ACTIVE));

		// this predicate has family id filter
		query.where(applicationBase.familyId.equalsIgnoreCase(targetFamilyId));
		query.where(applicationBase.id.ne(sourceApplicationId));
		query.distinct();

		if (pageable != null) {
			Querydsl queryDsl = new Querydsl(entityManager,
					new PathBuilder<ApplicationBase>(ApplicationBase.class, "applicationBase"));
			queryDsl.applyPagination(pageable, query);
		}
		return query.list(ConstructorExpression.create(ReferenceFlowRuleDTO.class, applicationBase));
	}

	/**
	 * @param sourceApplicationId
	 * @param exclusions
	 * @param refFlowRuleDTO
	 * @return
	 */
	private List<ReferenceFlowRuleDTO> generateFlowRules(final Long sourceApplicationId,
			List<IDSReferenceFlowRuleExclusion> exclusions, List<ReferenceFlowRuleDTO> refFlowRuleDTO) {

		Iterator<ReferenceFlowRuleDTO> listIterator = refFlowRuleDTO.iterator();

		MdmRecordDTO sourceApplication = applicationBaseDao.fetchApplicationData(sourceApplicationId);

		while (listIterator.hasNext()) {
			ReferenceFlowRuleDTO dto = listIterator.next();

			// set source application detail in every dto
			dto.setSourceApplication(sourceApplication);

			for (IDSReferenceFlowRuleExclusion exclusion : exclusions) {

				// update target application detail if exclusion is found
				if (exclusion.getSourceApplication().getId() == sourceApplicationId
						&& exclusion.getTargetApplication().getId() == dto.getTargetApplication().getDbId()) {

					dto.setComments(exclusion.getComments());
					updateAuditDates(dto, exclusion);
					updateAuditUsers(dto, exclusion);
					dto.setStatus(exclusion.getStatus());
					break;
				}
			}
		}
		return refFlowRuleDTO;
	}

	@Override
	public Page<MdmRecordDTO> findApplicationsByFamilyId(final Predicate predicate, final Pageable pageable) {
		return null;
	}

	@Override
	public Long fetchNotificationCount() {
		return 7L;
	}

	/**
	 * returns default mandatory conditions to fetch references.
	 *
	 * @param referenceBaseData
	 *            the reference base data
	 * @return the default predicate
	 */
	private static BooleanBuilder getDefaultPredicate(QReferenceBaseData referenceBaseData) {
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
	 * Gets the data access predicate.
	 *
	 * @return the data access predicate
	 */
	private static Predicate getDataAccessPredicate(QApplicationBase application) {
		BooleanBuilder predicate = new BooleanBuilder();
		UserAuthDetail authDetail = BlackboxSecurityContextHolder.getUserAuthData();

		predicate.and(application.jurisdiction.id.in(authDetail.getJurisdictionIds()));
		predicate.and(application.assignee.id.in(authDetail.getAssigneeIds()));
		predicate.and(application.organization.id.in(authDetail.getOrganizationsIds()));
		predicate.and(application.customer.id.in(authDetail.getCustomerIds()));
		predicate.and(application.technologyGroup.id.in(authDetail.getTechnologyGroupIds()));
		return predicate;
	}

	/**
	 * @param dto
	 * @param exclusion
	 */
	private static void updateAuditDates(ReferenceFlowRuleDTO dto, IDSReferenceFlowRuleExclusion exclusion) {
		String date = exclusion.getCreatedDate() != null
				? BlackboxDateUtil.dateToStr(exclusion.getCreatedDate().getTime(), TimestampFormat.MMMDDYYYY) : null;
		dto.setCreatedDate(date);

		date = exclusion.getUpdatedDate() != null
				? BlackboxDateUtil.dateToStr(exclusion.getUpdatedDate().getTime(), TimestampFormat.MMMDDYYYY) : null;
		dto.setModifiedDate(date);
	}

	/**
	 * @param dto
	 * @param exclusion
	 */
	private void updateAuditUsers(ReferenceFlowRuleDTO dto, IDSReferenceFlowRuleExclusion exclusion) {
		Long createdByUserId = exclusion.getCreatedByUser();
		Long updatedByUserId = exclusion.getUpdatedByUser();

		Long currentId = BlackboxSecurityContextHolder.getUserId();
		String createtedBy = USER_ME;
		String lastUpdateBy = USER_ME;

		if (createdByUserId != currentId) {
			createtedBy = userRepository.getUserFullName(userRepository.getEmailId(exclusion.getCreatedByUser()));
		}

		if (updatedByUserId != currentId) {
			lastUpdateBy = userRepository.getUserFullName(userRepository.getEmailId(exclusion.getUpdatedByUser()));
		}

		dto.setCreatedBy(createtedBy);
		dto.setModifiedBy(lastUpdateBy);
	}

	@Override
	public long claimRefFlowsForIDS(final long targetApplication, final Long idsId) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		return new JPAUpdateClause(entityManager, refFlow).set(refFlow.ids, new IDS(idsId))
				.where(refFlow.targetApplication.id.eq(targetApplication)
						.and(refFlow.referenceFlowStatus.eq(ReferenceFlowStatus.UNCITED)).and(refFlow.ids.isNull())
						.and(refFlow.referenceFlowSubStatus.in(
								Arrays.asList(ReferenceFlowSubStatus.NULL, ReferenceFlowSubStatus.CITED_IN_PARENT))))
				.execute();
	}

	@Override
	public long releaseRefFlowsFromIDS(final long ids, final long releasedBy) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		return new JPAUpdateClause(entityManager, refFlow).setNull(refFlow.ids)
				.set(refFlow.updatedDate, Calendar.getInstance()).set(refFlow.updatedByUser, releasedBy)
				.where(refFlow.ids.id.eq(ids)).execute();
	}

	@Override
	public SearchResult<ReferenceRecordDTO> fetchUSPatents(ReferenceRecordsFilter filter, PaginationInfo pageInfo) {

		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QReferenceBasePUSData refBase = QReferenceBasePUSData.referenceBasePUSData;
		JPAQuery query = new JPAQuery(entityManager).from(refFlow, refBase);

		// Set access predicate
		BooleanBuilder predicate = referenceFetchCondition(refFlow, filter);

		query.where(predicate.and(refFlow.referenceBaseData.referenceBaseDataId.eq(refBase.referenceBaseDataId))
				.and(refBase.referenceType.eq(filter.getReferenceType())).and(refBase.referenceFlowFlag.eq(true))
				.and(refBase.active.eq(true)));

		// Fetch records count.
		long totalRecords = query.count();
		List<ReferenceRecordDTO> listData = null;

		if (totalRecords > 0L) {
			// Set pagination details
			query.limit(pageInfo.getLimit()).offset(pageInfo.getOffset()).orderBy(refFlow.referenceFlowId.asc());

			// Fetch record details
			listData = query.list(ConstructorExpression.create(ReferenceRecordDTO.class, refFlow.referenceFlowId,
					refBase.convertedPublicationNumber, refBase.kindCode, refBase.publicationDate,
					refBase.applicantName, refBase.referenceComments, refBase.updatedByUser));
		}

		long recordsFiltered = pageInfo.getLimit() + pageInfo.getOffset();
		return new SearchResult<>(totalRecords, (recordsFiltered > totalRecords ? totalRecords : recordsFiltered),
				listData == null ? Collections.emptyList() : listData);
	}

	@Override
	public List<IDS1449ReferenceDTO> fetch1449USPatents(ReferenceType type, String finalIdsId) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QReferenceBasePUSData refBase = QReferenceBasePUSData.referenceBasePUSData;
		JPAQuery query = new JPAQuery(entityManager).from(refFlow, refBase);
		BooleanBuilder predicate = referenceRecordPredicate(refFlow, finalIdsId);
		predicate = predicate.and(refFlow.referenceBaseData.referenceBaseDataId.eq(refBase.referenceBaseDataId))
				.and(refBase.referenceType.eq(type)).and(refBase.active.eq(true));
		return query.where(predicate)
				.list(ConstructorExpression.create(IDS1449ReferenceDTO.class, refFlow.referenceFlowId, refFlow.citeId,
						refBase.kindCode, refBase.convertedPublicationNumber, refBase.publicationDate,
						refBase.applicantName, refFlow.referenceFlowStatus, refFlow.referenceFlowSubStatus));
	}

	@Override
	public List<IDS1449ReferenceDTO> fetch1449foreignData(ReferenceType type, String finalIdsId) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QReferenceBaseFPData refBase = QReferenceBaseFPData.referenceBaseFPData;
		JPAQuery query = new JPAQuery(entityManager).from(refFlow, refBase);
		BooleanBuilder predicate = referenceRecordPredicate(refFlow, finalIdsId);
		predicate = predicate.and(refFlow.referenceBaseData.referenceBaseDataId.eq(refBase.referenceBaseDataId))
				.and(refBase.referenceType.eq(type)).and(refBase.active.eq(true));
		return query.where(predicate)
				.list(ConstructorExpression.create(IDS1449ReferenceDTO.class, refFlow.referenceFlowId, refFlow.citeId,
						refBase.kindCode, refBase.convertedForeignDocumentNumber, refBase.publicationDate,
						refBase.applicantName, refBase.jurisdiction.code, refFlow.referenceFlowStatus,
						refFlow.referenceFlowSubStatus));
	}

	@Override
	public List<IDS1449ReferenceDTO> fetch1449NplData(ReferenceType type, String finalIdsId) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QReferenceBaseNPLData refBase = QReferenceBaseNPLData.referenceBaseNPLData;
		JPAQuery query = new JPAQuery(entityManager).from(refFlow, refBase);
		BooleanBuilder predicate = referenceRecordPredicate(refFlow, finalIdsId);
		predicate = predicate.and(refFlow.referenceBaseData.referenceBaseDataId.eq(refBase.referenceBaseDataId))
				.and(refBase.referenceType.eq(type)).and(refBase.active.eq(true));
		return query.where(predicate)
				.list(ConstructorExpression.create(IDS1449ReferenceDTO.class, refFlow.referenceFlowId, refFlow.citeId,
						refFlow.referenceFlowStatus, refFlow.referenceFlowSubStatus, refBase.stringData));
	}

	private BooleanBuilder referenceRecordPredicate(QIDSReferenceFlow refFlow, String finalIdsId) {
		return new BooleanBuilder()
				.and(refFlow.internalFinalIDSId.eq(finalIdsId).or(refFlow.externalFinalIDSId.eq(finalIdsId)))
				.and(refFlow.referenceFlowStatus.eq(ReferenceFlowStatus.CITED))
				.and(refFlow.referenceFlowSubStatus.eq(ReferenceFlowSubStatus.PENDING_1449));
	}

	@Override
	public long updateReferenceRecords(Map<Long, String> idStatusMap) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		long updatedRecords = 0L;
		for (Entry<Long, String> entry : idStatusMap.entrySet()) {
			new JPAUpdateClause(entityManager, refFlow)
					.set(refFlow.referenceFlowSubStatus, ReferenceFlowSubStatus.fromString(entry.getValue()))
					.where(refFlow.referenceFlowId.eq(entry.getKey())).execute();
			updatedRecords++;
		}
		return updatedRecords;
	}

	@Override
	public SearchResult<ReferenceRecordDTO> fetchForeignData(ReferenceRecordsFilter filter, PaginationInfo pageInfo) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QReferenceBaseFPData refBase = QReferenceBaseFPData.referenceBaseFPData;

		JPAQuery query = new JPAQuery(entityManager).from(refFlow, refBase);

		// Set access predicate.
		BooleanBuilder predicate = referenceFetchCondition(refFlow, filter);

		query.where(refFlow.referenceBaseData.referenceBaseDataId.eq(refBase.referenceBaseDataId)
				.and(refBase.referenceType.eq(filter.getReferenceType())).and(refBase.referenceFlowFlag.eq(true))
				.and(refBase.active.eq(true)).and(predicate));

		// Fetch records count.
		long totalRecords = query.count();
		List<ReferenceRecordDTO> listData = null;

		if (totalRecords > 0L) {
			// Set pagination details
			query.limit(pageInfo.getLimit()).offset(pageInfo.getOffset()).orderBy(refFlow.referenceFlowId.asc());

			// Fetch record details
			listData = query.list(ConstructorExpression.create(ReferenceRecordDTO.class, refFlow.referenceFlowId,
					refBase.convertedForeignDocumentNumber, refBase.kindCode, refBase.publicationDate,
					refBase.applicantName, refBase.referenceComments, refBase.updatedByUser));
		}

		long recordsFiltered = pageInfo.getLimit() + pageInfo.getOffset();
		return new SearchResult<>(totalRecords, (recordsFiltered > totalRecords ? totalRecords : recordsFiltered),
				listData == null ? Collections.emptyList() : listData);
	}

	@Override
	public SearchResult<ReferenceRecordDTO> fetchNPLData(ReferenceRecordsFilter filter, PaginationInfo pageInfo) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QReferenceBaseNPLData refBase = QReferenceBaseNPLData.referenceBaseNPLData;
		JPAQuery query = new JPAQuery(entityManager).from(refFlow, refBase);

		// Set access predicate
		BooleanBuilder predicate = referenceFetchCondition(refFlow, filter);

		query.where(refFlow.referenceBaseData.referenceBaseDataId.eq(refBase.referenceBaseDataId)
				.and(refBase.referenceType.eq(filter.getReferenceType())).and(refBase.referenceFlowFlag.eq(true))
				.and(refBase.active.eq(true)).and(predicate));

		// Fetch records count.
		long totalRecords = query.count();
		List<ReferenceRecordDTO> listData = null;

		if (totalRecords > 0L) {
			// Set pagination details
			query.limit(pageInfo.getLimit()).offset(pageInfo.getOffset()).orderBy(refFlow.referenceFlowId.asc());

			// Fetch record details
			listData = query.list(ConstructorExpression.create(ReferenceRecordDTO.class, refFlow.referenceFlowId,
					refBase.stringData, refBase.updatedByUser));
		}

		long recordsFiltered = pageInfo.getLimit() + pageInfo.getOffset();
		return new SearchResult<>(totalRecords, (recordsFiltered > totalRecords ? totalRecords : recordsFiltered),
				listData == null ? Collections.emptyList() : listData);
	}

	@Override
	public IDSFeeCalculationDetails fetchIDSFilingFeeParams(final long idsID) {
		final QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		final QApplicationBase application = QApplicationBase.applicationBase;

		final Expression<Calendar> firstReferenceCreatedOn = new JPASubQuery().from(QIDSReferenceFlow.iDSReferenceFlow)
				.where(QIDSReferenceFlow.iDSReferenceFlow.ids.id.eq(idsID))
				.unique(QIDSReferenceFlow.iDSReferenceFlow.createdDate.min());

		return new JPAQuery(entityManager).from(refFlow).innerJoin(refFlow.targetApplication, application)
				.where(refFlow.ids.id.eq(idsID)).distinct()
				.uniqueResult(new QIDSFeeCalculationDetails(refFlow.ids.id, application.appDetails.filingDate,
						application.organizationDetails.entity, application.organizationDetails.idsRelevantStatus,
						firstReferenceCreatedOn));
	}

	private BooleanBuilder referenceFetchCondition(QIDSReferenceFlow refFlow, ReferenceRecordsFilter filter) {
		return new BooleanBuilder().and(refFlow.ids.id.eq(filter.getIds())).and(refFlow.doNotFile.eq(false));
	}

	@Override
	public Map<ReferenceType, Long> countReferenceRecords(ReferenceRecordsFilter filter) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QReferenceBaseData refBase = QReferenceBaseData.referenceBaseData;

		JPAQuery query = new JPAQuery(entityManager).from(refFlow, refBase);
		// Set access predicate.
		BooleanBuilder predicate = referenceFetchCondition(refFlow, filter);

		query.where(refFlow.referenceBaseData.referenceBaseDataId.eq(refBase.referenceBaseDataId)
				.and(refBase.referenceFlowFlag.eq(true)).and(refBase.active.eq(true)).and(predicate));

		// Fetch SourceRef Count
		Map<ReferenceType, Long> refCount = query.groupBy(refBase.referenceType).map(refBase.referenceType,
				refBase.referenceBaseDataId.count());

		// sourece refCount
		QIDSSourceReferenceFlow sourceRefFlow = QIDSSourceReferenceFlow.iDSSourceReferenceFlow;
		QSourceReference sourceRef = QSourceReference.sourceReference;
		QIDSSourceReferenceFlowFilingInfo sourceRefFillingInfo = QIDSSourceReferenceFlowFilingInfo.iDSSourceReferenceFlowFilingInfo;

		JPAQuery querySource = new JPAQuery(entityManager).from(sourceRefFillingInfo)
				.innerJoin(sourceRefFillingInfo.sourceReferenceFlow, sourceRefFlow)
				.innerJoin(sourceRefFlow.sourceReference, sourceRef);

		QIDS idsO = QIDS.iDS;
		JPAQuery queryIDS = new JPAQuery(entityManager).from(idsO).where(idsO.id.eq(filter.getIds())).distinct();
		Long idsTempID = queryIDS.singleResult(idsO.id);
		querySource.where(sourceRefFlow.targetApplication.id.eq(filter.getTargetApplication())
				.and(sourceRefFlow.sourceReferenceFlowStatus.eq(SourceReferenceFlowStatus.ACTIVE))
				.and(sourceRefFillingInfo.sourceReferenceFlowSubStatus
						.eq(SourceReferenceFlowSubStatus.PENDING_USPTO_FILING))
				.and(sourceRefFillingInfo.tempIDS.id.eq(idsTempID))).distinct();

		// Fetch records count.
		long sourceCount = querySource.count();
		if (refCount.containsKey(ReferenceType.NPL)) {
			refCount.replace(ReferenceType.NPL, refCount.get(ReferenceType.NPL) + sourceCount);
		} else {
			refCount.put(ReferenceType.NPL, sourceCount);
		}
		return refCount;
		// Fetch records count.
		// return query.groupBy(refBase.referenceType).map(refBase.referenceType, refBase.referenceBaseDataId.count());
	}

	@Override
	public long doNotFileAction(Long refFlowId) {
		LOGGER.info(String.format("Don't File Action repository  for reference flow {0}", refFlowId));
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;

		// Dropping primary couple will also lead secondary couple.
		Set<Long> refFlowsToUpdate = refFlowListToUpdate(refFlow, refFlowId);
		refFlowsToUpdate.add(refFlowId);
		LOGGER.info(String.format("Updating don't file flag for {0}.", refFlowsToUpdate));

		return new JPAUpdateClause(entityManager, refFlow).set(refFlow.doNotFile, true).setNull(refFlow.ids)
				.set(refFlow.updatedByUser, BlackboxSecurityContextHolder.getUserId())
				.set(refFlow.updatedDate, Calendar.getInstance()).where(refFlow.referenceFlowId.in(refFlowsToUpdate))
				.execute();
	}

	@Override
	public long dropRefFlowFromIDS(Long refFlowId) {
		LOGGER.info(String.format("[DROP REFERENCE]: Dropping Reference Flow {0}.", refFlowId));

		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		// Dropping primary couple will also lead secondary couple.
		Set<Long> refFlowsToUpdate = refFlowListToUpdate(refFlow, refFlowId);
		refFlowsToUpdate.add(refFlowId);

		LOGGER.info(String.format("Updating don't file flag for {0}.", refFlowsToUpdate));
		return new JPAUpdateClause(entityManager, refFlow).setNull(refFlow.ids)
				.set(refFlow.updatedByUser, BlackboxSecurityContextHolder.getUserId())
				.set(refFlow.updatedDate, Calendar.getInstance()).where(refFlow.referenceFlowId.in(refFlowsToUpdate))
				.execute();
	}

	private Set<Long> refFlowListToUpdate(QIDSReferenceFlow refFlow, Long refFlowId) {
		QReferenceBaseData refBase = QReferenceBaseData.referenceBaseData;
		QIDSReferenceFlow refFlow2 = new QIDSReferenceFlow("refFlow2");
		QReferenceBaseData refBase2 = new QReferenceBaseData("refBase2");

		return new HashSet<>(new JPAQuery(entityManager).from(refFlow).innerJoin(refFlow.referenceBaseData, refBase)
				.where(refBase.primaryCouple.eq(false)
						.and(refBase.couplingId.eq(new JPASubQuery().from(refFlow2)
								.innerJoin(refFlow2.referenceBaseData, refBase2)
								.where(refFlow2.referenceFlowId.eq(refFlowId)).distinct().unique(refBase2.couplingId)))
						.and(refBase.referenceType
								.eq(new JPASubQuery().from(refFlow2).innerJoin(refFlow2.referenceBaseData, refBase2)
										.where(refFlow2.referenceFlowId.eq(refFlowId)).distinct()
										.unique(refBase2.referenceType))))
				.list(refFlow.referenceFlowId));

	}

	@Override
	public boolean dropReferenceFlow(final ReferenceBaseData referenceBaseData, final Long targetApplicationId,
			final ReferenceFlowStatus status) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		JPAUpdateClause updateQuery = new JPAUpdateClause(entityManager, refFlow);
		return updateQuery.set(refFlow.referenceFlowStatus, ReferenceFlowStatus.DROPPED)
				.set(refFlow.updatedByUser, BlackboxSecurityContextHolder.getUserId())
				.set(refFlow.updatedDate, Calendar.getInstance())
				.where(refFlow.referenceBaseData.publicationNumber.eq(referenceBaseData.getPublicationNumber())
						.and(refFlow.referenceFlowStatus.eq(status))
						.and(refFlow.targetApplication.id.eq(targetApplicationId)))
				.execute() > 0;
	}

	@Override
	public boolean dropReferenceFlowWithMultiplleStatus(final ReferenceBaseData referenceBaseData,
			final Long targetApplicationId, final List<ReferenceFlowStatus> statusList) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		JPAUpdateClause updateQuery = new JPAUpdateClause(entityManager, refFlow);
		return updateQuery.set(refFlow.referenceFlowStatus, ReferenceFlowStatus.DROPPED)
				.set(refFlow.updatedByUser, BlackboxSecurityContextHolder.getUserId())
				.set(refFlow.updatedDate, Calendar.getInstance())
				.where(refFlow.referenceBaseData.publicationNumber.eq(referenceBaseData.getPublicationNumber())
						.and(refFlow.referenceFlowStatus.in(statusList))
						.and(refFlow.targetApplication.id.eq(targetApplicationId)))
				.execute() > 0;
	}

	@Override
	public boolean duplicateCheck(ReferenceBaseData referenceBaseData, Long applicationId) {

		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		List<ReferenceFlowStatus> statusList = Collections.emptyList();
		statusList.add(ReferenceFlowStatus.DROPPED);
		// statusList.add(ReferenceFlowStatus.CITED);
		JPAQuery query = new JPAQuery(entityManager).from(refFlow);
		query.from(refFlow)
				.where(refFlow.referenceBaseData.publicationNumber.eq(referenceBaseData.getPublicationNumber())
						.and(refFlow.targetApplication.id.eq(applicationId))
						.and(refFlow.referenceFlowStatus.notIn(statusList))
						.and(refFlow.referenceFlowSubStatus.ne(ReferenceFlowSubStatus.REJECTED)));

		return query.exists();
	}

	@Override
	public List<IDSReferenceFlow> findDuplicateRefFlow(ReferenceBaseData referenceBaseData, Long applicationId) {

		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		List<ReferenceFlowStatus> statusList = Collections.emptyList();
		statusList.add(ReferenceFlowStatus.DROPPED);
		// statusList.add(ReferenceFlowStatus.CITED);
		JPAQuery query = new JPAQuery(entityManager).from(refFlow);
		query.from(refFlow)
				.where(refFlow.referenceBaseData.publicationNumber.eq(referenceBaseData.getPublicationNumber())
						.and(refFlow.targetApplication.id.eq(applicationId))
						.and(refFlow.referenceFlowStatus.notIn(statusList))
						.and(refFlow.referenceFlowSubStatus.ne(ReferenceFlowSubStatus.REJECTED)));

		return query.list(refFlow);
	}

	@Override
	public List<Long> findNewUncitedRef(final long appId) {

		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		return new JPAQuery(entityManager).from(referenceFlow)
				.where(referenceFlow.targetApplication.id.eq(appId)
						.and(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.UNCITED))
						.and(referenceFlow.referenceFlowSubStatus.eq(ReferenceFlowSubStatus.CITED_IN_PARENT))
						.and(referenceFlow.ids.id.isNull()))
				.list(referenceFlow.referenceFlowId);

	}

	@Override
	public long includeRefInCurrentIDS(long iDSId, List<Long> refFlowsIdsList) {

		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, referenceFlow);
		updateClause.where(referenceFlow.referenceFlowId.in(refFlowsIdsList)).set(referenceFlow.ids.id, iDSId);
		long rowUpdate = updateClause.execute();
		return rowUpdate;
	}

	@Override
	public long updateReferenceStatus(UpdateRefStatusDTO refStatusDTO) {
		// TODO Auto-generated method stub
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		Long rowUpdated = 0L;

		/*
		 * If the user clicks on Cited in IDS & enters the IDS filing date – update the status of reference in flow
		 * table to Cited in IDS / 1449 Pending & populate the IDS filing date.
		 */
		for (CitedReferenceDTO citedRefs : refStatusDTO.getCitedRef()) {
			JPAUpdateClause updateCited = new JPAUpdateClause(entityManager, referenceFlow)
					.set(referenceFlow.referenceFlowStatus, ReferenceFlowStatus.CITED)
					.set(referenceFlow.referenceFlowSubStatus, ReferenceFlowSubStatus.PENDING_1449)
					.set(referenceFlow.filingDate, citedRefs.getFilingDate())
					.where(referenceFlow.referenceFlowId.eq(citedRefs.getRefFlowId()));
			rowUpdated += updateCited.execute();

		}
		/*
		 * If the user clicks on Not Cited – reference status to be changed to previous status & release temp IDS ID.
		 */
		JPAUpdateClause updateUncited = new JPAUpdateClause(entityManager, referenceFlow)
				.set(referenceFlow.referenceFlowStatus, ReferenceFlowStatus.UNCITED)
				.where(referenceFlow.referenceFlowId.in(refStatusDTO.getNotCitedRef()));
		rowUpdated = +updateUncited.execute();

		return rowUpdated;
	}

	@Override
	public SearchResult<ReferenceRecordDTO> fetchSourceRefData(ReferenceRecordsFilter filter, PaginationInfo pageInfo) {
		QIDSSourceReferenceFlow sourceRefFlow = QIDSSourceReferenceFlow.iDSSourceReferenceFlow;
		QSourceReference sourceRef = QSourceReference.sourceReference;

		JPAQuery query = new JPAQuery(entityManager).from(sourceRefFlow).innerJoin(sourceRefFlow.sourceReference,
				sourceRef);
		query.where(sourceRefFlow.targetApplication.id.eq(filter.getTargetApplication())
				.and(sourceRefFlow.sourceReferenceFlowStatus.eq(SourceReferenceFlowStatus.ACTIVE)));
		/*-		BooleanBuilder predicate = referenceFetchCondition(refFlow, filter);
		
				query.where(refFlow.referenceBaseDataId.referenceBaseDataId.eq(refBase.referenceBaseDataId)
						.and(refBase.referenceType.eq(filter.getReferenceType())).and(refBase.referenceFlowFlag.eq(true))
						.and(refBase.active.eq(true)).and(predicate));
		 */
		// Fetch records count.
		long totalRecords = query.count();
		List<ReferenceRecordDTO> listData = null;

		if (totalRecords > 0L) {
			// Set pagination details
			// query.limit(pageInfo.getLimit()).offset(pageInfo.getOffset()).orderBy(refFlow.referenceFlowId.asc());

			// Fetch record details
			listData = query.list(ConstructorExpression.create(ReferenceRecordDTO.class,
					sourceRefFlow.sourceReferenceFlowId, sourceRef.nplString, sourceRef.updatedByUser));
		}

		long recordsFiltered = pageInfo.getLimit() + pageInfo.getOffset();
		return new SearchResult<>(totalRecords, ((recordsFiltered > totalRecords ? totalRecords : recordsFiltered)),
				listData == null ? Collections.emptyList() : listData);
	}

	@Override
	public int insertSourceRefFillingRecords(Long[] selectedSourceRefId, long ids) {
		List<IDSSourceReferenceFlowFilingInfo> listFilingInfos = new ArrayList<>();
		IDSSourceReferenceFlowFilingInfo refFlowFilling = null;
		IDSSourceReferenceFlow sourceReferenceFlow = null;
		/* IDS Build ID */
		QIDS idsO = QIDS.iDS;

		JPAQuery query = new JPAQuery(entityManager).from(idsO).where(idsO.id.eq(ids));
		Long tempId = query.singleResult(idsO.id);
		/**/

		/* Select query for contain list */
		QIDSSourceReferenceFlowFilingInfo idsSourceFlowFilinginfo = QIDSSourceReferenceFlowFilingInfo.iDSSourceReferenceFlowFilingInfo;

		JPAQuery queryCitedID = new JPAQuery(entityManager).from(idsSourceFlowFilinginfo);
		Long citedID = queryCitedID.count();
		queryCitedID.where(idsSourceFlowFilinginfo.sourceReferenceFlow.sourceReferenceFlowId.in(selectedSourceRefId)
				.and(idsSourceFlowFilinginfo.tempIDS.id.eq(tempId)));
		List<Long> containlist = queryCitedID.list(idsSourceFlowFilinginfo.sourceReferenceFlow.sourceReferenceFlowId);

		/**/
		/* Delete */
		QIDSSourceReferenceFlowFilingInfo idsSourceFlowFilingInfoDel = QIDSSourceReferenceFlowFilingInfo.iDSSourceReferenceFlowFilingInfo;
		JPADeleteClause queryDel = new JPADeleteClause(entityManager, idsSourceFlowFilingInfoDel);
		queryDel.where(idsSourceFlowFilingInfoDel.sourceReferenceFlow.sourceReferenceFlowId.notIn(selectedSourceRefId)
				.and(idsSourceFlowFilingInfoDel.tempIDS.id.eq(tempId)));
		queryDel.execute();
		/**/

		for (long element : selectedSourceRefId) {
			citedID++;
			if (!(containlist.contains(element))) {
				refFlowFilling = new IDSSourceReferenceFlowFilingInfo();
				sourceReferenceFlow = new IDSSourceReferenceFlow(element);
				// sourceReferenceFlow = new IDSSourceReferenceFlow();
				refFlowFilling.setSourceReferenceFlowStatus(SourceReferenceFlowStatus.ACTIVE);
				refFlowFilling.setSourceReferenceFlowSubStatus(SourceReferenceFlowSubStatus.PENDING_USPTO_FILING);
				refFlowFilling.setCiteId(citedID.toString());
				IDS tempIDS = new IDS(tempId);
				refFlowFilling.setTempIDS(tempIDS);
				refFlowFilling.setSourceReferenceFlow(sourceReferenceFlow);
				listFilingInfos.add(refFlowFilling);
			}
		}

		List<IDSSourceReferenceFlowFilingInfo> retList = idsSourceReferenceFlowFilingInfoRepository
				.save(listFilingInfos);
		return retList.size();
	}

	@Override
	public SearchResult<ReferenceRecordDTO> fetchSourceFilingRefData(ReferenceRecordsFilter filter,
			PaginationInfo pageInfo) {
		QIDSSourceReferenceFlow sourceRefFlow = QIDSSourceReferenceFlow.iDSSourceReferenceFlow;
		QSourceReference sourceRef = QSourceReference.sourceReference;
		QIDSSourceReferenceFlowFilingInfo sourceRefFillingInfo = QIDSSourceReferenceFlowFilingInfo.iDSSourceReferenceFlowFilingInfo;

		JPAQuery querySource = new JPAQuery(entityManager).from(sourceRefFillingInfo)
				.innerJoin(sourceRefFillingInfo.sourceReferenceFlow, sourceRefFlow)
				.innerJoin(sourceRefFlow.sourceReference, sourceRef);

		QIDS idsO = QIDS.iDS;
		JPAQuery queryIDS = new JPAQuery(entityManager).from(idsO).where(idsO.id.eq(filter.getIds())).distinct();
		Long idsTempID = queryIDS.singleResult(idsO.id);
		querySource.where(sourceRefFlow.targetApplication.id.eq(filter.getTargetApplication())
				.and(sourceRefFlow.sourceReferenceFlowStatus.eq(SourceReferenceFlowStatus.ACTIVE))
				.and(sourceRefFillingInfo.sourceReferenceFlowSubStatus
						.eq(SourceReferenceFlowSubStatus.PENDING_USPTO_FILING))
				.and(sourceRefFillingInfo.tempIDS.id.eq(idsTempID))).distinct();

		/*
		 * query.where(sourceRefFlow.targetApplication.id.eq(filter.getTargetApplication())
		 * .and(sourceRefFlow.sourceReferenceFlowStatus.eq(SourceReferenceFlowStatus.ACTIVE)));
		 */ // Fetch records count.
		long totalRecords = querySource.count();
		List<ReferenceRecordDTO> listData = null;

		if (totalRecords > 0L) {
			// Set pagination details
			// query.limit(pageInfo.getLimit()).offset(pageInfo.getOffset()).orderBy(refFlow.referenceFlowId.asc());

			// Fetch record details
			listData = querySource.list(ConstructorExpression.create(ReferenceRecordDTO.class,
					sourceRefFlow.sourceReferenceFlowId, sourceRef.nplString, sourceRef.updatedByUser));
		}

		long recordsFiltered = pageInfo.getLimit() + pageInfo.getOffset();
		return new SearchResult<>(totalRecords, ((recordsFiltered > totalRecords ? totalRecords : recordsFiltered)),
				listData == null ? Collections.emptyList() : listData);
	}

	@Override
	public Map<ReferenceType, Long> countNewReferenceRecords(ReferenceRecordsFilter refFilter) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QReferenceBaseData refBase = QReferenceBaseData.referenceBaseData;

		JPAQuery query = new JPAQuery(entityManager).from(refFlow, refBase);

		BooleanBuilder predicate = referenceFetchCondition(refFlow, refFilter);
		query.where(refFlow.referenceBaseData.referenceBaseDataId.eq(refBase.referenceBaseDataId)
				.and(refBase.referenceFlowFlag.eq(true)).and(refBase.active.eq(true)).and(predicate)
				.and(refFlow.notifyAttorney.eq(true)));

		// Fetch records count.
		return query.groupBy(refBase.referenceType).map(refBase.referenceType, refBase.referenceBaseDataId.count());

	}

	@Override
	public Long getNewReferencesCount(Long appId) {
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		JPAQuery query = new JPAQuery(entityManager).from(referenceFlow).where(referenceFlow.targetApplication.id
				.eq(appId).and(referenceFlow.notifyAttorney.eq(true)).and(referenceFlow.ids.isNull()));

		return query.count();
	}

	@Override
	public void updateNotifyAttorneyFlag(String appId) {

		Long appid = Long.valueOf(appId);
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		JPAUpdateClause query = new JPAUpdateClause(entityManager, referenceFlow)
				.set(referenceFlow.notifyAttorney, false)
				.where(referenceFlow.targetApplication.id.eq(appid).and(referenceFlow.ids.isNull()));
		query.execute();
	}

	@Override
	public void updateNewReferences(Long applicationId, Long iDSId) {

		QIDS qids = QIDS.iDS;
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		JPAQuery query = new JPAQuery(entityManager).from(qids).where(qids.id.eq(iDSId));
		IDS ids = (IDS) query.uniqueResult(qids);
		JPAUpdateClause query1 = new JPAUpdateClause(entityManager, referenceFlow).set(referenceFlow.ids, ids)
				.where(referenceFlow.targetApplication.id.eq(applicationId).and(referenceFlow.notifyAttorney.eq(true))
						.and(referenceFlow.ids.isNull()));
		query1.execute();
	}

	@Override
	public boolean checkForCitedInParentEligibility(ReferenceBaseData referenceBaseData,
			ApplicationBase targetAppBase) {
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		BooleanBuilder condition = new BooleanBuilder();

		condition.and(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.EXAMINER_CITED)
				.and(referenceFlow.referenceFlowSubStatus.eq(ReferenceFlowSubStatus.NULL)
						.and((referenceFlow.referenceBaseData.correspondence.isNotNull()
								.and(referenceFlow.referenceBaseData.correspondence.mailingDate
										.lt(targetAppBase.getAppDetails().getFilingDate()))
								.or(referenceFlow.referenceBaseData.selfCitationDate
										.lt(targetAppBase.getAppDetails().getFilingDate()))))));

		condition.or(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.CITED)
				.and(referenceFlow.referenceFlowSubStatus.eq(ReferenceFlowSubStatus.ACCEPTED)
						.and(referenceFlow.filingDate.lt(targetAppBase.getAppDetails().getFilingDate()))));

		condition.or(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.UNCITED)
				.and(referenceFlow.referenceFlowSubStatus.eq(ReferenceFlowSubStatus.CITED_IN_PARENT)
						.and((referenceFlow.referenceBaseData.correspondence.isNotNull()
								.and(referenceFlow.referenceBaseData.correspondence.mailingDate
										.lt(targetAppBase.getAppDetails().getFilingDate()))
								.or(referenceFlow.referenceBaseData.selfCitationDate
										.lt(targetAppBase.getAppDetails().getFilingDate()))))));

		JPAQuery query = new JPAQuery(entityManager);
		query.from(referenceFlow).where(referenceFlow.referenceBaseData.eq(referenceBaseData)
				.and(referenceFlow.targetApplication.eq(targetAppBase).and(condition)));

		return query.exists();
	}

	@Override
	public List<IDSReferenceFlow> findBySourceFamilyIdAndReferenceFlowStatus(String sourceFamily,
			ReferenceFlowStatus status) {

		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		JPAQuery query = new JPAQuery(entityManager).from(referenceFlow).where(referenceFlow.sourceApplication.familyId
				.eq(sourceFamily).and(referenceFlow.referenceFlowStatus.ne(status)));
		return query.list(referenceFlow);
	}

	@Override
	public boolean dropReferenceFlow(Long refFlowId) {
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		JPAUpdateClause updateQuery = new JPAUpdateClause(entityManager, refFlow);
		return updateQuery.set(refFlow.referenceFlowStatus, ReferenceFlowStatus.DROPPED)
				.set(refFlow.updatedByUser, BlackboxSecurityContextHolder.getUserId())
				.set(refFlow.updatedDate, Calendar.getInstance()).where(refFlow.referenceFlowId.eq(refFlowId))
				.execute() > 0;
	}

}