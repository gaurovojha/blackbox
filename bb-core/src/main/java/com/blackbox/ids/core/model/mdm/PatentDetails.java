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
 * The embeddable class {@code PatentDetails} bundles all patent office details for an application.
 *
 * @author ajay2258
 */
@Embeddable
public class PatentDetails {

	@Column(name = "FIRST_NAME_INVENTOR")
	private String firstNameInventor;

	@Column(name = "EXAMINER")
	private String examiner;

	@Column(name = "ART_UNIT")
	private String artUnit;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "PATENT_NUMBER_RAW")
	private String patentNumberRaw;

	/** Converted patent number. */
	@Column(name = "PATENT_NUMBER")
	private String patentNumber;

	@Column(name = "ISSUED_ON")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar issuedOn;

	public PatentDetails() {}

	public PatentDetails(String firstNameInventor, String examiner,
			String artUnit, String title, String patentNumberRaw,
			Calendar issuedOn) {
		this.firstNameInventor = firstNameInventor;
		this.examiner = examiner;
		this.artUnit = artUnit;
		this.title = title;
		this.patentNumberRaw = patentNumberRaw;
		this.issuedOn = issuedOn;
	}



	/*- ---------------------------- getter-setters -- */
	public String getFirstNameInventor() {
		return firstNameInventor;
	}

	public void setFirstNameInventor(String firstNameInventor) {
		this.firstNameInventor = firstNameInventor;
	}

	public String getExaminer() {
		return examiner;
	}

	public void setExaminer(String examiner) {
		this.examiner = examiner;
	}

	public String getArtUnit() {
		return artUnit;
	}

	public void setArtUnit(String artUnit) {
		this.artUnit = artUnit;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPatentNumberRaw() {
		return patentNumberRaw;
	}

	public void setPatentNumberRaw(String patentNumberRaw) {
		this.patentNumberRaw = patentNumberRaw;
	}

	public String getPatentNumber() {
		return patentNumber;
	}

	public void setPatentNumber(String patentNumber) {
		this.patentNumber = patentNumber;
	}

	public Calendar getIssuedOn() {
		return issuedOn;
	}

	public void setIssuedOn(Calendar issuedOn) {
		this.issuedOn = issuedOn;
	}

}
