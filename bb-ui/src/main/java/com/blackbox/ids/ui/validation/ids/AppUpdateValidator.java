/**
 *
 */
package com.blackbox.ids.ui.validation.ids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.blackbox.ids.core.dto.IDS.ApplicationDetailsDTO;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.dto.mdm.DocketNoValidationStatus;
import com.blackbox.ids.services.mdm.ApplicationService;

/**
 * @author ajay2258
 *
 */
@Component
public class AppUpdateValidator implements Validator {

	@Autowired
	private ApplicationService applicationService;

	@Override
	public boolean supports(Class<?> clazz) {
		return ApplicationDetailsDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		final ApplicationDetailsDTO applicationForm = (ApplicationDetailsDTO) target;
		validateUpdates(applicationForm, errors);
	}

	private void validateUpdates(ApplicationDetailsDTO application, final Errors errors) {
		// Validate filing date - future date
		if (application.getFilingDate() == null) {
			errors.rejectValue("filingDate", "ids.app.error.empty.dpFilingDate");
		} else if (!BlackboxDateUtil.isPastDate(
				BlackboxDateUtil.strToDate(application.getFilingDate(), ApplicationDetailsDTO.DATE_FORMAT))) {
			errors.rejectValue("filingDate", "ids.app.error.invalid.dpFilingDate");
		}

		// Validate docket no.
		String assignee = applicationService.getApplicationAssignee(application.getDbId()).getName();
		DocketNoValidationStatus status = applicationService.validateDocketNo(application.getDocketNo(), assignee);
		if (DocketNoValidationStatus.OK != status) {
			errors.rejectValue("docketNo", "ids.app.error.attorneyDocketNo." + status.name());
		}
	}

}
