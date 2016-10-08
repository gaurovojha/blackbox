/**
 *
 */
package com.blackbox.ids.core.model.mstr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The entity class <tt>AttorneyDocket</tt> represents an attroney docket number, and its' segments formats.
 *
 * @author ajay2258
 */
@Entity
@Table(name = "BB_MSTR_ATTORNEY_DOCKET_FORMAT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AttorneyDocketFormat implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -3040013405748871740L;

	@Id
	@Column(name = "CLIENT_ID")
	private Long clientId;

	@Column(name = "TOTAL_SEGMENTS", nullable = false)
	private int numSegments;

	@Column(name = "SEPARATOR", nullable = false, length = 10)
	private String separator;

	@Column(name = "SEGMENT_SIZE", nullable = false)
	private int segmentSize;

	@Column(name = "SEGMENT_TO_MATCH", nullable = true)
	private int segmentToMatch;

	/*- ---------------------------- getter-setters -- */
	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public int getNumSegments() {
		return numSegments;
	}

	public void setNumSegments(int numSegments) {
		this.numSegments = numSegments;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public int getSegmentSize() {
		return segmentSize;
	}

	public void setSegmentSize(int segmentSize) {
		this.segmentSize = segmentSize;
	}

	public int getSegmentToMatch() {
		return segmentToMatch;
	}

	public void setSegmentToMatch(int segmentToMatch) {
		this.segmentToMatch = segmentToMatch;
	}

}
