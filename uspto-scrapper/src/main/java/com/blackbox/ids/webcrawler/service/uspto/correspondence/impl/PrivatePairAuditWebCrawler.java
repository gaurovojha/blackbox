package com.blackbox.ids.webcrawler.service.uspto.correspondence.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.mstr.AuthenticationData;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.webcrawler.AuthenticationDataRepository;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.exception.AuthenticationFailureException;
import com.blackbox.ids.exception.WebCrawlerGenericException;
import com.blackbox.ids.services.common.EmailService;
import com.blackbox.ids.services.crawler.IPrivatePairCommonService;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IPrivatePairAuditScrapper;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IPrivatePairAuditWebCrawler;

// TODO: Auto-generated Javadoc
/**
 * The Class {@link PrivatePairAuditWebCrawler} is the implementing class for {@link IPrivatePairAuditWebCrawler}.
 * This class is used to scrap the USPTO website for outgoing correspondence and audits the data that are present in Correspondence Base Table or Correspondence Download Tables.
 */
@Service
public class PrivatePairAuditWebCrawler implements IPrivatePairAuditWebCrawler {


	/** The logger. */
	private static Logger logger = Logger.getLogger(PrivatePairAuditWebCrawler.class);

	/** The properties. */
	@Autowired
	private BBWebCrawlerPropertyConstants properties;
	
	/** The private pair common service. */
	@Autowired
	private IPrivatePairCommonService privatePairCommonService;
	
	/** The authentication data repository. */
	@Autowired
	private AuthenticationDataRepository authenticationDataRepository;
	
	/** The private pair audit scrapper. */
	@Autowired
	private IPrivatePairAuditScrapper privatePairAuditScrapper;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private UserRepository userRepository;
	

	/**
	 * This API scraps the USPTO website for all the outgoing correspondences and audits them , if they are present in Blackbox system. The Steps performed are :-
	 *
	 * <p><ul>
	 * <li> Scraps the USPTO website for all Outgoing correspondences for given filters.
	 * <li> Downloads the XML for the filters selected.
	 * <li> The correspondences downloaded in XML is checked if it exists in Blackbox system.
	 * <li> The correspondence in XML is checked for existence in Correspondence base table and Download Office Table.
	 * <li> If the data doesnot exists in either of them , then the correspondences in XML are populated in the <code><b>{@link DownloadOfficeActionQueue}</b></code> entity.
	 * <li> <li> Persists the <code><b>{@link CorrespondenceStaging}</b></code> entity to the database.
	 * @param webCrawlerJobAudit
	 *            the entity containing the meta data of the job.
	 */
	@Override
	@Transactional
	public void execute(WebCrawlerJobAudit webCrawlerJobAudit) {
		Long crawlerId = webCrawlerJobAudit.getId();

		logger.info("Web Crawler Service for Private Pair Audit Called ");

		List<AuthenticationData> epfFiles = authenticationDataRepository.findAll();

			if (CollectionUtils.isNotEmpty(epfFiles)) {

				for (AuthenticationData epfFile : epfFiles) {
					try {
					processEachAuthenticationData(webCrawlerJobAudit, crawlerId, epfFile);
					}  catch(AuthenticationFailureException afex) {
						String error = MessageFormat.format("Authentication failed for authentication id {0} , file name {2}. Exception is {1}", epfFile.getId(),afex,epfFile.getAuthenticationFileName());
						logger.error(error);
						sendEmail("WebCrawlerPrivatePairAuditJob");
					} 
				}
			} else {
				logger.info(MessageFormat.format("No EPF file found during crawler id {0}", crawlerId));
			}
			logger.info(MessageFormat.format("Crawler id [{0}] for Private pair audit completed succesfully",
					webCrawlerJobAudit.getId()));
		
	}


	/**
	 * Process each authentication data.
	 *
	 * @param webCrawlerJobAudit the web crawler job audit
	 * @param crawlerId the crawler id
	 * @param epfFile the epf file
	 */
	private void processEachAuthenticationData(WebCrawlerJobAudit webCrawlerJobAudit, Long crawlerId,
			AuthenticationData epfFile) {
		try {
			logger.info(MessageFormat.format("Web Crawling started for epf id {0}", epfFile.getId()));
			String absolutePath = BlackboxUtils
					.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath(BBWebCrawerConstants.DOWNLOADS_FOLDER));
			File absolutePathFile = new File(absolutePath);
			if (!absolutePathFile.exists()) {
				absolutePathFile.mkdirs();
			}

			privatePairAuditScrapper.performScrapping(epfFile, absolutePath, webCrawlerJobAudit);

			final File lastDownloadedFile = BBWebCrawlerUtils.getLatestFileInADirectory(
					BBWebCrawlerUtils.getCompleteFilePath(absolutePath, crawlerId, properties.clientId),
					BBWebCrawerConstants.XML_SUFFIX);

			checkAndProcessXML(webCrawlerJobAudit, crawlerId, lastDownloadedFile);
		} catch (FileNotFoundException e) {
			String error = MessageFormat.format(
					"Some exception occured while crawling for job id {0}. Exception is {1}",
					webCrawlerJobAudit.getId(), e);
			logger.error(error);
			throw new WebCrawlerGenericException(error);
		}
	}


	/**
	 * Check and process xml.
	 *
	 * @param webCrawlerJobAudit the web crawler job audit
	 * @param crawlerId the crawler id
	 * @param lastDownloadedFile the last downloaded file
	 * @throws FileNotFoundException the file not found exception
	 */
	private void checkAndProcessXML(WebCrawlerJobAudit webCrawlerJobAudit, Long crawlerId,
			final File lastDownloadedFile) throws FileNotFoundException {
		if(lastDownloadedFile != null) {
			
		logger.info(MessageFormat.format(
				"Scrapping completed succesfully for crawler id {0}. Downloaded XML path is {1}", crawlerId,
				lastDownloadedFile.getAbsolutePath()));

		boolean isAnyRecordInXML = privatePairCommonService.processAndValidateXMLRecords(lastDownloadedFile,
				User.SYSTEM_ID,lastDownloadedFile.getName());
		if (!isAnyRecordInXML) {
			logger.info("No XML records to process for crawler id " + webCrawlerJobAudit.getId());
		}
		} else {
			logger.info("The XML file downloaded for crawler id " + webCrawlerJobAudit.getId() + " is null");
		}
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
