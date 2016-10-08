package com.blackbox.ids.webcrawler.service.uspto.correspondence.impl;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationCorrespondenceData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.DocumentData;
import com.blackbox.ids.core.dto.webcrawler.PDFSplittingAndStagingDataDTO;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.correspondence.Correspondence.Source;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.Correspondence.SubSourceTypes;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.model.webcrawler.TrackIDSFilingQueue;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceStagingRepository;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.PDFUtil;
import com.blackbox.ids.core.util.PDFUtil.BookMarkType;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.dto.CorrespondenceCrawlerDTO;
import com.blackbox.ids.services.validation.NumberFormatValidationService;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IPDFSplittingNStagingDataPrepService;

/**
 * The Class PDFSplittingNStagingDataPrepServiceImpl.
 */
@Service
public class PDFSplittingNStagingDataPrepServiceImpl implements IPDFSplittingNStagingDataPrepService{
	
	/** The log. */
	private static Logger log = Logger.getLogger(PDFUtil.class);

	/** The Constant PDF_SUFFIX. */
	private static final String PDF_SUFFIX = ".pdf";

	/** The Constant DATE_FORMAT_1. */
	public static final String DATE_FORMAT_XML = "yyyy-MM-dd";

	/** The Constant DATE_FORMAT_1. */
	public static final String DATE_FORMAT_PDF = "MM-dd-yyyy";

	/** The Constant AUTOMATIC. */
	public static final String AUTOMATIC = "Automatic";

	/** The correspondence staging repository. */
	@Autowired
	private CorrespondenceStagingRepository correspondenceStagingRepository;
	
	/** The number format validation service. */
	@Autowired
	private NumberFormatValidationService numberFormatValidationService;
	

	 /**
    * This API splits the PDF and prepares the staging data . The Steps performed are :-
    *
    * <p><ul>
    * <li> Splits the Merged PDF path on the basis of validated bookmark.
    * <li> The Splitted PDF is renamed to a logical name.
    * <li> The Splitted PDF is copied to common correspondence staging folder.
    * <li> Prepares the Staging data.
    * <li> Populates the <code><b>{@link CorrespondenceStaging}</b></code> entity.
    * <li> Persists the <code><b>{@link CorrespondenceStaging}</b></code> entity to the database.
    * @param pdfSplittingAndStagingDataDTO
    *            the entity containing informatoin for splitting and preparing staging data.
    */
	@Override
	public void splitPDFAndPrepareStagingData(PDFSplittingAndStagingDataDTO pdfSplittingAndStagingDataDTO)
					throws IOException {
		
		String mergedPDFPath = pdfSplittingAndStagingDataDTO.getMergedPdfFilePath();
		List<CorrespondenceStaging> stagingDataPerMergedPDF = pdfSplittingAndStagingDataDTO.getStagingDataPerMergedPDF();
		String targetFolder = pdfSplittingAndStagingDataDTO.getTargetFolder();
		List<String> bookmarkDescriptions = pdfSplittingAndStagingDataDTO.getBookmarkDescriptions();
		boolean isCorrespondenceCall = pdfSplittingAndStagingDataDTO.isCorrespondenceCall();
		DownloadOfficeActionQueue downloadOfficeActionQueue = pdfSplittingAndStagingDataDTO.getDownloadOfficeActionQueue();
		Map<String, String> descriptionToCodeMap = pdfSplittingAndStagingDataDTO.getDescriptionToCodeMap();
		String macthingBookmarkText = pdfSplittingAndStagingDataDTO.getBookMarkText();
		CorrespondenceStaging correspondenceStaging = pdfSplittingAndStagingDataDTO.getCorrespondenceStaging();
		List<DownloadOfficeActionQueue> downloadOfficeActionQueues = pdfSplittingAndStagingDataDTO.getDownloadOfficeActionQueues();
		ApplicationCorrespondenceData applicationCorrespondenceData = pdfSplittingAndStagingDataDTO.getApplicationCorrespondenceData();
		DocumentData documentData = pdfSplittingAndStagingDataDTO.getDocumentData();
		List<Integer> pageNumber;
		PDDocument document = PDDocument.load(mergedPDFPath);
		PDDocument splittedPDF = null;
		String bookMarkText = BBWebCrawerConstants.EMPTY_STRING;
		
		try {
			pageNumber = PDFUtil.getBookMarkPageNumber(mergedPDFPath);
		
			@SuppressWarnings("unchecked")
			List<PDPage> list = document.getDocumentCatalog().getAllPages();

			for (int pageNumberIndex = 0; pageNumberIndex < pageNumber.size(); pageNumberIndex++) {
				CorrespondenceCrawlerDTO correspondenceCrawlerDTO = null;
				splittedPDF = new PDDocument();
				bookMarkText = bookmarkDescriptions.get(pageNumberIndex);
				if(isCorrespondenceCall) {
					correspondenceCrawlerDTO = new CorrespondenceCrawlerDTO(bookMarkText, list, pageNumberIndex,downloadOfficeActionQueues,applicationCorrespondenceData,documentData);
					processOutgoingCorrespondenceCall(targetFolder, macthingBookmarkText, correspondenceStaging,
							pageNumber, document, splittedPDF,correspondenceCrawlerDTO);
				} else {
					correspondenceCrawlerDTO = new CorrespondenceCrawlerDTO(bookMarkText, list, pageNumberIndex,descriptionToCodeMap,pageNumber,downloadOfficeActionQueue);
					processIFWCall(mergedPDFPath, stagingDataPerMergedPDF, targetFolder,
							document,splittedPDF,correspondenceCrawlerDTO);
				}
		}
		  } catch (CryptographyException e1) {
			throw new ApplicationException(1, "Exception occured while getting bookmark page number.", e1);
		} catch (COSVisitorException e) {
			throw new ApplicationException(1, "Exception occured while saving pdf.", e);
		} finally {
			if (splittedPDF != null)
				splittedPDF.close();
			if (document != null)
				document.close();
		}
	}
	
	@Override
	public void splitIDSDocumentAndPrepareStagingData(TrackIDSFilingQueue ids,
			List<CorrespondenceStaging> correspondenceStageList, String pdfPath, String targetFolder, final String idsDocumentDescription)
			throws IOException {
		List<Integer> pageNumber;
		PDDocument splittedPDF = null;
		String bookMarkText = BBWebCrawerConstants.EMPTY_STRING;
		PDDocument document = PDDocument.load(pdfPath);
		List<String> bookmarkDescriptions = PDFUtil.getBookmarksNames(pdfPath);

		try {
			pageNumber = PDFUtil.getBookMarkPageNumber(pdfPath);

			@SuppressWarnings("unchecked")
			List<PDPage> list = document.getDocumentCatalog().getAllPages();

			for (int pageNumberIndex = 0; pageNumberIndex < pageNumber.size(); pageNumberIndex++) {
				splittedPDF = new PDDocument();
				bookMarkText = bookmarkDescriptions.get(pageNumberIndex);

				processBookMark(pageNumber, document, list, pageNumberIndex, splittedPDF, bookMarkText);

				// Preparing splitted pdf file path.
				String splittedPDFPath = getSplittedFileCompletePath(targetFolder, bookMarkText);

				log.info("Name of Splitted PDF to be created is : " + splittedPDFPath);

				// Saving the splitted PDF.

				splittedPDF.save(splittedPDFPath);
				log.info("Splitted PDF Created is " + splittedPDFPath);

				int pageCountInSplittedPDF = PDFUtil.getPDFPageCount(splittedPDFPath);

				CorrespondenceStaging correspondenceStaging = populateStagingEntity(ids, bookMarkText, idsDocumentDescription);

				correspondenceStaging = getNewAttachmentNameNUpdateStagingData(splittedPDFPath, correspondenceStaging,
						pageCountInSplittedPDF);

				correspondenceStageList.add(correspondenceStaging);
			}
		} catch (CryptographyException e1) {
			throw new ApplicationException(1, "Exception occured while getting bookmark page number.", e1);
		} catch (COSVisitorException e) {
			throw new ApplicationException(1, "Exception occured while saving pdf.", e);
		} finally {
			if (splittedPDF != null)
				splittedPDF.close();
			if (document != null)
				document.close();
		}
	}

	/**
	 * Process ifw call.
	 *
	 * @param mergedPDFPath the merged pdf path
	 * @param stagingDataPerMergedPDF the staging data per merged pdf
	 * @param targetFolder the target folder
	 * @param pdfPathToDownloadQueueData the pdf path to download queue data
	 * @param descriptionToCodeMap the description to code map
	 * @param pageNumber the page number
	 * @param document the document
	 * @param splittedPDF the splitted pdf
	 * @param bookMarkText the book mark text
	 * @param list the list
	 * @param pageNumberIndex the i
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws COSVisitorException the COS visitor exception
	 * @throws CryptographyException the cryptography exception
	 */
	private void processIFWCall(String mergedPDFPath, List<CorrespondenceStaging> stagingDataPerMergedPDF,
			String targetFolder,PDDocument document,
			PDDocument splittedPDF, CorrespondenceCrawlerDTO correspondenceCrawlerDTO)
					throws IOException, COSVisitorException, CryptographyException {
		String splittedPDFPath;
		
		DownloadOfficeActionQueue downloadOfficeActionQueue = correspondenceCrawlerDTO.getDownloadOfficeActionQueue();
		Map<String, String> descriptionToCodeMap = correspondenceCrawlerDTO.getDescriptionToCodeMap();
		List<Integer> pageNumber = correspondenceCrawlerDTO.getPageNumber();
		String bookMarkText = correspondenceCrawlerDTO.getBookMarkText();
		List<PDPage> list = correspondenceCrawlerDTO.getList();
		int pageNumberIndex = correspondenceCrawlerDTO.getPageNumberIndex();
		
		processBookMark(pageNumber, document, list, pageNumberIndex, splittedPDF,bookMarkText);
		
		// Preparing splitted pdf file path.

		splittedPDFPath = getSplittedFileCompletePath(targetFolder, bookMarkText);

		log.info("Name of Splitted PDF to be created is : " + splittedPDFPath);
		
		// Saving the splitted PDF.
		
		splittedPDF.save(splittedPDFPath);
		log.info("Splitted PDF Created is " + splittedPDFPath);
		populateStagingEntityNRenameNCopyFileForIFW(stagingDataPerMergedPDF, downloadOfficeActionQueue, descriptionToCodeMap,
				splittedPDFPath,  bookMarkText);
	}

	/**
	 * Process outgoing correspondence call.
	 *
	 * @param targetFolder the target folder
	 * @param matchingBookmarkText the macthing bookmark text
	 * @param correspondenceStaging the correspondence staging
	 * @param pageNumber the page number
	 * @param document the document
	 * @param splittedPDF the splitted pdf
	 * @param bookMarkText the book mark text
	 * @param list the list
	 * @param i the i
	 * @param documentData 
	 * @param applicationCorrespondenceData 
	 * @param downloadOfficeActionQueues 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws COSVisitorException the COS visitor exception
	 */
	private void processOutgoingCorrespondenceCall(String targetFolder, String matchingBookmarkText,
			CorrespondenceStaging correspondenceStaging, List<Integer> pageNumber, PDDocument document,
			PDDocument splittedPDF,CorrespondenceCrawlerDTO correspondenceCrawlerDTO)
					throws IOException, COSVisitorException {
		String splittedPDFPath;
		String bookMarkText = correspondenceCrawlerDTO.getBookMarkText(); 
		
		List<PDPage> list = correspondenceCrawlerDTO.getList();
		int i = correspondenceCrawlerDTO.getPageNumberIndex(); 
		List<DownloadOfficeActionQueue> downloadOfficeActionQueues = correspondenceCrawlerDTO.getDownloadOfficeActionQueues();
		ApplicationCorrespondenceData applicationCorrespondenceData = correspondenceCrawlerDTO.getApplicationCorrespondenceData();
		DocumentData documentData = correspondenceCrawlerDTO.getDocumentData();
		
		if (bookMarkText.equalsIgnoreCase(matchingBookmarkText)) {
			
			log.info("Keys of Bookmark and xml match .Splitting the PDF for bookmark :: "+matchingBookmarkText);
			processBookMark(pageNumber, document, list, i, splittedPDF, bookMarkText);

			// Preparing splitted pdf file path.

			splittedPDFPath = getSplittedFileCompletePath(targetFolder, bookMarkText);

			log.info("Name of Splitted PDF to be created is : " + splittedPDFPath);

			// Saving the splitted PDF.

			splittedPDF.save(splittedPDFPath);
			log.info("Splitted PDF Created is " + splittedPDFPath);

			int pageCountInSplittedPDF = PDFUtil.getPDFPageCount(splittedPDFPath);
			int pageCountInXML = Integer.parseInt(documentData.getPageQuantity());
			
			if(pageCountInXML == pageCountInSplittedPDF) {
			getNewAttachmentNameNUpdateStagingData(splittedPDFPath, correspondenceStaging, pageCountInSplittedPDF);
			} else {
				log.info("PDF count in XML and Splitted PDF dont match. Moving to download office action queue ");
				
				//Move to Download Office Action Queue.
				prepareDownloadOfficeActionQueue(downloadOfficeActionQueues,
						applicationCorrespondenceData, documentData);
			}
		}
	}

	/**
	 * Prepare download office action queue.
	 *
	 * @param downloadOfficeActionQueues
	 *            the download office action queues
	 * @param xmlKey
	 *            the xml key
	 * @param applicationCorrespondenceData
	 *            the application correspondence data
	 * @param documentData
	 *            the document data
	 */
	private void prepareDownloadOfficeActionQueue(List<DownloadOfficeActionQueue> downloadOfficeActionQueues,
			ApplicationCorrespondenceData applicationCorrespondenceData, DocumentData documentData) {
		DownloadOfficeActionQueue downloadOfficeActionQueue = new DownloadOfficeActionQueue();
		Calendar filingDate = BlackboxDateUtil.toCalendar(applicationCorrespondenceData.getFilingDate(), TimestampFormat.yyyyMMdd);
		String usptoRawApplicationNumber = applicationCorrespondenceData.getApplicationNumber();
		boolean isWOApplication = usptoRawApplicationNumber.startsWith(BBWebCrawerConstants.WO_CODE_INDICATOR)
				? true : false;
		String jurisdictionCode = isWOApplication
				? BBWebCrawerConstants.WO_CODE : BBWebCrawerConstants.US_CODE;
		
		String formattedApplicationNumber = numberFormatValidationService.getConvertedValue(usptoRawApplicationNumber, NumberType.APPLICATION,
				jurisdictionCode, isWOApplication ? ApplicationType.PCT_US_SUBSEQUENT_FILING : ApplicationType.ALL,
						filingDate, null);
		downloadOfficeActionQueue.setApplicationNumberFormatted(formattedApplicationNumber);
		downloadOfficeActionQueue.setAppNumberRaw(usptoRawApplicationNumber);
		downloadOfficeActionQueue.setDocumentCode(documentData.getFileWrapperDocumentCode());
		downloadOfficeActionQueue.setPageCount(Integer.valueOf(documentData.getPageQuantity()));
		downloadOfficeActionQueue.setRetryCount(1);
		downloadOfficeActionQueue.setCustomerNumber(applicationCorrespondenceData.getCustomerNumber());
		downloadOfficeActionQueue.setJurisdictionCode(jurisdictionCode);
		downloadOfficeActionQueue.setMailingDate(BlackboxDateUtil.strToCalendar(documentData.getMailRoomDate(),TimestampFormat.yyyyMMdd));
		downloadOfficeActionQueue.setFilingDate(filingDate);
		downloadOfficeActionQueue.setStatus(QueueStatus.INITIATED);
		downloadOfficeActionQueue.setCreatedByUser(User.SYSTEM_ID);
		downloadOfficeActionQueue.setCreatedDate(Calendar.getInstance());
		
		downloadOfficeActionQueues.add(downloadOfficeActionQueue);
	}
	/**
	 * Gets the splitted file complete path.
	 *
	 * @param targetFolder the target folder
	 * @param bookMarkText the book mark text
	 * @return the splitted file complete path
	 */
	private String getSplittedFileCompletePath(String targetFolder, String bookMarkText) {
		String completeFilePath;
		completeFilePath = BlackboxUtils.concat(targetFolder, bookMarkText.replaceAll(BBWebCrawerConstants.FRONT_SLASH, BBWebCrawerConstants.EMPTY_STRING), PDF_SUFFIX);
		return completeFilePath;
	}


	/**
	 * Gets the new attachment name n populate staging data.
	 *
	 * @param splittedPDFPath the splitted pdf path
	 * @param correspondenceStaging the correspondence staging
	 * @param pageCountInSplittedPDF the page count in splitted pdf
	 * @return the new attachment name n populate staging data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private CorrespondenceStaging getNewAttachmentNameNUpdateStagingData(String splittedPDFPath,
			CorrespondenceStaging correspondenceStaging, int pageCountInSplittedPDF) throws IOException {
		correspondenceStaging = correspondenceStagingRepository.save(correspondenceStaging);
		Long id = correspondenceStaging.getId();
		log.debug("Correspondence Staging generated is "+id);

		String correspondenceStagingRelativePath = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("staging");
		File correspondenceStagingPathFile = new File(correspondenceStagingRelativePath);
		
		if(!correspondenceStagingPathFile.exists()) {
			correspondenceStagingPathFile.mkdirs();
		}
		
		correspondenceStaging.setAttachmentSize(new File(splittedPDFPath).length());
		String newSplittedPDFName = renameAndCopyFileToStagingLocation(splittedPDFPath, id, correspondenceStagingRelativePath,
				correspondenceStagingPathFile);
		
		correspondenceStaging.setAttachmentName(newSplittedPDFName);
		correspondenceStaging.setPageCount(pageCountInSplittedPDF);
		return correspondenceStaging;
	}

	/**
	 * Prepare staging data with attachment name.
	 * @param stagingDataPerPDF the staging data per pdf
	 * @param descriptionToCodeMap the description to code map
	 * @param splittedPDFPath the final path
	 * @param bookMarkText 
	 * @param pdfPathToDownloadQueueData the pdf path to download queue data
	 * @param actualDocumentDescriptionWithSpace the actual document description with space
	 * @param finalPath 
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CryptographyException 
	 */
	private void populateStagingEntityNRenameNCopyFileForIFW(List<CorrespondenceStaging> stagingDataPerPDF, DownloadOfficeActionQueue downloadOfficeActionQueue,
			Map<String, String> descriptionToCodeMap, String splittedPDFPath,
			String bookMarkText) throws IOException, CryptographyException {
		int pageCountInSplittedPDF = PDFUtil.getPDFPageCount(splittedPDFPath);
		
		CorrespondenceStaging correspondenceStaging = populateStagingEntity(downloadOfficeActionQueue,descriptionToCodeMap,bookMarkText);
		
		correspondenceStaging = getNewAttachmentNameNUpdateStagingData(splittedPDFPath, correspondenceStaging, pageCountInSplittedPDF);
		
		stagingDataPerPDF.add(correspondenceStaging);
	}
	/**
	 * Process book mark.
	 *
	 * @param bookmarkDescriptions the bookmark descriptions
	 * @param pageNumber the page number
	 * @param document the document
	 * @param list the list
	 * @param pageNumberIndex the i
	 * @param doc the doc
	 * @param bookMarkText 
	 * @return the string
	 */
	private String processBookMark(List<Integer> pageNumber, PDDocument document,
			List<PDPage> list, int pageNumberIndex, PDDocument doc, String bookMarkText) {
		int start;
		int end;
		start = pageNumber.get(pageNumberIndex) - 1;

		if (pageNumberIndex == (pageNumber.size() - 1))
			end = document.getPageCount();
		else
			end = pageNumber.get(pageNumberIndex + 1) - 1;


		

		// FOR ADDING BOOKMARKS IN PDF
		PDDocumentOutline outline = new PDDocumentOutline();
		doc.getDocumentCatalog().setDocumentOutline(outline);
		PDOutlineItem pagesOutline = new PDOutlineItem();
		// BOOKMARK NAME
		pagesOutline.setTitle(bookMarkText);
		outline.appendChild(pagesOutline);

		for (int j = start; j < end; j++) {
			doc.addPage(list.get(j));
		}

		outline.openNode();
		return bookMarkText;
	}


	

	/**
	 * Rename and copy file to staging location.
	 *
	 * @param sourceFileFinalPath the source file final path
	 * @param id the id
	 * @param correspondenceStagingPath the correspondence base path
	 * @param correspondenceStagingPathFile the correspondence base path file
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static String renameAndCopyFileToStagingLocation(String sourceFileFinalPath, Long id,
			String correspondenceStagingPath, File correspondenceStagingPathFile) throws IOException {
		String crawlerDirectoryPath = new File(sourceFileFinalPath).getParentFile().getAbsolutePath();
		String newAttachmentName = BlackboxUtils.concat(id.toString(),PDF_SUFFIX);
		String newAttachmentNameInCrawlerFolder = BlackboxUtils.concat(crawlerDirectoryPath,File.separator,newAttachmentName);
		File crawlerFilePathWithOldName = new File(sourceFileFinalPath); 
		File crawlerFilePathWithNewName = new File(newAttachmentNameInCrawlerFolder);
		
		log.info(MessageFormat.format("Renaming file {0} to {1}",sourceFileFinalPath,newAttachmentNameInCrawlerFolder));
		
		crawlerFilePathWithOldName.renameTo(new File(newAttachmentNameInCrawlerFolder));
		
		log.info(MessageFormat.format("Renaming file {0} to {1} succesfull",sourceFileFinalPath,newAttachmentNameInCrawlerFolder));
		
		log.info(MessageFormat.format("Copying file {0} to {1}",newAttachmentNameInCrawlerFolder,correspondenceStagingPath));
		
		FileUtils.copyFileToDirectory(crawlerFilePathWithNewName,correspondenceStagingPathFile);
		
		log.info(MessageFormat.format("Copying file {0} to {1} succesfull",newAttachmentNameInCrawlerFolder,correspondenceStagingPath));
		
		if(crawlerFilePathWithNewName.delete()) {
			log.info(MessageFormat.format("File {0} deleted succesfully", newAttachmentNameInCrawlerFolder));
		} else {
			log.info(MessageFormat.format("File {0} deletion failed", newAttachmentNameInCrawlerFolder));
		}
		
		return newAttachmentName;
	}

	/**
	 * Populate staging entity.
	 * @param downloadOfficeActionQueue 
	 *
	 * @param descriptionToCodeMap the description to code map
	 * @param pageNumber the page number
	 * @param actualDocumentDescriptionWithSpace the actual document description with space
	 * @param sourceFileFinalPath the source file final path
	 * @param applicationNumberInQueue the application number in queue
	 * @param customerNumber the customer number
	 * @param jurisdictionCode the jurisdiction code
	 * @param bookMarkText 
	 * @return the correspondence staging
	 */
	private CorrespondenceStaging populateStagingEntity(DownloadOfficeActionQueue downloadOfficeActionQueue, Map<String, String> descriptionToCodeMap,String bookMarkText) {
		
		String rawApplicationNumber = downloadOfficeActionQueue.getAppNumberRaw();
		String customerNumber = downloadOfficeActionQueue.getCustomerNumber();
		String jurisdictionCode = downloadOfficeActionQueue.getJurisdictionCode();
		Calendar filingDate = downloadOfficeActionQueue.getFilingDate();
		String documentDescription = BBWebCrawerConstants.EMPTY_STRING;
		String mailingDate = BBWebCrawerConstants.EMPTY_STRING;
		
		CorrespondenceStaging correspondenceStaging = new CorrespondenceStaging();
		BookMarkType bookMarkType = PDFUtil.getBookMarkType(bookMarkText);
		
		if(bookMarkType == BookMarkType.B1) {
			mailingDate = bookMarkText.split(BBWebCrawerConstants.SPACE_STRING)[2];
			String documentDescriptionComplete = bookMarkText.split(BBWebCrawerConstants.SPACE_STRING)[3];
			int indexStart = bookMarkText.indexOf(documentDescriptionComplete);
			documentDescription = bookMarkText.substring(indexStart, bookMarkText.length());
		} else if(bookMarkType == BookMarkType.B2) {
			mailingDate = bookMarkText.split(BBWebCrawerConstants.SPACE_STRING)[0];
			documentDescription = bookMarkText.substring(bookMarkText.indexOf(BBWebCrawerConstants.SPACE_STRING) + 1, bookMarkText.length());
		} else {
			log.info(MessageFormat.format("BookMark text {0} is invalid , as it does not belong to any category", bookMarkText));
		}
		Calendar mailingDateCal = BlackboxDateUtil.toCalendar(mailingDate, TimestampFormat.yyyyMMdd);
		
		correspondenceStaging.setApplicationNumber(rawApplicationNumber);
		correspondenceStaging.setApplicationNumberUnFormatted(rawApplicationNumber);
		correspondenceStaging.setCustomerNumber(customerNumber);
		correspondenceStaging.setJurisdictionCode(jurisdictionCode);
		correspondenceStaging.setFilingDate(filingDate);
		correspondenceStaging.setSource(Source.AUTOMATIC);
		correspondenceStaging.setSubSource(SubSourceTypes.IFW);
		correspondenceStaging.setCreatedByUser(User.SYSTEM_ID);
		
		correspondenceStaging.setDocumentCode(descriptionToCodeMap.get(documentDescription));
		correspondenceStaging.setDocumentDescription(documentDescription);
		correspondenceStaging.setMailingDate(mailingDateCal);
		return correspondenceStaging;
	}
	
	/**
	 * Creates and populate <code>CorrespondenceStaging</code> object by IDS tracking queue record.
	 * @param ids
	 *            - IDS tracking queue record
	 * @param bookMarkText
	 *            - bookmark text
	 * @return new <code>CorrespondenceStaging</code> object
	 */
	private static CorrespondenceStaging populateStagingEntity(final TrackIDSFilingQueue ids,
			final String bookMarkText, final String idsDocumentDescription) {
		CorrespondenceStaging correspondenceStaging = new CorrespondenceStaging();
		BookMarkType bookMarkType = PDFUtil.getBookMarkType(bookMarkText);

		String mailingDate = BBWebCrawerConstants.EMPTY_STRING;
		if (bookMarkType == BookMarkType.B1) {
			mailingDate = bookMarkText.split(BBWebCrawerConstants.SPACE_STRING)[2];
		} else if (bookMarkType == BookMarkType.B2) {
			mailingDate = bookMarkText.split(BBWebCrawerConstants.SPACE_STRING)[0];
		} else {
			log.error(MessageFormat.format("BookMark text {0} is invalid , as it does not belong to any category",
					bookMarkText));
		}
		Calendar mailingDateCal = null;
		if (StringUtils.isNotEmpty(mailingDate)) {
			BlackboxDateUtil.toCalendar(mailingDate, TimestampFormat.yyyyMMdd);
		}

		correspondenceStaging.setApplicationNumber(ids.getApplicationNumberFormatted());
		correspondenceStaging.setApplicationNumberUnFormatted(ids.getAppNumberRaw());
		correspondenceStaging.setCustomerNumber(ids.getCustomerNumber());
		correspondenceStaging.setJurisdictionCode(ids.getJurisdictionCode());
		correspondenceStaging.setFilingDate(ids.getFilingDate());
		correspondenceStaging.setSource(Source.AUTOMATIC);
		correspondenceStaging.setSubSource(SubSourceTypes.IFW);
		correspondenceStaging.setCreatedByUser(User.SYSTEM_ID);
		correspondenceStaging.setStatus(Status.PENDING);
		correspondenceStaging.setDocumentCode(BBWebCrawerConstants.IDS_DOCUMENT_CODE);
		correspondenceStaging.setDocumentDescription(idsDocumentDescription);
		correspondenceStaging.setMailingDate(mailingDateCal);
		return correspondenceStaging;
	}

}
