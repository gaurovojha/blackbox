package com.blackbox.ids.abbyy.exception;

/**
 * This exception is thrown when some exception occurs while connecting to Abby Engine.
 * 
 * @author nagarro
 *
 */
public class AbbyEngineConnectionException extends RuntimeException {

	/** system generated serial version id. */
	private static final long serialVersionUID = -8211436659601073737L;


	/**
	 * Instantiates a new abby engine connection exception.
	 *
	 * @param message the message
	 */
	public AbbyEngineConnectionException(String message) {
		super(message);
	}

	
	/**
	 * Instantiates a new abby engine connection exception.
	 *
	 * @param cause the cause
	 */
	public AbbyEngineConnectionException(Throwable cause) {
		super(cause);
	}

		/**
		 * Instantiates a new abby engine connection exception.
		 *
		 * @param message the message
		 * @param cause the cause
		 */
		public AbbyEngineConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

		/**
		 * Instantiates a new abby engine connection exception.
		 *
		 * @param message the message
		 * @param cause the cause
		 * @param enableSuppression the enable suppression
		 * @param writableStackTrace the writable stack trace
		 */
		public AbbyEngineConnectionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}