package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * It is the root element of the XML file
 * 
 * @author nagarro
 *
 */
@XStreamAlias("OutgoingCorrespondence")
public class OutgoingCorrespondence {

	@XStreamAlias("FileHeader")
	private FileHeader fileHeader;

	@XStreamAlias("CorrespondenceFileHeader")
	private CorrespondenceFileHeader correspondenceFileHeader;
	
	@XStreamAlias("ApplicationCorrespondence")
	private ApplicationCorrespondence applicationCorrespondence;

	/**
	 * @return the fileHeader
	 */
	public FileHeader getFileHeader() {
		return fileHeader;
	}

	/**
	 * @param fileHeader
	 *            the fileHeader to set
	 */
	public void setFileHeader(final FileHeader fileHeader) {
		this.fileHeader = fileHeader;
	}

	/**
	 * @return the correspondenceFileHeader
	 */
	public CorrespondenceFileHeader getCorrespondenceFileHeader() {
		return correspondenceFileHeader;
	}

	/**
	 * @param correspondenceFileHeader
	 *            the correspondenceFileHeader to set
	 */
	public void setCorrespondenceFileHeader(
			final CorrespondenceFileHeader correspondenceFileHeader) {
		this.correspondenceFileHeader = correspondenceFileHeader;
	}

	/**
	 * @return the applicationCorrespondence
	 */
	public ApplicationCorrespondence getApplicationCorrespondence() {
		return applicationCorrespondence;
	}

	/**
	 * @param applicationCorrespondence the applicationCorrespondence to set
	 */
	public void setApplicationCorrespondence(final ApplicationCorrespondence applicationCorrespondence) {
		this.applicationCorrespondence = applicationCorrespondence;
	}

}
