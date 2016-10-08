package com.blackbox.ids.ui.util;

import org.springframework.util.StringUtils;

/**
 * Utility class required during validation
 * 
 */
public final class NumberUtil {

	// Suppresses default constructor, ensuring non-instantiability.
	private NumberUtil() {
	}

	/**
	 * Validates the input so as to fit within the prescribed range.
	 * 
	 * @param value {@link Float} entered by the user.
	 * @param minimumValue {@link Float} prescribed starting value for variable.
	 * @param maximumValue {@link Float} prescribed ending value for variable.
	 * @return <code>TRUE</code> if is in Range and <code>FALSE</code> if not.
	 */
	public static boolean isValueInRange(final Float value, final Float minimumValue, final Float maximumValue) {
		boolean isValueInRange = false;
		if (value != null && minimumValue != null && maximumValue != null) {
			isValueInRange = (value >= minimumValue && value <= maximumValue) ? true : false;
		}
		return isValueInRange;
	}

	/**
	 * Validates the input so as to fit within the prescribed range. For Eg. confidence should lie between 0 to 100%
	 * 
	 * @param value {@link Double} entered by the user.
	 * @param minimumValue {@link Double} prescribed starting value for variable.
	 * @param maximumValue {@link Double} prescribed ending value for variable.
	 * @return <code>TRUE</code> if is in Range and <code>FALSE</code> if not.
	 */
	public static boolean isValueInRange(final Double value, final Double minimumValue, final Double maximumValue) {
		boolean isValueInRange = false;
		if (value != null && minimumValue != null && maximumValue != null) {
			isValueInRange = (value >= minimumValue && value <= maximumValue) ? true : false;
		}
		return isValueInRange;
	}

	/**
	 * Checks if the provided String input can be parsed into a valid number.
	 * 
	 * @param value {@link String} numeric input provided from user in form of String.
	 * @param isClass {@link Class} is the class against which values is to be verified.
	 * @return <code>TRUE</code> if valid numerical value and <code>FALSE</code> if not.
	 */
	public static boolean isValidNumericalValue(final String value, final Class<?> isClass) {
		boolean isValidNumber = false;
		if (!StringUtils.isEmpty(value)) {
			try {
				if (isClass == Float.class) {
					Float.valueOf(value);
				}
				if (isClass == Double.class) {
					Double.valueOf(value);
				}
				if (isClass == Integer.class) {
					Integer.valueOf(value);
				}
				if(isClass == Long.class){
					Long.valueOf(value);
				}
				isValidNumber = true;
			} catch (final NumberFormatException exception) {
				isValidNumber = false;
			}
		}
		return isValidNumber;
	}

	/**
	 * Checks if the input is a valid positive numerical value.
	 * 
	 * @param value {@link String} numeric input provided from user in form of String.
	 * @param isClass {@link Class} is the class against which values is to be verified.
	 * @return <code>TRUE</code> if valid positive numerical value and <code>FALSE</code> if not.
	 */
	public static boolean isValidPositiveValue(final String value, final Class<?> isClass) {
		boolean isPositiveNumber = false;
		// Check if it is a valid number.
		if (isValidNumericalValue(value, isClass)) {
			if (Integer.valueOf(value) > 0) {
				isPositiveNumber = true;
			}
		}
		return isPositiveNumber;
	}

	/**
	 * Checks if the input is a valid non negative numerical value.
	 * 
	 * @param value {@link String} numeric input provided from user in form of String.
	 * @param isClass {@link Class} is the class against which values is to be verified.
	 * @return <code>TRUE</code> if valid non negative numerical value and <code>FALSE</code> if not.
	 */
	public static boolean isValidNonNegativeNumericalValue(final String value, final Class<?> isClass) {
		boolean isValidNumber = false;
		if (!StringUtils.isEmpty(value)) {
			try {
				if ((isClass == Float.class && Float.compare(Float.parseFloat(value), 0.00f) >= 0)
						|| (isClass == Double.class && Double.compare(Double.parseDouble(value), 0.00) >= 0)
						|| (isClass == Integer.class)) {
					isValidNumber = true;
				}
			} catch (final NumberFormatException exception) {
				isValidNumber = false;
			}
		}
		return isValidNumber;
	}

	/**
	 * Checks if the input is a valid positive numerical value in the given range.
	 * 
	 * @param value {@link String} numeric input provided from user in form of String.
	 * @param isClass {@link Class} is the class against which values is to be verified.
	 * @return <code>TRUE</code> if valid positive numerical value and <code>FALSE</code> if not.
	 */
	public static boolean isValidPositiveRangeValue(String value, final Class<?> isClass, int minValue, int maxValue) {
		// Check if it is a valid number.
		boolean isPositiveNumber = false;
		if (isValidNumericalValue(value, isClass)) {
			Integer number = Integer.valueOf(value);
			if (number > 0 && number >= minValue && number <= maxValue) {
				isPositiveNumber = true;
			}
		}
		return isPositiveNumber;
	}

	public static boolean isValueInRange(final Integer value, final Integer minimumValue, final Integer maximumValue) {
		boolean isValueInRange = false;
		if (value != null && minimumValue != null && maximumValue != null) {
			isValueInRange = (value >= minimumValue && value <= maximumValue) ? true : false;
		}
		return isValueInRange;
	}

}
