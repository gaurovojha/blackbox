package com.blackbox.ids.scheduler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.services.scheduler.ImportReferenceStagingDataService;

@Service
public class ImportReferenceStagingDataScheduler {

	private Logger log = Logger.getLogger(ImportReferenceStagingDataScheduler.class);

	@Autowired
	private ImportReferenceStagingDataService importReferenceStagingDataService;

	public final void importReferenceStagingData() {
		log.info("Start Importing the Reference Staging data in Reference Base.");
		importReferenceStagingDataService.importStagingData();
		log.info("End Importing the Reference Staging data in Reference Base.");
	}


}
