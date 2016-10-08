package com.blackbox.ids.scheduler.correspondence;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.services.correspondence.IPairAuditService;


@Service
public class ManualPairAuditJob {
	
	private Logger log = Logger.getLogger(ManualPairAuditJob.class);
	
	@Autowired
	private IPairAuditService pairAuditService;
	
	public final void processManualAuditFile() {
		log.info("Auditing of the Uploaded File Start");
			try {
				pairAuditService.processManualAuditJobFiles();
			} catch (FileNotFoundException e) {
				log.info("Some exception Occured while fetching the Uploaded file. Exception is" +e,e);
			}
			log.info("Auditing of the Uploaded File Completed");
	}
}
