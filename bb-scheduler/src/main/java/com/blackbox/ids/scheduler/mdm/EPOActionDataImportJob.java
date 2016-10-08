package com.blackbox.ids.scheduler.mdm;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.blackbox.ids.services.mdm.IImportData;

public class EPOActionDataImportJob extends QuartzJobBean {

	private Logger logger = Logger.getLogger(EPOActionDataImportJob.class);

	@Autowired
	@Qualifier("ePOfficeActionImportService")
	private IImportData importDataService;

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("Import Job Started : " + this.getClass().getName());
		importDataService.importData();
	}
}
