package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author nagarro
 *
 */
@XStreamAlias("FileHeader")
public class FileHeader {

	@XStreamAlias("FileCreationTimeStamp")
	private String FileCreationTimeStamp;

	/**
	 * @return the fileCreationTimeStamp
	 */
	public String getFileCreationTimeStamp() {
		return FileCreationTimeStamp;
	}

	/**
	 * @param fileCreationTimeStamp the fileCreationTimeStamp to set
	 */
	public void setFileCreationTimeStamp(final String fileCreationTimeStamp) {
		FileCreationTimeStamp = fileCreationTimeStamp;
	}
	
	
}
