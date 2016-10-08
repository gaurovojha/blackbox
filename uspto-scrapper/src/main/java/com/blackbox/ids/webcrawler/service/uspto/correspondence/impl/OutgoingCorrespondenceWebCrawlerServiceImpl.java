package com.blackbox.ids.webcrawler.service.uspto.correspondence.impl;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationCorrespondence;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationCorrespondenceData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.DocumentData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.DocumentList;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.OutgoingCorrespondence;
import com.blackbox.ids.core.dto.webcrawler.PDFSplittingAndStagingDataDTO;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.correspondence.Correspondence.Source;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.Correspondence.SubSourceTypes;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.mstr.AuthenticationData;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.repository.CustomerRepository;
import com.blackbox.ids.core.repository.DocumentCodeRepository;
import com.blackbox.ids.core.repository.ExclusionListRepository;
import com.blackbox.ids.core.repository.InclusionListRepository;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceStagingRepository;
import com.blackbox.ids.core.repository.webcrawler.AuthenticationDataRepository;
import com.blackbox.ids.core.repository.webcrawler.DownloadOfficeActionQueueRepository;
import com.blackbox.ids.core.util.BlackboxPropertyUtil;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.PDFUtil;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.dto.CorrespondenceCrawlerDTO;
import com.blackbox.ids.exception.AuthenticationFailureException;
import com.blackbox.ids.exception.WebCrawlerGenericException;
import com.blackbox.ids.services.common.EmailService;
import com.blackbox.ids.services.validation.NumberFormatValidationService;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IOutgoingCorrespondenceScrapper;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IOutgoingCorrespondenceWebCrawlerService;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IPDFSplittingNStagingDataPrepService;

/**
 * The Class OutgoingCorrespondenceWebCrawlerServiceImpl is the implementing class for
 * {@link IOutgoingCorrespondenceWebCrawlerService}. This class is used to scrap the USPTO website and prepare
 * correspondence staging data after validations.
 */
@Service
public class OutgoingCorrespondenceWebCrawlerServiceImpl implements IOutgoingCorrespondenceWebCrawlerService {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(OutgoingCorrespondenceWebCrawlerServiceImpl.class);

	/** The Constant AUTOMATIC. */
	public static final String AUTOMATIC = "Automatic";

	/** The correspondence staging repository. */
	@Autowired
	private CorrespondenceStagingRepository correspondenceStagingRepository;

	/** The jurisdiction repository. */
	@Autowired
	private JurisdictionRepository jurisdictionRepository;

	/** The document code repository. */
	@Autowired
	private DocumentCodeRepository documentCodeRepository;

	/** The inclusion list repository. */
	@Autowired
	private InclusionListRepository inclusionListRepository;

	/** The exclusion list repository. */
	@Autowired
	private ExclusionListRepository exclusionListRepository;

	/** The properties. */
	@Autowired
	private BBWebCrawlerPropertyConstants properties;

	/** The download office action queue repository. */
	@Autowired
	private DownloadOfficeActionQueueRepository downloadOfficeActionQueueRepository;

	/** The data prep service. */
	@Autowired
	private IPDFSplittingNStagingDataPrepService dataPrepService;

	/** The customer repository. */
	@Autowired
	private CustomerRepository customerRepository;

	/** The authentication data repository. */
	@Autowired
	private AuthenticationDataRepository authenticationDataRepository;

	/** The outgoing correspondence scrapper. */
	@Autowired
	private IOutgoingCorrespondenceScrapper outgoingCorrespondenceScrapper;
	
	/** The number format validation service. */
	@Autowired
	private NumberFormatValidationService numberFormatValidationService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;

	/**
	 * This API scraps the USPTO website and prepares the staging data . The Steps performed are :-
	 * <p>
	 * <ul>
	 * <li>Scraps the USPTO website , applies some filter and downloads the XML.
	 * <li>The count of correspondences in XML is validated against the correspondence count on UI.
	 * <li>Selects the application number on UI based on application number in inclusion list of Blackbox.
	 * <li>Traverses the pages and selects the document code and downloads the PDF.
	 * <li>The XML data is validated with the bookmarks present in PDF
	 * <li>The PDF is splitted for each valid bookmarks and the attachment name is populated in
	 * {@link CorrespondenceStaging} entity
	 * <li>Populates the <code><b>{@link CorrespondenceStaging}</b></code> entity.
	 * <li>Persists the <code><b>{@link CorrespondenceStaging}</b></code> entity to the database.
	 *
	 * @param webCrawlerJobAudit            the entity containing the meta data of the job.
	 */
	@Override
	@Transactional
	public void execute(WebCrawlerJobAudit webCrawlerJobAudit) {

		List<CorrespondenceStaging> completeCorrespondenceStagingData = new ArrayList<CorrespondenceStaging>();
		List<OutgoingCorrespondence> outgoingCorrespondences = new ArrayList<OutgoingCorrespondence>();
		List<String> pdfNames = new ArrayList<String>();
		List<DownloadOfficeActionQueue> downloadOfficeActionQueues = new ArrayList<DownloadOfficeActionQueue>();
		Long crawlerId = webCrawlerJobAudit.getId();

		LOGGER.info(MessageFormat.format("Web Crawler Service for Outgoing Correspondence job id [{0}] Called ",
				crawlerId));

		List<String> countryCodes = new ArrayList<String>();
		Map<String, DocumentCode> documentCodeMap = new HashMap<String, DocumentCode>();
		HashMap<String, Jurisdiction> jurisdictionMap = new HashMap<String, Jurisdiction>();
		Map<String, List<String>> customerToAppNumbersInBBXMap = new HashMap<String, List<String>>();
		List<String> customersInBBX = new ArrayList<String>();

		countryCodes.add(BBWebCrawerConstants.US_CODE);
		countryCodes.add(BBWebCrawerConstants.WO_CODE);
		LOGGER.info("Fetching document codes for US jurisdiction");

		List<DocumentCode> documentCodes = documentCodeRepository.findDocumentByCountryCodes(countryCodes);
		List<Object[]> customerToApplicationNumberInBBX = customerRepository.findAllApplicationNumbersForAllCustomers();

		prepareBBXCustomerNoToAppNosMap(customerToAppNumbersInBBXMap, customerToApplicationNumberInBBX);
		prepareDocumentAndJursidictionMap(documentCodes, documentCodeMap, jurisdictionMap);
		customersInBBX = new ArrayList<String>(customerToAppNumbersInBBXMap.keySet());

		boolean isInclusionListOn = StringUtils.equalsIgnoreCase(Constant.ON,
				BlackboxPropertyUtil.getProperty(Constant.INCLUSION_LIST_ON_FLAG));
		List<String> inclusionList = new ArrayList<String>();
		List<String> exclusionList = new ArrayList<String>();
		
		CorrespondenceCrawlerDTO correspondenceCrawlerDTO = new CorrespondenceCrawlerDTO();
		correspondenceCrawlerDTO.setCustomersInBBX(customersInBBX);
		
		correspondenceCrawlerDTO.setJurisdictionMap(jurisdictionMap);
		correspondenceCrawlerDTO.setDownloadOfficeActionQueues(downloadOfficeActionQueues);
		correspondenceCrawlerDTO.setPdfNames(pdfNames);
		

		if (isInclusionListOn) {
			inclusionList = inclusionListRepository.findAllActiveApplicatinNumbers();
		} else {
			exclusionList = exclusionListRepository.findActiveApplicationNumbers();
		}
		
		correspondenceCrawlerDTO.setInclusionList(inclusionList);
		correspondenceCrawlerDTO.setExclusionList(exclusionList);

		List<AuthenticationData> epfFiles = authenticationDataRepository.findAll();

		if (CollectionUtils.isNotEmpty(epfFiles)) {
			for (AuthenticationData epfFile : epfFiles) {
				processEachAuthenticationData(webCrawlerJobAudit, completeCorrespondenceStagingData,
						outgoingCorrespondences, crawlerId,
						documentCodes, correspondenceCrawlerDTO, epfFile);
			}
		} else {
			LOGGER.info(MessageFormat.format("No EPF file found during crawler id {0}", crawlerId));
		}
		LOGGER.info(MessageFormat.format("Outgoing Correspondence Web Crawler id : [{0}] Completed Succesfully",
				crawlerId));

	}

	/**
	 * Process each authentication data.
	 *
	 * @param webCrawlerJobAudit            the web crawler job audit
	 * @param completeCorrespondenceStagingData            the complete correspondence staging data
	 * @param outgoingCorrespondences            the outgoing correspondences
	 * @param crawlerId            the crawler id
	 * @param documentCodes            the document codes
	 * @param correspondenceCrawlerDTO the correspondence crawler dto
	 * @param epfFile            the epf file
	 */
	private void processEachAuthenticationData(WebCrawlerJobAudit webCrawlerJobAudit,
			List<CorrespondenceStaging> completeCorrespondenceStagingData,
			List<OutgoingCorrespondence> outgoingCorrespondences, Long crawlerId,
			List<DocumentCode> documentCodes, CorrespondenceCrawlerDTO correspondenceCrawlerDTO, AuthenticationData epfFile) {
		WebDriver webDriver = null;
		
		List<String> pdfNames = correspondenceCrawlerDTO.getPdfNames();
		List<String> customersInBBX = correspondenceCrawlerDTO.getCustomersInBBX();
		Map<String, Jurisdiction> jurisdictionMap = correspondenceCrawlerDTO.getJurisdictionMap();
		List<DownloadOfficeActionQueue> downloadOfficeActionQueues = correspondenceCrawlerDTO.getDownloadOfficeActionQueues(); 
		
		try {
			// Step 2 : Scrap and Prepare Data for each EPF file.
			LOGGER.info(MessageFormat.format("Web Crawling started for epf id {0}", epfFile.getId()));
			
			String absolutePath = BlackboxUtils.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath(BBWebCrawerConstants.DOWNLOADS_FOLDER));

			File absolutePathFile = new File(absolutePath);
			if (!absolutePathFile.exists()) {
				absolutePathFile.mkdirs();
			}
			String clientId = properties.clientId;
			String downloadDir = BBWebCrawlerUtils.getCompleteFilePath(absolutePath, webCrawlerJobAudit.getId(),
					clientId);

			webDriver = BBWebCrawlerUtils.getWebDriver(downloadDir);
			boolean isAnyRecordsSelectedOnUI = performScrapping(outgoingCorrespondences, pdfNames, crawlerId,
					documentCodes, correspondenceCrawlerDTO, epfFile, webDriver);

			if (isAnyRecordsSelectedOnUI) {
				LOGGER.info(MessageFormat.format(
						"Web Scrapping for Crawler Id [{0}] , epf file id [{1}] completed . About to split PDF and prepare staging data",
						crawlerId, epfFile.getId()));

				validateAndPrepareStagingData(completeCorrespondenceStagingData, outgoingCorrespondences, pdfNames,
						downloadOfficeActionQueues, jurisdictionMap, webCrawlerJobAudit.getId(), customersInBBX);
			} else {
				LOGGER.info(MessageFormat.format(
						"No Staging data to prepare as no record has been selected on UI for crawler id [{0}],epf file id [{1}]",
						crawlerId, epfFile.getId()));
			}
		} catch (IOException e) {
			String errorMessage = "Error occurred during bookmark match and PDF splitting . Exception is "
					+ e.getMessage();
			LOGGER.error("Error occurred during bookmark match and PDF splitting . Exception is " + e.getMessage(), e);
			throw new WebCrawlerGenericException(errorMessage, e);
		} catch (AuthenticationFailureException afex) {
			String error = MessageFormat.format(
					"Some exception occured while crawling for job id {0} for epf file id {1}. Exception is {2}",
					webCrawlerJobAudit.getId(), afex, epfFile.getId());
			LOGGER.info(error);
			String emailMessage = "Customer with epf file "+ epfFile.getAuthenticationFileName() + " could not be authenticated succesfully";
			sendEmail(emailMessage);
			
		} finally {
			BBWebCrawlerUtils.logoutAndCloseWebDriver(webDriver, LOGGER);
		}
	}

	/**
	 * Perform scrapping.
	 *
	 * @param outgoingCorrespondences the outgoing correspondences
	 * @param pdfNames the pdf names
	 * @param crawlerId the crawler id
	 * @param documentCodes the document codes
	 * @param correspondenceCrawlerDTO the correspondence crawler dto
	 * @param epfFile the epf file
	 * @param webDriver the web driver
	 * @return true, if successful
	 */
	private boolean performScrapping(List<OutgoingCorrespondence> outgoingCorrespondences,
			List<String> pdfNames, Long crawlerId, List<DocumentCode> documentCodes,
			CorrespondenceCrawlerDTO correspondenceCrawlerDTO, AuthenticationData epfFile, WebDriver webDriver) {

		boolean isAnyRecordsSelectedOnUI = outgoingCorrespondenceScrapper.scrapAndPrepareData(webDriver,
				outgoingCorrespondences, pdfNames, documentCodes, crawlerId, correspondenceCrawlerDTO, epfFile);
		return isAnyRecordsSelectedOnUI;
	}

	/**
	 * Prepare bbx customer no to app nos map.
	 * @param customerToAppNumbersInBBXMap
	 *            the customer to app numbers in bbx map
	 * @param customerToApplicationNumberInBBX
	 *            the customer to application number in bbx
	 */
	private void prepareBBXCustomerNoToAppNosMap(Map<String, List<String>> customerToAppNumbersInBBXMap,
			List<Object[]> customerToApplicationNumberInBBX) {
		if (CollectionUtils.isNotEmpty(customerToApplicationNumberInBBX)) {
			for (Object[] object : customerToApplicationNumberInBBX) {
				String customerNumber = (String) object[0];
				String applicationNumber = (String) object[1];

				if (customerToAppNumbersInBBXMap.containsKey(customerNumber)) {
					List<String> applicationNumbers = customerToAppNumbersInBBXMap.get(customerNumber);
					applicationNumbers.add(applicationNumber);
				} else {
					List<String> applicationNumbers = new ArrayList<String>();
					applicationNumbers.add(applicationNumber);
					customerToAppNumbersInBBXMap.put(customerNumber, applicationNumbers);
				}
			}
		}
	}

	/**
	 * Validate and prepare staging data.
	 *
	 * @param completeCorrespondenceStagingData            the correspondence staging data
	 * @param outgoingCorrespondences            the outgoing correspondences
	 * @param pdfNames            the pdf names
	 * @param downloadOfficeActionQueues            the download office action queues
	 * @param jurisdictionMap            the jurisdiction map
	 * @param crawlerId the crawler id
	 * @param customersInBBX the customers in bbx
	 * @throws IOException             Signals that an I/O exception has occurred.
	 */
	private void validateAndPrepareStagingData(List<CorrespondenceStaging> completeCorrespondenceStagingData,
			List<OutgoingCorrespondence> outgoingCorrespondences, List<String> pdfNames,
			List<DownloadOfficeActionQueue> downloadOfficeActionQueues, Map<String, Jurisdiction> jurisdictionMap,
			Long crawlerId, List<String> customersInBBX) throws IOException {
		if (CollectionUtils.isNotEmpty(outgoingCorrespondences) && CollectionUtils.isNotEmpty(pdfNames)) {
			OutgoingCorrespondence outgoingCorrespondence = outgoingCorrespondences.get(0);
			final ApplicationCorrespondence applicationCorrespondence = outgoingCorrespondence
					.getApplicationCorrespondence();
			final List<ApplicationCorrespondenceData> applicationCorrespondenceDatas = applicationCorrespondence != null
					? applicationCorrespondence.getApplicationCorrespondenceData() : null;

			traverseXMLAndValidateBookmarks(completeCorrespondenceStagingData, pdfNames, downloadOfficeActionQueues,
					applicationCorrespondenceDatas, jurisdictionMap, crawlerId, customersInBBX);

			if (CollectionUtils.isNotEmpty(downloadOfficeActionQueues)) {
				LOGGER.info("Saving download office queue data");

				downloadOfficeActionQueueRepository.save(downloadOfficeActionQueues);

				LOGGER.info("Download office Action Queue Data saved succesfully");
			}
			if (CollectionUtils.isNotEmpty(completeCorrespondenceStagingData)) {
				LOGGER.info("Saving the Correspondence staging data");

				correspondenceStagingRepository.save(completeCorrespondenceStagingData);

				LOGGER.info("Correspondence Staging data saved succesfully");
			}
		}
	}

	/**
	 * Prepare document and jursidiction map.
	 * @param documentCodes
	 *            the document codes
	 * @param documentCodeMap
	 *            the document code map
	 * @param jurisdictionMap
	 *            the jurisdiction map
	 */
	private void prepareDocumentAndJursidictionMap(List<DocumentCode> documentCodes,
			Map<String, DocumentCode> documentCodeMap, HashMap<String, Jurisdiction> jurisdictionMap) {
		for (DocumentCode code : documentCodes) {
			documentCodeMap.put(code.getCode(), code);
		}

		List<Jurisdiction> jurisdictions = jurisdictionRepository.findAll();
		if (CollectionUtils.isNotEmpty(jurisdictions)) {
			for (Jurisdiction jurisdiction : jurisdictions) {
				jurisdictionMap.put(jurisdiction.getCode(), jurisdiction);
			}

		}
	}

	/**
	 * Traverse xml and validate bookmarks.
	 *
	 * @param completeCorrespondenceStagingData            the correspondence staging data
	 * @param pdfNames            the pdf names
	 * @param downloadOfficeActionQueues            the download office action queues
	 * @param applicationCorrespondenceDatas            the application correspondence datas
	 * @param jurisdictionMap            the jurisdiction map
	 * @param crawlerId the crawler id
	 * @param customersInBBX the customers in bbx
	 * @throws IOException             Signals that an I/O exception has occurred.
	 */
	private void traverseXMLAndValidateBookmarks(List<CorrespondenceStaging> completeCorrespondenceStagingData,
			List<String> pdfNames, List<DownloadOfficeActionQueue> downloadOfficeActionQueues,
			final List<ApplicationCorrespondenceData> applicationCorrespondenceDatas,
			Map<String, Jurisdiction> jurisdictionMap, Long crawlerId, List<String> customersInBBX)
					throws IOException {
		List<CorrespondenceStaging> stagingDataPerMergedPDF;
		List<String> folderList;

		if (CollectionUtils.isNotEmpty(pdfNames)) {

			// Iterate over each downloaded PDF's
			for (String mergedPdfPath : pdfNames) {
				stagingDataPerMergedPDF = new ArrayList<CorrespondenceStaging>();

				if (CollectionUtils.isNotEmpty(applicationCorrespondenceDatas)) {

					// Step 2.1 : Prepare partial staging data and download office
					// action queue data.
					LOGGER.info("Validating bookmark and preparing staging data for PDF " + mergedPdfPath);
					List<String> bookmarkDescriptions = PDFUtil.getBookmarksNames(mergedPdfPath);

					LOGGER.info("About to Split the PDF on the basis of bookmark for merged PDF " + mergedPdfPath);

					String absolutePath = BlackboxUtils
							.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath(BBWebCrawerConstants.DOWNLOADS_FOLDER));
					String targetFolder = BBWebCrawlerUtils.getCompleteFilePath(absolutePath, crawlerId,
							properties.clientId);

					folderList = new ArrayList<String>();
					folderList.add(mergedPdfPath);
					folderList.add(targetFolder);

					// Do the splitting for each PDF's
					matchBookmarkAndPrepareStagingDataPerPDF(folderList, stagingDataPerMergedPDF,
							downloadOfficeActionQueues, applicationCorrespondenceDatas, jurisdictionMap,
							bookmarkDescriptions, customersInBBX);

					completeCorrespondenceStagingData.addAll(stagingDataPerMergedPDF);

				}
			}
		} else {
			LOGGER.info("No PDF has been downloaded for Crawler id [" + crawlerId
					+ "] hence no validation of PDF bookmark with XML would occur.");
		}
	}

	/**
	 * Creates the pdf with bookmark.
	 *
	 * @param folderList the folder list
	 * @param stagingDataPerMergedPDF the staging data per merged pdf
	 * @param downloadOfficeActionQueues            the download office action queues
	 * @param applicationCorrespondenceDatas            the application correspondence datas
	 * @param jurisdictionMap            the jurisdiction map
	 * @param bookmarkDescriptions            the bookmark descriptions
	 * @param customersInBBX the customers in bbx
	 * @throws IOException             Signals that an I/O exception has occurred.
	 */
	public void matchBookmarkAndPrepareStagingDataPerPDF(List<String> folderList,
			List<CorrespondenceStaging> stagingDataPerMergedPDF,
			List<DownloadOfficeActionQueue> downloadOfficeActionQueues,
			List<ApplicationCorrespondenceData> applicationCorrespondenceDatas,
			Map<String, Jurisdiction> jurisdictionMap, List<String> bookmarkDescriptions,
			List<String> customersInBBX) throws IOException {

		String mergedPdfFilePath = folderList.get(0);
		String targetFolder = folderList.get(1);

		for (final ApplicationCorrespondenceData applicationCorrespondenceData : applicationCorrespondenceDatas) {
			final DocumentList documentList = applicationCorrespondenceData.getDocumentList();
			final List<DocumentData> documentDatas = documentList != null ? documentList.getDocumentData() : null;
			String usptoCustomerNumber = applicationCorrespondenceData.getCustomerNumber();

			if (customersInBBX.contains(usptoCustomerNumber)) {

				processValidUSPTOCustomers(stagingDataPerMergedPDF, downloadOfficeActionQueues, jurisdictionMap,
						bookmarkDescriptions, mergedPdfFilePath, targetFolder, applicationCorrespondenceData,
						documentDatas, usptoCustomerNumber);
			} else {
				LOGGER.info(MessageFormat.format("The customer number {0} in XML is not a valid BBX customer number. "
						+ "Hence not validating the XML key with PDF bookmark", usptoCustomerNumber));
			}

		}
	}

	/**
	 * Process valid uspto customers.
	 *
	 * @param stagingDataPerMergedPDF the staging data per merged pdf
	 * @param downloadOfficeActionQueues the download office action queues
	 * @param jurisdictionMap the jurisdiction map
	 * @param bookmarkDescriptions the bookmark descriptions
	 * @param mergedPdfFilePath the merged pdf file path
	 * @param targetFolder the target folder
	 * @param applicationCorrespondenceData the application correspondence data
	 * @param documentDatas the document datas
	 * @param usptoCustomerNumber the uspto customer number
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void processValidUSPTOCustomers(List<CorrespondenceStaging> stagingDataPerMergedPDF,
			List<DownloadOfficeActionQueue> downloadOfficeActionQueues, Map<String, Jurisdiction> jurisdictionMap,
			List<String> bookmarkDescriptions, String mergedPdfFilePath, String targetFolder,
			final ApplicationCorrespondenceData applicationCorrespondenceData, final List<DocumentData> documentDatas,
			String usptoCustomerNumber) throws IOException {
		if (CollectionUtils.isNotEmpty(documentDatas)) {

			for (DocumentData documentData : documentDatas) {
				processEachXMLRecord(stagingDataPerMergedPDF, downloadOfficeActionQueues, jurisdictionMap,
						bookmarkDescriptions, mergedPdfFilePath, targetFolder, applicationCorrespondenceData,
						documentData);

			}

		} else {
			LOGGER.info(
					MessageFormat.format(
							"The customer number {0} in XML does not have any documents specified. "
									+ "Hence unable to validate the XML key with PDF bookmark",
							usptoCustomerNumber));
		}
	}

	/**
	 * Process each xml record.
	 *
	 * @param stagingDataPerMergedPDF the staging data per merged pdf
	 * @param downloadOfficeActionQueues the download office action queues
	 * @param jurisdictionMap the jurisdiction map
	 * @param bookmarkDescriptions the bookmark descriptions
	 * @param mergedPdfFilePath the merged pdf file path
	 * @param targetFolder the target folder
	 * @param applicationCorrespondenceData the application correspondence data
	 * @param documentData the document data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void processEachXMLRecord(List<CorrespondenceStaging> stagingDataPerMergedPDF,
			List<DownloadOfficeActionQueue> downloadOfficeActionQueues, Map<String, Jurisdiction> jurisdictionMap,
			List<String> bookmarkDescriptions, String mergedPdfFilePath, String targetFolder,
			final ApplicationCorrespondenceData applicationCorrespondenceData, DocumentData documentData)
					throws IOException {
		String dateFormatInPDF = BlackboxDateUtil.changeDateFormat(BBWebCrawerConstants.DATE_FORMAT_XML,
				BBWebCrawerConstants.DATE_FORMAT_PDF, documentData.getMailRoomDate());
		String xmlKey = BlackboxUtils.concat(applicationCorrespondenceData.getCustomerNumber(),
				BBWebCrawerConstants.SPACE_STRING, applicationCorrespondenceData.getApplicationNumber(),
				BBWebCrawerConstants.SPACE_STRING, dateFormatInPDF, BBWebCrawerConstants.SPACE_STRING,
				documentData.getDocumentDescription());

		if (!bookmarkDescriptions.contains(xmlKey)) {
			LOGGER.debug(MessageFormat.format(
					"None of the bookmarks in the PDF {0} matches with the XML key {1}.Moving to download office action queue",
					mergedPdfFilePath, xmlKey));
			prepareDownloadOfficeActionQueue(downloadOfficeActionQueues, applicationCorrespondenceData,
					documentData);
		} else {
			processValidBookMarks(stagingDataPerMergedPDF, downloadOfficeActionQueues, jurisdictionMap,
					bookmarkDescriptions, mergedPdfFilePath, targetFolder, applicationCorrespondenceData, documentData,
					xmlKey);

		}
	}

	/**
	 * Process valid book marks.
	 *
	 * @param stagingDataPerMergedPDF the staging data per merged pdf
	 * @param downloadOfficeActionQueues the download office action queues
	 * @param jurisdictionMap the jurisdiction map
	 * @param bookmarkDescriptions the bookmark descriptions
	 * @param mergedPdfFilePath the merged pdf file path
	 * @param targetFolder the target folder
	 * @param applicationCorrespondenceData the application correspondence data
	 * @param documentData the document data
	 * @param xmlKey the xml key
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void processValidBookMarks(List<CorrespondenceStaging> stagingDataPerMergedPDF,
			List<DownloadOfficeActionQueue> downloadOfficeActionQueues, Map<String, Jurisdiction> jurisdictionMap,
			List<String> bookmarkDescriptions, String mergedPdfFilePath, String targetFolder,
			final ApplicationCorrespondenceData applicationCorrespondenceData, DocumentData documentData, String xmlKey)
					throws IOException {
		PDFSplittingAndStagingDataDTO pdfSplittingAndStagingDataDTO;
		for (String bookMarkText : bookmarkDescriptions) {

			LOGGER.debug("Xml Key is " + xmlKey);
			LOGGER.debug("Bookmark from PDF is " + bookMarkText);

			if (xmlKey.equalsIgnoreCase(bookMarkText)) {
				LOGGER.info("Keys in XML and bookmark match . Preparing Staging Data");
				CorrespondenceStaging correspondenceStaging = new CorrespondenceStaging();
				populateCorrespondenceStagingEntity(applicationCorrespondenceData, documentData,
						correspondenceStaging, jurisdictionMap);

				pdfSplittingAndStagingDataDTO = new PDFSplittingAndStagingDataDTO(mergedPdfFilePath,
						stagingDataPerMergedPDF, targetFolder, bookmarkDescriptions, true, null,
						null, bookMarkText, correspondenceStaging, downloadOfficeActionQueues,
						applicationCorrespondenceData, documentData);

				dataPrepService.splitPDFAndPrepareStagingData(pdfSplittingAndStagingDataDTO);

				stagingDataPerMergedPDF.add(correspondenceStaging);
			}

		}
	}

	/**
	 * Prepare download office action queue.
	 *
	 * @param downloadOfficeActionQueues            the download office action queues
	 * @param applicationCorrespondenceData            the application correspondence data
	 * @param documentData            the document data
	 */
	private void prepareDownloadOfficeActionQueue(List<DownloadOfficeActionQueue> downloadOfficeActionQueues,
			ApplicationCorrespondenceData applicationCorrespondenceData, DocumentData documentData) {
		DownloadOfficeActionQueue downloadOfficeActionQueue = new DownloadOfficeActionQueue();
		prepareDownloadOfficeActionEntity(downloadOfficeActionQueue, applicationCorrespondenceData, documentData);
		downloadOfficeActionQueues.add(downloadOfficeActionQueue);
	}

	/**
	 * Prepare entity.
	 *
	 * @param downloadOfficeActionQueue            the download office action queue
	 * @param applicationCorrespondenceData            the application correspondence data
	 * @param documentData            the document data
	 */
	private void prepareDownloadOfficeActionEntity(DownloadOfficeActionQueue downloadOfficeActionQueue,
			ApplicationCorrespondenceData applicationCorrespondenceData, DocumentData documentData) {
		Calendar filingDate = BlackboxDateUtil.toCalendar(applicationCorrespondenceData.getFilingDate(),
				TimestampFormat.yyyyMMdd);
		boolean isWOApplication = applicationCorrespondenceData.getApplicationNumber().startsWith(BBWebCrawerConstants.WO_CODE_INDICATOR)
				? true : false;
		String jurisdictionCode = isWOApplication ? BBWebCrawerConstants.WO_CODE : BBWebCrawerConstants.US_CODE;

		String rawApplicationNumber = applicationCorrespondenceData.getApplicationNumber();
		String formattedApplicationNumber = numberFormatValidationService.getConvertedValue(rawApplicationNumber, NumberType.APPLICATION,
				jurisdictionCode, isWOApplication ? ApplicationType.PCT_US_SUBSEQUENT_FILING : ApplicationType.ALL, filingDate, null);
		downloadOfficeActionQueue.setApplicationNumberFormatted(formattedApplicationNumber);
		downloadOfficeActionQueue.setAppNumberRaw(rawApplicationNumber);
		downloadOfficeActionQueue.setDocumentCode(documentData.getFileWrapperDocumentCode());
		downloadOfficeActionQueue.setPageCount(Integer.valueOf(documentData.getPageQuantity()));
		downloadOfficeActionQueue.setRetryCount(1);
		downloadOfficeActionQueue.setCustomerNumber(applicationCorrespondenceData.getCustomerNumber());
		downloadOfficeActionQueue.setJurisdictionCode(jurisdictionCode);
		downloadOfficeActionQueue.setMailingDate(
				BlackboxDateUtil.strToCalendar(documentData.getMailRoomDate(), TimestampFormat.yyyyMMdd));
		downloadOfficeActionQueue.setFilingDate(filingDate);
		downloadOfficeActionQueue.setStatus(QueueStatus.INITIATED);
		downloadOfficeActionQueue.setCreatedByUser(User.SYSTEM_ID);
		downloadOfficeActionQueue.setCreatedDate(Calendar.getInstance());

	}

	/**
	 * Populate entity.
	 * @param applicationCorrespondenceData
	 *            the application correspondence data
	 * @param documentData
	 *            the document data
	 * @param correspondenceStaging
	 *            the correspondence staging
	 * @param jurisdictionMap
	 *            the jurisdiction map
	 */
	private void populateCorrespondenceStagingEntity(final ApplicationCorrespondenceData applicationCorrespondenceData,
			DocumentData documentData, CorrespondenceStaging correspondenceStaging,
			Map<String, Jurisdiction> jurisdictionMap) {
		String usptoRawApplicationNumber = applicationCorrespondenceData.getApplicationNumber();
		String jurisdictionCode = usptoRawApplicationNumber.startsWith(BBWebCrawerConstants.WO_CODE_INDICATOR)
				? jurisdictionMap.get(BBWebCrawerConstants.WO_CODE).getCode()
				: jurisdictionMap.get(BBWebCrawerConstants.US_CODE).getCode();
		Calendar filingDate = BlackboxDateUtil.strToCalendar(applicationCorrespondenceData.getFilingDate(),
				TimestampFormat.yyyyMMdd);
		String usptoFormattedApplicationNumber = BBWebCrawlerUtils.formatApplicationNumber(usptoRawApplicationNumber,
				jurisdictionCode, filingDate);

		correspondenceStaging.setApplicationNumber(usptoFormattedApplicationNumber);
		correspondenceStaging.setApplicationNumberUnFormatted(usptoRawApplicationNumber);
		correspondenceStaging.setCustomerNumber(applicationCorrespondenceData.getCustomerNumber());
		correspondenceStaging.setCreatedByUser(User.SYSTEM_ID);
		correspondenceStaging.setDocumentCode(documentData.getFileWrapperDocumentCode());
		correspondenceStaging.setDocumentDescription(documentData.getDocumentDescription());
		correspondenceStaging.setFilingDate(filingDate);
		correspondenceStaging.setStatus(Status.PENDING);
		correspondenceStaging.setJurisdictionCode(jurisdictionCode);
		correspondenceStaging.setMailingDate(
				BlackboxDateUtil.getCalendar(documentData.getMailRoomDate(), BBWebCrawerConstants.DATE_FORMAT_XML));
		correspondenceStaging.setSource(Source.AUTOMATIC);
		correspondenceStaging.setSubSource(SubSourceTypes.PAIR_OUTGOING_CORRESPONDENCE);
		correspondenceStaging.setPrivatePairUploadDate(
				BlackboxDateUtil.toCalendar(documentData.getUploadDate(), TimestampFormat.yyyyMMdd));
	}
	
	/**
	 * Prepare and send email.
	 * 
	 * @param jobName
	 */
	private void sendEmail(String emailMessage) {
		Message message = new Message();
		message.setTemplateType(TemplateType.CRAWLER_AUTHENTICATION);
		message.setReceiverList(userRepository.getEmailId(User.SYSTEM_ID));
		message.setStatus(Constant.MESSAGE_STATUS_NOT_SENT);

		MessageData templateData = new MessageData("body", emailMessage);
		List<MessageData> list = new ArrayList<>(1);
		list.add(templateData);
		emailService.send(message, list);

	}

}
