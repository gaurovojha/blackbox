package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author nagarro
 *
 */
@XStreamAlias("CorrespondenceFileHeader")
public class CorrespondenceFileHeader {

	@XStreamAlias("DocumentCount") 
	private String documentCount;
	
	@XStreamAlias("CorrespondenceTimePeriod") 
	private String correspondenceTimePeriod;

	/**
	 * @return the documentCount
	 */
	public String getDocumentCount() {
		return documentCount;
	}

	/**
	 * @param documentCount
	 *            the documentCount to set
	 */
	public void setDocumentCount(final String documentCount) {
		this.documentCount = documentCount;
	}

	/**
	 * @return the correspondenceTimePeriod
	 */

	public String getCorrespondenceTimePeriod() {
		return correspondenceTimePeriod;
	}

	/**
	 * @param correspondenceTimePeriod
	 *            the correspondenceTimePeriod to set
	 */
	public void setCorrespondenceTimePeriod(final String correspondenceTimePeriod) {
		this.correspondenceTimePeriod = correspondenceTimePeriod;
	}

}
