/**
 *
 */
package com.blackbox.ids.ui.form.ids;

import org.springframework.web.multipart.MultipartFile;

import com.blackbox.ids.core.model.IDS.CertificationStatement;
import com.blackbox.ids.ui.form.base.EntityForm;

/**
 * The class {@code IDSCertificationForm} serves as a form for IDS certification statement.
 *
 * @author ajay2258
 */
public class IDSCertificationForm implements EntityForm<CertificationStatement> {

	public static final String MODEL_KEY = "idsCertificationForm";
	
	public static final String PRIVATE_PAIR_KEY = "privatePairKey";

	/** Database Id for certification statement */
	private Long dbId;

	/** Parent IDS identifier. */
	private long ids;

	/** Associated application identifier. */
	private long application;

	/**
	 * Indicates that each item of information contained in the information disclosure statement was first cited in any
	 * communication from a foreign patent office in a counterpart foreign application not more than three months prior
	 * to the filing of the information disclosure statement. See 37 CFR 1.97(e)(1).
	 */
	private boolean priorCited;

	/**
	 * Indicates that no item of information contained in the information disclosure statement was cited in a
	 * communication from a foreign patent office in a counterpart foreign application, and ...
	 */
	private boolean priorUncited;

	/** Indicates whether a certification statement is attached or not. */
	private boolean certificationAttached;

	/** Certification statement attached in conjuction with {@link #isCertificationAttached()}. */
	private MultipartFile certificationStatement;

	/** Indicates that the fee set forth in 37 CFR 1.17 (p) has been submitted herewith. */
	private boolean feeSubmitted;

	/** Indicates that a certification statement is not submitted herewith. */
	private boolean certificationStatementSubmitted;

	/** e-signature of the paralegal user, who has requested the approval for IDS. */
	// private MultipartFile signature;

	/** IDS filing fee. */
	private Double filingFee;

	/** Attorney user Id, whom the request for IDS approval will be sent to. */
	private Long attorney;

	/** Registration number of the attorney users. */
	private String registrationNo;

	/** Comments by the paralegal user before sending approval request. */
	private String comments;

	/** IDS final approval date. */
	private String idsApprovalDate;
	
	/*Source of certificate form either its INITIATE IDS or FILE IDS*/
	private String certificateSource;

	/*- --------------------------------- Constructors -- */
	public IDSCertificationForm() {
		super();
	}

	public IDSCertificationForm(Long idsID, Long application, CertificationStatement dbCertificate, String certificateSource) {
		super();
		this.ids = idsID;
		this.application = application;

		if (dbCertificate != null) {
			this.dbId = dbCertificate.getId();
			this.priorCited = dbCertificate.isPriorCited();
			this.priorUncited = dbCertificate.isPriorUncited();
			// this.certificationAttached = dbCertificate.isCertificationAttached();
			// XXX: this.attachmentName = dbCertificate.getCertificationStatement();
			this.feeSubmitted = dbCertificate.isFeeSubmitted();
			this.certificationStatementSubmitted = dbCertificate.isCertificationStatementSubmitted();
			this.comments = dbCertificate.getComment();
			this.certificateSource = certificateSource;
		}
	}

	/*- --------------------------------- To 'n' From Entity -- */
	@Override
	public CertificationStatement toEntity() {
		CertificationStatement certificate = new CertificationStatement();
		certificate.setId(dbId);
		certificate.setPriorCited(priorCited);
		certificate.setPriorUncited(priorUncited);
		certificate.setCertificationAttached(certificationAttached);
		if (certificationAttached) {
			certificate.setCertificationStatement(certificationStatement.getOriginalFilename());
			certificate.setAttachment(certificationStatement);
		}
		certificate.setFeeSubmitted(feeSubmitted);
		certificate.setCertificationStatementSubmitted(certificationStatementSubmitted);
		certificate.setComment(comments);
		return certificate;
	}

	@Override
	public void load(CertificationStatement object) {
	}

	/*- --------------------------------- getter-setters -- */
	public Long getDbId() {
		return dbId;
	}

	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}

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

	public boolean isPriorCited() {
		return priorCited;
	}

	public void setPriorCited(boolean priorCited) {
		this.priorCited = priorCited;
	}

	public boolean isPriorUncited() {
		return priorUncited;
	}

	public void setPriorUncited(boolean priorUncited) {
		this.priorUncited = priorUncited;
	}

	public boolean isCertificationAttached() {
		return certificationAttached;
	}

	public void setCertificationAttached(boolean certificationAttached) {
		this.certificationAttached = certificationAttached;
	}

	public MultipartFile getCertificationStatement() {
		return certificationStatement;
	}

	public void setCertificationStatement(MultipartFile certificationStatement) {
		this.certificationStatement = certificationStatement;
	}

	public boolean isFeeSubmitted() {
		return feeSubmitted;
	}

	public void setFeeSubmitted(boolean feeSubmitted) {
		this.feeSubmitted = feeSubmitted;
	}

	public boolean isCertificationStatementSubmitted() {
		return certificationStatementSubmitted;
	}

	public void setCertificationStatementSubmitted(boolean certificationStatementSubmitted) {
		this.certificationStatementSubmitted = certificationStatementSubmitted;
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

	public String getIdsApprovalDate() {
		return idsApprovalDate;
	}

	public void setIdsApprovalDate(String idsApprovalDate) {
		this.idsApprovalDate = idsApprovalDate;
	}

	public String getCertificateSource() {
		return certificateSource;
	}

	public void setCertificateSource(String certificateSource) {
		this.certificateSource = certificateSource;
	}

	
}
