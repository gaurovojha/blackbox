/**
 *
 */
package com.blackbox.ids.dto.ids.sb08.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author ajay2258
 *
 */
public class BooleanAdapter extends XmlAdapter<String, Boolean> {

	private final static String FLAG_TRUE = "yes";
	private final static String FLAG_FALSE = "no";

	@Override
	public Boolean unmarshal(String v) throws Exception {
		return FLAG_TRUE.equalsIgnoreCase(v);
	}

	@Override
	public String marshal(Boolean v) throws Exception {
		return (v != null && v) ? FLAG_TRUE : FLAG_FALSE;
	}

}
