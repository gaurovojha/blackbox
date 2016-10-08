package com.blackbox.ids.ui.validation.reference;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.model.reference.ReferenceType;

@Component
public class ReferenceFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ReferenceBaseDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {

		final ReferenceBaseDTO referenceForm = (ReferenceBaseDTO) target;

		final ReferenceType referenceType = referenceForm.getReferenceType();

		switch (referenceType) {

		case PUS:
		case US_PUBLICATION:
			validatePusForm(referenceForm, errors);
			break;
		case FP:
			validateFpForm(referenceForm, errors);
			break;
		case NPL:
			validateNplForm(referenceForm, errors);
			break;
		default:
			errors.rejectValue("referenceType","invalid.reference.type");
			break;
		}
	}

	/**
	 *
	 * @param referenceForm
	 * @param errors
	 */
	private void validateFpForm(ReferenceBaseDTO referenceForm, Errors errors) {

		/*		if(StringUtils.isEmpty(referenceForm.getjuReferenceFpDto().getJurisdiction())){
            errors.rejectValue("referenceFpDto.jurisdiction", "referenceForm.jurisdiction.required");
        }

		if(StringUtils.isEmpty(referenceForm.getReferenceFpDto().getForeignDocumentNumber())){
            errors.rejectValue("referenceFpDto.foreignDocumentNumber", "referenceForm.foreigndocumentnumber.required");
        }

		if(referenceForm.isManualAdd()) {

			if(StringUtils.isEmpty(referenceForm.getReferenceFpDto().getApplicantName())){
	            errors.rejectValue("referenceFpDto.applicantName", "referenceForm.applicantname.required");
	        }

			if(StringUtils.isEmpty(referenceForm.getReferenceFpDto().getPublicationDate())){
	            errors.rejectValue("referenceFpDto.publicationDate", "referenceForm.publicationdate.required");
	        }
		}

		 */
	}

	/**
	 *
	 * @param referenceForm
	 * @param errors
	 */
	private void validatePusForm(ReferenceBaseDTO referenceForm, Errors errors) {

		if(StringUtils.isEmpty(referenceForm.getPublicationNumber())){
			errors.rejectValue("referencePusDto.publicationNumber", "referenceForm.publicationnumber.required");
		}

		if(referenceForm.isManualAdd()) {

			if(StringUtils.isEmpty(referenceForm.getApplicantName())){
				errors.rejectValue("referencePusDto.applicantName", "referenceForm.applicantname.required");
			}

			/*	if(StringUtils.isEmpty(referenceForm.getPublicationDate())){
	            errors.rejectValue("referencePusDto.publicationDate", "referenceForm.publicationdate.required");
	        }
			 */	}
	}

	/**
	 *
	 * @param referenceForm
	 * @param errors
	 */
	private void validateNplForm(ReferenceBaseDTO referenceForm, Errors errors) {
		// TODO Auto-generated method stub

	}
}