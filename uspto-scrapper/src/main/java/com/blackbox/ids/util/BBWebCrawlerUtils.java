package com.blackbox.ids.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.activiti.bpmn.exceptions.XMLException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationCorrespondence;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationCorrespondenceData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.DocumentData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.DocumentList;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.OutgoingCorrespondence;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.PatentApplicationList;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.util.BlackboxPropertyUtil;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.RegexValidatorUtil;
import com.blackbox.ids.core.util.XmlParser;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.dto.CorrespondenceCrawlerDTO;
import com.blackbox.ids.exception.WebCrawlerGenericException;
import com.blackbox.ids.exception.XMLOrPDFCorruptedException;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.google.common.base.Function;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

// TODO: Auto-generated Javadoc
/**
 * The Class BBScraperUtils contains all the util methods required for all scrapping activities.
 */
public class BBWebCrawlerUtils extends BBWebCrawlerPropertyConstants {

	
	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(BBWebCrawlerUtils.class);
	
	
	/**
	 * Find element by value.
	 *
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 * @param webDriver
	 *            the web driver
	 * @return the web element
	 */
	public static WebElement findElementByValue(final String name, final String value, final WebDriver webDriver) {

		final List<WebElement> elements = webDriver.findElements(By.name(name));
		for (final WebElement element : elements) {
			if (element.getAttribute("value").equalsIgnoreCase(value)) {
				return element;
			}
		}
		return null;
	}

	/**
	 * Execute java script.
	 *
	 * @param element
	 *            the element
	 * @param htmlAttribute
	 *            the html attribute
	 * @param value
	 *            the value
	 * @param jse
	 *            the jse
	 */
	public static void executeJavaScript(final WebElement element, final String htmlAttribute, final String value,
			final JavascriptExecutor jse) {
		jse.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, htmlAttribute, value);
	}

	/**
	 * Execute click event.
	 *
	 * @param element
	 *            the element
	 * @param jse
	 *            the jse
	 */
	public static void executeClickEvent(final WebElement element, final JavascriptExecutor jse) {
		jse.executeScript("arguments[0].click();", element);
	}

	/**
	 * Sets the default waiting.
	 *
	 * @param locator
	 *            the locator
	 * @param webDriver
	 *            the web driver
	 * @param timeout
	 *            the timeout
	 * @return the web element
	 */
	public static WebElement setDefaultWaiting(final By locator, final WebDriver webDriver, final int timeout) {
		final Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(timeout, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		final WebElement element = wait.until(new Function<WebDriver, WebElement>() {
			@Override
			public WebElement apply(final WebDriver driver) {
				return driver.findElement(locator);
			}
		});

		return element;
	}

	/**
	 * Wait for specified duration.
	 *
	 * @param timeout
	 *            the timeout
	 * @param webDriver
	 *            the web driver
	 */

	public static void waitForSpecifiedDuration(final int timeout, final WebDriver webDriver) {
		webDriver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	}

	/**
	 * Sets the attribute value.
	 *
	 * @param element
	 *            the element
	 * @param htmlAttribute
	 *            the html attribute
	 * @param value
	 *            the value
	 * @param jse
	 *            the jse
	 */

	public static void setAttributeValue(final WebElement element, final String htmlAttribute, final String value,
			final JavascriptExecutor jse) {

		final String scriptSetAttrValue = "arguments[0].setAttribute(arguments[1],arguments[2])";
		jse.executeScript(scriptSetAttrValue, element, htmlAttribute, value);
	}

	/**
	 * Gets the pagination size.
	 *
	 * @param xPath
	 *            the x path
	 * @param webDriver
	 *            the web driver
	 * @return the pagination size
	 */

	public int getPaginationSize(final String xPath, final WebDriver webDriver) {
		final List<WebElement> pagination = webDriver.findElements(By.xpath(xPath));
		return pagination.size();
	}

	/**
	 * Gets the anchor tag web element.
	 *
	 * @param hyperLinkURL
	 *            the hyper link url
	 * @param webDriver
	 *            the web driver
	 * @return the anchor tag web element
	 */
	public static WebElement getAnchorTagWebElement(final String hyperLinkURL, final WebDriver webDriver) {
		final List<WebElement> anchors = webDriver.findElements(By.tagName("a"));
		final Iterator<WebElement> i = anchors.iterator();
		WebElement webElement = null;

		while (i.hasNext()) {
			final WebElement anchor = i.next();
			if (anchor.getAttribute("href").contains(hyperLinkURL)) {
				webElement = anchor;
				break;
			}
		}

		return webElement;
	}

	/**
	 * Gets the latest file in a directory.
	 *
	 * @param dirPath
	 *            the dir path
	 * @param fileSuffix
	 *            the file suffix
	 * @return the latest file in a directory
	 */
	public static File getLatestFileInADirectory(final String dirPath, final String fileSuffix) {
		final File dir = new File(dirPath);
		final File[] files = getFilesWithinDirectoryUsingSuffix(fileSuffix, dir);
		if (files == null || files.length == 0) {
			return null;
		}

		long lastCreatedFile = 0;
		long currentFileCreatedTime = 0;
		File lastFileCreated = files[0];

		for (final File file : files) {
			final Path path = Paths.get(file.getAbsolutePath());
			BasicFileAttributes attr;

			try {
				attr = Files.readAttributes(path, BasicFileAttributes.class);
				currentFileCreatedTime = attr.creationTime().toMillis();

				if (lastCreatedFile < currentFileCreatedTime) {
					lastCreatedFile = currentFileCreatedTime;
					lastFileCreated = file;
				}
			} catch (final IOException e) {
				throw new XMLException("Some exception occured while getting the last created file in directory", e);
			}
		}
		return lastFileCreated;
	}

	/**
	 * Gets the files within directory using suffix.
	 *
	 * @param fileSuffix
	 *            the file suffix
	 * @param dir
	 *            the dir
	 * @return the files within directory using suffix
	 */
	public static File[] getFilesWithinDirectoryUsingSuffix(final String fileSuffix, final File dir) {
		final File files[] = dir.listFiles((FileFilter) new SuffixFileFilter(fileSuffix));
		return files;
	}

	/**
	 * Gets the files within directory using prefix.
	 *
	 * @param filePrefix
	 *            the file prefix
	 * @param dir
	 *            the dir
	 * @return the files within directory using prefix
	 */
	public static File[] getFilesWithinDirectoryUsingPrefix(final String filePrefix, final File dir) {
		final File files[] = dir.listFiles((FileFilter) new PrefixFileFilter(filePrefix));
		return files;
	}

	/**
	 * Gets the correspondence count and marshalled object.
	 *
	 * @param lastDownloadedFile
	 *            the last downloaded file
	 * @param correspondenceCrawlerDTO 
	 * @return the correspondence count and marshalled object
	 * @throws CannotResolveClassException
	 *             the cannot resolve class exception
	 */
	public static void getCorrespondenceCountAndMarshalledObject(final File lastDownloadedFile, CorrespondenceCrawlerDTO correspondenceCrawlerDTO)
			throws CannotResolveClassException {
		OutgoingCorrespondence outgoingCorrespondence = getMarshalledCorrespondenceObject(lastDownloadedFile);

		final ApplicationCorrespondence applicationCorrespondence = outgoingCorrespondence
				.getApplicationCorrespondence();
		final List<ApplicationCorrespondenceData> applicationCorrespondenceDatas = applicationCorrespondence != null
				? applicationCorrespondence.getApplicationCorrespondenceData() : null;
		int countOfRecordsInXml = 0;

		if (CollectionUtils.isNotEmpty(applicationCorrespondenceDatas)) {
			for (final ApplicationCorrespondenceData applicationCorrespondenceData : applicationCorrespondenceDatas) {
				final DocumentList documentList = applicationCorrespondenceData.getDocumentList();
				final List<DocumentData> documentDatas = documentList != null ? documentList.getDocumentData() : null;

				if (CollectionUtils.isNotEmpty(documentDatas)) {

					for (int i = 0; i < documentDatas.size(); i++) {
						countOfRecordsInXml = countOfRecordsInXml + 1;
					}
				}

			}
		}
		correspondenceCrawlerDTO.setCorrespondenceCountInXML(countOfRecordsInXml);
		correspondenceCrawlerDTO.setOutgoingCorrespondence(outgoingCorrespondence);
	}

	/**
	 * Gets the marshalled correspondence object.
	 *
	 * @param lastDownloadedFile
	 *            the last downloaded file
	 * @return the marshalled correspondence object
	 */
	public static OutgoingCorrespondence getMarshalledCorrespondenceObject(final File lastDownloadedFile) {
		OutgoingCorrespondence outgoingCorrespondence = null;

		try {
			outgoingCorrespondence = (OutgoingCorrespondence) XmlParser.fetchXmlData(lastDownloadedFile,
					OutgoingCorrespondence.class);
		} catch (final FileNotFoundException e) {
			throw new XMLException("The file could not be found at the specified location", e);
		}
		return outgoingCorrespondence;
	}

	/**
	 * Gets the web driver.
	 *
	 * @param downLoadDir the down load dir
	 * @return the web driver
	 */
	public static WebDriver getWebDriver(String downLoadDir) {
		downLoadDir =  StringUtils.defaultString(downLoadDir);
		final FirefoxProfile fp = new FirefoxProfile();
		setUpFirefoxProfile(fp, downLoadDir);

		final WebDriver webDriver = new FirefoxDriver(fp);
		return webDriver;
	}

	/**
	 * Sets the up firefox profile.
	 *
	 * @param fp            the new up firefox profile
	 * @param baseDownloadPathOfBrowser the base download path of browser
	 */
	public static void setUpFirefoxProfile(final FirefoxProfile fp, String baseDownloadPathOfBrowser) {
		fp.setAcceptUntrustedCertificates(true);
		fp.setPreference("security.enable_java", true);
		fp.setPreference("plugin.state.java", 2);
		fp.setPreference("app.update.auto", false);
		fp.setPreference("app.update.enabled", false);
		fp.setPreference("applyBackgroundUpdates", 0);
		fp.setPreference("browser.download.dir", baseDownloadPathOfBrowser);
		fp.setPreference("browser.download.folderList", 2);
		fp.setPreference("browser.download.manager.alertOnEXEOpen", false);
		fp.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/xml,application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
		fp.setPreference("browser.download.manager.showWhenStarting", false);
		fp.setPreference("browser.download.manager.focusWhenStarting", false);
		fp.setPreference("browser.download.useDownloadDir", true);
		fp.setPreference("browser.helperApps.alwaysAsk.force", false);
		fp.setPreference("browser.download.manager.closeWhenDone", true);
		fp.setPreference("browser.download.manager.showAlertOnComplete", false);
		fp.setPreference("browser.download.manager.useWindow", false);
		fp.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
		fp.setPreference("pdfjs.disabled", true);
		fp.setPreference("plugin.disable_full_page_plugin_for_types",
				"application/pdf,application/vnd.adobe.xfdf,application/vnd.fdf,application/vnd.adobe.xdp+xml");

	}

	/**
	 * Gets the complete file path.
	 *
	 * @param relativePathForBrowserDownload
	 *            the relative path for browser download
	 * @param jobId
	 *            the job id
	 * @param clientId
	 *            the client id
	 * @return the complete file path
	 */
	public static String getCompleteFilePath(String relativePathForBrowserDownload, Long jobId, String clientId) {
		File relativeWithClientNameFolder = new File(BlackboxUtils.concat(relativePathForBrowserDownload, clientId));

		if (!relativeWithClientNameFolder.exists())
			relativeWithClientNameFolder.mkdirs();

		String baseDownloadPathOfBrowser = BlackboxUtils.concat(relativePathForBrowserDownload, clientId,
				File.separator, jobId.toString(), File.separator);

		File currentDateFolder = new File(baseDownloadPathOfBrowser);

		if (!currentDateFolder.exists())
			currentDateFolder.mkdirs();
		return baseDownloadPathOfBrowser;
	}

	/**
	 * Gets the current date folder location.
	 *
	 * @param relativePath
	 *            the relative path
	 * @return the current date folder location
	 */
	public static String getCurrentDateFolderLocation(String relativePath) {
		String dateInNumberFormat = BlackboxDateUtil.getStringFromDate(Calendar.getInstance().getTime(),
				BBWebCrawerConstants.DATE_IN_NUMBER_FORMAT);
		String currentDateFolderLocation = BlackboxUtils.concat(relativePath, dateInNumberFormat);
		return currentDateFolderLocation;
	}

	/**
	 * Wait for given duration.
	 *
	 * @param timeToWait
	 *            the time to wait
	 */
	public static void waitForGivenDurationInSeconds(int timeToWait) {
		long currentTime = Calendar.getInstance().getTimeInMillis();
		long iteratingTime = Calendar.getInstance().getTimeInMillis();

		while ((iteratingTime - currentTime) / 1000 < timeToWait) {
			iteratingTime = Calendar.getInstance().getTimeInMillis();
		}
	}

	/**
	 * Close web driver.
	 *
	 * @param webDriver
	 *            the web driver
	 * @param logger
	 *            the logger
	 */
	public static void closeWebDriver(WebDriver webDriver, Logger logger) {
		if (webDriver != null) {
			try {
				webDriver.close();
			} catch (UnreachableBrowserException e) {
				logger.info("Browser has been already closed. Hence unable to close the browser");
			}
		}
	}
	
	/**
	 * Logout and Close web driver.
	 *
	 * @param webDriver
	 *            the web driver
	 * @param logger
	 *            the logger
	 */
	public static void logoutAndCloseWebDriver(WebDriver webDriver, Logger logger) {
		logout(webDriver,logger);
		closeWebDriver(webDriver, logger);
	}

	/**
	 * Web click.
	 *
	 * @param element
	 *            the element
	 * @param webDriver
	 *            the web driver
	 */
	public static void webClick(By element, WebDriver webDriver) {
		Actions actions = new Actions(webDriver);
		WebElement hoverElement = webDriver.findElement(element);
		actions.moveToElement(hoverElement).click().perform();
	}

	/**
	 * Gets the last file name with given prefix.
	 *
	 * @param baseDirectory
	 *            the base directory
	 * @param fileName
	 *            the file name
	 * @return the last file name with given prefix
	 */
	public static File getLastFileNameWithGivenPrefix(String baseDirectory, String fileName) {
		File[] files = getFilesWithinDirectoryUsingPrefix(fileName, new File(baseDirectory));
		long lastCreatedFileTime = 0;
		long currentFileCreatedTime = 0;
		File lastFileCreated = files[0];

		for (final File file : files) {
			final Path path = Paths.get(file.getAbsolutePath());
			BasicFileAttributes attr;

			try {
				attr = Files.readAttributes(path, BasicFileAttributes.class);
				currentFileCreatedTime = attr.creationTime().toMillis();

				if (currentFileCreatedTime > lastCreatedFileTime) {
					lastCreatedFileTime = currentFileCreatedTime;
					lastFileCreated = file;
				}
			} catch (final IOException e) {
				throw new ApplicationException(1,
						"Some exception occured while getting the last created file in directory", e);
			}
		}
		return lastFileCreated;
	}

	/**
	 * Check app number completeness.
	 *
	 * @param applicationNumber
	 *            the application number
	 * @return true, if successful
	 */
	public static boolean checkAppNumberCompleteness(String applicationNumber) {
		String applicationNumberArray[] = applicationNumber.split(BBWebCrawerConstants.FRONT_SLASH);
		String ccAndYear = applicationNumberArray[1];
		String number = applicationNumberArray[2];
		return ccAndYear.length() == 6 && number.length() == 6 ? true : false;
	}

	/**
	 * Change to complete format.
	 *
	 * @param applicationNumber
	 *            the application number
	 * @param yearPrefix
	 *            the year prefix
	 * @return the string
	 */
	public static String changeToCompleteFormat(String applicationNumber, String yearPrefix) {
		String applicationNumberArray[] = applicationNumber.split(BBWebCrawerConstants.FRONT_SLASH);
		String ccAndYear = applicationNumberArray[1];
		String number = applicationNumberArray[2];
		String cc = ccAndYear.substring(0, 2);
		String year = ccAndYear.substring(2, ccAndYear.length());
		int leadingZeroesToBeAdded = 6 - number.length();
		String leadingZeroString = BBWebCrawerConstants.EMPTY_STRING;
		for (int i = 0; i < leadingZeroesToBeAdded; i++) {
			leadingZeroString = BlackboxUtils.concat(leadingZeroString, BBWebCrawerConstants.ZERO_STRING);
		}

		ccAndYear = (ccAndYear.length() != 6) ? BlackboxUtils.concat(cc, yearPrefix, year) : ccAndYear;
		number = (number.length() != 6) ? BlackboxUtils.concat(leadingZeroString, number) : number;
		return BlackboxUtils.concat(BBWebCrawlerUtils.WO_CODE_INDICATOR_WITHOUT_CC, BBWebCrawerConstants.FRONT_SLASH, ccAndYear,
				BBWebCrawerConstants.FRONT_SLASH, number);
	}

	/**
	 * Change format.
	 *
	 * @param applicationNumber            the application number
	 * @param toBeChangedInCompleteFormat            the to be changed in complete format
	 * @param filingDate the filing date
	 * @return the string
	 */
	public static String changeFormat(String applicationNumber, boolean toBeChangedInCompleteFormat,
			Calendar filingDate) {
		String otherFormatApplicationNumber = BBWebCrawerConstants.EMPTY_STRING;
		if (!toBeChangedInCompleteFormat) {
			// change to partial format.
			otherFormatApplicationNumber = changeToPartialFormat(applicationNumber);
		} else {
			// Change to complete format.
			String dateStr = BBWebCrawerConstants.EMPTY_STRING;
			String centuryPrefix = BBWebCrawerConstants.EMPTY_STRING;
			if(filingDate != null) {
			dateStr = BlackboxDateUtil.calendarToStr(filingDate, TimestampFormat.yyyyMMdd);
			centuryPrefix = dateStr.split(BBWebCrawerConstants.HYPHEN_STRING)[0].substring(0, 2);
			} else {
				centuryPrefix = CURRENT_CENTURY;
			}
			otherFormatApplicationNumber = changeToCompleteFormat(applicationNumber, centuryPrefix);
		}
		return otherFormatApplicationNumber;
	}

	/**
	 * Change to partial format.
	 *
	 * @param applicationNumber            the application number
	 * @return the string
	 */
	private static String changeToPartialFormat(String applicationNumber) {
		String applicationNumberArray[] = applicationNumber.split(BBWebCrawerConstants.FRONT_SLASH);
		String ccAndYear = applicationNumberArray[1];
		String number = applicationNumberArray[2];
		String cc = ccAndYear.substring(0, 2);
		String year = ccAndYear.substring(4, ccAndYear.length());
		return BlackboxUtils.concat(BBWebCrawlerUtils.WO_CODE_INDICATOR_WITHOUT_CC, BBWebCrawerConstants.FRONT_SLASH, cc, year,
				BBWebCrawerConstants.FRONT_SLASH, Integer.valueOf(number).toString());
	}

	/**
	 * Check wo app validity.
	 *
	 * @param applicationNumber
	 *            the application number
	 * @param filingDate
	 *            the date
	 * @return converted application numbers, if successful
	 */
	public static String convertToOtherForm(String applicationNumber, Calendar filingDate) {
		String convertedForm = BBWebCrawerConstants.EMPTY_STRING;

		if (RegexValidatorUtil.matches(applicationNumber, BBWebCrawerConstants.COMPLETE_FORMAT_REGEX)) {
			convertedForm = changeFormat(applicationNumber, false, filingDate);
		} else if (RegexValidatorUtil.matches(applicationNumber, BBWebCrawerConstants.PARTIAL_FORMAT_REGEX)) {
			convertedForm = changeFormat(applicationNumber, true, filingDate);
		}

		return convertedForm;
	}

	/**
	 * Formats application number on the basis of jurisdiction code and filling date. This will format application
	 * number with jurisdiction code 'US' or 'WO' and will ignore others.
	 * 
	 * @param applicationNumber
	 *            application number to format
	 * @param jurisdiction
	 *            jurisdiction code
	 * @param filingDate
	 *            filing date of the application
	 * @return converted application number
	 */
	public static String formatApplicationNumber(String applicationNumber, String jurisdiction, Calendar filingDate) {
		String convertedApplicationNumber = null;
		switch (jurisdiction) {
		case BBWebCrawerConstants.US_CODE:
			convertedApplicationNumber = StringUtils.remove(applicationNumber, "/");
			convertedApplicationNumber = StringUtils.remove(convertedApplicationNumber, ",");
			break;
		case BBWebCrawerConstants.WO_CODE:
			convertedApplicationNumber = applicationNumber;
			if (RegexValidatorUtil.matches(applicationNumber, BBWebCrawerConstants.PARTIAL_FORMAT_REGEX)) {
				convertedApplicationNumber = changeFormat(applicationNumber, true, filingDate);
			} else if (RegexValidatorUtil.matches(applicationNumber, BBWebCrawerConstants.COMPLETE_FORMAT_REGEX)) {
				convertedApplicationNumber = applicationNumber;
			} else {
				String errorMessage = MessageFormat.format("Application number [{0}] is invalid. Throwing an excption.",applicationNumber);
				LOGGER.error(errorMessage);
				throw new WebCrawlerGenericException(errorMessage);
			}
			break;
		default:
			break;
		}
		return convertedApplicationNumber;
	}
	
	/**
	 * Checks whether element exits or not.
	 * @param webDriver web driver instance
	 * @param elementLocator element locator
	 * @return true, if element exists
	 */
	public static boolean isElementPresent(WebDriver webDriver, By elementLocator) {
		boolean isElementPresent = webDriver.findElements(elementLocator).size() > 0;
		return isElementPresent;
	}
	
	/**
	 * Gets the formatted application number.
	 *
	 * @param applicationCorrespondenceData the application correspondence data
	 * @param usPTOApplicationNumberRaw the us pto application number raw
	 * @return the formatted application number
	 */
	public static String getFormattedApplicationNumber(
			final Calendar filingDate, String usPTOApplicationNumberRaw) {
		String jurisdictionCode = getJurisdictionCodeByApplicationNumber(usPTOApplicationNumberRaw);
		String usPTOApplicationNumberFormatted = BBWebCrawlerUtils.formatApplicationNumber(usPTOApplicationNumberRaw, jurisdictionCode, filingDate);
		return usPTOApplicationNumberFormatted;
	}
	
	/**
	 * Gets Jurisdiction code for the specified application number. This method will check for Jurisdiction (US/WO) only. 
	 * Default case is US
	 * @param applicationNumber application number to find Jurisdiction code
	 * @return jurisdiction code(US or WO)
	 */
	public static String getJurisdictionCodeByApplicationNumber(final String applicationNumber) {
		String jurisdictionCode = applicationNumber.startsWith(BBWebCrawerConstants.WO_CODE_INDICATOR)
				? BBWebCrawerConstants.WO_CODE
				: BBWebCrawerConstants.US_CODE;
		return jurisdictionCode;
	}
	
	/**
	 * Gets the abs path from relative path.
	 *
	 * @param relativeDownloadPath the relative download path
	 * @return the abs path from relative path
	 */
	public static String getAbsPathFromRelativePath(FolderRelativePathEnum relativeDownloadPath) {
		return BlackboxUtils.concat(BlackboxPropertyUtil.getProperty(BBWebCrawerConstants.ROOT_FOLDER_DIR),relativeDownloadPath.getModuleFolderName());
	}

	/**
	 * Logout.
	 *
	 * @param webDriver the web driver
	 * @param logger 
	 */
	public static void logout(WebDriver webDriver, Logger logger) {
		if (webDriver != null) {
			try {
				By cssSelector = By.cssSelector("a[href*='Logout.jsp']");
				if (isElementPresent(webDriver, cssSelector)) {
					webDriver.findElement(cssSelector).click();
					BBWebCrawlerUtils.waitForSpecifiedDuration(5, webDriver);
				}
			} catch (UnreachableBrowserException e) {
				logger.info("Browser has been already closed. Hence unable to logout from the browser");
			}
		}
	}
	
	/**
	 * Gets the marshalled patent application list object.
	 *
	 * @param lastDownloadedFile xml file last downloaded from the website
	 * @return the marshalled correspondence object
	 */
	public static PatentApplicationList getMarshalledApplicatonListObject(final File lastDownloadedFile) throws XMLOrPDFCorruptedException{
		PatentApplicationList patentApplicationList = null;

		try {
			patentApplicationList = (PatentApplicationList) XmlParser.fetchXmlData(lastDownloadedFile,
					PatentApplicationList.class);
		} catch (final FileNotFoundException e) {
			throw new XMLException("The file could not be found at the specified location", e);
		}
		return patentApplicationList;
	}
	
	/**
	 * Gets all files downloaded in a directory.
	 *
	 * @param dirPath
	 *            the dir path
	 * @param fileSuffix
	 *            the file suffix
	 * @return the latest file in a directory
	 */
	public static File[] getAllFilesDownloaded(final String dirPath, final String fileSuffix) {
		final File dir = new File(dirPath);
		File[] emptyArray = new File[1];
		final File[] files = getFilesWithinDirectoryUsingSuffix(fileSuffix, dir);
		if (files == null || files.length == 0) {
			return emptyArray; 
		}
		else{
			return files;
		}
	}
	
	/**
	 * This method return application type on basis of jurisdiction.
	 * for US - <code>ApplicationType.ALL</code>
	 * for WO - <code>ApplicationType.US_SUBSEQUENT_FILING</code>
	 * @param jurisdiction
	 * @return
	 */
	public static ApplicationType getApplicationType(String jurisdiction) {
		ApplicationType type = null;

		switch (jurisdiction) {
			case BBWebCrawerConstants.US_CODE:
				type = ApplicationType.ALL;
				break;
			case BBWebCrawerConstants.WO_CODE:
				type = ApplicationType.PCT_US_SUBSEQUENT_FILING;
				break;
			default:
				break;
		}
		return type;	
	}
	
	public static void getURL(final WebDriver driver, final String url) {
		driver.get(url);
	}
	
	public static void waitForSpecifiedDuration(long waitDurationInSecs) {
		try {
			Thread.sleep(waitDurationInSecs * 1000); 
		} catch(InterruptedException e) {
			// DO NOTHING
		}
	}
	
	public static void cleanTempDirectory(final String tmpStoreDirectory) {
		try {
			FileUtils.cleanDirectory(new File(tmpStoreDirectory));
		} catch (IOException e) {
			LOGGER.error(MessageFormat.format("Error while deleting content of directory{0}", tmpStoreDirectory));
		}
	}
	
	public static boolean isFileNotEmpty(final File file) {
		return (file != null && file.isFile() && file.length() > 0);
	}
}
