package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

// TODO: Auto-generated Javadoc
/**
 * The Class Error.
 *
 * @author nagarro
 */
@XStreamAlias("Error")
public class Error {

	/** The error message. */
	@XStreamAlias("ErrorMessage")
	private String errorMessage;

	/**
	 * Gets the error message.
	 *
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets the error message.
	 *
	 * @param errorMessage the new error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	

}