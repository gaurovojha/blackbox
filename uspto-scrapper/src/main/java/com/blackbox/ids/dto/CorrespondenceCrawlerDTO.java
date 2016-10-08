package com.blackbox.ids.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDPage;

import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationCorrespondenceData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.DocumentData;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.OutgoingCorrespondence;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class CorrespondenceCrawlerDTO.
 */
public class CorrespondenceCrawlerDTO {

	/** The customers in bbx. */
	private List<String> customersInBBX;
	
	/** The inclusion list. */
	private List<String> inclusionList;
	
	/** The exclusion list. */
	private List<String> exclusionList;
	
	/** The correspondence count in xml. */
	private int correspondenceCountInXML;
	
	/** The outgoing correspondence. */
	private OutgoingCorrespondence outgoingCorrespondence;
	
	/** The total records. */
	private int totalRecords;
	
	/** The is inclusion list on. */
	private boolean isInclusionListOn;
	
	/** The crawler id. */
	private Long crawlerId;
	
	/** The app number to mailing date and doc map. */
	private Map<String, Map<String, Set<String>>> appNumberToMailingDateAndDocMap;
	
	/** The is us application. */
	private boolean isUSApplication;
	
	/** The converted application number. */
	private String convertedApplicationNumber;
	
	/** The bbx application numbers. */
	private List<String> bbxApplicationNumbers;
	
	/** The is valid appplication number. */
	private boolean isValidAppplicationNumber;
	
	/** The customer number to download queues. */
	private Map<String, List<DownloadOfficeActionQueue>> customerNumberToDownloadQueues;
	
	/** The app number to doc and mailing date map. */
	private Map<String, Map<String, Set<String>>> appNumberToDocAndMailingDateMap;
	
	/** The jurisdiction map. */
	private Map<String, Jurisdiction> jurisdictionMap;
	
	/** The description to code map. */
	private Map<String, String> descriptionToCodeMap;
	
	/** The customer to app numbers in bbx map. */
	private Map<String, List<String>> customerToAppNumbersInBBXMap;
	
	/** The max record retry count. */
	private Integer maxRecordRetryCount; 
	
	/** The book mark text. */
	private String bookMarkText;
	
	/** The list. */
	private List<PDPage> list;
	
	/** The page number index. */
	private int pageNumberIndex;
	
	/** The download office action queues. */
	private List<DownloadOfficeActionQueue> downloadOfficeActionQueues;
	
	/** The application correspondence data. */
	private ApplicationCorrespondenceData applicationCorrespondenceData;
	
	/** The document data. */
	private DocumentData documentData;
	
	/** The page number. */
	private List<Integer> pageNumber;
	
	/** The download office action queue. */
	private DownloadOfficeActionQueue downloadOfficeActionQueue;
	
	/** The pdf names. */
	private List<String> pdfNames;
	
	/** The document codes. */
	private List<DocumentCode> documentCodes;


	/**
	 * Instantiates a new correspondence crawler dto.
	 * @param isUSApplication the is us application
	 * @param convertedApplicationNumber the converted application number
	 * @param isValidAppplicationNumber the is valid application number
	 * @param documentCodes the document codes in the system
	 */
	public CorrespondenceCrawlerDTO(boolean isUSApplication,
			String convertedApplicationNumber, boolean isValidAppplicationNumber, List<DocumentCode> documentCodes)
	{
		super();
		this.isUSApplication = isUSApplication;
		this.convertedApplicationNumber = convertedApplicationNumber;
		this.isValidAppplicationNumber = isValidAppplicationNumber;
		this.documentCodes = documentCodes;
	}

	/**
	 * Instantiates a new correspondence crawler dto.
	 *
	 * @param customerNumberToDownloadQueues the customer number to download queues
	 * @param jurisdictionMap the jurisdiction map
	 * @param descriptionToCodeMap the description to code map
	 * @param customerToAppNumbersInBBXMap the customer to app numbers in bbx map
	 * @param appNumberToMailingDateAndDocMap the app number to mailing date and doc map2
	 */
	public CorrespondenceCrawlerDTO(Map<String, List<DownloadOfficeActionQueue>> customerNumberToDownloadQueues,
			Map<String, Jurisdiction> jurisdictionMap,
			Map<String, String> descriptionToCodeMap, Map<String, List<String>> customerToAppNumbersInBBXMap,
			Map<String, Map<String, Set<String>>> appNumberToMailingDateAndDocMap)
	{
		this.customerNumberToDownloadQueues = customerNumberToDownloadQueues;
		this.jurisdictionMap = jurisdictionMap;
		this.descriptionToCodeMap = descriptionToCodeMap;
		this.customerToAppNumbersInBBXMap = customerToAppNumbersInBBXMap;
		this.appNumberToMailingDateAndDocMap = appNumberToMailingDateAndDocMap;
	}


	/**
	 * Instantiates a new correspondence crawler dto.
	 */
	public CorrespondenceCrawlerDTO()
	{
		super();
	}

	/**
	 * Instantiates a new correspondence crawler dto.
	 *
	 * @param bookMarkText the book mark text
	 * @param list the list
	 * @param pageNumberIndex the page number index
	 * @param downloadOfficeActionQueues the download office action queues
	 * @param applicationCorrespondenceData the application correspondence data
	 * @param documentData the document data
	 */
	public CorrespondenceCrawlerDTO(String bookMarkText, List<PDPage> list, int pageNumberIndex,
			List<DownloadOfficeActionQueue> downloadOfficeActionQueues,
			ApplicationCorrespondenceData applicationCorrespondenceData, DocumentData documentData)
	{
		super();
		this.bookMarkText = bookMarkText;
		this.list = list;
		this.pageNumberIndex = pageNumberIndex;
		this.downloadOfficeActionQueues = downloadOfficeActionQueues;
		this.applicationCorrespondenceData = applicationCorrespondenceData;
		this.documentData = documentData;
	}

	/**
	 * Instantiates a new correspondence crawler dto.
	 *
	 * @param bookMarkText the book mark text
	 * @param list the list
	 * @param pageNumberIndex the page number index
	 * @param descriptionToCodeMap the description to code map
	 * @param pageNumber the page number
	 * @param downloadOfficeActionQueue the download office action queue
	 */
	public CorrespondenceCrawlerDTO(String bookMarkText, List<PDPage> list, int pageNumberIndex,
			Map<String, String> descriptionToCodeMap, List<Integer> pageNumber,
			DownloadOfficeActionQueue downloadOfficeActionQueue)
	{
		this.bookMarkText = bookMarkText;
		this.list = list;
		this.pageNumberIndex = pageNumberIndex;
		this.descriptionToCodeMap = descriptionToCodeMap;
		this.pageNumber = pageNumber;
		this.downloadOfficeActionQueue = downloadOfficeActionQueue;
	}

	/**
	 * Gets the customers in bbx.
	 *
	 * @return the customers in bbx
	 */
	public List<String> getCustomersInBBX() {
		return customersInBBX;
	}

	/**
	 * Sets the customers in bbx.
	 *
	 * @param customersInBBX the new customers in bbx
	 */
	public void setCustomersInBBX(List<String> customersInBBX) {
		this.customersInBBX = customersInBBX;
	}

	/**
	 * Gets the inclusion list.
	 *
	 * @return the inclusion list
	 */
	public List<String> getInclusionList() {
		return inclusionList;
	}

	/**
	 * Sets the inclusion list.
	 *
	 * @param inclusionList the new inclusion list
	 */
	public void setInclusionList(List<String> inclusionList) {
		this.inclusionList = inclusionList;
	}

	/**
	 * Gets the exclusion list.
	 *
	 * @return the exclusion list
	 */
	public List<String> getExclusionList() {
		return exclusionList;
	}

	/**
	 * Sets the exclusion list.
	 *
	 * @param exclusionList the new exclusion list
	 */
	public void setExclusionList(List<String> exclusionList) {
		this.exclusionList = exclusionList;
	}

	/**
	 * Gets the correspondence count in xml.
	 *
	 * @return the correspondence count in xml
	 */
	public int getCorrespondenceCountInXML() {
		return correspondenceCountInXML;
	}

	/**
	 * Sets the correspondence count in xml.
	 *
	 * @param correspondenceCountInXML the new correspondence count in xml
	 */
	public void setCorrespondenceCountInXML(int correspondenceCountInXML) {
		this.correspondenceCountInXML = correspondenceCountInXML;
	}

	/**
	 * Gets the outgoing correspondence.
	 *
	 * @return the outgoing correspondence
	 */
	public OutgoingCorrespondence getOutgoingCorrespondence() {
		return outgoingCorrespondence;
	}

	/**
	 * Sets the outgoing correspondence.
	 *
	 * @param outgoingCorrespondence the new outgoing correspondence
	 */
	public void setOutgoingCorrespondence(OutgoingCorrespondence outgoingCorrespondence) {
		this.outgoingCorrespondence = outgoingCorrespondence;
	}

	/**
	 * Gets the total records.
	 *
	 * @return the total records
	 */
	public int getTotalRecords() {
		return totalRecords;
	}

	/**
	 * Sets the total records.
	 *
	 * @param totalRecords the new total records
	 */
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	/**
	 * Checks if is inclusion list on.
	 *
	 * @return true, if is inclusion list on
	 */
	public boolean isInclusionListOn() {
		return isInclusionListOn;
	}

	/**
	 * Sets the inclusion list on.
	 *
	 * @param isInclusionListOn the new inclusion list on
	 */
	public void setInclusionListOn(boolean isInclusionListOn) {
		this.isInclusionListOn = isInclusionListOn;
	}

	/**
	 * Gets the crawler id.
	 *
	 * @return the crawler id
	 */
	public Long getCrawlerId() {
		return crawlerId;
	}

	/**
	 * Sets the crawler id.
	 *
	 * @param crawlerId the new crawler id
	 */
	public void setCrawlerId(Long crawlerId) {
		this.crawlerId = crawlerId;
	}

	/**
	 * Gets the app number to mailing date and doc map.
	 *
	 * @return the app number to mailing date and doc map
	 */
	public Map<String, Map<String, Set<String>>> getAppNumberToMailingDateAndDocMap() {
		return appNumberToMailingDateAndDocMap;
	}

	/**
	 * Sets the app number to mailing date and doc map.
	 *
	 * @param appNumberToMailingDateAndDocMap the app number to mailing date and doc map
	 */
	public void setAppNumberToMailingDateAndDocMap(Map<String, Map<String, Set<String>>> appNumberToMailingDateAndDocMap) {
		this.appNumberToMailingDateAndDocMap = appNumberToMailingDateAndDocMap;
	}

	/**
	 * Checks if is US application.
	 *
	 * @return true, if is US application
	 */
	public boolean isUSApplication() {
		return isUSApplication;
	}

	/**
	 * Sets the US application.
	 *
	 * @param isUSApplication the new US application
	 */
	public void setUSApplication(boolean isUSApplication) {
		this.isUSApplication = isUSApplication;
	}

	/**
	 * Gets the converted application number.
	 *
	 * @return the converted application number
	 */
	public String getConvertedApplicationNumber() {
		return convertedApplicationNumber;
	}

	/**
	 * Sets the converted application number.
	 *
	 * @param convertedApplicationNumber the new converted application number
	 */
	public void setConvertedApplicationNumber(String convertedApplicationNumber) {
		this.convertedApplicationNumber = convertedApplicationNumber;
	}

	/**
	 * Gets the bbx application numbers.
	 *
	 * @return the bbx application numbers
	 */
	public List<String> getBbxApplicationNumbers() {
		return bbxApplicationNumbers;
	}

	/**
	 * Sets the bbx application numbers.
	 *
	 * @param bbxApplicationNumbers the new bbx application numbers
	 */
	public void setBbxApplicationNumbers(List<String> bbxApplicationNumbers) {
		this.bbxApplicationNumbers = bbxApplicationNumbers;
	}

	/**
	 * Checks if is valid appplication number.
	 *
	 * @return true, if is valid appplication number
	 */
	public boolean isValidAppplicationNumber() {
		return isValidAppplicationNumber;
	}

	/**
	 * Sets the valid appplication number.
	 *
	 * @param isValidAppplicationNumber the new valid appplication number
	 */
	public void setValidAppplicationNumber(boolean isValidAppplicationNumber) {
		this.isValidAppplicationNumber = isValidAppplicationNumber;
	}

	/**
	 * Gets the customer number to download queues.
	 *
	 * @return the customer number to download queues
	 */
	public Map<String, List<DownloadOfficeActionQueue>> getCustomerNumberToDownloadQueues() {
		return customerNumberToDownloadQueues;
	}

	/**
	 * Sets the customer number to download queues.
	 *
	 * @param customerNumberToDownloadQueues the customer number to download queues
	 */
	public void setCustomerNumberToDownloadQueues(
			Map<String, List<DownloadOfficeActionQueue>> customerNumberToDownloadQueues) {
		this.customerNumberToDownloadQueues = customerNumberToDownloadQueues;
	}

	/**
	 * Gets the app number to doc and mailing date map.
	 *
	 * @return the app number to doc and mailing date map
	 */
	public Map<String, Map<String, Set<String>>> getAppNumberToDocAndMailingDateMap() {
		return appNumberToDocAndMailingDateMap;
	}

	/**
	 * Sets the app number to doc and mailing date map.
	 *
	 * @param appNumberToDocAndMailingDateMap the app number to doc and mailing date map
	 */
	public void setAppNumberToDocAndMailingDateMap(Map<String, Map<String, Set<String>>> appNumberToDocAndMailingDateMap) {
		this.appNumberToDocAndMailingDateMap = appNumberToDocAndMailingDateMap;
	}

	/**
	 * Gets the jurisdiction map.
	 *
	 * @return the jurisdiction map
	 */
	public Map<String, Jurisdiction> getJurisdictionMap() {
		return jurisdictionMap;
	}

	/**
	 * Sets the jurisdiction map.
	 *
	 * @param jurisdictionMap the jurisdiction map
	 */
	public void setJurisdictionMap(Map<String, Jurisdiction> jurisdictionMap) {
		this.jurisdictionMap = jurisdictionMap;
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
	 * Gets the customer to app numbers in bbx map.
	 *
	 * @return the customer to app numbers in bbx map
	 */
	public Map<String, List<String>> getCustomerToAppNumbersInBBXMap() {
		return customerToAppNumbersInBBXMap;
	}

	/**
	 * Sets the customer to app numbers in bbx map.
	 *
	 * @param customerToAppNumbersInBBXMap the customer to app numbers in bbx map
	 */
	public void setCustomerToAppNumbersInBBXMap(Map<String, List<String>> customerToAppNumbersInBBXMap) {
		this.customerToAppNumbersInBBXMap = customerToAppNumbersInBBXMap;
	}

	/**
	 * Gets the max record retry count.
	 *
	 * @return the max record retry count
	 */
	public Integer getMaxRecordRetryCount() {
		return maxRecordRetryCount;
	}

	/**
	 * Sets the max record retry count.
	 *
	 * @param maxRecordRetryCount the new max record retry count
	 */
	public void setMaxRecordRetryCount(Integer maxRecordRetryCount) {
		this.maxRecordRetryCount = maxRecordRetryCount;
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
	 * Gets the list.
	 *
	 * @return the list
	 */
	public List<PDPage> getList() {
		return list;
	}

	/**
	 * Sets the list.
	 *
	 * @param list the new list
	 */
	public void setList(List<PDPage> list) {
		this.list = list;
	}

	/**
	 * Gets the page number index.
	 *
	 * @return the page number index
	 */
	public int getPageNumberIndex() {
		return pageNumberIndex;
	}

	/**
	 * Sets the page number index.
	 *
	 * @param pageNumberIndex the new page number index
	 */
	public void setPageNumberIndex(int pageNumberIndex) {
		this.pageNumberIndex = pageNumberIndex;
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

	/**
	 * Gets the page number.
	 *
	 * @return the page number
	 */
	public List<Integer> getPageNumber() {
		return pageNumber;
	}

	/**
	 * Sets the page number.
	 *
	 * @param pageNumber the new page number
	 */
	public void setPageNumber(List<Integer> pageNumber) {
		this.pageNumber = pageNumber;
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

	public List<String> getPdfNames() {
		return pdfNames;
	}

	public void setPdfNames(List<String> pdfNames) {
		this.pdfNames = pdfNames;
	}

	public void setDocumentCodes(List<DocumentCode> documentCodes) {
		this.documentCodes = documentCodes;
		
	}

	public List<DocumentCode> getDocumentCodes() {
		return documentCodes;
	}
}
