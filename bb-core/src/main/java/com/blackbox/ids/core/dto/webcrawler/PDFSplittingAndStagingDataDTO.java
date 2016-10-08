package com.blackbox.ids.core.dto.webcrawler;

import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationCorrespondenceData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.DocumentData;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;

// TODO: Auto-generated Javadoc
/**
 * Class {@code PDFSplittingAndStagingDataDTO} holds the attributes passed from web crawlers to helper services.
 *
 * @author nagarro
 */
public class PDFSplittingAndStagingDataDTO {
	
	/** The pdf path. */
	private String mergedPdfFilePath;
	
	/** The staging data per pdf. */
	private List<CorrespondenceStaging> stagingDataPerMergedPDF;
	
	/** The target folder. */
	private String targetFolder;
	
	/** The bookmark descriptions. */
	private List<String> bookmarkDescriptions;
	
	/** The is correspondence call. */
	private boolean isCorrespondenceCall;
	
	/** The download office action queue. */
	private DownloadOfficeActionQueue downloadOfficeActionQueue;
	
	/** The description to code map. */
	private Map<String, String> descriptionToCodeMap;
	
	/** The book mark text. */
	private String bookMarkText;
	
	/** The correspondence staging. */
	private CorrespondenceStaging correspondenceStaging;
	
	/** The download office action queues. */
	private List<DownloadOfficeActionQueue> downloadOfficeActionQueues;
	
	/** The application correspondence data. */
	private ApplicationCorrespondenceData applicationCorrespondenceData;
	
	/** The document data. */
	private DocumentData documentData;

	/**
	 * Instantiates a new PDF splitting and staging data dto.
	 *
	 * @param mergedPdfFilePath the merged pdf file path
	 * @param stagingDataPerMergedPDF the staging data per merged pdf
	 * @param targetFolder the target folder
	 * @param bookmarkDescriptions the bookmark descriptions
	 * @param isCorrespondenceCall the is correspondence call
	 * @param downloadOfficeActionQueue the download office action queue
	 * @param descriptionToCodeMap the description to code map
	 * @param bookMarkText the book mark text
	 * @param correspondenceStaging the correspondence staging
	 * @param downloadOfficeActionQueues the download office action queues
	 * @param applicationCorrespondenceData the application correspondence data
	 * @param documentData the document data
	 */
	public PDFSplittingAndStagingDataDTO(String mergedPdfFilePath, List<CorrespondenceStaging> stagingDataPerMergedPDF,
			String targetFolder, List<String> bookmarkDescriptions, boolean isCorrespondenceCall,
			DownloadOfficeActionQueue downloadOfficeActionQueue, Map<String, String> descriptionToCodeMap,
			String bookMarkText, CorrespondenceStaging correspondenceStaging,
			List<DownloadOfficeActionQueue> downloadOfficeActionQueues,
			ApplicationCorrespondenceData applicationCorrespondenceData, DocumentData documentData) {
		super();
		this.mergedPdfFilePath = mergedPdfFilePath;
		this.stagingDataPerMergedPDF = stagingDataPerMergedPDF;
		this.targetFolder = targetFolder;
		this.bookmarkDescriptions = bookmarkDescriptions;
		this.isCorrespondenceCall = isCorrespondenceCall;
		this.downloadOfficeActionQueue = downloadOfficeActionQueue;
		this.descriptionToCodeMap = descriptionToCodeMap;
		this.bookMarkText = bookMarkText;
		this.correspondenceStaging = correspondenceStaging;
		this.downloadOfficeActionQueues = downloadOfficeActionQueues;
		this.applicationCorrespondenceData = applicationCorrespondenceData;
		this.documentData = documentData;
	}

	/**
	 * Gets the merged pdf file path.
	 *
	 * @return the merged pdf file path
	 */
	public String getMergedPdfFilePath() {
		return mergedPdfFilePath;
	}

	/**
	 * Sets the merged pdf file path.
	 *
	 * @param mergedPdfFilePath the new merged pdf file path
	 */
	public void setMergedPdfFilePath(String mergedPdfFilePath) {
		this.mergedPdfFilePath = mergedPdfFilePath;
	}

	/**
	 * Gets the staging data per merged pdf.
	 *
	 * @return the staging data per merged pdf
	 */
	public List<CorrespondenceStaging> getStagingDataPerMergedPDF() {
		return stagingDataPerMergedPDF;
	}

	/**
	 * Sets the staging data per merged pdf.
	 *
	 * @param stagingDataPerMergedPDF the new staging data per merged pdf
	 */
	public void setStagingDataPerMergedPDF(List<CorrespondenceStaging> stagingDataPerMergedPDF) {
		this.stagingDataPerMergedPDF = stagingDataPerMergedPDF;
	}

	/**
	 * Gets the target folder.
	 *
	 * @return the target folder
	 */
	public String getTargetFolder() {
		return targetFolder;
	}

	/**
	 * Sets the target folder.
	 *
	 * @param targetFolder the new target folder
	 */
	public void setTargetFolder(String targetFolder) {
		this.targetFolder = targetFolder;
	}

	/**
	 * Gets the bookmark descriptions.
	 *
	 * @return the bookmark descriptions
	 */
	public List<String> getBookmarkDescriptions() {
		return bookmarkDescriptions;
	}

	/**
	 * Sets the bookmark descriptions.
	 *
	 * @param bookmarkDescriptions the new bookmark descriptions
	 */
	public void setBookmarkDescriptions(List<String> bookmarkDescriptions) {
		this.bookmarkDescriptions = bookmarkDescriptions;
	}

	/**
	 * Checks if is correspondence call.
	 *
	 * @return true, if is correspondence call
	 */
	public boolean isCorrespondenceCall() {
		return isCorrespondenceCall;
	}

	/**
	 * Sets the correspondence call.
	 *
	 * @param isCorrespondenceCall the new correspondence call
	 */
	public void setCorrespondenceCall(boolean isCorrespondenceCall) {
		this.isCorrespondenceCall = isCorrespondenceCall;
	}

	/**
	 * Gets the download office action queue.
	 *
	 * @return the download office action queue
	 */
	public DownloadOfficeActionQueue getDownloadOfficeActionQueue() {
		return downloadOfficeActionQueue;
	}

	/**
	 * Sets the download office action queue.
	 *
	 * @param downloadOfficeActionQueue the new download office action queue
	 */
	public void setDownloadOfficeActionQueue(DownloadOfficeActionQueue downloadOfficeActionQueue) {
		this.downloadOfficeActionQueue = downloadOfficeActionQueue;
	}

	/**
	 * Gets the description to code map.
	 *
	 * @return the description to code map
	 */
	public Map<String, String> getDescriptionToCodeMap() {
		return descriptionToCodeMap;
	}

	/**
	 * Sets the description to code map.
	 *
	 * @param descriptionToCodeMap the description to code map
	 */
	public void setDescriptionToCodeMap(Map<String, String> descriptionToCodeMap) {
		this.descriptionToCodeMap = descriptionToCodeMap;
	}

	/**
	 * Gets the book mark text.
	 *
	 * @return the book mark text
	 */
	public String getBookMarkText() {
		return bookMarkText;
	}

	/**
	 * Sets the book mark text.
	 *
	 * @param bookMarkText the new book mark text
	 */
	public void setBookMarkText(String bookMarkText) {
		this.bookMarkText = bookMarkText;
	}

	/**
	 * Gets the correspondence staging.
	 *
	 * @return the correspondence staging
	 */
	public CorrespondenceStaging getCorrespondenceStaging() {
		return correspondenceStaging;
	}

	/**
	 * Sets the correspondence staging.
	 *
	 * @param correspondenceStaging the new correspondence staging
	 */
	public void setCorrespondenceStaging(CorrespondenceStaging correspondenceStaging) {
		this.correspondenceStaging = correspondenceStaging;
	}

	/**
	 * Gets the download office action queues.
	 *
	 * @return the download office action queues
	 */
	public List<DownloadOfficeActionQueue> getDownloadOfficeActionQueues() {
		return downloadOfficeActionQueues;
	}

	/**
	 * Sets the download office action queues.
	 *
	 * @param downloadOfficeActionQueues the new download office action queues
	 */
	public void setDownloadOfficeActionQueues(List<DownloadOfficeActionQueue> downloadOfficeActionQueues) {
		this.downloadOfficeActionQueues = downloadOfficeActionQueues;
	}

	/**
	 * Gets the application correspondence data.
	 *
	 * @return the application correspondence data
	 */
	public ApplicationCorrespondenceData getApplicationCorrespondenceData() {
		return applicationCorrespondenceData;
	}

	/**
	 * Sets the application correspondence data.
	 *
	 * @param applicationCorrespondenceData the new application correspondence data
	 */
	public void setApplicationCorrespondenceData(ApplicationCorrespondenceData applicationCorrespondenceData) {
		this.applicationCorrespondenceData = applicationCorrespondenceData;
	}

	/**
	 * Gets the document data.
	 *
	 * @return the document data
	 */
	public DocumentData getDocumentData() {
		return documentData;
	}

	/**
	 * Sets the document data.
	 *
	 * @param documentData the new document data
	 */
	public void setDocumentData(DocumentData documentData) {
		this.documentData = documentData;
	}


	
}
