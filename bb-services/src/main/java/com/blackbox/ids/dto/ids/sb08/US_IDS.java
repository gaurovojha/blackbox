/**
 *
 */
package com.blackbox.ids.dto.ids.sb08;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.blackbox.ids.dto.ids.sb08.certification.CertificationInfo;
import com.blackbox.ids.dto.ids.sb08.filingInfo.IDSFilingInfo;
import com.blackbox.ids.dto.ids.sb08.reference.Foreign;
import com.blackbox.ids.dto.ids.sb08.reference.NPL;
import com.blackbox.ids.dto.ids.sb08.reference.Patent;
import com.blackbox.ids.dto.ids.sb08.reference.Publication;

/**
 * Maps this class object to IDS SB-08 form data.
 *
 * @author ajay2258
 */
/*-@XmlType(propOrder = { "filingInfo", "certificationInfo", "patents", "publications", "foreignReferences",
		"nplReferences", "version", "clientVersion", "numPages" })*/
@XmlRootElement(name = "us-ids")
@XmlAccessorType(XmlAccessType.FIELD)
public class US_IDS implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -877659701558918492L;

	public US_IDS() {
		this.filingInfo = new IDSFilingInfo();
		this.certificationInfo = new CertificationInfo();
		this.patents = Arrays.asList(new Patent());
		this.publications = new ArrayList<>();
		this.foreignReferences = new ArrayList<>();
		this.nplReferences = new ArrayList<>();
		this.producedOn = "20160216";
		this.lang = "EN";
		this.status = "ACTIVE";
	}

	@XmlElement(name = "us-filing-info")
	private IDSFilingInfo filingInfo;

	@XmlElement(name = "us-ids-certification")
	private CertificationInfo certificationInfo;

	@XmlElement(name = "us-patent-cite")
	private List<Patent> patents;

	@XmlElement(name = "us-pub-appl-cite")
	private List<Publication> publications;

	@XmlElement(name = "us-foreign-document-cite")
	private List<Foreign> foreignReferences;

	@XmlElement(name = "us-nplcit")
	private List<NPL> nplReferences;

	/*- ------------------- XML Attributes -- */
	@XmlAttribute(name = "date-produced")
	private String producedOn;

	@XmlAttribute(name = "dtd-version")
	private final String dtdVersion = "v20_EFSWeb";

	@XmlAttribute(name = "")
	private String file;

	@XmlAttribute(name = "lang")
	private String lang;

	@XmlAttribute(name = "status")
	private String status;

	/*- ------------------- XML Elements -- */
	@XmlElement(name = "version-info")
	private float version = 2.1f;

	@XmlElement(name = "clientversion")
	private float clientVersion = 11.014f;

	@XmlElement(name = "numofpages")
	private int numPages = 5;

	/*- --------------------------------- getter-setters -- */
	public IDSFilingInfo getFilingInfo() {
		return filingInfo;
	}

	public void setFilingInfo(IDSFilingInfo filingInfo) {
		this.filingInfo = filingInfo;
	}

	public CertificationInfo getCertificationInfo() {
		return certificationInfo;
	}

	public void setCertificationInfo(CertificationInfo certificationInfo) {
		this.certificationInfo = certificationInfo;
	}

	public List<Patent> getPatents() {
		return patents;
	}

	public void setPatents(List<Patent> patents) {
		this.patents = patents;
	}

	public List<Publication> getPublications() {
		return publications;
	}

	public void setPublications(List<Publication> publications) {
		this.publications = publications;
	}

	public List<Foreign> getForeignReferences() {
		return foreignReferences;
	}

	public void setForeignReferences(List<Foreign> foreignReferences) {
		this.foreignReferences = foreignReferences;
	}

	public List<NPL> getNplReferences() {
		return nplReferences;
	}

	public void setNplReferences(List<NPL> nplReferences) {
		this.nplReferences = nplReferences;
	}

	public String getProducedOn() {
		return producedOn;
	}

	public void setProducedOn(String producedOn) {
		this.producedOn = producedOn;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDtdVersion() {
		return dtdVersion;
	}

	public float getVersion() {
		return version;
	}

	public float getClientVersion() {
		return clientVersion;
	}

	public int getNumPages() {
		return numPages;
	}

}
