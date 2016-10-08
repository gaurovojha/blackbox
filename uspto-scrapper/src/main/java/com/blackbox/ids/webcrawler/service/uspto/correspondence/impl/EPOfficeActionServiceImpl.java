package com.blackbox.ids.webcrawler.service.uspto.correspondence.impl;

import static com.blackbox.ids.util.constant.BBWebCrawerConstants.BACK_SLASH;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.COMMA_STRING;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.EP_PTO_DATE_FORMAT;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.EP_THIRD_PARTIES;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.EP_THIRD_PARTY;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.PDF_SUFFIX_WITH_DOT;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.SPACE_STRING;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.UNDER_SCORE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.correspondence.Correspondence.Source;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.Correspondence.SubSourceTypes;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.repository.DocumentCodeRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceStagingRepository;
import com.blackbox.ids.core.repository.webcrawler.DownloadOfficeActionQueueRepository;
import com.blackbox.ids.core.util.PDFUtil;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.exception.WebCrawlerGenericException;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.IBaseCrawlerService;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IEPOWebCrawler;

@Configuration
@Component
@Service("ePOfficeActionService")
public class EPOfficeActionServiceImpl implements IBaseCrawlerService {

	private static final Logger LOGGER = Logger.getLogger(EPOfficeActionServiceImpl.class);

	@Value("${application.retry.count}")
	private int retryCount;

	@Value("${uspto.application.wait.time}")
	private long waitingTime;
	
	@Autowired
	@Resource(name = "scrProperty")
	private Properties env;

	@Autowired
	private DocumentCodeRepository documentCodeRepository;

	@Autowired
	private DownloadOfficeActionQueueRepository downloadOfficeActionQueueRepository;

	@Autowired
	private CorrespondenceStagingRepository correspondenceStagingRepository;

	@Autowired
	private BBWebCrawlerPropertyConstants properties;

	private WebDriver webDriver;

	private Map<String, List<String>> mDocumentNames;

	@Autowired
	@Qualifier("ePOWebCrawlerService")
	private IEPOWebCrawler epoWebCrawlerService;

	@Override
	public Map<String, Object> execute(WebCrawlerJobAudit webCrawlerJobAudit) {
		fetchData(webCrawlerJobAudit);
		return null;
	}

	/**
	 * Gets the Application Number from BB_SCR_DOWNLOAD_OFFICE_ACTION table and
	 * gets the Correspondence record from EPO Portal
	 * 
	 * @param webCrawlerJobAudit
	 */
	@Transactional
	private void fetchData(WebCrawlerJobAudit webCrawlerJobAudit) {

		List<DownloadOfficeActionQueue> dataList = downloadOfficeActionQueueRepository
				.findByDownloadQueueStatusAndJursidictionCode(Jurisdiction.Code.EUROPE.value);

		if (CollectionUtils.isNotEmpty(dataList)) {

			LOGGER.info("Count of European Queue Records " + dataList.size());

			String baseDownloadPathOfBrowser = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("crawler");
			long jobId = webCrawlerJobAudit.getId();
			String clientId = properties.getClientId();

			String defaultDir = BBWebCrawlerUtils.getCompleteFilePath(baseDownloadPathOfBrowser, jobId, clientId);
			String downloadDirName = "download";
			String dir = defaultDir + BACK_SLASH + downloadDirName;

			File downLoadDir = new File(dir);
			if (!downLoadDir.exists()) {
				downLoadDir.mkdir();
			}
			webDriver = BBWebCrawlerUtils.getWebDriver(downLoadDir.getAbsolutePath());

			mDocumentNames = new HashMap<String, List<String>>();
			LOGGER.info("Web Crawler Service for European Office Action Started");

			try {
				List<DocumentCode> documentCodes = null;

				for (DownloadOfficeActionQueue entity : dataList) {
					if (entity.getDocumentCode().equalsIgnoreCase(Constant.ALL_DOCUMENT_CODE)) {

						String juridictionCode = entity.getJurisdictionCode();

						documentCodes = documentCodeRepository.findByJurisdictionCode(juridictionCode);
						for (DocumentCode dCode : documentCodes) {
							populateDocumentNameList(dCode.getCode(), mDocumentNames);
						}
					} else {
						populateDocumentNameList(entity.getDocumentCode(), mDocumentNames);
					}

					if (retryCount == entity.getRetryCount()) {
						updateOfficeActionStatus(entity, QueueStatus.CRAWLER_ERROR);
						continue;
					}
					findApplicationDocuments(entity, defaultDir, downLoadDir.getAbsolutePath());
					updateOfficeActionStatus(entity, QueueStatus.SUCCESS);
				}
			} catch (WebCrawlerGenericException e) {
				LOGGER.error("Exception Occured while Crawling the page " + e);
				throw e;
			} catch (Exception e) {
				LOGGER.error("Exception Occured while Crawling the page " + e);
				throw e;
			} finally {
				BBWebCrawlerUtils.closeWebDriver(webDriver, LOGGER);
			}
		} else {
			LOGGER.info("No Record Found For Scrapping");
		}
	}

	private void findApplicationDocuments(DownloadOfficeActionQueue entity, String basePath, String downLoadDirPath) {
		try {
			epoWebCrawlerService.scrapApplicationDocuments(entity.getApplicationNumberFormatted(), webDriver);
			Thread.sleep(2000);

			if (epoWebCrawlerService.checkForSearchResult(webDriver)) {

				LOGGER.info("Processing Documents Data");
				Map<String, String> tableData = epoWebCrawlerService.processAllDocuments(webDriver);
				LOGGER.info("Filtering Table Data");
				Map<String, String> filterDtaList = filterTableData(tableData, mDocumentNames);
				downloadAndSaveRecord(filterDtaList, entity.getApplicationNumberFormatted(), basePath, downLoadDirPath);
				LOGGER.info(
						"Fetching Records for " + entity.getApplicationNumberFormatted() + "Successfully Completed");
				resetParameters();
			} else {
				updateOfficeActionRetryCount(entity);
			}
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted while sleeping the Thread ", e);
			throw new WebCrawlerGenericException("Interrupted while sleeping the Thread", e);
		}
	}

	/**
	 * This method scraps all the table Data and tableData map is populated
	 * where key is a combination of date + "_" + documentName and value is id
	 * of hyper link. This id is needed for downloading the PDF.
	 */

	/**
	 * Returns the filter dataList according to the searchDocumentNames.
	 * 
	 * @param dataList
	 * @param searchDocumentNames
	 * @return
	 */
	private static Map<String, String> filterTableData(Map<String, String> dataList,
			Map<String, List<String>> mDocumentNames) {

		Map<String, String> filterDtaList = new LinkedHashMap<String, String>();

		for (Entry<String, List<String>> entry : mDocumentNames.entrySet()) {
			List<String> searchDocumentNames = entry.getValue();
			String documentCode = entry.getKey();

			for (String documentName : searchDocumentNames) {

				for (Iterator<String> iterator = dataList.keySet().iterator(); iterator.hasNext();) {
					String key = iterator.next();

					if (documentName.equalsIgnoreCase(EP_THIRD_PARTY)
							|| documentName.equalsIgnoreCase(EP_THIRD_PARTIES)) {
						if (key.split(UNDER_SCORE)[1].toLowerCase().contains(documentName)) {
							filterDtaList.put(key + UNDER_SCORE + documentCode, dataList.get(key));
						}
					} else {
						if (key.split(UNDER_SCORE)[1].equalsIgnoreCase(documentName)) {
							filterDtaList.put(key + UNDER_SCORE + documentCode, dataList.get(key));
						}
					}
				}
			}
		}
		return filterDtaList;
	}

	/**
	 * 
	 * @param fDataList
	 * @param applicationNumber
	 * @throws WebCrawlerGenericException
	 */
	private void downloadAndSaveRecord(Map<String, String> fDataList, String applicationNumber, String defaultDir,
			String downLoadDirPath) {

		Map<String, List<String>> dateMap = new LinkedHashMap<String, List<String>>();
		Calendar c = Calendar.getInstance();
		long id = c.getTimeInMillis();

		File appNumFolder = new File(defaultDir + BACK_SLASH + applicationNumber + UNDER_SCORE + id);
		if (!appNumFolder.exists()) {
			appNumFolder.mkdirs();
			LOGGER.info("Dir Created  -->" + appNumFolder.getAbsolutePath());
		}

		for (String key : fDataList.keySet()) {
			String[] parts = key.split(UNDER_SCORE);
			String mKey = parts[0] + UNDER_SCORE + parts[2];
			if (dateMap.containsKey(mKey) && null != dateMap.get(mKey)) {
				dateMap.get(mKey).add(parts[1]);
			} else {
				List<String> list = new ArrayList<String>();
				list.add(parts[1]);
				dateMap.put(mKey, list);
			}
		}

		for (Entry<String, List<String>> entrySet : dateMap.entrySet()) {
			String dateKey = entrySet.getKey();
			List<String> fList = entrySet.getValue();
			for (String fileName : fList) {
				String combinedKey = dateKey.split(UNDER_SCORE)[0] + UNDER_SCORE + fileName + UNDER_SCORE
						+ dateKey.split(UNDER_SCORE)[1];

				LOGGER.debug("Downloading File . Identifier is : " + combinedKey);
				String identifier = fDataList.get(combinedKey);
				String xPath = "input[value='" + identifier + "']";
				epoWebCrawlerService.downLoadDocument(xPath, webDriver);
				moveFile(appNumFolder.getAbsolutePath(), downLoadDirPath, applicationNumber, dateKey);
			}
		}
		Map<String, String> map = mergeFiles(appNumFolder.getAbsolutePath(), applicationNumber);

		for (Map.Entry<String, String> es : map.entrySet()) {
			String key = es.getKey();
			String filePath = es.getValue();
			saveStagingData(applicationNumber, filePath, key.split(UNDER_SCORE)[0], key.split(UNDER_SCORE)[1]);
		}

	}

	/**
	 * It matches the documentCode in the property file and gets the
	 * documentName associated with the document code {@link path.properties} }
	 * 
	 * @param documentCode
	 * @param mDocumentNames
	 */
	private void populateDocumentNameList(String documentCode, Map<String, List<String>> mDocumentNames) {

		String[] documentNames = null;
		String value = env.getProperty(getKey(documentCode));
		if (null != value) {
			documentNames = value.split(COMMA_STRING);
		}
		mDocumentNames.put(documentCode, Arrays.asList(documentNames));
	}

	/**
	 * Moving the downloaded file into temDir because file gets downloaded with
	 * same application number
	 * 
	 * @param path
	 * @param fileName
	 * @param count
	 * @param tmpDir
	 */
	private void moveFile(String basePath, String downLoadPath, String fileName, String tmpDir) {
		Calendar c = Calendar.getInstance();
		String downloadedFileName = fileName.split("\\.")[0];

		File newTmpFolder = new File(basePath + BACK_SLASH + tmpDir);

		if (!newTmpFolder.exists()) {
			newTmpFolder.mkdir();
			LOGGER.info("Dir Created  -->" + newTmpFolder.getAbsolutePath());
		}

		File downLoadDir = new File(downLoadPath);
		
		for (final File fileEntry : downLoadDir.listFiles()) {

			if (fileEntry.isFile() && fileEntry.getName().endsWith(PDF_SUFFIX_WITH_DOT)) {
				boolean isFileDownloaded = true;

				int fileWaitCounter = 0;
				while (isFileDownloaded) {

					isFileDownloaded = fileEntry.getName().equals(downloadedFileName + PDF_SUFFIX_WITH_DOT);
					if (isFileDownloaded) {
						File newFileName = new File(newTmpFolder.getPath() + BACK_SLASH + downloadedFileName
								+ UNDER_SCORE + c.getTimeInMillis() + PDF_SUFFIX_WITH_DOT);
						try {

							Files.move(fileEntry.toPath(), newFileName.toPath(), StandardCopyOption.REPLACE_EXISTING);

							LOGGER.info("File Moved from " + fileEntry.getAbsolutePath() + " to "
									+ newFileName.getAbsolutePath());
						} catch (IOException e) {
							LOGGER.error("Failed to move File from " + fileEntry.getAbsolutePath() + " to "
									+ newFileName.getAbsolutePath(), e);
							throw new WebCrawlerGenericException("Failed to move File from "
									+ fileEntry.getAbsolutePath() + " to " + newFileName.getAbsolutePath(), e);
						}
						isFileDownloaded = false;

					} else {
						fileWaitCounter++;
						try {
							LOGGER.debug("Sleeping Thread for " + fileWaitCounter + " time");
							Thread.sleep(waitingTime);
						} catch (InterruptedException e) {
							LOGGER.error("Failed to Sleep the Thread ", e);
							throw new WebCrawlerGenericException("Failed to Sleep the Thread ", e);
						}
						if (fileWaitCounter > 3) {
							return;
						}
						isFileDownloaded = true;
					}

				}
			}

		}
	}

	/**
	 * Merge all the files present in folders of baseDirPath into single pdf.
	 * Name of the merged file is {applicationNumber}.pdf
	 * 
	 * @param baseDirPath
	 * @throws WebCrawlerGenericException
	 */
	public Map<String, String> mergeFiles(String baseDirPath, String applicationNumber) {

		Map<String, String> map = new HashMap<String, String>();

		LOGGER.info("Merging files in " + baseDirPath);

		File baseDir = new File(baseDirPath);
		String mergedFilePath = null;

		if (baseDir.listFiles().length == 0) {

			throw new WebCrawlerGenericException(
					MessageFormat.format("No File is present in dir {} for merging . At least one file must be present",
							baseDir.getAbsolutePath()));
		} else {
			for (File dir : baseDir.listFiles()) {
				File[] files = dir.listFiles();
				List<String> fileNames = new ArrayList<String>(files.length);
				for (File f : files) {
					fileNames.add(f.getAbsolutePath());
				}
				String destinationFileName = dir.getAbsolutePath() + BACK_SLASH + applicationNumber
						+ PDF_SUFFIX_WITH_DOT;
				Collections.sort(fileNames);
				try {
					mergedFilePath = PDFUtil.mergerPDF(fileNames, destinationFileName);
					LOGGER.info("Merged File Path : " + mergedFilePath);
				} catch (COSVisitorException | IOException e) {
					LOGGER.error("Failed to merge the files", e);
					throw new WebCrawlerGenericException(MessageFormat
							.format("Could not merge the files present in dir {} ", dir.getAbsolutePath()));
				}
				map.put(dir.getName(), mergedFilePath);
			}
		}
		return map;
	}

	/**
	 * Reseting for next Application Search
	 */
	private void resetParameters() {
		mDocumentNames.clear();
	}

	private static String getKey(String value) {
		return value.replace(SPACE_STRING, UNDER_SCORE);
	}

	private void updateOfficeActionRetryCount(DownloadOfficeActionQueue entity) {

		int count = entity.getRetryCount();
		entity.setRetryCount(++count);

		try {
			downloadOfficeActionQueueRepository.save(entity);
		} catch (Exception e) {
			LOGGER.error("Failed to update retry count of " + entity.getClass(), e);
			throw e;
		}

	}

	private void updateOfficeActionStatus(DownloadOfficeActionQueue entity, QueueStatus status) {
		LOGGER.info("Updating Office Status of Id:" + entity.getId());
		entity.setStatus(status);

		try {
			downloadOfficeActionQueueRepository.save(entity);
		} catch (Exception e) {
			LOGGER.error("Not able to persist entity " + entity.getClass().getName(), e);
			throw e;
		}

	}

	public void saveStagingData(String applicationNumber, String documentPath, String mailingDate,
			String documentCode) {
		CorrespondenceStaging entity = createCorrespondenceStagingEntity(applicationNumber, documentPath, mailingDate,
				documentCode);
		LOGGER.info("Persisting Statging Data for " + applicationNumber);
		try {
			correspondenceStagingRepository.save(entity);
		} catch (Exception e) {
			LOGGER.error("Not able to persist Statging Data" + e);
			throw e;
		}
	}

	private CorrespondenceStaging createCorrespondenceStagingEntity(String applicationNumber, String documentPath,
			String mailingDate, String documentCode) {
		DocumentCode code = documentCodeRepository.findByCode(documentCode);
		CorrespondenceStaging entity = new CorrespondenceStaging();

		entity.setApplicationNumber(applicationNumber);

		try {
			entity.setAttachmentSize(PDFUtil.getPDFsize(documentPath));
			entity.setPageCount(PDFUtil.getPDFPageCount(documentPath));
		} catch (IOException e) {
			LOGGER.error("Not able to Read Pdf File : Link " + documentPath, e);
			throw new WebCrawlerGenericException("Failed to get PDF size and Page Count ", e);
		}
		entity.setDocumentDescription(code.getCode());
		entity.setDocumentDescription(code.getDescription());
		entity.setStatus(Status.PENDING);
		entity.setJurisdictionCode(Jurisdiction.Code.EUROPE.toString());
		entity.setCreatedByUser(new Long(1));
		entity.setMailingDate(BlackboxDateUtil.getCalendar(mailingDate, EP_PTO_DATE_FORMAT));
		entity.setCreatedDate(Calendar.getInstance());
		entity.setSource(Source.AUTOMATIC);
		entity.setSubSource(SubSourceTypes.EP_PTO);
		entity.setAttachmentName(documentPath);

		return entity;
	}
}
