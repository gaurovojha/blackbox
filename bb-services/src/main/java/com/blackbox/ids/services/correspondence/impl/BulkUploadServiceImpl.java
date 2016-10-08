package com.blackbox.ids.services.correspondence.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dao.correspondence.IJobDAO;
import com.blackbox.ids.core.dao.correspondence.impl.CorrespondenceStageDAOImpl;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.correspondence.Correspondence;
import com.blackbox.ids.core.model.correspondence.Correspondence.Source;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.correspondence.Job;
import com.blackbox.ids.core.repository.DocumentCodeRepository;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceStagingRepository;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.PDFUtil;
import com.blackbox.ids.core.util.UnzipUtility;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.services.common.EmailService;
import com.blackbox.ids.services.correspondence.IBulkUploadService;

@Service
public class BulkUploadServiceImpl implements IBulkUploadService {

	private static final Logger LOGGER = Logger.getLogger(BulkUploadServiceImpl.class);

	@Autowired
	private IJobDAO	jobDao;

	@Autowired
	private CorrespondenceStagingRepository correspondenceStaging;

	/** The document code repository. */
	@Autowired
	private DocumentCodeRepository documentCodeRepository;
	
	@Autowired
	private UserRepository userRepo ;

	/** Correspondence Staging Dao **/
	@Autowired
	private CorrespondenceStageDAOImpl correspondenceStageDao;

	@Autowired
	private EmailService emailService;
	
	public static final String WO_CODE_INDICATOR = "PCT/US";
	
	/** The Constant WO_CODE_INDICATOR_WITHOUT_CC. */
	public static final String WO_CODE_INDICATOR_WITHOUT_CC = "PCT";

	/** The Constant WO_CODE. */
	public static final String WO_CODE = "WO";

	/** The Constant US_CODE. */
	public static final String US_CODE = "US";

	/** The Constant EU_CODE. */
	public static final String EU_CODE = "EU";
	
	@Override
	@Transactional
	public void processJobFiles() {
		LOGGER.debug("Processing job files started");
		List<Job> newBulkUploadJobs = jobDao.findBulkUploadNewEntries();
		String downloadDirectory = FolderRelativePathEnum.CORRESPONDENCE
				.getAbsolutePath("jobs");
		String tmpStoreDirectry= FolderRelativePathEnum.CORRESPONDENCE
				.getAbsolutePath("bulkupload_temp");
		for (Job each : newBulkUploadJobs) {
			try {
				String zipFilePath = downloadDirectory + File.separator + each.getId() + File.separator + each.getId() +".zip";
				// Destination path of unzip file.
				String tempStorePath = tmpStoreDirectry + File.separator + each.getId() + File.separator;
				// This method call unzip all files.
				File check = new File(zipFilePath);
				boolean proceed = (check.exists() && tempStorePath != null) ? true : false;
				if (proceed) {
					deleteAndCreateFolder(tmpStoreDirectry);
					String folderName = getUnzipFile(zipFilePath, tempStorePath);
					// This method call return names of all file in the
					// extracted folder
					File[] listOfFiles = getAllFileNames(folderName);
					// get documentCodeDescription from Document
					List<DocumentCode> documentCodes = documentCodeRepository.findAll();
					Map<String, String> docDescriptionCodeMap = new HashMap<>();
					prepareDocumentAndJursidictionMap(documentCodes, docDescriptionCodeMap);
					processEachFile(folderName, each, listOfFiles, docDescriptionCodeMap);
					deleteAndCreateFolder(tmpStoreDirectry);
				}
			} catch (COSVisitorException exeption) {
				each.setStatus(Job.Status.ERROR);
				jobDao.updateJobStatus(each) ;
				LOGGER.error(exeption);
			} catch (IOException exep) {
				each.setStatus(Job.Status.ERROR);
				jobDao.updateJobStatus(each) ;
				LOGGER.error(exep);
			} catch (CryptographyException exep) {
				each.setStatus(Job.Status.ERROR);
				jobDao.updateJobStatus(each) ;
				LOGGER.error(exep);
			}
		}
	}

	private static void deleteAndCreateFolder(String fileTobDeleted) throws IOException {
		File file = new File(fileTobDeleted);
		if (!file.exists()) {
			file.mkdir();
		} else {
			deleteDir(file) ;
			file.mkdir();
		}

	}
	
	private static  boolean deleteDir(File dir) {
	      if (dir.isDirectory()) {
	         String[] children = dir.list();
	         for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir
	            (new File(dir, children[i]));
	            if (!success) {
	               return false;
	            }
	         }
	      }
	      return dir.delete();
	 }

	/**
	 * This method return all file names in the folder.
	 * 
	 * @param zipFilePath
	 * @param unZipFilePath
	 * @return
	 */
	private static File[] getAllFileNames(String fold) {

		File folder = new File(fold);
		return folder.listFiles();
	}

	/**
	 * This method is used to unzip the file.
	 * 
	 * @param zipFilePath
	 * @param unZipFilePath
	 * @throws IOException 
	 */
	public static String getUnzipFile(String zipFilePath, String unZipFilePath) throws IOException {

		UnzipUtility unzipper = new UnzipUtility();
		String folder = "";
			folder = unzipper.unzip(zipFilePath, unZipFilePath);
		return folder;
	}

	/**
	 * This method return information about the document.
	 * 
	 * @param folderName
	 * @param listOfFiles
	 * @return current status of files
	 * @throws CryptographyException
	 * @throws IOException
	 * @throws COSVisitorException
	 */

	private void processEachFile(String folderName, Job job, File[] listOfFiles,
			Map<String, String> docDescriptionCodeMap) throws IOException, CryptographyException, COSVisitorException {
		job.setStatus(Job.Status.PROCESSING);
		jobDao.updateJobStatus(job);
		List<String> filesList = new ArrayList<>();
		for (File file : listOfFiles) {

			String fileName = file.getName();
			String filePath = folderName +File.separator+ fileName;
			String extension = "pdf"; 
			if (!extension.equalsIgnoreCase(fileName.substring(fileName.lastIndexOf(".") + 1))) {
				LOGGER.info(fileName + " file rejected under job-" + job.getId());
				// reject and send mail
				filesList.add(fileName);
			} else if (PDFUtil.isMultipleBookmark(filePath)) {
				if (PDFUtil.isValidPdfForBookMark(filePath, PDFUtil.BookMarkType.B1)) {
					createPdfWithBookmark(filePath, docDescriptionCodeMap);
				}else{
					// reject and send Email
					filesList.add(fileName);
				}
			}else{
				filesList.add(fileName);
			}
		}
	
		if(!CollectionUtils.isEmpty(filesList)) {
			List<MessageData> messageDatas = createMessageDataForInvalidFiles(job.getEmail() , filesList);
			List<String> recipient = new ArrayList<>() ;
			recipient.add(job.getEmail()) ;
			createAndSaveMessage(TemplateType.BULK_FILE_ERROR, recipient , messageDatas);
		}
		// update the status of record to PROCESSED
		job.setStatus(Job.Status.SUBMITTED);
		jobDao.updateJobStatus(job);
	}
	
	private List<MessageData> createMessageDataForInvalidFiles(String recieverEmail , List<String> filesList) {
		List<MessageData> messageDatas = null;
			messageDatas = new ArrayList<>();
			String firstName = userRepo.getFirstNameByLoginId(recieverEmail);
			if(!CollectionUtils.isEmpty(filesList)) {
				MessageData invalidFile = null;
				for(String fileName : filesList) {
					 invalidFile = new MessageData(com.blackbox.ids.core.constant.Constant.INVALID_FILE,fileName);
					messageDatas.add(invalidFile);
				}
			}
			MessageData receiverData = new MessageData(
					com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_RECEIVER_NAME, firstName);
			messageDatas.add(receiverData);
		return messageDatas;
	}
	
	private void createAndSaveMessage(final TemplateType templateType, List<String> recipients,
			Collection<MessageData> messageDatas) {
		Message message = null;
		if (templateType != null && !CollectionUtils.isEmpty(recipients)) {
			message = new Message();
			message.setTemplateType(templateType);
			StringBuilder strBuilder = new StringBuilder();
			for (String recipient : recipients) {
				strBuilder.append(recipient).append(Constant.SEMI_COLON);
			}
			message.setReceiverList(strBuilder.toString());
			message.setStatus(Constant.MESSAGE_STATUS_NOT_SENT);
			emailService.send(message, messageDatas);
		}
	}

	/**
	 * This method is used to create pdf with book mark.
	 *
	 * @param pdfFile
	 *            the pdf file
	 * @return list of new pdf book mark file names.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws CryptographyException
	 *             the cryptography exception
	 * @throws COSVisitorException
	 *             the COS visitor exception
	 */
	@SuppressWarnings("deprecation")
	private Boolean createPdfWithBookmark(String pdfFile, Map<String, String> docDescriptionCodeMap )
			throws IOException, CryptographyException, COSVisitorException {
		Boolean processed = false;
		int start;
		int end;
		String targetFolder = FolderRelativePathEnum.CORRESPONDENCE
				.getAbsolutePath("staging");
		File file = new File(targetFolder);
		if (!file.exists()) {
			file.mkdirs();
		}
		String extension = ".pdf";
		List<String> bookmarkDescription = PDFUtil.getBookmarksNames(pdfFile);
		List<Integer> pageNumber = PDFUtil.getBookMarkPageNumber(pdfFile);

		PDDocument document = PDDocument.load(pdfFile);
		@SuppressWarnings("unchecked")
		List<PDPage> list = document.getDocumentCatalog().getAllPages();
		List<CorrespondenceStaging> correspondencesToBeUpdated = new ArrayList<>();
		long splitId = correspondenceStageDao.getNextSequence(Constant.CORREPPONDENCE_STAGING_SPLIT_SEQ);
		for (int i = 0; i < pageNumber.size(); i++) {

			start = pageNumber.get(i) - 1;

			if (i == (pageNumber.size() - 1)) {
				end = document.getPageCount();
			} else {
				end = pageNumber.get(i + 1) - 1;
			}
			PDDocument doc = new PDDocument();
			String bookMark = bookmarkDescription.get(i);

			// FOR ADDING BOOKMARKS IN PDF
			PDDocumentOutline outline = new PDDocumentOutline();
			doc.getDocumentCatalog().setDocumentOutline(outline);
			PDOutlineItem pagesOutline = new PDOutlineItem();
			// BOOKMARK NAME
			pagesOutline.setTitle(bookMark);
			outline.appendChild(pagesOutline);

			for (int j = start; j < end; j++) {
				doc.addPage(list.get(j));
			}

			outline.openNode();
			String parentChild = "C" ;
			if(i==0){
				parentChild = "PC" ;
			}
			CorrespondenceStaging inserted = makeEntryToStaging(bookMark, splitId, docDescriptionCodeMap ,parentChild);

			long id = inserted.getId();
			File folder = new File(targetFolder+id) ;
			folder.mkdirs() ;
			// SAVING PATH
			doc.save(targetFolder + id +File.separator+id+ extension);
			LOGGER.info(targetFolder + id + extension+" is saved ");
			updateCorrespondenceAttributes(inserted, doc);
			doc.close();
			correspondencesToBeUpdated.add(inserted);
		}

		document.close();
		correspondenceStaging.save(correspondencesToBeUpdated);
		processed = true;
		return processed;
	}

	private static void updateCorrespondenceAttributes(CorrespondenceStaging inserted, PDDocument doc) {
		Long id = inserted.getId() ;
		String attachmentName = id.toString() + "" + ".pdf";
		int pageCount = doc.getNumberOfPages();
		String stagingFolder = FolderRelativePathEnum.CORRESPONDENCE
				.getAbsolutePath("staging");
		File file = new File(stagingFolder + File.separator + attachmentName);
		long size = file.length();
		inserted.setAttachmentName(attachmentName);
		inserted.setAttachmentSize(size);
		inserted.setPageCount(pageCount);
	}

	private CorrespondenceStaging makeEntryToStaging(String bookMark, long splitId,
			Map<String, String> docDescriptionCodeMap , String parentChild) {
		CorrespondenceStaging correspondenceStage = new CorrespondenceStaging();
		String[] details = bookMark.split(" ");
		String applicationNumber = details[1];
		String customerNumber = details[0];
		String filingDateString = details[2];
		StringBuilder description = new StringBuilder();
		
		for (int i = 3; i < details.length; i++) {
			description.append(details[i]);
		}
		String docCode = docDescriptionCodeMap.get(description);
		String[] test = filingDateString.split("-");
		filingDateString = test[0].trim() + "/" + test[1].trim() + "/" + test[2].trim();
		
		Calendar filingDate = BlackboxDateUtil.strToCalendar(filingDateString, TimestampFormat.MMDDYYYY);
		String jurisdictionCode = applicationNumber.startsWith(WO_CODE_INDICATOR)
				? WO_CODE : US_CODE;
		correspondenceStage.setJurisdictionCode(jurisdictionCode);
		correspondenceStage.setApplicationNumber(BlackboxUtils.formatApplicationNumber(applicationNumber,jurisdictionCode,filingDate));
		correspondenceStage.setCustomerNumber(customerNumber);
		correspondenceStage.setFilingDate(filingDate);
		correspondenceStage.setMailingDate(filingDate);
		correspondenceStage.setSplitId(splitId);
		correspondenceStage.setSource(Source.MANUAL);
		correspondenceStage.setSubSource(Correspondence.SubSourceTypes.BULK);
		correspondenceStage.setDocumentDescription(description.toString());
		correspondenceStage.setDocumentCode(docCode);
		correspondenceStage.setStatus(Status.PENDING);
		correspondenceStage.setCreatedByUser(User.SYSTEM_ID);
		correspondenceStage.setParentChild(parentChild);
		return correspondenceStaging.save(correspondenceStage);
	}

	/**
	 * Prepare document and jursidiction map.
	 *
	 * @param documentCodes
	 *            the document codes
	 * @param documentCodeMap
	 *            the document code map
	 * @param jurisdictionMap
	 *            the jurisdiction map
	 * @param descriptionToCodeMap
	 *            the description to code map
	 */
	private static void prepareDocumentAndJursidictionMap(List<DocumentCode> documentCodes,
			Map<String, String> descriptionToCodeMap) {
		for (DocumentCode code : documentCodes) {
			descriptionToCodeMap.put(code.getDescription(), code.getCode());
		}
	}

}
