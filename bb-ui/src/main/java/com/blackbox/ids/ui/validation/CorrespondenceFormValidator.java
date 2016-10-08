package com.blackbox.ids.ui.validation;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.services.correspondence.ICorrespondenceService;
import com.blackbox.ids.ui.form.correspondence.CorrespondenceForm;
@Component
public class CorrespondenceFormValidator implements Validator {

	private static final Logger LOGGER = Logger.getLogger(CorrespondenceFormValidator.class);
	
	private static final String PDF = "pdf";
	@Autowired
	ICorrespondenceService correspondenceService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		LOGGER.info("SUPPORTS");
		return CorrespondenceForm.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		
		final CorrespondenceForm user = (CorrespondenceForm) object;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "applicationNumber", "correspondence.createform.application.notempty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "jurisdiction", "correspondence.createform.jurisdiction.notempty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "documentDescription", "correspondence.createform.documentdescription.notempty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mailingDate", "correspondence.createform.mailingdate.notempty");
		if(user.getFile()!=null && !(user.getFile().getOriginalFilename().endsWith("." + PDF)))
		{
			errors.rejectValue("file", "correspondence.file.invalid.format");
		}
		if(user.getMailingDate()!=null )
		{
			
			try {
				Date mailingDate=BlackboxDateUtil.strToDate(user.getMailingDate(), TimestampFormat.MMMDDYYYY);
				// mailing date: must not be a future date.
				if (!BlackboxDateUtil.isPastDate(mailingDate) && !mailingDate.equals(new Date())) {
						errors.rejectValue("mailingDate", "correspondence.mailingdate.not.future");
					}

				}
			catch (Exception e) {
				LOGGER.error(e) ;
				errors.rejectValue("mailingDate", "correspondence.mailingdate.invalid.format");
			}
		}
	}

}