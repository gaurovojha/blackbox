package com.blackbox.ids.core.dao.webcrawler;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordDTO;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;

/**
 * The {@code DownloadOfficeActionQueueDAO} exposes APIs to perform database operations on entity class {@link DownloadOfficeActionQueue}.
 * @author sumanchandra
 *
 */
public interface DownloadOfficeActionQueueDAO extends BaseDAO<DownloadOfficeActionQueue, Long>{
	
	public void changeStatusByIdAndStatus(Long downloadOfficeId, QueueStatus status, QueueStatus changedStatus);
	
	public CorrespondenceRecordDTO getCorrespondenceDTOByDownloadOfficeId(Long downloadOfficeId);

}
