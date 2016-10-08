package com.blackbox.ids.services.ids.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.webcrawler.IN417TreatmentQueueDAO;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.webcrawler.N417TreatmentQueue;
import com.blackbox.ids.services.ids.IN417TreatmentQueueService;

@Service
public class N417TreatmentQueueServiceImpl implements IN417TreatmentQueueService {
	
	/** The application base dao. */
	@Autowired
	private ApplicationBaseDAO				applicationBaseDao;

	@Autowired
	private IN417TreatmentQueueDAO			n417TreatmentQueueDao;
	
	
	@Transactional
	@Override
	public void updateSuccessStatus(final N417TreatmentQueue app) {
		applicationBaseDao.persist(app.getApplication());
		app.setStatus(QueueStatus.SUCCESS);
		n417TreatmentQueueDao.persist(app);
	}

}
