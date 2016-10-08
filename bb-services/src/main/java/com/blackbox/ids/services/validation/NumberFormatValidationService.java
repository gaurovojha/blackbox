package com.blackbox.ids.services.validation;

import java.util.Calendar;

import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;

public interface NumberFormatValidationService {

	public String getConvertedValue(String originalValue, NumberType numberType, String jurisdiction,
			ApplicationType applicationType, Calendar filingDate,Calendar grantDate);
	
	public boolean isValidNumber(String applicationNumber, String publicationNumber, String grantNumber, NumberType numberType, String jurisdiction,
			ApplicationType applicationType, Calendar filingDate,Calendar grantDate);
		

}
