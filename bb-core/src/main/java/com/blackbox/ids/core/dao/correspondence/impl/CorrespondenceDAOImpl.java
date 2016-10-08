package com.blackbox.ids.core.dao.correspondence.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.auth.BlackboxUser;
import com.blackbox.ids.core.common.DataAccessPredicate;
import com.blackbox.ids.core.dao.correspondence.ICorrespondenceDAO;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceBaseKeysDTO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordDTO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordsFilter;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.model.QDocumentCode;
import com.blackbox.ids.core.model.QUser;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.correspondence.QCorrespondenceBase;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.QApplicationBase;
import com.blackbox.ids.core.model.mstr.QJurisdiction;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.QNotificationProcess;
import com.blackbox.ids.core.model.webcrawler.QDownloadOfficeActionQueue;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.query.DateTimeSubQuery;

/**
 * The <code>CorrespondenceDAOImpl</code> provides implementation of {@link ICorrespondenceDAO} for all type of
 * operations related to Correspondence functionality.
 * @author abhay2566
 */
@Repository
public class CorrespondenceDAOImpl extends BaseDaoImpl<CorrespondenceBase, Long>implements ICorrespondenceDAO {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(CorrespondenceDAOImpl.class);

	/**
	 * It will fetch the Active Documents based on filter.
	 * @param filter
	 *            the filter required for search i.e. userId, date search
	 * @param pageInfo
	 *            it will provide the pagination information
	 * @return SearchResult<CorrespondenceRecordDTO> it will result the list of CorrespondenceRecordDTO to be displayed
	 *         on UI.
	 */
	@Override
	public SearchResult<CorrespondenceRecordDTO> filterActiveDocuments(final CorrespondenceRecordsFilter filter,
			final PaginationInfo pageInfo) {
		Long userId = filter.getOwnedBy();
		Calendar startMailingDate = null;
		Calendar endMailingDate = null;
		List<CorrespondenceRecordDTO> items = new ArrayList<>();
		long fileteredResults = 0;
		if (filter.getStartDate() != null || filter.getEndDate() != null) {
			startMailingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endMailingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}

		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;
		QApplicationBase app = QApplicationBase.applicationBase;
		QCorrespondenceBase corr2 = new QCorrespondenceBase("corr2");
		QDocumentCode documentCode = QDocumentCode.documentCode;
		QUser user = QUser.user;

		JPAQuery query = getJPAQuery().from(correspondence, user, documentCode).innerJoin(correspondence.application,
				app);

		BooleanBuilder predicate = new BooleanBuilder().and(correspondence.documentCode.eq(documentCode))
				.and(user.id.eq(correspondence.createdByUser)).and(correspondence.status.eq(Status.ACTIVE));

		BooleanBuilder subQueryPredicate = new BooleanBuilder().and(corr2.status.eq(Status.ACTIVE))
				.and(DataAccessPredicate.getDataAccessPredicate(app))
				.and(correspondence.application.eq(corr2.application));

		if (userId != null) {
			predicate.and(correspondence.updatedByUser.eq(userId));
			subQueryPredicate.and(corr2.updatedByUser.eq(userId));
		}

		if (startMailingDate != null || endMailingDate != null) {
			subQueryPredicate.and(corr2.mailingDate.between(startMailingDate, endMailingDate));
		}

		DateTimeSubQuery<Calendar> subQuery = new JPASubQuery().from(corr2).where(subQueryPredicate)
				.unique(corr2.mailingDate.max());

		predicate.and(getExtendedSearchPredicate(app, correspondence, user, filter));
		subQueryPredicate.and(getExtendedSearchPredicate(app, correspondence, user, filter));
		query = createSortOrder(pageInfo, query, correspondence, documentCode);

		Map<Long, List<CorrespondenceRecordDTO>> resultSet = query
				.where((predicate).and(correspondence.mailingDate.eq(subQuery))).offset(pageInfo.getOffset())
				.limit(pageInfo.getLimit())
				.transform(com.mysema.query.group.GroupBy.groupBy(correspondence.application.id)
						.as(com.mysema.query.group.GroupBy.list(ConstructorExpression.create(
								CorrespondenceRecordDTO.class, correspondence.id, app.id, app.jurisdiction.code,
								app.applicationNumber, correspondence.mailingDate, documentCode.description,
								correspondence.createdByUser, correspondence.status, user.person.firstName,
								user.person.lastName, correspondence.comment, correspondence.createdDate,
								app.organizationDetails.exportControl, user.person.nationality.value))));

		if (MapUtils.isNotEmpty(resultSet)) {
			for (Map.Entry<Long, List<CorrespondenceRecordDTO>> entry : resultSet.entrySet()) {
				items.add(entry.getValue().get(0));
			}
		}
		fileteredResults = items.size();
		SearchResult<CorrespondenceRecordDTO> result = new SearchResult<>(fileteredResults, items);
		return result;
	}

	/**
	 * It will fetch the InActive Documents based on filter.
	 * @param filter
	 *            the filter required for search i.e. userId, date search
	 * @param pageInfo
	 *            it will provide the pagination information
	 * @return SearchResult<CorrespondenceRecordDTO> it will result the list of CorrespondenceRecordDTO to be displayed
	 *         on UI.
	 */
	@Override
	public SearchResult<CorrespondenceRecordDTO> filterInActiveDocuments(final CorrespondenceRecordsFilter filter,
			final PaginationInfo pageInfo) {

		Calendar startMailingDate = null;
		Calendar endMailingDate = null;
		List<CorrespondenceRecordDTO> items = new ArrayList<>();
		long filteredResults = 0;

		if (filter.getStartDate() != null || filter.getEndDate() != null) {
			startMailingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endMailingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}

		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;
		QApplicationBase app = QApplicationBase.applicationBase;
		QCorrespondenceBase corr2 = new QCorrespondenceBase("corr2");
		QDocumentCode documentCode = QDocumentCode.documentCode;

		QUser user = QUser.user;

		JPAQuery query = getJPAQuery().from(correspondence, user, documentCode).innerJoin(correspondence.application,
				app);

		BooleanBuilder predicate = new BooleanBuilder().and(user.id.eq(correspondence.updatedByUser))
				.and(correspondence.documentCode.eq(documentCode)).and(correspondence.status.eq(Status.DROPPED));

		BooleanBuilder subQueryPredicate = new BooleanBuilder().and(DataAccessPredicate.getDataAccessPredicate(app))
				.and(corr2.status.eq(Status.DROPPED)).and(correspondence.application.eq(corr2.application));

		if (startMailingDate != null || endMailingDate != null) {
			subQueryPredicate.and(corr2.mailingDate.between(startMailingDate, endMailingDate));
		}

		DateTimeSubQuery<Calendar> subQuery = new JPASubQuery().from(corr2).where(subQueryPredicate)
				.unique(corr2.mailingDate.max());

		predicate.and(getExtendedSearchPredicate(app, correspondence, user, filter));
		subQueryPredicate.and(getExtendedSearchPredicate(app, correspondence, user, filter));
		query = createSortOrder(pageInfo, query, correspondence, documentCode);

		Map<Long, List<CorrespondenceRecordDTO>> resultSet = query
				.where((predicate).and(correspondence.mailingDate.eq(subQuery))).offset(pageInfo.getOffset())
				.limit(pageInfo.getLimit()).transform(
						com.mysema.query.group.GroupBy.groupBy(correspondence.application.id)
								.as(com.mysema.query.group.GroupBy.list(ConstructorExpression.create(
										CorrespondenceRecordDTO.class, correspondence.id, app.id, app.jurisdiction.code,
										app.applicationNumber, correspondence.mailingDate, documentCode.description,
										correspondence.status, user.person.firstName, user.person.lastName,
										correspondence.comment, correspondence.updatedDate,
										app.organizationDetails.exportControl, user.person.nationality.value))));

		if (MapUtils.isNotEmpty(resultSet)) {
			for (Map.Entry<Long, List<CorrespondenceRecordDTO>> entry : resultSet.entrySet()) {
				items.add(entry.getValue().get(0));
			}
		}
		filteredResults = items.size();
		SearchResult<CorrespondenceRecordDTO> result = new SearchResult<>(filteredResults, items);
		return result;

	}

	/**
	 * It will fetch the single Correspondence Record.
	 * @param correspondenceId
	 *            the database Id of the record to be fetched.
	 * @return CorrespondenceRecordDTO it will return the CorrespondenceRecordDTO to be displayed on UI.
	 */
	@Override
	public CorrespondenceRecordDTO getCorrespondenceRecord(final Long correspondenceId) {

		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;
		QUser user = QUser.user;
		JPAQuery query = getJPAQuery().from(correspondence, user)
				.where(correspondence.id.eq(correspondenceId).and(user.id.eq(correspondence.createdByUser)));
		CorrespondenceRecordDTO correspondenceRecordDTO = query.uniqueResult(ConstructorExpression.create(
				CorrespondenceRecordDTO.class, correspondence.id, correspondence.application.id,
				correspondence.application.jurisdiction.code, correspondence.application.applicationNumber,
				correspondence.mailingDate, correspondence.documentCode.description,
				correspondence.application.organizationDetails.exportControl, user.person.nationality.value));
		return correspondenceRecordDTO;

	}

	/**
	 * It will soft delete i.e Update the correspondence Record with Dropped status.
	 * @param correspondenceId
	 *            the database Id of the record to be deleted.
	 * @param comments
	 *            the comments to be updated in db for deletion.
	 * @return noting.
	 */
	@Override
	public long deleteCorrespondenceRecord(final Long correspondenceId, final String comments) {
		Long updatedBy = BlackboxSecurityContextHolder.getUserId();
		Calendar updatedDate = Calendar.getInstance();
		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;

		JPAUpdateClause updateClause = getJPAUpdateClause(correspondence);
		updateClause.where(correspondence.id.eq(correspondenceId)).set(correspondence.comment, comments)
				.set(correspondence.status, Status.DROPPED).set(correspondence.updatedByUser, updatedBy)
				.set(correspondence.updatedDate, updatedDate);

		long rowUpdate = updateClause.execute();
		return rowUpdate;
	}

	/**
	 * It will fetch the Active Correspondences of a particular Application Number and Jurisdiction.
	 * @param filter
	 *            the filter required for search i.e. userId, date search
	 * @param correspondenceId
	 *            the correspondence record not to be included in this returned list
	 * @return SearchResult<CorrespondenceRecordDTO> it will result the list of CorrespondenceRecordDTO to be displayed
	 *         on UI.
	 */
	@Override
	public SearchResult<CorrespondenceRecordDTO> getActiveCorrespondences(final CorrespondenceRecordsFilter filter,
			final Long correspondenceId) {

		Long userId = filter.getOwnedBy();
		long applicationId = getJurisdictionAndApplication(correspondenceId);

		Calendar startMailingDate = null;
		Calendar endMailingDate = null;

		List<CorrespondenceRecordDTO> items = new ArrayList<>();
		long fileteredResults = 0;

		if (filter.getStartDate() != null || filter.getEndDate() != null) {
			startMailingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endMailingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}

		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;
		QApplicationBase app = QApplicationBase.applicationBase;
		QUser user = QUser.user;

		JPAQuery query = getJPAQuery().from(correspondence, user).innerJoin(correspondence.application, app);
		BooleanBuilder condition = new BooleanBuilder().and(DataAccessPredicate.getDataAccessPredicate(app))
				.and(user.id.eq(correspondence.createdByUser));

		if (userId != null) {
			condition.and(correspondence.updatedByUser.eq(userId));
		}

		if (startMailingDate != null || endMailingDate != null) {
			condition.and(correspondence.mailingDate.between(startMailingDate, endMailingDate));
		}
		condition.and(correspondence.application.id.eq(applicationId)).and(correspondence.id.ne(correspondenceId));
		condition.and(getExtendedSearchPredicate(app, correspondence, user, filter));

		items = query.where(condition).orderBy(correspondence.mailingDate.desc())
				.list(ConstructorExpression.create(CorrespondenceRecordDTO.class, correspondence.id,
						correspondence.application.id, correspondence.application.jurisdiction.code,
						correspondence.application.applicationNumber, correspondence.mailingDate,
						correspondence.documentCode.description, correspondence.createdByUser, correspondence.status,
						user.person.firstName, user.person.lastName, correspondence.comment, correspondence.createdDate,
						correspondence.application.organizationDetails.exportControl, user.person.nationality.value));

		fileteredResults = query.count();

		SearchResult<CorrespondenceRecordDTO> result = new SearchResult<>(fileteredResults, items);

		return result;
	}

	/**
	 * It will fetch the Inactive Correspondences of a particular Application Number and Jurisdiction.
	 * @param filter
	 *            the filter required for search i.e. userId, date search
	 * @param correspondenceId
	 *            the correspondence record not to be included in this returned list
	 * @return SearchResult<CorrespondenceRecordDTO> it will result the list of CorrespondenceRecordDTO to be displayed
	 *         on UI.
	 */
	@Override
	public SearchResult<CorrespondenceRecordDTO> getInactiveCorrespondences(final CorrespondenceRecordsFilter filter,
			final Long correspondenceId) {

		long applicationId = getJurisdictionAndApplication(correspondenceId);
		Calendar startMailingDate = null;
		Calendar endMailingDate = null;

		List<CorrespondenceRecordDTO> items = new ArrayList<>();
		long fileteredResults = 0;

		if (filter.getStartDate() != null || filter.getEndDate() != null) {
			startMailingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endMailingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}

		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;
		QApplicationBase app = QApplicationBase.applicationBase;
		QUser user = QUser.user;

		JPAQuery query = getJPAQuery().from(correspondence, user).innerJoin(correspondence.application, app);

		BooleanBuilder predicate = new BooleanBuilder().and(DataAccessPredicate.getDataAccessPredicate(app))
				.and(user.id.eq(correspondence.updatedByUser)).and(correspondence.status.eq(Status.DROPPED));

		if (startMailingDate != null || endMailingDate != null) {
			predicate.and(correspondence.mailingDate.between(startMailingDate, endMailingDate));
		}

		predicate.and(getExtendedSearchPredicate(app, correspondence, user, filter));
		predicate.and(correspondence.application.id.eq(applicationId)).and(correspondence.id.ne(correspondenceId));

		items = query.where(predicate).orderBy(correspondence.mailingDate.desc())
				.list(ConstructorExpression.create(CorrespondenceRecordDTO.class, correspondence.id,
						correspondence.application.id, correspondence.application.jurisdiction.code,
						correspondence.application.applicationNumber, correspondence.mailingDate,
						correspondence.documentCode.description, correspondence.status, user.person.firstName,
						user.person.lastName, correspondence.comment, correspondence.updatedDate,
						correspondence.application.organizationDetails.exportControl, user.person.nationality.value));
		fileteredResults = query.count();

		SearchResult<CorrespondenceRecordDTO> result = new SearchResult<>(fileteredResults, items);

		return result;
	}

	/**
	 * Gets the jurisdiction and application.
	 * @param correspondenceId
	 *            the correspondence id
	 * @return the jurisdiction and application
	 */
	private Long getJurisdictionAndApplication(Long correspondenceId) {

		QCorrespondenceBase base = QCorrespondenceBase.correspondenceBase;
		JPAQuery jpaQuery = getJPAQuery().from(base).where(base.id.eq(correspondenceId));
		long applicationId = jpaQuery.uniqueResult(base.application.id);
		return applicationId;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.blackbox.ids.core.dao.correspondence.ICorrespondenceDAO#getCorrespondenceRecordByDuplicateCheckParameters(
	 * java.lang.Long, java.lang.Long, java.util.Calendar, java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<CorrespondenceBase> getCorrespondenceRecordByDuplicateCheckParameters(Long applicationId,
			Long documentId, Calendar mailingDate, Long size, Integer pageCount) {

		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;
		JPAQuery query = getJPAQuery().from(correspondence)
				.where(correspondence.application.id.eq(applicationId).and(correspondence.pageCount.eq(pageCount))
						.and(correspondence.attachmentSize.eq(size)).and(correspondence.mailingDate.eq(mailingDate))
						.and(correspondence.status.ne(Status.DROPPED)));
		List<CorrespondenceBase> base = query.list(correspondence);
		return base;

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.blackbox.ids.core.dao.correspondence.ICorrespondenceDAO#fetchUpdateRequestRecords(com.blackbox.ids.core.dto.
	 * correspondence.dashboard.CorrespondenceRecordsFilter, com.blackbox.ids.core.dto.datatable.PaginationInfo)
	 */
	@Override
	public SearchResult<CorrespondenceRecordDTO> fetchUpdateRequestRecords(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo) {
		LOGGER.debug("Fetch Update Request Records in Correspondence");
		long totalRecords = 0;
		long filteredResults = 0;
		List<CorrespondenceRecordDTO> items = new ArrayList<>();
		BooleanBuilder dataAccessPredicate = null;
		BooleanBuilder predicate = null;
		Calendar startFillingDate = null;
		Calendar endFillingDate = null;
		if (filter.getStartDate() != null || filter.getEndDate() != null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}

		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;
		dataAccessPredicate = DataAccessPredicate.getDataAccessPredicate(correspondence.application);
		if (dataAccessPredicate != null) {
			JPAQuery query = getJPAQuery().from(correspondence);
			query.where(dataAccessPredicate);
			query.where(correspondence.status.eq(Status.ERROR));
			totalRecords = query.count();

			if (startFillingDate != null || endFillingDate != null) {
				predicate = new BooleanBuilder();
				predicate.and((correspondence.mailingDate.between(startFillingDate, endFillingDate)
						.or(correspondence.updatedDate.between(startFillingDate, endFillingDate))));
			}

			query.where(predicate);
			filteredResults = query.count();
			query = createSortOrder(pageInfo, query, correspondence);

			items = query.offset(pageInfo.getOffset()).limit(pageInfo.getLimit())
					.list(ConstructorExpression.create(CorrespondenceRecordDTO.class, correspondence.id,
							correspondence.application.jurisdiction.code, correspondence.application.applicationNumber,
							correspondence.mailingDate, correspondence.documentCode.description,
							correspondence.updatedDate, correspondence.ocrStatus));
		}
		SearchResult<CorrespondenceRecordDTO> result = new SearchResult<>(totalRecords, filteredResults, items);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.blackbox.ids.core.dao.correspondence.ICorrespondenceDAO#fetchUploadRequestRecords(com.blackbox.ids.core.dto.
	 * correspondence.dashboard.CorrespondenceRecordsFilter, com.blackbox.ids.core.dto.datatable.PaginationInfo)
	 */
	@Override
	public long fetchUpdateRequestCount() {
		LOGGER.debug("Fetch Update Request Records count in Correspondence");
		long totalRecords = 0;
		BooleanBuilder dataAccessPredicate = null;
		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;
		dataAccessPredicate = DataAccessPredicate.getDataAccessPredicate(correspondence.application);
		if (dataAccessPredicate != null) {
			JPAQuery query = getJPAQuery().from(correspondence);
			query.where(dataAccessPredicate); // Data Access
			query.where(correspondence.status.eq(Status.ERROR));
			totalRecords = query.count();
		}
		return totalRecords;
	}

	@Override
	public long fetchUploadRequestCount() {
		LOGGER.debug("Fetch Upload Request Records count in Correspondence");
		long totalRecords = 0;
		Set<Long> userRoles = null;
		BlackboxUser blackboxUser = BlackboxSecurityContextHolder.getUserDetails();
		if (blackboxUser != null) {
			userRoles = blackboxUser.getRoleIds();
		}

		if (!CollectionUtils.isEmpty(userRoles)) {
			QDocumentCode documentCode = QDocumentCode.documentCode;
			QNotificationProcess notificationprocess = QNotificationProcess.notificationProcess;
			QDownloadOfficeActionQueue downloadOfficeActionQueue = QDownloadOfficeActionQueue.downloadOfficeActionQueue;
			JPAQuery query = getJPAQuery().from(notificationprocess, downloadOfficeActionQueue, documentCode);

			query.where(notificationprocess.notificationProcessType
					.eq(NotificationProcessType.CORRESPONDENCE_RECORD_FAILED_IN_DOWNLOAD_QUEUE)
					.and(notificationprocess.entityName.eq(EntityName.DOWNLOAD_OFFICE_ACTION_QUEUE))
					.and(notificationprocess.active.eq(true)).and(notificationprocess.roles.any().id.in(userRoles))
					.and(notificationprocess.entityId.eq(downloadOfficeActionQueue.id))
					.and(downloadOfficeActionQueue.status.eq(QueueStatus.CRAWLER_ERROR))
					.and(downloadOfficeActionQueue.documentCode.eq(documentCode.code)));
			totalRecords = query.count();
		}
		return totalRecords;
	}

	@Override
	public SearchResult<CorrespondenceRecordDTO> fetchUploadRequestRecords(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo) {
		LOGGER.debug("Fetch Upload Request Records in Correspondence");
		long totalRecords = 0;
		long filteredResults = 0;
		List<CorrespondenceRecordDTO> items = new ArrayList<>();
		BooleanBuilder predicate = null;
		Set<Long> userRoles = null;
		Calendar startFillingDate = null;
		Calendar endFillingDate = null;
		if (filter.getStartDate() != null || filter.getEndDate() != null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}

		BlackboxUser blackboxUser = BlackboxSecurityContextHolder.getUserDetails();
		if (blackboxUser != null) {
			userRoles = blackboxUser.getRoleIds();
		}

		if (!CollectionUtils.isEmpty(userRoles)) {
			QDocumentCode documentCode = QDocumentCode.documentCode;
			QNotificationProcess notificationprocess = QNotificationProcess.notificationProcess;
			QDownloadOfficeActionQueue downloadOfficeActionQueue = QDownloadOfficeActionQueue.downloadOfficeActionQueue;
			JPAQuery query = getJPAQuery().from(notificationprocess, downloadOfficeActionQueue, documentCode);

			query.where(notificationprocess.notificationProcessType
					.eq(NotificationProcessType.CORRESPONDENCE_RECORD_FAILED_IN_DOWNLOAD_QUEUE)
					.and(notificationprocess.entityName.eq(EntityName.DOWNLOAD_OFFICE_ACTION_QUEUE))
					.and(notificationprocess.active.eq(true)).and(notificationprocess.roles.any().id.in(userRoles))
					.and(notificationprocess.entityId.eq(downloadOfficeActionQueue.id))
					.and(downloadOfficeActionQueue.status.eq(QueueStatus.CRAWLER_ERROR))
					.and(downloadOfficeActionQueue.documentCode.eq(documentCode.code)));
			totalRecords = query.count();

			if (startFillingDate != null || endFillingDate != null) {
				predicate = new BooleanBuilder();
				predicate.and((downloadOfficeActionQueue.mailingDate.between(startFillingDate, endFillingDate)
						.or(notificationprocess.updatedDate.between(startFillingDate, endFillingDate))));
			}

			query.where(predicate);
			filteredResults = query.count();

			query = createUploadRequestSortOrder(pageInfo, query, notificationprocess, downloadOfficeActionQueue,
					documentCode);

			items = query.offset(pageInfo.getOffset()).limit(pageInfo.getLimit())
					.list(ConstructorExpression.create(CorrespondenceRecordDTO.class,
							notificationprocess.notificationProcessId, downloadOfficeActionQueue.jurisdictionCode,
							downloadOfficeActionQueue.applicationNumberFormatted, downloadOfficeActionQueue.mailingDate,
							documentCode.description, notificationprocess.notifiedDate));
		}
		SearchResult<CorrespondenceRecordDTO> result = new SearchResult<>(totalRecords, filteredResults, items);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.blackbox.ids.core.dao.correspondence.ICorrespondenceDAO#getCorrespondenceRecordDTO(java.lang.Long)
	 */
	@Override
	public CorrespondenceRecordDTO getCorrespondenceRecordDTO(Long correspondenceId) {

		BooleanBuilder dataAccessPredicate = null;
		CorrespondenceRecordDTO correspondenceRecordDTO = null;
		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;
		JPAQuery query = getJPAQuery().from(correspondence);
		dataAccessPredicate = DataAccessPredicate.getDataAccessPredicate(correspondence.application);
		if (dataAccessPredicate != null) {
			query.where(dataAccessPredicate);
			query.where(correspondence.id.eq(correspondenceId));
			correspondenceRecordDTO = query.uniqueResult(ConstructorExpression.create(CorrespondenceRecordDTO.class,
					correspondence.id, correspondence.application.jurisdiction.code,
					correspondence.application.applicationNumber, correspondence.mailingDate,
					correspondence.documentCode.description, correspondence.updatedDate, correspondence.ocrStatus));
		}
		return correspondenceRecordDTO;
	}

	/**
	 * Creates the sort order.
	 * @param pageInfo
	 *            the page info
	 * @param query
	 *            the query
	 * @param correspondenceBase
	 *            the correspondence base
	 * @return the JPA query
	 */
	private JPAQuery createSortOrder(PaginationInfo pageInfo, JPAQuery query, QCorrespondenceBase correspondenceBase) {
		switch (pageInfo.getSortAttribute()) {

		case CORR_ACTION_ITEM_JURISDICTION:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.application.jurisdiction.code.asc())
					: query.orderBy(correspondenceBase.application.jurisdiction.code.desc());
			return query;
		case CORR_ACTION_ITEM_APPLICATION_NO:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.application.applicationNumber.asc())
					: query.orderBy(correspondenceBase.application.applicationNumber.desc());
			return query;
		case CORR_ACTION_ITEM_MAILING_DATE:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.mailingDate.asc())
					: query.orderBy(correspondenceBase.mailingDate.desc());
			return query;
		case CORR_ACTION_ITEM_DOCUMENT_DESCRIPTION:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.documentCode.description.asc())
					: query.orderBy(correspondenceBase.documentCode.description.desc());
			return query;
		case CORR_ACTION_ITEM_NOTIFIED:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.updatedDate.asc())
					: query.orderBy(correspondenceBase.updatedDate.desc());
			return query;
		case CORR_ACTION_ITEM_STATUS:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.ocrStatus.asc())
					: query.orderBy(correspondenceBase.ocrStatus.desc());
			return query;
		case CREATED_BY:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.createdByUser.asc())
					: query.orderBy(correspondenceBase.createdByUser.desc());
			return query;
		case LAST_EDITED_BY:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.updatedDate.asc())
					: query.orderBy(correspondenceBase.updatedDate.desc());
			return query;
		default:
			return query;
		}
	}

	/**
	 * Creates the sort order.
	 * @param pageInfo
	 *            the page info
	 * @param query
	 *            the query
	 * @param correspondenceBase
	 *            the correspondence base
	 * @param documentCode
	 *            the document code
	 * @return the JPA query
	 */
	private JPAQuery createSortOrder(PaginationInfo pageInfo, JPAQuery query, QCorrespondenceBase correspondenceBase,
			QDocumentCode documentCode) {
		switch (pageInfo.getSortAttribute()) {

		case CORR_ACTION_ITEM_JURISDICTION:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.application.jurisdiction.code.asc())
					: query.orderBy(correspondenceBase.application.jurisdiction.code.desc());
			return query;
		case CORR_ACTION_ITEM_APPLICATION_NO:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.application.applicationNumber.asc())
					: query.orderBy(correspondenceBase.application.applicationNumber.desc());
			return query;
		case CORR_ACTION_ITEM_MAILING_DATE:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.mailingDate.asc())
					: query.orderBy(correspondenceBase.mailingDate.desc());
			return query;
		case CORR_ACTION_ITEM_DOCUMENT_DESCRIPTION:
			query = pageInfo.isAsc() ? query.orderBy(documentCode.description.asc())
					: query.orderBy(documentCode.description.desc());
			return query;
		case CREATED_BY:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.createdByUser.asc())
					: query.orderBy(correspondenceBase.createdByUser.desc());
			return query;
		case LAST_EDITED_BY:
			query = pageInfo.isAsc() ? query.orderBy(correspondenceBase.updatedDate.asc())
					: query.orderBy(correspondenceBase.updatedDate.desc());
			return query;
		default:
			return query;
		}
	}

	/**
	 * Creates the upload request sort order.
	 * @param pageInfo
	 *            the page info
	 * @param query
	 *            the query
	 * @param notificationProcess
	 *            the notification process
	 * @param downloadQueue
	 *            the download queue
	 * @param documentCode
	 *            the document code
	 * @return the JPA query
	 */
	private JPAQuery createUploadRequestSortOrder(PaginationInfo pageInfo, JPAQuery query,
			QNotificationProcess notificationProcess, QDownloadOfficeActionQueue downloadQueue,
			QDocumentCode documentCode) {
		switch (pageInfo.getSortAttribute()) {

		case CORR_ACTION_ITEM_JURISDICTION:
			query = pageInfo.isAsc() ? query.orderBy(downloadQueue.jurisdictionCode.asc())
					: query.orderBy(downloadQueue.jurisdictionCode.desc());
			return query;
		case CORR_ACTION_ITEM_APPLICATION_NO:
			query = pageInfo.isAsc() ? query.orderBy(downloadQueue.applicationNumberFormatted.asc())
					: query.orderBy(downloadQueue.applicationNumberFormatted.desc());
			return query;
		case CORR_ACTION_ITEM_MAILING_DATE:
			query = pageInfo.isAsc() ? query.orderBy(downloadQueue.mailingDate.asc())
					: query.orderBy(downloadQueue.mailingDate.desc());
			return query;
		case CORR_ACTION_ITEM_DOCUMENT_DESCRIPTION:
			query = pageInfo.isAsc() ? query.orderBy(documentCode.description.asc())
					: query.orderBy(documentCode.description.desc());
			return query;
		case CORR_ACTION_ITEM_NOTIFIED:
			query = pageInfo.isAsc() ? query.orderBy(notificationProcess.updatedDate.asc())
					: query.orderBy(notificationProcess.updatedDate.desc());
			return query;
		default:
			return query;
		}
	}

	private BooleanBuilder getExtendedSearchPredicate(QApplicationBase application, QCorrespondenceBase correspondence,
			QUser user, CorrespondenceRecordsFilter filter) {

		BooleanBuilder predicate = new BooleanBuilder();

		if (!org.springframework.util.StringUtils.isEmpty(filter.getApplicationNo())) {
			predicate = predicate.and(application.applicationNumber.startsWithIgnoreCase(filter.getApplicationNo()));
		}
		if (!org.springframework.util.StringUtils.isEmpty(filter.getJurisdiction())) {
			predicate = predicate.and(application.jurisdiction.code.equalsIgnoreCase(filter.getJurisdiction()));
		}
		if (!org.springframework.util.StringUtils.isEmpty(filter.getAttorneyDocketNo())) {
			predicate = predicate
					.and(application.attorneyDocketNumber.segment.startsWithIgnoreCase(filter.getAttorneyDocketNo()));
		}
		if (!org.springframework.util.StringUtils.isEmpty(filter.getFamilyId())) {
			predicate = predicate.and(application.familyId.startsWithIgnoreCase(filter.getFamilyId()));
		}
		if (!org.springframework.util.StringUtils.isEmpty(filter.getDescription())) {
			predicate = predicate
					.and(correspondence.documentCode.description.containsIgnoreCase(filter.getDescription()));
		}
		if (!org.springframework.util.StringUtils.isEmpty(filter.getUploadedBy())) {
			predicate = predicate.andAnyOf(user.person.firstName.containsIgnoreCase(filter.getUploadedBy()),
					user.person.lastName.containsIgnoreCase(filter.getUploadedBy()));
		}
		if (filter.getUploadStartDate() != null || filter.getUploadEndDate() != null) {
			Calendar startUploadDate = BlackboxDateUtil.convertDateToCal(filter.getUploadStartDate());
			Calendar endUploadDate = BlackboxDateUtil.convertDateToCal(filter.getUploadEndDate());

			if (startUploadDate != null || endUploadDate != null) {
				predicate.and(correspondence.createdDate.between(startUploadDate, endUploadDate));
			}
		}
		return predicate;
	}
	
	/**
	 * returns all correspondenceBase keys
	 */
	@Override
	public List<CorrespondenceBaseKeysDTO> getCorrespondenceBaseKeysDTOs() {
		List<CorrespondenceBaseKeysDTO> dtos = new ArrayList<>() ;
		QCorrespondenceBase correspondence = new QCorrespondenceBase("correspondence");
		QApplicationBase applicationBase = new QApplicationBase("applicationBase");
		QDocumentCode documentCode = new QDocumentCode("documentCode");
		QJurisdiction jurisdiction = new QJurisdiction("jurisdiction");
		JPAQuery query = getJPAQuery().from(correspondence).leftJoin(correspondence.application,applicationBase).
				leftJoin(correspondence.documentCode,documentCode).leftJoin(applicationBase.jurisdiction,jurisdiction);
		dtos = query.list(ConstructorExpression.create(CorrespondenceBaseKeysDTO.class, applicationBase.applicationNumber,
				jurisdiction.code,documentCode.code,correspondence.mailingDate,correspondence.pageCount,correspondence.attachmentSize));
		return dtos;
	}
}
