package com.blackbox.ids.services.impl.reference;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.dto.reference.ReferenceDashboardDto;
import com.blackbox.ids.core.dto.reference.ReferenceEntryDTO;
import com.blackbox.ids.core.dto.reference.ReferenceEntryFilter;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceBaseRepository;
import com.blackbox.ids.core.repository.notification.process.NotificationProcessRepository;
import com.blackbox.ids.core.repository.reference.OcrDataRepository;
import com.blackbox.ids.core.repository.reference.ReferenceBaseDataRepository;
import com.blackbox.ids.core.repository.reference.ReferenceStagingDataRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.reference.ReferenceDashboardService;
import com.blackbox.ids.services.reference.ReferenceService;
import com.mysema.query.types.Predicate;

/**
 * The Class ReferenceDashboardServiceImpl.
 *
 * @author nagarro
 */
@Service
public class ReferenceDashboardServiceImpl implements ReferenceDashboardService {

	/** Reference publication constant **/
	private static final String REFERENCE_PUBLICATION = "Publication";

	/** The reference base data repository. */
	@SuppressWarnings("rawtypes")
	@Autowired
	private ReferenceBaseDataRepository referenceBaseDataRepository;

	/** The reference staging data repository. */
	@Autowired
	private ReferenceStagingDataRepository referenceStagingDataRepository;

	/** The ocr data repository. */
	@Autowired
	private OcrDataRepository ocrDataRepository;

	/** The notification process repository. */
	@Autowired
	private NotificationProcessRepository notificationProcessRepository;

	/** The notification process service. */
	@Autowired
	private NotificationProcessService notificationProcessService;

	/** The reference service. */
	@Autowired
	private ReferenceService referenceService;

	/** The CorrepondenceBase repository. */
	@Autowired
	private CorrespondenceBaseRepository correspondenceBaseRepository;

	/** The application base dao. */
	@Autowired
	private ApplicationBaseDAO applicationBaseDAO;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.services.reference.ReferenceDashboardService#getUpdaterRefData(com.mysema.query.types.Predicate,
	 * org.springframework.data.domain.Pageable)
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<ReferenceDashboardDto> getUpdaterRefData(Predicate predicate, Pageable pageable) {
		return notificationProcessRepository.getUpdateReferenceDtos(predicate, pageable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.services.reference.ReferenceDashboardService#getDuplicateCheckRefData(com.mysema.query.types.
	 * Predicate, org.springframework.data.domain.Pageable)
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<ReferenceDashboardDto> getDuplicateCheckRefData(Predicate predicate, Pageable pageable) {
		return notificationProcessRepository.getDuplicateNplReferenceDtos(predicate, pageable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.services.reference.ReferenceDashboardService#getRefEntryData(com.blackbox.ids.core.dto.reference
	 * .ReferenceEntryFilter, org.springframework.data.domain.Pageable)
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<ReferenceEntryDTO> getRefEntryData(ReferenceEntryFilter filter, Pageable pageable) {
		return ocrDataRepository.getReferenceEntryData(filter, pageable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.reference.ReferenceDashboardService#fetchAndLockNotification(java.lang.Long,
	 * java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional
	public ReferenceEntryDTO fetchAndLockNotification(Long notificationProcessId, Long ocrDataId, Long lockedByUser) {
		ReferenceEntryDTO referenceEntryDTO = null;

		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());

		notificationProcessId = validateNotification(notificationProcessId);
		// this is to check if a user is authorised to perform this operation or not.
		if (notificationProcessId != null) {
			notificationProcessRepository.assignNotificationProcess(new User(lockedByUser), currentDate, lockedByUser,
					notificationProcessId);

			referenceEntryDTO = ocrDataRepository
					.getReferenceDataByNotificationProcessIdAndOcrDataId(notificationProcessId, ocrDataId);
		}

		return referenceEntryDTO;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.reference.ReferenceDashboardService#fetchNotification(java.lang.Long,
	 * java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public ReferenceEntryDTO fetchNotification(Long notificationProcessId, Long ocrDataId, Long lockedByUser) {
		ReferenceEntryDTO referenceEntryDTO = null;

		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());

	//	notificationProcessId = validateNotification(notificationProcessId);
		// this is to check if a user is authorised to perform this operation or not.
		if (notificationProcessId != null) {
			referenceEntryDTO = ocrDataRepository
					.getReferenceDataByNotificationProcessIdAndOcrDataId(notificationProcessId, ocrDataId);
		}

		return populateApplicationDetails(referenceEntryDTO);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.reference.ReferenceDashboardService#getUpdateReferenceCounts()
	 */
	/*
	 * public Long getAllReferences(Long notificationProcessId) {
	 * 
	 * Long count = null; notificationProcessId = validateNotification(notificationProcessId);
	 * 
	 * if (notificationProcessId != null) { count =
	 * ocrDataRepository.getReferenceDataListByNotificationProcess(notificationProcessId); } else {
	 * 
	 * // TODO show error on client, doesn't have access. }
	 * 
	 * return count; }
	 */
	@Override
	@Transactional(readOnly = true)
	public long getUpdateReferenceCounts() {
		return notificationProcessRepository.getUpdateReferenceCounts();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.reference.ReferenceDashboardService#getDuplicateNplReferenceCounts()
	 */
	@Override
	@Transactional(readOnly = true)
	public long getDuplicateNplReferenceCounts() {
		return notificationProcessRepository.getDuplicateNplReferenceCounts();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.reference.ReferenceDashboardService#getReferenceEntryCount()
	 */
	@Override
	@Transactional(readOnly = true)
	public Long getReferenceEntryCount() {
		return ocrDataRepository.getReferenceEntryCount();
	}

	/**
	 * Validate notification.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @return the long
	 */
	private Long validateNotification(Long notificationProcessId) {

		Set<Long> roleList = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();

		Long notificationProcess = notificationProcessRepository.getNotificationsByIdAndRole(notificationProcessId,
				roleList);

		return notificationProcess;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.reference.ReferenceDashboardService#getCorrespondenceId(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Long getCorrespondenceId(Long notificationProcessId, Long ocrDataId) {
		Long correspondenceId = null;
		ReferenceDashboardDto referenceDTO = notificationProcessRepository
				.getCorrespondenceFromNotification(notificationProcessId, ocrDataId);
		if (referenceDTO != null) {
			correspondenceId = referenceDTO.getCorrespondenceId();
		}
		return correspondenceId;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.reference.ReferenceDashboardService#getDuplicateNPLReference(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public ReferenceDashboardDto getDuplicateNPLReference(Long referenceBaseDataId) {
		return referenceBaseDataRepository.getDuplicateNPLReferenceById(referenceBaseDataId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.reference.ReferenceDashboardService#deleteUpdateRefFromStaging(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	@Transactional
	public boolean deleteUpdateRefFromStaging(Long refStagingId, Long notificationProcessId) {
		referenceStagingDataRepository.deleteReferenceStatingData(BlackboxSecurityContextHolder.getUserId(),
				Calendar.getInstance(), refStagingId);
		notificationProcessService.completeTask(notificationProcessId, NotificationStatus.COMPLETED);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.reference.ReferenceDashboardService#getUpdateRefDto(java.lang.Long)
	 */
	@Override
	@Transactional
	public ReferenceDashboardDto getUpdateRefDto(Long notificationProcessId) {
		return notificationProcessRepository.getUpdateReferenceDto(notificationProcessId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.reference.ReferenceDashboardService#addReferenceDetails(com.blackbox.ids.core.dto.
	 * reference.ReferenceBaseDTO)
	 */
	@Override
	@Transactional
	public void addReferenceDetails(ReferenceBaseDTO referenceDto) {
		referenceService.addReference(referenceDto);
		notificationProcessService.completeTask(referenceDto.getNotificationProcessId(), NotificationStatus.COMPLETED);
		referenceStagingDataRepository.deleteReferenceStatingData(BlackboxSecurityContextHolder.getUserId(),
				Calendar.getInstance(), referenceDto.getRefStagingId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.services.reference.ReferenceDashboardService#createReferenceAndCloseNotification(com.blackbox.
	 * ids.core.dto.reference.ReferenceBaseDTO, java.lang.Long)
	 */
	@Override
	@Transactional
	public ReferenceBaseDTO createReferenceAndCloseNotification(ReferenceBaseDTO referenceDTO,
			Long notificationProcessId) throws ApplicationException {
		ReferenceBaseDTO referenceBaseDTO = referenceService.createReference(referenceDTO);
		notificationProcessService.completeTask(notificationProcessId, NotificationStatus.COMPLETED);
		return referenceBaseDTO;
	}

	@Override
	@Transactional
	public void createStagingReferenceAndCloseNotification(ReferenceBaseDTO referenceDTO, Long notificationProcessId)
			throws ApplicationException {
		referenceService.addReferenceStagingData(referenceDTO);
		notificationProcessService.completeTask(notificationProcessId, NotificationStatus.COMPLETED);
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.services.reference.ReferenceDashboardService#createNullReferenceDocumentAndCloseNotification(
	 * java.lang.Long, java.lang.String, java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional
	public void createNullReferenceDocumentAndCloseNotification(Long correspondenceid, String nplString, Long ocrDataId,
			Long notificationProcessId) {
		referenceService.updateNullReference(correspondenceid, true, ocrDataId, nplString);
		notificationProcessService.completeTask(notificationProcessId, NotificationStatus.COMPLETED);

	}

	@Override
	@Transactional(readOnly = true)
	public String createNPLStringForNullReferenceDoc(Long correspondenceid) {

		CorrespondenceBase correspondenceBase = correspondenceBaseRepository.findOne(correspondenceid);

		return createNPLString(correspondenceBase.getJurisdictionCode(),
				correspondenceBase.getApplication().getApplicationNumber(),
				correspondenceBase.getDocumentCode().getDescription(), correspondenceBase.getMailingDate());

	}

	private String createNPLString(String jurisdictionCode, String applicationNumber, String documentDescription,
			Calendar mailingDate) {

		StringBuilder sb = new StringBuilder();
		sb.append(jurisdictionCode);
		sb.append(applicationNumber);
		sb.append(documentDescription);
		String mailingDateStr = BlackboxDateUtil.calendarToStr(mailingDate, TimestampFormat.FULL_DATE_TIME);
		sb.append(mailingDateStr);

		return sb.toString();
	}

	/**
	 * Populate reference dto by the details of application.
	 *
	 * @param application
	 *            the application object
	 * @param referenceBaseDTO
	 *            the reference base dto
	 * @return ReferenceBaseDTO with populated application fields
	 */
	private ReferenceEntryDTO populateApplicationDetails(ReferenceEntryDTO referenceEntryDTO) {

		ApplicationBase application = null;
		if (referenceEntryDTO.getApplicationNumber() != null) {
			application = applicationBaseDAO.findByApplicationNoAndJurisdictionCode(
					referenceEntryDTO.getJurisdictionCode(), referenceEntryDTO.getApplicationNumber());
		}

		if (application != null) {
			referenceEntryDTO.setApplicationFilingDate(BlackboxDateUtil
					.calendarToStr(application.getAppDetails().getFilingDate(), TimestampFormat.YYYYMMDD));
			referenceEntryDTO.setApplicationIssuedOn(
					BlackboxDateUtil.calendarToStr(Calendar.getInstance(), TimestampFormat.YYYYMMDD));
			referenceEntryDTO.setTypeOfNumber(REFERENCE_PUBLICATION);

		}
		return referenceEntryDTO;
	}
}
