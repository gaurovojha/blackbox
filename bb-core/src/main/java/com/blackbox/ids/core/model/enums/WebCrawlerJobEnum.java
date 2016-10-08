package com.blackbox.ids.core.model.enums;

/**
 * The Enum WebCrawlerJobsEnum.
 */
public enum WebCrawlerJobEnum {
		
	/** The outgoing correspondence job. */
	OUTGOING_CORRESPONDENCE_JOB("OutgoingCorrespondenceJob"),
	
	/** The ifw doc download job. */
	IFW_DOC_DOWNLOAD_JOB("IFWDocDownloadJob"),
	
	/** The web crawler private pair audit job. */
	WEB_CRAWLER_PRIVATE_PAIR_AUDIT_JOB("WebCrawlerPrivatePairAuditJob");
	
	/** The job name. */
	private String jobName;

	/**
	 * Instantiates a new web crawler jobs enum.
	 *
	 * @param jobName the job name
	 */
	private WebCrawlerJobEnum(String jobName)
	{
		this.jobName = jobName;
	}

	/**
	 * Gets the job name.
	 *
	 * @return the job name
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * Sets the job name.
	 *
	 * @param jobName the new job name
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
}
