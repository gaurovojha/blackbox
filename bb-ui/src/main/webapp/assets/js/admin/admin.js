	function showInactiveRoleInfo(user, date) {
		
		$('#inactiveRoleUser').text("");
		$('#inactiveDate').text("");
		
		$("#inactiveRoleInfo").show();
		
		$('#inactiveRoleUser').text(user);
		$('#inactiveDate').text(date);
		$("#inactiveRoleInfo").removeClass("hide");
		$("#inactiveRoleInfo").wrap("<div class='overlay'>");
	}
	
	function hideInactiveRoleInfo(){
		$("#inactiveRoleInfo").addClass("hide");
		$("#inactiveRoleInfo").unwrap("<div class='overlay'>");
	}

	function showUserPopupForRole(roleId) {

		$('#modalRoles').html("");
		$('#modalRoles').off('show.bs.modal').on('show.bs.modal', function() {
			
			var target = "userDetails?roleId=" + roleId;

			$.get(target, function(data) {
				$('#modalRoles').html(data);
			});
		});
	}

	function showUserPopupForAccessProfile(accessProfileId) {

		$('#modalAccessProfiles').html("");
		$('#modalAccessProfiles').off('show.bs.modal').on('show.bs.modal', function() {
			
			var target = "userDetailsByProfile?accessProfileId=" + accessProfileId;

			$.get(target, function(data) {
				$('#modalAccessProfiles').html(data);
			});
		});
	}
	
	function popupMsgForRole(current){
	
		$("#popupMsgRole").removeClass("hide");
		$("#popupMsgRole").show();
		$("#popupMsgRole").wrap("<div class='overlay'>");
		var $href = $(current).attr("data-href");
		$("#popupMsgRole .btn-submit").attr("href",$href);
	}
	
	function popupMsgForProfile(current){
		
		$("#popupMsgProfile").removeClass("hide");
		$("#popupMsgProfile").show();
		$("#popupMsgProfile").wrap("<div class='overlay'>");
		var $href = $(current).attr("data-href");
		$("#popupMsgProfile .btn-submit").attr("href",$href);
	}

	function hideProfilePopUp(){
		$("#popupMsgProfile").addClass("hide");
		$("#popupMsgProfile").unwrap("<div class='overlay'>");
	}
	
	function hideRolePopUp(){
		$("#popupMsgRole").addClass("hide");
		$("#popupMsgRole").unwrap("<div class='overlay'>");
	}

	
	// This must be a hyperlink
	$(".export").on('click',function(event) {
		// CSV
		var hasClass = $('#roleTab').hasClass('active');
		if(hasClass) {
			exportTableToCSV.apply(this, ['rolesTable', 'RoleDetails.xls']);
		}
		
		hasClass = false;
		hasClass = $('#accessProfileTab').hasClass('active');
		if(hasClass) {
			exportTableToCSV.apply(this, ['accessProfileTable', 'AccessProfileDetails.xls']);
		}
		
		hasClass = false;
		hasClass = $('#userTab').hasClass('active');
		if(hasClass) {
			exportTableToCSVForUser.apply(this, [$('#usersTable'), 'UserDetails.xls']);
		}

		// IF CSV, don't do event.preventDefault() or return false
		// We actually need this to be a typical hyperlink
	});
	
	function exportTableToCSV(tab, filename) {

		var tab_text = "<table border='2px'>";
		var textRange;
		var j = 0;
		tab = document.getElementById(tab); // id of table

		for (j = 0; j < tab.rows.length; j++) {

			//only formatting of upper row
			if (j == 0) {
				tab_text = tab_text + "<tr bgcolor='#87AFC6'>";
			} else {
				tab_text = tab_text + "<tr>";
			}

			for (k = 0; k < tab.rows[j].cells.length-1; k++) {

				tab_text = tab_text + "<td>"
						+ tab.rows[j].cells[k].innerHTML
						+ "</td>";

			}
			tab_text = tab_text + "</tr>";

		}

		tab_text = tab_text + "</table>";
		tab_text = tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
		tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove if u want images in your table
		tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi,""); // reomves input params

		
		//Save file for IE 10+
		 if (window.navigator.msSaveOrOpenBlob) {
		     csvData = decodeURIComponent(tab_text);

		     if(window.navigator.msSaveBlob){
		         var blob = new Blob([csvData],{ type: "application/csv;charset=utf-8;", target: "_blank"});
		         navigator.msSaveBlob(blob, filename);
		     }
		 }
		//Other browsers
		 else
		 {
			// Data URI
			 csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(tab_text);
		     $(this).attr({
		         "href": csvData,
		         "target": "_blank",
		         "download": filename
		     });
		 }
	}
	
$(document).ready(function() {

	
	$('#modalConfirmDeleteProfile').on('show.bs.modal', function(e) {
	    $(this).find('.btn-ok').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#modalConfirmDeleteRole').on('show.bs.modal', function(e) {
	    $(this).find('.btn-ok').attr('href', $(e.relatedTarget).data('href'));
	});
	
	// making common search field for data tables
	jQuery.fn.dataTableExt.oApi.fnFilterAll = function(oSettings, sInput, iColumn, bRegex, bSmart) {
	    var settings = $.fn.dataTableSettings;
	    for ( var i=0 ; i<settings.length ; i++ ) {
	      settings[i].oInstance.fnFilter( sInput, iColumn, bRegex, bSmart);
	    }
	};

});	