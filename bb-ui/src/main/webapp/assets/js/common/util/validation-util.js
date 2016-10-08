/** A contact object contains all the contacts specific to the client-side. */
(function( $ ) {

	$.blackbox.util.ValidationUtil = function() {
	};

	$.blackbox.util.ValidationUtil = {
			
			/**
			 * This utility method checks whether all the required fields are filled or not.
			 * <p>If field is found containing null or empty value, an error message is displayed.</p>
			 * 
			 * @param mandatoryFields
			 * 				An array of jQuery ID selectors for all mandatory fields.
			 * @param messages
			 * 				An error message to show on validation failure.
			 * 
			 * @returns <code>false</code>, if any of the required fields is unfilled; <code>true</code> otherwise.
			 */
			checkRequiredFields : function(mandatoryFields, messages) {
				var status = true;
				var iter, fieldId;
				var $field = null;
				var $errorField = null;
				var selectorError = '.error';
				var numFields = mandatoryFields.length;

				for (iter = 0; iter < numFields; ++iter) {
					fieldId = mandatoryFields[iter];
					$field = $('#' + fieldId);
					$errorField = $field.parent('div').find(selectorError);
					
					if ($.trim($field.val()) === '') {
						if ($errorField.length === 0) {
							$field.parent('div').append('<span class="error">' + messages[iter] + '</span>');
						} else {
							$errorField.html(messages[iter]);
						}
						status = false;
					} else {
						$errorField.html('');
					}
				}

				return status;
			},

			/**
			 * This utility method checks whether all the required fields are filled or not.
			 * 
			 * <p>If field is found containing null or empty value, an error message is displayed.
			 * The error message is supposed to be associated with key <code>'error.empty.' + messageKeys[index]</code>
			 * inside the <code>.properties</code> files.
			 * </p>
			 * 
			 * @param mandatoryFields
			 * 				An array of jQuery ID selectors for all mandatory fields.
			 * @param messageKeys
			 * 				A postfix to read i18n messages from <code>.properties</code> files.
			 * @param keysPrefix
			 * 				An optional prefix for error messages's keys.
			 * 				This parameter need not to be passed if messageKeys array hold complete message keys.
			 * 
			 * @returns <code>false</code>, if any of the required fields is unfilled; <code>true</code> otherwise.
			 */
			checkMandatoryFields : function(mandatoryFields, messageKeys, keysPrefix) {
				var status = true;
				var iter, fieldId;
				var $field = null;
				var $errorField = null;
				var errorText = null;
				var selectorError = '.error';

				if (typeof keysPrefix === 'undefined') {
					keysPrefix = '';
				}
				var numFields = mandatoryFields.length;

				for (iter = 0; iter < numFields; ++iter) {
					fieldId = mandatoryFields[iter];
					$field = $('#' + fieldId);
					$errorField = $field.parent('div').find(selectorError);
					
					if ($.trim($field.val()) === '') {
						errorText = jQuery.i18n.prop(keysPrefix + messageKeys[iter]);
						if ($errorField.length === 0) {
							$field.parent('div').append('<span class="error">' + errorText + '</span>');
						} else {
							$errorField.html(errorText);
						}
						status = false;
					} else {
						$errorField.html('');
					}
				}

				return status;
			},

			/**
			 * This utility method checks whether all the specified fields are filled and satisfying the provided regex or not.
			 * 
			 * <b>USAGE SCENARIO:</b>
			 * 		error message keys are supposed to start with
			 * 			---------------------------------------------
			 * 			PREFIX					VALIDATION
			 * 			--------------------------------------------
			 * 			error.empty.			empty field
			 * 			error.format.			ill-formatted field
			 * 			--------------------------------------------
			 * 
			 * <p>Therefore, if field is found containing <tt>null</tt> or empty value, an error message associated with key <code>'error.empty.' + messageKeys[index]</code> is displayed.
			 * And if field is found containing some ill-formatted value, an error message associated with key <code>'error.empty.' + messageKeys[index]</code> is displayed
			 * </p>
			 * 
			 *@param formFields
			 * 				An array of jQuery ID selectors for all form fields the empty and regex check is to be applied on.
			 * @param regexes
			 * 				Regular expressions to be evaluated.
			 * 				Field and associated regex should be respective to each other is passed parameter arrays.
			 * @param messageKeys
			 * 				A postfix to read i18n messages from <code>.properties</code> files.
			 * @returns {Boolean}
			 * 				<code>false</code>, if any of the regex check fails; <code>true</code> otherwise.
			 */
			checkRegexedMandatoryFields : function(formFields, regexes, messageKeys) {
				var status = true;
				var iter, fieldId;
				var $parentBlock = null;

				var classBlock = CONFIG.get('CLASS_SELECTOR_BLOCK');
				var selectBlock = CONFIG.get('CLASS_SELECTOR_DROPDOWN');
				var classErrorHint = CONFIG.get('CLASS_SELECTOR_ERROR_HINT');
				var classErrorBlock = CONFIG.get('CLASS_NAME_ERROR_BLOCK');
				var classErrorBlockWorkSign = CONFIG.get('CLASS_NAME_ERROR_BLOCK_WORKSIGN');

				var prefixErrorEmpty = CONFIG.get('PREFIX_ERROR_EMPTY');
				var prefixErrorFormat = CONFIG.get('PREFIX_ERROR_FORMAT');
				var numFields = formFields.length;

				for (iter = 0; iter < numFields; ++iter) {
					fieldId = formFields[iter];
					var $field = $('#' + fieldId);
					$parentBlock = $field.parents(classBlock, selectBlock).first();

					/* Mandatory Check */
					if ($.trim($field.val()) === '') {
						status = false;
						$parentBlock.find(classErrorHint).html(jQuery.i18n.prop(prefixErrorEmpty + messageKeys[iter]) + '<br/>');						
						if(fieldId==='worksign'){		
							$parentBlock.removeClass(classErrorBlock);
							$parentBlock.addClass(classErrorBlockWorkSign);
							$('#worksign').parent('div').addClass('positionRel errorHint-container');
						}else{								
							$parentBlock.addClass(classErrorBlock);
						}
					} /* Regex Check */
					else if (regexes[iter].test($field.val()) === false) {
						status = false;
						$parentBlock.find(classErrorHint).html(jQuery.i18n.prop(prefixErrorFormat + messageKeys[iter]) + '<br/>');
						$parentBlock.addClass(classErrorBlock);
					} /* Validations Passed */
					else {
						if(fieldId==='worksign'){
							$parentBlock.removeClass(classErrorBlockWorkSign);
						}else{
							$parentBlock.removeClass(classErrorBlock);
						}

					}
				}

				return status;
			},

			/**
			 * This utility method checks whether all the specified fields are filled and satisfying the provided regex or not.
			 * 
			 * <p>If field is found containing <tt>null</tt> or empty value, an error message associated with key <code>messageKeysEmpty[index]</code> is displayed.
			 * And if field is found containing some ill-formatted value, an error message associated with key <code>messageKeysRegex[index]</code> is displayed
			 * </p>
			 * 
			 * @param formFields
			 * 			An array of jQuery ID selectors for all form fields the empty and regex check is to be applied on.
			 * @param regexes
			 * 			Regular expressions to be evaluated.
			 * 			Field and associated regex should be respective to each other is passed parameter arrays.
			 * @param messageKeysEmpty
			 * 			Key for error messages in <tt>.properties</tt> files, corresponding to the empty fields.
			 * @param messageKeysRegex
			 * 			Key for error messages in <tt>.properties</tt> files, corresponding to the ill-formatted fields.
			 * @returns {Boolean}
			 * 			<code>false</code>, if any of the regex check fails; <code>true</code> otherwise.
			 */
			checkMandatoryFeildsWithRegex : function(formFields, regexes, messageKeysEmpty, messageKeysRegex) {
				var status = true;
				var fieldId;
				var numFields = formFields.length;
				var $parentBlock = null;

				var classBlock = CONFIG.get('CLASS_SELECTOR_BLOCK');
				var selectBlock = CONFIG.get('CLASS_SELECTOR_DROPDOWN');
				var classErrorHint = CONFIG.get('CLASS_SELECTOR_ERROR_HINT');
				var classErrorBlock = CONFIG.get('CLASS_NAME_ERROR_BLOCK');

				for (var iter = 0; iter < numFields; ++iter) {
					fieldId = formFields[iter];
					var $field = $('#' + fieldId);
					$parentBlock = $field.parents(classBlock, selectBlock);

					/* Mandatory Check */
					if ($.trim($field.val()) === '') {
						status = false;
						$parentBlock.find(classErrorHint).html(jQuery.i18n.prop(messageKeysEmpty[iter]) + '<br/>');
						$parentBlock.addClass(classErrorBlock);
					} /* Regex Check */
					else if (regexes[iter].test($field.val()) === false) {
						status = false;
						$parentBlock.find(classErrorHint).html(jQuery.i18n.prop(messageKeysRegex[iter]) + '<br/>');
						$parentBlock.addClass(classErrorBlock);
					} /* Validations Passed */
					else {
						$parentBlock.removeClass(classErrorBlock);
					}
				}

				return status;
			},

	};

}) ( jQuery );