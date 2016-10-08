package com.blackbox.ids.services.referenceflow;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.SourceReference;

public interface ReferenceRuleStrategy {

	/**
	 * Reference base change strategies are implemented to manage reference flow entity
	 * 
	 * @param referenceBaseData
	 * @param referenceChangeDesciption
	 * @throws ApplicationException
	 */
	public void executeRule(ReferenceBaseData referenceBaseData, ReferenceChangeDesciption referenceChangeDesciption);

	/**
	 * Source reference change strategies are implemented to manage source reference flow entity
	 * 
	 * @param referenceBaseData
	 * @param referenceChangeDesciption
	 * @throws ApplicationException
	 */
	public void executeRule(ReferenceBaseData referenceBaseData, SourceReference sourceReference,
			ReferenceChangeDesciption referenceChangeDesciption);

	/**
	 * Application base change strategies are implemented to manage source reference flow entity
	 * 
	 * @param applicationBase
	 * @param referenceChangeDesciption
	 */
	public void executeRule(ApplicationBase applicationBase, ReferenceChangeDesciption referenceChangeDesciption);

}