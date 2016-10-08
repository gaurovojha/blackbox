/**
 *
 */
package com.blackbox.ids.ui.tags;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;

/**
 * The {@link DateTag} provides easy customization for date value display.
 *
 * @author ajay2258
 */
public class DateTag extends BaseTag {

	/** The serial version UID. */
	private static final long serialVersionUID = 2228126351135553978L;

	private Logger log = Logger.getLogger(DateTag.class);

	private Date date;

	private String dateFormat;

	@Override
	protected int doStartTagInternal() {
		String output = null;
		Integer diff = BlackboxDateUtil.daysDiff(date, new Date());
		int daysDiff = diff == null ? 0 : diff;

		if (daysDiff < 1) {
			output = "Today";
		} else if(daysDiff == 1) {
			output = "Yesterday";
		} else {
			TimestampFormat format = TimestampFormat.fromString(dateFormat);
			String strDate = BlackboxDateUtil.dateToStr(date, format);
			output = strDate == null ? "" : strDate;
		}

		try {
            this.pageContext.getOut().println(output);
        } catch (IOException ioEx) {
        	log.error("[DateTag]: IOException while computing output text.", ioEx);
        }
		return 0;
	}

	/*- ---------------------------- getter-setters -- */
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

}
