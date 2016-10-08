/**
 *
 */
package com.blackbox.ids.dto.mdm;

/**
 * Enumerates the possible validation status for an attorney docket number validation.
 *
 * @author ajay2258
 */
public enum DocketNoValidationStatus {

	/** All validations pass for given docket #. */
	OK,

	/** Some mandatory parameters are missing. */
	ILLEGAL_ARGS,

	/** Format for docket # doesn't match with defined in setup. */
	INVALID_FORMAT,

	/** Combination of Assignee and Docket # isn't valid. */
	WRONG_COMBO

}
