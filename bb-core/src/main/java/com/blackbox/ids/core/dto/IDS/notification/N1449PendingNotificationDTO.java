package com.blackbox.ids.core.dto.IDS.notification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.mysema.query.annotations.QueryProjection;

public class N1449PendingNotificationDTO extends NotificationBaseDTO {
	
	private List<Date> idsPending1449;
	private Long dbId;
	private String familyId;

	@QueryProjection
	public N1449PendingNotificationDTO(final Long notificationId, final Long appdbId,final String familyId,final String jurisdiction,
			final String appNo,final String message, final NotificationStatus status,
			final NotificationProcessType notificationName,final Calendar notifiedDate,final Long dbId,final Map<String, Calendar> map ) {
		super(notificationId,appdbId,jurisdiction,appNo,(notifiedDate==null ? null : notifiedDate.getTime()),message,status,notificationName);
		this.familyId = familyId;
		this.dbId = dbId;
		List<Date> idsPending1449Dates = new ArrayList<Date>();
		for(Entry<String, Calendar> entry : map.entrySet())
		{
			Date mydate = entry.getValue().getTime();
			idsPending1449Dates.add(mydate);
		}
		this.idsPending1449 = idsPending1449Dates;
	}


	public long getDbId() {
		return dbId;
	}
	
	public void setDbId(long dbId) {
		this.dbId = dbId;
	}

	public List<Date> getIdsPending1449() {
		return idsPending1449;
	}

	public void setIdsPending1449(List<Date> idsPending1449) {
		this.idsPending1449 = idsPending1449;
	}
	
	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}
	
}
