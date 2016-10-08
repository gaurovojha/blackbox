/**
 *
 */
package com.blackbox.ids.ui.validation.ids;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.ui.form.ids.IDSCertificationForm;

/**
 * {@link Validator} for form class {@link IDSCertificationForm}.
 * 
 * @author ajay2258
 */
@Component
public class CertificationStatementValidator implements Validator {

	@Autowired
	private NotificationProcessService notificationService;

	@Override
	public boolean supports(Class<?> clazz) {
		return IDSCertificationForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		IDSCertificationForm certificationForm = (IDSCertificationForm) target;
		validateCertificate(certificationForm, errors);
	}

	private void validateCertificate(IDSCertificationForm certificationForm, Errors errors) {
		// Certification statement must be attached (conditionally)
		if (certificationForm.isCertificationAttached() && certificationForm.getCertificationStatement() == null) {
			errors.rejectValue("certificationAttached", "ids.certificate.certificate.noAttachment");
		}

		// A valid attorney user must be satisfied.
		if (certificationForm.getAttorney() == null) {
			errors.rejectValue("attorney", "ids.certificate.blank.attorney");
		} else if (invalidAttorney(certificationForm)) {
			errors.rejectValue("attorney", "ids.certificate.invalid.attorney");
		}
	}

	private boolean invalidAttorney(IDSCertificationForm certificationForm) {
		List<Long> validAttorneys = notificationService.getReceiverUsers(NotificationProcessType.IDS_APPROVAL_REQUEST,
				certificationForm.getApplication());
		return validAttorneys.contains(certificationForm.getAttorney());
	}

}
