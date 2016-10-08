package com.blackbox.ids.exception;

/**
 * Thrown when the USPTO site is updating
 * 
 * @author nagarro
 *
 */
public class USPTOUpdationException extends RuntimeException {

	/**
	 * system generated serial version id
	 */
	private static final long serialVersionUID = -8211436659601073737L;

	public USPTOUpdationException(String message) {
		super(message);
	}

	public USPTOUpdationException(Throwable cause) {
		super(cause);
	}

	public USPTOUpdationException(String message, Throwable cause) {
		super(message, cause);
	}

	public USPTOUpdationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}