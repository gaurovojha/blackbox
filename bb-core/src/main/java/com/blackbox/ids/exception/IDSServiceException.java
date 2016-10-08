package com.blackbox.ids.exception;

/**
 * All the exceptions in the service layer should extend this
 * 
 * @author nagarro
 *
 */
public class IDSServiceException extends IDSException {
	
	
	/**
	 * system generated serial version id
	 */
	private static final long serialVersionUID = 7445156548121598431L;

	public IDSServiceException() {
	}
	
	public IDSServiceException(String message) {
		super(message);
	}

	public IDSServiceException(Throwable cause) {
		super(cause);
	}

	public IDSServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public IDSServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
