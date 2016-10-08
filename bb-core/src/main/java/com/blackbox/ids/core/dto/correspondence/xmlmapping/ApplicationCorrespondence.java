package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author rajatjain01
 *
 */
@XStreamAlias("ApplicationCorrespondence")
public class ApplicationCorrespondence {

	@XStreamAlias("ApplicationCorrespondenceData")
	@XStreamImplicit
	private List<ApplicationCorrespondenceData> applicationCorrespondenceData;

	/**
	 * @return the applicationCorrespondenceData
	 */
	public List<ApplicationCorrespondenceData> getApplicationCorrespondenceData() {
		return applicationCorrespondenceData;
	}

	/**
	 * @param applicationCorrespondenceData the applicationCorrespondenceData to set
	 */
	public void setApplicationCorrespondenceData(
			final List<ApplicationCorrespondenceData> applicationCorrespondenceData) {
		this.applicationCorrespondenceData = applicationCorrespondenceData;
	}

}