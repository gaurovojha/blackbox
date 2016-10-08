package com.blackbox.ids.webcrawler.service.uspto.ids.impl;

import java.io.File;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.ids.IIDSWebCrawler;

@Component
public class IDSWebCrawlerImpl implements IIDSWebCrawler {
	
	private static final Logger LOGGER = Logger.getLogger(IDSWebCrawlerImpl.class);
	
	private static final String	SELECTOR_IMAGE_FILE_WRAPPER_TAB	= "a[href*='ifwtab']";
	private static final String	SELECTOR_IFW_DATA	= "ifwinnertable";
	private static final String	SELECTOR_TABLE_COLUMN	= "td";
	private static final String	SELECTOR_TABLE_ROW	= "tr";
	private static final String	SELECTOR_INPUT_ELEMENT	= "input";
	private static final String	SELCTOR_IFW_PDF_DOWNLOAD_LINK	= "#buttonsID > a";
	
	/** The properties. */
	@Autowired
	private BBWebCrawlerPropertyConstants properties;
	
	@Override
	public boolean checkImageFileWrapperTab(WebDriver webDriver) {
		boolean isTabPresent = false;
		By imageFileWrapperCssSelector = By.cssSelector(SELECTOR_IMAGE_FILE_WRAPPER_TAB);
		isTabPresent = BBWebCrawlerUtils.isElementPresent(webDriver, imageFileWrapperCssSelector);
		if (isTabPresent) {
			webDriver.findElement(imageFileWrapperCssSelector).click();
			BBWebCrawlerUtils.waitForSpecifiedDuration(10, webDriver);
		}
		return isTabPresent;
	}

	@Override
	public Map<String, Calendar> readIFWData(WebDriver webDriver) {
		Map<String, Calendar> ifwData = new HashMap<String, Calendar>();
		WebElement baseTable = webDriver.findElement(By.id(SELECTOR_IFW_DATA));
		List<WebElement> tableRows = baseTable.findElements(By.tagName(SELECTOR_TABLE_ROW));
		if (CollectionUtils.isNotEmpty(tableRows)) {
			for (WebElement childRow : tableRows) {
				List<WebElement> columns = childRow.findElements(By.tagName(SELECTOR_TABLE_COLUMN));
				if (CollectionUtils.isNotEmpty(columns)) {
					String mailRoomDateTxt = columns.get(0).getText();
					String documentCode = columns.get(1).getText();
					Calendar mailRoomDate = BlackboxDateUtil.getCalendar(mailRoomDateTxt);
					
					Calendar existingRecordDate = ifwData.get(documentCode);
					if (existingRecordDate == null || mailRoomDate.compareTo(existingRecordDate) > 0) {
						ifwData.put(documentCode, mailRoomDate);
					}
				}
			}
		}
		return ifwData;
	}
	
	@Override
	public int selectIDSIFWDocuments(final WebDriver webDriver, final Calendar idsDate, final String idsDocumentCode) {
		LOGGER.info(MessageFormat.format("Selecting IDS documents on IFW post ids date{0}",
				BlackboxDateUtil.calendarToStr(idsDate, TimestampFormat.MM_DD_YYYY)));
		int selectedDocCount = 0;
		WebElement baseTable = webDriver.findElement(By.id(SELECTOR_IFW_DATA));
		List<WebElement> tableRows = baseTable.findElements(By.tagName(SELECTOR_TABLE_ROW));
		if (CollectionUtils.isNotEmpty(tableRows)) {
			for (WebElement childRow : tableRows) {
				List<WebElement> columns = childRow.findElements(By.tagName(SELECTOR_TABLE_COLUMN));
				if (CollectionUtils.isNotEmpty(columns)) {
					String mailRoomDateTxt = columns.get(0).getText();
					String documentCode = columns.get(1).getText();
					Calendar mailRoomDate = BlackboxDateUtil.getCalendar(mailRoomDateTxt);
					
					if (idsDocumentCode.equalsIgnoreCase(documentCode) && mailRoomDate.compareTo(idsDate) >= 0) {
						columns.get(5).findElement(By.tagName(SELECTOR_INPUT_ELEMENT)) .click();
						selectedDocCount++;
					}
				}
			}
		}
		// Wait so that all document codes are selected.
		BBWebCrawlerUtils.waitForSpecifiedDuration(5);
		LOGGER.info(MessageFormat.format("IFW - Selected IDS document count : {0}", selectedDocCount));
		return selectedDocCount;
	}
	
	@Override
	public boolean downloadIDSPDF(WebDriver webDriver, String downloadDir, String formattedAppNumber) {
		LOGGER.info(MessageFormat.format("Downloading IDS pdf to directory{0}", downloadDir));
		WebElement downloadLink = webDriver.findElement(By.cssSelector(SELCTOR_IFW_PDF_DOWNLOAD_LINK));
		downloadLink.click();
		BBWebCrawlerUtils.waitForSpecifiedDuration(Long.valueOf(properties.pdfDownloadTime));

		String downloadedPDFName = formattedAppNumber + BBWebCrawerConstants.PDF_SUFFIX_WITH_DOT;
		File downloadedPDF = new File(downloadDir, downloadedPDFName);
		File partlyDownloadedPDF = new File(downloadDir, downloadedPDFName + BBWebCrawerConstants.PART_FILE_EXTENSION);

		if (downloadedPDF.exists() && !partlyDownloadedPDF.exists()) {
			LOGGER.info("Downloaded PDF is " + downloadedPDF.getAbsolutePath());
			return true;
		} else {
			LOGGER.error(MessageFormat.format("PDF is not downloaded properly for application number({0}).", formattedAppNumber));
		}		
		return false;
	}

}
