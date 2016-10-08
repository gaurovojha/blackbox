/**
 *
 */
package com.blackbox.ids.core.dto.IDS;

import java.util.Calendar;

import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.IDSRelevantStatus;
import com.mysema.query.annotations.QueryProjection;

/**
 * @author ajay2258
 *
 */
public class IDSFeeCalculationDetails {

	public final Long idsID;

	public final Calendar applicationFilingDate;

	public final Assignee.Entity entity;

	public final IDSRelevantStatus prosecutionStatus;

	public final int agingOfReferences;

	@QueryProjection
	public IDSFeeCalculationDetails(final Long idsID, final Calendar applicationFilingDate,
			final Assignee.Entity entity, final IDSRelevantStatus prosecutionStatus,
			final Calendar referenceCreatedOn) {

		super();
		this.idsID = idsID;
		this.applicationFilingDate = applicationFilingDate;
		this.entity = entity;
		this.prosecutionStatus = prosecutionStatus;
		this.agingOfReferences = 1;
	}

}
