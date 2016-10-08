package com.blackbox.ids.core.dto.mdm;

import java.util.Calendar;

import com.blackbox.ids.core.model.notification.process.EntityName;
import com.mysema.query.annotations.QueryProjection;

public class CreateReqApplicationDTO extends ActionsBaseDto {

	private String sentBy;

	public CreateReqApplicationDTO() {
		super();
	}

	@QueryProjection
	public CreateReqApplicationDTO(final long notificationId,final long entityId, final EntityName entityName,
			final String jurisdiction,final String applicationNo,final String userFirstName , final String userLastName,final Calendar notifiedDate) {

		super(jurisdiction,applicationNo,notifiedDate,notificationId,entityId,entityName.name());
		this.sentBy = userFirstName + " " +userLastName;
	}

	/**
	 * @return the sentBy
	 */
	public String getSentBy() {
		return sentBy;
	}

	/**
	 * @param sentBy the sentBy to set
	 */
	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}

}
