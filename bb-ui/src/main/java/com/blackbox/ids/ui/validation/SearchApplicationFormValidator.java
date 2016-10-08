package com.blackbox.ids.ui.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.blackbox.ids.services.correspondence.ICorrespondenceService;
import com.blackbox.ids.ui.form.correspondence.CorrespondenceForm;

@Component
public class SearchApplicationFormValidator implements Validator {

	@Autowired
	ICorrespondenceService correspondenceService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		System.out.println("SUPPORTS");
		return CorrespondenceForm.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		
		
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "applicationNumber", "correspondence.createform.application.notempty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "jurisdiction", "correspondence.createform.jurisdiction.notempty");
	
	}

}
