package com.blackbox.ids.core.repository.impl.reference;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dto.reference.ReferenceEntryDTO;
import com.blackbox.ids.core.dto.reference.ReferenceEntryFilter;
import com.blackbox.ids.core.model.QPerson;
import com.blackbox.ids.core.model.QRole;
import com.blackbox.ids.core.model.QUser;
import com.blackbox.ids.core.model.correspondence.QCorrespondenceBase;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcess;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.model.notification.process.QNotificationProcess;
import com.blackbox.ids.core.model.reference.OcrData;
import com.blackbox.ids.core.model.reference.QOcrData;
import com.blackbox.ids.core.repository.reference.OcrDataCustomRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.path.PathBuilder;

/**
 * The Class OcrDataRepositoryImpl.
 */
public class OcrDataRepositoryImpl implements OcrDataCustomRepository {

	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;

	
	private Logger log = Logger.getLogger(OcrDataRepositoryImpl.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.core.repository.reference.OcrDataCustomRepository#getReferenceEntryData(com.blackbox.ids.core.
	 * dto.reference.ReferenceEntryFilter, org.springframework.data.domain.Pageable)
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<ReferenceEntryDTO> getReferenceEntryData(ReferenceEntryFilter filter, Pageable pageable) {

		String juris = filter.getJurisdiction();
		String myRecords = filter.getMyRecords();
		String dateRange = filter.getDateRange();
		Calendar startCalDate = null;
		Calendar endCalDate = null;

		QOcrData ocrData = QOcrData.ocrData;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QRole role = QRole.role;
		QUser user = QUser.user;
		QPerson person = QPerson.person;

		JPAQuery query = new JPAQuery(entityManager);
		query.from(notificationProcess, ocrData).join(notificationProcess.roles, role)
				.leftJoin(notificationProcess.lockedByUser, user).leftJoin(user.person, person);
		BooleanBuilder predicate = new BooleanBuilder();

		predicate.and(role.id.in(filter.getRoles()).and(notificationProcess.entityId.eq(ocrData.ocrDataId))
				.and(notificationProcess.entityName.eq(EntityName.OCR_DATA)).and(notificationProcess.active.eq(true)));
		predicate.and(notificationProcess.status.eq(NotificationStatus.PENDING));
		predicate.and(notificationProcess.notificationProcessType.eq(NotificationProcessType.REFERENCE_MANUAL_ENTRY));
		try {
			Date[] dateArray = BlackboxDateUtil.convertStringToDateRange(dateRange, TimestampFormat.MMMDDYYYY);
			Date startDate = dateArray[0];
			Date endDate = dateArray[1];
			startCalDate = BlackboxDateUtil.convertDateToCal(startDate);
			endCalDate = BlackboxDateUtil.convertDateToCal(endDate);
		} catch (Exception exception) {
			log.error("Reference Predicate :: Unable to parse Date passed in search filter." + exception);
		}

		if (Boolean.valueOf(myRecords)) {
			predicate.and(notificationProcess.updatedByUser.eq(BlackboxSecurityContextHolder.getUserId()));
		}

		if (!Constant.ALL.equalsIgnoreCase(juris)) {
			if (Constant.JURISDICTION_US.equalsIgnoreCase(juris)) {
				predicate.and(ocrData.jurisdictionCode.equalsIgnoreCase(juris));
			} else {
				predicate.and(ocrData.jurisdictionCode.ne(Constant.JURISDICTION_US));
			}
		}

		if (startCalDate != null) {
			predicate.and(ocrData.correspondenceId.updatedDate.between(startCalDate, endCalDate));
		}

		query.where(predicate);
		long count = query.count();

		Sort sort = pageable.getSort();
		if (sort != null && sort.getOrderFor("notifiedDate") != null) {

			Querydsl queryDsl = new Querydsl(entityManager,
					new PathBuilder<NotificationProcess>(NotificationProcess.class, "notificationProcess"));
			queryDsl.applyPagination(pageable, query);

		} else {
			Querydsl queryDsl = new Querydsl(entityManager, new PathBuilder<OcrData>(OcrData.class, "ocrData"));
			queryDsl.applyPagination(pageable, query);
		}

		List<ReferenceEntryDTO> refEntryDTO = query.list(ConstructorExpression.create(ReferenceEntryDTO.class,
				ocrData.ocrDataId, ocrData.correspondenceId.application.applicationNumber, ocrData.jurisdictionCode,
				ocrData.correspondenceId.id, ocrData.correspondenceId.mailingDate,
				ocrData.correspondenceId.documentCode.description, ocrData.ocrStatus,
				ocrData.correspondenceId.createdByUser, ocrData.correspondenceId.createdDate,
				ocrData.correspondenceId.updatedByUser, ocrData.correspondenceId.updatedDate,
				notificationProcess.notificationProcessId, user.id, person.firstName, person.lastName,
				notificationProcess.notifiedDate));

		return new PageImpl<ReferenceEntryDTO>(refEntryDTO, pageable, count);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.reference.OcrDataCustomRepository#getReferenceEntryCount()
	 */
	@Override
	@Transactional(readOnly = true)
	public Long getReferenceEntryCount() {

		QOcrData ocrData = QOcrData.ocrData;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QRole role = QRole.role;
		QUser user = QUser.user;
		QPerson person = QPerson.person;
		Set<Long> roles = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();
		JPAQuery query = new JPAQuery(entityManager);
		query.from(notificationProcess, ocrData).join(notificationProcess.roles, role)
				.leftJoin(notificationProcess.lockedByUser, user).leftJoin(user.person, person);
		BooleanBuilder predicate = new BooleanBuilder();

		predicate.and(role.id.in(roles).and(notificationProcess.entityId.eq(ocrData.ocrDataId))
				.and(notificationProcess.entityName.eq(EntityName.OCR_DATA)).and(notificationProcess.active.eq(true)));
		predicate.and(notificationProcess.status.eq(NotificationStatus.PENDING));
		predicate.and(notificationProcess.notificationProcessType.eq(NotificationProcessType.REFERENCE_MANUAL_ENTRY));

		query.where(predicate);
		long count = query.count();

		return count;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.core.repository.reference.OcrDataCustomRepository#
	 * getReferenceDataByNotificationProcessIdAndOcrDataId(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public ReferenceEntryDTO getReferenceDataByNotificationProcessIdAndOcrDataId(Long notificationProcessId,
			Long ocrDataId) {

		QOcrData ocrData = QOcrData.ocrData;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;

		JPAQuery query = new JPAQuery(entityManager);
		query.from(ocrData, notificationProcess).join(ocrData.correspondenceId, correspondence);

		query.where(ocrData.ocrDataId.eq(notificationProcess.entityId).and(ocrData.ocrDataId.eq(ocrDataId))
				.and(notificationProcess.entityName.eq(EntityName.OCR_DATA))
				.and(notificationProcess.notificationProcessId.eq(notificationProcessId)));

		ReferenceEntryDTO referenceEntryDTO = query.singleResult(ConstructorExpression.create(ReferenceEntryDTO.class,
				ocrData.ocrDataId, correspondence.application.applicationNumber, ocrData.jurisdictionCode,
				correspondence.id, correspondence.mailingDate, correspondence.documentCode.description,
				ocrData.ocrStatus, correspondence.createdByUser, correspondence.createdDate,
				correspondence.updatedByUser, correspondence.updatedDate));
		return referenceEntryDTO;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.core.repository.reference.OcrDataCustomRepository#getReferenceDataListByNotificationProcess(java
	 * .lang.Long)
	 */
/*	@Override
	@Transactional(readOnly = true)
	public Long getReferenceDataListByNotificationProcess(Long notificationProcessId) {

		QOcrData ocrData = QOcrData.ocrData;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;

		JPAQuery query = new JPAQuery(entityManager);
		query.from(ocrData, notificationProcess).join(ocrData.correspondenceId, correspondence);

		query.where(ocrData.ocrDataId.eq(notificationProcess.entityId)
				.and(notificationProcess.entityName.eq(EntityName.REFERENCE_BASE_DATA))
				.and(notificationProcess.notificationProcessId.eq(notificationProcessId)));

		Long count = query.count();

		return count;

	}
	*/
}
