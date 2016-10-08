package com.blackbox.ids.core.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.common.DataAccessPredicate;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.correspondence.ICorrespondenceStageDAO;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dao.webcrawler.CreateApplicationQueueDAO;
import com.blackbox.ids.core.dto.IDS.ApplicationDetailsDTO;
import com.blackbox.ids.core.dto.IDS.QApplicationDetailsDTO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.ActionsBaseDto;
import com.blackbox.ids.core.dto.mdm.ChangeRequestDTO;
import com.blackbox.ids.core.dto.mdm.CreateReqApplicationDTO;
import com.blackbox.ids.core.dto.mdm.CreateReqFamilyDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqApplicationDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqAssigneeDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqFamilyDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.ActiveRecordsFilter;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.QMdmRecordDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilyDetailDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilySearchFilter;
import com.blackbox.ids.core.model.EntityUser;
import com.blackbox.ids.core.model.QEntityUser;
import com.blackbox.ids.core.model.QUser;
import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.IDS.QIDS;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.correspondence.QCorrespondenceStaging;
import com.blackbox.ids.core.model.enums.ApplicationNumberStatus;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.ExclusionList;
import com.blackbox.ids.core.model.mdm.QApplicationBase;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.AssigneeAttorneyDocketNo;
import com.blackbox.ids.core.model.mstr.AttorneyDocketFormat;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.mstr.ProsecutionStatus;
import com.blackbox.ids.core.model.mstr.QAssignee;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.model.notification.process.QNotificationProcess;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.blackbox.ids.core.model.referenceflow.QIDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.model.webcrawler.QCreateApplicationQueue;
import com.blackbox.ids.core.model.webcrawler.QFindFamilyQueue;
import com.blackbox.ids.core.repository.ExclusionListRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.webcrawler.dao.IWebCrawlerApplicationDAO;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.expr.CaseBuilder;
import com.mysema.query.types.expr.StringExpression;
import com.mysql.jdbc.StringUtils;

/**
 * The <code>ApplicationBaseDaoImpl</code> provides implementation of {@link ApplicationBaseDAO} for common database CRUD
 * operations for entity class {@link ApplicationBase}.
 *
 * @author ajay2258
 */
@Repository
public class ApplicationBaseDaoImpl extends BaseDaoImpl<ApplicationBase, Long> implements ApplicationBaseDAO {

	@Autowired
	private ExclusionListRepository exclusionListRepository;

	@Autowired
	private ICorrespondenceStageDAO correspondenceStageDAO;

	@Autowired
	private CreateApplicationQueueDAO  createApplicationQueueDao;

	@Autowired
	private IWebCrawlerApplicationDAO  webCrawlerApplicationDao;

	@Override
	public String generateFamilyId() throws ApplicationException {
		return ApplicationBase.PREFIX_FAMILY_ID + String.format("%06d", getNextSequence(ApplicationBase.SEQ_FAMILY_ID));
	}

	@Override
	public List<FamilyDetailDTO> getFamilyDetails(FamilySearchFilter searchFilter) throws ApplicationException {

		QApplicationBase application = QApplicationBase.applicationBase;
		List<ApplicationType> firstFilings = Arrays.asList(ApplicationType.FIRST_FILING, ApplicationType.PCT_US_FIRST_FILING);

		BooleanBuilder predicate = new BooleanBuilder();
		if (searchFilter.isOnlyFirstFiling()) {
			predicate.and(application.appDetails.childApplicationType.in(firstFilings));
		}

		List<String> familyIds = null;

		switch (searchFilter.getFamilyLinker()) {
		case APPLICATION_NUMBER:
			familyIds = getJPAQuery().from(QApplicationBase.applicationBase)
			.where(QApplicationBase.applicationBase.appDetails.applicationNumberRaw
					.startsWithIgnoreCase(searchFilter.getApplicationNo())
					.and(QApplicationBase.applicationBase.jurisdiction.code.equalsIgnoreCase(searchFilter.getJurisdiction())))
			.list(QApplicationBase.applicationBase.familyId);
			break;

		case ATTORNEY_DOCKET_NUMBER:
			familyIds = getJPAQuery().from(QApplicationBase.applicationBase)
			.where(QApplicationBase.applicationBase.attorneyDocketNumber.segment
					.equalsIgnoreCase(searchFilter.getDocketNo()))
			.list(QApplicationBase.applicationBase.familyId);
			break;

		case FAMILY_ID:
			familyIds = Arrays.asList(searchFilter.getFamilyId().toUpperCase());
			break;

		default:
			predicate.and(application.id.isNull());	// Don't return any record.
			familyIds = Collections.emptyList();
			break;
		}

		return getJPAQuery()
				.from(application)
				.where(predicate.and(application.familyId.in(familyIds)))
				.list(ConstructorExpression.create(FamilyDetailDTO.class,
						application.id, application.familyId, application.jurisdiction.code,
						application.applicationNumber, application.attorneyDocketNumber.segment,
						application.appDetails.filingDate, application.assignee.name, application.appDetails.childApplicationType));
	}

	@Override
	public SearchResult<MdmRecordDTO> filterActiveRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo) {
		Long ownedBy = filter.getOwnedBy();
		Calendar startFillingDate = null ;
		Calendar endFillingDate = null ;
		if(filter.getStartDate()!=null || filter.getEndDate()!=null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}
		QApplicationBase application = QApplicationBase.applicationBase;
		QUser user = QUser.user;

		JPAQuery query = getJPAQuery().from(application, user);
		BooleanBuilder predicate = new BooleanBuilder()
				.and(DataAccessPredicate.getDataAccessPredicate(application))
				.and(application.createdByUser.eq(user.id));

		if (ownedBy != null) {
			predicate.and(application.recordStatus.eq(MDMRecordStatus.ACTIVE)
					.and(application.newRecordStatus.eq(MDMRecordStatus.BLANK))
					.and(application.updatedByUser.eq(ownedBy)));
		} else {
			predicate.and(application.recordStatus.eq(MDMRecordStatus.ACTIVE)
					.and(application.newRecordStatus.eq(MDMRecordStatus.BLANK)));
		}
		query = query.distinct();
		long totalRecords = query.where(predicate).count();
		long filteredResults = 0L;
		List<MdmRecordDTO> items = Collections.emptyList();

		if (totalRecords > 0L) {
			if(startFillingDate!=null || endFillingDate!=null) {
				predicate.and(application.appDetails.filingDate.between(startFillingDate, endFillingDate));
			}
			predicate.and(getExtendedSearchPredicate(application, user, filter));
			filteredResults = query.clone().where(predicate).count();

			if(filteredResults > 0L) {
				query = createSortOrder(pageInfo, query, application);
				query = createSortOrder(pageInfo,query,user);
				items = query.where(predicate)
						.limit(pageInfo.getLimit()).offset(pageInfo.getOffset())
						.list(ConstructorExpression.create(MdmRecordDTO.class, application.id
								,application.jurisdiction.code, application.appDetails.applicationNumberRaw
								, application.familyId,
								application.attorneyDocketNumber.segment, application.appDetails.filingDate,
								application.assignee.name
								, application.appDetails.childApplicationType, application.createdDate
								, user.person.firstName,user.person.lastName));
			}
		}
		filterLockedApplications(items);
		SearchResult<MdmRecordDTO> result = new SearchResult<>(totalRecords, filteredResults, items);

		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		long appId = 0L;
		for(MdmRecordDTO record : result.getItems()) {
			appId = record.getDbId();
			JPAQuery appEditableQuery = getJPAQuery().from(notificationProcess);
			BooleanBuilder appEditablePredicate = new BooleanBuilder()
					.and(notificationProcess.active.eq(true)
							.and(notificationProcess.status.eq(NotificationStatus.PENDING))
							.and(notificationProcess.applicationId.eq(appId)
									.or((notificationProcess.entityName.eq(EntityName.APPLICATION_BASE).and(notificationProcess.entityId.eq(appId))))));
			if(appEditableQuery.where(appEditablePredicate).count()>0) {
				record.setNonEditable(true);
			}
		}

		return result;
	}

	private void filterLockedApplications(List<MdmRecordDTO> items) {
		Set<Long> applicationIds = items.stream().map(MdmRecordDTO::getDbId).collect(Collectors.toSet());

		QApplicationBase application = QApplicationBase.applicationBase;
		QEntityUser entityUser = QEntityUser.entityUser;
		QUser user = QUser.user;

		Map<Long, String> lockedApplications =
				getJPAQuery().from(application, entityUser, user)
				.where(entityUser.entity.eq(EntityUser.EntityName.APPLICATION_BASE)
						.and(application.id.eq(entityUser.recordId))
						.and(user.id.eq(entityUser.lockedBy))
						.and(application.id.in(applicationIds)))
				.map(application.id, user.person.firstName.concat(" ").concat(user.person.lastName));

		items.stream().filter(e-> lockedApplications.containsKey(e.getDbId())).forEach(e->
		e.setLockedBy(lockedApplications.get(e.getDbId()))
				);
	}

	@Override
	public SearchResult<MdmRecordDTO> filterInActiveRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo) {
		Calendar startFillingDate = null ;
		Calendar endFillingDate = null ;
		if(filter.getStartDate()!=null || filter.getEndDate()!=null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}

		QApplicationBase application = QApplicationBase.applicationBase;
		QUser user = QUser.user;

		JPAQuery query = getJPAQuery().from(application, user);
		BooleanBuilder predicate = new BooleanBuilder()
				.and(DataAccessPredicate.getDataAccessPredicate(application))
				.and(application.updatedByUser.eq(user.id));

		predicate.and((application.recordStatus.ne(MDMRecordStatus.ACTIVE)
				.and(application.newRecordStatus.eq(MDMRecordStatus.BLANK)))
				.or(application.recordStatus.eq(MDMRecordStatus.ACTIVE)
						.and(application.newRecordStatus.ne(MDMRecordStatus.BLANK))));

		long totalRecords = query.where(predicate).count();
		long filteredResults = 0L;
		List<MdmRecordDTO> items = Collections.emptyList();

		if (totalRecords > 0L) {
			if(startFillingDate!=null || endFillingDate!=null) {
				predicate.and(application.appDetails.filingDate.between(startFillingDate, endFillingDate));
			}
			filteredResults = query.clone().where(predicate).count();

			if(filteredResults > 0L) {
				query = createSortOrder(pageInfo, query, application);
				query = createSortOrder(pageInfo,query,user);
				items =query.where(predicate)
						.limit(pageInfo.getLimit())
						.offset(pageInfo.getOffset())
						.list(ConstructorExpression.create(MdmRecordDTO.class, application.id,application.jurisdiction.code
								, application.appDetails.applicationNumberRaw, application.familyId, application.attorneyDocketNumber.segment
								, application.appDetails.filingDate,
								application.assignee.name
								, application.appDetails.childApplicationType
								, application.updatedDate, user.person.firstName , user.person.lastName
								,application.recordStatus,application.newRecordStatus,application.userComment));
			}
		}

		SearchResult<MdmRecordDTO> result = new SearchResult<>(totalRecords, filteredResults, items);
		return result;
	}


	@Override
	public List<ActionsBaseDto> getActionsBaseDTOfromBase() throws ApplicationException {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery().from(application)
				.list(ConstructorExpression.create(ActionsBaseDto.class
						, application.jurisdiction.code
						, application.applicationNumber));
	}

	@Override
	public SearchResult<CreateReqApplicationDTO> getCreateAppRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo)
	{
		Calendar startFillingDate = null;
		Calendar endFillingDate = null;

		if(filter.getStartDate()!=null || filter.getEndDate()!=null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}

		QNotificationProcess notification = QNotificationProcess.notificationProcess;
		QCreateApplicationQueue applicationQueue = QCreateApplicationQueue.createApplicationQueue;
		QCorrespondenceStaging correspondence = QCorrespondenceStaging.correspondenceStaging;
		QUser user = QUser.user;

		JPAQuery query = getJPAQuery().from(notification, applicationQueue, user, correspondence).distinct();
		BooleanBuilder predicate = getCreateAppRecordsPredicate(notification, applicationQueue, correspondence, user);

		long totalRecords = query.clone().where(predicate).count();
		//long totalRecords = countCreateAppRecords();
		long filteredRecords = 0L;
		List<CreateReqApplicationDTO> items = Collections.emptyList();

		StringExpression jurisdictionCode = new CaseBuilder()
				.when(notification.entityName.eq(EntityName.CREATE_APPLICATION_QUEUE)).then(applicationQueue.jurisdictionCode)
				.otherwise(correspondence.jurisdictionCode);
		StringExpression applicationNo = new CaseBuilder()
				.when(notification.entityName.eq(EntityName.CREATE_APPLICATION_QUEUE)).then(applicationQueue.applicationNumberFormatted)
				.otherwise(correspondence.applicationNumber);

		if (totalRecords > 0L) {
			if(startFillingDate!=null || endFillingDate!=null) {
				predicate.and(notification.createdDate.between(startFillingDate, endFillingDate));
			}
			filteredRecords = query.clone().where(predicate).count();
			if(filteredRecords > 0L) {
				query = createSortOrder(pageInfo,query,notification);
				query = createSortOrder(pageInfo,query,user);

				items = query.where(predicate)
						.limit(pageInfo.getLimit())
						.offset(pageInfo.getOffset())
						.list(ConstructorExpression.create(CreateReqApplicationDTO.class,
								notification.notificationProcessId,notification.entityId,
								notification.entityName,
								jurisdictionCode, applicationNo,
								user.person.firstName,user.person.lastName,
								notification.notifiedDate));
			}
		}
		return new SearchResult<CreateReqApplicationDTO>(totalRecords, filteredRecords, items);
	}

	@Override
	public long countCreateAppRecords() {
		QNotificationProcess notification = QNotificationProcess.notificationProcess;
		QCreateApplicationQueue applicationQueue = QCreateApplicationQueue.createApplicationQueue;
		QCorrespondenceStaging correspondence = QCorrespondenceStaging.correspondenceStaging;
		QUser user = QUser.user;

		JPAQuery query = getJPAQuery().from(notification, applicationQueue, user, correspondence).distinct();
		BooleanBuilder predicate = getCreateAppRecordsPredicate(notification, applicationQueue, correspondence, user);
		return query.where(predicate).count();
	}


	private BooleanBuilder getCreateAppRecordsPredicate(QNotificationProcess notification, QCreateApplicationQueue applicationQueue, QCorrespondenceStaging correspondence, QUser user){
		List<NotificationProcessType> notificationTypes = Arrays.asList(
				NotificationProcessType.CREATE_APPLICATION_REQUEST,
				NotificationProcessType.CREATE_APPLICATION_REQUEST_USER_INITIATED);

		return new BooleanBuilder()
				.andAnyOf(
						notification.entityName.eq(EntityName.CREATE_APPLICATION_QUEUE)
						.and(notification.entityId.eq(applicationQueue.id)),
						notification.entityName.eq(EntityName.CORRESPONDENCE_STAGING)
						.and(notification.entityId.eq(correspondence.Id)))
				.and(notification.createdByUser.eq(user.id))
				.and(notification.notificationProcessType.in(notificationTypes))
				.and(notification.active.eq(true))
				.and(DataAccessPredicate.getNotificationAccessPredicate(notification));
	}

	@Override
	public long acceptCreateAppRequest(Long notificationId) {
		long recordsUpdated = 0L;
		QNotificationProcess notification = QNotificationProcess.notificationProcess;
		Tuple resultSet = getJPAQuery().from(notification)
				.where(notification.notificationProcessId.eq(notificationId))
				.uniqueResult(new QTuple(notification.entityId, notification.entityName));

		Long id = resultSet.get(notification.entityId);
		EntityName entityName = resultSet.get(notification.entityName);

		switch (entityName) {
		case CREATE_APPLICATION_QUEUE:
			recordsUpdated = createApplicationQueueDao.createApplicationRequest(id);
			break;
		case CORRESPONDENCE_STAGING:
			recordsUpdated = correspondenceStageDAO.createApplicationRequest(id);
			break;
		default:
			recordsUpdated = 0L;
		}
		return recordsUpdated;
	}

	@Override
	public long rejectCreateAppRequest(long entityId, EntityName entityName) {
		long recordsUpdated = 0L;
		switch (entityName) {
		case CREATE_APPLICATION_QUEUE:
			recordsUpdated = createApplicationQueueDao.rejectApplicationRequest(entityId);
			break;
		case CORRESPONDENCE_STAGING:
			recordsUpdated = correspondenceStageDAO.rejectApplicationRequest(entityId);
			break;
		default:
			recordsUpdated = 0L;
		}

		return recordsUpdated;
	}

	@Override
	public SearchResult<CreateReqFamilyDTO> getCreateFamilyRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo)
			throws ApplicationException {
		Calendar startFillingDate = null;
		Calendar endFillingDate = null;

		if(filter.getStartDate()!=null || filter.getEndDate()!=null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}
		QNotificationProcess notification = QNotificationProcess.notificationProcess;
		QCreateApplicationQueue applicationQueue = QCreateApplicationQueue.createApplicationQueue;
		QApplicationBase application = QApplicationBase.applicationBase;

		JPAQuery query = getJPAQuery().from(notification,applicationQueue,application);
		BooleanBuilder predicate = getCreateReqFamilyRecordsPredicate(notification, applicationQueue, application);
		//predicate = predicate.and(DataAccessPredicate.getDataAccessPredicate(application));
		long totalRecords = query.clone().where(predicate).count();
		long filteredRecords = 0L;
		List<CreateReqFamilyDTO> items = Collections.emptyList();
		if (totalRecords > 0L) {
			if(startFillingDate!=null || endFillingDate!=null) {
				predicate.and(notification.createdDate.between(startFillingDate, endFillingDate));
			}
			filteredRecords = query.clone().where(predicate).count();
			if(filteredRecords > 0L) {
				query = createSortOrder(pageInfo,query,notification);
				items = query.where(predicate)
						.limit(pageInfo.getLimit())
						.offset(pageInfo.getOffset())
						.list(ConstructorExpression.create(CreateReqFamilyDTO.class,
								notification.notificationProcessId,notification.entityId,
								notification.entityName,
								applicationQueue.jurisdictionCode,applicationQueue.applicationNumberFormatted,
								notification.notifiedDate,
								applicationQueue.familyId,
								application.jurisdiction.code,
								application.applicationNumber));
			}
		}
		return new SearchResult<CreateReqFamilyDTO>(totalRecords, filteredRecords, items);



	}

	private BooleanBuilder getCreateReqFamilyRecordsPredicate(QNotificationProcess notification,
			QCreateApplicationQueue applicationQueue, QApplicationBase application) {
		BooleanBuilder predicate = new BooleanBuilder()
				.and(notification.entityId.eq(applicationQueue.id)).and(applicationQueue.familyId.eq(application.familyId))
				.and(application.appDetails.childApplicationType.in(ApplicationType.firstFilings()))
				.and(notification.notificationProcessType.eq(NotificationProcessType.CREATE_FAMILY_MEMBER))
				.and(notification.active.eq(true));
		predicate = predicate.and(DataAccessPredicate.getNotificationAccessPredicate(notification));
		return predicate;
	}

	@Override
	public long countCreateFamilyRecords(){
		QNotificationProcess notification = QNotificationProcess.notificationProcess;
		QCreateApplicationQueue applicationQueue = QCreateApplicationQueue.createApplicationQueue;
		QApplicationBase application = QApplicationBase.applicationBase;

		JPAQuery query = getJPAQuery().from(notification,applicationQueue,application);
		BooleanBuilder predicate = getCreateReqFamilyRecordsPredicate(notification, applicationQueue, application);
		return query.where(predicate).count();
	}

	@Override
	public SearchResult<ChangeRequestDTO> getChangeRequestRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo)
			throws ApplicationException {

		QApplicationBase application = QApplicationBase.applicationBase;
		QNotificationProcess notification =  QNotificationProcess.notificationProcess;
		QUser user = QUser.user;

		Calendar startFillingDate = null ;
		Calendar endFillingDate = null ;
		if(filter.getStartDate()!=null || filter.getEndDate()!=null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}

		JPAQuery query =  getJPAQuery().from(application, notification, user);
		BooleanBuilder predicate  = getChangeRequestRecordsPredicate(application, notification, user);
		if(startFillingDate!=null || endFillingDate!=null) {
			predicate.and(notification.updatedDate.between(startFillingDate, endFillingDate));
		}

		long totalRecords = query.where(predicate).count();
		if(pageInfo.getSortAttribute()!=null){
			query = createSortOrder(pageInfo, query, application);
			query = createSortOrder(pageInfo, query, notification);
			query = createSortOrder(pageInfo, query, user);
		}

		List<ChangeRequestDTO> items = query
				.limit(pageInfo.getLimit())
				.offset(pageInfo.getOffset())
				.list(ConstructorExpression.create(ChangeRequestDTO.class, application.familyId
						,application.jurisdiction.code, application.applicationNumber, application.newRecordStatus
						, user.person.firstName , user.person.lastName
						,notification.updatedDate, application.recordStatus ,  notification.notificationProcessId , notification.entityId,
						application.userComment));

		long filteredResults = query.count();

		SearchResult<ChangeRequestDTO> result =new SearchResult<>(totalRecords , filteredResults , items);
		return result;
	}

	@Override
	public long countChangeRequestRecords() {
		QApplicationBase application = QApplicationBase.applicationBase;
		QNotificationProcess notification =  QNotificationProcess.notificationProcess;
		QUser user = QUser.user;
		JPAQuery query =  getJPAQuery().from(application, notification, user);
		BooleanBuilder predicate  = getChangeRequestRecordsPredicate(application, notification, user);
		return query.where(predicate).count();
	}

	private BooleanBuilder getChangeRequestRecordsPredicate(QApplicationBase application, QNotificationProcess notification, QUser user){
		BooleanBuilder predicate  = new BooleanBuilder();
		predicate.and(notification.entityName.eq(EntityName.APPLICATION_BASE))
		.and(notification.entityId.eq(application.id))
		.and(notification.updatedByUser.eq(user.id))
		.and(notification.notificationProcessType.eq(NotificationProcessType.DASHBOARD_ACTION_STATUS))
		.and(notification.active.isTrue());
		predicate = predicate.and(DataAccessPredicate.getDataAccessPredicate(application));
		predicate = predicate.and(DataAccessPredicate.getNotificationAccessPredicate(notification));
		return predicate;
	}

	@Override
	public SearchResult<UpdateReqApplicationDTO> getUpdateReqApplicationRecords (ActiveRecordsFilter filter,PaginationInfo pageInfo)
	{
		List <UpdateReqApplicationDTO> items = getPagedAppRecords(pageInfo.getOffset(),pageInfo.getLimit());
		SearchResult<UpdateReqApplicationDTO> result = new SearchResult<>(0L, 0L, items);
		return result;

	}

	@Override
	public long countUpdateReqApplicationRecords(){
		return 0;
	}

	private List<UpdateReqApplicationDTO> getPagedAppRecords(long offset, long limit) {
		List<UpdateReqApplicationDTO> records = new ArrayList<>();
		return records;
	}

	@Override
	public SearchResult<UpdateReqAssigneeDTO> getUpdateReqAssigneeRecords (ActiveRecordsFilter filter,PaginationInfo pageInfo)
	{
		Calendar startFillingDate = null;
		Calendar endFillingDate = null;

		if(filter.getStartDate()!=null || filter.getEndDate()!=null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}
		QNotificationProcess notification = QNotificationProcess.notificationProcess;
		QApplicationBase application = QApplicationBase.applicationBase;

		JPAQuery query = getJPAQuery().from(notification,application);
		BooleanBuilder predicate = getUpdateReqAssigneeRecordsPredicate(notification, application);
		long totalRecords = query.clone().where(predicate).count();
		long filteredRecords = 0L;
		List<UpdateReqAssigneeDTO> items = Collections.emptyList();
		if (totalRecords > 0L) {
			if(startFillingDate!=null || endFillingDate!=null) {
				predicate.and(notification.createdDate.between(startFillingDate, endFillingDate));
			}
			filteredRecords = query.clone().where(predicate).count();
			if(filteredRecords > 0L) {
				query = createSortOrder(pageInfo, query, application);
				query = createSortOrder(pageInfo, query, notification);
				items = query.where(predicate)
						.limit(pageInfo.getLimit())
						.offset(pageInfo.getOffset())
						.list(ConstructorExpression.create(UpdateReqAssigneeDTO.class,
								notification.notificationProcessId,notification.entityId,
								notification.entityName,application.familyId,
								application.jurisdiction.code,application.applicationNumber,
								application.attorneyDocketNumber.segment,notification.notifiedDate));
			}
		}
		return new SearchResult<UpdateReqAssigneeDTO>(totalRecords, filteredRecords, items);
	}

	@Override
	public long countUpdateReqAssigneeRecords(){
		QNotificationProcess notification = QNotificationProcess.notificationProcess;
		QApplicationBase application = QApplicationBase.applicationBase;
		JPAQuery query = getJPAQuery().from(notification,application);
		BooleanBuilder predicate = getUpdateReqAssigneeRecordsPredicate(notification, application);
		return query.where(predicate).count();
	}

	private BooleanBuilder getUpdateReqAssigneeRecordsPredicate(QNotificationProcess notification, QApplicationBase application){
		BooleanBuilder predicate = new BooleanBuilder()					//TODO: data access
				.and(notification.entityId.eq(application.id))
				.and(notification.notificationProcessType.eq(NotificationProcessType.UPDATE_ASSIGNEE))
				.and(application.assignee.id.eq(Assignee.ID_DEFAULT_ASSIGNEE)).and(notification.active.eq(true));
		predicate = predicate.and(DataAccessPredicate.getDataAccessPredicate(application));
		predicate = predicate.and(DataAccessPredicate.getNotificationAccessPredicate(notification));
		return predicate;
	}


	@Override
	public SearchResult<UpdateReqFamilyDTO> getUpdateReqFamilyRecords (ActiveRecordsFilter filter,PaginationInfo pageInfo) {
		Calendar startFillingDate = null;
		Calendar endFillingDate = null;

		if(filter.getStartDate()!=null || filter.getEndDate()!=null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}
		QNotificationProcess notification = QNotificationProcess.notificationProcess;
		QFindFamilyQueue familyQueue = QFindFamilyQueue.findFamilyQueue;
		QApplicationBase application = QApplicationBase.applicationBase;
		QUser user = QUser.user;

		JPAQuery query = getJPAQuery().from(notification,familyQueue,application,user);
		BooleanBuilder predicate = getUpdateReqFamilyRecordsPredicate(notification, familyQueue, application, user);
		long totalRecords = query.clone().where(predicate).count();
		long filteredRecords = 0L;
		List<UpdateReqFamilyDTO> items = Collections.emptyList();
		if (totalRecords > 0L) {
			if(startFillingDate!=null || endFillingDate!=null) {
				predicate.and(notification.createdDate.between(startFillingDate, endFillingDate));
			}
			filteredRecords = query.clone().where(predicate).count();
			if(filteredRecords > 0L) {
				query = createSortOrder(pageInfo,query,notification);
				query = createSortOrder(pageInfo,query,user);
				items = query.where(predicate)
						.limit(pageInfo.getLimit())
						.offset(pageInfo.getOffset())
						.list(ConstructorExpression.create(UpdateReqFamilyDTO.class,
								application.id,
								notification.notificationProcessId,notification.entityId,
								notification.entityName,
								application.jurisdiction.code,application.applicationNumber,
								notification.notifiedDate,user.person.firstName , user.person.lastName,
								notification.createdDate,application.familyId,
								familyQueue.familyId,familyQueue.jurisdictionCode,
								familyQueue.applicationNumberFormatted));
			}
		}
		return new SearchResult<UpdateReqFamilyDTO>(totalRecords, filteredRecords, items);

	}

	@Override
	public long countUpdateReqFamilyRecords() {
		QNotificationProcess notification = QNotificationProcess.notificationProcess;
		QFindFamilyQueue familyQueue = QFindFamilyQueue.findFamilyQueue;
		QApplicationBase application = QApplicationBase.applicationBase;
		QUser user = QUser.user;

		JPAQuery query = getJPAQuery().from(notification,familyQueue,application,user);
		BooleanBuilder predicate = getUpdateReqFamilyRecordsPredicate(notification, familyQueue, application, user);
		return query.where(predicate).count();
	}

	private BooleanBuilder getUpdateReqFamilyRecordsPredicate(QNotificationProcess notification, QFindFamilyQueue familyQueue,QApplicationBase application, QUser user){
		BooleanBuilder predicate = new BooleanBuilder()					//TODO: data access
				.and(notification.entityId.eq(familyQueue.id)).and(notification.applicationId.eq(application.id))
				.and(notification.createdByUser.eq(user.id))
				.and(notification.notificationProcessType.eq(NotificationProcessType.UPDATE_FAMILY_LINKAGE))
				.and(notification.active.eq(true));
		predicate = predicate.and(DataAccessPredicate.getDataAccessPredicate(application));
		predicate = predicate.and(DataAccessPredicate.getNotificationAccessPredicate(notification));
		return predicate;
	}
	@Override
	public long rejectUpdateFamilyRequest(long entityId, EntityName entityName) {
		long recordsUpdated = 0L;
		switch (entityName) {
		case FIND_FAMILY_QUEUE:
			webCrawlerApplicationDao.updateFindFamilyQueueStatus(entityId, QueueStatus.REJECTED);
			break;
		default:
			recordsUpdated = 0L;
		}
		return recordsUpdated;
	}


	@Override
	public int getCreateRequestCount() {
		// TODO Auto-generated method stub
		return testCount();
	}

	@Override
	public int getUpdateRequestCount() {
		// TODO Auto-generated method stub
		return testCount();
	}

	@Override
	public int getChangeRequestCount() {
		// TODO Auto-generated method stub
		return testCount();
	}

	//DELETE after use
	private int testCount(){
		return 10;
	}

	@Override
	public ApplicationBase getApplicationBase(Long id) {
		String hql = "select a from ApplicationBase" + " a where a.id =:id";
		TypedQuery<ApplicationBase> query = getEntityManager().createQuery(hql, ApplicationBase.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}

	@Override
	public boolean updateRecordStatus(Long id, String comment, MDMRecordStatus mDMRecordStatus) {
		Long updatedBy = BlackboxSecurityContextHolder.getUserId();
		String updateHql = "UPDATE ApplicationBase a set a.recordStatus=:recordStatus, a.userComment=:userComment, a.updatedByUser=:updatedByUser "  + "WHERE a.id IN :id";
		Query query = getEntityManager().createQuery(updateHql);
		query.setParameter("recordStatus", mDMRecordStatus);
		query.setParameter("userComment", comment);
		query.setParameter("updatedByUser", updatedBy);
		query.setParameter("id", id);
		int rowUpdate = query.executeUpdate();
		boolean updateRecord = (rowUpdate>0)? true : false;
		return updateRecord;
	}

	@Override
	public boolean updateFamilyRecordStatus(String familyId, String comment, MDMRecordStatus mDMRecordStatus) {
		Long updatedBy = BlackboxSecurityContextHolder.getUserId();
		String updateHql = "UPDATE ApplicationBase a set a.recordStatus=:recordStatus, a.userComment=:userComment, a.updatedByUser=:updatedByUser "  + "WHERE a.familyId = :familyId";
		Query query = getEntityManager().createQuery(updateHql);
		query.setParameter("recordStatus", mDMRecordStatus);
		query.setParameter("userComment", comment);
		query.setParameter("updatedByUser", updatedBy);
		query.setParameter("familyId", familyId);
		int rowUpdate = query.executeUpdate();
		boolean updateRecord = (rowUpdate > 0) ? true : false;
		return updateRecord;
	}

	@Override
	public ApplicationBase findByApplicationNoAndJurisdictionCode(final String jurisdiction, final String applicationNo) {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery().from(application)
				.where(applicationPredicate(application, jurisdiction, applicationNo))
				.uniqueResult(application);
	}

	private BooleanBuilder applicationPredicate(QApplicationBase application, String jurisdiction, String applicationNo) {
		return new BooleanBuilder().and(application.jurisdiction.code.equalsIgnoreCase(jurisdiction)
				.and((application.applicationNumber.equalsIgnoreCase(applicationNo))
						.or(application.appDetails.applicationNumberRaw.equalsIgnoreCase(applicationNo)))
						.and(application.recordStatus.ne(MDMRecordStatus.DROPPED)));
	}

	@Override
	public Long canAccessApplication(final Long appId) {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery().from(application)
				.where(application.id.eq(appId).and(DataAccessPredicate.getDataAccessPredicate(application)))
				.uniqueResult(application.id);
	}

	@Override
	public MdmRecordDTO fetchApplicationDetails(final long applicationId) {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery().from(application)
				.where(application.id.eq(applicationId))
				.uniqueResult(new QMdmRecordDTO(application.id, application.assignee.name,
						application.customer.customerNumber, application.organizationDetails.entity));
	}

	@Override
	public List<MdmRecordDTO> findByApplicationNoAndJurisdiction(final String jurisdiction, final String applicationNo) {
		QApplicationBase application = QApplicationBase.applicationBase;

		BooleanBuilder predicate = new BooleanBuilder();

		predicate.and(application.recordStatus.eq(MDMRecordStatus.ACTIVE));

		if(!StringUtils.isNullOrEmpty(jurisdiction)) {
			predicate.and(application.jurisdiction.code.equalsIgnoreCase(jurisdiction));
		}

		if(!StringUtils.isNullOrEmpty(applicationNo)) {
			predicate.and(application.applicationNumber.startsWithIgnoreCase(applicationNo))
			.or(application.appDetails.applicationNumberRaw.startsWithIgnoreCase(applicationNo));
		}

		return getJPAQuery().from(application)
				.where(predicate)
				.list(new QMdmRecordDTO(application));
	}

	@Override
	public boolean updateNewRecordStatus(List<Long> id, String comment, MDMRecordStatus mdmRecordStatus) {
		Long updatedBy = BlackboxSecurityContextHolder.getUserId();
		String updateHql = "UPDATE ApplicationBase a set a.newRecordStatus=:newRecordStatus, a.userComment=:userComment, a.updatedByUser=:updatedByUser "  + "WHERE a.id IN :id";
		Query query = getEntityManager().createQuery(updateHql);
		query.setParameter("newRecordStatus",mdmRecordStatus);
		query.setParameter("userComment", comment);
		query.setParameter("updatedByUser", updatedBy);
		query.setParameter("id", id);
		int rowUpdate = query.executeUpdate();
		boolean updateRecord = (rowUpdate > 0) ? true : false;
		return updateRecord;
	}

	@Override
	public boolean updateDeactivateNewRecordStatus(Long recordId,
			MDMRecordStatus mdmRecordStatus) {
		String updateHql = "UPDATE ApplicationBase a set a.newRecordStatus=:newRecordStatus "  + "WHERE a.id = :id";
		Query query = getEntityManager().createQuery(updateHql);
		query.setParameter("newRecordStatus",mdmRecordStatus);
		query.setParameter("id", recordId);
		int rowUpdate = query.executeUpdate();
		boolean updateRecord = (rowUpdate > 0) ? true : false;
		return updateRecord;
	}

	@Override
	public boolean updateDroppedNewRecordStatus(List<Long> recordIdSet,
			MDMRecordStatus mdmRecordStatus) {
		String updateHql = "UPDATE ApplicationBase a set a.newRecordStatus=:newRecordStatus "  + "WHERE a.id IN :id";
		Query query = getEntityManager().createQuery(updateHql);
		query.setParameter("newRecordStatus",mdmRecordStatus);
		query.setParameter("id", recordIdSet);
		int rowUpdate = query.executeUpdate();
		boolean updateRecord = (rowUpdate > 0) ? true : false;
		return updateRecord;
	}

	@Override
	public List<MdmRecordDTO> findByFamily(String family) {
		QApplicationBase application = QApplicationBase.applicationBase;
		BooleanBuilder predicate = new BooleanBuilder();
		predicate.and(application.recordStatus.eq(MDMRecordStatus.ACTIVE));
		if(!StringUtils.isNullOrEmpty(family)) {
			predicate.and(application.familyId.startsWithIgnoreCase(family));
		}
		return getJPAQuery().from(application)
				.where(predicate)
				.list(new QMdmRecordDTO(application));
	}

	@Override
	public List<MdmRecordDTO> findByFamilyAndRecordStatus(long recordId, String familyId, MDMRecordStatus mdmRecordStatus) {
		QApplicationBase application = QApplicationBase.applicationBase;
		BooleanBuilder predicate = new BooleanBuilder();
		if(!StringUtils.isNullOrEmpty(familyId)) {
			predicate.and(application.familyId.eq(familyId)).and(application.recordStatus.eq(mdmRecordStatus)).and(application.id.ne(recordId));
		}
		return getJPAQuery().from(application)
				.where(predicate)
				.list(new QMdmRecordDTO(application));
	}

	@Override
	public List<MdmRecordDTO> findByAttorneyDocket(String attorneyDocket) {
		QApplicationBase application = QApplicationBase.applicationBase;
		BooleanBuilder predicate = new BooleanBuilder();
		predicate.and(application.recordStatus.eq(MDMRecordStatus.ACTIVE));
		if(!StringUtils.isNullOrEmpty(attorneyDocket)) {
			predicate.and(application.attorneyDocketNumber.segment.startsWithIgnoreCase(attorneyDocket));
		}
		return getJPAQuery().from(application)
				.where(predicate)
				.list(new QMdmRecordDTO(application));
	}

	@Override
	public long updateAssignee(String familyId, String strAssignee) {
		Assignee assignee = getJPAQuery().from(QAssignee.assignee)
				.where(QAssignee.assignee.name.equalsIgnoreCase(strAssignee))
				.uniqueResult(QAssignee.assignee);

		long recordsUpdated = 0L;
		if (assignee != null) {
			QApplicationBase application = QApplicationBase.applicationBase;
			recordsUpdated = getJPAUpdateClause(application)
					.set(application.assignee, assignee)
					.where(application.familyId.eq(familyId).and(application.assignee.id.eq(Assignee.ID_DEFAULT_ASSIGNEE)))
					.execute();
		}
		return recordsUpdated;
	}

	@Override
	public ApplicationBase findActiveAppByApplicationNo(final String jurisdiction, final String applicationNo) {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery().from(application)
				.where(application.jurisdiction.code.equalsIgnoreCase(jurisdiction)
						.and(application.applicationNumber.equalsIgnoreCase(applicationNo))
						.and(application.recordStatus.eq(MDMRecordStatus.ACTIVE)))
				.uniqueResult(application);
	}


	private JPAQuery createSortOrder(PaginationInfo pageInfo,JPAQuery query,QApplicationBase application ) {
		switch (pageInfo.getSortAttribute()) {

		case FAMILY_ID:
			query  = pageInfo.isAsc() ? query.orderBy(application.familyId.asc()) : query.orderBy(application.familyId.desc());
			return query;
		case JURISDICTION:
			query  = pageInfo.isAsc() ? query.orderBy(application.jurisdiction.code.asc()) : query.orderBy(application.jurisdiction.code.desc());
			return query;
		case APPLICATION_NO:
			query  = pageInfo.isAsc() ? query.orderBy(application.jurisdiction.code.asc()) : query.orderBy(application.jurisdiction.code.desc());
			return query;
		case FILING_DATE:
			query  = pageInfo.isAsc() ? query.orderBy(application.appDetails.filingDate.asc() , application.createdDate.asc()) : query.orderBy(application.appDetails.filingDate.desc() , application.createdDate.desc());
			return query;
		case ASSIGNEE:
			query  = pageInfo.isAsc() ? query.orderBy(application.assignee.name.asc()) : query.orderBy(application.assignee.name.desc());
			return query;
		case APPLICATION_TYPE:
			query  = pageInfo.isAsc() ? query.orderBy(application.appDetails.childApplicationType.asc()) : query.orderBy(application.appDetails.childApplicationType.desc());
			return query;
		case ATTORNEY_DOCKET_NO:
			query  = pageInfo.isAsc() ? query.orderBy(application.attorneyDocketNumber.segment.asc()) : query.orderBy(application.attorneyDocketNumber.segment.desc());
			return query;
		case LAST_EDITED_BY:
			query  = pageInfo.isAsc() ? query.orderBy(application.updatedDate.asc()) : query.orderBy(application.updatedDate.desc());
			return query;
		case CURRENT_STATUS:
			query  = pageInfo.isAsc() ? query.orderBy(application.recordStatus.asc()) : query.orderBy(application.recordStatus.desc());
			return query;
		case REQUESTED_FOR:
			query  = pageInfo.isAsc() ? query.orderBy(application.newRecordStatus.asc()) : query.orderBy(application.newRecordStatus.desc());
			return query;
		default:
			return query;
		}
	}

	private JPAQuery createSortOrder(PaginationInfo pageInfo,JPAQuery query, QCreateApplicationQueue applicationQueue) {
		switch (pageInfo.getSortAttribute()) {

		case JURISDICTION:
			query  = pageInfo.isAsc() ? query.orderBy(applicationQueue.jurisdictionCode.asc()) : query.orderBy(applicationQueue.jurisdictionCode.desc());
			return query;

		case APPLICATION_NO:
			query  = pageInfo.isAsc() ? query.orderBy(applicationQueue.applicationNumberFormatted.asc()) : query.orderBy(applicationQueue.applicationNumberFormatted.desc());
			return query;

		default:
			return query;
		}
	}


	private JPAQuery createSortOrder(PaginationInfo pageInfo,JPAQuery query,QNotificationProcess notification) {
		switch (pageInfo.getSortAttribute()) {

		case NOTIFIED_ON:
			query  = pageInfo.isAsc() ? query.orderBy(notification.notifiedDate.asc()) : query.orderBy(notification.notifiedDate.desc());
			return query;

		default:
			return query;
		}
	}

	private JPAQuery createSortOrder(PaginationInfo pageInfo,JPAQuery query,QUser user) {
		switch (pageInfo.getSortAttribute()) {

		case CREATED_BY:
			query  = pageInfo.isAsc() ? query.orderBy(user.person.firstName.asc()) : query.orderBy(user.person.firstName.desc());
			return query;

		case REQUESTED_BY:
			query  = pageInfo.isAsc() ? query.orderBy(user.updatedDate.asc()) : query.orderBy(user.updatedDate.desc());
			return query;

		case SENT_BY:
			query  = pageInfo.isAsc() ? query.orderBy(user.person.firstName.asc()) : query.orderBy(user.person.firstName.desc());
			return query;

		default:
			return query;
		}
	}

	private BooleanBuilder getExtendedSearchPredicate(QApplicationBase application, QUser user, ActiveRecordsFilter filter)
	{

		BooleanBuilder predicate = new BooleanBuilder();

		if(!org.springframework.util.StringUtils.isEmpty(filter.getApplicationNo()))
		{
			predicate = predicate.and(application.applicationNumber.startsWithIgnoreCase(filter.getApplicationNo()));
		}
		if(!org.springframework.util.StringUtils.isEmpty(filter.getJurisdiction()))
		{
			predicate = predicate.and(application.jurisdiction.code.equalsIgnoreCase(filter.getJurisdiction()));
		}
		if(!org.springframework.util.StringUtils.isEmpty(filter.getAttorneyDocketNo()))
		{
			predicate = predicate.and(application.attorneyDocketNumber.segment.startsWithIgnoreCase(filter.getAttorneyDocketNo()));
		}
		if(!org.springframework.util.StringUtils.isEmpty(filter.getFamilyId()))
		{
			predicate = predicate.and(application.familyId.startsWithIgnoreCase(filter.getFamilyId()));
		}
		if(!org.springframework.util.StringUtils.isEmpty(filter.getDescription()))
		{
			predicate = predicate.and(application.description.startsWithIgnoreCase(filter.getDescription()));
		}
		if(!org.springframework.util.StringUtils.isEmpty(filter.getUploadedBy()))
		{
			predicate = predicate.and(user.person.firstName.startsWithIgnoreCase(filter.getUploadedBy()));
		}
		if(filter.getUploadStartDate()!=null || filter.getUploadEndDate()!=null)
		{
			Calendar startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getUploadStartDate());
			Calendar endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getUploadEndDate());

			if(startFillingDate!=null || endFillingDate!=null) {
				predicate.and(application.createdDate.between(startFillingDate, endFillingDate));
			}
		}
		return predicate;
	}

	@Override
	public SearchResult<MdmRecordDTO> allFamilyApplications(ActiveRecordsFilter filter , String familyId, long appId, String viewName) {

		QApplicationBase application = QApplicationBase.applicationBase;
		QUser user = QUser.user;
		JPAQuery query = getJPAQuery().from(application, user);	//TODO .where(dataAccessRules)
		BooleanBuilder predicate = new BooleanBuilder();
		long totalRecords = 0L;
		List<MdmRecordDTO> items = null;
		if("ACTIVE".equals(viewName)) {
			predicate.and(application.createdByUser.eq(user.id))
			.and(application.familyId.eq(familyId).and(application.id.ne(appId)));

			totalRecords = query.where(predicate).count();

			items = query
					.list(ConstructorExpression.create(MdmRecordDTO.class, application.id,
							application.jurisdiction.code, application.applicationNumber, application.familyId,
							application.attorneyDocketNumber.segment, application.appDetails.filingDate,
							application.assignee.name, application.appDetails.childApplicationType, application.createdDate,
							user.person.firstName , user.person.lastName));
		}else if("INACTIVE".equals(viewName)) {

			predicate.and(application.updatedByUser.eq(user.id))
			.and(application.familyId.eq(familyId).and(application.id.ne(appId)));


			totalRecords = query.where(predicate).count();

			items = query
					.list(ConstructorExpression.create(MdmRecordDTO.class, application.id,application.jurisdiction.code, application.applicationNumber
							, application.familyId, application.attorneyDocketNumber.segment
							, application.appDetails.filingDate, application.assignee.name
							, application.appDetails.childApplicationType, application.updatedDate, user.person.firstName , user.person.lastName
							,application.recordStatus,application.newRecordStatus,application.userComment));
		}

		long filteredResults = query.count();
		SearchResult<MdmRecordDTO> result = new SearchResult<>(totalRecords, filteredResults, items);
		return result;
	}


	@Override
	public int countDeactivateAffectedTransactions(Long recordId) {
		int transactionCount = 0;
		return countAffectedTransactions(transactionCount, recordId);
	}

	private int countAffectedTransactions(int transactionCount, Long recordId){
		ApplicationBase baseObject = getApplicationBase(recordId);
		transactionCount = transactionCount + 1;
		Set<CorrespondenceBase> correspondenceBaseSet = baseObject.getCorrespondenceBase();
		if(correspondenceBaseSet!=null){
			transactionCount = transactionCount + correspondenceBaseSet.size();
			for(CorrespondenceBase correspondenceBase : correspondenceBaseSet){
				Set<ReferenceBaseData> refrenceBaseSet = correspondenceBase.getRefrenceBaseData();
				if(refrenceBaseSet!=null){
					transactionCount = transactionCount + refrenceBaseSet.size();
				}
			}
		}
		return transactionCount;
	}

	@Override
	public int countDeleteAffectedTransactions(List<Long> recordIdSet) {
		int transactionCount = 0;
		for(Long recordId : recordIdSet){
			transactionCount = countAffectedTransactions(transactionCount, recordId);
		}
		return transactionCount;
	}

	@Override
	public boolean updateStatusToDeactivate(Long recordId, MDMRecordStatus currentRecordStatus) {
		return updateRecordStatus(recordId, currentRecordStatus);
	}

	@Override
	public boolean updateStatusToDropped(List<Long> recordIdSet, MDMRecordStatus currentRecordStatus) {
		boolean updateStatus = false;
		for(Long recordId : recordIdSet){
			updateStatus = updateRecordStatus(recordId, currentRecordStatus);
		}
		return updateStatus;
	}

	private boolean updateRecordStatus(Long recordId, MDMRecordStatus currentRecordStatus){
		updateApplicationBase(recordId, currentRecordStatus);
		ApplicationBase baseObject = getApplicationBase(recordId);
		List<Long> correspondenceIdList = new ArrayList<Long>();
		List<Long> refrenceIdList = new ArrayList<Long>();
		Set<CorrespondenceBase> correspondenceBaseSet = baseObject.getCorrespondenceBase();
		for(CorrespondenceBase correspondenceBase : correspondenceBaseSet){
			correspondenceIdList.add(correspondenceBase.getId());
		}
		if(!correspondenceIdList.isEmpty()){
			updateCorrespondenceStatus(correspondenceIdList);
		}

		if(correspondenceBaseSet!=null){
			for(CorrespondenceBase correspondenceBase : correspondenceBaseSet){
				Set<ReferenceBaseData> refrenceBaseSet = correspondenceBase.getRefrenceBaseData();
				for(ReferenceBaseData referenceBaseData : refrenceBaseSet){
					refrenceIdList.add(referenceBaseData.getReferenceBaseDataId());
				}
			}
		}

		if(!refrenceIdList.isEmpty()){
			updateRefrenceBaseStatus(refrenceIdList);
		}
		return true;
	}

	private void updateApplicationBase(Long recordId, MDMRecordStatus currentRecordStatus){
		String updateHql = "UPDATE ApplicationBase a set a.recordStatus=:recordStatus "  + "WHERE a.id=:id";
		Query query = getEntityManager().createQuery(updateHql);
		query.setParameter("recordStatus", currentRecordStatus);
		query.setParameter("id",recordId);
		query.executeUpdate();
	}

	private void updateCorrespondenceStatus(List<Long> correspondenceId){
		String updateHql = "UPDATE CorrespondenceBase c set c.status=:status "  + "WHERE c.id IN :id";
		Query query = getEntityManager().createQuery(updateHql);
		query.setParameter("status",Status.DROPPED);
		query.setParameter("id", correspondenceId);
		query.executeUpdate();
	}

	private void updateRefrenceBaseStatus(List<Long> referenceId){
		String updateHql = "UPDATE ReferenceBaseData r set r.referenceStatus=:referenceStatus, r.referenceFlowFlag=:referenceFlowFlag "  + "WHERE r.id IN :id";
		Query query = getEntityManager().createQuery(updateHql);
		query.setParameter("referenceFlowFlag", Boolean.FALSE);
		query.setParameter("referenceStatus",ReferenceStatus.DROPPED);
		query.setParameter("id", referenceId);
		query.executeUpdate();
	}

	@Override
	public void saveDeactivatedRecordToExclusionList(ExclusionList exclusionList){
		exclusionListRepository.save(exclusionList);
	}

	@Override
	public Long findByPublicationNumberAndJurisdiction(String publicationNumber, String jurisdictionCode) {

		QApplicationBase application = QApplicationBase.applicationBase;
		JPAQuery query = getJPAQuery().from(application);
		query.where(application.publicationDetails.publicationNumber.eq(publicationNumber).and(application.jurisdiction.code.equalsIgnoreCase(jurisdictionCode)));
		Long count = query.count();
		return count;
	}

	@Override
	public boolean exists(String jurisdiction, String applicationNo) {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery().from(application)
				.where(application.jurisdiction.code.equalsIgnoreCase(jurisdiction)
						.and(application.applicationNumber.equalsIgnoreCase(applicationNo))
						.and(application.recordStatus.ne(MDMRecordStatus.DROPPED)))
				.count() > 0L;
	}

	@Override
	public boolean updateExclusionListStatus(ExclusionList exclusionList, ApplicationNumberStatus status) {
		String updateHql = "UPDATE ExclusionList e set e.applications.status =:status "  + "WHERE e.id = :id";
		Query query = getEntityManager().createQuery(updateHql);
		query.setParameter("status", status);
		query.setParameter("id", exclusionList.getId());
		int rowUpdate = query.executeUpdate();
		boolean updateRecord = (rowUpdate>0)? true : false;
		return updateRecord;
	}

	@Override
	public MdmRecordDTO fetchApplicationData(final Long applicationId) {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery().from(application).where(application.id.eq(applicationId))
				.uniqueResult(new QMdmRecordDTO(application));
	}

	@Override
	public AttorneyDocketFormat getAttorneyDocketFormat() {
		TypedQuery<AttorneyDocketFormat> query = getEntityManager().createQuery("SELECT a FROM AttorneyDocketFormat a",AttorneyDocketFormat.class);
		return query.getSingleResult();
	}

	@Override
	public String getAttorneyDocketSeperator() {
		String hql = "select a.separator FROM AttorneyDocketFormat a";
		TypedQuery<String> query = getEntityManager().createQuery(hql,String.class);
		return query.getSingleResult();
	}

	@Override
	public long countFamilyApplications(final String familyId) {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery()
				.from(application)
				.where(application.familyId.equalsIgnoreCase(familyId).and(
						application.recordStatus.ne(MDMRecordStatus.DROPPED))).count();
	}

	@Override
	public List<ApplicationBase> getDefaultAssigneeApplications()
	{
		QApplicationBase application = QApplicationBase.applicationBase;
		List<ApplicationBase> items = Collections.emptyList();
		items = getJPAQuery().from(application).where(application.assignee.id.eq(Assignee.ID_DEFAULT_ASSIGNEE)
				.and(application.appDetails.childApplicationType.in(ApplicationType.FIRST_FILING,ApplicationType.PCT_US_FIRST_FILING)))
				.list(application);
		return items;
	}

	@Override
	public long checkIfNotificationExists(long entityId)
	{
		QNotificationProcess notification = QNotificationProcess.notificationProcess;

		return getJPAQuery().from(notification).where(notification.entityId.eq(entityId).and(notification.notificationProcessType.eq(NotificationProcessType.UPDATE_ASSIGNEE))).count();


	}
	@Override
	public List<AssigneeAttorneyDocketNo> getAssigneeAttorneyDocketNo(String segmentValue) {
		TypedQuery<AssigneeAttorneyDocketNo> query = getEntityManager().createQuery("SELECT a FROM AssigneeAttorneyDocketNo a WHERE a.segmentValueToMatch = :segmentValue", AssigneeAttorneyDocketNo.class).setParameter("segmentValue", segmentValue);
		return query.getResultList();
	}

	@Override
	public long acceptCreateFamilyRequest(Long notificationId) {
		long recordsUpdated = 0L;
		QNotificationProcess notification = QNotificationProcess.notificationProcess;
		Tuple resultSet = getJPAQuery().from(notification)
				.where(notification.notificationProcessId.eq(notificationId))
				.uniqueResult(new QTuple(notification.entityId, notification.entityName));

		Long id = resultSet.get(notification.entityId);
		EntityName entityName = resultSet.get(notification.entityName);
		webCrawlerApplicationDao.updateFindFamilyQueueStatus(id, QueueStatus.SUCCESS);
		return recordsUpdated;
	}

	@Override
	public ApplicationDetailsDTO fetchAppInfoForIDS(long appId) {
		QApplicationBase application = QApplicationBase.applicationBase;
		QIDS ids = QIDS.iDS;

		return getJPAQuery().from(ids).innerJoin(ids.application, application)
				.where(application.id.eq(appId).and(ids.status.in(IDS.Status.IN_PROGRESS, IDS.Status.APPROVED))).singleResult(new QApplicationDetailsDTO(application.id, ids.id, application.applicationNumber,
						application.appDetails.filingDate, application.patentDetails.firstNameInventor,
						application.attorneyDocketNumber.segment, application.patentDetails.artUnit,
						application.patentDetails.examiner,application.familyId,application.jurisdiction.code));
	}
	
	@Override
	public ApplicationDetailsDTO fetchAppInfoForNotification(long appId) {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery().from(application)
				.where(application.id.eq(appId))
				.uniqueResult(ConstructorExpression.create(ApplicationDetailsDTO.class, application.id, application.applicationNumber,
						application.attorneyDocketNumber.segment, application.familyId,application.jurisdiction.code));
	}


	@Override
	public List<ApplicationBase> getApplicationsByProsecutionStatus() {
		QApplicationBase app = QApplicationBase.applicationBase;
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;

		JPAQuery query = getJPAQuery().from(refFlow).innerJoin(refFlow.targetApplication, app).distinct();
		Predicate predicate = new BooleanBuilder()
				.and(app.organizationDetails.prosecutionStatus.in(Arrays.asList(ProsecutionStatus.PUBLISHED,
						ProsecutionStatus.PENDING_PUBLICATION))).and(app.jurisdiction.code.eq(Jurisdiction.Code.US.name()))
				.and(refFlow.referenceFlowStatus.eq(ReferenceFlowStatus.UNCITED));
		query = query.where(predicate);
		return query.list(app);
	}

	@Override
	public Assignee findAssignee(final long applicationId) {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery().from(application)
				.where(application.id.eq(applicationId))
				.uniqueResult(application.assignee);
	}

	@Override
	public String findFamilyOfApplication(final long applicationId) {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery().from(application)
				.where(application.id.eq(applicationId))
				.uniqueResult(application.familyId);
	}
	
	
		@Override
		public List<Long> findFamilyMembers(final long applicationId,final String familyId) {
			QApplicationBase application = QApplicationBase.applicationBase;
			BooleanBuilder predicate = new BooleanBuilder();
			predicate.and(application.recordStatus.notIn(Arrays.asList(MDMRecordStatus.DROPPED,MDMRecordStatus.DEACTIVATED))
					.and(application.id.ne(applicationId)));
			if(!StringUtils.isNullOrEmpty(familyId)) {
				predicate.and(application.familyId.startsWithIgnoreCase(familyId));
			}
			return getJPAQuery().from(application)
					.where(predicate)
					.list(application.id);
		}
		
	@Override
	public List<Long> findAllApplicationIdListOfFamily(final String family, final Long applicationId) {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery().from(application)
				.where(application.id.ne(applicationId).and(application.familyId.eq(family)))
				.list(application.id);
	}

	@Override
	public String findFamilyByApplication(final String jurisdiction, final String applicationNo) {
		QApplicationBase application = QApplicationBase.applicationBase;
		return getJPAQuery().from(application)
				.where(applicationPredicate(application, jurisdiction, applicationNo))
				.uniqueResult(application.familyId);
	}
}