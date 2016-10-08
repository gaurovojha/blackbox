package com.blackbox.ids.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;

import org.activiti.bpmn.exceptions.XMLException;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.blackbox.ids.core.dto.correspondence.xmlmapping.OutgoingCorrespondence;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.PatentApplicationList;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.XmlParser;
import com.blackbox.ids.exception.XMLOrPDFCorruptedException;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;

public class BBWebCrawlerUtils {

	/**
	 * Gets the web driver.
	 *
	 * @param downLoadDir the down load dir
	 * @return the web driver
	 */
	public static WebDriver getWebDriver(String downLoadDir) {
		return null;
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
	 * Close web driver.
	 *
	 * @param webDriver
	 *            the web driver
	 * @param logger
	 *            the logger
	 */
	public static void closeWebDriver(WebDriver webDriver, Logger logger) {
		return;
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
	
	public static void logoutAndCloseWebDriver(WebDriver webDriver, Logger logger) {
		return ;
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

		return applicationNumber;
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
	public static String getJurisdictionCodeByApplicationNumber(final String applicationNumber) {
		String jurisdictionCode = applicationNumber.startsWith(BBWebCrawerConstants.WO_CODE_INDICATOR)
				? BBWebCrawerConstants.WO_CODE
				: BBWebCrawerConstants.US_CODE;
		return jurisdictionCode;
	}
	
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


}
