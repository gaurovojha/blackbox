
package com.blackbox.ids.core.repository.impl.notification.process;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.Querydsl;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.auth.UserAuthDetail;
import com.blackbox.ids.core.dto.IDS.notification.N1449PendingNotificationDTO;
import com.blackbox.ids.core.dto.IDS.notification.NPLDuplicateCheckNotificationDTO;
import com.blackbox.ids.core.dto.IDS.notification.NotificationBaseDTO;
import com.blackbox.ids.core.dto.IDS.notification.ReferenceEntryNotificationDTO;
import com.blackbox.ids.core.dto.IDS.notification.StatusChangeNotificationDTO;
import com.blackbox.ids.core.dto.IDS.notification.UpdateFamilyNotificationDTO;
import com.blackbox.ids.core.dto.IDS.notification.UpdateReferenceNotificationDTO;
import com.blackbox.ids.core.dto.IDS.notification.UploadDocumentNotificationDTO;
import com.blackbox.ids.core.dto.IDS.notification.UploadIDSNotificationDTO;
import com.blackbox.ids.core.dto.IDS.notification.ValidateRefStatusNotificationDTO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.reference.ReferenceDashboardDto;
import com.blackbox.ids.core.dto.reference.ReferencePage;
import com.blackbox.ids.core.model.QDocumentCode;
import com.blackbox.ids.core.model.QPerson;
import com.blackbox.ids.core.model.QRole;
import com.blackbox.ids.core.model.QUser;
import com.blackbox.ids.core.model.IDS.IDSFilingInfo.IDS_Source;
import com.blackbox.ids.core.model.IDS.QIDS;
import com.blackbox.ids.core.model.IDS.QIDSFilingInfo;
import com.blackbox.ids.core.model.correspondence.QCorrespondenceBase;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.QApplicationBase;
import com.blackbox.ids.core.model.mstr.QJurisdiction;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcess;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.model.notification.process.QNotificationProcess;
import com.blackbox.ids.core.model.reference.QOcrData;
import com.blackbox.ids.core.model.reference.QReferenceBaseData;
import com.blackbox.ids.core.model.reference.QReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.QReferenceStagingData;
import com.blackbox.ids.core.model.reference.ReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.ReferenceStagingData;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.referenceflow.QIDSReferenceFlow;
import com.blackbox.ids.core.model.webcrawler.QDownloadOfficeActionQueue;
import com.blackbox.ids.core.model.webcrawler.QFindFamilyQueue;
import com.blackbox.ids.core.repository.notification.process.NotificationProcessCustomRepository;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.Tuple;
import com.mysema.query.group.GroupBy;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.path.PathBuilder;

/**
 * The Class NotificationProcessRepositoryImpl.
 */
public class NotificationProcessRepositoryImpl implements NotificationProcessCustomRepository {

	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.notification.process.
	 * NotificationProcessCustomRepository#getUpdateReferenceDtos(
	 * com.mysema.query.types.Predicate,
	 * org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<ReferenceDashboardDto> getUpdateReferenceDtos(Predicate predicate, Pageable pageable) {
		QNotificationProcess np = QNotificationProcess.notificationProcess;
		QReferenceStagingData rsData = QReferenceStagingData.referenceStagingData;
		QUser user = QUser.user;
		QPerson person = QPerson.person;
		QCorrespondenceBase corres = QCorrespondenceBase.correspondenceBase;
		QDocumentCode docCode = QDocumentCode.documentCode;

		JPAQuery query = getUpdateReferenceQuery();

		long countWithoutFilter = query.count();
		long count = query.where(predicate).count();

		Querydsl queryDsl = null;

		Sort sort = pageable.getSort();

		if (sort != null && sort.getOrderFor("notifiedDate") != null) {
			queryDsl = new Querydsl(entityManager,
					new PathBuilder<NotificationProcess>(NotificationProcess.class, "notificationProcess"));

		} else {
			queryDsl = new Querydsl(entityManager,
					new PathBuilder<ReferenceStagingData>(ReferenceStagingData.class, "referenceStagingData"));
		}

		queryDsl.applyPagination(pageable, query);

		List<ReferenceDashboardDto> refDtos = query.list(ConstructorExpression.create(ReferenceDashboardDto.class,
				rsData.referenceStagingDataId, rsData.jurisdiction.code, rsData.publicationNumber,
				rsData.application.applicationNumber, corres.id, np.notifiedDate, user.id, person.firstName,
				person.lastName, np.notificationProcessId, rsData.application.jurisdiction.code, docCode.description,
				rsData.application.appDetails.filingDate, rsData.application.patentDetails.issuedOn));

		return new ReferencePage<ReferenceDashboardDto>(refDtos, pageable, count, countWithoutFilter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.notification.process.
	 * NotificationProcessCustomRepository#
	 * getDuplicateNplReferenceDtos(com.mysema.query.types.Predicate,
	 * org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<ReferenceDashboardDto> getDuplicateNplReferenceDtos(Predicate predicate, Pageable pageable) {
		QNotificationProcess np = QNotificationProcess.notificationProcess;
		QReferenceBaseNPLData rbData = QReferenceBaseNPLData.referenceBaseNPLData;
		QUser user = QUser.user;
		QPerson person = QPerson.person;

		JPAQuery query = getDuplicateNplReferenceQuery();

		long countWithoutFilter = query.count();
		long count = query.where(predicate).count();

		Querydsl queryDsl = null;

		if ("notifiedDate".equalsIgnoreCase(pageable.getSort().iterator().next().getProperty())) {
			queryDsl = new Querydsl(entityManager,
					new PathBuilder<NotificationProcess>(NotificationProcess.class, "notificationProcess"));

		} else {
			queryDsl = new Querydsl(entityManager,
					new PathBuilder<ReferenceBaseNPLData>(ReferenceBaseNPLData.class, "referenceBaseNPLData"));
		}

		queryDsl.applyPagination(pageable, query);

		List<ReferenceDashboardDto> refDtos = query
				.list(ConstructorExpression.create(ReferenceDashboardDto.class, rbData.application.jurisdiction.code,
						rbData.publicationNumber, rbData.application.applicationNumber, rbData.correspondence.id,
						rbData.stringData, np.updatedDate, rbData.referenceBaseDataId, user.id, person.firstName,
						person.lastName, np.notificationProcessId, rbData.correspondence.documentCode.description));

		return new ReferencePage<ReferenceDashboardDto>(refDtos, pageable, count, countWithoutFilter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.notification.process.
	 * NotificationProcessCustomRepository# getUpdateReferenceCounts()
	 */
	@Override
	public long getUpdateReferenceCounts() {
		return getUpdateReferenceQuery().count();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.notification.process.
	 * NotificationProcessCustomRepository# getDuplicateNplReferenceCounts()
	 */
	@Override
	public long getDuplicateNplReferenceCounts() {
		return getDuplicateNplReferenceQuery().count();
	}

	/**
	 * Gets the update reference query.
	 * 
	 * @return the update reference query
	 */
	private JPAQuery getUpdateReferenceQuery() {
		QNotificationProcess np = QNotificationProcess.notificationProcess;
		QReferenceStagingData rsData = QReferenceStagingData.referenceStagingData;
		QRole role = QRole.role;
		QJurisdiction juris = QJurisdiction.jurisdiction;
		QApplicationBase application = QApplicationBase.applicationBase;
		QUser user = QUser.user;
		QPerson person = QPerson.person;
		QCorrespondenceBase corres = QCorrespondenceBase.correspondenceBase;
		QDocumentCode docCode = QDocumentCode.documentCode;

		Set<Long> roleIds = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();
		JPAQuery query = new JPAQuery(entityManager);
		query.from(np, rsData).join(rsData.jurisdiction, juris).leftJoin(rsData.correspondence, corres)
				.leftJoin(corres.documentCode, docCode).join(rsData.application, application).join(np.roles, role)
				.leftJoin(np.lockedByUser, user).leftJoin(user.person, person);
		query.where(np.entityId.eq(rsData.referenceStagingDataId)
				.and(np.notificationProcessType.eq(NotificationProcessType.INPADOC_FAILED)).and(role.id.in(roleIds))
				.and(np.active.eq(true)).and(rsData.referenceStatus.eq(ReferenceStatus.INPADOC_FAILED))
				.and(application.recordStatus.ne(MDMRecordStatus.DROPPED)));
		query.where(getDataAccessPredicate());

		return query.distinct();
	}

	/**
	 * Gets the duplicate npl reference query.
	 * 
	 * @return the duplicate npl reference query
	 */
	private JPAQuery getDuplicateNplReferenceQuery() {
		QNotificationProcess np = QNotificationProcess.notificationProcess;
		QReferenceBaseNPLData rbData = QReferenceBaseNPLData.referenceBaseNPLData;
		QRole role = QRole.role;
		QJurisdiction juris = QJurisdiction.jurisdiction;
		QApplicationBase application = QApplicationBase.applicationBase;
		QUser user = QUser.user;
		QPerson person = QPerson.person;

		Set<Long> roleIds = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();
		JPAQuery query = new JPAQuery(entityManager);
		query.from(np, rbData).join(rbData.jurisdiction, juris).join(rbData.application, application)
				.join(np.roles, role).leftJoin(np.lockedByUser, user).leftJoin(user.person, person);
		query.where(np.entityId.eq(rbData.referenceBaseDataId)
				.and(np.notificationProcessType.eq(NotificationProcessType.NPL_DUPLICATE_CHECK))
				.and(role.id.in(roleIds)).and(np.active.eq(true))
				.and(rbData.referenceStatus.eq(ReferenceStatus.PENDING))
				.and(rbData.referenceType.eq(ReferenceType.NPL)));
		query.where(getDataAccessPredicate());

		return query.distinct();
	}

	/**
	 * Gets the data access predicate.
	 * 
	 * @return the data access predicate
	 */
	private Predicate getDataAccessPredicate() {
		BooleanBuilder predicate = new BooleanBuilder();
		QApplicationBase application = QApplicationBase.applicationBase;
		UserAuthDetail authDetail = BlackboxSecurityContextHolder.getUserAuthData();

		predicate.and(application.jurisdiction.id.in(authDetail.getJurisdictionIds()));
		predicate.and(application.assignee.id.in(authDetail.getAssigneeIds()));
		predicate.and(application.organization.id.in(authDetail.getOrganizationsIds()));
		predicate.and(application.customer.id.in(authDetail.getCustomerIds()));
		predicate.and(application.technologyGroup.id.in(authDetail.getTechnologyGroupIds()));
		return predicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.notification.process.
	 * NotificationProcessCustomRepository#
	 * getCorrespondenceFromNotification(java.lang.Long, java.lang.Long)
	 */
	@Override
	public ReferenceDashboardDto getCorrespondenceFromNotification(Long notificationId, Long ocrDataId) {

		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QRole role = QRole.role;
		QOcrData ocrData = QOcrData.ocrData;

		Set<Long> roleIds = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();
		JPAQuery query = new JPAQuery(entityManager);
		query.from(notificationProcess, ocrData).innerJoin(notificationProcess.roles, role)
				.where(notificationProcess.entityId.eq(ocrData.ocrDataId)
						.and(notificationProcess.entityName.eq(EntityName.OCR_DATA)
								.and(notificationProcess.notificationProcessId.eq(notificationId)
										.and(ocrData.ocrDataId.eq(ocrDataId).and(role.id.in(roleIds))))));

		ReferenceDashboardDto referenceDashboardDto = query.singleResult(ConstructorExpression.create(
				ReferenceDashboardDto.class, ocrData.correspondenceId.id, notificationProcess.notificationProcessId));

		return referenceDashboardDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.notification.process.
	 * NotificationProcessCustomRepository#getUpdateReferenceDto(
	 * java.lang.Long)
	 */
	@Override
	public ReferenceDashboardDto getUpdateReferenceDto(Long notificationProcessId) {
		QNotificationProcess np = QNotificationProcess.notificationProcess;
		QReferenceStagingData rsData = QReferenceStagingData.referenceStagingData;
		QUser user = QUser.user;
		QPerson person = QPerson.person;
		QCorrespondenceBase corres = QCorrespondenceBase.correspondenceBase;
		QDocumentCode docCode = QDocumentCode.documentCode;

		JPAQuery query = getUpdateReferenceQuery();
		query.where(np.notificationProcessId.eq(notificationProcessId));

		return query.singleResult(ConstructorExpression.create(ReferenceDashboardDto.class,
				rsData.referenceStagingDataId, rsData.jurisdiction.code, rsData.publicationNumber,
				rsData.application.applicationNumber, corres.id, np.notifiedDate, user.id, person.firstName,
				person.lastName, np.notificationProcessId, rsData.application.jurisdiction.code, docCode.description,
				rsData.application.appDetails.filingDate, rsData.application.patentDetails.issuedOn));
	}

	@Override
	public SearchResult<NotificationBaseDTO> getPendingNotifications(final List<Long> appIdList,
			final PaginationInfo pageInfo, final List<NotificationProcessType> notificationTypeList,
			final List<Long> skipNotificationList) {

		List<NotificationBaseDTO> pendingNotificationsList = new ArrayList<>();
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;

		SearchResult<NotificationBaseDTO> pendingNotificationItems = getPendingNotificationsResult(appIdList,
				notificationProcess, pageInfo, notificationTypeList, skipNotificationList);

		List<NotificationBaseDTO> notificationProcessDTOList = pendingNotificationItems.getItems();

		for (NotificationBaseDTO notificationBaseDTO : notificationProcessDTOList) {
			pendingNotificationsList = getNotificationProcessData(notificationBaseDTO, pendingNotificationsList);
		}

		SearchResult<NotificationBaseDTO> pendingNotificationDetailsItems = new SearchResult<>(
				pendingNotificationItems.getRecordsTotal(), pendingNotificationsList);

		return pendingNotificationDetailsItems;
	}

	// Fetch notification and entity Map for a Application
	private SearchResult<NotificationBaseDTO> getPendingNotificationsResult(final List<Long> appIdList,
			final QNotificationProcess notificationProcess, final PaginationInfo pageInfo,
			final List<NotificationProcessType> notificationTypesList, final List<Long> skipNotificationList) {

		BooleanBuilder predicate = getPendingNotificationsPredicate(appIdList, notificationProcess,
				notificationTypesList, skipNotificationList);

		JPAQuery query = new JPAQuery(entityManager).from(notificationProcess);

		long totalRecords = query.where(predicate).count();

		List<NotificationBaseDTO> notificationProcessBaseList = query.offset(pageInfo.getOffset())
				.limit(pageInfo.getLimit())
				.list(ConstructorExpression.create(NotificationBaseDTO.class, notificationProcess.notificationProcessId,
						notificationProcess.entityId, notificationProcess.notificationProcessType,
						notificationProcess.notification.message, notificationProcess.notifiedDate,
						notificationProcess.entityName));

		SearchResult<NotificationBaseDTO> result = new SearchResult<>(totalRecords, notificationProcessBaseList);

		return result;
	}

	private BooleanBuilder getPendingNotificationsPredicate(final List<Long> appIdList,
			final QNotificationProcess notificationProcess, final List<NotificationProcessType> notificationTypesList,
			final List<Long> skipNotificationList) {
		BooleanBuilder predicate = null;
		predicate = new BooleanBuilder().and(notificationProcess.status.eq(NotificationStatus.PENDING))
				.and(notificationProcess.applicationId.in(appIdList))
				.and(notificationProcess.notification.idsReviewDisplay.eq(true))
				.and(notificationProcess.notificationProcessType.in(notificationTypesList))
				.and(notificationProcess.active.eq(true));
		if (CollectionUtils.isNotEmpty(skipNotificationList)) {
			predicate.and(notificationProcess.notificationProcessId.notIn(skipNotificationList));
		}
		return predicate;
	}

	private List<NotificationBaseDTO> getNotificationProcessData(final NotificationBaseDTO notificationProcessDTO,
			final List<NotificationBaseDTO> pendingNotifications) {

		switch (notificationProcessDTO.getNotificationName()) {
		case NPL_DUPLICATE_CHECK:
			List<NPLDuplicateCheckNotificationDTO> nplDuplicateCheckNotificationList = getNPLReferenceNotificationDTO(
					notificationProcessDTO);
			pendingNotifications.addAll(nplDuplicateCheckNotificationList);
			break;
		case REFERENCE_MANUAL_ENTRY:
			List<ReferenceEntryNotificationDTO> refEntryNotificationList = getManualEntryRefNotificationDTO(
					notificationProcessDTO);
			pendingNotifications.addAll(refEntryNotificationList);
			break;
		case INPADOC_FAILED:
			List<UpdateReferenceNotificationDTO> updateReferenceNotificationList = getUpdateReferenceNotificationDTO(
					notificationProcessDTO);
			pendingNotifications.addAll(updateReferenceNotificationList);
			break;
		case CORRESPONDENCE_RECORD_FAILED_IN_DOWNLOAD_QUEUE:
			List<UploadDocumentNotificationDTO> uploadDocumentNotificationList = getUploadDocumentNotificationDTO(
					notificationProcessDTO);
			pendingNotifications.addAll(uploadDocumentNotificationList);
			break;
		case UPDATE_FAMILY_LINKAGE:
			List<UpdateFamilyNotificationDTO> updateFamilyNotificationList = getupdateFamilyNotificationDTO(
					notificationProcessDTO);
			pendingNotifications.addAll(updateFamilyNotificationList);
			break;
		case DASHBOARD_ACTION_STATUS:
			List<StatusChangeNotificationDTO> statusChangeNotificationList = getstatusChangeNotificationDTO(
					notificationProcessDTO);
			pendingNotifications.addAll(statusChangeNotificationList);
			break;

		case VALIDATE_REFERENCE_STATUS:
			List<ValidateRefStatusNotificationDTO> validateRefNotificationList = getvalidateRefNotificationDTO(
					notificationProcessDTO);
			pendingNotifications.addAll(validateRefNotificationList);
			break;

		case UPLOAD_MANUALLY_FILED_IDS:
			List<UploadIDSNotificationDTO> uploadIDSNotificationList = getuploadIDSNotificationDTO(
					notificationProcessDTO);
			pendingNotifications.addAll(uploadIDSNotificationList);
			break;
		case N1449:
			List<N1449PendingNotificationDTO> n1449NotificationDTOsList = get1449NotificationDTO(
					notificationProcessDTO);
			pendingNotifications.addAll(n1449NotificationDTOsList);
			break;

		case CREATE_SML:
			break;

		default:
			break;
		}
		return pendingNotifications;
	}

	private List<N1449PendingNotificationDTO> get1449NotificationDTO(NotificationBaseDTO notificationProcessDTO) {

		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QIDSFilingInfo idsFilingInfo = QIDSFilingInfo.iDSFilingInfo;
		QApplicationBase application = QApplicationBase.applicationBase;

		JPAQuery query = new JPAQuery(entityManager).from(notificationProcess, refFlow, idsFilingInfo).distinct();

		BooleanBuilder predicate = new BooleanBuilder()
				.and(refFlow.targetApplication.id.eq(notificationProcessDTO.getEntityId()))
				.and(notificationProcess.notificationProcessId.eq(notificationProcessDTO.getNotificationId()))
				.and(refFlow.internalFinalIDSId.eq(idsFilingInfo.idsFinalId)
						.or(refFlow.externalFinalIDSId.eq(idsFilingInfo.idsFinalId)));

		Map<Long, Map<String, Calendar>> idsPending1449List = query.where(predicate)
				.transform(GroupBy.groupBy(refFlow.targetApplication.id)
						.as(GroupBy.map(idsFilingInfo.idsFinalId, idsFilingInfo.filingDate)));

		List<Tuple> values = new JPAQuery(entityManager).from(notificationProcess, application)
				.where(notificationProcess.notificationProcessId.eq(notificationProcessDTO.getNotificationId())
						.and(notificationProcess.applicationId.eq(application.id)))
				.list(new QTuple(notificationProcess.notificationProcessId, application.id, application.familyId,
						application.jurisdiction.code, application.applicationNumber,
						notificationProcess.notification.message, notificationProcess.status,
						notificationProcess.notificationProcessType, notificationProcess.notifiedDate));

		List<N1449PendingNotificationDTO> items = new ArrayList<N1449PendingNotificationDTO>();

		for (Tuple tuple : values) {
			items.add(new N1449PendingNotificationDTO(tuple.get(notificationProcess.notificationProcessId),
					tuple.get(application.id), tuple.get(application.familyId),
					tuple.get(application.jurisdiction.code), tuple.get(application.applicationNumber),
					tuple.get(notificationProcess.notification.message), tuple.get(notificationProcess.status),
					tuple.get(notificationProcess.notificationProcessType), tuple.get(notificationProcess.notifiedDate),
					tuple.get(application.id), idsPending1449List.get(tuple.get(application.id))));
		}

		for (N1449PendingNotificationDTO n1449PendingNotificationDTO : items) {
			List<String> rolesList = getRolesofNotification(n1449PendingNotificationDTO.getNotificationId());
			n1449PendingNotificationDTO.setRoles(rolesList);
		}

		return items;
	}

	private List<UploadIDSNotificationDTO> getuploadIDSNotificationDTO(
			final NotificationBaseDTO notificationProcessDTO) {

		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QIDS ids = QIDS.iDS;

		List<UploadIDSNotificationDTO> uploadIDSNotificationList = Collections.emptyList();

		JPAQuery query = new JPAQuery(entityManager).from(notificationProcess, ids);

		query.where(ids.id.eq(notificationProcessDTO.getEntityId())
				.and(notificationProcess.notificationProcessId.eq(notificationProcessDTO.getNotificationId())));

		uploadIDSNotificationList = query.list(ConstructorExpression.create(UploadIDSNotificationDTO.class,
				notificationProcess.notificationProcessId, ids.application.id, ids.application.jurisdiction.code,
				ids.application.applicationNumber, notificationProcess.notification.message, notificationProcess.status,
				notificationProcess.notificationProcessType, notificationProcess.notifiedDate, ids.id,
				ids.filingInstructedOn));

		for (UploadIDSNotificationDTO uploadIDSNotificationDTO : uploadIDSNotificationList) {
			List<String> rolesList = getRolesofNotification(uploadIDSNotificationDTO.getNotificationId());
			uploadIDSNotificationDTO.setRoles(rolesList);
		}

		return uploadIDSNotificationList;
	}

	private List<ValidateRefStatusNotificationDTO> getvalidateRefNotificationDTO(
			final NotificationBaseDTO notificationProcessDTO) {

		QIDSFilingInfo idsFilingInfo = QIDSFilingInfo.iDSFilingInfo;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QIDSReferenceFlow refFlow = new QIDSReferenceFlow("refFlow");
		QApplicationBase application = QApplicationBase.applicationBase;

		List<ValidateRefStatusNotificationDTO> validateRefNotificationList = Collections.emptyList();

		JPAQuery query = new JPAQuery(entityManager).from(notificationProcess, idsFilingInfo)
				.innerJoin(idsFilingInfo.ids.application, application);

		query.where(idsFilingInfo.id.eq(notificationProcessDTO.getEntityId())
				.and(notificationProcess.notificationProcessId.eq(notificationProcessDTO.getNotificationId())));

		validateRefNotificationList = query
				.list(ConstructorExpression
						.create(ValidateRefStatusNotificationDTO.class, notificationProcess.notificationProcessId,
								application.id, idsFilingInfo.ids.application.familyId, application.jurisdiction.code,
								idsFilingInfo.ids.application.applicationNumber,
								notificationProcess.notification.message, notificationProcess.status,
								notificationProcess.notificationProcessType, notificationProcess.notifiedDate,
								idsFilingInfo.id,
								new JPASubQuery().from(refFlow).where(refFlow.internalFinalIDSId
										.eq(idsFilingInfo.idsFinalId).and(idsFilingInfo.source.eq(IDS_Source.INTERNAL)))
						.count()));

		for (ValidateRefStatusNotificationDTO validateRefStatusNotificationDTO : validateRefNotificationList) {
			List<String> rolesList = getRolesofNotification(validateRefStatusNotificationDTO.getNotificationId());
			validateRefStatusNotificationDTO.setRoles(rolesList);
		}

		return validateRefNotificationList;

	}

	private List<StatusChangeNotificationDTO> getstatusChangeNotificationDTO(
			final NotificationBaseDTO notificationProcessDTO) {

		QApplicationBase application = QApplicationBase.applicationBase;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QUser user = QUser.user;

		List<StatusChangeNotificationDTO> statusChangeNotificationList = Collections.emptyList();

		JPAQuery query = new JPAQuery(entityManager).from(notificationProcess, application, user);

		query.where(
				application.id.eq(notificationProcessDTO.getEntityId()).and(notificationProcess.notificationProcessId
						.eq(notificationProcessDTO.getNotificationId()).and(application.updatedByUser.eq(user.id))));

		statusChangeNotificationList = query.list(ConstructorExpression.create(StatusChangeNotificationDTO.class,
				notificationProcess.notificationProcessId, application.id, application.familyId,
				application.jurisdiction.code, application.applicationNumber, application.newRecordStatus,
				user.person.firstName, user.person.lastName, notificationProcess.updatedDate, application.recordStatus,
				application.userComment, notificationProcess.notification.message, notificationProcess.status,
				notificationProcess.notificationProcessType, notificationProcess.notifiedDate));

		for (StatusChangeNotificationDTO statusChangeNotificationDTO : statusChangeNotificationList) {
			List<String> rolesList = getRolesofNotification(statusChangeNotificationDTO.getNotificationId());
			statusChangeNotificationDTO.setRoles(rolesList);
		}

		return statusChangeNotificationList;
	}

	private List<UpdateFamilyNotificationDTO> getupdateFamilyNotificationDTO(
			final NotificationBaseDTO notificationProcessDTO) {

		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QFindFamilyQueue familyQueue = QFindFamilyQueue.findFamilyQueue;
		QApplicationBase application = QApplicationBase.applicationBase;
		QUser user = QUser.user;

		List<UpdateFamilyNotificationDTO> updateFamilyNotificationList = Collections.emptyList();

		JPAQuery query = new JPAQuery(entityManager).from(notificationProcess, familyQueue, application, user);

		query.where(familyQueue.id.eq(notificationProcessDTO.getEntityId())
				.and(notificationProcess.notificationProcessId.eq(notificationProcessDTO.getNotificationId()))
				.and(notificationProcess.applicationId.eq(application.id))
				.and(notificationProcess.createdByUser.eq(user.id)));

		updateFamilyNotificationList = query.list(ConstructorExpression.create(UpdateFamilyNotificationDTO.class,
				notificationProcess.notificationProcessId, application.id, application.jurisdiction.code,
				application.applicationNumber, notificationProcess.notifiedDate, user.person.firstName,
				user.person.lastName, notificationProcess.createdDate, application.familyId, familyQueue.familyId,
				familyQueue.jurisdictionCode, familyQueue.applicationNumberFormatted,
				notificationProcess.notification.message, notificationProcess.status,
				notificationProcess.notificationProcessType, notificationProcess.entityName));

		for (UpdateFamilyNotificationDTO familyNotificationDTO : updateFamilyNotificationList) {
			List<String> rolesList = getRolesofNotification(familyNotificationDTO.getNotificationId());
			familyNotificationDTO.setRoles(rolesList);
		}

		return updateFamilyNotificationList;
	}

	private List<UploadDocumentNotificationDTO> getUploadDocumentNotificationDTO(
			final NotificationBaseDTO notificationProcessDTO) {

		QDocumentCode documentCode = QDocumentCode.documentCode;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QDownloadOfficeActionQueue downloadOfficeActionQueue = QDownloadOfficeActionQueue.downloadOfficeActionQueue;

		List<UploadDocumentNotificationDTO> uploadDocNotificationList = Collections.emptyList();

		JPAQuery query = new JPAQuery(entityManager).from(notificationProcess, downloadOfficeActionQueue, documentCode);

		query.where(downloadOfficeActionQueue.id.eq(notificationProcessDTO.getEntityId())
				.and(notificationProcess.notificationProcessId.eq(notificationProcessDTO.getNotificationId()))
				.and(downloadOfficeActionQueue.status.eq(QueueStatus.CRAWLER_ERROR))
				.and(downloadOfficeActionQueue.documentCode.eq(documentCode.code)));

		uploadDocNotificationList = query.list(ConstructorExpression.create(UploadDocumentNotificationDTO.class,
				notificationProcess.notificationProcessId, downloadOfficeActionQueue.jurisdictionCode,
				downloadOfficeActionQueue.applicationNumberFormatted, notificationProcess.notifiedDate,
				documentCode.description, notificationProcess.notification.message, notificationProcess.status,
				notificationProcess.notificationProcessType, downloadOfficeActionQueue.mailingDate,
				downloadOfficeActionQueue.id));

		for (UploadDocumentNotificationDTO documentNotificationDTO : uploadDocNotificationList) {
			List<String> rolesList = getRolesofNotification(documentNotificationDTO.getNotificationId());
			documentNotificationDTO.setRoles(rolesList);
		}

		return uploadDocNotificationList;
	}

	private List<UpdateReferenceNotificationDTO> getUpdateReferenceNotificationDTO(
			final NotificationBaseDTO notificationProcessDTO) {

		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QReferenceStagingData refStagData = QReferenceStagingData.referenceStagingData;

		List<UpdateReferenceNotificationDTO> updateRefNotification = Collections.emptyList();

		JPAQuery query = new JPAQuery(entityManager).from(notificationProcess, refStagData)
				.where(refStagData.referenceStagingDataId.eq(notificationProcessDTO.getEntityId())
						.and(notificationProcess.notificationProcessId.eq(notificationProcessDTO.getNotificationId()))
						.and(refStagData.application.recordStatus.ne(MDMRecordStatus.DROPPED))
						.and(refStagData.referenceStatus.eq(ReferenceStatus.INPADOC_FAILED)));

		updateRefNotification = query.list(ConstructorExpression.create(UpdateReferenceNotificationDTO.class,
				notificationProcess.notificationProcessId, refStagData.application.id,
				refStagData.application.jurisdiction.code, refStagData.application.applicationNumber,
				refStagData.correspondence.id, notificationProcess.notifiedDate, refStagData.referenceStagingDataId,
				refStagData.correspondence.documentCode.description, notificationProcess.notification.message,
				notificationProcess.status, notificationProcess.notificationProcessType, refStagData.jurisdiction.code,
				refStagData.publicationNumber));

		for (UpdateReferenceNotificationDTO referenceNotificationDTO : updateRefNotification) {
			List<String> rolesList = getRolesofNotification(referenceNotificationDTO.getNotificationId());
			referenceNotificationDTO.setRoles(rolesList);
		}

		return updateRefNotification;
	}

	private List<ReferenceEntryNotificationDTO> getManualEntryRefNotificationDTO(
			final NotificationBaseDTO notificationProcessDTO) {

		QOcrData ocrData = QOcrData.ocrData;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QUser user = QUser.user;

		List<ReferenceEntryNotificationDTO> refEntryNotificationList = new ArrayList<>();

		JPAQuery query = new JPAQuery(entityManager).from(notificationProcess, ocrData, user)
				.where(ocrData.ocrDataId.eq(notificationProcessDTO.getEntityId())
						.and(notificationProcess.notificationProcessId.eq(notificationProcessDTO.getNotificationId()))
						.and(user.id.eq(ocrData.correspondenceId.createdByUser)));

		refEntryNotificationList = query.list(ConstructorExpression.create(ReferenceEntryNotificationDTO.class,
				notificationProcess.notificationProcessId, ocrData.correspondenceId.application.id,
				ocrData.jurisdictionCode, ocrData.correspondenceId.application.applicationNumber,
				ocrData.correspondenceId.id, notificationProcess.notifiedDate, ocrData.ocrDataId,
				ocrData.correspondenceId.documentCode.description, notificationProcess.notification.message,
				notificationProcess.status, notificationProcess.notificationProcessType,
				ocrData.correspondenceId.mailingDate, ocrData.ocrStatus, ocrData.correspondenceId.createdDate,
				user.person.firstName, user.person.lastName));

		for (ReferenceEntryNotificationDTO referenceEntryNotificationDTO : refEntryNotificationList) {
			List<String> rolesList = getRolesofNotification(referenceEntryNotificationDTO.getNotificationId());
			referenceEntryNotificationDTO.setRoles(rolesList);
		}

		return refEntryNotificationList;
	}

	private List<NPLDuplicateCheckNotificationDTO> getNPLReferenceNotificationDTO(
			final NotificationBaseDTO notificationProcessDTO) {

		QReferenceBaseData baseData = QReferenceBaseData.referenceBaseData;
		QReferenceBaseNPLData nplData = QReferenceBaseNPLData.referenceBaseNPLData;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;

		List<NPLDuplicateCheckNotificationDTO> nplDuplicateNotificationList = Collections.emptyList();

		JPAQuery query = new JPAQuery(entityManager).from(baseData, notificationProcess, nplData)
				.where(baseData.referenceBaseDataId.eq(notificationProcessDTO.getEntityId())
						.and(notificationProcess.notificationProcessId.eq(notificationProcessDTO.getNotificationId()))
						.and(baseData.referenceBaseDataId.eq(nplData.referenceBaseDataId))
						.and(nplData.referenceType.eq(ReferenceType.NPL)));

		nplDuplicateNotificationList = query.list(ConstructorExpression.create(NPLDuplicateCheckNotificationDTO.class,
				notificationProcess.notificationProcessId, baseData.application.id,
				baseData.application.jurisdiction.code, baseData.application.applicationNumber,
				baseData.correspondence.id, notificationProcess.notifiedDate, baseData.referenceBaseDataId,
				nplData.stringData, notificationProcess.notification.message, notificationProcess.status,
				notificationProcess.notificationProcessType, baseData.correspondence.documentCode.description,
				baseData.correspondence.application.familyId));

		for (NPLDuplicateCheckNotificationDTO checkNotificationDTO : nplDuplicateNotificationList) {
			List<String> rolesList = getRolesofNotification(checkNotificationDTO.getNotificationId());
			checkNotificationDTO.setRoles(rolesList);
		}

		return nplDuplicateNotificationList;
	}

	@Override
	public long countPendingNotifications(final List<Long> appIdList,
			final List<NotificationProcessType> notificationTypesList, final List<Long> skipNotificationList) {

		long count = 0;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		BooleanBuilder predicate = getPendingNotificationsPredicate(appIdList, notificationProcess,
				notificationTypesList, skipNotificationList);
		JPAQuery jpaQuery = new JPAQuery(entityManager).from(notificationProcess).where(predicate);
		count = jpaQuery.count();
		return count;
	}

	private List<String> getRolesofNotification(final long notificationId) {
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QRole role = QRole.role;
		JPAQuery query = new JPAQuery(entityManager).from(notificationProcess).innerJoin(notificationProcess.roles,
				role);
		query.where(notificationProcess.notificationProcessId.eq(notificationId));
		List<String> rolesName = query.list(role.name);
		return rolesName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getAssigneeUserIdsByNotificationProcessId(Long notificationProcessId) {
		//Notification Process Type should not of type IDS_APPROVAL_TYPE 
		Query query = entityManager.createNativeQuery(
				"Select U.BB_USER_ID from BB_NOTIFICATION_PROCESS NP JOIN BB_NOTIFICATION_PROCESS_ROLE NPR ON NP.BB_NOTIFICATION_PROCESS_ID = NPR.BB_NOTIFICATION_PROCESS_ID AND NP.NOTIFICATION_PROCESS_TYPE != 'IDS_APPROVAL_REQUEST' AND NP.BB_NOTIFICATION_PROCESS_ID = :notificationProcessId JOIN BB_USER_ROLE UR ON NPR.BB_ROLE_ID = UR.BB_ROLE_ID JOIN BB_USER U ON U.BB_USER_ID = UR.BB_USER_ID AND U.IS_ACTIVE = 1");
		query.setParameter("notificationProcessId", notificationProcessId);
		query.setParameter("active", "true");

		List<BigInteger> ids = query.getResultList();
		return ids.stream().map(id -> id.longValue()).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getNotificationProcessIdsOnlyDependentOnUserId(Long userId) {
		Query query = entityManager.createNativeQuery(
				"SELECT NP.BB_NOTIFICATION_PROCESS_ID FROM BB_NOTIFICATION_PROCESS NP JOIN BB_NOTIFICATION_PROCESS_ROLE NPR ON NP.BB_NOTIFICATION_PROCESS_ID = NPR.BB_NOTIFICATION_PROCESS_ID AND NP.ACTIVE = 'true' AND NP.STATUS = 'PENDING' AND NP.NOTIFICATION_PROCESS_TYPE != 'IDS_APPROVAL_REQUEST' JOIN BB_USER_ROLE UR ON UR.BB_ROLE_ID = NPR.BB_ROLE_ID AND UR.BB_USER_ID = :userId WHERE ((Select COUNT(u.BB_USER_ID) AS NO_OF_USER from BB_NOTIFICATION_PROCESS_ROLE npr, BB_USER_ROLE ur, BB_USER u where u.BB_USER_ID = ur.BB_USER_ID and ur.BB_ROLE_ID = npr.BB_ROLE_ID and npr.BB_NOTIFICATION_PROCESS_ID = NP.BB_NOTIFICATION_PROCESS_ID and u.IS_ACTIVE = 1) <= 1 ) UNION SELECT DISTINCT(NP.BB_NOTIFICATION_PROCESS_ID) FROM BB_NOTIFICATION_PROCESS NP JOIN BB_NOTIFICATION_PROCESS_USER NPU ON NP.BB_NOTIFICATION_PROCESS_ID = NPU.BB_NOTIFICATION_PROCESS_ID AND NP.NOTIFICATION_PROCESS_TYPE = 'IDS_APPROVAL_REQUEST' AND NP.ACTIVE = 'true' AND NPU.BB_USER_ID = :userId");
		query.setParameter("userId", userId);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getNotificationProcessIdsByUserId(Long userId) {
		Query query = entityManager.createNativeQuery(
				"SELECT DISTINCT(NP.BB_NOTIFICATION_PROCESS_ID) FROM BB_NOTIFICATION_PROCESS NP JOIN BB_NOTIFICATION_PROCESS_ROLE NPR ON NP.BB_NOTIFICATION_PROCESS_ID = NPR.BB_NOTIFICATION_PROCESS_ID AND NP.ACTIVE = 'true' AND NP.STATUS = 'PENDING' AND NP.NOTIFICATION_PROCESS_TYPE != 'IDS_APPROVAL_REQUEST' JOIN BB_USER_ROLE UR ON UR.BB_ROLE_ID = NPR.BB_ROLE_ID AND UR.BB_USER_ID = :userId UNION SELECT DISTINCT(NP.BB_NOTIFICATION_PROCESS_ID) FROM BB_NOTIFICATION_PROCESS NP JOIN BB_NOTIFICATION_PROCESS_USER NPU ON NP.BB_NOTIFICATION_PROCESS_ID = NPU.BB_NOTIFICATION_PROCESS_ID AND NP.NOTIFICATION_PROCESS_TYPE = 'IDS_APPROVAL_REQUEST' AND NP.ACTIVE = 'true' AND NPU.BB_USER_ID = :userId");
		query.setParameter("userId", userId);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getNotificationProcessIdsOnlyDependentOnRoleId(Long roleId) {
		Query query = entityManager.createNativeQuery(
				"SELECT NP.BB_NOTIFICATION_PROCESS_ID FROM BB_NOTIFICATION_PROCESS NP JOIN BB_NOTIFICATION_PROCESS_ROLE NPR ON NP.BB_NOTIFICATION_PROCESS_ID = NPR.BB_NOTIFICATION_PROCESS_ID AND NP.ACTIVE = 'true' AND NP.STATUS = 'PENDING' AND NP.NOTIFICATION_PROCESS_TYPE != 'IDS_APPROVAL_REQUEST' AND NPR.BB_ROLE_ID = :roleId WHERE ((Select COUNT(u.BB_USER_ID) AS NO_OF_USER from BB_NOTIFICATION_PROCESS_ROLE npr, BB_USER_ROLE ur, BB_USER u where u.BB_USER_ID = ur.BB_USER_ID and ur.BB_ROLE_ID = npr.BB_ROLE_ID and npr.BB_NOTIFICATION_PROCESS_ID = NP.BB_NOTIFICATION_PROCESS_ID and u.IS_ACTIVE = 'true') <= 1 )");
		query.setParameter("roleId", roleId);

		return query.getResultList();
	}
}
