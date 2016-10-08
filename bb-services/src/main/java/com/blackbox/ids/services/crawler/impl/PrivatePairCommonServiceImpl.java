package com.blackbox.ids.services.crawler.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.exceptions.XMLException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationCorrespondence;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationCorrespondenceData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.DocumentData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.DocumentList;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ErrorResponse;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.OutgoingCorrespondence;
import com.blackbox.ids.core.dto.webcrawler.PrivatePairAuditDTO;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.correspondence.PairAudit;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.enums.PairAuditStatusEnum;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.repository.CustomerRepository;
import com.blackbox.ids.core.repository.DocumentCodeRepository;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceBaseRepository;
import com.blackbox.ids.core.repository.correspondence.PairAuditRepository;
import com.blackbox.ids.core.repository.webcrawler.DownloadOfficeActionQueueRepository;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.XmlParser;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.exception.XMLOrPDFCorruptedException;
import com.blackbox.ids.services.common.EmailService;
import com.blackbox.ids.services.crawler.IPrivatePairCommonService;
import com.blackbox.ids.services.validation.NumberFormatValidationService;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

@Service
public class PrivatePairCommonServiceImpl implements IPrivatePairCommonService{

	/** The logger. */
	private static Logger logger = Logger.getLogger(PrivatePairCommonServiceImpl.class);

	/** The document code repository. */
	@Autowired
	private DocumentCodeRepository documentCodeRepository;

	/** The customer repository. */
	@Autowired
	private CustomerRepository customerRepository;

	/** The correspondence base repository. */
	@Autowired
	private CorrespondenceBaseRepository correspondenceBaseRepository;

	/** The download office action queue repository. */
	@Autowired
	private DownloadOfficeActionQueueRepository downloadOfficeActionQueueRepository;

	/** The pair audit repository. */
	@Autowired
	private PairAuditRepository pairAuditRepository;
	
	/** The number format validation service. */
	@Autowired
	private NumberFormatValidationService numberFormatValidationService;
	
	/** The user repository impl. */
	@Autowired
	private UserRepository userRepositoryImpl;
	
	/** The email service. */
	@Autowired
	private EmailService emailService;

	@Override
	public boolean processAndValidateXMLRecords(File lastDownloadedFile, Long userId,String fileName)
			throws FileNotFoundException {
		OutgoingCorrespondence outgoingCorrespondence = unMarshalXMLAndCheckForCorruption(lastDownloadedFile);
		boolean isAnyRecordInXML = false;

		if(outgoingCorrespondence != null) {
			List<String> unMatchedXMLKeys = new ArrayList<String>();
			Map<String, DocumentCode> documentCodeMap = new HashMap<String, DocumentCode>();
			Map<String,List<String>> customerToAppNumbersInBBXMap = new HashMap<String,List<String>>();
			Map<String,CorrespondenceBase> correspondenceTableKeyToObjectMap = new HashMap<String,CorrespondenceBase>();
			Map<String,DownloadOfficeActionQueue> downloadQueueKeyToEntityMap = new HashMap<String,DownloadOfficeActionQueue>();
			List<PairAudit> pairAuditRecords = new ArrayList<PairAudit>();
			List<DownloadOfficeActionQueue> newDownloadQueueRecords = new ArrayList<DownloadOfficeActionQueue>();
			
			prepareMasterMaps(documentCodeMap, customerToAppNumbersInBBXMap,
					correspondenceTableKeyToObjectMap,downloadQueueKeyToEntityMap);

			PrivatePairAuditDTO privatePairAuditDTO = new PrivatePairAuditDTO(pairAuditRecords,newDownloadQueueRecords,unMatchedXMLKeys,userId,downloadQueueKeyToEntityMap);
			
			List<String> customerNumbersInBBX = new ArrayList<String>(customerToAppNumbersInBBXMap.keySet());

			List<String> documentCodeStrList= new ArrayList<String>(documentCodeMap.keySet());

			ApplicationCorrespondence applicationCorrespondence = outgoingCorrespondence
					.getApplicationCorrespondence();
			final List<ApplicationCorrespondenceData> applicationCorrespondenceDatas = applicationCorrespondence != null
					? applicationCorrespondence.getApplicationCorrespondenceData() : null;

					if (CollectionUtils.isNotEmpty(applicationCorrespondenceDatas)) {
						isAnyRecordInXML = true;
						iterateXMLAndProcess(customerToAppNumbersInBBXMap, correspondenceTableKeyToObjectMap,
								customerNumbersInBBX, documentCodeStrList,
								applicationCorrespondenceDatas,privatePairAuditDTO);
					}

					checkAndSavePrivatePairRecords(pairAuditRecords);
					checkAndSaveDownloadQueueRecords(newDownloadQueueRecords);
					sendSuccessReportMail(unMatchedXMLKeys, userId, fileName);

		}

		return isAnyRecordInXML;

	}

	/**
	 * Check and save private pair records.
	 *
	 * @param pairAuditRecords the pair audit records
	 */
	private void checkAndSavePrivatePairRecords(List<PairAudit> pairAuditRecords) {
		if (CollectionUtils.isNotEmpty(pairAuditRecords)) {
			logger.info(MessageFormat.format("Persisting private pair audit records in database. Count is [{0}]",
					pairAuditRecords.size()));
			pairAuditRepository.save(pairAuditRecords);
			logger.info(MessageFormat.format("Persisted private pair audit records in database. Count is [{0}]",
					pairAuditRecords.size()));
		} else {
			logger.info("No Private pair audit records to persist in database.");
		}
	}

	/**
	 * Check and save download queue records.
	 *
	 * @param newDownloadQueueRecords the new download queue records
	 */
	private void checkAndSaveDownloadQueueRecords(List<DownloadOfficeActionQueue> newDownloadQueueRecords) {
		if (CollectionUtils.isNotEmpty(newDownloadQueueRecords)) {
			logger.info(
					MessageFormat.format("Persisting download office action records in database. Count is [{0}]",
							newDownloadQueueRecords.size()));
			downloadOfficeActionQueueRepository.save(newDownloadQueueRecords);
			logger.info(MessageFormat.format("Persisted download office action records in database. Count is [{0}]",
					newDownloadQueueRecords.size()));
		} else {
			logger.info("No download office action records to persist in database.");
		}
	}

	/**
	 * Prepare master maps.
	 *
	 * @param documentCodeMap the document code map
	 * @param customerToAppNumbersInBBXMap the customer to app numbers in bbx map
	 * @param correspondenceTableKeyToObjectMap the correspondence table key to object map
	 * @param downloadQueueKeyToEntityMap the download queue key to entity map
	 */
	private void prepareMasterMaps(Map<String, DocumentCode> documentCodeMap,
			Map<String, List<String>> customerToAppNumbersInBBXMap,
			Map<String, CorrespondenceBase> correspondenceTableKeyToObjectMap, Map<String, DownloadOfficeActionQueue> downloadQueueKeyToEntityMap) {

		List<Long> ids = customerRepository.findAllCustomers();
		List<Object[]> customerToApplicationNumberInBBX = customerRepository.findAllApplicationNumbersForGivenCustomers(ids);
		List<Object[]> correspondencesForActiveMDMRecords = correspondenceBaseRepository.findAllCorrespondencesForActiveMDMApplications();

		List<String> countryCodes = new ArrayList<String>();
		countryCodes.add(Constant.US_CODE);

		logger.info("Fetching document codes for US jurisdiction");
		List<DocumentCode> documentCodes = documentCodeRepository.findDocumentByCountryCodes(countryCodes);
		List<DownloadOfficeActionQueue> downloadOfficeActionQueues = downloadOfficeActionQueueRepository.findAll();

		prepareBBXCustomerNoToAppNosMap(customerToAppNumbersInBBXMap, customerToApplicationNumberInBBX);
		prepareDocumentMap(documentCodes, documentCodeMap);
		prepareCorrespondenceTableKeyMap(correspondenceTableKeyToObjectMap,correspondencesForActiveMDMRecords);
		prepareDownloadQueueMap(downloadQueueKeyToEntityMap,downloadOfficeActionQueues);
	}

	/**
	 * Prepare download queue map.
	 *
	 * @param downloadQueueKeyToEntityMap the download queue key to entity map
	 * @param downloadOfficeActionQueues
	 */
	private void prepareDownloadQueueMap(Map<String, DownloadOfficeActionQueue> downloadQueueKeyToEntityMap,
			List<DownloadOfficeActionQueue> downloadOfficeActionQueues) {

		if(CollectionUtils.isNotEmpty(downloadOfficeActionQueues)) {

			for(DownloadOfficeActionQueue downloadOfficeActionQueue : downloadOfficeActionQueues) {
				String applicationNumber = downloadOfficeActionQueue.getApplicationNumberFormatted();

				Integer pageCount = downloadOfficeActionQueue.getPageCount();
				String recordKey = BlackboxUtils.concat(applicationNumber,
						Constant.SPACE_STRING, downloadOfficeActionQueue.getMailingDate() == null ?
								Constant.ALL_MAILING_DATE : BlackboxDateUtil.calendarToStr(downloadOfficeActionQueue.getMailingDate(),TimestampFormat.yyyyMMdd)
								, Constant.SPACE_STRING,
								downloadOfficeActionQueue.getDocumentCode() == null ? com.blackbox.ids.core.constant.Constant.ALL_DOCUMENT_CODE : downloadOfficeActionQueue.getDocumentCode()
										,Constant.SPACE_STRING,
										pageCount == null ? Constant.EMPTY_STRING : pageCount.toString());

				if(!downloadQueueKeyToEntityMap.containsKey(recordKey)) {
					downloadQueueKeyToEntityMap.put(recordKey, downloadOfficeActionQueue);
				}
			}
		}

	}

	/**
	 * Prepare correspondence table key map.
	 *
	 * @param correspondenceTableKeyToObjectMap the correspondence table key to object map
	 * @param correspondencesForActiveMDMRecords the correspondences for active mdm records
	 */
	private void prepareCorrespondenceTableKeyMap(
			Map<String, CorrespondenceBase> correspondenceTableKeyToObjectMap, List<Object[]> correspondencesForActiveMDMRecords) {

		if(CollectionUtils.isNotEmpty(correspondencesForActiveMDMRecords)) {

			for(Object[] result : correspondencesForActiveMDMRecords) {
				String applicationNumber = (String) result[0];
				String jurisdictionCode = (String) result[1];
				String documentCode = (String) result[2];
				CorrespondenceBase correspondenceBase = (CorrespondenceBase) result[3];
				correspondenceBase.setJurisdictionCode(jurisdictionCode);
				String mailingDate = BlackboxDateUtil.calendarToStr(correspondenceBase.getMailingDate(), TimestampFormat.yyyyMMdd);

				String recordKey = BlackboxUtils.concat(applicationNumber,
						Constant.SPACE_STRING, mailingDate, Constant.SPACE_STRING,
						documentCode,Constant.SPACE_STRING,
						correspondenceBase.getPageCount().toString());

				if(!correspondenceTableKeyToObjectMap.containsKey(recordKey)) {
					logger.debug(MessageFormat.format("Correspondence Base Table key is {0}",recordKey));
					correspondenceTableKeyToObjectMap.put(recordKey, correspondenceBase);
				}

			}

		}

	}

	/**
	 * Prepare bbx customer no to app nos map.
	 *
	 * @param customerToAppNumbersInBBXMap the customer to app numbers in bbx map
	 * @param customerToApplicationNumberInBBX the customer to application number in bbx
	 */
	private void prepareBBXCustomerNoToAppNosMap(Map<String, List<String>> customerToAppNumbersInBBXMap,
			List<Object[]> customerToApplicationNumberInBBX) {
		if(CollectionUtils.isNotEmpty(customerToApplicationNumberInBBX)) {
			for(Object[] object : customerToApplicationNumberInBBX) {
				String customerNumber = (String) object[0];
				String applicationNumber = (String) object[1];

				if(customerToAppNumbersInBBXMap.containsKey(customerNumber)) {
					List<String> applicationNumbers= customerToAppNumbersInBBXMap.get(customerNumber);
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
	 * Un marshal xml and check for corruption.
	 *
	 * @param lastDownloadedFile the last downloaded file
	 * @return the outgoing correspondence
	 * @throws FileNotFoundException the file not found exception
	 */
	private OutgoingCorrespondence unMarshalXMLAndCheckForCorruption(final File lastDownloadedFile) throws FileNotFoundException {
		OutgoingCorrespondence outgoingCorrespondence = null;
		try {
			outgoingCorrespondence = (OutgoingCorrespondence) XmlParser
					.fetchXmlData(lastDownloadedFile, OutgoingCorrespondence.class);
		} catch (final FileNotFoundException e) {
			String error = MessageFormat.format("The file could not be found at the specified location. Exception is ", e);
			logger.error(error);
			throw new XMLException("The file could not be found at the specified location", e);
		} catch(CannotResolveClassException cex) {
			String error = MessageFormat.format("Some exception occured while parsing the outgoing correspondence XML. Exception is {0}. "
					+ "Checking if no data XML has arrived or an error XML has come", cex);
			logger.error(error);
			try {
				ErrorResponse errorObject = (ErrorResponse) XmlParser
						.fetchXmlData(lastDownloadedFile, ErrorResponse.class);
				logger.info(MessageFormat.format("Content of Error XML is {0}", errorObject.getError().getErrorMessage()));
			} catch(CannotResolveClassException cex1) {
				String error1 = MessageFormat.format("Some exception occured while parsing the no data XML. Exception is {0}. ", cex);
				logger.error(error1);
				throw new XMLOrPDFCorruptedException(error1, cex1);
			}
		}

		return outgoingCorrespondence;
	}

	/**
	 * Iterate xml and process.
	 *
	 * @param customerToAppNumbersInBBXMap the customer to app numbers in bbx map
	 * @param correspondenceTableKeyToObjectMap the correspondence table key to object map
	 * @param customerNumbersInBBX the customer numbers in bbx
	 * @param documentCodeStrList the document code str list
	 * @param applicationCorrespondenceDatas the application correspondence datas
	 * @param toBeInsertedObjects
	 */
	private void iterateXMLAndProcess(Map<String, List<String>> customerToAppNumbersInBBXMap,
			Map<String, CorrespondenceBase> correspondenceTableKeyToObjectMap,
			List<String> customerNumbersInBBX, List<String> documentCodeStrList,
			final List<ApplicationCorrespondenceData> applicationCorrespondenceDatas, PrivatePairAuditDTO privatePairAuditDTO) {

		for (final ApplicationCorrespondenceData applicationCorrespondenceData : applicationCorrespondenceDatas) {

			String usPTOCustomerNumber = applicationCorrespondenceData.getCustomerNumber();
			String usPTOApplicationNumberFormatted = BlackboxUtils.getFormattedApplicationNumberFromXML(applicationCorrespondenceData);

			logger.info("XML Application Number : "+usPTOApplicationNumberFormatted);

			if (customerNumbersInBBX.contains(usPTOCustomerNumber)) {
				List<String> appNumberInMDM = customerToAppNumbersInBBXMap.get(usPTOCustomerNumber);

				if (appNumberInMDM.contains(usPTOApplicationNumberFormatted)) {

					final DocumentList documentList = applicationCorrespondenceData.getDocumentList();
					final List<DocumentData> documentDatas = documentList != null
							? documentList.getDocumentData() : null;

							if (CollectionUtils.isNotEmpty(documentDatas)) {

								validateXMLRecordsNPrepareAuditData(correspondenceTableKeyToObjectMap,
										documentCodeStrList,
										applicationCorrespondenceData, documentDatas,privatePairAuditDTO);
							}

				}

			}

		}
	}

	/**
	 * Validate xml records n prepare audit data.
	 *
	 * @param correspondenceTableKeyToObjectMap the correspondence table key to object map
	 * @param downloadQueueKeyToEntityMap the download queue key to entity map
	 * @param pairAuditRecords the pair audit records
	 * @param documentCodeStrList the document code str list
	 * @param applicationCorrespondenceData the application correspondence data
	 * @param documentDatas the document datas
	 * @param newDownloadQueueRecords
	 */
	private void validateXMLRecordsNPrepareAuditData(Map<String, CorrespondenceBase> correspondenceTableKeyToObjectMap,
			List<String> documentCodeStrList, final ApplicationCorrespondenceData applicationCorrespondenceData,
			final List<DocumentData> documentDatas,PrivatePairAuditDTO privatePairAuditDTO) {

		List<String> allKeys = new ArrayList<String>();
		for (DocumentData documentData : documentDatas) {
			String documentCode = documentData.getFileWrapperDocumentCode();

			if (documentCodeStrList.contains(documentCode)) {
				logger.debug("Customer Number {0} , Application Number {1} , Document code {2} of USPTO matches with BBX , "
						+ "about to compare xml key with corresponding base table");

				String usPTOApplicationNumberFormatted = BlackboxUtils.getFormattedApplicationNumberFromXML(applicationCorrespondenceData);
				String xmlKeyWithSpecificDocCodeNMailingDate = BlackboxUtils.concat(usPTOApplicationNumberFormatted,
						Constant.SPACE_STRING, documentData.getMailRoomDate(), Constant.SPACE_STRING,
						documentData.getFileWrapperDocumentCode(),Constant.SPACE_STRING,
						documentData.getPageQuantity());

				if (!correspondenceTableKeyToObjectMap.containsKey(xmlKeyWithSpecificDocCodeNMailingDate)) {
					logger.info(MessageFormat.format("Key {0} in XML is not in Correspondence Base Table. Looking into Download Queue",xmlKeyWithSpecificDocCodeNMailingDate));
					preparePossibleKeysNCheckQueue(applicationCorrespondenceData,allKeys, documentData, documentCode,
							usPTOApplicationNumberFormatted, xmlKeyWithSpecificDocCodeNMailingDate,privatePairAuditDTO);
				} else {
					logger.info(MessageFormat.format("Key {0} is present in XML as well as Correspondence Base Table",xmlKeyWithSpecificDocCodeNMailingDate));
				}
			}
		}
	}

	/**
	 * Prepare possible keys n check queue.
	 * @param downloadQueueKeyToEntityMap
	 *            the download queue key to entity map
	 * @param applicationCorrespondenceData
	 *            the application correspondence data
	 * @param allKeys
	 *            the all keys
	 * @param documentData
	 *            the document data
	 * @param documentCode
	 *            the document code
	 * @param usPTOApplicationNumberFormatted
	 *            the us pto application number formatted
	 * @param xmlKeyWithSpecificDocCodeNMailingDate
	 *            the xml key with specific doc code n mailing date
	 * @param privatePairAuditDTO 
	 * @param toBeInsertedObjects
	 * @param userId TODO
	 * @param pairAuditRecords
	 *            the pair audit records
	 * @param newDownloadQueueRecords
	 *            the new download queue records
	 */
	private void preparePossibleKeysNCheckQueue(final ApplicationCorrespondenceData applicationCorrespondenceData,
			List<String> allKeys, DocumentData documentData,
			String documentCode, String usPTOApplicationNumberFormatted, String xmlKeyWithSpecificDocCodeNMailingDate, PrivatePairAuditDTO privatePairAuditDTO) {
		
		String xmlKeyWithSpecificDocCodeNAllMailingDate = BlackboxUtils.concat(usPTOApplicationNumberFormatted,
				Constant.SPACE_STRING, Constant.ALL_MAILING_DATE,
				Constant.SPACE_STRING, documentData.getFileWrapperDocumentCode(),
				Constant.SPACE_STRING, documentData.getPageQuantity());

		String xmlKeyWithAllDocCodeNSpecificMailingDate = BlackboxUtils.concat(usPTOApplicationNumberFormatted,
				Constant.SPACE_STRING, documentData.getMailRoomDate(), Constant.SPACE_STRING,
				Constant.ALL_DOCUMENT_CODE, Constant.SPACE_STRING,
				documentData.getPageQuantity());

		String xmlKeyWithAllDocCodeNAllMailingDate = BlackboxUtils.concat(usPTOApplicationNumberFormatted,
				Constant.SPACE_STRING, Constant.ALL_MAILING_DATE,
				Constant.SPACE_STRING, Constant.ALL_DOCUMENT_CODE,
				Constant.SPACE_STRING, documentData.getPageQuantity());

		allKeys.add(xmlKeyWithSpecificDocCodeNMailingDate);
		allKeys.add(xmlKeyWithSpecificDocCodeNAllMailingDate);
		allKeys.add(xmlKeyWithAllDocCodeNSpecificMailingDate);
		allKeys.add(xmlKeyWithAllDocCodeNAllMailingDate);

		checkInDownloadQueue(applicationCorrespondenceData, documentData,
				documentCode, allKeys,privatePairAuditDTO);
	}


	/**
	 * Check in download queue.
	 *
	 * @param downloadQueueKeyToEntityMap the download queue key to entity map
	 * @param applicationCorrespondenceData the application correspondence data
	 * @param documentData the document data
	 * @param documentCode the document code
	 * @param allKeys
	 * @param privatePairAuditDTO 
	 */
	private void checkInDownloadQueue(final ApplicationCorrespondenceData applicationCorrespondenceData,
			DocumentData documentData, String documentCode, List<String> allKeys, PrivatePairAuditDTO privatePairAuditDTO) {
		
		List<PairAudit> pairAuditRecords = privatePairAuditDTO.getPairAuditRecords();
		List<DownloadOfficeActionQueue> newDownloadQueueRecords = privatePairAuditDTO.getNewDownloadQueueRecords();
		List<String> unMatchedXMLKeys = (List<String>) privatePairAuditDTO.getUnMatchedXMLKeys();
		Map<String, DownloadOfficeActionQueue> downloadQueueKeyToEntityMap = privatePairAuditDTO.getDownloadQueueKeyToEntityMap();
		Long userId = privatePairAuditDTO.getUserId();

		String xmlKeyWithSpecificDocCodeNMailingDate = allKeys.get(0);
		String xmlKeyWithSpecificDocCodeNAllMailingDate = allKeys.get(1);
		String xmlKeyWithAllDocCodeNSpecificMailingDate = allKeys.get(2);
		String xmlKeyWithAllDocCodeNAllMailingDate = allKeys.get(3);

		if(!downloadQueueKeyToEntityMap.containsKey(xmlKeyWithSpecificDocCodeNMailingDate) &&
				!downloadQueueKeyToEntityMap.containsKey(xmlKeyWithSpecificDocCodeNAllMailingDate) &&
				!downloadQueueKeyToEntityMap.containsKey(xmlKeyWithAllDocCodeNSpecificMailingDate) &&
				!downloadQueueKeyToEntityMap.containsKey(xmlKeyWithAllDocCodeNAllMailingDate)) {

			logger.debug(MessageFormat.format("Neither of the Keys {0} ,{1} ,{2} , {3} manipulated from XML is not in Download Office Queue. "
					+ "Preparing records to move to Pair Audit Table",
					xmlKeyWithSpecificDocCodeNMailingDate,xmlKeyWithSpecificDocCodeNAllMailingDate,xmlKeyWithAllDocCodeNSpecificMailingDate,xmlKeyWithAllDocCodeNAllMailingDate));

			PairAudit pairAudit = null;
			pairAudit = checkAndCreatePairAuditEntity(applicationCorrespondenceData, documentData, documentCode,
					userId);
			String usPTOApplicationNumberRaw = applicationCorrespondenceData.getApplicationNumber();
			boolean isWOApplication = 	usPTOApplicationNumberRaw.startsWith(Constant.WO_CODE_INDICATOR) ? true : false;
			String jurisdictionCode = isWOApplication
					? Constant.WO_CODE
							: Constant.US_CODE;
			Calendar filingDate = BlackboxDateUtil.strToCalendar(applicationCorrespondenceData.getFilingDate(),TimestampFormat.YYYYMMDD);
			String formattedApplicatonNumber = numberFormatValidationService.getConvertedValue(usPTOApplicationNumberRaw, NumberType.APPLICATION,
					jurisdictionCode, isWOApplication ? ApplicationType.PCT_US_SUBSEQUENT_FILING : ApplicationType.ALL, filingDate, null);

			DownloadOfficeActionQueue newDownloadQueueRecord = null;
			newDownloadQueueRecord = checkAndCreateDownloadOfficeActionEntity(applicationCorrespondenceData,
					documentData, userId, jurisdictionCode, filingDate, formattedApplicatonNumber,
					usPTOApplicationNumberRaw);
			unMatchedXMLKeys.add(xmlKeyWithSpecificDocCodeNMailingDate);
			pairAuditRecords.add(pairAudit);
			newDownloadQueueRecords.add(newDownloadQueueRecord);
		} else {
			logger.info(MessageFormat.format("XML Key {0} is contained in Download Queue. Hence not moving to Private Pair Audit", xmlKeyWithSpecificDocCodeNMailingDate));
			logger.debug(MessageFormat.format("Either of the Keys {0} ,{1} ,{2} , {3} manipulated from XML is present in Download Office Queue. "
					+ "Hence not moving to Private Pair Audit table.",
					xmlKeyWithSpecificDocCodeNMailingDate,xmlKeyWithSpecificDocCodeNAllMailingDate,xmlKeyWithAllDocCodeNSpecificMailingDate,xmlKeyWithAllDocCodeNAllMailingDate));
		}
	}

	/**
	 * Check and create download office action entity.
	 *
	 * @param applicationCorrespondenceData the application correspondence data
	 * @param documentData the document data
	 * @param userId the user id
	 * @param jurisdictionCode the jurisdiction code
	 * @param filingDate the filing date
	 * @param formattedApplicatonNumber the formatted applicaton number
	 * @param appNumberRaw the app number raw
	 * @return the download office action queue
	 */
	private DownloadOfficeActionQueue checkAndCreateDownloadOfficeActionEntity(
			final ApplicationCorrespondenceData applicationCorrespondenceData, DocumentData documentData, Long userId,
			String jurisdictionCode, Calendar filingDate, String formattedApplicatonNumber, String appNumberRaw) {
		DownloadOfficeActionQueue newDownloadQueueRecord;
			newDownloadQueueRecord = new DownloadOfficeActionQueue(appNumberRaw, formattedApplicatonNumber,
					applicationCorrespondenceData.getCustomerNumber(),
					jurisdictionCode, Calendar.getInstance(), userId,
					null, null, BlackboxDateUtil.strToCalendar(documentData.getMailRoomDate(),TimestampFormat.yyyyMMdd),
					documentData.getFileWrapperDocumentCode(), 1,
					QueueStatus.INITIATED, filingDate, Integer.valueOf(documentData.getPageQuantity()));
		
		return newDownloadQueueRecord;
	}

	/**
	 * Check and create pair audit entity.
	 *
	 * @param applicationCorrespondenceData the application correspondence data
	 * @param documentData the document data
	 * @param documentCode the document code
	 * @param userId the user id
	 * @return the pair audit
	 */
	private PairAudit checkAndCreatePairAuditEntity(final ApplicationCorrespondenceData applicationCorrespondenceData,
			DocumentData documentData, String documentCode, Long userId) {
		PairAudit pairAudit;
			pairAudit = new PairAudit(Calendar.getInstance(),userId,
					documentCode, applicationCorrespondenceData.getApplicationNumber(), BlackboxDateUtil.strToCalendar(documentData.getMailRoomDate(),
							TimestampFormat.yyyyMMdd), documentCode, Integer.valueOf(documentData.getPageQuantity()),
					PairAuditStatusEnum.NOT_FOUND);
		return pairAudit;
	}

	/**
	 * Prepare document and jursidiction map.
	 *
	 * @param documentCodes
	 *            the document codes
	 * @param documentCodeMap
	 *            the document code map
	 */
	private void prepareDocumentMap(List<DocumentCode> documentCodes,
			Map<String, DocumentCode> documentCodeMap) {
		for (DocumentCode code : documentCodes) {
			documentCodeMap.put(code.getCode(), code);
		}

	}
	
	/**
	 * It will send the Success Report Mail to the User.
	 * @param email
	 *            the emailId of the user
	 * @param pairAuditDifferences
	 *            the PAIR audit differences
	 * @param fileName
	 *            the file name
	 * @return noting
	 */
	private void sendSuccessReportMail(final List<String> pairAuditDifferences, final Long userId,final String fileName) {		
		String userEmailId = userRepositoryImpl.getEmailId(userId);		
		String userName = userRepositoryImpl.getUserFullName(userEmailId);
		if (userEmailId != null) {
			List<String> recipient = new ArrayList<String>();
			recipient.add(userEmailId);
			List<MessageData> messageDatas = createMessageDataForPairAuditDifferences(userName,pairAuditDifferences, fileName);
			logger.info(MessageFormat.format("MessageData {0} {1} {2} has been created to sent to user",userName,pairAuditDifferences, fileName));
			
			createAndSaveMessage(TemplateType.PAIR_AUDIT_REPORT, recipient, messageDatas);
		} else
			logger.info("Exception occured while sending the mail to user for Pair Audit Report");
	}
	

	private void createAndSaveMessage(final TemplateType pairAuditReport, final List<String> recipients,
			final List<MessageData> messageDatas) {
		Message message = null;
		if (pairAuditReport != null && !CollectionUtils.isEmpty(recipients)) {
			message = new Message();
			message.setTemplateType(pairAuditReport);
			StringBuffer strBuffer = new StringBuffer();
			for (String recipient : recipients) {
				strBuffer.append(recipient).append(Constant.SEMI_COLON);
			}
			message.setReceiverList(strBuffer.toString());
			message.setStatus(Constant.MESSAGE_STATUS_NOT_SENT);
			logger.info("Calling Email Service to sent the report to the user");
			emailService.send(message, messageDatas);
		}
	}
	
	/**
	 * Creates the message data for PAIR Audit Report.
	 * @param userName
	 *            the user name
	 * @param pairAuditDifferences
	 *            the PAIR audit differences
	 * @param fileName
	 *            the file name
	 * @return the list
	 */
	private List<MessageData> createMessageDataForPairAuditDifferences(final String userName,
			final List<String> pairAuditDifferences, final String fileName) {
		List<MessageData> messageDatas = null;
		if (userName != null) {
			messageDatas = new ArrayList<>();
			messageDatas = createMessageDataForAuditFile(userName, fileName);			
			if(!CollectionUtils.isEmpty(pairAuditDifferences)) {
				MessageData pairAuditDiff = null;
				for(String pairAuditDifference : pairAuditDifferences) {
					pairAuditDiff = new MessageData(com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_PAIR_AUDIT_DIFFERENCE,pairAuditDifference);
					messageDatas.add(pairAuditDiff);
				}
			}
			else {
				MessageData noDifference = new MessageData(com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_PAIR_AUDIT_DIFFERENCE,"Zero"); 
				messageDatas.add(noDifference);
			}
		}
		return messageDatas;
	}


	
	/**
	 * Creates the message data for Audit File.
	 * @param userName
	 *            the user name
	 * @param fileName
	 *            the file name
	 * @return the list
	 */
	private List<MessageData> createMessageDataForAuditFile(final String userName, final String fileName) {
		List<MessageData> messageDatas = null;
		if (userName != null) {
			messageDatas = new ArrayList<>();

			MessageData receiverData = new MessageData(
					com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_RECEIVER_NAME, userName);
			messageDatas.add(receiverData);

			MessageData fileNameData = new MessageData(
					com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_FILE_NAME, fileName);
			messageDatas.add(fileNameData);

		}
		return messageDatas;
	}
}
