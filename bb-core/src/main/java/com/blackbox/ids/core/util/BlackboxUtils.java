package com.blackbox.ids.core.util;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dto.correspondence.xmlmapping.ApplicationCorrespondenceData;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.exception.WebCrawlerGenericException;

/**
 * Blackbox common utility class for collections.
 *
 * @author abhay2566
 */
public class BlackboxUtils {

	/**
	 * <code>BlackboxUtils</code> is a utility class which must not be instantiated. Use static methods of class to
	 * perform operations.
	 *
	 * @throws InstantiationException
	 *             if constructor is called for instantiation
	 */
	private BlackboxUtils() throws InstantiationException {
		throw new InstantiationException("Static class: must not be instantiated.");
	}

    /**
     * This method will concatenate all the string passed in parameters and crate a single string and send back as
     * response.
     *
     * @param strings
     *            the strings
     * @return the string
     */
    public static String concat(final String... strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            sb.append(strings[i]);
        }
        return sb.toString();
    }

    /**
     * This method will convert Date object to Calender.
     *
     * @param Date
     * @return Calendar
     */
    public static Calendar convertToCalendar(final Date date) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	return cal;
    }
    
	/**
	 * Gets the formatted application number from xml.
	 *
	 * @param applicationCorrespondenceData the application correspondence data
	 * @return the formatted application number from xml
	 */
	public static String getFormattedApplicationNumberFromXML(final ApplicationCorrespondenceData applicationCorrespondenceData) {
		String usPTOApplicationNumberRaw = applicationCorrespondenceData.getApplicationNumber();
		String usPTOApplicationNumberFormatted = getFormattedApplicationNumber(BlackboxDateUtil.strToCalendar(applicationCorrespondenceData.getFilingDate(),
				TimestampFormat.yyyyMMdd),
				usPTOApplicationNumberRaw);
		return usPTOApplicationNumberFormatted;
	}
	/**
	 * Gets the formatted application number.
	 *
	 * @param applicationCorrespondenceData the application correspondence data
	 * @param usPTOApplicationNumberRaw the us pto application number raw
	 * @return the formatted application number
	 */
	public static String getFormattedApplicationNumber(
			final Calendar filingDate, String usPTOApplicationNumberRaw) {
		String jurisdictionCode = usPTOApplicationNumberRaw.startsWith(Constant.WO_CODE_INDICATOR)
				? Constant.WO_CODE
				: Constant.US_CODE;
		
		String usPTOApplicationNumberFormatted = BlackboxUtils.formatApplicationNumber(usPTOApplicationNumberRaw, jurisdictionCode, filingDate);
		return usPTOApplicationNumberFormatted;
	}
	
	/**
	 * Formats application number on the basis of jurisdiction code and filling date. This will format application
	 * number with jurisdiction code 'US' or 'WO' and will ignore others.
	 * 
	 * @param applicationNumber
	 *            application number to format
	 * @param jurisdiction
	 *            jurisdiction code
	 * @param filingDate
	 *            filing date of the application
	 * @return converted application number
	 */
	public static String formatApplicationNumber(String applicationNumber, String jurisdiction, Calendar filingDate) {
		if (Constant.US_CODE.equals(jurisdiction)) {

		} else if (Constant.WO_CODE.equals(jurisdiction)) {

		}
		String convertedApplicationNumber = null;
		switch (jurisdiction) {
		case Constant.US_CODE:
			convertedApplicationNumber = StringUtils.replace(applicationNumber, Constant.FRONT_SLASH, Constant.EMPTY_STRING);
			convertedApplicationNumber = StringUtils.replace(convertedApplicationNumber,Constant.COMMA_STRING,Constant.EMPTY_STRING);
			break;
		case Constant.WO_CODE:
			convertedApplicationNumber = applicationNumber;
			if (RegexValidatorUtil.matches(applicationNumber, Constant.PARTIAL_FORMAT_REGEX)) {
				convertedApplicationNumber = changeFormat(applicationNumber, true, filingDate);
			} else if (RegexValidatorUtil.matches(applicationNumber, Constant.COMPLETE_FORMAT_REGEX)) {
				convertedApplicationNumber = applicationNumber;
			} else {
				String errorMessage = MessageFormat.format("Application number [{0}] is invalid. Throwing an excption.",applicationNumber);
				throw new WebCrawlerGenericException(errorMessage);
			}
			break;
		default:
			break;
		}
		return convertedApplicationNumber;
	}
	
	
	/**
	 * Change to complete format.
	 *
	 * @param applicationNumber
	 *            the application number
	 * @param yearPrefix
	 *            the year prefix
	 * @return the string
	 */
	public static String changeToCompleteFormat(String applicationNumber, String yearPrefix) {
		String applicationNumberArray[] = applicationNumber.split(Constant.FRONT_SLASH);
		String ccAndYear = applicationNumberArray[1];
		String number = applicationNumberArray[2];
		String cc = ccAndYear.substring(0, 2);
		String year = ccAndYear.substring(2, ccAndYear.length());
		int leadingZeroesToBeAdded = 6 - number.length();
		String leadingZeroString = Constant.EMPTY_STRING;
		for (int i = 0; i < leadingZeroesToBeAdded; i++) {
			leadingZeroString = BlackboxUtils.concat(leadingZeroString, Constant.ZERO_STRING);
		}

		ccAndYear = (ccAndYear.length() != 6) ? BlackboxUtils.concat(cc, yearPrefix, year) : ccAndYear;
		number = (number.length() != 6) ? BlackboxUtils.concat(leadingZeroString, number) : number;
		return BlackboxUtils.concat(Constant.WO_CODE_INDICATOR, Constant.FRONT_SLASH, ccAndYear,
				Constant.FRONT_SLASH, number);
	}

	/**
	 * Change format.
	 *
	 * @param applicationNumber            the application number
	 * @param toBeChangedInCompleteFormat            the to be changed in complete format
	 * @param filingDate the filing date
	 * @return the string
	 */
	public static String changeFormat(String applicationNumber, boolean toBeChangedInCompleteFormat,
			Calendar filingDate) {
		String otherFormatApplicationNumber = Constant.EMPTY_STRING;
		if (!toBeChangedInCompleteFormat) {
			// change to partial format.
			otherFormatApplicationNumber = changeToPartialFormat(applicationNumber);
		} else {
			// Change to complete format.
			String dateStr = Constant.EMPTY_STRING;
			String centuryPrefix = Constant.EMPTY_STRING;
			dateStr = BlackboxDateUtil.calendarToStr(filingDate, TimestampFormat.yyyyMMdd);
			centuryPrefix = dateStr.split(Constant.HYPHEN_STRING)[0].substring(0, 2);
			otherFormatApplicationNumber = changeToCompleteFormat(applicationNumber, centuryPrefix);
		}
		return otherFormatApplicationNumber;
	}

	/**
	 * Change to partial format.
	 *
	 * @param applicationNumber            the application number
	 * @return the string
	 */
	private static String changeToPartialFormat(String applicationNumber) {
		String applicationNumberArray[] = applicationNumber.split(Constant.FRONT_SLASH);
		String ccAndYear = applicationNumberArray[1];
		String number = applicationNumberArray[2];
		String cc = ccAndYear.substring(0, 2);
		String year = ccAndYear.substring(4, ccAndYear.length());
		return BlackboxUtils.concat(Constant.WO_CODE_INDICATOR, Constant.FRONT_SLASH, cc, year,
				Constant.FRONT_SLASH, Integer.valueOf(number).toString());
	}

}
