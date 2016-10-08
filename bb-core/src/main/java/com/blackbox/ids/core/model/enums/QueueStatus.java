package com.blackbox.ids.core.model.enums;

/**
 * The Enum QueueStatus.
 */
public enum QueueStatus {

	INITIATED,

	ERROR,

	CRAWLER_ERROR,

	ERROR_APPLICATION_IN_EXCLUSION,

	SUCCESS,

	/** Status for find child job */
	NO_CHILD_FOUND,

	/** Status for find foreign priority job */
	NO_FOREIGN_PRIORITY_FOUND,

	/** Status for find parent job */
	NO_PARENT_FOUND,

	REJECTED,

	AUTHENTICATION_ERROR,
	
	/** Import ready status for import job*/
	IMPORT_READY;
}