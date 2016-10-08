/**
 * 
 */
package com.blackbox.ids.core.model.referenceflow;

/**
 * @author nagarro
 *
 */
public enum ReferenceFlowSubStatus {

	NULL("NULL"), PENDING_USPTO_FILING("PENDING_USPTO_FILING"), PENDING_1449("PENDING_1449"), CITED_IN_PARENT("CITED_IN_PARENT"), ACCEPTED("ACCEPTED"), REJECTED("REJECTED");
	
	/** The name. */
	private String name;

	
	public static ReferenceFlowSubStatus fromString(final String val) {
		ReferenceFlowSubStatus refType = null;
		for (final ReferenceFlowSubStatus e : ReferenceFlowSubStatus.values()) {
			if (e.name().equalsIgnoreCase(val)) {
				refType = e;
				break;
			}
		}
		return refType;
	}
	
	/**
	 * Instantiates a new reference type.
	 *
	 * @param name
	 *            the name
	 */
	private ReferenceFlowSubStatus(String name) {
		this.name = name;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
}
