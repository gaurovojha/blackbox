$(document).ready(function() {

	/*var profileId = jQuery("#selectProfile option:selected").attr('value');

	if(profileId != null &&  profileId != undefined && profileId != '') {
		
		jQuery.ajax({
			type : "GET",
			url : "accessProfile?id=" + profileId,
			contentType : "text/html",
			success : function(response) {
				console.log(response);
				jQuery("#accessProfileView").html(response);
			}
		});
	}*/
	
	
	$('.multiselect').multiselect();
	
	function createNewRole() {
		window.location.href = "../role/create";
	}

	function createDuplicateRole(roleId) {
		window.location.href = "../role/createDuplicate?id=" + roleId;
	}

	// Not usable as of now
	$("#roleInfo").hover(function() {
		$( this ).append( $( "<span>${role.description}</span>" ) );
	  }, function() {
	    $( this ).find("span:last").remove();
	  }
	);
	
	var roleTable = $('#rolesTable').dataTable({
		"order": [[7,'desc'],[8,'desc']],
		"aLengthMenu": [[10, 20, 30, 40, -1], [10, 20, 30, 40, "All"]],
		"aoColumnDefs": [
          { 'bSortable': true,"bVisible": false, 'aTargets': [ 7 ] },
          { 'bSortable': true,"bVisible": false, 'aTargets': [ 8 ] }
       ],
       "iDisplayLength":20
	});

	$('#searchText').keyup(function() {
		roleTable.fnFilterAll(this.value);
		//roleTable.search($(this).val()).draw() ;
	});
	
	$('#modalCancelSaveRole').on('show.bs.modal', function(e) {
	    $(this).find('.btn-ok').attr('onclick', $(e.relatedTarget).data('href'));
	});
	
	$('#btnRole').click(function(){
		
		 var selected  = $('input[name=radioRole]:checked').attr('id');

		if (selected === '' || selected === 'select') {
			alert('Please select one of the options');
		} else {
			if (selected === 'newRole') {
				createNewRole();
			} else {
				var roleId = jQuery('input[name=actionRole]:checked').attr('value');
				if(roleId === undefined || roleId == null) {
					alert('Please select one of the Role options');
				}
				else {
					createDuplicateRole(roleId);
				}
			}
		}
	});
	
	$("#popupMsgCancelSaveRole a").click(function() {
		hideRoleCancelSavePopUp()
	});
	
});	

function validateRoleForm(form) {

	var jurisdictions = form.jurisdictions.value;
	var assignees = form.assignees.value;
	var customers = form.customers.value;
	var technologyGroups = form.technologyGroups.value;
	var organizations = form.organizations.value;
	
	return true;
}

function popupMsgForCancelSaveRole(current){
	
	$("#popupMsgCancelSaveRole").removeClass("hide");
	$("#popupMsgCancelSaveRole").show();
	$("#popupMsgCancelSaveRole").wrap("<div class='overlay'>");
	var $href = $(current).attr("data-href");
	$("#popupMsgCancelSaveRole .btn-submit").attr("href",$href);
}

function hideRoleCancelSavePopUp(){
	$("#popupMsgCancelSaveRole").addClass("hide");
	$("#popupMsgCancelSaveRole").addClass("hide");
	$("#popupMsgCancelSaveRole").unwrap("<div class='overlay'>");
}


function loadAccessProfile() {

	var profileId = jQuery("#selectProfile option:selected").attr('value');

	jQuery.ajax({
		type : "GET",
		url : "accessProfile?id=" + profileId,
		contentType : "text/html",
		success : function(response) {
			console.log(response);
			jQuery("#accessProfileView").html(response);
		}
	});
}
