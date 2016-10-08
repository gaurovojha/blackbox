/**
 *
 */
package com.blackbox.ids.ui.validation.common;

/**
 * @author ajay2258
 *
 */
public interface ValidationProp {

	/** Prefix for error messages, related to <tt>null</tt> or empty fields. */
	default String prefixErrorEmpty() {
        return "error.empty.";
    }

	/** Prefix for error messages, related to max allowed length violation. */
	default String prefixErrorLength() {
		return "error.length.";
	}

	/** Prefix for error messages, related to format/regex violation. */
	default String prefixErrorFormat() {
        return "error.format.";
    }

}
