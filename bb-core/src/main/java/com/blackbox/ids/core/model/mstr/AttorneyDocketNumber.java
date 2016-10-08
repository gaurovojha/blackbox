/**
 *
 */
package com.blackbox.ids.core.model.mstr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author ajay2258
 */
@Entity
@Table(name = "BB_ATTRONEY_DOCKET_NUMBER")
public class AttorneyDocketNumber implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 6828092512901583679L;

	private static final String DOCKET_SEPERATOR = "\\";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_ATTORNEY_DOCKET_NUMBER_ID", nullable = false)
	private Long id;

	@Column(name = "SEGMENT_1")
	private String segment1;

	@Column(name = "SEGMENT_2")
	private String segment2;

	@Column(name = "SEGMENT_3")
	private String segment3;

	@Column(name = "SEGMENT_4")
	private String segment4;

	@Column(name = "SEGMENT_5")
	private String segment5;

	@Column(name = "SEGMENT_6")
	private String segment6;

	@Column(name = "SEGMENT_7")
	private String segment7;

	/** The complete concatenated attorney docket number. */
	@Column(name = "SEGMENT", nullable = false)
	private String segment;

	@Transient
	private String separator;

	/*- ---------------------------- Constructors -- */
	public AttorneyDocketNumber() {
		super();
	}

	public AttorneyDocketNumber(final String docketNo) {
		super();
		this.segment = docketNo;
	}

	/*- ----------------------------- JPA Callbacks -- */
	@PrePersist
	public void prePersist() {
		tokenizeSegments();
	}

	@PreUpdate
	public void preUpdate() {
		tokenizeSegments();
	}

	private void tokenizeSegments() {
		if (separator != null && segment != null) {
			String delimiter = DOCKET_SEPERATOR + separator;
			String[] segments = segment.split(delimiter);
			setValue(segments);
		}
	}

	private void setValue(String[] segments) {
		int numSegments = segments.length;

		if (numSegments - 1 >= 0) {
			segment1 = segments[0];
		}
		if (numSegments - 1 >= 1) {
			segment2 = segments[1];
		}
		if (numSegments - 1 >= 2) {
			segment3  = segments[2];
		}
		if (numSegments - 1 >= 3) {
			segment4 = segments[3];
		}
		if (numSegments - 1 >= 4) {
			segment5 = segments[4];
		}
		if (numSegments - 1 >= 5) {
			segment6  = segments[5];
		}
		if (numSegments - 1 >= 6) {
			segment7 = segments[6];
		}

	}

	/*- ---------------------------- getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSegment1() {
		return segment1;
	}

	public void setSegment1(String segment1) {
		this.segment1 = segment1;
	}

	public String getSegment2() {
		return segment2;
	}

	public void setSegment2(String segment2) {
		this.segment2 = segment2;
	}

	public String getSegment3() {
		return segment3;
	}

	public void setSegment3(String segment3) {
		this.segment3 = segment3;
	}

	public String getSegment4() {
		return segment4;
	}

	public void setSegment4(String segment4) {
		this.segment4 = segment4;
	}

	public String getSegment5() {
		return segment5;
	}

	public void setSegment5(String segment5) {
		this.segment5 = segment5;
	}

	public String getSegment6() {
		return segment6;
	}

	public void setSegment6(String segment6) {
		this.segment6 = segment6;
	}

	public String getSegment7() {
		return segment7;
	}

	public void setSegment7(String segment7) {
		this.segment7 = segment7;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

}
