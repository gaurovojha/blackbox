package com.blackbox.ids.exception;

/**
 * This is application wide exception. All the layer specific exceptions should extend this.
 * 
 * @author nagarro
 *
 */
public class IDSException extends RuntimeException {

	/**
	 * system generated serial version id
	 */
	private static final long serialVersionUID = -9054118648259247631L;

	public IDSException() {
	}

	public IDSException(String message) {
		super(message);
	}

	public IDSException(Throwable cause) {
		super(cause);
	}

	public IDSException(String message, Throwable cause) {
		super(message, cause);
	}

	public IDSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}