<script type="text/javascript" src="${contextPath}/assets/js/common/util/validation-util.js"></script>
<script type="text/javascript" src="${contextPath}/assets/js/common/validator/application-validator.js"></script>

<script type="text/javascript">
	var validationUtil = null;
	var appValidator = null;
	var editApplication = null;
	
	(function() {
		validationUtil = $.blackbox.util.ValidationUtil;
		appValidator = $.blackbox.validator.ApplicationValidator;
		try {
			$.blackbox.util.CommonUtil.loadResourceBundle(encodeURIComponent('i18n/mdm'));
		} catch(error) { console.log(error); }
		

		editApplication = $('#editApplication').val() === 'true';
		(editApplication || numApps() > 1) ? bindDetailsPageControls() : bindInitPageControls();
		autoCompleteFields();
		attorneyDocketNoFocusOut();
	})();

	/*----------------------------------------------------------------------------
	  									Form Submission
	------------------------------------------------------------------------------ */
	function processFormSubmission() {
		if (appValidator.prototype.validateApplicationDetails('application-content_' + numApps())) {
			submitApplicationForm($('form').attr('action'));
		}
	}
	

	function addAditionalApplication() {
		if (appValidator.prototype.validateApplicationDetails('application-content_' + numApps())) {
			var action = $('#contextPath').val() + '/mdm/createApp/anotherApp';
			submitApplicationForm($('#contextPath').val() + '/mdm/createApp/anotherApp');
		}
	}
	
	function numApps() {
		return $('.mdmApplication').length;
	}
	
	/*----------------------------------------------------------------------------
									  Application Initails
	------------------------------------------------------------------------------ */
	(function() {
		if ($('#appJurisdiction').val() !== '') {
			var jurisdiction = $('#appJurisdiction').val();
			$('#ddApplicationType >option').not('[class^="appType' + jurisdiction + '"]').attr('disabled', true);
		}
	})();
	
	function populateApplicationTypes($this) {
		var $appInits = $this.parents('.appInits');
		
		$ddOptions = $appInits.find('.ddApplicationType >option');
		$ddOptions.attr('disabled', false).hide();
		$appInits.find('.ddApplicationType').val('');
		var jurisdiction = $this.val().trim();
		
		if (jurisdiction.length === 2) {
			$ddOptions.removeAttr('disabled');
			switch (jurisdiction.toUpperCase()) {
			case 'US':
				$ddOptions.filter('.appTypeUS').removeAttr('disabled').show();
				$ddOptions.not('.appTypeUS').attr("disabled", "disabled");
				break;
				
			case 'WO':
				$ddOptions.filter('.appTypeWO').removeAttr('disabled').show();
				$ddOptions.not('.appTypeWO').attr("disabled", "disabled");
				break;
				
			default:
				$ddOptions.filter('.appTypeOther').removeAttr('disabled').show();
				$ddOptions.not('.appTypeOther').attr("disabled", "disabled");
				break;
			}
		}
	}
	
	function bindInitPageControls() {
		$('.jurisdiction').on('focusout', function() {
			var $this = $(this);
			if (enableAppIdFields()) {
				populateApplicationTypes($this);
			}
		});
		
		$('.jurisdiction').on('change', function() {
			$('.familySearch').hide();
		});
		
		$('.applicationInit').on('focusout', function() {
			var $appInits = $(this).parents('.appInits');

			if (enableAppIdFields() && !$('#divFamilySearchForm').is(':visible')) {
				var numEmpty = $appInits.find('.applicationInit').filter(function () {
				    return this.value.trim() === '';
				}).length;
				
				if (numEmpty === 0) {
// 					$('#appParent').val(null);

					if (multiAppForm()) {
						$('#formApplication').submit();
					} else {
						submitApplicationForm($('#contextPath').val() + '/mdm/createApp/details');
						freezeApplicationInits();
					}
				}
			}
		});
	}
	
	function enableAppIdFields() {
		return $('#screen').val() === 'INIT';
	}
	
	function freezeApplicationInits() {
		var $appInits = $('.appInits');
		$appInits.find('.applicationInit').not('#applicationNo').prop('readonly', true);
		$appInits.find('.ddApplicationType option:not(:selected)').attr('disabled', true);
	}

	/*----------------------------------------------------------------------------
									  Link Family
	------------------------------------------------------------------------------ */
	function bindFamilyLink() {
		$('.linkFamily').on('click', function() {
			if (appValidator.prototype.validApplicationInits()) {
				$('#divFamilySearchForm, #tblFamilyResults').hide();
				var trFamily = $(this).parents('tr');
				showLinkedFamily(trFamily);
				$('#appFamilyId').val(trFamily.find('.familyId').attr('data'));
				$('#appParent').val(parseInt(trFamily.find('.tdAppDbId').val()));
				$('#screen').val('INIT');
				freezeApplicationInits();
				submitApplicationForm($('#contextPath').val() + '/mdm/createApp/details');
			}
			
		});
		
		$('.updateFamily').on('click', function() {
			$('#popupEditFamily').modal('hide');
			var trFamily = $(this).parents('tr');
			showLinkedFamily(trFamily);
			$('#divLinkedFamily').show();
			$('#appFamilyId').val(trFamily.find('.tdFamilyId').html());
	});
		
		$('#showFamilyLinkage').on('click', function() {
			$('#tblFamilyResults, #divLinkedFamily').remove()
			$('#application-content_1, #btnControls').html('');
			$('#divFamilySearchForm').show();
		});
		
		$('.showAllFamilies').on('click', function() {
			$('.trFamilyResult').not(':visible').show();
			$(this).remove();
		});
	}
	
	function showLinkedFamily(trFamily) {
		var $linkedFamily = $('#divLinkedFamily');
		$linkedFamily.find('#_familyId').html(trFamily.find('.tdFamilyId').html());
		$linkedFamily.find('#_familyId').attr('data',trFamily.find('.tdFamilyId').html());
		$linkedFamily.find('#_jurisdiction').html(trFamily.find('.tdJurisdiction').html());
		$linkedFamily.find('#_applicationNo').html(trFamily.find('.tdApplicationNo').html());
		$linkedFamily.find('#_docketNo').html(trFamily.find('.tdDocketNo').html());
		$linkedFamily.hide();
	}
	
	function submitApplicationForm(serviceUrl) {
		var data = $('#formApplication').serialize();

		$.ajax({
			type : "POST",
			url : serviceUrl,
			data : data,
			success : function(result) {
				if (result === 'success') {
					window.location = $('#contextPath').val() + '/mdm/dashboard';
				} else {
					processAjaxResponse(result);
				}
			},

		});
	}
	
	function processAjaxResponse(result) {
		if ((result.trim().indexOf('<!DOCTYPE html>')) == 0) {
			var newDoc = document.open("text/html", "replace");
			newDoc.write(result);
			newDoc.close();
			
			bindInitPageControls();
			bindDetailsPageControls();
			autoCompleteFields();
		} else {
			var config = $($(result)[0]).attr('value');
			var screen = config.split('-')[0];
			var idxApp = config.split('-')[1];
			$('#screen').val(screen);
			$('.error').remove();
			
			switch (screen.toUpperCase()) {
			case 'INIT':
				$('#divLinkedFamily').remove();
				$('#application-init_1').html(result);
				bindInitPageControls();
				autoCompleteFields();
				break;
				
			case 'DETAILS':
				$('#application-content_1').html(result);
				if ($('#firstFiling').val() !== 'true') {
					$('#divLinkedFamily').show();
				}
				if(!editApplication) {
					var $btnControls = $('#tempBtnControls');
					$('#btnControls').html($btnControls.html());
					$('#btnControls').addClass($btnControls.attr('class'));
					$btnControls.remove();
				}
				$('form').attr('action', $('#contextPath').val() + '/mdm/createApp/save');
				bindDetailsPageControls();
				autoCompleteFields();
				break;
			}
		}
	}
	
	/*----------------------------------------------------------------------------
									Search Families
	------------------------------------------------------------------------------ */
	$(document).on('change', '#ddApplicationType', function() {
		var linkFamily = $(this).find(':selected').attr('linkFamily');
		if (linkFamily !== 'true') {
			$('#divFamilySearchForm').hide();
			$('#resultFamilySearch').html('');
		} else {
			$('#divFamilySearchForm').show();
		}
	});
	
	$(document).on('change', '#ddFamilySearch', function() {
		$('.grpSearchAttr').hide();
		$('.error').html('');
		$('#searchAttr_' + $(this).val()).show();
	});
	
	$(document).on('click', '#btnSearchFamilies', function() {
		if (validFamilySearchForm()) {
			fxSearchFamilies();
		}
	});
	
	function validFamilySearchForm() {
		var linkage = $('#ddFamilySearch').val();
		var valid = false;
		
		switch (linkage) {
		case 'APPLICATION_NUMBER':
			valid = validationUtil.checkRequiredFields(['familySearch_jurisdiction', 'familySearch_applicationNo'],
			                ['Please enter a valid jurisdiction', 'Please enter a valid application #']);
			break;
			
		case 'ATTORNEY_DOCKET_NUMBER':
			valid = validationUtil.checkRequiredFields(['familySearch_docketNo'], ['Please enter a valid attorney docket #']);
			break;
			
		case 'FAMILY_ID':
			valid = validationUtil.checkRequiredFields(['familySearch_familyId'], ['Please enter a valid family Id']);
			break;
		}
		
		return valid;
	}
	
	function fxSearchFamilies() {
		var data = $('#divFamilySearchForm').find('input, select').serialize();
		var target = $('#contextPath').val() + '/mdm/createApp/familySearch';
			
		$.ajax({
			type : "POST",
			url : target,
			data : data,
			success : function(result) {
				$('#resultFamilySearch').html(result).show();
				bindFamilyLink();
				viewFamily();
			},

		});
	}
	
	function bindDetailsPageControls() {
		$('.datepicker').datepicker({
        	format: "M dd, yyyy"
        });
		
		$('.icon-calendar').click(function(){
		    $(document).ready(function(){
		        $("#config-demo").daterangepicker({
		        	opens:'left'
		        }).focus();
		    });
		});
		
		$('#btnSaveChanges').unbind('click').on('click', function() {
			processFormSubmission();
		});
		
		$('#addNewApp').unbind('click').on('click', function() {
			addAditionalApplication();
		});
		
		bindApplicationDelete();
		if (!editApplication) {
			bindDraftSaving();
		} else {
		    var $prevAppType;

		    $(".updateApplicationType").on('focus', function () {
		        $prevAppType = $(this).find(':selected');
		    }).change(function() {
		        var $this = $(this).find(':selected');
		        
		        var prevFirstFiling = $prevAppType.attr('linkFamily') !== 'true';
		        var thisFirstFiling = $this.attr('linkFamily') !== 'true';
		        
		        if (prevFirstFiling != thisFirstFiling) {
		        	if (thisFirstFiling) {
		        		// Hide family linkage
		        		$('#divLinkedFamily').hide();
		        		$('#appOldFamily').val($('#appFamilyId').val());
		        		$('#appFamilyId').val('');
		        	} else {
		        		// Show family linkage
		        		$('#appFamilyId').val($('#appOldFamily').val());
		        		$('#divLinkedFamily').show();
						$('#familySearchForm').trigger('click');
						$('#divFamilySearchForm').show();
		        	}
		        }
		        
		        $prevAppType = $this;
		    });
		}
	}

	/*-----------------------------------------------------------------------
	-------------------------------------------------------------------------
	------------------------------------------------------------------------- */
	$(".toggle-link").unbind('click').on("click", function() {
		var $divApp = $(this).parents('.mdm-summary');
		$divApp.find(".create-app-data .hidden-data").slideUp();
		$divApp.find(".toggle-link").not(this).text("Show Details");
		$(this).text(function(i, text) {
	      	if (text === "Show Details") {
	      		$divApp.find(".create-app-data").find(".hidden-data").slideToggle();
	      		return "Hide Details";
	      	} else {
	      		return "Show Details";
	      	}
	    });
	});
	
	function multiAppForm() {
		return $('#multiApps').length === 1;
	}
	
	/* View Family on family id hyperlink click */
	function viewFamily() {
		$('.familyId').on('click', function() {
			var familyId = $(this).attr('data');
			var target = $('#contextPath').val() + "/mdm/viewFamily/" + familyId;

			$.get(target, function(data) {
				$('#viewFamilyBody').html(data);
			});
		});
	}

	
	function autoCompleteJurisdiction() {
	$(".jurisdiction").autocomplete({
			source : $('#jurisdictionList').val() === undefined ? [] : $('#jurisdictionList').val().split(";"),
		minLength : 1,
			
	});
	}

	function autoCompAssignee() {
		$(".assignee").autocomplete({
			source : $('#assigneeList').val().split(";"),
			minLength : 1,
		});
	}

	function autoCompleteEntity() {
		$(".entity").autocomplete({
			source : $('#entityList').val().split(";"),
			minLength : 1,
		});
	}
	
	function autoCompleteCustomer() {
		$(".customerNo").autocomplete({
			source : $('#customerList').val().split(";"),
			minLength : 1,
		});
	}

	function bindApplicationDelete() {
		$('.deleteApplication').click(function() {
			$(this).parents('.mdmContainer').find('.uiDeleted').val(true);
			$(this).parents('.mdmRecord').remove();
		});
	}
	
	
	/*- --------------------------------------------------------------
								:: DRAFTS ::
	------------------------------------------------------------------ */
	function bindDraftSaving() {
		var draftIn = parseInt($('#draftAfterMS').val());
		if (!isNaN(draftIn)) {
			setInterval(saveDraft, draftIn);
		}
	};

	function saveDraft() {
		console.log(new Date());
		$('#progressBar').attr('disableLoader', true);
		$.ajax({
			  type: "POST",
			  url: $('#contextPath').val() + '/mdm/createApp/draftAutoSave',
			  data : $('#formApplication').serialize(),
			  success: function(response) {
				  $('#progressBar').attr('disableLoader', false);
				  console.log('Draftted Succesfully!');
			  },
		});
	}
	
	function showCancelMessage() {
		$("#cancelPopup").show();
		$("#cancelPopup").wrap("<div class='overlay'>");
	}
	
	function hideCancelMessage() {
		$("#cancelPopup").hide();
		$("#cancelPopup").unwrap("<div class='overlay'>");
	}
	
	function closePage() {
		/* if ($("#editApplication").val() == 'true') {
			window.location.href='${pageContext.request.contextPath}/mdm/drafts';
		} else {
			window.location.href='${pageContext.request.contextPath}/mdm/createApp/new';
		} */
		window.history.back();
	}
	
	function autoCompleteFields() {
		var firstScreen = $('#screen').val() === 'INIT';
		if (firstScreen) {
			autoCompleteJurisdiction();
		} else {
			autoCompAssignee();
			autoCompleteEntity();
			autoCompleteCustomer();
		}
		if (editApplication) {
			autoCompAssignee();
			autoCompleteEntity();
			autoCompleteCustomer();
		}
	}
	
	/*- ----------------- Releasing application lock. -- */
	window.onunload = function(event) {
		releaseApplicationLock();
	};

	$(function() {
		$('a, .btn').click(function() {
			window.onbeforeunload = null;
		});
	});
	
	function releaseApplicationLock() {
		var target = $('#contextPath').val() + '/mdm/leaveAppLock';
		$.get(target, function(data) {});
	}
	
	function attorneyDocketNoFocusOut() {
		$('#attorneyDocketNo').on('focusout', function () {
			var attorneyDocketNo = this.val();
			$.ajax({
				  type: "POST",
				  url: $('#contextPath').val() + '/mdm/getAssignee',
				  data : attorneyDocketNo,
				  success: function(response) {
					  $('#progressBar').attr('disableLoader', false);
					  console.log('Draftted Succesfully!');
				  },
			});
		})
	}
	
</script>