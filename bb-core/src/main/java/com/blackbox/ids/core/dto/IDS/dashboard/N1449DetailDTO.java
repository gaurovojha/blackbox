package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class N1449DetailDTO extends N1449PendingDTO {
	
	private Date mailingDate;
	private String documentDesc;
	
	
	public N1449DetailDTO(Long dbId, String jurisdiction, String applicationNo,
			Map<String, Calendar> calMap,  Calendar mailingDate, String documentDesc) {
		
		super(dbId, null, jurisdiction, applicationNo, calMap, null);
		this.mailingDate = mailingDate.getTime();
		this.documentDesc = documentDesc;
	}
	public Date getMailingDate() {
		return mailingDate;
	}
	public void setMailingDate(Date mailingDate) {
		this.mailingDate = mailingDate;
	}
	public String getDocumentDesc() {
		return documentDesc;
	}
	public void setDocumentDesc(String documentDesc) {
		this.documentDesc = documentDesc;
	}
	
	
	

}
