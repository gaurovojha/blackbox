package com.blackbox.ids.exception;

/**
 * Thrown when any server or connection related error occurs
 * 
 * @author nagarro
 *
 */
public class AuthenticationFailureException extends RuntimeException {

	/**
	 * system generated serial version id
	 */
	private static final long serialVersionUID = -8211436659601073737L;

	public AuthenticationFailureException(String message) {
		super(message);
	}

	public AuthenticationFailureException(Throwable cause) {
		super(cause);
	}

	public AuthenticationFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationFailureException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}