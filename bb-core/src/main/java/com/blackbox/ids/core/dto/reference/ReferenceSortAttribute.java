package com.blackbox.ids.core.dto.reference;

/**
 * The Enum ReferenceSortAttribute.
 */
public enum ReferenceSortAttribute {

	/** The jurisdiction. */
	jurisdiction("jurisdiction"),

	/** The application_no. */
	application_no("application"),

	/** The mailing_date. */
	mailing_date("mailingdate"),

	/** The document_description. */
	document_description("documentdescription");

	/** The attr. */
	public final String attr;

	/**
	 * Instantiates a new reference sort attribute.
	 */
	private ReferenceSortAttribute() {
		this.attr = this.name();
	}

	/**
	 * Instantiates a new reference sort attribute.
	 *
	 * @param attr
	 *            the attr
	 */
	private ReferenceSortAttribute(final String attr) {
		this.attr = attr;
	}

	/**
	 * From string.
	 *
	 * @param attr
	 *            the attr
	 * @return the reference sort attribute
	 */
	public static ReferenceSortAttribute fromString(final String attr) {
		ReferenceSortAttribute referenceSortAttribute = null;
		for (final ReferenceSortAttribute e : ReferenceSortAttribute.values()) {
			if (e.attr.equalsIgnoreCase(attr)) {
				referenceSortAttribute = e;
				break;
			}
		}
		return referenceSortAttribute;
	}
}
