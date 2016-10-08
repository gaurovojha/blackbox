/**
 *
 */
package com.blackbox.ids.core.dto.IDS;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.model.reference.ReferenceType;
import com.mysema.query.annotations.QueryProjection;

/**
 * The class <code>ReferenceRecordDTO</code> holds the attributes visible on US/foreign patent/publication numbers list
 * while building an IDS.
 *
 * @author ajay2258
 */
public class ReferenceRecordDTO {

	private long refFlowId;

	private ReferenceType referenceType;

	private String examinerInitial;

	/** Taken as patent or publication number. */
	private String patentNo;

	private String kindCode;

	private Date publishedOn;

	private String patentee;

	private String comments;

	private String reviewedBy;

	/*- ------------ Fields specific to foreign patents. -- */
	private String countryCode;

	private boolean translationsAvailable;

	/*- ------------ Fields specific to NPL. -- */
	private String npl;

	/*- --------------------------------- Constructors -- */
	public ReferenceRecordDTO() {
		super();
	}

	@QueryProjection
	public ReferenceRecordDTO(final Long refFlowId, final String patentNo, final String kindCode,
			final Calendar publishedOn, final String patentee, final String comments, final Long reviewedBy) {
		super();
		this.refFlowId = refFlowId;
		this.examinerInitial = null;
		this.patentNo = patentNo;
		this.kindCode = kindCode;
		this.publishedOn = publishedOn == null ? null : publishedOn.getTime();
		this.patentee = patentee;
		this.comments = comments;
		this.reviewedBy = String.valueOf(reviewedBy);
	}

	@QueryProjection
	public ReferenceRecordDTO(final Long refFlowId, final String nplString, final Long reviewedBy)
	{
		this.refFlowId = refFlowId;
		this.npl = nplString;
		this.reviewedBy = String.valueOf(reviewedBy);
	}
	/*- --------------------------------- getter-setters -- */
	public long getRefFlowId() {
		return refFlowId;
	}

	public void setRefFlowId(long refFlowId) {
		this.refFlowId = refFlowId;
	}

	public ReferenceType getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(ReferenceType referenceType) {
		this.referenceType = referenceType;
	}

	public String getExaminerInitial() {
		return examinerInitial;
	}

	public void setExaminerInitial(String examinerInitial) {
		this.examinerInitial = examinerInitial;
	}

	public String getPatentNo() {
		return patentNo;
	}

	public void setPatentNo(String patentNo) {
		this.patentNo = patentNo;
	}

	public String getKindCode() {
		return kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public Date getPublishedOn() {
		return publishedOn;
	}

	public void setPublishedOn(Date publishedOn) {
		this.publishedOn = publishedOn;
	}

	public String getPatentee() {
		return patentee;
	}

	public void setPatentee(String patentee) {
		this.patentee = patentee;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public boolean isTranslationsAvailable() {
		return translationsAvailable;
	}

	public void setTranslationsAvailable(boolean translationsAvailable) {
		this.translationsAvailable = translationsAvailable;
	}

	public String getNpl() {
		return npl;
	}

	public void setNpl(String npl) {
		this.npl = npl;
	}

}
