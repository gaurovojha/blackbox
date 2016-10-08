package com.blackbox.ids.services.ids.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.webcrawler.IN417TreatmentQueueDAO;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.webcrawler.N417TreatmentQueue;
import com.blackbox.ids.services.mdm.IImportData;

@Service("n417QueueImportService")
public class N417TreatmentQueueImportService implements IImportData {

	private static Log LOGGER = LogFactory.getLog(N417TreatmentQueueImportService.class);
	
	/** The application base dao. */
	@Autowired
	private ApplicationBaseDAO				applicationBaseDao;
		
	@Autowired
	private IN417TreatmentQueueDAO			n417TreatmentQueueDao;

	@Override
	@Transactional
	public void importData() {
		try {
			LOGGER.info("Deleting BB_SCR_N417_TREATMENT queue records");
			n417TreatmentQueueDao.deleteAll();
			
			LOGGER.info("Fetching Data from BB_APPLICATION_BASE table");
			List<ApplicationBase> applicationList = applicationBaseDao.getApplicationsByProsecutionStatus();
			LOGGER.debug("Found " + applicationList.size() + " Records");
			List<N417TreatmentQueue> queueData = applicationList.stream().map(N417TreatmentQueue::new)
					.collect(Collectors.toList());
			n417TreatmentQueueDao.persist(queueData);
		} catch (Exception e) {
			LOGGER.error("Failed to import data from  BB_APPLICATION_BASE table", e);
		}

	}
}
