package com.blackbox.ids.exception;

/**
 * This exception is thrown when an XML obtained is corrupted.
 * 
 * @author abhay2566
 *
 */
public class XMLCorruptedException extends RuntimeException {

	/**
	 * system generated serial version id
	 */
	private static final long serialVersionUID = -2882136152622129499L;
	
	public XMLCorruptedException(String message) {
		super(message);
	}

	public XMLCorruptedException(Throwable cause) {
		super(cause);
	}

	public XMLCorruptedException(String message, Throwable cause) {
		super(message, cause);
	}

	public XMLCorruptedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}