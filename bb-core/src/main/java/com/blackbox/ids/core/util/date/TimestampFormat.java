package com.blackbox.ids.core.util.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
* Contains pre-defined date formats.
* 
* @author ajay2258
*/
public enum TimestampFormat {

   /** yyyy-MM-dd */
   DDMMM("ddMMM"),

   /** yyyy-MM-dd */
   YYYYMMDD("yyyyMMdd"),
   
   /** MMM-dd-yyyy*/
   MMMddyyyy("MMM-dd-yyyy"),
   
   /** yyyy-MM-dd */
   yyyyMMdd("yyyy-MM-dd"),
   
   /** yyyy-MM-dd */
   yyyyddMM("yyyy-dd-MM"),


   /** The yyyymmddhhmmss. */
   YYYYMMDDHHMMSS("yyyyMMdd hh:mm:ss"),

   /** The yyyymmddhhmm. */
   YYYYMMDDHHMM("yyyyMMddhhmm"),

   /** The ddmmyyhhmm. */
   DDMMYYHHMM("ddMMyyhhmm"),

   DDMMYYHHMMSS("ddMMyyHHmmss"),

   /** yyyy-MM-dd hh:mm:ss */
   FULL_DATE_TIME("yyyy-MM-dd hh:mm:ss"),

   /** Time format for reporting */
   DATE_REPORTING_TIME("dd-MM-yyyy hh:mm"),

   /** Time format for reporting */
   FULL_DATE_REPORTING_TIME("dd.MM.yyyy HH:mm:ss"),

   /** The dot ddmmmyyyy comma time. */
   DOT_DDMMMYYYY_COMMA_TIME("dd.MM.yyyy, HH:mm"),

   /** dd-MM-yyyy */
   DDMMYYYY("dd-MM-yyyy"),

   /** dd-MM-yyyy */
   DDmmmYYYY("dd-MMM-yyyy"),

   /** dd-MMM-yyyy */
   DDmmmYY("dd-MMM-yy"),

   /** dd.MM.yyyy */
   DDMMYYYY_DOT("dd.MM.yyyy"),

   /** MM/dd/yyyy */
   MMDDYYYY("MM/dd/yyyy"),

   /** dd MMM yyyy HH:mm */
   DDMMMYYYYHHMM("dd MMM yyyy HH:mm'h'"),

   /** dd MMM yyyy h:mm */
   DDMMMYYYYhMM("dd MMM yyyy h:mm a"),

   /** dd MMM yyyy h:mm */
   DDMMMYYYYhMMZ("dd MMM yyyy, HH:mm"),

   /** dd MMM yyyy */
   DDMMMYYYY("dd MMM yyyy"),

   /** The eee dd mmm yyyy. */
   EEEDDMMMYYYY("EEE, dd MMM yyyy"),

   /** The eee dd mmm yyyy. */
   EEEDDMMMYYYY2("EEE dd MMM yyyy"),

   /** The eeeddmmmyyyyhhmm. */
   EEEDDMMMYYYYHHMM("EEE, dd MMM yyyy h:mm a"),

   /** The ddMMyy. */
   DDMMYY("ddMMyy"),

   /** h:mm a time format for 12 hour clock */
   HMMA("h:mm a"),

   /** HH:mm'h' time format for 24 hour clock */
   HHMMh("HH:mm'h'"),

   /** 12 hours time format. */
   TIME_12H("hh:mm:ss a"),

   /** 24 hours time format. */
   TIME_24H("HH:mm:ss"),

   TIME_24("HHmmss"),

   YYYYMMDDHHMM_24H("yyyyMMddHHmm"),
	
	MMMDDYYYY("MMM dd, yyyy"),
	
	/** MMM-dd-yyyy */
	MM_DD_YYYY("MM-dd-yyyy");

   /** The value of the time stamp format. */
   private final String enumCode;

   /** The date format. */
   private DateFormat dateFormat;

   /**
    * Instantiates a new date format.
    * 
    * @param dateFormat
    *            the date format
    */
   private TimestampFormat(final String dateFormat) {
       this.dateFormat = new SimpleDateFormat(dateFormat);
       enumCode = dateFormat;
   }

   /**
    * 
    * @return the hierarchy level code.
    */
   public String value() {
       return enumCode;
   }

   /**
    * Returns the dateFormat
    * 
    * @return the dateFormat
    */
   public DateFormat getDateFormat() {
       return dateFormat;
   }

   /**
    * Returns the dateFormat after setting time zone
    * 
    * @param timeZone
    *            set time zone in the date format
    * @return the dateFormat
    */
   public DateFormat getDateFormat(final TimeZone timeZone) {
       dateFormat.setTimeZone(timeZone);
       return dateFormat;
   }
   
   public static TimestampFormat fromString(final String dateFormat)  {
	   TimestampFormat format = null;
	   for (final TimestampFormat e : TimestampFormat.values()) {
		   if (e.enumCode.equalsIgnoreCase(dateFormat)) {
			   format = e;
			   break;
		   }
	   }
	   return format;
   }

}
