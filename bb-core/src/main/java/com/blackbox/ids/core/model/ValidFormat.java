package com.blackbox.ids.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BB_VALID_FORMAT")
public class ValidFormat implements Serializable{

    /**
	 *  Generated Serialization Id
	 */
	private static final long serialVersionUID = 5743296272172364060L;

	/** Id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_VALID_FORMAT_ID")
	private Long id;

	/** Valid Format */
	@Column(name = "VALID_FORMAT")
	private String validFormat;

	/** Regex Pattern */
	@Column(name = "REGEX_PATTERN")
	private String regexPattern;

	/** Converter Strategy */
	@Column(name = "CONVERTER_STRATEGY")
	@Enumerated(EnumType.STRING)
	private ConversionStrategy convertersionStrategy;

	/** Required Format */
	@Column(name = "REQ_FORMAT")
	private String reqFormat;



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		ValidFormat other = (ValidFormat) obj;
		if (id != other.id)
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

	public String getValidFormat() {
		return validFormat;
	}

	public void setValidFormat(String validFormat) {
		this.validFormat = validFormat;
	}

	public String getRegexPattern() {
		return regexPattern;
	}

	public void setRegexPattern(String regexPattern) {
		this.regexPattern = regexPattern;
	}

	public ConversionStrategy getConvertersionStrategy() {
		return convertersionStrategy;
	}

	public void setConvertersionStrategy(ConversionStrategy convertersionStrategy) {
		this.convertersionStrategy = convertersionStrategy;
	}

	public String getReqFormat() {
		return reqFormat;
	}

	public void setReqFormat(String reqFormat) {
		this.reqFormat = reqFormat;
	}



}
