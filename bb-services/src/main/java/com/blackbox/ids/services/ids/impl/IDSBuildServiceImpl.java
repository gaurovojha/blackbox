/**
 *
 */
package com.blackbox.ids.services.ids.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.AttorneyUserDetailsDAO;
import com.blackbox.ids.core.dao.ids.IDSDao;
import com.blackbox.ids.core.dao.mstr.IDSFilingFeeDAO;
import com.blackbox.ids.core.dto.IDS.ApplicationDetailsDTO;
import com.blackbox.ids.core.dto.IDS.IDSFeeCalculationDetails;
import com.blackbox.ids.core.dto.IDS.ReferenceRecordDTO;
import com.blackbox.ids.core.dto.IDS.ReferenceRecordsFilter;
import com.blackbox.ids.core.dto.IDS.UserDetails;
import com.blackbox.ids.core.dto.IDS.notification.NotificationBaseDTO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.model.IDS.CertificationStatement;
import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.IDS.IDS.Sequence;
import com.blackbox.ids.core.model.IDS.IDS.Status;
import com.blackbox.ids.core.model.enums.FilePackage;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Assignee.Entity;
import com.blackbox.ids.core.model.mstr.IDSRelevantStatus;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.repository.referenceflow.IDSReferenceFlowRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.file.FileIOUtil;
import com.blackbox.ids.services.ids.IDSBuildService;
import com.blackbox.ids.services.notification.process.NotificationProcessService;

/**
 * @author ajay2258
 */
@Service(IDSBuildService.BEAN_NAME)
public class IDSBuildServiceImpl implements IDSBuildService {

	private final Logger									log							= Logger
			.getLogger(IDSBuildService.class);
	private static final String								IDS_NOT_ISSUED				= "notIssued";
	private static final String								IDS_ISSUED					= "issued";
	private static final String								REF_FOUND_IDS_NOT_ISSUED	= "refFIDSNIssued";
	private static final String								REF_FOUND_IDS_ISSUED		= "refFIDSIssued";
	private static final String								REF_FOUND					= "refFound";
	private static final String								IDS_CONFIRM_POP_UP			= "popUpName";
	private static final String								REF_FLOWS_IDS				= "refFlowsIds";
	private static final Long								MAX_US_REFS_IN_1_IDS		= 300L;
	private static final Long								MAX_FP_REFS_IN_1_IDS		= 50L;
	private static final Long								MAX_NPL_REFS_IN_1_IDS		= 50L;

	@Autowired
	private IDSDao idsDAO;

	@Autowired
	private IDSReferenceFlowRepository<IDSReferenceFlow> refFlowRepository;

	@Autowired
	private ApplicationBaseDAO applicationDAO;

	@Autowired
	private IDSFilingFeeDAO idsFilingFeeDAO;

	@Autowired
	private AttorneyUserDetailsDAO attorneyUserDAO;

	@Autowired
	private NotificationProcessService notificationService;

	@Override
	@Transactional
	public IDS initiateIDS(final Long targetApplication) {
		IDS ids = new IDS(generateBuildId(), new ApplicationBase(targetApplication), Status.IN_PROGRESS);
		ids = idsDAO.persist(ids);
		long numRefFlowsClaimed = claimRefFlowsForIDS(targetApplication, ids.getId());
		log.info(String.format("Claimed {0} reference flows for IDS {1} preparation.", numRefFlowsClaimed,
				ids.getIdsBuildId()));
		return ids;
	}

	private String generateBuildId() {
		return idsDAO.generateSequenceId(Sequence.BUILD_ID);
	}

	private long claimRefFlowsForIDS(final long targetApplication, final long idsId) {
		return refFlowRepository.claimRefFlowsForIDS(targetApplication, idsId);
	}

	@Override
	@Transactional
	public boolean discardIDS(final long ids, final long discardedBy) {
		log.info(String.format("Processing discard request for IDS {0} by user {1}.", ids, discardedBy));
		refFlowRepository.releaseRefFlowsFromIDS(ids, discardedBy);
		return idsDAO.updateIDSStatus(ids, IDS.Status.DISCARDED, discardedBy);
	}

	@Override
	@Transactional
	public Long raiseIDSApprovalRequest(long idsID, long attorney, Long approvalNotificationId) {
		log.info(String.format("[IDS APPROVAL] IDS: {0}, Attorney: {1}. Updating database record.", idsID, attorney));
		final IDS ids = idsDAO.find(idsID);
		updateIDSForApproval(ids, attorney);
		idsDAO.persist(ids);
		return raiseApprovalNotification(ids, Arrays.asList(attorney), approvalNotificationId);
	}

	private Long raiseApprovalNotification(IDS ids, List<Long> attorneys, Long approvalNotificationId) {
		final long idsID = ids.getId();
		Long notificationProcessID = approvalNotificationId;

		if (approvalNotificationId == null) {
			log.info(String.format("[IDS APPROVAL] IDS: {0}, Attorney: {1}. Sending notification.", idsID, attorneys));
			notificationProcessID = notificationService.createNotification(ids.getApplication(), null, null, idsID,
					EntityName.IDS, NotificationProcessType.IDS_APPROVAL_REQUEST, attorneys, null);
		} else {
			log.info(String.format("[IDS APPROVAL] IDS: {0}, Attorney: {1}. Resuming notification.", idsID, attorneys));
			notificationService.resubmitNotification(approvalNotificationId, attorneys);
		}
		return notificationProcessID;
	}


	private IDS updateIDSForApproval(final IDS ids, final long attorney) {
		long currentUser = BlackboxSecurityContextHolder.getUserId();
		Calendar sysdate = Calendar.getInstance();
		ids.setStatus(Status.PENDING_APPROVAL);
		ids.setApprovalRequestedBy(currentUser);
		ids.setApprovalRequestedOn(sysdate);
		ids.setApprovedBy(attorney);
		return idsDAO.persist(ids);
	}

	@Override
	@Transactional
	public long saveCertificationStatement(final long idsID, final CertificationStatement certificate) {
		if (certificate == null) {
			throw new IllegalArgumentException("Missing mandatory attributes.");
		}

		final IDS ids = idsDAO.find(idsID);
		certificate.setId(ids.getCertificate() == null ? null : ids.getCertificate().getId());
		ids.setCertificate(certificate);
		idsDAO.persist(ids);
		long certificateID = ids.getCertificate().getId();

		// Save attachment
		if (certificate.isCertificationAttached()) {
			try {
				FileIOUtil.saveFile(FilePackage.CERTIFICATION_STATEMENT, String.valueOf(certificateID),
						certificate.getAttachment().getBytes());
			} catch (IOException e) {
				throw new ApplicationException(e, "Failed to save certification statement for IDS {0}.", idsID);
			}
		}

		return certificateID;
	}

	@Override
	@Transactional
	public void processIDSApproval(long idsID, long attorney) {
		IDS ids = idsDAO.find(idsID);
		ids.setApprovedBy(attorney);
		ids.setApprovedOn(Calendar.getInstance());
		ids.setStatus(IDS.Status.APPROVED);
		idsDAO.persist(ids);
	}

	@Override
	public SearchResult<ReferenceRecordDTO> fetchReferenceRecords(final ReferenceRecordsFilter filter,
			final PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Fetch Reference Records: Missing mandatory parameters.");
		}

		SearchResult<ReferenceRecordDTO> resultSet = null;
		switch (filter.getReferenceType()) {
		case PUS:
		case US_PUBLICATION:
			resultSet = refFlowRepository.fetchUSPatents(filter, pageInfo);
			break;

		case FP:
			resultSet = refFlowRepository.fetchForeignData(filter, pageInfo);
			break;

		case NPL:
			resultSet = refFlowRepository.fetchNPLData(filter, pageInfo);
			break;

		default:
			throw new IllegalArgumentException(
					String.format("Fetch Reference Records: Unhandled reference type {0}.", filter.getReferenceType()));
		}

		return resultSet;
	}

	@Override
	public ApplicationDetailsDTO fetchApplicationInfo(final long appId) {
		return applicationDAO.fetchAppInfoForIDS(appId);
	}

	@Override
	public ApplicationDetailsDTO fetchAppInfoForNotification(final long appId) {
		return applicationDAO.fetchAppInfoForNotification(appId);
	}

	@Override
	public SearchResult<NotificationBaseDTO> getNotificationsOfApp(long applicationId,
			PaginationInfo pageInfo, List<Long> skipNotificationList) {
		return idsDAO.getAppNotifications(applicationId, pageInfo, skipNotificationList);
	}

	@Override
	public SearchResult<NotificationBaseDTO> getNotificationsOfFamily(long applicationId,
			PaginationInfo pageInfo, List<Long> skipNotificationList) {
		return idsDAO.getFamilyNotifications(applicationId, pageInfo, skipNotificationList);
	}

	@Override
	public Map<String, Long> countNotifications(long applicationId, List<Long> skipAppNotificationList,
			List<Long> skipFamilyNotificationList) {
		return idsDAO.countNotifications(applicationId, skipAppNotificationList, skipFamilyNotificationList);
	}

	@Override
	public Double calculateIDSFilingFee(final long ids) {
		log.info(String.format("[IDS FILING FEE]: Calculating for IDS {0}.", ids));
		/*-
		•	3 months from the application filing date – Zero fees (irrespective of the prosecution status or aging of reference)
		•	No fee is applicable if an IDS is being cited when Prosecution status = First OA pending or Restriction requirement received or RCE filed. Irrespective of the aging
		•	Prosecution status is Non-final OA received & aging of reference 90 or less than 90 days, zero fees is applicable.
			If the aging of reference is greater than 90 days & prosecution status is Non-final OA received, IDS filing fees = applicable fee per entity status.
		•	Prosecution status = Final OA received, Advisory OA received, NOA received, and/or Ex-parte Quayle OA received & aging of reference is 90 or less than 90 days,
			applicable fee per entity status
		 */
		boolean feeApplies = false;
		IDSFeeCalculationDetails feeParams = refFlowRepository.fetchIDSFilingFeeParams(ids);
		int filedBefore = BlackboxDateUtil.daysDiff(feeParams.applicationFilingDate.getTime(), new Date());

		if (filedBefore > 90) {
			feeApplies = feeParams.prosecutionStatus == null;
			if (feeParams.prosecutionStatus != null) {
				switch (feeParams.prosecutionStatus) {
				case FIRST_OA_PENDING:
				case RESTRICTION_REQUIREMENT_RECEIVED:
				case RCE_FILED:
					feeApplies = false;
					break;

				case NON_FINAL_OA_RECEIVED:
					feeApplies = feeParams.agingOfReferences > 90;
					break;

				case FINAL_OA_RECEIVED:
				case ADVISORY_OA_RECEIVED:
				case NOA_RECEIVED:
				case EX_PARTE_QUAYLE_OA_RECEIVED:
					feeApplies = feeParams.agingOfReferences <= 90;
					break;

				default:
					feeApplies = true;
					break;
				}
			}
		}

		log.info(String.format(
				"[IDS FILING FEE] IDS: {0}, Fee Applicable: {1}, Prosecution Status: {2}, Aging of References: {3}.",
				ids, feeApplies, feeParams.prosecutionStatus, feeParams.agingOfReferences));
		return feeApplies ? fetchFeeForEntity(feeParams.entity) : null;
	}

	private Double fetchFeeForEntity(Entity entity) {
		return idsFilingFeeDAO.findFeeByEntityOn(entity, Calendar.getInstance());
	}

	// countTotalRecords
	@Override
	public Map<ReferenceType, Long> countReferenceRecords(final ReferenceRecordsFilter filter) {
		if (filter == null) {
			throw new IllegalArgumentException("Fetch Reference Records: Missing mandatory parameters.");
		}
		return refFlowRepository.countReferenceRecords(filter);
	}

	@Override
	public Long getApplicationIdForIDSId(long idsId) {
		return idsDAO.getApplicationIdForIDSId(idsId);
	}

	@Override
	public Long findIdsInProgressForApplication(final long applicationID) {
		List<Long> idsIDs = idsDAO.findWithStatusEq(applicationID, IDS.Status.IN_PROGRESS);
		return idsIDs.isEmpty() ? null : idsIDs.get(0);
	}
	
	@Override
	public Long findIdsInProgressOrApprovedForApplication(final long applicationID) {
		List<Long> idsIDs = idsDAO.findWithStatusEq(applicationID, IDS.Status.IN_PROGRESS, IDS.Status.APPROVED);
		return idsIDs.isEmpty() ? null : idsIDs.get(0);
	}

	@Override
	public List<UserDetails> fetchAttorneyUsersDetails(final List<Long> userIds) {
		if (CollectionUtils.isEmpty(userIds)) {
			throw new IllegalArgumentException("Missing mandatory parameters.");
		}
		return attorneyUserDAO.fetchUserDetails(userIds);
	}

	@Override
	@Transactional
	public Long dropReferenceRecords(final Long refFlowId) {
		if (refFlowId == null) {
			throw new IllegalArgumentException("[DONT FILE REFERENCE]: Missing mandatory parameters.");
		}
		log.info(String.format("Executing Don't File request for reference {0}.", refFlowId));
		return refFlowRepository.doNotFileAction(refFlowId);
	}

	@Override
	@Transactional
	public Long dropReferenceFromIDS(final Long refFlowId) {
		if (refFlowId == null) {
			throw new IllegalArgumentException("[DROP REFERENCES]: Missing mandatory parameters.");
		}
		log.info(String.format("Executing references {0} drop from IDS.", refFlowId));
		return refFlowRepository.dropRefFlowFromIDS(refFlowId);
	}


	@Override
	@Transactional
	public long includeRefInCurrentIDS(final long iDSId, List<Long> refFlowsIdsList) {
		return refFlowRepository.includeRefInCurrentIDS(iDSId, refFlowsIdsList);
	}

	@Override
	public CertificationStatement fetchCertificationStatement(final long idsID) {
		return idsDAO.findCertificationStatement(idsID);
	}

	@Override
	public Map<String, Object> getValipPopUpMap(final long appId, final long referenceAge,
			final String prosecutionStatus) {

		Map<String, Object> popUpMap = new HashMap<String, Object>();
		String popUpValue = "";
		List<Long> refList = new ArrayList<Long>();
		refList = refFlowRepository.findNewUncitedRef(appId);
		String idsConfirmPopUp = "";
		if (CollectionUtils.isNotEmpty(refList)) {
			popUpValue = checkProsecutionStatus(prosecutionStatus, referenceAge);
			idsConfirmPopUp = checkRefAndProsescutionPopUP(popUpValue);
		} else {
			idsConfirmPopUp = checkProsecutionStatus(prosecutionStatus, referenceAge);
		}
		popUpMap.put(IDS_CONFIRM_POP_UP, idsConfirmPopUp);
		popUpMap.put(REF_FLOWS_IDS, refList);

		return popUpMap;
	}

	/**
	 * Check the pop value based on Prosecution Status and age of the reference
	 * @param prosStatus
	 *            the Prosecution Status
	 * @param referenceAge
	 *            agee of the reference
	 * @return {@link String} the popUpvalue
	 */
	private String checkProsecutionStatus(final String prosStatus, final long referenceAge) {
		String popUpValue = "";
		if ((Arrays.asList(IDSRelevantStatus.FINAL_OA_RECEIVED.name(), IDSRelevantStatus.NOA_RECEIVED.name(),
				IDSRelevantStatus.EX_PARTE_QUAYLE_OA_RECEIVED.name()).contains(prosStatus)) && referenceAge > 90) {
			popUpValue = IDS_NOT_ISSUED;
		} else if (Arrays
				.asList(IDSRelevantStatus.ISSUE_FEE_PAID.name(), IDSRelevantStatus.ISSUE_NOTIFICATION_RECEIVED.name())
				.contains(prosStatus)) {
			popUpValue = IDS_ISSUED;
		}
		return popUpValue;
	}

	/**
	 * Check the pop value based on Prosecution Status and age of the reference and new references found
	 * @param popUpValue
	 *            the pop up value from Prosecution status and reference age
	 * @return {@link String} the popUpvalue
	 */
	private String checkRefAndProsescutionPopUP(final String popUpValue) {
		String idsConfirmPopUp;
		if (popUpValue.equalsIgnoreCase(IDS_NOT_ISSUED)) {
			idsConfirmPopUp = REF_FOUND_IDS_NOT_ISSUED;
		} else if (popUpValue.equalsIgnoreCase(IDS_ISSUED)) {
			idsConfirmPopUp = REF_FOUND_IDS_ISSUED;
		} else {
			idsConfirmPopUp = REF_FOUND;
		}
		return idsConfirmPopUp;
	}

	@Override
	public Map<ReferenceType, Boolean> checkReferencesCountPopUp(ReferenceRecordsFilter refFilter) {

		if (refFilter == null) {
			throw new IllegalArgumentException("Fetch Reference Records Count: Missing mandatory parameters.");
		}

		Map<ReferenceType, Long> countResult = refFlowRepository.countReferenceRecords(refFilter);
		Map<ReferenceType, Boolean> refPopUpMap = new HashMap<ReferenceType, Boolean>(countResult.size());

		for (Map.Entry<ReferenceType, Long> mapEntry : countResult.entrySet()) {
			switch (mapEntry.getKey()) {
			case US_PUBLICATION:
				if (mapEntry.getValue() > MAX_US_REFS_IN_1_IDS) {
					refPopUpMap.put(ReferenceType.US_PUBLICATION, true);
				}
				break;
			case FP:
				if (mapEntry.getValue() > MAX_FP_REFS_IN_1_IDS) {
					refPopUpMap.put(ReferenceType.FP, true);
				}
				break;
			case NPL:
				if (mapEntry.getValue() > MAX_NPL_REFS_IN_1_IDS) {
					refPopUpMap.put(ReferenceType.NPL, true);
				}
				break;
			default:
				break;
			}
		}
		return refPopUpMap;
	}

	@Override
	public Long getNewReferenceCount(Long appId) {
		 log.info("Getting count of references to notify attorney");
		return refFlowRepository.getNewReferencesCount(appId);
	}

	@Override
	@Transactional
	public void updateNotifyAttorneyFlag(String appId) {
	
		refFlowRepository.updateNotifyAttorneyFlag(appId);
		
	}

	@Override
	@Transactional
	public void updateNewReferences(Long applicationId, Long iDSId) {
		
		refFlowRepository.updateNewReferences(applicationId, iDSId);
		
	}

	
	/*@Override
	@Transactional
	public boolean updateIDSSubStatus(long idsID, SubStatus newStatus) {
		return idsDAO.updateIDSSubStatus(idsID, newStatus);
	}*/



	



	/*@Override
	@Transactional
	public boolean updateIDSSubStatus(long idsID, SubStatus newStatus) {
		return idsDAO.updateIDSSubStatus(idsID, newStatus);
	}*/

	@Override
	public SearchResult<ReferenceRecordDTO> fetchSourceRefRecords(final ReferenceRecordsFilter filter,
			final PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Fetch Reference Records: Missing mandatory parameters.");
		}

		SearchResult<ReferenceRecordDTO> resultSet = null;
		resultSet = refFlowRepository.fetchSourceRefData(filter, pageInfo);
		return resultSet;
	}

	@Override
	@Transactional
	public int insertSourceRefFillingInfo(Long[] selectedIndex, long idsID) {
		if (selectedIndex == null || selectedIndex.length == 0) {
			throw new IllegalArgumentException("Fetch Reference Records: Missing mandatory parameters.");
		}
		return refFlowRepository.insertSourceRefFillingRecords(selectedIndex, idsID);
	}
	/*- TODO
	 * 1.fetch all npl records
	 * 2.fetch all source reference records
	 * 3.combine all records
	 */
	@Override
	public SearchResult<ReferenceRecordDTO> fetchNPLRecordsForPreview(final ReferenceRecordsFilter filter,
			final PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Fetch Reference Records: Missing mandatory parameters.");
		}

		SearchResult<ReferenceRecordDTO> resultSetForSourceRef = refFlowRepository.fetchSourceFilingRefData(filter,
				pageInfo);
		SearchResult<ReferenceRecordDTO> resultSetForNPLRef = refFlowRepository.fetchNPLData(filter, pageInfo);
		List<ReferenceRecordDTO> listData = new ArrayList<ReferenceRecordDTO>();
		listData.addAll(resultSetForNPLRef.getItems());
		listData.addAll(resultSetForSourceRef.getItems());
		Long TotalRecords=resultSetForNPLRef.getRecordsTotal()+resultSetForSourceRef.getRecordsTotal();
		Long TotalFetchRecords=resultSetForNPLRef.getRecordsFiltered()+resultSetForSourceRef.getRecordsFiltered();
		SearchResult<ReferenceRecordDTO> resultSet = new SearchResult<ReferenceRecordDTO>(TotalRecords,
				TotalFetchRecords, listData);
		return resultSet;
	}

	@Override
	public Map<ReferenceType, Long> countNewReferenceRecords(ReferenceRecordsFilter refFilter) {
		return refFlowRepository.countNewReferenceRecords(refFilter);
	}


}
