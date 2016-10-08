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
				'COMMON_FIELDS' : ['assignee', 'dpFilingDate', 'attorneyDocketNo', 'title'],

				/* jQuery Id selectors and corresponding message keys for mandatory fields on user screen. */
				'SCENARIO_FIELDS'	: {
					'US_FIRST_FILING' : 	['customerNo', 'entity', 'confirmationNo'],
					'WO_FIRST_FILING' : 	['customerNo'],
					'US_CHILD_FILING' : 	['customerNo', 'entity', 'confirmationNo'],
					'WO_SUBSEQUENT_FILING': ['customerNo'],
					'WO_FOREIGN_FILING':	[],
					'OTHER_FOREIGN_FILING':	[]
				},
				
				'IDS_APPLICATION' : ['dpFilingDate', 'attorneyDocketNo'],
		};
		
		return {
			get : function(name) {
				return internal[name];
			}
		};
	})();
	
	/**
	 * ApplicationValidator is a client-side validator for the application form.
	 *
	 * @returns {$.blackbox.validator.ApplicationValidator}
	 * 				Instance of the ApplicationValidator.
	 */
	$.blackbox.validator.ApplicationValidator = function() {
	};
	
	$.blackbox.validator.ApplicationValidator.prototype = {
			
			validApplicationInits : function() {
				var validationUtil = $.blackbox.util.ValidationUtil;
				var fields = ['jurisdiction', 'applicationNo', 'ddApplicationType'];
				return validationUtil.checkMandatoryFields(fields, fields, 'mdm.app.error.empty.');
			},
			
			validateApplicationDetails : function(parentDiv) {
				var scenario = $('#' + parentDiv + ' #scenario').val();
				var validationUtil = $.blackbox.util.ValidationUtil;
				
				var fields = $.merge(CONFIG.get('COMMON_FIELDS'), CONFIG.get('SCENARIO_FIELDS')[scenario]);
				var fieldsToValidate = [];
				
				$(fields).each(function(index, value) {
					fieldsToValidate.push(parentDiv + ' #' + value);
				})
				
				return validationUtil.checkMandatoryFields(fieldsToValidate, fields, 'mdm.app.error.empty.');
			},
			
			validateIDSApp : function() {
				var validationUtil = $.blackbox.util.ValidationUtil;
				var fields = CONFIG.get('IDS_APPLICATION');
				return validationUtil.checkMandatoryFields(fields, fields, 'ids.app.error.empty.');
			}
	}
	
}) ( jQuery );