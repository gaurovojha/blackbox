package com.blackbox.ids.core.model.IDS;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.blackbox.ids.core.model.mstr.AuthenticationData;

@Entity
@Table(name="BB_MSTR_FILING_PRIVATE_PAIR")
public class IDSFilingPrivatePair implements Serializable{

	/** The serial version UID. */
	private static final long serialVersionUID = -1476697901788575028L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_PRIVATE_PAIR_KEY_ID")
	private Long id;
	
	@Column(name = "FILING_PKI_NAME")
	private String filingPKIName;
	
	@Column(name = "KEY_TYPE")
	private String keyType;
	
	@Column(name = "PKI_UPLOADED_BY")
	private Long uploadedBy;
	
	@Column(name = "LAST_ACTION_BY")
	private Long lastActionBy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_AUTH_DATA_ID", referencedColumnName = "BB_MSTR_AUTHENTICATION_DATA_ID", nullable = true)
	private AuthenticationData authenticationData;
	
	@Column(name = "DEPOSIT_ACC_NUMBER")
	private String depositAccountNumber;
	
	@Column(name= "DEPOSIT_ACC_ACCESS_CODE")
	private String DepositAccountAccessCode;

	
	/*- ---------------------------- Constructor -- */
	public IDSFilingPrivatePair() {
	}

	
	/*- ---------------------------- getter-setters -- */

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getFilingPKIName() {
		return filingPKIName;
	}


	public void setFilingPKIName(String filingPKIName) {
		this.filingPKIName = filingPKIName;
	}


	public String getKeyType() {
		return keyType;
	}


	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}


	public Long getUploadedBy() {
		return uploadedBy;
	}


	public void setUploadedBy(Long uploadedBy) {
		this.uploadedBy = uploadedBy;
	}


	public AuthenticationData getAuthenticationData() {
		return authenticationData;
	}


	public void setAuthenticationData(AuthenticationData authenticationData) {
		this.authenticationData = authenticationData;
	}


	public String getDepositAccountNumber() {
		return depositAccountNumber;
	}


	public void setDepositAccountNumber(String depositAccountNumber) {
		this.depositAccountNumber = depositAccountNumber;
	}


	public String getDepositAccountAccessCode() {
		return DepositAccountAccessCode;
	}


	public void setDepositAccountAccessCode(String depositAccountAccessCode) {
		DepositAccountAccessCode = depositAccountAccessCode;
	}

	
}