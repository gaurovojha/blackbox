package com.blackbox.ids.scheduler.webcrawler;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.exception.WebCrawlerGenericException;
import com.blackbox.ids.exception.XMLOrPDFCorruptedException;
import com.blackbox.ids.webcrawler.service.uspto.application.IApplicationListValidationService;


public class ApplicationListValidationJob extends AbstractWebCrawlerBaseJob {

	private static final Logger LOGGER = Logger.getLogger(ApplicationListValidationJob.class);

	@Autowired
	private IApplicationListValidationService applicationListValidationService;

	@Override
	public void executeJob(WebCrawlerJobAudit webCrawlerJobAudit, int retryCount) throws XMLOrPDFCorruptedException{

		LOGGER.info("Import Job Started : " + this.getClass().getName());
		try{
			applicationListValidationService.execute(webCrawlerJobAudit);
		}catch(ApplicationException e){
			String errorMessage = prepareErrorMessage(e);
			LOGGER.error("Application error occurred " + errorMessage);
		}catch(InterruptedException e){
			String errorMessage = prepareErrorMessage(e);
			LOGGER.error("Thread Interrupted error " + errorMessage);
		}catch(WebCrawlerGenericException e){
			String errorMessage = prepareErrorMessage(e);
			LOGGER.error("WebCrawlerGeneric error occurred " + errorMessage);
		}
		
	}
	
	/**
	 * Prepare error message.
	 *
	 * @param e exception object
	 * @return the string
	 */
	private String prepareErrorMessage(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String errorMessage = sw.toString();
		return errorMessage;
	}

}