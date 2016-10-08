package com.blackbox.ids.webcrawler.service.uspto.correspondence;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;

/**
 * The Interface {@link IIFWDownloadDoCScrapper} is the scrapper service for IFW Download DoC Web Crawler.
 * @author nagarro
 */
public interface IIFWDownloadDoCScrapper {

	/**
	 * Check and scrap application number details.
	 * @param webDriver
	 *            the web driver
	 * @param actionQueue
	 *            the action queue
	 * @param jobId
	 *            the job id
	 * @param pdfPathToDownloadQueueEntity
	 *            the pdf path to download queue entity
	 * @param documentCodes TODO
	 * @param maxRecordCount TODO
	 * @param appNumberToDocAndMailingDateMap
	 *            the app number to doc and mailing date map
	 * @return true, if successful
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	boolean checkAndScrapApplicationNumberDetails(WebDriver webDriver,
			DownloadOfficeActionQueue actionQueue,
			Long jobId, Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity, List<DocumentCode> documentCodes, int maxRecordCount) throws InterruptedException;

}
