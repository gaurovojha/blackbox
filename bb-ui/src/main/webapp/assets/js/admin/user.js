$.ajaxSetup({
    cache: true
});

$(document).on('click', '#createUser', function() {
	try {
		$.blackbox.util.CommonUtil.loadResourceBundle(encodeURIComponent('messages'));
	} catch(error) { console.log(error); }
	$('#progressBar').hide();
	var status = $.blackbox.validator.UserValidator.validate();
	if (status) {
		$('#createUser').addClass('disabled');
		$('#userForm').submit();
	} else {
		return false;
	}
});

$(document).on('click', '.linkMoreRoles', function() {
	var $parent = $(this).parents('td');
	$parent.find('.linkMoreRoles').hide();
	$parent.find('.divMoreRoles').show();
//	$(this).parents('td').find('.divMoreRoles, .linkMoreRoles').toggle();
});

/*- ------------------------- User Access Controls --*/
$(document).on('click', '.enableAccess, .unlockAccess', function() {
	var $this = $(this);
	var target = $this.hasClass('enableAccess') ? "../user/enable/" : "../user/unlock/";
	$.ajax({
		type: "GET",
		url: target + $(this).parents('tr').attr('data'),
		contentType: "application/json",
		cache: false,
		success: function(response) {
			$this.parents('td').html(response);
		}
	});
});

function loadUsersTable() {
	$.ajax({
		type: "GET",
		url: "usersTable",
		contentType: "application/json",
		success: function(response) {
			$("#userTab").html(response);
			prepareDataTable();
		}	
	});
}
	
	
function prepareDataTable() {
	var userTable = $('#usersTable').DataTable({
		"fnDrawCallback":function(){
			selectAll();
			disableButton();
			disableRows();
			editUser();
			generateOtp();
			//disableUserAccess();
			//dropUserAccess();
			disableUserIcon();
			dropUserIcon();
		},
		 "order": [[8,'desc'],[9,'desc']],
		"aLengthMenu": [[10,20,30, 40, -1], [10,20,30, 40, "All"]],
		"aoColumnDefs": [
          { 'bSortable': false, 'aTargets': [ 0 ] },
          { 'bSortable': true,"bVisible": false, 'aTargets': [ 8 ] },
          { 'bSortable': true,"bVisible": false, 'aTargets': [ 9 ] }
       ],
       "iDisplayLength":20
	});
	$('.dataTables_filter').hide();
	$('#selectAllUsers').parents('th').removeClass('sorting_asc');
	$('#searchText').keyup(function() {
		//userTable.search($(this).val()).draw();
		userTable.fnFilterAll(this.value);
  });
}
	
function createUser()
{
	$('#create_user').click(function(){
		window.location.href='../user/newuser';
	});
}

function editUser()
{
	$('#edit_user').click(function(){
		var allVals = [];
		$('#usersTable :checked').each(function() {		
			  allVals.push($(this).val());
			});	
		if(allVals.length==1)
		{		
			window.location.href='../user/edit/'+allVals[0];
		}else
		{
			//alert("Please Select only One item");
		}	
	});
}
	 
function generateOtp() {
	$('#generate_otp').click(function(){
		if(!$("#generate_otp").hasClass('disabled'))
		{
			var allVals = [];
			$("#loadingModal").modal("show");
			$('#usersTable :checked').each(function() {		
				allVals.push($(this).val());
				});	
			if(allVals.length==1)
			{
			//window.location.href='generateotp/user/'+allVals[0];		
				$.ajax({
					type: "GET",
					url: '../user/generateotp',
					data: {userId:allVals[0]},
					success: function(result) {
						$("#loadingModal").modal("hide");
						var otpResult = jQuery.parseJSON(result);
						$('#generateOtpPopup').find('.msg ').text('Generated OTP :' + otpResult.code);
						showMessage('#generateOtpPopup');
					}			  
					});
		}else
		{
			//alert("Please Select only One item");
		}
	  }
	});
			
}		
		
/* Utility functions */		
Array.prototype.diff = function(a) {
    return this.filter(function(i) {return a.indexOf(i) < 0;});
};

function selectAll()
{
	var disableBtn = false
	var dropBtn =  false;
	$('#selectAllUsers').click(function(event){		
		if(this.checked)
		{
			$('.selectUser').each(function(){
				var otpcheck = $(this).attr("data-otp");
				var enabled = $(this).parents('tr').find('.isEnabled').val();
				var active = $(this).parents('tr').find('.status').val();
				var locked = $(this).parents('tr').find('.isLocked').val();
				if(active=='true')
				{
					this.checked=true;
					if(enabled=='false')
					{
						disableBtn = true;
					}
					$('#drop_access').removeClass('disabled');
				}else
				{
					$(this).attr("disabled",true);
					//$('#myRow').find('input, a').prop('disabled', true);
					this.checked=false;
					dropBtn=true;
				}				
			});
			(disableBtn) ? $('#disable_access').addClass('disabled')
						: $('#disable_access').removeClass('disabled');
			(dropBtn) ? $('#drop_access').addClass('disabled')
						:$('#drop_access').removeClass('disabled');
		}
		else
		{
			$('.selectUser').each(function(){
				this.checked=false;
			});
			$('#edit_user').addClass('disabled');
			$('#generate_otp').addClass('disabled');
			$('#disable_access').addClass('disabled');
			$('#drop_access').addClass('disabled');
		}
	});
}

function disableButton(){
	$('.selectUser').click(function(){
			var checkedCount = $(".selectUser:checked").length;
			if(checkedCount==0)
			{
				$('#edit_user').addClass('disabled');
				$('#generate_otp').addClass('disabled');
				$('#disable_access').addClass('disabled');
				$('#drop_access').addClass('disabled');
				$("#selectAllUsers").removeAttr('checked');
			}else{
				$(".selectUser:checked").each(function(){
					var otpcheck = $(".selectUser:checked").attr("data-otp");
					var enabled = $(this).parents('tr').find('.isEnabled').val();
					var active = $(this).parents('tr').find('.status').val();
					var locked = $(this).parents('tr').find('.isLocked').val();
					if(checkedCount==1)
					{
						if(active =="true")
						{
							$('#edit_user').removeClass('disabled');
						}
						if(otpcheck=="true" && enabled=="true" && active =="true" && locked =="false") 
						{
							$('#generate_otp').removeClass('disabled');
						}		
						if(enabled=="true" && active=="true")
						{
							$('#disable_access').removeClass('disabled');
						}			
						if(active =="true")
						{
							$('#drop_access').removeClass('disabled');
						}			
					}
					else if(checkedCount>1)
					{
						$('#edit_user').addClass('disabled');
						$('#generate_otp').addClass('disabled');
						$('#disable_access').removeClass('disabled');
						$('#drop_access').removeClass('disabled');
						if(active=="false" || enabled=="false")
						{
							$('#disable_access').addClass('disabled');
						}			
						if(active =="false")
						{
							$('#drop_access').addClass('disabled');
						}	
					}
				});
			}		
	});
}

function disableRows()
{
	$('.selectUser').each(function(){
		var otpcheck = $(this).attr("data-otp");
		var enabled = $(this).attr("data-isEnabled");
		var active = $(this).attr("data-status");
		var locked = $(this).attr("data-isLocked");
		if(active=='false')
		{
			$(this).attr("disabled",true);
		}	
	});
}

function exportTableToCSVForUser($table, filename)
{   
	var tab_text="<table border='2px'>";
	var textRange; var j=0;
	tab = document.getElementById('usersTable'); // id of table
	    
	
		for (j = 0; j < tab.rows.length; j++) {
			// only formatting of upper row
			if (j == 0) {
				tab_text = tab_text + "<tr bgcolor='#87AFC6'>";
			} else {
				tab_text = tab_text + "<tr>";
			}
	
			for (k = 1; k < tab.rows[j].cells.length; k++) {
	
				tab_text = tab_text + "<td>" + tab.rows[j].cells[k].innerHTML
						+ "</td>";
	
			}
			tab_text = tab_text + "</tr>";
	
		}
	
	    
	tab_text=tab_text+"</table>";
	tab_text= tab_text.replace(/<A[^>]*>|<\/A>/g, "");// remove if u want links in
														// your table
	tab_text= tab_text.replace(/<img[^>]*>/gi,""); // remove if u want images in
													// your table
	tab_text= tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params
	
	  // Data URI
	 //csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(tab_text);
	 
	//Save file for IE 10+
	 if (window.navigator.msSaveOrOpenBlob) {
	     csvData = decodeURIComponent(tab_text);

	     if(window.navigator.msSaveBlob){
	         var blob = new Blob([csvData],{ type: "application/csv;charset=utf-8;"});
	         navigator.msSaveBlob(blob, filename);
	     }
	 }
	//Other browsers
	 else
	 {
		 csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(tab_text);
	     $(this).attr({
	         "href": csvData,
	         "target": "_blank",
	         "download": filename
	     });
	 }
}

//function exportToCSV()
//{
//	$(".export").on('click', function (event) {
//        // CSV
//		exportTableToCSV.apply(this, [$('#usersTable'), 'UserDetails.xlsx']);
//         
//        // IF CSV, don't do event.preventDefault() or return false
//        // We actually need this to be a typical hyperlink
//    });
//}


function dropUserIcon()
{
	$(".dropUserIcon").on('click',function(){
		if(!$(".dropUserIcon").hasClass('disabled'))
		{
			allVals = [];
		//userIds="";
			requestUrl ="";
			var id = $(this).attr("data-id");
			if($('#usersTable :checked').length>0)
			{
				$('#usersTable :checked').each(function() {		
				  allVals.push($(this).val());
				  //userIds+=($($(this)).parents('tr').find('.userEmail').text())+"<br>";
				});
			 requestUrl = "../user/drop";
			}
			else if(id!="")
			{
				allVals.push(id);
				//userIds+=$(this).attr("data-email");
				requestUrl = "../../user/drop";
			}
			$('#dropUserPopup').find('.msg ').text('Do you want to drop the user(s) ?');
			showMessage('#dropUserPopup');
		}
	});
}


function dropUser()
{
	if(allVals.length>0)
	{
		$.ajax({
			  type: "POST",
			  url: requestUrl,
			  contentType: "application/json",
			  data: JSON.stringify(allVals),
			  success: function(result) {
				  var failedIds = jQuery.parseJSON(result);
				  var passedIds = allVals.diff(failedIds);
				  /*$.each(passedIds,function(index,value){
					  //location.reload();
					  //$("#drop_access_msg").html("<div class= 'container'><label>Dropped Users :</label> </br>" + userIds+"</div>");
			  			$("#dropUserModal").modal("show");
						  });*/
				  ctxPath = $('#contextPath').val();
					window.location.href=ctxPath+'/admin/';
			  },
			  
			});
	}
}
function disableUserIcon()
{
	$(".disableUserIcon").on('click',function(){
		if(!$(".disableUserIcon").hasClass('disabled'))
		{
			allUserVals = [];
		// allUserIds="";
			$('.selectUser:checked').each(function() {		
			  allUserVals.push($(this).val());
			 // allUserIds+=($($(this)).parents('tr').find('.userEmail').text())+"<br>";
			});	
			$('#disableUserPopup').find('.msg ').text('Do you want to disable the user ?');
		   
		   showMessage('#disableUserPopup');
		}
	});
	
}


function disableUser()
{
	if(allUserVals.length>0)
	{
		$.ajax({
			  type: "POST",
			  url: "../user/disableaccess",
			  contentType: "application/json",
			  data: JSON.stringify(allUserVals),
			  success: function(result) {
				  /*var failedIds = jQuery.parseJSON(result);
				  var passedIds = allVals.diff(failedIds);
				  $.each(passedIds,function(index,value)
						  {
						  //	$("#disable_access_msg").html("Disabled Users : <div></div>" + allUserIds);
				  			//$("#disableUserModal").modal("show");							  
						  });*/
				  
				  ctxPath = $('#contextPath').val();
					window.location.href=ctxPath+'/admin/';
			  },
			  
			});
	}
	
}


function showMessage(id){
	$(id).show();
	//$(id).removeClass('hide');
	$(id).wrap("<div class='overlay'>");
}
function hideDropMessage(){
	$('#dropUserPopup').hide();
	$('#dropUserPopup').unwrap("<div class='overlay'>");
}
function hideDisableMessage()
{
	$('#disableUserPopup').hide();
	$('#disableUserPopup').unwrap("<div class='overlay'>");
};
function hideOtpMessage()
{
	$('#generateOtpPopup').hide();
	$('#generateOtpPopup').unwrap("<div class='overlay'>");
};

	
	/*$(document).on("click", ".popup-msg a.close, .popup-msg button.btn-cancel", function(){ 
		$(this).parents(".popup-msg").addClass("hide"); 
		$(this).parents(".popup-msg").unwrap("<div class='overlay'>"); 
		}); */