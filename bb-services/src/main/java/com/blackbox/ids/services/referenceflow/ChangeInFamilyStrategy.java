package com.blackbox.ids.services.referenceflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.ProsecutionStatus;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.SourceReference;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.blackbox.ids.core.repository.referenceflow.IDSReferenceFlowRepository;

public class ChangeInFamilyStrategy implements ReferenceRuleStrategy {

	private IDSReferenceFlowRepository<IDSReferenceFlow> idsReferenceFlowRepository;

	@Override
	public void executeRule(ApplicationBase applicationBase, ReferenceChangeDesciption referenceChangeDesciption) {
		switch (referenceChangeDesciption) {

		case NEW_APPLICATION_ADDED_TO_FAMILY_IN_MDM:
			this.handleNewApplicationAdded(applicationBase);
			break;
		case APPLICATION_FAMILTY_ID_CHANGED_IN_MDM_TO_A_DIFF_FAMILY:
			this.handleApplicationFamilyIdChange(applicationBase);
			break;
		default:
			break;
		}
	}

	/**
	 * Manage ref flows creation when new application is added in the family
	 * 
	 * @param applicationBase
	 */
	private void handleNewApplicationAdded(ApplicationBase applicationBase) {

		switch (applicationBase.getJurisdiction().getCode()) {
		case "US":
			this.createReferennceFlowForUSFamilyMember(applicationBase);
			break;
		default:
			this.createReferenceFlowForForeignFamilyMember(applicationBase);
			break;
		}
	}

	/**
	 * Creates reference flows when jurisdiction of new application is US
	 * 
	 * @param applicationBase
	 */
	private void createReferennceFlowForUSFamilyMember(ApplicationBase applicationBase) {

		// fetch all reference flows for family
		List<IDSReferenceFlow> refFlowList = idsReferenceFlowRepository
				.findBySourceFamilyIdAndReferenceFlowStatus(applicationBase.getFamilyId(), ReferenceFlowStatus.DROPPED);

		// grouping target apps with references
		Map<ReferenceBaseData, List<ApplicationBase>> map = createRefBaseAndAppliontionBaseMap(refFlowList);
		for (ReferenceBaseData refBase : map.keySet()) {
			boolean toBeCitedInParent = true;

			// to create a reference flow to be In CITED_IN_PARENT status, all the target apps for that reference flow
			// must follow certain set of conditions
			for (ApplicationBase targetAppBase : map.get(refBase)) {
				// business rule
				if (targetAppBase.getJurisdiction().getCode().equalsIgnoreCase("US") && !targetAppBase
						.getOrganizationDetails().getProsecutionStatus().equals(ProsecutionStatus.GRANTED)) {
					if (!checkConditionForCitedInParent(refBase, targetAppBase)) {
						toBeCitedInParent = false;
						break;
					}
				} else {
					toBeCitedInParent = false;
					break;
				}
			}
			// create new reference flow
			IDSReferenceFlow newRefFlow = new IDSReferenceFlow();
			newRefFlow.setCorrespondence(refBase.getCorrespondence());
			newRefFlow.setDoNotFile(false);
			newRefFlow.setReferenceBaseData(refBase);
			newRefFlow.setSourceApplication(refBase.getApplication());
			newRefFlow.setTargetApplication(applicationBase);
			newRefFlow.setReferenceFlowStatus(ReferenceFlowStatus.UNCITED);
			if (toBeCitedInParent) {
				newRefFlow.setReferenceFlowSubStatus(ReferenceFlowSubStatus.CITED_IN_PARENT);
				newRefFlow.setOriginalReferenceFlowSubStatus(ReferenceFlowSubStatus.CITED_IN_PARENT);
			} else {
				newRefFlow.setReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
				newRefFlow.setOriginalReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
			}
		}
	}

	/**
	 * checks if the reference flow full fill criteria for cited in parent status
	 * 
	 * @param referencneBase
	 * @param applicationBase
	 * @return boolean
	 */
	private boolean checkConditionForCitedInParent(ReferenceBaseData referencneBase, ApplicationBase applicationBase) {
		return idsReferenceFlowRepository.checkForCitedInParentEligibility(referencneBase, applicationBase);
	}

	/**
	 * returns a map of reference base and corresponding target app base list
	 * 
	 * @param refFlowList
	 * @return
	 */
	private Map<ReferenceBaseData, List<ApplicationBase>> createRefBaseAndAppliontionBaseMap(
			List<IDSReferenceFlow> refFlowList) {

		Map<ReferenceBaseData, List<ApplicationBase>> map = new HashMap<ReferenceBaseData, List<ApplicationBase>>();
		for (IDSReferenceFlow refFlow : refFlowList) {
			if (map.containsKey(refFlow.getReferenceBaseData())) {
				map.get(refFlow.getReferenceBaseData()).add(refFlow.getTargetApplication());
			} else {
				List<ApplicationBase> appBaseList = new ArrayList<>();
				appBaseList.add(refFlow.getTargetApplication());
				map.put(refFlow.getReferenceBaseData(), appBaseList);
			}
		}
		return map;
	}

	/**
	 * Creates reference flows when jurisdiction of new application is Non-US
	 * 
	 * @param applicationBase
	 */
	private void createReferenceFlowForForeignFamilyMember(ApplicationBase applicationBase) {
		List<IDSReferenceFlow> refFlowList = idsReferenceFlowRepository
				.findBySourceFamilyIdAndReferenceFlowStatus(applicationBase.getFamilyId(), ReferenceFlowStatus.DROPPED);

		for (IDSReferenceFlow refFlow : refFlowList) {
			IDSReferenceFlow newRefFlow = new IDSReferenceFlow();
			newRefFlow.setCorrespondence(refFlow.getCorrespondence());
			newRefFlow.setDoNotFile(false);
			newRefFlow.setReferenceBaseData(refFlow.getReferenceBaseData());
			newRefFlow.setReferenceFlowStatus(ReferenceFlowStatus.UNCITED);
			newRefFlow.setReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
			newRefFlow.setOriginalReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
			newRefFlow.setSourceApplication(refFlow.getSourceApplication());

			// flow to new application added in the family
			newRefFlow.setTargetApplication(applicationBase);

			idsReferenceFlowRepository.save(newRefFlow);
		}
	}

	/**
	 * Manage ref flows creation when application family is changed
	 * 
	 * @param applicationBase
	 */
	private void handleApplicationFamilyIdChange(ApplicationBase applicationBase) {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeRule(ReferenceBaseData referenceBaseData, SourceReference sourceReference,
			ReferenceChangeDesciption referenceChangeDesciption) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void executeRule(ReferenceBaseData referenceBaseData, ReferenceChangeDesciption referenceChangeDesciption) {
		throw new UnsupportedOperationException();
	}

}