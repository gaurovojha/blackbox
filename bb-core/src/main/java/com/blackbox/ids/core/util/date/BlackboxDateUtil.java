package com.blackbox.ids.core.util.date;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.util.StringUtils;

import com.blackbox.ids.core.ApplicationException;

/**
 * Utility class containing common utility methods for date and time.
 *
 * @author ajay2258
 */
public final class BlackboxDateUtil {

	/** The Constant MS_IN_DAY represents Milliseconds in a day. */
	private static final int MS_IN_DAY = 24 * 60 * 60 * 1000;

	public static final String DATE_FORMAT = "MM-dd-yyyy";

	/**
	 * <code>BlackboxDateUtil</code> is a utility class which must not be
	 * instantiated. Use static methods of class to perform operations.
	 *
	 * @throws InstantiationException
	 *             if constructor is called for instantiation
	 */
	private BlackboxDateUtil() throws InstantiationException {
		throw new InstantiationException("Utility class: can't instantiate.");
	}

	/**
	 *
	 * Converts a given string to calendar object.
	 *
	 * @param date
	 *            the String representation of date.
	 * @param dateFormat
	 *            the {@link TimestampFormat} object
	 * @return {@link Calendar}
	 */

	public static Calendar strToCalendar(final String date, final TimestampFormat dateFormat) {
		if (StringUtils.isEmpty(date) || dateFormat == null) {
			throw new IllegalArgumentException("String to Date conversion: Parameters must not be null.");
		}
		Date utilDate = BlackboxDateUtil.strToDate(date, dateFormat.getDateFormat());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(utilDate);
		return calendar;
	}

	/**
	 * Converts a given string to date object.
	 *
	 * @param date
	 *            the String representation of date.
	 * @param dateFormat
	 *            the {@link TimestampFormat} object
	 * @return {@link Date}
	 *
	 */
	public static Date strToDate(final String date, final TimestampFormat dateFormat) {
		if (StringUtils.isEmpty(date) || dateFormat == null) {
			throw new IllegalArgumentException("String to Date conversion: Parameters must not be null.");
		}
		return BlackboxDateUtil.strToDate(date, dateFormat.getDateFormat());
	}

	/**
	 * Converts a given string to date object.
	 *
	 * @param date
	 *            the String representation of date.
	 * @param dateFormat
	 *            the {@link java.text.DateFormat} object
	 * @return {@link Date}
	 *
	 */
	private static Date strToDate(final String date, final DateFormat dateFormat) {
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Failed to parse date in given format.", e);
		}
	}

	/**
	 * Converts a date to desired string representation.
	 *
	 * @param date
	 *            the {@link Date}
	 * @param dateFormat
	 *            the {@link TimestampFormat} object
	 * @return {@link String} representation of date
	 */
	public static String dateToStr(final Date date, final TimestampFormat dateFormat) {
		if (date == null || dateFormat == null) {
			// throw new IllegalArgumentException("Date to String conversion:
			// Parameters must not be null.");
			return null;
		}

		return BlackboxDateUtil.dateToStr(date, dateFormat.getDateFormat());
	}

	/**
	 * Converts the calendar to desired string representation.
	 *
	 * @param calendar
	 *            Calendar containing the date
	 * @param dateFormat
	 *            desired date format
	 * @return String representation of calendar date
	 */
	public static String calendarToStr(final Calendar calendar, final TimestampFormat dateFormat) {
		if (calendar == null || dateFormat == null) {
			throw new IllegalArgumentException("Calendar to String conversion: Parameters must not be null.");
		}
		return BlackboxDateUtil.dateToStr(calendar.getTime(), dateFormat.getDateFormat());
	}

	/**
	 * Converts a date to desired string representation.
	 *
	 * @param date
	 *            the {@link Date}
	 * @param dateFormat
	 *            the {@link java.text.DateFormat} object
	 * @return {@link String} representation of date
	 */
	private static String dateToStr(final Date date, final DateFormat dateFormat) {
		return dateFormat.format(date);
	}

	/**
	 * Changes date from one format to another.
	 *
	 * @param date
	 *            the date
	 * @param oldDateFormat
	 *            the format of date provided
	 * @param newDateFormat
	 *            the new format
	 * @return date in new format
	 *
	 */
	public static String changeFormat(final String date, final TimestampFormat oldDateFormat,
			final TimestampFormat newDateFormat) {
		Date d = BlackboxDateUtil.strToDate(date, oldDateFormat);
		String newDate = BlackboxDateUtil.dateToStr(d, newDateFormat);
		return newDate;
	}

	/**
	 * Creates an equivalent {@link Calendar} object.
	 *
	 * @param date
	 *            the date
	 * @return calendar object
	 */
	public static Calendar toCalendar(final Date date) {
		Calendar cal = null;
		if (date != null) {
			cal = Calendar.getInstance();
			cal.setTime(date);
		}
		return cal;
	}

	/**
	 * Casts <tt>java.sql.timestamp</tt> to calendar.
	 *
	 * @param timestamp
	 *            the SQL timestamp
	 * @return calendar object
	 */
	public static Calendar toCalendar(final Timestamp timestamp) {
		Calendar calendar = null;
		if (timestamp != null) {
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(timestamp.getTime());
		}
		return calendar;
	}

	/**
	 * The method returns the {@link Calendar} object for the given date in
	 * given timestamp format.
	 *
	 * @param strDate
	 *            Date to be converted
	 * @param dateFormat
	 *            format in which time is to be converted
	 * @return calendar object
	 */
	public static Calendar toCalendar(final String strDate, final TimestampFormat dateFormat) {
		Date date = BlackboxDateUtil.strToDate(strDate, dateFormat);
		return BlackboxDateUtil.toCalendar(date);
	}

	/**
	 * Gets string representation of current date in given format.
	 *
	 * @param dateFormat
	 *            the required {@link TimestampFormat}
	 * @return date in desired format.
	 */
	public static String getCurrentDate(final TimestampFormat dateFormat) {
		Calendar cal = Calendar.getInstance();
		return BlackboxDateUtil.dateToStr(cal.getTime(), dateFormat);
	}

	/**
	 * Returns days difference between two dates.
	 *
	 * @param earlierDate
	 *            First date
	 * @param laterDate
	 *            Second data
	 * @return Difference of days between given dates.
	 */
	public static Integer daysDiff(final Date earlierDate, final Date laterDate) {
		if (earlierDate == null || laterDate == null) {
			return null;
		}

		Calendar day1 = Calendar.getInstance();
		day1.setTime(earlierDate);

		Calendar day2 = Calendar.getInstance();
		day2.setTime(laterDate);

		BlackboxDateUtil.initTime(day1);
		BlackboxDateUtil.initTime(day2);

		return (int) ((day2.getTime().getTime() - day1.getTime().getTime()) / MS_IN_DAY);
	}

	/**
	 * Initializes time to 00:00.
	 *
	 * @param calendar
	 *            Object to perform operation
	 */
	public static void initTime(final Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * Initializes time to 23:59:59.999.
	 *
	 * @param calendar
	 *            Object to perform operation
	 */
	public static void initLastTime(final Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
	}

	/**
	 * Gets the date that is before days provided.
	 *
	 * @param days
	 *            the days before
	 * @param resetTime
	 *            if true then time will also be reset to 00:00.
	 * @return the date before days
	 */
	public static Calendar getDateBeforeDays(int days, boolean resetTime) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -days);
		if (resetTime) {
			BlackboxDateUtil.initTime(cal);
		}
		return cal;
	}

	/**
	 * add days to date.
	 *
	 * @param date
	 *            the date
	 * @param days
	 *            the days
	 * @return the date after days
	 */
	public static Date getDateAfterDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);

		return cal.getTime();
	}

	/**
	 * Adds or subtracts the specified amount of days to the given calendar date
	 * field, based on the calendar's rules.
	 *
	 * @param calendar
	 *            Calendar whose after date is to be calculated
	 * @param numDays
	 *            Number of days to be added or subtracted
	 * @return New calendar after specified number of days.
	 */
	public static Calendar dateAfterDays(Calendar calendar, int numDays) {
		Calendar newCalendar = (Calendar) calendar.clone();
		newCalendar.add(Calendar.DAY_OF_MONTH, numDays);
		return newCalendar;
	}

	/**
	 * This method id used to compare two Calendar dates.
	 *
	 * @param c1
	 *            The date d1
	 * @param c2
	 *            The date d2
	 * @return Result is true if d1 < d2
	 */
	public static boolean compareDates(final Calendar c1, final Calendar c2) {
		return c1 == null || c2 == null ? false : c1.before(c2);
	}

	/**
	 * The method prepares a new calendar instance with time (in given calendar)
	 * adjusted according to the given timezone. Basically it create a new
	 * calendar in specified timezone, set to this date and add the offset.
	 *
	 * @param date
	 *            Date, which is to be updated
	 * @param timeZone
	 *            the target timezone
	 * @return Date, with time set as per the given timezone
	 */
	public static Date convertToTimezone(Date date, TimeZone timeZone) {
		if (date == null || timeZone == null) {
			throw new IllegalArgumentException(
					"Convert calendar's timezone: Either given calender or timezone instance is null.");
		}

		// Step 1. Convert given date in UTC timezone
		Date utcDate = toUtcDate(date);

		// Step 2. Create a new calendar in specified timezone, set to this date
		// and add the offset
		Calendar tzCal = Calendar.getInstance(timeZone);
		tzCal.setTime(utcDate);
		tzCal.add(Calendar.MILLISECOND, tzCal.getTimeZone().getOffset(utcDate.getTime()));

		return tzCal.getTime();
	}

	/**
	 * The method updates the given date time to UTC time using
	 * {@link DateFormat}.
	 *
	 * @param date
	 *            Date, in local timezone
	 * @return Date, with time update to <code>UTC</code> timezone.
	 */
	public static Date toUtcDate(Date date) {
		final String fullDateFormat = "yyyy-MMM-dd HH:mm:ss";
		SimpleDateFormat dateFormatUTC = new SimpleDateFormat(fullDateFormat);
		dateFormatUTC.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

		// Local time zone
		SimpleDateFormat dateFormatLocal = new SimpleDateFormat(fullDateFormat);
		try {
			return dateFormatLocal.parse(dateFormatUTC.format(date));
		} catch (ParseException e) {
			// Since using same date format, won't get parse exception.
			return date;
		}
	}

	public static boolean isPastDate(final Date date) {
		if (date == null) {
			throw new IllegalArgumentException("Date must not be null.");
		}
		if (date.equals(new Date()) || new Date().after(date)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the String from date in specified input format.
	 *
	 * @param inputDate
	 *            the input date
	 * @param dateFormat
	 *            the date format
	 * @return the date.
	 */
	public static String getStringFromDate(final Date inputDate, final String dateFormat) {

		DateFormat format = new SimpleDateFormat(dateFormat);

		return format.format(inputDate);
	}

	/**
	 * Create a Calendar instance using the specified date in specified format.
	 *
	 * @param dateString
	 *            the date string
	 * @param dateFormat
	 *            the date format
	 * @return appropriate Calendar object
	 */
	public static Calendar getCalendar(final String dateString, final String dateFormat) {
		Calendar calDate = null;
		if (!StringUtils.isEmpty(dateString)) {
			Date date = BlackboxDateUtil.strToDate(dateString, getSpecificDateFormat(dateFormat));
			calDate = Calendar.getInstance();
			calDate.setTime(date);
		}
		return calDate;
	}

	/**
	 * Null safe operation to convert date text to calendar object
	 * <p>
	 * Create a Calendar instance using the specified date with default
	 * format(MM-dd-yyyy).
	 * </p>
	 *
	 * @param dateString
	 *            the date string
	 * @return Calendar object
	 */
	public static Calendar getCalendar(final String dateString) {
		Calendar calDate = null;
		if (!StringUtils.isEmpty(dateString)) {
			Date date = BlackboxDateUtil.strToDate(dateString, getSpecificDateFormat(DATE_FORMAT));
			calDate = Calendar.getInstance();
			calDate.set(Calendar.HOUR, 0);
			calDate.set(Calendar.MINUTE, 0);
			calDate.set(Calendar.SECOND, 0);
			calDate.setTime(date);
		}
		return calDate;
	}

	/**
	 * Gets the specific date format.
	 *
	 * @param format
	 *            the format
	 * @return the specific date format
	 */
	public static DateFormat getSpecificDateFormat(final String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat;
	}

	/**
	 * This API changes the date format from one to another.
	 *
	 * @param inputFormat
	 *            the input format
	 * @param outputFormat
	 *            the output format
	 * @param inputDate
	 *            the input date
	 * @return the string
	 */
	public static String changeDateFormat(final String inputFormat, final String outputFormat, final String inputDate) {

		return getStringFromDate(parseDate(inputDate, inputFormat), outputFormat);
	}

	/**
	 * Parses the date.
	 *
	 * @param str
	 *            the str
	 * @param format
	 *            the format
	 * @return the date
	 */
	public static Date parseDate(final String str, final String format) {
		Date parsedDate = null;
		if (StringUtils.isEmpty(str)) {
			return null;
		}

		try {
			parsedDate = BlackboxDateUtil.getSpecificDateFormat(format).parse(str);
		} catch (ParseException e) {
			throw new ApplicationException(1, e.getMessage(), e);
		}
		return parsedDate;
	}

	/**
	 * Converts String to Date Range
	 *
	 * @param inputDate
	 *            as string
	 *
	 * @param dateFormat
	 *            as TimestampFormat
	 *
	 * @return Date array
	 */
	public static Date[] convertStringToDateRange(final String inputDate, final TimestampFormat dateFormat) {
		Date[] dates = new Date[2];
		String[] strDates = inputDate.split("-");
		dates[0] = strToDate(strDates[0].trim(), dateFormat);
		dates[1] = strToDate(strDates[1].trim(), dateFormat);
		return dates;
	}

	/**
	 * Converts a date to Calendar
	 *
	 * @param date
	 *            input date for conversion
	 *
	 * @return Calendar
	 *
	 */
	public static Calendar convertDateToCal(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static Calendar timeAfterHours(Calendar dateTime, int hours) {
		Calendar cal = (Calendar) dateTime.clone();
		cal.add(Calendar.HOUR_OF_DAY, hours);
		return cal;
	}
	
	public static String dateDiffInDays(Date date1 ,  Date date2) {
		Long days = 0L;
		    days = TimeUnit.DAYS.convert(date2.getTime() - date1.getTime() , TimeUnit.MILLISECONDS);
		    if(days>0) {
		    	return days.toString();
		    }
		return "";
	}

}
