package com.blackbox.ids.webcrawler.service.uspto.correspondence.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.ApplicationStageDAO;
import com.blackbox.ids.core.dao.correspondence.ICorrespondenceDAO;
import com.blackbox.ids.core.dao.webcrawler.CreateApplicationQueueDAO;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceBaseKeysDTO;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.correspondence.Correspondence;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.Correspondence.StepStatus;
import com.blackbox.ids.core.model.correspondence.Correspondence.StepType;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.webcrawler.CreateApplicationQueue;
import com.blackbox.ids.core.model.webcrawler.ValidateApplication;
import com.blackbox.ids.core.repository.ApplicationRepository;
import com.blackbox.ids.core.repository.DocumentCodeRepository;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceBaseRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceStagingRepository;
import com.blackbox.ids.core.repository.webcrawler.CreateApplicationQueueRepository;
import com.blackbox.ids.core.repository.webcrawler.ValidateApplicationRepository;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.services.validation.NumberFormatValidationService;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.ICorrespondenceStagingImportService;

/**
 * The Class CorrespondenceStagingImportServiceImpl. imports data from correspondencestaging to correspondencebase along with files after checking all
 * the validations
 */
@Service
public class CorrespondenceStagingImportServiceImpl implements ICorrespondenceStagingImportService {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(CorrespondenceStagingImportServiceImpl.class);

	/** The correspondence staging repository. */
	@Autowired
	private CorrespondenceStagingRepository correspondenceStagingRepository;

	/** The correspondence repository. */
	@Autowired
	private CorrespondenceBaseRepository correspondenceRepository;
	
	/** The correspondence dao. */
	@Autowired
	private ICorrespondenceDAO correspondenceDAO;

	/** The create application queue repository. */
	@Autowired
	private CreateApplicationQueueRepository createApplicationQueueRepository;

	/** The validate application repository. */
	@Autowired
	private ValidateApplicationRepository validateApplicationRepository;

	/** The user repository. */
	@Autowired
	private UserRepository userRepository;

	/** The document code repository. */
	@Autowired
	private DocumentCodeRepository documentCodeRepository;

	@Autowired
	private ApplicationStageDAO mdmStageDAO;

	@Autowired
	private ApplicationBaseDAO mdmBaseDAO;
	
	@Autowired
	private CreateApplicationQueueDAO createApplicationQDAO ;
	
	@Autowired
	private ApplicationRepository applicationRepository ;
	
	@Autowired
	private JurisdictionRepository jurisdictionRepository ;
	
	/** The number format validation service. */
	@Autowired
	private NumberFormatValidationService numberFormatValidationService;
	
	@Override
	public void importData() {
		logger.info("Import of Correspondence Staging Data Started");
		System.out.println("hello");
		List<CorrespondenceStaging> pendingStagingData = correspondenceStagingRepository.findStagingDataToBeImported("N417");

		if (CollectionUtils.isNotEmpty(pendingStagingData)) {
			
			logger.info("got the data----");
			List<CorrespondenceStaging> stagingDataToBeDeleted = new ArrayList<CorrespondenceStaging>();
			List<ValidateApplication> validateApplications = new ArrayList<ValidateApplication>();
			Map<String , Integer> compositeKeyCountMapStaging = getKeyCountMapFromStaging(pendingStagingData);
			Map<String , Integer> compositeKeyCountMapBase = getKeyCountMapFromBase() ;
			
			if(compositeKeyCountMapBase==null){
				compositeKeyCountMapBase = new HashMap<>();
				logger.info("base map is null++\n");
			}else{
				logger.info("base is not null++\n");
			}
			for (CorrespondenceStaging correspondenceStaging : pendingStagingData) {
				boolean proceed = checkIfMandatoryFieldsPresent(correspondenceStaging) ;
				if(!proceed){
					correspondenceStaging.setStatus(Status.ERROR);
					stagingDataToBeDeleted.add(correspondenceStaging) ;
					continue ;
				}
				String stagingApplicationNumber = correspondenceStaging.getApplicationNumber();
				String formattedApplicationNumber = getFormattedApplicationNumber(stagingApplicationNumber,correspondenceStaging.getFilingDate()) ;
				correspondenceStaging.setApplicationNumber(formattedApplicationNumber);
				String stagingJurisdictionNumber = correspondenceStaging.getJurisdictionCode();
				try{
					if(Correspondence.Status.PENDING.equals( correspondenceStaging.getStatus() ) ){
						if( isApplicationPresentInBase(formattedApplicationNumber,stagingJurisdictionNumber) ){
							if( isDuplicateCheckLogicPassed(correspondenceStaging , compositeKeyCountMapStaging , compositeKeyCountMapBase) ){
								CorrespondenceBase saved = prepareImportDataToBaseTable(correspondenceStaging) ;
								boolean successSaved = importFile(saved , correspondenceStaging) ;
								if(successSaved){
									correspondenceStaging.setStatus(Status.IMPORTED_TO_BASE_TABLE);
								}else{
									correspondenceRepository.delete(saved);
								}
							} else {
								correspondenceStaging.setStatus(Status.DUPLCATE_RECORD_FOUND_IN_BASE);
							}
							prepareValidationQueueData(validateApplications, correspondenceStaging);
						} else if( isApplicationPresentInQ(formattedApplicationNumber,stagingJurisdictionNumber)
								|| isAppJurisdictionPresentInStage(formattedApplicationNumber,stagingJurisdictionNumber)) {
							correspondenceStaging.setStatus(Status.CREATE_APPLICATION_QUEUE_INITIATED);
						} else {
							CreateApplicationQueue applicationQueue = moveToCreateApplicationQueue(correspondenceStaging);
							createApplicationQueueRepository.save(applicationQueue) ;
							logger.info("inserted to Application Queue ");
							correspondenceStaging.setStatus(Status.CREATE_APPLICATION_QUEUE_INITIATED);
						}
					} else if(Correspondence.Status.CREATE_APPLICATION_QUEUE_INITIATED.equals(correspondenceStaging.getStatus()) &&
						   isApplicationPresentInBase(formattedApplicationNumber,stagingJurisdictionNumber) ){
							if( isDuplicateCheckLogicPassed(correspondenceStaging , compositeKeyCountMapStaging , compositeKeyCountMapBase) ){
								CorrespondenceBase saved = prepareImportDataToBaseTable(correspondenceStaging) ;
								boolean successSaved = importFile(saved , correspondenceStaging) ;
								if(successSaved){
									correspondenceStaging.setStatus(Status.IMPORTED_TO_BASE_TABLE);
								}else{
									correspondenceRepository.delete(saved);
								}
							} else {
								correspondenceStaging.setStatus(Status.DUPLCATE_RECORD_FOUND_IN_BASE);
							}
							prepareValidationQueueData(validateApplications, correspondenceStaging);
					}
					stagingDataToBeDeleted.add(correspondenceStaging) ;
				}catch(ApplicationException exeption){
					logger.error("exeption caught in staging import service :"+exeption);
				}

			}

			correspondenceStagingRepository.save(stagingDataToBeDeleted);
			logger.info("updated correspondence staging ");
			validateApplicationRepository.save(validateApplications);
			logger.info("inserted to ValidateApplication ");
			
		}

	}


	private static boolean checkIfMandatoryFieldsPresent(CorrespondenceStaging correspondenceStaging) {
		return (correspondenceStaging.getApplicationNumber()!=null && correspondenceStaging.getMailingDate()!=null
											&& correspondenceStaging.getDocumentCode()!=null && correspondenceStaging.getStatus()!=null && correspondenceStaging.getSource()!=null
											&& correspondenceStaging.getSubSource()!=null && correspondenceStaging.getAttachmentSize()!=null 
											&& correspondenceStaging.getPageCount()!=null && correspondenceStaging.getVersion()!=0
											&& correspondenceStaging.getJurisdictionCode()!=null)?true  :false ; 
	}


	private static boolean importFile(CorrespondenceBase saved, CorrespondenceStaging correspondenceStaging) {
		boolean success = true ;
		String stagingFolder = FolderRelativePathEnum.CORRESPONDENCE
				.getAbsolutePath("staging") ;
		String stageFilePath = stagingFolder+correspondenceStaging.getId();
		File stageFile = new File(stageFilePath) ;
		String baseFilePath = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("base")+saved.getId();
		File baseFile = new File(baseFilePath) ;
		try {
			copyFolder(stageFile, baseFile);
		} catch (IOException exeption) {
			success=false;
			logger.debug("failed saving file "+stageFilePath);
			logger.error(exeption);
		}
		return success;
	}

	

	private  static void copyFolder(File src, File dest)
	    	throws IOException{
	    	
	    	if(src.isDirectory()){
	    		
	    		//if directory not exists, create it
	    		if(!dest.exists()){
	    		   dest.mkdir();
	    		}
	    		
	    		//list all the directory contents
	    		String[] files = src.list();
	    		
	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}
	    	   
	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest); 
	    	                     
	    	        byte[] buffer = new byte[1024];
	    	    
	    	        int length;
	    	        //copy the file content in bytes 
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }
	 
	    	        in.close();
	    	        out.close();	    	
	    	    }
	    }

	private String getFormattedApplicationNumber(String unformattedAppno,Calendar filingDateCal){
		boolean isWOApplication = 	unformattedAppno.startsWith(Constant.WO_CODE_INDICATOR) ? true : false;
		String jurisdictionCode = isWOApplication
				? Constant.WO_CODE
						: Constant.US_CODE;
		return numberFormatValidationService.getConvertedValue(unformattedAppno, NumberType.APPLICATION,
				jurisdictionCode, isWOApplication ? ApplicationType.PCT_US_SUBSEQUENT_FILING : ApplicationType.ALL, filingDateCal, null);
	}
	/**
	 * 
	 * @param stagingApplicationNumber
	 * @param stagingJurisdictionNumber
	 * @return isPresent
	 */
	private boolean isAppJurisdictionPresentInStage(String stagingApplicationNumber, String stagingJurisdictionNumber) {
		boolean isPresent = true;
		List<ApplicationStage> applctns = mdmStageDAO.findApplicationStageByNumberJurisdiction(stagingJurisdictionNumber, stagingApplicationNumber) ;
		if(applctns==null || applctns.isEmpty()){
			isPresent = false;
		}
		return isPresent;
	}

	/**
	 * findCreateApplicationQ by jurisdiction and appNumber
	 * @param stagingApplicationNumber
	 * @param stagingJurisdictionNumber
	 * @return isPresent
	 */
	private boolean isApplicationPresentInQ(String stagingApplicationNumber, String stagingJurisdictionNumber) {
		boolean isPresent = true;
		List<CreateApplicationQueue> applications = createApplicationQDAO.findApplicationQByNumberJurisdictionWithError(stagingJurisdictionNumber, stagingApplicationNumber);
		if(applications==null || applications.isEmpty()){
			isPresent = false;
		}
		for(CreateApplicationQueue each : applications){
			each.setStatus(QueueStatus.INITIATED);
		}
		createApplicationQueueRepository.save(applications) ;
		return isPresent;
	}

	/**
	 * create hashmap of compositeKey and count in correspondence base table
	 * @param correspondencesInBaseTable
	 * @return compositeKeyCountMapBase
	 */
	public  Map<String, Integer> getKeyCountMapFromBase() {
		List<CorrespondenceBaseKeysDTO> correspondencesInBaseTable = correspondenceDAO.getCorrespondenceBaseKeysDTOs();
		Map<String,Integer> compositeKeyCountMapBase = new HashMap<>();
		for (CorrespondenceBaseKeysDTO correspondence : correspondencesInBaseTable) {
			
			String mailingDate = "" ;
			if(correspondence.getMailingDate()!=null){
				mailingDate = BlackboxDateUtil.calendarToStr(correspondence.getMailingDate(),
						TimestampFormat.FULL_DATE_TIME);
			}
			String docCode = correspondence.getDocCode()!=null ? correspondence.getDocCode() : "" ;
			String jurisdictionCode = correspondence.getJurisdictionCode()!=null ? correspondence.getJurisdictionCode() : "" ;
			String baseTableKey = BlackboxUtils.concat(correspondence.getApplicationNumber(),
					jurisdictionCode,
					docCode, 
					mailingDate ,
					String.valueOf(correspondence.getPageCount()),
					String.valueOf(correspondence.getAttachmentSize()));
			
			if( compositeKeyCountMapBase.containsKey(baseTableKey) ){
				int count = compositeKeyCountMapBase.get(baseTableKey) ;
				compositeKeyCountMapBase.put(baseTableKey, count+1) ;
			} else {
				compositeKeyCountMapBase.put(baseTableKey, 1) ;
			}
			
		}
		return compositeKeyCountMapBase;
	}

	/**
	 * create hashmap of compositeKey and count in correspondence base table
	 * @param correspondencesInBaseTable
	 * @return compositeKeyCountMapBase
	 */
	private static Map<String, Integer> getKeyCountMapFromStaging(List<CorrespondenceStaging> pendingStagingData) {
		Map<String,Integer> compositeKeyCountMapStaging = new HashMap<>();
		for(CorrespondenceStaging correspondenceStaging : pendingStagingData) {
			String mailingDate = "" ;
			if(correspondenceStaging.getMailingDate()!=null){
				mailingDate = BlackboxDateUtil.calendarToStr(correspondenceStaging.getMailingDate(),
					TimestampFormat.FULL_DATE_TIME);
			}
			String baseTableKey = BlackboxUtils.concat(correspondenceStaging.getApplicationNumber(),
					correspondenceStaging.getJurisdictionCode(),
					correspondenceStaging.getDocumentCode(), 
					mailingDate ,
					String.valueOf(correspondenceStaging.getPageCount()),
					String.valueOf(correspondenceStaging.getAttachmentSize()));
			if( compositeKeyCountMapStaging.containsKey(baseTableKey) ){
				int count = compositeKeyCountMapStaging.get(baseTableKey);
				compositeKeyCountMapStaging.put(baseTableKey, count+1) ;
			} else {
				compositeKeyCountMapStaging.put(baseTableKey, 1) ;
			}
		}
		return compositeKeyCountMapStaging ;
	}

	/**
	 * If the records in corresponding staging with the same composite key is greater than those with same composite key
	 * in Corresponding base then return true else false
	 * @param correspondenceStaging
	 * @param compositeKeyCountStaging
	 * @param compositeKeyCountBase
	 * @return checkPassed
	 */
	private static boolean isDuplicateCheckLogicPassed(CorrespondenceStaging correspondenceStaging,
			Map<String, Integer> compositeKeyCountStaging, Map<String, Integer> compositeKeyCountBase) {
		boolean checkPassed = false;
		String mailingDate = "" ;
		if(correspondenceStaging.getMailingDate()!=null){
			mailingDate = BlackboxDateUtil.calendarToStr(correspondenceStaging.getMailingDate(),TimestampFormat.FULL_DATE_TIME);
		}
		String baseTableKey = BlackboxUtils.concat(correspondenceStaging.getApplicationNumber(),
				correspondenceStaging.getJurisdictionCode(),
				correspondenceStaging.getDocumentCode(), 
				mailingDate ,
				String.valueOf(correspondenceStaging.getPageCount()),
				String.valueOf(correspondenceStaging.getAttachmentSize()));
		int countOfSameKeyInBase = 0;
		int countOfSameKeyInStaging = 0;
		countOfSameKeyInStaging = compositeKeyCountStaging.get(baseTableKey) ;
		if(compositeKeyCountBase==null){
			logger.info("inside DupCheck compositeMapBase is null\n");
		}else{
			countOfSameKeyInBase = compositeKeyCountBase.containsKey(baseTableKey)?compositeKeyCountBase.get(baseTableKey) : 0;
			logger.info("inside DupCheck compositeMapBase  not null\n");
			
		}
		if( countOfSameKeyInStaging>countOfSameKeyInBase ){
			checkPassed = true;
		}
		logger.info("checkPassed is "+checkPassed+"\n");
		return checkPassed;
	}

	/**
	 * Check if Application with number and jurisdiction present 
	 * @param stagingApplicationNumber
	 * @param stagingJurisdictionNumber
	 * @return isPresent
	 */
	private boolean isApplicationPresentInBase(String stagingApplicationNumber, String stagingJurisdictionNumber) {
		boolean isPresent = false;
		ApplicationBase app = mdmBaseDAO.findActiveAppByApplicationNo(stagingJurisdictionNumber, stagingApplicationNumber);
		if(app!=null){
			isPresent = true;
		}
		return isPresent;
	}

	/**
	 * Prepare import data to base table.
	 *
	 * @param toBeImportedToBase
	 *            the to be imported to base
	 * @param correspondenceStaging
	 *            the correspondence staging
	 */
	private CorrespondenceBase prepareImportDataToBaseTable(CorrespondenceStaging correspondenceStaging) {
		CorrespondenceBase stagingToCorrespondenceBase = new CorrespondenceBase();

		stagingToCorrespondenceBase.setAttachmentSize(correspondenceStaging.getAttachmentSize());
		Jurisdiction jurisdiction = jurisdictionRepository.findByJurisdictionValue(correspondenceStaging.getJurisdictionCode()) ;
		ApplicationBase applicationBase = applicationRepository.findApplicationBaseByApn(correspondenceStaging.getApplicationNumber(),jurisdiction); 
		stagingToCorrespondenceBase.setPrivatePairUploadDate(correspondenceStaging.getPrivatePairUploadDate());
		stagingToCorrespondenceBase.setMailingDate(correspondenceStaging.getMailingDate());
		stagingToCorrespondenceBase.setCreatedByUser(userRepository.getUserIdByLoginId(BBWebCrawerConstants.BBX));
		logger.info("doc code is "+correspondenceStaging.getDocumentCode());
		DocumentCode document = documentCodeRepository.findByDocumentCode(correspondenceStaging.getDocumentCode());
			stagingToCorrespondenceBase
				.setDocumentCode(document);
		stagingToCorrespondenceBase.setSource(correspondenceStaging.getSource());
		stagingToCorrespondenceBase.setSubSource(correspondenceStaging.getSubSource());
		stagingToCorrespondenceBase.setStatus(Status.IMPORTED_FROM_STAGING_JOB);
		stagingToCorrespondenceBase.setApplication(applicationBase);
		stagingToCorrespondenceBase.setPageCount(correspondenceStaging.getPageCount());
		stagingToCorrespondenceBase.setStep(StepType.OCR);
		stagingToCorrespondenceBase.setStepStatus(StepStatus.NEW);
		stagingToCorrespondenceBase.setSplitId(correspondenceStaging.getSplitId());
		stagingToCorrespondenceBase.setParentChild(correspondenceStaging.getParentChild());
		stagingToCorrespondenceBase.setVersion(correspondenceStaging.getVersion());
		return correspondenceRepository.save(stagingToCorrespondenceBase) ;	
	}

	/**
	 * Prepare validation queue data.
	 *
	 * @param validateApplications
	 *            the validate applications
	 * @param correspondenceStaging
	 *            the correspondence staging
	 */
	private static void prepareValidationQueueData(List<ValidateApplication> validateApplications,
			CorrespondenceStaging correspondenceStaging) {
		ValidateApplication validateApplication = new ValidateApplication();
		validateApplication.setCustomerNumber(correspondenceStaging.getCustomerNumber());
		validateApplication.setApplicationNumber(correspondenceStaging.getApplicationNumber());
		validateApplication.setJurisdictionCode(correspondenceStaging.getJurisdictionCode());

		validateApplications.add(validateApplication);
	}

	/**
	 * Move to create application queue.
	 *
	 * @param createApplicationQueues
	 *            the create application queues
	 * @param correspondenceStaging
	 *            the correspondence staging
	 * @param stagingApplicationNumber
	 *            the staging application number
	 */
	private static CreateApplicationQueue moveToCreateApplicationQueue(CorrespondenceStaging correspondenceStaging) {
		logger.debug(
				MessageFormat.format("Application Number {0} not found in MDM. Inserting in Create Application Queue. ",
						correspondenceStaging.getApplicationNumber()));
			CreateApplicationQueue applicationQueue = new CreateApplicationQueue();
	
			applicationQueue.setCustomerNumber(correspondenceStaging.getCustomerNumber());
			applicationQueue.setApplicationNumberFormatted(correspondenceStaging.getApplicationNumber());
			applicationQueue.setJurisdictionCode(correspondenceStaging.getJurisdictionCode());
			applicationQueue.setStatus(QueueStatus.INITIATED);
			applicationQueue.setCorrespondenceLink(correspondenceStaging.getAttachmentName());
			applicationQueue.setCustomerNumber(correspondenceStaging.getCustomerNumber());
			applicationQueue.setFilingDate(correspondenceStaging.getFilingDate());
			applicationQueue.setAppNumberRaw(correspondenceStaging.getApplicationNumber());
			return applicationQueue ;
	}

 	
}
