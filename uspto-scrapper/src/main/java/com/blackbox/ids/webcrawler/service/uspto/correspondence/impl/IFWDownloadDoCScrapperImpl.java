package com.blackbox.ids.webcrawler.service.uspto.correspondence.impl;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.repository.webcrawler.DownloadOfficeActionQueueRepository;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.dto.CorrespondenceCrawlerDTO;
import com.blackbox.ids.exception.XMLOrPDFCorruptedException;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IIFWDownloadDoCScrapper;

/**
 * The Class {@link IFWDownloadDoCScrapperImpl} is the implementing class for
 * {@link IIFWDownloadDoCScrapper}. This class is used to scrap the USPTO website for IFW Documents.
 */
@Component("ifwDownloadDoCScrapperImpl")
public class IFWDownloadDoCScrapperImpl implements IIFWDownloadDoCScrapper {
	
	private static final Logger	logger	= Logger.getLogger(IFWDownloadDoCScrapperImpl.class);

	/** The properties. */
	@Autowired
	private BBWebCrawlerPropertyConstants properties;
	
	/** The application base dao. */
	@Autowired
	private ApplicationBaseDAO applicationBaseDAO;

	/** The notification process service. */
	@Autowired
	private NotificationProcessService notificationProcessService;
	
	/** The download office action queue repository. */
	@Autowired
	private DownloadOfficeActionQueueRepository downloadOfficeActionQueueRepository;


	@Override
	public boolean checkAndScrapApplicationNumberDetails(WebDriver webDriver,
			DownloadOfficeActionQueue actionQueue,
			Long jobId, Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity, List<DocumentCode> documentCodes, int maxRecordCount) throws InterruptedException {

		JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		
		boolean isUSApplication = true;
		String convertedApplicationNumber = BBWebCrawerConstants.EMPTY_STRING;
		String rawApplicationNumber = actionQueue.getAppNumberRaw();

		if (rawApplicationNumber.startsWith(BBWebCrawerConstants.WO_CODE_INDICATOR)) {
			// Conversion required as alternate try with different format of application number is required for WO applications only.
			convertedApplicationNumber = BBWebCrawlerUtils.convertToOtherForm(rawApplicationNumber, actionQueue.getFilingDate());
			logger.info("Converted Application Number is " + convertedApplicationNumber);
			isUSApplication = false;
		} else {
			convertedApplicationNumber = rawApplicationNumber;
		}
		boolean isValidAppplicationNumber = false;
		if (StringUtils.isNotEmpty(convertedApplicationNumber)) {
			CorrespondenceCrawlerDTO correspondenceCrawlerDTO = new CorrespondenceCrawlerDTO(isUSApplication,
					convertedApplicationNumber, isValidAppplicationNumber, documentCodes);
			correspondenceCrawlerDTO.setMaxRecordRetryCount(maxRecordCount);
			isValidAppplicationNumber = processValidFormatApplications(webDriver,
					actionQueue, jobId, pdfPathToDownloadQueueEntity,jse,correspondenceCrawlerDTO);
		}
		return isValidAppplicationNumber;
	}

	/**
	 * Process valid format applications.
	 *
	 * @param webDriver the web driver
	 * @param actionQueue the action queue
	 * @param jobId the job id
	 * @param pdfPathToDownloadQueueEntity the pdf path to download queue entity
	 * @param appNumberToMailingDateAndDocMap the app number to mailing date and doc map
	 * @param jse the jse
	 * @param isUSApplication the is us application
	 * @param convertedApplicationNumber the converted application number
	 * @param bbxApplicationNumbers the bbx application numbers
	 * @param isValidAppplicationNumber the is valid appplication number
	 * @return true, if successful
	 * @throws InterruptedException the interrupted exception
	 */
	private boolean processValidFormatApplications(WebDriver webDriver, DownloadOfficeActionQueue actionQueue,
			Long jobId, Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity,
			JavascriptExecutor jse, CorrespondenceCrawlerDTO correspondenceCrawlerDTO) throws InterruptedException {
		
		String applicationNumberRaw = actionQueue.getAppNumberRaw();
		boolean isValidAppplicationNumber = correspondenceCrawlerDTO.isValidAppplicationNumber();
		boolean isUSApplication = correspondenceCrawlerDTO.isUSApplication();
		String convertedApplicationNumber = correspondenceCrawlerDTO.getConvertedApplicationNumber();

		isValidAppplicationNumber = applyFiltersAndNavigateToTab(webDriver, applicationNumberRaw,
					isUSApplication, convertedApplicationNumber, jse);
		if (isValidAppplicationNumber) {
			scrapValidApplicationNumbers(webDriver, actionQueue.getApplicationNumberFormatted(), actionQueue,
					jobId, pdfPathToDownloadQueueEntity,correspondenceCrawlerDTO);
		}
		return isValidAppplicationNumber;
	}

	/**
	 * Apply filters and navigate to tab.
	 *
	 * @param webDriver            the web driver
	 * @param applicationNumber            the application number
	 * @param isUSApplication the is us application
	 * @param otherFormatApplicationNumber the other format application number
	 * @param jse 
	 * @return true, if successful
	 * @throws InterruptedException             the interrupted exception
	 */
	private boolean applyFiltersAndNavigateToTab(WebDriver webDriver, String applicationNumber,
			boolean isUSApplication, String otherFormatApplicationNumber, JavascriptExecutor jse)
					throws InterruptedException {
		WebElement element;
		boolean isValidApplicationNumber = false;

		if (isUSApplication) {
			element = webDriver.findElement(By.xpath(properties.applicationNumberUSRadioButtonXPath));
			searchingAnApplicationNumber(webDriver, applicationNumber, element,jse);
			isValidApplicationNumber = checkApplicationNumber(webDriver, applicationNumber);
		} else if (StringUtils.isNotEmpty(otherFormatApplicationNumber)) {
			isValidApplicationNumber = checkWOApplications(webDriver, applicationNumber, otherFormatApplicationNumber,
					jse);
		}
		return isValidApplicationNumber;
	}

	/**
	 * Check wo applications.
	 *
	 * @param webDriver the web driver
	 * @param applicationNumber the application number
	 * @param otherFormatApplicationNumber the other format application number
	 * @param jse 
	 * @return true, if successful
	 * @throws InterruptedException the interrupted exception
	 */
	private boolean checkWOApplications(WebDriver webDriver, String applicationNumber,
			String otherFormatApplicationNumber, JavascriptExecutor jse) throws InterruptedException {
		WebElement element;
		boolean isValidApplicationNumber = false;
		for (int rowIndex = 0; rowIndex < 2; rowIndex++) {
			element = webDriver.findElement(By.xpath(properties.applicationNumberWORadioButtonXPath));
			String applicationNumber1 = rowIndex == 0 ? applicationNumber : otherFormatApplicationNumber;
			searchingAnApplicationNumber(webDriver, applicationNumber1, element,jse);

			isValidApplicationNumber = !BBWebCrawlerUtils.isElementPresent(webDriver, By.xpath("//*[@id=\"ERRORDIV\"]/table/tbody/tr/td"));
			
				if (isValidApplicationNumber) {
					logger.info("Scrapping for application Number " + applicationNumber1);
					element = webDriver.findElement(By.xpath(properties.imageFileWrapperXPath));
					element.click();
					Thread.sleep(Integer.valueOf(properties.ifwImageFileWrapperWaitTime) * 1000L);
					break;
				} else {
					String message = rowIndex == 0
							? MessageFormat.format(
									" No Data found for for WO application number {0}. "
											+ "Looking for other format application number {1}",
									applicationNumber1, otherFormatApplicationNumber)
							: " No Data found for for WO application number in either formats";
					logger.info(message);
				}
			
		}
		return isValidApplicationNumber;
	}

	/**
	 * Check application number.
	 *
	 * @param webDriver the web driver
	 * @param applicationNumber the application number
	 * @return true, if successful
	 * @throws InterruptedException the interrupted exception
	 */
	private boolean checkApplicationNumber(WebDriver webDriver, String applicationNumber) throws InterruptedException {
		WebElement element;
		boolean isValidApplicationNumber;
		isValidApplicationNumber = !BBWebCrawlerUtils.isElementPresent(webDriver, By.id("ERRORDIV"));
		
		if (isValidApplicationNumber) {
			logger.info("Scrapping for application Number " + applicationNumber);
			
			element = webDriver.findElement(By.xpath(properties.imageFileWrapperXPath));
			element.click();
			Thread.sleep(Integer.valueOf(properties.ifwImageFileWrapperWaitTime) * 1000L);
		} else {
			logger.info(" No Data found for application Number " + applicationNumber);
		}
		return isValidApplicationNumber;
	}
	
	/**
	 * Searching an application number.
	 *
	 * @param webDriver the web driver
	 * @param applicationNumber the application number
	 * @param element the element
	 * @param jse 
	 * @throws InterruptedException the interrupted exception
	 */
	private void searchingAnApplicationNumber(WebDriver webDriver, String applicationNumber, WebElement element, JavascriptExecutor jse) 
			throws InterruptedException {
		element.click();

		WebElement element1 = webDriver.findElement(By.id(properties.applicationNumberTextId));
		BBWebCrawlerUtils.setAttributeValue(element1, BBWebCrawlerPropertyConstants.VALUE, applicationNumber, jse);

		WebElement element2 = webDriver.findElement(By.xpath(properties.searchApplicationNumberXPath));
		element2.click();
		Thread.sleep(Integer.valueOf(properties.ifwApplicationSearchTime) * 1000L);
	}
	
	/**
	 * Scrap valid application numbers.
	 *
	 * @param webDriver the web driver
	 * @param applicationNumber the application number
	 * @param actionQueue the action queue
	 * @param jobId the job id
	 * @param pdfPathToDownloadQueueEntity the pdf path to download queue entity
	 * @param correspondenceCrawlerDTO 
	 * @throws InterruptedException the interrupted exception
	 */
	private void scrapValidApplicationNumbers(WebDriver webDriver, String applicationNumber,
			DownloadOfficeActionQueue actionQueue, Long jobId,
			Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity, CorrespondenceCrawlerDTO correspondenceCrawlerDTO) throws InterruptedException {

		
		List<DocumentCode> documentCodes = correspondenceCrawlerDTO.getDocumentCodes();
		
		WebElement element;

		int validRecordsSelectedForApplicationNumber = iterateAndCheckDocuments(webDriver,
				actionQueue,documentCodes);
		 Thread.sleep(5000); // Wait so that all document codes are selected.

		if (validRecordsSelectedForApplicationNumber > 0) {
			logger.info(MessageFormat.format("{0} records selected on UI for application number {1} , id {2}", validRecordsSelectedForApplicationNumber,
					actionQueue.getAppNumberRaw(),actionQueue.getId()));
			clickAndDownloadPDFForEachApplication(webDriver, actionQueue, jobId, pdfPathToDownloadQueueEntity);
		} else {
			logger.info(MessageFormat.format("No records selected on UI for application number {0} , id {1}", actionQueue.getAppNumberRaw(),actionQueue.getId()));
			int currentRetryCount = actionQueue.getRetryCount();
			if (currentRetryCount == correspondenceCrawlerDTO.getMaxRecordRetryCount()) {
				handleMaxRetryCountReachedCase(actionQueue);
			} else if (currentRetryCount < 3) {
				actionQueue.setStatus(QueueStatus.ERROR);
				handleNonMaxTriesReachedCase(actionQueue);
			}
			
		}
		element = webDriver.findElement(By.xpath(properties.selectNewCaseXPath));
		element.click();
		Thread.sleep(3000);
	}
	
	/**
	 * Click and download pdf for each application.
	 *
	 * @param webDriver the web driver
	 * @param actionQueue the action queue
	 * @param jobId the job id
	 * @param pdfPathToDownloadQueueEntity the pdf path to download queue entity
	 * @throws InterruptedException the interrupted exception
	 */
	private void clickAndDownloadPDFForEachApplication(WebDriver webDriver, DownloadOfficeActionQueue actionQueue,
			Long jobId, Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity)
					throws InterruptedException {
		WebElement element;
		element = webDriver.findElement(By.cssSelector(properties.ifwPDFDownloadLink));
		element.click();
		Thread.sleep(Integer.valueOf(properties.pdfDownloadTime) * 1000);

		String relativePath = BlackboxUtils.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath("downloads"));
		File downloadedPDF = BBWebCrawlerUtils.getLatestFileInADirectory(BBWebCrawlerUtils
				.getCompleteFilePath(relativePath, jobId, properties.clientId),
				BBWebCrawerConstants.PDF_SUFFIX);
		File partlyDownloadedPDF = BBWebCrawlerUtils
				.getLatestFileInADirectory(
						BBWebCrawlerUtils.getCompleteFilePath(relativePath, jobId,
								properties.clientId),
				BBWebCrawerConstants.PARTLY_DOWNLOADED_PDF_SUFFIX);

		if (downloadedPDF != null && partlyDownloadedPDF == null) {
			logger.info("Downloaded PDF is " + downloadedPDF.getAbsolutePath());
			String absolutePath = downloadedPDF.getAbsolutePath();
			pdfPathToDownloadQueueEntity.put(absolutePath, actionQueue);
		} else {
			String errorMessage = "PDF is not downloaded properly . The part PDF downloaded(if any) is "
					+ partlyDownloadedPDF == null ? BBWebCrawerConstants.EMPTY_STRING
							: partlyDownloadedPDF.getAbsolutePath();
			logger.info("PDF is not downloaded properly . The part PDF downloaded(if any) is "
					+ partlyDownloadedPDF == null ? BBWebCrawerConstants.EMPTY_STRING
							: partlyDownloadedPDF.getAbsolutePath());
			throw new XMLOrPDFCorruptedException(errorMessage);
		}
	}
	
	/**
	 * Iterate and check documents.
	 *
	 * @param webDriver
	 *            the web driver
	 * @param validRecords
	 *            the valid records
	 * @param actionQueue 
	 * @param documentCodes 
	 * @param documentCodes
	 *            the document codes
	 * @param mailingDates 
	 * @return the int
	 */
	private int iterateAndCheckDocuments(WebDriver webDriver, DownloadOfficeActionQueue actionQueue, List<DocumentCode> documentCodes) {
		int wpsTableShdRowClassRows = webDriver.findElements(By.xpath(properties.paginationRowType1XPath)).size();
		int wpsTableNrmRowClassRows = webDriver.findElements(By.xpath(properties.paginationRowType2XPath)).size();

		int validRecords = 0;
		
		List<String> documentCodesString = new ArrayList<String>();

		if (CollectionUtils.isNotEmpty(documentCodes)) {

			for (DocumentCode documentCode : documentCodes) {
				documentCodesString.add(documentCode.getCode());
			}
		}
		for (int rowIndex = 0; rowIndex < wpsTableNrmRowClassRows; rowIndex++) {
			int checkboxSelectorIndex = rowIndex * 2;
			int mailRoomDateAndDocCodeIndex = 2 * (rowIndex + 1);

			validRecords = validateRecordAndSelectCheckbox(webDriver,checkboxSelectorIndex,
					mailRoomDateAndDocCodeIndex, actionQueue,documentCodesString) ? validRecords + 1 : validRecords;

		}
		
		for (int rowIndex = 0; rowIndex < wpsTableShdRowClassRows; rowIndex++) {
			int cssSelectorIndex = rowIndex * 2 + 1;
			int mailRoomDateAndDocCodeIndex = 2 * (rowIndex + 1) + 1;

			validRecords = validateRecordAndSelectCheckbox(webDriver, cssSelectorIndex,
					mailRoomDateAndDocCodeIndex, actionQueue,documentCodesString) ? validRecords + 1 : validRecords;

		}

		return validRecords;
	}
	
	/**
	 * Validate record and select checkbox.
	 *
	 * @param webDriver
	 *            the web driver
	 * @param documentCodes
	 *            the document codes
	 * @param cssSelectorIndex
	 *            the css selector index
	 * @param mailRoomDateAndDocCodeIndex
	 *            the mail room date and doc code index
	 * @param mailingDates2 
	 * @param actionQueue 
	 * @param documentCodes2 
	 * @return true, if successful
	 */
	private boolean validateRecordAndSelectCheckbox(WebDriver webDriver,
			int cssSelectorIndex, int mailRoomDateAndDocCodeIndex, DownloadOfficeActionQueue actionQueue, List<String> documentCodes2) {
		String mailRoomDateXPath = BlackboxUtils.concat(properties.ifwMailDatePrefixXPath,
				String.valueOf(mailRoomDateAndDocCodeIndex), properties.ifwMailDateSuffixXPath);
		String documentCodeXPath = BlackboxUtils.concat(properties.ifwMailDatePrefixXPath,
				String.valueOf(mailRoomDateAndDocCodeIndex), properties.ifwDocCodeSuffixXPath);
	
		boolean isValidRecord = false;
		
		String mailRoomDateText = webDriver.findElement(By.xpath(mailRoomDateXPath)).getText();
		String documentCodeText = webDriver.findElement(By.xpath(documentCodeXPath)).getText();
		mailRoomDateText = BlackboxDateUtil.changeDateFormat(BBWebCrawerConstants.DATE_FORMAT_PDF,BBWebCrawerConstants.DATE_FORMAT_XML,mailRoomDateText);
		
		String queueMailingDate = actionQueue.getMailingDate() == null ? Constant.ALL_MAILING_DATE : 
			BlackboxDateUtil.calendarToStr(actionQueue.getMailingDate(), TimestampFormat.yyyyMMdd);
		String queueDocumentCode = actionQueue.getDocumentCode();

		if(queueMailingDate.equals(Constant.ALL_MAILING_DATE)) {
			isValidRecord = performDocumentCodeCheck(webDriver, cssSelectorIndex,
					documentCodeText, queueDocumentCode,documentCodes2);
		} else if(queueMailingDate.equals(mailRoomDateText)) {
			isValidRecord = performDocumentCodeCheck(webDriver, cssSelectorIndex, documentCodeText, queueDocumentCode,documentCodes2);
		}
		return isValidRecord;
	}

	/**
	 * Perform document code check.
	 *
	 * @param webDriver the web driver
	 * @param cssSelectorIndex the css selector index
	 * @param isValidRecord the is valid record
	 * @param documentCodeText the document code text
	 * @param queueDocumentCode the queue document code
	 * @param documentCodes2 
	 * @return true, if successful
	 */
	private boolean performDocumentCodeCheck(WebDriver webDriver, int cssSelectorIndex,
			String documentCodeText, String queueDocumentCode, List<String> documentCodes2) {
		boolean isValidRecord = false;
		if(queueDocumentCode.equals(Constant.ALL_DOCUMENT_CODE) && documentCodes2.contains(documentCodeText)) {
			String checkboxSelectorXPath = BlackboxUtils.concat(properties.cssSelectorPrefix,
					String.valueOf(cssSelectorIndex), BBWebCrawerConstants.SUFFIX);
			webDriver.findElement(By.xpath(checkboxSelectorXPath)).click();
			isValidRecord = true;
		} else if(queueDocumentCode.equals(documentCodeText)) {
				String checkboxSelectorXPath = BlackboxUtils.concat(properties.cssSelectorPrefix,
						String.valueOf(cssSelectorIndex), BBWebCrawerConstants.SUFFIX);
				webDriver.findElement(By.xpath(checkboxSelectorXPath)).click();
				isValidRecord = true;
		}
		return isValidRecord;
	}
	
	/**
	 * Handle non max tries reached case.
	 *
	 * @param actionQueue
	 *            the action queue
	 */
	private void handleNonMaxTriesReachedCase(DownloadOfficeActionQueue actionQueue) {
		actionQueue.setRetryCount(actionQueue.getRetryCount() + 1);
		actionQueue.setUpdatedByUser(User.SYSTEM_ID);
		actionQueue.setUpdatedDate(Calendar.getInstance());
		logger.info("Updating the retry count and changing the status in database");
		try {
			downloadOfficeActionQueueRepository.save(actionQueue);
		} catch (DataAccessException dex) {
			logger.error(MessageFormat.format(
					"Some Exception occured while updating retry count and status for download office id {0}. Exception is {1}",
					actionQueue.getId(), dex));
		}
	}

	/**
	 * Handle max retry count reached case.
	 *
	 * @param actionQueue
	 *            the action queue
	 */
	private void handleMaxRetryCountReachedCase(DownloadOfficeActionQueue actionQueue) {
		actionQueue.setStatus(QueueStatus.CRAWLER_ERROR);
		actionQueue.setRetryCount(actionQueue.getRetryCount() + 1);
		actionQueue.setUpdatedByUser(User.SYSTEM_ID);
		actionQueue.setUpdatedDate(Calendar.getInstance());
		logger.info("Updating the retry count in database and sending the notification.");
		try {

			ApplicationBase applicationBase = applicationBaseDAO.findByApplicationNoAndJurisdictionCode(
					actionQueue.getJurisdictionCode(), actionQueue.getApplicationNumberFormatted());

			logger.info(MessageFormat.format(
					"Application Entity id [{0}] fetched for sending notification corresponing to erroneous download queue entry is : ",
					applicationBase == null ? null : applicationBase.getId()));

			if(applicationBase != null) {
			Long notificationProcessId = notificationProcessService.createNotification(applicationBase, null, null, 
					actionQueue.getId(), EntityName.DOWNLOAD_OFFICE_ACTION_QUEUE, 
					NotificationProcessType.CORRESPONDENCE_RECORD_FAILED_IN_DOWNLOAD_QUEUE, null, null);
			
			logger.info(MessageFormat
					.format("Notification created for Download Queue request [notification process id : {0}, queue id : {1},  Application Number : {2}, Jurisdiction : {3}].",
							notificationProcessId, actionQueue.getId(), actionQueue.getApplicationNumberFormatted(),
							actionQueue.getJurisdictionCode()));
			}
			
			downloadOfficeActionQueueRepository.save(actionQueue);
			logger.info("Retry Count and Status succesfully updated for Download office queue id "+actionQueue.getId());
		} catch (Exception ex) {
			logger.error(MessageFormat
					.format("Some Exception occured while updating retry count as 4 and status as CRAWLER_ERROR or sending the notification"
							+ " for download office id {0}." + " Unable to send the notification/persist in DB."
							+ "Exception is {1}", actionQueue.getId(), ex));
		}
	}


}
