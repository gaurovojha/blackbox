/**
 *
 */
package com.blackbox.ids.dto.ids;

import java.util.Date;

/**
 * @author ajay2258
 *
 */
public class IDSApprovalDetails {

	/** Parent IDS identifier. */
	private long ids;

	/** Associated application identifier. */
	private long application;

	/** IDS filing fee. */
	private Double filingFee;

	/** Attorney user Id, whom the request for IDS approval will be sent to. */
	private Long attorney;

	/** Registration number of the attorney users. */
	private String registrationNo;

	/** Comments by the paralegal user before sending approval request. */
	private String comments;

	/** IDS final approval date. */
	private Date idsApprovalDate;

	/*- -------------------------------- Constructors -- */
	public IDSApprovalDetails() {
		super();
	}

	public IDSApprovalDetails(long ids, long application, Long attorney) {
		super();
		this.ids = ids;
		this.application = application;
		this.attorney = attorney;
	}

	/*- -------------------------------- getter-setters -- */
	public long getIds() {
		return ids;
	}

	public void setIds(long ids) {
		this.ids = ids;
	}

	public long getApplication() {
		return application;
	}

	public void setApplication(long application) {
		this.application = application;
	}

	public Double getFilingFee() {
		return filingFee;
	}

	public void setFilingFee(Double filingFee) {
		this.filingFee = filingFee;
	}

	public Long getAttorney() {
		return attorney;
	}

	public void setAttorney(Long attorney) {
		this.attorney = attorney;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getIdsApprovalDate() {
		return idsApprovalDate;
	}

	public void setIdsApprovalDate(Date idsApprovalDate) {
		this.idsApprovalDate = idsApprovalDate;
	}

}
