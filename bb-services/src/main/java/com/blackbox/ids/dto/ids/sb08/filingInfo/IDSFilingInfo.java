/**
 *
 */
package com.blackbox.ids.dto.ids.sb08.filingInfo;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.blackbox.ids.dto.ids.sb08.common.Name;

/**
 * @author ajay2258
 *
 */
@XmlRootElement(name = "us-filing-info")
@XmlAccessorType(XmlAccessType.FIELD)
public class IDSFilingInfo implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 3081068246837740181L;

	public IDSFilingInfo() {
		this.identificationInfo = new ApplicationIdentificationInfo();
		identificationInfo.setApplicationNo("14210289");
		identificationInfo.setPublishedOn(new Date());

		this.inventor = new Inventor();
		inventor.setName(new Name());
		inventor.getName().setName("Ajay Singh Raman");

		this.docketNo = "00630.0395-US-U1";
		this.artUnit = "2498";
	}

	@XmlElement(name = "us-application-identification-info")
	private ApplicationIdentificationInfo identificationInfo;

	@XmlElement(name = "us-first-named-inventor")
	private Inventor inventor;

	@XmlElement(name = "file-reference-id")
	private String docketNo;

	@XmlElement(name = "us-group-art-unit")
	private String artUnit;

	@XmlElement(name = "primary-examiner")
	private Examiner examiner;

	/*- --------------------------------- getter-setters -- */
	public ApplicationIdentificationInfo getIdentificationInfo() {
		return identificationInfo;
	}

	public void setIdentificationInfo(ApplicationIdentificationInfo identificationInfo) {
		this.identificationInfo = identificationInfo;
	}

	public Inventor getInventor() {
		return inventor;
	}

	public void setInventor(Inventor inventor) {
		this.inventor = inventor;
	}

	public String getDocketNo() {
		return docketNo;
	}

	public void setDocketNo(String docketNo) {
		this.docketNo = docketNo;
	}

	public String getArtUnit() {
		return artUnit;
	}

	public void setArtUnit(String artUnit) {
		this.artUnit = artUnit;
	}

	public Examiner getExaminer() {
		return examiner;
	}

	public void setExaminer(Examiner examiner) {
		this.examiner = examiner;
	}

}
