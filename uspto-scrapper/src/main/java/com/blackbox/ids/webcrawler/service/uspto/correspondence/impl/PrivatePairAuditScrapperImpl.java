package com.blackbox.ids.webcrawler.service.uspto.correspondence.impl;

import java.io.File;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.mstr.AuthenticationData;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.exception.WebCrawlerGenericException;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IPrivatePairAuditScrapper;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;

@Component("privatePairAuditScrapperImpl")
public class PrivatePairAuditScrapperImpl implements IPrivatePairAuditScrapper {
	
	private static final Logger	LOGGER	= Logger.getLogger(PrivatePairAuditScrapperImpl.class);

	/** The login service. */
	@Autowired
	private IUSPTOLoginService loginService;
	
	/** The properties. */
	@Autowired
	private BBWebCrawlerPropertyConstants properties;
	
	@Override
	public void performScrapping(AuthenticationData epfFile, String absolutePath, WebCrawlerJobAudit webCrawlerJobAudit) {
		LOGGER.info("About to Login");

		String clientId = properties.clientId;
		String downloadDir = BBWebCrawlerUtils.getCompleteFilePath(absolutePath, webCrawlerJobAudit.getId(),
				clientId);
		
		String completePath = BlackboxUtils.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath(BBWebCrawerConstants.AUTHENTICATION_FOLDER),
				epfFile.getAuthenticationFileName(),File.separator);
		WebDriver webDriver = null;
		 
		try {
		webDriver = BBWebCrawlerUtils.getWebDriver(downloadDir);
		loginService.login(webDriver, completePath, epfFile.getPassword());

		LOGGER.info("Logged in Succesfully");

		scrapAndDownloadXML(webDriver);
		} catch (InterruptedException e) {
			String error = MessageFormat.format("Some exception occured while crawling . Exception is {1}", e);
			LOGGER.error(error);
			throw new WebCrawlerGenericException(error);
		} finally {
		  BBWebCrawlerUtils.logoutAndCloseWebDriver(webDriver, LOGGER);
		}
	}
	/**
	 * Scrap and download xml.
	 *
	 * @param webDriver the web driver
	 * @throws InterruptedException 
	 * @throws  
	 * @throws  
	 * @throws InterruptedException the interrupted exception
	 */
	private void scrapAndDownloadXML(WebDriver webDriver) throws InterruptedException {
		WebElement element = webDriver.findElement(By.id(properties.customerSearchName));
		element.click();
		BBWebCrawlerUtils.waitForSpecifiedDuration(15, webDriver);

		element = webDriver.findElement(By.id(properties.viewOutgoingCorrespondenceId));
		element.click();

		Select dropdown = new Select(webDriver.findElement(By.name(properties.filterPastDaysName)));
		dropdown.selectByValue(properties.filterPastDaysPrivatePaiValue);

		dropdown = new Select(webDriver.findElement(By.name(properties.filterSortByName)));
		dropdown.selectByValue(properties.filterSortByValue);

		// find element for element with value "All"
		element = BBWebCrawlerUtils.findElementByValue(properties.filterSearchRadioButtonName,
				properties.filterCustomerNumberValue, webDriver);
		element.click();

		element = BBWebCrawlerUtils.getAnchorTagWebElement(properties.xmlDownloadLinkPath, webDriver);
		element.click();
		 Thread.sleep(Integer.valueOf(properties.xmlDownloadTime) * 1000L);
	}
}
