package com.blackbox.ids.scheduler.webcrawler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.blackbox.ids.common.constant.Constant;
import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.enums.WebCrawlerJobStatusEnum;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobStatus;
import com.blackbox.ids.core.repository.webcrawler.WebCrawlerJobAuditRepository;
import com.blackbox.ids.core.repository.webcrawler.WebCrawlerJobStatusRepository;
import com.blackbox.ids.exception.LoginHostNotFoundException;
import com.blackbox.ids.exception.USPTOUpdationException;
import com.blackbox.ids.exception.XMLAndUICorrespondenceMisMatchException;
import com.blackbox.ids.exception.XMLOrPDFCorruptedException;
import com.blackbox.ids.scheduler.SchedulerUtil;
import com.blackbox.ids.services.common.EmailService;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;

/**
 * Base class for background jobs.
 *
 */
@DisallowConcurrentExecution
public abstract class AbstractWebCrawlerBaseJob extends QuartzJobBean {

	/** The logger. */
	private Logger logger = Logger.getLogger(AbstractWebCrawlerBaseJob.class);

	/** The scheduler util. */
	@Autowired
	private SchedulerUtil schedulerUtil;

	/** The properties. */
	@Autowired
	BBWebCrawlerPropertyConstants properties;

	/** The web crawler job audit repository. */
	@Autowired
	WebCrawlerJobAuditRepository webCrawlerJobAuditRepository;

	/** The web crawler job status repository. */
	@Autowired
	WebCrawlerJobStatusRepository webCrawlerJobStatusRepository;

	@Autowired
	private EmailService emailService;

	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		WebCrawlerJobAudit webCrawlerJobAudit = new WebCrawlerJobAudit(WebCrawlerJobStatusEnum.RUNNING,
				BBWebCrawerConstants.EMPTY_STRING, Calendar.getInstance(), null, this.getClass().getSimpleName());

		Calendar triggerTime = Calendar.getInstance();
		WebCrawlerJobStatus webCrawlerJobStatus = webCrawlerJobStatusRepository
				.findByJobName(this.getClass().getSimpleName());

		int reschedulingTime = properties.getRechedulingTime();
		try {
			webCrawlerJobAuditRepository.save(webCrawlerJobAudit);
			logger.info(MessageFormat.format("Executing Crawler Job id [{0}] , job name [{1}] ", webCrawlerJobAudit.getId(),this.getClass().getSimpleName()));
			executeJob(webCrawlerJobAudit, webCrawlerJobStatus.getMaxRecordRetryCount());
			webCrawlerJobAudit.setStatus(WebCrawlerJobStatusEnum.SUCCESS);
			resetRetryParams(webCrawlerJobStatus);
		} catch (LoginHostNotFoundException e) {
			logger.error(e);
			populateEntity(webCrawlerJobAudit, prepareErrorMessage(e));
			int retryCount = webCrawlerJobStatus.getRetryCountForHostNotFound();
			if (webCrawlerJobStatus.getMaxCountForHostNotFound() != retryCount) {
				rescheduleTrigger(triggerTime, reschedulingTime);
				webCrawlerJobStatus.setRetryCountForHostNotFound(++retryCount);
			}
			sendNotification(this.getClass().getSimpleName());
		} catch (USPTOUpdationException e) {
			String errorMessage = prepareErrorMessage(e);
			logger.error("USPTO site is updating . Exception is " + errorMessage);
			populateEntity(webCrawlerJobAudit, errorMessage);

			int retryCount = webCrawlerJobStatus.getRetryCountForUsptoUpdation();
			if (webCrawlerJobStatus.getMaxCountForUsptoUpdation() != retryCount) {
				rescheduleTrigger(triggerTime, reschedulingTime);
				webCrawlerJobStatus.setRetryCountForUsptoUpdation(++retryCount);
			}
			sendNotification(this.getClass().getSimpleName());

		} catch (XMLOrPDFCorruptedException e) {
			String errorMessage = prepareErrorMessage(e);
			logger.error("The XML/PDF downloaded is corrupt. Exception is " + errorMessage);
			populateEntity(webCrawlerJobAudit, errorMessage);

			int retryCount = webCrawlerJobStatus.getXmlOrPDFCorruptRetryCount();
			if (webCrawlerJobStatus.getMaxXmlCorruptRetryCount() != retryCount) {
				rescheduleTrigger(triggerTime, reschedulingTime);
				webCrawlerJobStatus.setXmlOrPDFCorruptRetryCount(++retryCount);
			}
			sendNotification(this.getClass().getSimpleName());
		} catch (XMLAndUICorrespondenceMisMatchException xuex) {
			String errorMessage = prepareErrorMessage(xuex);
			logger.error("The count of record in XML and UI dont match. Exception is " + errorMessage);
			populateEntity(webCrawlerJobAudit, errorMessage);

			int retryCount = webCrawlerJobStatus.getXmlUIMismatchRetryCount();
			if (webCrawlerJobStatus.getMaxXmlUIMismatchRetryCount() != retryCount) {
				rescheduleTrigger(triggerTime, reschedulingTime);
				webCrawlerJobStatus.setXmlUIMismatchRetryCount(++retryCount);
			}
			sendNotification(this.getClass().getSimpleName());
		} catch (Exception e) {
			String errorMessage = prepareErrorMessage(e);
			logger.error("Some Exception ocured while crawling. Exception is " + errorMessage);
			populateEntity(webCrawlerJobAudit, errorMessage);
			sendNotification(this.getClass().getSimpleName());
		} finally {
			webCrawlerJobAudit.setEndTime(Calendar.getInstance());
			try {
				webCrawlerJobStatusRepository.save(webCrawlerJobStatus);
				webCrawlerJobAuditRepository.save(webCrawlerJobAudit);
			} catch (DataAccessException dex) {
				logger.info(MessageFormat
						.format("Some exception occured while updating the final status for crawler id [{0}]."
								+ " Exception is [{1}]", webCrawlerJobAudit.getId(), prepareErrorMessage(dex)));
			}
		}
	}

	/**
	 * Reset retry params.
	 *
	 * @param webCrawlerJobStatus
	 *            the web crawler job status
	 */
	private void resetRetryParams(WebCrawlerJobStatus webCrawlerJobStatus) {
		webCrawlerJobStatus.setRetryCountForUsptoUpdation(0);
		webCrawlerJobStatus.setRetryCountForHostNotFound(0);
		webCrawlerJobStatus.setXmlOrPDFCorruptRetryCount(0);
		webCrawlerJobStatus.setXmlUIMismatchRetryCount(0);
	}

	/**
	 * Prepare error message.
	 *
	 * @param e
	 *            the e
	 * @return the string
	 */
	private String prepareErrorMessage(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String errorMessage = sw.toString();
		return errorMessage;
	}

	/**
	 * Populate entity.
	 *
	 * @param webCrawlerJobAudit
	 *            the web crawler job audit
	 * @param errorMessage
	 *            the error message
	 */
	private void populateEntity(WebCrawlerJobAudit webCrawlerJobAudit, String errorMessage) {
		webCrawlerJobAudit.setStatus(WebCrawlerJobStatusEnum.ERROR);
		webCrawlerJobAudit.setErrorMessage(StringUtils.substring(errorMessage, 0, 4000));
	}

	/**
	 * Execute job.
	 *
	 * @param webCrawlerJobAudit
	 *            the web crawler job audit
	 * @param retryCount
	 *            max retry count for each record
	 */
	public abstract void executeJob(WebCrawlerJobAudit webCrawlerJobAudit, int retryCount);

	/**
	 * Prepare and send email.
	 * 
	 * @param jobName
	 */
	private void sendNotification(String jobName) {
		Message message = new Message();
		message.setTemplateType(TemplateType.CRAWLER_NOTIFICATION);
		message.setReceiverList(properties.adminEmailId);
		message.setStatus(Constant.MESSAGE_STATUS_NOT_SENT);

		MessageData data1 = new MessageData("jobName", jobName);
		List<MessageData> list = new ArrayList<>();
		list.add(data1);
		emailService.send(message, list);
	}

	private void rescheduleTrigger(Calendar trigger, int reschedulingTimeInMin) {
		trigger.add(Calendar.MINUTE, reschedulingTimeInMin);
		schedulerUtil.scheduleJob(this, trigger);
	}

	/**
	 * Gets the scheduler util.
	 *
	 * @return the scheduler util
	 */
	public SchedulerUtil getSchedulerUtil() {
		return schedulerUtil;
	}

}
