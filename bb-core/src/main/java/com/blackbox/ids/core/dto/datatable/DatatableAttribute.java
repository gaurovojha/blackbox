/**
 *
 */
package com.blackbox.ids.core.dto.datatable;

/**
 * Enumerates the jQuery datatable attributes.
 *
 * @author ajay2258
 */
public enum DatatableAttribute {

	LIMIT("iLength"),
	OFFSET("iStart"),
	SORT_BY("iSortBy"),
	SORT_ORDER("iSortOrder"),
	SEARCH_TEXT("iSearch"),
	DATE_RANGE("iDateRange"),
	MY_RECORDS_ONLY("iMyRecords"),
	JURISDICTION("iJurisdiction"),
	FILTER_DATE_BY("iFilterDateBy"),
	FILTER_DATE("iFilterDate"),
	SORT_COLUMN("sColumns"),
	APPLICATION_NUMBER("iApplicationNumber"),
	ATTORNEY_DOCKET_NUMBER("iAttorneyDocketNumber"),
	FAMILY_ID("sFamilyId"),
	DOCUMENT_DESCRIPTION("sDocumentDescription"),
	UPLOADED_BY("sUploadedBy"),
	UPLOADED_ON("iUploadedDateRange"),
	RECORD_VIEW("iRecordFlag"),
	PROSECUTION_STATUS("iprosecutionStatus"),
	UNCITATED_REFERENCE("iUncitatedReference");

	public final String attrName;

	private DatatableAttribute(final String attrName) {
		this.attrName = attrName;
	}

}
