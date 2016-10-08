/**
 * 
 */
package com.blackbox.ids.exception;

/**
 * @author gurtegsingh
 *
 */
public class BBError {
	private String errorCode;
	private String field;

	public BBError(final String field,final String errorCode) {
		this.errorCode=errorCode;
		this.field=field;
	}
	
	
	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(final String errorCode) {
		this.errorCode = errorCode;
	}


	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}


	/**
	 * @param field the field to set
	 */
	public void setField(final String field) {
		this.field = field;
	}
	
	
	
	
}
