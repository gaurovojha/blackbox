package com.blackbox.ids.services.impl.reference;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.blackbox.ids.common.constant.Constant;
import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.mstr.JurisdictionDAO;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.dto.reference.DuplicateNPLAttributes;
import com.blackbox.ids.core.dto.reference.JurisdictionDTO;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.dto.reference.ReferenceDashboardDto;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.model.reference.NullReferenceDocument;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceBaseFPData;
import com.blackbox.ids.core.model.reference.ReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.ReferenceBasePUSData;
import com.blackbox.ids.core.model.reference.ReferenceCategoryType;
import com.blackbox.ids.core.model.reference.ReferenceSourceType;
import com.blackbox.ids.core.model.reference.ReferenceStagingData;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.blackbox.ids.core.model.reference.ReferenceSubSourceType;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.reference.SourceReference;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceBaseRepository;
import com.blackbox.ids.core.repository.reference.NullReferenceDocumentRepository;
import com.blackbox.ids.core.repository.reference.ReferenceBaseDataRepository;
import com.blackbox.ids.core.repository.reference.ReferenceStagingDataRepository;
import com.blackbox.ids.core.repository.reference.SourceReferenceRepository;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.reference.OcrDataService;
import com.blackbox.ids.services.reference.ReferenceService;
import com.blackbox.ids.services.referenceflow.FlowChangeType;
import com.blackbox.ids.services.referenceflow.ReferenceChangeDesciption;
import com.blackbox.ids.services.referenceflow.ReferenceFlowEngine;
import com.blackbox.ids.services.validation.NumberFormatValidationService;
import com.blackbox.ids.util.ReferenceUtil;

/**
 * The Class ReferenceServiceImpl.
 *
 * @author nagarro
 */
@Service("referenceService")
public class ReferenceServiceImpl implements ReferenceService {

	/** The reference base data repository. */
	@Autowired
	ReferenceBaseDataRepository<ReferenceBaseData> referenceBaseDataRepository;

	/** The reference staging data repository. */
	@Autowired
	ReferenceStagingDataRepository referenceStagingDataRepository;

	/** The correspondence base repository. */
	@Autowired
	CorrespondenceBaseRepository correspondenceBaseRepository;

	/** The jurisdiction repository. */
	@Autowired
	JurisdictionRepository jurisdictionRepository;

	/** The user repository. */
	@Autowired
	UserRepository userRepository;

	/** The null reference document repository. */
	@Autowired
	NullReferenceDocumentRepository nullReferenceDocumentRepository;

	/** The number format validation service. */
	@Autowired
	NumberFormatValidationService numberFormatValidationService;

	/** The source reference repository. */
	@Autowired
	SourceReferenceRepository sourceReferenceRepository;

	/** The notification process service. */
	@Autowired
	NotificationProcessService notificationProcessService;

	/** The application base dao. */
	@Autowired
	ApplicationBaseDAO applicationBaseDAO;

	/** The ocr dataservice. */
	@Autowired
	OcrDataService ocrDataservice;

	/** The jurisdiction dao. */
	@Autowired
	JurisdictionDAO jurisdictionDAO;

	/** The reference util. */
	@Autowired
	ReferenceUtil referenceUtil;

	/** The model mapper. */
	@Autowired
	ModelMapper modelMapper;

	/** The npl min percentage. */
	@Value("${npl.min.percentage}")
	private String nplMinPercentage;

	/** The npl max percentage. */
	@Value("${npl.max.percentage}")
	private String nplMaxPercentage;

	@Autowired
	@Qualifier("refBaseChangeFlowEngine")
	public ReferenceFlowEngine referenceFlowEngine;

	private static final String NULL_REFENCE_MESSAGE = "ReferenceDTO is null. Can't create new reference.";
	private static final String INVALID_REFENCE_MESSAGE = "Invalid Reference Type";

	/** The log. **/
	private static final Logger log = Logger.getLogger(ReferenceServiceImpl.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see com.blackbox.ids.services.reference.ReferenceService#createReference(com.blackbox.ids.core.dto.reference.
	 * ReferenceBaseDTO)
	 */
	@Override
	@Transactional
	public ReferenceBaseDTO createReference(final ReferenceBaseDTO referenceDTO) {

		log.info("creating new reference : " + referenceDTO);
		ReferenceBaseData referenceBaseData = null;
		if (referenceDTO != null) {
			CorrespondenceBase base = null;
			ApplicationBase applicationBase = null;
			if (referenceDTO.getCorrespondenceId() == null) {

				// this case is of self citation.
				base = correspondenceBaseRepository.findOne(referenceDTO.getCorrespondenceNumber());
				CorrespondenceDTO correspondenceDTO = getCorrespondenceDTOFromEntity(base);
				referenceDTO.setCorrespondenceId(correspondenceDTO);
				referenceDTO.setReferenceCatogory(ReferenceCategoryType.SELF_CITATION);

			} else {
				base = correspondenceBaseRepository.findOne(referenceDTO.getCorrespondenceId().getId());
				if (referenceDTO.getReferenceCatogory() == null) {
					referenceDTO.setReferenceCatogory(ReferenceCategoryType.PTO_CORRESPONDENCE);
				}

			}

			// the reference source & sub source will always be manual in this case.
			referenceDTO.setReferenceSource(ReferenceSourceType.MANUAL);
			referenceDTO.setReferenceSubSource(ReferenceSubSourceType.MANUAL);

			// if the reference is to be completed by JOB, in that case the status will be pending
			if (!referenceDTO.isManualAdd()) {
				referenceDTO.setReferenceStatus(ReferenceStatus.PENDING);
			}

			ReferenceType referenceType = getReferenceType(referenceDTO.getReferencetype());
			referenceDTO.setReferenceType(referenceType);

			setConvertedString(referenceDTO, base, applicationBase);
			String familyId = base.getApplication().getFamilyId();

			List<ReferenceBaseData> duplicateReferenceList = referenceBaseDataRepository
					.getAllByFamilyIdAndReferenceTypeAndReferenceStatus(familyId, referenceType,
							ReferenceStatus.DROPPED);

			List<ReferenceBaseData> referenceList = null;
			if (duplicateReferenceList != null && !duplicateReferenceList.isEmpty()) {
				referenceList = getDuplicateReferences(duplicateReferenceList, referenceType, referenceDTO);
			}

			if (ReferenceType.NPL.equals(referenceDTO.getReferenceType()) && referenceDTO.isCreateFYANotification()) {
				createNPLPendingReference(referenceDTO, base, applicationBase);
				// TODO : POST ACTION REF CHANGE STRATEGY
			} else {

				if (referenceList != null && !referenceList.isEmpty()) {
					referenceBaseData = createDuplicateReferences(referenceDTO, referenceList, false, base,
							applicationBase);
					// TODO : POST ACTION REF CHANGE STRATEGY

				} else {
					referenceBaseData = createNewReferences(referenceDTO, base, applicationBase);
					// TODO : POST ACTION REF CHANGE STRATEGY
				}
			}

		} else {
			log.info(NULL_REFENCE_MESSAGE);
			// TODO error while validating the reference.
		}

		if (referenceBaseData != null) {
			referenceDTO.setReferenceBaseDataId(referenceBaseData.getReferenceBaseDataId());
		}
		return referenceDTO;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.blackbox.ids.services.reference.ReferenceService#addReference(com.blackbox.ids.core.dto.reference.
	 * ReferenceBaseDTO)
	 */
	@Override
	@Transactional
	public ReferenceBaseDTO addReference(final ReferenceBaseDTO referenceDTO) {

		log.info("creating new reference : " + referenceDTO);
		ReferenceBaseData referenceBaseData = null;
		if (referenceDTO != null) {
			CorrespondenceBase base = null;
			ApplicationBase applicationBase = null;
			String familyId = null;
			if (referenceDTO.getCorrespondenceNumber() != null) {
				base = correspondenceBaseRepository.getOne(referenceDTO.getCorrespondenceNumber());
				CorrespondenceDTO correspondenceDTO = getCorrespondenceDTOFromEntity(base);
				referenceDTO.setCorrespondenceId(correspondenceDTO);
				familyId = base.getApplication().getFamilyId();
				referenceDTO.setReferenceCatogory(ReferenceCategoryType.PTO_CORRESPONDENCE);
			} else {
				// self citation
				String jurisdictionCode = referenceDTO.getApplicationJurisdictionCode();
				String applicationNumber = referenceDTO.getApplicationNumber();
				applicationBase = applicationBaseDAO.findByApplicationNoAndJurisdictionCode(jurisdictionCode,
						applicationNumber);
				familyId = applicationBase.getFamilyId();
				referenceDTO.setReferenceCatogory(ReferenceCategoryType.SELF_CITATION);
			}

			referenceDTO.setReferenceSource(ReferenceSourceType.MANUAL);
			referenceDTO.setReferenceSubSource(ReferenceSubSourceType.MANUAL);

			ReferenceType referenceType = getReferenceType(referenceDTO.getReferencetype());
			referenceDTO.setReferenceType(referenceType);

			if (!referenceDTO.isManualAdd()) {
				referenceDTO.setReferenceStatus(ReferenceStatus.PENDING);
			}

			setConvertedString(referenceDTO, base, applicationBase);

			List<ReferenceBaseData> duplicateReferenceList = referenceBaseDataRepository
					.getAllByFamilyIdAndReferenceTypeAndReferenceStatus(familyId, referenceType,
							ReferenceStatus.DROPPED);

			List<ReferenceBaseData> referenceList = null;
			if (duplicateReferenceList != null && !duplicateReferenceList.isEmpty()) {
				referenceList = getDuplicateReferences(duplicateReferenceList, referenceType, referenceDTO);
			}

			if (ReferenceType.NPL.equals(referenceDTO.getReferenceType()) && referenceDTO.isCreateFYANotification()) {
				createNPLPendingReference(referenceDTO, base, applicationBase);
				// TODO : POST ACTION REF CHANGE STRATEGY
			} else {

				if (referenceList != null && !referenceList.isEmpty()) {
					referenceBaseData = createDuplicateReferences(referenceDTO, referenceList, false, base,
							applicationBase);
					// TODO : POST ACTION REF CHANGE STRATEGY

				} else {
					referenceBaseData = createNewReferences(referenceDTO, base, applicationBase);
					// TODO : POST ACTION REF CHANGE STRATEGY
				}
			}

		} else {
			log.info(NULL_REFENCE_MESSAGE);
		}

		if (referenceBaseData != null) {
			referenceDTO.setReferenceBaseDataId(referenceBaseData.getReferenceBaseDataId());
		}
		return referenceDTO;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.blackbox.ids.services.reference.ReferenceService#importReference(com.blackbox.ids.core.model.reference.
	 * ReferenceStagingData)
	 */
	@Override
	@Transactional
	public void importReference(final ReferenceStagingData referenceStagingData) {

		log.info("importing reference : " + referenceStagingData);

		if (referenceStagingData != null) {
			ReferenceType referenceType = referenceStagingData.getReferenceType();
			String familyId = referenceStagingData.getApplication().getFamilyId();

			referenceUtil.setConvertedString(referenceStagingData);
			List<ReferenceBaseData> duplicateReferenceList = referenceBaseDataRepository
					.getAllByFamilyIdAndReferenceTypeAndReferenceStatus(familyId, referenceType,
							ReferenceStatus.DROPPED);

			List<ReferenceBaseData> referenceList = null;
			DuplicateNPLAttributes nplAttributes = new DuplicateNPLAttributes();
			if (duplicateReferenceList != null && !duplicateReferenceList.isEmpty()) {
				referenceList = referenceUtil.getDuplicateReferences(duplicateReferenceList, referenceStagingData,
						nplAttributes);
			}

			if (ReferenceType.NPL.equals(referenceStagingData.getReferenceType())
					&& nplAttributes.isCreateFYANotification()) {
				createNPLPendingReference(referenceStagingData, nplAttributes);
				// TODO : POST ACTION SOURCE REF CHANGE STRATEGY
			} else {

				if (referenceList != null && !referenceList.isEmpty()) {
					createDuplicateReferences(referenceStagingData, referenceList);
					// TODO : POST ACTION SOURCE REF CHANGE STRATEGY

				} else {
					createNewReferences(referenceStagingData);
					// TODO : POST ACTION SOURCE REF CHANGE STRATEGY
				}
			}

		} else {
			log.info(NULL_REFENCE_MESSAGE);
		}
		return;

	}

	/**
	 * Creates the npl pending reference.
	 *
	 * @param newReference
	 *            the new reference
	 * @param base
	 *            the base
	 * @param applicationBase
	 *            the application base
	 * @return the reference base npl data
	 */
	private ReferenceBaseNPLData createNPLPendingReference(final ReferenceBaseDTO newReference, CorrespondenceBase base,
			ApplicationBase applicationBase) {
		ReferenceBaseNPLData entity = referenceUtil.getNPLReferenceBaseEntityFromDTO(newReference, base,
				applicationBase);
		entity = referenceBaseDataRepository.saveAndFlush(entity);
		return entity;

	}

	/**
	 * Creates the npl pending reference.
	 *
	 * @param referenceStagingData
	 *            the reference staging data
	 * @param nplAttributes
	 *            the npl attributes
	 * @return the reference base npl data
	 */
	private ReferenceBaseNPLData createNPLPendingReference(final ReferenceStagingData referenceStagingData,
			DuplicateNPLAttributes nplAttributes) {

		ReferenceBaseNPLData entity = referenceUtil.getNPLReferenceBaseEntityFromStaging(referenceStagingData,
				nplAttributes.isReferenceFlowFlag());
		entity.setReferenceStatus(ReferenceStatus.PENDING);
		entity = referenceBaseDataRepository.saveAndFlush(entity);
		return entity;

	}

	/**
	 * Creates the new references.
	 *
	 * @param newReference
	 *            the new reference for PTO_CORRESPONDENCE
	 * @param base
	 *            the base
	 * @param applicationBase
	 *            the application base
	 */
	private ReferenceBaseData createNewReferences(final ReferenceBaseDTO newReference, CorrespondenceBase base,
			ApplicationBase applicationBase) {
		newReference.setReferenceFlowFlag(true);
		newReference.setReferenceStatus(ReferenceStatus.NEW);
		ReferenceBaseData referenceBaseData = saveReferenceEntities(newReference, base, applicationBase);
		checkAndCreateSMLNotification(referenceBaseData);

		// TODO : POST ACTION SOURCE REF CHANGE STRATEGY

		// create reference flow rules
		if (referenceBaseData.isReferenceFlowFlag()) {
			referenceFlowEngine.executeStrategy(referenceBaseData, null, FlowChangeType.REF_BASE_TABLE_CHANGE,
					ReferenceChangeDesciption.REF_RECORD_CREATED_NEW_REF_FLOW_FLAG_YES);
		} else {
			referenceFlowEngine.executeStrategy(referenceBaseData, null, FlowChangeType.REF_BASE_TABLE_CHANGE,
					ReferenceChangeDesciption.REF_RECORD_CREATED_NEW_REF_FLOW_FLAG_NO);
		}

		return referenceBaseData;
	}

	/**
	 * Creates the new references for SELF_CITATION
	 *
	 * @param newReference
	 *            the new reference
	 * @return the reference base data
	 */
	private ReferenceBaseData createNewReferences(final ReferenceStagingData newReference) {
		newReference.setReferenceStatus(ReferenceStatus.NEW);
		ReferenceBaseData referenceBaseData = saveReferenceEntities(newReference, true);
		checkAndCreateSMLNotification(referenceBaseData);

		// TODO : POST ACTION SOURCE REF CHANGE STRATEGY

		// create reference flow rules
		if (referenceBaseData.isReferenceFlowFlag()) {
			referenceFlowEngine.executeStrategy(referenceBaseData, null, FlowChangeType.REF_BASE_TABLE_CHANGE,
					ReferenceChangeDesciption.REF_RECORD_CREATED_NEW_REF_FLOW_FLAG_YES);
		} else {
			referenceFlowEngine.executeStrategy(referenceBaseData, null, FlowChangeType.REF_BASE_TABLE_CHANGE,
					ReferenceChangeDesciption.REF_RECORD_CREATED_NEW_REF_FLOW_FLAG_NO);
		}

		return referenceBaseData;
	}

	/**
	 * Adds the source reference.
	 *
	 * @param referenceBaseData
	 *            the reference base data
	 */
	private SourceReference addSourceReference(ReferenceBaseData referenceBaseData, String nplString) {

		SourceReference sourceReference = null;
		CorrespondenceBase corespondenceBase = referenceBaseData.getCorrespondence();

		if (corespondenceBase != null) {
			Long sourceReferenceId = sourceReferenceRepository
					.getSourceReferenceByCorrespondence(corespondenceBase.getId());
			if (sourceReferenceId == null) {
				sourceReference = new SourceReference();
				sourceReference.setCorrespondence(referenceBaseData.getCorrespondence());
				sourceReference.setJurisdiction(referenceBaseData.getJurisdiction());
				sourceReference.setMailingDate(referenceBaseData.getCorrespondence().getMailingDate());
				sourceReference.setReferenceCategory(referenceBaseData.getReferenceCatogory());
				sourceReference.setReferenceCouplingId(referenceBaseData.getCouplingId());
				sourceReference.setReferenceFlowFlag(true);
				sourceReference.setReferenceReviewStatus(referenceBaseData.isReviewedStatus());
				sourceReference.setReferenceStatus(referenceBaseData.getReferenceStatus());
				sourceReference.setReferenceType(referenceBaseData.getReferenceType());
				sourceReference.setSelfCitationCreationDate(referenceBaseData.getSelfCitationDate());
				sourceReference.setSourceApplicationNumber(referenceBaseData.getSourceApplication());
				sourceReference.setSourceOfReference(referenceBaseData.getReferenceSource());
				sourceReference.setSubSourceOfReference(referenceBaseData.getReferenceSubSource());

				// TODO to be determined
				sourceReference.setOriginalReferenceBaseData(null);
				sourceReference.setReferenceComments(null);
				sourceReference.setNplString(nplString);

				sourceReference = sourceReferenceRepository.saveAndFlush(sourceReference);

				// TODO : POST ACTION SOURCE REF CHANGE STRATEGY
			} else {
				sourceReference = new SourceReference();
				sourceReference.setId(sourceReferenceId);
			}
		}
		return sourceReference;

	}

	/**
	 * Creates the npl duplicate references manualy.
	 *
	 * @param newReference
	 *            the new reference
	 * @param duplicateReferenceData
	 *            the duplicate reference data
	 * @param nplManualUpdate
	 *            the npl manual update
	 * @return the reference base data
	 */
	private ReferenceBaseData createNPLDuplicateReferencesManualy(final ReferenceBaseNPLData newReference,
			List<ReferenceBaseData> duplicateReferenceData, boolean nplManualUpdate) {

		ReferenceBaseData referenceBaseData = null;
		ReferenceBaseData existingReferenceBaseData = setReferenceFlowFlagForManualNPL(duplicateReferenceData,
				newReference);

		if (existingReferenceBaseData != null) {
			referenceBaseDataRepository.updateReferenceFlowFlagById(existingReferenceBaseData.isReferenceFlowFlag(),
					existingReferenceBaseData.getReferenceBaseDataId());
		}

		setParentReferenceForNPLManual(newReference, duplicateReferenceData);
		newReference.setReferenceStatus(ReferenceStatus.DUPLICATE);

		if (nplManualUpdate) {
			referenceBaseDataRepository.updateNPLReference(ReferenceStatus.DUPLICATE, false,
					newReference.getReferenceBaseDataId());
		}

		return referenceBaseData;
	}

	/**
	 * Creates the duplicate references.
	 *
	 * @param newReference
	 *            the new reference
	 * @param duplicateReferenceData
	 *            the duplicate reference data
	 * @param nplManualUpdate
	 *            the npl manual update
	 * @param base
	 *            the base
	 * @param applicationBase
	 *            the application base
	 * @return the reference base data
	 */
	private ReferenceBaseData createDuplicateReferences(final ReferenceBaseDTO newReference,
			List<ReferenceBaseData> duplicateReferenceData, boolean nplManualUpdate, CorrespondenceBase base,
			ApplicationBase applicationBase) {

		ReferenceBaseData referenceBaseData = null;

		if (newReference.isSelfcited()) {

			/*
			 * in case of duplicate mark the reference flow flag as false;
			 */
			newReference.setReferenceFlowFlag(false);
		} else {
			ReferenceBaseData existingReferenceBaseData = setReferenceFlowFlag(duplicateReferenceData, newReference,
					base);
			if (existingReferenceBaseData != null) {
				referenceBaseDataRepository.updateReferenceFlowFlagById(existingReferenceBaseData.isReferenceFlowFlag(),
						existingReferenceBaseData.getReferenceBaseDataId());
			}
		}

		setParentReference(newReference, duplicateReferenceData);
		newReference.setReferenceStatus(ReferenceStatus.DUPLICATE);

		if (nplManualUpdate) {
			referenceBaseDataRepository.updateNPLReference(ReferenceStatus.DUPLICATE,
					newReference.isReferenceFlowFlag(), newReference.getReferenceBaseDataId());
		} else {
			referenceBaseData = saveReferenceEntities(newReference, base, applicationBase);
		}

		// TODO : POST ACTION SOURCE REF CHANGE STRATEGY

		// create reference flow rules
		if (referenceBaseData.isReferenceFlowFlag()) {
			referenceFlowEngine.executeStrategy(referenceBaseData, null, FlowChangeType.REF_BASE_TABLE_CHANGE,
					ReferenceChangeDesciption.REF_RECORD_CREATED_DUPLICATE_REF_FLOW_FLAG_YES);
		} else {
			referenceFlowEngine.executeStrategy(referenceBaseData, null, FlowChangeType.REF_BASE_TABLE_CHANGE,
					ReferenceChangeDesciption.REF_RECORD_CREATED_DUPLICATE_REF_FLOW_FLAG_NO);
		}

		return referenceBaseData;

	}

	/**
	 * Creates the duplicate references.
	 *
	 * @param referenceStagingData
	 *            the reference staging data
	 * @param duplicateReferenceData
	 *            the duplicate reference data
	 * @return the reference base data
	 */
	private ReferenceBaseData createDuplicateReferences(final ReferenceStagingData referenceStagingData,
			List<ReferenceBaseData> duplicateReferenceData) {

		ReferenceBaseData referenceBaseData = null;
		boolean flowFlag = updateReferenceFlowFlag(duplicateReferenceData, referenceStagingData);

		setParentReference(referenceStagingData, duplicateReferenceData);
		referenceStagingData.setReferenceStatus(ReferenceStatus.DUPLICATE);

		referenceBaseData = saveReferenceEntities(referenceStagingData, flowFlag);

		// TODO : POST ACTION REF CHANGE STRATEGY

		// create reference flow rules
		if (referenceBaseData.isReferenceFlowFlag()) {
			referenceFlowEngine.executeStrategy(referenceBaseData, null, FlowChangeType.REF_BASE_TABLE_CHANGE,
					ReferenceChangeDesciption.REF_RECORD_CREATED_NEW_REF_FLOW_FLAG_YES);
		} else {
			referenceFlowEngine.executeStrategy(referenceBaseData, null, FlowChangeType.REF_BASE_TABLE_CHANGE,
					ReferenceChangeDesciption.REF_RECORD_CREATED_NEW_REF_FLOW_FLAG_NO);
		}

		return referenceBaseData;

	}

	/**
	 * Sets the parent reference.
	 *
	 * @param newReference
	 *            the new reference
	 * @param duplicateReferenceData
	 *            the duplicate reference data
	 */
	private void setParentReference(final ReferenceBaseDTO newReference,
			List<ReferenceBaseData> duplicateReferenceData) {
		ReferenceBaseData parentReference = getParentReference(duplicateReferenceData);
		if (parentReference != null) {
			newReference.setParentReferenceId(parentReference.getReferenceBaseDataId());
		}
	}

	/**
	 * Sets the parent reference.
	 *
	 * @param newReference
	 *            the new reference
	 * @param duplicateReferenceData
	 *            the duplicate reference data
	 */
	private void setParentReference(final ReferenceStagingData newReference,
			List<ReferenceBaseData> duplicateReferenceData) {
		ReferenceBaseData parentReference = getParentReference(duplicateReferenceData);
		if (parentReference != null) {
			newReference.setParentReferenceId(parentReference.getReferenceBaseDataId());
		}
	}

	/**
	 * Sets the parent reference for npl manual.
	 *
	 * @param newReference
	 *            the new reference
	 * @param duplicateReferenceData
	 *            the duplicate reference data
	 */
	private void setParentReferenceForNPLManual(final ReferenceBaseNPLData newReference,
			List<ReferenceBaseData> duplicateReferenceData) {
		ReferenceBaseData parentReference = getParentReference(duplicateReferenceData);
		if (parentReference != null) {
			newReference.setParentReferenceId(parentReference);
		}
	}

	/**
	 * Gets the parent reference.
	 *
	 * @param duplicateReferenceData
	 *            the duplicate reference data
	 * @return the parent reference
	 */
	private static ReferenceBaseData getParentReference(List<ReferenceBaseData> duplicateReferenceData) {
		ReferenceBaseData parentReference = null;
		for (ReferenceBaseData referenceBaseData : duplicateReferenceData) {
			if (referenceBaseData.getReferenceStatus().equals(ReferenceStatus.NEW)) {
				parentReference = referenceBaseData;
				break;
			}
		}

		return parentReference;

	}

	/**
	 * Save reference entities.
	 *
	 * @param newReference
	 *            the new reference
	 * @param duplicate
	 *            the duplicate
	 * @param base
	 *            the base
	 * @param applicationBase
	 *            the application base
	 * @return the reference base data
	 */
	private ReferenceBaseData saveReferenceEntities(final ReferenceBaseDTO newReference, CorrespondenceBase base,
			ApplicationBase applicationBase) {

		ReferenceBaseData referenceBaseData = null;
		String nplData = null;
		ReferenceType referenceType = newReference.getReferenceType();

		switch (referenceType) {
		case FP:
			ReferenceBaseFPData referenceBaseFPData = referenceUtil.getFPReferenceBaseEntityFromDTO(newReference, base,
					applicationBase);
			referenceBaseData = referenceBaseDataRepository.saveAndFlush(referenceBaseFPData);
			break;
		case PUS:
		case US_PUBLICATION:
			ReferenceBasePUSData referenceBasePUSData = referenceUtil.getPUSReferenceBaseEntityFromDTO(newReference,
					base, applicationBase);
			referenceBaseData = referenceBaseDataRepository.saveAndFlush(referenceBasePUSData);
			break;
		case NPL:
			ReferenceBaseNPLData referenceBaseNPLData = referenceUtil.getNPLReferenceBaseEntityFromDTO(newReference,
					base, applicationBase);
			referenceBaseData = referenceBaseDataRepository.saveAndFlush(referenceBaseNPLData);
			break;
		default:
			log.error(INVALID_REFENCE_MESSAGE + referenceType);
			break;
		}

		// create npl string for source and null reference
		if (ReferenceCategoryType.PTO_CORRESPONDENCE.equals(newReference.getReferenceCatogory())) {

			nplData = createNPLString(referenceBaseData.getJurisdiction().getCode(),
					referenceBaseData.getApplication().getApplicationNumber(),
					referenceBaseData.getCorrespondence().getDocumentCode().getDescription(),
					referenceBaseData.getMailingDate());
		}

		if (!newReference.isSelfcited()) {
			CorrespondenceDTO correspondence = newReference.getCorrespondenceId();
			if (correspondence != null) {
				updateNullReference(correspondence.getId(), false, null, nplData);
			}

			SourceReference sourceRef = addSourceReference(referenceBaseData, nplData);

			referenceBaseDataRepository.updateSourceReferenceById(referenceBaseData.getReferenceBaseDataId(),
					sourceRef);
		}

		return referenceBaseData;

	}

	/**
	 * Save reference entities.
	 *
	 * @param newReference
	 *            the new reference
	 * @param duplicate
	 *            the duplicate
	 * @param flowFlag
	 *            the flow flag
	 * @return the reference base data
	 */
	private ReferenceBaseData saveReferenceEntities(final ReferenceStagingData newReference, boolean flowFlag) {

		String nplStringData = null;
		ReferenceBaseData referenceBaseData = null;
		ReferenceType referenceType = newReference.getReferenceType();

		switch (referenceType) {
		case FP:
			ReferenceBaseFPData referenceBaseFPData = referenceUtil.getFPReferenceBaseEntityFromStaging(newReference,
					flowFlag);
			referenceBaseData = referenceBaseDataRepository.saveAndFlush(referenceBaseFPData);
			break;
		case PUS:
		case US_PUBLICATION:
			ReferenceBasePUSData referenceBasePUSData = referenceUtil.getPUSReferenceBaseEntityFromStaging(newReference,
					flowFlag);
			referenceBaseData = referenceBaseDataRepository.saveAndFlush(referenceBasePUSData);
			break;
		case NPL:

			ReferenceBaseNPLData referenceBaseNPLData = referenceUtil.getNPLReferenceBaseEntityFromStaging(newReference,
					flowFlag);
			referenceBaseData = referenceBaseDataRepository.saveAndFlush(referenceBaseNPLData);

			break;

		default:
			log.error(INVALID_REFENCE_MESSAGE + referenceType);
			break;
		}

		// create npl string for source and null reference
		if (ReferenceCategoryType.PTO_CORRESPONDENCE.equals(newReference.getReferenceCatogoryType())) {
			nplStringData = createNPLString(referenceBaseData.getJurisdiction().getCode(),
					referenceBaseData.getApplication().getApplicationNumber(),
					referenceBaseData.getCorrespondence().getDocumentCode().getDescription(),
					referenceBaseData.getMailingDate());
		}

		if (referenceBaseData.getCorrespondence() != null) {
			updateNullReference(referenceBaseData.getCorrespondence().getId(), false, null, nplStringData);
		}

		SourceReference sourceRef = addSourceReference(referenceBaseData, nplStringData);

		referenceBaseDataRepository.updateSourceReferenceById(referenceBaseData.getReferenceBaseDataId(), sourceRef);

		return referenceBaseData;

	}

	/**
	 * Check and create sml notification.
	 *
	 * @param referenceBaseData
	 *            the reference base data
	 */
	private void checkAndCreateSMLNotification(ReferenceBaseData referenceBaseData) {

		if (referenceBaseData instanceof ReferenceBasePUSData) {

			ReferenceBasePUSData referenceBasePUSData = (ReferenceBasePUSData) referenceBaseData;
			Long count = applicationBaseDAO
					.findByPublicationNumberAndJurisdiction(referenceBasePUSData.getPublicationNumber(), "US");

			if (count != null && count > 0) {
				Set<Long> roleIds = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();

				// creating SML notification
				notificationProcessService.createNotification(referenceBasePUSData.getApplication(),
						referenceBasePUSData.getReferenceBaseDataId(), referenceBasePUSData.getCorrespondence().getId(),
						referenceBasePUSData.getReferenceBaseDataId(), EntityName.REFERENCE_BASE_DATA,
						NotificationProcessType.CREATE_SML, null, null);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.blackbox.ids.services.reference.ReferenceService#updateNPLReferenceManually(java.lang.Long, boolean,
	 * java.lang.Long)
	 */
	@Override
	@Transactional
	public void updateNPLReferenceManually(Long referenceId, boolean duplicate, Long notificationProcessId) {

		ReferenceBaseNPLData baseDate = (ReferenceBaseNPLData) referenceBaseDataRepository.findOne(referenceId);
		if (duplicate) {

			List<ReferenceBaseData> referenceBaseDataList = referenceBaseDataRepository
					.getAllChildReferences(baseDate.getParentReferenceId());

			createNPLDuplicateReferencesManualy(baseDate, referenceBaseDataList, true);

			// TODO : POST ACTION REF BASE CHANGE STRATEGY
		} else {

			referenceBaseDataRepository.updateNPLReference(ReferenceStatus.NEW, true, referenceId);

			// TODO : POST ACTION REF BASE CHANGE STRATEGY
		}

		notificationProcessService.completeTask(notificationProcessId, NotificationStatus.COMPLETED);

	}

	/**
	 * Adds the null reference.
	 *
	 * @param referenceBaseData
	 *            the reference base data
	 * @param isNull
	 *            the is null
	 */
	@Override
	@Transactional
	public void updateNullReference(Long correspondenceId, boolean isNull, Long ocrDataId, String nplStringData) {

		NullReferenceDocument nullReferenceDocument = nullReferenceDocumentRepository
				.getNullReferenceByCorrespondenceId(correspondenceId);
		if (nullReferenceDocument != null) {
			if (nplStringData != null) {
				nullReferenceDocumentRepository.updateNPLStringAndNullReferenceFlagByCorrespondenceId(nplStringData,
						isNull, correspondenceId);
			} else {
				nullReferenceDocumentRepository.updateNullReferenceFlagByCorrespondenceId(isNull, correspondenceId);
			}

		} else {
			nullReferenceDocument = new NullReferenceDocument();
			nullReferenceDocument.setCorrespondenceID(correspondenceId);
			nullReferenceDocument.setNullReference(isNull);
			nullReferenceDocument.setOcrDataId(ocrDataId);
			nullReferenceDocument.setNplString(nplStringData);
			nullReferenceDocumentRepository.save(nullReferenceDocument);
		}

	}

	/**
	 * Sets the converted string.
	 *
	 * @param referenceDTO
	 *            the reference dto
	 * @param base
	 *            the base
	 * @param applicationBase
	 *            the application base
	 */
	private void setConvertedString(final ReferenceBaseDTO referenceDTO, CorrespondenceBase base,
			ApplicationBase applicationBase) {

		ReferenceType referenceType = referenceDTO.getReferenceType();
		String jurisdictionCode = null;
		if (ReferenceType.PUS.equals(referenceType) || ReferenceType.US_PUBLICATION.equals(referenceType)) {
			jurisdictionCode = "US";
		} else if (ReferenceType.FP.equals(referenceType)) {
			jurisdictionCode = referenceDTO.getJurisdiction().getName();
		}

		Calendar fillingDate = null;
		if (base != null) {
			fillingDate = base.getFilingDate();
		} else {
			fillingDate = applicationBase.getAppDetails().getFilingDate();
		}

		Calendar grantDate = referenceDTO.getGrantDate();

		String patentPublicationNumber = referenceDTO.getPublicationNumber();
		if (ReferenceType.NPL.equals(referenceType)) {
			String convertedString = createNPLString(referenceDTO);
			referenceDTO.setStringData(convertedString);
		} else {
			// Fetch the particular regex from DB & perform conversion.
			String convertedString = convert(jurisdictionCode, fillingDate, grantDate, patentPublicationNumber);
			if (convertedString == null || convertedString.isEmpty()) {
				convertedString = patentPublicationNumber;
			}

			switch (referenceType) {
			case PUS:
				referenceDTO.setConvertedPublicationNumber(convertedString);
				break;
			case FP:
				referenceDTO.setConvertedForeignDocumentNumber(convertedString);
				break;
			case NPL:
				break;
			default:
				break;
			}
		}

	}

	/**
	 * Gets the reference type.
	 *
	 * @param referenceType
	 *            the reference type
	 * @return the reference type
	 */
	private static ReferenceType getReferenceType(String referenceType) {
		ReferenceType type = ReferenceType.NPL;
		switch (referenceType) {

		case "PUS":
			type = ReferenceType.PUS;
			break;
		case "FP":
			type = ReferenceType.FP;
			break;
		default:
			type = ReferenceType.NPL;
			break;

		}
		return type;
	}

	/**
	 * Gets the correspondence dto from entity.
	 *
	 * @param correspondenceBase
	 *            the correspondence base
	 * @return the correspondence dto from entity
	 */
	private CorrespondenceDTO getCorrespondenceDTOFromEntity(CorrespondenceBase correspondenceBase) {
		return modelMapper.map(correspondenceBase, CorrespondenceDTO.class);
	}

	/**
	 * Sets the reference flow flag.
	 *
	 * @param duplicateReferenceList
	 *            the duplicate reference list
	 * @param newReference
	 *            the new reference
	 * @param base
	 *            the base
	 * @return the reference base data
	 */
	private static ReferenceBaseData setReferenceFlowFlag(final List<ReferenceBaseData> duplicateReferenceList,
			final ReferenceBaseDTO newReference, CorrespondenceBase base) {
		ReferenceBaseData existingReferenceBaseData = null;
		if (base != null) {
			Calendar referenceDate = base.getMailingDate();

			for (ReferenceBaseData referenceBaseData : duplicateReferenceList) {
				// in case of self citation mailing would be null.. do not consider that into comparison
				if (referenceBaseData.isReferenceFlowFlag() && referenceBaseData.getMailingDate() != null
						&& referenceBaseData.getMailingDate().after(referenceDate)) {
					referenceBaseData.setReferenceFlowFlag(false);
					newReference.setReferenceFlowFlag(true);
					existingReferenceBaseData = referenceBaseData;
					break;
				}

			}
		}
		return existingReferenceBaseData;

	}

	/**
	 * Update reference flow flag.
	 *
	 * @param duplicateReferenceList
	 *            the duplicate reference list
	 * @param referenceStagingData
	 *            the reference staging data
	 * @return true, if successful
	 */
	private boolean updateReferenceFlowFlag(final List<ReferenceBaseData> duplicateReferenceList,
			final ReferenceStagingData referenceStagingData) {

		boolean flowFlag = false;
		ReferenceBaseData existingReferenceBaseData = null;
		CorrespondenceBase correspondence = referenceStagingData.getCorrespondence();
		if (correspondence != null) {
			Calendar referenceDate = correspondence.getMailingDate();
			for (ReferenceBaseData referenceBaseData : duplicateReferenceList) {
				if (referenceBaseData.isReferenceFlowFlag()
						&& referenceBaseData.getMailingDate().after(referenceDate)) {
					referenceBaseData.setReferenceFlowFlag(false);
					flowFlag = true;
					existingReferenceBaseData = referenceBaseData;
					break;
				}
			}

			if (existingReferenceBaseData != null) {
				referenceBaseDataRepository.updateReferenceFlowFlagById(existingReferenceBaseData.isReferenceFlowFlag(),
						existingReferenceBaseData.getReferenceBaseDataId());
			}
		}
		return flowFlag;

	}

	/**
	 * Sets the reference flow flag for manual npl.
	 *
	 * @param duplicateReferenceList
	 *            the duplicate reference list
	 * @param newReference
	 *            the new reference
	 * @return the reference base data
	 */
	private static ReferenceBaseData setReferenceFlowFlagForManualNPL(
			final List<ReferenceBaseData> duplicateReferenceList, final ReferenceBaseNPLData newReference) {
		ReferenceBaseData existingReferenceBaseData = null;

		for (ReferenceBaseData referenceBaseData : duplicateReferenceList) {
			if (referenceBaseData.isReferenceFlowFlag()
					&& referenceBaseData.getMailingDate().after(newReference.getMailingDate())) {
				referenceBaseData.setReferenceFlowFlag(false);
				newReference.setReferenceFlowFlag(true);
				existingReferenceBaseData = referenceBaseData;
				break;
			}

		}
		return existingReferenceBaseData;

	}

	/**
	 * Gets the duplicate references.
	 *
	 * @param duplicateReferenceList
	 *            the duplicate reference list
	 * @param referenceType
	 *            the reference type
	 * @param referenceDTO
	 *            the reference dto
	 * @return the duplicate references
	 */
	private List<ReferenceBaseData> getDuplicateReferences(List<ReferenceBaseData> duplicateReferenceList,
			ReferenceType referenceType, final ReferenceBaseDTO referenceDTO) {

		List<ReferenceBaseData> referenceList = new ArrayList<ReferenceBaseData>();

		for (ReferenceBaseData referenceBaseData : duplicateReferenceList) {

			if (referenceBaseData.getReferenceType().equals(referenceType)) {
				switch (referenceType) {
				case PUS:
				case US_PUBLICATION:
					if (matchPUSReference(referenceBaseData, referenceDTO.getConvertedPublicationNumber())) {
						referenceList.add(referenceBaseData);
					}
					break;
				case FP:
					if (matchFPReference(referenceBaseData, referenceDTO.getConvertedForeignDocumentNumber())) {
						referenceList.add(referenceBaseData);
					}
					break;
				case NPL:
					if (matchNPLReference(referenceBaseData, referenceDTO.getStringData())) {
						referenceList.add(referenceBaseData);
					} else {
						referenceDTO.setCreateFYANotification(true);
						referenceDTO.setReferenceStatus(ReferenceStatus.PENDING);
						referenceDTO.setReferenceFlowFlag(false);
					}
					break;
				default:
					log.error(NULL_REFENCE_MESSAGE);
					break;
				}
			}
		}

		return referenceList;

	}

	/**
	 * Match pus reference.
	 *
	 * @param referenceBaseData
	 *            the reference base data
	 * @param convertedString
	 *            the converted string
	 * @return true, if successful
	 */
	private boolean matchPUSReference(ReferenceBaseData referenceBaseData, String convertedString) {

		ReferenceBasePUSData referenceBasePUSData;

		if (referenceBaseData instanceof ReferenceBasePUSData) {
			referenceBasePUSData = (ReferenceBasePUSData) referenceBaseData;
			return matchString(referenceBasePUSData.getConvertedPublicationNumber(), convertedString);
		}

		return false;
	}

	/**
	 * Match fp reference.
	 *
	 * @param referenceBaseData
	 *            the reference base data
	 * @param convertedString
	 *            the converted string
	 * @return true, if successful
	 */
	private boolean matchFPReference(ReferenceBaseData referenceBaseData, String convertedString) {
		ReferenceBaseFPData referenceBaseFPData = null;
		if (referenceBaseData instanceof ReferenceBaseFPData) {
			referenceBaseFPData = (ReferenceBaseFPData) referenceBaseData;
			return matchString(referenceBaseFPData.getConvertedForeignDocumentNumber(), convertedString);
		}

		return false;
	}

	/**
	 * Match string.
	 *
	 * @param str1
	 *            the str1
	 * @param str2
	 *            the str2
	 * @return true, if successful
	 */
	private static boolean matchString(String str1, String str2) {
		boolean matched = false;
		if (str1 != null && !str1.isEmpty() && str1.equalsIgnoreCase(str2)) {
			matched = true;
		}
		return matched;
	}

	/**
	 * Match npl reference.
	 *
	 * @param referenceBaseData
	 *            the reference base data
	 * @param convertedString
	 *            the converted string
	 * @return true, if successful
	 */
	private boolean matchNPLReference(ReferenceBaseData referenceBaseData, String convertedString) {
		ReferenceBaseNPLData referenceBaseNPLData = null;
		if (referenceBaseData instanceof ReferenceBaseNPLData) {
			referenceBaseNPLData = (ReferenceBaseNPLData) referenceBaseData;
			String nplString = referenceBaseNPLData.getStringData();

			return matchNPL(convertedString, nplString);
		}

		return false;
	}

	private boolean matchNPL(String convertedString, String nplString) {
		boolean match = false;
		float minPercentage = Float.parseFloat(nplMinPercentage);
		float maxPercentage = Float.parseFloat(nplMaxPercentage);

		int distance = getDistance(nplString, convertedString);
		float percentage = Float.valueOf(distance) / (nplString.length() + convertedString.length()) * 100F;

		// adding Fuzzy logic to match NPL String. This will check the string
		// matching percentage.
		if (percentage >= minPercentage && percentage <= maxPercentage) {
			match = true;
		}
		return match;
	}

	/**
	 * Creates the npl string.
	 *
	 * @param jurisdictionCode
	 *            the jurisdiction code
	 * @param applicationNumber
	 *            the application number
	 * @param documentDescription
	 *            the document description
	 * @param mailingDate
	 *            the mailing date
	 * @return the string
	 */
	private static String createNPLString(String jurisdictionCode, String applicationNumber, String documentDescription,
			Calendar mailingDate) {

		StringBuilder sb = new StringBuilder();
		sb.append(jurisdictionCode);
		sb.append(applicationNumber);
		sb.append(documentDescription);
		String mailingDateStr = BlackboxDateUtil.calendarToStr(mailingDate, TimestampFormat.FULL_DATE_TIME);
		sb.append(mailingDateStr);

		return sb.toString();
	}

	private static String createNPLString(ReferenceBaseDTO referenceDTO) {
		StringBuilder sb = new StringBuilder();
		if (referenceDTO.getAuthor() != null) {
			sb.append(referenceDTO.getAuthor()).append(Constant.SEPARATOR_COMMA);
		}
		if (referenceDTO.getTitle() != null) {
			sb.append(referenceDTO.getTitle()).append(Constant.SEPARATOR_COMMA);
		}
		if (referenceDTO.getPublicationDetail() != null) {
			sb.append(referenceDTO.getPublicationDetail()).append(Constant.SEPARATOR_COMMA);
		}
		if (referenceDTO.getPublicationMonth() != null) {
			sb.append(referenceDTO.getPublicationMonth()).append(Constant.SEPARATOR_COMMA);
		}
		if (referenceDTO.getPublicationDay() != null) {
			sb.append(referenceDTO.getPublicationDay()).append(Constant.SEPARATOR_COMMA);
		}
		if (referenceDTO.getPublicationYear() != null) {
			sb.append(referenceDTO.getPublicationYear()).append(Constant.SEPARATOR_COMMA);
		}
		if (referenceDTO.getRelevantPages() != null) {
			sb.append(referenceDTO.getRelevantPages()).append(Constant.SEPARATOR_COMMA);
		}
		if (referenceDTO.getVolumeNumber() != null) {
			sb.append(referenceDTO.getVolumeNumber()).append(Constant.SEPARATOR_COMMA);
		}
		if (referenceDTO.getURL() != null) {
			sb.append(referenceDTO.getURL()).append(Constant.SEPARATOR_COMMA);
		}
		if (referenceDTO.getPublicationCity() != null) {
			sb.append(referenceDTO.getPublicationCity()).append(Constant.SEPARATOR_COMMA);
		}
		if (referenceDTO.getStringData() != null) {
			sb.append(referenceDTO.getStringData()).append(Constant.SEPARATOR_COMMA);
		}
		if (referenceDTO.getRetrivalDate() != null) {
			sb.append(referenceDTO.getRetrivalDate()).append(Constant.SEPARATOR_COMMA);
		}
		if (referenceDTO.getApplicationSerialNumber() != null) {
			sb.append(referenceDTO.getApplicationSerialNumber()).append(Constant.SEPARATOR_COMMA);
		}
		return sb.toString();
	}

	/**
	 * Gets the distance.
	 *
	 * @param firstString
	 *            the first string
	 * @param secondString
	 *            the second string
	 * @return the distance
	 */
	private static int getDistance(String firstString, String secondString) {
		return StringUtils.getLevenshteinDistance(firstString, secondString);
	}

	/**
	 * Convert.
	 *
	 * @param jurisdictionCode
	 *            the jurisdiction code
	 * @param fillingDate
	 *            the filling date
	 * @param grantDate
	 *            the grant date
	 * @param patentPublicationNumber
	 *            the patent publication number
	 * @return the string
	 * @throws ApplicationException
	 *             the application exception
	 */
	private String convert(String jurisdictionCode, Calendar fillingDate, Calendar grantDate,
			String patentPublicationNumber) {

		NumberType type = NumberType.PUBLICATION;

		return numberFormatValidationService.getConvertedValue(patentPublicationNumber, type, jurisdictionCode,
				ApplicationType.ALL, fillingDate, grantDate);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.blackbox.ids.services.reference.ReferenceService#getReferenceById(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public ReferenceBaseDTO getReferenceById(final Long id) {
		ReferenceBaseData referenceData = referenceBaseDataRepository.findOne(id);
		return populateDTOFromReference(referenceData);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.blackbox.ids.services.reference.ReferenceService#removeReference(java.lang.Long)
	 */
	@Override
	@Transactional
	public void removeReference(final Long id) {

		ReferenceBaseData referenceBaseData = referenceBaseDataRepository.getOne(id);
		List<ReferenceBaseData> duplicateReferenceList = referenceBaseDataRepository
				.getAllByFamilyIdAndReferenceTypeAndReferenceStatus(referenceBaseData.getFamilyId(),
						referenceBaseData.getReferenceType(), ReferenceStatus.DROPPED);

		// If reference flowFlag for this reference (reference to be deleted) is
		// Y, then mark the reference with latest
		// mailing date as Y.
		if (referenceBaseData.isReferenceFlowFlag()) {
			ReferenceBaseData duplicateRefWithLatestMailingDate = getReferenceWithLatestMailingDate(
					duplicateReferenceList);
			referenceBaseDataRepository.updateReferenceFlowFlagById(true,
					duplicateRefWithLatestMailingDate.getReferenceBaseDataId());

			// TODO : POST ACTION REF BASE CHANGE STRATEGY
		}

		// IF reference is NEW (reference to be deleted) then in that case
		// update the next reference (mailing date) with
		// status as NEW & use its ID to update the parent Id of rest of the
		// duplicate references.
		if (ReferenceStatus.NEW.equals(referenceBaseData.getReferenceStatus())) {
			ReferenceBaseData duplicateRefWithLatestMailingDate = getReferenceWithLatestMailingDate(
					duplicateReferenceList);
			referenceBaseDataRepository.updateReferenceStatusAndParentById(ReferenceStatus.NEW, null,
					duplicateRefWithLatestMailingDate.getReferenceBaseDataId());

			// TODO : POST ACTION REF BASE CHANGE STRATEGY
			duplicateReferenceList.remove(duplicateRefWithLatestMailingDate);

			Set<Long> duplicateReferenceIds = getDupicateReferenceIds(duplicateReferenceList);
			if (!duplicateReferenceIds.isEmpty()) {
				referenceBaseDataRepository.updateParentById(duplicateRefWithLatestMailingDate, duplicateReferenceIds);
			}
		}

		boolean primaryCouple = false;
		if (Boolean.TRUE.equals(referenceBaseData.getPrimaryCouple())) {
			referenceBaseDataRepository.updateReferenceStatusAndFlowFlagBycouplingId(ReferenceStatus.DROPPED, false,
					false, referenceBaseData.getCouplingId());
			primaryCouple = true;
			// TODO : POST ACTION REF BASE CHANGE STRATEGY

		} else {
			// dropping the reference and updating the flow flag and active to
			// false.
			referenceBaseDataRepository.updateReferenceStatusAndFlowFlag(ReferenceStatus.DROPPED, false, false, id);
			// TODO : POST ACTION REF BASE CHANGE STRATEGY

		}

		// If only 1 reference exists then update the null reference table to Y.
		if (duplicateReferenceList == null || duplicateReferenceList.isEmpty() || primaryCouple) {
			CorrespondenceBase base = referenceBaseData.getCorrespondence();
			Long correspondenceId = base.getId();
			nullReferenceDocumentRepository.updateNullReferenceFlagByCorrespondenceId(true, correspondenceId);

		}
		return;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.blackbox.ids.services.reference.ReferenceService#removeReferences(java.util.List)
	 */
	@Override
	@Transactional
	public void removeReferences(final List<Long> ids) {

		for (Long id : ids) {
			removeReference(id);
		}
	}

	/**
	 * Gets the reference with latest mailing date.
	 *
	 * @param duplicateReferenceList
	 *            the duplicate reference list
	 * @return the reference with latest mailing date
	 */
	private ReferenceBaseData getReferenceWithLatestMailingDate(final List<ReferenceBaseData> duplicateReferenceList) {

		ReferenceBaseData referenceBaseData = null;
		if (duplicateReferenceList != null && !duplicateReferenceList.isEmpty()) {
			// Sorting the duplicate references according to the Mailing date
			// with latest mailing date first.

			Collections.sort(duplicateReferenceList, new Comparator<ReferenceBaseData>() {

				@Override
				public int compare(ReferenceBaseData r1, ReferenceBaseData r2) {

					if (r1.getMailingDate() != null && r2.getMailingDate() != null) {
						return r1.getMailingDate().compareTo(r2.getMailingDate());
					} else {
						return 0;
					}

				}
			});

			referenceBaseData = duplicateReferenceList.get(0);
		}

		return referenceBaseData;

	}

	/**
	 * Gets the dupicate reference ids.
	 *
	 * @param duplicateReferenceList
	 *            the duplicate reference list
	 * @return the dupicate reference ids
	 */
	private Set<Long> getDupicateReferenceIds(final List<ReferenceBaseData> duplicateReferenceList) {
		Set<Long> duplicateReferenceIds = new HashSet<Long>();
		if (duplicateReferenceList != null && !duplicateReferenceList.isEmpty()) {
			for (ReferenceBaseData referenceBaseData : duplicateReferenceList)

				duplicateReferenceIds.add(referenceBaseData.getReferenceBaseDataId());
		}

		return duplicateReferenceIds;

	}

	/**
	 * Populate dto from reference.
	 *
	 * @param referenceData
	 *            the reference data
	 * @return the reference base dto
	 */
	private ReferenceBaseDTO populateDTOFromReference(ReferenceBaseData referenceData) {

		ReferenceBaseDTO dto = new ReferenceBaseDTO();
		dto.setReferenceBaseDataId(referenceData.getReferenceBaseDataId());
		dto.setApplicationId(referenceData.getApplication().getId());
		dto.setActive(referenceData.isActive());
		// TODO convert correspondence to DTO
		CorrespondenceDTO correspondenceDTO = new CorrespondenceDTO();
		dto.setCorrespondenceId(correspondenceDTO);
		dto.setCouplingId(referenceData.getCouplingId());
		// dto.setCreatedBy(referenceData.getCreatedByUser());
		// dto.setCreatedDate(BlackboxDateUtil.calendarToStr(referenceData.getCreatedDate(),TimestampFormat.MMMDDYYYY));
		// dto.setDocumentDescription(referenceData.getCorrespondenceId().getDocumentCode().getDescription());

		JurisdictionDTO jdto = new JurisdictionDTO();
		jdto.setId(referenceData.getJurisdiction().getId());
		jdto.setName(referenceData.getJurisdiction().getCode());
		dto.setJurisdiction(jdto);
		// dto.setMailingDate(referenceData.getMailingDate());
		dto.setParentReferenceId(referenceData.getParentReferenceId().getReferenceBaseDataId());
		// dto.setPrimaryCouple(referenceData.getP);
		dto.setReferenceCatogory(referenceData.getReferenceCatogory());
		dto.setReferenceComments(referenceData.getReferenceComments());
		dto.setReferenceFlowFlag(referenceData.isReferenceFlowFlag());
		dto.setReferenceRecordId(referenceData.getReferenceRecordId());
		dto.setReferenceSource(referenceData.getReferenceSource());
		dto.setReferenceStatus(referenceData.getReferenceStatus());
		dto.setReferenceSubSource(referenceData.getReferenceSubSource());
		dto.setReviewedStatus(referenceData.isReviewedStatus());
		// dto.setSelfCitationDate(referenceData.getSelfCitationDate());

		dto.setSourceApplication(referenceData.getSourceApplication());

		return dto;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.blackbox.ids.services.reference.ReferenceService#deleteDocument(java.lang.Long)
	 */
	@Override
	@Transactional
	public void deleteDocument(Long notificationProcessId) {
		Long entityId = notificationProcessService.getEntityIdByNotificationId(notificationProcessId);
		if (entityId != null) {
			ocrDataservice.updateOcrDataAsProcessed(entityId);
			notificationProcessService.completeTask(notificationProcessId, NotificationStatus.COMPLETED);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.reference.ReferenceService#addReferenceStagingData(com.blackbox.ids.core.dto.reference.
	 * ReferenceBaseDTO)
	 */
	@Override
	@Transactional
	public void addReferenceStagingData(ReferenceBaseDTO referenceBaseDTO) {

		log.info("adding reference in Staging table ");

		if (referenceBaseDTO != null) {
			CorrespondenceBase base = null;
			ApplicationBase applicationBase = null;
			if (referenceBaseDTO.getCorrespondenceId() != null) {
				base = correspondenceBaseRepository.getOne(referenceBaseDTO.getCorrespondenceId().getId());
			} else {
				String jurisdictionCode = referenceBaseDTO.getApplicationJurisdictionCode();
				String applicationNumber = referenceBaseDTO.getApplicationNumber();
				// if correspondence is null then Self Citation.
				referenceBaseDTO.setReferenceCatogory(ReferenceCategoryType.SELF_CITATION);
				applicationBase = applicationBaseDAO.findByApplicationNoAndJurisdictionCode(jurisdictionCode,
						applicationNumber);
			}

			ReferenceType referenceType = getReferenceType(referenceBaseDTO.getReferencetype());
			referenceBaseDTO.setReferenceType(referenceType);
			referenceBaseDTO.setReferenceFlowFlag(true);
			referenceBaseDTO.setReferenceStatus(ReferenceStatus.INPADOC_PENDING);
			referenceBaseDTO.setActive(true);

			ReferenceStagingData stagingData = referenceUtil.getReferenceStageEntityFromDto(referenceBaseDTO, base,
					applicationBase);
			referenceStagingDataRepository.saveAndFlush(stagingData);
		}
		return;

	}

	@Override
	@Transactional(readOnly = true)
	public List<ReferenceDashboardDto> getNPLDuplicates(ReferenceDashboardDto referenceDTO) {

		List<ReferenceDashboardDto> duplicateReferenceList = referenceBaseDataRepository
				.getDuplicateNPLByFamilyId(referenceDTO.getFamilyId(), referenceDTO.getReferenceBaseId());

		List<ReferenceDashboardDto> referenceList = null;
		if (duplicateReferenceList != null && !duplicateReferenceList.isEmpty()) {
			referenceList = getNPLDuplicateReferences(duplicateReferenceList, referenceDTO);
		}
		return referenceList;
	}

	private List<ReferenceDashboardDto> getNPLDuplicateReferences(List<ReferenceDashboardDto> duplicateReferenceList,
			final ReferenceDashboardDto referenceDTO) {
		List<ReferenceDashboardDto> referenceList = new ArrayList<ReferenceDashboardDto>();
		for (ReferenceDashboardDto referenceBaseData : duplicateReferenceList) {
			if (!matchNPL(referenceBaseData.getNplDescription(), referenceDTO.getNplDescription())) {
				referenceList.add(referenceBaseData);
			}
		}
		return referenceList;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.blackbox.ids.services.reference.ReferenceService#uploadPDFAttachment(com.blackbox.ids.core.dto.reference.
	 * ReferenceBaseDTO)
	 */
	@Override
	public void uploadPDFAttachment(ReferenceBaseDTO referenceBaseDTO) {

		try {
			log.debug("Inside Reference upload PDF method started");
			String uploadDirectory = getReferenceAttachmentUploadDirectory(referenceBaseDTO.getReferenceType());

			long id = referenceBaseDTO.getReferenceBaseDataId();
			final String fileUploadPath = BlackboxUtils.concat(uploadDirectory, String.valueOf(id), File.separator,
					String.valueOf(id));

			String temporaryFilePath = BlackboxUtils.concat(uploadDirectory, String.valueOf(id));
			File convFile = File.createTempFile(temporaryFilePath, ".pdf");
			MultipartFile multipartFile = referenceBaseDTO.getFile();
			multipartFile.transferTo(convFile);

			File file = new File(fileUploadPath + ".pdf");
			FileUtils.copyFile(convFile, file);
			convFile.deleteOnExit();

			// update reference attachment flag after document upload
			referenceBaseDataRepository.updateAttachmentFlag(id, true);
		} catch (Exception e) {
			log.info("I/O exception occured while uploading a file to a specific location");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public String getReferenceAttachmentUploadPath(final Long id, final ReferenceType referenceType) {

		String uploadDirectory = this.getReferenceAttachmentUploadDirectory(referenceType);
		return BlackboxUtils.concat(uploadDirectory, String.valueOf(id), File.separator, String.valueOf(id));
	}

	/**
	 * @param referenceBaseDTO
	 * @return
	 */
	private String getReferenceAttachmentUploadDirectory(final ReferenceType referenceType) {
		String uploadDirectory = null;
		if (referenceType.equals(ReferenceType.FP.toString())) {
			uploadDirectory = FolderRelativePathEnum.REFERENCE.getAbsolutePath("referenceFP");
		} else {
			uploadDirectory = FolderRelativePathEnum.REFERENCE.getAbsolutePath("referenceNPL");
		}
		if (uploadDirectory != null) {
			File filedir = new File(uploadDirectory);
			if (!filedir.exists()) {
				filedir.mkdirs();
			}
		}
		return uploadDirectory;
	}

	/*************** Family Changes ******************************************/

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateReferenceFamilyLinkage(Long applicationId, String oldFamilyId, String newFamilyId) {

		List<ReferenceBaseData> referenceBaseDataList = referenceBaseDataRepository
				.getReferenceByApplicationAndFamilyId(applicationId, oldFamilyId);
		if (referenceBaseDataList != null && !referenceBaseDataList.isEmpty()) {
			dropReferencesWithOldFamilyId(referenceBaseDataList);
			addAllReferences(referenceBaseDataList, newFamilyId);
		}

	}

	private void dropReferencesWithOldFamilyId(List<ReferenceBaseData> referenceBaseDataList) {

		List<Long> referenceIds = new ArrayList<Long>();
		for (ReferenceBaseData referenceBaseData : referenceBaseDataList) {
			referenceIds.add(referenceBaseData.getReferenceBaseDataId());
		}
		referenceBaseDataRepository.updateStatusAndActiveFlagById(referenceIds, false, ReferenceStatus.DROPPED);

		// TODO : POST ACTION REF BASE CHANGE STRATEGY
	}

	private void addAllReferences(List<ReferenceBaseData> referenceBaseDataList, String newFamilyId) {

		for (ReferenceBaseData baseData : referenceBaseDataList) {
			ReferenceBaseDTO baseDTO = referenceUtil.getReferenceBaseDTOFromReferenceBaseEntity(baseData);
			baseDTO.setFamilyId(newFamilyId);
			baseDTO.setUpdateFamily(true);
			updateReferenceFamily(baseDTO);
		}

	}

	public ReferenceBaseDTO updateReferenceFamily(final ReferenceBaseDTO referenceDTO) throws ApplicationException {

		log.info("updating reference family : " + referenceDTO);

		if (referenceDTO != null) {

			ApplicationBase applicationBase = null;
			CorrespondenceBase base = null;
			if (referenceDTO.getCorrespondenceId() != null) {
				base = correspondenceBaseRepository.getOne(referenceDTO.getCorrespondenceId().getId());
				CorrespondenceDTO correspondenceDTO = getCorrespondenceDTOFromEntity(base);
				referenceDTO.setCorrespondenceId(correspondenceDTO);

			}
			if (referenceDTO.getApplicationId() != null) {
				applicationBase = applicationBaseDAO.getApplicationBase(referenceDTO.getApplicationId());
			}

			List<ReferenceBaseData> duplicateReferenceList = referenceBaseDataRepository
					.getAllByFamilyIdAndReferenceTypeAndReferenceStatus(referenceDTO.getFamilyId(),
							referenceDTO.getReferenceType(), ReferenceStatus.DROPPED);

			List<ReferenceBaseData> referenceList = null;
			if (duplicateReferenceList != null && !duplicateReferenceList.isEmpty()) {
				referenceList = getDuplicateReferences(duplicateReferenceList, referenceDTO.getReferenceType(),
						referenceDTO);
			}

			if (ReferenceType.NPL.equals(referenceDTO.getReferenceType()) && referenceDTO.isCreateFYANotification()) {

				ReferenceBaseNPLData entity = referenceUtil.getNPLReferenceBaseEntityFromDTO(referenceDTO, base,
						applicationBase);
				entity = referenceBaseDataRepository.saveAndFlush(entity);

				createNPLPendingReference(referenceDTO, base, applicationBase);
				// TODO : POST ACTION REF BASE CHANGE STRATEGY
			} else {

				if (referenceList != null && !referenceList.isEmpty()) {
					createDuplicateReferences(referenceDTO, referenceList, false, base, applicationBase);
					// TODO : POST ACTION REF BASE CHANGE STRATEGY

				} else {
					createNewReferences(referenceDTO, base, applicationBase);
					// TODO : POST ACTION REF BASE CHANGE STRATEGY
				}
			}

		} else {
			log.info(NULL_REFENCE_MESSAGE);
		}
		return referenceDTO;

	}

	/*************** POST ACTIONS AFTER REFERENCE CREATE/UPDATE/DROP ******************************************/

}
