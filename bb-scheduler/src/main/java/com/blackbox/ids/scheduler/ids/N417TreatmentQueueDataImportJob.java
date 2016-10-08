package com.blackbox.ids.scheduler.ids;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.blackbox.ids.services.mdm.IImportData;

public class N417TreatmentQueueDataImportJob extends QuartzJobBean {

	private Logger logger = Logger.getLogger(N417TreatmentQueueDataImportJob.class);

	@Autowired
	@Qualifier("n417QueueImportService")
	private IImportData importDataService;

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("Data import job started for N417 Treatment queue");
		importDataService.importData();
		logger.info("Data import job ended for N417 Treatment queue");
	}
}
