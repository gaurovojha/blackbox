/*
 * 
 */
package com.blackbox.ids.webcrawler.service.uspto.correspondence.impl;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dto.webcrawler.PDFSplittingAndStagingDataDTO;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.enums.WebCrawlerJobEnum;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.AuthenticationData;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobStatus;
import com.blackbox.ids.core.repository.CustomerRepository;
import com.blackbox.ids.core.repository.DocumentCodeRepository;
import com.blackbox.ids.core.repository.ExclusionListRepository;
import com.blackbox.ids.core.repository.InclusionListRepository;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceStagingRepository;
import com.blackbox.ids.core.repository.webcrawler.AuthenticationDataRepository;
import com.blackbox.ids.core.repository.webcrawler.DownloadOfficeActionQueueRepository;
import com.blackbox.ids.core.repository.webcrawler.WebCrawlerJobStatusRepository;
import com.blackbox.ids.core.util.BlackboxPropertyUtil;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.PDFUtil;
import com.blackbox.ids.dto.CorrespondenceCrawlerDTO;
import com.blackbox.ids.exception.AuthenticationFailureException;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IIFWDownloadDoCScrapper;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IIFWDownloadDoCWebCrawlerService;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IPDFSplittingNStagingDataPrepService;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;

// TODO: Auto-generated Javadoc
/**
 * The Class {@link IFWDownloadDoCWebCrawlerServiceImpl} is the implementing class for
 * {@link IIFWDownloadDoCWebCrawlerService}. This class is used to scrap the USPTO website for IFW Documents and prepare
 * correspondence staging data after validations.
 */
@Service
public class IFWDownloadDoCWebCrawlerServiceImpl implements IIFWDownloadDoCWebCrawlerService {

	/** The logger. */
	private static Logger logger = Logger.getLogger(IFWDownloadDoCWebCrawlerServiceImpl.class);

	/** The login service. */
	@Autowired
	private IUSPTOLoginService loginService;

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

	/** The application base dao. */
	@Autowired
	private ApplicationBaseDAO applicationBaseDAO;

	/** The notification process service. */
	@Autowired
	private NotificationProcessService notificationProcessService;

	/** The customer repository. */
	@Autowired
	private CustomerRepository customerRepository;
	
	/** The authentication data repository. */
	@Autowired
	private AuthenticationDataRepository authenticationDataRepository;
	
	/** The web crawler job status repository. */
	@Autowired
	private WebCrawlerJobStatusRepository webCrawlerJobStatusRepository;
	
	/** The iifw download doc scrapper. */
	@Autowired
	private IIFWDownloadDoCScrapper iifwDownloadDoCScrapper;

	/**
	 * This API scraps the USPTO website for all the application numbers present in the correspondence queue and
	 * prepares the staging data . The Steps performed are :-
	 *
	 * <p>
	 * <ul>
	 * <li>Scraps the USPTO website for each application number.
	 * <li>Matches the document codes for the application number with those in the Download Office Action Queue.
	 * <li>Downloads the accumulated PDF corresponding to all the document codes selected.
	 * <li>The PDF is splitted on the basis of bookmarks and the attachment name is populated in
	 * <code><b>{@link CorrespondenceStaging}</b></code> entity
	 * <li>Populates the <code><b>{@link CorrespondenceStaging}</b></code> entity.
	 * <li>Persists the <code><b>{@link CorrespondenceStaging}</b></code> entity to the database.
	 * 
	 * @param webCrawlerJobAudit
	 *            the entity containing the meta data of the job.
	 */
	@Override
	public void execute(WebCrawlerJobAudit webCrawlerJobAudit) {

		logger.info("Web Crawler Service for IFW download code Started ");

		WebDriver webDriver = null;

		List<String> jurisdictionCodes = new ArrayList<String>();
		
		jurisdictionCodes.add(BBWebCrawerConstants.US_CODE);
		jurisdictionCodes.add(BBWebCrawerConstants.WO_CODE);

		List<DownloadOfficeActionQueue> downloadOfficeActionData = downloadOfficeActionQueueRepository
				.findByDownloadQueueStatusByJursidictionCodes(jurisdictionCodes);
		boolean isInclusionListOn = StringUtils.equalsIgnoreCase(Constant.ON,BlackboxPropertyUtil.getProperty(Constant.INCLUSION_LIST_ON_FLAG));
		List<String> inclusionList = inclusionListRepository.findAllActiveApplicatinNumbers();
		List<String> exclusionList = exclusionListRepository.findActiveApplicationNumbers();
		WebCrawlerJobStatus webCrawlerJobStatus = webCrawlerJobStatusRepository
				.findByJobName(WebCrawlerJobEnum.IFW_DOC_DOWNLOAD_JOB.getJobName());
		List<String> customerNumbers = customerRepository.findAllCustomersByNumber();
		
		
		checkCustomerNumbers(downloadOfficeActionData, webCrawlerJobStatus, customerNumbers);
		prepareDownloadQueueAfterInclExcl(downloadOfficeActionData, isInclusionListOn, inclusionList, exclusionList);

		if (CollectionUtils.isNotEmpty(downloadOfficeActionData)) {
			
				List<String> jurisdictionCodes1 = new ArrayList<String>();
				jurisdictionCodes1.add(BBWebCrawerConstants.US_CODE);
				jurisdictionCodes1.add(BBWebCrawerConstants.WO_CODE);

				List<DocumentCode> documentCodes = documentCodeRepository.findDocumentByCountryCodes(jurisdictionCodes1);
				List<Long> ids = customerRepository.findAllCustomers(); 
				List<Object[]> customerToApplicationNumberInBBX = customerRepository
						.findAllApplicationNumbersForGivenCustomers(ids);
				
				Map<String,List<DownloadOfficeActionQueue>> customerNumberToDownloadQueues = new HashMap<String,List<DownloadOfficeActionQueue>>();
				Map<String, Map<String, Set<String>>> appNumberToMailingDateAndDocMap = new HashMap<String, Map<String, Set<String>>>();
				HashMap<String, Jurisdiction> jurisdictionMap = new HashMap<String, Jurisdiction>();
				Map<String, String> descriptionToCodeMap = new HashMap<String, String>();
				Long crawlerId = webCrawlerJobAudit.getId();
				Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity = new HashMap<String, DownloadOfficeActionQueue>();
				Map<String, List<String>> customerToAppNumbersInBBXMap = new HashMap<String, List<String>>();
				
				CorrespondenceCrawlerDTO correspondenceCrawlerDTO = new CorrespondenceCrawlerDTO(customerNumberToDownloadQueues,jurisdictionMap,
						descriptionToCodeMap,customerToAppNumbersInBBXMap,appNumberToMailingDateAndDocMap);
				correspondenceCrawlerDTO.setDocumentCodes(documentCodes);
				
				correspondenceCrawlerDTO.setMaxRecordRetryCount(webCrawlerJobStatus.getMaxRecordRetryCount());
			
				List<String> customerNumbersHavingDownloadQueueData = prepareCompleteMasterData(
						downloadOfficeActionData, documentCodes,
						customerToApplicationNumberInBBX, correspondenceCrawlerDTO);

				List<AuthenticationData> authenticationDatas = authenticationDataRepository.findAll();
				
				if(CollectionUtils.isNotEmpty(authenticationDatas)) {
					
					checkNProcessAllEPFs(webCrawlerJobAudit, webDriver,
							customerNumbersHavingDownloadQueueData,
							crawlerId, pdfPathToDownloadQueueEntity,
							authenticationDatas,correspondenceCrawlerDTO);
				} else {
					logger.info("No EPF File Configuration data has been configured");
				}
				logger.info(MessageFormat.format(
						"IFW Download Doc Code with crawler id {0} completed succesfully",
						webCrawlerJobAudit.getId()));
		}

	}

	/**
	 * Check customer numbers.
	 *
	 * @param downloadOfficeActionData the download office action data
	 * @param webCrawlerJobStatus the web crawler job status
	 * @param customerNumbers the customer numbers
	 */
	private void checkCustomerNumbers(List<DownloadOfficeActionQueue> downloadOfficeActionData,
			WebCrawlerJobStatus webCrawlerJobStatus, List<String> customerNumbers) {
		if(CollectionUtils.isNotEmpty(downloadOfficeActionData)) {
			
			for(DownloadOfficeActionQueue downloadOfficeActionQueue : downloadOfficeActionData) {
				if(!customerNumbers.contains(downloadOfficeActionQueue.getCustomerNumber())) {
					int currentRetryCount = downloadOfficeActionQueue.getRetryCount();
					if (currentRetryCount == webCrawlerJobStatus.getMaxRecordRetryCount()) {
						handleMaxRetryCountReachedCase(downloadOfficeActionQueue);
					} else if (currentRetryCount < 3) {
						downloadOfficeActionQueue.setStatus(QueueStatus.ERROR);
						handleNonMaxTriesReachedCase(downloadOfficeActionQueue);
					}
				}
			}
		}
	}

	/**
	 * Prepare download queue after incl excl.
	 *
	 * @param downloadOfficeActionData the download office action data
	 * @param isInclusionListOn the is inclusion list on
	 * @param inclusionList the inclusion list
	 * @param exclusionList the exclusion list
	 */
	private void prepareDownloadQueueAfterInclExcl(List<DownloadOfficeActionQueue> downloadOfficeActionData,
			boolean isInclusionListOn, List<String> inclusionList, List<String> exclusionList) {
		if(isInclusionListOn) {
			processInclusionList(downloadOfficeActionData, inclusionList);
		} else {
			processExclusionList(downloadOfficeActionData, exclusionList);
		
		}
	}

	/**
	 * Process exclusion list.
	 *
	 * @param downloadOfficeActionData the download office action data
	 * @param exclusionList the exclusion list
	 * @param maxRetryCount 
	 */
	private void processExclusionList(List<DownloadOfficeActionQueue> downloadOfficeActionData,
			List<String> exclusionList) {
		synchronized (this) {
			if (CollectionUtils.isNotEmpty(downloadOfficeActionData)) {
				Iterator<DownloadOfficeActionQueue> ite = downloadOfficeActionData.iterator();
				while (ite.hasNext()) {
					DownloadOfficeActionQueue downloadOfficeActionQueue = ite.next();
					String applicationNumber = downloadOfficeActionQueue.getApplicationNumberFormatted();

					if (exclusionList.contains(applicationNumber)) {
						ite.remove();
					}
				}
			}
		}
	}

	/**
	 * Process inclusion list.
	 *
	 * @param downloadOfficeActionData the download office action data
	 * @param inclusionList the inclusion list
	 */
	private void processInclusionList(List<DownloadOfficeActionQueue> downloadOfficeActionData,
			List<String> inclusionList) {
		synchronized (this) {
			if (CollectionUtils.isNotEmpty(downloadOfficeActionData)) {
				Iterator<DownloadOfficeActionQueue> ite = downloadOfficeActionData.iterator();
				while (ite.hasNext()) {
					DownloadOfficeActionQueue downloadOfficeActionQueue = ite.next();
					String applicationNumber = downloadOfficeActionQueue.getApplicationNumberFormatted();

					if (!inclusionList.contains(applicationNumber)) {
						ite.remove();
					}
				}
			}
		}
	}

	/**
	 * Prepare complete master data.
	 *
	 * @param downloadOfficeActionData the download office action data
	 * @param documentCodes the document codes
	 * @param customerToApplicationNumberInBBX the customer to application number in bbx
	 * @param correspondenceCrawlerDTO the correspondence crawler dto
	 * @return the list
	 */
	private List<String> prepareCompleteMasterData(List<DownloadOfficeActionQueue> downloadOfficeActionData,
			List<DocumentCode> documentCodes, List<Object[]> customerToApplicationNumberInBBX, CorrespondenceCrawlerDTO correspondenceCrawlerDTO) {
		
		Map<String, List<DownloadOfficeActionQueue>> customerNumberToDownloadQueues = correspondenceCrawlerDTO.getCustomerNumberToDownloadQueues();
		Map<String, Jurisdiction> jurisdictionMap = correspondenceCrawlerDTO.getJurisdictionMap();
		Map<String, String> descriptionToCodeMap = correspondenceCrawlerDTO.getDescriptionToCodeMap();
		Map<String, List<String>> customerToAppNumbersInBBXMap = correspondenceCrawlerDTO.getCustomerToAppNumbersInBBXMap();
		
		prepareCustomerNumberToQueuesMap(downloadOfficeActionData, customerNumberToDownloadQueues);
		List<String> customerNumbersHavingDownloadQueueData = new ArrayList<String>(customerNumberToDownloadQueues.keySet());

		prepareBBXCustomerNoToAppNosMap(customerToAppNumbersInBBXMap, customerToApplicationNumberInBBX);
		prepareDocumentAndJursidictionMap(documentCodes, jurisdictionMap,
				descriptionToCodeMap);
		return customerNumbersHavingDownloadQueueData;
	}

	/**
	 * Check n process all ep fs.
	 *
	 * @param webCrawlerJobAudit the web crawler job audit
	 * @param webDriver the web driver
	 * @param customerNumbersHavingDownloadQueueData the customer numbers having download queue data
	 * @param crawlerId the crawler id
	 * @param pdfPathToDownloadQueueEntity the pdf path to download queue entity
	 * @param authenticationDatas the authentication datas
	 * @param correspondenceCrawlerDTO the correspondence crawler dto
	 */
	private void checkNProcessAllEPFs(WebCrawlerJobAudit webCrawlerJobAudit, WebDriver webDriver,
			List<String> customerNumbersHavingDownloadQueueData,
			Long crawlerId, Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity,
			List<AuthenticationData> authenticationDatas,
			CorrespondenceCrawlerDTO correspondenceCrawlerDTO) {
		
		int maxRecordCount = correspondenceCrawlerDTO.getMaxRecordRetryCount();
		Map<String, List<DownloadOfficeActionQueue>> customerNumberToDownloadQueues = correspondenceCrawlerDTO.getCustomerNumberToDownloadQueues();
		Map<String, String> descriptionToCodeMap = correspondenceCrawlerDTO.getDescriptionToCodeMap();
		
		for (AuthenticationData authenticationData : authenticationDatas) {
			Long authenticationId = authenticationData.getId();
			List<DownloadOfficeActionQueue> actionQueuesForAllEligibleCustomersInEPF = null;
			try {
			List<String> customerNumbersInEPF = authenticationDataRepository.findCustomerNumbersFromAuthenticationId(authenticationData.getId());
			
			if (CollectionUtils.isNotEmpty(customerNumbersInEPF)) {
				logger.info(MessageFormat.format("Processing EPF id : [{0}] , {1}", authenticationId,authenticationData.getAuthenticationFileName()));

				
				 boolean isAnyCustomerHavingQueueEntry = false;
				isAnyCustomerHavingQueueEntry = isAnyCustomerInQueueForAnEPF(customerNumbersHavingDownloadQueueData,
						customerNumbersInEPF);
				
				if (isAnyCustomerHavingQueueEntry) {

					actionQueuesForAllEligibleCustomersInEPF = new ArrayList<DownloadOfficeActionQueue>();
					
					for(String customerNumberInEPF : customerNumbersInEPF) {
						if(customerNumberToDownloadQueues.containsKey(customerNumberInEPF)) {
							actionQueuesForAllEligibleCustomersInEPF.addAll(customerNumberToDownloadQueues.get(customerNumberInEPF));
						}
					}
					
					validateAndScrapApplicationNumbers(crawlerId, pdfPathToDownloadQueueEntity,
							authenticationData,actionQueuesForAllEligibleCustomersInEPF,correspondenceCrawlerDTO);

					logger.info(MessageFormat.format(
							"Web Scrapping for all download action entries for EPF Id {0} completed . About to split PDF and prepare staging data",
							authenticationData.getId()));

					// PDF splitting and preparing staging data.
					prepareStagingDataForEligibleCustomersInEPF(webCrawlerJobAudit, descriptionToCodeMap,
							pdfPathToDownloadQueueEntity, authenticationData);

				} else {
					logger.info(MessageFormat.format("Not trying to Log in USPTO for EPF Id {0} , as there are no customer numbers in EPF which have atleast 1 Download queue data for it."
							,authenticationData.getId()));
				}

			} else {
				logger.info("No Customer numbers configured in system for Authentication Id "
						+ authenticationId);
			}
		} catch(AuthenticationFailureException afex) {
			String error = MessageFormat.format("Some exception occured while crawling for job id {0} for epf file id {1}. Exception is {2}", 
					webCrawlerJobAudit.getId(),afex,authenticationId);
			logger.info(error);
			updateAllQueueDataForEPF(maxRecordCount, actionQueuesForAllEligibleCustomersInEPF);
		} 
		} 
	}

	/**
	 * Update all queue data for epf.
	 *
	 * @param maxRecordCount the max record count
	 * @param actionQueuesForAllEligibleCustomersInEPF the action queues for all eligible customers in epf
	 */
	private void updateAllQueueDataForEPF(int maxRecordCount,
			List<DownloadOfficeActionQueue> actionQueuesForAllEligibleCustomersInEPF) {
		if(CollectionUtils.isNotEmpty(actionQueuesForAllEligibleCustomersInEPF)) {
			for (DownloadOfficeActionQueue actionQueue : actionQueuesForAllEligibleCustomersInEPF) {
				int currentRetryCount = actionQueue.getRetryCount();
				if (currentRetryCount == maxRecordCount) {
					handleMaxRetryCountReachedCase(actionQueue);
				} else if (currentRetryCount < 3) {
					actionQueue.setStatus(QueueStatus.ERROR);
					handleNonMaxTriesReachedCase(actionQueue);
				}
			}
		}
	}

	/**
	 * Prepare staging data for eligible customers in epf.
	 *
	 * @param webCrawlerJobAudit the web crawler job audit
	 * @param descriptionToCodeMap the description to code map
	 * @param pdfPathToDownloadQueueEntity the pdf path to download queue entity
	 * @param authenticationData the authentication data
	 */
	private void prepareStagingDataForEligibleCustomersInEPF(WebCrawlerJobAudit webCrawlerJobAudit,
			Map<String, String> descriptionToCodeMap,
			Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity,
			AuthenticationData authenticationData) {
		if (MapUtils.isNotEmpty(pdfPathToDownloadQueueEntity)) {
			processPDFAndPrepareStagingData(webCrawlerJobAudit, descriptionToCodeMap,
					pdfPathToDownloadQueueEntity);
		} else {
			logger.info(MessageFormat.format(
					"No PDF's downloaded , hence no staging data to prepare for queue entries for EPF file Id {0}", authenticationData.getId()));
		}
	}

	/**
	 * Checks if is any customer in queue for an epf.
	 *
	 * @param customerNumbersHavingDownloadQueueData the customer numbers having download queue data
	 * @param customerNumbersInEPF the customer numbers in epf
	 * @return true, if is any customer in queue for an epf
	 */
	private boolean isAnyCustomerInQueueForAnEPF(List<String> customerNumbersHavingDownloadQueueData,
			List<String> customerNumbersInEPF) {
		boolean isAnyCustomerHavingQueueEntry = false;
		for(String customerInEPF : customerNumbersInEPF) {
			if(customerNumbersHavingDownloadQueueData.contains(customerInEPF)) {
				isAnyCustomerHavingQueueEntry = true;
				break;
			}
		}
		return isAnyCustomerHavingQueueEntry;
	}

	/**
	 * Prepare customer number to queues map.
	 *
	 * @param downloadOfficeActionData the download office action data
	 * @param customerNumberToDownloadQueues the customer number to download queues
	 */
	private void prepareCustomerNumberToQueuesMap(List<DownloadOfficeActionQueue> downloadOfficeActionData,
			Map<String, List<DownloadOfficeActionQueue>> customerNumberToDownloadQueues) {
		for(DownloadOfficeActionQueue downloadOfficeActionQueue : downloadOfficeActionData) {
			String customerNumber = downloadOfficeActionQueue.getCustomerNumber();
			if (customerNumberToDownloadQueues.containsKey(customerNumber)) {
				List<DownloadOfficeActionQueue> actionQueues = customerNumberToDownloadQueues.get(customerNumber);
				actionQueues.add(downloadOfficeActionQueue);
			} else {
				List<DownloadOfficeActionQueue> actionQueues = new ArrayList<DownloadOfficeActionQueue>();
				actionQueues.add(downloadOfficeActionQueue);
				customerNumberToDownloadQueues.put(customerNumber, actionQueues);
			}
		}
	}

	/**
	 * Prepare bbx customer no to app nos map.
	 *
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
	 * Process pdf and prepare staging data.
	 *
	 * @param webCrawlerJobAudit
	 *            the web crawler job audit
	 * @param descriptionToCodeMap
	 *            the description to code map
	 * @param pdfPathToDownloadQueueEntity
	 *            the pdf path to download queue entity
	 */
	private void processPDFAndPrepareStagingData(WebCrawlerJobAudit webCrawlerJobAudit,
			Map<String, String> descriptionToCodeMap,
			Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity) {
		List<CorrespondenceStaging> stagingDataPerMergedPDF;
		PDFSplittingAndStagingDataDTO pdfSplittingAndStagingDataDTO = null;

		for (String pdfPath : pdfPathToDownloadQueueEntity.keySet()) {
			logger.info(MessageFormat.format("About to split pdf {0}", pdfPath));

			stagingDataPerMergedPDF = new ArrayList<CorrespondenceStaging>();
			DownloadOfficeActionQueue actionQueue = pdfPathToDownloadQueueEntity.get(pdfPath);
			try {
				List<String> bookmarkDescriptions = PDFUtil.getBookmarksNames(pdfPath);
				
				String relativePath = BlackboxUtils.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath("downloads"));
				String targetFolder = BBWebCrawlerUtils.getCompleteFilePath(relativePath,
						webCrawlerJobAudit.getId(), properties.clientId);

				pdfSplittingAndStagingDataDTO = new PDFSplittingAndStagingDataDTO(pdfPath, stagingDataPerMergedPDF,
						targetFolder, bookmarkDescriptions, false, actionQueue, descriptionToCodeMap, null, null, null,
						null, null);

				dataPrepService.splitPDFAndPrepareStagingData(pdfSplittingAndStagingDataDTO);

				logger.info(MessageFormat.format("Saving Correspondence Staging Data for pdf path {0}",pdfPath));
				correspondenceStagingRepository.save(stagingDataPerMergedPDF);
				logger.info(MessageFormat.format("Correspondence Staging Data for pdf path {0} saved succesfully",pdfPath));
				actionQueue.setStatus(QueueStatus.SUCCESS);
				downloadOfficeActionQueueRepository.save(actionQueue);
				logger.info(MessageFormat.format("Status for Download office queue entry for pdf path {0} updated succesfully",pdfPath));

			} catch (Exception e) {
				logger.error(MessageFormat.format(
						"Some Exception occured while splitting PDF for download office id {0}. Exception is {1}",
						actionQueue.getId(), e));
				if (actionQueue.getRetryCount() == 3) {
					handleMaxRetryCountReachedCase(actionQueue);
				} else if (actionQueue.getRetryCount() < 3) {
					actionQueue.setStatus(QueueStatus.ERROR);
					handleNonMaxTriesReachedCase(actionQueue);
				}

			}
		}
	}

	/**
	 * Handle non max tries reached case.
	 *
	 * @param actionQueue
	 *            the action queue
	 */
	private void handleNonMaxTriesReachedCase(DownloadOfficeActionQueue actionQueue) {
		actionQueue.setRetryCount(actionQueue.getRetryCount() + 1);
		actionQueue.setUpdatedByUser(User.SYSTEM_ID);
		actionQueue.setUpdatedDate(Calendar.getInstance());
		logger.info("Updating the retry count and changing the status in database");
		try {
			downloadOfficeActionQueueRepository.save(actionQueue);
		} catch (DataAccessException dex) {
			logger.error(MessageFormat.format(
					"Some Exception occured while updating retry count and status for download office id {0}. Exception is {1}",
					actionQueue.getId(), dex));
		}
	}

	/**
	 * Handle max retry count reached case.
	 *
	 * @param actionQueue
	 *            the action queue
	 */
	private void handleMaxRetryCountReachedCase(DownloadOfficeActionQueue actionQueue) {
		actionQueue.setStatus(QueueStatus.CRAWLER_ERROR);
		actionQueue.setRetryCount(actionQueue.getRetryCount() + 1);
		actionQueue.setUpdatedByUser(User.SYSTEM_ID);
		actionQueue.setUpdatedDate(Calendar.getInstance());
		logger.info("Updating the retry count in database and sending the notification.");
		try {

			ApplicationBase applicationBase = applicationBaseDAO.findByApplicationNoAndJurisdictionCode(
					actionQueue.getJurisdictionCode(), actionQueue.getApplicationNumberFormatted());

			logger.info(MessageFormat.format(
					"Application Entity id [{0}] fetched for sending notification corresponing to erroneous download queue entry is : ",
					applicationBase == null ? null : applicationBase.getId()));

			if(applicationBase != null) {
			Long notificationProcessId = notificationProcessService.createNotification(applicationBase, null, null, 
					actionQueue.getId(), EntityName.DOWNLOAD_OFFICE_ACTION_QUEUE, 
					NotificationProcessType.CORRESPONDENCE_RECORD_FAILED_IN_DOWNLOAD_QUEUE, null, null);
			
			logger.info(MessageFormat
					.format("Notification created for Download Queue request [notification process id : {0}, queue id : {1},  Application Number : {2}, Jurisdiction : {3}].",
							notificationProcessId, actionQueue.getId(), actionQueue.getApplicationNumberFormatted(),
							actionQueue.getJurisdictionCode()));
			}
			downloadOfficeActionQueueRepository.save(actionQueue);
			logger.info("Retry Count and Status succesfully updated for Download office queue id "+actionQueue.getId());
			
		} catch (Exception ex) {
			logger.error(MessageFormat
					.format("Some Exception occured while updating retry count as 4 and status as CRAWLER_ERROR or sending the notification"
							+ " for download office id {0}." + " Unable to send the notification/persist in DB."
							+ "Exception is {1}", actionQueue.getId(), ex));
		}
	}

	/**
	 * Validate and scrap application numbers.
	 * @param jobId            the job id
	 * @param pdfPathToDownloadQueueEntity            - Map used to track the entity corresponding to path. This will be used for exception handling in the
	 *            caller methods.
	 * @param authenticationData the authentication data
	 * @param actionQueuesForAllEligibleCustomersInEPF the action queues for all eligible customers in epf
	 * @param correspondenceCrawlerDTO the correspondence crawler dto
	 *
	 * @return the web driver
	 */
	private void validateAndScrapApplicationNumbers(Long jobId, Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity,
			AuthenticationData authenticationData,
			List<DownloadOfficeActionQueue> actionQueuesForAllEligibleCustomersInEPF,
			CorrespondenceCrawlerDTO correspondenceCrawlerDTO) {
		
			int maxRecordCount = correspondenceCrawlerDTO.getMaxRecordRetryCount();
			
			String absolutePath = BlackboxUtils.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath(BBWebCrawerConstants.DOWNLOADS_FOLDER));
			
			File absolutePathFile = new File(absolutePath);
			if(!absolutePathFile.exists()) {
				absolutePathFile.mkdirs();
			}
			String clientId = properties.clientId;
			String downloadDir = BBWebCrawlerUtils.getCompleteFilePath(absolutePath, jobId, clientId);
			WebDriver webDriver = null;
			
			try {
			webDriver = BBWebCrawlerUtils.getWebDriver(downloadDir);

			String completePath = BlackboxUtils.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath(BBWebCrawerConstants.AUTHENTICATION_FOLDER),
					authenticationData.getAuthenticationFileName(),File.separator);
			
			loginService.login(webDriver, completePath, authenticationData.getPassword());

			processAllQueueEntries(webDriver, jobId, pdfPathToDownloadQueueEntity,
					actionQueuesForAllEligibleCustomersInEPF, maxRecordCount,correspondenceCrawlerDTO.getDocumentCodes());
			} finally {
				BBWebCrawlerUtils.logoutAndCloseWebDriver(webDriver, logger);
			}
	}

	/**
	 * Process all queue entries.
	 *
	 * @param webDriver the web driver
	 * @param jobId the job id
	 * @param pdfPathToDownloadQueueEntity the pdf path to download queue entity
	 * @param actionQueuesForAllEligibleCustomersInEPF the action queues for all eligible customers in epf
	 * @param maxRecordCount the max record count
	 * @param documentCodes 
	 */
	private void processAllQueueEntries(WebDriver webDriver, Long jobId,
			Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity,
			List<DownloadOfficeActionQueue> actionQueuesForAllEligibleCustomersInEPF, int maxRecordCount, List<DocumentCode> documentCodes) {
		if(CollectionUtils.isNotEmpty(actionQueuesForAllEligibleCustomersInEPF)) {
			
		logger.info(MessageFormat.format("Count of Download Queue Entries [{0}]", actionQueuesForAllEligibleCustomersInEPF.size()));
		
		for (DownloadOfficeActionQueue actionQueue : actionQueuesForAllEligibleCustomersInEPF) {
			logger.info(MessageFormat.format("Processing Download Queue Id [{0}] with application number [{1}]", actionQueue.getId(),actionQueue.getApplicationNumberFormatted()));
				processEachQueueEntry(webDriver, jobId, pdfPathToDownloadQueueEntity,
						actionQueue,maxRecordCount,documentCodes);
				logger.info(MessageFormat.format("Download Queue Id [{0}] with application number [{1}] processed succesfully",
						actionQueue.getId(),actionQueue.getApplicationNumberFormatted()));
		}
		} else {
			logger.info("No Download Action Queue Entries found..");
		}
	}

	/**
	 * Process each queue entry.
	 *
	 * @param webDriver            the web driver
	 * @param jobId            the job id
	 * @param pdfPathToDownloadQueueEntity            the pdf path to download queue entity
	 * @param actionQueue            the action queue
	 * @param maxRecordCount the max record count
	 * @param documentCodes 
	 */
	private void processEachQueueEntry(WebDriver webDriver, Long jobId,
			Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity,
			DownloadOfficeActionQueue actionQueue, int maxRecordCount, List<DocumentCode> documentCodes)  {
		try {
			int retryCount = actionQueue.getRetryCount();
			if (retryCount < (maxRecordCount + 1)) {
				processRecordsNotReachedMaxRetry(webDriver, jobId, actionQueue,
						pdfPathToDownloadQueueEntity,maxRecordCount,documentCodes);
			}
		} catch (Exception ex) {
			logger.error(MessageFormat.format(
					"Exception occured while processing the download office action id {0}. Exception is {1}",
					actionQueue.getId(), ex));
			WebElement element = webDriver.findElement(By.xpath(properties.selectNewCaseXPath));
			element.click();
			int currentRetryCount = actionQueue.getRetryCount();
			if (currentRetryCount == maxRecordCount) {
				handleMaxRetryCountReachedCase(actionQueue);
			} else if (currentRetryCount < 3) {
				actionQueue.setStatus(QueueStatus.ERROR);
				handleNonMaxTriesReachedCase(actionQueue);
			}
		}
	}
	/**
	 * Process records not reaching max retry.
	 *
	 * @param webDriver            the web driver
	 * @param jobId            the job id
	 * @param actionQueue            the action queue
	 * @param pdfPathToDownloadQueueEntity            the pdf path to download queue entity
	 * @param maxRecordCount the max record count
	 * @param documentCodes 
	 * @throws InterruptedException             the interrupted exception
	 */
	private void processRecordsNotReachedMaxRetry(WebDriver webDriver, Long jobId,
			DownloadOfficeActionQueue actionQueue, Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity, int maxRecordCount, List<DocumentCode> documentCodes)
					throws InterruptedException {
		boolean isValidApplicationNumber = iifwDownloadDoCScrapper.checkAndScrapApplicationNumberDetails(webDriver,
				actionQueue, jobId, pdfPathToDownloadQueueEntity, documentCodes, maxRecordCount);

		if (!isValidApplicationNumber) {
			logger.info(MessageFormat.format("Download Action Queue Id {0} has an invalid application Number {1}. "
					+ "Updating the retry count and saving the status in DB.", actionQueue.getId(),actionQueue.getApplicationNumberFormatted()));
			int currentRetryCount = actionQueue.getRetryCount();
			if (currentRetryCount == maxRecordCount) {
				handleMaxRetryCountReachedCase(actionQueue);
			} else if (currentRetryCount < 3) {
				actionQueue.setStatus(QueueStatus.ERROR);
				handleNonMaxTriesReachedCase(actionQueue);
			}
		}

	}

	/**
	 * Prepare document and jursidiction map.
	 *
	 * @param documentCodes
	 *            the document codes
	 * @param jurisdictionMap
	 *            the jurisdiction map
	 * @param descriptionToCodeMap
	 *            the description to code map
	 */
	private void prepareDocumentAndJursidictionMap(List<DocumentCode> documentCodes,
			Map<String, Jurisdiction> jurisdictionMap,
			Map<String, String> descriptionToCodeMap) {
		for (DocumentCode code : documentCodes) {
			descriptionToCodeMap.put(code.getDescription(), code.getCode());
		}

		List<Jurisdiction> jurisdictions = jurisdictionRepository.findAll();
		if (CollectionUtils.isNotEmpty(jurisdictions)) {
			for (Jurisdiction jurisdiction : jurisdictions) {
				jurisdictionMap.put(jurisdiction.getCode(), jurisdiction);
			}

		}
	}
}
