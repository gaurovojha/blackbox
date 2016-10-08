package com.blackbox.ids.webcrawler.service.uspto.ids.impl;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.common.constant.Constant;
import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.webcrawler.ITrackIDSFilingQueueDao;
import com.blackbox.ids.core.model.IDS.IDS.SubStatus;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.webcrawler.TrackIDSFilingQueue;
import com.blackbox.ids.core.repository.OAResponseCodeRepository;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceStagingRepository;
import com.blackbox.ids.services.common.EmailService;
import com.blackbox.ids.services.ids.ITrackIDSFilingQueueService;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.application.IApplicationWebCrawler;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IPDFSplittingNStagingDataPrepService;
import com.blackbox.ids.webcrawler.service.uspto.ids.IIDSWebCrawler;
import com.blackbox.ids.webcrawler.service.uspto.ids.ITrackIDSFilingService;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;

/**
 * Service to tracking IDS filing at USPTO. This service will read IDS from tracking queue and tracking filing status at
 * USPTO site.
 */
@Service
public class TrackIDSFilingServiceImpl implements ITrackIDSFilingService {

	/** The Constant LOGGER. */
	private static final Logger						LOGGER							= Logger.getLogger(TrackIDSFilingServiceImpl.class);

	/** The ids queue dao. */
	@Autowired
	private ITrackIDSFilingQueueDao					idsQueueDao;

	/** The login service. */
	@Autowired
	private IUSPTOLoginService						loginService;

	/** The web crawler. */
	@Autowired
	private IApplicationWebCrawler					webCrawler;

	/** The ids web crawler. */
	@Autowired
	private IIDSWebCrawler							idsWebCrawler;

	/** The Constant IDS_TRACKING_TEMP_DOWNLOAD_DIR. */
	private static final String						IDS_TRACKING_TEMP_DOWNLOAD_DIR	= "idsTracking";

	/** The data prep service. */
	@Autowired
	private IPDFSplittingNStagingDataPrepService	dataPrepService;

	/** The correspondence staging repository. */
	@Autowired
	private CorrespondenceStagingRepository			correspondenceStagingRepository;

	/** The ids service. */
	@Autowired
	private ITrackIDSFilingQueueService				idsService;

	/** The notification process service. */
	@Autowired
	private NotificationProcessService				notificationProcessService;

	@Autowired
	private OAResponseCodeRepository				responseCodeRepository;

	/** The user repository impl. */
	@Autowired
	private UserRepository							userRepositoryImpl;

	@Autowired
	private ApplicationBaseDAO 						applicationDao;

	/** The constants. */
	@Autowired
	private BBWebCrawlerPropertyConstants			properties;

	@Autowired
	private EmailService							emailService;

	@Override
	public void execute(final int maxRetryCount) {
		LOGGER.info("Web crawler for track IDS filing started..");

		// step : fetch application staging records sorted by customer number
		Map<String, List<TrackIDSFilingQueue>> customerIDSMap = idsQueueDao.getIdSTrackingQueueByCustomer();
		final String downloadDir = FolderRelativePathEnum.CRAWLER.getAbsolutePath(IDS_TRACKING_TEMP_DOWNLOAD_DIR);

		// clean download directory
		BBWebCrawlerUtils.cleanTempDirectory(downloadDir);

		if (MapUtils.isNotEmpty(customerIDSMap)) {
			final String idsDocumentDescription = responseCodeRepository
					.findDescriptionByDocumentCode(BBWebCrawerConstants.IDS_DOCUMENT_CODE);
			customerIDSMap.entrySet().forEach(
					entry -> initWebCrawler(entry.getKey(), entry.getValue(), downloadDir, maxRetryCount,
							idsDocumentDescription));
		} else {
			LOGGER.info("Track IDS filing queue found empty.");
		}
		LOGGER.info("Web crawler for track IDS filing completed.");
	}

	/**
	 * Initialize the web crawler for IDS tracking.
	 * @param customerNumber
	 *            the customer number
	 * @param idsList
	 *            the ids list
	 * @param downloadDir
	 *            the download dir
	 * @param maxRetryCount
	 *            the max retry count
	 */
	private void initWebCrawler(final String customerNumber, final List<TrackIDSFilingQueue> idsList,
			final String downloadDir, final int maxRetryCount, String idsDocumentDescription) {
		// step1 : create web driver
		final WebDriver webDriver = BBWebCrawlerUtils.getWebDriver(downloadDir);
		try {
			LOGGER.info(MessageFormat.format("Started IDS tracking for Customer [{0}] ", customerNumber));

			// step2 : authenticate customer
			boolean success = loginService.login(webDriver, customerNumber);
			if (success) {
				// step3 : process IFW(IDS) for ids list
				IntStream.range(0, idsList.size()).forEach(
						idx -> scrapIDSApplication(idsList.get(idx), idx == 0, webDriver, downloadDir, maxRetryCount));

				// PDF splitting and preparing staging data.
				processPDFAndPrepareStagingData(idsList, downloadDir, maxRetryCount, idsDocumentDescription);
			} else {
				LOGGER.error(MessageFormat
						.format("Authentication failed for customer({0}), updating status for all the corresponding queue entries.",
								customerNumber));
				idsList.forEach(ids -> handleError(ids, maxRetryCount));
			}
		} finally {
			BBWebCrawlerUtils.logoutAndCloseWebDriver(webDriver, LOGGER);
		}
	}

	/**
	 * Scrap IDS application at USPTO and search IFW tab.
	 * @param ids
	 *            the ids
	 * @param isFirstRecord
	 *            the is first record
	 * @param webDriver
	 *            the web driver
	 * @param downloadDir
	 *            the download directory
	 * @param maxRetryCount
	 *            the max retry count
	 */
	private void scrapIDSApplication(final TrackIDSFilingQueue ids, final boolean isFirstRecord,
			final WebDriver webDriver, final String downloadDir, final int maxRetryCount) {
		// step1 : select new case
		if (!isFirstRecord) {
			webCrawler.selectNewCase(webDriver);
		}

		String appNumber = ids.getAppNumberRaw();
		String jurisdiction = ids.getJurisdictionCode();

		LOGGER.info(MessageFormat.format("Searching IFW data for the application({0})..", appNumber));

		// step2 : Search for application
		webCrawler.searchApplicationNumber(webDriver, appNumber, jurisdiction);

		// step3 : check if application details page opened
		boolean isFindApplicationSuccess = webCrawler.checkApplicationDetailsPageStatus(webDriver, appNumber,
				jurisdiction, null);

		// step4 : check for error case
		if (!isFindApplicationSuccess) {
			// error opening page
			LOGGER.error(MessageFormat.format(
					"Unable to open application details(application number :{0}, IDS id : {1}) page.", appNumber,
					ids.getIdsId()));
			handleError(ids, maxRetryCount);
			return;
		}

		// step5 : find IFW tab
		boolean isImageFileWrapperTabPresent = idsWebCrawler.checkImageFileWrapperTab(webDriver);

		// IFW tab is not present for the application
		if (!isImageFileWrapperTabPresent) {
			LOGGER.error(MessageFormat.format("IFW tab is not present for the application({0}).", appNumber));
			handleError(ids, maxRetryCount);
			return;
		}

		// step6 : IFW documents data - select IDS after creation date
		int selectedIDSRecordCount = idsWebCrawler.selectIDSIFWDocuments(webDriver, ids.getIdsDate(),
				BBWebCrawerConstants.IDS_DOCUMENT_CODE);

		boolean success = false;

		if (selectedIDSRecordCount > 0) {
			String formattedAppNumber = BBWebCrawlerUtils.formatApplicationNumber(appNumber, jurisdiction,
					ids.getFilingDate());
			success = idsWebCrawler.downloadIDSPDF(webDriver, downloadDir, formattedAppNumber);
		}

		if (!success) {
			handleError(ids, maxRetryCount);
		}
	}

	/**
	 * Process IDS pdf and prepare staging data.
	 * @param idsList
	 *            the ids list
	 * @param downloadDir
	 *            the download directory
	 * @param maxRetryCount
	 *            the max retry count
	 */
	private void processPDFAndPrepareStagingData(final List<TrackIDSFilingQueue> idsList, String downloadDir,
			final int maxRetryCount, final String idsDocumentDescription) {
		List<CorrespondenceStaging> correspondenceStageList;
		for (TrackIDSFilingQueue ids : idsList) {
			try {
				String pdfPath = downloadDir + ids.getApplicationNumberFormatted()
						+ BBWebCrawerConstants.PDF_SUFFIX_WITH_DOT;
				if (new File(pdfPath).exists()) {
					String targetFolder = downloadDir + ids.getApplicationNumberFormatted() + File.separator;
					createDirectory(targetFolder);
					correspondenceStageList = new ArrayList<CorrespondenceStaging>();
					LOGGER.debug(MessageFormat.format("Spliting pdf {0} ...", pdfPath));

					dataPrepService.splitIDSDocumentAndPrepareStagingData(ids, correspondenceStageList, pdfPath,
							targetFolder, idsDocumentDescription);
					correspondenceStagingRepository.save(correspondenceStageList);
					updateSuccessStatus(ids);
				}

			} catch (Exception e) {
				LOGGER.error(MessageFormat.format(
						"Some Exception occured while splitting PDF for IDS (IDS id : {0}). Exception is {1}",
						ids.getIdsId(), e));
				handleError(ids, maxRetryCount);
			}
		}
	}

	/**
	 * Update success status of IDS .
	 * @param ids
	 *            the ids
	 */
	private void updateSuccessStatus(final TrackIDSFilingQueue ids) {
		ids.setStatus(QueueStatus.SUCCESS);
		ids.setRetryCount(ids.getRetryCount() + 1);

		SubStatus idsSubStatus = null;
		switch (ids.getRequestType()) {
		case FIRST_RUN:
			// send email to paralegal and attorney
			sendEmail(ids);
			idsSubStatus = SubStatus.TRACKING_STATUS_REQUEST1_SUCCESS;
			break;
		case SUBSEQUENT_RUN:
			// send notification
			sendNotification(ids);
			idsSubStatus = SubStatus.TRACKING_STATUS_REQUEST2_SUCCESS;
			break;
		default:
			break;
		}
		idsService.updateIDSTrackingStatus(ids, idsSubStatus);
	}

	/**
	 * Creates the directory.
	 * @param targetFolder
	 *            the target folder
	 */
	private static void createDirectory(String targetFolder) {
		File targetDir = new File(targetFolder);
		if (!targetDir.exists()) {
			targetDir.mkdir();
		}
	}

	/**
	 * Handle error.
	 * @param ids
	 *            the ids
	 * @param maxRetryCount
	 *            the max retry count
	 */
	private void handleError(final TrackIDSFilingQueue ids, final int maxRetryCount) {
		LOGGER.error(MessageFormat.format("Updating status and retry deatils for the queue entry(id : {0})",
				ids.getId()));
		int retryCount = ids.getRetryCount() + 1;

		ids.setRetryCount(retryCount);
		if (retryCount >= maxRetryCount) {
			ids.setStatus(QueueStatus.CRAWLER_ERROR);
			SubStatus idsSubStatus = null;

			switch (ids.getRequestType()) {
			case FIRST_RUN:
				// send email to paralegal and attorney
				sendEmail(ids);
				idsSubStatus = SubStatus.TRACKING_STATUS_REQUEST1_FAILED;
				break;
			case SUBSEQUENT_RUN:
				// send notification
				sendNotification(ids);
				idsSubStatus = SubStatus.TRACKING_STATUS_REQUEST2_FAILED;
				break;
			default:
				break;
			}
			// update tracking failed status of IDS
			idsService.updateIDSTrackingStatus(ids, idsSubStatus);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, properties.idsTrackFilingNextRunDateDays);
			ids.setNextRunDate(cal);
			idsService.updateTrackIDSFilingQueue(ids);
		}
	}

	/**
	 * Send email.
	 * @param ids
	 *            the ids queue entry
	 */
	private void sendEmail(final TrackIDSFilingQueue ids) {
		// send email
		try {
			List<String> receiverList = new ArrayList<>(2);
			receiverList.add(userRepositoryImpl.getEmailId(ids.getApprovedBy()));
			receiverList.add(userRepositoryImpl.getEmailId(ids.getFiledBy()));

			Message message = new Message();
			message.setTemplateType(TemplateType.IDS_TRACKING_FAILED);
			message.setReceiverList(StringUtils.join(receiverList, ";"));
			message.setStatus(Constant.MESSAGE_STATUS_NOT_SENT);

			MessageData idsData = new MessageData("idsId", ids.getIdsBuildId());
			MessageData applicationNumber = new MessageData("applicationNumber", ids.getAppNumberRaw());
			List<MessageData> list = new ArrayList<>(2);
			list.add(idsData);
			list.add(applicationNumber);
			emailService.send(message, list);
			LOGGER.info(MessageFormat.format("Email sent for IDS Tracking failed [queue id : {1},  IDS id : {2}].",
					ids.getId(), ids.getIdsId()));
		} catch (Exception e) {
			LOGGER.error("Error while send email to paralegal and attorney", e);
		}
	}

	/**
	 * Send notification.
	 * @param ids
	 *            the ids
	 */
	private void sendNotification(final TrackIDSFilingQueue ids) {
		try {
			ApplicationBase application = applicationDao.find(ids.getApplicationId());
			Long notificationProcessId = notificationProcessService.createNotification(application, null, null,
					ids.getId(), EntityName.TRACK_IDS_FILING_QUEUE, NotificationProcessType.TRACK_IDS_FILING, null, null);
			LOGGER.info(MessageFormat
					.format("Notification created for IDS Tracking failed [notification process id : {0}, queue id : {1},  IDS id : {2}].",
							notificationProcessId, ids.getId(), ids.getIdsId()));
		} catch (Exception e) {
			LOGGER.error("Error while send email to paralegal and attorney", e);
		}
	}

}
