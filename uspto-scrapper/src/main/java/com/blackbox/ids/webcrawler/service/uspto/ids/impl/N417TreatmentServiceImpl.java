package com.blackbox.ids.webcrawler.service.uspto.ids.impl;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.webcrawler.IN417TreatmentQueueDAO;
import com.blackbox.ids.core.model.OAResponseCode;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.mdm.ApplicationDetails;
import com.blackbox.ids.core.model.mdm.OrganizationDetails;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.IDSRelevantStatus;
import com.blackbox.ids.core.model.mstr.ProsecutionStatus;
import com.blackbox.ids.core.model.webcrawler.N417TreatmentQueue;
import com.blackbox.ids.core.repository.OAResponseCodeRepository;
import com.blackbox.ids.core.util.UnzipUtility;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.services.ids.IN417TreatmentQueueService;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.application.IApplicationWebCrawler;
import com.blackbox.ids.webcrawler.service.uspto.ids.IIDSWebCrawler;
import com.blackbox.ids.webcrawler.service.uspto.ids.IN417TreatmentService;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;

@Service
public class N417TreatmentServiceImpl implements IN417TreatmentService {

	private static final String				N417_TEMP_DOWNLOAD_DIR	= "n417Treatment";

	private static final String				IFW_FILE_NAME			= "-image_file_wrapper";

	@Autowired
	private IUSPTOLoginService				loginService;

	@Autowired
	private IApplicationWebCrawler			webCrawler;

	@Autowired
	private IIDSWebCrawler					idsWebCrawler;

	/** The application base dao. */
	@Autowired
	private ApplicationBaseDAO				applicationBaseDao;

	@Autowired
	private IN417TreatmentQueueDAO			n417TreatmentQueueDao;

	@Autowired
	private IN417TreatmentQueueService		queueService;

	@Autowired
	private OAResponseCodeRepository		oaResponseCodeRepository;

	/** The constants. */
	@Autowired
	private BBWebCrawlerPropertyConstants	properties;

	private static final Logger				LOGGER					= Logger.getLogger(N417TreatmentServiceImpl.class);

	@Override
	public void execute() {
		LOGGER.info("N417-Treatment process started..");

		// step : fetch application staging records sorted by customer number
		List<N417TreatmentQueue> applicationList = n417TreatmentQueueDao.getApplicationsList();

		if (CollectionUtils.isNotEmpty(applicationList)) {
			Map<ProsecutionStatus, List<N417TreatmentQueue>> applicationQueue = applicationList.stream().collect(
					Collectors.groupingBy(a -> a.getApplication().getOrganizationDetails().getProsecutionStatus()));
			List<OAResponseCode> responseCodeList = oaResponseCodeRepository.findAll();

			if (CollectionUtils.isEmpty(responseCodeList)) {
				LOGGER.error("OA response code list found empty");
				return;
			}
			final Map<String, String> oaDocumentCodes = responseCodeList.stream().collect(
					Collectors.toMap(d -> d.getCode(), d -> d.getDescription()));

			List<N417TreatmentQueue> publishedApplicationList = applicationQueue.get(ProsecutionStatus.PUBLISHED);
			List<N417TreatmentQueue> pendingApplicationList = applicationQueue
					.get(ProsecutionStatus.PENDING_PUBLICATION);

			// Reed-Tech treatment
			if (CollectionUtils.isNotEmpty(publishedApplicationList)) {
				LOGGER.info(MessageFormat.format("Published Application Count : {0}", publishedApplicationList.size()));
				processPublishedApplications(publishedApplicationList, oaDocumentCodes);
			} else {
				LOGGER.info("Published Application Count : 0");
			}

			// private pair treatment
			if (CollectionUtils.isNotEmpty(pendingApplicationList)) {
				LOGGER.info(MessageFormat.format("Pending Application Count : {1}.", pendingApplicationList.size()));
				Map<Customer, List<N417TreatmentQueue>> customerApplicationsMap = pendingApplicationList.stream().collect(
						Collectors.groupingBy(app -> app.getApplication().getCustomer()));
				customerApplicationsMap.entrySet().forEach(
						entry -> initPrivatePairWebCrawler(entry.getKey(), entry.getValue(), oaDocumentCodes));
			} else {
				LOGGER.info("Pending Application Count : 0");
			}

		} else {
			LOGGER.info("Application list found empty.");
		}
		LOGGER.info("N417-Treatment process completed.");
	}
	
	private void initPrivatePairWebCrawler(final Customer customer, final List<N417TreatmentQueue> applicationList,
			final Map<String, String> oaDocumentCodes) {
		// step1 : create web driver
		final WebDriver webDriver = BBWebCrawlerUtils.getWebDriver(null);
		try {
			// step2 : fetch list of applications for each customer
			LOGGER.info(MessageFormat.format("Started N417 treatment for Customer [{0}] ", customer));

			// step3 : authenticate customer
			boolean success = loginService.login(webDriver, customer);
			if (success) {
				// step4 : process IFW for applications
				IntStream.range(0, applicationList.size()).forEach(
						idx -> scrapApplication(applicationList.get(idx), idx == 0,
								oaDocumentCodes, webDriver));
			}
		} finally {
			BBWebCrawlerUtils.logoutAndCloseWebDriver(webDriver, LOGGER);
		}
	}

	private void initReedTechTreatment(final N417TreatmentQueue app, final WebDriver webDriver,
			final String tmpStoreDirectory, final UnzipUtility unzipper, final Map<String, String> oaDocumentCodes) {
		String formattedAppNumber = app.getApplication().getApplicationNumber();
		String ifwFilePath = tmpStoreDirectory + formattedAppNumber;
		String zipFilePath = ifwFilePath + BBWebCrawerConstants.ZIP_FILE_EXTENSION;
		String sourceZipFileURL = properties.reedTechBaseURL + formattedAppNumber + BBWebCrawerConstants.ZIP_FILE_EXTENSION;

		LOGGER.info(MessageFormat.format("Clicking Reed-Tech URL - {0}", sourceZipFileURL));
		BBWebCrawlerUtils.getURL(webDriver, sourceZipFileURL);

		boolean success = false;
		boolean isFileDownloaded = checkIfFileDownloaded(zipFilePath);

		if (isFileDownloaded) {
			success = unzipIFWFile(tmpStoreDirectory, unzipper, zipFilePath);
			if (success) {
				String tsvPath = ifwFilePath + File.separator + formattedAppNumber + IFW_FILE_NAME;
				String tsvFileName = formattedAppNumber + IFW_FILE_NAME + BBWebCrawerConstants.TSV_FILE_EXTENSION;
				File tsvFile = new File(tsvPath, tsvFileName);
				if (tsvFile.exists()) {
					Map<String, Calendar> ifwData = readIFWFile(tsvFile);
					if (MapUtils.isNotEmpty(ifwData)) {
						processApplication(app, oaDocumentCodes, formattedAppNumber, ifwData);
					}
				} else {
					LOGGER.error(MessageFormat.format(
							"TSV({0}) file doesn't exist in the package. Searching on private pair...", tsvFileName));
				}
			}
		} else {
			// if file is not downloaded after re-attempts, log exception
			LOGGER.error(MessageFormat.format("Error while downloading IFW file for application {0}.",
					formattedAppNumber));

		}

		if (!success) {
			// search on private pair
			LOGGER.error(MessageFormat.format("Searching on private pair for the application({0})...",
					formattedAppNumber));
			searchOnPrivatePair(app, oaDocumentCodes);
		}
	}

	private void searchOnPrivatePair(final N417TreatmentQueue app, final Map<String, String> oaDocumentCodes) {
		List<N417TreatmentQueue> applicationList = new ArrayList<>();
		applicationList.add(app);
		initPrivatePairWebCrawler(app.getApplication().getCustomer(), applicationList, oaDocumentCodes);
	}

	private boolean checkIfFileDownloaded(final String zipFilePath) {
		boolean success = Boolean.FALSE;
		File file = new File(zipFilePath);
		for (int noOfAttemptsTaken = 1; noOfAttemptsTaken <= properties.ifwZipDownloadRetryCount; noOfAttemptsTaken++) {

			File partFile = new File(zipFilePath + BBWebCrawerConstants.PART_FILE_EXTENSION);
			if (partFile.exists()) {
				try {
					long milliseconds = properties.ifwZipDownloadRetryDuration * 1000;
					Thread.sleep(milliseconds);
					continue;
				} catch (InterruptedException ie) {
					LOGGER.error("Exception occured while checking for IFW downloaded file. ", ie);
				}
			} else if (file.exists()) {
				success = true;
				break;
			} else {
				LOGGER.error("IFW file(Reed-Tech) is not downloaded.");
				break;
			}
		}

		return success;
	}

	private void scrapApplication(final N417TreatmentQueue app, final boolean isFirstRecord,
			final Map<String, String> oaDocumentCodes, final WebDriver webDriver) {
		// step1 : select new case
		if (!isFirstRecord) {
			webCrawler.selectNewCase(webDriver);
		}

		ApplicationDetails appDetails = app.getApplication().getAppDetails();
		String appNumber = appDetails.getApplicationNumberRaw();
		Calendar filingDate = appDetails.getFilingDate();
		String jurisdiction = app.getApplication().getJurisdiction().getCode();

		LOGGER.info(MessageFormat.format("Searching IFW data for the application({0})..", appNumber));

		// step2 : Search for application
		webCrawler.searchApplicationNumber(webDriver, appNumber, jurisdiction);

		// step3 : check if application details page opened
		boolean isFindApplicationSuccess = webCrawler.checkApplicationDetailsPageStatus(webDriver, appNumber,
				jurisdiction, filingDate);

		// step4 : check for error case
		if (!isFindApplicationSuccess) {
			// error opening page
			LOGGER.error(MessageFormat.format("Unable to open application details({0}) page.", appNumber));
			return;
		}

		// step5 : find IFW tab
		boolean isImageFileWrapperTabPresent = idsWebCrawler.checkImageFileWrapperTab(webDriver);

		// IFW tab is not present for the application
		if (!isImageFileWrapperTabPresent) {
			LOGGER.error(MessageFormat.format("IFW tab is not present for the application({0}).", appNumber));
			return;
		}

		// step6 : IFW documents data
		Map<String, Calendar> ifwData = idsWebCrawler.readIFWData(webDriver);

		processApplication(app, oaDocumentCodes, appNumber, ifwData);
	}

	private void processApplication(final N417TreatmentQueue app, final Map<String, String> oaDocumentCodes,
			String appNumber, Map<String, Calendar> ifwData) {
		OrganizationDetails orgDetails = app.getApplication().getOrganizationDetails();
		Calendar idsRelevantStatusDate = orgDetails.getIdsRelevantStatusUpdatedOn();

		// filter IFW data
		Predicate<Entry<String, Calendar>> filterPredicate;
		if (idsRelevantStatusDate != null) {
			filterPredicate = e -> oaDocumentCodes.keySet().contains(e.getKey())
					&& e.getValue().after(idsRelevantStatusDate);
		} else {
			filterPredicate = e -> oaDocumentCodes.keySet().contains(e.getKey());
		}
		Map<String, Calendar> filteredIFWData = ifwData.entrySet().stream().filter(filterPredicate)
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

		if (MapUtils.isNotEmpty(filteredIFWData)) {
			// step 7 : update application record
			Map<String, Calendar> latestIFWMap = filteredIFWData.entrySet().stream()
					.sorted(Map.Entry.<String, Calendar> comparingByValue().reversed()).limit(1)
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
			Entry<String, Calendar> latestIFW = latestIFWMap.entrySet().iterator().next();
			orgDetails.setOaResponseDate(latestIFW.getValue());
			orgDetails.setOaResponseDesc(oaDocumentCodes.get(latestIFW.getKey()));
			LOGGER.info(MessageFormat.format("IFW({0}) found for the application({1}). Updating OA response details..",
					latestIFW.getKey(), appNumber));

			if (filteredIFWData.containsKey(BBWebCrawerConstants.DOCUMENT_CODE_RCEX)) {
				// check for RCE document
				orgDetails.setIdsRelevantStatus(IDSRelevantStatus.RCE_FILED);
				orgDetails.setIdsRelevantStatusUpdatedOn(filteredIFWData.get(BBWebCrawerConstants.DOCUMENT_CODE_RCEX));
				LOGGER.info(MessageFormat.format("IFW(RCEX) found for the application({0}). Updating status..",
						appNumber));
			} else if (filteredIFWData.containsKey(BBWebCrawerConstants.DOCUMENT_CODE_IFEE)) {
				// check for IFEE document
				orgDetails.setIdsRelevantStatus(IDSRelevantStatus.ISSUE_FEE_PAID);
				orgDetails.setIdsRelevantStatusUpdatedOn(filteredIFWData.get(BBWebCrawerConstants.DOCUMENT_CODE_IFEE));
				LOGGER.info(MessageFormat.format("IFW(IFEE) found for the application({0}). Updating status..",
						appNumber));
			}

			// update MDM application base record
			queueService.updateSuccessStatus(app);
		} else {
			LOGGER.info(MessageFormat.format("No IFW found for the application({0}).", appNumber));
		}
	}

	private void processPublishedApplications(final List<N417TreatmentQueue> publishedApplicationList,
			final Map<String, String> oaDocumentCodes) {
		String tmpStoreDirectory = FolderRelativePathEnum.CRAWLER.getAbsolutePath(N417_TEMP_DOWNLOAD_DIR);
		// clean ifw temp store
		BBWebCrawlerUtils.cleanTempDirectory(tmpStoreDirectory);

		final UnzipUtility unzipper = new UnzipUtility();
		final WebDriver webDriver = BBWebCrawlerUtils.getWebDriver(tmpStoreDirectory);
		try {
			publishedApplicationList.forEach(app -> initReedTechTreatment(app, webDriver, tmpStoreDirectory, unzipper,
					oaDocumentCodes));

			// clean ifw temp store
			BBWebCrawlerUtils.cleanTempDirectory(tmpStoreDirectory);
		} finally {
			BBWebCrawlerUtils.logoutAndCloseWebDriver(webDriver, LOGGER);
		}
	}

	private static Map<String, Calendar> readIFWFile(final File ifwFile) {
		Map<String, Calendar> ifwData = new HashMap<String, Calendar>();
		if (BBWebCrawlerUtils.isFileNotEmpty(ifwFile)) {

			LineIterator lineIterator;
			try {
				lineIterator = FileUtils.lineIterator(ifwFile);
				String dataRow = null;
				lineIterator.nextLine();

				while (lineIterator.hasNext()) {
					dataRow = lineIterator.nextLine();
					String[] data = StringUtils.split(dataRow);

					String mailRoomDateTxt = StringUtils.trim(data[0]);
					String documentCode = data[1];
					Calendar mailRoomDate = BlackboxDateUtil.getCalendar(mailRoomDateTxt);

					Calendar existingRecordDate = ifwData.get(documentCode);
					if (existingRecordDate == null || mailRoomDate.compareTo(existingRecordDate) > 0) {
						ifwData.put(documentCode, mailRoomDate);
					}
				}
				lineIterator.close();
			} catch (IOException e) {
				LOGGER.error("Error while reading IFW tsv file", e);
			}
		} else {
			LOGGER.error("IFW tsv file is found empty.");
		}
		return ifwData;
	}
	
	private static boolean unzipIFWFile(final String tmpStoreDirectory, final UnzipUtility unzipper, String zipFilePath) {
		boolean success = false;
		try {
			unzipper.unzip(zipFilePath, tmpStoreDirectory);
			success = true;
		} catch (IOException e) {
			LOGGER.error(MessageFormat.format("Unable to unzip downloaded package({0})", zipFilePath), e);
		}
		return success;
	}

}
