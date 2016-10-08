package com.blackbox.ids.ui.validation.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;


/**
 * A util file that provides method to validate form data against various checks.
 *
 * @author ajay2258
 */
public class ValidationUtil {

	/** A util class. Can't be instantiated publicly.
	 * @throws InstantiationException */
	private ValidationUtil() throws InstantiationException {
		throw new InstantiationException("Can't construct a static class instance.");
	}

	/**
	 * This utility method is used to validate a form field against following checks <br/>
	 * 1. <b>Null or empty check</b>: Checks whether the passed {@code val} is null or empty.<br/>
	 * 2. <b>Regex check</b>: Validate whether the {@code val} matches the required regex or not.<br/>
	 * 3. <b>Max length check</b>: Validate {@code val} length against allowed max length.<br/>
	 *
	 * <p>
	 * Regex and max length checks are implemented as optional. These validations are executed only if non-null
	 * parameters are passed in correspondence to the respective parameters.
	 * </p>
	 *
	 * <p>
	 * Error messages are fetched from the <code>.properties</code> files. The key for checks is supposed to be in
	 * following format:
	 * <table border="1">
	 * <tr>
	 * <th>Validation check</th>
	 * <th>Error message key format</th>
	 * </tr>
	 * <tr>
	 * <td>Null or empty</td>
	 * <td>error.<b>empty</b>.<u>messageKey</u></td>
	 * </tr>
	 * <tr>
	 * <td>Regex</td>
	 * <td>error.<b>format</b>.<u>messageKey</u></td>
	 * </tr>
	 * <tr>
	 * <td>Max length</td>
	 * <td>error.<b>length</b>.<u>messageKey</u></td>
	 * </tr>
	 * </table>
	 * </p>
	 *
	 * @param errors
	 *            The Errors instance for binding error results.
	 * @param val
	 *            Value to apply validation checks on.
	 * @param path
	 *            field name. Specifies the attribute path in data model hierarchy. For eg.
	 *            <code>userDTO.username</code>
	 * @param messageKey
	 *            The key for error message, specified in <code>.properties</code> file.
	 * @param checkNullOrEmpty
	 *            Specifies whether to perform null or empty check on {@code val} or not.
	 * @param regex
	 *            Regular expression to be matched with the {@code val}.
	 * @param maxLength
	 *            Maximum allowed length for the field value.
	 */
	public static void validateString(final ValidationProp prop, final Errors errors, final String val, final String path,
			final String messageKey, final boolean checkNullOrEmpty, final String regex, final Integer maxLength) {

		// Validate mandatory fields
		if (Boolean.TRUE.equals(checkNullOrEmpty) && (StringUtils.isBlank(val))) {
			errors.rejectValue(path, prop.prefixErrorEmpty() + messageKey);
			/* Skip further validation checks and return. */
			return;
		}

		// Validate field value for desired format.
		if (StringUtils.isNotBlank(val) && RegexValidator.fails(val, regex)) {
			errors.rejectValue(path, prop.prefixErrorFormat() + messageKey);
		}

		// Continue validation for non-mandatory fields, for eg. middle name.
		if ((val != null) && (maxLength != null) && (val.trim().length() > maxLength)) {
			errors.rejectValue(path, prop.prefixErrorLength() + messageKey, new Integer[] { maxLength }, null);
		}

	}

	/**
	 * This utility method validates if a string contains numeric value or not. Also rejects fields for null or empty
	 * value.
	 *
	 * @param errors
	 *            The {@link Errors} instance for binding error results.
	 * @param fieldValue
	 *            Value to apply validation checks on.
	 * @param path
	 *            field name. Specifies the attribute path in data model hierarchy. For eg.
	 *            <code>userDTO.username</code>
	 * @param messageKey
	 *            The key for error message, specified in <code>.properties</code> file.
	 * @param integer
	 *            A flag to specify whether <tt>fieldValue</tt> is supposed to contain an integer value or not.
	 * @param maxLength
	 *            Maximum allowed length for the <tt>fieldValue</tt>.
	 */
	public static void validateNumericString(final ValidationProp prop, final Errors errors, final String fieldValue, final String path,
			final String messageKey, final boolean integer, final Integer maxLength) {
		if (StringUtils.isNotEmpty(fieldValue)) {
			try {
				final Double value = Double.parseDouble(fieldValue);
				if (maxLength != null && maxLength < fieldValue.length()) {
					errors.rejectValue(path, prop.prefixErrorEmpty() + messageKey, new Integer[] { maxLength }, null);
				}
				if (integer && ((value != Math.floor(value)) || Double.isInfinite(value))) {
					errors.rejectValue(path, prop.prefixErrorFormat() + messageKey);
				}
			} catch (NumberFormatException e) {
				errors.rejectValue(path, prop.prefixErrorFormat() + messageKey);
			}
		} else {
			errors.rejectValue(path, prop.prefixErrorEmpty() + messageKey);
		}

	}

}
