package com.blackbox.ids.scheduler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.services.scheduler.OcrDataSchedulerService;

@Service
public class OcrDataScheduler {

	private Logger log = Logger.getLogger(OcrDataScheduler.class);

	@Autowired
	private OcrDataSchedulerService ocrDataSchedulerService;

	public final void processOcrData() {
		log.info("Start processing OCR Data.");
		ocrDataSchedulerService.processOcrData();
		log.info("OCR data processed.");
	}

}
