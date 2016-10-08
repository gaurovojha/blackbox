package com.blackbox.ids.exception;

/**
 * All the exceptions related to Repository layer should extend this 
 * 
 * @author nagarro
 *
 */
public class IDSRepositoryException extends IDSException {

	/**
	 * system generated serial version id
	 */
	private static final long serialVersionUID = -6063144543203454681L;

	public IDSRepositoryException() {
	}
	
	public IDSRepositoryException(String message) {
		super(message);
	}

	public IDSRepositoryException(Throwable cause) {
		super(cause);
	}

	public IDSRepositoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public IDSRepositoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
