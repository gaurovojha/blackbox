package com.blackbox.ids.services.mdm;

import java.io.File;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.test.context.transaction.TransactionConfiguration;

import com.blackbox.ids.core.dao.ApplicationStageDAO;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationStatusData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.PatentApplicationList;

import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.webcrawler.CreateApplicationQueue;

import com.blackbox.ids.core.repository.ApplicationRepository;
import com.blackbox.ids.core.repository.CustomerRepository;
import com.blackbox.ids.core.repository.webcrawler.WebCrawlerJobAuditRepository;
import com.blackbox.ids.core.repository.webcrawler.WebCrawlerJobStatusRepository;
import com.blackbox.ids.core.webcrawler.dao.IWebCrawlerApplicationDAO;

import com.blackbox.ids.it.AbstractIntegrationTest;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.application.IApplicationListValidationService;
import com.blackbox.ids.webcrawler.service.uspto.application.impl.ApplicationListValidationScrappingService;

@TransactionConfiguration(defaultRollback = true)
public class ApplicationListValidationTest extends AbstractIntegrationTest{
	
	private static final String folder1 = "src/test/resources/META-INF/test-data/mdm/64"; // for new application nos.
	private static final String folder2 = "src/test/resources/META-INF/test-data/mdm/68"; // for MDM found application nos. status abandoned
	private static final String folder3 = "src/test/resources/META-INF/test-data/mdm/69"; //  for MDM found application nos. status transferred
	
	@Autowired
	@Qualifier("applicationListValidationService")
	private IApplicationListValidationService applicationListValidationService;
	
	@Autowired
	private ApplicationListValidationScrappingService applicationListValidationScrappingService;
	
	@Autowired
	private WebCrawlerJobStatusRepository webCrawlerJobStatusRepository;
	
	/** The web crawler job audit repository. */
	@Autowired
	private WebCrawlerJobAuditRepository webCrawlerJobAuditRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private IWebCrawlerApplicationDAO applicationDAO;
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private ApplicationStageDAO applicationStageDAO;
	
	/** The properties. */
	@Autowired
	private BBWebCrawlerPropertyConstants properties;
	
	/** The logger. */
	private Logger logger = Logger.getLogger(ApplicationListValidationTest.class);
	
	private Map<String, List<ApplicationStatusData>> applicationNosMap;
	
	private List<String> appNosCreateQ;
	
	private List<String> appNosMdm;
	
	private List<String> appNosStage;
	
	private List<ApplicationBase> mdmApplicationBaseList;
	
	private List<String> inputCustomerList;
	
	private String folderName;
	
	
	/**
	 * Create applicatonQueue entry use case
	 * To test this use case. Put some dummy application nos. in XML, present in folder 64
	 */
	@Test
	public void testCreateApplicationEntry(){
		/* UI List */
		List<Long> addedToQueue= new ArrayList<Long>();
		File file = new File(folder1);
        inputCustomerList = new ArrayList<String>();
		inputCustomerList.add("136388");
		inputCustomerList.add("123-456-FAC");
   	    setInputCustomerList(inputCustomerList);
   	    folderName = file.getAbsolutePath();
   		/* Preparing data for functional testing */
		applicationNosMap = new HashMap<String, List<ApplicationStatusData>>();
		appNosCreateQ=new ArrayList<String>();
		appNosMdm=new ArrayList<String>();
		appNosStage=new ArrayList<String>();
		mdmApplicationBaseList=new ArrayList<ApplicationBase>();
	
		preparingData(applicationNosMap, mdmApplicationBaseList, appNosMdm, appNosCreateQ, appNosStage);
		mdmApplicationBaseList = applicationRepository.findAllApplicationNumbersByCustomer(inputCustomerList);
		// Create application logic
		Map<String, ArrayList<Long>> applicationsAddedToQueue = applicationListValidationService.compareCustomerApplicationNumbers(applicationNosMap, mdmApplicationBaseList,appNosMdm,appNosCreateQ,appNosStage);
	    for(Long id : applicationsAddedToQueue.get("CreatedApplications")){
			CreateApplicationQueue addedQueueObj = applicationDAO.getCreateApplicationQueueEntry(id);
			if(addedQueueObj!=null){
				addedToQueue.add(id);
			}
		} 
		Assert.assertNotEquals("application object found",0, addedToQueue.size());
	}
	
	/**
	 * Status update when application object found in application base table
	 * Folder 68 in test-data contains ids with necessary conditions.
	 */
	@Test
	public void testStatusUpdateTransferred(){
		List<Long> statusUpdatedAppNos= new ArrayList<Long>();
		inputCustomerList = new ArrayList<String>();
		inputCustomerList.add("136388");
		inputCustomerList.add("123-456-FAC");
   	    setInputCustomerList(inputCustomerList);
   	    folderName = folder3; //69 contains application nos in mdm with status transferred and filing date +90 = today's date 
   	       	    
   	 /* Preparing data for functional testing */
   	    applicationNosMap = new HashMap<String, List<ApplicationStatusData>>();
		appNosCreateQ=new ArrayList<String>();
		appNosMdm=new ArrayList<String>();
		appNosStage=new ArrayList<String>();
		mdmApplicationBaseList=new ArrayList<ApplicationBase>();
	
		preparingData(applicationNosMap, mdmApplicationBaseList, appNosMdm, appNosCreateQ, appNosStage);
		mdmApplicationBaseList = applicationRepository.findAllApplicationNumbersByCustomer(inputCustomerList);
   	    
   	    Map<String, ArrayList<Long>> applicationsUpdated = applicationListValidationService.compareCustomerApplicationNumbers(applicationNosMap, mdmApplicationBaseList,appNosMdm,appNosCreateQ,appNosStage);
   	    for(Long id : applicationsUpdated.get("StatusUpdatedApplications")){
			ApplicationBase statusUpdatedObject = applicationRepository.getOne(id);
			if(statusUpdatedObject!=null){
				statusUpdatedAppNos.add(id);
			}
		}   
   	    Assert.assertNotEquals("Update_IDS notification sent", 0 , statusUpdatedAppNos.size());
		
	}
	
	/**
	 * Status update when application object found in application base table
	 * Folder 68 in test-data contains ids with necessary conditions.
	 */
	@Test
	public void testStatusUpdateAbandoned(){
		List<Long> statusUpdatedAppNos= new ArrayList<Long>();
		inputCustomerList = new ArrayList<String>();
		inputCustomerList.add("136388");
		inputCustomerList.add("123-456-FAC");
   	    setInputCustomerList(inputCustomerList);
   	    folderName = folder2; //68 contains application nos in mdm with status abandoned and filing date +90 = today's date 
   	       	    
   	 /* Preparing data for functional testing */
   	    applicationNosMap = new HashMap<String, List<ApplicationStatusData>>();
		appNosCreateQ=new ArrayList<String>();
		appNosMdm=new ArrayList<String>();
		appNosStage=new ArrayList<String>();
		mdmApplicationBaseList=new ArrayList<ApplicationBase>();
		
		preparingData(applicationNosMap, mdmApplicationBaseList, appNosMdm, appNosCreateQ, appNosStage);
		mdmApplicationBaseList = applicationRepository.findAllApplicationNumbersByCustomer(inputCustomerList);
   	    
   	    Map<String, ArrayList<Long>> applicationsUpdated = applicationListValidationService.compareCustomerApplicationNumbers(applicationNosMap, mdmApplicationBaseList,appNosMdm,appNosCreateQ,appNosStage);
   	    for(Long id : applicationsUpdated.get("StatusUpdatedApplications")){
			ApplicationBase statusUpdatedObject = applicationRepository.getOne(id);
			if(statusUpdatedObject!=null){
				statusUpdatedAppNos.add(id);
			}
		}   
   	    Assert.assertNotEquals("Update_IDS notification sent", 0 , statusUpdatedAppNos.size());
		
	}
	
	/**
	 * Reading XML files and preparing Map for customer number as key and list of application numbers as values
	 */
     private void preparingData(Map<String, List<ApplicationStatusData>> applicationNosMap, List<ApplicationBase> mdmApplicationBaseList, List<String> appNosMdm , List<String> appNosCreateQ, List<String> appNosStage){ 
     
    	//applicationNosMap = new HashMap<String, List<ApplicationStatusData>>();
    	Map<String, PatentApplicationList> customerApplicationListMap = new HashMap<String, PatentApplicationList>();
    	 
    	/* 1. set up application  nos. in above customer xml a. application not present in stage, base or create application queue
    	 * 2. set up application  nos. in above customer xml a. application present in either stage, base or create application queue
    	 * 3. set up application  nos. in above customer xml a. application containing WO and US jurisdiction code.
    	 * 4. set up application  nos. in above customer xml a. application present in create application queue with status ERROR
    	 */
    	     	 
    	PatentApplicationList patentApplicationList = null;
    	List<String> customersNosFoundInMDM = new ArrayList<String>();
    	
    	final File[] downloadedFiles = BBWebCrawlerUtils.getAllFilesDownloaded(folderName,
    			 BBWebCrawerConstants.XML_SUFFIX);
		if(downloadedFiles!=null && downloadedFiles.length!=0){
			for(File file:downloadedFiles){
				patentApplicationList = BBWebCrawlerUtils.getMarshalledApplicatonListObject(file);
				if(patentApplicationList.getApplicationStatusData()!=null && patentApplicationList.getApplicationStatusData().size()!=0){
					String customerNumber = patentApplicationList.getApplicationStatusData().get(0).getCustomerNumber();
					customerApplicationListMap.put(customerNumber, patentApplicationList);
				}
				else{
					logger.info("application status data list is empty");
				}
			}
		}
		if(customerApplicationListMap.size()!=0){
			List<Customer> customers = null;
			if ((getInputCustomerList()!= null) &&CollectionUtils.isNotEmpty(getInputCustomerList())) {
				customers = customerRepository.findAllCustomersByNumber(getInputCustomerList());
				for(Customer customer : customers){
					customersNosFoundInMDM.add(customer.getCustomerNumber());
					final List<ApplicationStatusData> applicationStatusDataList = patentApplicationList.getApplicationStatusData();
					if(applicationStatusDataList.size()!=0){
						String customerNoForPatentListObject = applicationStatusDataList.get(0).getCustomerNumber(); 
						if(customerNoForPatentListObject.equals(customer.getCustomerNumber())){
							applicationNosMap.put(customer.getCustomerNumber(), applicationStatusDataList);
						}
					}
				}
				List<CreateApplicationQueue> createApplicationQueueList = applicationDAO.getCreateApplicationQueueList(customersNosFoundInMDM);
				//appNosCreateQ=new ArrayList<String>();
				for(CreateApplicationQueue appBaseObj:createApplicationQueueList){
					appNosCreateQ.add(appBaseObj.getApplicationNumberFormatted());
				}
				mdmApplicationBaseList = applicationRepository.findAllApplicationNumbersByCustomer(customersNosFoundInMDM);
				//appNosMdm=new ArrayList<String>();
				for(ApplicationBase appBaseObj:mdmApplicationBaseList){
					appNosMdm.add(appBaseObj.getApplicationNumber());
				}
				List<ApplicationStage> stagingApplicationStageList = applicationStageDAO.getApplicationStageForCustomers(customersNosFoundInMDM);
				//appNosStage=new ArrayList<String>();
				for(ApplicationStage appBaseObj:stagingApplicationStageList){
					appNosStage.add(appBaseObj.getApplicationNumber());
				}
				
			}
		}
     }
     
     private void setInputCustomerList(List<String> customerList){
    	 this.inputCustomerList=customerList;
     }
     
     private List<String> getInputCustomerList(){
    	 return inputCustomerList;
     }
     
}