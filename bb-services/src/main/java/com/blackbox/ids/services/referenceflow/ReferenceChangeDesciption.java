package com.blackbox.ids.services.referenceflow;

public enum ReferenceChangeDesciption {

	/**
	 * Use to manage reference creation/update/drop
	 */
	REF_RECORD_CREATED_NEW_REF_FLOW_FLAG_YES, 
	REF_RECORD_CREATED_NEW_REF_FLOW_FLAG_NO, 
	REF_RECORD_CREATED_DUPLICATE_REF_FLOW_FLAG_YES, 
	REF_RECORD_CREATED_DUPLICATE_REF_FLOW_FLAG_NO, 
	REF_RECORD_DROPPED, 
	REF_RECORD_FLOW_FLAG_TO_CHANGE_YES_TO_NO, 
	REF_RECORD_FLOW_FLAG_TO_CHANGE_NO_TO_YES,

	/**
	 * Use to manage reference creation/drop on application/family create/update/drop
	 */
	NEW_APPLICATION_ADDED_TO_FAMILY_IN_MDM, 
	APPLICATION_FAMILTY_ID_CHANGED_IN_MDM_TO_A_DIFF_FAMILY,

	/**
	 * Use to manage source reference creation/drop
	 */
	SOURCE_REF_RECORD_CREATED, 
	SOURCE_REF_RECORD_DROPPED, 
	SOURCE_REF_NPL_DUPLICATE_EXIST
}