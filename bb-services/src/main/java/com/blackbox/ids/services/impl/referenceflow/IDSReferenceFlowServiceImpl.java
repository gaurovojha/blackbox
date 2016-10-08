package com.blackbox.ids.services.impl.referenceflow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlow1449Pending;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.blackbox.ids.core.repository.impl.referenceflow.ReferenceFlow1449PendingRepository;
import com.blackbox.ids.core.repository.referenceflow.IDSReferenceFlowRepository;
import com.blackbox.ids.services.referenceflow.IDSReferenceFlowService;

/**
 * Handles reference flow related requests
 * 
 * @author nagarro
 *
 */
@Service
public class IDSReferenceFlowServiceImpl implements IDSReferenceFlowService {

	@Autowired
	private IDSReferenceFlowRepository<IDSReferenceFlow> iDSReferenceFlowRepository;

	@Autowired
	private ReferenceFlow1449PendingRepository<ReferenceFlow1449Pending> referenceFlow1449PendingRepository;

	@Override
	public boolean duplicateCheck(final ReferenceBaseData referenceBaseData, final Long applicationId) {

		boolean duplicate = iDSReferenceFlowRepository.duplicateCheck(referenceBaseData, applicationId);

		// if there is any record with CITED-PENDING_1499 then insert it in ReferenceFlow1449Pending table
		// business rule
		if (duplicate) {
			List<IDSReferenceFlow> refFlowList = iDSReferenceFlowRepository.findDuplicateRefFlow(referenceBaseData,
					applicationId);
			for (IDSReferenceFlow flow : refFlowList) {
				if (flow.getReferenceFlowSubStatus().equals(ReferenceFlowSubStatus.PENDING_1449)) {
					ReferenceFlow1449Pending pending1449Flow = new ReferenceFlow1449Pending();
					pending1449Flow.setIdsReferenceFlow(flow);
					pending1449Flow.setReferenceBaseData(referenceBaseData);
					referenceFlow1449PendingRepository.save(pending1449Flow);
				}
			}
		}
		return duplicate;
	}

	@Override
	public IDSReferenceFlow findDuplicateRefFlow(final ReferenceBaseData referenceBaseData, final Long applicationId) {
		return iDSReferenceFlowRepository.findDuplicateRefFlow(referenceBaseData, applicationId).get(0);
	}

	@Override
	public boolean isEligibleForCitedInParentStatus(ReferenceBaseData referenceBaseData,
			ApplicationBase targetAppBase) {
		return iDSReferenceFlowRepository.checkForCitedInParentEligibility(referenceBaseData, targetAppBase);
	}

	@Override
	public void dropReferenceFlow(final Long refFlowId) {
		iDSReferenceFlowRepository.dropReferenceFlow(refFlowId);
	}

	@Override
	public void createReferenceFlowAfterRejectAction(final Long refFlowId) {
		IDSReferenceFlow refFlow = new IDSReferenceFlow(refFlowId);
		ReferenceFlow1449Pending pending1449Flow = referenceFlow1449PendingRepository.findByIdsReferenceFlow(refFlow);

		if (pending1449Flow != null) {
			IDSReferenceFlow newFlow = new IDSReferenceFlow();
			newFlow.setCorrespondence(pending1449Flow.getReferenceBaseData().getCorrespondence());
			newFlow.setDoNotFile(false);
			newFlow.setReferenceBaseData(pending1449Flow.getReferenceBaseData());
			newFlow.setReferenceFlowStatus(ReferenceFlowStatus.UNCITED);
			newFlow.setReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
			newFlow.setOriginalReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
			newFlow.setSourceApplication(pending1449Flow.getIdsReferenceFlow().getSourceApplication());
			newFlow.setTargetApplication(pending1449Flow.getIdsReferenceFlow().getTargetApplication());
			iDSReferenceFlowRepository.save(newFlow);
		}
	}
}