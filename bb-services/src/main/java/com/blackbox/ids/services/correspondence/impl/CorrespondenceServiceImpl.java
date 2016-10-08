package com.blackbox.ids.services.correspondence.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.auth.BlackboxUser;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.correspondence.ICorrespondenceDAO;
import com.blackbox.ids.core.dao.correspondence.ICorrespondenceStageDAO;
import com.blackbox.ids.core.dao.correspondence.IJobDAO;
import com.blackbox.ids.core.dao.notification.NotificationProcessDAO;
import com.blackbox.ids.core.dao.webcrawler.DownloadOfficeActionQueueDAO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordDTO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordsFilter;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.correspondence.Correspondence.Source;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.Correspondence.StepStatus;
import com.blackbox.ids.core.model.correspondence.Correspondence.StepType;
import com.blackbox.ids.core.model.correspondence.Correspondence.SubSourceTypes;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.correspondence.Job;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.repository.AssigneeRepository;
import com.blackbox.ids.core.repository.DocumentCodeRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceBaseRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceStagingRepository;
import com.blackbox.ids.core.repository.reference.ReferenceBaseDataCustomRepository;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.PDFUtil;
import com.blackbox.ids.core.util.PDFUtil.BookMarkType;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.dto.ResponseDTO;
import com.blackbox.ids.dto.ResponseDTO.ResponseStatus;
import com.blackbox.ids.dto.correspondence.CorrespondenceFormDto;
import com.blackbox.ids.dto.correspondence.CorrespondenceFormDto.CorrespondenceFormFieldErrors;
import com.blackbox.ids.dto.correspondence.CorrespondenceFormDto.CorrespondenceFormFields;
import com.blackbox.ids.exception.BBError;
import com.blackbox.ids.services.assignee.IAssigneeService;
import com.blackbox.ids.services.correspondence.ICorrespondenceService;
import com.blackbox.ids.services.jurisdiction.IJurisdictionService;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.reference.ReferenceService;
import com.blackbox.ids.services.validation.NumberFormatValidationService;

/**
 * The Class CorrespondenceServiceImpl. This class has all the service methods required for correspondence
 */
@Service
public class CorrespondenceServiceImpl implements ICorrespondenceService {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(CorrespondenceServiceImpl.class);

	/** The correspondence base repository. */
	@Autowired
	CorrespondenceBaseRepository correspondenceBaseRepository;

	/** The number format validation service. */
	@Autowired
	NumberFormatValidationService numberFormatValidationService;

	/** The jurisdiction service. */
	@Autowired
	IJurisdictionService jurisdictionService;

	/** The assignee repository. */
	@Autowired
	AssigneeRepository assigneeRepository;

	/** The application base dao. */
	@Autowired
	ApplicationBaseDAO applicationBaseDao;

	/** The document code repository. */
	@Autowired
	DocumentCodeRepository documentCodeRepository;

	/** The correspondence dao impl. */
	@Autowired
	ICorrespondenceDAO correspondenceDAOImpl;

	/** The correspondence staging repository. */
	@Autowired
	CorrespondenceStagingRepository correspondenceStagingRepository;

	/** The notification service. */
	@Autowired
	NotificationProcessService notificationService;

	/** The assignee service. */
	@Autowired
	IAssigneeService assigneeService;

	/** The correspondence dao. */
	@Autowired
	private ICorrespondenceDAO correspondenceDAO;

	/** The correspondence stag dao. */
	@Autowired
	private ICorrespondenceStageDAO correspondenceStagDAO;

	/** The notification process service. */
	@Autowired
	private NotificationProcessService notificationProcessService;

	/** The job dao. */
	@Autowired
	private IJobDAO jobDao;

	/** The reference base data repository. */
	@Autowired
	private ReferenceBaseDataCustomRepository<ReferenceBaseData> referenceBaseDataRepository;

	/** The reference service. */
	@Autowired
	private ReferenceService referenceService;

	/** The download qdao. */
	@Autowired
	private DownloadOfficeActionQueueDAO downloadQDAO;

	/** The notification process dao. */
	@Autowired
	private NotificationProcessDAO notificationProcessDAO;

	/** The Constant US_JURISDICTIION. */
	public static final String US_JURISDICTIION = "US";

	/** The Constant WIPO_JURISDICTION. */
	public static final String WIPO_JURISDICTION = "WO";

	/** The Constant WIPO_APPLICATION_PREFIX. */
	public static final String WIPO_APPLICATION_PREFIX = "PCT/US";

	/**
	 * Validate us manual correspondence data. This method is called if Jurisdiction type is US or Jurisdiction type is
	 * WO and application number starts with PCT/US. This api validates that there is only one bookmark and data entered
	 * is consistent with the bookmark
	 * @param correspondenceFormDto
	 *            the correspondence form dto
	 * @param applicationBase
	 *            the application base
	 * @param covertedApplicationNumber
	 *            the coverted application number
	 * @return the response dto
	 * @throws Exception
	 *             the exception
	 */

	@Override
	public ResponseDTO validateUSManualCorrespondenceData(CorrespondenceFormDto correspondenceFormDto,
			ApplicationBase applicationBase, String covertedApplicationNumber) {
		ResponseDTO responseDTO = new ResponseDTO();
		List<BBError> bbErrors = new ArrayList<>();

		responseDTO.setStatus(ResponseStatus.ERROR);

		try {
			String file = correspondenceFormDto.getFile();
			if (StringUtils.isNotBlank(file)) {

				if (!isAtleastOneBookmark(file)) {
					bbErrors.add(new BBError(CorrespondenceFormFields.FILE,
							CorrespondenceFormFieldErrors.CORRESPONDENCE_NOBOOKMARK_CORRESPONDENCEFORM));

				} else if (isMultipleBookmark(file)) {
					bbErrors.add(new BBError(CorrespondenceFormFields.FILE,
							CorrespondenceFormFieldErrors.CORRESPONDENCE_MULTIPLEBOOKMARKS_CORRESPONDENCEFORM));
				} else if (!isValidBookmarkType(file)) {
					bbErrors.add(new BBError(CorrespondenceFormFields.FILE,
							CorrespondenceFormFieldErrors.CORRESPONDENCE_INVALIDBOOKMARK_CORRESPONDENCEFORM));

				} else {

					List<String> bookmarks = PDFUtil.getBookmarksNames(file);
					String bookmark = bookmarks.get(0);
					BookMarkType bookMarkType = PDFUtil.getBookMarkType(bookmark);
					if (BookMarkType.B1.equals(bookMarkType)) {
						if (!covertedApplicationNumber.equals(PDFUtil.getBookmarkApplicationNumber(bookmark).trim())) {

							bbErrors.add(new BBError(CorrespondenceFormFields.FILE,
									CorrespondenceFormFieldErrors.CORRESPONDENCE_INVALIDBOOKMARK_APPLICATIONNUMBER_MISMATCH));
						}

						if (!applicationBase.getCustomer().getCustomerNumber()
								.equals(PDFUtil.getBookmarkCustomerNumber(bookmark).trim())) {

							bbErrors.add(new BBError(CorrespondenceFormFields.FILE,
									CorrespondenceFormFieldErrors.CORRESPONDENCE_INVALIDBOOKMARK_CUSTOMER_NUMBER_MISMATCH));
						}
						String date1 = BlackboxDateUtil.dateToStr(correspondenceFormDto.getMailingDate(),
								TimestampFormat.MM_DD_YYYY);
						if (!date1.equals(PDFUtil.getBookmarkDate(bookmark).trim())) {

							bbErrors.add(new BBError(CorrespondenceFormFields.FILE,
									CorrespondenceFormFieldErrors.CORRESPONDENCE_INVALIDBOOKMARK_MAILING_DATE_MISMATCH));
						}

						String description = documentCodeRepository.findById(correspondenceFormDto.getDocumentCode())
								.getDescription();
						if (!description.equalsIgnoreCase(PDFUtil.getBookmarkDescription(bookmark).trim())) {

							bbErrors.add(new BBError(CorrespondenceFormFields.FILE,
									CorrespondenceFormFieldErrors.CORRESPONDENCE_INVALIDBOOKMARK_DOCUMENT_DESCRIPTION_MISMATCH));
						}

					} else if (BookMarkType.B2.equals(bookMarkType)) {
						String date1 = BlackboxDateUtil.dateToStr(correspondenceFormDto.getMailingDate(),
								TimestampFormat.yyyyMMdd);
						if (!date1.equals(PDFUtil.getBookmarkB2Date(bookmark).trim())) {

							bbErrors.add(new BBError(CorrespondenceFormFields.FILE,
									CorrespondenceFormFieldErrors.CORRESPONDENCE_INVALIDBOOKMARK_MAILING_DATE_MISMATCH));
						}

						String description = documentCodeRepository.findById(correspondenceFormDto.getDocumentCode())
								.getDescription();
						if (!description.equalsIgnoreCase(PDFUtil.getBookmarkB2Description(bookmark).trim())) {

							bbErrors.add(new BBError(CorrespondenceFormFields.FILE,
									CorrespondenceFormFieldErrors.CORRESPONDENCE_INVALIDBOOKMARK_DOCUMENT_DESCRIPTION_MISMATCH));
						}
					} else {
						bbErrors.add(new BBError(CorrespondenceFormFields.FILE,
								CorrespondenceFormFieldErrors.CORRESPONDENCE_INVALIDBOOKMARK_CORRESPONDENCEFORM));
					}

				}
			}

		} catch (Exception exception) {
			LOGGER.error("Exception occured during validating manual correspondence data", exception);
			throw new ApplicationException(exception);
		}

		responseDTO.setErrors(bbErrors);
		return responseDTO;

	}

	/**
	 * Checks if is atleast one bookmark.
	 * @param file
	 *            the file path
	 * @return true, if is atleast one bookmark
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public boolean isAtleastOneBookmark(String file) throws IOException {

		if (PDFUtil.getBookmarksNames(file).isEmpty()) {
			return false;
		}

		return true;
	}

	/**
	 * Checks if is multiple bookmark.
	 * @param file
	 *            the file path
	 * @return true, if is multiple bookmark
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public boolean isMultipleBookmark(String file) throws IOException {
		if (PDFUtil.getBookmarksNames(file).isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if is valid bookmark type.
	 * @param file
	 *            the file path
	 * @return true, if is valid bookmark type
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public boolean isValidBookmarkType(String file) throws IOException {

		return PDFUtil.isValidPdfForBookMark(file, BookMarkType.B1)
				|| PDFUtil.isValidPdfForBookMark(file, BookMarkType.B2);

	}

	/**
	 * Creates the manual correspondence. This api does all the business validations and creates correspondence and
	 * saves the pdf file with name as correspondenceid.pdf at base location
	 * @param correspondenceFormDto
	 *            the correspondence form dto
	 * @return the response dto
	 * @throws ApplicationException
	 *             the application exception
	 */

	@Override
	@Transactional
	public ResponseDTO createManualCorrespondence(CorrespondenceFormDto correspondenceFormDto) {
		/*
		 * Transactional is aded to fetch customer from application
		 */
		ResponseDTO responseDTO = new ResponseDTO();
		List<BBError> bbErrors = responseDTO.getErrors();
		responseDTO.setStatus(ResponseStatus.ERROR);
		try {
			boolean isApplicationValid = false;
			ApplicationBase applicationBase = new ApplicationBase();
			String jurisdiction = correspondenceFormDto.getJurisdiction();
			String file = correspondenceFormDto.getFile();
			String applicationNumber = correspondenceFormDto.getApplicationNumber();

			if (!isValidJurisdiction(jurisdiction)) {
				bbErrors.add(new BBError(CorrespondenceFormFields.JURISDICTION,
						CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_JURISDICTION_INVALID));
			}

			String converted = null;
			if (WIPO_JURISDICTION.equalsIgnoreCase(jurisdiction)) {
				converted = getConvertedValueForWO(applicationNumber, NumberType.APPLICATION, jurisdiction);
			} else {
				converted = numberFormatValidationService.getConvertedValue(applicationNumber, NumberType.APPLICATION,
						jurisdiction, ApplicationType.ALL, null, null);
			}
			if (converted != null) {
				// check for existence
				applicationBase = applicationBaseDao.findByApplicationNoAndJurisdictionCode(jurisdiction, converted);
				if (applicationBase == null) {
					bbErrors.add(new BBError(CorrespondenceFormFields.APPLICATION_NUMBER,
							CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_NOTFOUND_APPLICATION));

				} else {
					if (applicationBaseDao.canAccessApplication(applicationBase.getId()) != null) {
						isApplicationValid = true;
					} else {
						bbErrors.add(new BBError(CorrespondenceFormFields.APPLICATION_NUMBER,
								CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_NOTACCESS_APPLICATION));
					}
				}
			} else {
				bbErrors.add(new BBError(CorrespondenceFormFields.APPLICATION_NUMBER,
						CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_APPLICATION_INVALID));
			}

			if (isApplicationValid && US_JURISDICTIION.equals(jurisdiction) || (WIPO_JURISDICTION.equals(jurisdiction)
					&& applicationNumber.startsWith(WIPO_APPLICATION_PREFIX))) {
				bbErrors.addAll(validateUSManualCorrespondenceData(correspondenceFormDto, applicationBase, converted)
						.getErrors());

			}
			if (bbErrors.isEmpty() && applicationBase != null) {

				CorrespondenceBase base = new CorrespondenceBase();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(correspondenceFormDto.getMailingDate());

				base.setMailingDate(calendar);
				base.setPageCount(PDFUtil.getPDFPageCount(file));
				base.setAttachmentSize(PDFUtil.getPDFsize(file));

				DocumentCode documentCode = new DocumentCode();
				documentCode.setId(correspondenceFormDto.getDocumentCode());
				base.setSource(Source.MANUAL);
				base.setSubSource(SubSourceTypes.SINGLE);
				base.setStatus(Status.ACTIVE);
				if (correspondenceFormDto.isUrgent()) {
					base.setStep(StepType.POST_OCR_VALIDATION);
					base.setStepStatus(StepStatus.COMPLETED);
				} else {
					base.setStep(StepType.OCR);
					base.setStepStatus(StepStatus.NEW);
				}
				base.setDocumentCode(documentCode);
				base.setApplication(applicationBase);
				base.setJurisdictionCode(jurisdiction);

				List<CorrespondenceBase> correspondenceBases = correspondenceDAOImpl
						.getCorrespondenceRecordByDuplicateCheckParameters(base.getApplication().getId(),
								base.getDocumentCode().getId(), base.getMailingDate(), base.getAttachmentSize(),
								base.getPageCount());
				if (CollectionUtils.isEmpty(correspondenceBases)) {
					CorrespondenceBase correspondence = (CorrespondenceBase) saveCorrespondenceBase(base);
					if (correspondence != null) {
						PDDocument doc = PDDocument.load(new File(file));
						String UPLOAD_DIRECTORY = FolderRelativePathEnum.CORRESPONDENCE
								.getAbsolutePath("base" + File.separator + correspondence.getId() + File.separator);
						File filepath = new File(UPLOAD_DIRECTORY);
						if (!filepath.exists()) {
							filepath.mkdirs();
						}
						doc.save(new File(UPLOAD_DIRECTORY + File.separator + correspondence.getId() + ".pdf"));
					}
				} else {
					bbErrors.add(new BBError(CorrespondenceFormFields.FILE,
							CorrespondenceFormFieldErrors.CORRESPONDENCE_ALREADYEXISTS));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		responseDTO.setErrors(bbErrors);
		if (responseDTO.getErrors().isEmpty()) {
			responseDTO.setStatus(ResponseStatus.SUCCESS);

		}
		return responseDTO;

	}

	/**
	 * Save correspondence base.
	 * @param correspondence
	 *            the correspondence
	 * @return the correspondence base
	 * @throws ApplicationException
	 *             the application exception
	 */

	@Override
	public CorrespondenceBase saveCorrespondenceBase(CorrespondenceBase correspondence) {
		return correspondenceBaseRepository.save(correspondence);
	}

	/**
	 * Checks if is valid jurisdiction.
	 * @param jurisdiction
	 *            the jurisdiction
	 * @return true, if is valid jurisdiction
	 */
	@Override
	public boolean isValidJurisdiction(String jurisdiction) {

		List<String> jurisdictions = jurisdictionService.findAllJurisdictionsValues();
		for (String jur : jurisdictions) {
			if (jurisdiction.equalsIgnoreCase(jur)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if is valid assignee.
	 * @param assignee
	 *            the assignee
	 * @return true, if is valid assignee
	 */
	@Override
	public boolean isValidAssignee(String assignee) {

		Set<Assignee> assignees = assigneeService
				.getAssigneesByIds(BlackboxSecurityContextHolder.getUserAuthData().getAssigneeIds());
		List<String> assigneeNames = new ArrayList<>();
		for (Assignee assignee2 : assignees) {
			assigneeNames.add(assignee2.getName());
		}
		return assigneeNames.contains(assignee);
	}

	/**
	 * Creates the application request.
	 * @param correspondenceFormDto
	 *            the correspondence form dto
	 * @return the response dto
	 * @throws ApplicationException
	 *             the application exception
	 */
	@Override
	@Transactional
	public ResponseDTO createApplicationRequest(CorrespondenceFormDto correspondenceFormDto) {

		ResponseDTO responseDTO = new ResponseDTO();
		List<BBError> bbErrors = responseDTO.getErrors();
		responseDTO.setStatus(ResponseStatus.ERROR);
		try {
			String jurisdiction = correspondenceFormDto.getJurisdiction();
			String applicationNumber = correspondenceFormDto.getApplicationNumber();
			if (!isValidJurisdiction(jurisdiction)) {
				bbErrors.add(new BBError(CorrespondenceFormFields.JURISDICTION,
						CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_JURISDICTION_INVALID));
			}
			if (StringUtils.isNotBlank(correspondenceFormDto.getAssignee())
					&& !isValidAssignee(correspondenceFormDto.getAssignee())) {
				bbErrors.add(new BBError(CorrespondenceFormFields.ASSIGNEE,
						CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_ASSIGNEE_INVALID));
			}
			String converted = null;
			if (WIPO_JURISDICTION.equalsIgnoreCase(jurisdiction)) {
				converted = getConvertedValueForWO(applicationNumber, NumberType.APPLICATION, jurisdiction);
			} else {
				converted = numberFormatValidationService.getConvertedValue(applicationNumber, NumberType.APPLICATION,
						jurisdiction, ApplicationType.ALL, null, null);
			}
			if (converted == null) {
				bbErrors.add(new BBError(CorrespondenceFormFields.APPLICATION_NUMBER,
						CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_APPLICATION_INVALID));
			}
			if (bbErrors.isEmpty()) {
				CorrespondenceStaging correspondenceStaging = new CorrespondenceStaging();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(correspondenceFormDto.getMailingDate());
				correspondenceStaging.setMailingDate(calendar);
				correspondenceStaging.setApplicationNumberUnFormatted(correspondenceFormDto.getApplicationNumber());
				correspondenceStaging.setApplicationNumber(BlackboxUtils
						.formatApplicationNumber(correspondenceFormDto.getApplicationNumber(), jurisdiction, null));
				DocumentCode documentCode = documentCodeRepository.findById(correspondenceFormDto.getDocumentCode());
				correspondenceStaging.setDocumentCode(documentCode.getCode());
				correspondenceStaging.setDocumentDescription(documentCode.getDescription());
				correspondenceStaging.setSource(Source.MANUAL);
				correspondenceStaging.setSubSource(SubSourceTypes.SINGLE);
				correspondenceStaging.setStatus(Status.CREATE_APPLICATION_REQUEST_PENDING);
				if (StringUtils.isNotBlank(correspondenceFormDto.getAssignee())) {
					correspondenceStaging.setAssignee(correspondenceFormDto.getAssignee());
				}
				correspondenceStaging.setPageCount(PDFUtil.getPDFPageCount(correspondenceFormDto.getFile()));
				correspondenceStaging.setAttachmentSize(PDFUtil.getPDFsize(correspondenceFormDto.getFile()));
				correspondenceStaging.setJurisdictionCode(jurisdiction);
				CorrespondenceStaging staging = correspondenceStagingRepository.save(correspondenceStaging);
				if (staging != null) {
					PDDocument doc = PDDocument.load(new File(correspondenceFormDto.getFile()));
					String uploadDir = FolderRelativePathEnum.CORRESPONDENCE
							.getAbsolutePath("staging" + File.separator + staging.getId() + File.separator);
					File filepath = new File(uploadDir);
					if (!filepath.exists()) {
						filepath.mkdirs();
					}

					doc.save(new File(uploadDir + File.separator + staging.getId() + ".pdf"));

					if (responseDTO.getErrors().isEmpty()) {
						responseDTO.setStatus(ResponseStatus.SUCCESS);
					}
					//TODO Need application here Gurteg/Mohit
					notificationService.createNotification(null, null, null, staging.getId(), 
							EntityName.CORRESPONDENCE_STAGING, 
							NotificationProcessType.CREATE_APPLICATION_REQUEST_USER_INITIATED, null, null);
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		responseDTO.setErrors(bbErrors);
		if (responseDTO.getErrors().isEmpty()) {
			responseDTO.setStatus(ResponseStatus.SUCCESS);
		}
		return responseDTO;

	}

	/**
	 * Search application and validates application number format and jurisdiction.
	 * @param correspondenceFormDto
	 *            the correspondence form dto
	 * @return the response dto
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseDTO searchApplication(CorrespondenceFormDto correspondenceFormDto) {
		ResponseDTO responseDTO = new ResponseDTO();
		List<BBError> bbErrors = responseDTO.getErrors();
		responseDTO.setStatus(ResponseStatus.ERROR);
		ApplicationBase applicationBase = new ApplicationBase();
		String jurisdiction = correspondenceFormDto.getJurisdiction();
		String applicationNumber = correspondenceFormDto.getApplicationNumber();
		if (!isValidJurisdiction(jurisdiction)) {
			bbErrors.add(new BBError(CorrespondenceFormFields.JURISDICTION,
					CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_JURISDICTION_INVALID));
		}
		String converted = null;
		if (WIPO_JURISDICTION.equalsIgnoreCase(jurisdiction)) {
			converted = getConvertedValueForWO(applicationNumber, NumberType.APPLICATION, jurisdiction);
		} else {
			converted = numberFormatValidationService.getConvertedValue(applicationNumber, NumberType.APPLICATION,
					jurisdiction, ApplicationType.ALL, null, null);
		}

		if (converted != null) {
			// check for existence
			applicationBase = applicationBaseDao.findByApplicationNoAndJurisdictionCode(jurisdiction, converted);
			if (applicationBase == null) {
				bbErrors.add(new BBError(CorrespondenceFormFields.APPLICATION_NUMBER,
						CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_NOTFOUND_APPLICATION));

			} else {
				if (applicationBaseDao.canAccessApplication(applicationBase.getId()) == null) {
					bbErrors.add(new BBError(CorrespondenceFormFields.APPLICATION_NUMBER,
							CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_NOTACCESS_APPLICATION));
				}
			}
		} else {
			bbErrors.add(new BBError(CorrespondenceFormFields.APPLICATION_NUMBER,
					CorrespondenceFormFieldErrors.CORRESPONDENCE_CREATEFORM_APPLICATION_INVALID));
		}

		responseDTO.setErrors(bbErrors);
		if (responseDTO.getErrors().isEmpty()) {
			responseDTO.setStatus(ResponseStatus.SUCCESS);
		}

		return responseDTO;

	}

	/*
	 * (non-Javadoc)
	 * @see com.blackbox.ids.services.correspondence.ICorrespondenceService#
	 * addEntryToJobs(com.blackbox.ids.core.model.correspondence.Job.Type)
	 */
	@Override
	@Transactional
	public Job addEntryToJobs(Job.Type jobType) {
		Job job = new Job();
		job.setType(jobType);
		job.setStatus(Job.Status.NEW);
		job.setComments("");
		job.setVersion(1);
		return jobDao.persist(job);
	}

	/*
	 * (non-Javadoc)
	 * @see com.blackbox.ids.services.correspondence.ICorrespondenceService#
	 * updateStatusInJobs(com.blackbox.ids.core.model.correspondence.Job)
	 */
	@Override
	@Transactional
	public boolean updateStatusInJobs(Job job) {
		return jobDao.updateJobStatus(job);
	}

	@Override
	public SearchResult<CorrespondenceRecordDTO> fetchUpdateRequestRecords(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo) {
		LOGGER.debug("fetch Update Request Records in Correspondence Action Item Tab");
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}
		return correspondenceDAO.fetchUpdateRequestRecords(filter, pageInfo);
	}

	@Override
	public SearchResult<CorrespondenceRecordDTO> fetchUploadRequestRecords(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo) {
		LOGGER.debug("fetch Upload Request Records in Correspondence Action Item Tab");
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}
		return correspondenceDAO.fetchUploadRequestRecords(filter, pageInfo);
	}

	@Override
	public CorrespondenceRecordDTO getCorrespondenceRecordDTO(Long correspondenceId) {
		return correspondenceDAO.getCorrespondenceRecordDTO(correspondenceId);
	}

	@Override
	@Transactional
	public void rejectDownloadOfficeNotification(Long notificationProcessId) {

		Set<Long> userRolesId = getLoggedInUserRolesId();
		if (CollectionUtils.isEmpty(userRolesId)) {
			throw new ApplicationException("User Roles are null.");
		}

		Long downloadOfficeId = notificationProcessDAO.getNotificationProcess(notificationProcessId,
				NotificationProcessType.CORRESPONDENCE_RECORD_FAILED_IN_DOWNLOAD_QUEUE,
				EntityName.DOWNLOAD_OFFICE_ACTION_QUEUE, userRolesId);
		if (downloadOfficeId != null) {
			downloadQDAO.changeStatusByIdAndStatus(downloadOfficeId, QueueStatus.CRAWLER_ERROR, QueueStatus.REJECTED);
			notificationService.completeTask(notificationProcessId, NotificationStatus.COMPLETED);
		}
	}

	/**
	 * Gets the logged in user roles id.
	 * @return the logged in user roles id
	 */
	private static Set<Long> getLoggedInUserRolesId() {
		Set<Long> userRolesId = null;

		BlackboxUser blackboxUser = BlackboxSecurityContextHolder.getUserDetails();
		if (blackboxUser != null) {
			userRolesId = blackboxUser.getRoleIds();
		}
		return userRolesId;
	}
	
	@Override
	@Transactional(readOnly = true)
	public SearchResult<CorrespondenceRecordDTO> filterActiveDocuments(final CorrespondenceRecordsFilter filter,
			final PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter Active Documents: Filter and pagination details are must.");
		}
		return correspondenceDAO.filterActiveDocuments(filter, pageInfo);
	}

	@Override
	@Transactional(readOnly = true)
	public SearchResult<CorrespondenceRecordDTO> filterInactiveDocuments(final CorrespondenceRecordsFilter filter,
			final PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter Inactive Documents: Filter and pagination details are must.");
		}
		return correspondenceDAO.filterInActiveDocuments(filter, pageInfo);
	}

	@Override
	public CorrespondenceRecordDTO getCorrespondenceRecord(final Long correspondenceId) {
		if (correspondenceId == null) {
			throw new IllegalArgumentException("Correspondence Id is must to fetch the respective Correspondence");
		}
		return correspondenceDAO.getCorrespondenceRecord(correspondenceId);
	}

	/**
	 * Delete correspondence record.
	 * @param correspondenceId
	 *            the correspondence id
	 * @param comments
	 *            the comments
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public long deleteCorrespondenceRecord(final Long correspondenceId, final String comments) {
		if (correspondenceId == null) {
			throw new IllegalArgumentException("Correspondence Id is must to delete the respective Correspondence");
		}
		notificationProcessService.deleteNotificationForCorrespondenceId(correspondenceId, comments);
		List<Long> referenceIdsList = referenceBaseDataRepository.fetchReferenceIdsByCorrespondenceId(correspondenceId);
		if (CollectionUtils.isNotEmpty(referenceIdsList)) {
			referenceService.removeReferences(referenceIdsList);
		}
		return correspondenceDAO.deleteCorrespondenceRecord(correspondenceId, comments);

	}

	@Override
	@Transactional(readOnly = true)
	public SearchResult<CorrespondenceRecordDTO> getActiveCorrespondences(final CorrespondenceRecordsFilter filter,
			final Long correspondenceId) {
		if (correspondenceId == null) {
			throw new IllegalArgumentException("Correspondence Id is must to fetch all respective correspondences");
		}
		return correspondenceDAO.getActiveCorrespondences(filter, correspondenceId);
	}

	@Override
	@Transactional(readOnly = true)
	public SearchResult<CorrespondenceRecordDTO> getInactiveCorrespondences(final CorrespondenceRecordsFilter filter,
			final Long correspondenceId) {
		if (correspondenceId == null) {
			throw new IllegalArgumentException(
					"Correspondence Id is must to fetch all respective inactive correspondences");
		}
		return correspondenceDAO.getInactiveCorrespondences(filter, correspondenceId);
	}

	@Override
	@Transactional
	public SearchResult<CorrespondenceRecordDTO> fetchAllTrackApplications(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo) {
		LOGGER.debug("fetch All Track Applications in Correspondence Track Application Tab");
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException(
					"Filter All Track Applications: Filter and pagination details are must.");
		}
		return correspondenceStagDAO.fetchAllTrackApplications(filter, pageInfo);
	}

	@Override
	@Transactional
	public SearchResult<CorrespondenceRecordDTO> fetchMyTrackApplications(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo) {
		LOGGER.debug("fetch My Track Applications in Correspondence Track Application Tab");
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter My Track Applications: Filter and pagination details are must.");
		}
		return correspondenceStagDAO.fetchMyTrackApplications(filter, pageInfo);
	}

	@Override
	public long fetchActionItemCount() {
		LOGGER.debug("fetch Action Item Records count in Correspondence");
		return correspondenceDAO.fetchUpdateRequestCount() + correspondenceDAO.fetchUploadRequestCount();
	}

	@Override
	public long fetchUpdateRequestCount() {
		LOGGER.debug("fetch Update Request Records count in Correspondence Action Item Tab");
		return correspondenceDAO.fetchUpdateRequestCount();
	}

	@Override
	public long fetchUploadRequestCount() {
		LOGGER.debug("fetch Upload Request Records count in Correspondence Action Item Tab");
		return correspondenceDAO.fetchUploadRequestCount();
	}

	@Override
	public String getConvertedValueForWO(String originalValue, NumberType numberType, String jurisdiction) {
		String converted = null;
		List<ApplicationType> woAppTypes = ApplicationType.woAppTypes();
		for (ApplicationType applicationType : woAppTypes) {
			converted = numberFormatValidationService.getConvertedValue(originalValue, NumberType.APPLICATION,
					jurisdiction, applicationType, null, null);
			if (converted != null) {
				return converted;
			}
		}

		return converted;
	}
}
