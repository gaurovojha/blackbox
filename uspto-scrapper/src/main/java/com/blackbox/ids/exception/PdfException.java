package com.blackbox.ids.exception;

/**
 * Thrown when any pdf processing related error occurs
 * 
 * @author nagarro
 *
 */
public class PdfException extends RuntimeException {

	/**
	 * system generated serial version id
	 */
	private static final long serialVersionUID = -9054118648259247631L;

	public PdfException(String message) {
		super(message);
	}

	public PdfException(Throwable cause) {
		super(cause);
	}

	public PdfException(String message, Throwable cause) {
		super(message, cause);
	}

	public PdfException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}