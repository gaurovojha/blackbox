$(function() {
	$("#duplicateRoleContainer").hide();
	$("#selectRole").on("change", function() {
		if ($(this).val() == "duplicate role") {
			$("#duplicateRoleContainer").show();
		} else {
			$("#duplicateRoleContainer").hide();
		}
	});
});


$('#reminder').on('click', function() {
	if ($(this).is(':checked')) {
		$('#reminderContainer').show();
	} else {
		$('#reminderContainer').hide();
	}
});

$('#escalation').on('click', function() {
	if ($(this).is(':checked')) {
		$('.escalationContainer').show();
	} else {
		$('.escalationContainer').hide();
	}
});

$( document ).ready(function() {
	
	if ($('#reminder').is(':checked')) {
		$('#reminderContainer').show();
	} else {
		$('#reminderContainer').hide();
	}
	
	if ($('#escalation').is(':checked')) {
		$('.escalationContainer').show();
	} else {
		$('.escalationContainer').hide();
	}
	
	});


$('select#escalationRoles').on('change', function(i) {
	$(this).find('option').each(function(i) {
		var $rowid = '#escalationableRow' + this.id;
		if ($(this).is(':selected')) {
			$($rowid).show();
		} else {
			$($rowid).hide();
		}
	})
});

function setFormValues() {
	var escalation = $('.inputEscalation');
	var value = '';
	$.each(escalation, function(i) {
		value += $(escalation[i]).attr('id') + ',';
	})
	$('#escalationSelectedRoles').val(value);
}

function onEscalationDelete() {
	$('.escalationDelete').on('click', function() {
		$(this).parents('.input-group').remove();

	})
}

function populateRoles() {

	var inputs = $('.escalationContainer').find('input');
	$.each(inputs, function(i) {
		var id = inputs[i].id;

		var selctedOptions = $('select#escalationRoles option');

		selctedOptions.each(function(optionIndex) {

			var option = $(selctedOptions[optionIndex]);

			var optionRoleId = option.attr('value').trim();

			if (inputs[i].id === optionRoleId) {

				option.hide();
			}

		});

	})
	$('#addRolesForSetUp').modal('show');

}

function addingRolesToEscalationRoles() {

	var selctedOptions = $('select#escalationRoles').find('option:selected');

	$.each(
					selctedOptions,
					function(i) {

						var value = selctedOptions[i].value;
						var arr = value.split('#');

						var content = '<div class="input-group role-input-block">'
								+ '<input type="text" id='
								+ value
								+ '  value='
								+ arr[1]
								+ ' style="width:150px;height:25px;" class="form-control inputEscalation"></input>'
								+ '<span class="input-group-btn"><i class="icon icon-delete escalationDelete"></i></span>'
								+ '</div>';

						$('.role-details').append(content);
					})

	$('#addRolesForSetUp').modal('hide');
}

function createFocusOnInputBoxes() {
	$('.icon-edit').on('click', function() {
		$(this).parents('.col-sm-6').find('.editSmallTextBox').focus()
	});

	$('#noOfReminders').on('change');
}

function validateNotificationSetUpForm() {

	if ($('#escalation').is(':checked')) {

		if ($('.escalationContainer').find('.inputEscalation').size() === 0) {

			$('#errorMsg').find('.content').html(
					'<p>please add atleast one escalation role.</p>');

			errorMsg();

			return false;
		} else if ($('#noOfPastDueDays').val() == 0
				|| $('#noOfPastDueDays').val() == "") {

			$('#errorMsg').find('.content').html(
					'<p>please enter more than 0 days.</p>');

			errorMsg();

			return false;
		} else {
			return true;
		}
	}
	return true;

}
createFocusOnInputBoxes();
onEscalationDelete();

function closePage() {

	window.location.href = '../notification/dashBoard';

}

$(document).on("keyup", "#frequencyOfSendingRemindes,#noOfPastDueDays ", function (event) {
	   
	   if(event.target.value<0 || isNaN(event.target.value) || event.target.value===""){
	
	event.target.value=0;
	}
	});

function showMessage() {
	$("#popupMsg").show();
	$("#popupMsg").wrap("<div class='overlay'>");
}

function hideMessage() {
	$("#popupMsg").hide();
	$("#popupMsg").unwrap("<div class='overlay'>");
}

function simpleMsg() {
	$("#simpleMsg").removeClass("hide");
	$("#simpleMsg").show();
	$("#simpleMsg").wrap("<div class='overlay'>");
}
function infoMsg() {
	$("#infoMsg").removeClass("hide");
	$("#infoMsg").show();
	$("#infoMsg").wrap("<div class='overlay'>");
}
function errorMsg() {
	$("#errorMsg").removeClass("hide");
	$("#errorMsg").show();
	$("#errorMsg").wrap("<div class='overlay'>");
}
$(document).on("click", ".popup-msg a.close", function() {
	$(this).parents(".popup-msg").addClass("hide");
	$(this).parents(".popup-msg").unwrap("<div class='overlay'>");
});
