package com.blackbox.ids.webcrawler.service.uspto.login.impl;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.common.constant.Constant;
import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.mstr.AuthenticationData;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.repository.CustomerRepository;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.exception.AuthenticationFailureException;
import com.blackbox.ids.exception.LoginHostNotFoundException;
import com.blackbox.ids.exception.USPTOUpdationException;
import com.blackbox.ids.exception.WebCrawlerGenericException;
import com.blackbox.ids.services.common.EmailService;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;

@Service
public class USPTOLoginServiceImpl implements IUSPTOLoginService {

	/** The logger. */
	private static final Log				LOGGER						= LogFactory
																				.getLog(USPTOLoginServiceImpl.class);

	/** The Constant UPDATION_TEXT. */
	public static final String				UPDATION_TEXT				= "US Private Pair is UPDATING NOW";

	/** The Constant AUTHENTICATION_ERROR_TEXT. */
	private static final String				AUTHENTICATION_ERROR_TEXT	= "Authenticate User Bad Input";

	@Autowired
	private CustomerRepository				customerRepository;

	/** The constants. */
	@Autowired
	private BBWebCrawlerPropertyConstants	properties;
	
	@Autowired
	private EmailService emailService;

	@Override
	public void login(final WebDriver webDriver, String epfFilePath, String password) {

		try {
			JavascriptExecutor jse = (JavascriptExecutor) webDriver;
			WebElement element;

			partialLoginAction(webDriver, epfFilePath, password, jse);

			Thread.sleep(Integer.valueOf(properties.loginAuthenticationTimeInSeconds) * 1000L);

			checkAuthenticationFailureAndThrowException(webDriver);

			LOGGER.info("Looking for USPTO Updation Text");
			element = webDriver.findElement(By.cssSelector(properties.notificationTextSelector));

			String notificationText = element.getText();
			String textToSearchFor = UPDATION_TEXT;

			checkUpdationNotificationAndThrowException(notificationText, textToSearchFor);
		} catch (InterruptedException e) {
			throw new WebCrawlerGenericException("Some exception occured while thread sleeping", e);
		}
	}

	/**
	 * Partial login action.
	 *
	 * @param webDriver the web driver
	 * @param epfFilePath the epf file path
	 * @param password the password
	 * @param jse the jse
	 * @throws InterruptedException the interrupted exception
	 */
	private void partialLoginAction(final WebDriver webDriver, String epfFilePath, String password,
			JavascriptExecutor jse) throws InterruptedException {
		WebElement element;
		try {
			LOGGER.info("Clicking Login URL");
			webDriver.get(properties.usptoLoginURL1);

			BBWebCrawlerUtils.waitForSpecifiedDuration(5, webDriver);

			LOGGER.info("Clicking Private Pair Link");
			element = BBWebCrawlerUtils.getAnchorTagWebElement(properties.usptoLoginURL2, webDriver);
			BBWebCrawlerUtils.executeClickEvent(element, jse);

			LOGGER.info("Finding Frame on the Authentication Page");
			webDriver.switchTo().frame(webDriver.findElement(By.xpath(properties.usptoLoginFrameName)));

			LOGGER.info("Finding Browse button on the Authentication Page");
			element = webDriver.findElement(By.name(properties.browseButton));

			BBWebCrawlerUtils.waitForSpecifiedDuration(60, webDriver);

			LOGGER.info("Finding user name field on the Authentication Page");
			element = webDriver.findElement(By.name("username"));
			element.sendKeys(epfFilePath);

			LOGGER.info("Finding password field on the Authentication Page");
			element = webDriver.findElement(By.name(properties.passwordName));
			element.sendKeys(password);
			Thread.sleep(2000);

			// checking checkbox
			webDriver.findElement(By.name(properties.acceptCheckBoxName)).click();

			LOGGER.info("About to click the authentication button");
			webDriver.findElement(By.name(properties.authenticateButtonName)).click();

		} catch (NoSuchElementException nsex) {
			String errorMessage = "Some element not found during login . Exception is " + prepareErrorMessage(nsex);
			LOGGER.error(errorMessage);
			throw new LoginHostNotFoundException(errorMessage, nsex);
		} catch (WebDriverException wdex) {
			String errorMessage = "Some web driver exception found during login . Exception is "
					+ prepareErrorMessage(wdex);
			LOGGER.error(errorMessage);
			throw new LoginHostNotFoundException(errorMessage, wdex);
		}
	}
	
	@Override
	public boolean login(final WebDriver webDriver, Customer customer) {
		try {
			AuthenticationData epfFile = customer.getAuthenticationData();
			String completePath = BlackboxUtils.concat(
					FolderRelativePathEnum.CRAWLER.getAbsolutePath(BBWebCrawerConstants.AUTHENTICATION_FOLDER),
					epfFile.getAuthenticationFileName(), File.separator);
			login(webDriver, completePath, epfFile.getPassword());
			return true;
		} catch (AuthenticationFailureException e) {
			LOGGER.error(MessageFormat.format("Login authentication failed for the cutomer[{0}]. Exception is {1}",
					customer.getCustomerNumber(), prepareErrorMessage(e)));
			sendEmail();
			return false;
		}
	}

	@Override
	public boolean login(final WebDriver webDriver, String customerNumber) {
		Customer customer = customerRepository.findCustomerAuthenticationData(customerNumber);
		if (customer == null || customer.getAuthenticationData() == null) {
			LOGGER.error(MessageFormat.format("Customer [{0}] doesn't exist in system.", customer));
			sendEmail();
			return false;
		}
		return login(webDriver, customer);
	}

	/**
	 * Check authentication failure and throw exception.
	 * @param webDriver
	 *            the web driver
	 */
	private void checkAuthenticationFailureAndThrowException(final WebDriver webDriver) {
		LOGGER.info("Looking for Authentication Success Text");
		boolean isNonAuthenticationErrorTextFound = webDriver.findElements(
				By.xpath(properties.authenticationSuccessText)).size() > 0 ? true : false;

		if (!isNonAuthenticationErrorTextFound) {
			LOGGER.info("Looking for Authentication Failure Text");
			List<WebElement> webElements = webDriver.findElements(By.xpath(properties.authenticationFailureText));
			boolean isAuthenticationErrorTextFound = webElements.size() > 0 ? true : false;
			String authenticationFailureText = isAuthenticationErrorTextFound ? webElements.get(0).getText()
					: BBWebCrawerConstants.EMPTY_STRING;

			if (isAuthenticationErrorTextFound || authenticationFailureText.indexOf(AUTHENTICATION_ERROR_TEXT) > -1) {
				LOGGER.error("User Authentication Failed");
				throw new AuthenticationFailureException("User Authentication Failed");
			} else {
				LOGGER.error("USPTO Site is Updating .");
				throw new USPTOUpdationException("USPTO Site is Updating .");
			}
		}
	}

	/**
	 * Check updation notification and retry.
	 * @param notificationText
	 *            the notification text
	 * @param textToSearchFor
	 *            the text to search for
	 */
	private void checkUpdationNotificationAndThrowException(String notificationText, String textToSearchFor) {
		boolean isUpdating = notificationText.indexOf(textToSearchFor) > -1 ? true : false;
		if (isUpdating) {
			throw new USPTOUpdationException("USPTO Site is Updating . Throwing the exception ");
		}
	}

	/**
	 * Prepare error message.
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
	 * Prepare and send authentication failure email.
	 * 
	 */
	private void sendEmail() {
		Message message = new Message();
		String emailMessage = properties.authenticationFailureEmailContent;
		message.setTemplateType(TemplateType.CRAWLER_AUTHENTICATION);
		message.setReceiverList(properties.adminEmailId);
		message.setStatus(Constant.MESSAGE_STATUS_NOT_SENT);

		MessageData templateData = new MessageData("body", emailMessage);
		List<MessageData> list = new ArrayList<>(1);
		list.add(templateData);
		emailService.send(message, list);

	}

}
