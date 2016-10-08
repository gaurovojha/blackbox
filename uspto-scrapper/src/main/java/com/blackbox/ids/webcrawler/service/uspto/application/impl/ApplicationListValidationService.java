package com.blackbox.ids.webcrawler.service.uspto.application.impl;

import java.io.File;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.spi.FormatConversionProvider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.ApplicationStageDAO;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationStatusData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.PatentApplicationList;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.mstr.AuthenticationData;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.IDSRelevantStatus;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.webcrawler.CreateApplicationQueue;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.repository.ApplicationRepository;
import com.blackbox.ids.core.repository.CustomerRepository;
import com.blackbox.ids.core.repository.webcrawler.AuthenticationDataRepository;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.webcrawler.dao.IWebCrawlerApplicationDAO;
import com.blackbox.ids.exception.AuthenticationFailureException;
import com.blackbox.ids.exception.WebCrawlerGenericException;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.validation.NumberFormatValidationService;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.application.IApplicationListValidationScrappingService;
import com.blackbox.ids.webcrawler.service.uspto.application.IApplicationListValidationService;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;


@Service
public class ApplicationListValidationService implements IApplicationListValidationService {

	private static final Logger LOGGER = Logger.getLogger(ApplicationListValidationService.class);
	
	//Below variables and constants will be used when commented code in compareCustomerApplicationNumbers method will be used.
	//So keeping it commented.
	
	private static final int NUMBER_OF_DAYS_EXCEEDED = 90;
	private static final String FALSE_VALUE = "false";
	private static final String STATUS_ABANDON = "Abandon";
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private IWebCrawlerApplicationDAO applicationDAO;
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private AuthenticationDataRepository authenticationDataRepository;
	
	@Autowired
	private ApplicationStageDAO applicationStageDAO;
	
	/** The login service. */
	@Autowired
	private IUSPTOLoginService loginService;
	
	/** The properties. */
	@Autowired
	private BBWebCrawlerPropertyConstants properties;
	
	@Autowired
	private NumberFormatValidationService formatService;
	
	/**  ApplicationListValidationScrappingService */
	@Autowired
	private IApplicationListValidationScrappingService applicationListValidationScrappingService;
	
	@Autowired
	private NotificationProcessService notificationProcessService;

	
	@Override
	public void execute(WebCrawlerJobAudit webCrawlerJobAudit) throws InterruptedException, ApplicationException, WebCrawlerGenericException{
		LOGGER.debug("Web crawler for create-application started..");
		WebDriver webDriver = null;
		// step : fetch application number based on records sorted by customer number
		Map<String, List<ApplicationStatusData>> customerApplicationData = new HashMap<String, List<ApplicationStatusData>>();
		Map<String, PatentApplicationList> customerApplicationListMap = new HashMap<String, PatentApplicationList>();
		List<String> uiSelectedCustomerNos = null;
		List<String> customersNosFoundInMDM = new ArrayList<String>();
		
		Long crawlerId = webCrawlerJobAudit.getId();
		String clientId = properties.clientId;
		String downloadDir = BBWebCrawlerUtils.getCompleteFilePath(properties.baseDownloadPath, crawlerId, clientId);
		
		List<AuthenticationData> loginUsersList= authenticationDataRepository.findAll();
		/* For each user that logins to USPTO, have a set of customers for which validation is performed and respectively application numbers are processed */
		
		for(AuthenticationData loginUser: loginUsersList){
			try{
				webDriver = BBWebCrawlerUtils.getWebDriver(downloadDir);
				boolean isLoginSuccessful = loginToPortal(crawlerId, loginUser, webDriver);
				if(isLoginSuccessful){
					/* Customers displayed in a select list when any user logs in */
					uiSelectedCustomerNos = applicationListValidationScrappingService.scrappingData(webDriver);
					LOGGER.info("Scrapping of data is completed");
					TimeUnit.SECONDS.sleep(10);
													
					/* Downloaded xmls for USPTO listed customers */
					final File[] downloadedFiles = BBWebCrawlerUtils.getAllFilesDownloaded(BBWebCrawlerUtils.getCompleteFilePath(properties.baseDownloadPath, crawlerId, properties.clientId),
											BBWebCrawerConstants.XML_SUFFIX);
					
					if(downloadedFiles.length!=0||downloadedFiles!=null){
						for(File file:downloadedFiles){
							PatentApplicationList patentApplicationList = BBWebCrawlerUtils.getMarshalledApplicatonListObject(file);
							if(patentApplicationList.getApplicationStatusData()!=null && !(patentApplicationList.getApplicationStatusData().isEmpty())){
								String customerNumber = patentApplicationList.getApplicationStatusData().get(0).getCustomerNumber();
								customerApplicationListMap.put(customerNumber, patentApplicationList);
								LOGGER.info("XML read and customer application object created");
							}
							else{
								LOGGER.info("application status data list is empty");
							}
						}
					}
					
					/*Preparing map customerApplicationData with customers number as key and their set applications data. Also, getting list of application numbers 
					 * from application base, stage and create application tables */
					
					if(!(customerApplicationListMap.isEmpty())){
						List<Customer> customers = null;
						if ((uiSelectedCustomerNos!= null) && CollectionUtils.isNotEmpty(uiSelectedCustomerNos)) {
							customers = customerRepository.findAllCustomersByNumber(uiSelectedCustomerNos);
							if(customers!=null && !(customers.isEmpty())){
								for(Customer customer : customers){
									customersNosFoundInMDM.add(customer.getCustomerNumber());
									prepareCustomerApplicationNumbersMap(customer.getCustomerNumber(),customerApplicationListMap.get(customer.getCustomerNumber()), customerApplicationData);
								}
								List<CreateApplicationQueue> createApplicationQueueList = applicationDAO.getCreateApplicationQueueList(customersNosFoundInMDM);
								List<String> appNosCreateAppQue = prepareApplicationNosFromCreateQ(createApplicationQueueList);
								List<ApplicationBase> mdmApplicationBaseList = applicationRepository.findAllApplicationNumbersByCustomer(customersNosFoundInMDM);
								List<String> appNosMdm = prepareApplicationNosFromMdm(mdmApplicationBaseList);
								List<ApplicationStage> stagingApplicationStageList = applicationStageDAO.getApplicationStageForCustomers(customersNosFoundInMDM);
								List<String> appNosStage = prepareApplicationNosFromStage(stagingApplicationStageList);
								if(customerApplicationData.entrySet()!=null && customerApplicationData.entrySet().size()!=0){
									compareCustomerApplicationNumbers(customerApplicationData, mdmApplicationBaseList,appNosMdm,appNosCreateAppQue,appNosStage);
								}
							}else{
								LOGGER.info("No customer matched in USPTO and Master customer table ");
							}	
						}else{
							LOGGER.info("No customer found on USPTO for this login");
						}
					}else {
						LOGGER.info("Application list is empty");
					}
				}
				LOGGER.info("User specific cutomer application nos validation process is completed");
			}finally {
				BBWebCrawlerUtils.closeWebDriver(webDriver, LOGGER);
			}
		}
	}
		
	/**
	 * 
	 * Login to portal
	 * @param crawlerId
	 */
	private boolean loginToPortal(long crawlerId, AuthenticationData loginUserInfo, WebDriver webDriver){
		boolean isLoggedIn = false;
		try{
			LOGGER.info("About to Login");
			String completePath = BlackboxUtils.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath("authenticationFiles"),
					loginUserInfo.getAuthenticationFileName());
			loginService.login(webDriver, completePath, loginUserInfo.getPassword());
			isLoggedIn = true;
			LOGGER.info("Login Successful");
			return isLoggedIn;
		}catch(AuthenticationFailureException e){
			isLoggedIn = false;
			LOGGER.info("Login Unsuccessful");
			LOGGER.info(e);
			return isLoggedIn;
		}		
		
		
	}
	
	/**
	 * 
	 * To prepare from uspto site downloaded xmls, a map for customer number as key and their application numbers as values
	 * @param customerNumber
	 * @param patentApplicationList
	 * @param customerApplicationData
	 */
	public void prepareCustomerApplicationNumbersMap(String customerNumber, PatentApplicationList patentApplicationList, Map<String, List<ApplicationStatusData>> customerApplicationData){
		final List<ApplicationStatusData> applicationStatusDataList = patentApplicationList.getApplicationStatusData();
		if(!(applicationStatusDataList.isEmpty())){
			String customerNoForPatentListObject = applicationStatusDataList.get(0).getCustomerNumber(); 
			if(customerNoForPatentListObject.equals(customerNumber)){
				customerApplicationData.put(customerNumber, applicationStatusDataList);
			}
		}
	}
	
	/**
	 * To get list of application nos. present in mdm 
	 * @param mdmApplicationNumbers
	 * @return appNosMdm list of application numbers in ApplicationBase table
	 */
	public List<String> prepareApplicationNosFromMdm(List<ApplicationBase> mdmApplicationNumbers){
		List<String> appNosMdm=new ArrayList<String>();
		for(ApplicationBase appBaseObj:mdmApplicationNumbers){
			appNosMdm.add(appBaseObj.getApplicationNumber());
		}
		return appNosMdm;
	}
	
	/**
	 * To get list of application nos. present in createApplication queue 
	 * @param mdmApplicationNumbers
	 * @return appNosCreateQ list of application numbers in CreateApplicationQueue table
	 */
	public List<String> prepareApplicationNosFromCreateQ(List<CreateApplicationQueue> createApplicationQueueList){
		List<String> appNosCreateQ=new ArrayList<String>();
		for(CreateApplicationQueue appBaseObj:createApplicationQueueList){
			appNosCreateQ.add(appBaseObj.getApplicationNumberFormatted());
		}
		return appNosCreateQ;
	}
	
	/**
	 * To get list of application nos. present in staging 
	 * @param mdmApplicationNumbers
	 * @return appNosStage list of application numbers in ApplicationStage table
	 */
	private List<String> prepareApplicationNosFromStage(List<ApplicationStage> stagingApplicationNumbers){
		List<String> appNosStage=new ArrayList<String>();
		for(ApplicationStage appBaseObj:stagingApplicationNumbers){
			appNosStage.add(appBaseObj.getApplicationNumber());
		}
		return appNosStage;
	}
	
	/**
	 * 
	 * Comparing application numbers from xmls downlaoded, ApplicationBase, ApplicationStage, CreateApplication.
	 * If any application number is found in ApplicationBase table, check for its data inconsistency based on status field value of these application numbers and send a pair notification for it.
	 * and continue with next application number.
	 * Else If application number is found in staging table or create application table, then nothing to do for that application and move to next application.
	 * Else create an entry for the new application number found if it is not a part of exclusion list.
	 * @param customerApplicationData
	 * @param mdmApplicationBaseList
	 * @param mdmApplicationNumbers
	 * @param createQApplicationNumbers
	 * @param stageApplicationNumbers
	 */
	@Override
	public Map<String, ArrayList<Long>> compareCustomerApplicationNumbers(final Map<String, List<ApplicationStatusData>> customerApplicationData, final List<ApplicationBase> mdmApplicationBaseList,
			final List<String> mdmApplicationNumbers, final List<String> createQApplicationNumbers, final List<String> stageApplicationNumbers){
		
		Map<String, ArrayList<Long>> processedApplicationIdsMap = new HashMap<String, ArrayList<Long>>();
		ArrayList<Long> appNosCreated = new ArrayList<Long>();
		ArrayList<Long> appNosStatusUpdated = new ArrayList<Long>();
		for(Entry<String, List<ApplicationStatusData>> aCustomerApplication:customerApplicationData.entrySet()){
			
			for(ApplicationStatusData application: aCustomerApplication.getValue()){
				Calendar todayCalendarDate = Calendar.getInstance();		     
				
				Date date;
				Calendar calendarFilingDate = null;
				try {
					date = sdf.parse(application.getFilingDate());
					calendarFilingDate = dateToCalendar(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					LOGGER.info("parse exception convertig date to some format"+e);
				}
				ApplicationType applicationType = null;
				String jurisdictionCode = BBWebCrawlerUtils.getJurisdictionCodeByApplicationNumber(application.getApplicationNumber());
				if(jurisdictionCode.equals("US")){
					applicationType = ApplicationType.US_SUBSEQUENT_FILING;
				}else{
					applicationType = ApplicationType.PCT_US_SUBSEQUENT_FILING;
				}
				String convertedAppNumber = formatService.getConvertedValue(application.getApplicationNumber(), NumberType.APPLICATION, jurisdictionCode,
						applicationType, calendarFilingDate , null);

				boolean existsInExclustions = applicationDAO.checkApplicationInExclustionList(convertedAppNumber, jurisdictionCode);
				
				
				if(!(mdmApplicationNumbers.contains(convertedAppNumber)) && 
						!(stageApplicationNumbers.contains(convertedAppNumber)) && 
						!(createQApplicationNumbers.contains(convertedAppNumber)) && !existsInExclustions){
				    /* Create Application queue entry, as application is NOT found in master, stage, create application queue tables */
					LOGGER.info("Customer application number not found in master, stage and queue table. Need to create new entry in Create Application queue");
					Long id = createEntryInApplicationQueue(convertedAppNumber, application.getApplicationNumber(), application.getCustomerNumber(),jurisdictionCode, todayCalendarDate);
					appNosCreated.add(id);
				}else{
					/* If application is found in Master table, check its status and filing date and if required create a pair notification*/
					if(mdmApplicationNumbers.contains(convertedAppNumber)){
						LOGGER.info("Customer application number found in Application base table");
						processApplicationNumbersFoundInMDM(mdmApplicationBaseList, application,appNosStatusUpdated);
					}else if(createQApplicationNumbers.contains(convertedAppNumber)){
						/* If application is found in application queue, based on error status and jurisdiction code, create a new entry to queue,*/
						
						boolean isApplicationNumberExists = applicationDAO.checkIfApplicationQueueEntryExists(convertedAppNumber, jurisdictionCode, null);
						if(!isApplicationNumberExists && !existsInExclustions){
							Long id = createEntryInApplicationQueue(convertedAppNumber, application.getApplicationNumber(), application.getCustomerNumber(),jurisdictionCode, todayCalendarDate);
							appNosCreated.add(id);
						}
						LOGGER.info("Customer application number found in Application create application queue table");
					}
				}
			}
		}
		processedApplicationIdsMap.put("CreatedApplications", appNosCreated);
		processedApplicationIdsMap.put("StatusUpdatedApplications", appNosStatusUpdated);
		return processedApplicationIdsMap;
	}
	
	
	/**
	 * Process application numbers found in MDM based on status, difference of days from filing date to today's date and USPTO customer state 
	 * @param mdmApplicationBaseList
	 * @param application
	 */
	public void processApplicationNumbersFoundInMDM(final List<ApplicationBase> mdmApplicationBaseList, ApplicationStatusData application, ArrayList<Long> appNosStatusUpdated){
		
		for(ApplicationBase mdmEntry:mdmApplicationBaseList){
			Calendar recordUpdatedDate =  mdmEntry.getUpdatedDate();
			recordUpdatedDate.roll(Calendar.DAY_OF_YEAR, NUMBER_OF_DAYS_EXCEEDED);
			boolean imageAvailabilityFlag = Boolean.parseBoolean(application.getImageAvailabilityIndicator());
			String status = application.getApplicationStatusText();
			
			if((mdmEntry.getRecordStatus()==MDMRecordStatus.TRANSFERRED) && (Calendar.getInstance().compareTo(recordUpdatedDate)>0) && 
					(true == imageAvailabilityFlag)){
				//Create Pair Notification
				sendNotification(mdmEntry);
				appNosStatusUpdated.add(mdmEntry.getId());
			} 
			if((mdmEntry.getRecordStatus()==MDMRecordStatus.ALLOWED_TO_ABANDON) && (Calendar.getInstance().compareTo(recordUpdatedDate)>0) && 
					(!(status.contains(STATUS_ABANDON)))){
				//Create Pair Notification
				sendNotification(mdmEntry);
				appNosStatusUpdated.add(mdmEntry.getId());
			}
		}
	}
	
	/**
	 * Update IDS relevant status for application object
	 */
	public void updateIDSRelevantStatusForApplication(String status, ApplicationBase mdmEntry){
		applicationRepository.findOne(mdmEntry.getId()).getOrganizationDetails().setIdsRelevantStatus(IDSRelevantStatus.TRANSFERRED);
	}
	
	/**
	 * Send Notification
	 * @param application ApplicationBase
	 */
	private boolean sendNotification(ApplicationBase application){
			
		String jurisdictionCode = BBWebCrawlerUtils.getJurisdictionCodeByApplicationNumber(application.getApplicationNumber());
		String convertedAppNumber = formatService.getConvertedValue(application.getApplicationNumber(), NumberType.APPLICATION, jurisdictionCode,
				ApplicationType.US_SUBSEQUENT_FILING, null , null);
		boolean isNotificationSent = false;
		Long notificationProcessId = notificationProcessService.createNotification(application, null, null, 
				application.getId(), EntityName.APPLICATION_BASE, NotificationProcessType.UPDATE_IDS_STATUS, null, null);
		
		if(notificationProcessId!=null){
			LOGGER.info(MessageFormat
					.format("Notification created for Application number found in MDM with status transferred [notification process id : {0}, queue id : {1},  "
							+ "Application Number : {2}, IDS status : {3}",		
							notificationProcessId, application.getId(), convertedAppNumber,
							application.getOrganizationDetails().getIdsRelevantStatus()));
			isNotificationSent = true;
		}
		
		return isNotificationSent;
	}
	/**
	 * To create an entry in Create Application Entry table
	 * @param appNo
	 * @param customerNo
	 * @param jurisdictionCode
	 * @param todayCalendarDate
	 */
	private Long createEntryInApplicationQueue(String appNo, String appNoRaw, String customerNo, String jurisdictionCode, Calendar todayCalendarDate){
		CreateApplicationQueue applicationObjectToCreate = new CreateApplicationQueue(null, appNo, customerNo, jurisdictionCode, todayCalendarDate);
		applicationObjectToCreate.setAppNumberRaw(appNoRaw);
		Long id = applicationDAO.createApplicationRequestEntry(applicationObjectToCreate);
		LOGGER.info("CreateApplicationQueue entry for application number is created");
		return (id!=null)?id:0;
	}
	
	/**
	 * Conver Date to Calendar
	 * @param date
	 * @return
	 */
	private Calendar dateToCalendar(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;

	}
}
