package com.blackbox.ids.services.ids.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.dao.ids.IDSDao;
import com.blackbox.ids.core.dao.webcrawler.ITrackIDSFilingQueueDao;
import com.blackbox.ids.core.model.IDS.IDS.SubStatus;
import com.blackbox.ids.core.model.webcrawler.TrackIDSFilingQueue;
import com.blackbox.ids.services.ids.ITrackIDSFilingQueueService;

@Service
public class TrackIDSFilingQueueServiceImpl implements ITrackIDSFilingQueueService {
	
	@Autowired
	private ITrackIDSFilingQueueDao trackIDSQueueDao;
	
	@Autowired
	private IDSDao idsDao;
	
	@Transactional
	@Override
	public void updateIDSTrackingStatus(TrackIDSFilingQueue trackIDSQueue, SubStatus idsSubStatus) {
		idsDao.updateIDSSubStatus(trackIDSQueue.getIdsId(), idsSubStatus);
		trackIDSQueueDao.persist(trackIDSQueue);
	}
	
	
	@Transactional
	@Override
	public void updateTrackIDSFilingQueue(TrackIDSFilingQueue trackIDSQueue) {
		trackIDSQueueDao.persist(trackIDSQueue);
	}

}
