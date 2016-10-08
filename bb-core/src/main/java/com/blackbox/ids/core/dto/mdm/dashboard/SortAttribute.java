package com.blackbox.ids.core.dto.mdm.dashboard;

public enum SortAttribute {


	FAMILY_ID("familyid"),
	JURISDICTION("jurisdiction"),
	APPLICATION_NO("application"),
	ATTORNEY_DOCKET_NO("attorneydocket"),
	FILING_DATE("filingdate"),
	ASSIGNEE("assignee"),
	APPLICATION_TYPE("applicationtype"),
	CREATED_BY("createdby"),
	LAST_EDITED_BY("lastedited"),
	NOTIFIED_ON("notifiedon"),
	CURRENT_STATUS("currentstatus"),
	REQUESTED_FOR("requestedFor"),
	REQUESTED_BY("requestedBy"),
	LINKED_FAMILY_ID("linkedFamilyId"),
	LINKED_JURISDICTION("linkedJurisdiction"),
	LINKED_APPLICATION_NO("linkedApplication"),
	SOURCE("source"),
	DISCREPENCIES("discrepencies"),
	REFERENCE_DOCUMENT("document"),
	CORR_ACTION_ITEM_JURISDICTION("jurisdiction_code"),
	CORR_ACTION_ITEM_APPLICATION_NO("application_no"),
	CORR_ACTION_ITEM_MAILING_DATE("mailing_date"),
	CORR_ACTION_ITEM_DOCUMENT_DESCRIPTION("document_desc"),
	CORR_ACTION_ITEM_NOTIFIED("notified"),
	CORR_ACTION_ITEM_STATUS("status"),
	CORR_REQUESTER("requester"),
	CORR_APPROVER("approver"),
	SENT_BY("sentby");



	public final String attr;

	private SortAttribute() {
		this.attr = this.name();
	}

	private SortAttribute(final String attr) {
		this.attr = attr;
	}

	public static SortAttribute fromString(final String attr) {
		SortAttribute sortAttribute = null;
		for (final SortAttribute e : SortAttribute.values()) {
			if (e.attr.equalsIgnoreCase(attr)) {
				sortAttribute = e;
				break;
			}
		}
		return sortAttribute;
	}
}
