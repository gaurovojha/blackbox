package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.core.model.mstr.Jurisdiction;


public class IDSDrillDownInfoDTO extends IDSDashBoardBaseDTO {
	 
	public static final TimestampFormat DATE_FORMAT 	 = TimestampFormat.MMMDDYYYY;
	public static final String MODEL_HEADER 			 = "idsDrillDownInfo";
	public static final String MODEL_FILINGDATES 		 = "idsDrillDownInfoFilingDates";
	public static final String MODEL_REFERENCETYPES      = "idsDrillDownInfoReferenceTypes";
	public static final String MODEL_REFERENCETYPECOUNTS = "idsReferenceTypeCounts";

	private static final String US_PATENT      = "US Patents";
	private static final String US_PUBLICATION = "US Publication";
	private static final String FOREIGN 	   = "Foreign";
	private static final String NPL 	       = "NPL";
	
	private Long IDSId;

	private String idsFilingDate;
	
	private Date idsFilingDate1;
	
	private long count;
	
	private Long citeId;
	
	private String publicationNumber;
	
	private Long citedReferencesUSPatent;
	
	private Long citedReferencesUSPublications;
	
	private Long citedReferencesForeign;
	
	private Long citedReferencesNPL;
	
	private String NPLString;
	
	private String documentDescription;
	
	private String mailingDate;
	
	private String filingDate;
	
	private String filingChannel;
	
	private String attorneyDocketNo;
	
	private String referenceType;

	private String jurisdictionCode;
	
	private Long referenceFlowId;
	
	private Long referenceBaseId;
	
	private Long referenceSourceId;
	
	private Long corrId;
	
	private String refFlowStatus; 
	
	private String RFFilingDate;
	
	public IDSDrillDownInfoDTO() {
		super();
	}

	//For Header Section
	public IDSDrillDownInfoDTO(Long dbId, String familyId, String jurisdiction,
			String applicationNo, Calendar appFilingDate,  String attorneyDocketNumber) {
		super(dbId, familyId, jurisdiction, applicationNo);
		//this.IDSId = idsId;
		this.filingDate = BlackboxDateUtil.calendarToStr(appFilingDate, TimestampFormat.DDMMYYYY);
		this.attorneyDocketNo = attorneyDocketNumber;
	}

	//For IDS Filing Dates
	public IDSDrillDownInfoDTO(Long dbId, Long idsId, Long idsFilingInfoIDSId, 
			Calendar idsFilingDate, long countOfRecords ) {
		super(dbId, null, null, null);
		this.IDSId = idsId;
		this.idsFilingDate = BlackboxDateUtil.calendarToStr(idsFilingDate, TimestampFormat.DDMMYYYY);
		this.idsFilingDate1 = idsFilingDate.getTime();
		this.count=countOfRecords;
	}

	//For cited reference counts
	public IDSDrillDownInfoDTO(Long dbId, Long idsId, ReferenceType referenceType, 
			long countOfUSPatent, long countOfUSPublication, long countOfForeign, long countOfNPL) {
		super(dbId, null, null, null);
		this.IDSId = idsId;
		this.referenceType = referenceType.toString();
		if(referenceType.equals(ReferenceType.PUS)){
			this.referenceType = US_PATENT;
		}else if(referenceType.equals(ReferenceType.US_PUBLICATION)){
			this.referenceType = US_PUBLICATION;
		}else if(referenceType.equals(ReferenceType.FP)){
			this.referenceType = FOREIGN;
		}else if(referenceType.equals(ReferenceType.NPL)){
			this.referenceType = NPL;
		}
		this.citedReferencesUSPatent=countOfUSPatent;
		this.citedReferencesUSPublications=countOfUSPublication;
		this.citedReferencesForeign=countOfForeign;
		this.citedReferencesNPL=countOfNPL;
	}
	
	//For Cited References Detailed count
		public IDSDrillDownInfoDTO(ReferenceType referenceType, 
				Long count) {
			super();
			this.referenceType = referenceType.toString();
			this.count = count;
		}
		
	//For Cited References Detailed section
	public IDSDrillDownInfoDTO(Long dbId, Long idsId, Long referenceFlowId, Long referenceBaseId, ReferenceType referenceType, 
			long citeId, String code, String publicationNumber,String NPLString) {
		super(dbId, null, null, null);
		this.IDSId = idsId;
		this.referenceFlowId = referenceFlowId;
		this.referenceBaseId = referenceBaseId;
		this.referenceType = referenceType.toString();
		this.citeId = citeId;
		this.jurisdictionCode = code;
		this.publicationNumber=publicationNumber;
		this.NPLString = NPLString;
	}
	
	//For Cited References Detailed section
		public IDSDrillDownInfoDTO(Long dbId, Long idsId, Long referenceFlowId, Long referenceBaseId, ReferenceType referenceType, 
				long citeId, String code, String publicationNumber,List<String> NPLStrings) {
			super(dbId, null, null, null);
			this.IDSId = idsId;
			this.referenceFlowId = referenceFlowId;
			this.referenceBaseId = referenceBaseId;
			this.referenceType = referenceType.toString();
			this.citeId = citeId;
			this.jurisdictionCode = code;
			this.publicationNumber=publicationNumber;
			this.NPLString = NPLStrings.get(0);
		}
		
	//For Cited Reference Source Document
	public IDSDrillDownInfoDTO(Long corrId, String code, 
			String applicationNumber, String familyId, String documentDescription, Calendar mailingDate) {
		super(null, familyId, null, applicationNumber);
		//this.referenceBaseId = referenceBaseId;
		this.corrId = corrId;
		this.jurisdictionCode = code;
		this.mailingDate = BlackboxDateUtil.calendarToStr(mailingDate, TimestampFormat.DDMMYYYY);
		this.documentDescription = documentDescription;
	}
	
	//For Cited Reference Source Other Documents
	public IDSDrillDownInfoDTO(Long referenceBaseId, String code, String publicationNumber, ReferenceType referenceType, 
			ReferenceFlowStatus refFlowStatus, String NPLString, Calendar RFFilingDate) {
		super();
		this.referenceBaseId = referenceBaseId;
		this.jurisdictionCode = code;
		this.publicationNumber=publicationNumber;
		this.referenceType = referenceType.toString();
		this.refFlowStatus = refFlowStatus.toString();
		this.NPLString = NPLString;
		this.RFFilingDate = BlackboxDateUtil.calendarToStr(RFFilingDate, TimestampFormat.DDMMYYYY);
	}
	
	public IDSDrillDownInfoDTO(Long idsId, Long dbId, String familyId, String jurisdiction,
			String applicationNo, List<Date> idsFilingdates, List<Long> citeIds,
			List<String> publicationNumbers, Long citedReferencesUSPatent,
			Long citedReferencesUSPublications, Long citedReferencesForeign,
			Long citedReferencesNPL, //String nPLString,
			//String documentDescription,
			Calendar mailingDate,
			IDS.FilingChannel filingChannel) {
		super(dbId, familyId, jurisdiction, applicationNo);
		this.IDSId = idsId;
		//this.idsFilingdates = idsFilingdates;
		this.citedReferencesUSPatent = citedReferencesUSPatent;
		this.citedReferencesUSPublications = citedReferencesUSPublications;
		this.citedReferencesForeign = citedReferencesForeign;
		this.citedReferencesNPL = citedReferencesNPL;
		/*NPLString = nPLString;
		DocumentDescription = documentDescription;*/
		this.filingChannel = filingChannel.toString();
	}
	
	public String getRFFilingDate() {
		return RFFilingDate;
	}

	public void setRFFilingDate(String rFFilingDate) {
		RFFilingDate = rFFilingDate;
	}

	
	public String getRefFlowStatus() {
		return refFlowStatus;
	}

	public void setRefFlowStatus(String refFlowStatus) {
		this.refFlowStatus = refFlowStatus;
	}

	public Long getReferenceSourceId() {
		return referenceSourceId;
	}

	public void setReferenceSourceId(Long referenceSourceId) {
		this.referenceSourceId = referenceSourceId;
	}

	
	public Long getCorrId() {
		return corrId;
	}

	public void setCorrId(Long corrId) {
		this.corrId = corrId;
	}
	
	public Long getReferenceBaseId() {
		return referenceBaseId;
	}

	public void setReferenceBaseId(Long referenceBaseId) {
		this.referenceBaseId = referenceBaseId;
	}

	public Long getReferenceFlowId() {
		return referenceFlowId;
	}

	public void setReferenceFlowId(Long referenceFlowId) {
		this.referenceFlowId = referenceFlowId;
	}
	
	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	public String getAttorneyDocketNo() {
		return attorneyDocketNo;
	}

	public void setAttorneyDocketNo(String attorneyDocketNo) {
		this.attorneyDocketNo = attorneyDocketNo;
	}

	public Long getIDSId() {
		return IDSId;
	}

	public void setIDSId(Long iDSId) {
		IDSId = iDSId;
	}
	
	public String getIdsFilingDate() {
		return idsFilingDate;
	}

	public void setIdsFilingDate(String idsFilingDate) {
		this.idsFilingDate = idsFilingDate;
	}

	
	public String getPublicationNumber() {
		return publicationNumber;
	}

	public void setPublicationNumber(String publicationNumber) {
		this.publicationNumber = publicationNumber;
	}

	public Long getCitedReferencesUSPatent() {
		return citedReferencesUSPatent;
	}

	public void setCitedReferencesUSPatent(Long citedReferencesUSPatent) {
		this.citedReferencesUSPatent = citedReferencesUSPatent;
	}

	public Long getCitedReferencesUSPublications() {
		return citedReferencesUSPublications;
	}

	public void setCitedReferencesUSPublications(Long citedReferencesUSPublications) {
		this.citedReferencesUSPublications = citedReferencesUSPublications;
	}

	public Long getCitedReferencesForeign() {
		return citedReferencesForeign;
	}

	public void setCitedReferencesForeign(Long citedReferencesForeign) {
		this.citedReferencesForeign = citedReferencesForeign;
	}

	public Long getCitedReferencesNPL() {
		return citedReferencesNPL;
	}

	public void setCitedReferencesNPL(Long citedReferencesNPL) {
		this.citedReferencesNPL = citedReferencesNPL;
	}

	public String getNPLString() {
		return NPLString;
	}

	public void setNPLString(String nPLString) {
		NPLString = nPLString;
	}

	public String getDocumentDescription() {
		return documentDescription;
	}

	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	public String getMailingDate() {
		return mailingDate;
	}

	public void setMailingDate(String mailingDate) {
		this.mailingDate = mailingDate;
	}

	public String getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(String filingDate) {
		this.filingDate = filingDate;
	}
	
	public String getFilingChannel() {
		return filingChannel;
	}

	public void setFilingChannel(String filingChannel) {
		this.filingChannel = filingChannel;
	}

	public Date getIdsFilingDate1() {
		return idsFilingDate1;
	}

	public void setIdsFilingDate1(Date idsFilingDate1) {
		this.idsFilingDate1 = idsFilingDate1;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
	
	public Long getCiteId() {
		return citeId;
	}

	public void setCiteId(Long citeId) {
		this.citeId = citeId;
	}
	

	public String getJurisdictionCode() {
		return jurisdictionCode;
	}

	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}
}
