/**
 *
 */
package com.blackbox.ids.core.dto.mdm.dashboard;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.blackbox.ids.core.model.mstr.ProsecutionStatus;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;

/**
 * The class object holds the filters, applicable on MDM dashboard active records list.
 *
 * @author ajay2258
 */
public class ActiveRecordsFilter {

	private Logger log = Logger.getLogger(ActiveRecordsFilter.class);

	public static final TimestampFormat DATE_FORMAT = TimestampFormat.MMDDYYYY;

	private String searchText;
	
	private String applicationNo;
	
	private String jurisdiction;
	
	private String attorneyDocketNo;
	
	private String familyId;
	
	private String description;
	
	private String uploadedBy;

	private Long ownedBy;

	private Date startDate;

	private Date endDate;
	
	private Date uploadStartDate;
	
	private Date uploadEndDate;
	
	private Date lastIDSFilingDate;
	
	private Date timeSinceLastReport;
	
	private String uncitedReference;
	
	private ProsecutionStatus prosecutionStatus;

	/*- ---------------------------- constructors -- */
	public ActiveRecordsFilter() {
		super();
	}

	public ActiveRecordsFilter(final String searchText, final String dateRange, final Long owner) {
		this.searchText = searchText;
		this.ownedBy = owner;
		try{
			if (!StringUtils.isEmpty(dateRange)) {
				Date[] range = BlackboxDateUtil.convertStringToDateRange(dateRange, TimestampFormat.MMMDDYYYY);
				this.startDate = range[0];
				this.endDate = range[1];
			}
		} catch(Exception e) {
			log.error("ActiveRecordsFilter :: Unable to parse Date passed in search filter");
		}
	}

	
	public ActiveRecordsFilter(final String applicationNo,final String jurisdiction, final String attorneyDocketNo, final String familyId,final String description,final String uploadedBy, final String uploadedOn, final String dateRange, final Long owner) {
		this.applicationNo = applicationNo;
		this.jurisdiction = jurisdiction;
		this.attorneyDocketNo = attorneyDocketNo;
		this.familyId = familyId;
		this.description = description;
		this.uploadedBy = uploadedBy;
		this.ownedBy = owner;
		try{
			if (!StringUtils.isEmpty(uploadedOn)) {
				Date[] range = BlackboxDateUtil.convertStringToDateRange(uploadedOn, TimestampFormat.MMMDDYYYY);
				this.uploadStartDate = range[0];
				this.setUploadEndDate(range[1]);
			}
		} catch(Exception e) {
			log.error("ActiveRecordsFilter :: Unable to parse Date passed in search filter");
		}
	}
	
	/*--------------------------------- constructor to apply more filters --------------------------------------*/
	
	public ActiveRecordsFilter(final String applicationNo,final String jurisdiction, final String attorneyDocketNo, 
			final String familyId, final String lastIDSFilingDate, final String timeSinceLastReport, 
			final String uncitedReferences, final String prosecutionStatus,final String dateRange){
		
		this.applicationNo=applicationNo;
		this.jurisdiction= jurisdiction;
		this.attorneyDocketNo=attorneyDocketNo;
		this.familyId=familyId;
		this.uncitedReference= uncitedReferences;
		this.prosecutionStatus= (ProsecutionStatus.contains(prosecutionStatus)) ? (ProsecutionStatus.valueOf(prosecutionStatus)) : null;
		try{
			if (!StringUtils.isEmpty(lastIDSFilingDate)) {
				Date range = BlackboxDateUtil.strToDate(lastIDSFilingDate, TimestampFormat.MMMDDYYYY);
				this.lastIDSFilingDate=range;
			}
		} catch(Exception e) {
			log.error("ActiveRecordsFilter :: Unable to parse Date passed in search filter");
		}
		
		try{
			if (!StringUtils.isEmpty(timeSinceLastReport)) {
				Date range =BlackboxDateUtil.strToDate(timeSinceLastReport, TimestampFormat.MMMDDYYYY);
				this.timeSinceLastReport= range;
			}
		} catch(Exception e) {
			log.error("ActiveRecordsFilter :: Unable to parse Date passed in search filter");
		}
		
		try{
			if (!StringUtils.isEmpty(dateRange)) {
				Date[] range = BlackboxDateUtil.convertStringToDateRange(dateRange, TimestampFormat.MMMDDYYYY);
				this.startDate = range[0];
				this.endDate = range[1];
			}
		} catch(Exception e) {
			log.error("ActiveRecordsFilter :: Unable to parse Date passed in search filter");
		}
		
		
		
	}

	
	/*- ---------------------------- getter-setters -- */
	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
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

	public Long getOwnedBy() {
		return ownedBy;
	}

	public void setOwnedBy(Long ownedBy) {
		this.ownedBy = ownedBy;
	}


	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
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

	public static TimestampFormat getDateFormat() {
		return DATE_FORMAT;
	}

	public Date getLastIDSFilingDate() {
		return lastIDSFilingDate;
	}

	public void setLastIDSFilingDate(Date lastIDSFilingDate) {
		this.lastIDSFilingDate = lastIDSFilingDate;
	}

	public Date getTimeSinceLastReport() {
		return timeSinceLastReport;
	}

	public void setTimeSinceLastReport(Date timeSinceLastReport) {
		this.timeSinceLastReport = timeSinceLastReport;
	}

	public String getUncitedReference() {
		return uncitedReference;
	}

	public void setUncitedReference(String uncitedReference) {
		this.uncitedReference = uncitedReference;
	}

	public ProsecutionStatus getProsecutionStatus() {
		return prosecutionStatus;
	}

	public void setProsecutionStatus(ProsecutionStatus prosecutionStatus) {
		this.prosecutionStatus = prosecutionStatus;
	}

}
