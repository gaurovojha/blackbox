package com.blackbox.ids.ui.validation.mdm;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.ApplicationValidationStatus;
import com.blackbox.ids.core.model.mdm.IncompatibleAttribute;
import com.blackbox.ids.core.model.mdm.IncompatibleAttribute.Field;
import com.blackbox.ids.core.model.mdm.IncompatibleAttribute.Jurisdiction;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.Assignee.Entity;
import com.blackbox.ids.core.model.mstr.AttorneyDocketFormat;
import com.blackbox.ids.core.util.BlackboxCollectionUtil;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.dto.mdm.ApplicationValidationDTO;
import com.blackbox.ids.dto.mdm.DocketNoValidationStatus;
import com.blackbox.ids.dto.mdm.MdmValidationData;
import com.blackbox.ids.services.common.MasterDataService;
import com.blackbox.ids.services.mdm.ApplicationService;
import com.blackbox.ids.services.validation.NumberFormatValidationService;
import com.blackbox.ids.ui.controller.MdmController.ApplicationScreen;
import com.blackbox.ids.ui.form.mdm.ApplicationForm;
import com.blackbox.ids.ui.form.mdm.ApplicationForm.Action;
import com.blackbox.ids.ui.form.mdm.SingleApplicationForm;
import com.blackbox.ids.ui.validation.common.MdmValidationProp;
import com.blackbox.ids.ui.validation.common.ValidationUtil;

@Component
public class ApplicationFormValidator implements Validator {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private MasterDataService masterDataService;

	@Autowired
	private NumberFormatValidationService numberFormatValidator;

	@Value("${assignee.docketno.validation}")
	private String assigneeDocketValidation ;


	@Override
	public boolean supports(Class<?> clazz) {
		return ApplicationForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		final ApplicationForm applicationForm = (ApplicationForm) target;

		final ApplicationScreen screen = ApplicationScreen.fromString(applicationForm.getScreen());
		switch (screen) {
		case INIT:
			// validateInit(applicationForm, errors);
			break;

		case DETAILS:
			removeDeletedApplications(applicationForm);
			validateDetails(applicationForm, errors);
			break;

		default:
			errors.reject("mdm.app.error.common.invalidScreen", new String[] { screen.name() }, null);
			break;
		}
	}

	private void validateInit(ApplicationForm form, Errors errors) {
		AtomicInteger idx = new AtomicInteger(0);
		form.getApps().stream().forEach(e -> this.validateInit(e, errors, "apps[" + idx.getAndIncrement() + "]."));
	}

	private void validateInit(SingleApplicationForm appForm, Errors errors, String prefix) {
		Calendar filedOn = StringUtils.isBlank(appForm.getFilingDate()) ? null
				: BlackboxDateUtil.toCalendar(appForm.getFilingDate(), ApplicationForm.DATE_FORMAT);

		ApplicationValidationDTO validationData = new ApplicationValidationDTO(appForm.getJurisdictionName(),
				appForm.getApplicationNo(), ApplicationType.fromString(appForm.getApplicationType()), filedOn);
		ApplicationValidationStatus status = applicationService.validateApplicationNo(validationData);

		if (status != ApplicationValidationStatus.OK) {
			String errorField = (status == ApplicationValidationStatus.INVALID_JURISDICTION) ? "jurisdictionName"
					: "applicationNo";
			errors.rejectValue(prefix + errorField, "mdm.app.error.applicationNo." + status.name(),
					new String[] { appForm.getApplicationNo() }, null);
		}

	}

	private void validateDetails(ApplicationForm form, Errors errors) {
		AtomicInteger idx = new AtomicInteger(0);
		form.getApps().stream().forEach(
				e -> this.validateDetails(ApplicationForm.Action.fromString(form.getAction()),
						e, errors, fetchValidationData(), "apps[" + idx.getAndIncrement() + "]."));
	}

	private MdmValidationData fetchValidationData() {
		List<String> customers = masterDataService.getUserCustomerNumbers();
		List<String> assignees = masterDataService.getUserAssignees();
		List<String> entities = Arrays.asList(Entity.values()).stream().map(Enum::name).collect(Collectors.toList());
		return new MdmValidationData(customers, assignees, entities);
	}

	private void validateDetails(Action action, SingleApplicationForm application, Errors errors,
			MdmValidationData validationData,
			String prefix) {

		if (!Action.UPDATE_APPLICATION.equals(action)) {
			validateInit(application, errors, prefix);
		}
		final Jurisdiction jurisdiction = Jurisdiction.fromString(application.getJurisdictionName());
		final ApplicationType applicationType = ApplicationType.fromString(application.getApplicationType());

		// Customer Number: Not required for foreign family member applications.
		if (IncompatibleAttribute.isMandatory(Field.CUSTOMER_NO, jurisdiction, applicationType)
				&& !containsIgnoreCase(validationData.getCustomerNumbers(), application.getCustomerNo())) {
			errors.rejectValue(prefix + "customerNo", "mdm.app.error.empty.customerNo");
		}

		// Assignee: Required for all scenarios.
		if (!BlackboxCollectionUtil.containsIgnoreCase(validationData.getAssignees(), application.getAssignee())) {
			errors.rejectValue(prefix + "assignee", "mdm.app.error.empty.assignee");
		} else if ("ON".equals(assigneeDocketValidation)) {
			// Assignee: Attorney Docket No. validation.
			Assignee validAssignee  = applicationService.getAssigneeFromDocketNo(application.getAttorneyDocketNo());
			if (validAssignee == null || !application.getAssignee().equals(validAssignee.getName())) {
				errors.rejectValue(prefix + "assignee", "mdm.app.error.empty.assignee");
			}
		}

		// Entity: Required for US applications only.
		if (IncompatibleAttribute.isMandatory(Field.ENTITY, jurisdiction, applicationType)
				&& !containsIgnoreCase(validationData.getEntityTypes(), application.getEntity())) {
			errors.rejectValue(prefix + "entity", "mdm.app.error.empty.entity");
		}

		validateNumberFormats(application, errors, prefix);

		// Attorney Docket # Validation
		DocketNoValidationStatus status = applicationService.validateDocketNo(application.getAttorneyDocketNo(),
				application.getAssignee());
		if (DocketNoValidationStatus.OK != status) {
			errors.rejectValue(prefix + "attorneyDocketNo", "mdm.app.error.empty.attorneyDocketNo");
		}

		// Confirmation No: Free text, Numeric, No Space
		if (IncompatibleAttribute.isMandatory(Field.CONFIRMATION_NO, jurisdiction, applicationType)) {
			ValidationUtil.validateNumericString(MdmValidationProp.INSTANCE, errors, application.getConfirmationNo(),
					prefix + "confirmationNo", "confirmationNo", true, null);
		}

		/*-
		// Required for all scenarios.
		Filing Date

		Title */

	}

	private void validateNumberFormats(SingleApplicationForm application, Errors errors, String prefix) {
		final ApplicationType applicationType = ApplicationType.fromString(application.getApplicationType());

		Calendar filingDate = StringUtils.isBlank(application.getFilingDate()) ? null
				: BlackboxDateUtil.toCalendar(application.getFilingDate(), ApplicationForm.DATE_FORMAT);
		Calendar publicationDate = StringUtils.isBlank(application.getPublicationDate()) ? null
				: BlackboxDateUtil.toCalendar(application.getPublicationDate(), ApplicationForm.DATE_FORMAT);

		// Publication No: Non-mandatory for all scenarios.
		if (StringUtils.isNoneBlank(application.getPublicationNumber())) {
			String publicationNoConverted = numberFormatValidator.getConvertedValue(application.getPublicationNumber(),
					NumberType.PUBLICATION, application.getJurisdictionName(), applicationType, filingDate,
					publicationDate);
			if (StringUtils.isBlank(publicationNoConverted)) {
				errors.rejectValue(prefix + "publicationNumber", "mdm.app.error.empty.publicationNumber");
			}
		}

		// Patent No: Non-mandatory for all scenarios.
		if (StringUtils.isNoneBlank(application.getPatentNumber())) {
			String patentNoConverted = numberFormatValidator.getConvertedValue(application.getPatentNumber(),
					NumberType.PATENT, application.getJurisdictionName(), applicationType, filingDate, publicationDate);
			if (StringUtils.isBlank(patentNoConverted)) {
				errors.rejectValue(prefix + "patentNumber", "mdm.app.error.empty.patentNumber");
			}
		}
	}

	private boolean validateDocketNumber(String docketNumber) {
		AttorneyDocketFormat docketFormat = masterDataService.getAttorneyDocketFormat();
		int segmentSize = docketFormat.getSegmentSize();
		int totalSegments = docketFormat.getNumSegments();
		String seperator = docketFormat.getSeparator();
		String regex ="(.{"+segmentSize+"}"+seperator+")"+"{"+(totalSegments-1)+"}"+".{"+segmentSize+"}";
		return docketNumber.matches(regex);
	}

	private void removeDeletedApplications(ApplicationForm applicationForm) {
		Set<SingleApplicationForm> deletedApps = applicationForm.getApps().stream()
				.filter(SingleApplicationForm::isUiDeleted).collect(Collectors.toSet());
		applicationForm.getApps().removeAll(deletedApps);
	}

	private boolean containsIgnoreCase(final Collection<String> collection, final String value) {
		return BlackboxCollectionUtil.containsIgnoreCase(collection, value);
	}

}
