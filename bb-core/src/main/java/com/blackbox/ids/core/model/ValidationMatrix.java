package com.blackbox.ids.core.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;

@Entity
@NamedQueries({
	@NamedQuery(name="findByType",
			query="SELECT n FROM ValidationMatrix n WHERE n.numberType=:numberType AND n.jurisdiction=:jurisdiction AND (n.applicationType=:applicationType OR n.applicationType=:defaultType) AND ((n.startFilingDate<=:filingDate AND n.endFilingDate>=:filingDate) OR (n.startFilingDate<=:filingDate AND n.endFilingDate is NULL) OR (n.startFilingDate is NULL AND n.endFilingDate>=:filingDate) OR (n.startFilingDate is NULL AND n.endFilingDate is NULL)) AND ( (n.startGrantDate<=:grantDate AND n.endGrantDate>=:grantDate) OR (n.startGrantDate<:grantDate AND n.endGrantDate is NULL) OR (n.startGrantDate is NULL AND n.endGrantDate>=:grantDate) OR (n.startGrantDate is NULL AND n.endGrantDate is NULL))"),
}) 
@Table(name = "BB_VALIDATION_MATRIX")
public class ValidationMatrix implements Serializable{

	/** system generated serial version id. */
	private static final long serialVersionUID = 5810654947659076493L;

	/** Id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_VALIDATION_MATRIX_ID")
	private Long id;

	/** Number Type */
	@Column(name = "NUMBER_TYPE")
	@Enumerated(EnumType.STRING)
	private NumberType numberType;

	/** Jurisdiction Type */
	@Column(name = "JURISDICTION")
	private String jurisdiction;

	/** Application Type */
	@Column(name = "APPLICATION_TYPE")
	@Enumerated(EnumType.STRING)
	private ApplicationType applicationType;

	/**Start Filing Date */
	@Column(name = "START_FILING_DATE",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startFilingDate;

	/**End Filing Date */
	@Column(name = "END_FILING_DATE",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endFilingDate;

	/**Start Grant Date */
	@Column(name = "START_GRANT_DATE",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startGrantDate;

	/**End Grant Date */
	@Column(name = "END_GRANT_DATE",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endGrantDate;
	
	/**Check Numbers Equality */
	@Column(name = "CHECK_NUM_EQUALITY")
	@Enumerated(EnumType.STRING)
	private CompareNumber checkNumEquality;

	/** Valid Format Set */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "BB_VALIDATION_MATRIX_ID")
	private Set<ValidFormat> validFormat;




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((applicationType == null) ? 0 : applicationType.hashCode());
		result = prime * result
				+ ((endFilingDate == null) ? 0 : endFilingDate.hashCode());
		result = prime * result
				+ ((endGrantDate == null) ? 0 : endGrantDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((jurisdiction == null) ? 0 : jurisdiction.hashCode());
		result = prime * result
				+ ((numberType == null) ? 0 : numberType.hashCode());
		result = prime * result
				+ ((startFilingDate == null) ? 0 : startFilingDate.hashCode());
		result = prime * result
				+ ((startGrantDate == null) ? 0 : startGrantDate.hashCode());
		result = prime * result
				+ ((validFormat == null) ? 0 : validFormat.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValidationMatrix other = (ValidationMatrix) obj;
		if (applicationType == null) {
			if (other.applicationType != null)
				return false;
		} else if (!applicationType.equals(other.applicationType))
			return false;
		if (endFilingDate == null) {
			if (other.endFilingDate != null)
				return false;
		} else if (!endFilingDate.equals(other.endFilingDate))
			return false;
		if (endGrantDate == null) {
			if (other.endGrantDate != null)
				return false;
		} else if (!endGrantDate.equals(other.endGrantDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (jurisdiction == null) {
			if (other.jurisdiction != null)
				return false;
		} else if (!jurisdiction.equals(other.jurisdiction))
			return false;
		if (numberType == null) {
			if (other.numberType != null)
				return false;
		} else if (!numberType.equals(other.numberType))
			return false;
		if (startFilingDate == null) {
			if (other.startFilingDate != null)
				return false;
		} else if (!startFilingDate.equals(other.startFilingDate))
			return false;
		if (startGrantDate == null) {
			if (other.startGrantDate != null)
				return false;
		} else if (!startGrantDate.equals(other.startGrantDate))
			return false;
		if (validFormat == null) {
			if (other.validFormat != null)
				return false;
		} else if (!validFormat.equals(other.validFormat))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NumberType getNumberType() {
		return numberType;
	}

	public void setNumberType(NumberType numberType) {
		this.numberType = numberType;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

	public Calendar getStartFilingDate() {
		return startFilingDate;
	}

	public void setStartFilingDate(Calendar startFilingDate) {
		this.startFilingDate = startFilingDate;
	}

	public Calendar getEndFilingDate() {
		return endFilingDate;
	}

	public void setEndFilingDate(Calendar endFilingDate) {
		this.endFilingDate = endFilingDate;
	}

	public Calendar getStartGrantDate() {
		return startGrantDate;
	}

	public void setStartGrantDate(Calendar startGrantDate) {
		this.startGrantDate = startGrantDate;
	}

	public Calendar getEndGrantDate() {
		return endGrantDate;
	}

	public void setEndGrantDate(Calendar endGrantDate) {
		this.endGrantDate = endGrantDate;
	}

	public Set<ValidFormat> getValidFormat() {
		return validFormat;
	}

	public void setValidFormat(Set<ValidFormat> validFormat) {
		this.validFormat = validFormat;
	}

	public CompareNumber getCheckNumEquality() {
		return checkNumEquality;
	}

	public void setCheckNumEquality(CompareNumber checkNumEquality) {
		this.checkNumEquality = checkNumEquality;
	}
	
	


}
