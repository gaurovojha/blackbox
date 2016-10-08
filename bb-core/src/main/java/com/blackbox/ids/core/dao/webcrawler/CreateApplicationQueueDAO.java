package com.blackbox.ids.core.dao.webcrawler;

import java.util.List;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.dto.mdm.ActionsBaseDto;
import com.blackbox.ids.core.model.webcrawler.CreateApplicationQueue;

/**
 * The {@code CreateApplicationQueueDAO} exposes APIs to perform database operations on entity class {@link CreateApplicationQueue}.
 * @author sumanchandra
 *
 */
public interface CreateApplicationQueueDAO extends BaseDAO<CreateApplicationQueue, Long>{

	List<ActionsBaseDto> getCreateApplicationQueue() throws ApplicationException ;

	List<CreateApplicationQueue> findApplicationQByNumberJurisdictionWithoutError(String jurisdiction, String applicationNo)
			throws ApplicationException;

	Long rejectApplicationRequest(long entityId);

	long createApplicationRequest(long entityId);

	List<CreateApplicationQueue> findApplicationQByNumberJurisdictionWithError(String stagingJurisdictionNumber,
			String stagingApplicationNumber) throws ApplicationException;
}
