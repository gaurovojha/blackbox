/**
 *
 */
package com.blackbox.ids.core.model.mdm;

/**
 * Enumerates the possible validation status for an application validation.
 *
 * @author ajay2258
 */
public enum ApplicationValidationStatus {

	/** All validations pass for given application. */
	OK,

	/** Some mandatory parameters are missing. */
	ILLEGAL_ARGS,

	/** Given jurisdiction doesn't exist in system. */
	INVALID_JURISDICTION,

	/** Given application already exists in the system. */
	DUPLICATE,

	/** Format for application no. is not valid. */
	BAD_FORMAT,

	/** Application no. is listed in exclusion list. */
	EXCLUDED,
	
	CHILD_APPLICATION_TYPE_INVALID, 
	FAMILY_LINKAGE_NOT_FOUND, 
	CUSTOMER_NOT_FOUND, 
	CUSTOMER_NOT_EXIST, 
	DOCKET_NUMBER_NOT_FOUND, 
	DOCKET_NUMBER_INVALID_FORMAT, 
	ASSIGNEE_NOT_FOUND, 
	ASSIGNEE_NOT_EXIST, 
	FILING_DATE_NOT_FOUND, 
	CONFIRMATION_NUMBER_NOT_FOUND, 
	ENTITY_NOT_FOUND, 
	TITLE_NOT_FOUND, 
	PUBLICATION_NUMBER_NOT_FOUND, 
	PUBLICATION_DATE_NOT_FOUND, 
	PATENT_NUMBER_NOT_FOUND, 
	ISSUED_DATE_NOT_FOUND,
	FIRST_FILING_IMPORT_FAILED, 
	PENDING, 
	FAMILY_ID_MISMATCH, 
	FAMILY_NOT_EXIST;

}
