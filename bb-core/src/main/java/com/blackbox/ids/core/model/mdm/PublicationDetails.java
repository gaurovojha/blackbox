/**
 *
 */
package com.blackbox.ids.core.model.mdm;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The embeddable class {@code PublicationDetails} holds the publication details for a patent application.
 *
 * @author ajay2258
 */
@Embeddable
public class PublicationDetails {

	@Column(name = "PUBLICATION_NUMBER_RAW")
	private String publicationNumberRaw;

	@Column(name = "PUBLICATION_NUMBER")
	private String publicationNumber;

	@Column(name = "PUBLISHED_ON")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar publishedOn;

	public PublicationDetails(){}
	
	public PublicationDetails(String publicationNumberRaw,
			String publicationNumber, Calendar publishedOn) {
		this.publicationNumberRaw = publicationNumberRaw;
		this.publicationNumber = publicationNumber;
		this.publishedOn = publishedOn;
	}

	/*- ---------------------------- getter-setters -- */
	public String getPublicationNumberRaw() {
		return publicationNumberRaw;
	}

	public void setPublicationNumberRaw(String publicationNumberRaw) {
		this.publicationNumberRaw = publicationNumberRaw;
	}

	public String getPublicationNumber() {
		return publicationNumber;
	}

	public void setPublicationNumber(String publicationNumber) {
		this.publicationNumber = publicationNumber;
	}

	public Calendar getPublishedOn() {
		return publishedOn;
	}

	public void setPublishedOn(Calendar publishedOn) {
		this.publishedOn = publishedOn;
	}

}
