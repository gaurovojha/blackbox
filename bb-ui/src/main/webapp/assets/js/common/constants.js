/** A contact object contains all the contacts specific to the client-side. */
(function( $ ) {
	
	$.blackbox.Constant = {
			
			/** Prefix for error messages, related to <tt>null</tt> or empty fields. */
			PREFIX_ERROR_EMPTY : 'error.empty.',

			/** Prefix for error messages, related to max allowed length violation. */
			PREFIX_ERROR_LENGTH : 'error.length.',

			/** Prefix for error messages, related to format/regex violation. */
			PREFIX_ERROR_FORMAT : 'error.format.',
			
			/** Only alphabets with white spaces. */
			REGEX_ALPHA_WITH_SPACES : new RegExp('^[a-zA-z ]+$'),

			/** Only alphabets with white spaces and Slash. */
			REGEX_ALPHA_WITH_SPACES_SLASH : new RegExp('^[a-zA-z// ]+$'),

			/** Not null numeric string without white spaces. */
			REGEX_NUMERIC	: new RegExp('^[\\s]*[0-9]+[\\s]*$'),
			
			/** Not null real/fractional numeric string without white space. */
			REGEX_REAL_NUMBER : new RegExp('^((\\d+)|(\\d*.\\d+)|(\\d+.\\d+))$'),
			
			/** Regex that allow alphabets spaces and hyphen. */
			REGEX_USER_NAME : new RegExp("^[ A-Za-z-]*$"),

			/** A well formatted email address. */
			REGEX_EMAIL	: new RegExp('^[\\s]*[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}[\\s]*$'),

			/** Only alpha numeric characters with spaces. */
			REGEX_ALHANUMERIC_WITH_SPACES : new RegExp('^[A-Za-z0-9 ]+$'),
			
			/** Only numeric characters without spaces. */
			REGEX_ALPHA_NUMERIC : new RegExp('^[\\s]*[A-Za-z0-9]+[\\s]*$'),

			/** only alphabets characters */
			REGEX_ALPHA : new RegExp('^[a-zA-Z]+$'),
			
			/** jQuery selector for error field. */
			SELECTOR_ERROR : '.error',
			
	};
	
}) ( jQuery );