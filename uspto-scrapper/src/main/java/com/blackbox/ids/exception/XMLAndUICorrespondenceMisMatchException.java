package com.blackbox.ids.exception;

/**
 * Thrown when the count on UI of correspondences does not match with the correspondences in the XML.
 * 
 * @author nagarro
 *
 */
public class XMLAndUICorrespondenceMisMatchException extends RuntimeException {

	/**
	 * system generated serial version id
	 */
	private static final long serialVersionUID = -8211436659601073737L;

	public XMLAndUICorrespondenceMisMatchException(String message) {
		super(message);
	}

	public XMLAndUICorrespondenceMisMatchException(Throwable cause) {
		super(cause);
	}

	public XMLAndUICorrespondenceMisMatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public XMLAndUICorrespondenceMisMatchException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}