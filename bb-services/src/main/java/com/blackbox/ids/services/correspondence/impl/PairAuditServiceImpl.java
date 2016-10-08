package com.blackbox.ids.services.correspondence.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.blackbox.ids.core.dao.correspondence.IJobDAO;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationCorrespondence;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationCorrespondenceData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.CorrespondenceFileHeader;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.DocumentData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.DocumentList;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.FileHeader;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.OutgoingCorrespondence;
import com.blackbox.ids.core.model.correspondence.Job;
import com.blackbox.ids.core.model.correspondence.Job.Type;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.XmlParser;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.services.correspondence.ICorrespondenceService;
import com.blackbox.ids.services.correspondence.IPairAuditService;
import com.blackbox.ids.services.crawler.IPrivatePairCommonService;

/**
 * The Class PairAuditServiceImpl.
 */
@Service
public class PairAuditServiceImpl implements IPairAuditService {

	@Autowired
	private ICorrespondenceService correspondenceService;

	@Autowired
	private IJobDAO jobDAO;

	@Autowired
	private IPrivatePairCommonService privatePairCommonService;

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(PairAuditServiceImpl.class);

	/**
	 * It will validate the input file for Date Range.
	 * @param inputFile
	 *            the inputFile(uploaded xml file) to validate
	 * @return map the date range of the uploaded Xml.
	 * @throws FileNotFoundException
	 */
	@Override
	public Map<String, String> validateInputXML(final File inputFile) throws FileNotFoundException {

		Map<String, String> dateRangeMap = new HashMap<String, String>();

		List<Calendar> mailRoomDates = new ArrayList<>();
		OutgoingCorrespondence outgoingCorrespondence = null;
		outgoingCorrespondence = (OutgoingCorrespondence) XmlParser.fetchXmlData(inputFile,
				OutgoingCorrespondence.class);
		final FileHeader fileHeader = outgoingCorrespondence.getFileHeader();
		final CorrespondenceFileHeader correspondenceFileHeader = outgoingCorrespondence.getCorrespondenceFileHeader();
		final ApplicationCorrespondence applicationCorrespondence = outgoingCorrespondence
				.getApplicationCorrespondence();
		if (fileHeader != null && correspondenceFileHeader != null && applicationCorrespondence != null) {
			fetchApplicationCorrespodencesFromXml(dateRangeMap, mailRoomDates, applicationCorrespondence);
		} else {
			LOGGER.info(MessageFormat.format("Exception occured while processing the file. File {0} is corrupted",
					inputFile.getName()));
		}
		return dateRangeMap;
	}

	/**
	 * It will fetch Application Correspondence Data(s) from uploaded xml file
	 * @param dateRangeMap
	 *            the dateRangeMap containing the first and last date in xml
	 * @param mailRoomDates
	 *            the list of mailRoomDates in the uploaded xml
	 * @param applicationCorrespondence
	 *            the applicationCorrespondence of the uploaded Xml
	 */
	private void fetchApplicationCorrespodencesFromXml(Map<String, String> dateRangeMap, List<Calendar> mailRoomDates,
			final ApplicationCorrespondence applicationCorrespondence) {
		final List<ApplicationCorrespondenceData> applicationCorrespondenceDatas = applicationCorrespondence != null
				? applicationCorrespondence.getApplicationCorrespondenceData() : null;
		if (CollectionUtils.isNotEmpty(applicationCorrespondenceDatas)) {
			iterateAndProcessApplicationCorresppondences(dateRangeMap, mailRoomDates, applicationCorrespondenceDatas);
		}
	}

	/**
	 * It will process the Application Correspondence Data(s) from uploaded xml file and sort the mailRoomDate(s) of the
	 * uploaded xml
	 * @param dateRangeMap
	 *            the dateRangeMap containing the first and last date in xml
	 * @param mailRoomDates
	 *            the list of mailRoomDates in the uploaded xml
	 * @param applicationCorrespondence
	 *            the applicationCorrespondence of the uploaded Xml
	 */
	private void iterateAndProcessApplicationCorresppondences(Map<String, String> dateRangeMap,
			List<Calendar> mailRoomDates, final List<ApplicationCorrespondenceData> applicationCorrespondenceDatas) {
		for (ApplicationCorrespondenceData applicationCorrespondenceData : applicationCorrespondenceDatas) {
			processApplicationCorrespondenceData(mailRoomDates, applicationCorrespondenceData);
		}
		sortAndCreateDateRangeMap(dateRangeMap, mailRoomDates);
	}

	/**
	 * It will process the Application Correspondence Data(s) from uploaded xml file and fetch the mailRoomDate(s) of
	 * the uploaded xml
	 * @param mailRoomDates
	 *            the list of mailRoomDates in the uploaded xml
	 * @param applicationCorrespondence
	 *            the applicationCorrespondence of the uploaded Xml
	 */
	private void processApplicationCorrespondenceData(List<Calendar> mailRoomDates,
			ApplicationCorrespondenceData applicationCorrespondenceData) {
		if (applicationCorrespondenceData.getApplicationNumber() != null
				&& applicationCorrespondenceData.getCustomerNumber() != null) {
			final DocumentList documentList = applicationCorrespondenceData.getDocumentList();
			final List<DocumentData> documentDatas = documentList != null ? documentList.getDocumentData() : null;
			for (DocumentData documentData : documentDatas) {
				String mailRoomDate = BlackboxDateUtil.changeDateFormat("yyyy-MM-dd", "MMM-dd-yyyy",
						documentData.getMailRoomDate());
				mailRoomDates.add(BlackboxDateUtil.strToCalendar(mailRoomDate, TimestampFormat.MMMddyyyy));
			}
		}
	}

	/**
	 * It will sort the mailRoomDate(s) from the list of mailRoomDates and put it in a Map havinf first and last
	 * MailRoomDate
	 * @param dateRangeMap
	 *            the dateRangeMap containing the first and last date in xml
	 * @param mailRoomDates
	 *            the list of mailRoomDates in the uploaded xml
	 */
	private void sortAndCreateDateRangeMap(Map<String, String> dateRangeMap, List<Calendar> mailRoomDates) {
		if (CollectionUtils.isNotEmpty(mailRoomDates)) {
			Collections.sort(mailRoomDates);
			dateRangeMap.put(com.blackbox.ids.core.constant.Constant.INITIAL_DATE,
					BlackboxDateUtil.calendarToStr(mailRoomDates.get(0), TimestampFormat.MMMddyyyy));
			dateRangeMap.put(com.blackbox.ids.core.constant.Constant.FINAL_DATE, BlackboxDateUtil
					.calendarToStr(mailRoomDates.get(mailRoomDates.size() - 1), TimestampFormat.MMMddyyyy));
		}
	}

	/**
	 * It will upload the file to the specific location.
	 * @param multipartFile
	 *            file to be uploaded
	 * @param fileUploadBasePath
	 *            the BasePath for the uploadedFolder
	 * @return noting
	 */
	@Override
	public void uploadFileToSpecificLocation(final String fileUploadBasePath, final MultipartFile multipartFile)
			throws IOException {

		Job job = correspondenceService.addEntryToJobs(Type.PAIR);
		long id = job.getId();

		File convFile;
		try {
			String temporaryFilePath = BlackboxUtils.concat(fileUploadBasePath, multipartFile.getOriginalFilename());
			convFile = File.createTempFile(temporaryFilePath, ".xml");
			multipartFile.transferTo(convFile);
			final String fileUploadPath = BlackboxUtils.concat(fileUploadBasePath, String.valueOf(id), File.separator,
					multipartFile.getOriginalFilename());
			File file = new File(fileUploadPath);
			FileUtils.copyFile(convFile, file);
			convFile.deleteOnExit();
		} catch (IllegalStateException | IOException e) {
			job.setStatus(Job.Status.ERROR);
			job.setComments(e.getLocalizedMessage());
			correspondenceService.updateStatusInJobs(job);
			LOGGER.info("I/O exception occured while uploading a file to a specific location");
			throw e;
		}
	}

	/**
	 * It will download the unprocessed File and do Audit of that files.
	 * @return noting
	 */
	@Override
	@Transactional
	public void processManualAuditJobFiles() throws FileNotFoundException {

		final String fileDownloadBasePath = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("jobs");
		LOGGER.info("Auditing of Manually Uploaded Files started");
		List<Job> newPairAuditJobs = jobDAO.findPairAuditNewEntries();
		if (CollectionUtils.isNotEmpty(newPairAuditJobs)) {
			for (Job job : newPairAuditJobs) {
				processPairAuditJob(fileDownloadBasePath, job);
			}
		}
	}

	/**
	 * It will process the file corresponding to each job
	 * @param fileUploadBasePath
	 *            the BasePath for the uploadedFolder
	 * @param job
	 *            job related to file
	 * @return noting
	 */
	private void processPairAuditJob(final String fileDownloadBasePath, Job job) throws FileNotFoundException {
		String fileFolderPath = BlackboxUtils.concat(fileDownloadBasePath, String.valueOf(job.getId()));
		File[] uploadedFile = getAllFileNames(fileFolderPath);
		if (uploadedFile != null) {
			auditManualUploadedFile(job, uploadedFile);
		} else {
			job.setStatus(Job.Status.ERROR);
			jobDAO.updateJobStatus(job);
			LOGGER.info(MessageFormat.format("Job {0} is failed because there is no file to process", job.getId()));
		}
	}

	/**
	 * It will process the file corresponding to each job
	 * @param fileUploadBasePath
	 *            the BasePath for the uploadedFolder
	 * @param job
	 *            job related to file
	 * @return noting
	 */
	private void auditManualUploadedFile(Job job, File[] uploadedFile) throws FileNotFoundException {
		for (File file : uploadedFile) {
			LOGGER.info(MessageFormat.format("file {0} is present to process", file.getName()));
			processEachPrivatePairFile(file, job);
		}
	}

	/**
	 * It will do the Audit process on the Uploaded File and send the success report or corrupted file mail to the use.
	 * @param file
	 *            the File to be processed
	 * @param job
	 *            job associated with the file
	 * @return noting
	 */
	private void processEachPrivatePairFile(final File file, final Job job) throws FileNotFoundException {
		job.setStatus(Job.Status.PROCESSING);
		jobDAO.updateJobStatus(job);
		LOGGER.info(MessageFormat.format("Updating the Job {0} to PROCESSING Status", job.getId()));
		String fileName = file.getName();
		boolean isAnyRecords = false;
		isAnyRecords = privatePairCommonService.processAndValidateXMLRecords(file, job.getCreatedByUser(), fileName);
		if (isAnyRecords) {
			job.setStatus(Job.Status.SUBMITTED);
			jobDAO.updateJobStatus(job);
			LOGGER.info(MessageFormat.format("Updating the Job {0} to SUBMITTED Status", job.getId()));
		} else {
			job.setStatus(Job.Status.ERROR);
			jobDAO.updateJobStatus(job);
			LOGGER.info(MessageFormat.format("Job {0} is failed due to invalid file format", job.getId()));
		}
	}

	/**
	 * This method return all file names in the folder.
	 * @param foldername
	 * @param unZipFilePath
	 * @return array of the file
	 */
	private static File[] getAllFileNames(String folderName) {
		File folder = new File(folderName);
		return folder.listFiles();
	}

}