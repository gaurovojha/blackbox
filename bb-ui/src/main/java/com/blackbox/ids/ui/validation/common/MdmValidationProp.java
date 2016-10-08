/**
 *
 */
package com.blackbox.ids.ui.validation.common;

/**
 * @author ajay2258
 */
public class MdmValidationProp implements ValidationProp {

	private static final String PREFIX_MDM = "mdm.app.";

	public static final MdmValidationProp INSTANCE = new MdmValidationProp();

	/** Prefix for error messages, related to <tt>null</tt> or empty fields. */
	@Override
	public String prefixErrorEmpty() {
		return PREFIX_MDM + ValidationProp.super.prefixErrorEmpty();
	}

	/** Prefix for error messages, related to max allowed length violation. */
	@Override
	public String prefixErrorLength() {
		return PREFIX_MDM + ValidationProp.super.prefixErrorLength();
	}

	/** Prefix for error messages, related to format/regex violation. */
	@Override
	public String prefixErrorFormat() {
		return PREFIX_MDM + ValidationProp.super.prefixErrorFormat();
	}

}
