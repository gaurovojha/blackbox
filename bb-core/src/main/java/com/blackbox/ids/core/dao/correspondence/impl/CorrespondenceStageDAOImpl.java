package com.blackbox.ids.core.dao.correspondence.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.auth.BlackboxUser;
import com.blackbox.ids.core.dao.correspondence.ICorrespondenceStageDAO;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordDTO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordsFilter;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.model.QUser;
import com.blackbox.ids.core.model.correspondence.Correspondence;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.correspondence.QCorrespondenceStaging;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.QNotificationProcess;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ConstructorExpression;

@Service
public class CorrespondenceStageDAOImpl extends BaseDaoImpl<CorrespondenceStaging, Long>
		implements ICorrespondenceStageDAO {

	private static final Logger LOGGER = Logger.getLogger(CorrespondenceStageDAOImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public long createApplicationRequest(long entityId) {
		QCorrespondenceStaging correspondence = QCorrespondenceStaging.correspondenceStaging;
		return getJPAUpdateClause(correspondence)
				.set(correspondence.status, Correspondence.Status.CREATE_APPLICATION_REQUEST_APPROVED)
				.where(correspondence.Id.eq(entityId)).execute();
	}

	@Override
	public long rejectApplicationRequest(long entityId) {

		QCorrespondenceStaging correspondence = QCorrespondenceStaging.correspondenceStaging;
		return getJPAUpdateClause(correspondence)
				.set(correspondence.status, Correspondence.Status.CREATE_APPLICATION_REQUEST_REJECTED)
				.where(correspondence.Id.eq(entityId)).execute();

	}

	@Override
	public SearchResult<CorrespondenceRecordDTO> fetchAllTrackApplications(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo) {
		LOGGER.debug("Fetch All Track Applications in Correspondence");
		long totalRecords = 0;
		long filteredResults = 0;
		List<CorrespondenceRecordDTO> items = new ArrayList<>();
		BooleanBuilder predicate = new BooleanBuilder();
		Set<Long> roles = null;
		Calendar startFillingDate = null;
		Calendar endFillingDate = null;
		if (filter.getStartDate() != null || filter.getEndDate() != null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}
		QCorrespondenceStaging corrStag = QCorrespondenceStaging.correspondenceStaging;
		QNotificationProcess notificationprocess = QNotificationProcess.notificationProcess;
		QUser user1 = QUser.user;
		BlackboxUser blackboxUser = BlackboxSecurityContextHolder.getUserDetails();
		if (blackboxUser != null) {
			roles = blackboxUser.getRoleIds();
		}
		if (!CollectionUtils.isEmpty(roles)) {
			JPAQuery query = getJPAQuery().from(corrStag, notificationprocess, user1);
			query.where(corrStag.Id.eq(notificationprocess.entityId)
					.and(corrStag.status.in(Correspondence.Status.CREATE_APPLICATION_REQUEST_PENDING,
							Correspondence.Status.CREATE_APPLICATION_REQUEST_APPROVED,
							Correspondence.Status.CREATE_APPLICATION_REQUEST_REJECTED))
					.and(notificationprocess.entityName.eq(EntityName.CORRESPONDENCE_STAGING))
					.and(notificationprocess.createdByUser.eq(user1.id)).and(user1.userRoles.any().id.in(roles)))
					.distinct();
			totalRecords = query.count();
			if (startFillingDate != null || endFillingDate != null) {
				predicate.and((corrStag.mailingDate.between(startFillingDate, endFillingDate)
						.or(notificationprocess.notifiedDate.between(startFillingDate, endFillingDate))));
			}
			query.where(predicate);
			filteredResults = query.count();
			query = createSortOrder(pageInfo, query, corrStag, notificationprocess);
			items = query.offset(pageInfo.getOffset()).limit(pageInfo.getLimit()).distinct()
					.list(ConstructorExpression.create(CorrespondenceRecordDTO.class,
							notificationprocess.notificationProcessId, corrStag.jurisdictionCode,
							corrStag.applicationNumber, corrStag.mailingDate, corrStag.documentDescription,
							corrStag.status, user1, notificationprocess));
			for (CorrespondenceRecordDTO recordDTO : items) {
				if (recordDTO.getUpdatedBy() != null) {
					recordDTO.setApprover(userRepository.getUserFirstAndLastName(recordDTO.getUpdatedBy()));
				}
			}
		}
		SearchResult<CorrespondenceRecordDTO> result = new SearchResult<>(totalRecords, filteredResults, items);
		return result;
	}

	@Override
	public SearchResult<CorrespondenceRecordDTO> fetchMyTrackApplications(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo) {
		LOGGER.debug("Fetch My Track Applications in Correspondence");
		long totalRecords = 0;
		long filteredResults = 0;
		List<CorrespondenceRecordDTO> items = new ArrayList<>();
		BooleanBuilder predicate = new BooleanBuilder();
		Long id = null;
		Calendar startFillingDate = null;
		Calendar endFillingDate = null;
		if (filter.getStartDate() != null || filter.getEndDate() != null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}
		QCorrespondenceStaging corrStag = QCorrespondenceStaging.correspondenceStaging;
		QNotificationProcess notificationprocess = QNotificationProcess.notificationProcess;
		QUser user1 = QUser.user;
		BlackboxUser blackboxUser = BlackboxSecurityContextHolder.getUserDetails();
		if (blackboxUser != null) {
			id = blackboxUser.getId();
		}
		if (id != null) {
			JPAQuery query = getJPAQuery().from(corrStag, notificationprocess, user1);
			query.where(corrStag.Id.eq(notificationprocess.entityId)
					.and(corrStag.status.in(Correspondence.Status.CREATE_APPLICATION_REQUEST_PENDING,
							Correspondence.Status.CREATE_APPLICATION_REQUEST_APPROVED,
							Correspondence.Status.CREATE_APPLICATION_REQUEST_REJECTED))
					.and(notificationprocess.entityName.eq(EntityName.CORRESPONDENCE_STAGING))
					.and(notificationprocess.createdByUser.eq(id)).and(user1.id.eq(id))).distinct();
			totalRecords = query.count();
			if (startFillingDate != null || endFillingDate != null) {
				predicate.and((corrStag.mailingDate.between(startFillingDate, endFillingDate)
						.or(notificationprocess.notifiedDate.between(startFillingDate, endFillingDate))));
			}
			query.where(predicate);
			filteredResults = query.count();
			query = createSortOrder(pageInfo, query, corrStag, notificationprocess);
			items = query.offset(pageInfo.getOffset()).limit(pageInfo.getLimit()).distinct()
					.list(ConstructorExpression.create(CorrespondenceRecordDTO.class,
							notificationprocess.notificationProcessId, corrStag.jurisdictionCode,
							corrStag.applicationNumber, corrStag.mailingDate, corrStag.documentDescription,
							corrStag.status, user1, notificationprocess));
			for (CorrespondenceRecordDTO recordDTO : items) {
				if (recordDTO.getUpdatedBy() != null) {
					recordDTO.setApprover(userRepository.getUserFirstAndLastName(recordDTO.getUpdatedBy()));
				}
			}
		}
		SearchResult<CorrespondenceRecordDTO> result = new SearchResult<>(totalRecords, filteredResults, items);
		return result;
	}

	private JPAQuery createSortOrder(PaginationInfo pageInfo, JPAQuery query, QCorrespondenceStaging corrStag,
			QNotificationProcess notificationProcess) {
		switch (pageInfo.getSortAttribute()) {

		case CORR_ACTION_ITEM_JURISDICTION:
			query = pageInfo.isAsc() ? query.orderBy(corrStag.jurisdictionCode.asc())
					: query.orderBy(corrStag.jurisdictionCode.desc());
			return query;
		case CORR_ACTION_ITEM_APPLICATION_NO:
			query = pageInfo.isAsc() ? query.orderBy(corrStag.applicationNumber.asc())
					: query.orderBy(corrStag.applicationNumber.desc());
			return query;
		case CORR_ACTION_ITEM_MAILING_DATE:
			query = pageInfo.isAsc() ? query.orderBy(corrStag.mailingDate.asc())
					: query.orderBy(corrStag.mailingDate.desc());
			return query;
		case CORR_ACTION_ITEM_DOCUMENT_DESCRIPTION:
			query = pageInfo.isAsc() ? query.orderBy(corrStag.documentDescription.asc())
					: query.orderBy(corrStag.documentDescription.desc());
			return query;
		case CORR_ACTION_ITEM_STATUS:
			query = pageInfo.isAsc() ? query.orderBy(corrStag.status.asc()) : query.orderBy(corrStag.status.desc());
			return query;
		case CORR_REQUESTER:
			query = pageInfo.isAsc() ? query.orderBy(notificationProcess.notifiedDate.asc())
					: query.orderBy(notificationProcess.notifiedDate.desc());
			return query;
		default:
			return query;
		}
	}

}
