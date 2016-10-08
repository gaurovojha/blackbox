package com.blackbox.ids.core.dao.webcrawler.impl;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dao.webcrawler.DownloadOfficeActionQueueDAO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordDTO;
import com.blackbox.ids.core.model.QDocumentCode;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.model.webcrawler.QDownloadOfficeActionQueue;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.ConstructorExpression;

@Repository
public class DownloadOfficeActionQueueDaoImpl extends BaseDaoImpl<DownloadOfficeActionQueue, Long>
		implements DownloadOfficeActionQueueDAO {

	public void changeStatusByIdAndStatus(Long downloadOfficeId, QueueStatus status, QueueStatus changedStatus) {

		if (changedStatus != null) {
			QDownloadOfficeActionQueue downloadQueue = QDownloadOfficeActionQueue.downloadOfficeActionQueue;
			BooleanBuilder predicate = new BooleanBuilder();
			if (downloadOfficeId != null) {
				predicate.and(downloadQueue.id.eq(downloadOfficeId));
			}
			if (status != null) {
				predicate.and(downloadQueue.status.eq(status));
			}
			if (predicate.getValue() != null) {
				JPAUpdateClause updateQuery = getJPAUpdateClause(downloadQueue).set(downloadQueue.status, changedStatus)
						.where(predicate);
				updateQuery.execute();
			}
		}
	}

	public CorrespondenceRecordDTO getCorrespondenceDTOByDownloadOfficeId(Long downloadOfficeId) {
		QDownloadOfficeActionQueue downloadQueue = QDownloadOfficeActionQueue.downloadOfficeActionQueue;
		QDocumentCode documentCode = QDocumentCode.documentCode;
		JPAQuery query = getJPAQuery().from(downloadQueue, documentCode); // TODO .where(dataAccessRules)
		query.where(downloadQueue.status.eq(QueueStatus.CRAWLER_ERROR)
				.and(downloadQueue.documentCode.eq(documentCode.code)));
		query.where(downloadQueue.id.eq(downloadOfficeId));
		CorrespondenceRecordDTO correspondenceRecordDTO = query
				.uniqueResult(ConstructorExpression.create(CorrespondenceRecordDTO.class, downloadQueue.id,
						downloadQueue.jurisdictionCode, downloadQueue.applicationNumberFormatted, downloadQueue.mailingDate,
						documentCode.description, downloadQueue.updatedDate));
		return correspondenceRecordDTO;
	}

}
