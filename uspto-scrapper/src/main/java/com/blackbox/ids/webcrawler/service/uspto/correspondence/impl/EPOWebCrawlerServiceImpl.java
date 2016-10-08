package com.blackbox.ids.webcrawler.service.uspto.correspondence.impl;

import static com.blackbox.ids.util.constant.BBWebCrawerConstants.A_TAG;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.ID_TAG;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.TD_TAG;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.TR_TAG;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.UNDER_SCORE;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.blackbox.ids.exception.LoginHostNotFoundException;
import com.blackbox.ids.exception.WebCrawlerGenericException;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IEPOWebCrawler;

@Configuration
@Service("ePOWebCrawlerService")
public class EPOWebCrawlerServiceImpl implements IEPOWebCrawler {

	private static final Log LOGGER = LogFactory.getLog(EPOWebCrawlerServiceImpl.class);

	@Value("${epo.base.url_suffix}")
	private String suffixURL;

	@Value("${epo.base.url_prefix}")
	private String prefixURL;

	@Value("${epo.search.text}")
	private String searchText;

	@Value("${epo.filedownload.link}")
	private String downLoadLink;

	@Value("${epo.data.table}")
	private String xPathTable;

	@Value("${epo.no.search.result}")
	private String noSearchResult;

	@Override
	public void scrapApplicationDocuments(String applicationNumber, WebDriver webDriver) {
		String xPath = searchText;

		try {
			LOGGER.info("Fetching Records for " + applicationNumber);
			String trimmedNumber = applicationNumber.split("\\.")[0];
			String url = prefixURL + trimmedNumber + suffixURL;

			LOGGER.info("Opening URL:" + url);
			webDriver.get(url);

		} catch (NoSuchElementException e) {
			LOGGER.error(MessageFormat.format("Web Element with XPath {0} could not found on Page", xPath));
			throw new WebCrawlerGenericException("Web Element not Found", e);
		} catch (WebDriverException e) {
			LOGGER.error(e);
			throw new LoginHostNotFoundException("EPTO is down", e);
		}
	}

	/**
	 * This method scraps all the table Data and tableData map is populated
	 * where key is a combination of date + "_" + documentName and value is id
	 * of hyper link. This id is needed for downloading the PDF.
	 */
	@Override
	public Map<String, String> processAllDocuments(WebDriver webDriver) {
		Map<String, String> documentMap = new HashMap<String, String>();

		WebElement elementTable = null;
		String searchKey = null;
		try {
			searchKey = xPathTable;
			elementTable = webDriver.findElement(By.xpath(searchKey));
			searchKey = TR_TAG;
			List<WebElement> tableRows = elementTable.findElements(By.tagName(searchKey));

			for (int i = 2; i < tableRows.size(); i++) {
				searchKey = TD_TAG;
				List<WebElement> rowCols = tableRows.get(i).findElements(By.tagName(searchKey));
				String date = null;
				String linkIdentifier = null;
				String documentName = null;

				for (int j = rowCols.size(); j >= 0; j--) {
					if (j == 1) {
						date = rowCols.get(j).getText();
					}
					if (j == 2) {
						searchKey = A_TAG;
						WebElement link = rowCols.get(j).findElement(By.tagName(searchKey));

						searchKey = ID_TAG;
						linkIdentifier = link.getAttribute(searchKey);
						documentName = rowCols.get(j).getText();
					}
				}
				documentMap.put(date + UNDER_SCORE + documentName, linkIdentifier);
			}
		} catch (NoSuchElementException e) {
			LOGGER.error(MessageFormat.format("Web Element with key  could not found on Page", searchKey));
			throw new WebCrawlerGenericException("Web Element not Found" + e);
		}

		return documentMap;
	}

	@Override
	public void downLoadDocument(String xPath, WebDriver webDriver) {
		long sleepTime = 6000L;
		try {
			LOGGER.debug("Xpath for downloading the file is: " + xPath);
			WebElement element = null;
			WebElement checkBox = webDriver.findElement(By.cssSelector(xPath));
			checkBox.click();

			element = webDriver.findElement(By.xpath(downLoadLink));

			element.click();
			// Assuming file gets downloaded
			Thread.sleep(sleepTime);
			// unchecking the checked Checkbox
			checkBox.click();
		} catch (InterruptedException e) {
			LOGGER.error("InterruptedException while sleeping Thread" + e);
			throw new WebCrawlerGenericException("InterruptedException while sleeping Thread" + e);
		}

	}

	@Override
	public boolean checkForSearchResult(WebDriver webDriver) {
		List<WebElement> elements = webDriver.findElements(By.xpath(noSearchResult));
		return elements.isEmpty();
	}
}
