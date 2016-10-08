package com.blackbox.ids.core.model.reference;

import com.blackbox.ids.core.dto.mdm.family.FamilySearchFilter.FamilyLinkage;

/**
 * The Enum ReferenceType.
 */
public enum ReferenceType {
	/** Reference type - US Patents. */
	PUS("PUS"),

	/** Reference type - US Publications. */
	US_PUBLICATION("US_PUB"),

	/** The fp. */
	FP("FP"),

	/** The npl. */
	NPL("NPL");

	/** The name. */
	private String name;

	
	public static ReferenceType fromString(final String val) {
		ReferenceType refType = null;
		for (final ReferenceType e : ReferenceType.values()) {
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
	private ReferenceType(String name) {
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