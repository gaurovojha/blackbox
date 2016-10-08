package com.blackbox.ids.core.model.reference;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The Class ReferenceStagingPUSData.
 */
@Embeddable
public class ReferenceStagingPUSData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3630541298585332129L;

	/** The converted publication number. */
	@Column(name = "PUBLICATION_NUMBER_CONVERTED")
	private String convertedPublicationNumber;

	/**
	 * Gets the converted publication number.
	 *
	 * @return the converted publication number
	 */
	public String getConvertedPublicationNumber() {
		return convertedPublicationNumber;
	}

	/**
	 * Sets the converted publication number.
	 *
	 * @param convertedPublicationNumber
	 *            the new converted publication number
	 */
	public void setConvertedPublicationNumber(String convertedPublicationNumber) {
		this.convertedPublicationNumber = convertedPublicationNumber;
	}
}
