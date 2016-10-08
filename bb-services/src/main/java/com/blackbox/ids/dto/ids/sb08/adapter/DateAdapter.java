/**
 *
 */
package com.blackbox.ids.dto.ids.sb08.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author ajay2258
 *
 */
public class DateAdapter extends XmlAdapter<String, Date> {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public String marshal(Date date) throws Exception {
		return dateFormat.format(date);
	}

	@Override
	public Date unmarshal(String strDate) throws Exception {
		return dateFormat.parse(strDate);
	}

}
