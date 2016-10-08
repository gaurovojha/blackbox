package com.blackbox.ids.scheduler.mdm;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.blackbox.ids.services.mdm.IImportData;

/**
 * Backend job to import application data from application-stage to application-base.
 */
public class MDMDataImportJob extends QuartzJobBean {

	private static Logger LOGGER = Logger.getLogger(MDMDataImportJob.class);

	@Autowired
	@Qualifier("mdmDataImportService")
	private IImportData importDataService;

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		LOGGER.info("MDM Data import job started");
		try {
			importDataService.importData();
		} catch (Exception e) {
			LOGGER.error("Some exception has occured with MDM data import : ", e);
		}
		LOGGER.info("MDM Data import job ended");
	}

}
