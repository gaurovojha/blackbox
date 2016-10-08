package com.blackbox.ids.webcrawler.service.uspto.correspondence.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ErrorResponse;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.OutgoingCorrespondence;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.mstr.AuthenticationData;
import com.blackbox.ids.core.util.BlackboxPropertyUtil;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.XmlParser;
import com.blackbox.ids.dto.CorrespondenceCrawlerDTO;
import com.blackbox.ids.exception.WebCrawlerGenericException;
import com.blackbox.ids.exception.XMLAndUICorrespondenceMisMatchException;
import com.blackbox.ids.exception.XMLOrPDFCorruptedException;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IOutgoingCorrespondenceScrapper;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

@Component("outgoingCorrespondenceScrapper")
public class OutgoingCorrespondenceScrapperImpl implements IOutgoingCorrespondenceScrapper {
	
	private static final Logger	LOGGER	= Logger.getLogger(OutgoingCorrespondenceScrapperImpl.class);

	/** The login service. */
	@Autowired
	private IUSPTOLoginService loginService;
	
	/** The properties. */
	@Autowired
	private BBWebCrawlerPropertyConstants properties;

	@Override
	public boolean scrapAndPrepareData(WebDriver webDriver , List<OutgoingCorrespondence> correspondences, List<String> pdfNames,
			List<DocumentCode> documentCodes, Long crawlerId, CorrespondenceCrawlerDTO correspondenceCrawlerDTO, AuthenticationData epfFile) {

		boolean isAnyRecordsSelectedInUI = false;

		LOGGER.info("About to Login");

		String completePath = BlackboxUtils.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath(BBWebCrawerConstants.AUTHENTICATION_FOLDER),
				epfFile.getAuthenticationFileName(),File.separator);
		loginService.login(webDriver, completePath, epfFile.getPassword());

		LOGGER.info("Logged in Succesfully");
		try {
			applyFiltersAndVerifyCount(correspondences, crawlerId,webDriver,correspondenceCrawlerDTO);

				int totalRecords = correspondenceCrawlerDTO.getCorrespondenceCountInXML();

				if (totalRecords > 0) {
					isAnyRecordsSelectedInUI = processRecordsAndPrepareStagingData(totalRecords, pdfNames,
							documentCodes, correspondences, crawlerId, correspondenceCrawlerDTO,webDriver);
				} else {
					LOGGER.info("No records found for the specified filter condition");
				}
			BBWebCrawlerUtils.logoutAndCloseWebDriver(webDriver, LOGGER);
		} catch (InterruptedException | IOException e) {
			LOGGER.info("Some Generic Exception occured while web crawling . Exception is " + e.getMessage());
			throw new WebCrawlerGenericException(
					"Some Generic Exception occured while web crawling . Exception is " + e.getMessage(), e);
		} catch (NoSuchElementException ex) {
			LOGGER.error("No such element exception occured while fetching response from website . Exception is "
					+ ex.getMessage());
			throw new WebCrawlerGenericException("Some exception occured while fetching response from website", ex);
		} catch (WebDriverException ex) {
			LOGGER.error("Generic Web Driver exception occured while fetching response from website . Exception is "
					+ ex.getMessage());
			throw new WebCrawlerGenericException("Some exception occured while fetching response from website", ex);
		}

		return isAnyRecordsSelectedInUI;
	}
	
	/**
	 * Apply filters and verify count.
	 * @param correspondences
	 *            the correspondences
	 * @param crawlerId
	 * @param webDriver 
	 * @param correspondenceCrawlerDTO 
	 * @return the list
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	private void applyFiltersAndVerifyCount(List<OutgoingCorrespondence> correspondences, Long crawlerId, WebDriver webDriver, CorrespondenceCrawlerDTO correspondenceCrawlerDTO)
			throws InterruptedException {

		WebElement element;
		applyBasicFilters(webDriver);

		element = BBWebCrawlerUtils.getAnchorTagWebElement(properties.xmlDownloadLinkPath, webDriver);
		element.click();
		Thread.sleep(Integer.valueOf(properties.xmlDownloadTime) * 1000L);

		String absolutePath = BlackboxUtils.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath(BBWebCrawerConstants.DOWNLOADS_FOLDER));
		String lookUpFolder = BBWebCrawlerUtils.getCompleteFilePath(absolutePath, crawlerId,
				properties.clientId);
		final File lastDownloadedFile = BBWebCrawlerUtils.getLatestFileInADirectory(lookUpFolder,
				BBWebCrawerConstants.XML_SUFFIX);
		int correspondenceCountInXML = 0;

		if (lastDownloadedFile != null) {
			try {
				BBWebCrawlerUtils
						.getCorrespondenceCountAndMarshalledObject(lastDownloadedFile,correspondenceCrawlerDTO);
				correspondenceCountInXML = correspondenceCrawlerDTO.getCorrespondenceCountInXML();

				correspondences.add((OutgoingCorrespondence) correspondenceCrawlerDTO.getOutgoingCorrespondence());
				LOGGER.info("Correspondence Count in XML is " + correspondenceCountInXML);
			} catch (CannotResolveClassException cex) {
				checkErrorXML(lastDownloadedFile, cex);

			}
			// Click on Search button and waiting
			int searchCountInUI = getCorrespondenceSearchCountFromUIText(webDriver);
			LOGGER.info("Correspondence Count on UI is " + searchCountInUI);

			if (searchCountInUI != correspondenceCountInXML) {
				String errorMessage = MessageFormat.format(
						"Correspondence Count on UI and count in XML did not match for crawler id {0}.Rescheduling the job",
						crawlerId);
				LOGGER.info(errorMessage);
				throw new XMLAndUICorrespondenceMisMatchException(errorMessage);
			}

		} else {
			LOGGER.info(MessageFormat.format(
					"The XML could not be downloaded in the provided time for crawler id {0}. Try increasing the XML download time.",
					crawlerId));
		}

	}
	
	/**
	 * Process records and move to staging tables.
	 * @param webDriver
	 *            the web driver
	 * @param totalRecords
	 *            the total records
	 * @param pdfNames
	 *            the pdf names
	 * @param documentCodeObjects
	 *            the document code objects
	 * @param correspondences
	 * @param crawlerId
	 * @param webDriver 
	 * @param customersInBBX
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private boolean processRecordsAndPrepareStagingData(int totalRecords, List<String> pdfNames,
			List<DocumentCode> documentCodeObjects, List<OutgoingCorrespondence> correspondences, Long crawlerId,
			CorrespondenceCrawlerDTO correspondenceCrawlerDTO, WebDriver webDriver) throws InterruptedException, IOException {
		List<String> documentCodes = new ArrayList<String>();
		boolean isInclusionListOn = StringUtils.equalsIgnoreCase(Constant.ON,BlackboxPropertyUtil.getProperty(Constant.INCLUSION_LIST_ON_FLAG));
		
		correspondenceCrawlerDTO.setTotalRecords(totalRecords);
		correspondenceCrawlerDTO.setInclusionListOn(isInclusionListOn);
		correspondenceCrawlerDTO.setCrawlerId(crawlerId);

		for (DocumentCode code : documentCodeObjects) {
			documentCodes.add(code.getCode());
		}
		return traversePaginationAndSelectDocumentCodes(documentCodes, pdfNames,
				correspondences, correspondenceCrawlerDTO, webDriver);
	}
	
	/**
	 * Apply basic filters.
	 * @param webDriver 
	 */
	private void applyBasicFilters(WebDriver webDriver) {
		WebElement element = webDriver.findElement(By.id(properties.customerSearchName));
		element.click();

		element = webDriver.findElement(By.id(properties.viewOutgoingCorrespondenceId));
		element.click();

		Select dropdown = new Select(webDriver.findElement(By.name(properties.filterPastDaysName)));
		dropdown.selectByValue(properties.filterPastDaysValue);

		dropdown = new Select(webDriver.findElement(By.name(properties.filterSortByName)));
		dropdown.selectByValue(properties.filterSortByValue);

		// find element for element with value "All"
		element = BBWebCrawlerUtils.findElementByValue(properties.filterSearchRadioButtonName,
				properties.filterCustomerNumberValue, webDriver);
		element.click();

		BBWebCrawlerUtils.waitForSpecifiedDuration(15, webDriver);
	}
	
	/**
	 * Traverse pagination and select document codes.
	 * @param totalRecords
	 *            the total records
	 * @param documentCodes
	 *            TODO
	 * @param applicationNumbers
	 *            the application numbers
	 * @param isInclusionListOn
	 *            the is inclusion list on
	 * @param excludedApplicationNumbers
	 *            the excluded application numbers
	 * @param pdfNames
	 *            the pdf names
	 * @param correspondences
	 * @param customersInBBX
	 * @param webDriver 
	 * @param crawlerId
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private boolean traversePaginationAndSelectDocumentCodes(List<String> documentCodes,List<String> pdfNames,
			List<OutgoingCorrespondence> correspondences, CorrespondenceCrawlerDTO correspondenceCrawlerDTO, WebDriver webDriver)
					throws InterruptedException, IOException {
		
		int totalRecords = correspondenceCrawlerDTO.getTotalRecords();
		boolean isInclusionListOn = correspondenceCrawlerDTO.isInclusionListOn();
		Long crawlerId = correspondenceCrawlerDTO.getCrawlerId();
		int numberOfPages = totalRecords / 100;
		boolean isAnyRecordSelectedOnUI = false;

		if (numberOfPages > 0) {
			List<Long> numberOfPagesAndCrawlerId = new ArrayList<Long>(2);
			numberOfPagesAndCrawlerId.add((long) numberOfPages);
			numberOfPagesAndCrawlerId.add(crawlerId);
			int totalMatchingRecords = iteratePagesAndProcess(documentCodes, isInclusionListOn,
					pdfNames, numberOfPagesAndCrawlerId,webDriver,correspondenceCrawlerDTO);

			if (totalMatchingRecords > 0) {
				isAnyRecordSelectedOnUI = true;
			}
		} else {
			LOGGER.info("Only one page found . Checking and selecting document code for the same.");
			int validRecords = checkAndSelectDocumentCodes(documentCodes, isInclusionListOn,
					webDriver,correspondenceCrawlerDTO);
			Thread.sleep(3000);

			if (validRecords > 0) {
				isAnyRecordSelectedOnUI = true;
				BBWebCrawlerUtils.getAnchorTagWebElement(properties.pdfDownloadLinkPath, webDriver).click();
				 Thread.sleep(Integer.valueOf(properties.pdfDownloadTime) * 1000L);

				String absolutePath = BlackboxUtils.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath(BBWebCrawerConstants.DOWNLOADS_FOLDER));
				File downloadedPDF = BBWebCrawlerUtils.getLatestFileInADirectory(BBWebCrawlerUtils.getCompleteFilePath(
						absolutePath, crawlerId, properties.clientId), BBWebCrawerConstants.PDF_SUFFIX);

				File partlyDownloadedPDF = BBWebCrawlerUtils
						.getLatestFileInADirectory(
								BBWebCrawlerUtils.getCompleteFilePath(absolutePath, crawlerId,
										properties.clientId),
						BBWebCrawerConstants.PARTLY_DOWNLOADED_PDF_SUFFIX);

				if (downloadedPDF != null && partlyDownloadedPDF == null) {
					LOGGER.info("Downloaded PDF is " + downloadedPDF.getAbsolutePath());
					pdfNames.add(downloadedPDF.getAbsolutePath());
				} else {
					// Indication of partly downloaded PDF or undownloaded case.
					String errorMessage = "PDF is not downloaded properly . The part PDF downloaded(if any) is "
							+ partlyDownloadedPDF == null ? BBWebCrawerConstants.EMPTY_STRING
									: partlyDownloadedPDF.getAbsolutePath();
					LOGGER.info("PDF is not downloaded properly . The part PDF downloaded(if any) is "
							+ partlyDownloadedPDF == null ? BBWebCrawerConstants.EMPTY_STRING
									: partlyDownloadedPDF.getAbsolutePath());
					throw new XMLOrPDFCorruptedException(errorMessage);
				}
			} else {
				LOGGER.info(
						"No Valid combination of application number and document code found in the current page of UI");
			}
		}
		return isAnyRecordSelectedOnUI;
	}
	

	/**
	 * Check error xml.
	 *
	 * @param lastDownloadedFile the last downloaded file
	 * @param cex the cex
	 * @return the int
	 */
	private void checkErrorXML(final File lastDownloadedFile,
			CannotResolveClassException cex) {
		String error = MessageFormat
				.format("Some exception occured while parsing the outgoing correspondence XML. Exception is {0}. "
						+ "Checking if no data XML has arrived or an error XML has come", cex);
		LOGGER.error(error);
		try {
			ErrorResponse errorObject = (ErrorResponse) XmlParser.fetchXmlData(lastDownloadedFile,
					ErrorResponse.class);
			LOGGER.info(MessageFormat.format("Content of Error XML is {0}",
					errorObject.getError().getErrorMessage()));
		} catch (FileNotFoundException | CannotResolveClassException cex1) {
			String error1 = MessageFormat
					.format("Some exception occured while parsing the no data XML. Exception is {0}. ", cex);
			LOGGER.error(error1);
			throw new XMLOrPDFCorruptedException(error1, cex1);
		}
	}
	
	/**
	 * Iterate pages and process.
	 * @param documentCodes
	 *            the document codes
	 * @param applicationNumbers
	 *            the application numbers
	 * @param isInclusionListOn
	 *            the is inclusion list on
	 * @param excludedApplicationNumbers
	 *            the excluded application numbers
	 * @param pdfNames
	 *            the pdf names
	 * @param customersInBBX
	 * @param numberOfPagesAndCrawlerId
	 * @param webDriver 
	 * @param correspondenceCrawlerDTO 
	 * @return the int
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private int iteratePagesAndProcess(List<String> documentCodes,
			boolean isInclusionListOn, List<String> pdfNames,
			List<Long> numberOfPagesAndCrawlerId, WebDriver webDriver, CorrespondenceCrawlerDTO correspondenceCrawlerDTO)
					throws InterruptedException, IOException {

		int totalMatchingRecordsForAllPages = 0;
		Long numberOfPages = numberOfPagesAndCrawlerId.get(0);
		Long crawlerId = numberOfPagesAndCrawlerId.get(1);
		for (int pageIndex = 0; pageIndex <= numberOfPages; pageIndex++) {

			LOGGER.info("Iterating and selecting document code for Page " + pageIndex);
			int validRecords = checkAndSelectDocumentCodes(documentCodes, isInclusionListOn,
					webDriver,correspondenceCrawlerDTO);
			totalMatchingRecordsForAllPages += validRecords;

			if (validRecords > 0) {

				BBWebCrawlerUtils.getAnchorTagWebElement(properties.pdfDownloadLinkPath, webDriver).click();
				Thread.sleep(5000);

				String absolutePath = BlackboxUtils.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath(BBWebCrawerConstants.DOWNLOADS_FOLDER));
				File downloadedPDF = BBWebCrawlerUtils.getLatestFileInADirectory(BBWebCrawlerUtils.getCompleteFilePath(absolutePath
						, crawlerId, properties.clientId), BBWebCrawerConstants.PDF_SUFFIX);
				File partlyDownloadedPDF = BBWebCrawlerUtils
						.getLatestFileInADirectory(
								BBWebCrawlerUtils.getCompleteFilePath(absolutePath, crawlerId,
										properties.clientId),
						BBWebCrawerConstants.PARTLY_DOWNLOADED_PDF_SUFFIX);

				if (downloadedPDF != null && partlyDownloadedPDF == null) {
					LOGGER.info("Downloaded PDF is " + downloadedPDF.getAbsolutePath());
					pdfNames.add(downloadedPDF.getAbsolutePath());
				} else {
					String errorMessage = "PDF is not downloaded properly . The part PDF downloaded(if any) is "
							+ partlyDownloadedPDF == null ? BBWebCrawerConstants.EMPTY_STRING
									: partlyDownloadedPDF.getAbsolutePath();
					LOGGER.info("PDF is not downloaded properly . The part PDF downloaded(if any) is "
							+ partlyDownloadedPDF == null ? BBWebCrawerConstants.EMPTY_STRING
									: partlyDownloadedPDF.getAbsolutePath());
					throw new XMLOrPDFCorruptedException(errorMessage);
				}
				if (pageIndex != numberOfPages) {
					String nextPageXPathLink = properties.paginationTraversalPrefixXPath + (pageIndex + 1)
							+ properties.paginationTraversalSuffixXPath;
					webDriver.findElement(By.xpath(nextPageXPathLink)).click();
					Thread.sleep(5000);
				}
			} else {
				LOGGER.info(
						"No Valid combination of application number and document code to be checked in the current page");
			}
		}
		return totalMatchingRecordsForAllPages;
	}
	
	/**
	 * Check and select document codes.
	 * @param acceptableDocumentCodes
	 *            the acceptable document codes
	 * @param applicationNumbers
	 *            the application numbers
	 * @param isInclusionListOn
	 *            the is inclusion list on
	 * @param excludedApplicationNumbers
	 *            the excluded application numbers
	 * @param customersInBBX
	 * @param webDriver 
	 * @param correspondenceCrawlerDTO 
	 * @return the int
	 */
	private int checkAndSelectDocumentCodes(List<String> acceptableDocumentCodes,
			boolean isInclusionListOn, WebDriver webDriver, CorrespondenceCrawlerDTO correspondenceCrawlerDTO) {
		
		List<String> customersInBBX = correspondenceCrawlerDTO.getCustomersInBBX();
		List<String> applicationNumbers = correspondenceCrawlerDTO.getInclusionList();
		List<String> excludedApplicationNumbers = correspondenceCrawlerDTO.getExclusionList();
		
		int wpsTableShdRowClassRows = webDriver.findElements(By.cssSelector(properties.paginationRowType1Selector))
				.size();
		int wpsTableNrmRowClassRows = webDriver.findElements(By.cssSelector(properties.paginationRowType2Selector))
				.size();
		int toBeCheckedRecords = 0;
		List<String> docCodeAndAppNumber = null;

		for (int rowIndex = 0; rowIndex < wpsTableShdRowClassRows; rowIndex++) {
			docCodeAndAppNumber = new ArrayList<String>();
			int cssSelectorIndex = (rowIndex + 1) * 2;
			String documentCodeCssSelector = properties.paginationRowType1DocumentPrefix + cssSelectorIndex
					+ properties.paginationRowDocumentCodePrefix;
			String documentCode = webDriver.findElement(By.cssSelector(documentCodeCssSelector)).getText();
			String applicationNumberXPath = properties.paginationApplicationNumberPrefix + cssSelectorIndex
					+ properties.paginationApplicationNumberSuffix;
			String applicationNumberFromUI = webDriver.findElement(By.xpath(applicationNumberXPath)).getText();
			String customerNumberXPath = properties.paginationApplicationNumberPrefix + cssSelectorIndex
					+ properties.paginationCustomerNumberSuffix;
			String customerNumberFromUI = webDriver.findElement(By.xpath(customerNumberXPath)).getText();

			if (!applicationNumberFromUI.startsWith(BBWebCrawerConstants.WO_CODE_INDICATOR)) {
				applicationNumberFromUI = removeSlashAndCommaFromUIString(applicationNumberFromUI);
			}

			docCodeAndAppNumber.add(documentCode);
			docCodeAndAppNumber.add(applicationNumberFromUI);

			if (customersInBBX.contains(customerNumberFromUI)) {
				// Check only for the customer number existing in BBX.
				toBeCheckedRecords += processInclusionExclusionForShd(acceptableDocumentCodes, applicationNumbers,
						isInclusionListOn, excludedApplicationNumbers, rowIndex, docCodeAndAppNumber,webDriver);
			}
		}

		int wpsTableNrmRowIndex = 3;
		int wpsTableNrmCheckboxIndex = 1;
		for (int rowIndex = 0; rowIndex < wpsTableNrmRowClassRows; rowIndex++) {
			docCodeAndAppNumber = new ArrayList<String>();
			String documentCodeCssSelector = properties.paginationRowType2DocPrefix + wpsTableNrmRowIndex
					+ properties.paginationRowDocumentCodePrefix;
			String documentCode = webDriver.findElement(By.cssSelector(documentCodeCssSelector)).getText();
			String applicationNumberXPath = properties.paginationApplicationNumberPrefix + wpsTableNrmRowIndex
					+ properties.paginationApplicationNumberSuffix;
			String applicationNumberFromUI = webDriver.findElement(By.xpath(applicationNumberXPath)).getText();
			String customerNumberXPath = properties.paginationApplicationNumberPrefix + wpsTableNrmRowIndex
					+ properties.paginationCustomerNumberSuffix;
			String customerNumberFromUI = webDriver.findElement(By.xpath(customerNumberXPath)).getText();

			applicationNumberFromUI = removeSlashAndCommaFromUIString(applicationNumberFromUI);
			docCodeAndAppNumber.add(documentCode);
			docCodeAndAppNumber.add(applicationNumberFromUI);

			if (customersInBBX.contains(customerNumberFromUI)) {
				// Check only for the customer number existing in BBX.
				toBeCheckedRecords += processInclusionExclusionForNrmRows(acceptableDocumentCodes, applicationNumbers,
						isInclusionListOn, excludedApplicationNumbers, wpsTableNrmCheckboxIndex, docCodeAndAppNumber,webDriver);
			}
			wpsTableNrmRowIndex += 2;
			wpsTableNrmCheckboxIndex += 2;
		}

		return toBeCheckedRecords;
	}
	
	/**
	 * Removes the slash and comma from ui string.
	 * @param applicationNumberFromUI
	 *            the application number from ui
	 * @return the string
	 */
	private String removeSlashAndCommaFromUIString(String applicationNumberFromUI) {
		if (!applicationNumberFromUI.startsWith(BBWebCrawerConstants.WO_CODE_INDICATOR)) {
			// Removing "/" and "," from application numberin UI to match with XML in case of US country.

			String applicationNumberFromUI1 = applicationNumberFromUI.replaceAll(BBWebCrawerConstants.FRONT_SLASH,
					BBWebCrawerConstants.EMPTY_STRING);
			applicationNumberFromUI = applicationNumberFromUI1.replaceAll(BBWebCrawerConstants.COMMA_STRING,
					BBWebCrawerConstants.EMPTY_STRING);
		}
		return applicationNumberFromUI;
	}
	
	/**
	 * Process inclusion exclusion for nrm rows.
	 * @param acceptableDocumentCodes
	 *            the acceptable document codes
	 * @param applicationNumbers
	 *            the application numbers
	 * @param isInclusionListOn
	 *            the is inclusion list on
	 * @param excludedApplicationNumbers
	 *            the excluded application numbers
	 * @param toBeCheckedRecords
	 *            the to be checked records
	 * @param wpsTableNrmCheckboxIndex
	 *            the wps table nrm checkbox index
	 * @param docCodeAndAppNumber
	 * @param webDriver 
	 * @param documentCode
	 *            the document code
	 * @param applicationNumber
	 *            the application number
	 * @return the int
	 */
	private int processInclusionExclusionForNrmRows(List<String> acceptableDocumentCodes,
			List<String> applicationNumbers, boolean isInclusionListOn, List<String> excludedApplicationNumbers,
			int wpsTableNrmCheckboxIndex, List<String> docCodeAndAppNumber, WebDriver webDriver) {
		String documentCode = docCodeAndAppNumber.get(0);
		String applicationNumber = docCodeAndAppNumber.get(1);
		int toBeCheckedRecords = 0;
		if (isInclusionListOn) {
			if (applicationNumbers.contains(applicationNumber) && acceptableDocumentCodes.contains(documentCode)) {
				String checkBoxId = properties.paginationCheckBoxPrefix + wpsTableNrmCheckboxIndex;
				webDriver.findElement(By.id(checkBoxId)).click();
				toBeCheckedRecords = 1;
			}
		} else {
			if (!excludedApplicationNumbers.contains(applicationNumber)
					&& acceptableDocumentCodes.contains(documentCode)) {
				String checkBoxId = properties.paginationCheckBoxPrefix + wpsTableNrmCheckboxIndex;
				webDriver.findElement(By.id(checkBoxId)).click();
				toBeCheckedRecords = 1;
			}
		}
		return toBeCheckedRecords;
	}

	/**
	 * Process inclusion exclusion for shd.
	 * @param acceptableDocumentCodes
	 *            the acceptable document codes
	 * @param applicationNumbers
	 *            the application numbers
	 * @param isInclusionListOn
	 *            the is inclusion list on
	 * @param excludedApplicationNumbers
	 *            the excluded application numbers
	 * @param webDriver 
	 * @param toBeCheckedRecords
	 *            the to be checked records
	 * @param documentCode
	 *            the document code
	 * @param applicationNumber
	 *            the application number
	 * @return the int
	 */
	private int processInclusionExclusionForShd(List<String> acceptableDocumentCodes, List<String> applicationNumbers,
			boolean isInclusionListOn, List<String> excludedApplicationNumbers, int rowIndex,
			List<String> docCodeAndAppNumber, WebDriver webDriver) {
		String documentCode = docCodeAndAppNumber.get(0);
		String applicationNumber = docCodeAndAppNumber.get(1);
		int toBeCheckedRecords = 0;
		if (isInclusionListOn) {
			if (applicationNumbers.contains(applicationNumber) && acceptableDocumentCodes.contains(documentCode)) {
				String checkBoxId = properties.paginationCheckBoxPrefix + rowIndex * 2;
				webDriver.findElement(By.id(checkBoxId)).click();
				toBeCheckedRecords += 1;
			}
		} else {
			if (!excludedApplicationNumbers.contains(applicationNumber)
					&& acceptableDocumentCodes.contains(documentCode)) {
				String checkBoxId = properties.paginationCheckBoxPrefix + rowIndex * 2;
				webDriver.findElement(By.id(checkBoxId)).click();
				toBeCheckedRecords += 1;
			}

		}
		return toBeCheckedRecords;
	}

	/**
	 * Gets the correspondence search count from ui text.
	 * @param webDriver 
	 * @return the correspondence search count from ui text
	 */
	private int getCorrespondenceSearchCountFromUIText(WebDriver webDriver) {
		final WebElement searchCustomer = webDriver.findElement(By.id(properties.submitCustomerName));
		
		JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		BBWebCrawlerUtils.executeClickEvent(searchCustomer, jse);

		String text = null;
		try {
			text = webDriver.findElement(By.xpath(properties.correspondenceUICount)).getText();
			text = text.split(BBWebCrawerConstants.SPACE_STRING)[0];
		} catch (final NoSuchElementException e) {
			text = BBWebCrawerConstants.ZERO_STRING;
			LOGGER.error("Exception occured while fetching correspondence count from UI. Exception is " + e);
		}
		return Integer.parseInt(text);
	}

}
