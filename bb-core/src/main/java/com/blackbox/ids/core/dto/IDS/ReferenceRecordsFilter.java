/**
 *
 */
package com.blackbox.ids.core.dto.IDS;

import com.blackbox.ids.core.model.reference.ReferenceType;

/**
 * @author ajay2258
 *
 */
public class ReferenceRecordsFilter {

	private long targetApplication;

	private long ids;

	private ReferenceType referenceType;

	/*- ---------------------------------- Constructors -- */
	public ReferenceRecordsFilter() {
		super();
	}

	public ReferenceRecordsFilter(final long application, final long ids, ReferenceType refType) {
		super();
		this.targetApplication = application;
		this.ids = ids;
		this.referenceType = refType;
	}
	
	public ReferenceRecordsFilter(final long application, final long ids) {
		super();
		this.targetApplication = application;
		this.ids = ids;
	}


	/*- ---------------------------------- getter-setters -- */
	public long getTargetApplication() {
		return targetApplication;
	}

	public void setTargetApplication(long targetApplication) {
		this.targetApplication = targetApplication;
	}

	public long getIds() {
		return ids;
	}

	public void setIds(long ids) {
		this.ids = ids;
	}

	public ReferenceType getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(ReferenceType referenceType) {
		this.referenceType = referenceType;
	}
	
}
