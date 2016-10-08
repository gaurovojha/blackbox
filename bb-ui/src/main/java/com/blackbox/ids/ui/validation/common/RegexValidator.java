package com.blackbox.ids.ui.validation.common;

/**
 * The class {@code RegexValidator} exposes APIs to validate whether a particular string value satisfies a regular 
 * expression or not. 
 * 
 * @author ajay2258
 */
public class RegexValidator {

	/** A util class. Can't be instantiated publicly. 
	 * @throws InstantiationException */
	private RegexValidator() throws InstantiationException {
		throw new InstantiationException("Can't construct a static class instance.");
	}
	
	/**
	 * Checks whether or not provided <tt>str</tt> matches the given regular expression. The API is {@code null} safe 
	 * and returns {@code false} if any of the parameters is found {@code null}.
	 * 
	 * @param str
	 *            the string value which is to be tested for regular expression.
	 * @param regex
	 *            the regular expression to which this string is to be matched
	 * @return {@code true} if given value matches the given regular expression, {@code false} otherwise.
	 */
	public static boolean matches(final String str, final String regex) {
		return (str != null) && (regex != null) && (str.matches(regex));
	}
	
	/** The method negates the behavior exposed by method {@link #matches(String, String)}. */
	public static boolean fails(final String str, final String regex) {
		return !matches(str, regex);
	}
	
}
