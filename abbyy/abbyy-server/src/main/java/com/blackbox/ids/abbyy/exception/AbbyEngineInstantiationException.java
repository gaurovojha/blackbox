package com.blackbox.ids.abbyy.exception;

/**
 * Thrown when the attempt to create the singleton bean has been made.
 * 
 * @author nagarro
 *
 */
public class AbbyEngineInstantiationException extends RuntimeException {

	/** system generated serial version id. */
	private static final long serialVersionUID = -8211436659601073737L;

	/**
	 * Instantiates a new abby engine instantiation exception.
	 *
	 * @param message the message
	 */
	public AbbyEngineInstantiationException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new abby engine instantiation exception.
	 *
	 * @param cause the cause
	 */
	public AbbyEngineInstantiationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new abby engine instantiation exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public AbbyEngineInstantiationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new abby engine instantiation exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public AbbyEngineInstantiationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}