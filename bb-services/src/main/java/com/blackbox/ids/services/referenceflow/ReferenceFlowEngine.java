package com.blackbox.ids.services.referenceflow;

import org.springframework.stereotype.Service;

import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.SourceReference;

@Service
public class ReferenceFlowEngine {

	private ReferenceRuleStrategy referenceRuleStrategy;
	
	public ReferenceFlowEngine() {
		super();
	}
	

	public ReferenceFlowEngine(ReferenceRuleStrategy referenceRuleStrategy) {
		super();
		this.referenceRuleStrategy = referenceRuleStrategy;
	}

	public void executeStrategy(ReferenceBaseData referenceBaseData, SourceReference sourceReference,
			FlowChangeType flowChangeType, ReferenceChangeDesciption referenceChangeDesciption) {
		if (flowChangeType.equals(FlowChangeType.REF_BASE_TABLE_CHANGE)
				&& referenceRuleStrategy instanceof RefBaseTableChangeStrategy) {
			referenceRuleStrategy.executeRule(referenceBaseData, referenceChangeDesciption);
		} else if (flowChangeType.equals(FlowChangeType.CHANGE_IN_FAMILY)
				&& referenceRuleStrategy instanceof ChangeInFamilyStrategy) {
			referenceRuleStrategy.executeRule(referenceBaseData, referenceChangeDesciption);
		} else if (flowChangeType.equals(FlowChangeType.SOURCE_REF_TABLE_CHANGE)
				&& referenceRuleStrategy instanceof SourceRefTableChangeStrategy) {
			referenceRuleStrategy.executeRule(referenceBaseData, sourceReference, referenceChangeDesciption);
		}
	}
}
