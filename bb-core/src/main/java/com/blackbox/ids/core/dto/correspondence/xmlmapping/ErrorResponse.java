package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The Class ErrorResponse.
 *
 * @author nagarro
 */
@XStreamAlias("ErrorResponse")
public class ErrorResponse {

	/** The file header. */
	@XStreamAlias("FileHeader")
	private FileHeader fileHeader;

	/** The error. */
	@XStreamAlias("Error")
	private Error error;

	/**
	 * Gets the file header.
	 *
	 * @return the file header
	 */
	public FileHeader getFileHeader() {
		return fileHeader;
	}

	/**
	 * Sets the file header.
	 *
	 * @param fileHeader the new file header
	 */
	public void setFileHeader(FileHeader fileHeader) {
		this.fileHeader = fileHeader;
	}

	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	public Error getError() {
		return error;
	}

	/**
	 * Sets the error.
	 *
	 * @param error the new error
	 */
	public void setError(Error error) {
		this.error = error;
	}

}