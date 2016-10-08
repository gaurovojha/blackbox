package com.blackbox.ids.scheduler.ids;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.services.ids.IDSExcelService;

@Service
public class ExportUrgentIDSRecordsJob {

	private Logger	log	= Logger.getLogger(ExportUrgentIDSRecordsJob.class);

	@Autowired
	private IDSExcelService	idsExcelService;

	public final void generateUrgentIDSRecordReport() {
		log.info("Generating urgent IDS Records Report");
		idsExcelService.generateUrgentIDSReport();
		log.info("Generating urgent IDS Records Report");
	}
}
