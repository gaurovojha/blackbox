package com.blackbox.ids.core.model.IDS;

/**
 * Each trigger has a set of conditions which should be met while populating data in "Urgent" tab of IDS.
 * We are expecting a base query with following table join -  IDSReferenceFlow (alias idsRefFlow), ApplicationBase (alias app),
 * IDSInternalFiling (alias in), IDSExternalFiling (alias ex).
 * @author ruchiwadhwa
 *
 */
public enum TriggerType {
	
	TRIGGER1("Trigger 1"),
	
	TRIGGER2("Trigger 2"),
	
	TRIGGER3("Trigger 3"),
	
	TRIGGER4("Trigger 4"),
	
	TRIGGER5("Trigger 5"),
	
	TRIGGER6("Trigger 6");
		
	private String value;
	
	private TriggerType(String value){
		this.value = value;
	}
}
