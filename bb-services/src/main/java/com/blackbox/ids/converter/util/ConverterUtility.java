package com.blackbox.ids.converter.util;

import java.util.Calendar;

import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;

import com.blackbox.ids.core.model.ConversionStrategy;

public class ConverterUtility {

	public static String getConvertedValue(ConversionStrategy conversionStrategy, String originalValue, Calendar fillingDate){
		String convertedValue = null;
		switch(conversionStrategy){
		case REMOVE_CHARACTER:
			convertedValue = replaceCharacter(originalValue);
			break;
		case CORRECT_FORMAT:
			convertedValue = originalValue;
			break;
		case ADD_YEAR:
			convertedValue = addYear(originalValue);
			break;
		case ADD_WO:
			if(fillingDate!=null){
				convertedValue = addWO(originalValue, fillingDate);
			}
			break;
		case REMOVE_DIGIT:
			convertedValue = removeDigit(originalValue);
			break;
		case NUMBER_TO_YEAR:
			convertedValue = numberToYearConverter(originalValue);
			break;
		case VALIDATE_YEAR:
			if(fillingDate!=null){
				convertedValue = validateYear(originalValue,fillingDate);
			}
			break;
		case REMOVE_CC:
			convertedValue = removeCC(originalValue);
			break;
		default:
			break;
		}
		return convertedValue;
	}

	private static String replaceCharacter(String originalValue){
		return originalValue.replaceAll("[^\\w\\s]", "");
	}

	private static String addYear(String originalValue){
		return new StringBuilder(originalValue).insert(9, 0).toString();
	}

	private static String addWO(String originalValue, Calendar fillingDate){
		String convertedValue = null;
		if(originalValue.length()==11 && !originalValue.contains("WO")){
			convertedValue = new StringBuilder(originalValue).insert(0,"WO/").toString();
		}
		else if(originalValue.length()==8){
			int century = (fillingDate.get(Calendar.YEAR) / 100);
			String reqPart = "WO/"+century;
			convertedValue = new StringBuilder(originalValue).insert(0,reqPart).toString();
		}
		else if(originalValue.length()==11 && originalValue.contains("WO")){
			int century = (fillingDate.get(Calendar.YEAR) / 100);
			convertedValue = new StringBuilder(originalValue).insert(3,century).toString();
		}
		return convertedValue;

	}

	private static String removeDigit(String originalValue){
		int index = originalValue.indexOf('.');
		return originalValue.substring(0, index);		
	}

	private static String removeCC(String originalValue){
		String toBeReplaced = originalValue.substring(6, 8);
		return originalValue.replace(toBeReplaced, "");		
	}

	private static String numberToYearConverter(String originalValue){
		String convertedValue = null;
		int index = originalValue.indexOf("-");
		String requiredString = originalValue.substring(0, index);
		if(StringUtils.isNumeric(requiredString)){
			int numValue = Integer.valueOf(requiredString);
			if(numValue>=01 && numValue<12){
				convertedValue = new StringBuilder(originalValue.substring(index+1, originalValue.length())).insert(0, numValue+1988).toString();
			}
			else if(numValue>=12 && numValue<65){
				convertedValue = new StringBuilder(originalValue.substring(index+1, originalValue.length())).insert(0, numValue+1925).toString();
			}
			else{
				convertedValue = originalValue;
			}
		}
		return convertedValue;
	}

	private static String validateYear(String originalValue, Calendar fillingDate){
		String convertedValue = null;
		if(originalValue.contains(".")){
			convertedValue = removeDigit(originalValue);
		} else {
			convertedValue = originalValue;
		}
		Integer originalYear = (fillingDate.get(Calendar.YEAR) % 100);
		return convertedValue.replace(convertedValue.substring(1, 3), originalYear.toString());
	}
}
