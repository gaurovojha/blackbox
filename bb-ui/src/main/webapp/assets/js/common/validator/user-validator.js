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
				                  	   ['lastName', 'firstName', 'emailId', 'roleIds'],
				                  	    /* MessageKeys */
				                  	    ['userForm.lastName', 'userForm.firstName', 'userForm.emailId', 'userForm.accessRole']
				                  	   ],
				                  	   
				 /* jQuery Id selectors and corresponding message keys for fields with regex check on user screen. */                  	   
				'REGEXED_MANDATORY_FIELDS' : [
				                      /* Ids */
				                      ['firstName', 'lastName', 'adminEmail', 'adminPhoneNumber', 'userID', 'worksign'],
				                      /* Regex */
				                      [constant.REGEX_USER_NAME, constant.REGEX_USER_NAME,
					                     constant.REGEX_EMAIL, constant.REGEX_NUMERIC, constant.REGX_LH_USER_UNUMBER, constant.REGEX_WORKSIGN],
				                      /* MessageKeys */
				                      ['user.firstName', 'user.lastName', 'email', 'phoneNumber' ,'user.username', 'user.workSign']
				                     ],
				                     
				'REGEXED_OPTIONAL_FIELDS' : [
				                     /* Ids */
				                     ['middleName'],
				                     /* Regex */
				                     [constant.REGEX_USER_NAME],
				                     /* MessageKeys */
				                     ['user.middleName']
				                     ],

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
	$.blackbox.validator.UserValidator = function() {
	};
	
	$.blackbox.validator.UserValidator.validate = function() {
		var constant = $.blackbox.Constant;
		var validationUtil = $.blackbox.util.ValidationUtil;
		var status = true;

		status = validationUtil.checkMandatoryFields(CONFIG.get('MANDATORY_FIELDS')[0],
				CONFIG.get('MANDATORY_FIELDS')[1],
				constant.PREFIX_ERROR_EMPTY);

		/*-status = validationUtil.checkRegex(CONFIG.get('REGEXED_OPTIONAL_FIELDS')[0],
				CONFIG.get('REGEXED_OPTIONAL_FIELDS')[1],
				CONFIG.get('REGEXED_OPTIONAL_FIELDS')[2],
				constant.PREFIX_ERROR_FORMAT) && status;*/
		
		return status;
	};
	
}) ( jQuery );