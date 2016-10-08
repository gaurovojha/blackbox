package com.blackbox.ids.webcrawler.service.uspto.correspondence;

import java.util.Map;

import org.openqa.selenium.WebDriver;

public interface IEPOWebCrawler {

	void scrapApplicationDocuments(String applicationNumber, WebDriver webDriver);

	Map<String, String> processAllDocuments(WebDriver webDriver);

	void downLoadDocument(String xPath, WebDriver webDriver);

	boolean checkForSearchResult(WebDriver webDriver);

}
