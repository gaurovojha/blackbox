package com.blackbox.ids.core.dto.correspondence.dashboard;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;

public class CorrespondenceRecordsFilter {

	private static final Logger LOGGER = Logger.getLogger(CorrespondenceRecordsFilter.class);

	public static final TimestampFormat DATE_FORMAT = TimestampFormat.MMDDYYYY;

	private Long ownedBy;

	private Date startDate;

	private Date endDate;

	private String applicationNo;

	private String jurisdiction;

	private String attorneyDocketNo;

	private String familyId;

	private String description;

	private String uploadedBy;

	private Date uploadStartDate;

	private Date uploadEndDate;

	public CorrespondenceRecordsFilter()
	{

	}

	public CorrespondenceRecordsFilter(final Long ownedBy, final String dateRange)
	{
		super();
		this.ownedBy = ownedBy;
		try {
			if (!StringUtils.isEmpty(dateRange)) {
				Date[] range = BlackboxDateUtil.convertStringToDateRange(dateRange, TimestampFormat.MMMDDYYYY);
				this.startDate = range[0];
				this.endDate = range[1];
			}
		} catch (Exception e) {
			LOGGER.error("CorrespondenceRecordsFilter :: Unable to parse Date passed in search filter");
			LOGGER.error(e);
		}
	}

	public CorrespondenceRecordsFilter(final String applicationNo, final String jurisdiction,
			final String attorneyDocketNo, final String familyId, final String description, final String uploadedBy,
			final String uploadedOn, final String dateRange, final Long ownedBy)
	{
		this.applicationNo = applicationNo;
		this.jurisdiction = jurisdiction;
		this.attorneyDocketNo = attorneyDocketNo;
		this.familyId = familyId;
		this.description = description;
		this.uploadedBy = uploadedBy;
		this.ownedBy = ownedBy;
		try {
			if (!StringUtils.isEmpty(dateRange)) {
				Date[] range = BlackboxDateUtil.convertStringToDateRange(dateRange, TimestampFormat.MMMDDYYYY);
				this.startDate = range[0];
				this.endDate = range[1];
			}

			if (!StringUtils.isEmpty(uploadedOn)) {
				Date[] range = BlackboxDateUtil.convertStringToDateRange(uploadedOn, TimestampFormat.MMMDDYYYY);
				this.uploadStartDate = range[0];
				this.uploadEndDate = range[1];
			}
		} catch (Exception e) {
			LOGGER.error("ActiveRecordsFilter :: Unable to parse Date passed in search filter");
			LOGGER.error(e);
		}
	}

	public Long getOwnedBy() {
		return ownedBy;
	}

	public void setOwnedBy(Long ownedBy) {
		this.ownedBy = ownedBy;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getAttorneyDocketNo() {
		return attorneyDocketNo;
	}

	public void setAttorneyDocketNo(String attorneyDocketNo) {
		this.attorneyDocketNo = attorneyDocketNo;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public Date getUploadStartDate() {
		return uploadStartDate;
	}

	public void setUploadStartDate(Date uploadStartDate) {
		this.uploadStartDate = uploadStartDate;
	}

	public Date getUploadEndDate() {
		return uploadEndDate;
	}

	public void setUploadEndDate(Date uploadEndDate) {
		this.uploadEndDate = uploadEndDate;
	}

}
