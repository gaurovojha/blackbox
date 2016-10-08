/**
 *
 */
package com.blackbox.ids.core.model.IDS;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Maintains the IDS filing information. The entity class serves for both types of IDSs, filled from or outside the
 * system.
 *
 * @author ajay2258
 */
@Entity
@Table(name = "BB_IDS_FILING_INFO")
public class IDSFilingInfo implements Serializable {

	public enum IDS_Source {
		INTERNAL, EXTERNAL
	}

	/** The serial version UID. */
	private static final long serialVersionUID = 278167441871046029L;

	/** Database primary key. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_INTERNAL_FILING_INFO_ID", updatable = false)
	private Long id;

	/** IDS instance whose filing info is this object. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_IDS", referencedColumnName = "BB_IDS_ID", nullable = false)
	private IDS ids;

	/**
	 * System generated sequence. This can be either the internal or external final IDS Id. Exact meaning is derived
	 * with {@link #getSource() source} attribute.
	 * <p/>
	 * <ul>
	 * <li><b>INTERNAL</b>: Created when IDS File is generated. IDS File generation can result in multiple IDS files.
	 * For each IDS File, one Final IDS id is generated. There can be multiple Final IDs for one idsBuildId.</li>
	 * <li><b>EXTERNAL</b>: Created for IDS, that aren't filled from the system. Created when an IDS document is
	 * received in correspondence store.</li>
	 * </ul>
	 */
	@Column(name = "IDS_FINAL_ID")
	private String idsFinalId;

	/** Date of successful upload for IDS file. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILING_DATE")
	private Calendar filingDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "IDS_SOURCE")
	private IDS_Source source;
	
	@Column(name = "DOCUMENT_DESCRIPTION")
	private String documentDesc;

	/*- ------------------------------------ getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IDS getIds() {
		return ids;
	}

	public void setIds(IDS ids) {
		this.ids = ids;
	}

	public String getIdsFinalId() {
		return idsFinalId;
	}

	public void setIdsFinalId(String idsFinalId) {
		this.idsFinalId = idsFinalId;
	}

	public Calendar getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(Calendar filingDate) {
		this.filingDate = filingDate;
	}

	public IDS_Source getSource() {
		return source;
	}

	public void setSource(IDS_Source source) {
		this.source = source;
	}

	public String getDocumentDesc() {
		return documentDesc;
	}

	public void setDocumentDesc(String documentDesc) {
		this.documentDesc = documentDesc;
	}
}
