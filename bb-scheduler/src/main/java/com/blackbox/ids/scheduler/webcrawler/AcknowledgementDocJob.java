package com.blackbox.ids.scheduler.webcrawler;

import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.scheduler.SchedulerUtil;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.webcrawler.service.uspto.IBaseCrawlerService;

@Service
public class AcknowledgementDocJob extends AbstractWebCrawlerBaseJob {

	private Logger logger = Logger.getLogger(AcknowledgementDocJob.class);

	@Autowired
	@Qualifier("acknowledgementDocCrawlerService")
	private IBaseCrawlerService acknowledgementDocCrawlerService;

	@Override
	public void executeJob(WebCrawlerJobAudit webCrawlerJobAudit, int retryCount) {
		logger.info("Starting AcknowledgementDocJob");
		Map<String, Object> map = acknowledgementDocCrawlerService.execute(webCrawlerJobAudit);

		int lowerValue = (Integer) map.get(BBWebCrawerConstants.COUNTER_LOWER_VALUE);
		int upperValue = (Integer) map.get(BBWebCrawerConstants.COUNTER_UPPER_VALUE);
		int currentValue = (Integer) map.get(BBWebCrawerConstants.COUNTER_VAL);

		SchedulerUtil schedulerUtil = getSchedulerUtil();

		int newTriggerTime = 0;
		try {
			if (currentValue > lowerValue && currentValue < upperValue) {
				newTriggerTime = 30;
			} else if (currentValue <= lowerValue) {
				newTriggerTime = 20;
			} else {
				newTriggerTime = 45;
			}
			schedulerUtil.rescheduleExistingTrigger(newTriggerTime, BBWebCrawerConstants.TRIGGER_NAME,
					BBWebCrawerConstants.TRIGGER_GROUP);

		} catch (SchedulerException e) {
			logger.error("Failed to Reschedule  AcknowledgementDocJob", e);
		}
	}
}
