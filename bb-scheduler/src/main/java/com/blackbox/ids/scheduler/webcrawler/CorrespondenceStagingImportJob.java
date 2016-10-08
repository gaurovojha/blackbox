package com.blackbox.ids.scheduler.webcrawler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.webcrawler.service.uspto.correspondence.ICorrespondenceStagingImportService;


@Service
public class CorrespondenceStagingImportJob {
	
	private Logger log = Logger.getLogger(CorrespondenceStagingImportJob.class);
	
	@Autowired
	private ICorrespondenceStagingImportService correspondenceStagingImportService;
	
	public final void importStagingData() {
		
		try {
		correspondenceStagingImportService.importData();
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error("Some exception occured while importing the staging data to base table. Exceptiion is "+ex,ex);
		}
	}
}
