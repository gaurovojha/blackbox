package com.blackbox.ids.dto;

import java.util.ArrayList;
import java.util.List;

import com.blackbox.ids.exception.BBError;


public class ResponseDTO {
	
	public enum ResponseStatus{
		SUCCESS,ERROR;
	}
	
	private List<BBError> errors = new ArrayList<>();
	private ResponseStatus status;
	/**
	 * @return the errors
	 */
	public List<BBError> getErrors() {
		return errors;
	}
	/**
	 * @param errors the errors to set
	 */
	public void setErrors(List<BBError> errors) {
		this.errors = errors;
	}
	/**
	 * @return the status
	 */
	public ResponseStatus getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
	
	
	
	
}
