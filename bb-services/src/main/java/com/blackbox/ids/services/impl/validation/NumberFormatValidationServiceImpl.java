package com.blackbox.ids.services.impl.validation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.converter.util.CompareNumberUtility;
import com.blackbox.ids.converter.util.ConverterUtility;
import com.blackbox.ids.core.dao.NumberFormatValidationDao;
import com.blackbox.ids.core.dao.correspondence.impl.CorrespondenceDAOImpl;
import com.blackbox.ids.core.model.ConversionStrategy;
import com.blackbox.ids.core.model.ValidationMatrix;
import com.blackbox.ids.core.model.ValidFormat;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.services.validation.NumberFormatValidationService;

@Service("numberFormatValidationServiceImpl")
public class NumberFormatValidationServiceImpl implements NumberFormatValidationService {

	@Autowired
	private NumberFormatValidationDao numberFormatValidationDao;
	
	private Logger logger = Logger.getLogger(NumberFormatValidationServiceImpl.class);

	/**
	 * This method is used to return the converted value if the format is valid otherwise return null
	 */
	@Override
	@Transactional(readOnly=true)
	public String getConvertedValue(String originalValue, NumberType numberType, String jurisdiction,
			ApplicationType applicationType,  Calendar filingDate,Calendar grantDate) {
		boolean validate = false;
		String convertedValue = null;
		ConversionStrategy conversionStrategy = null;
		List<ValidationMatrix> validationMatrixList = getValidationMatrixList(numberType, jurisdiction, applicationType, filingDate, grantDate);

		validationMatrixloop:
			for(ValidationMatrix validationMatrix:validationMatrixList){
				Set<ValidFormat> validFormat = validationMatrix.getValidFormat();
				for(ValidFormat format : validFormat){
					if(isValidateNumberFormat(format, originalValue, filingDate)){
						validate = true;
						conversionStrategy = format.getConvertersionStrategy();
						break validationMatrixloop;
					}
				}
			}
		if(validate){
			convertedValue = ConverterUtility.getConvertedValue(conversionStrategy, originalValue, filingDate);
		}
		return  convertedValue;
	}
	
	private List<ValidationMatrix> getValidationMatrixList(NumberType numberType, String jurisdiction,
			ApplicationType applicationType,  Calendar filingDate,Calendar grantDate){
		List<ValidationMatrix> validationMatrixList = new ArrayList<ValidationMatrix>();
		try{
			validationMatrixList =  numberFormatValidationDao.getValidationMatrix(numberType, jurisdiction,applicationType,filingDate,grantDate);
		}catch(DataRetrievalFailureException e){
			logger.error("No Record Found due to invalid entries", e);
		}catch(DataAccessException e){
			logger.error("Cannot Access Database due to System Problems	", e);
		}
		return validationMatrixList;
	}

	/**
	 * This method is used to validate the number format
	 */
	private boolean isValidateNumberFormat(ValidFormat format, String originalValue, Calendar filingDate){
		boolean validate = false;
		if(originalValue.matches(format.getRegexPattern())){
			if(isValidate(format.getConvertersionStrategy(), originalValue, filingDate)){
				validate = true;
			}
		}
		return validate;
	}

	private boolean isValidate(ConversionStrategy conversionStrategy, String originalValue, Calendar fillingDate){
		boolean validate = true;
		switch(conversionStrategy){
		case NUMBER_TO_YEAR:
			validate = isValidateNumber(originalValue);
			break;
		case VALIDATE_YEAR:
			validate = isValidateYear(originalValue, fillingDate);
			break;
		default:
			break;
		}
		return validate;
	}

	/**
	 * This method is used to check whether number is in specific range or not
	 */
	private boolean isValidateNumber(String originalValue){
		int index = originalValue.indexOf("-");
		String requiredString = originalValue.substring(0, index);
		if(StringUtils.isNumeric(requiredString)){
			int numValue = Integer.valueOf(requiredString);
			return (numValue<=64 && numValue>=01);
		}
		return false;
	}
	private boolean isValidateYear(String originalValue, Calendar fillingDate){
		int originalYear = (fillingDate.get(Calendar.YEAR) % 100);
		int requiredYear = Integer.valueOf(originalValue.substring(1, 3));
		return (requiredYear == originalYear-40);
	}

	/**
	 * This method is used to check equality of application number, publication number and grant number
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean isValidNumber(String applicationNumber, String publicationNumber,
			String grantNumber, NumberType numberType, String jurisdiction,
			ApplicationType applicationType, Calendar filingDate,
			Calendar grantDate) {
		boolean validate = false;
		List<ValidationMatrix> validationMatrixList = getValidationMatrixList(numberType, jurisdiction,applicationType,filingDate,grantDate);
		for(ValidationMatrix validationMatrix : validationMatrixList){
			if(CompareNumberUtility.isValidNumber(validationMatrix.getCheckNumEquality(), applicationNumber, publicationNumber, grantNumber)){
				validate = true;
				break;
			}
		}
		return validate;
	}

}
