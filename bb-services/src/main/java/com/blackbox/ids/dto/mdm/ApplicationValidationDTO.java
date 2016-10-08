/**
 *
 */
package com.blackbox.ids.dto.mdm;

import java.util.Calendar;

import com.blackbox.ids.core.model.mdm.ApplicationType;

/**
 * The class <code>ApplicationValidationDTO</code> contains the application attributes required for various application
 * number validations.
 *
 * @author ajay2258
 */
public class ApplicationValidationDTO {

	public final String jurisdiction;

	public final String applicationNo;

	public final ApplicationType applicationType;

	public final Calendar filingDate;

	/*- ----------------------------- Constructor -- */
	public ApplicationValidationDTO(final String jurisdiction, final String applicationNo,
			final ApplicationType applicationType, final Calendar filingDate) {
		super();
		this.jurisdiction = jurisdiction;
		this.applicationNo = applicationNo;
		this.applicationType = applicationType;
		this.filingDate = filingDate;
	}

}
