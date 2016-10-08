package com.blackbox.ids.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.mstr.JurisdictionDAO;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.dto.reference.DuplicateNPLAttributes;
import com.blackbox.ids.core.dto.reference.JurisdictionDTO;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.dto.reference.SourceReferenceDTO;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.reference.OcrData;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceBaseFPData;
import com.blackbox.ids.core.model.reference.ReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.ReferenceBasePUSData;
import com.blackbox.ids.core.model.reference.ReferenceSourceType;
import com.blackbox.ids.core.model.reference.ReferenceStagingData;
import com.blackbox.ids.core.model.reference.ReferenceStagingFPData;
import com.blackbox.ids.core.model.reference.ReferenceStagingNPLData;
import com.blackbox.ids.core.model.reference.ReferenceStagingPUSData;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.blackbox.ids.core.model.reference.ReferenceSubSourceType;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.services.validation.NumberFormatValidationService;

@Component
public class ReferenceUtil {

	@Autowired
	NumberFormatValidationService numberFormatValidationService;

	@Autowired
	JurisdictionDAO jurisdictionDAO;

	@Value("${npl.min.percentage}")
	String nplMinPercentage;

	@Value("${npl.max.percentage}")
	String nplMaxPercentage;

	public void setConvertedString(final ReferenceStagingData referenceStagingData) {

		Calendar fillingDate = null;
		String applicationNumber = null;
		CorrespondenceBase correspondenceBase = referenceStagingData.getCorrespondence();
		if (correspondenceBase != null) {
			fillingDate = correspondenceBase.getFilingDate();
			applicationNumber = correspondenceBase.getApplication().getApplicationNumber();
		}

		ReferenceType referenceType = referenceStagingData.getReferenceType();
		String jurisdictionCode = referenceStagingData.getJurisdiction().getCode();
		String patentPublicationNumber = referenceStagingData.getPublicationNumber();

		if (ReferenceType.NPL.equals(referenceType)) {

			Calendar mailingDate = null;
			String description = null;
			if (correspondenceBase != null) {
				mailingDate = correspondenceBase.getMailingDate();
				description = correspondenceBase.getDocumentCode().getDescription();
			}

			String convertedString = createNPLString(jurisdictionCode, applicationNumber, description, mailingDate);
			referenceStagingData.getNplData().setStringData(convertedString);
		} else {
			// Fetch the particular regex from DB & perform conversion.
			String convertedString = convert(jurisdictionCode, null, fillingDate, null, patentPublicationNumber);
			if (convertedString == null || convertedString.isEmpty()) {
				convertedString = patentPublicationNumber;
			}

			switch (referenceType) {
			case PUS:
			case US_PUBLICATION:
				ReferenceStagingPUSData pusData = referenceStagingData.getPusData();
				if (pusData == null) {
					pusData = new ReferenceStagingPUSData();
					referenceStagingData.setPusData(pusData);
				}
				referenceStagingData.getPusData().setConvertedPublicationNumber(convertedString);
				break;
			case FP:
				ReferenceStagingFPData fpData = referenceStagingData.getFpData();
				if (fpData == null) {
					fpData = new ReferenceStagingFPData();
					referenceStagingData.setFpData(fpData);
				}
				referenceStagingData.getFpData().setConvertedForeignDocumentNumber(convertedString);
				break;
			}
		}

	}

	public String createNPLString(String jurisdictionCode, String applicationNumber, String documentDescription,
			Calendar mailingDate) {

		StringBuilder sb = new StringBuilder();
		sb.append(jurisdictionCode);
		sb.append(applicationNumber);
		sb.append(documentDescription);
		String mailingDateStr = BlackboxDateUtil.calendarToStr(mailingDate, TimestampFormat.FULL_DATE_TIME);
		sb.append(mailingDateStr);

		return sb.toString();
	}

	public List<ReferenceBaseData> getDuplicateReferences(List<ReferenceBaseData> duplicateReferenceList,
			final ReferenceStagingData referenceStagingData, final DuplicateNPLAttributes nplAttributes) {

		ReferenceType referenceType = referenceStagingData.getReferenceType();
		List<ReferenceBaseData> referenceList = new ArrayList<ReferenceBaseData>();

		for (ReferenceBaseData referenceBaseData : duplicateReferenceList) {

			if (referenceBaseData.getReferenceType().equals(referenceType)) {
				switch (referenceType) {
				case PUS:
				case US_PUBLICATION:
					if (matchPUSReference(referenceBaseData,
							referenceStagingData.getPusData().getConvertedPublicationNumber())) {
						referenceList.add(referenceBaseData);
					}
					break;
				case FP:
					if (matchFPReference(referenceBaseData,
							referenceStagingData.getFpData().getConvertedForeignDocumentNumber())) {
						referenceList.add(referenceBaseData);
					}
					break;
				case NPL:
					if (matchNPLReference(referenceBaseData, referenceStagingData.getNplData().getStringData())) {
						referenceList.add(referenceBaseData);
					} else {
						nplAttributes.setCreateFYANotification(true);
						nplAttributes.setReferenceStatus(ReferenceStatus.PENDING);
						nplAttributes.setReferenceFlowFlag(false);
					}
					break;
				}
			}
		}

		return referenceList;

	}

	public String convert(String jurisdictionCode, String numberType, Calendar fillingDate, Calendar grantDate,
			String patentPublicationNumber) throws ApplicationException {

		return numberFormatValidationService.getConvertedValue(patentPublicationNumber, NumberType.PUBLICATION,
				jurisdictionCode, ApplicationType.ALL, fillingDate, grantDate);

	}

	private boolean matchPUSReference(ReferenceBaseData referenceBaseData, String convertedString) {
		boolean match = false;
		ReferenceBasePUSData referenceBasePUSData = (ReferenceBasePUSData) referenceBaseData;
		if (referenceBasePUSData.getConvertedPublicationNumber().equalsIgnoreCase(convertedString)) {
			match = true;
		}
		return match;
	}

	private boolean matchFPReference(ReferenceBaseData referenceBaseData, String convertedString) {
		boolean match = false;
		ReferenceBaseFPData referenceBaseFPData = (ReferenceBaseFPData) referenceBaseData;
		if (referenceBaseFPData.getConvertedForeignDocumentNumber().equalsIgnoreCase(convertedString)) {
			match = true;
		}
		return match;
	}

	private boolean matchNPLReference(ReferenceBaseData referenceBaseData, String convertedString) {
		boolean match = false;
		ReferenceBaseNPLData referenceBaseNPLData = (ReferenceBaseNPLData) referenceBaseData;
		String nplString = referenceBaseNPLData.getStringData();

		float minPercentage = Float.valueOf(nplMinPercentage);
		float maxPercentage = Float.valueOf(nplMaxPercentage);

		/*
		 * CorrespondenceBase correspondence = referenceBaseNPLData.getCorrespondence();
		 *
		 * String nplString = createNPLString(correspondence.getJurisdictionCode(),
		 * correspondence.getApplication().getApplicationNumber(), correspondence.getDocumentCode().getDescription(),
		 * correspondence.getMailingDate());
		 */
		int distance = getDistance(nplString, convertedString);
		float percentage = Float.valueOf(distance)
				/ (referenceBaseNPLData.getStringData().length() + convertedString.length()) * 100F;

		// adding Fuzzy logic to match NPL String. This will check the string
		// matching percentage.
		if (percentage >= minPercentage && percentage <= maxPercentage) {
			match = true;
		}
		return match;
	}

	public ReferenceBaseData getReferenceBaseDataFromStaging(final ReferenceStagingData referenceStagingData,
			boolean flowFlag, ReferenceBaseData baseData) {

		ApplicationBase applicationBase = referenceStagingData.getApplication();
		CorrespondenceBase correspondenceBase = referenceStagingData.getCorrespondence();

		if (correspondenceBase != null) {
			baseData.setCorrespondence(correspondenceBase);
			baseData.setMailingDate(correspondenceBase.getMailingDate());

		} else {

			// TODO to be removed
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			baseData.setMailingDate(cal);
		}

		if (applicationBase != null) {
			baseData.setApplication(applicationBase);
			baseData.setFamilyId(applicationBase.getFamilyId());

		}
		baseData.setActive(referenceStagingData.isActive());

		baseData.setCouplingId(referenceStagingData.getCouplingId());

		baseData.setJurisdiction(referenceStagingData.getJurisdiction());

		baseData.setOcrData(referenceStagingData.getOcrData());

		if (referenceStagingData.getParentReferenceId() != null) {
			baseData.setParentReferenceId(new ReferenceBaseData(referenceStagingData.getParentReferenceId()));
		}

		baseData.setPrimaryCouple(referenceStagingData.getPrimaryCouple());
		baseData.setPublicationNumber(referenceStagingData.getPublicationNumber());
		baseData.setReferenceCatogory(referenceStagingData.getReferenceCatogoryType());
		baseData.setReferenceComments(referenceStagingData.getReferenceComments());
		baseData.setReferenceFlowFlag(flowFlag);
		baseData.setReferenceRecordId(referenceStagingData.getReferenceRecordId());
		baseData.setReferenceSource(getReferenceSourceType(referenceStagingData.getReferenceSource()));
		baseData.setReferenceStatus(referenceStagingData.getReferenceStatus());
		baseData.setReferenceSubSource(getReferenceSubSourceType(referenceStagingData.getReferenceSubSource()));
		baseData.setReferenceType(referenceStagingData.getReferenceType());
		baseData.setSelfCitationDate(referenceStagingData.getSelfCitationDate());
		baseData.setSourceApplication(referenceStagingData.getSourceApplication());
		return baseData;

	}

	public ReferenceBaseNPLData getNPLReferenceBaseEntityFromStaging(final ReferenceStagingData referenceStagingData,
			boolean flowFlag) {

		ReferenceBaseData baseData = new ReferenceBaseNPLData();
		baseData = getReferenceBaseDataFromStaging(referenceStagingData, flowFlag, baseData);

		ReferenceBaseNPLData referenceBaseNplData = (ReferenceBaseNPLData) baseData;
		ReferenceStagingNPLData referenceStagingNplData = referenceStagingData.getNplData();

		referenceBaseNplData.setAuthor(referenceStagingNplData.getAuthor());
		referenceBaseNplData.setTitle(referenceStagingNplData.getTitle());
		referenceBaseNplData.setPublicationDetail(referenceStagingNplData.getPublicationDetail());
		referenceBaseNplData.setPublicationMonth(referenceStagingNplData.getPublicationMonth());
		referenceBaseNplData.setPublicationDay(referenceStagingNplData.getPublicationDay());
		referenceBaseNplData.setPublicationYear(referenceStagingNplData.getPublicationYear());
		referenceBaseNplData.setRelevantPages(referenceStagingNplData.getRelevantPages());
		referenceBaseNplData.setVolumeNumber(referenceStagingNplData.getVolumeNumber());
		referenceBaseNplData.setURL(referenceStagingNplData.getURL());
		referenceBaseNplData.setPublicationCity(referenceStagingNplData.getPublicationCity());
		referenceBaseNplData.setStringData(referenceStagingNplData.getStringData());
		referenceBaseNplData.setApplicationSerialNumber(referenceStagingNplData.getApplicationSerialNumber());
		referenceBaseNplData.setUnPublished(referenceStagingNplData.isUnPublished());
		return referenceBaseNplData;
	}

	public ReferenceBaseFPData getFPReferenceBaseEntityFromStaging(ReferenceStagingData referenceStagingData,
			boolean flowFlag) {

		ReferenceBaseData baseData = new ReferenceBaseFPData();
		baseData = getReferenceBaseDataFromStaging(referenceStagingData, flowFlag, baseData);

		ReferenceBaseFPData referenceBaseFPData = (ReferenceBaseFPData) baseData;
		referenceBaseFPData.setKindCode(referenceStagingData.getKindCode());
		referenceBaseFPData.setPublicationDate(referenceStagingData.getPublicationDate());
		referenceBaseFPData.setApplicantName(referenceStagingData.getApplicantName());

		ReferenceStagingFPData stagingFPData = referenceStagingData.getFpData();
		referenceBaseFPData.setForeignDocumentNumber(stagingFPData.getForeignDocumentNumber());
		referenceBaseFPData.setConvertedForeignDocumentNumber(stagingFPData.getConvertedForeignDocumentNumber());

		return referenceBaseFPData;
	}

	public ReferenceBasePUSData getPUSReferenceBaseEntityFromStaging(ReferenceStagingData referenceStagingData,
			boolean flowFlag) {

		ReferenceBaseData baseData = new ReferenceBasePUSData();
		baseData = getReferenceBaseDataFromStaging(referenceStagingData, flowFlag, baseData);

		ReferenceBasePUSData referenceBasePUSData = (ReferenceBasePUSData) baseData;
		ReferenceStagingPUSData stagingPUSData = referenceStagingData.getPusData();

		referenceBasePUSData.setConvertedPublicationNumber(stagingPUSData.getConvertedPublicationNumber());
		referenceBasePUSData.setKindCode(referenceStagingData.getKindCode());
		referenceBasePUSData.setPublicationDate(referenceStagingData.getPublicationDate());
		referenceBasePUSData.setApplicantName(referenceStagingData.getApplicantName());

		return referenceBasePUSData;
	}

	private ReferenceBaseData getReferenceBaseEntityFromDto(ReferenceBaseDTO referenceDTO,
			CorrespondenceBase correspondenceBase, ReferenceBaseData baseData, ApplicationBase applicationBase) {

		String familyId = null;

		if (correspondenceBase != null) {
			baseData.setApplication(correspondenceBase.getApplication());
			baseData.setCorrespondence(correspondenceBase);
			familyId = correspondenceBase.getApplication().getFamilyId();
			baseData.setMailingDate(correspondenceBase.getMailingDate());
		} else if (applicationBase != null) {

			// Self Citation
			baseData.setApplication(applicationBase);
			familyId = applicationBase.getFamilyId();
			Calendar selfCitationDate = Calendar.getInstance();
			selfCitationDate.setTime(new Date());
			baseData.setSelfCitationDate(selfCitationDate);
		}

		if (referenceDTO.isUpdateFamily()) {
			baseData.setFamilyId(referenceDTO.getFamilyId());
		}else{
			baseData.setFamilyId(familyId);
		}

		baseData.setCouplingId(referenceDTO.getCouplingId());

		Long jurisdictionId = jurisdictionDAO.getIdByCode(referenceDTO.getJurisdiction().getName());
		Jurisdiction jurisdiction = new Jurisdiction();
		jurisdiction.setId(jurisdictionId);

		baseData.setJurisdiction(jurisdiction);

		if (referenceDTO.getOcrDataId() != null) {
			OcrData ocrData = new OcrData();
			ocrData.setOcrDataId(referenceDTO.getOcrDataId());
			baseData.setOcrData(ocrData);
		}

		if (referenceDTO.getParentReferenceId() != null) {
			baseData.setParentReferenceId(new ReferenceBaseData(referenceDTO.getParentReferenceId()));
		}

		baseData.setPrimaryCouple(referenceDTO.isPrimaryCouple());
		baseData.setPublicationNumber(referenceDTO.getPublicationNumber());
		baseData.setReferenceCatogory(referenceDTO.getReferenceCatogory());
		baseData.setReferenceComments(referenceDTO.getReferenceComments());
		baseData.setReferenceFlowFlag(referenceDTO.isReferenceFlowFlag());
		baseData.setReferenceRecordId(referenceDTO.getReferenceRecordId());
		baseData.setReferenceSource(referenceDTO.getReferenceSource());
		baseData.setReferenceStatus(referenceDTO.getReferenceStatus());
		baseData.setReferenceSubSource(referenceDTO.getReferenceSubSource());
		baseData.setReferenceType(referenceDTO.getReferenceType());
		baseData.setSourceApplication(referenceDTO.getSourceApplication());
		return baseData;

	}

	public ReferenceBaseNPLData getNPLReferenceBaseEntityFromDTO(ReferenceBaseDTO referenceDTO,
			CorrespondenceBase correspondenceBase, ApplicationBase applicationBase) {

		ReferenceBaseData referenceBaseData = new ReferenceBaseNPLData();

		ReferenceBaseData baseData = getReferenceBaseEntityFromDto(referenceDTO, correspondenceBase, referenceBaseData,
				applicationBase);

		ReferenceBaseNPLData referenceBaseNplData = (ReferenceBaseNPLData) baseData;

		referenceBaseNplData.setAuthor(referenceDTO.getAuthor());
		referenceBaseNplData.setTitle(referenceDTO.getTitle());
		referenceBaseNplData.setPublicationDetail(referenceDTO.getPublicationDetail());
		referenceBaseNplData.setPublicationMonth(referenceDTO.getPublicationMonth());
		referenceBaseNplData.setPublicationDay(referenceDTO.getPublicationDay());
		referenceBaseNplData.setPublicationYear(referenceDTO.getPublicationYear());
		referenceBaseNplData.setRelevantPages(referenceDTO.getRelevantPages());
		referenceBaseNplData.setVolumeNumber(referenceDTO.getVolumeNumber());
		referenceBaseNplData.setURL(referenceDTO.getURL());
		referenceBaseNplData.setPublicationCity(referenceDTO.getPublicationCity());
		referenceBaseNplData.setStringData(referenceDTO.getStringData());
		referenceBaseNplData.setApplicationSerialNumber(referenceDTO.getApplicationSerialNumber());
		referenceBaseNplData.setUnPublished(referenceDTO.isUnPublished());
		return referenceBaseNplData;
	}

	public ReferenceBaseFPData getFPReferenceBaseEntityFromDTO(ReferenceBaseDTO referenceDTO,
			CorrespondenceBase correspondenceBase, ApplicationBase applicationBase) {

		ReferenceBaseData referenceBaseData = new ReferenceBaseFPData();

		ReferenceBaseData baseData = getReferenceBaseEntityFromDto(referenceDTO, correspondenceBase, referenceBaseData,
				applicationBase);

		ReferenceBaseFPData referenceBaseFPData = (ReferenceBaseFPData) baseData;

		referenceBaseFPData.setKindCode(referenceDTO.getKindCode());

		String publicationDateStr = referenceDTO.getPublicationDateStr();
		if (!StringUtils.isEmpty(publicationDateStr)) {
			Calendar publicationDate = BlackboxDateUtil.strToCalendar(publicationDateStr, TimestampFormat.MMMDDYYYY);
			referenceBaseFPData.setPublicationDate(publicationDate);
		}

		referenceBaseFPData.setApplicantName(referenceDTO.getApplicantName());
		referenceBaseFPData.setForeignDocumentNumber(referenceDTO.getPublicationNumber());
		referenceBaseFPData.setConvertedForeignDocumentNumber(referenceDTO.getConvertedForeignDocumentNumber());

		return referenceBaseFPData;
	}

	public ReferenceBasePUSData getPUSReferenceBaseEntityFromDTO(ReferenceBaseDTO referenceDTO,
			CorrespondenceBase correspondenceBase, ApplicationBase applicationBase) {

		ReferenceBaseData referenceBaseData = new ReferenceBasePUSData();
		ReferenceBaseData baseData = getReferenceBaseEntityFromDto(referenceDTO, correspondenceBase, referenceBaseData,
				applicationBase);
		ReferenceBasePUSData referenceBasePUSData = (ReferenceBasePUSData) baseData;

		String publicationDateStr = referenceDTO.getPublicationDateStr();

		if (!StringUtils.isEmpty(publicationDateStr)) {
			Calendar publicationDate = BlackboxDateUtil.strToCalendar(publicationDateStr, TimestampFormat.MMMDDYYYY);
			referenceBasePUSData.setPublicationDate(publicationDate);
		}

		referenceBasePUSData.setConvertedPublicationNumber(referenceDTO.getConvertedPublicationNumber());
		referenceBasePUSData.setKindCode(referenceDTO.getKindCode());
		referenceBasePUSData.setApplicantName(referenceDTO.getApplicantName());

		return referenceBasePUSData;
	}

	public ReferenceStagingData getReferenceStageEntityFromDto(ReferenceBaseDTO referenceDTO,
			CorrespondenceBase correspondenceBase, ApplicationBase applicationBase) {

		ReferenceStagingData referenceStagingData = new ReferenceStagingData();

		referenceStagingData.setActive(referenceDTO.isActive());
		referenceStagingData.setApplicantName(referenceDTO.getApplicantName());
		if (correspondenceBase != null) {
			referenceStagingData.setApplication(correspondenceBase.getApplication());
			referenceStagingData.setCorrespondence(correspondenceBase);
			referenceStagingData.setMailingDate(correspondenceBase.getMailingDate());
		} else if (applicationBase != null) {
			referenceStagingData.setApplication(applicationBase);
		}

		referenceStagingData.setCouplingId(referenceDTO.getCouplingId());

		Long jurisdictionId = jurisdictionDAO.getIdByCode(referenceDTO.getJurisdiction().getName());
		Jurisdiction jurisdiction = new Jurisdiction();
		jurisdiction.setId(jurisdictionId);
		referenceStagingData.setJurisdiction(jurisdiction);

		referenceStagingData.setKindCode(referenceDTO.getKindCode());

		if (referenceDTO.getOcrDataId() != null) {
			OcrData ocrData = new OcrData();
			ocrData.setOcrDataId(referenceDTO.getOcrDataId());
			referenceStagingData.setOcrData(ocrData);
		}

		referenceStagingData.setParentReferenceId(referenceDTO.getParentReferenceId());
		referenceStagingData.setPrimaryCouple(referenceDTO.isPrimaryCouple());
		referenceStagingData.setPublicationDate(referenceDTO.getPublicationDate());
		referenceStagingData.setPublicationNumber(referenceDTO.getPublicationNumber());

		referenceStagingData.setReferenceCatogoryType(referenceDTO.getReferenceCatogory());
		referenceStagingData.setReferenceComments(referenceDTO.getReferenceComments());
		referenceStagingData.setReferenceRecordId(referenceDTO.getReferenceRecordId());
		if (referenceDTO.getReferenceSource() != null) {
			referenceStagingData.setReferenceSource(referenceDTO.getReferenceSource().toString());
		}
		referenceStagingData.setReferenceStatus(referenceDTO.getReferenceStatus());
		if (referenceDTO.getReferenceSubSource() != null) {
			referenceStagingData.setReferenceSubSource(referenceDTO.getReferenceSubSource().toString());
		}

		referenceStagingData.setReferenceType(referenceDTO.getReferenceType());
		referenceStagingData.setSelfCitationDate(referenceDTO.getSelfCitationDate());
		referenceStagingData.setSourceApplication(referenceDTO.getSourceApplication());
		referenceStagingData.setNotificationSent(false);

		if (ReferenceType.FP.equals(referenceDTO.getReferenceType())) {
			ReferenceStagingFPData fpData = new ReferenceStagingFPData();
			fpData.setConvertedForeignDocumentNumber(referenceDTO.getConvertedForeignDocumentNumber());
			fpData.setForeignDocumentNumber(referenceDTO.getForeignDocumentNumber());
			fpData.setJurisdictionCode(referenceDTO.getApplicationJurisdictionCode());
			referenceStagingData.setFpData(fpData);
		}

		if (ReferenceType.NPL.equals(referenceDTO.getReferenceType())) {
			ReferenceStagingNPLData nplData = new ReferenceStagingNPLData();
			nplData.setApplicationSerialNumber(referenceDTO.getApplicationSerialNumber());
			nplData.setAuthor(referenceDTO.getAuthor());
			nplData.setPublicationCity(referenceDTO.getPublicationCity());
			nplData.setPublicationDay(referenceDTO.getPublicationDay());
			nplData.setPublicationDetail(referenceDTO.getPublicationDetail());
			nplData.setPublicationMonth(referenceDTO.getPublicationMonth());
			nplData.setPublicationYear(referenceDTO.getPublicationYear());
			nplData.setRelevantPages(referenceDTO.getRelevantPages());
			nplData.setStringData(referenceDTO.getStringData());
			nplData.setTitle(referenceDTO.getTitle());
			nplData.setUnPublished(referenceDTO.isUnPublished());
			nplData.setURL(referenceDTO.getURL());
			nplData.setVolumeNumber(referenceDTO.getVolumeNumber());
			referenceStagingData.setNplData(nplData);
		}

		if (ReferenceType.PUS.equals(referenceDTO.getReferenceType())
				|| ReferenceType.US_PUBLICATION.equals(referenceDTO.getReferenceType())) {
			ReferenceStagingPUSData stagingPUSData = new ReferenceStagingPUSData();
			stagingPUSData.setConvertedPublicationNumber(referenceDTO.getConvertedPublicationNumber());
			referenceStagingData.setPusData(stagingPUSData);
		}

		return referenceStagingData;

	}

	public ReferenceBaseDTO getReferenceBaseDTOFromReferenceBaseEntity(ReferenceBaseData referenceBaseData) {

		ReferenceBaseDTO dto = new ReferenceBaseDTO();
		if (referenceBaseData.getApplication() != null) {
			dto.setApplicationId(referenceBaseData.getApplication().getId());

		}
		if (referenceBaseData.getCorrespondence() != null) {
			CorrespondenceDTO correspondenceDTO = new CorrespondenceDTO();
			correspondenceDTO.setId(referenceBaseData.getCorrespondence().getId());
			dto.setCorrespondenceId(correspondenceDTO);
			dto.setMailingDate(referenceBaseData.getMailingDate());
		}

		dto.setCouplingId(referenceBaseData.getCouplingId());

		// referenceBaseData.getCreatedByUser();
		// referenceBaseData.getCreatedDate();
		dto.setFamilyId(referenceBaseData.getFamilyId());
		dto.setJurisdiction(getJurisdictionDTOFromEntity(referenceBaseData.getJurisdiction()));

		dto.setNotificationSent(referenceBaseData.getNotificationSent());

		if (referenceBaseData.getOcrData() != null) {
			dto.setOcrDataId(referenceBaseData.getOcrData().getOcrDataId());
		}

		// referenceBaseData.getParentReferenceId();
		// referenceBaseData.getPrimaryCouple();
		dto.setPublicationNumber(referenceBaseData.getPublicationNumber());
		dto.setReferenceCatogory(referenceBaseData.getReferenceCatogory());
		dto.setReferenceComments(referenceBaseData.getReferenceComments());
		dto.setReferenceRecordId(referenceBaseData.getReferenceRecordId());
		dto.setReferenceSource(referenceBaseData.getReferenceSource());

		// referenceBaseData.getReferenceStatus();
		dto.setReferenceSubSource(referenceBaseData.getReferenceSubSource());
		dto.setReferenceType(referenceBaseData.getReferenceType());
		dto.setSelfCitationDate(referenceBaseData.getSelfCitationDate());
		dto.setSourceApplication(referenceBaseData.getSourceApplication());
		if (referenceBaseData.getSourceReference() != null) {

			SourceReferenceDTO sourceReference = new SourceReferenceDTO(referenceBaseData.getSourceReference().getId());
			dto.setSourceReference(sourceReference);
		}

		referenceBaseData.getSourceReference();
		// referenceBaseData.getUpdatedByUser();
		// referenceBaseData.getUpdatedDate();

		if (ReferenceType.FP.equals(referenceBaseData.getReferenceType())) {

			ReferenceBaseFPData fpData = (ReferenceBaseFPData) referenceBaseData;
			dto.setForeignDocumentNumber(fpData.getForeignDocumentNumber());
			dto.setConvertedForeignDocumentNumber(fpData.getConvertedForeignDocumentNumber());
			dto.setKindCode(fpData.getKindCode());
			dto.setPublicationDate(fpData.getPublicationDate());
			dto.setApplicantName(fpData.getApplicantName());
		} else if (ReferenceType.PUS.equals(referenceBaseData.getReferenceType())
				|| ReferenceType.US_PUBLICATION.equals(referenceBaseData.getReferenceType())) {

			ReferenceBasePUSData pusData = (ReferenceBasePUSData) referenceBaseData;
			dto.setConvertedPublicationNumber(pusData.getConvertedPublicationNumber());
			dto.setKindCode(pusData.getKindCode());

			Calendar publicationDate = pusData.getPublicationDate();

			if (publicationDate != null) {
				String publicationDateStr = BlackboxDateUtil.calendarToStr(publicationDate, TimestampFormat.MMMDDYYYY);
				dto.setPublicationDateStr(publicationDateStr);
			}
			dto.setApplicantName(pusData.getApplicantName());
		} else {

			ReferenceBaseNPLData nplData = (ReferenceBaseNPLData) referenceBaseData;
			dto.setAuthor(nplData.getAuthor());
			dto.setTitle(nplData.getTitle());
			dto.setPublicationDetail(nplData.getPublicationDetail());
			dto.setPublicationMonth(nplData.getPublicationMonth());
			dto.setPublicationDay(nplData.getPublicationDay());
			dto.setPublicationYear(nplData.getPublicationYear());
			dto.setRelevantPages(nplData.getRelevantPages());
			dto.setVolumeNumber(nplData.getVolumeNumber());
			dto.setURL(nplData.getURL());
			dto.setPublicationCity(nplData.getPublicationCity());
			dto.setStringData(nplData.getStringData());
			dto.setUnPublished(nplData.isUnPublished());
			dto.setApplicationSerialNumber(nplData.getApplicationSerialNumber());
		}

		return dto;
	}

	private JurisdictionDTO getJurisdictionDTOFromEntity(Jurisdiction jurisdiction) {
		JurisdictionDTO dto = null;
		if (jurisdiction != null) {
			dto = new JurisdictionDTO();
		}
		dto.setId(jurisdiction.getId());
		dto.setName(jurisdiction.getCode());
		return dto;

	}

	private ReferenceSourceType getReferenceSourceType(String referenceSourceType) {

		ReferenceSourceType sourceType = null;

		if (referenceSourceType != null) {
			switch (referenceSourceType) {
			case "AUTOMATIC":
				sourceType = ReferenceSourceType.AUTOMATIC;
				break;
			case "MANUAL":
				sourceType = ReferenceSourceType.MANUAL;
				break;

			}
		}

		return sourceType;
	}

	private ReferenceSubSourceType getReferenceSubSourceType(String referenceSubSourceType) {

		ReferenceSubSourceType sourceType = null;

		if (referenceSubSourceType != null) {
			switch (referenceSubSourceType) {
			case "OCR":
				sourceType = ReferenceSubSourceType.OCR;
				break;
			case "MANUAL":
				sourceType = ReferenceSubSourceType.MANUAL;
				break;
			case "THIRD_PARTY":
				sourceType = ReferenceSubSourceType.THIRD_PARTY;
				break;
			case "SYSTEM":
				sourceType = ReferenceSubSourceType.SYSTEM;
				break;

			}
		}

		return sourceType;
	}

	private static int getDistance(String firstString, String secondString) {
		return (StringUtils.getLevenshteinDistance(firstString, secondString));
	}

}
