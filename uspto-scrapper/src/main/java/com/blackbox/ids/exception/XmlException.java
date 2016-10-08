package com.blackbox.ids.exception;

/**
 * Thrown when any xml processing related error occurs
 * 
 * @author nagarro
 *
 */
public class XmlException extends RuntimeException {

	/**
	 * system generated serial version id
	 */
	private static final long serialVersionUID = 3946887690961831657L;

	public XmlException(String message) {
		super(message);
	}

	public XmlException(Throwable cause) {
		super(cause);
	}

	public XmlException(String message, Throwable cause) {
		super(message, cause);
	}

	public XmlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}