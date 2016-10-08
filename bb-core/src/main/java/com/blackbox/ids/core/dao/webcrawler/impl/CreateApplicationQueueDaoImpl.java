package com.blackbox.ids.core.dao.webcrawler.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dao.webcrawler.CreateApplicationQueueDAO;
import com.blackbox.ids.core.dto.mdm.ActionsBaseDto;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.webcrawler.CreateApplicationQueue;
import com.blackbox.ids.core.model.webcrawler.QCreateApplicationQueue;
import com.mysema.query.types.ConstructorExpression;

@Repository
public class CreateApplicationQueueDaoImpl extends BaseDaoImpl<CreateApplicationQueue, Long>
		implements CreateApplicationQueueDAO {

	@Override
	public List<ActionsBaseDto> getCreateApplicationQueue() throws ApplicationException {
		QCreateApplicationQueue queue = QCreateApplicationQueue.createApplicationQueue;

		return getJPAQuery().from(queue).list(
				ConstructorExpression.create(ActionsBaseDto.class, queue.jurisdictionCode, queue.applicationNumberFormatted));
	}

	@Override
	public List<CreateApplicationQueue> findApplicationQByNumberJurisdictionWithoutError(final String jurisdiction, final String applicationNo) throws ApplicationException {
		QCreateApplicationQueue queue = QCreateApplicationQueue.createApplicationQueue;
		List<QueueStatus> errorList = new ArrayList<>();
		errorList.add(QueueStatus.AUTHENTICATION_ERROR) ;
		errorList.add(QueueStatus.CRAWLER_ERROR) ;
		errorList.add(QueueStatus.ERROR) ;
		errorList.add(QueueStatus.ERROR_APPLICATION_IN_EXCLUSION) ;
		return getJPAQuery().from(queue).
				where(queue.jurisdictionCode.equalsIgnoreCase(jurisdiction). and(queue.applicationNumberFormatted.equalsIgnoreCase(applicationNo))
						.and(queue.status.notIn(errorList)))
				.list(queue) ;
	}

	@Override
	public List<CreateApplicationQueue> findApplicationQByNumberJurisdictionWithError(String jurisdiction,
			String applicationNo) throws ApplicationException  {
		QCreateApplicationQueue queue = QCreateApplicationQueue.createApplicationQueue;
		return getJPAQuery().from(queue).
				where(queue.jurisdictionCode.equalsIgnoreCase(jurisdiction). and(queue.applicationNumberFormatted.equalsIgnoreCase(applicationNo)) )
				.list(queue) ;
	}

	@Override
	public Long rejectApplicationRequest(long entityId)
	{
		QCreateApplicationQueue queue = QCreateApplicationQueue.createApplicationQueue;
		return getJPAUpdateClause(queue).set(queue.status,QueueStatus.REJECTED).where(queue.id.eq(entityId)).execute();
	}

	@Override
	public long createApplicationRequest(long entityId) {
		QCreateApplicationQueue queue = QCreateApplicationQueue.createApplicationQueue;
		return getJPAUpdateClause(queue).set(queue.status,QueueStatus.SUCCESS).where(queue.id.eq(entityId)).execute();
	}



}
