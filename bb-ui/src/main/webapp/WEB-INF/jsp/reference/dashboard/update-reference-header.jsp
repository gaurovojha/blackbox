<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<div class="popup-msg alert" id="updateRefPopup">
	<div class="text-right"><a class="close" href="#">&times;</a></div>
	<div role="alert" class="content">
		<p>Are you sure want to continue ?</p>
		<button class="btn btn-submit" onclick="actionUpdateRefDelYes();">Yes</button>	
		<button class="btn btn-cancel">No</button>
	</div>
</div>

<div class="form-horizontal row">
	<div class="tab-info-text col-sm-7">
		<span><i><img src="<%=images%>/svg/info.svg" class="icon16x"></i></span> <p>${notificationMessage}</p> 
	</div>
	<div class="list-doc-control  col-sm-5">
		<span class="title">Jurisdiction 
			<div class="form-inline inline-block">
				<select class="form-control" id="jurisdictionFilter">
                    <option selected value="all">All</option>
                    <option value="US">US</option>
                    <option value="Non-US">Non-US</option>
                </select>	
			</div>
		</span> 
		<input type="checkbox" id="showMyRecordsFilter"><label class="control-label" for="uploadedDocs">Show My Notifications Only</label>
	</div>
</div>  	


<input type="hidden" id="dashbordSubMenu" value="${dashbordSubMenu}" />

<table id="updateReferenceTable" class="table custom-table updateReferenceTable">
    <thead>
    	<tr class="small-height-row">
			<th></th>
			<th></th>
			<th colspan="3" class="text-center noSort">Source Application</th>
			<th></th>
			<th></th>
		</tr>
        <tr>
            <th>Reference Jurisdiction</th>
            <th>Reference Publication #</th>
            <th class="noSort bdr-rt-none">Jurisdiction</th>
            <th class="noSort bdr-rt-none">Application #</th>
            <th class="noSort">Source Doc.</th>
            <th>Notified</th>
            <th class="text-center noSort">Actions</th>
        </tr>
    </thead>
    <tbody id="updateRefTableBody">
		<jsp:text/>
	</tbody>
 </table>
 
 <div id="pageInfo"><jsp:text/></div>
 
 <script type="text/javascript">
 	
 	var formIndex;
 
	function openUpdateRefPopup(index) {
		formIndex = index;
		
		$("#updateRefPopup").removeClass("hide");
		$("#updateRefPopup").show();
		$("#updateRefPopup").wrap("<div class='overlay'>");
	}
	
	function actionUpdateRefDelYes() {
		$("#updateRefPopup").addClass("hide");
		$("#updateRefPopup").unwrap("<div class='overlay'>");
		$("#updateRefDeleteForm" + formIndex).submit();
		//deleteUpdateRefAction('/reference/dashboard/deleteUpdateReference', referenceStagingId, notificationProcessId);
	}
	
	function deleteUpdateRefAction(url, referenceStagingId, notificationProcessId) {
		$.ajax({
			url: $('#contextPath').val() + url,
			"type" : "POST",
			'data':  {'referenceStagingId': referenceStagingId, 'notificationProcessId': notificationProcessId},
			//contentType: "application/json",
			success: function(response) {
				if (response == 'true') {
					alert(recordIndex);
					console.log($('#updateRefTableBody').find('tr'));
					console.log($('#updateRefTableBody').eq(recordIndex));
					$('#updateRefTableBody').find('tr').eq(recordIndex).hide();
				}
			}	
		});
	}
	
	$(document).on("click", ".popup-msg a.close, .popup-msg .btn-cancel", function() {
		$(this).parents(".popup-msg").addClass("hide");
		$(this).parents(".popup-msg").unwrap("<div class='overlay'>");
	});
</script>
