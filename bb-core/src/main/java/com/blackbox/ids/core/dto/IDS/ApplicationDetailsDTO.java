/**
 *
 */
package com.blackbox.ids.core.dto.IDS;

import java.util.Calendar;

import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.mysema.query.annotations.QueryProjection;

/**
 * The class {@code ApplicationDetailsDTO} holds the application attributes visible on the build IDS screen.
 *
 * @author ajay2258
 */
public class ApplicationDetailsDTO {

	public static final TimestampFormat DATE_FORMAT = TimestampFormat.MMMDDYYYY;
	public static final String MODEL_NAME = "appForm";

	private long dbId;

	private Long idsID;

	private String applicationNo;

	private String filingDate;

	private String inventor;

	private String docketNo;

	private String artUnit;

	private String examiner;

	private boolean editView = false;

	private String jurisdiction;
	
	private String familyId;

	/*- ------------------------------------ Constructor -- */
	public ApplicationDetailsDTO() {
		super();
	}

	@QueryProjection
	public ApplicationDetailsDTO(final Long dbId, final Long idsID, final String applicationNo, final Calendar filingDate,
			final String inventor, final String docketNo, final String artUnit, final String examiner, final String familyId,
			final String jurisdiction) {
		super();
		this.dbId = dbId;
		this.idsID = idsID == null ? null : idsID;
		this.applicationNo = applicationNo;
		this.filingDate = filingDate == null ? null : BlackboxDateUtil.dateToStr(filingDate.getTime(), DATE_FORMAT);
		this.inventor = inventor;
		this.docketNo = docketNo;
		this.artUnit = artUnit;
		this.examiner = examiner;
		this.familyId =familyId;
		this.jurisdiction =jurisdiction;

	}
	
	@QueryProjection
	public ApplicationDetailsDTO(final Long dbId, final String applicationNo, final String docketNo, final String familyId,
			final String jurisdiction) {
		super();
		this.dbId = dbId;
		this.applicationNo = applicationNo;
		this.docketNo = docketNo;
		this.familyId =familyId;
		this.jurisdiction =jurisdiction;

	}

	/*- ------------------------------------ getter-setters -- */
	public long getDbId() {
		return dbId;
	}

	public void setDbId(long dbId) {
		this.dbId = dbId;
	}

	public Long getIdsID() {
		return idsID;
	}

	public void setIdsID(Long idsID) {
		this.idsID = idsID;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(String filingDate) {
		this.filingDate = filingDate;
	}

	public String getInventor() {
		return inventor;
	}

	public void setInventor(String inventor) {
		this.inventor = inventor;
	}

	public String getDocketNo() {
		return docketNo;
	}

	public void setDocketNo(String docketNo) {
		this.docketNo = docketNo;
	}

	public String getArtUnit() {
		return artUnit;
	}

	public void setArtUnit(String artUnit) {
		this.artUnit = artUnit;
	}

	public String getExaminer() {
		return examiner;
	}

	public void setExaminer(String examiner) {
		this.examiner = examiner;
	}

	public boolean isEditView() {
		return editView;
	}

	public void setEditView(boolean editView) {
		this.editView = editView;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

}
