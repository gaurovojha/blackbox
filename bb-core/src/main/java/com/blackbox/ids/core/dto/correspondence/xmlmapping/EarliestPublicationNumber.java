package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The Class Earliest publication number.
 */
@XStreamAlias("EarliestPublicationNumber")
public class EarliestPublicationNumber {

	/** The publication date. */
	@XStreamAlias("PublicationDate")
	private String publicationDate;

	/** The publication kind code. */
	@XStreamAlias("PublicationKindCode")
	private String publicationKindCode;

	/** The publication sequence number. */
	@XStreamAlias("PublicationSequenceNumber")
	private String publicationSequenceNumber;

	/** The earliest publication year. */
	@XStreamAlias("EarliestPublicationYear")
	private String earliestPublicationYear;

	/**
	 * Gets the earliest publication year.
	 *
	 * @return the earliest publication year
	 */
	public String getEarliestPublicationYear() {
		return earliestPublicationYear;
	}

	/**
	 * Sets the earliest publication year.
	 *
	 * @param earliestPublicationYear the new earliest publication year
	 */
	public void setEarliestPublicationYear(final String earliestPublicationYear) {
		this.earliestPublicationYear = earliestPublicationYear;
	}

}
