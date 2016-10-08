package com.blackbox.ids.core.model.correspondence;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.base.BaseEntity;

/**
 * The Class Correspondence.
 */
@MappedSuperclass
public abstract class Correspondence extends BaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2651184214261182813L;

	/** Enumerates all possible sources for an application. */
	public enum Status {

		/** The pending. */
		PENDING,
		/** The deleted. */
		DELETED,
		/** Added to the create application queue */
		CREATE_APPLICATION_QUEUE_INITIATED,
		/** The create application queue processed. */
		CREATE_APPLICATION_QUEUE_PROCESSED,
		/** The imported from staging. */
		DUPLCATE_RECORD_FOUND_IN_BASE,
		/** The imported to base. */
		IMPORTED_TO_BASE_TABLE, IMPORTED_FROM_STAGING_JOB,
		/** The dropped. */
		DROPPED,
		/** Active Status. **/
		ACTIVE, 
		/** Error status **/
		ERROR,
		/** Create Application request status*/
		CREATE_APPLICATION_REQUEST_PENDING,
		CREATE_APPLICATION_REQUEST_APPROVED,
		CREATE_APPLICATION_REQUEST_REJECTED,
		APPROVED, REJECTED;
	}

	/**
	 * The Enum Source.
	 */
	public enum Source {

		/** The automatic. */
		AUTOMATIC,

		/** The manual. */
		MANUAL;
	}

	/**
	 * The Enum SubSourceTypes.
	 */
	public enum SubSourceTypes {
		SINGLE, BULK,
		/** The PAI r_ outgoin g_ correspondence. */
		PAIR_OUTGOING_CORRESPONDENCE,
		/** The ifw. */
		IFW, N417, EP_PTO;
	}

	/**
	 * The Enum Step Types
	 */
	public enum StepType {

		/** Post submit validation step */
		POST_SUBMIT_VALIDATION,
		/** OCR step **/
		OCR,
		/** Post OCR validation step */
		POST_OCR_VALIDATION
	}

	/**
	 * Step Status enum types
	 */
	public enum StepStatus {
		NEW, RUNNING, COMPLETED, ERROR;
	}
	
	
	/** The efs id. */
	@Column(name = "N417_EFS_ID")
	private String efsId;


	/** The mailing date. */
	@Column(name = "MAILING_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar mailingDate;

	/** The status. */
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private Status status;

	/** The source. */
	@Column(name = "SOURCE")
	@Enumerated(EnumType.STRING)
	private Source source;

	/** The sub source. */
	@Column(name = "SUB_SOURCE")
	@Enumerated(EnumType.STRING)
	private SubSourceTypes subSource;

	/** The split id. */
	@Column(name = "SPLIT_ID")
	private Long splitId;

	/** The parent child. */
	@Column(name = "PARENT_CHILD")
	private String parentChild;

	/** The comment. */
	@Column(name = "COMMENT")
	private String comment;

	/** The attachment size. */
	@Column(name = "ATTACHMENT_SIZE")
	private Long attachmentSize;

	/** The page count. */
	@Column(name = "ATTACHMENT_PAGE_COUNT")
	private Integer pageCount;

	/** The filing date. */
	@Column(name = "FILING_DATE")
	private Calendar filingDate;

	/**
	 * Gets the mailing date.
	 *
	 * @return the mailing date
	 */
	public Calendar getMailingDate() {
		return mailingDate;
	}

	/**
	 * Sets the mailing date.
	 *
	 * @param mailingDate
	 *            the new mailing date
	 */
	public void setMailingDate(Calendar mailingDate) {
		this.mailingDate = mailingDate;
	}

	/**
	 * Gets the split id.
	 *
	 * @return the split id
	 */
	public Long getSplitId() {
		return splitId;
	}

	/**
	 * Sets the split id.
	 *
	 * @param splitId
	 *            the new split id
	 */
	public void setSplitId(Long splitId) {
		this.splitId = splitId;
	}

	/**
	 * Gets the parent child.
	 *
	 * @return the parent child
	 */
	public String getParentChild() {
		return parentChild;
	}

	/**
	 * Sets the parent child.
	 *
	 * @param parentChild
	 *            the new parent child
	 */
	public void setParentChild(String parentChild) {
		this.parentChild = parentChild;
	}

	/**
	 * Gets the comment.
	 *
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment.
	 *
	 * @param comment
	 *            the new comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Gets the page count.
	 *
	 * @return the page count
	 */
	public Integer getPageCount() {
		return pageCount;
	}

	/**
	 * Sets the page count.
	 *
	 * @param pageCount
	 *            the new page count
	 */
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Gets the attachment size.
	 *
	 * @return the attachment size
	 */
	public Long getAttachmentSize() {
		return attachmentSize;
	}

	/**
	 * Sets the attachment size.
	 *
	 * @param attachmentSize
	 *            the new attachment size
	 */
	public void setAttachmentSize(Long attachmentSize) {
		this.attachmentSize = attachmentSize;
	}

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public Source getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 *
	 * @param source
	 *            the new source
	 */
	public void setSource(Source source) {
		this.source = source;
	}

	/**
	 * Gets the sub source.
	 *
	 * @return the sub source
	 */
	public SubSourceTypes getSubSource() {
		return subSource;
	}

	/**
	 * Sets the sub source.
	 *
	 * @param subSource
	 *            the new sub source
	 */
	public void setSubSource(SubSourceTypes subSource) {
		this.subSource = subSource;
	}

	public Calendar getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(Calendar filingDate) {
		this.filingDate = filingDate;
	}
	
	public String getEfsId() {
		return efsId;
	}

	public void setEfsId(String efsId) {
		this.efsId = efsId;
	}

}
