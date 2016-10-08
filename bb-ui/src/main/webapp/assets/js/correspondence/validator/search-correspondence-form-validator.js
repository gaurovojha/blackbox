/** A contact object contains all the contacts specific to the client-side. */
(function( $ ) {
	
	/**
	 * CROSS-BROWSER CONSTANTS
	 * 
	 *  Valid Usage: CONFIG.get('MY_CONST');					==>> Returns correct value
	 *  False Usage: CONFIG.MY_CONST = 'new value';				==>> No Impact
	 *  Invalid Usage: CONFIG.internal.MY_CONST = 'new value';	==>> Error
	 */
	var CONFIG = (function() {

		/** Represents client side validation constants object. */
		var constant = $.blackbox.Constant;
		
		var internal = {

				/* jQuery Id selectors and corresponding message keys for mandatory fields on user screen. */
				'MANDATORY_FIELDS'	: [
				                  	   /* Ids */
				                  	   ['applicationNumber', 'jurisdiction'],
				                  	    /* MessageKeys */
				                  	    ['correspondenceForm.applicationNumber', 'correspondenceForm.jurisdiction']
				                  	   ],
				                  	 /* jQuery Id selectors and corresponding message keys for fields with regex check on user screen. */                  	   
			     
				                  	   
				
		};
		
		return {
			get : function(name) {
				return internal[name];
			}
		};
	})();

	/**
	 * UserValidator is a client-side validator for the UserDTO.
	 *
	 * @returns {$.blackbox.validator.UserValidator}
	 * 				Instance of the userValidator.
	 */
	$.blackbox.validator.SearchCorrespondenceFormValidator = function() {
	};
	
	$.blackbox.validator.SearchCorrespondenceFormValidator.validate = function() {
		var constant = $.blackbox.Constant;
		var validationUtil = $.blackbox.util.ValidationUtil;
		var status = true;

		status = validationUtil.checkMandatoryFields(CONFIG.get('MANDATORY_FIELDS')[0],
				CONFIG.get('MANDATORY_FIELDS')[1],
				constant.PREFIX_ERROR_EMPTY);

		
		return status;
	};
	
}) ( jQuery );