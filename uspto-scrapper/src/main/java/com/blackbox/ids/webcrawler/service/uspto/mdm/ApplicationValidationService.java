package com.blackbox.ids.webcrawler.service.uspto.mdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.common.constant.Constant;
import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationDetails;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.mdm.OrganizationDetails;
import com.blackbox.ids.core.model.mdm.PatentDetails;
import com.blackbox.ids.core.model.mdm.PublicationDetails;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.AttorneyDocketNumber;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.webcrawler.ValidateApplication;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.repository.ApplicationRepository;
import com.blackbox.ids.core.repository.CustomerRepository;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.core.repository.mdm.AttorneyDocketRepository;
import com.blackbox.ids.core.repository.webcrawler.ValidateApplicationRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.exception.AuthenticationFailureException;
import com.blackbox.ids.exception.WebCrawlerGenericException;
import com.blackbox.ids.services.common.EmailService;
import com.blackbox.ids.services.validation.NumberFormatValidationService;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.IBaseCrawlerService;
import com.blackbox.ids.webcrawler.service.uspto.application.IApplicationWebCrawler;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;

/**
 * This Class validates the Application Metadata from USPTO
 * 
 * @author Nagarro
 *
 */
@Service("applicationValidationService")
public class ApplicationValidationService implements IBaseCrawlerService {

	private static final Logger LOGGER = Logger.getLogger(ApplicationValidationService.class);

	@Value("${uspto.application.wait.time}")
	private long waitingTime;

	@Autowired
	private IUSPTOLoginService loginService;

	@Autowired
	private ValidateApplicationRepository validateApplicationRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private AttorneyDocketRepository attorneyDocketRepository;

	@Autowired
	private BBWebCrawlerPropertyConstants properties;

	private WebDriver webDriver;

	@Autowired
	private IApplicationWebCrawler applicationWebCrawlerImpl;

	@Autowired
	private JurisdictionRepository jurisdictionRepository;

	@Autowired
	private NumberFormatValidationService numberValidationService;

	@Value("${application.retry.count}")
	private int retryCount;

	@Autowired
	private EmailService emailService;

	@Override
	@Transactional
	public Map<String, Object> execute(WebCrawlerJobAudit webCrawlerJobAudit) {
		fetchData(webCrawlerJobAudit);
		return null;
	}

	/**
	 * 
	 */
	private void fetchData(WebCrawlerJobAudit webCrawlerJobAudit) {

		long jobId = webCrawlerJobAudit.getId();
		String clientId = properties.getClientId();

		String baseDownloadPathOfBrowser = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("crawler");

		String downloadDir = BBWebCrawlerUtils.getCompleteFilePath(baseDownloadPathOfBrowser, jobId, clientId);
		List<ValidateApplication> applicationMetaDataList = getApplicationMetadataList();
		Map<String, List<ValidateApplication>> map = getMap(applicationMetaDataList);

		for (Entry<String, List<ValidateApplication>> entry : map.entrySet()) {
			try {
				Customer cEntity = customerRepository.findCustomerByNumber(entry.getKey());
				if (null == cEntity) {
					LOGGER.debug("Customer Number Could not be found: " + entry.getKey());
					String messageDescription = "Could not Login as Customer Number Could not be found: "
							+ entry.getKey() + " in the system";
					sendNotification(messageDescription);
					throw new WebCrawlerGenericException("Customer Number Could not be found: " + entry.getKey());
				}
				webDriver = BBWebCrawlerUtils.getWebDriver(downloadDir);
				applicationMetaDataList = entry.getValue();
				String epfFileBasePath = FolderRelativePathEnum.CRAWLER.getAbsolutePath("authenticationFiles");
				String epfFileName = cEntity.getAuthenticationData().getAuthenticationFileName();
				epfFileBasePath = epfFileBasePath + epfFileName;

				String password = cEntity.getAuthenticationData().getPassword();

				loginService.login(webDriver, epfFileBasePath, password);

				for (ValidateApplication va : applicationMetaDataList) {

					applicationWebCrawlerImpl.searchApplicationNumber(webDriver, va.getApplicationNumberRaw(),
							va.getJurisdictionCode());

					Thread.sleep(waitingTime);
					if (!applicationWebCrawlerImpl.isErrorFound(webDriver)) {
						MdmRecordDTO applicationRecord = applicationWebCrawlerImpl.getAppMetaData(webDriver,
								va.getApplicationNumber());
						updateRecords(createApplicationBaseEnity(applicationRecord, va), va);
						Thread.sleep(waitingTime);
						applicationWebCrawlerImpl.selectNewCase(webDriver);
					} else {
						LOGGER.info("Application Number Not Found on USPTO: " + va.getApplicationNumberRaw());
						String messageDescription = "Application Number Not Found on USPTO: "
								+ va.getApplicationNumber();
						int count = va.getRetryCount();
						if (retryCount == va.getRetryCount()) {
							va.setStatus(QueueStatus.CRAWLER_ERROR);
							LOGGER.info("Sending email for this Application Number " + va.getApplicationNumberRaw());
							sendNotification(messageDescription);
						} else {
							va.setRetryCount(++count);
						}

					}
				}
			} catch (AuthenticationFailureException ex) {
				LOGGER.info("Failed to Login in USPTO Portal as Credential are invalid", ex);
				String messageDescription = "Failed to Login in USPTO Portal as Credential are invalid for Customer "
						+ entry.getKey();
				updateBulkErrorStatus(applicationMetaDataList, messageDescription);

				BBWebCrawlerUtils.closeWebDriver(webDriver, LOGGER);
				continue;
			} catch (WebCrawlerGenericException ex) {
				BBWebCrawlerUtils.closeWebDriver(webDriver, LOGGER);
				continue;
			} catch (InterruptedException ex) {
				LOGGER.error("InterruptedException Occured", ex);
				throw new WebCrawlerGenericException(ex);

			} finally {
				BBWebCrawlerUtils.logoutAndCloseWebDriver(webDriver, LOGGER);
			}
		}
	}

	private List<ValidateApplication> getApplicationMetadataList() {
		List<ValidateApplication> applicationMetaDataList = null;
		try {
			applicationMetaDataList = validateApplicationRepository.findByCustomerNumberOrderByCustomerNumber();
		} catch (Exception exception) {
			LOGGER.error("Failed to get ValidateApplication Records from database :", exception);
			throw exception;
		}

		return applicationMetaDataList;
	}

	/**
	 * 
	 * @param applicationMetaDataList
	 * @return Returns a Map having key as a Customer Number.
	 */
	private static Map<String, List<ValidateApplication>> getMap(List<ValidateApplication> applicationMetaDataList) {

		Map<String, List<ValidateApplication>> map = new HashMap<String, List<ValidateApplication>>();

		for (ValidateApplication va : applicationMetaDataList) {
			String customerNumber = va.getCustomerNumber();

			if (null == map.get(customerNumber)) {
				List<ValidateApplication> lst = new ArrayList<ValidateApplication>();
				lst.add(va);
				map.put(customerNumber, lst);
			} else {
				List<ValidateApplication> lst = map.get(customerNumber);
				lst.add(va);
			}
		}
		return map;
	}

	private ApplicationBase createApplicationBaseEnity(MdmRecordDTO applicationRecord, ValidateApplication va) {

		String applicationNumber = applicationRecord.getApplicationNumber();
		LOGGER.info("Updating Data for Application Id : " + applicationNumber);

		Jurisdiction jurisdiction = jurisdictionRepository.findByJurisdictionValue(va.getJurisdictionCode());
		ApplicationBase entity = applicationRepository.findApplicationBaseByApn(va.getApplicationNumber(),
				jurisdiction);

		LOGGER.info("Found ApplicationBase :" + entity);

		String customerNumber = applicationRecord.getCustomerNumber();
		Customer cEntity = customerRepository.findCustomerByNumber(customerNumber);

		if (!customerNumber.equalsIgnoreCase(entity.getCustomer().getCustomerNumber())) {

			if (null != cEntity) {
				LOGGER.info("Updating Customer number");
				entity.setCustomer(cEntity);
			} else {
				LOGGER.info("Found new Customer number in the US Portal but not in the System");
			}
		}

		if (null != entity.getAppDetails()) {

			if (null != applicationRecord.getFilingDate()) {
				entity.getAppDetails().setFilingDate(BlackboxDateUtil.toCalendar(applicationRecord.getFilingDate()));
			}
			if (null != applicationRecord.getConfirmationNumber()) {
				entity.getAppDetails().setConfirmationNumber(applicationRecord.getConfirmationNumber());
			}
		}

		if (null == entity.getAppDetails()
				&& (null != applicationRecord.getFilingDate() || null != applicationRecord.getConfirmationNumber())) {
			entity.setAppDetails(new ApplicationDetails());
			entity.getAppDetails().setFilingDate(BlackboxDateUtil.toCalendar(applicationRecord.getFilingDate()));
			entity.getAppDetails().setConfirmationNumber(applicationRecord.getConfirmationNumber());
		}

		setPatentDetails(entity, applicationRecord, va);

		if (null != applicationRecord.getPublicationNumber() && null != applicationRecord.getPublicationDate()) {
			if (null == entity.getPublicationDetails()) {
				PublicationDetails publicationDetails = new PublicationDetails();
				entity.setPublicationDetails(publicationDetails);
			}

			String convertedPublicationNumber = numberValidationService.getConvertedValue(
					applicationRecord.getPublicationNumber(), NumberType.PUBLICATION, va.getJurisdictionCode(),
					ApplicationType.ALL, null, null);

			LOGGER.info("Converted Publicatiob Number is :" + convertedPublicationNumber);
			
			entity.getPublicationDetails().setPublicationNumber(convertedPublicationNumber);
			entity.getPublicationDetails()
					.setPublishedOn(BlackboxDateUtil.toCalendar(applicationRecord.getPublicationDate()));
		}

		if (null != applicationRecord.getEntityStatus()) {
			if (null == entity.getOrganizationDetails()) {
				entity.setOrganizationDetails(new OrganizationDetails());
			}
			entity.getOrganizationDetails().setEntity(Assignee.Entity.fromString(applicationRecord.getEntityStatus()));
		}

		if (null != applicationRecord.getProsecutionStatus()) {
			if (null == entity.getOrganizationDetails()) {
				entity.setOrganizationDetails(new OrganizationDetails());
			}
			entity.getOrganizationDetails().setProsecutionStatus(applicationRecord.getProsecutionStatus());
		}

		if (null != applicationRecord.getAttorneyDocket()) {
			if (null != entity.getAttorneyDocketNumber() && !entity.getAttorneyDocketNumber().getSegment()
					.equalsIgnoreCase(applicationRecord.getAttorneyDocket())) {

				AttorneyDocketNumber dNumber = attorneyDocketRepository
						.findAttorneyDocketNumberByValue(applicationRecord.getAttorneyDocket());

				if (null == dNumber) {
					dNumber = new AttorneyDocketNumber();
					dNumber.setSegment(applicationRecord.getAttorneyDocket());
					attorneyDocketRepository.save(dNumber);
				}
				entity.setAttorneyDocketNumber(dNumber);
			}
		}

		Customer customer = customerRepository.findCustomerByNumber(applicationRecord.getCustomerNumber());
		if (null == customer) {
			LOGGER.info("Could Not Found Customer Number In the System :" + applicationRecord.getCustomerNumber());
		}
		return entity;
	}

	private void updateRecords(ApplicationBase entity, ValidateApplication va) {
		applicationRepository.save(entity);
		va.setStatus(QueueStatus.SUCCESS);
		validateApplicationRepository.save(va);
		LOGGER.info("Updating Completed");
	}

	private void updateBulkErrorStatus(List<ValidateApplication> applicationMetaDataList, String messageDescription) {

		for (ValidateApplication va : applicationMetaDataList) {
			int count = va.getRetryCount();
			if (retryCount == va.getRetryCount()) {
				va.setStatus(QueueStatus.AUTHENTICATION_ERROR);
				sendNotification(messageDescription);
			} else {
				va.setRetryCount(++count);
			}
		}
		validateApplicationRepository.save(applicationMetaDataList);
	}

	private void setPatentDetails(ApplicationBase entity, MdmRecordDTO applicationRecord, ValidateApplication va) {

		if (null != applicationRecord.getPatentNumber() && null != applicationRecord.getPatentDate()) {

			String convertedPatentNumber = numberValidationService.getConvertedValue(
					applicationRecord.getPatentNumber(), NumberType.PATENT, va.getJurisdictionCode(),
					ApplicationType.ALL, null, null);
			if (null != convertedPatentNumber) {

				if (null == entity.getPatentDetails()) {
					entity.setPatentDetails(new PatentDetails());
				}
				entity.getPatentDetails().setPatentNumber(convertedPatentNumber);
				entity.getPatentDetails().setIssuedOn(BlackboxDateUtil.toCalendar(applicationRecord.getPatentDate()));
			}
		}

		if (null != applicationRecord.getTitleOfInvention() || null != applicationRecord.getGroupArtUnit()
				|| null != applicationRecord.getExaminerName()
				|| null != applicationRecord.getFirstNamedInventor() && null == entity.getPatentDetails()) {

			entity.setPatentDetails(new PatentDetails());

		}

		if (null != applicationRecord.getTitleOfInvention()) {
			entity.getPatentDetails().setTitle(applicationRecord.getTitleOfInvention());
		}
		if (null != applicationRecord.getGroupArtUnit()) {
			entity.getPatentDetails().setArtUnit(applicationRecord.getGroupArtUnit());
		}

		if (null != applicationRecord.getExaminerName()) {
			entity.getPatentDetails().setExaminer(applicationRecord.getExaminerName());
		}

		if (null != applicationRecord.getFirstNamedInventor()) {
			entity.getPatentDetails().setFirstNameInventor(applicationRecord.getFirstNamedInventor());
		}

	}

	private void sendNotification(String messageDescription) {
		Message message = new Message();
		message.setTemplateType(TemplateType.CRAWLER_AUTHENTICATION);
		message.setReceiverList(properties.adminEmailId);
		message.setStatus(Constant.MESSAGE_STATUS_NOT_SENT);

		MessageData templateData = new MessageData("body", messageDescription);
		List<MessageData> list = new ArrayList<>(1);
		list.add(templateData);
		emailService.send(message, list);

	}

}
