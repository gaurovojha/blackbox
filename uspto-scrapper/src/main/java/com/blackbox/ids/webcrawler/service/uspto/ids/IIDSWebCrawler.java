package com.blackbox.ids.webcrawler.service.uspto.ids;

import java.util.Calendar;
import java.util.Map;

import org.openqa.selenium.WebDriver;

public interface IIDSWebCrawler {

	boolean checkImageFileWrapperTab(WebDriver webDriver);

	Map<String, Calendar> readIFWData(WebDriver webDriver);

	int selectIDSIFWDocuments(WebDriver webDriver, Calendar idsDate, String idsDocumentCode);

	boolean downloadIDSPDF(WebDriver webDriver, String downloadDir, String formattedAppNumber);

}
