package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * It is the root element for ApplicationByCustomer xml file
 * 
 * @author ruchiwadhwa
 *
 */
@XStreamAlias("PatentApplicationList")
public class PatentApplicationList {

	@XStreamAlias("FileHeader")
	private FileHeader fileHeader;

	@XStreamAlias("ApplicationStatusData")
	@XStreamImplicit
	private List<ApplicationStatusData> applicationStatusData;

	public FileHeader getFileHeader() {
		return fileHeader;
	}

	public void setFileHeader(FileHeader fileHeader) {
		this.fileHeader = fileHeader;
	}

	public List<ApplicationStatusData> getApplicationStatusData() {
		return applicationStatusData;
	}

	public void setApplicationStatusData(
			List<ApplicationStatusData> applicationStatusData) {
		this.applicationStatusData = applicationStatusData;
	}

}
