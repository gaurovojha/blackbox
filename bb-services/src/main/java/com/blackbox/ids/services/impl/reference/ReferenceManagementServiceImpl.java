package com.blackbox.ids.services.impl.reference;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.dto.reference.JurisdictionDTO;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.dto.reference.ReferenceFpDTO;
import com.blackbox.ids.core.dto.reference.ReferenceNplDTO;
import com.blackbox.ids.core.dto.reference.ReferencePusDTO;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceBaseFPData;
import com.blackbox.ids.core.model.reference.ReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.ReferenceBasePUSData;
import com.blackbox.ids.core.model.reference.ReferenceReviewLog;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.reference.ReferenceBaseDataRepository;
import com.blackbox.ids.core.repository.reference.ReferenceReviewLogRepository;
import com.blackbox.ids.core.repository.reference.ReferenceStagingDataRepository;
import com.blackbox.ids.core.repository.referenceflow.IDSReferenceFlowRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.services.reference.ReferenceManagementService;
import com.blackbox.ids.services.reference.ReferenceService;
import com.mysema.query.types.Predicate;

/**
 * The Class ReferenceManagementServiceImpl.
 *
 * @author nagarro
 */
@Service("referenceManagementService")
public class ReferenceManagementServiceImpl implements ReferenceManagementService {

	/** current user **/
	private static final String USER_ME = "Me";

	/** The log. */
	private final Logger log = Logger.getLogger(ReferenceManagementServiceImpl.class);

	/** The reference repository. */
	@Autowired
	private ReferenceBaseDataRepository<ReferenceBaseData> referenceBaseDataRepository;

	/** The reference repository. */
	@Autowired
	private ReferenceStagingDataRepository referenceBaseStagingDataRepository;

	/** The jurisdiction repository. */
	@Autowired
	private JurisdictionRepository judrisdictionRepository;

	/** The user custom repository. */
	@Autowired
	private UserRepository userRepository;

	/** The reference review log repository. */
	@Autowired
	private ReferenceReviewLogRepository referenceReviewLogRepository;

	/** The application base dao. */
	@Autowired
	private ApplicationBaseDAO applicationBaseDAO;

	/** The IDSReferenceFlowRepository. */
	@Autowired
	private IDSReferenceFlowRepository<IDSReferenceFlow> idsReferenceFlowRepository;

	/** The ReferenceService. */
	@Autowired
	private ReferenceService referenceService;

	/**
	 * To get the jurisdiction of the document.
	 *
	 * @param jurisdictionCode
	 *            the jurisdiction code
	 * @return the judrisdiction
	 */
	private JurisdictionDTO getJudrisdiction(final String jurisdictionCode) {

		Jurisdiction jurisdiction = new Jurisdiction();
		JurisdictionDTO juridictionDTO = new JurisdictionDTO();
		jurisdiction = judrisdictionRepository.findByJurisdictionValue(jurisdictionCode);
		juridictionDTO.setId(jurisdiction.getId());
		juridictionDTO.setName(jurisdiction.getCode());
		return juridictionDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.services.reference.ReferenceManagementService#getAllRecords(com.mysema.query.types.Predicate,
	 * org.springframework.data.domain.Pageable)
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<CorrespondenceDTO> getAllRecords(final Predicate predicate, final Pageable pageable) {
		Page<CorrespondenceDTO> correspondenceDatePage = referenceBaseDataRepository.filterCorrespondenceData(predicate,
				pageable);
		return correspondenceDatePage;
	}

	/**
	 * Gets the references by correspondence.
	 *
	 * @param corrId
	 *            the corr id
	 * @return the references by correspondence
	 */
	@Override
	@Transactional(readOnly = true)
	public ReferenceBaseDTO getReferencesByCorrespondence(Long corrId) {

		log.debug("Fetching all references by correspondence id");
		List<ReferenceBaseData> referenceBaseDataList = referenceBaseDataRepository
				.fetchReferencesByCorrespondenceId(corrId);
		List<ReferenceFpDTO> fpDtoList = new ArrayList<>();
		List<ReferencePusDTO> pusDtoList = new ArrayList<>();
		List<ReferenceNplDTO> nplDtoList = new ArrayList<>();
		Map<String, String> referenceEnteredByDetails = new HashMap<>();
		ReferenceBaseDTO referenceDto = new ReferenceBaseDTO();

		for (ReferenceBaseData referenceData : referenceBaseDataList) {

			String user = userRepository.getUserFullName(userRepository.getEmailId(referenceData.getCreatedByUser()));
			String date = BlackboxDateUtil.calendarToStr(referenceData.getCreatedDate(), TimestampFormat.MMMDDYYYY);
			referenceEnteredByDetails.put(user, date);

			if (referenceData instanceof ReferenceBaseFPData) {
				ReferenceBaseFPData baseData = (ReferenceBaseFPData) referenceData;
				ReferenceFpDTO dto = populateFPDTO(baseData);
				fpDtoList.add(dto);
			}

			if (referenceData instanceof ReferenceBaseNPLData) {
				ReferenceBaseNPLData baseData = (ReferenceBaseNPLData) referenceData;
				ReferenceNplDTO dto = populateNPLDTO(baseData);
				nplDtoList.add(dto);
			}

			if (referenceData instanceof ReferenceBasePUSData) {
				ReferenceBasePUSData baseData = (ReferenceBasePUSData) referenceData;
				ReferencePusDTO dto = populatePUSDTO(baseData);
				pusDtoList.add(dto);
			}
		}

		// add common values specific to correspondence
		if (referenceBaseDataList != null && CollectionUtils.isNotEmpty(referenceBaseDataList)) {
			referenceDto.setApplicationNumber(referenceBaseDataList.get(0).getApplication().getApplicationNumber());
			referenceDto.setJurisdiction(getJudrisdiction(referenceBaseDataList.get(0).getJurisdiction().getCode()));
			referenceDto.setDocumentDescription(
					referenceBaseDataList.get(0).getCorrespondence().getDocumentCode().getDescription());
			referenceDto.setMailingDateStr(BlackboxDateUtil.calendarToStr(
					referenceBaseDataList.get(0).getCorrespondence().getMailingDate(), TimestampFormat.MMMDDYYYY));
			referenceDto.setApplicationId(referenceBaseDataList.get(0).getApplication().getId());
			referenceDto.setApplicationJurisdictionType(
					referenceBaseDataList.get(0).getApplication().getJurisdiction().getCode());
			referenceDto.setApplicationJurisdictionCode(
					referenceBaseDataList.get(0).getApplication().getJurisdiction().getCode());
		}

		// Create and set correspondence dto id
		CorrespondenceDTO corrDto = new CorrespondenceDTO();
		corrDto.setId(corrId);
		referenceDto.setCorrespondenceId(corrDto);

		referenceDto.setReferenceFpData(fpDtoList);
		referenceDto.setReferenceNplData(nplDtoList);
		referenceDto.setReferencePusData(pusDtoList);

		// additional values to be displayed
		referenceDto.setReferenceEnteredByDetails(referenceEnteredByDetails);
		setReviewedByDetail(referenceDto);

		return referenceDto;

	}

	/**
	 * Sets the reviewed by detail.
	 *
	 * @param referenceDto
	 *            the new reviewed by detail
	 */
	private void setReviewedByDetail(ReferenceBaseDTO referenceDto) {
		List<ReferenceReviewLog> reviewLog = referenceReviewLogRepository
				.findByReferenceBaseDataId(referenceDto.getReferenceBaseDataId());
		if (reviewLog != null && CollectionUtils.isNotEmpty(reviewLog)) {
			String user = userRepository
					.getUserFullName(userRepository.getEmailId(reviewLog.get(0).getCreatedByUser()));
			String date = BlackboxDateUtil.calendarToStr(reviewLog.get(0).getCreatedDate(), TimestampFormat.MMMDDYYYY);
			referenceDto.setReferenceReviewedBy(user);
			referenceDto.setReferenceReviewedDate(date);
		} else {
			referenceDto.setReferenceReviewedBy("-");
			referenceDto.setReferenceReviewedDate("-");
		}
	}

	/**
	 * Populate PUS DTO.
	 *
	 * @param baseData
	 *            the base data
	 * @return the reference pus dto
	 */
	private ReferencePusDTO populatePUSDTO(ReferenceBasePUSData baseData) {
		ReferencePusDTO dto = new ReferencePusDTO();
		dto.setId(baseData.getReferenceBaseDataId());
		dto.setApplicantName(baseData.getApplicantName());
		dto.setKindCode(baseData.getKindCode());
		if (baseData.getPublicationDate() != null) {
			dto.setPublicationDate(
					BlackboxDateUtil.calendarToStr(baseData.getPublicationDate(), TimestampFormat.MMMDDYYYY));
		}
		dto.setConvertedPublicationNumber(baseData.getConvertedPublicationNumber());
		dto.setPublicationNumber(baseData.getPublicationNumber());
		dto.setComment(baseData.getReferenceComments());
		return dto;
	}

	/**
	 * Populate npldto.
	 *
	 * @param baseData
	 *            the base data
	 * @return the reference npl dto
	 */
	private ReferenceNplDTO populateNPLDTO(ReferenceBaseNPLData baseData) {
		ReferenceNplDTO dto = new ReferenceNplDTO();
		dto.setId(baseData.getReferenceBaseDataId());
		dto.setApplicationSerialNumber(baseData.getApplicationSerialNumber());
		dto.setPublicationDetail(baseData.getPublicationDetail());
		dto.setTitle(baseData.getTitle());
		dto.setAuthor(baseData.getAuthor());
		dto.setRelevantPages(baseData.getRelevantPages());
		dto.setStringData(baseData.getStringData());
		dto.setIssueNumber(baseData.getVolumeNumber());
		dto.setURL(baseData.getURL());
		dto.setAttachment(baseData.isAttachment());
		return dto;
	}

	/**
	 * Populate FP DTO
	 * 
	 * @param baseData
	 *            the base data
	 * @return the reference fp dto
	 */
	private ReferenceFpDTO populateFPDTO(ReferenceBaseFPData baseData) {
		ReferenceFpDTO dto = new ReferenceFpDTO();
		dto.setJurisdiction(baseData.getJurisdiction().getCode());
		dto.setId(baseData.getReferenceBaseDataId());
		dto.setApplicantName(baseData.getApplicantName());
		dto.setForeignDocumentNumber(baseData.getForeignDocumentNumber());
		dto.setConvertedForeignDocumentNumber(baseData.getConvertedForeignDocumentNumber());
		dto.setKindCode(baseData.getKindCode());
		dto.setAttachment(baseData.isAttachment());
		dto.setComment(baseData.getReferenceComments());
		return dto;
	}

	/**
	 * (non-Javadoc).
	 *
	 * @param predicate
	 *            the predicate
	 * @param pageable
	 *            the pageable
	 * @return the pending references
	 * @throws ApplicationException
	 *             the application exception
	 * @see com.blackbox.ids.services.reference.ReferenceManagementService#getPendingReferences(com.mysema.query.types.Predicate,
	 *      org.springframework.data.domain.Pageable)
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<CorrespondenceDTO> getPendingReferences(final Predicate predicate, final Pageable pageable) {

		Page<CorrespondenceDTO> refPendDtoList = referenceBaseStagingDataRepository.filterPendingRecords(predicate,
				pageable);
		return refPendDtoList;
	}

	/**
	 * (non-Javadoc).
	 *
	 * @param jurisCode
	 *            the juris code
	 * @param appNo
	 *            the app no
	 * @return the correspondence
	 * @throws ApplicationException
	 *             the application exception
	 * @see com.blackbox.ids.services.reference.ReferenceManagementService#getCorrespondence(java.lang.Long,
	 *      java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CorrespondenceDTO> getCorrespondence(final String jurisCode, final String appNo) {
		List<CorrespondenceDTO> dtoList = referenceBaseDataRepository.fetchCorrespondenceData(jurisCode, appNo);
		return dtoList;
	}

	/**
	 * Update reviewed references.
	 *
	 * @param referenceIds
	 *            the reference ids
	 * @param updatedByUser
	 *            the updated by user
	 * @return true, if successful
	 * @throws ApplicationException
	 *             the application exception
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean updateReviewedReference(final Long referenceBaseDataId, final String comments) {

		log.debug("Updating Review References");
		ReferenceReviewLog referenceReviewLog = new ReferenceReviewLog();
		referenceReviewLog.setReferenceBaseDataId(referenceBaseDataId);
		referenceReviewLogRepository.save(referenceReviewLog);

		// update review status for given reference
		referenceBaseDataRepository.updateReviewedStatus(true, referenceBaseDataId);

		return true;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean updateReviewedReferences(final long[] referenceBaseDataId) {

		for(long id : referenceBaseDataId) {
			this.updateReviewedReference(id, null);
		}
		return true;
	}
	
	/**
	 * Gets the correspondence from staging.
	 *
	 * @param jurisCode
	 *            the juris code
	 * @param appNo
	 *            the app no
	 * @return the correspondence from staging
	 * @throws ApplicationException
	 *             the application exception
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CorrespondenceDTO> getCorrespondenceFromStaging(final String jurisCode, final String appNo) {
		List<CorrespondenceDTO> dtoList = referenceBaseStagingDataRepository.fetchCorrespondenceData(jurisCode, appNo);
		return dtoList;
	}

	/**
	 * Gets the correspondence by id.
	 *
	 * @param corrId
	 *            the corr id
	 * @return the correspondence by id
	 * @throws ApplicationException
	 *             the application exception
	 */
	@Override
	@Transactional(readOnly = true)
	public CorrespondenceDTO getCorrespondenceById(Long corrId) {
		return referenceBaseStagingDataRepository.findCorrespondenceDataById(corrId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.services.reference.ReferenceManagementService#getReferenceWithApplicationDetails(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public ReferenceBaseDTO getReferenceWithApplicationDetails(Long correspondenceId) {
		ReferenceBaseDTO referenceBaseDTO = this.getReferencesByCorrespondence(correspondenceId);
		Long applicationId = referenceBaseDTO.getApplicationId();

		if (applicationId != null) {
			ApplicationBase application = applicationBaseDAO.getApplicationBase(applicationId);
			populateReferenceDTO(application, referenceBaseDTO);
		}

		else {
			log.debug("No application found for the given Application ID");
		}

		return referenceBaseDTO;
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
	private ReferenceBaseDTO populateReferenceDTO(ApplicationBase application, ReferenceBaseDTO referenceBaseDTO) {

		referenceBaseDTO.setApplicationFilingDate(
				BlackboxDateUtil.calendarToStr(application.getAppDetails().getFilingDate(), TimestampFormat.MMMDDYYYY));
		referenceBaseDTO.setApplicationIssuedOn(
				BlackboxDateUtil.calendarToStr(Calendar.getInstance(), TimestampFormat.MMMDDYYYY));
		referenceBaseDTO.setTypeOfNumber("Publication");
		referenceBaseDTO.setApplicationJurisdictionType(application.getJurisdiction().getCode());

		return referenceBaseDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.reference.ReferenceManagementService#isReferenceExist(java.lang.Long)
	 */
	@Override
	public boolean isReferenceExist(Long corrId) {
		Long count = referenceBaseDataRepository.getReferenceCountbyCorrepondenceId(corrId);
		return count > 0 ? true : false;
	}

	@Override
	@Transactional(readOnly = true)
	public ReferenceBaseDTO getReferenceWithApplicationDetailsAndFlowStatus(Long correspondenceId) {
		ReferenceBaseDTO referenceBaseDTO = this.getReferenceWithApplicationDetails(correspondenceId);
		populateReferenceDTOWithFlowAndIDSData(referenceBaseDTO);
		return referenceBaseDTO;
	}

	/**
	 * populates dto with reference flow data
	 * 
	 * @param referenceBaseDTO
	 */
	private void populateReferenceDTOWithFlowAndIDSData(ReferenceBaseDTO referenceBaseDTO) {

		for (ReferencePusDTO refDTO : referenceBaseDTO.getReferencePusData()) {
			ReferenceBaseData refData = new ReferenceBaseData();
			refData.setReferenceBaseDataId(refDTO.getId());
			IDSReferenceFlow refFlow = idsReferenceFlowRepository.findByReferenceBaseDataAndReferenceFlowStatus(refData,
					ReferenceFlowStatus.DROPPED);
			if (refFlow != null) {
				String date = refFlow.getFilingDate() != null
						? BlackboxDateUtil.calendarToStr(refFlow.getFilingDate(), TimestampFormat.MMMDDYYYY) : null;
				refDTO.setFilingDate(date);
				refDTO.setFlowStatus(
						refFlow.getReferenceFlowStatus() != null ? refFlow.getReferenceFlowStatus().name() : null);
				refDTO.setDoNotFile(refFlow.getDoNotFile());

				if (refFlow.getIds() != null) {
					refDTO.setIdsId(refFlow.getIds().getId());
					refDTO.setIdsStatus(refFlow.getIds().getStatus().name());
				}
			}
			refDTO.setReferenceReviewedDetails(this.getReviewDetails(refDTO.getId()));
		}

		for (ReferenceFpDTO refDTO : referenceBaseDTO.getReferenceFpData()) {
			ReferenceBaseData refData = new ReferenceBaseData();
			refData.setReferenceBaseDataId(refDTO.getId());
			IDSReferenceFlow refFlow = idsReferenceFlowRepository.findByReferenceBaseDataAndReferenceFlowStatus(refData,
					ReferenceFlowStatus.DROPPED);
			if (refFlow != null) {
				String date = refFlow.getFilingDate() != null
						? BlackboxDateUtil.calendarToStr(refFlow.getFilingDate(), TimestampFormat.MMMDDYYYY) : null;
				refDTO.setFilingDate(date);
				refDTO.setFlowStatus(
						refFlow.getReferenceFlowStatus() != null ? refFlow.getReferenceFlowStatus().name() : null);
				refDTO.setDoNotFile(refFlow.getDoNotFile());

				if (refFlow.getIds() != null) {
					refDTO.setIdsId(refFlow.getIds().getId());
					refDTO.setIdsStatus(refFlow.getIds().getStatus().name());
				}
			}
			refDTO.setReferenceReviewedDetails(this.getReviewDetails(refDTO.getId()));
		}

		for (ReferenceNplDTO refDTO : referenceBaseDTO.getReferenceNplData()) {
			ReferenceBaseData refData = new ReferenceBaseData();
			refData.setReferenceBaseDataId(refDTO.getId());
			IDSReferenceFlow refFlow = idsReferenceFlowRepository.findByReferenceBaseDataAndReferenceFlowStatus(refData,
					ReferenceFlowStatus.DROPPED);
			if (refFlow != null) {
				String date = refFlow.getFilingDate() != null
						? BlackboxDateUtil.calendarToStr(refFlow.getFilingDate(), TimestampFormat.MMMDDYYYY) : null;
				refDTO.setFilingDate(date);
				refDTO.setFlowStatus(
						refFlow.getReferenceFlowStatus() != null ? refFlow.getReferenceFlowStatus().name() : null);
				refDTO.setDoNotFile(refFlow.getDoNotFile());

				if (refFlow.getIds() != null) {
					refDTO.setIdsId(refFlow.getIds().getId());
					refDTO.setIdsStatus(refFlow.getIds().getStatus().name());
				}
			}
			refDTO.setReferenceReviewedDetails(this.getReviewDetails(refDTO.getId()));
		}
	}

	/**
	 * populate dto with review details
	 * 
	 * @param refDTO
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, String> getReviewDetails(Long refId) {

		Map<String, String> reviewDetails = new HashMap<>();
		List<ReferenceReviewLog> reviewLogList = referenceReviewLogRepository.findByReferenceBaseDataId(refId);

		for (ReferenceReviewLog reviewLog : reviewLogList) {
			String user = userRepository.getUserFullName(userRepository.getEmailId(reviewLog.getCreatedByUser()));
			Long currentId = BlackboxSecurityContextHolder.getUserId();
			String currentUser = userRepository.getUserFullName(userRepository.getEmailId(currentId));
			String date = reviewLog.getCreatedDate() != null
					? BlackboxDateUtil.calendarToStr(reviewLog.getCreatedDate(), TimestampFormat.MMMDDYYYY) : null;
			if (user.equalsIgnoreCase(currentUser)) {
				user = USER_ME;
			}
			reviewDetails.put(user, date);
		}
		return reviewDetails;
	}

	@Override
	@Transactional
	public void updateComments(final Long referenceBaseDataId, final String comments) {
		referenceBaseDataRepository.updateComment(referenceBaseDataId, comments);
	}

	@Override
	@Transactional
	public void updateTranslationFlag(final Long referenceBaseDataId, final Boolean flag) {
		referenceBaseDataRepository.updateTranslationFlagk(referenceBaseDataId, flag);
	}

	@Override
	@Transactional
	public boolean deleteAttachment(final Long referenceId, final ReferenceType referenceType) {
		String filePath = referenceService.getReferenceAttachmentUploadPath(referenceId, referenceType);
		File file = new File(filePath + ".pdf");
		file.delete();

		// also update attachment flag in reference base
		referenceBaseDataRepository.updateAttachmentFlag(referenceId, false);
		return true;
	}
}