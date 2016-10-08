package com.blackbox.ids.core.model.reference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * The Class ReferenceBaseNPLData.
 */
@Entity
@Table(name = "BB_REFERENCE_BASE_NPL_DATA")
@PrimaryKeyJoinColumn(name = "BB_REFERENCE_BASE_DATA_ID")
public class ReferenceBaseNPLData extends ReferenceBaseData {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3515753192013058715L;

	/** The author. */
	@Column(name = "AUTHOR")
	private String author;

	/** The title. */
	@Column(name = "TITLE", nullable = false)
	private String title;

	/** The publication detail. */
	@Column(name = "PUBLICATION_DETAIL")
	private String publicationDetail;

	/** The publication month. */
	@Column(name = "PUBLICATION_MONTH")
	private Integer publicationMonth;

	/** The publication day. */
	@Column(name = "PUBLICATION_DAY")
	private Integer publicationDay;

	/** The publication year. */
	@Column(name = "PUBLICATION_YEAR")
	private Integer publicationYear;

	/** The relevant pages. */
	@Column(name = "RELEVANT_PAGES")
	private String relevantPages;

	/** The volume number. */
	@Column(name = "VOLUME_NUMBER")
	private String volumeNumber;

	/** The url. */
	@Column(name = "URL")
	private String URL;

	/** The publication city. */
	@Column(name = "PUBLICATION_CITY")
	private String publicationCity;

	/** The string data. */
	@Column(name = "STRING_DATA", nullable = false)
	private String stringData;

	/** The un published. */
	@Column(name = "US_UNPUBLISHED", nullable = false)
	private Boolean unPublished;

	/** The application serial number. */
	@Column(name = "APPLICATION_SERIAL_NUMBER")
	private String applicationSerialNumber;

	/**
	 * Gets the author.
	 *
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the author.
	 *
	 * @param author
	 *            the new author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the publication detail.
	 *
	 * @return the publication detail
	 */
	public String getPublicationDetail() {
		return publicationDetail;
	}

	/**
	 * Sets the publication detail.
	 *
	 * @param publicationDetail
	 *            the new publication detail
	 */
	public void setPublicationDetail(String publicationDetail) {
		this.publicationDetail = publicationDetail;
	}

	/**
	 * Gets the publication month.
	 *
	 * @return the publication month
	 */
	public Integer getPublicationMonth() {
		return publicationMonth;
	}

	/**
	 * Sets the publication month.
	 *
	 * @param publicationMonth
	 *            the new publication month
	 */
	public void setPublicationMonth(Integer publicationMonth) {
		this.publicationMonth = publicationMonth;
	}

	/**
	 * Gets the publication day.
	 *
	 * @return the publication day
	 */
	public Integer getPublicationDay() {
		return publicationDay;
	}

	/**
	 * Sets the publication day.
	 *
	 * @param publicationDay
	 *            the new publication day
	 */
	public void setPublicationDay(Integer publicationDay) {
		this.publicationDay = publicationDay;
	}

	/**
	 * Gets the publication year.
	 *
	 * @return the publication year
	 */
	public Integer getPublicationYear() {
		return publicationYear;
	}

	/**
	 * Sets the publication year.
	 *
	 * @param publicationYear
	 *            the new publication year
	 */
	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	/**
	 * Gets the relevant pages.
	 *
	 * @return the relevant pages
	 */
	public String getRelevantPages() {
		return relevantPages;
	}

	/**
	 * Sets the relevant pages.
	 *
	 * @param relevantPages
	 *            the new relevant pages
	 */
	public void setRelevantPages(String relevantPages) {
		this.relevantPages = relevantPages;
	}

	/**
	 * Gets the volume number.
	 *
	 * @return the volume number
	 */
	public String getVolumeNumber() {
		return volumeNumber;
	}

	/**
	 * Sets the volume number.
	 *
	 * @param volumeNumber
	 *            the new volume number
	 */
	public void setVolumeNumber(String volumeNumber) {
		this.volumeNumber = volumeNumber;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * Sets the url.
	 *
	 * @param uRL
	 *            the new url
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * Gets the publication city.
	 *
	 * @return the publication city
	 */
	public String getPublicationCity() {
		return publicationCity;
	}

	/**
	 * Sets the publication city.
	 *
	 * @param publicationCity
	 *            the new publication city
	 */
	public void setPublicationCity(String publicationCity) {
		this.publicationCity = publicationCity;
	}

	/**
	 * Gets the string data.
	 *
	 * @return the string data
	 */
	public String getStringData() {
		return stringData;
	}

	/**
	 * Sets the string data.
	 *
	 * @param stringData
	 *            the new string data
	 */
	public void setStringData(String stringData) {
		this.stringData = stringData;
	}

	/**
	 * Checks if is un published.
	 *
	 * @return true, if is un published
	 */
	public Boolean isUnPublished() {
		return unPublished;
	}

	/**
	 * Sets the un published.
	 *
	 * @param unPublished
	 *            the new un published
	 */
	public void setUnPublished(Boolean unPublished) {
		this.unPublished = unPublished;
	}

	/**
	 * Gets the application serial number.
	 *
	 * @return the application serial number
	 */
	public String getApplicationSerialNumber() {
		return applicationSerialNumber;
	}

	/**
	 * Sets the application serial number.
	 *
	 * @param applicationSerialNumber
	 *            the new application serial number
	 */
	public void setApplicationSerialNumber(String applicationSerialNumber) {
		this.applicationSerialNumber = applicationSerialNumber;
	}

	/**
	 * Instantiates a new reference base npl data.
	 */
	public ReferenceBaseNPLData() {
		super();
	}
}
