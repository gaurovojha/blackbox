$(document).on('click', '.linkMoreRoles', function() {
	var $parent = $(this).parents('td');
	$parent.find('.linkMoreRoles').hide();
	$parent.find('.spanMoreRoles').show();
});

function createAccessProfile() {
	window.location.href = "../accessprofile/create";
}

function createDuplicateAccessProfile(accessProfileId) {
	window.location.href = "../accessprofile/createDuplicate?id=" + accessProfileId;
}

function popupMsgForCancelSaveProfile(current){
	
	$("#popupMsgCancelSaveProfile").removeClass("hide");
	$("#popupMsgCancelSaveProfile").show();
	$("#popupMsgCancelSaveProfile").wrap("<div class='overlay'>");
	var $href = $(current).attr("data-href");
	$("#popupMsgCancelSaveProfile .btn-submit").attr("href",$href);
}

function hideProfileCancelSavePopUp(){
	$("#popupMsgCancelSaveProfile").addClass("hide");
	$("#popupMsgCancelSaveProfile").addClass("hide");
	$("#popupMsgCancelSaveProfile").unwrap("<div class='overlay'>");
}

$(document).ready(function() {
	
	$("#popupMsgCancelSaveProfile a").click(function() {
		hideProfileCancelSavePopUp()
	});
	
	$("#accessProfileInfo").hover(function() {
		$( this ).append( $( "<span>${accessProfile.description}</span>" ) );
	  }, function() {
	    $( this ).find("span:last").remove();
	  }
	);
	
	$('.multiselect').multiselect();
	
	$('#modalCancelSaveProfile').on('show.bs.modal', function(e) {
	    $(this).find('.btn-ok').attr('onclick', $(e.relatedTarget).data('href'));
	});
	
	var accessProfileTable = $('#accessProfileTable').DataTable({
		"order": [[6,'desc'],[7,'desc']],
		"aLengthMenu": [[10, 20, 30, 40, -1], [10, 20, 30, 40, "All"]],
		"aoColumnDefs": [
          { 'bSortable': true,"bVisible": false, 'aTargets': [ 6 ] },
          { 'bSortable': true,"bVisible": false, 'aTargets': [ 7 ] }
       ],
       "iDisplayLength":20
	});
	
	$('#searchText').keyup(function() {
		//accessProfileTable.search($(this).val()).draw() ;
		accessProfileTable.fnFilterAll(this.value);
	});
	
	$('#selectProfile').change(function() {
		
		var profileId = jQuery("#selectProfile option:selected").attr('value');
		
		$.ajax({
			  type: "GET",
			  url: "getAccessProfile?id="+profileId,
			  contentType: "application/json",
			  success: function(response) {
				  console.log(response);
					jQuery("#accessProfileNew").html(response);
			  },	  
		});
	});
	
	$('#btnAccessProfile').click(function(){
		
		 var selected  = $('input[name=radioAccessProfile]:checked').attr('id');

		if (selected === '' || selected === 'select') {
			alert('Please select one of the Options');
		} else {
			if (selected === 'newAccessProfile') {
				createAccessProfile();
			} else {
				var accessProfileId = jQuery('input[name=actionAccessProfile]:checked').attr('value');
				if(accessProfileId === undefined || accessProfileId === null) {
					alert('Please select one of the Access Profile Options');
				}
				else {
					createDuplicateAccessProfile(accessProfileId);
				}
			}
		}
	});
		
});