package com.blackbox.ids.ui.validation.common;

/**
 * This class servers as a central container for constants to validate data inside various validation classes.
 * <p>
 * Constants specifying the maximum allowed length for fields are prefixed with <tt>MAX_LENGTH</tt>, whereas regular
 * expressions to match desired format are prefixed with <tt>REGEX</tt>.
 *
 * @author ajay2258
 */
public class ValidationConstant {

	/** Regex for matching alphabetic strings without white spaces. */
	public static final String REGEX_ALPHA = "^[A-Za-z]*$";

	/** Regex for matching non decimal numeric string. */
	public static final String REGEX_INTEGER = "^[+-]?[\\d]*$";

	/** Regex for matching decimal numbers. */
	public static final String REGEX_REAL = "^[+-]?[\\d]+[\\.]?[\\d]*$";

	 /** Regex for matching alphanumeric string. */
    public static final String REGEX_ALPHA_NUMERIC = "^[A-Za-z0-9]*$";

	/** Regex for matching alphanumeric strings with white spaces. */
    public static final String REGEX_ALPHA_NUMERIC_WITH_SPACE = "^[A-Za-z0-9 ]*$";

    /** Regex for matching whether string is a well formed email address or not. */
    public static final String REGEX_EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    /** Regex for name fields validation - alphabets with dot and hyphen. */
    public static final String REGEX_NAME = "^[\\p{L} .'-]+$";

    public static final String REGEX_JURISDICTION = "^[A-Za-z]{2}$";

    /** Utility class. Not permitted to be instantiated.
     * @throws InstantiationException */
    private ValidationConstant() throws InstantiationException {
    	throw new InstantiationException("Can't construct a static class instance.");
    }

}