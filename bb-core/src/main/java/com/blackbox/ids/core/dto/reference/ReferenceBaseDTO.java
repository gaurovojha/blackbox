package com.blackbox.ids.core.dto.reference;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.model.reference.OcrStatus;
import com.blackbox.ids.core.model.reference.ReferenceCategoryType;
import com.blackbox.ids.core.model.reference.ReferenceSourceType;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.blackbox.ids.core.model.reference.ReferenceSubSourceType;
import com.blackbox.ids.core.model.reference.ReferenceType;

/**
 * The Class ReferenceBaseDTO.
 *
 * @author nagarro
 */
public class ReferenceBaseDTO {

	// Reference Staging Data Id
	private Long refStagingId;

	// Notification process id.
	private Long notificationProcessId;

	/** ****************************************. */

	private Long referenceBaseDataId;

	/** The source application. */
	private String sourceApplication;

	/** The publication number. */
	private String publicationNumber;

	/** The family id. */
	private String familyId;

	/** The application id. */
	private Long applicationId;

	/** The correspondence id. */
	private CorrespondenceDTO correspondenceId;

	/** The ocr data id. */
	private Long ocrDataId;

	/** The mailing date. */
	private Calendar mailingDate;

	/** The reference record id. */
	private Long referenceRecordId;

	/** The reference catogory. */
	private ReferenceCategoryType referenceCatogory;

	/** The self citation date. */
	private Calendar selfCitationDate;

	/** The reference source. */
	private ReferenceSourceType referenceSource;

	/** The reference sub source. */
	private ReferenceSubSourceType referenceSubSource;

	/** The jurisdiction. */
	private JurisdictionDTO jurisdiction;

	/** The reviewed status. */
	private boolean reviewedStatus;

	/** The reference flow flag. */
	private boolean referenceFlowFlag;

	/** The coupling id. */
	private Long couplingId;

	/** The primary couple. */
	private boolean primaryCouple;

	/** The primary coupling. */
	private boolean primaryCoupling;

	/** The parent reference id. */
	private Long parentReferenceId;

	/** The reference status. */
	private ReferenceStatus referenceStatus;

	/** The reference type. */
	private ReferenceType referenceType;

	/** The reference comments. */
	private String referenceComments;

	/** The active. */
	private boolean active;

	/** ** FP ****. */
	private String foreignDocumentNumber;

	/** The converted foreign document number. */
	private String convertedForeignDocumentNumber;

	/** The kind code. */
	private String kindCode;

	/** The publication date. */
	private Calendar publicationDate;

	/** The applicant name. */
	private String applicantName;

	/** The applicant name. */
	private boolean selfcited = false;

	/** ******** NPL **********. */
	private String author;

	/** The title. */
	private String title;

	/** The publication detail. */
	private String publicationDetail;

	/** The publication month. */
	private Integer publicationMonth;

	/** The publication day. */
	private Integer publicationDay;

	/** The publication year. */
	private Integer publicationYear;

	/** The relevant pages. */
	private String relevantPages;

	/** The volume number. */
	private String volumeNumber;

	/** The url. */
	private String URL;

	/** The publication city. */
	private String publicationCity;

	/** The string data. */
	private String stringData;

	/** The un published. */
	private boolean unPublished;

	/** The application serial number. */
	private String applicationSerialNumber;

	/** The retrival date. */
	private String retrivalDate;

	/** ************* PUS ************. */
	private String convertedPublicationNumber;

	/** ********** extra ***************. */

	private String documentDescription;

	/** The application number. */
	private String applicationNumber;

	/** jurisdiction code of application */
	private String applicationJurisdictionCode;

	/** The document uploaded by user. */
	private String documentUploadedByUser;

	/** The document uploaded on */
	private String documentUploadedOn;

	/** Document updated by **/
	private String documentUpdatedByUser;

	/** Document updated on **/
	private String documentUpdatedOn;

	/** The ocr status. */
	private OcrStatus ocrStatus;

	/** The filling date. */
	private Calendar fillingDate;

	/** The grant date. */
	private Calendar grantDate;

	/** The number type. */
	private String numberType;

	/** The mailing date str. */
	private String mailingDateStr;

	/** The reference count. */
	private Integer referenceCount;

	/** The create fya notification. */
	private boolean createFYANotification;

	/** The manual add. */
	private boolean manualAdd;

	/** The reference fp data. */
	private List<ReferenceFpDTO> referenceFpData;

	/** The reference npl data. */
	private List<ReferenceNplDTO> referenceNplData;

	/** The reference pus data. */
	private List<ReferencePusDTO> referencePusData;

	/** ********** extra information ***************. */
	private Map<String, String> referenceEnteredByDetails;

	/** The reference reviewed by. */
	private String referenceReviewedBy;

	/** The reference reviewed date. */
	private String referenceReviewedDate;

	/** The reference reviewed details. */
	private Map<String, String> referenceReviewedDetails;

	/** The correspondence number. */
	private Long correspondenceNumber;

	private String referencetype;

	private String publicationDateStr;

	/** The filingDate of the application **/
	private String applicationFilingDate;

	/** The Date of issue of application **/
	private String applicationIssuedOn;

	/** The type of Publication Number **/
	private String typeOfNumber;

	/** The Jurisdiction of application **/
	private String applicationJurisdictionType;

	/** English translation **/
	private boolean englishTranslation;

	/** Attribute for PDF file **/
	private transient MultipartFile file;

	private Boolean notificationSent;

	private SourceReferenceDTO sourceReference;

	private boolean updateFamily;

	private ReferenceNplDTO referenceNplDTO;

	/**
	 * /** Gets the reference base data id.
	 *
	 * @return the reference base data id
	 */
	public Long getReferenceBaseDataId() {
		return referenceBaseDataId;
	}

	/**
	 * Sets the reference base data id.
	 *
	 * @param referenceBaseDataId
	 *            the new reference base data id
	 */
	public void setReferenceBaseDataId(Long referenceBaseDataId) {
		this.referenceBaseDataId = referenceBaseDataId;
	}

	/**
	 * Gets the source application.
	 *
	 * @return the source application
	 */
	public String getSourceApplication() {
		return sourceApplication;
	}

	/**
	 * Sets the source application.
	 *
	 * @param sourceApplication
	 *            the new source application
	 */
	public void setSourceApplication(String sourceApplication) {
		this.sourceApplication = sourceApplication;
	}

	/**
	 * Gets the publication number.
	 *
	 * @return the publication number
	 */
	public String getPublicationNumber() {
		return publicationNumber;
	}

	/**
	 * Sets the publication number.
	 *
	 * @param publicationNumber
	 *            the new publication number
	 */
	public void setPublicationNumber(String publicationNumber) {
		this.publicationNumber = publicationNumber;
	}

	/**
	 * Gets the family id.
	 *
	 * @return the family id
	 */
	public String getFamilyId() {
		return familyId;
	}

	/**
	 * Sets the family id.
	 *
	 * @param familyId
	 *            the new family id
	 */
	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	/**
	 * Gets the application id.
	 *
	 * @return the application id
	 */
	public Long getApplicationId() {
		return applicationId;
	}

	/**
	 * Sets the application id.
	 *
	 * @param applicationId
	 *            the new application id
	 */
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * Gets the correspondence id.
	 *
	 * @return the correspondence id
	 */
	public CorrespondenceDTO getCorrespondenceId() {
		return correspondenceId;
	}

	/**
	 * Sets the correspondence id.
	 *
	 * @param correspondenceId
	 *            the new correspondence id
	 */
	public void setCorrespondenceId(CorrespondenceDTO correspondenceId) {
		this.correspondenceId = correspondenceId;
	}

	/**
	 * Gets the ocr data id.
	 *
	 * @return the ocr data id
	 */
	public Long getOcrDataId() {
		return ocrDataId;
	}

	/**
	 * Sets the ocr data id.
	 *
	 * @param ocrDataId
	 *            the new ocr data id
	 */
	public void setOcrDataId(Long ocrDataId) {
		this.ocrDataId = ocrDataId;
	}

	/**
	 * Gets the mailing date.
	 *
	 * @return the mailing date
	 */
	public Calendar getMailingDate() {
		return mailingDate;
	}

	/**
	 * Sets the mailing date.
	 *
	 * @param mailingDate
	 *            the new mailing date
	 */
	public void setMailingDate(Calendar mailingDate) {
		this.mailingDate = mailingDate;
	}

	/**
	 * Gets the reference record id.
	 *
	 * @return the reference record id
	 */
	public Long getReferenceRecordId() {
		return referenceRecordId;
	}

	/**
	 * Sets the reference record id.
	 *
	 * @param referenceRecordId
	 *            the new reference record id
	 */
	public void setReferenceRecordId(Long referenceRecordId) {
		this.referenceRecordId = referenceRecordId;
	}

	/**
	 * Gets the reference catogory.
	 *
	 * @return the reference catogory
	 */
	public ReferenceCategoryType getReferenceCatogory() {
		return referenceCatogory;
	}

	/**
	 * Sets the reference catogory.
	 *
	 * @param referenceCatogory
	 *            the new reference catogory
	 */
	public void setReferenceCatogory(ReferenceCategoryType referenceCatogory) {
		this.referenceCatogory = referenceCatogory;
	}

	/**
	 * Gets the self citation date.
	 *
	 * @return the self citation date
	 */
	public Calendar getSelfCitationDate() {
		return selfCitationDate;
	}

	/**
	 * Sets the self citation date.
	 *
	 * @param selfCitationDate
	 *            the new self citation date
	 */
	public void setSelfCitationDate(Calendar selfCitationDate) {
		this.selfCitationDate = selfCitationDate;
	}

	/**
	 * Gets the reference source.
	 *
	 * @return the reference source
	 */
	public ReferenceSourceType getReferenceSource() {
		return referenceSource;
	}

	/**
	 * Sets the reference source.
	 *
	 * @param referenceSource
	 *            the new reference source
	 */
	public void setReferenceSource(ReferenceSourceType referenceSource) {
		this.referenceSource = referenceSource;
	}

	/**
	 * Gets the reference sub source.
	 *
	 * @return the reference sub source
	 */
	public ReferenceSubSourceType getReferenceSubSource() {
		return referenceSubSource;
	}

	/**
	 * Sets the reference sub source.
	 *
	 * @param referenceSubSource
	 *            the new reference sub source
	 */
	public void setReferenceSubSource(ReferenceSubSourceType referenceSubSource) {
		this.referenceSubSource = referenceSubSource;
	}

	/**
	 * Gets the jurisdiction.
	 *
	 * @return the jurisdiction
	 */
	public JurisdictionDTO getJurisdiction() {
		return jurisdiction;
	}

	/**
	 * Sets the jurisdiction.
	 *
	 * @param jurisdiction
	 *            the new jurisdiction
	 */
	public void setJurisdiction(JurisdictionDTO jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	/**
	 * Checks if is reviewed status.
	 *
	 * @return true, if is reviewed status
	 */
	public boolean isReviewedStatus() {
		return reviewedStatus;
	}

	/**
	 * Sets the reviewed status.
	 *
	 * @param reviewedStatus
	 *            the new reviewed status
	 */
	public void setReviewedStatus(boolean reviewedStatus) {
		this.reviewedStatus = reviewedStatus;
	}

	/**
	 * Checks if is reference flow flag.
	 *
	 * @return true, if is reference flow flag
	 */
	public boolean isReferenceFlowFlag() {
		return referenceFlowFlag;
	}

	/**
	 * Sets the reference flow flag.
	 *
	 * @param referenceFlowFlag
	 *            the new reference flow flag
	 */
	public void setReferenceFlowFlag(boolean referenceFlowFlag) {
		this.referenceFlowFlag = referenceFlowFlag;
	}

	/**
	 * Gets the coupling id.
	 *
	 * @return the coupling id
	 */
	public Long getCouplingId() {
		return couplingId;
	}

	/**
	 * Sets the coupling id.
	 *
	 * @param couplingId
	 *            the new coupling id
	 */
	public void setCouplingId(Long couplingId) {
		this.couplingId = couplingId;
	}

	/**
	 * Checks if is primary couple.
	 *
	 * @return true, if is primary couple
	 */
	public boolean isPrimaryCouple() {
		return primaryCouple;
	}

	/**
	 * Sets the primary couple.
	 *
	 * @param primaryCouple
	 *            the new primary couple
	 */
	public void setPrimaryCouple(boolean primaryCouple) {
		this.primaryCouple = primaryCouple;
	}

	/**
	 * Checks if is primary coupling.
	 *
	 * @return true, if is primary coupling
	 */
	public boolean isPrimaryCoupling() {
		return primaryCoupling;
	}

	/**
	 * Sets the primary coupling.
	 *
	 * @param primaryCoupling
	 *            the new primary coupling
	 */
	public void setPrimaryCoupling(boolean primaryCoupling) {
		this.primaryCoupling = primaryCoupling;
	}

	/**
	 * Gets the parent reference id.
	 *
	 * @return the parent reference id
	 */
	public Long getParentReferenceId() {
		return parentReferenceId;
	}

	/**
	 * Sets the parent reference id.
	 *
	 * @param parentReferenceId
	 *            the new parent reference id
	 */
	public void setParentReferenceId(Long parentReferenceId) {
		this.parentReferenceId = parentReferenceId;
	}

	/**
	 * Gets the reference status.
	 *
	 * @return the reference status
	 */
	public ReferenceStatus getReferenceStatus() {
		return referenceStatus;
	}

	/**
	 * Sets the reference status.
	 *
	 * @param referenceStatus
	 *            the new reference status
	 */
	public void setReferenceStatus(ReferenceStatus referenceStatus) {
		this.referenceStatus = referenceStatus;
	}

	/**
	 * Gets the reference type.
	 *
	 * @return the reference type
	 */
	public ReferenceType getReferenceType() {
		return referenceType;
	}

	/**
	 * Sets the reference type.
	 *
	 * @param referenceType
	 *            the new reference type
	 */
	public void setReferenceType(ReferenceType referenceType) {
		this.referenceType = referenceType;
	}

	/**
	 * Gets the reference comments.
	 *
	 * @return the reference comments
	 */
	public String getReferenceComments() {
		return referenceComments;
	}

	/**
	 * Sets the reference comments.
	 *
	 * @param referenceComments
	 *            the new reference comments
	 */
	public void setReferenceComments(String referenceComments) {
		this.referenceComments = referenceComments;
	}

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active
	 *            the new active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets the foreign document number.
	 *
	 * @return the foreign document number
	 */
	public String getForeignDocumentNumber() {
		return foreignDocumentNumber;
	}

	/**
	 * Sets the foreign document number.
	 *
	 * @param foreignDocumentNumber
	 *            the new foreign document number
	 */
	public void setForeignDocumentNumber(String foreignDocumentNumber) {
		this.foreignDocumentNumber = foreignDocumentNumber;
	}

	/**
	 * Gets the converted foreign document number.
	 *
	 * @return the converted foreign document number
	 */
	public String getConvertedForeignDocumentNumber() {
		return convertedForeignDocumentNumber;
	}

	/**
	 * Sets the converted foreign document number.
	 *
	 * @param convertedForeignDocumentNumber
	 *            the new converted foreign document number
	 */
	public void setConvertedForeignDocumentNumber(String convertedForeignDocumentNumber) {
		this.convertedForeignDocumentNumber = convertedForeignDocumentNumber;
	}

	/**
	 * Gets the kind code.
	 *
	 * @return the kind code
	 */
	public String getKindCode() {
		return kindCode;
	}

	/**
	 * Sets the kind code.
	 *
	 * @param kindCode
	 *            the new kind code
	 */
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	/**
	 * Gets the publication date.
	 *
	 * @return the publication date
	 */
	public Calendar getPublicationDate() {
		return publicationDate;
	}

	/**
	 * Sets the publication date.
	 *
	 * @param publicationDate
	 *            the new publication date
	 */
	public void setPublicationDate(Calendar publicationDate) {
		this.publicationDate = publicationDate;
	}

	/**
	 * Gets the applicant name.
	 *
	 * @return the applicant name
	 */
	public String getApplicantName() {
		return applicantName;
	}

	/**
	 * Sets the applicant name.
	 *
	 * @param applicantName
	 *            the new applicant name
	 */
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	/**
	 * Gets the author.
	 *
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the author.
	 *
	 * @param author
	 *            the new author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the publication detail.
	 *
	 * @return the publication detail
	 */
	public String getPublicationDetail() {
		return publicationDetail;
	}

	/**
	 * Sets the publication detail.
	 *
	 * @param publicationDetail
	 *            the new publication detail
	 */
	public void setPublicationDetail(String publicationDetail) {
		this.publicationDetail = publicationDetail;
	}

	/**
	 * Gets the publication month.
	 *
	 * @return the publication month
	 */
	public Integer getPublicationMonth() {
		return publicationMonth;
	}

	/**
	 * Sets the publication month.
	 *
	 * @param publicationMonth
	 *            the new publication month
	 */
	public void setPublicationMonth(Integer publicationMonth) {
		this.publicationMonth = publicationMonth;
	}

	/**
	 * Gets the publication day.
	 *
	 * @return the publication day
	 */
	public Integer getPublicationDay() {
		return publicationDay;
	}

	/**
	 * Sets the publication day.
	 *
	 * @param publicationDay
	 *            the new publication day
	 */
	public void setPublicationDay(Integer publicationDay) {
		this.publicationDay = publicationDay;
	}

	/**
	 * Gets the publication year.
	 *
	 * @return the publication year
	 */
	public Integer getPublicationYear() {
		return publicationYear;
	}

	/**
	 * Sets the publication year.
	 *
	 * @param publicationYear
	 *            the new publication year
	 */
	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	/**
	 * Gets the relevant pages.
	 *
	 * @return the relevant pages
	 */
	public String getRelevantPages() {
		return relevantPages;
	}

	/**
	 * Sets the relevant pages.
	 *
	 * @param relevantPages
	 *            the new relevant pages
	 */
	public void setRelevantPages(String relevantPages) {
		this.relevantPages = relevantPages;
	}

	/**
	 * Gets the volume number.
	 *
	 * @return the volume number
	 */
	public String getVolumeNumber() {
		return volumeNumber;
	}

	/**
	 * Sets the volume number.
	 *
	 * @param volumeNumber
	 *            the new volume number
	 */
	public void setVolumeNumber(String volumeNumber) {
		this.volumeNumber = volumeNumber;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * Sets the url.
	 *
	 * @param uRL
	 *            the new url
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * Gets the publication city.
	 *
	 * @return the publication city
	 */
	public String getPublicationCity() {
		return publicationCity;
	}

	/**
	 * Sets the publication city.
	 *
	 * @param publicationCity
	 *            the new publication city
	 */
	public void setPublicationCity(String publicationCity) {
		this.publicationCity = publicationCity;
	}

	/**
	 * Gets the string data.
	 *
	 * @return the string data
	 */
	public String getStringData() {
		return stringData;
	}

	/**
	 * Sets the string data.
	 *
	 * @param stringData
	 *            the new string data
	 */
	public void setStringData(String stringData) {
		this.stringData = stringData;
	}

	/**
	 * Checks if is un published.
	 *
	 * @return true, if is un published
	 */
	public boolean isUnPublished() {
		return unPublished;
	}

	/**
	 * Sets the un published.
	 *
	 * @param unPublished
	 *            the new un published
	 */
	public void setUnPublished(boolean unPublished) {
		this.unPublished = unPublished;
	}

	/**
	 * Gets the application serial number.
	 *
	 * @return the application serial number
	 */
	public String getApplicationSerialNumber() {
		return applicationSerialNumber;
	}

	/**
	 * Sets the application serial number.
	 *
	 * @param applicationSerialNumber
	 *            the new application serial number
	 */
	public void setApplicationSerialNumber(String applicationSerialNumber) {
		this.applicationSerialNumber = applicationSerialNumber;
	}

	/**
	 * Gets the converted publication number.
	 *
	 * @return the converted publication number
	 */
	public String getConvertedPublicationNumber() {
		return convertedPublicationNumber;
	}

	/**
	 * Sets the converted publication number.
	 *
	 * @param convertedPublicationNumber
	 *            the new converted publication number
	 */
	public void setConvertedPublicationNumber(String convertedPublicationNumber) {
		this.convertedPublicationNumber = convertedPublicationNumber;
	}

	/**
	 * Gets the document description.
	 *
	 * @return the document description
	 */
	public String getDocumentDescription() {
		return documentDescription;
	}

	/**
	 * Sets the document description.
	 *
	 * @param documentDescription
	 *            the new document description
	 */
	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	/**
	 * Gets the application number.
	 *
	 * @return the application number
	 */
	public String getApplicationNumber() {
		return applicationNumber;
	}

	/**
	 * Sets the application number.
	 *
	 * @param applicationNumber
	 *            the new application number
	 */
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	/**
	 * Gets the document uploaded by user.
	 *
	 * @return the document uploaded by user
	 */
	public String getDocumentUploadedByUser() {
		return documentUploadedByUser;
	}

	/**
	 * Sets the document uploaded by user.
	 *
	 * @param documentUploadedByUser
	 *            the new document uploaded by user
	 */
	public void setDocumentUploadedByUser(String documentUploadedByUser) {
		this.documentUploadedByUser = documentUploadedByUser;
	}

	/**
	 * Gets the ocr status.
	 *
	 * @return the ocr status
	 */
	public OcrStatus getOcrStatus() {
		return ocrStatus;
	}

	/**
	 * Sets the ocr status.
	 *
	 * @param ocrStatus
	 *            the new ocr status
	 */
	public void setOcrStatus(OcrStatus ocrStatus) {
		this.ocrStatus = ocrStatus;
	}

	/**
	 * Gets the reference fp data.
	 *
	 * @return the reference fp data
	 */
	public List<ReferenceFpDTO> getReferenceFpData() {
		return referenceFpData;
	}

	/**
	 * Sets the reference fp data.
	 *
	 * @param referenceFPData
	 *            the new reference fp data
	 */
	public void setReferenceFpData(List<ReferenceFpDTO> referenceFPData) {
		this.referenceFpData = referenceFPData;
	}

	/**
	 * Gets the reference npl data.
	 *
	 * @return the reference npl data
	 */
	public List<ReferenceNplDTO> getReferenceNplData() {
		return referenceNplData;
	}

	/**
	 * Sets the reference npl data.
	 *
	 * @param referenceNPLData
	 *            the new reference npl data
	 */
	public void setReferenceNplData(List<ReferenceNplDTO> referenceNPLData) {
		this.referenceNplData = referenceNPLData;
	}

	/**
	 * Gets the reference pus data.
	 *
	 * @return the reference pus data
	 */
	public List<ReferencePusDTO> getReferencePusData() {
		return referencePusData;
	}

	/**
	 * Sets the reference pus data.
	 *
	 * @param referencePUSData
	 *            the new reference pus data
	 */
	public void setReferencePusData(List<ReferencePusDTO> referencePUSData) {
		this.referencePusData = referencePUSData;
	}

	/**
	 * Gets the filling date.
	 *
	 * @return the filling date
	 */
	public Calendar getFillingDate() {
		return fillingDate;
	}

	/**
	 * Sets the filling date.
	 *
	 * @param fillingDate
	 *            the new filling date
	 */
	public void setFillingDate(Calendar fillingDate) {
		this.fillingDate = fillingDate;
	}

	/**
	 * Gets the grant date.
	 *
	 * @return the grant date
	 */
	public Calendar getGrantDate() {
		return grantDate;
	}

	/**
	 * Sets the grant date.
	 *
	 * @param grantDate
	 *            the new grant date
	 */
	public void setGrantDate(Calendar grantDate) {
		this.grantDate = grantDate;
	}

	/**
	 * Gets the number type.
	 *
	 * @return the number type
	 */
	public String getNumberType() {
		return numberType;
	}

	/**
	 * Sets the number type.
	 *
	 * @param numberType
	 *            the new number type
	 */
	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}

	/**
	 * Gets the mailing date str.
	 *
	 * @return the mailing date str
	 */
	public String getMailingDateStr() {
		return mailingDateStr;
	}

	/**
	 * Sets the mailing date str.
	 *
	 * @param mailingDateStr
	 *            the new mailing date str
	 */
	public void setMailingDateStr(String mailingDateStr) {
		this.mailingDateStr = mailingDateStr;
	}

	/**
	 * Checks if is creates the fya notification.
	 *
	 * @return true, if is creates the fya notification
	 */
	public boolean isCreateFYANotification() {
		return createFYANotification;
	}

	/**
	 * Sets the creates the fya notification.
	 *
	 * @param createFYANotification
	 *            the new creates the fya notification
	 */
	public void setCreateFYANotification(boolean createFYANotification) {
		this.createFYANotification = createFYANotification;
	}

	/**
	 * Gets the reference count.
	 *
	 * @return the reference count
	 */
	public Integer getReferenceCount() {
		return referenceCount;
	}

	/**
	 * Sets the reference count.
	 *
	 * @param referenceCount
	 *            the new reference count
	 */
	public void setReferenceCount(Integer referenceCount) {
		this.referenceCount = referenceCount;
	}

	/**
	 * Checks if is manual add.
	 *
	 * @return true, if is manual add
	 */
	public boolean isManualAdd() {
		return manualAdd;
	}

	/**
	 * Sets the manual add.
	 *
	 * @param manualAdd
	 *            the new manual add
	 */
	public void setManualAdd(boolean manualAdd) {
		this.manualAdd = manualAdd;
	}

	/**
	 * Gets the reference entered by details.
	 *
	 * @return the referenceEnteredByDetails
	 */
	public Map<String, String> getReferenceEnteredByDetails() {
		return referenceEnteredByDetails;
	}

	/**
	 * Sets the reference entered by details.
	 *
	 * @param referenceEnteredByDetails
	 *            the referenceEnteredByDetails to set
	 */
	public void setReferenceEnteredByDetails(Map<String, String> referenceEnteredByDetails) {
		this.referenceEnteredByDetails = referenceEnteredByDetails;
	}

	/**
	 * Gets the reference reviewed by.
	 *
	 * @return the referenceReviewedBy
	 */
	public String getReferenceReviewedBy() {
		return referenceReviewedBy;
	}

	/**
	 * Sets the reference reviewed by.
	 *
	 * @param referenceReviewedBy
	 *            the referenceReviewedBy to set
	 */
	public void setReferenceReviewedBy(String referenceReviewedBy) {
		this.referenceReviewedBy = referenceReviewedBy;
	}

	/**
	 * Gets the reference reviewed date.
	 *
	 * @return the referenceReviewedDate
	 */
	public String getReferenceReviewedDate() {
		return referenceReviewedDate;
	}

	/**
	 * Sets the reference reviewed date.
	 *
	 * @param referenceReviewedDate
	 *            the referenceReviewedDate to set
	 */
	public void setReferenceReviewedDate(String referenceReviewedDate) {
		this.referenceReviewedDate = referenceReviewedDate;
	}

	/**
	 * Gets the correspondence number.
	 *
	 * @return the correspondence number
	 */
	public Long getCorrespondenceNumber() {
		return correspondenceNumber;
	}

	/**
	 * Sets the correspondence number.
	 *
	 * @param correspondenceNumber
	 *            the new correspondence number
	 */
	public void setCorrespondenceNumber(Long correspondenceNumber) {
		this.correspondenceNumber = correspondenceNumber;
	}

	public String getReferencetype() {
		return referencetype;
	}

	public void setReferencetype(String referencetype) {
		this.referencetype = referencetype;
	}

	public String getPublicationDateStr() {
		return publicationDateStr;
	}

	public void setPublicationDateStr(String publicationDateStr) {
		this.publicationDateStr = publicationDateStr;
	}

	/**
	 * @return the selfcited
	 */
	public boolean isSelfcited() {
		return selfcited;
	}

	/**
	 * @param selfcited
	 *            the selfcited to set
	 */
	public void setSelfcited(boolean selfcited) {
		this.selfcited = selfcited;
	}

	/**
	 * @return the applicationJurisdictionCode
	 */
	public String getApplicationJurisdictionCode() {
		return applicationJurisdictionCode;
	}

	/**
	 * @param applicationJurisdictionCode
	 *            the applicationJurisdictionCode to set
	 */
	public void setApplicationJurisdictionCode(String applicationJurisdictionCode) {
		this.applicationJurisdictionCode = applicationJurisdictionCode;
	}

	/**
	 * @return the documentUpdatedByUser
	 */
	public String getDocumentUpdatedByUser() {
		return documentUpdatedByUser;
	}

	/**
	 * @param documentUpdatedByUser
	 *            the documentUpdatedByUser to set
	 */
	public void setDocumentUpdatedByUser(String documentUpdatedByUser) {
		this.documentUpdatedByUser = documentUpdatedByUser;
	}

	/**
	 * @return the documentUploadedOn
	 */
	public String getDocumentUploadedOn() {
		return documentUploadedOn;
	}

	/**
	 * @param documentUploadedOn
	 *            the documentUploadedOn to set
	 */
	public void setDocumentUploadedOn(String documentUploadedOn) {
		this.documentUploadedOn = documentUploadedOn;
	}

	/**
	 * @return the documentUpdatedOn
	 */
	public String getDocumentUpdatedOn() {
		return documentUpdatedOn;
	}

	/**
	 * @param documentUpdatedOn
	 *            the documentUpdatedOn to set
	 */
	public void setDocumentUpdatedOn(String documentUpdatedOn) {
		this.documentUpdatedOn = documentUpdatedOn;
	}

	public String getApplicationFilingDate() {
		return applicationFilingDate;
	}

	public void setApplicationFilingDate(String applicationFilingDate) {
		this.applicationFilingDate = applicationFilingDate;
	}

	public String getApplicationIssuedOn() {
		return applicationIssuedOn;
	}

	public void setApplicationIssuedOn(String applicationIssuedOn) {
		this.applicationIssuedOn = applicationIssuedOn;
	}

	public String getTypeOfNumber() {
		return typeOfNumber;
	}

	public void setTypeOfNumber(String typeOfNumber) {
		this.typeOfNumber = typeOfNumber;
	}

	public String getApplicationJurisdictionType() {
		return applicationJurisdictionType;
	}

	public void setApplicationJurisdictionType(String applicationJurisdictionType) {
		this.applicationJurisdictionType = applicationJurisdictionType;
	}

	/**
	 * @return the englishTranslation
	 */
	public boolean isEnglishTranslation() {
		return englishTranslation;
	}

	/**
	 * @param englishTranslation
	 *            the englishTranslation to set
	 */
	public void setEnglishTranslation(boolean englishTranslation) {
		this.englishTranslation = englishTranslation;
	}

	public Long getNotificationProcessId() {
		return notificationProcessId;
	}

	public void setNotificationProcessId(Long notificationProcessId) {
		this.notificationProcessId = notificationProcessId;
	}

	/****************************************/

	public Long getRefStagingId() {
		return refStagingId;
	}

	public void setRefStagingId(Long refStagingId) {
		this.refStagingId = refStagingId;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Boolean getNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(Boolean notificationSent) {
		this.notificationSent = notificationSent;
	}

	public SourceReferenceDTO getSourceReference() {
		return sourceReference;
	}

	public void setSourceReference(SourceReferenceDTO sourceReference) {
		this.sourceReference = sourceReference;
	}

	public boolean isUpdateFamily() {
		return updateFamily;
	}

	public void setUpdateFamily(boolean updateFamily) {
		this.updateFamily = updateFamily;
	}

	public ReferenceNplDTO getReferenceNplDTO() {
		return referenceNplDTO;
	}

	public void setReferenceNplDTO(ReferenceNplDTO referenceNplDTO) {
		this.referenceNplDTO = referenceNplDTO;
	}

	public String getRetrivalDate() {
		return retrivalDate;
	}

	public void setRetrivalDate(String retrivalDate) {
		this.retrivalDate = retrivalDate;
	}

	/**
	 * @return the referenceReviewedDetails
	 */
	public Map<String, String> getReferenceReviewedDetails() {
		return referenceReviewedDetails;
	}

	/**
	 * @param referenceReviewedDetails
	 *            the referenceReviewedDetails to set
	 */
	public void setReferenceReviewedDetails(Map<String, String> referenceReviewedDetails) {
		this.referenceReviewedDetails = referenceReviewedDetails;
	}

}
