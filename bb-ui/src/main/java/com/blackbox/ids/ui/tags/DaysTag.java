package com.blackbox.ids.ui.tags;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;

/**
 * The {@link DaysTag} provides easy customization for date value display.
 *
 * @author shikha
 */
public class DaysTag extends BaseTag {
	
	/** The serial version UID. */
	private static final long serialVersionUID = -3513593247273853453L;

	private static final String DAYS = "days";
	private static final Logger log = Logger.getLogger(DaysTag.class);

	private Date date;

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
			output = BlackboxUtils.concat(String.valueOf(daysDiff),Constant.SPACE_STRING, DAYS);
		}

		try {
            this.pageContext.getOut().println(output);
        } catch (IOException ioEx) {
        	log.error("[DaysTag]: IOException while computing output text.", ioEx);
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

}
