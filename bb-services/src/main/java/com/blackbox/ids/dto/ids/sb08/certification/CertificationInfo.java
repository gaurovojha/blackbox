/**
 *
 */
package com.blackbox.ids.dto.ids.sb08.certification;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.blackbox.ids.dto.ids.sb08.common.Signature;

/**
 * @author ajay2258
 *
 */
@XmlRootElement(name = "us-ids-certification")
@XmlAccessorType(XmlAccessType.FIELD)
public class CertificationInfo implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 8453011955150950824L;

	@XmlRootElement(name = "us-foreign-pat-office-citation")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class OfficeCitation implements Serializable {

		/** The serial version UID. */
		private static final long serialVersionUID = -6860798358148233189L;

		@XmlAttribute(name = "text", required = false)
		private String text = "0";

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}

	@XmlRootElement(name = "us-new-findings")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class NewFinding implements Serializable {

		/** The serial version UID. */
		private static final long serialVersionUID = -7584603820442615063L;

		@XmlAttribute(name = "text", required = false)
		private String text = "0";

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}

	@XmlRootElement(name = "us-certification-statement-attached")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class CertificationAttachment implements Serializable {

		/** The serial version UID. */
		private static final long serialVersionUID = 6412240699045643985L;

		@XmlAttribute(name = "boilerplate-text")
		private String text = "0";

		@XmlAttribute(name = "file", required = false)
		private String file;

		@XmlAttribute(name = "type", required = false)
		private String type;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getFile() {
			return file;
		}

		public void setFile(String file) {
			this.file = file;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

	@XmlRootElement(name = "us-fee-submitted")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class FeeSubmitted implements Serializable {

		/** The serial version UID. */
		private static final long serialVersionUID = -5893244125929288870L;

		@XmlAttribute(name = "boilerplate-text")
		private String text;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}

	@XmlAttribute(name = "text")
	private String text = "1";

	@XmlElement
	private OfficeCitation officeCitation;

	@XmlElement
	private NewFinding newFinding;

	@XmlElement
	private CertificationAttachment certificationAttachment;

	@XmlElement
	private FeeSubmitted feeSubmitted;

	@XmlElement
	private Applicant applicant;

	@XmlElement
	private Signature signature;

	/*- ------------------------------ getter-setters -- */
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public OfficeCitation getOfficeCitation() {
		return officeCitation;
	}

	public void setOfficeCitation(OfficeCitation officeCitation) {
		this.officeCitation = officeCitation;
	}

	public NewFinding getNewFinding() {
		return newFinding;
	}

	public void setNewFinding(NewFinding newFinding) {
		this.newFinding = newFinding;
	}

	public CertificationAttachment getCertificationAttachment() {
		return certificationAttachment;
	}

	public void setCertificationAttachment(CertificationAttachment certificationAttachment) {
		this.certificationAttachment = certificationAttachment;
	}

	public FeeSubmitted getFeeSubmitted() {
		return feeSubmitted;
	}

	public void setFeeSubmitted(FeeSubmitted feeSubmitted) {
		this.feeSubmitted = feeSubmitted;
	}

	public Applicant getApplicant() {
		return applicant;
	}

	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

}
